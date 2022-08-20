package com.wheeler.ytarchiver.downloader.binary.youtubedl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class ProcessCommandFactory {

    @Value("${downloader.binary.youtubedl.path}")
    private final String binaryPath;


    public String[] forMp3(String url, String filenameFormat) {
        return new String[]{binaryPath, "--extract-audio", "--audio-format", "mp3", "--output", filenameFormat, url};
    }

    public String[] forMp4(String url, String filenameFormat) {
        return new String[]{binaryPath, url, "--output", filenameFormat, "-S", "ext:mp4:m4a", "--format", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best"};
    }


}
