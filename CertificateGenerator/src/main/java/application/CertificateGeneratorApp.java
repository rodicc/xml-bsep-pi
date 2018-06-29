package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages= {"controller", "service", "application", "security"})
@EntityScan("model")
@EnableJpaRepositories("repository")
public class CertificateGeneratorApp {
	
	public static void main(String[] args) {
		SpringApplication.run(CertificateGeneratorApp.class, args);
	}
	
}
