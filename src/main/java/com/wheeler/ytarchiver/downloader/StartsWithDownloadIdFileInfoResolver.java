package com.wheeler.ytarchiver.downloader;

import com.wheeler.ytarchiver.downloader.binary.youtubedl.DownloadedFile;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

//TODO: some refactoring as i feel this class sucks d(-.-)b
public class StartsWithDownloadIdFileInfoResolver {
    private final String downloadId;
    @Qualifier("downloadDirectory") private final File downloadDirectory;

    private File file;

    private boolean isDownloadedFileFound = false;

    public StartsWithDownloadIdFileInfoResolver(File downloadDirectory) {
        this.downloadDirectory = Objects.requireNonNull(downloadDirectory);
        this.downloadId = UUID.randomUUID().toString();
    }

    public DownloadedFile getFileInfo() {
        return new DownloadedFile(getFile(), getOutputFilename());
    }

    public String getOutputFormat() {
        return downloadDirectory + File.separator + downloadId + "%(title)s.%(ext)s";
    }

    private String getOutputFilename() {
        findDownloadedFile();
        // Filename returned to the user will be downloaded file name with downloadId removed
        // so "f18308ac-0290-4a93-a670-078eaa2c5591VIDEO NAME.mp3" will become "VIDEO NAME.mp3"
        return file.getName().substring(downloadId.length());
    }

    private File getFile() {
        findDownloadedFile();
        return file;
    }

    private void findDownloadedFile() {
        if (isDownloadedFileFound) return;
        File downloadedFile = Arrays.stream(Objects.requireNonNull(downloadDirectory.listFiles()))
                .filter(file -> file.getName().startsWith(downloadId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File with downloadId: " + downloadId + " is not present in directory: " + downloadDirectory));

        this.isDownloadedFileFound = true;
        this.file = downloadedFile;
    }
}