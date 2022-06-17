package com.wheeler.ytarchiver.downloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class StartsWithDownloadIdFileInfoResolver {
    private final String downloadId;
    private final String downloadDirectory;

    private String filename;
    private String filePath;

    public StartsWithDownloadIdFileInfoResolver(String downloadId, String downloadDirectory) throws IOException {
        this.downloadId = Objects.requireNonNull(downloadId);
        this.downloadDirectory = Objects.requireNonNull(downloadDirectory);
        resolve();
    }

    public String getFilename() {
        // Filename returned to the user will be downloaded file name with downloadId removed
        // so "f18308ac-0290-4a93-a670-078eaa2c5591VIDEO NAME.mp3" will become "VIDEO NAME.mp3"
        return filename.substring(downloadId.length());
    }

    public String getFilePath() {
        return filePath;
    }

    private void resolve() throws IOException {
        File downloadedFile = Arrays.stream(getFilesInDirectory(downloadDirectory))
                .filter(file -> file.getName().startsWith(downloadId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File with downloadId: " + downloadId + " is not present in directory: " + downloadDirectory));

        this.filename = downloadedFile.getName();
        this.filePath = downloadedFile.getPath();
    }

    private File[] getFilesInDirectory(String downloadDirectory) throws IOException{
        File targetDirectory = Paths.get(downloadDirectory).toFile();
        if (!targetDirectory.isDirectory())
            throw new IOException("file: '" + targetDirectory + "' is not a directory");
        return Objects.requireNonNull(targetDirectory.listFiles());
    }
}