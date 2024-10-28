package com.wheeler.ytarchiver.downloader.binary.youtubedl;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class ProcessCommandFactory {

    @Value("${downloader.binary.youtubedl.path}")
    private final String binaryPath;


    public ProcessCommandBuilder forMp3(String url, String filenameFormat) {
        return new ProcessCommandBuilder(
                new String[]{
                        binaryPath,
                        "--extract-audio",
                        "--audio-format", "mp3",
                        "--output", filenameFormat,
                        url});
    }

    public ProcessCommandBuilder forMp4(String url, String filenameFormat, String videoFormat) {
        return new ProcessCommandBuilder(
                new String[]{binaryPath,
                        "--output", filenameFormat,
                        "-S", "ext:mp4:m4a",
                        "--format", videoFormat,
                        url});
    }

    static class ProcessCommandBuilder {
        String[] args;

        ProcessCommandBuilder(String[] args) {
            //TODO: validation
            this.args = args;
        }

        String[] build() {
            return this.args;
        }

        ProcessCommandBuilder addCookies(boolean cookiesEnabled, String cookiesPath){
            if(cookiesEnabled){
                List<String> argsList = new ArrayList<>(List.of(args));
                argsList.add("--cookies");
                argsList.add(cookiesPath);
                this.args = argsList.toArray(new String[0]);
            }
            return this;
        }
    }
}
