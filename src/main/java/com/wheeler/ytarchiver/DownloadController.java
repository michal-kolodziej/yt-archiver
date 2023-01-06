package com.wheeler.ytarchiver;

import com.wheeler.ytarchiver.downloader.DownloadedFileInfo;
import com.wheeler.ytarchiver.downloader.Downloader;
import com.wheeler.ytarchiver.downloader.binary.youtubedl.VideoQualityService;
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

    @GetMapping(value = "/mp3", produces = "audio/mpeg")
    public ResponseEntity<InputStreamResource> getMp3(@RequestParam String url) {
        DownloadedFileInfo downloadedFileInfo = downloader.getMp3(url);
        return ResponseEntity.ok()
                .headers(getAttachmentHeaders(downloadedFileInfo))
                .body(new InputStreamResource(wrapInFileDeletingInputStream(downloadedFileInfo.getFile())));
    }

    @GetMapping(value = "/mp4", produces = "video/mp4")
    public ResponseEntity<InputStreamResource> getMp4(@RequestParam String url, @RequestParam String quality) {
        DownloadedFileInfo downloadedFileInfo = downloader.getMp4(url, quality);
        return ResponseEntity.ok()
                .headers(getAttachmentHeaders(downloadedFileInfo))
                .body(new InputStreamResource(wrapInFileDeletingInputStream(downloadedFileInfo.getFile())));
    }

    private HttpHeaders getAttachmentHeaders(DownloadedFileInfo downloadedFileInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(downloadedFileInfo.getOutputFilename(), StandardCharsets.UTF_8));
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
