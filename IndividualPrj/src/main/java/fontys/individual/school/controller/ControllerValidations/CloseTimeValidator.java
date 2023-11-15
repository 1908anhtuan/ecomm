package fontys.individual.school.controller.ControllerValidations;

import fontys.individual.school.controller.Constraints.CloseTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class CloseTimeValidator implements ConstraintValidator<CloseTime, LocalTime> {
    private LocalTime maxValue;

    @Override
    public void initialize(CloseTime constraintAnnotation){
        maxValue = LocalTime.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context){
        if(value == null){
            return true;
        }
        return value.isBefore(maxValue);
    }
}
