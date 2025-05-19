package org.darr.mirr.task.models;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

@Slf4j
public class AccommodateFileLeaderTask {

    public void accommodate(Path sourcePath) throws IOException {
        accommodate(sourcePath, this::getLeaderDirName);
    }

    void accommodate(Path sourcePath, Function<Path, String> getLeaderDirNameFunction) throws IOException {
        var leaderDirName = getLeaderDirNameFunction.apply(sourcePath);
        var leaderDirPath = sourcePath.getParent().resolve(leaderDirName);

        if (Files.notExists(leaderDirPath)) {
            Files.createDirectory(leaderDirPath);
        }

        Path targetPath;
        if (isLeaderModelSet(sourcePath)) {
            var modelSetDirName = getLeaderDirModelSetName(sourcePath);
            targetPath = leaderDirPath.resolve(modelSetDirName);

            if (Files.notExists(targetPath)) {
                Files.createDirectory(targetPath);
            }
        } else {
            targetPath = leaderDirPath;
        }

        var fileName = sourcePath.toFile().getName();
        targetPath = targetPath.resolve(fileName);

        log.debug("Move leader file : source={}, target={}", sourcePath, targetPath);
        Files.move(sourcePath, targetPath);
    }

    private boolean isLeaderModelSet(Path path) {
        return path.toFile().getName().contains("_set");
    }

    private String getLeaderDirName(Path path) {
        var parts = path.toFile().getName().split("_");
        for (String part : parts) {
            if (part.contains("leader") && part.contains("-")) {
                return part
                        .split("\\.")[0]
                        .toLowerCase();
            }
        }
        throw new IllegalStateException("Could not extract leader dir name from path = " + path);
    }

    private String getLeaderDirModelSetName(Path path) {
        var parts = path.toFile().getName().split("_");
        return parts[parts.length - 1]
                .split("\\.")[0]
                .toLowerCase();
    }
}
