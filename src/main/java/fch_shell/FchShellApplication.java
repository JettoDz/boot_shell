package fch_shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fch_shell.security.DatabaseAuthProvider;

@SpringBootApplication
public class FchShellApplication {

	public static void main(String[] args) {
		SpringApplication.run(FchShellApplication.class, args);
	}
	
	@Bean
	public DatabaseAuthProvider authProvider() {
		return new DatabaseAuthProvider();
	}

}
