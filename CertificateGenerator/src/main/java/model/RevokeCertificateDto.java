package model;

public class RevokeCertificateDto {

	private String certificateAlias;
	private String caKeyEntryAlias;
	private String caKeyEntryPassword;
	
	
	
	
	public RevokeCertificateDto() {
		super();
	}


	public RevokeCertificateDto(String certificateAlias, String caKeyEntryAlias, String caKeyEntryPassword) {
		super();
		this.certificateAlias = certificateAlias;
		this.caKeyEntryAlias = caKeyEntryAlias;
		this.caKeyEntryPassword = caKeyEntryPassword;
	}


	public String getCertificateAlias() {
		return certificateAlias;
	}


	public void setCertificateAlias(String certificateAlias) {
		this.certificateAlias = certificateAlias;
	}


	public String getCaKeyEntryAlias() {
		return caKeyEntryAlias;
	}


	public void setCaKeyEntryAlias(String caKeyEntryAlias) {
		this.caKeyEntryAlias = caKeyEntryAlias;
	}


	public String getCaKeyEntryPassword() {
		return caKeyEntryPassword;
	}


	public void setCaKeyEntryPassword(String caKeyEntryPassword) {
		this.caKeyEntryPassword = caKeyEntryPassword;
	}
	
	
	
}
