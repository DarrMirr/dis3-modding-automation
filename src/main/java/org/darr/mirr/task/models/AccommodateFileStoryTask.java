package org.darr.mirr.task.models;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Path;

@RequiredArgsConstructor
public class AccommodateFileStoryTask {
    private final AccommodateFileLeaderTask leaderTask;
    private final AccommodateFileUnitTask unitTask;

    public void accommodate(Path sourcePath) throws IOException {
        if (sourcePath.toFile().getName().contains("_set")) {
            leaderTask.accommodate(sourcePath, this::getLeaderDirName);
        } else {
            unitTask.accommodate(sourcePath);
        }
    }

    private String getLeaderDirName(Path path) {
        var parts = path.toFile().getName().split("_");
        boolean isNextLeaderDirNameToken = false;
        for (String part : parts) {
            if (isNextLeaderDirNameToken) {
                return part
                        .split("\\.")[0]
                        .toLowerCase();
            }
            if (part.equals("story")) {
                isNextLeaderDirNameToken = true;
            }
        }
        throw new IllegalStateException("Could not extract leader dir name from path = " + path);
    }
}
