package org.darr.mirr.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.command.Command;
import org.darr.mirr.model.arenazone.ArenaZone;
import org.darr.mirr.model.arenazone.operation.ArenaZonePrinter;
import org.darr.mirr.util.CmdArgExtractor;
import org.darr.mirr.util.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
public class PrintAreaZonesCommand implements Command {
    @Override
    public void run(String[] args) throws Exception {
        var sourceParam = CmdArgExtractor.getArgMandatory(args, "source");
        var sourcePathString = CmdArgExtractor.getArgValue(sourceParam);

        log.info("Source parameter : {}", sourceParam);

        doWork(sourcePathString);
    }

    private void doWork(String sourcePathString) throws IOException {
        var arenaZonePrinter = new ArenaZonePrinter();

        FileUtils
                .readFileAsLineList(Path.of(sourcePathString), line -> line.startsWith("arenazone "))
                .stream()
                .map(ArenaZone::of)
                .forEach(arenaZonePrinter::add);

        arenaZonePrinter.print();
    }
}
