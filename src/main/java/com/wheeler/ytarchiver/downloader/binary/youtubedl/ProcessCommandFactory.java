package com.wheeler.ytarchiver.downloader.binary.youtubedl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProcessCommandFactory {

    @Value("${downloader.binary.youtubedl.path}")
    private final String binaryPath;

    @Value("${downloader.binary.cookies.enabled}")
    private final boolean cookiesEnabled;

    @Value("${downloader.binary.netrc.enabled}")
    private final boolean netrcEnabled;

    @Value("${downloader.binary.oauth.enabled}")
    private final boolean oAuthEnabled;

    @Value("${downloader.binary.cookies.path}")
    private final String cookiesPath;

    public String[] forMp3(String url, String filenameFormat) {
        return new ProcessCommandBuilder(
                new String[]{
                        binaryPath,
                        "--extract-audio",
                        "--audio-format", "mp3",
                        "--output", filenameFormat,
                        url})
                .addCookies(cookiesEnabled, cookiesPath)
                .oAuth(oAuthEnabled, netrcEnabled)
                .build();
    }

    public String[] forMp4(String url, String filenameFormat, String videoFormat) {
        return new ProcessCommandBuilder(
                new String[]{binaryPath,
                        "--output", filenameFormat,
                        "-S", "ext:mp4:m4a",
                        "--format", videoFormat,
                        url})
                .addCookies(cookiesEnabled, cookiesPath)
                .oAuth(oAuthEnabled, netrcEnabled)
                .build();
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

        ProcessCommandBuilder addCookies(boolean cookiesEnabled, String cookiesPath) {
            if (cookiesEnabled) {
                List<String> argsList = new ArrayList<>(List.of(args));
                argsList.add("--cookies");
                argsList.add(cookiesPath);
                this.args = argsList.toArray(new String[0]);
            }
            return this;
        }

        ProcessCommandBuilder oAuth(boolean oauthEnabled, boolean netrcEnabled) {
            List<String> argsList = new ArrayList<>(List.of(args));
            if (netrcEnabled) {
                argsList.add("--netrc");
            }
            if (oauthEnabled) {
                argsList.add("--username=oauth");
                argsList.add("--password=\"\"");
            }
            this.args = argsList.toArray(new String[0]);
            return this;
        }
    }
}
