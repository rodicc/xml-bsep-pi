package ftn.xmlwebservisi.firme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.w3c.dom.Document;

import ftn.xmlwebservisi.firme.helpers.Mapper;
import ftn.xmlwebservisi.firme.model.ZahtevZaIzvod;
import ftn.xmlwebservisi.firme.xmlSignatureAndEncryption.XMLSignAndEncryptUtility;
import soap.NalogZaPlacanje;
import soap.PosaljiNalogZaPlacanjeRequest;
import soap.PosaljiZahtevZaIzvodRequest;
import soap.PosaljiZahtevZaIzvodResponse;

public class SoapClient extends WebServiceGatewaySupport {
	
	private XMLSignAndEncryptUtility xmlSignAndEncryptUtility;
	private static final String WS_URI = "http://localhost:8082/ws";
	private final Logger logger = LoggerFactory.getLogger(SoapClient.class);
	
	public PosaljiNalogZaPlacanjeRequest posaljiNalogZaPlacanje(ftn.xmlwebservisi.firme.model.NalogZaPlacanje nalog){
		Mapper maper = new Mapper();
		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		
		NalogZaPlacanje nalogSoap = maper.nalogZaPlacanjeEntityToSoap(nalog);
		PosaljiNalogZaPlacanjeRequest request = new PosaljiNalogZaPlacanjeRequest();
		request.setNalogZaPlacanje(nalogSoap); 
		request.setJwt(xmlSignAndEncryptUtility.getJwtToken());
		
		SoapActionCallback callback = new SoapActionCallback("http://www.ftn.xml/banke/PosaljiNalogZaPlacanjeRequest");
		WebServiceTemplate template = getWebServiceTemplate();  
		JAXBElement<PosaljiNalogZaPlacanjeRequest> jaxbElement = null;
		try {
			//Postavljanje promenjlivih za enkripciju objekta u Source
			jaxbElement = new JAXBElement<PosaljiNalogZaPlacanjeRequest>(new QName(PosaljiNalogZaPlacanjeRequest.class.getSimpleName()),PosaljiNalogZaPlacanjeRequest.class, request);
			JAXBContext requestContext = JAXBContext.newInstance(PosaljiNalogZaPlacanjeRequest.class);
			
			DOMSource source = xmlSignAndEncryptUtility.encryptToSource(jaxbElement, requestContext, "nalogZaPlacanje");
			
			//Postavljanje promenljivih za upisivanje odgovora
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(outStream);
			
			//Slanje zahteva(source) i upisivanje odgovora(result)
			logger.info("Sending NalogZaPlacanje Request to: {}, ObjID={}", WS_URI,  nalog.getIdPoruke());
			template.sendSourceAndReceiveToResult(WS_URI, source, callback, result);
			
			//Dekripcija i provera odgovora 
			Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(new ByteArrayInputStream(outStream.toByteArray()));
			if((decryptedDocument != null) &&(decryptedDocument.getDocumentElement().getLocalName().equals(PosaljiNalogZaPlacanjeRequest.class.getSimpleName()))) {
				JAXBContext resultContext = JAXBContext.newInstance(PosaljiNalogZaPlacanjeRequest.class);
				Unmarshaller unmarshaller = resultContext.createUnmarshaller();
				JAXBElement<PosaljiNalogZaPlacanjeRequest> posaljiNalogZaPlacanjeRequest = unmarshaller.unmarshal(decryptedDocument, PosaljiNalogZaPlacanjeRequest.class );
				
				return posaljiNalogZaPlacanjeRequest.getValue();
			}
			
		} catch (JAXBException e) {
			logger.error("Invalid encryption element: Obj={}", jaxbElement, e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Could not reach: {}", WS_URI, e);
		}
		return null;
	}
	

	public PosaljiZahtevZaIzvodResponse posaljiZahtevZaIzvod(ZahtevZaIzvod zahtev){
		Mapper mapper = new Mapper();
		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		
		soap.ZahtevZaIzvod zahtevZaIzvodSoap = mapper.zahtevZaIzvodEntityToSoap(zahtev);
		PosaljiZahtevZaIzvodRequest request = new PosaljiZahtevZaIzvodRequest();
		request.setZahtevZaIzvod(zahtevZaIzvodSoap);
		request.setJwt(xmlSignAndEncryptUtility.getJwtToken());
		
		SoapActionCallback callback = new SoapActionCallback("http://www.ftn.xml/banke/PosaljiZahtevZaIzvodRequest");
		WebServiceTemplate template = getWebServiceTemplate();
		JAXBElement<PosaljiZahtevZaIzvodRequest> jaxbElement = null;
		try {
			//Postavljanje promenjlivih za enkripciju objekta u Source
			jaxbElement = new JAXBElement<PosaljiZahtevZaIzvodRequest>(new QName(PosaljiZahtevZaIzvodRequest.class.getSimpleName()),PosaljiZahtevZaIzvodRequest.class, request);
			JAXBContext requestContext = JAXBContext.newInstance(PosaljiZahtevZaIzvodRequest.class);
			
			DOMSource source = xmlSignAndEncryptUtility.encryptToSource(jaxbElement, requestContext, "zahtevZaIzvod");
			
			//Postavljanje promenljivih za upisivanje odgovora
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(outStream);
			
			//Slanje zahteva(source) i upisivanje odgovora(result)
			logger.info("Sending ZahtevZaIzvod Request to: {}, Obj={}", WS_URI,  zahtev);
			template.sendSourceAndReceiveToResult(WS_URI, source, callback, result);
			
			//Dekripcija i provera odgovora 
			Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(new ByteArrayInputStream(outStream.toByteArray()));
			if((decryptedDocument != null) &&(decryptedDocument.getDocumentElement().getLocalName().equals(PosaljiZahtevZaIzvodResponse.class.getSimpleName()))) {
				JAXBContext resultContext = JAXBContext.newInstance(PosaljiZahtevZaIzvodResponse.class);
				Unmarshaller unmarshaller = resultContext.createUnmarshaller();
				JAXBElement<PosaljiZahtevZaIzvodResponse> posaljiZahtevZaIzvodResponse = unmarshaller.unmarshal(decryptedDocument, PosaljiZahtevZaIzvodResponse.class );
				
				return posaljiZahtevZaIzvodResponse.getValue();
			}
			
		} catch (JAXBException e) {
			logger.error("Invalid encryption element: Obj={}", jaxbElement, e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Could not reach: {}", WS_URI, e);
		}
		return null;
	}
	
	
}	
