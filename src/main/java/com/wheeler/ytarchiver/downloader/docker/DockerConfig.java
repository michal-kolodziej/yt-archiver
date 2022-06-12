package com.wheeler.ytarchiver.downloader.docker;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.AccessMode;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DockerConfig {

    private static final String DOWNLOADER_DOCKER_IMAGE_NAME = "mikenye/youtube-dl";

    @Value("${downloader.docker.target.directory}")
    private final String targetDirectory;

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
                .withEnv("PGID=1346505716", "UGID=1346505716");
    }
}
