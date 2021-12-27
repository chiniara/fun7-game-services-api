package org.fun7.validator.timeZone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZoneId;

public class TimeZoneValidator implements ConstraintValidator<TimeZoneConstraint, String> {
    @Override
    public void initialize(TimeZoneConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        try {
            var timezone = ZoneId.of(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
