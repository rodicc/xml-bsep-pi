package model.certificateGenerator;

public class CertificateResponseDto {

	private String certificateText;

	public CertificateResponseDto(String certificateText) {
		super();
		this.certificateText = certificateText;
	}

	public String getCertificateText() {
		return certificateText;
	}

	public void setCertificateText(String certificateText) {
		this.certificateText = certificateText;
	}
	
	
}
