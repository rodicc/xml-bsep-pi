package ftn.xmlwebservisi.banke;

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

import soap.MT102;
import soap.MT102Response;
import soap.MT103;
import soap.MT103Response;
import soap.SendMT102Request;
import soap.SendMT102Response;
import soap.SendMT103Request;
import soap.SendMT103Response;
import xmlSignatureAndEncryption.XMLSignAndEncryptUtility;

public class SoapClient extends WebServiceGatewaySupport {

	private XMLSignAndEncryptUtility xmlSignAndEncryptUtility;
	private static final String WS_URI = "http://localhost:8083/ws";
	private final Logger logger = LoggerFactory.getLogger(SoapClient.class);

	
	public MT103Response sendMT103(MT103 mt103) {
		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		SendMT103Request request = new SendMT103Request();
		request.setMT103(mt103);
		request.setJwt(xmlSignAndEncryptUtility.getJwtToken());
		//SendMT103Response response = (SendMT103Response)getWebServiceTemplate().marshalSendAndReceive("http://localhost:8083/ws", request,
		//		new SoapActionCallback("http://www.ftn.xml/centralnabanka/sendMT103Request"));
		SoapActionCallback callback = new SoapActionCallback("http://www.ftn.xml/centralnabanka/sendMT103Request");
		WebServiceTemplate template = getWebServiceTemplate();
		
		try {
			//Postavljanje promenjlivih za enkripciju objekta u Source
			JAXBElement<SendMT103Request> jaxbElement =
					new JAXBElement<SendMT103Request>(new QName(SendMT103Request.class.getSimpleName()),SendMT103Request.class, request);
			JAXBContext requestContext = JAXBContext.newInstance(SendMT103Request.class);
			
			
			DOMSource source = xmlSignAndEncryptUtility.encryptToSource(jaxbElement, requestContext, XMLSignAndEncryptUtility.CENTRALNA_BANKA, "mt103");
			
			//Postavljanje promenljivih za upisivanje odgovora
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(outStream);
			
			//Slanje zahteva(source) i upisivanje odgovora(result)
			logger.info("Sending MT103 Request to: {}, ObjID={}", WS_URI,  mt103.getIdPoruke());
			template.sendSourceAndReceiveToResult(WS_URI, source, callback, result);
			
			//Dekripcija i provera odgovora 
			Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(new ByteArrayInputStream(outStream.toByteArray()));
			if((decryptedDocument != null) &&(decryptedDocument.getDocumentElement().getLocalName().equals(SendMT103Response.class.getSimpleName()))) {
				JAXBContext resultContext = JAXBContext.newInstance(SendMT103Response.class);
				Unmarshaller unmarshaller = resultContext.createUnmarshaller();
				JAXBElement<SendMT103Response> sendMT103Response = unmarshaller.unmarshal(decryptedDocument, SendMT103Response.class );
				
				 SendMT103Response response = (SendMT103Response) sendMT103Response.getValue();
				 return response.getMT103Response();
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Could not reach: {}", WS_URI, e);
		}
		return null;	
	}
	
	public MT102Response sendMT102(MT102 mt102) {
		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		SendMT102Request request = new SendMT102Request();
		request.setMT102(mt102);
		request.setJwt(xmlSignAndEncryptUtility.getJwtToken());
		//SendMT102Response response = (SendMT102Response)getWebServiceTemplate().marshalSendAndReceive("http://localhost:8083/ws", request,
		//		new SoapActionCallback("http://www.ftn.xml/centralnabanka/sendMT102Request"));
		SoapActionCallback callback = new SoapActionCallback("http://www.ftn.xml/centralnabanka/sendMT102Request");
		WebServiceTemplate template = getWebServiceTemplate();
		
		try {
			//Postavljanje promenjlivih za enkripciju objekta u Source
			JAXBElement<SendMT102Request> jaxbElement =
					new JAXBElement<SendMT102Request>(new QName(SendMT102Request.class.getSimpleName()),SendMT102Request.class, request);
			JAXBContext requestContext = JAXBContext.newInstance(SendMT102Request.class);
			
			
			DOMSource source = xmlSignAndEncryptUtility.encryptToSource(jaxbElement, requestContext, XMLSignAndEncryptUtility.CENTRALNA_BANKA, "mt102");
			
			//Postavljanje promenljivih za upisivanje odgovora
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(outStream);
			
			//Slanje zahteva(source) i upisivanje odgovora(result)
			logger.info("Sending ZahtevZaIzvod Request to: {}, Obj={}", WS_URI,  mt102.getZaglavljeMT102().getIdPoruke());
			template.sendSourceAndReceiveToResult(WS_URI, source, callback, result);
			
			//Dekripcija i provera odgovora 
			Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(new ByteArrayInputStream(outStream.toByteArray()));
			if((decryptedDocument != null) &&(decryptedDocument.getDocumentElement().getLocalName().equals(SendMT102Response.class.getSimpleName()))) {
				JAXBContext resultContext = JAXBContext.newInstance(SendMT102Response.class);
				Unmarshaller unmarshaller = resultContext.createUnmarshaller();
				JAXBElement<SendMT102Response> sendMT102Response = unmarshaller.unmarshal(decryptedDocument, SendMT102Response.class );
				
				SendMT102Response response = (SendMT102Response) sendMT102Response.getValue();
				return response.getMT102Response();
			}
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Could not reach: {}", WS_URI, e);
		}
		return null;
	}
	
}
