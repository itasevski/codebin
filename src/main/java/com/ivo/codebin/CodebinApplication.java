package com.ivo.codebin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class CodebinApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodebinApplication.class, args);
	}

}
