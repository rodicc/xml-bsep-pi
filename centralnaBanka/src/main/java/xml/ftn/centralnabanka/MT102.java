//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.25 at 06:49:23 PM CEST 
//


package xml.ftn.centralnabanka;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MT102 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MT102">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="zaglavljeMT102" type="{http://www.ftn.xml/centralnabanka}zaglavljeMT102"/>
 *         &lt;element name="pojedinacnoPlacanjeMT102" type="{http://www.ftn.xml/centralnabanka}pojedinacnoPlacanjeMT102" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MT102", propOrder = {
    "zaglavljeMT102",
    "pojedinacnoPlacanjeMT102"
})
public class MT102 {

    @XmlElement(required = true)
    protected ZaglavljeMT102 zaglavljeMT102;
    @XmlElement(required = true)
    protected List<PojedinacnoPlacanjeMT102> pojedinacnoPlacanjeMT102;

    /**
     * Gets the value of the zaglavljeMT102 property.
     * 
     * @return
     *     possible object is
     *     {@link ZaglavljeMT102 }
     *     
     */
    public ZaglavljeMT102 getZaglavljeMT102() {
        return zaglavljeMT102;
    }

    /**
     * Sets the value of the zaglavljeMT102 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZaglavljeMT102 }
     *     
     */
    public void setZaglavljeMT102(ZaglavljeMT102 value) {
        this.zaglavljeMT102 = value;
    }

    /**
     * Gets the value of the pojedinacnoPlacanjeMT102 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pojedinacnoPlacanjeMT102 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPojedinacnoPlacanjeMT102().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PojedinacnoPlacanjeMT102 }
     * 
     * 
     */
    public List<PojedinacnoPlacanjeMT102> getPojedinacnoPlacanjeMT102() {
        if (pojedinacnoPlacanjeMT102 == null) {
            pojedinacnoPlacanjeMT102 = new ArrayList<PojedinacnoPlacanjeMT102>();
        }
        return this.pojedinacnoPlacanjeMT102;
    }

}
