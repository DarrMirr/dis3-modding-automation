package org.darr.mirr.model.scene;

import org.darr.mirr.model.block.Block;
import org.darr.mirr.util.BlockParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SceneArenaTest {
    private final static String RESOURCE_PATH = "arena_back_land_empire_01_noon.scene";
    private static SceneArena sceneArena;

    @BeforeAll
    public static void init() throws URISyntaxException, IOException {
        var url = Thread.currentThread().getContextClassLoader().getResource(RESOURCE_PATH);
        var resourceContent = Files.readString(Paths.get(url.toURI()));
        sceneArena = BlockParser.parse(resourceContent);
    }

    @Test
    void findAllByParamName() {
        var result = sceneArena
                .getOperations()
                .findAllBlockByParameterName("load_counter");

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getName(), equalTo("globalsettings"));
        assertThat(result.get(0).getParameters(), hasSize(23));
    }

    @Test
    void findAllByParamValue() {
        var result = sceneArena
                .getOperations()
                .findAllBlocksByParameterValue("\"npc\" \"3\"");

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getName(), equalTo("child group \"_n_pass_Point1281\""));
        assertThat(result.get(0).getParameters(), hasSize(19));
    }

    @Test
    void findAllContainsParamValue() {
        var result = sceneArena.getOperations().findAllBlockContainsParamValue("npc");
        var paramNames = result
                .stream()
                .map(Block::getName)
                .toList();

        assertThat(result, hasSize(8));
        assertThat(paramNames, containsInAnyOrder(
                "child group \"_n_pass_Point1281\"",
                "child group \"_n_pass_Point1280\"",
                "child group \"_n_pass_Point1279\"",
                "child group \"_n_pass_Point1278\"",
                "child group \"_n_pass_Point1277\"",
                "child group \"_n_pass_Point1275\"",
                "child group \"_n_pass_Point1272\"",
                "child group \"_n_pass_Point1267\""
        ));
    }

    @Test
    void updateParameterValues() {
        sceneArena.getOperations().updateParameterValues(
                "coords",
                "-1.000000,0.000000,-1.000000,0.000000,-1.000000,0.000000",
                params -> params.getValue().equals("\"npc\" \"3\""));

        var result = sceneArena.getOperations().findAllBlocksByParameterValue("\"npc\" \"3\"");
        var coordsParam = result
                .get(0)
                .getOperations()
                .findFirstCurrentBlockParameterByName("coords")
                .get();

        assertThat(coordsParam.getValue(), equalTo("-1.000000,0.000000,-1.000000,0.000000,-1.000000,0.000000"));
    }
}