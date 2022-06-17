package com.wheeler.ytarchiver.downloader.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.AccessMode;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@RequiredArgsConstructor
public class DockerConfig {

    private static final String DOWNLOADER_DOCKER_IMAGE_NAME = "mikenye/youtube-dl";

    @Value("${downloader.docker.download.directory}")
    private final String targetDirectory;

    @Value("${downloader.docker.user.PGID}")
    private final String dockerUserPGID;

    @Value("${downloader.docker.user.UGID}")
    private final String dockerUserUGID;

    @Bean
    DockerClient dockerClient() {
        return DockerClientBuilder.getInstance().build();
    }

    @Bean
    @Scope("prototype")
    CreateContainerCmd createContainerCmd(DockerClient dockerClient) {
        return dockerClient.createContainerCmd(DOWNLOADER_DOCKER_IMAGE_NAME)
                .withHostConfig(HostConfig.newHostConfig()
                        .withBinds(new Bind(targetDirectory, new Volume("/workdir"), AccessMode.rw)))
                .withEnv("PGID=" + dockerUserPGID, "UGID=" + dockerUserUGID);
    }
}
