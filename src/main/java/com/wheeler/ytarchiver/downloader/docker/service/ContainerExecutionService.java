package com.wheeler.ytarchiver.downloader.docker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class ContainerExecutionService {

    private final DockerClient dockerClient;

    public ContainerExecutionResult runContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
        waitForContainerToExit(containerId);
        ContainerExecutionResult executionResult = getExecutionResult(containerId);
        //CompletableFuture.runAsync(() -> removeContainer(containerId));
        return executionResult;
    }

    private ContainerExecutionResult getExecutionResult(String containerId) {
        InspectContainerResponse.ContainerState containerState = getContainerState(containerId);
        return new ContainerExecutionResult(containerState.getExitCodeLong(), getDockerLogs(containerId));
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
