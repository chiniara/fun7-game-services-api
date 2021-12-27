package org.fun7.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockAdApiService implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        var goodReturn = "{\"ads\": \"sure, why not!\"}";
        var badReturn = "{\"ads\": \"you shall not pass!\"}";

        stubFor(get(urlEqualTo("/"))
                .withQueryParam("countryCode", equalTo("US"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(goodReturn)));

        stubFor(get(urlEqualTo("/"))
                .withQueryParam("countryCode", equalTo("MX"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(badReturn)));

        stubFor(get(urlEqualTo("/"))
                .withQueryParam("countryCode", equalTo("JP"))
                .willReturn(aResponse().withStatus(500)));

        return Collections.singletonMap("quarkus.rest-client.\"quarkus.rest-client.ad-api.url", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}
