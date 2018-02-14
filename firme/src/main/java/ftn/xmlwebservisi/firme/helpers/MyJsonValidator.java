package ftn.xmlwebservisi.firme.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;

import ftn.xmlwebservisi.firme.model.NalogZaPlacanje;


public class MyJsonValidator {
	
	public static boolean validirajNalogZaPlacanje(NalogZaPlacanje nalog) {
		final String nalogJsonAsString = konvertujObjekatUJsonString(nalog);
		final String nalogSchemaAsString = ucitajSemuKaoString();
		
		if (nalogJsonAsString == null) {
			return false;
		}
		
		boolean isValid = validirajJson(nalogSchemaAsString, nalogJsonAsString);
		return isValid;
	}

	private static boolean validirajJson(final String schema, final String data) {
		try {
			final JsonNode schemaNode = JsonLoader.fromString(schema);
			final JsonNode nalogNode = JsonLoader.fromString(data);
			
			final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			final JsonValidator validator = factory.getValidator();
			
			ProcessingReport report = validator.validate(schemaNode, nalogNode);
			System.out.println(report);
			
			if (report.isSuccess()) {
				return true;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static String konvertujObjekatUJsonString(Object obj) {
		ObjectMapper maper = new ObjectMapper();
		
		if (obj instanceof NalogZaPlacanje) {
			NalogZaPlacanje nzp = (NalogZaPlacanje) obj;
			try {
				String nalogJsonAsString = maper.writeValueAsString(nzp);
				return nalogJsonAsString;
			} catch (JsonProcessingException e) {
				System.out.println("Neuspesno konvertovanje naloga u json string");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static String ucitajSemuKaoString() {
		InputStream schemaInputStream =  MyJsonValidator.class.getClassLoader().getResourceAsStream("nalogSchema.json");
		final String schemaString = konvertujInputStreamUString(schemaInputStream);
		return schemaString;
	}
	
	@SuppressWarnings("resource")
	private static String konvertujInputStreamUString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		return result;
	}
}
