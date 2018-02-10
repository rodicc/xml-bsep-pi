package ftn.xmlwebservisi.banke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import soap.SendMT103Request;
import soap.SendMT103Response;

public class SoapClient extends WebServiceGatewaySupport {

	public void sendMT103() {
		SendMT103Request request = new SendMT103Request();
		SendMT103Response response = (SendMT103Response)getWebServiceTemplate().marshalSendAndReceive("http://localhost:8083/ws", request,
				new SoapActionCallback("http://localhost:8083/ws/sendMT103"));
		System.out.println(response);
	}
}
