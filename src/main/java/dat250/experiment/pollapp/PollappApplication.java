package dat250.experiment.pollapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = { MongoAutoConfiguration.class })
@Profile("default")
public class PollappApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollappApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")   // Allow all endpoints
						.allowedOrigins("*") // Allow all origins
						.allowedMethods("*"); // Allow all HTTP methods (GET, POST, etc.)
			}
		};
	}

}
