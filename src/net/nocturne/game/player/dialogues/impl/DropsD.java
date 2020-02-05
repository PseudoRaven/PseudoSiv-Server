package net.nocturne.game.player.dialogues.impl;

import java.util.List;

import net.nocturne.game.player.dialogues.Dialogue;

public class DropsD extends Dialogue {

	private boolean removing = false, excluding = false;

	@Override
	public void start() {
		boolean start = (Boolean) parameters[0];
		if (start) {
			stage = 0;
			sendOptionsDialogue("What would you like to do?", "Add drops",
					"Remove drops", "Exclude drops", "All drops", "Clear drops");
		}
	}

	@Override
	public boolean run(String itemName) {
		if (itemName.length() <= 3) {
			stage = -2;
			sendDialogue("The item name has to be longer than that!");
			return true;
		}
		if (excluding)
			excludeFromDrops(itemName);
		else if (removing)
			deleteFromDrops(itemName);
		else
			addToDrops(itemName);
		return true;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue("What would you like to do?", "Add drops",
					"Remove drops", "Exclude drops", "All drops", "Clear drops");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				removing = false;
				sendItemName();
			} else if (componentId == OPTION_2) {
				removing = true;
				sendItemName();
			} else if (componentId == OPTION_3) {
				excluding = true;
				sendItemName();
			} else if (componentId == OPTION_4) {
				stage = 1;
				sendOptionsDialogue("What would you like to do?",
						"Turn on all drops(won't edit list)",
						"Turn off all drops(return to list)", "Back");
			} else if (componentId == OPTION_5) {
				stage = 2;
				sendOptionsDialogue("What would you like to do?",
						"Yes, clear drops.", "No, don't clear drops.", "Back");
			} else
				end();
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				stage = -2;
				player.getGamePointManager().setGamePointAllDrops(true);
				sendDialogue("All drops mode is now turned on. Exclusions can still be used.");
			} else if (componentId == OPTION_2) {
				player.getGamePointManager().setGamePointAllDrops(false);
				stage = -2;
				sendDialogue("All drops mode is now turned off.");
			} else if (componentId == OPTION_3) {
				stage = 0;
				sendOptionsDialogue("What would you like to do?", "Add drops",
						"Remove drops", "Exclude drops", "All drops",
						"Clear drops");
			} else
				end();
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				stage = -1;
				player.getGamePointManager().getGamePointDrops().clear();
				player.getGamePointManager().getGamePointDropExclusions()
						.clear();
				sendDialogue("Your drops have been cleared. This includes exclusions.");
			} else if (componentId == OPTION_2) {
				end();
			} else if (componentId == OPTION_3) {
				stage = 0;
				sendOptionsDialogue("What would you like to do?", "Add drops",
						"Remove drops", "Exclude drops", "All drops",
						"Clear drops");
			} else
				end();
		} else if (stage == 10) {
			stage = -2;
			sendDialogue("Woah bro, this is being worked on! CALM DOWN GUY.");
		} else
			end();
	}

	public void sendItemName() {
		end();
		if (player.getGamePointManager().getGamePointDrops().size() > 30
				&& !removing && !excluding) {
			stage = 10;
			sendDialogue("You have too many items in your list. You'll need to remove some.");
			return;
		}
		player.getPackets().sendInputLongTextScript("Enter an item name:");
		player.getDialogueManager().startDialogue("DropsD", false);
	}

	public void addToDrops(String drop) {
		stage = -1;
		sendDialogue("Successfully added " + drop + " to the list.");
		player.getGamePointManager().getGamePointDrops().add(drop);
		List<String> copy = player.getGamePointManager()
				.getGamePointDropExclusions();
		for (String name : copy) {
			if (name != null && name.contains(drop)) {
				player.getGamePointManager().getGamePointDropExclusions()
						.remove(name);
				sendDialogue("Successfully removed the exclusion " + name + ".");
			}
		}
	}

	public void deleteFromDrops(String drop) {
		stage = -1;
		String[] copy = player
				.getGamePointManager()
				.getGamePointDrops()
				.toArray(
						new String[player.getGamePointManager()
								.getGamePointDrops().size()]);
		for (String name : copy) {
			if (name != null && name.contains(drop)) {
				player.getGamePointManager().getGamePointDrops().remove(name);
				sendDialogue("Successfully removed " + name + " from the list.");
			}
		}
		sendDialogue("Finished removing items from the list.");
	}

	public void excludeFromDrops(String drop) {
		stage = -1;
		player.getGamePointManager().getGamePointDropExclusions().add(drop);
		sendDialogue("Successfully excluded " + drop + " from the list.");
	}

	@Override
	public void finish() {

	}
}
