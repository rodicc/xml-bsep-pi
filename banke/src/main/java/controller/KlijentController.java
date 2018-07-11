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
	
	@PostMapping("/noviKlijent")
	public ResponseEntity<KlijentDto> noviKlijent(@RequestBody KlijentDto dto) {
		Racun racun = klijentService.noviKlijent(dto);
		if (racun != null) {
			return new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return ResponseEntity.badRequest().build();
	}
	
}
