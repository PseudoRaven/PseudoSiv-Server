package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.npc.others.Pet;
import net.nocturne.game.npc.pet.LegendaryPetAbilities;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.Utils;

/**
 * 
 * @author Homeboy927
 *
 */
public class LegendaryPetDialogue extends Dialogue {

	Item usedItem;
	Pet pet;
	boolean forage;

	@Override
	public void start() {
		usedItem = (Item) parameters[0];
		pet = (Pet) parameters[1];

		// determine what options to display to the user
		if (usedItem == null) {
			sendOptions(3);
		} else if (ItemConstants.getItemFixed(usedItem.getId()) != -1)
			sendOptions(2);
		else
			sendOptions(1);
		stage = -1;

	}

	private void sendOptions(int option) {
		switch (option) {
		case 1:
			sendOptionsDialogue("Legendary Pet Options", "Bank Item",
					"Alch Item");
			break;
		case 2:
			sendOptionsDialogue("Legendary Pet Options", "Bank Item",
					"Alch Item", "Fix Broken Item");
			break;
		case 3:
			forage = true;
			sendOptionsDialogue("Legendary Pet Options",
					"Item Forage: " + pet.isForging(), "Open Pet Inventory",
					"Pet Vampyrism", "Cancel");
			break;
		}
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (forage) {
			switch (componentId) {
			case OPTION_1:
				// stops you from getting an item when the ability is first
				// turned on
				// also stops you from being able to spam toggle to get items
				if (LegendaryPetAbilities.onCoolDown(7200,
						pet.getPetItemForageReset())) {
					player.getPackets().sendGameMessage(
							"Your pet is too tired and cant forage for items");
					player.getPackets().sendGameMessage(
							"This ability will be ready in "
									+ LegendaryPetAbilities.getRemainingTime(
											7200, pet.getPetItemForageReset())
									/ 60 + " minutes.");
				} else {
					pet.setPetItemForge(player, Utils.currentTimeMillis());
					pet.setForaging(!pet.isForging());
				}
				break;
			case OPTION_2:
				if (pet.bob != null) {
					pet.bob.open();
				} else {
					player.getPackets()
							.sendGameMessage(
									"The beast of burnden ability its not active. Add a BoB summoning pouch to activite this ability.");
				}
				break;
			case OPTION_3:
				if (LegendaryPetAbilities.onCoolDown(3600,
						pet.getPetVampReset())) {
					player.getPackets().sendGameMessage(
							"This ability will be ready in "
									+ LegendaryPetAbilities.getRemainingTime(
											7200, pet.getPetVampReset()) / 60
									+ " minutes.");
				} else {
					pet.setPetVampReset(player, Utils.currentTimeMillis()
							+ (30 * 1000));
					pet.setVamp(true);
				}
				break;
			}
		} else {
			switch (componentId) {
			case OPTION_1:
				if (LegendaryPetAbilities.petBank(player, usedItem, pet)) {
					pet.setPetItemBank(player, Utils.currentTimeMillis());
				}
				break;
			case OPTION_2:
				if (LegendaryPetAbilities.petAlch(player, usedItem, pet)) {
					pet.setPetHighAlchemy(player, Utils.currentTimeMillis());
				}
				break;
			case OPTION_3:
				if (ItemConstants.getItemFixed(usedItem.getId()) != -1) {
					if (LegendaryPetAbilities.petRepair(player, usedItem, pet)) {
						pet.setPetItemRepair(player, Utils.currentTimeMillis());
					}
				} else {
					player.getPackets().sendGameMessage("You cant fix this.");
				}
				break;
			}
		}
		end();
	}

	@Override
	public void finish() {

	}

}
