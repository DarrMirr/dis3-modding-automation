package org.darr.mirr.command.impl.scene;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.command.Command;
import org.darr.mirr.model.scene.SceneArena;
import org.darr.mirr.task.scene.DeleteParameterByNameSceneTask;
import org.darr.mirr.task.scene.SceneTask;
import org.darr.mirr.task.scene.SetUnitArenaLocatorSceneTask;
import org.darr.mirr.util.BlockParser;
import org.darr.mirr.util.CmdArgExtractor;
import org.darr.mirr.util.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CopyUnitArenaLocationCommand implements Command {

    @Override
    public void run(String[] args) throws IOException {
        String sourceParam = CmdArgExtractor.getArgMandatory(args, "source");
        String targetParam = CmdArgExtractor.getArgMandatory(args, "target");

        log.info("Source parameter : {}", sourceParam);
        log.info("Target parameter : {}", targetParam);

        var sourcePath = CmdArgExtractor.getArgValue(sourceParam);
        var targetParamValue = CmdArgExtractor.getArgValue(targetParam);
        var targetPathList = FileUtils.resolvePath(targetParamValue);

        log.debug("Source path : {}", sourcePath);
        log.debug("Target paths : {}", targetPathList);

        var sourceContent = FileUtils.readContent(sourcePath);
        var targetContentMap = FileUtils
                .readContentAndFilter(targetPathList, (fileName, fileContent) -> fileName.endsWith(".scene"));

        var sourceSceneArena = BlockParser.parse(sourceContent);
        var taskList = getTaskList(sourceSceneArena);
        var outputDirectory = FileUtils.createOutputDirectory(FileUtils.getParentDirectoryPath(targetParamValue), "output");

        doWork(taskList, targetContentMap, outputDirectory);
    }

    private List<SceneTask> getTaskList(SceneArena sourceSceneArena) {
        return List.of(
                new SetUnitArenaLocatorSceneTask(sourceSceneArena),
                new DeleteParameterByNameSceneTask("resourcefile")
        );
    }

    private void doWork(List<SceneTask> sceneTaskList, Map<String, String> targetContentMap, Path outputDirectory) throws IOException {
        for (var targetContent : targetContentMap.entrySet()) {
            var targetSceneArena = BlockParser.parse(targetContent.getValue());

            for (var task : sceneTaskList) {
                log.debug("Start task : file={}, task_name={}", targetContent.getKey(), task.getClass().getSimpleName());
                task.execute(targetSceneArena);

                log.debug("Finish task : file={}, task_name={}", targetContent.getKey(), task.getClass().getSimpleName());
            }

            var newTargetContent = targetSceneArena.serialize();
            var newTargetFilePath = outputDirectory.resolve(targetContent.getKey());
            log.info("Write content to output directory : output_file={}", newTargetFilePath);
            Files.writeString(newTargetFilePath, newTargetContent, Charset.forName("windows-1251"));
        }
    }
}
