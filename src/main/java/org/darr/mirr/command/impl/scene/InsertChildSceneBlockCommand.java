package org.darr.mirr.command.impl.scene;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.command.Command;
import org.darr.mirr.model.scene.SceneArena;
import org.darr.mirr.task.scene.InsertChildSceneBlockSceneTask;
import org.darr.mirr.util.BlockParser;
import org.darr.mirr.util.CmdArgExtractor;
import org.darr.mirr.util.FileUtils;
import org.darr.mirr.util.function.FunctionUtil;
import org.darr.mirr.util.function.Try;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class InsertChildSceneBlockCommand implements Command {

    @Override
    public void run(String[] args) throws Exception {
        var sourceParam = CmdArgExtractor.getArgMandatory(args, "source");
        var targetParam = CmdArgExtractor.getArgMandatory(args, "target");
        var parentParam = CmdArgExtractor.getArgMandatory(args, "parent");

        var sourcePathString = CmdArgExtractor.getArgValue(sourceParam);
        var targetPathString = CmdArgExtractor.getArgValue(targetParam);
        var parentNameString = CmdArgExtractor.getArgValue(parentParam);

        log.info("Parameter is parsed : source_path={}", sourcePathString);
        log.info("Parameter is parsed : target_path={}", targetPathString);
        log.info("Parameter is parsed : parent_name={}", parentNameString);

        doWork(sourcePathString, targetPathString, parentNameString);
    }

    private Map<String, SceneArena> prepareTarget(String targetPath) {
        var targetPathList = FileUtils.resolveToFileList(targetPath);
        var targetContentMap = FileUtils.readContent(targetPathList);
        return targetContentMap
                .entrySet()
                .stream()
                .map(Try.of(entry -> {
                    var sceneArena = BlockParser.parse(entry.getValue());
                    return Map.of(entry.getKey(), sceneArena);
                }))
                .reduce(new HashMap<>(), FunctionUtil.concatMap());
    }

    private SceneArena prepareSource(String sourcePath) throws IOException {
        var sourceContent = Files.readString(Paths.get(sourcePath));
        return BlockParser.parse(sourceContent);
    }

    private void doWork(String sourcePath, String targetPath, String parentName) throws IOException {
        var sourceSceneArena = prepareSource(sourcePath);
        var outputSceneMap = prepareTarget(targetPath);

        for (var block : sourceSceneArena.getBlocks()) {
            for (var targetEntry : outputSceneMap.entrySet()) {
                log.debug("Insert block : block={}, parent_name={}, target={}", block, parentName, targetEntry.getKey());
                new InsertChildSceneBlockSceneTask(parentName, block)
                        .execute(targetEntry.getValue());
            }
        }

        var outputDirectory = FileUtils.createOutputDirectory(FileUtils.getParentDirectoryPath(targetPath), "output");
        for (var targetEntry : outputSceneMap.entrySet()) {
            var newTargetContent = targetEntry
                    .getValue()
                    .serialize();
            var newTargetFilePath = outputDirectory.resolve(targetEntry.getKey());
            log.info("Write content to output directory : output_file={}", newTargetFilePath);
            Files.writeString(newTargetFilePath, newTargetContent, Charset.forName("windows-1251"));
        }
    }
}
