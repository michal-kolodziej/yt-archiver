package com.wheeler.ytarchiver.cleaner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Slf4j
@RequiredArgsConstructor
public class DownloadDirectoryCleaner {

    private static final int FILE_MAX_AGE_MINUTES = 120;
    private static final int CLEAN_FREQUENCY_MILLIS = 1000 * 1800;

    private final File downloadDirectory;

    @Scheduled(fixedDelay = CLEAN_FREQUENCY_MILLIS)
    void removeOldFiles() {
        Instant fileAgeCutoff = LocalDateTime.now().atZone(ZoneId.systemDefault()).minusMinutes(FILE_MAX_AGE_MINUTES).toInstant();
        log.info("Removing files older than " + fileAgeCutoff);
        File[] files = downloadDirectory.listFiles(pathname -> FileUtils.isFileOlder(pathname, fileAgeCutoff.toEpochMilli()));
        for (File file : files) {
            String lastModifiedDateReadable = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime().toString();
            if (file.delete()) {
                log.info("Deleted file: " + file.getName() + ", lastModifiedDate " + lastModifiedDateReadable);
            } else {
                log.warn("Cannot delete file: " + file.getName() + ", lastModifiedDate " + lastModifiedDateReadable);
            }
        }
    }

}
