//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.25 at 06:49:50 PM CEST 
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
 *         &lt;element name="RequestString" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
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
    "requestString"
})
@XmlRootElement(name = "RevocationRequestDto")
public class RevocationRequestDto {

    @XmlElement(name = "RequestString", required = true)
    protected Object requestString;

    /**
     * Gets the value of the requestString property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRequestString() {
        return requestString;
    }

    /**
     * Sets the value of the requestString property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRequestString(Object value) {
        this.requestString = value;
    }

}
