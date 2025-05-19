package org.darr.mirr.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class BlockParserTest {
    private final static String RESOURCE_PATH = "arena_back_land_empire_01_noon.scene";

    @Test
    void parse() throws URISyntaxException, IOException {
        var url = Thread.currentThread().getContextClassLoader().getResource(RESOURCE_PATH);
        var resourceContent = Files.readString(Paths.get(url.toURI()));

        var result = BlockParser.parse(resourceContent);

        assertThat(result.getBlocks(), hasSize(2));
        // check globalsettings
        assertThat(result.getBlocks().get(0).getChildBlocks(), empty());
        assertThat(result.getBlocks().get(0).getParameters(), hasSize(23));
        assertThat(result.getBlocks().get(0).getName(), equalTo("globalsettings"));

        //check group "Scene Root"
        assertThat(result.getBlocks().get(1).getChildBlocks(), hasSize(9));
        assertThat(result.getBlocks().get(1).getParameters(), hasSize(13));
        assertThat(result.getBlocks().get(1).getName(), equalTo("group \"Scene Root\""));

        // check one in the middle
        assertThat(result.getBlocks().get(1).getChildBlocks().get(6).getChildBlocks(), hasSize(51));
        assertThat(result.getBlocks().get(1).getChildBlocks().get(6).getParameters(), hasSize(17));
        assertThat(result.getBlocks().get(1).getChildBlocks().get(6).getName(), equalTo("child group \"hexinfo\""));

        // check last child
        assertThat(result.getBlocks().get(1).getChildBlocks().get(8).getChildBlocks(), empty());
        assertThat(result.getBlocks().get(1).getChildBlocks().get(8).getParameters(), hasSize(34));
        assertThat(result.getBlocks().get(1).getChildBlocks().get(8).getName(), equalTo("child gobj \"stones_upgrade\""));
    }
}