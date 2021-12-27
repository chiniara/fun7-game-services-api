package org.fun7.service;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.fun7.model.responses.AdResponse;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Base64;
import java.util.concurrent.CompletionStage;

@Path("/")
@RegisterRestClient(configKey = "ad-api")
@ClientHeaderParam(name = "Authorization", value = "{getCredentials}")
public interface AdApiService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<AdResponse> getAdStatusForCountryCode(@QueryParam String countryCode) throws Exception;

    default String getCredentials() {
        var user = ConfigProvider.getConfig().getValue("ad-api-user", String.class);
        var pass = ConfigProvider.getConfig().getValue("ad-api-pass", String.class);
        var credentials = String.format("%s:%s", user, pass);
        return "Basic " +
                Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
