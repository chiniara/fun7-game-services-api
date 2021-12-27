package org.fun7.util;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@ApplicationScoped
public class TimeUtils {
    private final ZoneId ljubljanaTimeZoneId = TimeZone.getTimeZone("Europe/Ljubljana").toZoneId();

    public LocalDateTime getDateTimeInLjubljana() {
        return LocalDateTime.now(ljubljanaTimeZoneId);
    }

}
