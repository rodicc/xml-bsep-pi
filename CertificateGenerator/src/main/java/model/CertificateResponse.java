package model;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

public class CertificateResponse {
	
	
	private String certificateString;
	private Date startDate;
	private Date expirationDate;
	private X500NameWrapper issuer;
	private X500NameWrapper subject;
	
	
	public CertificateResponse(String certificateString, Date startDate, Date expirationDate, X500NameWrapper issuer,
			X500NameWrapper subject) {
		super();
		this.certificateString = certificateString;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.issuer = issuer;
		this.subject = subject;
	}
	
	public CertificateResponse(Certificate certificate) throws CertificateEncodingException {
		
		this.certificateString = certificate.toString(); System.out.println(certificate.toString());
		
		this.startDate = ((X509Certificate) certificate).getNotBefore();
		this.expirationDate = ((X509Certificate) certificate).getNotAfter();
		
		X500Name x500name;
		x500name = new JcaX509CertificateHolder((X509Certificate) certificate).getIssuer();
		this.issuer = new X500NameWrapper(x500name);
		
		x500name = new JcaX509CertificateHolder((X509Certificate) certificate).getSubject();
		this.subject = new X500NameWrapper(x500name);	
	}
	


	public String getCertificateString() {
		return certificateString;
	}


	public void setCertificateString(String certificateString) {
		this.certificateString = certificateString;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}


	public X500NameWrapper getIssuer() {
		return issuer;
	}


	public void setIssuer(X500NameWrapper issuer) {
		this.issuer = issuer;
	}


	public X500NameWrapper getSubject() {
		return subject;
	}


	public void setSubject(X500NameWrapper subject) {
		this.subject = subject;
	}
	
	
	
}
