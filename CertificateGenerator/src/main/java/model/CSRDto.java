package model;

import java.io.Serializable;
import java.security.PublicKey;

@SuppressWarnings("serial")
public class CSRDto extends CertificateDto implements Serializable{

	private PublicKey publicKey;
	private String keyStoreAlias;
	private String keyStorePassword;
	
	
	
	public CSRDto() {
		super();
	}

	public CSRDto(PublicKey publicKey, String keyStoreAlias, String keyStorePassword) {
		super();
		this.publicKey = publicKey;
		this.keyStoreAlias = keyStoreAlias;
		this.keyStorePassword = keyStorePassword;
	}
	
	public CSRDto(PublicKey publicKey, String keyStoreAlias, String keyStorePassword, String certificateAlias, String commonName, String surname, String givenName,
			String organisation, String location, String country, String email, String issuer, String keyAlias,
			String keyPassword, boolean isCA, boolean isSelfSigned) {
		super();
		this.publicKey = publicKey;
		this.keyStoreAlias = keyStoreAlias;
		this.keyStorePassword = keyStorePassword;
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
	
	public CSRDto( String keyStoreAlias, String keyStorePassword, String certificateAlias, String commonName, String surname, String givenName,
			String organisation, String location, String country, String email, String issuer, String keyAlias,
			String keyPassword, boolean isCA, boolean isSelfSigned) {
		super();
		this.publicKey = null;
		this.keyStoreAlias = keyStoreAlias;
		this.keyStorePassword = keyStorePassword;
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
	
	public boolean isValid() {
		CertificateDto cerDto = new CertificateDto(this.getCertificateAlias(),
											       this.getCommonName(),
											       this.getSurname(),
											       this.getGivenName(),
											       this.getOrganisation(),
											       this.getLocation(),
											       this.getCountry(),
											       this.getEmail(),
											       this.getIssuer(),
											       this.getKeyAlias(),
											       this.getKeyPassword(),
											       this.isCA(),
											       this.isSelfSigned());
		if(super.isValid(cerDto)) {
			if(this.getKeyStoreAlias() == null || this.getKeyStoreAlias() == "")
				return false;
			else if(this.getKeyStorePassword() == null || this.getKeyStorePassword() == "")
				return false;
			else {
				return true;
			}
		}
		return false;
		
	}
	
	public CertificateDto getCertificateDto() {
		return new CertificateDto(this.getCertificateAlias(),
			       this.getCommonName(),
			       this.getSurname(),
			       this.getGivenName(),
			       this.getOrganisation(),
			       this.getLocation(),
			       this.getCountry(),
			       this.getEmail(),
			       this.getIssuer(),
			       this.getKeyAlias(),
			       this.getKeyPassword(),
			       this.isCA(),
			       this.isSelfSigned());
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public String getKeyStoreAlias() {
		return keyStoreAlias;
	}

	public void setKeyStoreAlias(String keyStoreAlias) {
		this.keyStoreAlias = keyStoreAlias;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	
	
}
