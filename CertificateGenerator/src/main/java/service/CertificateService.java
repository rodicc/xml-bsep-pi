package service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
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
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.springframework.stereotype.Service;

import model.CSRDto;
import model.CertificateDto;
import model.OCSPResponseStatus;
import model.RevokeCertificateDto;

@Service
public class CertificateService {
	
	//private static final String KEY_STORE_ALIAS = "tomcat";
	private static final String KEY_STORE_PASSWORD = "123456";
	private static final String KEY_STORE_PATH = "./certificates/BANKE.jks";
	private static final String CRL_PATH = "./certificates/revoked_certificates.crl.";
	
	
	
	private KeyStore keyStore;
	
	public CertificateService() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

	}
	
	public CertificateDto generateNewCertificate(CertificateDto certificate) 
			throws NoSuchAlgorithmException, FileNotFoundException, IOException, KeyStoreException, NoSuchProviderException {
		
		if(certificate.isValid(certificate)) {
			try {
				
				//Ucita keyStore
				if(keyStore == null) {
					keyStore = KeyStore.getInstance("BKS", "BC");
				}
				
				keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
				
				KeyPair keyPair = generateKeyPair();
				X500Name subject = this.generateX500Name(certificate);
				BigInteger serialNumber = new BigInteger(16, new SecureRandom());
			    Date startDate = new Date();
			    
			    Calendar endDate = Calendar.getInstance(); endDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) + 1);
				
				Date  expDate = new Date(endDate.getTimeInMillis());
				
				//Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
				JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption"); builder.setProvider("BC");
				
				ContentSigner contentSigner = builder.build(keyPair.getPrivate());
				
				X509v3CertificateBuilder certGen;
	
				//Postavljaju se podaci za generisanje samopotpisanog sertifiakta
				if(certificate.isSelfSigned()) {
					certGen = new JcaX509v3CertificateBuilder(
							subject,
							serialNumber,
							startDate,
							expDate,
							subject,
							keyPair.getPublic());
				//Postavljaju se podaci za generisanje sertifiakta izdatog od CA
				} else {
					Certificate issuerCertificate = this.keyStore.getCertificate(certificate.getIssuer());
					
					//Provera da li je issuer CA
					if(((X509Certificate) issuerCertificate).getBasicConstraints() == -1) {
						System.out.println("Issuer is not a CA!");
						return null;
					}
					//Provera da li je CA sertifikat vazeci
					try {
						((X509Certificate) issuerCertificate).checkValidity();
					} catch (CertificateExpiredException e) {
						System.out.println("CA certificate expired!");
						e.printStackTrace();
						return null;
					} catch (CertificateNotYetValidException e) {
						System.out.println("CA certificate not yet valid!");
						e.printStackTrace();
						return null;
					}
					
					X500Name issuer = new JcaX509CertificateHolder((X509Certificate) issuerCertificate).getIssuer();
					certGen = new JcaX509v3CertificateBuilder(
							issuer,
							serialNumber,
							startDate,
							expDate,
							subject,
							keyPair.getPublic());	
				}
				
				//Generise se sertifikat
				certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(certificate.isCA()));
				X509CertificateHolder certHolder = certGen.build(contentSigner);
	
				JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
				certConverter = certConverter.setProvider("BC");
				
				//Upisuje privatni kljuc i sertifikat u keyStore 
				keyStore.setKeyEntry(
						certificate.getKeyAlias(),
						keyPair.getPrivate(),
						certificate.getKeyPassword().toCharArray(),
						new Certificate[] { (Certificate) certConverter.getCertificate(certHolder) });
				System.out.println("\nCertificate alias: " + certificate.getCertificateAlias() + 
								   "\nKey alias: " + certificate.getKeyAlias() + 
								   "\nKeyPassword: " + certificate.getKeyPassword()  + "\n");
				keyStore.setCertificateEntry(certificate.getCertificateAlias(), certConverter.getCertificate(certHolder));
				keyStore.store(new FileOutputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
				
				//Ako je samopotpisani sertifikat generise se CRL fajl koji on potpisuje
				if(certificate.isSelfSigned()) {
					X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(new JcaX509CertificateHolder((X509Certificate) certConverter.getCertificate(certHolder)).getIssuer(), new Date());
					X509CRLHolder crlHolder = crlBuilder.build(contentSigner);
					
					try(FileOutputStream fos = new FileOutputStream(CRL_PATH)){
						fos.write(crlHolder.getEncoded());
					}
				}
				
				
				return certificate;
				
			} catch (CertificateEncodingException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (OperatorCreationException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public PKCS10CertificationRequest generateCSR(CSRDto csr) {
		if(csr.isValid()) {
			try {
				//Generisanje para kljuceva i postavljanje parametra za CSR
				KeyPair keyPair = generateKeyPair();
				csr.setPublicKey(keyPair.getPublic());
				SubjectPublicKeyInfo spkInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.id_RSAES_OAEP), ASN1Sequence.getInstance(keyPair.getPublic().getEncoded()));
				PKCS10CertificationRequestBuilder pkcsBuilder = new PKCS10CertificationRequestBuilder(this.generateX500Name(csr.getCertificateDto()),spkInfo);
				
				//Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
				JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption"); csBuilder.setProvider("BC");
				ContentSigner contentSigner = csBuilder.build(keyPair.getPrivate());
				
				//Generise se se objekat koji predstavlja CSR
				PKCS10CertificationRequest pkCSR = null;
				pkCSR = pkcsBuilder.build(contentSigner);
				
				//Generisanje privremenog samopotpisanog sertifikata radi cuvanja  privatnog kjluca u KeyStore-u
				//Ucita keyStore
				if(keyStore == null) {
					keyStore = KeyStore.getInstance("BKS", "BC");
				}
				keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
				
				X500Name subject = this.generateX500Name(csr);
			    Calendar endDate = Calendar.getInstance(); endDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) + 1);
				Date  expDate = new Date(endDate.getTimeInMillis());
				X509v3CertificateBuilder certGen;
	
				//Postavljaju se podaci za generisanje samopotpisanog sertifiakta
				certGen = new JcaX509v3CertificateBuilder(
							subject,
							new BigInteger(16, new SecureRandom()),
							new Date(),
							expDate,
							subject,
							keyPair.getPublic());
				
				//Generise se sertifikat
				certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
				X509CertificateHolder certHolder = certGen.build(contentSigner);
				JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
				certConverter = certConverter.setProvider("BC");
	
				keyStore.setKeyEntry(
						csr.getKeyAlias(),
						keyPair.getPrivate(),
						csr.getKeyPassword().toCharArray(),
						new Certificate[] { (Certificate) certConverter.getCertificate(certHolder) });
				//keyStore.setCertificateEntry(csr.getCertificateAlias(), certConverter.getCertificate(certHolder));
				keyStore.store(new FileOutputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
				
				return pkCSR;

			} catch (IOException e) {
				e.printStackTrace();
			} catch (OperatorCreationException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} 
		}
		return null;
	}

	public String saveCertificate(byte[] encodedCertificate) {
		try {
			//Ucitava KeyStore
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			
			//Rekonstruise sertifikat
			CertificateFactory cf;
			cf = CertificateFactory.getInstance("X509");
			X509Certificate certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(encodedCertificate));
			
			//Upisuje sertifikat u trustStore
			keyStore.setCertificateEntry(certificate.getSerialNumber().toString(),certificate);
			keyStore.store(new FileOutputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			
			return certificate.getSerialNumber().toString();
			
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
/*	public CertificateResponseDto getCertificate(String alias) {
			
			try {
				if(keyStore == null) {
					keyStore = KeyStore.getInstance("BKS", "BC"); 
				}
				
				keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
				
				Certificate certificate = keyStore.getCertificate(alias);
				
				if(certificate == null) {
					return null;
				}
				
				return new CertificateResponseDto(certificate.toString());
				
			} catch (KeyStoreException e) {
				System.out.println(e.getMessage());
			} catch (NoSuchProviderException e) {
				System.out.println(e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				System.out.println(e.getMessage());
			} catch (CertificateException e) {
				System.out.println(e.getMessage());
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			
			return null;
	}*/
	
/*	//TODO: alias-> S/N
	public ByteArrayResource getCertificateFile(String alias) {
		try {
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			
			keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			Certificate certificate = keyStore.getCertificate(alias);
	
			if(certificate == null) {
				return null;
			}
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			
			outStream.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
			outStream.write(Base64.encodeBase64(certificate.getEncoded(), true));
			outStream.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
			outStream.close();
		
			ByteArrayResource resource = new ByteArrayResource(outStream.toByteArray());
			
			return resource;
			
		} catch (KeyStoreException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchProviderException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} catch (CertificateException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}*/
	
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
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
		
	}
	
	public String revokeCertificate(RevokeCertificateDto revokeCertificateDto) {
		
		try {
			//Ucitava sertifikat
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			
			keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			X509Certificate certificate = (X509Certificate) keyStore.getCertificate(revokeCertificateDto.getCertificateAlias());
			
			if(certificate == null) {
				return null;
			}
			
			//Ucitava CRL fajl
			X509CRLHolder crlHolder = new X509CRLHolder(new FileInputStream(CRL_PATH));
			X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(new JcaX509CertificateHolder((X509Certificate) certificate).getIssuer(), new Date());
			crlBuilder.addCRL(crlHolder);
			
			//Upisuje serijski broj sertifikata u CRL 
			if(crlHolder.getRevokedCertificate(certificate.getSerialNumber()) != null) {
				return null;
			}
			crlBuilder.addCRLEntry(certificate.getSerialNumber(), new Date(), CRLReason.unspecified);
			
			//Ucitava privatni kljuc od CA iz keyStore-a 
			KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(revokeCertificateDto.getCaKeyEntryPassword().toCharArray());
			
			KeyStore.PrivateKeyEntry caPrivateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(revokeCertificateDto.getCaKeyEntryAlias(), protParam);
		    PrivateKey caPrivateKey = caPrivateKeyEntry.getPrivateKey();
		    
		    //Formira se objekat koji ce sadrzati privatni kljuc CA sa kojim potpisuje CRL fajl
		    JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption"); builder.setProvider("BC");
			ContentSigner contentSigner = builder.build(caPrivateKey);
			crlHolder = crlBuilder.build(contentSigner);
			
			try(FileOutputStream fos = new FileOutputStream(CRL_PATH)){
				fos.write(crlHolder.getEncoded());
			}
			
			return certificate.toString();
			
		} catch (KeyStoreException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchProviderException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} catch (CertificateException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (UnrecoverableEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperatorCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public OCSPResponseStatus checkCertificate(BigInteger serialNumber) {
		
		 try {
			/* //Ucita sertifikat
			 if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			 }
			
			 keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
					
			 X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
				
			 if(certificate == null) {
				 return null;
			 }*/
			 //Ucita CRL fajl
		     CertificateFactory cf = CertificateFactory.getInstance("X.509");
		     X509CRL crl = (X509CRL)cf.generateCRL(new FileInputStream(CRL_PATH));
		     X509CRLEntry entry = crl.getRevokedCertificate(serialNumber);
		    // X509CRLEntry entry = crl.getRevokedCertificate(certificate);
		     
		     if(entry == null) {
		    	 return OCSPResponseStatus.GOOD;
		     }
		     else {
		     	return OCSPResponseStatus.REVOKED;
		     }
		     
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (CRLException e) {
			e.printStackTrace();
		} /*catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/		
		return null;
	}
	
	private KeyPair generateKeyPair() {
        try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA","BC"); 
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
