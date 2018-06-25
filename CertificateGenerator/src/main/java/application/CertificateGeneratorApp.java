package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"controller",
"service", "application"})
public class CertificateGeneratorApp {

	public static void main(String[] args) {
		SpringApplication.run(CertificateGeneratorApp.class, args);
	}
	
}
