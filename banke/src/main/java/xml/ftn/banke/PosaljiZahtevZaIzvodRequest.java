//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.07.08 at 12:40:07 AM CEST 
//


package xml.ftn.banke;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="zahtev_za_izvod" type="{http://www.ftn.xml/banke}zahtev_za_izvod"/>
 *         &lt;element name="jwt" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "zahtevZaIzvod",
    "jwt"
})
@XmlRootElement(name = "posaljiZahtevZaIzvodRequest")
public class PosaljiZahtevZaIzvodRequest {

    @XmlElement(name = "zahtev_za_izvod", required = true)
    protected ZahtevZaIzvod zahtevZaIzvod;
    @XmlElement(required = true)
    protected Object jwt;

    /**
     * Gets the value of the zahtevZaIzvod property.
     * 
     * @return
     *     possible object is
     *     {@link ZahtevZaIzvod }
     *     
     */
    public ZahtevZaIzvod getZahtevZaIzvod() {
        return zahtevZaIzvod;
    }

    /**
     * Sets the value of the zahtevZaIzvod property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZahtevZaIzvod }
     *     
     */
    public void setZahtevZaIzvod(ZahtevZaIzvod value) {
        this.zahtevZaIzvod = value;
    }

    /**
     * Gets the value of the jwt property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getJwt() {
        return jwt;
    }

    /**
     * Sets the value of the jwt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setJwt(Object value) {
        this.jwt = value;
    }

}
