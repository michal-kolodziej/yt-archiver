package com.wheeler.ytarchiver.downloader.binary.youtubedl;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
@Getter
public class DownloadedFile {
    @NonNull
    private final File file;
    @NonNull
    private final String outputFilename;
}
