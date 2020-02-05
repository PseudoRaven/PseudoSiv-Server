package net.nocturne.game.player.dialogues.impl;

import java.util.ArrayList;

import net.nocturne.Settings;
import net.nocturne.game.npc.others.GraveStone;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.content.activities.reaper.ReaperRewardsShop;
import net.nocturne.game.player.content.activities.reaper.ReaperTasks;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.Utils;

public class ReaperDialogue extends Dialogue {

	ArrayList<Integer> items = new ArrayList<Integer>();

	private int npcId = 8867;
	public int amount;
	private Integer[][] slots;

	@Override
	public void start() {
		int option = (int) parameters[0];
		switch (option) {
		case 1:
			stage = 25;
			player.lock();
			sendNPCDialogue(
					npcId,
					NORMAL,
					"I thought you would return "
							+ player.getDisplayName()
							+ ". Do you wish to reclaim your items for a fee of course?");
			amount = new Double((player.getInventoryValue()
					+ player.getEquipmentValue() - player.getInventory()
					.getAmountOf(995)) * 0.3D).intValue();
			break;
		case 2:
			if (player.reaperTask >= (player.getDonationManager().isDonator() ? 3
					: 1)
					&& (Utils.currentTimeMillis() - player.lastReaperTask) < (24 * 60 * 60 * 1000)) {
				stage = 20;
				if (player.getDonationManager().isDonator())
					sendNPCDialogue(npcId, NORMAL,
							"You may only have three tasks from me a day.");
				else
					sendNPCDialogue(npcId, NORMAL,
							"You may only have one tasks from me a day.");
				return;
			}
			if (player.firstReaperTask == true) {
				stage = 1;
				sendNPCDialogue(npcId, NORMAL,
						"Welcome, " + player.getDisplayName() + ".");
			} else if (player.getReaperTasks().getCurrentTask() != null) {
				stage = 19;
				sendNPCDialogue(npcId, NORMAL,
						"You are taking a long time to complete my assignment, "
								+ player.getDisplayName()
								+ ". Are you finding it too difficult?.");
			} else if (player.getReaperTasks().getCurrentTask() == null) {
				stage = 20;
				sendNPCDialogue(npcId, NORMAL, "What brings you to my realm?");
			}
			break;
		}
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		switch (stage) {
		case -1:
			player.unlock();
			end();
			break;
		case 1:
			stage = 2;
			sendPlayerDialogue(SCARED, "Am I dead? What's going on here?");
			break;
		case 2:
			stage = 3;
			sendNPCDialogue(
					npcId,
					NORMAL,
					"You are not dead... yet. I have allowed you to enter as I need your help with a task.");
			break;
		case 3:
			stage = 4;
			sendNPCDialogue(
					npcId,
					NORMAL,
					"As you are no doubt aware, I am Death. The Reaper, the Collector of Souls, or any other name civilisations have given me. My task is to retrieve the soul from a dying creature, enabling its passage to the underworld.a");
			break;
		case 4:
			stage = 5;
			sendPlayerDialogue(QUESTIONS, "So what do you need me for?");
			break;
		case 5:
			stage = 6;
			sendNPCDialogue(
					npcId,
					NORMAL,
					"There is an imbalance in the harmony of life and death. There is far too much... life.");
			break;
		case 6:
			stage = 7;
			sendNPCDialogue(
					npcId,
					NORMAL,
					"My eyes have been on you in this age, "
							+ player.getDisplayName()
							+ ", as have the eyes of many others. You appear to have the skills I require to bring balance.");
			break;
		case 7:
			stage = 8;
			sendPlayerDialogue(QUESTIONS, "So you want me to kill stuff?");
			break;
		case 8:
			stage = 9;
			sendNPCDialogue(npcId, NORMAL,
					"If you wish to put it so indelicately, yes.");
			break;
		case 9:
			stage = 10;
			sendPlayerDialogue(HAPPY, "Great! When do I start?");
			break;
		case 10:
			stage = 11;
			sendNPCDialogue(
					npcId,
					NORMAL,
					"Immediately. First, you must take this gem. It will allow you to contact me remotely, should you require aid or a new assignment.");
			break;
		case 11:
			stage = 12;
			sendItemDialogue(31846, "You receive a grim gem.");
			player.getInventory().addItem(31846, 1);
			npcId = 8867;
			break;
		case 12:
			stage = 13;
			sendNPCDialogue(
					npcId,
					NORMAL,
					"And one final thing before you receive your first assignment. Are you happy to take on creatures as a group, or do you prefer to kill alone? You may change your mind later on, if you wish.");
			break;
		case 13:
			stage = 14;
			sendOptionsDialogue("Allow group bosses to be assigned?",
					"Yes, I'm happy to receive group bosses.",
					"No, I don't want to team up with other adventurers.");
			break;
		case 14:
			switch (componentId) {
			case OPTION_1:
				stage = 16;
				player.firstReaperTask = false;
				sendNPCDialogue(npcId, NORMAL,
						"Now for your first assignment. I hope you are ready.");
				break;
			case OPTION_2:
				stage = 16;
				player.toggleGroupAssignments();
				player.firstReaperTask = false;
				sendNPCDialogue(npcId, NORMAL,
						"Now for your first assignment. I hope you are ready.");
				break;
			}
			break;
		case 16:
			if (player.reaperTask >= (player.getDonationManager().isDonator() ? 3
					: 1)
					&& (Utils.currentTimeMillis() - player.lastReaperTask) < (24 * 60 * 60 * 1000)) {
				stage = -1;
				if (player.getDonationManager().isDonator())
					sendNPCDialogue(npcId, NORMAL,
							"You may only have three tasks from me a day.");
				else
					sendNPCDialogue(npcId, NORMAL,
							"You may only have one tasks from me a day.");
				return;
			}
			stage = 17;
			final ArrayList<ReaperTasks> tasks = new ArrayList<ReaperTasks>();
			for (final ReaperTasks t : ReaperTasks.values()) {
				if (player.getSkills().getCombatLevel() < t.getRequirement())
					continue;
				tasks.add(t);
			}
			if (tasks.isEmpty()) {
				sendNPCDialogue(npcId, NORMAL,
						"You don't meet the requirements to get tasks from me yet.");
				stage = 100;
			} else {
				player.getReaperTasks().generateTask();
				sendNPCDialogue(npcId, NORMAL, "I require you to collect "
						+ player.getReaperTasks().getAmount()
						+ " souls from the following battle: "
						+ player.getReaperTasks().getCurrentTask().getName(),
						". Can I trust you with this task?");
			}
			break;
		case 17:
			stage = 18;
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE,
					"You certainly can. Thanks!",
					"I'd like a different assignment.",
					"I need some hints about my assignment.");
			break;
		case 18:
			switch (componentId) {
			case OPTION_1:
				end();
				break;
			case OPTION_2:
				stage = -1;
				player.getReaperTasks().generateTask();
				sendNPCDialogue(npcId, NORMAL, "Your new task is to retrieve "
						+ player.getReaperTasks().getAmount()
						+ " souls from the following battle: "
						+ player.getReaperTasks().getCurrentTask().getName(),
						".");
				break;
			case OPTION_3:
				stage = -1;
				sendNPCDialogue(npcId, NORMAL, player.getReaperTasks()
						.getCurrentTask().getHints());
				break;
			}
			break;
		case 19:
			stage = 20;
			sendDialogue("If you choose to reset your assignment this will be treated as a brand new task, with fresh rerolls. There is no guarantee the new assignment will be different to your current one. If you wish to reroll your current task instead, use your grim gem.");
			break;
		case 20:
			stage = 21;
			if (player.getReaperTasks().getCurrentTask() != null) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE,
						"I'd like a new assignment/re-roll.",
						"I'd like another grim gem.",
						"Are there any rewards for this?",
						"I need some hints about my assignment.",
						"Am I dead? What do you want from me?");
			} else {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE,
						"I'd like a new assignment/re-roll.",
						"I'd like another grim gem.",
						"Are there any rewards for this?",
						"Am I dead? What do you want from me?");
			}
			break;
		case 21:
			switch (componentId) {
			case OPTION_1:
				if (player.getRerollCount() >= 5) {
					sendNPCDialogue(npcId, NORMAL,
							"You have already used the maximum amount of rerolls.");
					return;
				}
				if (player.getReaperTasks().getCurrentTask() != null) {
					player.increaseRerollCount();
				} else if (player.reaperTask >= (player.getDonationManager()
						.isDonator() ? 3 : 1)
						&& (Utils.currentTimeMillis() - player.lastReaperTask) < (24 * 60 * 60 * 1000)) {
					stage = -1;
					if (player.getDonationManager().isDonator())
						sendNPCDialogue(npcId, NORMAL,
								"You may only have three tasks from me a day.");
					else
						sendNPCDialogue(npcId, NORMAL,
								"You may only have one tasks from me a day.");
					return;
				}
				stage = 17;
				player.getReaperTasks().generateTask();
				if (player.getRerollCount() > 1) {
					sendNPCDialogue(npcId, NORMAL, "I require you to collect "
							+ player.getReaperTasks().getAmount()
							+ " souls from the following battle: "
							+ player.getReaperTasks().getCurrentTask()
									.getName(),
							". Your reward points have been reduced by 25%.");
				} else {
					sendNPCDialogue(npcId, NORMAL, "I require you to collect "
							+ player.getReaperTasks().getAmount()
							+ " souls from the following battle: "
							+ player.getReaperTasks().getCurrentTask()
									.getName(),
							". Can I trust you with this task?");
				}
				break;
			case OPTION_2:
				stage = -1;
				sendItemDialogue(31846, "You receive a grim gem.");
				player.getInventory().addItem(31846, 1);
				break;
			case OPTION_3:
				ReaperRewardsShop.openShop(player);
				end();
				break;
			case OPTION_4:
				if (player.getReaperTasks().getCurrentTask() != null) {
					stage = -1;
					sendNPCDialogue(npcId, NORMAL, player.getReaperTasks()
							.getCurrentTask().getHints());
				} else {
					stage = 2;
					sendPlayerDialogue(SCARED,
							"Am I dead? What's going on here?");
				}
				break;
			case OPTION_5:
				stage = 2;
				sendPlayerDialogue(SCARED, "Am I dead? What's going on here?");
				break;
			}
			break;
		case 25:
			if (amount < 1000)
				stage = 35;
			else
				stage = 26;
			sendPlayerDialogue(NORMAL, "Yes.");
			break;
		case 26:
			if (player.isBeginningAccount()) {
				stage = 31;
				sendNPCDialogue(
						npcId,
						NORMAL,
						"Actually, since recently started playing "
								+ Settings.SERVER_NAME
								+ " I will not charge you.<br>Use the portal to exit.");
				return;
			}
			if (player.getDonationManager().isDemonicDonator()) {
				stage = 31;
				sendNPCDialogue(npcId, NORMAL, "Use the portal to exit.");
				return;
			}
			stage = 27;
			sendNPCDialogue(npcId, NORMAL,
					"Based on your Inventory Wealth upon your death, That will cost you "
							+ Utils.format(amount) + "gp, are you sure?");
			break;
		case 27:
			stage = 28;
			sendOptionsDialogue("Claim items for " + Utils.format(amount)
					+ "gp?", "Yes, claim items.", "Nevermind.");
			break;
		case 28:
			switch (componentId) {
			case OPTION_1:
				if (player.isBeginningAccount()) {
					stage = 31;
					sendNPCDialogue(
							npcId,
							NORMAL,
							"Actually, You're a new player, You're soul isn't worth anything.....yet, you're free to go");
					return;
				}
				if (player.getDonationManager().isDemonicDonator()) {
					stage = 31;
					sendNPCDialogue(npcId, NORMAL, "You don't have to pay me, "
							+ player.getDisplayName()
							+ ", demonic donators shall pass.");
					return;
				}
				if (player.getMoneyPouch().getCoinsAmount() >= amount) {
					stage = 31;
					player.getMoneyPouch().setAmount(amount, true);
					sendNPCDialogue(
							npcId,
							NORMAL,
							"Glad doing business with you, "
									+ player.getDisplayName() + ".",
							"Use the portal to exit.");
				} else if (player.getInventory().getCoinsAmount() >= amount) {
					stage = 31;
					player.getInventory().deleteItem(995, amount);
					sendNPCDialogue(
							npcId,
							NORMAL,
							"Glad doing business with you, "
									+ player.getDisplayName() + ".",
							"Use the portal to exit.");
				} else {
					if (player.getRights() > 1)
						stage = 31;
					else
						stage = 30;
					sendNPCDialogue(npcId, NORMAL,
							"It does not seem like you have enough money to pay me, "
									+ player.getDisplayName() + ".",
							"Use the portal to exit.");
				}
				break;
			case OPTION_2:
				stage = 30;
				sendNPCDialogue(npcId, CONFUSED, "As you wish.",
						"Use the portal to exit.");
				break;
			}
			break;
		case 30:
			slots = GraveStone.getItemSlotsKeptOnDeath(player, false, false,
					player.getPrayer().isProtectingItem());
			synchronized (slots) {
				player.sendItemsOnDeath(null, Settings.HOME_LOCATION,
						Settings.HOME_LOCATION, false, slots, true);
			}
			player.setHitpoints(player.getMaxHitpoints());
			player.isInDeathRoom = false;
			player.setCloseInterfacesEvent(null);
			Magic.sendObjectTeleportSpell(player, true, Settings.HOME_LOCATION);
			player.unlock();
			end();
			break;
		case 31:
			player.setHitpoints(player.getMaxHitpoints());
			player.isInDeathRoom = false;
			player.setCloseInterfacesEvent(null);
			Magic.sendObjectTeleportSpell(player, true, Settings.HOME_LOCATION);
			player.unlock();
			end();
			break;
		case 35:
			stage = 31;
			sendNPCDialogue(npcId, LAUGHING,
					"Actually, based on your wealth...you'll be back soon enough! "
							+ " I will not charge you on this occasion.");
			break;
		case 100:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}