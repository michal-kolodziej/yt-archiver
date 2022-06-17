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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class YoutubeDlDockerImageBasedDownloadService implements DownloadService {

    private final CreateContainerCmd createContainerCmd;
    private final ContainerExecutionService containerExecutionService;

    @Value("${downloader.docker.download.directory}")
    private final String downloadDirectory;

    public DownloadResult downloadVideo(String url) throws IOException {
        String downloadId = UUID.randomUUID().toString();
        String filenameFormat = downloadId + "%(title)s.%(ext)s";

        CreateContainerResponse createdContainer = createContainerCmd
                .withCmd(url, "--extract-audio", "--audio-format", "mp3", "--output", filenameFormat).exec();
        containerExecutionService.runContainer(createdContainer.getId());

        var fileInfoResolver = new StartsWithDownloadIdFileInfoResolver(downloadId, downloadDirectory);
        Path downloadedFilePath = Paths.get(fileInfoResolver.getFilePath());
        byte[] fileBytes = Files.readAllBytes(downloadedFilePath);

        CompletableFuture.runAsync(() -> deleteFile(downloadedFilePath));
        return new DownloadResult(fileBytes, fileInfoResolver.getFilename());
    }

    private void deleteFile(Path downloadedFilePath) {
        try {
            Files.delete(downloadedFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
