package ftn.xmlwebservisi.firme.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	
}
