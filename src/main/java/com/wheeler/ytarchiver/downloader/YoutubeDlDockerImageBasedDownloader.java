package com.wheeler.ytarchiver.downloader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class YoutubeDlDockerImageBasedDownloader implements Downloader {
    private final DockerClient dockerClient = DockerClientBuilder.getInstance().build();

    private static final String DOWNLOADER_DOCKER_IMAGE_NAME = "mikenye/youtube-dl";

    @Value("${downloader.target.directory}")
    private final String targetDirectory;

    public void downloadVideo(String url) {
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(DOWNLOADER_DOCKER_IMAGE_NAME);
        CreateContainerCmd baseConfiguredContainerCmd = setBaseImageConfig(containerCmd);
        CreateContainerCmd fullyConfiguredvideoDownloadContainerCmd = baseConfiguredContainerCmd
                .withCmd(url, "--extract-audio", "--audio-format", "mp3");
        CreateContainerResponse createdContainer = fullyConfiguredvideoDownloadContainerCmd.exec();
        dockerClient.startContainerCmd(createdContainer.getId()).exec();
    }

    private CreateContainerCmd setBaseImageConfig(CreateContainerCmd containerUnderCreation) {
        return containerUnderCreation
                .withHostConfig(HostConfig.newHostConfig().withAutoRemove(true))
                //TODO: replace deprecated method
                .withBinds(Bind.parse(getDownloadDirVolumeBind()))
                .withEnv("PGID=$(id -g)")
                .withEnv("UGID=$(id -u)");
    }

    private String getDownloadDirVolumeBind() {
        return targetDirectory + ":/workdir:rw";
    }
}
