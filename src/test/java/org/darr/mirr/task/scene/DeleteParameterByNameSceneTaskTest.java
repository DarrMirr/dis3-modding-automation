package org.darr.mirr.task.scene;

import org.darr.mirr.model.scene.SceneArena;
import org.darr.mirr.task.scene.DeleteParameterByNameSceneTask;
import org.darr.mirr.task.scene.SceneTask;
import org.darr.mirr.util.BlockParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

class DeleteParameterByNameSceneTaskTest {
    private static final String INCORRECT_BLOCK = """
            child group "_0pass_Point2066"
            {
            	isrotate 0, 0, 0;
            	rotate 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	isfluctuation 0, 0, 0;
            	fluctuation 0.000000, 0.000000, 0.000000;
            	fluctuationamplitude 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	uid 229 99933356
            	coords 2.249998,0.000000,-1.299146,0.000000,0.000000,0.000000
            	obbdata -2.249998,0.000000,1.299146,-2.249998,0.000000,1.299146
            	dwflags 0
            	plvl 500
            	attr "name" "_0pass_Point2066"
            	attr "parent" "hexinfo"
            	attr "dwNode" "337600992"
            	attr "dwParent" "337599896"
            	resourcefile "Resources\\Hexagons\\Models\\Arena\\arena_back_land_empire\\arena_back_land_empire_01.g",1
            }
            """;

    private static SceneArena sceneArena;
    private final SceneTask sceneTask = new DeleteParameterByNameSceneTask("resourcefile");

    @BeforeAll
    public static void init() throws IOException {
        sceneArena = BlockParser.parse(INCORRECT_BLOCK);
    }

    @Test
    void execute() {
        sceneTask.execute(sceneArena);

        var blockList = sceneArena
                .getOperations()
                .findAllBlockContainsParamValue("resourcefile");

        assertThat(blockList, empty());
    }
}