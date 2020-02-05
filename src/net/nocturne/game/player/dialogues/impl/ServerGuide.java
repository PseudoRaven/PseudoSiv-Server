package net.nocturne.game.player.dialogues.impl;

import net.nocturne.Settings;
import net.nocturne.game.World;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.dialogues.Dialogue;

public class ServerGuide extends Dialogue {

	private boolean introlong = false;

	@Override
	public void start() {
		stage = 1;
		player.lock();
		sendNPCDialogue(945, HAPPY, "Welcome to <col=55728b>"
				+ Settings.SERVER_NAME + "</col>, " + player.getDisplayName()
				+ ".");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 0:
			end();
			player.unlock();
			player.getAppearence().generateAppearenceData();
			if (player.isAnIronMan()) {
				World.sendWorldMessage(
						"<col=43dde5>Welcome, " + player.getIronmanTitle(false)
								+ "<col=43dde5>" + player.getDisplayName()
								+ " to the world of Nocturne", false);
			} else {
				World.sendWorldMessage("Welcome, " + player.getDisplayName()
						+ " to the world of Nocturne.", false);
			}
			break;
		case 1:
			sendOptionsDialogue(
					DEFAULT_OPTIONS_TITLE,
					"Give me the basics of <col=55728b>" + Settings.SERVER_NAME,
					"I've never played <col=55728b>" + Settings.SERVER_NAME
							+ " before, please explain it to me!");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				sendNPCDialogue(945, HAPPY,
						"Eager to get on with it are we?  Very well.");
				stage = 19;
				break;
			case OPTION_2:
				sendNPCDialogue(945, HAPPY,
						"Oh a noob? Well you're just as welcome! Let's get started.");
				introlong = true;
				stage = 3;
				break;
			}
			break;
		case 3:
			sendNPCDialogue(
					945,
					NORMAL,
					"Welcome to the world of Nocturne! a wonderful place, There are some items that can be bought from the shops, located around here. But most useful items need to be obtained.");
			stage = 4;
			break;
		case 4:
			sendNPCDialogue(
					945,
					NORMAL,
					"Things are still in the early stage here, so if you come across a bug please let us know!, however, we still offer a load of unique content. Speak to <col=FFFF000>Ariane</col> to travel around the world of <col=55728b> Nocturne</col>.");
			if (introlong == true) {
				stage = 19; // 6
			} else {
				stage = 19;
			}
			break;
		case 5:
			sendNPCDialogue(945, NORMAL,
					"Our XP rates for Normal players are <col=FFFF000>x"
							+ Settings.XP_RATE + "</col>.");
			stage = 51;
			break;
		case 51:
			sendNPCDialogue(
					945,
					HAPPY,
					"At Nocturne, we're commited to giving you the best <col=FFFF000>Experience</col>., to achieve this, we are constantly improving Nocturne");
			stage = 6;
			break;
		case 6:
			sendNPCDialogue(
					945,
					HAPPY,
					"Any questions? Don't be shy, just ask in ::yell or '/' for friends chat, our more seasoned players & Staff will be more than happy to assist you.");
			stage = 7;
			break;
		case 7:
			sendNPCDialogue(945, QUESTIONS,
					"Now, a very serious question, would you like to look into Ironman mode?");
			stage = 8;
			break;
		case 8:
			sendOptionsDialogue(
					"Would you like to be an Ironman?",
					"No, I just want the regular Experience",
					"I fancy the challenge of being an <img=11><col=5F6169>Ironman</col>!",
					"I came here for the Grind! <img=13><col=A30920>Hardcore Ironman</col> me or DIE!");
			stage = 9;
			break;
		case 9:
			switch (componentId) {
			case OPTION_1:
				sendNPCDialogue(945, LAUGHING, "That's fine, have fun!");
				stage = 19;
				break;
			case OPTION_2:
				sendNPCDialogue(
						945,
						HAPPY,
						"Daring Choice! I admire that, I must warn you its not for the feint Hearted. don't expect to max anytime soon!");
				stage = 10;
				break;
			case OPTION_3:
				sendNPCDialogue(945, SCARED,
						"Clearly you're not one to mess with! Very Well.");
				stage = 16;
				break;
			}
			break;
		case 10:
			sendNPCDialogue(
					945,
					NORMAL,
					"You will not be able to use the grand exchange, trade, dungeoneer with a party, enter other players' house, and some more. You are on your own!");
			stage = 11;
			break;
		case 11:
			sendNPCDialogue(
					945,
					NORMAL,
					"Basically you will be playing as a single player. Do you still want to be an <img=11><col=5F6169>Ironman</col>?");
			stage = 12;
			break;
		case 12:
			sendOptionsDialogue("Would you like to be an Ironman?",
					"No, I want to play casually!",
					"I fancy the Challenge! <img=11><col=5F6169>Ironman</col> me!.");
			stage = 13;
			break;
		case 13:
			switch (componentId) {
			case OPTION_1:
				sendNPCDialogue(945, HAPPY, "That's fine, have fun!");
				stage = 19;
				break;
			case OPTION_2:
				sendNPCDialogue(
						945,
						HAPPY,
						"I like your attitude, you'll go far!, you are now an <img=11><col=5F6169>Ironman</col>. Have fun!");
				player.setIronman(true);
				player.setHardcoreIronMan(false);
				stage = 19;
				break;
			}
			break;
		case 15:
			sendNPCDialogue(
					945,
					NORMAL,
					"You will not be able to use the grand exchange, trade, dungeoneer with a party, enter other players' house, and some more.");
			stage = 16;
			break;
		case 16:
			sendNPCDialogue(
					945,
					NORMAL,
					"Basically you will be playing as a single player. Do you still want to be a <img=13><col=A30920>Hardcore Ironman</col>?");
			stage = 17;
			break;
		case 17:
			sendOptionsDialogue("Would you like to be an Ironman?",
					"....actually now that you mention it.",
					"*Draw Sword Demanding HC Ironman Status*");
			stage = 18;
			break;
		case 18:
			switch (componentId) {
			case OPTION_1:
				sendNPCDialogue(945, HAPPY, "That's fine, have fun!");
			case OPTION_2:
				sendNPCDialogue(
						945,
						SCARED,
						"The Gods help your Enemies!, you are now a <img=13><col=A30920>Hardcore Ironman</col>, Have fun!");
				player.setHardcoreIronMan(true);
				player.setIronman(false);
				break;
			}
			stage = 19;
			break;
		case 19:
			sendOptionsDialogue("Choose a starter class!", "Warrior (Melee)",
					"Archer (Ranged)", "Mage (Magic)", "Skiller (Non-Combat)");
			stage = 20;
			break;
		case 20:
			sendNPCDialogue(945, HAPPY,
					"You will find the rest in your toolbelt already!");
			switch (componentId) {
			case OPTION_1:
				player.getInventory().addItem(ItemIdentifiers.BRONZE_SCIMITAR,
						1);
				player.getInventory().addItem(
						ItemIdentifiers.OFFHAND_BRONZE_SCIMITAR, 1);
				player.getInventory().addItem(ItemIdentifiers.IRON_SCIMITAR, 1);
				player.getInventory().addItem(
						ItemIdentifiers.OFFHAND_IRON_SCIMITAR, 1);
				player.getInventory().addItem(ItemIdentifiers.RUNE_SCIMITAR, 1);
				player.getInventory().addItem(
						ItemIdentifiers.OFFHAND_RUNE_SCIMITAR, 1);
				player.getInventory().addItem(
						ItemIdentifiers.IRON_ARMOUR_SET_LG, 1);
				player.getInventory().addItem(
						ItemIdentifiers.AMULET_OF_STRENGTH, 1);
				player.getInventory().addItem(
						ItemIdentifiers.ROCK_CLIMBING_BOOTS, 1);
				player.getInventory().addItem(
						ItemIdentifiers.SUPER_STRENGTH_4_NOTED, 5);
				player.getInventory().addItem(
						ItemIdentifiers.SUPER_ATTACK_4_NOTED, 5);
				player.getInventory().addItem(
						ItemIdentifiers.SUPER_DEFENCE_4_NOTED, 5);
				player.getInventory().addItem(ItemIdentifiers.LOBSTER_NOTED,
						100);
				player.getMoneyPouch().sendDynamicInteraction(1000000, false);
				stage = 0;
				break;
			case OPTION_2:
				player.getInventory().addItem(ItemIdentifiers.SHORTBOW, 1);
				player.getInventory().addItem(ItemIdentifiers.SHIELDBOW, 1);
				player.getInventory()
						.addItem(ItemIdentifiers.MAGIC_SHORTBOW, 1);
				player.getInventory().addItem(ItemIdentifiers.MAGIC_SHIELDBOW,
						1);
				player.getInventory().addItem(ItemIdentifiers.COIF, 1);
				player.getInventory().addItem(ItemIdentifiers.LEATHER_BODY, 1);
				player.getInventory().addItem(ItemIdentifiers.LEATHER_CHAPS, 1);
				player.getInventory().addItem(ItemIdentifiers.AMULET_OF_POWER,
						1);
				player.getInventory().addItem(ItemIdentifiers.LEATHER_BOOTS, 1);
				player.getInventory()
						.addItem(ItemIdentifiers.BRONZE_ARROW, 250);
				player.getInventory().addItem(ItemIdentifiers.IRON_ARROW, 250);
				player.getInventory().addItem(
						ItemIdentifiers.SUPER_RANGING_POTION_4_NOTED, 5);
				player.getInventory().addItem(
						ItemIdentifiers.SUPER_DEFENCE_4_NOTED, 5);
				player.getInventory().addItem(ItemIdentifiers.LOBSTER_NOTED,
						100);
				player.getMoneyPouch().sendDynamicInteraction(1000000, false);
				stage = 0;
				break;
			case OPTION_3:
				player.getInventory().addItem(ItemIdentifiers.STAFF_OF_AIR, 1);
				player.getInventory().addItem(ItemIdentifiers.IMPHIDE_BOOK, 1);
				player.getInventory().addItem(ItemIdentifiers.IMP_HORN_WAND, 1);
				player.getInventory().addItem(ItemIdentifiers.ANCIENT_STAFF, 1);
				player.getInventory().addItem(ItemIdentifiers.WIZARD_HAT, 1);
				player.getInventory().addItem(ItemIdentifiers.WIZARD_ROBE_TOP,
						1);
				player.getInventory().addItem(
						ItemIdentifiers.WIZARD_ROBE_SKIRT, 1);
				player.getInventory().addItem(ItemIdentifiers.AMULET_OF_MAGIC,
						1);
				player.getInventory().addItem(ItemIdentifiers.IMPHIDE_BOOTS, 1);
				player.getInventory().addItem(ItemIdentifiers.EARTH_RUNE, 250);
				player.getInventory().addItem(ItemIdentifiers.AIR_RUNE, 250);
				player.getInventory().addItem(ItemIdentifiers.FIRE_RUNE, 250);
				player.getInventory().addItem(ItemIdentifiers.WATER_RUNE, 250);
				player.getInventory().addItem(ItemIdentifiers.CHAOS_RUNE, 100);
				player.getInventory().addItem(
						ItemIdentifiers.SUPER_DEFENCE_4_NOTED, 5);
				player.getInventory().addItem(ItemIdentifiers.LOBSTER_NOTED,
						100);
				player.getMoneyPouch().sendDynamicInteraction(1000000, false);
				stage = 0;
				break;
			case OPTION_4:
				player.getInventory().addItem(ItemIdentifiers.LOGS_NOTED, 25);
				player.getInventory().addItem(ItemIdentifiers.OAK_LOGS_NOTED,
						25);
				player.getInventory().addItem(ItemIdentifiers.UNCUT_OPAL_NOTED,
						25);
				player.getInventory().addItem(
						ItemIdentifiers.RAW_SHRIMPS_NOTED, 25);
				player.getInventory().addItem(ItemIdentifiers.FEATHER, 1000);
				player.getInventory().addItem(ItemIdentifiers.RUNE_HATCHET, 1);
				player.getInventory().addItem(ItemIdentifiers.RUNE_PICKAXE, 1);
				player.getInventory().addItem(
						ItemIdentifiers.PURE_ESSENCE_NOTED, 100);
				player.getInventory().addItem(ItemIdentifiers.GRIMY_GUAM_NOTED,
						15);
				player.getInventory().addItem(ItemIdentifiers.COPPER_ORE_NOTED,
						20);
				player.getInventory()
						.addItem(ItemIdentifiers.TIN_ORE_NOTED, 20);
				player.getInventory().addItem(ItemIdentifiers.COAL_NOTED, 100);
				player.getInventory().addItem(ItemIdentifiers.PLANK_NOTED, 25);
				player.getInventory().addItem(ItemIdentifiers.POTATO_SEED, 15);
				player.getMoneyPouch().sendDynamicInteraction(1000000, false);
				stage = 0;
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {

	}
}