package com.amcglynn.myenergi;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;

import com.amcglynn.myenergi.apiresponse.ZappiDayHistory;
import com.amcglynn.myenergi.apiresponse.ZappiHourlyDayHistory;
import com.amcglynn.myenergi.apiresponse.ZappiStatusResponse;
import com.amcglynn.myenergi.exception.ClientException;
import com.amcglynn.myenergi.exception.InvalidResponseFormatException;
import com.amcglynn.myenergi.exception.ServerCommunicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class MyEnergiClient {

    private static final URI DIRECTOR_BASE_URL = URI.create("https://director.myenergi.net");
    private static final String ASN_HEADER = "x_myenergi-asn";
    private final CredentialsProvider credsProvider;
    private HttpHost targetHost;
    private HttpClient httpClient;
    private HttpClientContext httpClientContext;
    private final String serialNumber;

    public MyEnergiClient(String serialNumber, String apiKey) {
        this.serialNumber = serialNumber;
        targetHost = new HttpHost(DIRECTOR_BASE_URL.getHost(), DIRECTOR_BASE_URL.getPort(), "https");

        credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(serialNumber, apiKey));
    }

    protected MyEnergiClient(String serialNumber, String apiKey, HttpClient httpClient, HttpClientContext context) {
        this(serialNumber, apiKey);
        this.httpClient = httpClient;
        this.httpClientContext = context;
    }

    /**
     * Set the charge mode of Zappi. Note that this API does not take effect immediately and can take a few seconds to
     * complete, presumably because the server communicates with the Zappi asynchronously to change the mode.
     * @param zappiChargeMode the mode being switched to
     */
    public void setZappiChargeMode(ZappiChargeMode zappiChargeMode) {
        getRequest("/cgi-zappi-mode-Z" + serialNumber + "-" + zappiChargeMode.getApiValue() + "-0-0-0000");
    }

    public ZappiStatusResponse getZappiStatus() {
        var response = getRequest("/cgi-jstatus-Z" + serialNumber);
        try {
            return new ObjectMapper().readValue(response, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new InvalidResponseFormatException();
        }
    }

    public ZappiHourlyDayHistory getZappiHourlyHistory(LocalDate localDate) {
        var response = getRequest("/cgi-jdayhour-Z" + serialNumber + "-" + localDate.getYear() +
                "-" + localDate.getMonthValue()  + "-" + localDate.getDayOfMonth());
        try {
            return new ObjectMapper().readValue(response, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new InvalidResponseFormatException();
        }
    }

    public ZappiDayHistory getZappiHistory(LocalDate localDate) {
        var response = getRequest("/cgi-jday-Z" + serialNumber + "-" + localDate.getYear() +
                "-" + localDate.getMonthValue()  + "-" + localDate.getDayOfMonth());
        try {
            return new ObjectMapper().readValue(response, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new InvalidResponseFormatException();
        }
    }

    private String getRequest(String endPointUrl) {
        try {
            configureDigestAuthentication();

            var httpget = new HttpGet(endPointUrl);
            var response = httpClient
                    .execute(targetHost, httpget, httpClientContext);
            handleServerRedirect(response);
            handleErrorResponse(response);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new ServerCommunicationException();
        } catch (MalformedChallengeException e) {
            throw new InvalidResponseFormatException();
        }
    }

    private void handleErrorResponse(HttpResponse response) {
        if (response.getStatusLine().getStatusCode() >= 300) {
            throw new ClientException(response.getStatusLine().getStatusCode());
        }
    }

    /**
     * If the client is communicating to the wrong server for the requested serial number, the server will return
     * the desired server through the x_myenergi-asn header. The client has to honour this and redirect all requests
     * to this server.
     * @param httpResponse response from the initial request
     */
    private void handleServerRedirect(final HttpResponse httpResponse) {
        if (httpResponse.containsHeader(ASN_HEADER)) {
            var assignedServer = httpResponse.getFirstHeader(ASN_HEADER).getElements()[0].getName();
            targetHost = new HttpHost(assignedServer, DIRECTOR_BASE_URL.getPort(), "https");
        }
    }

    /**
     * Make request to the server and expect to get a 401. Get the WWW-Authenticate challenge header from the response.
     * Instantiate the HTTP client and use challenge header to configure digest authentication.
     * @throws IOException Thrown if server cannot be reached
     * @throws MalformedChallengeException thrown if server response with a malformed challenge header
     */
    private void configureDigestAuthentication() throws IOException, MalformedChallengeException {
        if (httpClient == null) {
            var challengeHeader = executeGetRequestToGetChallengeHeader();
            initHttpClient(challengeHeader);
        }
    }

    /**
     * Instantiate the HTTP client and use challenge header to configure digest authentication.
     * @throws MalformedChallengeException thrown if server response with a malformed challenge header
     */
    private void initHttpClient(Header challengeHeader) throws MalformedChallengeException {
        httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        AuthCache authCache = new BasicAuthCache();
        DigestScheme digestAuth = new DigestScheme();
        digestAuth.processChallenge(challengeHeader);
        authCache.put(targetHost, digestAuth);

        httpClientContext = HttpClientContext.create();
        httpClientContext.setAuthCache(authCache);
    }

    private Header executeGetRequestToGetChallengeHeader() throws IOException {
        var context = HttpClientContext.create();
        var response = HttpClientBuilder.create().build()
                .execute(targetHost, new HttpGet("/"), context);
        return response.getFirstHeader(AUTH.WWW_AUTH);
    }
}
