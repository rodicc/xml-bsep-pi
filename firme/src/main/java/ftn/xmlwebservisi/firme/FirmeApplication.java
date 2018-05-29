package ftn.xmlwebservisi.firme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages= {"soap","ftn.xmlwebservisi.firme", "ftn.xmlwebservisi.firme.helpers",
		"ftn.xmlwebservisi.firme.service"})
@EntityScan("ftn.xmlwebservisi.firme.model")
@EnableJpaRepositories("ftn.xmlwebservisi.firme.repository")
public class FirmeApplication {
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(FirmeApplication.class, args);
	}
}
