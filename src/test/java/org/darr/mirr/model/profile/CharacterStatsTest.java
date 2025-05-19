package org.darr.mirr.model.profile;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class CharacterStatsTest {
    private static final String content = """
            // Characters stats profile
                     
                     unit_class	caster;
                     unit_side	"elves";
                     ship	"elves2";
                     attack_type	"earth";
                     level	8;
                     side	elves;
                     exp_base	175;
                     
                     //stats
                     str	5;
                     spd	3;
                     dex	5;
                     agi	5;
                     vit	5;
                     
                     
                     ap	1;
                     up	100;
                     mov	0;
                     movwm	0;
                     
                     atck_range	15;
                     zone_radius	2;
                     atck_zone;
                     
                     //resists
                     res_slash	-25;
                     res_pierce	25;
                     res_blunt	25;
                     res_fire	-25;
                     res_earth	25;
                     res_mind	25;
                     
                     no_retreat;
                     no_rotate;
                     Leadership	3;
                     leadership_cost	2;
                     cost	4590;
                     skill_grid	"elves_rod-1";
                     viewradius	8;
                     
                     //Modifi
                     dmg_str_mod	 500;
                     dmg_agi_mod	 500;
                     crit_dex_mod	 300;
                     crit_vit_mod	 150;
                     flee_agi_mod	 100;
                     flee_spd_mod	 50;
                     hp_vit_mod	 4000;
                     hp_str_mod	 2000;
                     ini_spd_mod	 200;
                     ini_dex_mod	 100;
                     mov_mod	 0;
                     movwm_mod	 0;
                     cover_mod	 0;
                     viewradius_mod	 0;
                     Discount_Mod	 0;
                     Bonusexp_Mod	 0;
                     level_cap	20;
                     levels_to_up	3;
                     Landguardian;
            """;

    @Test
    void of() throws IOException {
        var charStats = CharacterStats.of("test character", content);

        assertThat(charStats.count(), equalTo(51));
        assertThat(charStats.getName(), equalTo("test character"));
        assertThat(charStats.getStatValue("unit_class"), equalTo("caster"));
        assertThat(charStats.getStatValue("unit_side"), equalTo("elves"));
        assertThat(charStats.getStatValue("ship"), equalTo("elves2"));
        assertThat(charStats.getStatValue("attack_type"), equalTo("earth"));
        assertThat(charStats.getStatValue("level"), equalTo("8"));
        assertThat(charStats.getStatValue("side"), equalTo("elves"));
        assertThat(charStats.getStatValue("exp_base"), equalTo("175"));
        assertThat(charStats.getStatValue("str"), equalTo("5"));
        assertThat(charStats.getStatValue("spd"), equalTo("3"));
        assertThat(charStats.getStatValue("dex"), equalTo("5"));
        assertThat(charStats.getStatValue("agi"), equalTo("5"));
        assertThat(charStats.getStatValue("vit"), equalTo("5"));
        assertThat(charStats.getStatValue("ap"), equalTo("1"));
        assertThat(charStats.getStatValue("up"), equalTo("100"));
        assertThat(charStats.getStatValue("mov"), equalTo("0"));
        assertThat(charStats.getStatValue("movwm"), equalTo("0"));
        assertThat(charStats.getStatValue("atck_range"), equalTo("15"));
        assertThat(charStats.getStatValue("zone_radius"), equalTo("2"));
        assertThat(charStats.getStatValue("atck_zone"), equalTo("1"));
        assertThat(charStats.getStatValue("res_slash"), equalTo("-25"));
        assertThat(charStats.getStatValue("res_pierce"), equalTo("25"));
        assertThat(charStats.getStatValue("res_blunt"), equalTo("25"));
        assertThat(charStats.getStatValue("res_fire"), equalTo("-25"));
        assertThat(charStats.getStatValue("res_earth"), equalTo("25"));
        assertThat(charStats.getStatValue("res_mind"), equalTo("25"));
        assertThat(charStats.getStatValue("no_retreat"), equalTo("1"));
        assertThat(charStats.getStatValue("no_rotate"), equalTo("1"));
        assertThat(charStats.getStatValue("Leadership"), equalTo("3"));
        assertThat(charStats.getStatValue("leadership_cost"), equalTo("2"));
        assertThat(charStats.getStatValue("cost"), equalTo("4590"));
        assertThat(charStats.getStatValue("skill_grid"), equalTo("elves_rod-1"));
        assertThat(charStats.getStatValue("viewradius"), equalTo("8"));
        assertThat(charStats.getStatValue("dmg_str_mod"), equalTo("500"));
        assertThat(charStats.getStatValue("dmg_agi_mod"), equalTo("500"));
        assertThat(charStats.getStatValue("crit_dex_mod"), equalTo("300"));
        assertThat(charStats.getStatValue("crit_vit_mod"), equalTo("150"));
        assertThat(charStats.getStatValue("flee_agi_mod"), equalTo("100"));
        assertThat(charStats.getStatValue("flee_spd_mod"), equalTo("50"));
        assertThat(charStats.getStatValue("hp_vit_mod"), equalTo("4000"));
        assertThat(charStats.getStatValue("hp_str_mod"), equalTo("2000"));
        assertThat(charStats.getStatValue("ini_spd_mod"), equalTo("200"));
        assertThat(charStats.getStatValue("ini_dex_mod"), equalTo("100"));
        assertThat(charStats.getStatValue("mov_mod"), equalTo("0"));
        assertThat(charStats.getStatValue("movwm_mod"), equalTo("0"));
        assertThat(charStats.getStatValue("cover_mod"), equalTo("0"));
        assertThat(charStats.getStatValue("viewradius_mod"), equalTo("0"));
        assertThat(charStats.getStatValue("Discount_Mod"), equalTo("0"));
        assertThat(charStats.getStatValue("Bonusexp_Mod"), equalTo("0"));
        assertThat(charStats.getStatValue("level_cap"), equalTo("20"));
        assertThat(charStats.getStatValue("levels_to_up"), equalTo("3"));
        assertThat(charStats.getStatValue("Landguardian"), equalTo("1"));

        var line1 = charStats.getLine("levels_to_up");
        assertThat(line1.getStatName(), equalTo("levels_to_up"));
        assertThat(line1.getStatValue(), equalTo("3"));

        var line2 = charStats.getLine("Landguardian");
        assertThat(line2.getStatName(), equalTo("Landguardian"));
        assertThat(line2.getStatValue(), equalTo("1"));
    }
}