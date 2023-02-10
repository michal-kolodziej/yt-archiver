package com.wheeler.ytarchiver.history;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DownloadDto {
    private final String url;
    private final String fileName;
    private final String quality;
    private final LocalDateTime downloadDate = LocalDateTime.now();
}
