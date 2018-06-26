package application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"controller", "service", "application"})
public class CertificateGeneratorApp {
	
	final static Logger logger = LoggerFactory.getLogger(CertificateGeneratorApp.class);

	public static void main(String[] args) {
		SpringApplication.run(CertificateGeneratorApp.class, args);
	
		logger.warn("This is warn");
		logger.error("This is error");
	}
	
}
