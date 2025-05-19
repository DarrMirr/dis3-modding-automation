package org.darr.mirr.model.block;

import lombok.*;
import org.darr.mirr.model.block.operation.BlockOperations;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Point to notice:
 * block's element name is important. For example: group child element must start with 'child'.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Block {
    @ToString.Exclude
    private final BlockOperations operations = new BlockOperations(this);
    private String name;
    private final List<Parameter> parameters = new ArrayList<>();
    private final List<Block> childBlocks = new ArrayList<>();

    public void addChild(Block block) {
        if (block != null) {
            childBlocks.add(block);
        }
    }

    public Block getLastChildBlock() {
        if (childBlocks.isEmpty()) {
            return null;
        }
        return childBlocks.getLast();
    }

    public void addParameterString(String parameterString) {
        var parameterParts = parameterString.trim().split("\\s", 2);
        var parameterName = parameterParts[0];
        var parameterValue = parameterParts[1];
        parameters.add(new Parameter(parameterName, parameterValue));
    }

    public String serialize() {
        return "\t" + name + System.lineSeparator()
                + "{" + System.lineSeparator()
                + serializeParameters()
                + serializeChildBlocks()
                + "}" + System.lineSeparator();
    }

    private String serializeParameters() {
        if (parameters.isEmpty()) {
            return "";
        }
        var optionalLineSeparator = childBlocks.isEmpty() ? "" : System.lineSeparator();
        return parameters.stream().map(Parameter::serialize).collect(joining(System.lineSeparator()))
                + System.lineSeparator() + optionalLineSeparator;
    }

    private String serializeChildBlocks() {
        if (childBlocks.isEmpty()) {
            return "";
        }
        return childBlocks.stream().map(Block::serialize).collect(joining(System.lineSeparator()))
                + System.lineSeparator();
    }

    @Setter
    @Getter
    @ToString
    @AllArgsConstructor
    public static class Parameter {
        private String name;
        private String value;

        public String serialize() {
            return "\t" + name + " " + value;
        }
    }
}
