package ftn.xmlwebservisi.firme.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ftn.xmlwebservisi.firme.dto.MessageDTO;
import ftn.xmlwebservisi.firme.dto.NewPasswordDTO;
import ftn.xmlwebservisi.firme.dto.UserDTO;
import ftn.xmlwebservisi.firme.model.User;
import ftn.xmlwebservisi.firme.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDto) {
		User createdUser = userService.createUser(userDto);
		if (createdUser == null) {
			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<String>("User successfully created", HttpStatus.CREATED);
	}

	@GetMapping("/logoutUser")
	public ResponseEntity<?> logoutUser() {
		HttpHeaders cookieHeader = new HttpHeaders();
		cookieHeader.add(HttpHeaders.SET_COOKIE, "jjwt=null; HttpOnly;");
		return new ResponseEntity<>(cookieHeader, HttpStatus.OK);
	}

	@PostMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody NewPasswordDTO pwDto) {
		UserDetails user = 
				(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (!pwDto.getPassword().equals(pwDto.getPasswordMatch())) {
			
			return new ResponseEntity<>(new MessageDTO("Passwords don't match"), HttpStatus.BAD_REQUEST);
		}
		
		boolean isOldPasswordValid = userService.checkOldPassword(user, pwDto.getOldPassword());
		if (isOldPasswordValid) {
			User updatedUser = userService.updatePassword(user.getUsername(), pwDto.getPassword());
			if (updatedUser != null) {
				return new ResponseEntity<>(new MessageDTO("Password successfully updated"), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(new MessageDTO("Old password is not valid"), HttpStatus.BAD_REQUEST);
	}
	
}
