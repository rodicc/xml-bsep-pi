package model;

public class CAWebServiceInfo {
	
	private static final String BANKA_ALIAS = "BANKA";
	private static final String BANKA_WS = "http://localhost:8082/ws";
	private static final String BANKA_CALLBACK = "http://www.ftn.xml/banke/HandleCSR";
	private static final String CENTRALNA_BANKA_ALIAS = "CBANKA";
	private static final String CENTRALNA_BANKA_WS = "http://localhost:8083/ws";
	private static final String CENTRALNA_BANKA_CALLBACK = "http://www.ftn.xml/banke/HandleCSR";
	
	public static String getWSfor(String alias){
		if(alias.equals(BANKA_ALIAS)) {
			return BANKA_WS;
		}
		else if(alias.equals(CENTRALNA_BANKA_ALIAS)) {
			return CENTRALNA_BANKA_WS;
		}
		else return null;
	}
	
	public static String getCallbackFor(String alias){
		if(alias.equals(BANKA_ALIAS)) {
			return BANKA_CALLBACK;
		}
		else if(alias.equals(CENTRALNA_BANKA_ALIAS)) {
			return CENTRALNA_BANKA_CALLBACK;
		}
		else return null;
	}
	
}