package com.wheeler.ytarchiver.downloader;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class DownloadController {

    private final DownloadService downloadService;

    @GetMapping(value = "/mp3", produces = "audio/mpeg")
    public ResponseEntity<byte[]> getMp3(@RequestParam String url) {
        DownloadResult downloadResult = downloadService.getMp3(url);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadResult.getFilename());
        return ResponseEntity.ok()
                .headers(headers)
                .body(downloadResult.getBytes());
    }

    @GetMapping(value = "/mp4", produces = "video/mp4")
    public ResponseEntity<byte[]> getMp4(@RequestParam String url) {
        DownloadResult downloadResult = downloadService.getMp4(url);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadResult.getFilename());
        return ResponseEntity.ok()
                .headers(headers)
                .body(downloadResult.getBytes());
    }
}
