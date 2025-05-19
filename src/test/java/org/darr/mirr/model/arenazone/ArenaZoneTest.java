package org.darr.mirr.model.arenazone;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ArenaZoneTest {

    @Test
    void of() {
        var arenaZone = ArenaZone.of("arenazone \"Center\", 4,7,2,4;");

        assertThat(arenaZone.getName(), equalTo("Center"));
        assertThat(arenaZone.getX1(), equalTo(4));
        assertThat(arenaZone.getX2(), equalTo(7));
        assertThat(arenaZone.getY1(), equalTo(2));
        assertThat(arenaZone.getY2(), equalTo(4));
        assertThat(arenaZone.toString(), equalTo("arenazone \"Center\", 4,7,2,4;"));
    }
}