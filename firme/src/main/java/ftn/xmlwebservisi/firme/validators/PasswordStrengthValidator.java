package ftn.xmlwebservisi.firme.validators;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import com.google.common.base.Joiner;

import ftn.xmlwebservisi.firme.annotations.PasswordStrength;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {
	
	@Override
	public void initialize(PasswordStrength constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		PasswordValidator validator = new PasswordValidator(Arrays.asList(
				// length between 8 and 50 characters
				new LengthRule(8, 50),
				// at least one upper-case character
				new CharacterRule(EnglishCharacterData.UpperCase, 1),
				// at least one digit character
				new CharacterRule(EnglishCharacterData.Digit, 1),
				// no white spaces
				new WhitespaceRule()
				));
		
		RuleResult result = validator.validate(new PasswordData(value));
		if (result.isValid()) {
			return true;
		}
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result)))
				.addConstraintViolation();
		return false;
	}
}
