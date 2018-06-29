package service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import model.CertificateDto;

@Service
public class CertificateService {
	
	
	private static final String KEY_STORE_PASSWORD = "123456";
	private static final String KEY_STORE_PATH = "./certificates/BANKE.jks";
	private final Logger logger = LoggerFactory.getLogger(CertificateService.class);
	private KeyStore keyStore;
	
	public CertificateService() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

	}
	
	public String generateSelfSignedCertificate(CertificateDto certificate, boolean saveToKeystore) 
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
				X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
															subject,
															serialNumber,
															startDate,
															expDate,
															subject,
															keyPair.getPublic());
				//Dodavanje AIA ekstenzije
				AccessDescription caIssuers = new AccessDescription(AccessDescription.id_ad_caIssuers,
				        new GeneralName(GeneralName.uniformResourceIdentifier, new DERIA5String("https://www.certholder.com/certificate.cer")));
				AccessDescription ocsp = new AccessDescription(AccessDescription.id_ad_ocsp,
				        new GeneralName(GeneralName.uniformResourceIdentifier, new DERIA5String("https://www.certholder.com/ocsp")));
				ASN1EncodableVector aia_ASN = new ASN1EncodableVector();
				aia_ASN.add(caIssuers);
				aia_ASN.add(ocsp);
				certGen.addExtension(Extension.authorityInfoAccess, false, new DERSequence(aia_ASN));
				
				//Dodavanje CRL Distribution Points ekstenzije
				DistributionPointName distPointOne = new DistributionPointName(new GeneralNames(
				        new GeneralName(GeneralName.uniformResourceIdentifier,"https://www.certholder.com/revoked_certificates.crl")));
				
				ReasonFlags flag = new ReasonFlags(ReasonFlags.unused);
				DistributionPoint distPoints = new DistributionPoint(distPointOne,flag, null);
				certGen.addExtension(Extension.cRLDistributionPoints, false, distPoints);
				
				//Key Usage
				certGen.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign));
				
				//Extended Key Usage
				KeyPurposeId[] EKU = new KeyPurposeId[1];
				EKU[0] = KeyPurposeId.id_kp_clientAuth;
				certGen.addExtension(Extension.extendedKeyUsage, false, new ExtendedKeyUsage(EKU));
				certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(certificate.isCA()));
				//Generise se sertifikat
				X509CertificateHolder certHolder = certGen.build(contentSigner);
	
				JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
				certConverter = certConverter.setProvider("BC");
				X509Certificate cert = certConverter.getCertificate(certHolder);
				//Upisuje privatni kljuc i sertifikat u keyStore 
				keyStore.setKeyEntry(
						certificate.getKeyAlias(),
						keyPair.getPrivate(),
						certificate.getKeyPassword().toCharArray(),
						new Certificate[] { cert });
				if(saveToKeystore) {
					keyStore.setCertificateEntry(cert.getSerialNumber().toString(), cert);
				}
				keyStore.store(new FileOutputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
				
				return cert.getSerialNumber().toString();
				
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

	public PKCS10CertificationRequest generateCSR(CertificateDto csr) {
		if(csr.isValid(csr)) {
			try {
				//Generisanje para kljuceva i postavljanje parametra za CSR
				KeyPair keyPair = generateKeyPair();
				
				X500Name subject = this.generateX500Name(csr);
				SubjectPublicKeyInfo spkInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.id_RSAES_OAEP), ASN1Sequence.getInstance(keyPair.getPublic().getEncoded()));
				PKCS10CertificationRequestBuilder pkcsBuilder = new PKCS10CertificationRequestBuilder(subject,spkInfo);
				pkcsBuilder.addAttribute(Extension.basicConstraints, new BasicConstraints(csr.isCA()));
				
				//Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
				JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption"); csBuilder.setProvider("BC");
				ContentSigner contentSigner = csBuilder.build(keyPair.getPrivate());
				
				//Generise se se objekat koji predstavlja CSR
				PKCS10CertificationRequest pkCSR = null;
				pkCSR = pkcsBuilder.build(contentSigner);
				
				//Generisanje privremenog samopotpisanog sertifikata radi cuvanja  privatnog kjluca u KeyStore-u
				this.generateSelfSignedCertificate(csr, false);
				
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
			} 
		}
		return null;
	}
	
	public byte[] getCertificateFile(String serialNumber) {
		try {
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			
			keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray());
			

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
