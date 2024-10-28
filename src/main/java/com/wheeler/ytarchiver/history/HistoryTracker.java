package com.wheeler.ytarchiver.history;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;

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

    public HistoryDto getHistory() {
        long downloadsInLast7Days = lastDownloads.stream()
                .filter(dl -> dl.getDownloadDate().isAfter(LocalDateTime.now().minusDays(7)))
                .count();
        return new HistoryDto(downloadsInLast7Days, lastDownloads);
    }
}
