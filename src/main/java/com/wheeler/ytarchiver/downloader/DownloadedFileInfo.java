package com.wheeler.ytarchiver.downloader;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Path;

@RequiredArgsConstructor
@Getter
public class DownloadedFileInfo {
    @NonNull
    private final File file;
    @NonNull
    private final String outputFilename;
}
