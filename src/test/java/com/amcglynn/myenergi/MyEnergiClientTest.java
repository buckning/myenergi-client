package com.amcglynn.myenergi;

import com.amcglynn.myenergi.exception.InvalidRequestException;
import com.amcglynn.myenergi.units.KiloWattHour;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyEnergiClientTest {

    @Mock
    private HttpClient mockClient;
    @Mock
    private HttpResponse mockHttpResponse;
    @Mock
    private HttpClientContext mockHttpClientContext;
    @Mock
    private StatusLine mockStatusLine;
    @Mock
    private HttpEntity mockEntity;
    @Captor
    ArgumentCaptor<HttpGet> httpGetCaptor;
    private MyEnergiClient client;

    @BeforeEach
    public void setUp() throws Exception {
        when(mockClient.execute(any(HttpHost.class), any(HttpGet.class), any(HttpClientContext.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.containsHeader(anyString()))
                .thenReturn(false);
        when(mockStatusLine.getStatusCode()).thenReturn(200);
        when(mockHttpResponse.getEntity()).thenReturn(mockEntity);

        when(mockHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockEntity.getContent()).thenReturn(new ByteArrayInputStream(ZappiResponse.getGenericResponse().getBytes()));
        client = new MyEnergiClient("12345678", "fakeKey", mockClient, mockHttpClientContext);
    }

    @Test
    void testGetStatus() throws Exception {
        when(mockEntity.getContent()).thenReturn(new ByteArrayInputStream(ZappiResponse.getExampleResponse().getBytes()));
        var response = client.getZappiStatus();

        assertThat(response.getZappi()).hasSize(1);
        var zappiResponse = response.getZappi().get(0);
        assertThat(zappiResponse.getSerialNumber()).isEqualTo("12345678");
        assertThat(zappiResponse.getSolarGeneration()).isEqualTo(594);
        assertThat(zappiResponse.getChargeAddedThisSessionKwh()).isEqualTo(21.39);
        assertThat(zappiResponse.getEvConnectionStatus()).isEqualTo("A");
        assertThat(zappiResponse.getZappiChargeMode()).isEqualTo(3);
        assertThat(zappiResponse.getChargeStatus()).isEqualTo(ChargeStatus.PAUSED.ordinal());
    }

    @Test
    void testBoostModeThrowsInvalidRequestExceptionWhenMinuteIsNotDivisibleBy15() throws Exception {
        when(mockEntity.getContent()).thenReturn(new ByteArrayInputStream(ZappiResponse.getErrorResponse().getBytes()));
        var endTime = LocalTime.now().withMinute(3);
        var kiloWattHour = new KiloWattHour(5);

        assertThatThrownBy(() -> client.boost(endTime, kiloWattHour))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void testSmartBoostUrlIsCorrectlyFormed() throws Exception {
        var endTime = LocalTime.now().withHour(2).withMinute(15);

        client.boost(endTime, new KiloWattHour(5));
        verify(mockClient).execute(any(HttpHost.class), httpGetCaptor.capture(), any(HttpClientContext.class));
        assertThat(httpGetCaptor.getValue().getURI())
                .hasToString("/cgi-zappi-mode-Z12345678-0-11-5-0215");
    }

    @Test
    void testSmartBoostWithOnlyEndTimeSpecifiedChangesTheChargeAmountTo99Kwh() throws Exception {
        var endTime = LocalTime.now().withHour(15).withMinute(45);

        client.boost(endTime);
        verify(mockClient).execute(any(HttpHost.class), httpGetCaptor.capture(), any(HttpClientContext.class));
        assertThat(httpGetCaptor.getValue().getURI())
                .hasToString("/cgi-zappi-mode-Z12345678-0-11-99-1545");
    }

    @Test
    void testBoostWithKiloWattHours() throws Exception {
        client.boost(new KiloWattHour(34));
        verify(mockClient).execute(any(HttpHost.class), httpGetCaptor.capture(), any(HttpClientContext.class));
        assertThat(httpGetCaptor.getValue().getURI())
                .hasToString("/cgi-zappi-mode-Z12345678-0-10-34-0000");
    }

    @Test
    void testStopBoostMode() throws Exception {
        client.stopBoost();
        verify(mockClient).execute(any(HttpHost.class), httpGetCaptor.capture(), any(HttpClientContext.class));
        assertThat(httpGetCaptor.getValue().getURI())
                .hasToString("/cgi-zappi-mode-Z12345678-0-2-0-0000");
    }

    @MethodSource("chargeModesAndExpectedUrls")
    @ParameterizedTest
    void testZappiChargeMode(ZappiChargeMode zappiChargeMode, String expectedUrl) throws Exception {
        client.setZappiChargeMode(zappiChargeMode);
        verify(mockClient).execute(any(HttpHost.class), httpGetCaptor.capture(), any(HttpClientContext.class));
        assertThat(httpGetCaptor.getValue().getURI())
                .hasToString(expectedUrl);
    }

    private static Stream<Arguments> chargeModesAndExpectedUrls() {
        return Stream.of(
                Arguments.of(ZappiChargeMode.FAST, "/cgi-zappi-mode-Z12345678-1-0-0-0000"),
                Arguments.of(ZappiChargeMode.ECO_PLUS, "/cgi-zappi-mode-Z12345678-3-0-0-0000"),
                Arguments.of(ZappiChargeMode.ECO, "/cgi-zappi-mode-Z12345678-2-0-0-0000"),
                Arguments.of(ZappiChargeMode.STOP, "/cgi-zappi-mode-Z12345678-4-0-0-0000")
        );
    }
}
