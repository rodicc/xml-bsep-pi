package xml.ftn.banke;

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
import xmlSignatureAndEncryption.CertificateService;
import xmlSignatureAndEncryption.XMLSignAndEncryptUtility;

@Endpoint
public class SoapEndpoint {

	//private static final String NAMESPACE_URI = "http://www.ftn.xml/banke";
	@Autowired
	private Servis servis;
	private XMLSignAndEncryptUtility xmlSignAndEncryptUtility;
	private CertificateService certificateService;
	private final Logger logger = LoggerFactory.getLogger(SoapEndpoint.class);

	
	//@PayloadRoot(namespace = NAMESPACE_URI, localPart = "posaljiNalogZaPlacanjeRequest")
	@ResponsePayload
	@SoapAction("http://www.ftn.xml/banke/PosaljiNalogZaPlacanjeRequest") 
	public Source posaljiNalogZaPlacanje(@RequestPayload StreamSource request) {
		//Dekripcija dokumenta
		System.out.println("soap endpoint");
		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		InputStream inStream = request.getInputStream();
		Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(inStream);
		JAXBElement<PosaljiNalogZaPlacanjeRequest> posaljiNalogZaPlacanjeRequest = null;
		try {
			JAXBContext jaxbContext;
			if((decryptedDocument != null) && (decryptedDocument.getDocumentElement().getLocalName().equals(PosaljiNalogZaPlacanjeRequest.class.getSimpleName()))) {
				//Unmarshal dokumenta u objekat
				jaxbContext = JAXBContext.newInstance(PosaljiNalogZaPlacanjeRequest.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				posaljiNalogZaPlacanjeRequest = unmarshaller.unmarshal(decryptedDocument, PosaljiNalogZaPlacanjeRequest.class );
				
				//Regulise se zahtev
				NalogZaPlacanje nalogZaPlacanje = posaljiNalogZaPlacanjeRequest.getValue().getNalogZaPlacanje();
				servis.regulisiNalogZaPlacanje(nalogZaPlacanje);
				
				//Enkripcija odgovora
				xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
				DOMSource source = xmlSignAndEncryptUtility.encryptToSource(posaljiNalogZaPlacanjeRequest, jaxbContext, XMLSignAndEncryptUtility.FIRMA, null);
				
				return source;
			}	
		} catch (JAXBException e) {
			logger.error("Invalid encryption element: Obj={}", posaljiNalogZaPlacanjeRequest, e);
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
	
	//@PayloadRoot(namespace = NAMESPACE_URI, localPart = "posaljiZahtevZaIzvodRequest")
	@ResponsePayload
	@SoapAction("http://www.ftn.xml/banke/PosaljiZahtevZaIzvodRequest") 
	public Source posaljiZahtevZaIzvod(@RequestPayload StreamSource request) {
		//Dekripcija dokumenta
		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		InputStream inStream = request.getInputStream();
		Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(inStream);
		JAXBElement<PosaljiZahtevZaIzvodRequest> posaljiZahtevZaIzvodRequest = null;
		try {
			JAXBContext jaxbContext;
			if((decryptedDocument != null) && (decryptedDocument.getDocumentElement().getLocalName().equals(PosaljiZahtevZaIzvodRequest.class.getSimpleName()))) {
				//Unmarshal dokumenta u objekat
				jaxbContext = JAXBContext.newInstance(PosaljiZahtevZaIzvodRequest.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				posaljiZahtevZaIzvodRequest = unmarshaller.unmarshal(decryptedDocument, PosaljiZahtevZaIzvodRequest.class );
				
				//Regulise se zahtev
				PosaljiZahtevZaIzvodResponse response = new PosaljiZahtevZaIzvodResponse();
				ZahtevZaIzvod zahtevZaIzvod = posaljiZahtevZaIzvodRequest.getValue().getZahtevZaIzvod();
				response.setPresek(servis.regulisiZahtevZaIzvod(zahtevZaIzvod));
				
				//Enkripcija odgovora
				JAXBElement<PosaljiZahtevZaIzvodResponse> jaxbElement =
						new JAXBElement<PosaljiZahtevZaIzvodResponse>(new QName(PosaljiZahtevZaIzvodResponse.class.getSimpleName()),PosaljiZahtevZaIzvodResponse.class, response);
				JAXBContext responseContext = JAXBContext.newInstance(PosaljiZahtevZaIzvodResponse.class);
				
				xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
				DOMSource source = xmlSignAndEncryptUtility.encryptToSource(jaxbElement, responseContext, XMLSignAndEncryptUtility.FIRMA, null);
				
				return source;
			}
			
		} catch (JAXBException e) {
			logger.error("Invalid encryption element: Obj={}", posaljiZahtevZaIzvodRequest, e);
			e.printStackTrace();
			return null;
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
			PKCS10CertificationRequest csr = new PKCS10CertificationRequest(Base64.decode((String)dto.requestString));
			certificateService = new CertificateService();
			X509Certificate certificate = certificateService.handleCSR(csr);
			dto.setRequestString(certificate.getSerialNumber().toString());
			return dto;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return dto;
	}
	
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