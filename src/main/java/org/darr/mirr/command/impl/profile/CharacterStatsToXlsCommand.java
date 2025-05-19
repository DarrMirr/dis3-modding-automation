package org.darr.mirr.command.impl.profile;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.BaseFormulaEvaluator;
import org.darr.mirr.command.Command;
import org.darr.mirr.model.profile.CharacterStats;
import org.darr.mirr.model.profile.XlsCharacterProfile;
import org.darr.mirr.util.CmdArgExtractor;
import org.darr.mirr.util.FileUtils;
import org.darr.mirr.util.XlsUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
public class CharacterStatsToXlsCommand implements Command {
    private static final String PATH_TEMPLATE_PROFILE = "templates/dis3_profiles.xlsx";

    @Override
    public void run(String[] args) throws Exception {
        String sourceParam = CmdArgExtractor.getArgMandatory(args, "source");

        log.info("Source parameter : {}", sourceParam);

        var sourcePath = CmdArgExtractor.getArgValue(sourceParam);
        var sourcePathList = FileUtils.resolvePath(sourcePath);

        log.debug("Source paths : {}", sourcePath);

        var sourceContentMap = FileUtils
                .readContentAndFilter(sourcePathList, (fileName, fileContent) -> fileName.endsWith(".char"));

        var outputDirectory = FileUtils.createOutputDirectory(FileUtils.getParentDirectoryPath(sourcePathList.get(0)), "output");

        doWork(sourceContentMap, outputDirectory);
    }

    private void doWork(Map<String, String> sourceContentMap, Path outputDirectory) throws IOException {
        var outputXls = outputDirectory.resolve("dis3_profiles.xlsx");

        copyXlsTemplateToOutputDir(outputXls);
        fillXlsTemplate(sourceContentMap, outputXls);
    }

    private void copyXlsTemplateToOutputDir(Path outputXls) throws IOException {
        try (var xlsTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH_TEMPLATE_PROFILE);
             var outputStream = Files.newOutputStream(outputXls);
        ) {
            assert xlsTemplateStream != null;
            xlsTemplateStream.transferTo(outputStream);
        }
    }

    private void fillXlsTemplate(Map<String, String> sourceContentMap, Path outputXls) throws IOException {
        try (var workbook = XlsUtils.openWorkbook(outputXls)) {
            var charactersSheet = workbook.getSheet("Characters");

            var headerRow = charactersSheet.getRow(0);
            var xlsCharacterProfile = new XlsCharacterProfile(headerRow);

            for (var sourceContent : sourceContentMap.entrySet()) {
                var charStats = CharacterStats.of(sourceContent.getKey(), sourceContent.getValue());
                xlsCharacterProfile.operations().addCharacterStats(charStats);
            }

            xlsCharacterProfile.operations().writeIntoXls(charactersSheet);
            BaseFormulaEvaluator.evaluateAllFormulaCells(workbook);

            try (var output = Files.newOutputStream(outputXls)) {
                workbook.write(output);
            }
        }
    }

}
