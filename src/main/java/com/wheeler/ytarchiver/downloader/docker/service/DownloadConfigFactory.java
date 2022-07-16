package com.wheeler.ytarchiver.downloader.docker.service;

public class DownloadConfigFactory {
    public static String[] forMp3(String url, String outputFilenameFormat) {
        return new String[]{url, "--output", outputFilenameFormat, "--extract-audio", "--audio-format", "mp3"};
    }

    public static String[] forMp4(String url, String outputFilenameFormat) {
        return new String[]{url, "--output", outputFilenameFormat, "--format", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best"};

    }
}