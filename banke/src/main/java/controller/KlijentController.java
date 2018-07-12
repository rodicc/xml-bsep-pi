package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dto.KlijentDto;
import model.Firma;
import model.Racun;
import service.KlijentService;
import service.RacunService;

@RestController
@RequestMapping("/klijenti")
public class KlijentController {
		
	@Autowired
	KlijentService klijentService;
	
	@Autowired
	RacunService racunService;
	
	@GetMapping
	@ResponseBody
	public List<Firma> sviKlijenti(){
		return  klijentService.sviKlijenti();
	}
	
	@PostMapping("/novi")
	public ResponseEntity<Firma> noviKlijent(@RequestBody KlijentDto dto) {
		Firma firma = klijentService.noviKlijent(dto);
		if (firma != null) {
			return new ResponseEntity<>(firma, HttpStatus.OK);
		}
		return ResponseEntity.badRequest().build();
	}
	
}
