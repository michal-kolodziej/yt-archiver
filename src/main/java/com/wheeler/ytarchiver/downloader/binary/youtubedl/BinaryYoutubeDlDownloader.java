package com.wheeler.ytarchiver.downloader.binary.youtubedl;

import com.wheeler.ytarchiver.downloader.Downloader;
import com.wheeler.ytarchiver.downloader.StartsWithDownloadIdFileInfoResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class BinaryYoutubeDlDownloader implements Downloader {

    private final ProcessCommandFactory processCommandFactory;
    private final VideoQualityService videoQualityService;
    private final File downloadDirectory;

    @Override
    public DownloadedFile getMp3(String url) {
        var fileInfoResolver = new StartsWithDownloadIdFileInfoResolver(downloadDirectory);
        return downloadInternal(processCommandFactory.forMp3(url, fileInfoResolver.getOutputFormat()), fileInfoResolver);
    }

    @Override
    public DownloadedFile getMp4(String url, String selectedQuality) {
        var fileInfoResolver = new StartsWithDownloadIdFileInfoResolver(downloadDirectory);
        String[] processCommand = processCommandFactory.forMp4(url,
                fileInfoResolver.getOutputFormat(),
                videoQualityService.getFormatForQuality(selectedQuality));
        return downloadInternal(processCommand, fileInfoResolver);
    }

    private DownloadedFile downloadInternal(String[] args, StartsWithDownloadIdFileInfoResolver fileInfoResolver) {
        var processBuilder = buildProcess(args);
        var downloaderProcess = runProcess(processBuilder);
        waitForProcessToFinish(downloaderProcess);
        return fileInfoResolver.getFileInfo();
    }

    private ProcessBuilder buildProcess(String[] args) {
        log.info("Building process with args: {}", (Object) args);
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        return pb;
    }

    private Process runProcess(ProcessBuilder processBuilder) {
        try {
            return processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException("Error when trying to run youtube-dl process ", e);
        }
    }

    private void waitForProcessToFinish(Process downloaderProcess) {
        ProcessOutputReader outputReader = new ProcessOutputReader(downloaderProcess);
        try {
            checkExitCode(downloaderProcess.waitFor(), outputReader);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error when running youtube-dl process, output: " + outputReader.getOutput(), e);
        }
    }

    private void checkExitCode(int exitCode, ProcessOutputReader processOutputReader) {
        if (exitCode != 0) {
            throw new RuntimeException("Process exit code wasn't 0, output: " + processOutputReader.getOutput());
        }
    }


}
