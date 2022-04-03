package com.example.pieceart;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan //@WebFilter 사용에 필요한 어노테이션
@OpenAPIDefinition(info = @Info(title="PieceArt API", version = "0.0.1"))
public class PieceartApplication {

	public static void main(String[] args) {
		SpringApplication.run(PieceartApplication.class, args);
	}

}
