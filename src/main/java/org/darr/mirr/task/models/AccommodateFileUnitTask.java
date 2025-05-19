package org.darr.mirr.task.models;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class AccommodateFileUnitTask {

    public void accommodate(Path sourcePath) throws IOException {
        var unitDirName = getUnitDirName(sourcePath);
        var targetPath = sourcePath.getParent().resolve(unitDirName);

        if (Files.notExists(targetPath)) {
            Files.createDirectory(targetPath);
        }

        var fileName = sourcePath.toFile().getName();
        targetPath = targetPath.resolve(fileName);

        log.debug("Move unit file : source={}, target={}", sourcePath, targetPath);
        Files.move(sourcePath, targetPath);
    }

    private String getUnitDirName(Path path) {
        var fileName = path.toFile().getName();

        int unitNameIdx = 2;
        if (fileName.contains("_ship") && fileName.contains("_small_")) {
            unitNameIdx = 3;
        }

        var parts = fileName.split("_");
        var unitName = parts[unitNameIdx]
                .split("\\.")[0]
                .toLowerCase();

        // unit with name separated by "_" character
        if (unitName.equals("hydra")) {
            unitName = unitName + "_" + parts[unitNameIdx + 1];
        }

        unitName = cutOffFileExtension(unitName);

        return unitName;
    }

    private String cutOffFileExtension(String name) {
        return name.contains(".") ? name.split("\\.")[0] : name;
    }
}
