package ftn.xmlwebservisi.firme.xmlSignatureAndEncryption;

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
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

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
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
@SuppressWarnings("rawtypes") 

public class XMLSignAndEncryptUtility {

    public XMLSignAndEncryptUtility() {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public DOMSource encryptToSource(JAXBElement jaxbElement, JAXBContext jaxbContext) {
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(jaxbElement, byteOutStream);
			ByteArrayInputStream inStream = new ByteArrayInputStream(byteOutStream.toByteArray());
			inStream.reset();
			//Enkripcija i potpisivanje dokument
			Document document = encryptAndSign(inStream);
			
			
			DOMSource source = new DOMSource(document); 
			 
			return source;
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
    
    public Document encryptAndSign(ByteArrayInputStream inStream) throws CertificateException, FileNotFoundException {
    	
    	Document document = loadDocument(inStream);
    	SecretKey secretKey = generateDataEncryptionKey();
    	
    	KeyStoreUtitlity ksUtility = new KeyStoreUtitlity();
    	PrivateKey senderPrivateKey = ksUtility.readDefaultPrivateKey();
    	
    	CertificateFactory cf = CertificateFactory.getInstance("X509");
    	Certificate senderCertificate = cf.generateCertificate(new FileInputStream("./certificates/FIRMA.cer"));
    	Certificate recieverCertificate = cf.generateCertificate(new FileInputStream("./certificates/BANKA.cer"));
    	
    	document = encrypt(document, null, secretKey, recieverCertificate.getPublicKey());
    	document = signDocument(document, senderPrivateKey, senderCertificate);
    
    	return document;
    }
    
    public Document veryfyAndDecrypt(InputStream inStream) {
    	
    	Document encryptedDocument = loadDocument(inStream);
    	if(verifySignature(encryptedDocument)) {
    		KeyStoreUtitlity ksUtility = new KeyStoreUtitlity();
    		PrivateKey recieverPrivateKey = ksUtility.readDefaultPrivateKey();
    		Document decrypredDocument = decrypt(encryptedDocument, recieverPrivateKey);
    		return decrypredDocument;
    	}
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
		try {
			//Pronalazi se prvi Signature element 
			NodeList signatures = document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
			Element signatureEl = (Element) signatures.item(0);
			
			//Kreira se signature objekat od elementa
			XMLSignature signature = new XMLSignature(signatureEl, null);
			//Preuzima se key info
			KeyInfo keyInfo = signature.getKeyInfo();
			if(keyInfo != null) {
				//Registruju se resolver-i za javni kljuc i sertifikat
				keyInfo.registerInternalKeyResolver(new RSAKeyValueResolver());
			    keyInfo.registerInternalKeyResolver(new X509CertificateResolver());
			    
			    //Ako postoji sertifikat, provera potpisa
			    if(keyInfo.containsX509Data() && keyInfo.itemX509Data(0).containsCertificate()) { 
			        Certificate cert = keyInfo.itemX509Data(0).itemCertificate(0).getX509Certificate();
			        if(cert != null) 
			        	return signature.checkSignatureValue((X509Certificate) cert);
			    }
			}
		} catch (XMLSignatureException e) {
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
				NodeList elementsToEncrypt = document.getElementsByTagName(elementToEncrypt);
				for(int i=0; i < elementsToEncrypt.getLength(); i++) {
					xmlContentCipher.doFinal(document, ((Element) elementsToEncrypt.item(i)), true);
				}
	        }
	        
			return document;
			
		} catch (XMLEncryptionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Document decrypt(Document document, PrivateKey privateKey) {
		try {
			//Inicijalizacija za desifrovanje
			XMLCipher xmlContentCipher = XMLCipher.getInstance();
			xmlContentCipher.init(XMLCipher.DECRYPT_MODE, null);
			//Postavlja se kljuc za desifrovanje tajnog kljuca
			xmlContentCipher.setKEK(privateKey);
			
			
			xmlContentCipher.doFinal(document, (Element)document.getFirstChild());
			
			//Desifruju se sva pojavljivanja EncryptedData elementa
			NodeList elementsToDecrypt = document.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData");
			for(int i=0; i < elementsToDecrypt.getLength(); i++) {
				xmlContentCipher.doFinal(document, ((Element) elementsToDecrypt.item(i)));
			} 
			
			return document;
			
		} catch (XMLEncryptionException e) {
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
			e.printStackTrace();
		} catch (IOException e) {
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
