package model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class CertificateDto implements Serializable {
	
	protected String certificateAlias;
	protected String commonName;
	protected String surname;
	protected String givenName;
	protected String organisation;
	protected String location;
	protected String country;
	protected String email;
	
	protected String issuer;
	
	protected String keyAlias;
	protected String keyPassword;
	
	@JsonProperty //jer JAckson lose prevodi, bez ovog bude uvek false
	protected boolean isCA;
	@JsonProperty
	protected boolean isSelfSigned; 
	
	
	public CertificateDto(){
		super();
	}
	


	public CertificateDto(String certificateAlias, String commonName, String surname, String givenName,
			String organisation, String location, String country, String email, String issuer, String keyAlias,
			String keyPassword, boolean isCA, boolean isSelfSigned) {
		super();
		this.certificateAlias = certificateAlias;
		this.commonName = commonName;
		this.surname = surname;
		this.givenName = givenName;
		this.organisation = organisation;
		this.location = location;
		this.country = country;
		this.email = email;
		this.issuer = issuer;
		this.keyAlias = keyAlias;
		this.keyPassword = keyPassword;
		this.isCA = isCA;
		this.isSelfSigned = isSelfSigned;
	}






	public boolean isValid(CertificateDto certificate) {
		if(certificate.getCertificateAlias() == null || certificate.getCertificateAlias() == "")
			return false;
		else if(certificate.getCommonName() == null || certificate.getCommonName() == "")
			return false;
		else if(certificate.getSurname() == null || certificate.getSurname() == "")
			return false;
		else if(certificate.getGivenName() == null || certificate.getGivenName() == "")
			return false;
		else if(certificate.getOrganisation() == null || certificate.getOrganisation() == "")
			return false;
		else if(certificate.getLocation() == null || certificate.getLocation() == "")
			return false;
		else if(certificate.getCountry() == null || certificate.getCountry() == "")
			return false;
		else if(certificate.getEmail() == null || certificate.getEmail() == "")
			return false;
		else if(certificate.getOrganisation() == null || certificate.getOrganisation() == "")
			return false;
		else if(certificate.getKeyAlias() == null || certificate.getKeyAlias() == "")
			return false;
		else if(certificate.getKeyPassword() == null || certificate.getKeyPassword() == "")
			return false;
		else if(!certificate.isSelfSigned && (certificate.getIssuer() == null || certificate.getIssuer() == ""))
			return false;
		else if(certificate.isSelfSigned && certificate.getIssuer() != null && certificate.getIssuer() != "")
			return false;
		
		return true;
	}






	public String getCommonName() {
		return commonName;
	}






	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}






	public String getSurname() {
		return surname;
	}






	public void setSurname(String surname) {
		this.surname = surname;
	}






	public String getGivenName() {
		return givenName;
	}






	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}






	public String getOrganisation() {
		return organisation;
	}






	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}






	public String getLocation() {
		return location;
	}






	public void setLocation(String location) {
		this.location = location;
	}






	public String getCountry() {
		return country;
	}






	public void setCountry(String country) {
		this.country = country;
	}






	public String getEmail() {
		return email;
	}






	public void setEmail(String email) {
		this.email = email;
	}







	public String getIssuer() {
		return issuer;
	}






	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}






	public boolean isCA() {
		return isCA;
	}






	public void setCA(boolean isCA) {
		this.isCA = isCA;
	}






	public boolean isSelfSigned() {
		return isSelfSigned;
	}






	public void setSelfSigned(boolean isSelfSigned) {
		this.isSelfSigned = isSelfSigned;
	}






	public String getCertificateAlias() {
		return certificateAlias;
	}






	public void setCertificateAlias(String alias) {
		this.certificateAlias = alias;
	}






	public String getKeyAlias() {
		return keyAlias;
	}






	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}






	public String getKeyPassword() {
		return keyPassword;
	}






	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}



	
	
}
