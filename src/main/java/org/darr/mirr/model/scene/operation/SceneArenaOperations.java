package org.darr.mirr.model.scene.operation;

import lombok.RequiredArgsConstructor;
import org.darr.mirr.model.block.Block;
import org.darr.mirr.model.scene.SceneArena;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RequiredArgsConstructor
public final class SceneArenaOperations {
    private final SceneArena sceneArena;

    public List<Block> findAllBlockByParameterName(String name) {
        return sceneArena
                .getBlocks()
                .stream()
                .map(block -> block
                        .getOperations()
                        .findAllBlockByPredicateOnParameter(parameter -> parameter.getName().equals(name)))
                .flatMap(List::stream)
                .toList();
    }

    public List<Block> findAllBlocksByParameterValue(String value) {
        return sceneArena
                .getBlocks()
                .stream()
                .map(block ->
                        block.getOperations().findFirstBlockContainsParameterValue(value))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Block> findAllBlockContainsParamValue(String value) {
        return sceneArena
                .getBlocks()
                .stream()
                .map(block ->
                        block.getOperations().findAllBlockByPredicateOnParameter(param -> param.getValue().contains(value)))
                .flatMap(List::stream)
                .toList();
    }

    public Optional<Block> findOneBlockByName(String name) {
        return sceneArena
                .getBlocks()
                .stream()
                .map(block ->
                        block.getOperations().findOneBlockByName(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public SceneArena updateParameterValues(String paramName, String newValue, Predicate<Block.Parameter> predicate) {
        sceneArena
                .getBlocks()
                .stream()
                .map(block -> block.getOperations().findAllBlockByPredicateOnParameter(predicate))
                .flatMap(List::stream)
                .forEach(block ->
                        block.getOperations().updateCurrentBlockParameterValue(paramName, newValue));
        return sceneArena;
    }
}
