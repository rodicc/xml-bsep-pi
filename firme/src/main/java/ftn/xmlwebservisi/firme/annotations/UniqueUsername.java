package ftn.xmlwebservisi.firme.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ftn.xmlwebservisi.firme.validators.UniqueUsernameValidator;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {
	String message() default "The given username is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
