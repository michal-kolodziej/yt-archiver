package com.wheeler.ytarchiver.downloader.docker.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ContainerExecutionService {
    private static final Long SUCCESS_EXIT_CODE = 0L;
    private final DockerClient dockerClient;

    public void runContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
        waitForContainerToExit(containerId);
        checkExecutionSuccessful(containerId);
        CompletableFuture.runAsync(() -> removeContainer(containerId));
    }

    private void checkExecutionSuccessful(String containerId) {
        InspectContainerResponse.ContainerState containerState = getContainerState(containerId);
        Long exitCode = containerState.getExitCodeLong();
        List<String> logMessages = getDockerLogs(containerId);
        if (!Objects.equals(exitCode, SUCCESS_EXIT_CODE)) {
            throw new RuntimeException("Got unexpected error code: " + exitCode + ", docker logs: " + String.join("\n", logMessages));
        }
    }

    @SneakyThrows
    private void waitForContainerToExit(String containerId) {
        while (!containerExited(containerId)) {
            Thread.sleep(500);
        }
    }

    public List<String> getDockerLogs(String containerId) {
        LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(containerId);
        logContainerCmd.withStdOut(true).withStdErr(true);
        final List<String> logs = new ArrayList<>();
        try {
            logContainerCmd.exec(new LogContainerResultCallback() {
                @Override
                public void onNext(Frame item) {
                    logs.add(item.toString());
                }
            }).awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to retrieve logs of container " + containerId, e);
        }
        return logs;
    }

    private boolean containerExited(String containerId) {
        return Boolean.FALSE.equals(getContainerState(containerId).getRunning());
    }

    private InspectContainerResponse.ContainerState getContainerState(String containerId) {
        return dockerClient.inspectContainerCmd(containerId).exec().getState();
    }

    public void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }
}
