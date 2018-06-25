package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import application.SoapClient;
import model.CSRDto;
import model.CertificateDto;
import model.OCSPResponseStatus;
import service.CertificateService;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

	@Autowired
	CertificateService certificateService;
	@Autowired
	SoapClient client;
	@Autowired
	private ServletContext servletContext;
	
	@PostMapping("/newCSR/{caAlias}")
	public ResponseEntity<CertificateDto> sendCSR(@RequestBody CSRDto csr, @PathVariable String caAlias)
			throws NoSuchAlgorithmException, FileNotFoundException, KeyStoreException, NoSuchProviderException, IOException{
		
		
		//dok se ne implemenira logovanje
		boolean isAutorized = true;
		
		if(isAutorized) {
			CSRDto response = client.sendCSR(csr, caAlias);
			if(response != null) {
				return new ResponseEntity<CertificateDto>(response, HttpStatus.OK);
			}
			else
				return new ResponseEntity<CertificateDto>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
	}
	
	@PostMapping("/new")
	public ResponseEntity<CertificateDto> generateNewCertificate(@RequestBody CertificateDto certificate)
			throws NoSuchAlgorithmException, FileNotFoundException, KeyStoreException, NoSuchProviderException, IOException{
		
		
		//dok se ne implemenira logovanje
		boolean isAutorized = true;
		
		if(isAutorized) {
			CertificateDto response = certificateService.generateNewCertificate(certificate);
			if(response != null) {
				return new ResponseEntity<CertificateDto>(response, HttpStatus.OK);
			}
			else
				return new ResponseEntity<CertificateDto>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
	}
	
	
	@PostMapping("/revoke/{caAlias}")
	public ResponseEntity<OCSPResponseStatus> revokeCertificate(@RequestBody String serialNumber, @PathVariable String caAlias){
		boolean isAutorized = true;
		
		if(isAutorized) {
			OCSPResponseStatus response = client.revokeCertificate(serialNumber, caAlias);
			if(response != null) {
				return new ResponseEntity<OCSPResponseStatus>(response, HttpStatus.OK);
			}
			else
				return new ResponseEntity<OCSPResponseStatus>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	//TODO: provera da li je string cifri
	@PostMapping("/check/{caAlias}")
	public ResponseEntity<OCSPResponseStatus> checkCertificate(@PathVariable("caAlias") String caAlias, @RequestBody String serialNumber){

		boolean isAutorized = true;
		
		if(isAutorized) {
			OCSPResponseStatus response = client.checkCertificate(serialNumber, caAlias);
			if(response != null) {
				return new ResponseEntity<OCSPResponseStatus>(response, HttpStatus.OK);
			}
			else
				return new ResponseEntity<OCSPResponseStatus>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
/*	@GetMapping("/{certificateAlias}")
	public ResponseEntity<CertificateResponseDto> getCertificate(@PathVariable("certificateAlias") String certificateAlias){
		
		boolean isAutorized = true;
		
		if(isAutorized) {
			CertificateResponseDto response = certificateService.getCertificate(certificateAlias);
			if(response != null) {
				return new ResponseEntity<CertificateResponseDto>(response, HttpStatus.OK);
			}
			else
				return new ResponseEntity<CertificateResponseDto>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	*/
	@PostMapping(value = "/file/{caAlias}", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ResponseEntity<ByteArrayResource> getCertificateFile(@PathVariable("certificateAlias") String caAlias, @RequestBody String serialNumber){
		
		boolean isAutorized = true;
		
		if(isAutorized) {
			ByteArrayResource response = client.getCertificateFile(serialNumber, caAlias);
			if(response != null) {
				HttpHeaders header = new HttpHeaders();
				header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+ serialNumber +".cer");
				return new ResponseEntity<ByteArrayResource>(response, header, HttpStatus.OK);
						
			}
			else
				return new ResponseEntity<ByteArrayResource>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping
	public ResponseEntity<List<String>> getAllCACertificates (){
		
		boolean isAutorized = true;
		
		if(isAutorized) {
			List<String> response = certificateService.getAllCACertificates();
			if(response != null) {
				return new ResponseEntity<List<String>>(response, HttpStatus.OK);
			}
			else
				return new ResponseEntity<List<String>>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	
}
