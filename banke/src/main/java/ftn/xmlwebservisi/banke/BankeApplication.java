package ftn.xmlwebservisi.banke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages= {"service", "helpers", "ftn.xmlwebservisi.banke", "xml.ftn.banke", "controller"})
@EnableJpaRepositories("repository")
@EntityScan("model")
public class BankeApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(BankeApplication.class, args);
		
	}
	
	
}
