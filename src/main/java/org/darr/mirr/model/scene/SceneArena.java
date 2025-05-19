package org.darr.mirr.model.scene;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.darr.mirr.model.block.Block;
import org.darr.mirr.model.scene.operation.SceneArenaOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class SceneArena {
    @ToString.Exclude
    private final SceneArenaOperations operations = new SceneArenaOperations(this);
    private final List<Block> blocks = new ArrayList<>();

    public void add(Block block) {
        if (block != null) {
            blocks.add(block);
        }
    }

    public Block getLastAddedBlock() {
        if (blocks.isEmpty()) {
            return null;
        }
        return blocks.getLast();
    }

    public String serialize() {
        return blocks
                .stream()
                .map(Block::serialize)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
