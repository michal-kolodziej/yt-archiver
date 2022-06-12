package com.wheeler.ytarchiver.downloader.docker.service;

import org.springframework.stereotype.Component;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.wheeler.ytarchiver.downloader.DownloadResult;
import com.wheeler.ytarchiver.downloader.DownloadService;
import com.wheeler.ytarchiver.downloader.docker.service.ContainerExecutionResult;
import com.wheeler.ytarchiver.downloader.docker.service.ContainerExecutionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class YoutubeDlDockerImageBasedDownloadService implements DownloadService {

    private final CreateContainerCmd createContainerCmd;
    private final ContainerExecutionService containerExecutionService;

    public DownloadResult downloadVideo(String url) {
        CreateContainerResponse createdContainer = createContainerCmd
                .withCmd(url, "--extract-audio", "--audio-format", "mp3").exec();
        ContainerExecutionResult executionResult = containerExecutionService.runContainer(createdContainer.getId());
        return handleExecutionResult(executionResult);
    }

    private DownloadResult handleExecutionResult(ContainerExecutionResult executionResult) {
        if (executionResult.isSuccessful()) {
            return DownloadResult.success("we don't know the path YET");
        }
        return DownloadResult.failed();
    }

    private DownloadResult handleContainerFinished() {
        //TODO implement
        return DownloadResult.failed();
    }
}
