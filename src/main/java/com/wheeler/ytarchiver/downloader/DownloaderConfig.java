package com.wheeler.ytarchiver.downloader;

import com.wheeler.ytarchiver.downloader.binary.youtubedl.BinaryYoutubeDlDownloader;
import com.wheeler.ytarchiver.downloader.binary.youtubedl.ProcessCommandFactory;
import com.wheeler.ytarchiver.downloader.binary.youtubedl.VideoQualityService;
import com.wheeler.ytarchiver.history.HistoryTracker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class DownloaderConfig {
    @Bean
    Downloader downloader(ProcessCommandFactory processCommandFactory,
                          VideoQualityService videoQualityService,
                          @Qualifier("downloadDirectory") File downloadDirectory,
                          HistoryTracker historyTracker,
                          @Value("${downloader.binary.always-log-yt-dlp-output}") boolean alwaysLogYtDlpOutput) {
        var binaryYoutubeDlDownloader = new BinaryYoutubeDlDownloader(processCommandFactory, videoQualityService, downloadDirectory, alwaysLogYtDlpOutput);
        return new HistoryTrackingDownloaderProxy(binaryYoutubeDlDownloader, historyTracker);
    }
}
