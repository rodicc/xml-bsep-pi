package ftn.xmlwebservisi.firme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages= {"soap","ftn.xmlwebservisi.firme", "ftn.xmlwebservisi.firme.helpers",
		"ftn.xmlwebservisi.firme.service","ftn.xmlwebservisi.firme.security"})
@EntityScan("ftn.xmlwebservisi.firme.model")
@EnableJpaRepositories("ftn.xmlwebservisi.firme.repository")
public class FirmeApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(FirmeApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(FirmeApplication.class, args);
		logger.info("this is info");
		logger.debug("this is debug");
	}
}
