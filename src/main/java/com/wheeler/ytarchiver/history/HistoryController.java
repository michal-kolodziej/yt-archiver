package com.wheeler.ytarchiver.history;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryTracker historyTracker;

    @Value("${downloader.history.password}")
    private final String historyPasswordHash;

    @GetMapping
    public ResponseEntity<HistoryDto> getHistory(@RequestParam String password) {
        if (BCrypt.checkpw(password, historyPasswordHash)) {
            return ResponseEntity.ok(historyTracker.getHistory());
        }
        return ResponseEntity.notFound().build();
    }
}
