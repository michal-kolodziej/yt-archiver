package com.wheeler.ytarchiver.history;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

@Component
@Slf4j
public class HistoryTracker {
    private static final int STORED_HISTORY_LENGTH = 100;
    private final LinkedBlockingDeque<DownloadDto> lastDownloads = new LinkedBlockingDeque<>();

    public void recordDownload(DownloadDto downloadDto) {
        if (lastDownloads.size() >= STORED_HISTORY_LENGTH) {
            lastDownloads.removeLast();
        }
        log.info("Storing download in history, url: " + downloadDto.getUrl() + ", filename: " + downloadDto.getFileName());
        lastDownloads.addFirst(downloadDto);
    }

    public HistoryDto getHistory() {
        long downloadsInLast7Days = lastDownloads.stream()
                .filter(dl -> dl.getDownloadDate().isAfter(LocalDateTime.now().minusDays(7)))
                .count();
        return new HistoryDto(downloadsInLast7Days, new ArrayList<>(lastDownloads));
    }
}
