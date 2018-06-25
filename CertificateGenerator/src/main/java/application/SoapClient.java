package application;

import java.io.IOException;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import model.CAWebServiceInfo;
import model.CSRDto;
import model.OCSPResponseStatus;
import service.CertificateService;
import soap.CSRRequestDto;
import soap.DownloadRequestDto;
import soap.RevocationRequestDto;
import soap.StatusRequestDto;

public class SoapClient extends WebServiceGatewaySupport{
	
	@Autowired
	private CertificateService certificateService;
	
	//TODO:  srediti response
	public CSRDto sendCSR(CSRDto csr, String caAlias) {
			SoapActionCallback callback = new SoapActionCallback(CAWebServiceInfo.getCallbackFor(caAlias));
			WebServiceTemplate template = getWebServiceTemplate();       
			
			PKCS10CertificationRequest request = certificateService.generateCSR(csr); 
			CSRRequestDto dto = new CSRRequestDto();
			
			try {
				dto.setRequestString(Base64.toBase64String(request.getEncoded()));
				//CSRRequestDto response = (CSRRequestDto) template.marshalSendAndReceive(CAWebServiceInfo.getWSfor(caAlias), dto, callback);
				CSRRequestDto response = (CSRRequestDto) template.marshalSendAndReceive(CAWebServiceInfo.getWSfor(caAlias), dto);
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		
		return null;
	}
	
	
	public ByteArrayResource getCertificateFile(String serialNumber, String caAlias) {
		SoapActionCallback callback = new SoapActionCallback(CAWebServiceInfo.getCallbackFor(caAlias));
		WebServiceTemplate template = getWebServiceTemplate(); 
		DownloadRequestDto dto = new DownloadRequestDto();
		
		dto.setRequestString(serialNumber);
		//DownloadRequestDto response = (DownloadRequestDto) template.marshalSendAndReceive(CAWebServiceInfo.getWSfor(caAlias), dto, callback);
		DownloadRequestDto response = (DownloadRequestDto) template.marshalSendAndReceive(CAWebServiceInfo.getWSfor(caAlias), dto);
		ByteArrayResource result = new ByteArrayResource(Base64.decode((String)response.getRequestString()));		
		
		return result;
	}
	
	public OCSPResponseStatus revokeCertificate(String serialNumber, String caAlias) {
		SoapActionCallback callback = new SoapActionCallback(CAWebServiceInfo.getCallbackFor(caAlias));
		WebServiceTemplate template = getWebServiceTemplate();
		RevocationRequestDto dto = new RevocationRequestDto();
		
		dto.setRequestString(serialNumber);
		//RevocationRequestDto response = (RevocationRequestDto) template.marshalSendAndReceive(CAWebServiceInfo.getWSfor(caAlias), dto, callback);
		RevocationRequestDto response = (RevocationRequestDto) template.marshalSendAndReceive(CAWebServiceInfo.getWSfor(caAlias), dto);
		if(response != null) {
			if(response.getRequestString().equals("UNKNOWN")) {
				return OCSPResponseStatus.UNKNOWN;
			}
			if (response.getRequestString().equals("GOOD")) {
				return OCSPResponseStatus.GOOD;
			}
			else if(response.getRequestString().equals("REVOKED"))
				return OCSPResponseStatus.REVOKED;
		}
		return null;
	}


	public OCSPResponseStatus checkCertificate(String serialNumber, String caAlias) {
		WebServiceTemplate template = getWebServiceTemplate();
		StatusRequestDto dto = new StatusRequestDto();
		
		dto.setRequestString(serialNumber);
		StatusRequestDto response = (StatusRequestDto) template.marshalSendAndReceive(CAWebServiceInfo.getWSfor(caAlias), dto);
		if(response != null) {
			if(response.getRequestString().equals("UNKNOWN")) {
				return OCSPResponseStatus.UNKNOWN;
			}
			if (response.getRequestString().equals("GOOD")) {
				return OCSPResponseStatus.GOOD;
			}
			else if(response.getRequestString().equals("REVOKED"))
				return OCSPResponseStatus.REVOKED;
		}
		return null;
	}
}
