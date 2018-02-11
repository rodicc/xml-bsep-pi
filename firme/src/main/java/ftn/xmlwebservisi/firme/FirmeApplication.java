package ftn.xmlwebservisi.firme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages= {"soap","ftn.xmlwebservisi.firme"})
@EntityScan("ftn.xmlwebservisi.firme.model")
@EnableJpaRepositories("ftn.xmlwebservisi.firme.repository")
public class FirmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirmeApplication.class, args);
	}
}
