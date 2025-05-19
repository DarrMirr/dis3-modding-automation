package org.darr.mirr.task.scene;

import org.darr.mirr.model.block.Block;
import org.darr.mirr.model.scene.SceneArena;
import org.darr.mirr.util.BlockParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class InsertChildSceneBlockSceneTaskTest {
    private final static String TARGET_PATH = "arena_back_land_empire_01_noon.scene";
    private final static String SOURCE_PATH = "block_to_insert.scene";
    private static SceneArena targetSceneArena;
    private static SceneArena sourceSceneArena;

    @BeforeAll
    public static void init() throws URISyntaxException, IOException {
        var url = Thread.currentThread().getContextClassLoader().getResource(TARGET_PATH);
        var resourceContent = Files.readString(Paths.get(url.toURI()));
        targetSceneArena = BlockParser.parse(resourceContent);

        url = Thread.currentThread().getContextClassLoader().getResource(SOURCE_PATH);
        resourceContent = Files.readString(Paths.get(url.toURI()));
        sourceSceneArena = BlockParser.parse(resourceContent);
    }

    @Test
    void execute() {
        var insertedBlockNames = new ArrayList<String>();

        for(var block : sourceSceneArena.getBlocks()) {
            var task = new InsertChildSceneBlockSceneTask("group \"Scene Root\"", block);
            task.execute(targetSceneArena);
            insertedBlockNames.add(block.getName());
        }

        var insertedBlocks = targetSceneArena
                .getOperations()
                .findOneBlockByName("group \"Scene Root\"")
                .stream()
                .map(Block::getChildBlocks)
                .flatMap(List::stream)
                .filter(childBlock ->
                        insertedBlockNames.contains(childBlock.getName()))
                .toList();

        assertThat(insertedBlocks, hasSize(2));
    }
}