package com.wheeler.ytarchiver.downloader.binary.youtubedl;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ProcessOutputReader {

    private final StringBuilder outputBuilder = new StringBuilder();

    public ProcessOutputReader(Process downloaderProcess) {
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(downloaderProcess.getInputStream()));
        String line;
        try {
            while ((line = stdInput.readLine()) != null) {
                outputBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read process output", e);
        }
    }

    public String getOutput(){
        return outputBuilder.toString();
    }
}
