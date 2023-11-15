package fontys.individual.school.controller.Constraints;

import fontys.individual.school.controller.ControllerValidations.OpenTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD,METHOD,PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = OpenTimeValidator.class)
public @interface OpenTime {
    String message() default "Open time must be after {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    String  value();

    @Target({FIELD,METHOD,PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List{
        OpenTime[] value();
    }

}
