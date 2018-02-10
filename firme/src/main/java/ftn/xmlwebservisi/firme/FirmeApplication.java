package ftn.xmlwebservisi.firme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"soap","ftn.xmlwebservisi.firme"})
public class FirmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirmeApplication.class, args);
	}
}
