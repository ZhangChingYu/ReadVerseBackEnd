package com.elec5620.readverseserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.elec5620.readverseserver.utils.FileHandler;

@SpringBootApplication
public class ReadVerseServerApplication {

	public static void main(String[] args) {
		FileHandler.initEBookDir();
		SpringApplication.run(ReadVerseServerApplication.class, args);
	}

}