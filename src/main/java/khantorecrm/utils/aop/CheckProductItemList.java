package khantorecrm.utils.aop;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckItemsValidator.class)
public @interface CheckProductItemList {
    String message() default "Product items isn't valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
