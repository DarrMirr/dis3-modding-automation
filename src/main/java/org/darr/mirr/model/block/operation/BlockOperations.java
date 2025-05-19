package org.darr.mirr.model.block.operation;

import lombok.RequiredArgsConstructor;
import org.darr.mirr.model.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RequiredArgsConstructor
public final class BlockOperations {
    private final Block block;

    public Optional<Block> findFirstBlockEqualsParameterName(String value) {
        return findFirstBlockByPredicateOnParameter(param -> param.getName().equals(value));
    }

    public Optional<Block> findFirstBlockContainsParameterValue(String value) {
        return findFirstBlockByPredicateOnParameter(param -> param.getValue().contains(value));
    }

    public Optional<Block> findFirstBlockByPredicateOnParameter(Predicate<Block.Parameter> predicate) {
        var inThisBlock = block
                .getParameters()
                .stream()
                .filter(predicate)
                .findFirst();
        if (inThisBlock.isPresent()) {
            return Optional.of(block);
        } else {
            return block
                    .getChildBlocks()
                    .stream()
                    .map(block ->
                            block.getOperations().findFirstBlockByPredicateOnParameter(predicate))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
        }
    }

    public Optional<Block.Parameter> findFirstParameterContainsValue(String value) {
        return block
                .getParameters()
                .stream()
                .filter(param -> param.getValue().contains(value))
                .findFirst()
                .or(() -> block
                        .getChildBlocks()
                        .stream()
                        .map(block ->
                                block.getOperations().findFirstParameterContainsValue(value))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst()
                );
    }

    public Optional<Block.Parameter> findFirstCurrentBlockParameterByName(String name) {
        return block
                .getParameters()
                .stream()
                .filter(param -> param.getName().equals(name))
                .findFirst();
    }

    public List<Block> findAllBlockByPredicateOnParameter(Predicate<Block.Parameter> predicate) {
        var resultList = new ArrayList<Block>();

        var inThisBlock = block
                .getParameters()
                .stream()
                .filter(predicate)
                .findFirst();
        if (inThisBlock.isPresent()) {
            resultList.add(block);
        }

        block.getChildBlocks()
                .stream()
                .map(block ->
                        block.getOperations().findAllBlockByPredicateOnParameter(predicate))
                .flatMap(List::stream)
                .forEach(resultList::add);

        return resultList;

    }

    public Optional<Block> findOneBlockByName(String name) {
        return this.block.getName().equals(name)
                ? Optional.of(this.block)
                : block
                .getChildBlocks()
                .stream()
                // recursive invocation
                .map(childBlock ->
                        childBlock.getOperations().findOneBlockByName(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public Block updateCurrentBlockParameterValue(String paramName, String newValue) {
        updateCurrentBlockParameterValue(newValue, param -> param.getName().equals(paramName));
        return block;
    }

    public Block updateCurrentBlockParameterValue(String newValue, Predicate<Block.Parameter> predicate) {
        block.getParameters()
                .stream()
                .filter(predicate)
                .forEach(param -> param.setValue(newValue));
        return block;
    }

    public void deleteParameterContainsValue(String value) {
        int indexToDelete = -1;
        var parameters = block.getParameters();

        for (int i = 0; i < parameters.size(); i++) {
            var parameter = parameters.get(i);
            if (parameter.getValue().contains(value)) {
                indexToDelete = i;
                break;
            }
        }
        if (indexToDelete > 0) {
            parameters.remove(indexToDelete);
        }
    }

    public void deleteAllParameterByName(String name) {
        var indexListToDelete = new ArrayList<Integer>();
        var parameters = block.getParameters();

        for (int i = 0; i < parameters.size(); i++) {
            var parameter = parameters.get(i);
            if (parameter.getName().equals(name)) {
                indexListToDelete.add(i);
            }
        }
        indexListToDelete.forEach(index -> parameters.remove((int) index));
    }
}
