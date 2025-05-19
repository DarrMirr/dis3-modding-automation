package org.darr.mirr.command.impl.models;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.command.Command;
import org.darr.mirr.util.CmdArgExtractor;
import org.darr.mirr.util.function.Try;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Slf4j
public class CollectModelFilesIntoDirCommand implements Command {
    private final Map<String, Predicate<String>> modelFileFilterMap = new HashMap<>();

    {
        modelFileFilterMap.put("g", fileName -> fileName.endsWith(".g"));
        modelFileFilterMap.put("a", fileName -> fileName.endsWith(".a"));
        modelFileFilterMap.put("t", fileName -> fileName.endsWith(".t"));
    }

    @Override
    public void run(String[] args) throws Exception {
        String sourceParam = CmdArgExtractor.getArgMandatory(args, "source");
        String targetParam = CmdArgExtractor.getArgMandatory(args, "target");

        var sourcePathString = CmdArgExtractor.getArgValue(sourceParam);
        var targetPathString = CmdArgExtractor.getArgValue(targetParam);
        var filterArray = "g,a";

        log.info("Parameter is parsed : source_path={}", sourcePathString);
        log.info("Parameter is parsed : target_path={}", targetPathString);
        log.info("Parameter is hardcoded : filter={}", filterArray);

        doWork(sourcePathString, targetPathString, filterArray);
    }

    private Predicate<String> toFilterPredicate(String filterArray) {
        var filterParts = filterArray.split(",");
        return Stream
                .of(filterParts)
                .map(String::trim)
                .filter(modelFileFilterMap::containsKey)
                .map(modelFileFilterMap::get)
                .reduce(Predicate::or)
                .orElseThrow(() -> new IllegalStateException("Filters are not found by filter array = " + filterArray));
    }

    private void doWork(String sourcePathDirString, String targetPathDirString, String filterArray) throws IOException {
        var filterPredicate = toFilterPredicate(filterArray);
        var sourcePathDir = Path.of(sourcePathDirString);

        try (var fileStream = Files.walk(sourcePathDir)) {
            fileStream
                    .filter(path ->
                            filterPredicate.test(path.getFileName().toString()))
                    .forEach(Try.of(sourceModelPath -> {
                        log.debug("Copy file : source={}, target={}", sourceModelPath, targetPathDirString);
                        Files.copy(sourceModelPath, Path.of(targetPathDirString, sourceModelPath.getFileName().toString()));
                    }));
        }
    }
}
