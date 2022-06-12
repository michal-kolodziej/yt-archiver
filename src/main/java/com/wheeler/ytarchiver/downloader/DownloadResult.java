package com.wheeler.ytarchiver.downloader;

import java.util.Optional;

import com.google.common.base.Strings;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DownloadResult {
    private static final DownloadResult INSTANCE_FAILED = new DownloadResult(false, null);

    private final boolean isSuccessful;
    private final String path;

    public static DownloadResult success(String path){
        if (Strings.isNullOrEmpty(path)) throw new IllegalArgumentException("Downloaded file path cannot be null or empty");
        return new DownloadResult(true, path);
    }

    public static DownloadResult failed(){
        return INSTANCE_FAILED;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public Optional<String> getPath() {
        return Optional.ofNullable(path);
    }
}
