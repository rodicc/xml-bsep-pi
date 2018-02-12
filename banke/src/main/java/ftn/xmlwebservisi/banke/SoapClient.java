package ftn.xmlwebservisi.banke;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import soap.MT102;
import soap.MT102Response;
import soap.MT103;
import soap.MT103Response;
import soap.SendMT102Request;
import soap.SendMT102Response;
import soap.SendMT103Request;
import soap.SendMT103Response;

public class SoapClient extends WebServiceGatewaySupport {

	public MT103Response sendMT103(MT103 mt103) {
		SendMT103Request request = new SendMT103Request();
		request.setMT103(mt103);
		SendMT103Response response = (SendMT103Response)getWebServiceTemplate().marshalSendAndReceive("http://localhost:8083/ws", request,
				new SoapActionCallback("http://www.ftn.xml/centralnabanka/sendMT103Request"));
		return response.getMT103Response();
	}
	
	public MT102Response sendMT102(MT102 mt102) {
		SendMT102Request request = new SendMT102Request();
		request.setMT102(mt102);
		SendMT102Response response = (SendMT102Response)getWebServiceTemplate().marshalSendAndReceive("http://localhost:8083/ws", request,
				new SoapActionCallback("http://www.ftn.xml/centralnabanka/sendMT102Request"));
		return response.getMT102Response();
	}
}
