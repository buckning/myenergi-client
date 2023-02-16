package com.amcglynn.myenergi;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @BeforeEach
    public void setUp() throws Exception {
        when(mockClient.execute(any(HttpHost.class), any(HttpGet.class), any(HttpClientContext.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.containsHeader(anyString()))
                .thenReturn(false);
        when(mockStatusLine.getStatusCode()).thenReturn(200);
        when(mockHttpResponse.getEntity()).thenReturn(mockEntity);
        when(mockEntity.getContent()).thenReturn(new ByteArrayInputStream(ZappiResponse.getExampleResponse().getBytes()));
        when(mockHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
    }

    @Test
    void testGetStatus() {
        var client = new MyEnergiClient("fakeSerialNumber", "fakeKey", mockClient, mockHttpClientContext);
        var response = client.getZappiStatus();

        assertThat(response.getZappi()).hasSize(1);
        var zappiResponse = response.getZappi().get(0);
        assertThat(zappiResponse.getSerialNumber()).isEqualTo("12345678");
        assertThat(zappiResponse.getSolarGeneration()).isEqualTo(594);
        assertThat(zappiResponse.getChargeAddedThisSessionKwh()).isEqualTo(21.39);
        assertThat(zappiResponse.getEvConnectionStatus()).isEqualTo("A");
        assertThat(zappiResponse.getZappiChargeMode()).isEqualTo(3);
    }
}
