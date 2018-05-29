package ftn.xmlwebservisi.firme.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ftn.xmlwebservisi.firme.annotations.PasswordValueMatch;
import ftn.xmlwebservisi.firme.dto.UserDTO;

public class PasswordValueMatchValidator implements ConstraintValidator<PasswordValueMatch, Object> {
	
	@Override
	public void initialize(PasswordValueMatch constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		
		if (obj instanceof UserDTO) {
			UserDTO user = (UserDTO)obj;
			
			if (user.getPassword() == null || user.getPasswordMatch() == null)
				return false;
				
			return user.getPassword().equals(user.getPasswordMatch());
		}
		return false;
	}

}
