package org.darr.mirr.model.profile;

import java.util.Optional;
import java.util.stream.Stream;

public enum CharStatEnum {
    LEVEL("level", ValueType.INTEGER),
    LEVEL_CAP("level_cap", ValueType.INTEGER),
    LEVELS_TO_UP("levels_to_up", ValueType.INTEGER),
    SIDE("side", ValueType.STRING),
    UNIT_SIDE("unit_side", ValueType.STRING, true),
    UNIT_CLASS("unit_class", ValueType.STRING),
    ATTACK_TYPE("attack_type", ValueType.STRING, true),

    STR("str", ValueType.INTEGER),
    SPD("spd", ValueType.INTEGER),
    DEX("dex", ValueType.INTEGER),
    AGI("agi", ValueType.INTEGER),
    VIT("vit", ValueType.INTEGER),

    MOV("mov", ValueType.INTEGER),
    MOVWM("movwm", ValueType.INTEGER),
    ATCK_RANGE("atck_range", ValueType.INTEGER),
    ZONE_RADIUS("zone_radius", ValueType.INTEGER),
    ATCK_ZONE("atck_zone",  ValueType.BOOLEAN),

    CONTROL("Control", ValueType.INTEGER),
    COVER("Cover", ValueType.INTEGER),
    RESERVE("reserve", ValueType.INTEGER),

    DEF("def", ValueType.INTEGER),
    RES("res", ValueType.INTEGER),
    RES_SLASH("res_slash", ValueType.INTEGER),
    RES_PIERCE("res_pierce", ValueType.INTEGER),
    RES_BLUNT("res_blunt", ValueType.INTEGER),
    RES_FIRE("res_fire", ValueType.INTEGER),
    RES_WATER("res_water", ValueType.INTEGER),
    RES_AIR("res_air", ValueType.INTEGER),
    RES_EARTH("res_earth", ValueType.INTEGER),
    RES_DEATH("res_death", ValueType.INTEGER),
    RES_MIND("res_mind", ValueType.INTEGER),

    NO_RETREAT("no_retreat",  ValueType.BOOLEAN),
    NO_ROTATE("no_rotate", ValueType.BOOLEAN),
    KEYCHARACTER("keycharacter",  ValueType.BOOLEAN),
    SHIP("ship", ValueType.STRING, true),
    HEALER("Healer",  ValueType.INTEGER),
    LANDGUARDIAN("Landguardian",  ValueType.BOOLEAN),
    RACELEADER("raceleader",  ValueType.BOOLEAN),
    LEADERSHIP("Leadership", ValueType.INTEGER),
    LEADERSHIP_COST("leadership_cost", ValueType.INTEGER),
    EXP_BASE("exp_base", ValueType.INTEGER),

    COST("cost", ValueType.INTEGER),
    SKILL_GRID("skill_grid", ValueType.STRING, true),
    VIEWRADIUS("viewradius", ValueType.INTEGER),
    AP("ap", ValueType.INTEGER),
    UP("up", ValueType.INTEGER),

    DMG_STR_MOD("dmg_str_mod", ValueType.INTEGER),
    DMG_AGI_MOD("dmg_agi_mod", ValueType.INTEGER),
    CRIT_DEX_MOD("crit_dex_mod", ValueType.INTEGER),
    CRIT_VIT_MOD("crit_vit_mod", ValueType.INTEGER),
    FLEE_AGI_MOD("flee_agi_mod", ValueType.INTEGER),
    FLEE_SPD_MOD("flee_spd_mod", ValueType.INTEGER),
    HP_VIT_MOD("hp_vit_mod", ValueType.INTEGER),
    HP_STR_MOD("hp_str_mod", ValueType.INTEGER),
    INI_SPD_MOD("ini_spd_mod", ValueType.INTEGER),
    INI_DEX_MOD("ini_dex_mod", ValueType.INTEGER),
    MOV_MOD("mov_mod", ValueType.INTEGER),
    MOVWM_MOD("movwm_mod", ValueType.INTEGER),
    COVER_MOD("cover_mod", ValueType.INTEGER),
    VIEWRADIUS_MOD("viewradius_mod", ValueType.INTEGER),
    REGEN_MOD("Regen_Mod", ValueType.INTEGER),
    DISCOUNT_MOD("Discount_Mod", ValueType.INTEGER),
    BONUSEXP_MOD("Bonusexp_Mod", ValueType.INTEGER);

    public final String strValue;
    public final ValueType type;
    public final boolean isQuoted;

    CharStatEnum(String strValue, ValueType type) {
        this(strValue, type, false);
    }

    CharStatEnum(String strValue, ValueType type, boolean isQuoted) {
        this.strValue = strValue;
        this.type = type;
        this.isQuoted = isQuoted;
    }
    
    public enum ValueType {
        INTEGER, BOOLEAN, STRING
    }

    public static Optional<CharStatEnum> findByStrValueSafety(String strValue) {
        return Stream
                .of(CharStatEnum.values())
                .filter(charStatEnum -> charStatEnum.strValue.equals(strValue))
                .findFirst();
    }
}
