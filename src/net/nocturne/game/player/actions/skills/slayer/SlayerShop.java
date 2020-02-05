package net.nocturne.game.player.actions.skills.slayer;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.Equipment;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

public class SlayerShop {

	/**
	 * @author: Tommeh
	 */

	public static int INTER = 1308;

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 529:
		case 554:
		case 969:
		case 985:
			player.getPackets().sendGameMessage("You have reset your task.");
			player.getSlayerManager().skipCurrentTask(false);
			break;

		case 123:
			if (player.getSlayerManager().getPoints() >= 400) {
				if (player.getSkills().getXp(18) > 199999999) {
					player.getPackets().sendGameMessage(
							"You can't purchase XP in a skill where you already have maximum amount of XP.");
					return;
				}
				player.getSlayerManager().slayerPoints -= 400;
				player.getPackets().sendGameMessage("You have purchased slayer experience.");
				player.getSkills().addXp(Skills.SLAYER, 2);
				SlayerShop.sendInterface(player);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 400 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			break;
		case 203:
			if (player.getSlayerManager().getPoints() >= 75) {
				if (player.getInventory().getFreeSlots() < 1) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
					return;
				}
				player.getSlayerManager().slayerPoints -= 75;
				player.getPackets().sendGameMessage("You have purchased a ring of slaying.");
				player.getInventory().addItem(13281, 1);
				SlayerShop.sendInterface(player);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 75 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			break;
		case 208:
			if (player.getSlayerManager().getPoints() >= 35) {
				if (player.getInventory().getFreeSlots() < 3) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
					return;
				}
				player.getSlayerManager().slayerPoints -= 35;
				player.getPackets().sendGameMessage("You have purchased runes for slayer dart.");
				player.getInventory().addItem(560, 1000);
				player.getInventory().addItem(556, 3000);
				SlayerShop.sendInterface(player);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 35 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			break;
		case 216:
			if (player.getSlayerManager().getPoints() >= 35) {
				if (player.getInventory().getFreeSlots() < 1) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
					return;
				}
				player.getSlayerManager().slayerPoints -= 35;
				player.getPackets().sendGameMessage("You have purchased broad bolts.");
				player.getInventory().addItem(13280, 1000);
				SlayerShop.sendInterface(player);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 35 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			break;
		case 224:
			if (player.getSlayerManager().getPoints() >= 35) {
				if (player.getInventory().getFreeSlots() < 1) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
					return;
				}
				player.getSlayerManager().slayerPoints -= 35;
				player.getPackets().sendGameMessage("You have purchased broad arrows.");
				player.getInventory().addItem(4160, 1000);
				SlayerShop.sendInterface(player);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 35 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			break;
		case 243:
			int bitValue = player.getVarsManager().getBitValue(9065);
			if (bitValue != 1){
			if (player.getSlayerManager().getPoints() >= 300) {
				player.getSlayerManager().slayerPoints -= 300;
				player.getPackets().sendGameMessage("You have learned how to fletch broad arrows and bolts.");
				player.getVarsManager().sendVarBit(9065, 1);
				player.getVarbits().put(9065, 1);
				SlayerShop.sendInterface(player);
				player.getSlayerManager().unlockAbility(3);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 300 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			} else {
				player.getPackets().sendGameMessage("You have already purchased this ability.");
			}
			break;
		case 290:
			int bitValue1 = player.getVarsManager().getBitValue(9085);
			if (bitValue1 != 1){
			if (player.getSlayerManager().getPoints() >= 300) {
				player.getSlayerManager().slayerPoints -= 300;
				player.getPackets().sendGameMessage("You have learned how to deliver killing blows quicker.");
				player.getVarsManager().sendVarBit(9085, 1);
				player.getVarbits().put(9085, 1);
				SlayerShop.sendInterface(player);
				player.getSlayerManager().unlockAbility(1);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 300 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			} 
			} else {
				player.getPackets().sendGameMessage("You have already purchased this ability.");
			}
			break;
		case 309:
			int bitValue2 = player.getVarsManager().getBitValue(9084);
			if (bitValue2 != 1){
			if (player.getSlayerManager().getPoints() >= 2000) {
				player.getSlayerManager().slayerPoints -= 2000;
				player.getPackets().sendGameMessage("You have learned a new technique to attack ice strykewyrms.");
				player.getVarsManager().sendVarBit(9084, 1);
				player.getVarbits().put(9084, 1);
				SlayerShop.sendInterface(player);
				player.getSlayerManager().unlockAbility(2);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 2000 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			} else {
				player.getPackets().sendGameMessage("You have already purchased this ability.");
			}
			break;
		case 366:
			int bitValue3 = player.getVarsManager().getBitValue(9066);
			if (bitValue3 != 1){
			if (player.getSlayerManager().getPoints() >= 300) {
				player.getSlayerManager().slayerPoints -= 300;
				player.getPackets().sendGameMessage("You have learned how to craft rings of slaying.");
				player.getVarsManager().sendVarBit(9066, 1);
				player.getVarbits().put(9066, 1);
				SlayerShop.sendInterface(player);
				player.getSlayerManager().unlockAbility(4);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 300 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			} else {
				player.getPackets().sendGameMessage("You have already purchased this ability.");
			}
			break;
		case 385:
			int bitValue4 = player.getVarsManager().getBitValue(9067);
			if (bitValue4 != 1){
			if (player.getSlayerManager().getPoints() >= 400) {
				player.getSlayerManager().slayerPoints -= 400;
				player.getPackets().sendGameMessage("You have learned how to create slayer helmets.");
				player.getVarsManager().sendVarBit(9067, 1);
				player.getVarbits().put(9067, 1);
				SlayerShop.sendInterface(player);
				player.getSlayerManager().unlockAbility(5);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 400 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			} else {
				player.getPackets().sendGameMessage("You have already purchased this ability.");
			}
			break;
		/*
		 * case 953: if (player.getSlayerManager().getPoints() >= 100) { if
		 * (player.getInventory().getFreeSlots() < 1) {
		 * player.getPackets().sendGameMessage(
		 * "Not enough space in your inventory."); return; }
		 * player.getSlayerManager().slayerPoints -= 100;
		 * player.getPackets().sendGameMessage(
		 * "You have purchased the Ferocious ring upgrade.");
		 * player.getInventory().addItem(4160, 1000); } else {
		 * player.getPackets().sendGameMessage(
		 * "Sorry. That would cost 100 and you only have " +
		 * player.getSlayerManager().getPoints() + " Slayer points."); }
		 */
		case 147:
			break;
		case 622:
			int bitValue5 = player.getVarsManager().getBitValue(21387);
			if (bitValue5 != 1){
			if (player.getSlayerManager().getPoints() >= 500) {
				player.getSlayerManager().slayerPoints -= 500;
				player.getPackets()
						.sendGameMessage("You have learned how to fuse rings of slaying to full slayer helmets.");
				player.getVarsManager().sendVarBit(21387, 1);
				player.getVarbits().put(21387, 1);
				SlayerShop.sendInterface(player);
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost you 500 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			} else {
				player.getPackets().sendGameMessage("You have already purchased this ability.");
			}
			
			break;
		case 642:
			if (player.getSlayerManager().getPoints() >= 100) {
				if (player.getEquipment().getHatId() == 15492) {
					player.getSlayerManager().slayerPoints -= 100;
					player.getEquipment().getItems().set(Equipment.SLOT_HAT, new Item(30656));
					player.getEquipment().refresh(Equipment.SLOT_HAT);
					player.getAppearence().generateAppearenceData();
					player.getPackets().sendGameMessage("You have upgraded your full slayer helmet.");
					SlayerShop.sendInterface(player);
				} else {
					player.getPackets().sendGameMessage("You need to have a full slayer helmet equipped.");
				}
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 100 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			break;

		case 662:
			if (player.getSlayerManager().getPoints() >= 400) {
				if (player.getEquipment().getHatId() == 30656) {
					player.getSlayerManager().slayerPoints -= 400;
					player.getEquipment().getItems().set(Equipment.SLOT_HAT, new Item(30686));
					player.getEquipment().refresh(Equipment.SLOT_HAT);
					player.getAppearence().generateAppearenceData();
					player.getPackets().sendGameMessage("You have upgraded your reinforced slayer helmet.");
					SlayerShop.sendInterface(player);
				} else {
					player.getPackets().sendGameMessage("You need to have a reinforced slayer helmet equipped.");
				}
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 400 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			break;
		case 682:
			if (player.getSlayerManager().getPoints() >= 600) {
				if (player.getEquipment().getHatId() == 30686) {
					player.getSlayerManager().slayerPoints -= 600;
					player.getEquipment().getItems().set(Equipment.SLOT_HAT, new Item(30716));
					player.getEquipment().refresh(Equipment.SLOT_HAT);
					player.getAppearence().generateAppearenceData();
					player.getPackets().sendGameMessage("You have upgraded your strong slayer helmet.");
					SlayerShop.sendInterface(player);
				} else {
					player.getPackets().sendGameMessage("You need to have a strong slayer helmet equipped.");
				}
			} else {
				player.getPackets().sendGameMessage("Sorry. That would cost 600 and you only have "
						+ player.getSlayerManager().getPoints() + " Slayer points.");
			}
			break;
		default:
		}
	}

	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendCentralInterface(INTER);
		player.getPackets().sendIComponentText(INTER, 392, " " + player.getSlayerManager().getPoints());
		player.getVarsManager().sendVarBit(9071, player.getSlayerManager().getPoints());
	}
}