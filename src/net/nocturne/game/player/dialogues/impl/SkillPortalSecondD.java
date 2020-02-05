package net.nocturne.game.player.dialogues.impl;

import java.util.Locale;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.dialogues.Dialogue;

public class SkillPortalSecondD extends Dialogue {

	private int i = 0;
	private int price;
	private int maxedLevels = 0;

	@Override
	public void start() {
		int bit = player.getVarsManager().getBitValue(25055);
		if (bit == 0) {
			sendDialogue("There is no cost to tune this portal for the first time");
			stage = 1;
		} else {
			maxedLevels = 0;
			for (int i = 0; i < 26; i++)
				if (player.getSkills().getLevel(i) >= 99)
					maxedLevels++;
			Locale.setDefault(Locale.US);
			price = maxedLevels >= 20 ? 50000 : maxedLevels >= 15 ? 60000
					: maxedLevels >= 10 ? 70000 : maxedLevels >= 5 ? 80000
							: 90000;
			String formattedprice = String.format("%,d", price);
			sendDialogue("To retune this portal will cost " + formattedprice
					+ " coins");
			stage = 1;
		}
	}

	private boolean checkForCoins() {
		int bit = player.getVarsManager().getBitValue(25055);
		if (bit == 0)
			sendDialogue("There is no cost to tune this portal for the first time");
		else if (player.getInventory().getCoinsAmount() < price) {
			sendDialogue("You don't have enough coins on you to retune this portal.");
			return false;
		}
		return true;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		switch (stage) {
		case 1:
			if (!checkForCoins())
				stage = 9;
			else {
				sendOptionsDialogue(
						"Where would you like to direct this portal?",
						"Active crystal tree", "Jadinko Lair",
						"Lava Flow Mine", "Living Rock Caverns",
						"[More options]");
				stage = 3;
			}
			break;

		case 3:
			switch (componentId) {
			case OPTION_1:
				/*
				 * sendDialogue(
				 * "You focus this portal towards the Active crystal tree.");
				 * for (i = 0; i < 26; i++) if (player.getSkills().getLevel(i)
				 * >= 99) maxedLevels++;
				 * player.getInventory().removeItemMoneyPouch(new Item(995,
				 * price); player.getVarsManager().sendVarBit(25055, 1); stage =
				 * 9;
				 */
				player.getDialogueManager().startDialogue("SimpleMessage",
						"This option hasn't been added yet.");
				break;
			case OPTION_2:
				sendDialogue("You focus this portal towards the Jadinko Lair.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 2);
				player.getVarbits().put(25055, 2);
				stage = 9;
				break;
			case OPTION_3:
				sendDialogue("You focus this portal towards the Lava Flow Mine.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 3);
				player.getVarbits().put(25055, 3);
				stage = 9;
				break;
			case OPTION_4:
				sendDialogue("You focus this portal towards the Living Rock Caverns.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 4);
				player.getVarbits().put(25055, 4);
				stage = 9;
				break;
			case OPTION_5:
				sendOptionsDialogue(
						"Where would you like to direct this portal?",
						"Brilliant wisp colony (near Polypore Dungeon",
						"Radiant wisp colony (Dragontooth Island)",
						"Luminous wisp colony (south of Sophanem)",
						"Incandescent wisp colony (Poison Wastes)",
						"[More options]");
				stage = 4;
				break;
			}
			break;
		case 4:
			switch (componentId) {
			case OPTION_1:
				sendDialogue("You focus this portal towards the Brilliant wisp colony.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 5);
				player.getVarbits().put(25055, 5);
				stage = 9;
				break;
			case OPTION_2:
				sendDialogue("You focus this portal towards the Radiant wisp colony.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 6);
				player.getVarbits().put(25055, 6);
				stage = 9;
				break;
			case OPTION_3:
				sendDialogue("You focus this portal towards the Luminous wisp colony.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 7);
				player.getVarbits().put(25055, 7);
				stage = 9;
				break;
			case OPTION_4:
				sendDialogue("You focus this portal towards the Incandescent wisp colony.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 8);
				player.getVarbits().put(25055, 8);
				stage = 9;
				break;

			case OPTION_5:
				sendOptionsDialogue(
						"Where would you like to direct this portal?",
						"Runespan (upper floor)", "Tree Gnome Stronghold",
						"Zanaris Fairy Ring", "Cancel", "[More options]");
				stage = 7;
				break;
			}
			break;
		case 7:
			switch (componentId) {
			case OPTION_1:
				sendDialogue("You focus this portal towards the Runespan (upper floor).");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 9);
				player.getVarbits().put(25055, 9);
				stage = 9;
				break;
			case OPTION_2:
				sendDialogue("You focus this portal towards the Tree Gnome Stronghold.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 10);
				player.getVarbits().put(25055, 10);
				stage = 9;
				break;
			case OPTION_3:
				sendDialogue("You focus this portal towards the Zanaris Fairy Ring.");
				for (i = 0; i < 26; i++)
					if (player.getSkills().getLevel(i) >= 99)
						maxedLevels++;
				player.getInventory()
						.removeItemMoneyPouch(new Item(995, price));
				player.getVarsManager().sendVarBit(25055, 11);
				player.getVarbits().put(25055, 11);
				stage = 9;
				break;
			case OPTION_4:
				end();
				break;
			case OPTION_5:
				sendOptionsDialogue(
						"Where would you like to direct this portal?",
						"Active crystal tree", "Jadinko Lair",
						"Lava Flow Mine", "Living Rock Caverns",
						"[More options]");
				stage = 3;
				break;
			}
			break;
		case 9:
			end();
			break;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}