package org.darr.mirr.task.scene;

import lombok.RequiredArgsConstructor;
import org.darr.mirr.model.block.Block;
import org.darr.mirr.model.scene.SceneArena;

@RequiredArgsConstructor
public class InsertChildSceneBlockSceneTask implements SceneTask {
    private final String parentName;
    private final Block blockToInsert;

    @Override
    public void execute(SceneArena sceneArena) {
        if (!blockToInsert.getName().startsWith("child ")) {
            throw new IllegalStateException("Child block must start with 'child' word.");
        }
        sceneArena
                .getOperations()
                .findOneBlockByName(parentName)
                .ifPresent(block ->
                        block.addChild(blockToInsert)
                );
    }
}
