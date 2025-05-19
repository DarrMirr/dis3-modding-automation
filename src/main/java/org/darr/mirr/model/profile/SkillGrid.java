package org.darr.mirr.model.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: work in process
@RequiredArgsConstructor
public class SkillGrid {
    private final String unitName;
    // Map<Skill_line_number, List of skill lines>
    private final Map<Integer, List<SkillLine>> skills = new HashMap<>();

    public boolean isMultiline() {
        return skills.size() > 1;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SkillLine {
        private String skillName;
        private Integer usesCount;
        // first digit
        private Integer isShow;
        // second digit
        private Integer isSet;
        // third digit
        private int numInLine;
        // fourth digit
        private int lineNum;

        @Override
        public String toString() {
            var stringLine = "unit_skill \"" +
                    getSkillName() + "\" " +
                    getIsShow() + " " +
                    getIsSet() + " " +
                    getNumInLine() + " " +
                    getLineNum() + ";";
            if (usesCount != null) {
                stringLine = stringLine + System.lineSeparator() + "uses " + usesCount + ";";
            }
            return stringLine;
        }
    }

    public final static class StartLine extends SkillLine {

        public StartLine(int numInLine, int lineNum) {
            super(null, null, null, null, numInLine, lineNum);
        }

        @Override
        public String toString() {
            return "unit_start " +
                    getNumInLine() + " " +
                    getLineNum() + ";";
        }

        @Override
        public void setUsesCount(Integer usesCount) {
            // do nothing
        }

        @Override
        public void setIsShow(Integer isShow) {
            // do nothing
        }

        @Override
        public void setIsSet(Integer isSet) {
            // do nothing
        }

        @Override
        public void setSkillName(String skillName) {
            // do nothing
        }
    }
}
