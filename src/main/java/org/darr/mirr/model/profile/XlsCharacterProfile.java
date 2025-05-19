package org.darr.mirr.model.profile;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.darr.mirr.util.XlsUtils;
import org.darr.mirr.util.function.FunctionUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@ToString
public class XlsCharacterProfile {
    private static final String ID = "ID";
    // TODO: take into account cost column value
    private static final List<String> STAT_NAMES = List.of(
            ID, "level", "level_cap", "levels_to_up", "side", "unit_side", "unit_class", "attack_type",
            "str", "spd", "dex", "agi", "vit", "mov", "movwm", "atck_range", "zone_radius", "atck_zone",
            "Control", "Cover", "def", "res", "res_slash", "res_pierce", "res_blunt", "res_fire", "res_water",
            "res_air", "res_earth", "res_death", "res_mind", "no_retreat", "no_rotate", "keycharacter",
            "ship", "Healer", "Landguardian", "raceleader", "Leadership", "leadership_cost", "exp_base",
            "cost", "skill_grid", "viewradius", "ap", "up", "dmg_str_mod", "dmg_agi_mod", "crit_dex_mod",
            "crit_vit_mod", "flee_agi_mod", "flee_spd_mod", "hp_vit_mod", "hp_str_mod", "ini_spd_mod",
            "ini_dex_mod", "mov_mod", "movwm_mod", "cover_mod", "viewradius_mod", "Regen_Mod", "Discount_Mod",
            "Bonusexp_Mod"
    );

    private final Map<String, Integer> statNameColumnIndex = new HashMap<>();
    private final List<CharacterStats> characterStatsList = new ArrayList<>();
    @ToString.Exclude
    private final XlsCharacterProfileOperations operations = new XlsCharacterProfileOperations();

    public XlsCharacterProfile(Row headerRow) {
        for (var cell : headerRow) {
            var value = cell.getStringCellValue();
            STAT_NAMES
                    .stream()
                    .filter(statName -> statName.equalsIgnoreCase(value))
                    .findFirst()
                    .ifPresent(statName -> statNameColumnIndex.put(statName, cell.getColumnIndex()));
        }
    }

    public XlsCharacterProfileOperations operations() {
        return operations;
    }


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class XlsCharacterProfileOperations {

        private Optional<Integer> getColumnIndexByStatName(String name) {
            return statNameColumnIndex
                    .entrySet()
                    .stream()
                    .filter(statEntry ->
                            statEntry.getKey().equalsIgnoreCase(name))
                    .findFirst()
                    .map(Map.Entry::getValue);
        }

        public void addCharacterStats(CharacterStats characterStats) {
            if (characterStats != null) {
                characterStatsList.add(characterStats);
            }
        }

        public void writeIntoXls(Sheet characterSheet) {
            characterStatsList.forEach(characterStats ->
                    findCharacterRow(characterSheet, characterStats.getName())
                            .ifPresent(characterRow ->
                                    characterStats
                                            .getLineMap()
                                            .forEach(setValue(characterRow))
                            )
            );
        }

        private BiConsumer<String, String> setValue(Row characterRow) {
            return (key, value) ->
                    getColumnIndexByStatName(key)
                            .ifPresent(columnIndex -> {
                                var cell = characterRow.getCell(columnIndex);
                                if (cell == null) {
                                    cell = characterRow.createCell(columnIndex);
                                }
                                if (StringUtils.isNumeric(value)) {
                                    cell.setCellValue(Double.parseDouble(value));
                                } else {
                                    cell.setCellValue(value);
                                }
                            });
        }

        private Optional<Row> findCharacterRow(Sheet characterSheet, String characterName) {
            for (var row : characterSheet) {
                var idIndex = statNameColumnIndex.get(ID);
                var cell = row.getCell(idIndex);
                if (cell != null && cell.getStringCellValue().equalsIgnoreCase(characterName)) {
                    return Optional.of(row);
                }
            }
            return Optional.empty();
        }

        public void readFromXls(Sheet characterSheet, FormulaEvaluator formulaEvaluator) {
            for (int i = 2; i <= characterSheet.getLastRowNum(); i++) {
                var row = characterSheet.getRow(i);
                var idIndex = statNameColumnIndex.get(ID);
                var idCell = row.getCell(idIndex);
                if (idCell != null) {
                    var statMap = STAT_NAMES
                            .stream()
                            .filter(columnName ->
                                    !columnName.equals(ID))
                            .map(toColumnValue(row, formulaEvaluator))
                            .filter(Objects::nonNull)
                            .reduce(new HashMap<>(), FunctionUtil.concatMap());

                    var characterStats = new CharacterStats(idCell.getStringCellValue(), statMap);
                    addCharacterStats(characterStats);
                }
            }
        }

        private Function<String, Map<String, String>> toColumnValue(Row row, FormulaEvaluator formulaEvaluator) {
            return columnName -> {
                var columnIndex = statNameColumnIndex.get(columnName);
                if (columnIndex != null) {
                    var cellValue = row.getCell(columnIndex);
                    if (cellValue != null) {
                        if (cellValue.getCellType() == CellType.FORMULA) {
                            cellValue = formulaEvaluator.evaluateInCell(cellValue);
                        }
                        var stringValue = XlsUtils.getCellValueAsString(cellValue);
                        if (stringValue != null) {
                            return Map.of(columnName, stringValue);
                        }
                    }
                }
                return null;
            };
        }

        public Map<String, String> toFileContentCharacterStats() {
            return characterStatsList
                    .stream()
                    .map(CharacterStats::toFileContent)
                    .reduce(new HashMap<>(), FunctionUtil.concatMap());
        }
    }
}
