package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.ReportDto;
import service.ReportService;

@RestController
@RequestMapping("/izvestaj")
public class ReportController {

	@Autowired
	ReportService reportService;
	
	
	@PostMapping("/spisakRacuna")
	public ResponseEntity spisakRacuna(@RequestBody String brRacuna) {
		if(reportService.spisakRacuna(brRacuna)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/izvodKlijenta")
	public ResponseEntity izvodKlijenta(@RequestBody ReportDto dto) {
		if(reportService.izvodKlijenta(dto)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return ResponseEntity.badRequest().build();
	}
	
}
