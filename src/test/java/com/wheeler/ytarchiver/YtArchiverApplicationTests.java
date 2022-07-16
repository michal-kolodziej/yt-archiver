package com.wheeler.ytarchiver;

import com.wheeler.ytarchiver.downloader.DownloadResult;
import com.wheeler.ytarchiver.downloader.DownloadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class YtArchiverApplicationTests {

    @Autowired
    private DownloadService downloadService;

    @Value("${downloader.docker.download.directory}")
    private String downloadDirectory;

    @Test
    void contextLoads() {
    }

    @Test
    void downloadService_getMp3_returns_notEmpty_bytes() {
        //given
        String urlToDownload = "https://www.youtube.com/watch?v=tPEE9ZwTmy0";

        //when
        DownloadResult downloadResult = downloadService.getMp3(urlToDownload);

        //then
        assertTrue(downloadResult.getBytes().length > 0);
    }

}
