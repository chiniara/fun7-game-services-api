package org.fun7.model.requests;

import org.fun7.validator.countryCode.CountryCodeConstraint;
import org.fun7.validator.timeZone.TimeZoneConstraint;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.validation.constraints.NotNull;

public class CheckServicesQueryRequest {
    @NotNull(message = "Please provide a timezone")
    @TimeZoneConstraint
    @QueryParam
    private String timezone;

    @NotNull(message = "Please provide an user id")
    @QueryParam
    private Long userId;

    @NotNull(message = "Please provide a country code")
    @CountryCodeConstraint
    @QueryParam
    private String cc;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }
}
