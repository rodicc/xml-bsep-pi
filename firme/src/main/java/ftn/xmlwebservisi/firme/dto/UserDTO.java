package ftn.xmlwebservisi.firme.dto;

import ftn.xmlwebservisi.firme.annotations.PasswordValueMatch;
import ftn.xmlwebservisi.firme.annotations.UniqueUsername;

@PasswordValueMatch
public class UserDTO {
	
	@UniqueUsername
	private String username;
	private String password;
	private String passwordMatch;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordMatch() {
		return passwordMatch;
	}

	public void setPasswordMatch(String matchingPassword) {
		this.passwordMatch = matchingPassword;
	}
}
