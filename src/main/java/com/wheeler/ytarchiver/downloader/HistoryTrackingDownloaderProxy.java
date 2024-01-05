package com.wheeler.ytarchiver.downloader;

import com.wheeler.ytarchiver.downloader.binary.youtubedl.DownloadedFile;
import com.wheeler.ytarchiver.history.DownloadDto;
import com.wheeler.ytarchiver.history.HistoryTracker;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HistoryTrackingDownloaderProxy implements Downloader {

    private final Downloader delegate;
    private final HistoryTracker historyTracker;
    @Override
    public DownloadedFile getMp3(String url) {
        DownloadedFile mp3 = delegate.getMp3(url);
        historyTracker.recordDownload(new DownloadDto(url, mp3.getOutputFilename()));
        return mp3;
    }

    @Override
    public DownloadedFile getMp4(String url, String quality) {
        DownloadedFile mp4 = delegate.getMp4(url, quality);
        historyTracker.recordDownload(new DownloadDto(url, mp4.getOutputFilename(), quality));
        return mp4;
    }
}
