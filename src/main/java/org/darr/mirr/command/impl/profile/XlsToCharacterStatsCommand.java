package org.darr.mirr.command.impl.profile;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.command.Command;
import org.darr.mirr.model.profile.XlsCharacterProfile;
import org.darr.mirr.util.CmdArgExtractor;
import org.darr.mirr.util.FileUtils;
import org.darr.mirr.util.XlsUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class XlsToCharacterStatsCommand implements Command {

    @Override
    public void run(String[] args) throws Exception {
        String sourceParam = CmdArgExtractor.getArgMandatory(args, "source");

        log.info("Source parameter : {}", sourceParam);
        var sourcePath = CmdArgExtractor.getArgValue(sourceParam);

        log.debug("Source paths : {}", sourcePath);

        var outputDirectory = FileUtils.createOutputDirectory(FileUtils.getParentDirectoryPath(sourcePath), "output");

        doWork(sourcePath, outputDirectory);
    }

    private void doWork(String xlsPath, Path outputDirectory) throws IOException {
        try (var workbook = XlsUtils.openWorkbook(Paths.get(xlsPath))) {
            var charactersSheet = workbook.getSheet("Characters");

            var headerRow = charactersSheet.getRow(0);
            var xlsCharacterProfile = new XlsCharacterProfile(headerRow);

            var formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            xlsCharacterProfile.operations().readFromXls(charactersSheet, formulaEvaluator);
            xlsCharacterProfile
                    .operations()
                    .toFileContentCharacterStats()
                    .forEach((fileName, fileContent) -> {
                        try {
                            var filePath = outputDirectory.resolve(fileName);
                            Files.writeString(filePath, fileContent);
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    });
        }
    }
}
