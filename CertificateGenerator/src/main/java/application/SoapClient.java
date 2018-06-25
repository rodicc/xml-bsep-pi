package application;

import java.io.IOException;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import model.CSRDto;
import service.CertificateService;
import soap.CSRRequestDto;
import xmlSignatureAndEncryption.XMLSignAndEncryptUtility;

public class SoapClient extends WebServiceGatewaySupport{
	
	@Autowired
	private CertificateService certificateService;
	
	XMLSignAndEncryptUtility xmlSignAndEncryptUtility;
	//TODO:  srediti response
	public CSRDto sendCSR(CSRDto csr, String caAlias) {
		
			SoapActionCallback callback = new SoapActionCallback("http://www.ftn.xml/banke/HandleCSR");
			WebServiceTemplate template = getWebServiceTemplate();       
			
			PKCS10CertificationRequest request = certificateService.generateCSR(csr); 
			CSRRequestDto dto = new CSRRequestDto();
			
			try {
				dto.setCSRRequestString(Base64.toBase64String(request.getEncoded()));
				CSRRequestDto response = (CSRRequestDto) template.marshalSendAndReceive("http://localhost:8082/ws", dto, callback);
				String certificateAlias = certificateService.saveCertificate(Base64.decode((String)response.getCSRRequestString()));
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		
		return null;
	}
}
