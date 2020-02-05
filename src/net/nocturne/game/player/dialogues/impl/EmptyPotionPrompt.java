package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.Item;
import net.nocturne.game.item.actions.Drinkables;
import net.nocturne.game.item.actions.Drinkables.Drink;
import net.nocturne.game.player.dialogues.Dialogue;

public class EmptyPotionPrompt extends Dialogue {

	/**
	 * @author: miles M
	 */

	private Item item;

	private String previousName;

	private int slotId;

	@Override
	public void start() {
		item = (Item) parameters[0];
		previousName = (String) parameters[1];
		slotId = (int) parameters[2];
		sendOptionsDialogue("Are you sure you want to empty this potion?",
				"Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (componentId) {
		case OPTION_1:
			Drink pot = Drinkables.getDrink(item.getId());
			if (pot == null || pot.isFlask())
				return;
			item.setId(Drinkables.VIAL);
			player.getInventory().refresh(slotId);
			player.getPackets().sendGameMessage(
					"You empty the " + previousName + ".", true);
			end();
			break;
		case OPTION_2:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}