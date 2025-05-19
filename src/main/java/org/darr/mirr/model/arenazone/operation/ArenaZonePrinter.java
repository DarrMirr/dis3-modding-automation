package org.darr.mirr.model.arenazone.operation;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.model.arenazone.ArenaZone;

@Slf4j
public class ArenaZonePrinter {
    private static final int ARENA_SIZE_X = 11;
    private static final int ARENA_SIZE_Y = 7;
    private static final int PRINT_CELL_SIZE = 7;
    private final String[][] printedLines = new String[ARENA_SIZE_Y][ARENA_SIZE_X];

    {
        // init lines to print
        for (int y = 0; y < ARENA_SIZE_Y; y++) {
            for (int x = 0; x < ARENA_SIZE_X; x++ ) {
                printedLines[y][x] = "       ";
            }
        }
    }

    // extremely weird code :-( but it works
    public void add(ArenaZone arenaZone) {
        int nameLineY;
        // condition to make decision to print whether on top of block or in the middle of printed block
        if ((arenaZone.getY2() - arenaZone.getY1()) <= 1) {
            nameLineY = arenaZone.getY2();
        } else {
            nameLineY = arenaZone.getY1() + ((arenaZone.getY2() - arenaZone.getY1()) / 2);
        }

        for (int y = arenaZone.getY1(); y <= arenaZone.getY2(); y++) {
            var yLine = printedLines[y];

            for (int x = arenaZone.getX1(), i = 0; x <= arenaZone.getX2(); x++ ) {
                var printChar = "     ";
                if (isBorderLeft(x, arenaZone)) {
                    // print corner or print wall
                    if (isBorderBottom(y, arenaZone) || isBorderTop(y, arenaZone)) {
                        printChar = "+------";
                    } else {
                        printChar = "|      ";
                    }
                }
                if (isBorderRight(x, arenaZone)) {
                    // print corner or print wall
                    if (isBorderBottom(y, arenaZone) || isBorderTop(y, arenaZone)) {
                        printChar = "------+";
                    } else {
                        printChar = "      |";
                    }
                }

                // print area border between corners
                if ((isBorderBottom(y, arenaZone) || isBorderTop(y, arenaZone))
                        && !(isBorderLeft(x, arenaZone) || isBorderRight(x, arenaZone))) {
                    printChar = "-------";
                }
                // print zone name
                if (y == nameLineY && arenaZone.getName().length() > i) {
                    if (isBorderLeft(x, arenaZone) && isBorderTop(y, arenaZone)) {
                        // handle corner case
                        printChar = "+";
                    } else if (isBorderLeft(x, arenaZone)) {
                        // handle case to print in the middle on left side
                        printChar = "|";
                    } else {
                        // for rest cases
                        printChar = "";
                    }
                    int leftCellSpace = PRINT_CELL_SIZE - printChar.length();
                    for (int j = 0; j < leftCellSpace ; j++) {
                        if (arenaZone.getName().length() <= (i + j)) {
                            break;
                        } else {
                            printChar += arenaZone.getName().charAt(i + j);
                        }
                    }
                    if (isBorderRight(x, arenaZone)
                            && !(isBorderBottom(y, arenaZone) || isBorderTop(y, arenaZone))
                            && printChar.length() == leftCellSpace
                    ) {
                        // handle case when name is longer than cell size in the middle of block
                        leftCellSpace -= 1;
                        printChar = printChar.substring(0, leftCellSpace) + "|";
                    }
                    i = i + leftCellSpace;
                }
                // handle situation when print name's last characters are shorter than PRINT_CELL_SIZE
                if (printChar.length() < PRINT_CELL_SIZE) {
                    for (int j = printChar.length(); j < PRINT_CELL_SIZE; j++) {
                        if (isBorderBottom(y, arenaZone) || isBorderTop(y, arenaZone)) {
                            printChar += "-";
                        } else if (isBorderRight(x, arenaZone) && j == (PRINT_CELL_SIZE - 1)) {
                            printChar += "|";
                        } else {
                            printChar += " ";
                        }
                    }
                }

                // case when x1 == x2
                if (isBorderLeft(x, arenaZone) && isBorderRight(x, arenaZone)) {
                    if (isBorderTop(y, arenaZone) || isBorderBottom(y, arenaZone)) {
                        printChar = "+" + printChar.substring(1, PRINT_CELL_SIZE - 1) + "+";
                    } else {
                        printChar = "|" + printChar.substring(1, PRINT_CELL_SIZE - 1) + "|";
                    }
                }

                yLine[x] = printChar;
            }
        }
    }

    public void print() {
        var outputString = new StringBuilder();
        outputString
                .append(System.lineSeparator())
                .append("y")
                .append(System.lineSeparator());
        for (int y = ARENA_SIZE_Y - 1; y >= 0; y--) {
            var yLine = printedLines[y];
            outputString.append(y);
            for (int x = 0; x < ARENA_SIZE_X; x++ ) {
                outputString.append(yLine[x]);
            }
            outputString.append(System.lineSeparator());
        }
        outputString.append("x");
        for (int x = 0; x < ARENA_SIZE_X; x++ ) {
            // amount of spaces = print cell size - 1
            outputString.append(x).append("      ");
        }
        log.info(outputString.toString());
    }

    private boolean isBorderLeft(int x, ArenaZone arenaZone) {
        return x == arenaZone.getX1();
    }

    private boolean isBorderRight(int x, ArenaZone arenaZone) {
        return x == arenaZone.getX2();
    }

    private boolean isBorderBottom(int y, ArenaZone arenaZone) {
        return y == arenaZone.getY1();
    }

    private boolean isBorderTop(int y, ArenaZone arenaZone) {
        return y == arenaZone.getY2();
    }
}
