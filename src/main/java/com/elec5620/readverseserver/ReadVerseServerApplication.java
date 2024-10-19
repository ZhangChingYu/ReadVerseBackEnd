package com.elec5620.readverseserver;

import com.elec5620.readverseserver.utils.FileHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReadVerseServerApplication {

	public static void main(String[] args) {
		FileHandler.initEBookDir();
		SpringApplication.run(ReadVerseServerApplication.class, args);
	}

}
