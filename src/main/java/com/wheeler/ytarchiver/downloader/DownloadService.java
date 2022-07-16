package com.wheeler.ytarchiver.downloader;

public interface DownloadService {
    DownloadResult getMp3(String url);

    DownloadResult getMp4(String url);
}
