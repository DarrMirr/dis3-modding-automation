package org.darr.mirr.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XlsUtils {

    public static Workbook openWorkbook(Path outputXls) throws IOException {
        if (outputXls.toFile().getName().endsWith(".xlsx")) {
            return new XSSFWorkbook(Files.newInputStream(outputXls));
        }
        if (outputXls.toFile().getName().endsWith(".xls")) {
            return new HSSFWorkbook(Files.newInputStream(outputXls));
        }
        throw new IllegalStateException("Unsupported excel file: " + outputXls);
    }

    public static String getCellValueAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case _NONE -> null;
            case NUMERIC -> Integer.toString((int) cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case FORMULA -> null;
            case BLANK -> null;
            case BOOLEAN -> null;
            case ERROR -> null;
        };
    }
}
