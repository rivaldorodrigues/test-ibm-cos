package br.com.logique.starter;

import br.com.logique.starter.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {
        "br.com.logique.starter.repository.*",
})
public class RepositoryContext {

    @Autowired
    private MessageSource messageSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MessageService messageService() {
        return new MessageService(messageSource);
    }

	public static void main(String[] args) {
	    SpringApplication.run(RepositoryContext.class, args);
	}
}
