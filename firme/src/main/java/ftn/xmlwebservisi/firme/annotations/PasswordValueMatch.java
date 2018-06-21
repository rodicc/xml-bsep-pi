package ftn.xmlwebservisi.firme.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ftn.xmlwebservisi.firme.validators.PasswordValueMatchValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValueMatchValidator.class)
public @interface PasswordValueMatch {
	
	String message() default "Passwords don't match!";
	Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
