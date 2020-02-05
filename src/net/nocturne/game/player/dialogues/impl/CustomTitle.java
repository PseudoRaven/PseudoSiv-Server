package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.Censor;
import net.nocturne.utils.Utils;

public class CustomTitle extends Dialogue {

	public int option;

	public static final int TITLE_IN_FRONT = 0, TITLE_IN_BACK = 1,
			TITLE_ID = 2, TITLE_COLOR = 3, TITLE_SHADE = 4;

	public static final char[] VALID_CHARS = { '#', '_', 'a', 'A', 'b', 'B',
			'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', 'g', 'G', 'h', 'H', 'i',
			'I', 'j', 'J', 'k', 'K', 'l', 'L', 'm', 'M', 'n', 'N', 'o', 'O',
			'p', 'P', 'q', 'Q', 'r', 'R', 's', 'S', 't', 'T', 'u', 'U', 'v',
			'V', 'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', ' ' };

	public static boolean containsInvalidCharacter(char c) {
		for (char vc : VALID_CHARS) {
			if (vc == c)
				return false;
		}
		return true;
	}

	public static boolean containsInvalidCharacter(String name) {
		for (char c : name.toCharArray()) {
			if (containsInvalidCharacter(c))
				return true;
		}
		return false;
	}

	public void resetTitles() {
		player.setCustomTitle(null);
		player.setTitleString(null);
	}

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option",
				"Select title in front of name", "Select title behind name",
				"Select title Id", "Select title color", "Continue");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			end();
			if (componentId == OPTION_1) {
				player.getPackets().sendInputLongTextScript(
						"Enter a new title:");
				option = TITLE_IN_FRONT;
			} else if (componentId == OPTION_2) {
				player.getPackets().sendInputLongTextScript(
						"Enter a new title:");
				option = TITLE_IN_BACK;
			} else if (componentId == OPTION_3) {
				player.getPackets().sendInputIntegerScript(
						"Enter a new title id:");
				option = TITLE_ID;
			} else if (componentId == OPTION_4) {
				player.getPackets().sendInputLongTextScript(
						"Enter a new title color(Hex code):");
				option = TITLE_COLOR;
			} else if (componentId == OPTION_5) {
				player.getPackets().sendInputLongTextScript(
						"Enter a new title shade(Hex code):");
				option = TITLE_SHADE;
			}
			player.getTemporaryAttributtes().put("Title", option);
		} else
			end();

	}

	@Override
	public void finish() {

	}

}