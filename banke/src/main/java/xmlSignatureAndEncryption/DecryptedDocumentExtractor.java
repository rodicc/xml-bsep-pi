package xmlSignatureAndEncryption;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.springframework.ws.client.core.SourceExtractor;
import org.w3c.dom.Document;

public class DecryptedDocumentExtractor implements SourceExtractor<Document>{

	private XMLSignAndEncryptUtility xmlSignAndEncryptUtility;
	
	@Override
	public Document extractData(Source source) throws IOException, TransformerException {

		xmlSignAndEncryptUtility = new XMLSignAndEncryptUtility();
		
		Document decryptedDocument = xmlSignAndEncryptUtility.veryfyAndDecrypt(((StreamSource) source).getInputStream());
		if(decryptedDocument != null) {
			return decryptedDocument;
		}
		
		return null;
	}

}
