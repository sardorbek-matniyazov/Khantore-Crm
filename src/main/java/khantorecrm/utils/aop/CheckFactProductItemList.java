package khantorecrm.utils.aop;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckFactProductItemValidator.class)
public @interface CheckFactProductItemList {
    String message() default "Fact Product items isn't valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
