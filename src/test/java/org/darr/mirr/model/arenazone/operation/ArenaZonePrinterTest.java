package org.darr.mirr.model.arenazone.operation;

import org.darr.mirr.model.arenazone.ArenaZone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArenaZonePrinterTest {

    // It is test for manual check by human and debugging
    @Test
    void add() {
        var printer = new ArenaZonePrinter();
        printer.add(ArenaZone.of("arenazone \"Center\", 4,5,2,3;"));
        printer.add(ArenaZone.of("arenazone \"Back\", 4,8,6,6;"));
        printer.add(ArenaZone.of("arenazone \"BackRight\", 9,10,5,6;"));
        printer.add(ArenaZone.of("arenazone \"BackLeft\", 0,3,5,6;"));
        printer.add(ArenaZone.of("arenazone \"CenterMiddleLeft\", 3,3,2,4;"));
        printer.add(ArenaZone.of("arenazone \"CenterLeft\", 0,2,2,4;"));
        printer.add(ArenaZone.of("arenazone \"CenterMiddleRight\", 8,8,2,4;"));
        printer.add(ArenaZone.of("arenazone \"CenterRight\", 9,10,2,4;"));
        printer.add(ArenaZone.of("arenazone \"FrontRight\", 8,10,0,1;"));
        printer.add(ArenaZone.of("arenazone \"Front\", 4,7,0,0;  "));
        printer.add(ArenaZone.of("arenazone \"FrontLeft\", 0,3,0,1; "));
        printer.add(ArenaZone.of("arenazone \"NpcLine1Center\", 4,5,1,1; "));
        printer.add(ArenaZone.of("arenazone \"NpcLine1Right\", 6,7,1,1; "));
        printer.add(ArenaZone.of("arenazone \"NpcLine2\", 6,7,2,2; "));
        printer.add(ArenaZone.of("arenazone \"NpcPlayerLine\", 6,7,3,4; "));
        printer.add(ArenaZone.of("arenazone \"PlayerLine4\", 4,5,4,4; "));
        printer.add(ArenaZone.of("arenazone \"PlayerLine5\", 4,8,5,5; "));
        printer.print();
    }
}