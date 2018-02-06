package org.example.soap;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SoapEndpoint {

	private static final String NAMESPACE_URI = "http://www.example.org/soap";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendMT103Request")
	@ResponsePayload
	public SendMT103Response sendMT103(@RequestPayload SendMT103Request request) {
		SendMT103Response response = new SendMT103Response();
		return response;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendMT102Request")
	@ResponsePayload
	public SendMT102Response sendMT102(@RequestPayload SendMT102Request request) {
		SendMT102Response response = new SendMT102Response();
		return response;
	}
}
