package model.certificateGenerator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", namespace = "", propOrder = {
    "csrRequestString"
})
@XmlRootElement(name = "CSRRequestDto")
public class CSRRequestDto {
	
	 @XmlElement(name = "csrRequestString", required = true)
	    protected String csrRequestDto;

	   
	    public String getCsrRequestDto() {
	        return csrRequestDto;
	    }

	   
	    public void setCsrRequestDto(String value) {
	        this.csrRequestDto = value;
	    }
}
