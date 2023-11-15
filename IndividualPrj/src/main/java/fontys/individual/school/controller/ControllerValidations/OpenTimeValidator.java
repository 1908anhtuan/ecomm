package fontys.individual.school.controller.ControllerValidations;

import fontys.individual.school.controller.Constraints.OpenTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class OpenTimeValidator implements ConstraintValidator<OpenTime, LocalTime> {
    private LocalTime minValue;

    @Override
    public void initialize(OpenTime constraintAnnotation){
        minValue = LocalTime.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context){
        if(value == null){
            return true;
        }
        return value.isAfter(minValue);
    }
}
