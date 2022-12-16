package ngo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ngo.backend.Cryptography.deEncrypt;

@SpringBootApplication
public class NgoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NgoApiApplication.class, args);
		System.out.println("\033[0;31m" + "I am alive!" + "\033[0m");

		String originalString = "howtodoinjava.com";
        String encryptedString = deEncrypt.encrypt(originalString, "secret");
        String decryptedString = deEncrypt.decrypt(encryptedString, "secret");


        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
	}

}
