package com.wheeler.ytarchiver.downloader.docker.service;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.wheeler.ytarchiver.downloader.DownloadResult;
import com.wheeler.ytarchiver.downloader.DownloadService;
import com.wheeler.ytarchiver.downloader.StartsWithDownloadIdFileInfoResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class YoutubeDlDockerImageBasedDownloadService implements DownloadService {

    private final CreateContainerCmd createContainerCmdWithBasicConfig;
    private final ContainerExecutionService containerExecutionService;

    @Value("${downloader.docker.download.directory}")
    private final String downloadDirectory;

    public DownloadResult getMp3(String url) {
        var fileInfoResolver = createFileInfoResolver();
        return downloadInternal(fileInfoResolver, DownloadConfigFactory.forMp3(url, fileInfoResolver.getFilenameFormat()));
    }

    public DownloadResult getMp4(String url) {
        var fileInfoResolver = createFileInfoResolver();
        return downloadInternal(fileInfoResolver, DownloadConfigFactory.forMp4(url, fileInfoResolver.getFilenameFormat()));
    }

    private DownloadResult downloadInternal(StartsWithDownloadIdFileInfoResolver fileInfoResolver, String[] downloadConfig) {
        CreateContainerResponse createdContainer = createContainerCmdWithBasicConfig
                .withCmd(downloadConfig).exec();
        containerExecutionService.runContainer(createdContainer.getId());

        Path downloadedFilePath = Paths.get(fileInfoResolver.getFilePath());
        byte[] fileBytes = readBytes(downloadedFilePath);
        CompletableFuture.runAsync(() -> deleteFile(downloadedFilePath));
        return new DownloadResult(fileBytes, fileInfoResolver.getFilename());
    }

    private byte[] readBytes(Path downloadedFilePath) {
        try {
            return Files.readAllBytes(downloadedFilePath);
        } catch (IOException e) {
            //TODO: could use some wrapper exception to get handled more elegantly
            throw new RuntimeException("Unable to open downloaded file at: " + downloadedFilePath);
        }
    }

    private StartsWithDownloadIdFileInfoResolver createFileInfoResolver() {
        try {
            return new StartsWithDownloadIdFileInfoResolver(downloadDirectory);
        } catch (IOException e) {
            //TODO: could use some wrapper exception to get handled more elegantly
            throw new RuntimeException("Unable to open downloadDirectory: " + downloadDirectory);
        }
    }

    private void deleteFile(Path downloadedFilePath) {
        try {
            Files.delete(downloadedFilePath);
        } catch (IOException e) {
            //TODO: could use some wrapper exception to get handled more elegantly
            throw new RuntimeException("Unable to open downloadDirectory: " + downloadDirectory);
        }
    }
}
