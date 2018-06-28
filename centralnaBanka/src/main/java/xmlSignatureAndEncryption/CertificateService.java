package xmlSignatureAndEncryption;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import model.certificateGenerator.CertificateDto;
import model.certificateGenerator.OCSPResponseStatus;
import xml.ftn.banke.RevocationRequestDto;

@Service
public class CertificateService {
	
	//private static final String KEY_STORE_ALIAS = "tomcat";
	private static final String KEY_STORE_PASSWORD = "123456";
	private static final String KEY_STORE_PATH = "./certificates/BANKE.jks";
	private static final String CRL_PATH = "./certificates/revoked_certificates.crl.";
	private static final String CERTIFICATE_ALIAS = "CBANKA";
	private static final String PRIVATE_KEY_ALIAS = "CBANKA_KEY";
	private static final String PRIVATE_KEY_PASSWORD = "123456";
	private final Logger logger = LoggerFactory.getLogger(CertificateService.class);
	
	private KeyStore keyStore;
	
	public CertificateService() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

	}
	
	public X509Certificate generateCertificate(X500Name subject, PublicKey subjectPublicKey, X509Certificate issuerCertificate, PrivateKey issuerPrivateKey) {
		try {
			//Generisanje seriskog broja i datuma vazenja sertifikata
			BigInteger serialNumber = new BigInteger(16, new SecureRandom());
		    Date startDate = new Date();
		    Calendar endDate = Calendar.getInstance(); endDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) + 1);
			Date  expDate = new Date(endDate.getTimeInMillis());
			
			//Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption"); builder.setProvider("BC");
			ContentSigner contentSigner = builder.build(issuerPrivateKey);
			X500Name issuer = new JcaX509CertificateHolder((X509Certificate) issuerCertificate).getIssuer();
			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
					issuer,
					serialNumber,
					startDate,
					expDate,
					subject,
					subjectPublicKey);	
			//Generisanje sertifikata
			X509CertificateHolder certHolder = certGen.build(contentSigner);
			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");
			X509Certificate certificate = certConverter.getCertificate(certHolder);
			
			//Dodavanje novog sertifikata u trustStore
			keyStore.setCertificateEntry(certificate.getSubjectX500Principal().getName(), certificate);
			keyStore.store(new FileOutputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			
			return certificate;
			
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		} catch (OperatorCreationException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public X509Certificate handleCSR(PKCS10CertificationRequest csr) {
		try {
			//Ucitavanje sopstvenog sertifikata radi potpisivanja novoizdatog
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			X509Certificate issuerCertificate = (X509Certificate) keyStore.getCertificate(CERTIFICATE_ALIAS);
			PrivateKey issuerPrivateKey = (PrivateKey) keyStore.getKey(PRIVATE_KEY_ALIAS, KEY_STORE_PASSWORD.toCharArray());
		
			if((issuerCertificate == null) || (issuerPrivateKey == null)) {
				return null;
			}
			//Izvlacenje javnog kljuca iz CSR-a i generisanje sertifikata
			KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(csr.getSubjectPublicKeyInfo().parsePublicKey().getEncoded()));
			X500Name subject = csr.getSubject();
			X509Certificate certificate = generateCertificate(subject, publicKey, issuerCertificate, issuerPrivateKey);
			
			
			return certificate;
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			logger.error("Invalid CA certificate file", e);
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] getCertificateFile(String serialNumber) {
		try {
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			
			keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			
			if(checkCertificate(new BigInteger(serialNumber)) == OCSPResponseStatus.REVOKED) {
				return null;
			}
			Certificate certificate = keyStore.getCertificate(serialNumber);
			
			if(certificate == null) {
				return null;
			}
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			
			outStream.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
			outStream.write(Base64.encodeBase64(certificate.getEncoded(), true));
			outStream.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
			outStream.close();
			
			return outStream.toByteArray();
			
		} catch (KeyStoreException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchProviderException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} catch (CertificateException e) {
			logger.error("Invalid certificate file with serial number: Obj={}",serialNumber, e);
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("Key store file not found", e);
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	//TODO: da izbaci povucene sertifikate
	public List<String> getAllCACertificates(){
		
		try {
			ArrayList<String> result = new ArrayList<>();
			
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			
			keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			
			Enumeration<String> enumeration = keyStore.aliases();
			
			while(enumeration.hasMoreElements()) {
				String alias = (String)enumeration.nextElement();
			    X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias); 
			    if(keyStore.isCertificateEntry(alias))
			    	if(certificate.getBasicConstraints() != -1) {
			    		result.add(alias);
			    	}
			}
			
			return result;
			
		}  catch (KeyStoreException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchProviderException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} catch (CertificateException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("Key store file not found", e);
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
		
	}
	
	public boolean checkIfInIssuedList(BigInteger serialNumber){
			
			try {
				
				if(keyStore == null) {
					keyStore = KeyStore.getInstance("BKS", "BC"); 
				}
				
				keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
				
				Enumeration<String> enumeration = keyStore.aliases();
				
				while(enumeration.hasMoreElements()) {
					String alias = (String)enumeration.nextElement();
				    X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias); 
				    System.out.println("Cert S/N: "+ certificate.getSerialNumber().toString());
				    System.out.println("S/N: "+ serialNumber.toString());
				    if(certificate.getSerialNumber().equals(serialNumber)) {
				    	return true;
				    }
				    	
				}
				
			}  catch (KeyStoreException e) {
				System.out.println(e.getMessage());
			} catch (NoSuchProviderException e) {
				System.out.println(e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				System.out.println(e.getMessage());
			} catch (CertificateException e) {
				System.out.println(e.getMessage());
			} catch (FileNotFoundException e) {
				logger.error("Key store file not found", e);
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			
			return false;
			
		}
	
	
	public OCSPResponseStatus revokeCertificate(RevocationRequestDto revokeCertificateDto) {
		
		try {
			//Ucitava sertifikat
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			//ucitavanje sertifikata issuer-a
			keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			X509Certificate certificate = (X509Certificate) keyStore.getCertificate(CERTIFICATE_ALIAS);
			if(certificate == null) {
				return null;
			}
			
			//Ucitava CRL fajl
			X509CRLHolder crlHolder = new X509CRLHolder(new FileInputStream(CRL_PATH));
			X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(new JcaX509CertificateHolder((X509Certificate) certificate).getIssuer(), new Date());
			crlBuilder.addCRL(crlHolder);
			
			//Upisuje serijski broj sertifikata u CRL 
			BigInteger serialNumber = new BigInteger((String)revokeCertificateDto.getRequestString());
			if(checkIfInIssuedList(serialNumber) == false) {
				System.out.println("not in cert list");
				return OCSPResponseStatus.UNKNOWN;
			}
			
			crlBuilder.addCRLEntry(serialNumber, new Date(), CRLReason.unspecified);
			
			//Ucitava privatni kljuc od CA iz keyStore-a 
			KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(PRIVATE_KEY_PASSWORD.toCharArray());
			
			KeyStore.PrivateKeyEntry caPrivateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(PRIVATE_KEY_ALIAS, protParam);
		    PrivateKey caPrivateKey = caPrivateKeyEntry.getPrivateKey();
		    
		    //Formira se objekat koji ce sadrzati privatni kljuc CA sa kojim potpisuje CRL fajl
		    JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption"); builder.setProvider("BC");
			ContentSigner contentSigner = builder.build(caPrivateKey);
			crlHolder = crlBuilder.build(contentSigner);
			try(FileOutputStream fos = new FileOutputStream(CRL_PATH)){
				fos.write(crlHolder.getEncoded());
			}
			return OCSPResponseStatus.REVOKED;
			
		} catch (KeyStoreException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchProviderException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} catch (CertificateException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("CRL file not found", e);
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (UnrecoverableEntryException e) {
			e.printStackTrace();
		} catch (OperatorCreationException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	//TODO: treba da prima serial number
	public OCSPResponseStatus checkCertificate(BigInteger serialNumber) {
			if(checkIfInIssuedList(serialNumber) == false) {
				return OCSPResponseStatus.UNKNOWN;
			}
		
		 try {
			 //Ucita CRL fajl
		     CertificateFactory cf = CertificateFactory.getInstance("X.509");
		     X509CRL crl = (X509CRL)cf.generateCRL(new FileInputStream(CRL_PATH));
		     X509CRLEntry entry = crl.getRevokedCertificate(serialNumber);
		     
		     if(entry == null) {
		    	 return OCSPResponseStatus.GOOD;
		     }
		     else {
		     	return OCSPResponseStatus.REVOKED;
		     }
		     
		} catch (FileNotFoundException e) {
			logger.error("CRL file not found", e);
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (CertificateException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (CRLException e) {
			
			e.printStackTrace();
		}
		 
		return null;
	}
	
	private KeyPair generateKeyPair() {
        try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	private X500Name generateX500Name(CertificateDto certificate) {
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
	    builder.addRDN(BCStyle.CN, certificate.getCommonName());
	    builder.addRDN(BCStyle.SURNAME, certificate.getSurname());
	    builder.addRDN(BCStyle.GIVENNAME, certificate.getGivenName());
	    builder.addRDN(BCStyle.O, certificate.getOrganisation());
	    builder.addRDN(BCStyle.C, certificate.getCountry());
	    builder.addRDN(BCStyle.E,certificate.getEmail());
	    String uid = UUID.randomUUID().toString();
	    builder.addRDN(BCStyle.UID, uid);

		return builder.build();
	}

	
	
	
	
}
