//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.25 at 06:49:23 PM CEST 
//


package xml.ftn.centralnabanka;

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
 *         &lt;element name="MT103Response" type="{http://www.ftn.xml/centralnabanka}MT103Response"/>
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
    "mt103Response"
})
@XmlRootElement(name = "sendMT103Response")
public class SendMT103Response {

    @XmlElement(name = "MT103Response", required = true)
    protected MT103Response mt103Response;

    /**
     * Gets the value of the mt103Response property.
     * 
     * @return
     *     possible object is
     *     {@link MT103Response }
     *     
     */
    public MT103Response getMT103Response() {
        return mt103Response;
    }

    /**
     * Sets the value of the mt103Response property.
     * 
     * @param value
     *     allowed object is
     *     {@link MT103Response }
     *     
     */
    public void setMT103Response(MT103Response value) {
        this.mt103Response = value;
    }

}
