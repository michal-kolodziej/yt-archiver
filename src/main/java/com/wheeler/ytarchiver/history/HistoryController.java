package com.wheeler.ytarchiver.history;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryTracker historyTracker;
    @GetMapping
    public ResponseEntity<List<DownloadDto>> getHistory(@RequestParam String password){
        if(password.equals("2137")){
            return ResponseEntity.ok(historyTracker.getHistory());
        }
        return ResponseEntity.notFound().build();
    }
}
