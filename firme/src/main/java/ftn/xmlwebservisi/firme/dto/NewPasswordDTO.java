package ftn.xmlwebservisi.firme.dto;

import org.hibernate.validator.constraints.NotBlank;

public class NewPasswordDTO {
	
	@NotBlank
	private String oldPassword;
	@NotBlank
	private String password;
	@NotBlank
	private String passwordMatch;
	
	public NewPasswordDTO() {}
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
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
	public void setPasswordMatch(String passwordMatch) {
		this.passwordMatch = passwordMatch;
	}
	
	
}
