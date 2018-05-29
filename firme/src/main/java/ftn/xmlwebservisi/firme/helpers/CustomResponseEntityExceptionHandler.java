package ftn.xmlwebservisi.firme.helpers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String error = "";
		FieldError fieldError = ex.getBindingResult().getFieldError();
		if (fieldError != null) {
			error = fieldError.getField() + " " + fieldError.getDefaultMessage();
		} else {
			ObjectError objectError = ex.getBindingResult().getGlobalError();
			if (objectError != null) {
				error = objectError.getDefaultMessage();
			}
		}
		
		CustomMessageError customMessage = 
				new CustomMessageError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return handleExceptionInternal(ex, customMessage, headers, customMessage.getStatus(), request);
	}
}
