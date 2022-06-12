package com.wheeler.ytarchiver.downloader.docker.service;

import java.util.List;

import lombok.Value;

@Value
public class ContainerExecutionResult {
    Long exitCode;
    List<String> logMessages;

    public boolean isSuccessful() {
        if (exitCode == 0L) return true;
        throw new RuntimeException("Got unexpected error code: " + exitCode + ", docker logs: " + String.join("\n", logMessages));
    }
}
