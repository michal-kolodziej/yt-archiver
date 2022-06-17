package com.wheeler.ytarchiver.downloader;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DownloadResult {
    @NonNull
    private final byte[] bytes;
    @NonNull
    private final String filename;
}
