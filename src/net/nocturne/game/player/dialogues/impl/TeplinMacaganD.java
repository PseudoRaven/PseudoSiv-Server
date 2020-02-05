package net.nocturne.game.player.dialogues.impl;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.actions.Drinkables.Drink;
import net.nocturne.game.player.dialogues.Dialogue;

/**
 * 
 * @author Miles Black (bobismyname)
 * @date Feb 20, 2017
 */

public class TeplinMacaganD extends Dialogue {

	private int npcId;
	private int option;

	@Override
	public void start() {
		npcId = (int) parameters[0];
		option = (int) parameters[1];
		switch (option) {
		case 1:
			stage = -1;
			sendOptionsDialogue(
					DEFAULT_OPTIONS_TITLE,
					"Can you convert my flasks into normal vials for me?",
					"Can you decant my potions into flasks, please?",
					"Can you decant my flasks back into 6 dosed flasks, please?",
					"I've got to go.");
			break;
		case 2:
			stage = 0;
			sendNPCDialogue(
					npcId,
					NORMAL,
					"I'll see what i can do. I can make use of the flasks, so I'll give you the vials for free.");
			break;
		case 3:
			stage = 0;
			if (player.getInventory().containsItem(
					ItemIdentifiers.POTION_FLASK, 1)
					|| player.getInventory().containsItem(
							ItemIdentifiers.POTION_FLASK + 1, 1))
				sendNPCDialogue(
						npcId,
						NORMAL,
						"Lets see what you have. I will decant potion vials and all flasks with up to 4 doses into flasks. You'll need to supply the empty flasks yourself; they're not cheap.");
			else
				sendNPCDialogue(
						npcId,
						NORMAL,
						"You'll need to supply the empty flasks yourself; they're not cheap. You can bring them to me noted or unnoted.");
			break;
		case 4:
			stage = 0;
			sendNPCDialogue(
					npcId,
					NORMAL,
					"Let's see what you have. I will decant your potion flasks back into 6 dosed flasks. I can make use of the flasks, so I'll take those as payment.");
			break;
		}
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			switch (componentId) {
			case OPTION_1:
			case OPTION_2:
			case OPTION_3:
				player.getDialogueManager().startDialogue(
						"TeplinMacaganD",
						npcId,
						componentId == OPTION_1 ? 2
								: componentId == OPTION_2 ? 3 : 4);
				break;
			default:
				end();
				break;
			}
			break;
		case 0:
			if (option == 3) {
				if (player.getInventory().containsItem(
						ItemIdentifiers.POTION_FLASK, 1)
						|| player.getInventory().containsItem(
								ItemIdentifiers.POTION_FLASK + 1, 1))
					startDecantingProcess();
				else
					end();
				return;
			}
			end();
			startDecantingProcess();
			break;
		}
	}

	private void startDecantingProcess() {
		switch (option) {
		case 2:
			List<Item> potionsToAdd = new ArrayList<>();
			for (Drink flask : Drink.values()) {
				int flaskDoses = 0;
				if (flask == null || !flask.isFlask()
						|| flask.ordinal() >= Drink.WEAK_MELEE_POTION.ordinal())
					continue;
				for (int i = flask.getId().length - 1; i >= 0; i--) {
					int id = flask.getId()[i];
					if (player.getInventory().containsItem(id, 1)) {
						flaskDoses += player.getInventory().getAmountOf(id)
								* (flask.getId().length - i);
					}
					if (player.getInventory().containsItem(
							ItemDefinitions.getItemDefinitions(id).getCertId(),
							1)) {
						flaskDoses += player.getInventory().getAmountOf(
								ItemDefinitions.getItemDefinitions(id)
										.getCertId())
								* (flask.getId().length - i);
					}
				}
				Drink potion = flask.toPotion();
				if (potion == null)
					continue;
				boolean hasThisFlask = false;
				for (int flaskItemId : flask.getId()) {
					if (player.getInventory().containsItem(flaskItemId, 1))
						hasThisFlask = true;
				}
				for (int flaskItemId : flask.getNotedId()) {
					if (player.getInventory().containsItem(flaskItemId, 1))
						hasThisFlask = true;
				}
				if (!hasThisFlask)
					continue;
				int dosesRemoved = 0;
				for (int i = 6; i > 0; i--) {
					while (dosesRemoved <= flaskDoses
							&& (player.getInventory().containsItem(
									flask.getIdForDoses(i), 1) || player
									.getInventory().containsItem(
											ItemDefinitions.getItemDefinitions(
													flask.getIdForDoses(i))
													.getCertId(), 1))) {
						if (player.getInventory().containsItem(
								flask.getIdForDoses(i), 1)) {
							player.getInventory().deleteItem(
									new Item(flask.getIdForDoses(i), 1));
							dosesRemoved += i;
						}
						if (player.getInventory().containsItem(
								ItemDefinitions.getItemDefinitions(
										flask.getIdForDoses(i)).getCertId(), 1)) {
							player.getInventory().deleteItem(
									new Item(ItemDefinitions
											.getItemDefinitions(
													flask.getIdForDoses(i))
											.getCertId(), 1));
							dosesRemoved += i;
						}
					}
				}
				for (int i = 4; i > 0; i--) {
					int amount = (dosesRemoved / i);
					if (amount == 0 || dosesRemoved <= 0)
						continue;
					Item item = new Item(ItemDefinitions.getItemDefinitions(
							potion.getIdForDoses(i)).getCertId(), amount);
					if (player.getInventory().getFreeSlots() > 0) {
						potionsToAdd.add(item);
						dosesRemoved = (dosesRemoved % i);
					}
				}
			}
			for (Item potion : potionsToAdd) {
				if (potion == null)
					continue;
				player.getInventory().addItem(potion);
			}
			player.getDialogueManager()
					.startDialogue(
							"SimpleNPCMessage",
							npcId,
							potionsToAdd.size() > 0 ? "Here you go. That's the best i can do!"
									: "Sorry you don't seem to have anything I can help you with! Make sure you have the correct potions and some free space in your inventory and I can try again.");
			break;
		case 3:
			potionsToAdd = new ArrayList<>();
			int maxDoses = (player.getInventory().getAmountOf(
					ItemIdentifiers.POTION_FLASK) * 6)
					+ (player.getInventory().getAmountOf(
							ItemIdentifiers.POTION_FLASK + 1) * 6);
			for (Drink potion : Drink.values()) {
				int doses = 0;
				if (potion == null
						|| !potion.isPotion()
						|| potion.ordinal() >= Drink.WEAK_MELEE_POTION
								.ordinal())
					continue;
				for (int i = 3; i >= 0; i--) {
					int id = potion.getId()[i];
					if (player.getInventory().containsItem(id, 1)) {
						doses += player.getInventory().getAmountOf(id)
								* (potion.getId().length - i);
					}
					if (player.getInventory().containsItem(
							ItemDefinitions.getItemDefinitions(id).getCertId(),
							1)) {
						doses += player.getInventory().getAmountOf(
								ItemDefinitions.getItemDefinitions(id)
										.getCertId())
								* (potion.getId().length - i);
					}
				}
				Drink flask = potion.toFlask();
				if (flask == null)
					continue;
				boolean hasThisPotion = false;
				for (int flaskItemId : potion.getId()) {
					if (player.getInventory().containsItem(flaskItemId, 1))
						hasThisPotion = true;
				}
				for (int flaskItemId : potion.getNotedId()) {
					if (player.getInventory().containsItem(flaskItemId, 1))
						hasThisPotion = true;
				}
				if (!hasThisPotion)
					continue;
				doses = doses > maxDoses ? maxDoses : doses;
				if (doses < 6)
					continue;
				for (int i = doses; i > 0; i -= 6) {
					if (player.getInventory().containsItem(
							ItemIdentifiers.POTION_FLASK, 1)) {
						player.getInventory().deleteItem(
								ItemIdentifiers.POTION_FLASK, 1);
						continue;
					}
					if (player.getInventory().containsItem(
							ItemIdentifiers.POTION_FLASK + 1, 1)) {
						player.getInventory().deleteItem(
								ItemIdentifiers.POTION_FLASK + 1, 1);
					}
				}
				int dosesRemoved = 0;
				for (int i = 4; i > 0; i--) {
					while (dosesRemoved <= doses
							&& (player.getInventory().containsItem(
									potion.getIdForDoses(i), 1) || player
									.getInventory().containsItem(
											ItemDefinitions.getItemDefinitions(
													potion.getIdForDoses(i))
													.getCertId(), 1))) {
						if (player.getInventory().containsItem(
								potion.getIdForDoses(i), 1)) {
							player.getInventory().deleteItem(
									new Item(potion.getIdForDoses(i), 1));
							potionsToAdd.add(new Item(
									ItemIdentifiers.EMPTY_VIAL + 1, 1));
							dosesRemoved += i;
						}
						if (player.getInventory()
								.containsItem(
										ItemDefinitions.getItemDefinitions(
												potion.getIdForDoses(i))
												.getCertId(), 1)) {
							player.getInventory().deleteItem(
									new Item(ItemDefinitions
											.getItemDefinitions(
													potion.getIdForDoses(i))
											.getCertId(), 1));
							potionsToAdd.add(new Item(
									ItemIdentifiers.EMPTY_VIAL + 1, 1));
							dosesRemoved += i;
						}
					}
				}
				for (int i = 6; i > 0; i--) {
					int amount = (dosesRemoved / i);
					if (amount == 0 || dosesRemoved <= 0)
						continue;
					Item item = new Item(ItemDefinitions.getItemDefinitions(
							flask.getIdForDoses(i)).getCertId(), amount);
					if (player.getInventory().getFreeSlots() > 0) {
						potionsToAdd.add(item);
						dosesRemoved = (dosesRemoved % i);
					}
				}
			}
			for (Item potion : potionsToAdd) {
				if (potion == null)
					continue;
				player.getInventory().addItem(potion);
			}
			player.getDialogueManager()
					.startDialogue(
							"SimpleNPCMessage",
							npcId,
							potionsToAdd.size() > 0 ? "Here you go. That's the best i can do!"
									: "Sorry you don't seem to have anything I can help you with! Make sure you have the correct potions and some free space in your inventory and I can try again.");
			break;
		case 4:
			potionsToAdd = new ArrayList<>();
			for (Drink flask : Drink.values()) {
				if (flask == null || !flask.isFlask()
						|| flask.ordinal() >= Drink.WEAK_MELEE_POTION.ordinal())
					continue;
				int flaskDoses = 0;
				for (int i = flask.getId().length - 1; i >= 0; i--) {
					int id = flask.getId()[i];
					if (player.getInventory().containsItem(id, 1)) {
						flaskDoses += player.getInventory().getAmountOf(id)
								* (flask.getId().length - i);
					}
					if (player.getInventory().containsItem(
							ItemDefinitions.getItemDefinitions(id).getCertId(),
							1)) {
						flaskDoses += player.getInventory().getAmountOf(
								ItemDefinitions.getItemDefinitions(id)
										.getCertId())
								* (flask.getId().length - i);
					}
				}
				boolean hasThisFlask = false;
				for (int flaskItemId : flask.getId()) {
					if (player.getInventory().containsItem(flaskItemId, 1))
						hasThisFlask = true;
				}
				for (int flaskItemId : flask.getNotedId()) {
					if (player.getInventory().containsItem(flaskItemId, 1))
						hasThisFlask = true;
				}
				if (!hasThisFlask)
					continue;
				int dosesRemoved = 0;
				for (int i = 6; i > 0; i--) {
					while (dosesRemoved <= flaskDoses
							&& (player.getInventory().containsItem(
									flask.getIdForDoses(i), 1) || player
									.getInventory().containsItem(
											ItemDefinitions.getItemDefinitions(
													flask.getIdForDoses(i))
													.getCertId(), 1))) {
						if (player.getInventory().containsItem(
								flask.getIdForDoses(i), 1)) {
							player.getInventory().deleteItem(
									new Item(flask.getIdForDoses(i), 1));
							dosesRemoved += i;
						}
						if (player.getInventory().containsItem(
								ItemDefinitions.getItemDefinitions(
										flask.getIdForDoses(i)).getCertId(), 1)) {
							player.getInventory().deleteItem(
									new Item(ItemDefinitions
											.getItemDefinitions(
													flask.getIdForDoses(i))
											.getCertId(), 1));
							dosesRemoved += i;
						}
					}
				}
				for (int i = 6; i > 0; i--) {
					int amount = (dosesRemoved / i);
					if (amount == 0 || dosesRemoved <= 0)
						continue;
					Item item = new Item(ItemDefinitions.getItemDefinitions(
							flask.getIdForDoses(i)).getCertId(), amount);
					if (player.getInventory().getFreeSlots() > 0) {
						potionsToAdd.add(item);
						dosesRemoved = (dosesRemoved % i);
					}
				}
			}
			for (Item potion : potionsToAdd) {
				if (potion == null)
					continue;
				player.getInventory().addItem(potion);
			}
			player.getDialogueManager()
					.startDialogue(
							"SimpleNPCMessage",
							npcId,
							potionsToAdd.size() > 0 ? "Here you go. That's the best i can do!"
									: "Sorry you don't seem to have anything I can help you with! Make sure you have the correct potions and some free space in your inventory and I can try again.");
			break;
		}
	}

	@Override
	public void finish() {

	}

}