package org.fun7.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.fun7.model.ServicesStatus;
import org.fun7.model.ServicesStatus.EStatus;
import org.fun7.model.User;
import org.fun7.model.requests.CheckServicesQueryRequest;
import org.fun7.util.TimeUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.DayOfWeek;

@ApplicationScoped
public class CheckServicesService {

    @Inject
    TimeUtils timeUtils;

    @Inject
    @RestClient
    AdApiService adApiService;

    public ServicesStatus checkServicesStatus(CheckServicesQueryRequest request, User user) {

        var status = new ServicesStatus();
        status.setMultiplayer(checkMultiplayer(user, request.getCc()));
        status.setUserSupport(checkCustomerSupport());
        status.setAds(checkAdService(request.getCc()));

        user.setTimesPlayed(user.getTimesPlayed() + 1);

        return status;
    }

    private EStatus checkMultiplayer(User user, String countryCode) {
        return user.getTimesPlayed() > 5 && countryCode.equals("US") ? EStatus.Enabled : EStatus.Disabled;
    }

    private EStatus checkCustomerSupport() {
        var dateTimeInLju = timeUtils.getDateTimeInLjubljana();
        var currentDayOfWeek = dateTimeInLju.getDayOfWeek().getValue();
        var currentHour = dateTimeInLju.getHour();

        if ((currentDayOfWeek < DayOfWeek.SATURDAY.getValue() && currentHour >= 9 && currentHour <= 14)) {
            return EStatus.Enabled;
        }
        return EStatus.Disabled;
    }

    private EStatus checkAdService(String countryCode) {
        try {
            var response = adApiService.getAdStatusForCountryCode(countryCode).toCompletableFuture().get();
            return response.getAds().equals("sure, why not!") ? EStatus.Enabled : EStatus.Disabled;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return EStatus.Disabled;
        }
    }
}
