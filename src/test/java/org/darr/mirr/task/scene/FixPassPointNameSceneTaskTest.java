package org.darr.mirr.task.scene;

import org.darr.mirr.model.scene.SceneArena;
import org.darr.mirr.task.scene.FixPassPointNameSceneTask;
import org.darr.mirr.task.scene.SceneTask;
import org.darr.mirr.util.BlockParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class FixPassPointNameSceneTaskTest {
    private static final String INCORRECT_SCENE_CONTENT = """
            	child group "_pass_Point1279"\s
            {
            	isrotate 0, 0, 0;
            	rotate 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	isfluctuation 0, 0, 0;
            	fluctuation 0.000000, 0.000000, 0.000000;
            	fluctuationamplitude 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	uid 240 87780236
            	coords 17.999998,-0.000000,12.990346,0.000000,0.000000,0.000000
            	obbdata -17.999998,0.000000,-12.990346,-17.999998,0.000000,-12.990346
            	dwflags 0
            	plvl 500
            	attr "name" "_pass_Point1279"
            	attr "parent" "hexinfo"
            	attr "pass" "1"
            	attr "npc" "0"
            	attr "keep_land_nodes" "1"
            	attr "dwNode" "337611952"
            	attr "dwParent" "337599896"
            }
            
            	child group "_pass_Point1239"\s
            {
            	isrotate 0, 0, 0;
            	rotate 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	isfluctuation 0, 0, 0;
            	fluctuation 0.000000, 0.000000, 0.000000;
            	fluctuationamplitude 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	uid 262 92295320
            	coords 2.250000,-0.000000,16.887459,0.000000,0.000000,0.000000
            	obbdata -2.250000,0.000000,-16.887459,-2.250000,0.000000,-16.887459
            	dwflags 0
            	plvl 500
            	attr "name" "_pass_Point1239"
            	attr "parent" "hexinfo"
            	attr "dwNode" "337636064"
            	attr "dwParent" "337599896"
            }
            
            	child group "_pass_Point1277"\s
            {
            	isrotate 0, 0, 0;
            	rotate 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	isfluctuation 0, 0, 0;
            	fluctuation 0.000000, 0.000000, 0.000000;
            	fluctuationamplitude 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	uid 242 87778356
            	coords 15.749996,-0.000000,9.093229,0.000000,0.000000,0.000000
            	obbdata -15.749996,0.000000,-9.093229,-15.749996,0.000000,-9.093229
            	dwflags 0
            	plvl 500
            	attr "name" "_pass_Point1277"
            	attr "parent" "hexinfo"
            	attr "pass" "1"
            	attr "player" "8"
            	attr "keep_land_nodes" "1"
            	attr "dwNode" "337614144"
            	attr "dwParent" "337599896"
            }
            
            	child group "_pass_Point029"\s
            {
            	isrotate 0, 0, 0;
            	rotate 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	isfluctuation 0, 0, 0;
            	fluctuation 0.000000, 0.000000, 0.000000;
            	fluctuationamplitude 0.000000, 0.000000, 0.000000;
            	apllyfluctuationtochildren 0;
            	uid 274 93652732
            	coords 11.250001,0.000000,9.093229,0.000000,0.000000,0.000000
            	obbdata -11.250001,0.000000,-9.093229,-11.250001,0.000000,-9.093229
            	dwflags 0
            	plvl 500
            	attr "name" "_pass_Point029"
            	attr "parent" "hexinfo"
            	attr "pass" "1"
            	attr "center" "1"
            	attr "keep_land_nodes" "1"
            	attr "nd_nodes" "1"
            	attr "dwNode" "337618528"
            	attr "dwParent" "337599896"
            }           
            """;

    private static SceneArena sceneArena;
    private final SceneTask sceneTask = new FixPassPointNameSceneTask();

    @BeforeAll
    public static void init() throws IOException {
        sceneArena = BlockParser.parse(INCORRECT_SCENE_CONTENT);
    }

    @Test
    void execute() {
        sceneTask.execute(sceneArena);

        var npcBlocks = sceneArena.getOperations().findAllBlockContainsParamValue("npc");
        var playerBlocks = sceneArena.getOperations().findAllBlockContainsParamValue("player");
        var noPassBlocks = sceneArena.getOperations().findAllBlockContainsParamValue("_0pass");
        var centerBlock = sceneArena.getOperations().findAllBlockContainsParamValue("_c_");

        assertThat(npcBlocks, hasSize(1));
        assertThat(npcBlocks.get(0).getName(), equalTo("child group _n_pass_Point1279"));
        assertThat(npcBlocks.get(0).getOperations().findFirstParameterContainsValue("pass_Point").get().getValue(),
                equalTo("\"name\" \"_n_pass_Point1279\""));

        assertThat(playerBlocks, hasSize(1));
        assertThat(playerBlocks.get(0).getName(), equalTo("child group _p_pass_Point1277"));
        assertThat(playerBlocks.get(0).getOperations().findFirstParameterContainsValue("pass_Point").get().getValue(),
                equalTo("\"name\" \"_p_pass_Point1277\""));

        assertThat(noPassBlocks, hasSize(1));
        assertThat(noPassBlocks.get(0).getName(), equalTo("child group _0pass_Point1239"));
        assertThat(noPassBlocks.get(0).getOperations().findFirstParameterContainsValue("pass_Point").get().getValue(),
                equalTo("\"name\" \"_0pass_Point1239\""));

        assertThat(centerBlock, hasSize(1));
        assertThat(centerBlock.get(0).getName(), equalTo("child group _c_pass_Point029"));
        assertThat(centerBlock.get(0).getOperations().findFirstParameterContainsValue("pass_Point").get().getValue(),
                equalTo("\"name\" \"_c_pass_Point029\""));
    }
}