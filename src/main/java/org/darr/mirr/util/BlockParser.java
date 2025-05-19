package org.darr.mirr.util;

import org.darr.mirr.model.block.Block;
import org.darr.mirr.model.scene.SceneArena;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Block parser like *.scene files. It only parses text enclosed by pattern "block_name { block_content }
 * 'block_content' may contain another blocks. Therefore, parser build block tree as output result.
 */
public class BlockParser {
    private static final Integer END_OF_FILE_CODE = -1;

    public static SceneArena parse(String content) throws IOException {
        var sceneAreaBlock = new SceneArena();
        try (var reader = new BufferedReader(new StringReader(content))) {
            // Build indexed tree and indexed file content partition by line.
            // Each tree node is block that follows pattern 'block_name { block_content }'
            // Index tree represent line number where block is begins (character '{' from block content)
            // Index line represent line number of file content as parser it has read.
            // Index tree number correspond index line number. It means if you find '{' character in lines by index tree number.
            var lineCounter = new AtomicInteger(0);
            var treeLevel = new AtomicInteger(0);
            var blockIndexMap = new TreeMap<Integer, Block>();

            var lineIndexMap = reader
                    .lines()
                    .map(String::trim)
                    .filter(line -> !(line.isEmpty() || line.isBlank()))
                    .map(line -> {
                        var index = lineCounter.incrementAndGet();

                        if (line.startsWith("{")) {
                            Block levelBlock;

                            // search current level block
                            if (treeLevel.get() == 0) {
                                levelBlock = new Block();
                                sceneAreaBlock.add(levelBlock);
                            } else {
                                var childBlock = sceneAreaBlock.getLastAddedBlock();
                                for (int i = 1; i < treeLevel.get(); i++) {
                                    childBlock = childBlock.getLastChildBlock();
                                }
                                var nextChildBlock = new Block();
                                childBlock.addChild(nextChildBlock);

                                levelBlock = nextChildBlock;
                            }

                            blockIndexMap.put(index, levelBlock);
                            treeLevel.incrementAndGet();
                        }
                        if (line.startsWith("}")) {
                            treeLevel.decrementAndGet();
                        }

                        return Map.of(index, line);
                    })
                    .reduce(new HashMap<>(), (collector, streamItem) -> { collector.putAll(streamItem); return collector; });

            // fill blocks by data from read lines
            var blockKeyIndexList = new ArrayList<>(blockIndexMap.keySet());
            for(int iter = 0; iter < blockKeyIndexList.size(); iter++) {
                int blockIndexStart = blockKeyIndexList.get(iter);
                var block = blockIndexMap.get(blockIndexStart);
                // fill block name
                var blockName = lineIndexMap.get(blockIndexStart - 1);
                block.setName(blockName);
                // fill parameters
                int blockIndexEnd = calcBlockIndexEnd(iter, blockKeyIndexList);
                blockIndexEnd = blockIndexEnd == END_OF_FILE_CODE ? lineIndexMap.size() : blockIndexEnd;
                fillBlockParameter(block, lineIndexMap, blockIndexStart + 1, blockIndexEnd);
            }
        }
        return sceneAreaBlock;
    }

    private static int calcBlockIndexEnd(int curIter, List<Integer> blockKeyIndexList) {
        int nextIter = curIter + 1;
        if (nextIter >= blockKeyIndexList.size()) {
            return END_OF_FILE_CODE;
        } else {
            int nextBlockIndexStart = blockKeyIndexList.get(nextIter);
            // numeration starts with 1 because counter increase at start reading loop
            // one for the first line is always block name
            // one for the second line is always start block '{' character
            return nextBlockIndexStart - 2;
        }
    }

    private static void fillBlockParameter(Block block, Map<Integer, String> lineIndexMap, int blockIndexStart, int blockIndexEnd) {
        for (int i = blockIndexStart; i <= blockIndexEnd; i++) {
            var line = lineIndexMap.get(i);
            if (line.startsWith("}") || line.startsWith("{") || line.isBlank()) {
                continue;
            }
            block.addParameterString(line);
        }
    }
}
