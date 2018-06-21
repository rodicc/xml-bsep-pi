package ftn.xmlwebservisi.firme.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ftn.xmlwebservisi.firme.annotations.UniqueUsername;
import ftn.xmlwebservisi.firme.repository.UserRepository;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public void initialize(UniqueUsername arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return username != null && userRepository.findByUsername(username) == null;
	}

}
