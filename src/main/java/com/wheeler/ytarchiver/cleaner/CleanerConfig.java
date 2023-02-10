package com.wheeler.ytarchiver.cleaner;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class CleanerConfig {

    @Qualifier("downloadDirectory")
    private final File downloadDirectory;

    @Bean
    DownloadDirectoryCleaner downloadDirectoryCleaner() {
        return new DownloadDirectoryCleaner(downloadDirectory);
    }
}
