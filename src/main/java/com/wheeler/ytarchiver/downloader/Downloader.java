package com.wheeler.ytarchiver.downloader;

public interface Downloader {
    DownloadedFileInfo getMp3(String url);

    DownloadedFileInfo getMp4(String url);
}
