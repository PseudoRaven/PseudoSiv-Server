package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.content.activities.distractions.PenguinHS;
import net.nocturne.game.player.dialogues.Dialogue;

/**
 * @author Miles Black (mablack15@gmail.com)
 */

public class PenguinRewards extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				end();
				player.getPackets().sendGameMessage(
						"You currently have " + player.getPenguinPoints()
								+ " penguins points.");
			} else if (componentId == OPTION_2) {
				end();
				player.getPackets().sendGameMessage(
						"Hint: " + PenguinHS.current + "");
			} else if (componentId == OPTION_3) {
				stage = 2;
				sendOptionsDialogue("Pick an Option.", "Coins and Spins",
						"Skill Exp", "Masks and Cosmetics", "None");
			} else if (componentId == OPTION_4) {
				end();
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				stage = 3;
				sendOptionsDialogue("Pick an Option.",
						"10k + 1 Spin (1 point)", "100k + 10 Spins (8 points)",
						"200k + 20 Spins (15 points)", "None");
			} else if (componentId == OPTION_2) {
				stage = 4;
				sendOptionsDialogue("Pick an Option.", "Small Exp (1 point)",
						"Medium Exp (2 points)", "Large Exp (3 points)", "None");
			} else if (componentId == OPTION_3) {
				stage = 5;
				sendOptionsDialogue("Pick an Option.",
						"Botanist's Mask (35 points)",
						"Scarecrow Mask (45 points)",
						"Scabaras Mask (50 points)",
						"Scabaras Mask (50 points)", "More");
			} else if (componentId == OPTION_4) {
				end();
			}
		} else if (stage == 3) {
			end();
			if (componentId == OPTION_1) {
				if (player.getPenguinPoints() >= 1) {
					player.setPenguinPoints(player.getPenguinPoints() - 1);
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.COINS, 10000);
					player.getTreasureHunter().handleEarnedKeys(1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_2) {
				if (player.getPenguinPoints() >= 8) {
					player.setPenguinPoints(player.getPenguinPoints() - 8);
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.COINS, 100000);
					player.getTreasureHunter().handleEarnedKeys(10);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_3) {
				if (player.getPenguinPoints() >= 15) {
					player.setPenguinPoints(player.getPenguinPoints() - 15);
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.COINS, 200000);
					player.getTreasureHunter().handleEarnedKeys(20);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			}
		} else if (stage == 4) {
			end();
			if (componentId == OPTION_1) {
				if (player.getPenguinPoints() >= 1) {
					player.setPenguinPoints(player.getPenguinPoints() - 1);
					player.getInventory().addItemMoneyPouch(23713, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_2) {
				if (player.getPenguinPoints() >= 2) {
					player.setPenguinPoints(player.getPenguinPoints() - 2);
					player.getInventory().addItemMoneyPouch(23714, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_3) {
				if (player.getPenguinPoints() >= 3) {
					player.setPenguinPoints(player.getPenguinPoints() - 3);
					player.getInventory().addItemMoneyPouch(23715, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			}
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				end();
				if (player.getPenguinPoints() >= 35) {
					player.setPenguinPoints(player.getPenguinPoints() - 35);
					player.getInventory().addItemMoneyPouch(25190, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_2) {
				end();
				if (player.getPenguinPoints() >= 45) {
					player.setPenguinPoints(player.getPenguinPoints() - 45);
					player.getInventory().addItemMoneyPouch(25322, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_3) {
				end();
				if (player.getPenguinPoints() >= 50) {
					player.setPenguinPoints(player.getPenguinPoints() - 50);
					player.getInventory().addItemMoneyPouch(25124, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_4) {
				end();
				if (player.getPenguinPoints() >= 20) {
					player.setPenguinPoints(player.getPenguinPoints() - 20);
					player.getInventory().addItemMoneyPouch(7003, 1);
				} else {
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
				}
			} else if (componentId == OPTION_5) {
				stage = 6;
				sendOptionsDialogue("Pick an Option.",
						"Apmeken Mask (45 points)", "Factory Mask (40 points)",
						"Gorilla Mask (50 points)", "Sheep Mask (15 points)",
						"More");
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				end();
				if (player.getPenguinPoints() >= 45) {
					player.setPenguinPoints(player.getPenguinPoints() - 45);
					player.getInventory().addItemMoneyPouch(25122, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_2) {
				end();
				if (player.getPenguinPoints() >= 40) {
					player.setPenguinPoints(player.getPenguinPoints() - 40);
					player.getInventory().addItemMoneyPouch(22959, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_3) {
				end();
				if (player.getPenguinPoints() >= 50) {
					player.setPenguinPoints(player.getPenguinPoints() - 50);
					player.getInventory().addItemMoneyPouch(22314, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_4) {
				end();
				if (player.getPenguinPoints() >= 15) {
					player.setPenguinPoints(player.getPenguinPoints() - 15);
					player.getInventory().addItemMoneyPouch(13107, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_5) {
				stage = 7;
				sendOptionsDialogue("Pick an Option.", "Bat Mask (17 points)",
						"Penguin Mask (20 points)", "Cat Mask (23 points)",
						"Wolf Mask (25 points)", "More");
			}
		} else if (stage == 7) {
			if (componentId == OPTION_1) {
				end();
				if (player.getPenguinPoints() >= 17) {
					player.setPenguinPoints(player.getPenguinPoints() - 17);
					player.getInventory().addItemMoneyPouch(13109, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_2) {
				end();
				if (player.getPenguinPoints() >= 20) {
					player.setPenguinPoints(player.getPenguinPoints() - 20);
					player.getInventory().addItemMoneyPouch(13111, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_3) {
				end();
				if (player.getPenguinPoints() >= 23) {
					player.setPenguinPoints(player.getPenguinPoints() - 23);
					player.getInventory().addItemMoneyPouch(13113, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_4) {
				end();
				if (player.getPenguinPoints() >= 25) {
					player.setPenguinPoints(player.getPenguinPoints() - 25);
					player.getInventory().addItemMoneyPouch(13115, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_5) {
				stage = 8;
				sendOptionsDialogue("Pick an Option.", "Fox Mask (35 points)",
						"White Unicorn Mask (45 points)",
						"Black Unicorn Mask (45 points)", "None");
			}
		} else if (stage == 8) {
			end();
			if (componentId == OPTION_1) {
				if (player.getPenguinPoints() >= 35) {
					player.setPenguinPoints(player.getPenguinPoints() - 35);
					player.getInventory().addItemMoneyPouch(19272, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_2) {
				if (player.getPenguinPoints() >= 45) {
					player.setPenguinPoints(player.getPenguinPoints() - 45);
					player.getInventory().addItemMoneyPouch(19275, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			} else if (componentId == OPTION_3) {
				if (player.getPenguinPoints() >= 45) {
					player.setPenguinPoints(player.getPenguinPoints() - 45);
					player.getInventory().addItemMoneyPouch(19278, 1);
				} else
					player.getPackets()
							.sendGameMessage(
									"You do not have enough penguin points to purchase this.");
			}
		}

	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Pick an Option.", "Check Penguin Points",
				"Current Hint", "Rewards Shop", "None");
	}

}