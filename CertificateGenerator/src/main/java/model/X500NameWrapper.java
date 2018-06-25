package model;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

public class X500NameWrapper {

	private String commonName;			
	private String surname;
	private String givenName;
	private String location;	
	private String organizationName;	
	private String country;				
	private String email;

	
	public X500NameWrapper(String commonName, String surname, String givenName, String location,
			String organizationName, String country, String email) {
		super();
		this.commonName = commonName;
		this.surname = surname;
		this.givenName = givenName;
		this.location = location;
		this.organizationName = organizationName;
		this.country = country;
		this.email = email;
	}

	public X500NameWrapper(X500Name x500name) {
		
		RDN commonName = x500name.getRDNs(BCStyle.CN)[0];
		RDN surname = x500name.getRDNs(BCStyle.SURNAME)[0];
		RDN givenName = x500name.getRDNs(BCStyle.GIVENNAME)[0];
		RDN location = x500name.getRDNs(BCStyle.L)[0];
		RDN organizationName = x500name.getRDNs(BCStyle.O)[0];
		RDN country = x500name.getRDNs(BCStyle.C)[0];
		RDN email = x500name.getRDNs(BCStyle.E)[0];
		
		this.commonName	= IETFUtils.valueToString(commonName.getFirst().getValue());
		this.surname = IETFUtils.valueToString(surname.getFirst().getValue());
		this.givenName = IETFUtils.valueToString(givenName.getFirst().getValue()); 
		this.location = IETFUtils.valueToString(location.getFirst().getValue());
		this.organizationName = IETFUtils.valueToString(organizationName.getFirst().getValue()); 
		this.country = IETFUtils.valueToString(country.getFirst().getValue());
		this.email = IETFUtils.valueToString(email.getFirst().getValue()); 
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
