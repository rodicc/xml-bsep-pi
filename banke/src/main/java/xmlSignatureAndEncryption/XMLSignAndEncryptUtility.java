package xmlSignatureAndEncryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.keyresolver.implementations.RSAKeyValueResolver;
import org.apache.xml.security.keys.keyresolver.implementations.X509CertificateResolver;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.jsonwebtoken.Claims;
import model.certificateGenerator.OCSPResponseStatus;
@SuppressWarnings("rawtypes")

@Service
public class XMLSignAndEncryptUtility {
	
	public static final String CENTRALNA_BANKA = "CBANKA";
	public static final String BANKA = "BANKA";
	public static final String FIRMA = "FIRMA";
	
	private final Logger logger = LoggerFactory.getLogger(XMLSignAndEncryptUtility.class);
	private List<String> jwtIDs	;
	
    public XMLSignAndEncryptUtility() {
        Security.addProvider(new BouncyCastleProvider());
        jwtIDs = new ArrayList<>();
       // org.apache.xml.security.Init.init();
    }
    
    
	public DOMSource encryptToSource(JAXBElement jaxbElement, JAXBContext jaxbContext, String recieverCertificateAlias, String elementName) {
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(jaxbElement, byteOutStream);
			ByteArrayInputStream inStream = new ByteArrayInputStream(byteOutStream.toByteArray());
			inStream.reset();
			//Enkripcija i potpisivanje dokument
			Document document = encryptAndSign(inStream, recieverCertificateAlias, elementName);
			
			DOMSource source = new DOMSource(document); 
			
			return source;
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
    
    public Document encryptAndSign(ByteArrayInputStream inStream, String recieverCertificateAlias, String elementName){
    	
    	Document document = loadDocument(inStream);
    	SecretKey secretKey = generateDataEncryptionKey();
    	
    	KeyStoreUtitlity ksUtility = new KeyStoreUtitlity();
    	PrivateKey senderPrivateKey = ksUtility.readDefaultPrivateKey();
    	
    	CertificateFactory cf;
		try {
			cf = CertificateFactory.getInstance("X509");
			Certificate senderCertificate = cf.generateCertificate(new FileInputStream("./certificates/BANKA.cer"));
	    	Certificate recieverCertificate = cf.generateCertificate(new FileInputStream("./certificates/"+recieverCertificateAlias +".cer"));
	    	
	    	document = encrypt(document, elementName, secretKey, recieverCertificate.getPublicKey());
	    	document = signDocument(document, senderPrivateKey, senderCertificate);
	    
	    	return document;
		} catch (CertificateException e) {
			logger.error("Invalid certificate: Obj={}", e.getCause(), e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.error("Cerrtificate file could not be found: Obj:{}", e.getCause(), e);
			e.printStackTrace();
		}
    	return null;
    }
    
    public String getJwtToken() {
		TokenService tokenService = new TokenService();
		KeyStoreUtitlity keyStoreUtitlity = new KeyStoreUtitlity();
		PrivateKey privateKey = keyStoreUtitlity.readDefaultPrivateKey();
		X509Certificate certificate = (X509Certificate)keyStoreUtitlity.readDefaultCertificate();
		return tokenService.generateToken(certificate.getIssuerX500Principal().getName(), privateKey);
    }
    
    public boolean isJwtValid(String token) {
    	try {
    		CertificateFactory cf = CertificateFactory.getInstance("X509");
			Certificate certificate = cf.generateCertificate(new FileInputStream("./certificates/FIRMA.cer"));
			PublicKey publicKey = certificate.getPublicKey();
			
			TokenService tokenService = new TokenService();
	    	Claims claims = tokenService.getClaims(token, publicKey);
	    	if(claims != null) {
		    	if (new Date(System.currentTimeMillis()).before(claims.getExpiration())) {
		    		if(!jwtIDs.contains(claims.getId())) {
		    			jwtIDs.add(claims.getId());
		    			return true;
		    		}
		    	}
	    	}
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    public Document veryfyAndDecrypt(InputStream inStream) {
    	Document encryptedDocument = loadDocument(inStream);
    	//Provera jwt-a
    	Element e = encryptedDocument.getDocumentElement();
    	NodeList nodes = e.getElementsByTagNameNS("http://www.ftn.xml/banke", "jwt");
    	String jwt = nodes.item(0).getTextContent();
    	if(!isJwtValid(jwt)) {
    		logger.error("Invalid request token Obj={}", jwt);
    		return null;
    	}
    	//Provera sadrzaja dokumenta
    	if(verifySignature(encryptedDocument)) {
    		KeyStoreUtitlity ksUtility = new KeyStoreUtitlity();
    		PrivateKey recieverPrivateKey = ksUtility.readDefaultPrivateKey();
    		Document decrypredDocument = decrypt(encryptedDocument, recieverPrivateKey);
    		return decrypredDocument;
    	}
    	logger.error("Invalid request signature");
    	return null;
    }
	
    public Document signDocument(Document document, PrivateKey privateKey, Certificate certificate) {
        try {
			Element rootElement = document.getDocumentElement();
			
			//Kreira se signature objekat
			XMLSignature signature = new XMLSignature(document, null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
			//Kreiraju se transformacije nad dokumentom
			Transforms transforms = new Transforms(document);
			    
			//Iz potpisa uklanja Signature element, sto je potrebno za enveloped tip po specifikaciji
			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
			//Normalizacija
			transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
			    
			//Potpisuje se citav dokument (URI "")
			signature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
			    
			//U KeyInfo se postavalja javni kljuc uz citav sertifikat, za proveru digitalnog potpisa
			signature.addKeyInfo(certificate.getPublicKey());
			signature.addKeyInfo((X509Certificate) certificate);
			    
			//Poptis je child root elementa
			rootElement.appendChild(signature.getElement());
			    
			//Potpisivanje
			signature.sign(privateKey);
			
			return document;
		} catch (TransformationException e) {
			e.printStackTrace();
		} catch (XMLSignatureException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XMLSecurityException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public boolean verifySignature(Document document) {
		XMLSignature signature = null;
		try {
			org.apache.xml.security.Init.init();
			//Pronalazi se prvi Signature element 
			NodeList signatures = document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
			Element signatureEl = (Element) signatures.item(0);
			
			//Kreira se signature objekat od elementa
			signature = new XMLSignature(signatureEl, null);
			//Preuzima se key info
			KeyInfo keyInfo = signature.getKeyInfo();
			if(keyInfo != null) {
				//Registruju se resolver-i za javni kljuc i sertifikat
				keyInfo.registerInternalKeyResolver(new RSAKeyValueResolver());
			    keyInfo.registerInternalKeyResolver(new X509CertificateResolver());
			    
			    //Ako postoji sertifikat, provera potpisa
			    if(keyInfo.containsX509Data() && keyInfo.itemX509Data(0).containsCertificate()) { 
			        Certificate cert = keyInfo.itemX509Data(0).itemCertificate(0).getX509Certificate();
			        if(cert != null) {
			        	CertificateService certificateService = new CertificateService();
			        	try {//Provera da li je sertifikat kojim je potpisan dokument istekao ili je povucen
							((X509Certificate) cert).checkValidity();
							return (certificateService.checkCertificate(((X509Certificate) cert).getSerialNumber()).equals(OCSPResponseStatus.GOOD) &&
							       	signature.checkSignatureValue((X509Certificate) cert));
								
						} catch (CertificateExpiredException | CertificateNotYetValidException e) {
							e.printStackTrace();
						}
			        }
			    }
			}
		} catch (XMLSignatureException e) {
			logger.error("Invalid signature Obj={}, in document Obj={}",signature ,document);
			e.printStackTrace();
		} catch (XMLSecurityException e) {
			e.printStackTrace();
		} 
		return false;
	}
    
	public SecretKey generateDataEncryptionKey() {
        try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede"); //Triple DES
			return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	
	public Document encrypt(Document document, String elementToEncrypt, SecretKey secretKey, PublicKey publicKey) {
		try {
			org.apache.xml.security.Init.init();
		    //Inicijalizacija algoritma za simetricno sifrovanje xml podataka
		    XMLCipher xmlContentCipher = XMLCipher.getInstance(XMLCipher.TRIPLEDES);
		    xmlContentCipher.init(XMLCipher.ENCRYPT_MODE, secretKey);
		    
		    //Inicijalizacija algoritma za asimetricno sifrovanje tajnog kljuca. Sifruje se javnim kljucem primaoca
			XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.RSA_v1dot5);
		    keyCipher.init(XMLCipher.WRAP_MODE, publicKey);
		    EncryptedKey encryptedKey = keyCipher.encryptKey(document, secretKey);
		    
		    //U sadrzaj EncryptedData elementa se dodaje sifrovan tajni kljuc
		    EncryptedData encryptedData = xmlContentCipher.getEncryptedData();
		    KeyInfo keyInfo = new KeyInfo(document);
		    keyInfo.addKeyName("Encrypted SecretKey");
		    keyInfo.add(encryptedKey);
	        encryptedData.setKeyInfo(keyInfo);
			
	        //Ako je prosledjeno ime elementa sifruje sve elemente sa datim imenom, u suprotnom sifruje celokupan dokument
	        if(elementToEncrypt == null) {
	        	xmlContentCipher.doFinal(document, document);
	        }
	        else {
				NodeList elementsToEncrypt = document.getElementsByTagNameNS("http://www.ftn.xml/banke", elementToEncrypt);
				for(int i=0; i < elementsToEncrypt.getLength(); i++) {
					xmlContentCipher.doFinal(document, ((Element) elementsToEncrypt.item(i)), true);
				}
	        }
	        
			return document;
			
		} catch (XMLEncryptionException e) {
			logger.error("Could not encrypt document Obj={}",document, e);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Document decrypt(Document document, PrivateKey privateKey) {
		try {
			org.apache.xml.security.Init.init();
			//Inicijalizacija za desifrovanje
			XMLCipher xmlContentCipher = XMLCipher.getInstance();
			xmlContentCipher.init(XMLCipher.DECRYPT_MODE, null);
			//Postavlja se kljuc za desifrovanje tajnog kljuca
			xmlContentCipher.setKEK(privateKey);
			
			//Desifruju se sva pojavljivanja EncryptedData elementa
			NodeList elementsToDecrypt = document.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData");
			for(int i=0; i < elementsToDecrypt.getLength(); i++) {
				xmlContentCipher.doFinal(document, ((Element) elementsToDecrypt.item(i)));
			} 
			
			return document;
			
		} catch (XMLEncryptionException e) {
			logger.error("Could not decrypt document Obj={}",document, e);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		  
	public Document loadDocument(InputStream inStream) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			//Onemogucava parsiranje eksternih xml elemenata
			dbf.setExpandEntityReferences(false);
			String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
		    dbf.setFeature(FEATURE, true);
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(inStream);

			return document;
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			logger.error("Could not parse XML stream", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Could not parse XML stream", e);
			e.printStackTrace();
		}
		return null;
	}
		
	public void saveDocument(Document doc, String fileName) {
		try {
			File outFile = new File(fileName);
			FileOutputStream f = new FileOutputStream(outFile);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
				
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
				
			transformer.transform(source, result);
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
