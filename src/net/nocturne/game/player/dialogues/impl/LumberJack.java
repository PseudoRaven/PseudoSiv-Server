package net.nocturne.game.player.dialogues.impl;

import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.game.TemporaryAtributtes.Key;
import net.nocturne.game.player.content.activities.distractions.EvilTree;
import net.nocturne.game.player.dialogues.Dialogue;

public class LumberJack extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendPlayerDialogue(9827, "Nothing much, just traveling around...");
			break;
		case 0:
			end();
			break;
		case 1:
			stage = 0;
			EvilTree.processReward(player);
			sendPlayerDialogue(9827, "Thank you!");
			break;
		}
	}

	@Override
	public void start() {
		int npcId = (Integer) parameters[0];
		if (EvilTree.tree != null
				&& player.getTemporaryAttributtes().contains(
						Key.EVIL_TREE_DAMAGE)) {
			if (EvilTree.tree.getHealth() <= 0) {
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Thanks for helping me defeat this foul creature!",
						"Please take this reward for helping me!" }, IS_NPC,
						npcId, 9827);
				stage = 1;
			} else {
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Please, you must help me defeat this Evil Tree!" },
						IS_NPC, npcId, 9827);
				stage = 0;
			}
		} else {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Howdy, what are you doing around here?" }, IS_NPC,
					npcId, 9827);
			stage = -1;
		}
	}

}
