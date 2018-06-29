package ftn.xmlwebservisi.firme.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ftn.xmlwebservisi.firme.validators.PasswordStrengthValidator;

@Documented
@Retention(RUNTIME)
@Target({ ElementType.FIELD })
@Constraint(validatedBy = PasswordStrengthValidator.class)
public @interface PasswordStrength {
	String message() default "Invalid Password";
	 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
}
