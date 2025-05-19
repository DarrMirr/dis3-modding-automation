package org.darr.mirr.task.scene;

import lombok.RequiredArgsConstructor;
import org.darr.mirr.model.block.Block;
import org.darr.mirr.model.scene.SceneArena;
import org.darr.mirr.model.scene.SceneNameAttribute;
import org.darr.mirr.model.scene.SceneNameParameter;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class SetUnitArenaLocatorSceneTask implements SceneTask {
    private final SceneArena source;

    @Override
    public void execute(SceneArena target) {
        update(source, target, SceneNameAttribute.NPC);
        update(source, target, SceneNameAttribute.PLAYER);
    }

    private void update(SceneArena source, SceneArena target, String filterParamValue) {
        source.getOperations()
                .findAllBlockContainsParamValue(filterParamValue)
                .forEach(sourceBlock -> {
                    var equalsByParamValue = sourceBlock
                            .getOperations()
                            .findFirstParameterContainsValue(filterParamValue)
                            .map(this::equalsByParamValue)
                            .orElseThrow(() -> new IllegalStateException("Filter parameter '" + filterParamValue + "' has not found."));

                    update(sourceBlock, target, equalsByParamValue, SceneNameParameter.COORDS);
                    update(sourceBlock, target, equalsByParamValue, SceneNameParameter.OBB_DATA);
                });
    }

    private void update(
            Block sourceBlock,
            SceneArena target,
            Predicate<Block.Parameter> predicate,
            String paramNameToUpdate
    ) {
        sourceBlock
                .getOperations()
                .findFirstCurrentBlockParameterByName(paramNameToUpdate)
                .ifPresent(parameter ->
                        target.getOperations()
                                .updateParameterValues(
                                        parameter.getName(),
                                        parameter.getValue(),
                                        predicate
                                ));
    }

    private Predicate<Block.Parameter> equalsByParamValue(Block.Parameter filterParameter) {
        return parameter -> parameter.getValue().equals(filterParameter.getValue());
    }
}
