package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.SoapClient;
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
	private final Logger logger = LoggerFactory.getLogger(CertificateController.class);
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/newSelfSigned")
	public ResponseEntity<String> sendSelfSignedCSR(@RequestBody CertificateDto csr)
			throws NoSuchAlgorithmException, FileNotFoundException, KeyStoreException, NoSuchProviderException, IOException{
		boolean isAutorized = true;
		
		if(isAutorized) {
			if (csr.isValid(csr)) {
				String response = null;
				if(csr.isSelfSigned()) {
					response = certificateService.generateSelfSignedCertificate(csr, true);
				}
				if(response != null) {
					return new ResponseEntity<String>(response, HttpStatus.OK);
				}
				else
					return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Invalid CSR Request: Obj={}", csr);
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	
	@PostMapping("/newCSR/{caAlias}")
	public ResponseEntity<String> sendCSR(@RequestBody CertificateDto csr, @PathVariable("caAlias") String caAlias)
			throws NoSuchAlgorithmException, FileNotFoundException, KeyStoreException, NoSuchProviderException, IOException{
		//dok se ne implemenira logovanje
		boolean isAutorized = true;
		
		if(isAutorized) {
			if (csr.isValid(csr)) {
				String response = client.sendCSR(csr, caAlias);
				if(response != null) {
					return new ResponseEntity<String>(response, HttpStatus.OK);
				}
				else
					return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Invalid CSR Request: Obj={}", csr);
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/revoke/{caAlias}")
	public ResponseEntity<OCSPResponseStatus> revokeCertificate(@RequestBody String serialNumber, @PathVariable("caAlias") String caAlias){
		boolean isAutorized = true;
		
		if(isAutorized) {
			if(serialNumber.matches("^[0-9]*$")) {
				OCSPResponseStatus response = client.revokeCertificate(serialNumber, caAlias);
				if(response != null) {
					return new ResponseEntity<OCSPResponseStatus>(response, HttpStatus.OK);
				} else
					return new ResponseEntity<OCSPResponseStatus>(HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Invalid Revocation Request parameter: Obj={}", serialNumber);
				return new ResponseEntity<OCSPResponseStatus>(HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	
	@PostMapping("/check/{caAlias}")
	public ResponseEntity<OCSPResponseStatus> checkCertificate(@PathVariable("caAlias") String caAlias, @RequestBody String serialNumber){

		boolean isAutorized = true;
		if(isAutorized) {
			if(serialNumber.matches("^[0-9]*$")) {
				OCSPResponseStatus response = client.checkCertificate(serialNumber, caAlias);
				if(response != null) {
					return new ResponseEntity<OCSPResponseStatus>(response, HttpStatus.OK);
				} else
					return new ResponseEntity<OCSPResponseStatus>(HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Invalid Certificate Status Request parameter: Obj={}", serialNumber);
				return new ResponseEntity<OCSPResponseStatus>(HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	@PostMapping(value = "/download/{caAlias}", produces = MediaType.TEXT_PLAIN_VALUE)
	//@ResponseBody
	public ResponseEntity<ByteArrayResource> getCertificateFile(@PathVariable("caAlias") String caAlias, @RequestBody String serialNumber){
		
		boolean isAutorized = true;
		
		if(isAutorized) {
			if(serialNumber.matches("^[0-9]*$")) {
				ByteArrayResource response = null;
				if(caAlias.equals("SELF")) {
					response = new ByteArrayResource(certificateService.getCertificateFile(serialNumber));
				} else {
					response = client.getCertificateFile(serialNumber, caAlias);
				}
				if(response != null) {
					HttpHeaders header = new HttpHeaders();
					header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+ serialNumber +".cer");
					return new ResponseEntity<ByteArrayResource>(response, header, HttpStatus.OK);
				} else
					return new ResponseEntity<ByteArrayResource>(HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Invalid Certificate File Request parameter: Obj={}", serialNumber);
				return new ResponseEntity<ByteArrayResource>(HttpStatus.BAD_REQUEST);
			}		
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
