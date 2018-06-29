package xml.ftn.centralnabanka;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.cert.X509Certificate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;
import org.w3c.dom.Document;

import model.certificateGenerator.OCSPResponseStatus;
import service.Servis;
import xml.ftn.banke.CSRRequestDto;
import xml.ftn.banke.DownloadRequestDto;
import xml.ftn.banke.RevocationRequestDto;
import xml.ftn.banke.StatusRequestDto;
import xmlSignatureAndEncryption.CertificateService;
import xmlSignatureAndEncryption.XMLSignAndEncryptUtility;

@Endpoint
public class SoapEndpoint {

	//private static final String NAMESPACE_URI = "http://www.ftn.xml/centralnabanka";
	
	@Autowired
	private Servis servis;
	private XMLSignAndEncryptUtility xmlSignAndEncryptUtility;
	private CertificateService certificateService;
	private final Logger logger = LoggerFactory.getLogger(SoapEndpoint.class);

	//@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendMT103Request")
	@ResponsePayload
	@SoapAction("http://www.ftn.xml/centralnabanka/sendMT103Request") 
	public Source sendMT103(@RequestPayload StreamSource request) {
		System.out.println("centralna banka");
		//Dekripcija dokumenta
		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		InputStream inStream = request.getInputStream();
		Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(inStream);
		JAXBElement<SendMT103Request> sendMT103Request = null;
		try {
			JAXBContext jaxbContext;
			if((decryptedDocument != null) && (decryptedDocument.getDocumentElement().getLocalName().equals(SendMT103Request.class.getSimpleName()))) {
				//Unmarshal dokumenta u objekat
				jaxbContext = JAXBContext.newInstance(SendMT103Request.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				sendMT103Request = unmarshaller.unmarshal(decryptedDocument, SendMT103Request.class );
				
				//Regulise se zahtev
				SendMT103Response response = new SendMT103Response();
				MT103 mt103 = sendMT103Request.getValue().getMT103();
				response.setMT103Response(servis.regulisiMT103(mt103));
				
				//Enkripcija odgovora
				JAXBElement<SendMT103Response> jaxbElement =
						new JAXBElement<SendMT103Response>(new QName(SendMT103Response.class.getSimpleName()),SendMT103Response.class, response);
				JAXBContext responseContext = JAXBContext.newInstance(SendMT103Response.class);
			
				xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
				DOMSource source = xmlSignAndEncryptUtility.encryptToSource(jaxbElement, responseContext);
				
				return source;
			}	
		} catch (JAXBException e) {
			logger.error("Invalid encryption element: Obj={}", sendMT103Request, e);
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
		
		
		
		
		
	}
	
	//@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendMT102Request")
	@ResponsePayload
	@SoapAction("http://www.ftn.xml/centralnabanka/sendMT102Request") 
	public Source sendMT102(@RequestPayload StreamSource request) {
		//Dekripcija dokumenta
		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		InputStream inStream = request.getInputStream();
		Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(inStream);
		JAXBElement<SendMT102Request> sendMT102Request = null;
		try {
			JAXBContext jaxbContext;
			if((decryptedDocument != null) && (decryptedDocument.getDocumentElement().getLocalName().equals(SendMT102Request.class.getSimpleName()))) {
				//Unmarshal dokumenta u objekat
				jaxbContext = JAXBContext.newInstance(SendMT102Request.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				sendMT102Request = unmarshaller.unmarshal(decryptedDocument, SendMT102Request.class );
				
				//Regulise se zahtev
				SendMT102Response response = new SendMT102Response();
				MT102 mt102 = sendMT102Request.getValue().getMT102();
				response.setMT102Response(servis.regulisiMT102(mt102));
				if(servis.regulisiMT102(mt102) == null) {
					
				}
				
				//Enkripcija odgovora
				JAXBElement<SendMT102Response> jaxbElement =
						new JAXBElement<SendMT102Response>(new QName(SendMT102Response.class.getSimpleName()),SendMT102Response.class, response);
				JAXBContext responseContext = JAXBContext.newInstance(SendMT102Response.class);
			
				xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
				DOMSource source = xmlSignAndEncryptUtility.encryptToSource(jaxbElement, responseContext);
				
				return source;
			}	
		} catch (JAXBException e) {
			logger.error("Invalid encryption element: Obj={}", sendMT102Request, e);
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@ResponsePayload
	@PayloadRoot(namespace = "http://www.ftn.xml/banke", localPart = "CSRRequestDto")
	public CSRRequestDto handleCSR(@RequestPayload CSRRequestDto dto ) {
		try {
			PKCS10CertificationRequest csr = new PKCS10CertificationRequest(Base64.decode((String)dto.getRequestString()));
			certificateService = new CertificateService();
			X509Certificate certificate = certificateService.handleCSR(csr);
			
			dto.setRequestString(certificate.getSerialNumber().toString());
			return dto;
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return dto;
	}
	
/*	@ResponsePayload
	//@PayloadRoot(namespace = "http://www.ftn.xml/banke", localPart = "CSRRequestDto")
	@SoapAction("http://localhost:8083/ws/handleSelfSignedCSR")
	public CSRRequestDto handleSelfSignedCSR(@RequestPayload CSRRequestDto dto ) {
		try {
			PKCS10CertificationRequest csr = new PKCS10CertificationRequest(Base64.decode((String)dto.getRequestString()));
			certificateService = new CertificateService();
			X509Certificate certificate = certificateService.handleCSR(csr);
			
			dto.setRequestString(certificate.getSerialNumber().toString());
			return dto;
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return dto;
	}*/
	
	@ResponsePayload
	@PayloadRoot(namespace = "http://www.ftn.xml/banke", localPart = "DownloadRequestDto")
	public DownloadRequestDto sendCertificateFile(@RequestPayload DownloadRequestDto dto) {
		certificateService = new CertificateService();
		DownloadRequestDto response = new DownloadRequestDto();
		byte[] result = certificateService.getCertificateFile((String)dto.getRequestString());
		
		response.setRequestString(Base64.toBase64String(result));
		return response;
		
	
	}
	
	@ResponsePayload
	@PayloadRoot(namespace = "http://www.ftn.xml/banke", localPart = "RevocationRequestDto")
	public RevocationRequestDto revokeCertificate(@RequestPayload RevocationRequestDto dto) {
		certificateService = new CertificateService();
		RevocationRequestDto response = new RevocationRequestDto();
		OCSPResponseStatus status = certificateService.revokeCertificate(dto);
		if(status != null) {
			response.setRequestString(status.toString());
			return response;
		}
		
		return null;
	}
	
	@ResponsePayload
	@PayloadRoot(namespace = "http://www.ftn.xml/banke", localPart = "StatusRequestDto")
	public StatusRequestDto checkCertificate(@RequestPayload StatusRequestDto dto) {
		certificateService = new CertificateService();
		StatusRequestDto response = new StatusRequestDto();
		OCSPResponseStatus status = certificateService.checkCertificate(new BigInteger((String)dto.getRequestString()));
		if(status != null) {
			response.setRequestString(status.toString());
			return response;
		}
		
		return null;
	}
}
