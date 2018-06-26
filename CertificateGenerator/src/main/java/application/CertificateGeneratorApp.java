package application;




import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"controller",
"service", "application"})



public class CertificateGeneratorApp {
	
	final static Logger logger = Logger.getLogger(CertificateGeneratorApp.class);

	public static void main(String[] args) {
		SpringApplication.run(CertificateGeneratorApp.class, args);
	
		String parameter = "ASDF";
		if(logger.isDebugEnabled()){
			logger.debug("This is debug : " + parameter);
		}
		
		if(logger.isInfoEnabled()){
			logger.info("This is info : " + parameter);
		}
		
		logger.warn("This is warn : " + parameter);
		logger.error("This is error : " + parameter);
		logger.fatal("This is fatal : " + parameter);
	
	}
	
}
