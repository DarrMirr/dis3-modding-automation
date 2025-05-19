package org.darr.mirr.command.impl.scene;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.command.Command;
import org.darr.mirr.task.scene.FixPassPointNameSceneTask;
import org.darr.mirr.util.BlockParser;
import org.darr.mirr.util.CmdArgExtractor;
import org.darr.mirr.util.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class FixPassPointNameSceneCommand implements Command {

    @Override
    public void run(String[] args) throws Exception {
        String sourceParam = CmdArgExtractor.getArgMandatory(args, "source");

        log.info("Source parameter : {}", sourceParam);

        var sourceParamValue = CmdArgExtractor.getArgValue(sourceParam);

        var outputDirectory = FileUtils.createOutputDirectory(FileUtils.getParentDirectoryPath(sourceParamValue), "output");
        var sourcePathList = FileUtils.resolvePath(sourceParamValue);

        log.debug("Source paths : {}", sourcePathList);
        log.debug("Output dir path : {}", outputDirectory);

        doWork(sourcePathList, outputDirectory);
    }

    public void doWork(List<String> sourcePathList, Path outputDirectory) throws IOException {
        var contentMap = FileUtils.readContent(sourcePathList);
        var task = new FixPassPointNameSceneTask();

        for (var sourceContent : contentMap.entrySet()) {
            var targetSceneArena = BlockParser.parse(sourceContent.getValue());

            log.debug("Start task : file={}, task_name={}", sourceContent.getKey(), task.getClass().getSimpleName());
            task.execute(targetSceneArena);
            log.debug("Finish task : file={}, task_name={}", sourceContent.getKey(), task.getClass().getSimpleName());

            var newTargetContent = targetSceneArena.serialize();
            var newTargetFilePath = outputDirectory.resolve(sourceContent.getKey());
            log.info("Write content to output directory : output_file={}", newTargetFilePath);
            Files.writeString(newTargetFilePath, newTargetContent, Charset.forName("windows-1251"));
        }
    }
}
