package ec.com.tecnointel.soem.general.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Constraint(validatedBy = ValidadorTest.class)
public @interface ValidadorTestInt {

    String message() default "El valor no puede ser mayor que 100";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

	
}
