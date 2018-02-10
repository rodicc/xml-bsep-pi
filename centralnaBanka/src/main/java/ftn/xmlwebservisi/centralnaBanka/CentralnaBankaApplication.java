package ftn.xmlwebservisi.centralnaBanka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"service", "helpers", "ftn.xmlwebservisi.centralnaBanka"})
@EnableJpaRepositories("repository")
@EntityScan("model")
public class CentralnaBankaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralnaBankaApplication.class, args);
	}
}
