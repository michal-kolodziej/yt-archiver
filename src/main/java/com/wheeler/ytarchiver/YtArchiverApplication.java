package com.wheeler.ytarchiver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class YtArchiverApplication {
    public static void main(String[] args) {
        SpringApplication.run(YtArchiverApplication.class, args);
    }

    @Bean
    File downloadDirectory(@Value("${downloader.binary.download.directory}") String downloadDirectoryPath) {
        File downloadDirectory = new File(downloadDirectoryPath);
        if (!downloadDirectory.isDirectory()) {
            //TODO: could use more appropriate dedicated exception
            throw new RuntimeException("file: '" + downloadDirectoryPath + "' is not a directory");
        }
        return downloadDirectory;
    }

}
