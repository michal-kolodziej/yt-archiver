package com.wheeler.ytarchiver;

import com.wheeler.ytarchiver.downloader.DownloadResult;
import com.wheeler.ytarchiver.downloader.DownloadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void downloadDirectoryNotEmptyAfterDownload() throws IOException {
        //given
        String urlToDownload = "https://www.youtube.com/watch?v=tPEE9ZwTmy0";
        File targetDirectory = Paths.get(downloadDirectory).toFile();

        //when
        dirClear(downloadDirectory);
        DownloadResult downloadResult = downloadService.downloadVideo(urlToDownload);

        //then
        assertEquals(1, targetDirectory.listFiles().length);
    }

    private void dirClear(String path) throws IOException {
        File targetDirectory = Paths.get(path).toFile();
        if (!targetDirectory.isDirectory()) throw new IOException("file: '" + path + "' is not a directory");
        File[] files = Objects.requireNonNull(targetDirectory.listFiles());
        for (File file : files)
            if (!file.isDirectory()) file.delete();
    }

}
