package com.wheeler.ytarchiver;

import com.wheeler.ytarchiver.downloader.binary.youtubedl.DownloadedFile;
import com.wheeler.ytarchiver.downloader.Downloader;
import com.wheeler.ytarchiver.history.DownloadDto;
import com.wheeler.ytarchiver.history.HistoryTracker;
import com.wheeler.ytarchiver.util.CallbackOnCloseFileInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/download")
@RequiredArgsConstructor
@Slf4j
public class DownloadController {

    private final Downloader downloader;
    private final HistoryTracker historyTracker;

    @GetMapping(value = "/mp3", produces = "audio/mpeg")
    public ResponseEntity<InputStreamResource> getMp3(@RequestParam String url) {
        DownloadedFile downloadedFile = downloader.getMp3(url);
        return ResponseEntity.ok()
                .headers(getAttachmentHeaders(downloadedFile))
                .body(new InputStreamResource(wrapInFileDeletingInputStream(downloadedFile.getFile())));
    }

    @GetMapping(value = "/mp4", produces = "video/mp4")
    public ResponseEntity<InputStreamResource> getMp4(@RequestParam String url, @RequestParam String quality) {
        DownloadedFile downloadedFile = downloader.getMp4(url, quality);

        historyTracker.recordDownload(new DownloadDto(url, downloadedFile.getOutputFilename(), quality));
        return ResponseEntity.ok()
                .headers(getAttachmentHeaders(downloadedFile))
                .body(new InputStreamResource(wrapInFileDeletingInputStream(downloadedFile.getFile())));
    }

    private HttpHeaders getAttachmentHeaders(DownloadedFile downloadedFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(downloadedFile.getOutputFilename(), StandardCharsets.UTF_8));
        return headers;
    }

    private FileInputStream wrapInFileDeletingInputStream(File file) {
        try {
            return new CallbackOnCloseFileInputStream(file, () -> CompletableFuture.runAsync(file::delete));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot create async delete file task, file not found.", e);
        }
    }
}
