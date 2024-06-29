package br.com.easytax.easytax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EasytaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasytaxApplication.class, args);
	}

}
