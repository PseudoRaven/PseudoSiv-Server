package net.nocturne.game.player.actions.skills.herblore;

import java.util.HashMap;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

/**
 * @author Tommeh
 */

public class Herblore extends Action {
	
	public enum HerbloreType {
		CLEAN(),
		POTION()
	}
	
	public enum CleanAction {
	
		
	    GUAM(1, 2.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_GUAM)}, new Item(ItemIdentifiers.CLEAN_GUAM), Skills.HERBLORE),
	    SNAKE_WEED(3, 2.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_SNAKE_WEED)}, new Item(ItemIdentifiers.CLEAN_SNAKE_WEED), Skills.HERBLORE),
	    ARDIGAL(3, 2.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_ARDRIGAL)}, new Item(ItemIdentifiers.CLEAN_ARDRIGAL), Skills.HERBLORE),
	    ROGUES_PURSE(3, 2.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_ROGUES_PURSE)}, new Item(ItemIdentifiers.CLEAN_ROGUES_PURSE), Skills.HERBLORE),
	    SITO_FOIL(3, 2.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_SITO_FOIL)}, new Item(ItemIdentifiers.CLEAN_SITO_FOIL), Skills.HERBLORE),
	    VOLENCIA_MOSS(3, 2.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_VOLENCIA_MOSS)}, new Item(ItemIdentifiers.CLEAN_VOLENCIA_MOSS), Skills.HERBLORE),
	    MARRENTIL(9, 5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_MARRENTILL)}, new Item(ItemIdentifiers.CLEAN_MARRENTILL), Skills.HERBLORE),
	    TARROMIN(9, 3.8, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_TARROMIN)}, new Item(ItemIdentifiers.CLEAN_TARROMIN), Skills.HERBLORE),
	    HARRALANDER(20, 6.3, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_HARRALANDER)}, new Item(ItemIdentifiers.CLEAN_HARRALANDER), Skills.HERBLORE),
	    RANARR(25, 7.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_RANARR)}, new Item(ItemIdentifiers.CLEAN_RANARR), Skills.HERBLORE),
	    TOADFLAX(30, 8, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_TOADFLAX)}, new Item(ItemIdentifiers.CLEAN_TOADFLAX), Skills.HERBLORE),
	    SPIRIT_WEED(35, 7.8, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_SPIRIT_WEED)}, new Item(ItemIdentifiers.CLEAN_SPIRIT_WEED), Skills.HERBLORE),
	    IRIT(40, 8.8, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_IRIT)}, new Item(ItemIdentifiers.CLEAN_IRIT), Skills.HERBLORE),
	    WERGALI(41, 9.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_WERGALI)}, new Item(ItemIdentifiers.CLEAN_WERGALI), Skills.HERBLORE),
	    AVANTOE(48, 10, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_AVANTOE)}, new Item(ItemIdentifiers.CLEAN_AVANTOE), Skills.HERBLORE),
	    KWUARM(54, 11.3, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_KWUARM)}, new Item(ItemIdentifiers.CLEAN_KWUARM), Skills.HERBLORE),
	    SNAPDRAGON(59, 11.8, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_SNAPDRAGON)}, new Item(ItemIdentifiers.CLEAN_SNAPDRAGON), Skills.HERBLORE),
	    CADANTINE(65, 12.5, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_CADANTINE)}, new Item(ItemIdentifiers.CLEAN_CADANTINE), Skills.HERBLORE),
	    LANTADYME(67, 13.1, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_LANTADYME)}, new Item(ItemIdentifiers.CLEAN_LANTADYME), Skills.HERBLORE),
	    DWARF_WEED(70, 13.8, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_DWARF_WEED)}, new Item(ItemIdentifiers.CLEAN_DWARF_WEED), Skills.HERBLORE),
	    TORSTOL(75, 15, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_TORSTOL)}, new Item(ItemIdentifiers.CLEAN_TORSTOL), Skills.HERBLORE),
	    FELLSTALK(91, 16.8, 10119, new Item[]{ new Item(ItemIdentifiers.GRIMY_FELLSTALK)}, new Item(ItemIdentifiers.CLEAN_FELLSTALK), Skills.HERBLORE),
	
		COCONUT_MILK(1, 1, 363, new Item[]{ new Item(ItemIdentifiers.COCONUT), new Item (ItemIdentifiers.EMPTY_VIAL)}, new Item(ItemIdentifiers.COCONUT_MILK), Skills.HERBLORE),
        GUAM_UNF(1, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_GUAM), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.GUAM_POTION_UNF), Skills.HERBLORE),
        SNAKEWEED_MIXTURE(1, 1, 363,  new Item[]{ new Item(ItemIdentifiers.CLEAN_GUAM), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.SNAKEWEED_MIXTURE), Skills.HERBLORE),
        ARDRIGAL_MIXTURE(1, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_GUAM), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.ARDRIGAL_MIXTURE), Skills.HERBLORE),
        ROGUES_PURSE_UNF(1, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_GUAM), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.ROGUES_PURSE_POTION_UNF), Skills.HERBLORE),
        TARROMIN_UNF(5, 1, 363,  new Item[]{ new Item(ItemIdentifiers.CLEAN_TARROMIN), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.TARROMIN_POTION_UNF), Skills.HERBLORE),
        MARRENTIL_UNF(9, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_MARRENTILL), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.MARRENTILL_POTION_UNF), Skills.HERBLORE),
        HARRALANDER_UNF(18, 1, 363,  new Item[]{ new Item(ItemIdentifiers.CLEAN_HARRALANDER), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.HARRALANDER_POTION_UNF), Skills.HERBLORE),
        RANARR_UNF(25, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_RANARR), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.RANARR_POTION_UNF), Skills.HERBLORE),
        TOADFLAX_UNF(30, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_TOADFLAX), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.TOADFLAX_POTION_UNF), Skills.HERBLORE),
        SPIRIT_WEED_UNF(35, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_SPIRIT_WEED), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.SPIRIT_WEED_POTION_UNF), Skills.HERBLORE),
        IRIT_UNF(40, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_IRIT), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.IRIT_POTION_UNF), Skills.HERBLORE),
        WERGALI_UNF(41, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_WERGALI), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.WERGALI_POTION_UNF), Skills.HERBLORE),
        AVANTOE_UNF(48, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_AVANTOE), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.AVANTOE_POTION_UNF), Skills.HERBLORE),
        KWUARM_UNF(54, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_KWUARM), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.KWUARM_POTION_UNF), Skills.HERBLORE),
        MAGIC_ESSENCE_UNF(57, 1, 363, new Item[]{ new Item(ItemIdentifiers.STAR_FLOWER), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.MAGIC_ESSENCE_UNF), Skills.HERBLORE),
        SNAPDRAGON_UNF(59, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_SNAPDRAGON), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.SNAPDRAGON_POTION_UNF), Skills.HERBLORE),
        CADANTINE_UNF(65, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_CADANTINE), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.CADANTINE_POTION_UNF), Skills.HERBLORE),
        LANTADYME_UNF(67, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_LANTADYME), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.LANTADYME_POTION_UNF), Skills.HERBLORE),
        ANTIPOISON_PLUS_UNF(68, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_TOADFLAX), new Item (ItemIdentifiers.COCONUT_MILK)}, new Item(ItemIdentifiers.ANTIPOISON_PLUS_UNF), Skills.HERBLORE),
        DWARF_WEED_UNF(70, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_DWARF_WEED), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.DWARF_WEED_POTION_UNF), Skills.HERBLORE),
        WEAPONPOISON_PLUS_UNF(73, 1, 363, new Item[]{ new Item(ItemIdentifiers.COCONUT_MILK), new Item (ItemIdentifiers.CACTUS_SPINE)}, new Item(ItemIdentifiers.WEAPON_POISON_PLUS_UNF), Skills.HERBLORE),
        TORSTOL_UNF(75, 1, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_TORSTOL), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.TORSTOL_POTION_UNF), Skills.HERBLORE),
        ANTIPOISON_PLUS_PLUS_UNF(79, 1, 363, new Item[]{ new Item(ItemIdentifiers.COCONUT_MILK), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.ANTIPOISON_PLUS_PLUS_UNF), Skills.HERBLORE),
        WEAPONPOISON_PLUS_PLUS_UNF(82, 1, 363, new Item[]{ new Item(ItemIdentifiers.COCONUT_MILK), new Item (ItemIdentifiers.CAVE_NIGHTSHADE)}, new Item(ItemIdentifiers.WEAPON_POISON_PLUS_PLUS_UNF), Skills.HERBLORE),
        FELLSTALK_UNF(91, 1, 363,  new Item[]{ new Item(ItemIdentifiers.CLEAN_FELLSTALK), new Item (ItemIdentifiers.VIAL_OF_WATER)}, new Item(ItemIdentifiers.FELLSTALK_POTION_UNF), Skills.HERBLORE),
        
        ATTACK_POTION(1, 25, 363, new Item[]{ new Item(ItemIdentifiers.GUAM_POTION_UNF), new Item (ItemIdentifiers.EYE_OF_NEWT)}, new Item(ItemIdentifiers.ATTACK_POTION_3), Skills.HERBLORE),
        RANGING_POTION(3, 30, 363, new Item[]{ new Item(ItemIdentifiers.GUAM_POTION_UNF), new Item (ItemIdentifiers.REDBERRIES)}, new Item(ItemIdentifiers.RANGING_POTION_3), Skills.HERBLORE),
        MAGIC_POTION(5, 35, 363, new Item[]{ new Item(ItemIdentifiers.TARROMIN_POTION_UNF), new Item (ItemIdentifiers.BLACK_BEAD)}, new Item(ItemIdentifiers.MAGIC_POTION_3), Skills.HERBLORE),
        STRENGTH_POTION(7, 40, 363, new Item[]{ new Item(ItemIdentifiers.TARROMIN_POTION_UNF), new Item (ItemIdentifiers.LIMPWURT_ROOT)}, new Item(ItemIdentifiers.STRENGTH_POTION_3), Skills.HERBLORE),
        RELICYMS_BALM(8, 40, 363, new Item[]{ new Item(ItemIdentifiers.ROGUES_PURSE_POTION_UNF), new Item (ItemIdentifiers.GOAT_HORN_DUST)}, new Item(ItemIdentifiers.RELICYMS_BALM_3), Skills.HERBLORE),
        DEFENCE_POTION(9, 45, 363, new Item[]{ new Item(ItemIdentifiers.MARRENTILL_POTION_UNF), new Item (ItemIdentifiers.BEAR_FUR)}, new Item(ItemIdentifiers.DEFENCE_POTION_3), Skills.HERBLORE),
        ANTIPOISON(13, 50, 363, new Item[]{ new Item(ItemIdentifiers.MARRENTILL_POTION_UNF), new Item (ItemIdentifiers.UNICORN_HORN_DUST)}, new Item(ItemIdentifiers.ANTIPOISON_3), Skills.HERBLORE),
        SERUM207(15, 50, 363, new Item[]{ new Item(ItemIdentifiers.MARRENTILL_POTION_UNF), new Item (ItemIdentifiers.UNICORN_HORN_DUST)}, new Item(ItemIdentifiers.SERUM_207_3), Skills.HERBLORE),
        GUTHIX_REST(18, 59.5, 363, new Item[]{ new Item(ItemIdentifiers.HARRALANDER_POTION_UNF), new Item (ItemIdentifiers.CLEAN_MARRENTILL)}, new Item(ItemIdentifiers.GUTHIX_REST_3), Skills.HERBLORE),
        RESTORE_POTION(22, 62.5, 363, new Item[]{ new Item(ItemIdentifiers.HARRALANDER_POTION_UNF), new Item (ItemIdentifiers.RED_SPIDERS_EGGS)}, new Item(ItemIdentifiers.RESTORE_POTION_3), Skills.HERBLORE),
        GUTHIX_BALANCE_UNF(22, 25, 363, new Item[]{ new Item(ItemIdentifiers.RESTORE_POTION_3), new Item (ItemIdentifiers.GARLIC)}, new Item(ItemIdentifiers.GUTHIX_BALANCE_UNF), Skills.HERBLORE),
        GUTHIX_BALANCE(22, 25, 363, new Item[]{ new Item(ItemIdentifiers.GUTHIX_BALANCE_UNF), new Item (ItemIdentifiers.SILVER_DUST)}, new Item(ItemIdentifiers.GUTHIX_BALANCE_3), Skills.HERBLORE),
        BLAMISH_OIL(25, 80, 363, new Item[]{ new Item(ItemIdentifiers.HARRALANDER_POTION_UNF), new Item (ItemIdentifiers.BLAMISH_SNAIL_SLIME)}, new Item(ItemIdentifiers.BLAMISH_OIL), Skills.HERBLORE),
        ENERGY_POTION(26, 67.5, 363, new Item[]{ new Item(ItemIdentifiers.HARRALANDER_POTION_UNF), new Item (ItemIdentifiers.CHOCOLATE_DUST)}, new Item(ItemIdentifiers.ENERGY_POTION_3), Skills.HERBLORE),
        SUPER_FISHING_EXPLOSIVE(31, 55, 363, new Item[]{ new Item(ItemIdentifiers.GUAM_POTION_UNF), new Item (ItemIdentifiers.RUBIUM)}, new Item(ItemIdentifiers.SUPER_FISHING_EXPLOSIVE), Skills.HERBLORE),
        AGILITY_POTION(34, 80, 363, new Item[]{ new Item(ItemIdentifiers.TOADFLAX_POTION_UNF), new Item (ItemIdentifiers.TOADS_LEGS)}, new Item(ItemIdentifiers.AGILITY_POTION_3), Skills.HERBLORE),
        COMBAT_POTION(36, 84, 363, new Item[]{ new Item(ItemIdentifiers.HARRALANDER_POTION_UNF), new Item (ItemIdentifiers.GOAT_HORN_DUST)}, new Item(ItemIdentifiers.COMBAT_POTION_3), Skills.HERBLORE),
        GOBLIN_POTION(37, 85, 363, new Item[]{ new Item(ItemIdentifiers.TOADFLAX_POTION_UNF), new Item (ItemIdentifiers.PHARMAKOS_BERRIES)}, new Item(ItemIdentifiers.GOBLIN_POTION_3), Skills.HERBLORE),
        PRAYER_POTION(38, 87.5, 363, new Item[]{ new Item(ItemIdentifiers.RANARR_POTION_UNF), new Item (ItemIdentifiers.SNAPE_GRASS)}, new Item(ItemIdentifiers.PRAYER_POTION_3), Skills.HERBLORE),
        SUMMONING_POTION(40, 92, 363, new Item[]{ new Item(ItemIdentifiers.SPIRIT_WEED_POTION_UNF), new Item (ItemIdentifiers.COCKATRICE_EGG)}, new Item(ItemIdentifiers.SUMMONING_POTION_3), Skills.HERBLORE),
        CRAFTING_POTION(42, 95, 363, new Item[]{ new Item(ItemIdentifiers.WERGALI_POTION_UNF), new Item (ItemIdentifiers.FROG_SPAWN)}, new Item(ItemIdentifiers.CRAFTING_POTION_3), Skills.HERBLORE),
        SUPER_ATTACK(45, 100, 363, new Item[]{ new Item(ItemIdentifiers.IRIT_POTION_UNF), new Item (ItemIdentifiers.EYE_OF_NEWT)}, new Item(ItemIdentifiers.SUPER_ATTACK_3), Skills.HERBLORE),
        SUPER_ANTIPOISON(48, 106.3, 363, new Item[]{ new Item(ItemIdentifiers.IRIT_POTION_UNF), new Item (ItemIdentifiers.UNICORN_HORN_DUST)}, new Item(ItemIdentifiers.SUPER_ANTIPOISON_3), Skills.HERBLORE),
        FISHING_POTION(50, 112.5, 363, new Item[]{ new Item(ItemIdentifiers.AVANTOE_POTION_UNF), new Item (ItemIdentifiers.SNAPE_GRASS)}, new Item(ItemIdentifiers.FISHING_POTION_3), Skills.HERBLORE),
        SUPER_ENERGY(52, 117.5, 363, new Item[]{ new Item(ItemIdentifiers.AVANTOE_POTION_UNF), new Item (ItemIdentifiers.MORT_MYRE_FUNGUS)}, new Item(ItemIdentifiers.SUPER_ENERGY_3), Skills.HERBLORE),
        HUNTER_POTION(53, 120, 363, new Item[]{ new Item(ItemIdentifiers.AVANTOE_POTION_UNF), new Item (ItemIdentifiers.KEBBIT_TEETH_DUST)}, new Item(ItemIdentifiers.HUNTER_POTION_3), Skills.HERBLORE),
        SUPER_STRENGTH_POTION(55, 125, 363, new Item[]{ new Item(ItemIdentifiers.KWUARM_POTION_UNF), new Item (ItemIdentifiers.LIMPWURT_ROOT)}, new Item(ItemIdentifiers.SUPER_STRENGTH_3), Skills.HERBLORE),
        MAGIC_ESSENCE_POTION(57, 130, 363, new Item[]{ new Item(ItemIdentifiers.MAGIC_ESSENCE_UNF), new Item (ItemIdentifiers.GORAK_CLAW_POWDER)}, new Item(ItemIdentifiers.MAGIC_ESSENCE_3), Skills.HERBLORE),
        FLETCHING_POTION(58, 132, 363, new Item[]{ new Item(ItemIdentifiers.WERGALI_POTION_UNF), new Item (ItemIdentifiers.WIMPY_FEATHER)}, new Item(ItemIdentifiers.FLETCHING_POTION_3), Skills.HERBLORE),
        WEAPON_POISON(60, 137.5, 363, new Item[]{ new Item(ItemIdentifiers.KWUARM_POTION_UNF), new Item (ItemIdentifiers.DRAGON_SCALE_DUST)}, new Item(ItemIdentifiers.WEAPON_POISON_3), Skills.HERBLORE),
        SUPER_RESTORE_POTION(63, 142.5, 363, new Item[]{ new Item(ItemIdentifiers.SNAPDRAGON_POTION_UNF), new Item (ItemIdentifiers.RED_SPIDERS_EGGS)}, new Item(ItemIdentifiers.SUPER_RESTORE_3), Skills.HERBLORE),
        MIXTURE_STEP1(65, 47.5, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_RESTORE_3), new Item (ItemIdentifiers.UNICORN_HORN_DUST)}, new Item(ItemIdentifiers.MIXTURE__STEP_1_3), Skills.HERBLORE),
        MIXTURE_STEP2(65, 52.5, 363, new Item[]{ new Item(ItemIdentifiers.MIXTURE__STEP_1_1), new Item (ItemIdentifiers.CLEAN_SNAKE_WEED)}, new Item(ItemIdentifiers.MIXTURE__STEP_2_3), Skills.HERBLORE),
        SANFEW_SERUM(65, 60, 363, new Item[]{ new Item(ItemIdentifiers.MIXTURE__STEP_2_1), new Item (ItemIdentifiers.NAIL_BEAST_NAILS)}, new Item(ItemIdentifiers.SANFEW_SERUM_3), Skills.HERBLORE),
        SUPER_DEFENCE_POTION(66, 150, 363, new Item[]{ new Item(ItemIdentifiers.CADANTINE_POTION_UNF), new Item (ItemIdentifiers.WHITE_BERRIES)}, new Item(ItemIdentifiers.SUPER_DEFENCE_3), Skills.HERBLORE),
        ANTIPOISON_PLUS(68, 155, 363, new Item[]{ new Item(ItemIdentifiers.ANTIPOISON_PLUS_UNF), new Item (ItemIdentifiers.YEW_ROOTS)}, new Item(ItemIdentifiers.ANTIPOISON_PLUS_3), Skills.HERBLORE),
        ANTIFIRE_POTION(69, 157.5, 363, new Item[]{ new Item(ItemIdentifiers.LANTADYME_POTION_UNF), new Item (ItemIdentifiers.DRAGON_SCALE_DUST)}, new Item(ItemIdentifiers.ANTIFIRE_3), Skills.HERBLORE),
        SUPER_RANGING_POTION(72, 162.5, 363, new Item[]{ new Item(ItemIdentifiers.DWARF_WEED_POTION_UNF), new Item (ItemIdentifiers.WINE_OF_ZAMORAK)}, new Item(ItemIdentifiers.SUPER_RANGING_POTION_3), Skills.HERBLORE),
        WEAPON_POISON_PLUS(73, 165, 363, new Item[]{ new Item(ItemIdentifiers.WEAPON_POISON_PLUS_UNF), new Item (ItemIdentifiers.POISON_IVY_BERRIES)}, new Item(ItemIdentifiers.WEAPON_POISON_PLUS_3), Skills.HERBLORE),
        SUPER_MAGIC_POTION(76, 172.5, 363, new Item[]{ new Item(ItemIdentifiers.LANTADYME_POTION_UNF), new Item (ItemIdentifiers.POTATO_CACTUS)}, new Item(ItemIdentifiers.SUPER_MAGIC_POTION_3), Skills.HERBLORE),
        ZAMORAK_BREW(78, 175, 363, new Item[]{ new Item(ItemIdentifiers.TORSTOL_POTION_UNF), new Item (ItemIdentifiers.JANGERBERRIES)}, new Item(ItemIdentifiers.ZAMORAK_BREW_3), Skills.HERBLORE),
        ANTIPOISON_PLUS_PLUS(79, 177.5, 363, new Item[]{ new Item(ItemIdentifiers.ANTIPOISON_PLUS_PLUS_UNF), new Item (ItemIdentifiers.MAGIC_ROOTS)}, new Item(ItemIdentifiers.ANTIPOISON_PLUS_PLUS_3), Skills.HERBLORE),
        SARADOMIN_BREW(81, 180, 363, new Item[]{ new Item(ItemIdentifiers.TOADFLAX_POTION_UNF), new Item (ItemIdentifiers.CRUSHED_NEST)}, new Item(ItemIdentifiers.SARADOMIN_BREW_3), Skills.HERBLORE),
        WEAPON_POISON_PLUS_PLUS(82, 190, 363, new Item[]{ new Item(ItemIdentifiers.WEAPON_POISON_PLUS_PLUS_UNF), new Item (ItemIdentifiers.POISON_IVY_BERRIES)}, new Item(ItemIdentifiers.WEAPON_POISON_PLUS_PLUS_3), Skills.HERBLORE),
        ADRENALINE_POTION(84, 200, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_ENERGY_3), new Item (ItemIdentifiers.PAPAYA_FRUIT)}, new Item(ItemIdentifiers.ADRENALINE_POTION_3), Skills.HERBLORE),
        SUPER_ANTIFIRE(85, 210, 363, new Item[]{ new Item(ItemIdentifiers.ANTIFIRE_3), new Item (ItemIdentifiers.PHOENIX_FEATHER)}, new Item(ItemIdentifiers.SUPER_ANTIFIRE_3), Skills.HERBLORE),
        EXTREME_ATTACK(88, 220, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_ATTACK_3), new Item (ItemIdentifiers.CLEAN_AVANTOE)}, new Item(ItemIdentifiers.EXTREME_ATTACK_3), Skills.HERBLORE),
        EXTREME_STRENGTH(89, 230, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_STRENGTH_3), new Item (ItemIdentifiers.CLEAN_DWARF_WEED)}, new Item(ItemIdentifiers.EXTREME_STRENGTH_3), Skills.HERBLORE),
        EXTREME_DEFENCE(90, 240, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_DEFENCE_3), new Item (ItemIdentifiers.CLEAN_LANTADYME)}, new Item(ItemIdentifiers.EXTREME_DEFENCE_3), Skills.HERBLORE),
        EXTREME_MAGIC(91, 250, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_MAGIC_POTION_3), new Item (ItemIdentifiers.GROUND_MUD_RUNES)}, new Item(ItemIdentifiers.EXTREME_MAGIC_3), Skills.HERBLORE),
        EXTREME_RANGING(92, 260, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_RANGING_POTION_3), new Item (ItemIdentifiers.GRENWALL_SPIKES, 5)}, new Item(ItemIdentifiers.EXTREME_RANGING_3), Skills.HERBLORE),
        SUPER_ZAMORAK_BREW(93, 180, 363, new Item[]{ new Item(ItemIdentifiers.ZAMORAK_BREW_3), new Item (ItemIdentifiers.WINE_OF_ZAMORAK)}, new Item(ItemIdentifiers.SUPER_ZAMORAK_BREW_3), Skills.HERBLORE),
        SUPER_SARADOMIN_BREW(93, 180, 363, new Item[]{ new Item(ItemIdentifiers.SARADOMIN_BREW_3), new Item (ItemIdentifiers.WINE_OF_SARADOMIN)}, new Item(ItemIdentifiers.SUPER_SARADOMIN_BREW_3), Skills.HERBLORE),
        SUPER_GUTHIX_REST(93, 59.3, 363, new Item[]{ new Item(ItemIdentifiers.GUTHIX_REST_3), new Item (ItemIdentifiers.WINE_OF_GUTHIX)}, new Item(ItemIdentifiers.SUPER_GUTHIX_REST_3), Skills.HERBLORE),
        SUPER_PRAYER(94, 270, 363, new Item[]{ new Item(ItemIdentifiers.PRAYER_POTION_3), new Item (ItemIdentifiers.WYVERN_BONEMEAL)}, new Item(ItemIdentifiers.SUPER_PRAYER_3), Skills.HERBLORE),
        PRAYER_RENEWAL(94, 190, 363, new Item[]{ new Item(ItemIdentifiers.FELLSTALK_POTION_UNF), new Item (ItemIdentifiers.MORCHELLA_MUSHROOM)}, new Item(ItemIdentifiers.PRAYER_RENEWAL_3), Skills.HERBLORE),
        OVERLOAD(96, 1000, 363, new Item[]{ new Item(ItemIdentifiers.CLEAN_TORSTOL), new Item (ItemIdentifiers.EXTREME_ATTACK_3), new Item (ItemIdentifiers.EXTREME_STRENGTH_3), new Item(ItemIdentifiers.EXTREME_DEFENCE_3),
        		new Item(ItemIdentifiers.EXTREME_MAGIC_3), new Item(ItemIdentifiers.EXTREME_RANGING_3)}, new Item(ItemIdentifiers.OVERLOAD_3), Skills.HERBLORE),
        
        GRAND_STRENGTH(75, 123.8, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_STRENGTH_4), new Item(ItemIdentifiers.STRENGTH_POTION_3), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.GRAND_STRENGTH_POTION_6), Skills.HERBLORE),
        GRAND_RANGING(76, 144.4, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_RANGING_POTION_4), new Item(ItemIdentifiers.RANGING_POTION_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.GRAND_RANGING_POTION_6), Skills.HERBLORE),
        GRAND_MAGIC(77, 155.7, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_MAGIC_POTION_4), new Item(ItemIdentifiers.MAGIC_POTION_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.GRAND_MAGIC_POTION_6), Skills.HERBLORE),
        GRAND_ATTACK(78, 93.8, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_ATTACK_4), new Item(ItemIdentifiers.ATTACK_POTION_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.GRAND_ATTACK_POTION_6), Skills.HERBLORE),
        GRAND_DEFENCE(79, 146.3, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_DEFENCE_4), new Item (ItemIdentifiers.DEFENCE_POTION_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.GRAND_DEFENCE_POTION_6), Skills.HERBLORE),
        SUPER_MELEE(81, 281.3, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_ATTACK_4), new Item(ItemIdentifiers.SUPER_STRENGTH_4), new Item(ItemIdentifiers.SUPER_DEFENCE_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPER_MELEE_POTION_4), Skills.HERBLORE),
        SUPER_WARMASTERS(85, 500, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_ATTACK_4), new Item(ItemIdentifiers.SUPER_STRENGTH_4), new Item(ItemIdentifiers.SUPER_DEFENCE_4), new Item(ItemIdentifiers.SUPER_RANGING_POTION_4),
        		new Item(ItemIdentifiers.SUPER_MAGIC_POTION_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPER_WARMASTERS_POTION_6), Skills.HERBLORE),
        REPLENISHMENT(87, 256.9, 363, new Item[]{ new Item(ItemIdentifiers.ADRENALINE_POTION_4), new Item(ItemIdentifiers.SUPER_RESTORE_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.REPLENISHMENT_POTION_6), Skills.HERBLORE),
        WYRMFIRE(89, 275.7, 363, new Item[]{ new Item(ItemIdentifiers.ANTIFIRE_4), new Item(ItemIdentifiers.SUPER_ANTIFIRE_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.WYRMFIRE_POTION_6), Skills.HERBLORE), //26025 buff
        EXTREME_BRAWLER(91, 367.5, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_ATTACK_4), new Item(ItemIdentifiers.EXTREME_STRENGTH_4), new Item(ItemIdentifiers.EXTREME_DEFENCE_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.EXTREME_BRAWLERS_POTION_6), Skills.HERBLORE),
        EXTREME_BATTLEMAGE(91, 367.5, 363, new Item[]{ new Item(ItemIdentifiers.EXTREME_MAGIC_4), new Item(ItemIdentifiers.EXTREME_DEFENCE_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.EXTREME_BATTLEMAGES_POTION_6), Skills.HERBLORE),
        EXTREME_SHARPSHOOTER(91, 367.5, 363, new Item[]{ new Item(ItemIdentifiers.EXTREME_RANGING_4), new Item (ItemIdentifiers.EXTREME_DEFENCE_4)}, new Item(ItemIdentifiers.EXTREME_SHARPSHOOTERS_POTION_6), Skills.HERBLORE),
        EXTREME_WARMASTERS(93, 500, 363, new Item[]{ new Item(ItemIdentifiers.EXTREME_ATTACK_4), new Item(ItemIdentifiers.EXTREME_ATTACK_4), new Item(ItemIdentifiers.EXTREME_DEFENCE_4), new Item(ItemIdentifiers.EXTREME_MAGIC_4), new Item(ItemIdentifiers.EXTREME_RANGING_4),
        		new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.EXTREME_WARMASTERS_POTION_6), Skills.HERBLORE),
        SUPREME_STRENGTH(93, 266.3, 363, new Item[]{ new Item(ItemIdentifiers.EXTREME_STRENGTH_4), new Item(ItemIdentifiers.SUPER_STRENGTH_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPREME_STRENGTH_POTION_6), Skills.HERBLORE),
        SUPREME_ATTACK(93, 240, 363, new Item[]{ new Item(ItemIdentifiers.EXTREME_ATTACK_4), new Item(ItemIdentifiers.SUPER_ATTACK_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPREME_ATTACK_POTION_6), Skills.HERBLORE),
        SUPREME_DEFENCE(93, 292.5, 363, new Item[]{ new Item(ItemIdentifiers.EXTREME_DEFENCE_4), new Item(ItemIdentifiers.SUPER_DEFENCE_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPREME_DEFENCE_POTION_6), Skills.HERBLORE),
        SUPREME_MAGIC(93, 316.9, 363, new Item[]{ new Item(ItemIdentifiers.EXTREME_MAGIC_4), new Item (ItemIdentifiers.SUPER_MAGIC_POTION_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPREME_MAGIC_POTION_6), Skills.HERBLORE),
        SUPREME_RANGING(93, 217.5, 363, new Item[]{ new Item(ItemIdentifiers.EXTREME_RANGING_4), new Item (ItemIdentifiers.SUPER_RANGING_POTION_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPREME_RANGING_POTION_6), Skills.HERBLORE),
        BRIGHTFIRE(94, 300, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_ANTIFIRE_4), new Item(ItemIdentifiers.PRAYER_RENEWAL_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.BRIGHTFIRE_POTION_6), Skills.HERBLORE),
        SUPER_PRAYER_RENEWAL(96, 208.2, 363, new Item[]{ new Item(ItemIdentifiers.PRAYER_RENEWAL_4), new Item (ItemIdentifiers.PRAYER_POTION_4), new Item(ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPER_PRAYER_RENEWAL_POTION_6), Skills.HERBLORE), // 25852 buff
        HOLY_OVERLOAD(97, 350, 363, new Item[]{ new Item(ItemIdentifiers.PRAYER_RENEWAL_4), new Item (ItemIdentifiers.OVERLOAD_4), new Item(ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.HOLY_OVERLOAD_POTION_6), Skills.HERBLORE),
        SEARING_OVERLOAD(97, 350, 363, new Item[]{ new Item(ItemIdentifiers.SUPER_ANTIFIRE_4), new Item (ItemIdentifiers.OVERLOAD_4), new Item(ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SEARING_OVERLOAD_POTION_6), Skills.HERBLORE),
        OVERLOAD_SALVE(97, 500, 363, new Item[]{ new Item(ItemIdentifiers.OVERLOAD_4), new Item(ItemIdentifiers.SUPER_ANTIFIRE_4), new Item(ItemIdentifiers.ANTIFIRE_4), new Item(ItemIdentifiers.PRAYER_RENEWAL_4), new Item(ItemIdentifiers.PRAYER_POTION_4), new Item(ItemIdentifiers.SUPER_ANTIPOISON_4),
        		new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.OVERLOAD_SALVE_6), Skills.HERBLORE),
        SUPREME_OVERLOAD(98, 600, 363, new Item[]{ new Item(ItemIdentifiers.OVERLOAD_4), new Item(ItemIdentifiers.SUPER_ATTACK_4), new Item(ItemIdentifiers.SUPER_STRENGTH_4), new Item(ItemIdentifiers.SUPER_DEFENCE_4), new Item(ItemIdentifiers.SUPER_RANGING_POTION_4), new Item(ItemIdentifiers.SUPER_MAGIC_POTION_4),
        		new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPREME_OVERLOAD_POTION_6), Skills.HERBLORE),
        SUPREME_OVERLOAD_SALVE(98, 700, 363, new Item[]{ new Item(ItemIdentifiers.SUPREME_OVERLOAD_POTION_6), new Item(ItemIdentifiers.SUPER_ANTIFIRE_4), new Item(ItemIdentifiers.ANTIFIRE_4), new Item(ItemIdentifiers.PRAYER_RENEWAL_4), new Item(ItemIdentifiers.PRAYER_POTION_4),
        		new Item(ItemIdentifiers.SUPER_ANTIPOISON_4), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.SUPREME_OVERLOAD_SALVE_6), Skills.HERBLORE),
        PERFECT_PLUS(99, 1000, 363, new Item[]{ new Item(ItemIdentifiers.OVERLOAD_4), new Item(ItemIdentifiers.HARMONY_MOSS), new Item(ItemIdentifiers.CRYSTAL_TREE_BLOSSOM), new Item (ItemIdentifiers.CRYSTAL_FLASK)}, new Item(ItemIdentifiers.PERFECT_PLUS_POTION_6), Skills.HERBLORE); //25854 buff
        
     
		public static CleanAction getHerbByProduce(int id) {
		    for(CleanAction herb : CleanAction.values())
				if(herb.getProducedHerb().getId() == id)
					return herb;
		    return null;
		}

		public static CleanAction getHerb(int id) {
		    for(CleanAction herb : CleanAction.values())
		    	for(Item item : herb.getItemsRequired())
		    		if(item.getId() == id)
		    			return herb;
		    return null;
		}
		
		private static final HashMap<Integer, CleanAction> HERBLORE =new HashMap<>();
		
		static {
			for (CleanAction f : values()) {
				HERBLORE.put(f.getItemsRequired()[0].getId(), f);
			}
		}
		
		public static CleanAction getHerbItem(int id) {
			return HERBLORE.get(id);
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedHerb;
		private int skillType;
		private int emote;

		CleanAction(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedHerb, int skillType) {
		    this.levelRequired = levelRequired;
		    this.experience = experience;
		    this.itemsRequired = itemsRequired;
		    this.producedHerb = producedHerb;
		    this.skillType = skillType;
		    this.emote = emote;
		}

		public Item[] getItemsRequired() {
		    return itemsRequired;
		}

		public int getLevelRequired() {
		    return levelRequired;
		}
		
        
		public Item getProducedHerb() {
		    return producedHerb;
		}
		
		public double getExperience() {
		    return experience;
		}

		public int getSkillType() {
		    return skillType;
		}
		
		public int getEmote() {
			return emote;
		}
	    }

	    public CleanAction herb;
	    public int ticks;
	    public Item item;
	    private int xpMultiplier = 1;

	    public Herblore(CleanAction herb, Item item, int ticks) {
		this.herb = herb;
		this.item = item;
		this.ticks = ticks;
	    }
	
	@Override
	public boolean start(Player player) {

		return !(herb==null||player==null);
	}
	    @Override
	    public boolean process(Player player) {
		if (herb == null || player == null) {
			player.getInterfaceManager().removeInterface(1251);
		    return false;
		}
		if (!player.getInventory().containsItemToolBelt(herb.getItemsRequired()[0].getId(), herb.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (herb.getItemsRequired().length > 1) {
		    if (!player.getInventory().containsItemToolBelt(herb.getItemsRequired()[1].getId(), herb.getItemsRequired()[1].getAmount())) {
		    	player.getInterfaceManager().removeInterface(1251);
		    	return false;
		    }
		}
		    return player.getSkills().hasLevel(herb.getSkillType(), herb.getLevelRequired());
	    }
	    
	    public static CleanAction isMixing(Player player, Item used, Item usedWith) {
        	CleanAction herb;
        	herb = CleanAction.getHerbItem(used.getId());
    		if (herb == null)
    			herb = CleanAction.getHerbItem(usedWith.getId());
    		if (herb != null)
    			return herb;
    		return null;
        }

	@Override
	public int processWithDelay(Player player) {
		ticks--;			
		int multiplier = 0;	
		int emote = herb.getEmote();
		double xp = herb.getExperience();
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		int amount = herb.getProducedHerb().getAmount() * multiplier ;
		if (herb.getItemsRequired().length == 1 && herb.getItemsRequired()[0].getName().toLowerCase().contains("grimy"))
			player.getSkillTasks().handleTask(HerbloreType.CLEAN, 1);
		else
			player.getSkillTasks().handleTask(HerbloreType.POTION, 1);
		for (Item required : herb.getItemsRequired()) {
		    player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, herb.getProducedHerb().getId(), maxQuantity, (int) xp);
		player.getPackets().sendExecuteScript(3373, 1018);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		player.getInventory().addItem(herb.getProducedHerb().getId(), amount);
		if (herb.toString().toLowerCase().contains("overload"))
			player.getCompCapeManager().increaseRequirement(Requirement.OVERLOADS, 1);
		if (player.getClanManager() != null && player.getClanManager().getClan() != null)
			player.getClanManager().getClan().increaseGatheredResources(amount);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,	herb.getProducedHerb().getId(), Skills.HERBLORE);
		player.getSkills().addXp(herb.getSkillType(), xp);
		player.setNextAnimation(new Animation(emote));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
		
	}

}
