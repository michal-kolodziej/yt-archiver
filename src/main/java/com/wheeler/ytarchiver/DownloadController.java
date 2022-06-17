package com.wheeler.ytarchiver;

import com.wheeler.ytarchiver.downloader.DownloadResult;
import com.wheeler.ytarchiver.downloader.DownloadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class DownloadController {

    private final DownloadService downloadService;

    @GetMapping(value = "/mp3", produces = "audio/mpeg")
    public ResponseEntity<byte[]> getMp3(@RequestParam String url) throws IOException {
        DownloadResult downloadResult = downloadService.downloadVideo(url);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadResult.getFilename());
        return ResponseEntity.ok()
                .headers(headers)
                .body(downloadResult.getBytes());
    }
}
