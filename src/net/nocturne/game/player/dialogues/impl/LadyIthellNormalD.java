package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class LadyIthellNormalD extends Dialogue {

	@Override
	public void start() {
		sendNPCDialogue(
				20282,
				HAPPY_FACE,
				"It's wonderful, isn't it?",
				"The crystal spires of Prifddinas.. it's more beautiful than I",
				"could've dreamed. The stories didn't do it justice.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue("choose an option",
					"Ask about Lady Ithell and her clan..",
					"Can you make me a crystal hatchet or pickaxe?",
					"Ask about storing crystal seeds..",
					"Is there anything else I can do for you?",
					"Nothing, thanks.");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				end();
				break;
			case OPTION_2:
				sendNPCDialogue(
						20282,
						HAPPY_FACE,
						"Hmm..",
						"They're rather large tools, but in principle it'd be no"
								+ "different from attuning say a crystal halberd. It would degrade to a seed like other tools, of course.");
				stage = 3;
				break;
			case OPTION_3:
				end();
				break;
			case OPTION_4:
				end();
				break;
			case OPTION_5:
				end();
				break;
			}
			break;
		case 3:
			sendPlayerDialogue(
					NORMAL,
					"That's not good enough.",
					"I want a tool that I can store in my tool belt, never degrades, and is better than a dragon hatchet or pickaxe.");
			stage = 4;
			break;
		case 4:
			sendNPCDialogue(
					20282,
					NORMAL,
					"Well, aren't we the demanding one?",
					"It's possible in theory, I suppose. I cold coat a dragon metal item in harmonic dust, crystallise it..");
			stage = 5;
			break;
		case 5:
			sendNPCDialogue(
					20282,
					NORMAL,
					"That would maintain the tool's structural integrity. And I",
					"could use an enchantment similar to the crystal chime, to inhibit degradation");
			stage = 6;
			break;
		case 6:
			sendNPCDialogue(
					20282,
					NORMAL,
					"Use a dragon hatchet or pickaxe, and atleast 4.000 harmonic dust on me, and I'll see what I can do.");
			stage = 7;
			break;
		case 7:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
