package com.wheeler.ytarchiver.downloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

//TODO: some refactoring as i feel this class does too much.
// it probably shouldn't be responsible for generating downloadId
public class StartsWithDownloadIdFileInfoResolver {
    private final String downloadId;
    private final String downloadDirectory;

    private String filename;
    private String filePath;

    private boolean isResolved = false;

    public StartsWithDownloadIdFileInfoResolver(String downloadDirectory) throws IOException {
        this.downloadDirectory = Objects.requireNonNull(downloadDirectory);
        this.downloadId = UUID.randomUUID().toString();
    }

    public String getFilenameFormat() {
        return downloadId + "%(title)s.%(ext)s";
    }

    public String getFilename() {
        resolve();
        // Filename returned to the user will be downloaded file name with downloadId removed
        // so "f18308ac-0290-4a93-a670-078eaa2c5591VIDEO NAME.mp3" will become "VIDEO NAME.mp3"
        return filename.substring(downloadId.length());
    }

    public String getFilePath() {
        resolve();
        return filePath;
    }

    private void resolve() {
        if (isResolved) return;
        File downloadedFile = Arrays.stream(getFilesInDirectory(downloadDirectory))
                .filter(file -> file.getName().startsWith(downloadId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File with downloadId: " + downloadId + " is not present in directory: " + downloadDirectory));

        this.isResolved = true;
        this.filename = downloadedFile.getName();
        this.filePath = downloadedFile.getPath();
    }

    private File[] getFilesInDirectory(String downloadDirectory) {
        File targetDirectory = Paths.get(downloadDirectory).toFile();
        if (!targetDirectory.isDirectory()) {
            //TODO: could use more appropriate dedicated exception
            throw new RuntimeException("file: '" + targetDirectory + "' is not a directory");
        }
        return Objects.requireNonNull(targetDirectory.listFiles());
    }
}