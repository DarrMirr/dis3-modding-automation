package org.darr.mirr.task.scene;

import lombok.RequiredArgsConstructor;
import org.darr.mirr.model.scene.SceneArena;

@RequiredArgsConstructor
public class DeleteParameterByNameSceneTask implements SceneTask {
    private final String parameterName;

    @Override
    public void execute(SceneArena sceneArena) {
        sceneArena
                .getOperations()
                .findAllBlockByParameterName(parameterName)
                .forEach(block -> block.getOperations().deleteAllParameterByName(parameterName));
    }
}
