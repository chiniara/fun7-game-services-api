package org.fun7.mock;

import io.quarkus.test.Mock;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.fun7.model.responses.AdResponse;
import org.fun7.service.AdApiService;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;

@Mock
@ApplicationScoped
@RegisterRestClient
@RestClient
public class MockAdApiService implements AdApiService {
    @Override
    public CompletionStage<AdResponse> getAdStatusForCountryCode(String countryCode) throws Exception {
        var completableFuture = new CompletableFuture<AdResponse>();

        if (countryCode.equals("JP")) {
            throw new Exception("500");
        }

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            var adResponse = new AdResponse();

            if (countryCode.equals("US") || countryCode.equals("BR")) {
                adResponse.setAds("sure, why not!");
            } else {
                adResponse.setAds("you shall not pass!");
            }

            completableFuture.complete(adResponse);
            return null;
        });

        return completableFuture;

    }

}
