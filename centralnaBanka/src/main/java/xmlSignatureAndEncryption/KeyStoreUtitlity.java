package xmlSignatureAndEncryption;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class KeyStoreUtitlity {
	
	private KeyStore keyStore;
	public static final String KEY_STORE_PATH = "./certificates/BANKE.jks";
	public static final String KEY_STORE_PASSWORD = "123456";
	public static final String CERTIFICATE_ALIAS = "CBANKA";
	public static final String PRIVATE_KEY_ALIAS = "CBANKA_KEY";
	public static final String PRIVATE_KEY_PASSWORD = "123456";
	
	public KeyStoreUtitlity() {
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
		BufferedInputStream in = null;
		try {
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, keyStorePass.toCharArray());
				
			if(keyStore.isKeyEntry(alias)) {
				Certificate cert = keyStore.getCertificate(alias);
				return cert;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in !=null) {
		        try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		return null;
	}
	
	public Certificate readDefaultCertificate() {
		BufferedInputStream in = null;
		try {
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			in = new BufferedInputStream(new FileInputStream(KEY_STORE_PATH));
			keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());
				
			if(keyStore.isKeyEntry(CERTIFICATE_ALIAS)) {
				Certificate cert = keyStore.getCertificate(CERTIFICATE_ALIAS);
				return cert;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in !=null) {
		        try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		return null;
	}
		
	public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
		BufferedInputStream in = null;
		try {
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, keyStorePass.toCharArray());
				
			if(keyStore.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) keyStore.getKey(alias, pass.toCharArray());
				return pk;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} finally {
			if (in !=null) {
		        try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		return null;
	}
	
	public PrivateKey readDefaultPrivateKey() {
		BufferedInputStream in = null;
		try {
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			in = new BufferedInputStream(new FileInputStream(KEY_STORE_PATH));
			keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());
				
			if(keyStore.isKeyEntry(PRIVATE_KEY_ALIAS)) {
				PrivateKey pk = (PrivateKey) keyStore.getKey(PRIVATE_KEY_ALIAS, PRIVATE_KEY_PASSWORD.toCharArray());
				return pk;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} finally {
			if (in !=null) {
		        try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		return null;
	}
	
	public void loadKeyStore(String keyStorePath, String keyStorePassword) {
		BufferedInputStream in = null;
		try {
			if(keyStore == null) {
				keyStore = KeyStore.getInstance("BKS", "BC"); 
			}
			in = new BufferedInputStream(new FileInputStream(keyStorePath));
			if(keyStorePath != null) {
				
				keyStore.load(in, keyStorePassword.toCharArray());
			} else {
				keyStore.load(null, keyStorePassword.toCharArray());
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} finally {
			if (in !=null) {
		        try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
	}
	
	public void saveKeyStore(String fileName, char[] password) {
		try {
			keyStore.store(new FileOutputStream(fileName), password);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeKeyEntry(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
		try {
			keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}
}
