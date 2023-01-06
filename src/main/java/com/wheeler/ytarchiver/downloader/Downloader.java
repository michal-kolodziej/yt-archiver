package com.wheeler.ytarchiver.downloader;

import com.wheeler.ytarchiver.downloader.binary.youtubedl.DownloadedFile;

public interface Downloader {
    DownloadedFile getMp3(String url);

    DownloadedFile getMp4(String url, String quality);
}
