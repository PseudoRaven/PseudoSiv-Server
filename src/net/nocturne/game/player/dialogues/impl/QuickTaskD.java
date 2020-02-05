package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.slayer.Slayer.SlayerMaster;
import net.nocturne.game.player.dialogues.Dialogue;

public class QuickTaskD extends Dialogue {

	@Override
	public void start() {
		SlayerMaster master = (SlayerMaster) parameters[0];
		int npcId = master.getNPCId();
		if (player.getSlayerManager().getCurrentTask() != null) {
			if (master == SlayerMaster.TURAEL
					&& player.getSlayerManager().getCurrentMaster() != SlayerMaster.TURAEL) {
			} else {
				sendNPCDialogue(npcId, NORMAL, "You're still hunting "
						+ player.getSlayerManager().getCurrentTask().getName()
						+ "; come back when you've finished your task.");
				return;
			}
			stage = 10;
		}
		if (player.getSkills().getCombatLevel() < master
				.getRequiredCombatLevel()) {
			sendNPCDialogue(npcId, 9827,
					"Your too weak overall, come back when you've become stronger.");
			stage = 10;
		} else if (player.getSkills().getLevel(Skills.SLAYER) < master
				.getRequiredSlayerLevel()) {
			sendNPCDialogue(
					npcId,
					9827,
					"Sorry, but you're not a good enough slayer yet for my teachings. Come back when you're worth something to me.");
			stage = 10;
		} else {
			if (master == SlayerMaster.TURAEL
					&& player.getSlayerManager().getCurrentTask() != null)
				player.getSlayerManager().skipCurrentTask(true);
			player.getSlayerManager().setCurrentTask(true, master);
			sendNPCDialogue(npcId, 9827,
					"Excellent, you're doing great. Your new assignment is to kill "
							+ " "
							+ player.getSlayerManager().getCount()
							+ " "
							+ player.getSlayerManager().getCurrentTask()
									.getName() + ".");
			stage = 1;
		}

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue("Select an option", "Got any tips for me?",
					"Okay, great!");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				sendPlayerDialogue(NORMAL, "Got any tips for me?");
				stage = 3;
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		case 3:
			sendNPCDialogue(npcId, NORMAL, player.getSlayerManager()
					.getCurrentTask().getTips());
			stage = 4;
			break;
		case 4:
			end();
			break;
		}
		end();
	}

	@Override
	public void finish() {

	}
}