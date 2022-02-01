package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	// createdBy, lastModifiedBy의 값을 채워줌
	// Session이나 cookie, token 을 사용해서 원하는 값 을 넣어주면 됨
	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
