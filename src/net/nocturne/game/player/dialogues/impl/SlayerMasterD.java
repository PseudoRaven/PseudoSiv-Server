package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.slayer.Slayer.SlayerMaster;
import net.nocturne.game.player.actions.skills.slayer.SlayerShop;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.ShopsHandler;

public class SlayerMasterD extends Dialogue {

	private int npcId;
	private SlayerMaster master;

	public void start() {
		npcId = (Integer) parameters[0];
		master = (SlayerMaster) parameters[1];
		sendNPCDialogue(npcId, NORMAL, "'Ello, and what are you after then?");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue(
					"Select an option",
					"I need another assignment",
					"I would like to reset my current assignment",
					"Do you have anything for trade?",
					"I am here to discuss any rewards I might be eligible for.",
					"Er...nothing...");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_2:
				sendPlayerDialogue(NORMAL,
						"I would like to reset my current assignment.");
				stage = 14;
				break;
			case OPTION_1:
				sendPlayerDialogue(NORMAL, "I need a another assignment.");
				stage = 3;
				break;
			case OPTION_3:
				ShopsHandler.openShop(player, 54);
				end();
				break;
			case OPTION_4:
				sendPlayerDialogue(NORMAL,
						"I am here to discuss any rewards I might be eligible for.");
				stage = 10;
				break;
			case OPTION_5:
				sendPlayerDialogue(NORMAL, "Er...nothing...");
				stage = 500;
				break;
			}
			break;
		case 500:
			end();
			break;
		case 3:
			if (player.getSlayerManager().getCurrentTask() != null) {
				sendNPCDialogue(npcId, NORMAL, "You are still hunting "
						+ player.getSlayerManager().getCurrentTask().getName()
						+ "; come back when you have finished your task.");
			} else if (player.getSkills().getCombatLevel() < master
					.getRequiredCombatLevel())
				sendNPCDialogue(npcId, 9827,
						"You are too weak overall, come back when you have become stronger.");
			else if (player.getSkills().getLevel(Skills.SLAYER) < master
					.getRequiredSlayerLevel()) {
				sendNPCDialogue(
						npcId,
						NORMAL,
						"Sorry, but you are not a good enough slayer yet for my teachings. Come back when you are worth something to me.");
			} else {
				player.getSlayerManager().setCurrentTask(true, master);
				sendNPCDialogue(npcId, 9827,
						"Excellent, you're doing great. Your new assignment is to kill "
								+ " "
								+ player.getSlayerManager().getCount()
								+ " "
								+ player.getSlayerManager().getCurrentTask()
										.getName() + ".");
				stage = 4;
				break;
			}
			break;
		case 4:
			sendOptionsDialogue("Select an option", "Got any tips for me?",
					"Okay, great!");
			stage = 5;
			break;
		case 5:
			switch (componentId) {
			case OPTION_1:
				sendPlayerDialogue(NORMAL, "Got any tips for me?");
				stage = 6;
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		case 6:
			sendNPCDialogue(npcId, NORMAL, player.getSlayerManager()
					.getCurrentTask().getTips());
			stage = 7;
			break;
		case 7:
			end();
			break;
		case 10:
			sendOptionsDialogue("select an option", "View rewards.",
					"Where can I use the co-op food and potion rewards?",
					"That's all, thanks.");
			stage = 11;
			break;
		case 11:
			switch (componentId) {
			case OPTION_1:
				SlayerShop.sendInterface(player);
				end();
				break;
			case OPTION_2:
				sendNPCDialogue(
						npcId,
						NORMAL,
						"You'll gain co-op reward points if you complete at least ",
						"half of your contribution to a task with a friend.");
				stage = 13;
				break;
			case OPTION_3:
				end();
				break;
			}
			break;
		case 13:
			sendNPCDialogue(
					npcId,
					NORMAL,
					"That means you need to be in a group for most of the "
							+ "time you're working on your task, but it doesn't matter if "
							+ "your friend beats you to the majority of kills!");
			stage = 26;
			break;
		case 14:
			sendNPCDialogue(npcId, 9827,
					"Your current assignment has been reset.");
			player.getSlayerManager().skipCurrentTask(false);
			player.getSlayerManager().updateInterface();
			stage = 500;
			break;
		case 26:
			sendNPCDialogue(
					npcId,
					NORMAL,
					"You'll still have been in a group for most of your "
							+ "contribution, so you're still entitled to a co-op reward point");
			stage = 10;
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
