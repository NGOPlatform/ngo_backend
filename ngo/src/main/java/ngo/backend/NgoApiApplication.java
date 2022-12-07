package ngo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NgoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NgoApiApplication.class, args);
		System.out.println("\033[0;31m" + "I am alive!" + "\033[0m");
	}

}
