package com.example.servingwebcontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ServingWebContentApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServingWebContentApplication.class, args);

		System.out.println("Starting browser");

		Runtime rt = Runtime.getRuntime();
		String url = "http://localhost:8080/greeting";
		try {
			rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
