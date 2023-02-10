package com.wheeler.ytarchiver.history;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class HistoryTracker {
    private static final int STORED_HISTORY_LENGTH = 100;
    private final LinkedList<DownloadDto> lastDownloads = new LinkedList<>();

    public void recordDownload(DownloadDto downloadDto) {
        if (lastDownloads.size() >= STORED_HISTORY_LENGTH) {
            lastDownloads.removeLast();
        }
        lastDownloads.addFirst(downloadDto);
    }

    public List<DownloadDto> getHistory() {
        return lastDownloads;
    }
}
