package com.wheeler.ytarchiver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.wheeler.ytarchiver.downloader.Downloader;
@SpringBootApplication
public class YtArchiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(YtArchiverApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(Downloader downloader){
		return (args) -> downloader.downloadVideo("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
	}

}
