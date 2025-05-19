package org.darr.mirr.task.scene;

import org.darr.mirr.model.scene.SceneArena;

/**
 * Task interface for all classes are dedicated to apply some actions on {@link SceneArena} object.
 */
public interface SceneTask {

    void execute(SceneArena sceneArena);
}
