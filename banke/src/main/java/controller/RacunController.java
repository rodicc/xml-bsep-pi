package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dto.KlijentDto;
import dto.RacunDto;
import dto.UkidanjeDto;
import model.Racun;
import model.UkidanjeRacuna;
import service.RacunService;

@RestController
@RequestMapping("/racuni")
public class RacunController {

		@Autowired
		RacunService racunService;
		
		@GetMapping
		@ResponseBody
		public List<Racun> sviRacuni(){
			return  racunService.sviRacuni();
		}
		
		@GetMapping("/ukinuti")
		public List<Racun> sviUkinutiRacuni(){
			return  racunService.sviUkinutiRacuni();
		}
		
		@PostMapping("/novi")
		public ResponseEntity<Racun> noviRacun(@RequestBody RacunDto dto) {
			Racun racun = racunService.noviRacun(dto);
			if (racun != null) {
				return new ResponseEntity<>(racun, HttpStatus.OK);
			}
			return ResponseEntity.badRequest().build();
		}
		
		@PostMapping("/ukini")
		public ResponseEntity<UkidanjeDto> ukiniRacun(@RequestBody UkidanjeDto dto){
			UkidanjeRacuna ukidanje = racunService.ukiniRacun(dto);
			if (ukidanje != null) {
				return new ResponseEntity<>(dto, HttpStatus.OK);
			}
			return ResponseEntity.badRequest().build();
		}
		
		
}
