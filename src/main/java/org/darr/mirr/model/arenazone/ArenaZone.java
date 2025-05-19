package org.darr.mirr.model.arenazone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArenaZone {
    private final String name;
    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;

    public static ArenaZone of(String arenazone) {
        arenazone = arenazone.contains("//") ? arenazone.split("//")[0] : arenazone;
        var parts = arenazone
                .replace("arenazone", "")
                .replace(";", "")
                .replaceAll("\"", "")
                .trim()
                .split(",");

        var name = parts[0].trim();
        var x1 = Integer.parseInt(parts[1].trim());
        var x2 = Integer.parseInt(parts[2].trim());
        var y1 = Integer.parseInt(parts[3].trim());
        var y2 = Integer.parseInt(parts[4].trim());
        return new ArenaZone(name, x1, x2, y1, y2);
    }

    @Override
    public String toString() {
        return "arenazone \""
                + name
                + "\", "
                + String.join(",", "" + x1, "" + x2, "" + y1, "" + y2)
                + ";";
    }
}
