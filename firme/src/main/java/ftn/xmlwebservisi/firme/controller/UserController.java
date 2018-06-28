package ftn.xmlwebservisi.firme.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	
	@GetMapping("/userlogout")
	public ResponseEntity<?> logoutUser() {
		HttpHeaders cookieHeader = new HttpHeaders();
		cookieHeader.add(HttpHeaders.SET_COOKIE, "jjwt=null; HttpOnly;");
		return new ResponseEntity<>(cookieHeader, HttpStatus.OK);
	}
	
	@GetMapping("/public")
	public String pub() {
		return "Permit all";
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/secured") 
	public String secured() {
		return "Only for users";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admins") 
	public String admin() {
		return "Only for admins";
	}
}
