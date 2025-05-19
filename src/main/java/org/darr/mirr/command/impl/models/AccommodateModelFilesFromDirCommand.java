package org.darr.mirr.command.impl.models;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.command.Command;
import org.darr.mirr.task.models.AccommodateFileLeaderTask;
import org.darr.mirr.task.models.AccommodateFileStoryTask;
import org.darr.mirr.task.models.AccommodateFileUnitTask;
import org.darr.mirr.util.CmdArgExtractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

@Slf4j
public class AccommodateModelFilesFromDirCommand implements Command {
    private final AccommodateFileLeaderTask leaderTask = new AccommodateFileLeaderTask();
    private final AccommodateFileUnitTask unitTask = new AccommodateFileUnitTask();
    private final AccommodateFileStoryTask storyTask = new AccommodateFileStoryTask(leaderTask, unitTask);

    @Override
    public void run(String[] args) throws Exception {
        String sourceParam = CmdArgExtractor.getArgMandatory(args, "source");

        var sourcePathString = CmdArgExtractor.getArgValue(sourceParam);

        log.info("Parameter is parsed : source_path={}", sourcePathString);

        doWork(sourcePathString);
    }

    private void doWork(String sourcePathDirString) throws IOException {
        Predicate<Path> modelFilesOnly = path -> path.toFile().getName().endsWith(".g")
                || path.toFile().getName().endsWith(".a")
                || path.toFile().getName().endsWith(".t");
        var sourcePathDir = Path.of(sourcePathDirString);

        try (var fileStream = Files.walk(sourcePathDir)) {
            fileStream
                    .filter(modelFilesOnly)
                    .forEach(sourceModelPath -> {
                        try {
                            if (isLeaderFile(sourceModelPath)) {
                                leaderTask.accommodate(sourceModelPath);
                            } else if (isUnitFile(sourceModelPath)) {
                                unitTask.accommodate(sourceModelPath);
                            } else if (isStoryFile(sourceModelPath)) {
                                storyTask.accommodate(sourceModelPath);
                            } else {
                                log.debug("Unsupported file : path={}", sourceModelPath);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private boolean isStoryFile(Path path) {
        return path.toFile().getName().contains("story");
    }

    private boolean isLeaderFile(Path path) {
        return path.toFile().getName().contains("_leader") && !isStoryFile(path);
    }

    private boolean isUnitFile(Path path) {
        return path.toFile().getName().startsWith("character_") && !isLeaderFile(path);
    }

}
