package org.darr.mirr.task.scene;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.model.block.Block;
import org.darr.mirr.model.scene.SceneArena;
import org.darr.mirr.model.scene.SceneNameAttribute;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class FixPassPointNameSceneTask implements SceneTask {
    private static final String BASE_NAME = "pass_Point";
    private static final String PLAYER_PASS_NAME = "_p_" + BASE_NAME;
    private static final String NPC_PASS_NAME = "_n_" + BASE_NAME;
    private static final String CENTER_PASS_NAME = "_c_" + BASE_NAME;
    private static final String NO_PASS_PASS_NAME = "_0" + BASE_NAME;

    @Override
    public void execute(SceneArena sceneArena) {
        sceneArena
                .getOperations()
                .findAllBlockContainsParamValue(BASE_NAME)
                .forEach(block -> {
                    // TODO: 04.12.2024 make invoke only one method instead of every
                    fixOptionallyPassPointUnit(block, SceneNameAttribute.PLAYER, PLAYER_PASS_NAME);
                    fixOptionallyPassPointUnit(block, SceneNameAttribute.NPC, NPC_PASS_NAME);
                    fixOptionallyPassPointUnit(block, SceneNameAttribute.CENTER, CENTER_PASS_NAME);
                    fixOptionallyPassPointNoPass(block);
                });
    }

    private void fixOptionallyPassPointUnit(Block block, String attrName, String validPassNamePart) {
        log.debug("Start optional pass point fixing : block_name={}, attr_name={}, valid_pass_name_without_id={}", block.getName(), attrName, validPassNamePart);
        var parameter = block.getOperations().findFirstParameterContainsValue(attrName);
        var hexInfoParent = block.getOperations().findFirstParameterContainsValue("\"parent\" \"hexinfo\"");
        if (parameter.isPresent() && hexInfoParent.isPresent()) {
            log.debug("Fix is needed : block_name={}, attr_name={}, valid_pass_name_without_id={}", block.getName(), attrName, validPassNamePart);
            fixOptionallyPassPoint(block, validPassNamePart);
        } else {
            log.debug("Fix is not required : block_name={}, attr_name={}, valid_pass_name_without_id={}", block.getName(), attrName, validPassNamePart);
        }
    }

    private void fixOptionallyPassPointNoPass(Block block) {
        log.debug("Start optional pass point fixing : block_name={}, valid_pass_name_without_id={}", block.getName(), NO_PASS_PASS_NAME);
        var hexInfoParent = block
                .getOperations()
                .findFirstParameterContainsValue("\"parent\" \"hexinfo\"");
        var player = block
                .getOperations()
                .findFirstParameterContainsValue(SceneNameAttribute.PLAYER);
        var npc = block
                .getOperations()
                .findFirstParameterContainsValue(SceneNameAttribute.NPC);
        var center = block
                .getOperations()
                .findFirstParameterContainsValue(SceneNameAttribute.CENTER);

        if (hexInfoParent.isPresent() && player.isEmpty() && npc.isEmpty() && center.isEmpty()) {
            log.debug("Fix is needed : block_name={}, valid_pass_name_without_id={}", block.getName(), NO_PASS_PASS_NAME);
            fixOptionallyPassPoint(block, NO_PASS_PASS_NAME);
        } else {
            log.debug("Fix is not required : block_name={}, valid_pass_name_without_id={}", block.getName(), NO_PASS_PASS_NAME);
        }
    }

    private void fixOptionallyPassPoint(Block block, String validPassNamePart) {
        if (isNotValidPassNameBlock(block.getName(), validPassNamePart)) {
            var nameParts = block
                    .getName()
                    .split(BASE_NAME);
            var id = nameParts[1].replaceAll("\"","");
            var validPassName = createValidPassNameBlock(validPassNamePart, id);

            nameParts = nameParts[0].split("\\s");
            var nameSuffix = Stream
                    .of(nameParts)
                    .limit(nameParts.length - 1)
                    .collect(Collectors.joining(" "));

            block.setName(nameSuffix + " " + validPassName);
        }
        block.getOperations()
                .findFirstParameterContainsValue(BASE_NAME)
                .filter(attribute -> isNotValidPassNameAttr(attribute, validPassNamePart))
                .ifPresent(attribute -> {
                    var passName = attribute.getValue().split("\"\\s\"")[1];
                    var id = passName.split(BASE_NAME)[1].replaceAll("\"","");
                    var validPassName = createValidPassNameAttr(validPassNamePart, id);
                    attribute.setValue(validPassName);
                });
    }


    private boolean isNotValidPassNameBlock(String blockName, String expectedName) {
        return !blockName.startsWith(expectedName);
    }

    private boolean isNotValidPassNameAttr(Block.Parameter attr, String expectedName) {
        var passName = attr.getValue().split("\"\\s\"")[1];
        return isNotValidPassNameBlock(passName, expectedName);
    }

    private String createValidPassNameBlock(String validPassNamePart, String id) {
        return validPassNamePart + id;
    }

    private String createValidPassNameAttr(String validPassNamePart, String id) {
        return "\"name\" \"" + createValidPassNameBlock(validPassNamePart, id) + "\"";
    }
}
