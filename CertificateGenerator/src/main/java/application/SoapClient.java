package application;

import java.io.IOException;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import model.CAWebServiceInfo;
import model.CertificateDto;
import model.OCSPResponseStatus;
import service.CertificateService;
import soap.CSRRequestDto;
import soap.DownloadRequestDto;
import soap.RevocationRequestDto;
import soap.StatusRequestDto;

public class SoapClient extends WebServiceGatewaySupport{
	
	@Autowired
	private CertificateService certificateService;
	private final Logger logger = LoggerFactory.getLogger(SoapClient.class);
	
	public String sendCSR(CertificateDto csr, String caAlias) {
			WebServiceTemplate template = getWebServiceTemplate();       
			
			PKCS10CertificationRequest request = certificateService.generateCSR(csr); 
			CSRRequestDto dto = new CSRRequestDto();
			String caWS = CAWebServiceInfo.getWSfor(caAlias);
			try {
				dto.setRequestString(Base64.toBase64String(request.getEncoded()));
				
				logger.info("Sending CSR to: {}", caWS);
				CSRRequestDto response = (CSRRequestDto) template.marshalSendAndReceive(caWS, dto);
				if(response != null) {
					return (String)response.getRequestString();
				}
				
			} catch (IOException e) {
				logger.error("Sending CSR to: {} failed", caWS , e);
				e.printStackTrace();
			} 
			
			logger.error("Invalid CSR response");
		return null;
	}
	
/*	public String sendSelfSignedCSR(CertificateDto csr, String caAlias) {
		WebServiceTemplate template = getWebServiceTemplate();   
		String caWS = CAWebServiceInfo.getWSfor(caAlias);
		SoapActionCallback callback = new SoapActionCallback(caWS+"/handleSelfSignedCSR");
		
		PKCS10CertificationRequest request = certificateService.generateCSR(csr); 
		CSRRequestDto dto = new CSRRequestDto();
		try {
			dto.setRequestString(Base64.toBase64String(request.getEncoded()));
			
			logger.info("Sending CSR to: {}", caWS);
			CSRRequestDto response = (CSRRequestDto) template.marshalSendAndReceive(caWS, dto, callback);
			if(response != null) {
				return (String)response.getRequestString();
			}
			
		} catch (IOException e) {
			logger.error("Sending CSR to: {} failed", caWS , e);
			e.printStackTrace();
		} 
		
		logger.error("Invalid CSR response");
	return null;
	}*/
	
	public ByteArrayResource getCertificateFile(String serialNumber, String caAlias) {
		WebServiceTemplate template = getWebServiceTemplate(); 
		DownloadRequestDto dto = new DownloadRequestDto();
		String caWS = CAWebServiceInfo.getWSfor(caAlias);
		
		dto.setRequestString(serialNumber);
		logger.info("Sending Certificate File Request to: {}, Obj={}", caWS, serialNumber );
		try {	
			DownloadRequestDto response = (DownloadRequestDto) template.marshalSendAndReceive(caWS, dto);
			ByteArrayResource result = new ByteArrayResource(Base64.decode((String)response.getRequestString()));	
			return result;
		} catch (Exception e)	{
			logger.error("Could not reach: {}", caWS, e);
		}
		logger.error("Invalid Certificate File response");
		return null;
	}
	
	public OCSPResponseStatus revokeCertificate(String serialNumber, String caAlias) {
		WebServiceTemplate template = getWebServiceTemplate();
		RevocationRequestDto dto = new RevocationRequestDto();
		String caWS = CAWebServiceInfo.getWSfor(caAlias);
		
		dto.setRequestString(serialNumber);
		logger.info("Sending Revocation Request to: {}, Obj={}", caWS, serialNumber );
		try {
			RevocationRequestDto response = (RevocationRequestDto) template.marshalSendAndReceive(caWS, dto);
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
		} catch (Exception e) {
			logger.error("Could not reach: {}", caWS, e);
		}
		logger.error("Invalid Revocation response");
		return null;
	}


	public OCSPResponseStatus checkCertificate(String serialNumber, String caAlias) {
		WebServiceTemplate template = getWebServiceTemplate();
		StatusRequestDto dto = new StatusRequestDto();
		String caWS = CAWebServiceInfo.getWSfor(caAlias);
		
		dto.setRequestString(serialNumber);
		logger.info("Sending Certificate Status Request to: {}, Obj={}", caWS, serialNumber );
		try {
			StatusRequestDto response = (StatusRequestDto) template.marshalSendAndReceive(caWS, dto);
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
			
		} catch(Exception e) {
			logger.error("Could not reach: {}", caWS, e);
		}
		logger.error("Invalid Certificate Status response");
		return null;
	}

	
}
