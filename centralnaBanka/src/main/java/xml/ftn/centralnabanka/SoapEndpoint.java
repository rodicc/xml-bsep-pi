package xml.ftn.centralnabanka;

import xml.ftn.centralnabanka.SendMT102Request;
import xml.ftn.centralnabanka.SendMT102Response;
import xml.ftn.centralnabanka.SendMT103Request;
import xml.ftn.centralnabanka.SendMT103Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import service.Servis;

@Endpoint
public class SoapEndpoint {

	private static final String NAMESPACE_URI = "http://www.ftn.xml/centralnabanka";
	
	@Autowired
	private Servis servis;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendMT103Request")
	@ResponsePayload
	public SendMT103Response sendMT103(@RequestPayload SendMT103Request request) {
		System.out.println("centralna banka");
		SendMT103Response response = new SendMT103Response();
		response.setMT103Response(servis.regulisiMT103(request.getMT103()));
		return response;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendMT102Request")
	@ResponsePayload
	public SendMT102Response sendMT102(@RequestPayload SendMT102Request request) {
		SendMT102Response response = new SendMT102Response();
		response.setMT102Response(servis.regulisiMT102(request.getMT102()));
		return response;
	}
}
