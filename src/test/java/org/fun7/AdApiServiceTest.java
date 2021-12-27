package org.fun7;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.fun7.mock.WireMockAdApiService;
import org.fun7.service.AdApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Base64;

@QuarkusTest
@QuarkusTestResource(WireMockAdApiService.class)
public class AdApiServiceTest {
    @Inject
    @RestClient
    AdApiService apiService;

    @Test
    public void testGetResponseValidCountryCode() throws Exception {
        var response = apiService.getAdStatusForCountryCode("US");

        Assertions.assertEquals("sure, why not!", response.toCompletableFuture().get().getAds());

    }

    @Test
    public void testGetResponseInvalidCountryCode() throws Exception {
        var response = apiService.getAdStatusForCountryCode("MX");

        Assertions.assertEquals("you shall not pass!", response.toCompletableFuture().get().getAds());

    }

    @Test
    public void testGetResponseInternalServerError() {

        var thrown = Assertions.assertThrows(Exception.class, () -> {
            apiService.getAdStatusForCountryCode("JP");
        });

        Assertions.assertEquals("500", thrown.getMessage());
    }

    @Test
    public void testGetCredentials() {
        var credentials = apiService.getCredentials();

        var expectedCredentials = "Basic " + Base64.getEncoder()
                .encodeToString(String.format("%s:%s", "test", "test").getBytes());

        Assertions.assertEquals(expectedCredentials, credentials);
    }
}
