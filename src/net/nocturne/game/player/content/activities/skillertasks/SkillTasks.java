package net.nocturne.game.player.content.activities.skillertasks;

import java.io.Serializable;

import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.agility.Agility.AgilityCourses;
import net.nocturne.game.player.actions.skills.cooking.Cooking.Cookables;
import net.nocturne.game.player.actions.skills.divination.WispInfo;
import net.nocturne.game.player.actions.skills.firemaking.Firemaking.Fire;
import net.nocturne.game.player.actions.skills.fishing.Fishing.Fish;
import net.nocturne.game.player.actions.skills.herblore.Herblore.HerbloreType;
import net.nocturne.game.player.actions.skills.hunter.TrapAction.Traps;
import net.nocturne.game.player.actions.skills.mining.Mining.RockDefinitions;
import net.nocturne.game.player.actions.skills.runecrafting.Runecrafting.RunecraftingTypes;
import net.nocturne.game.player.actions.skills.smithing.Smelting.SmeltingBar;
import net.nocturne.game.player.actions.skills.thieving.Thieving.ThievingTypes;
import net.nocturne.game.player.actions.skills.woodcutting.Woodcutting.TreeDefinitions;

/**
 * @author King Fox & Miles Black August 19, 2014 Handles the Skiller Tasks
 */

public enum SkillTasks implements Serializable {

	// Agility
	APE1(AgilityCourses.APE_ATOLL, "Ape Atoll Laps Medium",
			"You must complete 15 laps around the Ape Atoll Agility Course.",
			SkillerTasks.MEDIUM, 15, Skills.AGILITY, 48), APE2(
			AgilityCourses.APE_ATOLL, "Ape Atoll Laps Hard",
			"You must complete 20 laps around the Ape Atoll Agility Course.",
			SkillerTasks.HARD, 20, Skills.AGILITY, 48), APE3(
			AgilityCourses.APE_ATOLL, "Ape Atoll Laps Elite",
			"You must complete 35 laps around the Ape Atoll Agility Course.",
			SkillerTasks.ELITE, 35, Skills.AGILITY, 48), BARB1(
			AgilityCourses.BARBARIAN_OUTPOST, "Barbarian Laps Easy",
			"You must complete 10 laps around the Barbarian Agility Course.",
			SkillerTasks.EASY, 10, Skills.AGILITY, 35), BARB2(
			AgilityCourses.BARBARIAN_OUTPOST, "Barbarian Laps Medium",
			"You must complete 15 laps around the Barbarian Agility Course.",
			SkillerTasks.MEDIUM, 15, Skills.AGILITY, 35), BARB3(
			AgilityCourses.BARBARIAN_OUTPOST_ADVANCED,
			"Barbarian Adv Laps Hard",
			"You must complete 20 laps around the Barbarian Adv Agility Course.",
			SkillerTasks.HARD, 20, Skills.AGILITY, 90), BARB4(
			AgilityCourses.BARBARIAN_OUTPOST_ADVANCED,
			"Barbarian Adv Laps Elite",
			"You must complete 35 laps around the Barbarian Adv Agility Course.",
			SkillerTasks.ELITE, 35, Skills.AGILITY, 90), GNOME1(
			AgilityCourses.GNOME_AGILITY, "Gnome Laps Easy",
			"You must complete 10 laps around the Gnome Agility Course.",
			SkillerTasks.EASY, 10, Skills.AGILITY, 1), GNOME2(
			AgilityCourses.GNOME_AGILITY, "Gnome Laps Medium",
			"You must complete 15 laps around the Gnome Agility Course.",
			SkillerTasks.MEDIUM, 15, Skills.AGILITY, 1), GNOME3(
			AgilityCourses.GNOME_AGILITY_ADVANCED, "Gnome Laps Hard",
			"You must complete 20 laps around the Gnome Adv Agility Course.",
			SkillerTasks.HARD, 20, Skills.AGILITY, 85), PYRAMID1(
			AgilityCourses.AGILITY_PYRAMID, "Pyramid Laps Hard",
			"You must complete 8 laps around the Pyramid Agility Course.",
			SkillerTasks.HARD, 8, Skills.AGILITY, 30), PYRAMID2(
			AgilityCourses.AGILITY_PYRAMID, "Pyramid Laps Elite",
			"You must complete 12 laps around the Pyramid Agility Course.",
			SkillerTasks.ELITE, 12, Skills.AGILITY, 30), WILD1(
			AgilityCourses.WILDERNESS_AGILITY, "Wilderness Laps Medium",
			"You must complete 15 laps around the Wilderness Agility Course.",
			SkillerTasks.MEDIUM, 15, Skills.AGILITY, 52), WILD2(
			AgilityCourses.WILDERNESS_AGILITY, "Wilderness Laps Hard",
			"You must complete 20 laps around the Wilderness Agility Course.",
			SkillerTasks.HARD, 20, Skills.AGILITY, 52), WILD3(
			AgilityCourses.WILDERNESS_AGILITY, "Wilderness Laps Elite",
			"You must complete 35 laps around the Wilderness Agility Course.",
			SkillerTasks.ELITE, 35, Skills.AGILITY, 52), HEFIN1(
			AgilityCourses.HEFIN_AGILITY, "Hefin Laps Elite",
			"You must complete 35 laps around the Hefin Agility Course.",
			SkillerTasks.ELITE, 35, Skills.AGILITY, 77),

	// Cooking
	CANCHOVIES1(Cookables.RAW_ANCHOVIES, "Cook Anchovies Easy",
			"You must successfully cook 50 Raw Anchovies.", SkillerTasks.EASY,
			50, Skills.COOKING, 1), CANCHOVIES2(Cookables.RAW_ANCHOVIES,
			"Cook Anchovies Medium",
			"You must successfully cook 125 Raw Anchovies.",
			SkillerTasks.MEDIUM, 125, Skills.COOKING, 1), CHERRING1(
			Cookables.RAW_HERRING, "Cook Herring Easy",
			"You must successfully cook 50 Raw Herring.", SkillerTasks.EASY,
			50, Skills.COOKING, 5), CHERRING2(Cookables.RAW_HERRING,
			"Cook Herring Mediun",
			"You must successfully cook 125 Raw Herring.", SkillerTasks.MEDIUM,
			125, Skills.COOKING, 5), CLOBSTER1(Cookables.RAW_LOBSTER,
			"Cook Lobster Medium",
			"You must successfully cook 75 Raw Lobster.", SkillerTasks.MEDIUM,
			75, Skills.COOKING, 40), CLOBSTER2(Cookables.RAW_LOBSTER,
			"Cook Lobster Hard", "You must successfully cook 160 Raw Lobster.",
			SkillerTasks.HARD, 160, Skills.COOKING, 40), CROCKTAIL1(
			Cookables.RAW_ROCKTAIL, "Cook Rocktail Hard",
			"You must successfully cook 100 Raw Rocktail.", SkillerTasks.HARD,
			100, Skills.COOKING, 93), CROCKTAIL2(Cookables.RAW_ROCKTAIL,
			"Cook Rocktail Elite",
			"You must successfully cook 180 Raw Rocktail.", SkillerTasks.ELITE,
			180, Skills.COOKING, 93), CSALMON1(Cookables.RAW_SALMON,
			"Cook Salmon Easy", "You must successfully cook 90 Raw Salmon.",
			SkillerTasks.EASY, 90, Skills.COOKING, 25), CSALMON2(
			Cookables.RAW_SALMON, "Cook Salmon Medium",
			"You must successfully cook 200 Raw Salmon.", SkillerTasks.MEDIUM,
			200, Skills.COOKING, 25), CSHARK1(Cookables.RAW_SHARK,
			"Cook Shark Medium", "You must successfully cook 75 Raw Shark.",
			SkillerTasks.MEDIUM, 75, Skills.COOKING, 80), CSHARK2(
			Cookables.RAW_SHARK, "Cook Shark Hard",
			"You must successfully cook 150 Raw Shark.", SkillerTasks.HARD,
			150, Skills.COOKING, 80), CSHARK3(Cookables.RAW_SHARK,
			"Cook Shark Elite", "You must successfully cook 275 Raw Shark.",
			SkillerTasks.ELITE, 275, Skills.COOKING, 80), CSHRIMP1(
			Cookables.RAW_SHRIMP, "Cook Shrimp Easy",
			"You must successfully cook 50 Raw Shrimp.", SkillerTasks.EASY, 50,
			Skills.COOKING, 1), CSHRIMP2(Cookables.RAW_SHRIMP,
			"Cook Shrimp Medium", "You must successfully cook 125 Raw Shrimp.",
			SkillerTasks.MEDIUM, 125, Skills.COOKING, 1), CSWORD1(
			Cookables.RAW_SWORDFISH, "Cook Swordfish Medium",
			"You must successfully cook 100 Raw Swordfish.",
			SkillerTasks.MEDIUM, 100, Skills.COOKING, 45), CSWORD2(
			Cookables.RAW_SWORDFISH, "Cook Swordfish Hard",
			"You must successfully cook 225 Raw Swordfish.", SkillerTasks.HARD,
			225, Skills.COOKING, 45), CTROUT1(Cookables.RAW_TROUT,
			"Cook Trout Easy", "You must successfully cook 50 Raw Trout.",
			SkillerTasks.EASY, 50, Skills.COOKING, 15), CTROUT2(
			Cookables.RAW_TROUT, "Cook Trout Medium",
			"You must successfully cook 125 Raw Trout.", SkillerTasks.MEDIUM,
			125, Skills.COOKING, 15), CTUNA1(Cookables.RAW_TUNA,
			"Cook Tuna Easy", "You must successfully cook 50 Raw Tuna.",
			SkillerTasks.EASY, 50, Skills.COOKING, 30), CTUNA2(
			Cookables.RAW_TUNA, "Cook Tuna",
			"You must successfully cook 125 Raw Tuna.", SkillerTasks.MEDIUM,
			125, Skills.COOKING, 30),

	// Crafting
	/*
	 * AMULET1("Craft Amulet Easy", "You must successfully craft 60 Amulets.",
	 * SkillerTasks.EASY, 60, Skills.CRAFTING, 8), AMULET2(
	 * "Craft Amulet Medium", "You must successfully craft 150 Amulets.",
	 * SkillerTasks.MEDIUM, 150, Skills.CRAFTING, 8), AMULET3(
	 * "Craft Amulet Hard", "You must successfully craft 225 Amulets.",
	 * SkillerTasks.HARD, 225, Skills.CRAFTING, 8), AMULET4(
	 * "Craft Amulet Elite", "You must successfully craft 350 Amulets.",
	 * SkillerTasks.ELITE, 350, Skills.CRAFTING, 8), BRACELET1(
	 * "Craft Bracelet Easy", "You must successfully craft 60 Bracelets.",
	 * SkillerTasks.EASY, 60, Skills.CRAFTING, 7),
	 * BRACELET2("Craft Bracelet Medium",
	 * "You must successfully craft 150 Bracelets.", SkillerTasks.MEDIUM, 150,
	 * Skills.CRAFTING, 7), BRACELET3("Craft Bracelet Hard",
	 * "You must successfully craft 225 Bracelets.", SkillerTasks.HARD, 225,
	 * Skills.CRAFTING, 7), BRACELET4("Craft Bracelet Elite",
	 * "You must successfully craft 350 Bracelets.", SkillerTasks.ELITE, 350,
	 * Skills.CRAFTING, 7), DIAMOND1("Cut Diamond Medium",
	 * "You must successfully cut 75 Uncut Diamonds.", SkillerTasks.MEDIUM, 75,
	 * Skills.CRAFTING, 43), DIAMOND2( "Cut Diamond Hard",
	 * "You must successfully cut 150 Uncut Diamonds.", SkillerTasks.HARD, 150,
	 * Skills.CRAFTING, 43), EMERALD1("Cut Emerald Easy",
	 * "You must successfully cut 50 Uncut Emeralds.", SkillerTasks.EASY, 50,
	 * Skills.CRAFTING, 27), EMERALD2("Cut Emerald Medium",
	 * "You must successfully cut 150 Uncut Emeralds.", SkillerTasks.MEDIUM,
	 * 150, Skills.CRAFTING, 27), NECKLACE1( "Craft Neaklace Easy",
	 * "You must successfully craft 60 Necklace.", SkillerTasks.EASY, 60,
	 * Skills.CRAFTING, 6), NECKLACE2( "Craft Necklace Medium",
	 * "You must successfully craft 150 Necklace.", SkillerTasks.MEDIUM, 150,
	 * Skills.CRAFTING, 6), NECKLACE3("Craft Necklace Hard",
	 * "You must successfully craft 225 Necklace.", SkillerTasks.HARD, 225,
	 * Skills.CRAFTING, 6), NECKLACE4("Craft Necklace Elite",
	 * "You must successfully craft 350 Necklace.", SkillerTasks.ELITE, 350,
	 * Skills.CRAFTING, 6), RING1("Craft Ring Easy",
	 * "You must successfully craft 60 Rings.", SkillerTasks.EASY, 60,
	 * Skills.CRAFTING, 5), RING2("Craft Ring Medium",
	 * "You must successfully craft 150 Rings.", SkillerTasks.MEDIUM, 150,
	 * Skills.CRAFTING, 5), RING3("Craft Ring Hard",
	 * "You must successfully craft 225 Rings.", SkillerTasks.HARD, 225,
	 * Skills.CRAFTING, 5), RING4("Craft Ring Elite",
	 * "You must successfully craft 350 Rings.", SkillerTasks.ELITE, 350,
	 * Skills.CRAFTING, 5), RUBY1("Cut Ruby Easy",
	 * "You must successfully cut 50 Uncut Rubies.", SkillerTasks.EASY, 50,
	 * Skills.CRAFTING, 34), RUBY2("Cut Ruby Medium",
	 * "You must successfully cut 150 Uncut Rubies.", SkillerTasks.MEDIUM, 150,
	 * Skills.CRAFTING, 34), RUBY3("Cut Ruby Hard",
	 * "You must successfully cut 225 Uncut Rubies.", SkillerTasks.HARD, 225,
	 * Skills.CRAFTING, 34), SAPPHIRE1("Cut Sapphire Easy",
	 * "You must successfully cut 50 Uncut Sapphires.", SkillerTasks.EASY, 50,
	 * Skills.CRAFTING, 20), SAPPHIRE2( "Cut Sapphire Medium",
	 * "You must successfully cut 150 Uncut Sapphires.", SkillerTasks.MEDIUM,
	 * 150, Skills.CRAFTING, 20),
	 */

	/**
	 * Divination
	 */
	DIV1(WispInfo.PALE, "Divinaton Easy", "Harvest 75 pale wisps.",
			SkillerTasks.EASY, 75, Skills.DIVINATION, 1), DIV1_2(WispInfo.PALE,
			"Divinaton Medium", "Harvest 125 pale wisps.", SkillerTasks.MEDIUM,
			125, Skills.DIVINATION, 1), DIV1_3(WispInfo.PALE, "Divinaton Hard",
			"Harvest 250 pale wisps.", SkillerTasks.HARD, 250,
			Skills.DIVINATION, 1), DIV1_4(WispInfo.PALE, "Divinaton Elite",
			"Harvest 400 pale wisps.", SkillerTasks.ELITE, 400,
			Skills.DIVINATION, 1), DIV2(WispInfo.GLEAMING, "Divinaton Easy",
			"Harvest 75 gleaming wisps.", SkillerTasks.EASY, 75,
			Skills.DIVINATION, 50), DIV2_2(WispInfo.GLEAMING,
			"Divinaton Medium", "Harvest 125 gleaming wisps.",
			SkillerTasks.MEDIUM, 125, Skills.DIVINATION, 50), DIV2_3(
			WispInfo.GLEAMING, "Divinaton Hard", "Harvest 250 gleaming wisps.",
			SkillerTasks.HARD, 250, Skills.DIVINATION, 50), DIV2_4(
			WispInfo.GLEAMING, "Divinaton Elite",
			"Harvest 400 gleaming wisps.", SkillerTasks.ELITE, 400,
			Skills.DIVINATION, 50), DIV3(WispInfo.BRILLIANT, "Divinaton Easy",
			"Harvest 75 brilliant wisps.", SkillerTasks.EASY, 75,
			Skills.DIVINATION, 80), DIV3_2(WispInfo.BRILLIANT,
			"Divinaton Medium", "Harvest 125 brilliant wisps.",
			SkillerTasks.MEDIUM, 125, Skills.DIVINATION, 80), DIV3_3(
			WispInfo.BRILLIANT, "Divinaton Hard",
			"Harvest 250 brilliant wisps.", SkillerTasks.HARD, 250,
			Skills.DIVINATION, 80), DIV3_4(WispInfo.BRILLIANT,
			"Divinaton Elite", "Harvest 400 brilliant wisps.",
			SkillerTasks.ELITE, 400, Skills.DIVINATION, 80), DIV4(
			WispInfo.INCANDESCENT, "Divinaton Easy",
			"Harvest 75 incandescent wisps.", SkillerTasks.EASY, 75,
			Skills.DIVINATION, 95), DIV4_2(WispInfo.INCANDESCENT,
			"Divinaton Medium", "Harvest 125 incandescent wisps.",
			SkillerTasks.MEDIUM, 125, Skills.DIVINATION, 95), DIV4_3(
			WispInfo.INCANDESCENT, "Divinaton Hard",
			"Harvest 250 incandescent wisps.", SkillerTasks.HARD, 250,
			Skills.DIVINATION, 95), DIV4_4(WispInfo.INCANDESCENT,
			"Divinaton Elite", "Harvest 400 incandescent wisps.",
			SkillerTasks.ELITE, 400, Skills.DIVINATION, 95),

	// Farming
	/*
	 * FLOWER1("Harvest Flowers Easy", "You must harvest 20 Flowers.",
	 * SkillerTasks.EASY, 20, Skills.FARMING, 2), FLOWER2(
	 * "Harvest Flowers Medium", "You must harvest 50 Flowers.",
	 * SkillerTasks.MEDIUM, 50, Skills.FARMING, 2), FLOWER3(
	 * "Harvest Flowers Hard", "You must harvest 85 Flowers.",
	 * SkillerTasks.HARD, 85, Skills.FARMING, 2), FLOWER4(
	 * "Harvest Flowers Elite", "You must harvest 125 Flowers.",
	 * SkillerTasks.ELITE, 125, Skills.FARMING, 2), FRUIT1(
	 * "Harvest Fruit Easy", "You must harvest 20 Fruit.", SkillerTasks.EASY,
	 * 20, Skills.FARMING, 27), FRUIT2( "Harvest Fruit Medium",
	 * "You must harvest 50 Fruit.", SkillerTasks.MEDIUM, 50, Skills.FARMING,
	 * 27), FRUIT3( "Harvest Fruit Hard", "You must harvest 85 Fruit.",
	 * SkillerTasks.HARD, 85, Skills.FARMING, 27), FRUIT4(
	 * "Harvest Fruit Elite", "You must harvest 125 Fruit.", SkillerTasks.ELITE,
	 * 125, Skills.FARMING, 27), HERB1( "Harvest Herbs Easy",
	 * "You must harvest 20 Herbs.", SkillerTasks.EASY, 20, Skills.FARMING, 9),
	 * HERB2( "Harvest Herbs Medium", "You must harvest 50 Herbs.",
	 * SkillerTasks.MEDIUM, 50, Skills.FARMING, 9), HERB3( "Harvest Herbs Hard",
	 * "You must harvest 85 Herbs.", SkillerTasks.HARD, 85, Skills.FARMING, 9),
	 * HERB4( "Harvest Herbs Elite", "You must harvest 125 Herbs.",
	 * SkillerTasks.ELITE, 125, Skills.FARMING, 9), RAKE1( "Rake Weeds Easy",
	 * "You must rake any patch 20 times.", SkillerTasks.EASY, 20,
	 * Skills.FARMING, 1), RAKE2( "Rake Weeds Medium",
	 * "You must rake any patch 50 times.", SkillerTasks.MEDIUM, 50,
	 * Skills.FARMING, 1), VEG1( "Harvest Vegetables Easy",
	 * "You must harvest 20 Vegetables.", SkillerTasks.EASY, 20, Skills.FARMING,
	 * 1), VEG2( "Harvest Vegetables Medium", "You must harvest 50 Vegetables.",
	 * SkillerTasks.MEDIUM, 50, Skills.FARMING, 1), VEG3(
	 * "Harvest Vegetables Hard", "You must harvest 85 Vegetables.",
	 * SkillerTasks.HARD, 85, Skills.FARMING, 1), VEG4(
	 * "Harvest Vegetables Elite", "You must harvest 125 Vegetables.",
	 * SkillerTasks.ELITE, 125, Skills.FARMING, 1),
	 */

	// Firemaking
	FBON1(Fire.BONFIRE, "Fuel Bonfire Medium",
			"You must fuel 200 logs into a Bonfire.", SkillerTasks.MEDIUM, 200,
			Skills.FIREMAKING, 1), FBON2(Fire.BONFIRE, "Fuel Bonfire Hard",
			"You must fuel 300 logs into a Bonfire.", SkillerTasks.HARD, 300,
			Skills.FIREMAKING, 1), FBON3(Fire.BONFIRE, "Fuel Bonfire Elite",
			"You must fuel 400 logs into a Bonfire.", SkillerTasks.ELITE, 400,
			Skills.FIREMAKING, 1), FOAK1(Fire.OAK, "Burn Oak Easy",
			"You must successfully burn 65 Oak Logs.", SkillerTasks.EASY, 65,
			Skills.FIREMAKING, 15), FOAK2(Fire.OAK, "Burn Oak Medium",
			"You must successfully burn 150 Oak Logs.", SkillerTasks.MEDIUM,
			150, Skills.FIREMAKING, 15), FMAGIC1(Fire.MAGIC, "Magic Logs Hard",
			"You must successfully burn 275 Magic Logs.", SkillerTasks.HARD,
			275, Skills.FIREMAKING, 75), FMAGIC2(Fire.MAGIC,
			"Magic Logs Elite", "You must successfully burn 350 Magic Logs.",
			SkillerTasks.ELITE, 350, Skills.FIREMAKING, 75), FMAPLE1(
			Fire.MAPLE, "Burn Maple Medium",
			"You must successfully burn 150 Maple Logs.", SkillerTasks.MEDIUM,
			150, Skills.FIREMAKING, 45), FMAPLE2(Fire.MAPLE, "Burn Maple Hard",
			"You must successfully burn 275 Maple Logs.", SkillerTasks.HARD,
			275, Skills.FIREMAKING, 45), FNORMAL1(Fire.NORMAL,
			"Burn Normal Easy", "You must successfully burn 65 Regular Logs.",
			SkillerTasks.EASY, 65, Skills.FIREMAKING, 1), FNORMAL2(Fire.NORMAL,
			"Burn Normal Medium",
			"You must successfully burn 150 Regular Logs.",
			SkillerTasks.MEDIUM, 150, Skills.FIREMAKING, 1), FWILLOW1(
			Fire.WILLOW, "Burn Willow Easy",
			"You must successfully burn 65 Willow Logs.", SkillerTasks.EASY,
			65, Skills.FIREMAKING, 30), FWILLOW2(Fire.WILLOW,
			"Burn Willow Medium",
			"You must successfully burn 150 Willow Logs.", SkillerTasks.MEDIUM,
			150, Skills.FIREMAKING, 30), FWILLOW3(Fire.WILLOW,
			"Burn Willow Hard", "You must successfully burn 275 Willow Logs.",
			SkillerTasks.HARD, 275, Skills.FIREMAKING, 30), FYEW1(Fire.YEW,
			"Burn Yew Medium", "You must successfully burn 150 Yew Logs.",
			SkillerTasks.MEDIUM, 150, Skills.FIREMAKING, 60), FYEW2(Fire.YEW,
			"Burn Yew Hard", "You must successfully burn 275 Yew Logs.",
			SkillerTasks.HARD, 275, Skills.FIREMAKING, 60), FYEW3(Fire.YEW,
			"Burn Yew Elite", "You must successfully burn 350 Yew Logs.",
			SkillerTasks.ELITE, 350, Skills.FIREMAKING, 60),

	// Fishing
	FANCHOVIES1(Fish.ANCHOVIES, "Fish Anchovies Easy",
			"You must successfully catch 50 Raw Anchovies.", SkillerTasks.EASY,
			50, Skills.FISHING, 15), FANCHOVIES2(Fish.ANCHOVIES,
			"Fish Anchovies Medium",
			"You must successfully catch 130 Raw Anchovies.",
			SkillerTasks.MEDIUM, 130, Skills.FISHING, 15), FHERRING1(
			Fish.HERRING, "Fish Herring Easy",
			"You must successfully catch 50 Raw Herring.", SkillerTasks.EASY,
			50, Skills.FISHING, 10), FHERRING2(Fish.HERRING,
			"Fish Herring Medium",
			"You must successfully catch 125 Raw Herring.",
			SkillerTasks.MEDIUM, 125, Skills.FISHING, 10), FLOBSTER1(
			Fish.LOBSTER, "Fish Lobster Medium",
			"You must successfully catch 75 Raw Lobster.", SkillerTasks.MEDIUM,
			75, Skills.FISHING, 40), FLOBSTER2(Fish.LOBSTER,
			"Fish Lobster Hard",
			"You must successfully catch 160 Raw Lobster.", SkillerTasks.HARD,
			160, Skills.FISHING, 40), FROCKTAIL1(Fish.ROCKTAIL,
			"Fish Rocktail Hard",
			"You must successfully catch 100 Raw Rocktail.", SkillerTasks.HARD,
			100, Skills.FISHING, 90), FROCKTAIL2(Fish.ROCKTAIL,
			"Fish Rocktail Elite",
			"You must successfully catch 180 Raw Rocktail.",
			SkillerTasks.ELITE, 180, Skills.FISHING, 90), FSALMON1(Fish.SALMON,
			"Fish Salmon Easy", "You must successfully catch 90 Raw Salmon.",
			SkillerTasks.EASY, 90, Skills.FISHING, 30), FSALMON2(Fish.SALMON,
			"Fish Salmon Medium",
			"You must successfully catch 200 Raw Salmon.", SkillerTasks.MEDIUM,
			200, Skills.FISHING, 30), FSHARK1(Fish.SHARK, "Fish Shark Medium",
			"You must successfully catch 65 Raw Shark.", SkillerTasks.MEDIUM,
			65, Skills.FISHING, 76), FSHARK2(Fish.SHARK, "Fish Shark Hard",
			"You must successfully catch 150 Raw Shark.", SkillerTasks.HARD,
			150, Skills.FISHING, 76), FSHARK3(Fish.SHARK, "Fish Shark Elite",
			"You must successfully catch 275 Raw Shark.", SkillerTasks.ELITE,
			275, Skills.FISHING, 76), FSHRIMP1(Fish.SHRIMP, "Fish Shrimp Easy",
			"You must successfully catch 75 Raw Shrimp.", SkillerTasks.EASY,
			75, Skills.FISHING, 1), FSHRIMP2(Fish.SHRIMP, "Fish Shrimp Medium",
			"You must successfully catch 150 Raw Shrimp.", SkillerTasks.MEDIUM,
			75, Skills.FISHING, 1), FSWORD1(Fish.SWORDFISH,
			"Fish Swordfish Medium",
			"You must successfully catch 100 Raw Swordfish.",
			SkillerTasks.MEDIUM, 100, Skills.FISHING, 50), FSWORD2(
			Fish.SWORDFISH, "Fish Swordfish Hard",
			"You must successfully catch 225 Raw Swordfish.",
			SkillerTasks.HARD, 225, Skills.FISHING, 50), FTROUT1(Fish.TROUT,
			"Fish Trout Easy", "You must successfully catch 90 Raw Trout.",
			SkillerTasks.EASY, 90, Skills.FISHING, 20), FTROUT2(Fish.TROUT,
			"Fish Trout Medium", "You must successfully catch 200 Raw Trout.",
			SkillerTasks.MEDIUM, 200, Skills.FISHING, 20), FTUNA1(Fish.TUNA,
			"Fish Tuna Easy", "You must successfully catch 50 Raw Tuna.",
			SkillerTasks.EASY, 50, Skills.FISHING, 35), FTUNA2(Fish.TUNA,
			"Fish Tuna Medium", "You must successfully catch 125 Raw Tuna.",
			SkillerTasks.MEDIUM, 125, Skills.FISHING, 35),

	// Fletching
	/*
	 * BOAK1("Fletch Oak Easy", "You must fletch 65 bows from an Oak Log.",
	 * SkillerTasks.EASY, 65, Skills.FLETCHING, 20), BOAK2( "Fletch Oak Medium",
	 * "You must fletch 140 bows from an Oak Log.", SkillerTasks.MEDIUM, 140,
	 * Skills.FLETCHING, 20), BMAGIC1( "Magic Logs Hard",
	 * "You must fletch 120 bows from a Magic Log.", SkillerTasks.HARD, 120,
	 * Skills.FLETCHING, 80), BMAGIC2( "Magic Logs Elite",
	 * "You must fletch 220 bows from a Magic Log.", SkillerTasks.ELITE, 220,
	 * Skills.FLETCHING, 80), BMAPLE1( "Fletch Maple Medium",
	 * "You must fletch 175 bows from a Maple Log.", SkillerTasks.MEDIUM, 175,
	 * Skills.FLETCHING, 50), BMAPLE2("Fletch Maple Hard",
	 * "You must fletch 275 bows from a Maple Log.", SkillerTasks.HARD, 275,
	 * Skills.FLETCHING, 50), BNORMAL1("Fletch Normal Easy",
	 * "You must fletch 50 bows from a Regular Log.", SkillerTasks.EASY, 50,
	 * Skills.FLETCHING, 1), BNORMAL2("Fletch Normal Medium",
	 * "You must fletch 120 bows from a Regular Log.", SkillerTasks.MEDIUM, 120,
	 * Skills.FLETCHING, 1), BRUNE1( "Fletch Rune Medium",
	 * "You must fletch 250 Rune Arrows.", SkillerTasks.MEDIUM, 250,
	 * Skills.FLETCHING, 75), BRUNE2( "Fletch Rune Hard",
	 * "You must fletch 400 Rune Arrows.", SkillerTasks.HARD, 400,
	 * Skills.FLETCHING, 75), BWILLOW1( "Fletch Willow Easy",
	 * "You must fletch 60 bows from a Willow Log.", SkillerTasks.EASY, 60,
	 * Skills.FLETCHING, 35), BWILLOW2("Fletch Willow Medium",
	 * "You must fletch 150 bows from a Willow Log.", SkillerTasks.MEDIUM, 150,
	 * Skills.FLETCHING, 35), BWILLOW3("Fletch Willow Hard",
	 * "You must fletch 275 bows from a Willow Log.", SkillerTasks.HARD, 275,
	 * Skills.FLETCHING, 35), BYEW1("Fletch Yew Medium",
	 * "You must fletch 135 bows from a Yew Log.", SkillerTasks.MEDIUM, 135,
	 * Skills.FLETCHING, 65), BYEW2("Fletch Yew Hard",
	 * "You must fletch 200 bows from a Yew Log.", SkillerTasks.HARD, 200,
	 * Skills.FLETCHING, 65), BYEW3("Fletch Yew Elite",
	 * "You must fletch 200 bows from a Yew Log.", SkillerTasks.ELITE, 200,
	 * Skills.FLETCHING, 65),
	 */

	// Herblore
	CLEAN1(HerbloreType.CLEAN, "Clean Herbs Easy",
			"You must clean 126 Grimy Herbs.", SkillerTasks.EASY, 126,
			Skills.HERBLORE, 1), CLEAN2(HerbloreType.CLEAN,
			"Clean Herbs Medium", "You must clean 180 Grimy Herbs.",
			SkillerTasks.MEDIUM, 180, Skills.HERBLORE, 1), CLEAN3(
			HerbloreType.CLEAN, "Clean Herbs Hard",
			"You must clean 375 Grimy Herbs.", SkillerTasks.HARD, 375,
			Skills.HERBLORE, 1), CLEAN4(HerbloreType.CLEAN,
			"Clean Herbs Elite", "You must clean 480 Grimy Herbs.",
			SkillerTasks.ELITE, 480, Skills.HERBLORE, 1), POTION1(
			HerbloreType.POTION, "Create Potions Easy",
			"You must create 84 Potions.", SkillerTasks.EASY, 84,
			Skills.HERBLORE, 1), POTION2(HerbloreType.POTION,
			"Create Potions Medium", "You must create 120 Potions.",
			SkillerTasks.MEDIUM, 120, Skills.HERBLORE, 1), POTION3(
			HerbloreType.POTION, "Create Potions Hard",
			"You must create 250 Potions.", SkillerTasks.HARD, 250,
			Skills.HERBLORE, 1), POTION4(HerbloreType.POTION,
			"Create Potions Elite", "You must create 322 Potions.",
			SkillerTasks.ELITE, 322, Skills.HERBLORE, 1),

	// Hunter
	BIRD1(Traps.SNARE, "Bird Snare Easy",
			"You must catch 40 birds using a Bird Snare.", SkillerTasks.EASY,
			40, Skills.HUNTER, 1), BIRD2(Traps.SNARE, "Bird Snare Medium",
			"You must catch 95 birds using a Bird Snare.", SkillerTasks.MEDIUM,
			95, Skills.HUNTER, 1), BIRD3(Traps.SNARE, "Bird Snare Hard",
			"You must catch 140 birds using a Bird Snare.", SkillerTasks.HARD,
			140, Skills.HUNTER, 1), BIRD4(Traps.SNARE, "Bird Snare Elite",
			"You must catch 175 birds using a Bird Snare.", SkillerTasks.ELITE,
			175, Skills.HUNTER, 1), BOX1(Traps.BOX, "Box Trap Easy",
			"You must catch 40 animals using a Box Trap.", SkillerTasks.EASY,
			40, Skills.HUNTER, 27), BOX2(Traps.BOX, "Box Trap Medium",
			"You must catch 95 animals using a Box Trap.", SkillerTasks.MEDIUM,
			95, Skills.HUNTER, 27), BOX3(Traps.BOX, "Box Trap Hard",
			"You must catch 140 animals using a Box Trap.", SkillerTasks.HARD,
			140, Skills.HUNTER, 27), BOX4(Traps.BOX, "Box Trap Elite",
			"You must catch 175 animals using a Box Trap.", SkillerTasks.ELITE,
			175, Skills.HUNTER, 27), NET1(Traps.NET, "Butterfly Net Easy",
			"You must catch 40 critters using a Butterfly Net.",
			SkillerTasks.EASY, 40, Skills.HUNTER, 15), NET2(Traps.NET,
			"Butterfly Net Medium",
			"You must catch 95 critters using a Butterfly Net.",
			SkillerTasks.MEDIUM, 95, Skills.HUNTER, 15), NET3(Traps.NET,
			"Butterfly Net Hard",
			"You must catch 140 critters using a Butterfly Net.",
			SkillerTasks.HARD, 140, Skills.HUNTER, 15), NET4(Traps.NET,
			"Butterfly Net Elite",
			"You must catch 175 critters using a Butterfly Net.",
			SkillerTasks.ELITE, 175, Skills.HUNTER, 15),

	// Mining
	MADAMANT1(RockDefinitions.ADAMANT_ORE, "Mine Adamant Hard",
			"You must mine 65 ores from a Adamant Rock.", SkillerTasks.HARD,
			65, Skills.MINING, 70), MADAMANT2(RockDefinitions.ADAMANT_ORE,
			"Mine Adamant Elite",
			"You must mine 115 ores from a Adamant Rock.", SkillerTasks.ELITE,
			115, Skills.MINING, 70), MCOAL1(RockDefinitions.COAL_ORE,
			"Mine Coal Easy", "You must mine 60 ores from a Coal Rock.",
			SkillerTasks.EASY, 60, Skills.MINING, 30), MCOAL2(
			RockDefinitions.COAL_ORE, "Mine Coal Medium",
			"You must mine 165 ores from a Coal Rock.", SkillerTasks.MEDIUM,
			165, Skills.MINING, 30), MCOAL3(RockDefinitions.COAL_ORE,
			"Mine Coal Hard", "You must mine 275 ores from a Coal Rock.",
			SkillerTasks.HARD, 275, Skills.MINING, 30), MCOPPER1(
			RockDefinitions.COPPER_ORE, "Mine Copper Easy",
			"You must mine 50 ores from a Copper Rock.", SkillerTasks.EASY, 50,
			Skills.MINING, 1), MCOPPER2(RockDefinitions.COPPER_ORE,
			"Mine Copper Medium", "You must mine 150 ores from a Copper Rock.",
			SkillerTasks.MEDIUM, 150, Skills.MINING, 1), MGEM1(
			RockDefinitions.GEM_ROCK, "Mine Gems Medium",
			"You must mine 50 gems from a Gem Rock.", SkillerTasks.MEDIUM, 50,
			Skills.MINING, 40), MGEM2(RockDefinitions.GEM_ROCK,
			"Mine Gems Medium", "You must mine 125 gems from a Gem Rock.",
			SkillerTasks.MEDIUM, 125, Skills.MINING, 40), MGEM3(
			RockDefinitions.GEM_ROCK, "Mine Gems Medium",
			"You must mine 200 gems from a Gem Rock.", SkillerTasks.MEDIUM,
			200, Skills.MINING, 40), MGEM4(RockDefinitions.GEM_ROCK,
			"Mine Gems Medium", "You must mine 300 gems from a Gem Rock.",
			SkillerTasks.MEDIUM, 300, Skills.MINING, 40), MGOLD1(
			RockDefinitions.GOLD_ORE, "Mine Gold Easy",
			"You must mine 50 ores from a Gold Rock.", SkillerTasks.EASY, 50,
			Skills.MINING, 40), MGOLD2(RockDefinitions.GOLD_ORE,
			"Mine Gold Medium", "You must mine 150 ores from a Gold Rock.",
			SkillerTasks.MEDIUM, 150, Skills.MINING, 40), MGOLD3(
			RockDefinitions.GOLD_ORE, "Mine Gold Hard",
			"You must mine 275 ores from a Gold Rock.", SkillerTasks.HARD, 275,
			Skills.MINING, 40), MIRON1(RockDefinitions.IRON_ORE,
			"Mine Iron Easy", "You must mine 65 ores from a Iron Rock.",
			SkillerTasks.EASY, 65, Skills.MINING, 15), MIRON2(
			RockDefinitions.IRON_ORE, "Mine Iron Medium",
			"You must mine 150 ores from a Iron Rock.", SkillerTasks.MEDIUM,
			150, Skills.MINING, 15), MIRON3(RockDefinitions.IRON_ORE,
			"Mine Iron Hard", "You must mine 225 ores from a Iron Rock.",
			SkillerTasks.HARD, 225, Skills.MINING, 15), MMITHRIL1(
			RockDefinitions.MITHRIL_ORE, "Mine Mithril Medium",
			"You must mine 125 ores from a Mithril Rock.", SkillerTasks.MEDIUM,
			125, Skills.MINING, 55), MMITHRIL2(RockDefinitions.MITHRIL_ORE,
			"Mine Mithril Hard", "You must mine 200 ores from a Mithril Rock.",
			SkillerTasks.HARD, 200, Skills.MINING, 55), MRUNE1(
			RockDefinitions.RUNITE_ORE, "Mine Rune Hard",
			"You must mine 50 ores from a Rune Rock.", SkillerTasks.HARD, 50,
			Skills.MINING, 85), MRUNE2(RockDefinitions.RUNITE_ORE,
			"Mine Rune Elite", "You must mine 100 ores from a Rune Rock.",
			SkillerTasks.ELITE, 100, Skills.MINING, 85), MSILVER1(
			RockDefinitions.SILVER_ORE, "Mine Silver Easy",
			"You must mine 75 ores from a Silver Rock.", SkillerTasks.EASY, 75,
			Skills.MINING, 20), MSILVER2(RockDefinitions.SILVER_ORE,
			"Mine Silver Medium", "You must mine 180 ores from a Silver Rock.",
			SkillerTasks.MEDIUM, 180, Skills.MINING, 20), MSILVER3(
			RockDefinitions.SILVER_ORE, "Mine Silver Hard",
			"You must mine 250 ores from a Silver Rock.", SkillerTasks.HARD,
			250, Skills.MINING, 20), MTIN1(RockDefinitions.TIN_ORE,
			"Mine Tin Easy", "You must mine 50 ores from a Tin Rock.",
			SkillerTasks.EASY, 50, Skills.MINING, 1), MTIN2(
			RockDefinitions.TIN_ORE, "Mine Tin Medium",
			"You must mine 150 ores from a Tin Rock.", SkillerTasks.MEDIUM,
			900, Skills.MINING, 1),

	// Runecrafting
	CRAFT1(RunecraftingTypes.REGULAR, "Craft Rune Easy",
			"You must craft 500 Runes.", SkillerTasks.EASY, 500,
			Skills.RUNECRAFTING, 1), CRAFT2(RunecraftingTypes.REGULAR,
			"Craft Rune Medium", "You must craft 1,000 Runes.",
			SkillerTasks.MEDIUM, 1000, Skills.RUNECRAFTING, 1), CRAFT3(
			RunecraftingTypes.REGULAR, "Craft Rune Hard",
			"You must craft 1,800 Runes.", SkillerTasks.HARD, 1800,
			Skills.RUNECRAFTING, 1), CRAFT4(RunecraftingTypes.REGULAR,
			"Craft Rune Elite", "You must craft 2,500 Runes.",
			SkillerTasks.ELITE, 2500, Skills.RUNECRAFTING, 1),

	// Smithing
	SADAMANT1(SmeltingBar.ADAMANT, "Smelt Adamant Hard",
			"You must successfully smelt 85 Adamant Bars.", SkillerTasks.HARD,
			85, Skills.SMITHING, 70), SADAMANT2(SmeltingBar.ADAMANT,
			"Smelt Adamant Elite",
			"You must successfully smelt 150 Adamant Bars.",
			SkillerTasks.ELITE, 150, Skills.SMITHING, 70), SBRONZE1(
			SmeltingBar.BRONZE, "Smelt Bronze Easy",
			"You must successfully smelt 50 Bronze Bars.", SkillerTasks.EASY,
			50, Skills.SMITHING, 1), SBRONZE2(SmeltingBar.BRONZE,
			"Smelt Bronze Medium",
			"You must successfully smelt 150 Bronze Bars.",
			SkillerTasks.MEDIUM, 150, Skills.SMITHING, 1), SCANNON1(
			SmeltingBar.CANNON_BALLS, "Smelt Cannon Balls Easy",
			"You must successfully smelt 200 Cannon Balls.", SkillerTasks.EASY,
			200, Skills.SMITHING, 35), SCANNON2(SmeltingBar.CANNON_BALLS,
			"Smelt Cannon Balls Medium",
			"You must successfully smelt 600 Cannon Balls.",
			SkillerTasks.MEDIUM, 600, Skills.SMITHING, 35), SCANNON3(
			SmeltingBar.CANNON_BALLS, "Smelt Cannon Balls Hard",
			"You must successfully smelt 800 Cannon Balls.", SkillerTasks.HARD,
			800, Skills.SMITHING, 35), SCANNON4(SmeltingBar.CANNON_BALLS,
			"Smelt Cannon Balls Elite",
			"You must successfully smelt 1,200 Cannon Balls.",
			SkillerTasks.ELITE, 1200, Skills.SMITHING, 35), SGOLD1(
			SmeltingBar.GOLD, "Smelt Gold Easy",
			"You must successfully smelt 50 Gold Bars.", SkillerTasks.EASY, 50,
			Skills.SMITHING, 40), SGOLD2(SmeltingBar.GOLD, "Smelt Gold Medium",
			"You must successfully smelt 150 Gold Bars.", SkillerTasks.MEDIUM,
			150, Skills.SMITHING, 40), SGOLD3(SmeltingBar.GOLD,
			"Smelt Gold Hard", "You must successfully smelt 300 Gold Bars.",
			SkillerTasks.HARD, 300, Skills.SMITHING, 40), SIRON1(
			SmeltingBar.IRON, "Smelt Iron Easy",
			"You must successfully smelt 50 Iron Bars.", SkillerTasks.EASY, 50,
			Skills.SMITHING, 15), SIRON2(SmeltingBar.IRON, "Smelt Iron Medium",
			"You must successfully smelt 150 Iron Bars.", SkillerTasks.MEDIUM,
			150, Skills.SMITHING, 15), SMITHRIL1(SmeltingBar.MITHRIL,
			"Smelt Mithril Medium",
			"You must successfully smelt 80 Mithril Bars.",
			SkillerTasks.MEDIUM, 80, Skills.SMITHING, 50), SMITHRIL2(
			SmeltingBar.MITHRIL, "Smelt Mithril Hard",
			"You must successfully smelt 120 Mithril Bars.", SkillerTasks.HARD,
			120, Skills.SMITHING, 50), SMITHRIL3(SmeltingBar.MITHRIL,
			"Smelt Mithril Elite",
			"You must successfully smelt 200 Mithril Bars.",
			SkillerTasks.ELITE, 200, Skills.SMITHING, 50), SRUNE1(
			SmeltingBar.RUNE, "Smelt Rune Hard",
			"You must successfully smelt 75 Rune Bars.", SkillerTasks.HARD, 75,
			Skills.SMITHING, 85), SRUNE2(SmeltingBar.RUNE, "Smelt Rune Elite",
			"You must successfully smelt 125 Rune Bars.", SkillerTasks.ELITE,
			125, Skills.SMITHING, 85), SSILVER1(SmeltingBar.SILVER,
			"Smelt Silver Easy", "You must successfully smelt 50 Silver Bars.",
			SkillerTasks.EASY, 50, Skills.SMITHING, 20), SSILVER2(
			SmeltingBar.SILVER, "Smelt Silver Medium",
			"You must successfully smelt 150 Silver Bars.",
			SkillerTasks.MEDIUM, 150, Skills.SMITHING, 20), SSTEEL1(
			SmeltingBar.STEEL, "Smelt Steel Easy",
			"You must successfully smelt 40 Steel Bars.", SkillerTasks.EASY,
			40, Skills.SMITHING, 30), SSTEEL2(SmeltingBar.STEEL,
			"Smelt Steel Medium",
			"You must successfully smelt 120 Steel Bars.", SkillerTasks.MEDIUM,
			120, Skills.SMITHING, 30),

	// Thieving
	CRACK1(ThievingTypes.WALLSAFE, "Crack Safe Easy",
			"You must crack open 30 Wall Safes.", SkillerTasks.EASY, 30,
			Skills.THIEVING, 50), CRACK2(ThievingTypes.WALLSAFE,
			"Crack Safe Medium", "You must crack open 65 Wall Safes.",
			SkillerTasks.MEDIUM, 65, Skills.THIEVING, 50), CRACK3(
			ThievingTypes.WALLSAFE, "Crack Safe Hard",
			"You must crack open 100 Wall Safes.", SkillerTasks.HARD, 100,
			Skills.THIEVING, 50), CRACK4(ThievingTypes.WALLSAFE,
			"Crack Safe Elite", "You must crack open 250 Wall Safes.",
			SkillerTasks.ELITE, 250, Skills.THIEVING, 50), POCKET1(
			ThievingTypes.PICKPOCKET, "Pickpocket Easy",
			"You must steal from 50 People.", SkillerTasks.EASY, 50,
			Skills.THIEVING, 1), POCKET2(ThievingTypes.PICKPOCKET,
			"Pickpocket Medium", "You must steal from 125 People.",
			SkillerTasks.MEDIUM, 125, Skills.THIEVING, 1), POCKET3(
			ThievingTypes.PICKPOCKET, "Pickpocket Hard",
			"You must steal from 200 People.", SkillerTasks.HARD, 200,
			Skills.THIEVING, 1), POCKET4(ThievingTypes.PICKPOCKET,
			"Pickpocket Elite", "You must steal from 400 People.",
			SkillerTasks.ELITE, 400, Skills.THIEVING, 1), STALL1(
			ThievingTypes.STALLS, "Thieve Stall Easy",
			"You must steal from 85 Stalls.", SkillerTasks.EASY, 85,
			Skills.THIEVING, 1), STALL2(ThievingTypes.STALLS,
			"Thieve Stall Medium", "You must steal from 125 Stalls.",
			SkillerTasks.MEDIUM, 125, Skills.THIEVING, 1), STALL3(
			ThievingTypes.STALLS, "Thieve Stall Hard",
			"You must steal from 200 Stalls.", SkillerTasks.HARD, 200,
			Skills.THIEVING, 1), STALL4(ThievingTypes.STALLS,
			"Thieve Stall Elite", "You must steal from 400 Stalls.",
			SkillerTasks.ELITE, 400, Skills.THIEVING, 1),

	// Woodcutting
	WIVY1(TreeDefinitions.IVY, "Chop Ivy Medium",
			"You must chop 65 pieces of Choking Ivy.", SkillerTasks.MEDIUM, 65,
			Skills.WOODCUTTING, 68), WIVY2(TreeDefinitions.IVY,
			"Chop Ivy Hard", "You must chop 120 pieces of Choking Ivy.",
			SkillerTasks.HARD, 120, Skills.WOODCUTTING, 68), WIVY3(
			TreeDefinitions.IVY, "Chop Ivy Elite",
			"You must chop 200 pieces of Choking Ivy.", SkillerTasks.ELITE,
			200, Skills.WOODCUTTING, 68), WOAK1(TreeDefinitions.OAK,
			"Chop Oak Easy", "You must chop 100 logs from an Oak Tree.",
			SkillerTasks.EASY, 100, Skills.WOODCUTTING, 15), WOAK2(
			TreeDefinitions.OAK, "Chop Oak Medium",
			"You must chop 250 logs from an Oak Tree.", SkillerTasks.MEDIUM,
			250, Skills.WOODCUTTING, 15), WMAGIC1(TreeDefinitions.MAGIC,
			"Magic Logs Hard", "You must chop 50 logs from a Magic Tree.",
			SkillerTasks.HARD, 50, Skills.WOODCUTTING, 75), WMAGIC2(
			TreeDefinitions.MAGIC, "Magic Logs Elite",
			"You must chop 150 logs from a Magic Tree.", SkillerTasks.ELITE,
			150, Skills.WOODCUTTING, 75), WMAPLE1(TreeDefinitions.MAPLE,
			"Chop Maple Medium", "You must chop 180 logs from a Maple Tree.",
			SkillerTasks.MEDIUM, 180, Skills.WOODCUTTING, 45), WMAPLE2(
			TreeDefinitions.MAPLE, "Chop Maple Hard",
			"You must chop 300 logs from a Maple Tree.", SkillerTasks.HARD,
			300, Skills.WOODCUTTING, 45), WNORMAL1(TreeDefinitions.NORMAL,
			"Chop Normal Easy", "You must chop 75 logs from a Regular Tree.",
			SkillerTasks.EASY, 75, Skills.WOODCUTTING, 1), WNORMAL2(
			TreeDefinitions.NORMAL, "Chop Normal Medium",
			"You must chop 200 logs from a Regular Tree.", SkillerTasks.MEDIUM,
			200, Skills.WOODCUTTING, 1), WWILLOW1(TreeDefinitions.WILLOW,
			"Chop Willow Easy", "You must chop 100 logs from a Willow Tree.",
			SkillerTasks.EASY, 100, Skills.WOODCUTTING, 30), WWILLOW2(
			TreeDefinitions.WILLOW, "Chop Willow Medium",
			"You must chop 250 logs from a Willow Tree.", SkillerTasks.MEDIUM,
			250, Skills.WOODCUTTING, 30), WWILLOW3(TreeDefinitions.WILLOW,
			"Chop Willow Hard", "You must chop 350 logs from a Willow Tree.",
			SkillerTasks.HARD, 350, Skills.WOODCUTTING, 30), WYEW1(
			TreeDefinitions.YEW, "Chop Yew Medium",
			"You must chop 65 logs from a Yew Tree.", SkillerTasks.MEDIUM, 65,
			Skills.WOODCUTTING, 60), WYEW2(TreeDefinitions.YEW,
			"Chop Yew Hard", "You must chop 125 logs from a Yew Tree.",
			SkillerTasks.HARD, 125, Skills.WOODCUTTING, 60), WYEW3(
			TreeDefinitions.YEW, "Chop Yew Elite",
			"You must chop 200 logs from a Yew Tree.", SkillerTasks.ELITE, 200,
			Skills.WOODCUTTING, 60);

	private Object element;
	private String assignment;
	private String description;
	private int difficulty;
	private int amount;
	private int skill;
	private int level;

	SkillTasks(Object element, String assignment, String description,
			int difficulty, int amount, int skill, int level) {
		this.element = element;
		this.assignment = assignment;
		this.description = description;
		this.difficulty = difficulty;
		this.amount = amount;
		this.skill = skill;
		this.level = level;
	}

	public Object getElement() {
		return element;
	}

	public String getAssignment() {
		return assignment;
	}

	public String getDescription() {
		return description;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getAmount() {
		return amount;
	}

	public int getSkill() {
		return skill;
	}

	public int getLevel() {
		return level;
	}

}