package com.wheeler.ytarchiver.history;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class DownloadDto {
    private final String url;
    private final String fileName;
    private final String quality;
    private final LocalDateTime downloadDate = LocalDateTime.now();

    public DownloadDto(String url, String outputFilename) {
        this.url = url;
        this.fileName = outputFilename;
        this.quality = null;
    }

    public DownloadDto(String url, String outputFilename, String quality) {
        this.url = url;
        this.fileName = outputFilename;
        this.quality = quality;
    }
}
