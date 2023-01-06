package com.wheeler.ytarchiver;

import com.wheeler.ytarchiver.downloader.binary.youtubedl.DownloadedFile;
import com.wheeler.ytarchiver.downloader.Downloader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class YtArchiverApplicationTests {

    @Autowired
    private Downloader downloader;

    @Value("${downloader.binary.download.directory}")
    private String downloadDirectory;

    @Test
    void contextLoads() {
    }

    @Test
    void downloadService_getMp3_returns_notEmpty_bytes() {
        //given
        String urlToDownload = "https://www.youtube.com/watch?v=tPEE9ZwTmy0";

        //when
        DownloadedFile downloadedFile = downloader.getMp3(urlToDownload);

        //then
        try {
            FileInputStream fileInputStream = new FileInputStream(downloadedFile.getFile());
            assertTrue(fileInputStream.available() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
