package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class LadyIthellD extends Dialogue {

	@Override
	public void start() {
		sendItemDialogue(32625, "",
				"Lady Ithell gives you a free attuned crystal seed with which to"
						+ " show your dedication to the elves.");
		player.getInventory().addItem(32625, 1);
		stage = 1;

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendItemDialogue(
					32625,
					"",
					"If you destroy this seed you will have to wait a week before you can claim a replacement. Once you"
							+ " complete the Xena-Phile task you will not be able to claim another.");
			stage = 2;
			break;
		case 2:
			sendNPCDialogue(20282, HAPPY_FACE,
					"I am looking forward to seeing what you create.");
			stage = 3;
			break;
		case 3:
			sendOptionsDialogue("choose an option:",
					"Ask about Lady Ithell and here clan..",
					"Can you make me a crystal hatchet or pickaxe?",
					"Is there anything I can do for you?", "Nothing, thanks.");
			stage = 4;
			break;

		case 4:
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
				stage = 5;
				break;
			case OPTION_3:
				end();
				break;
			case OPTION_4:
				end();
				break;
			}
			break;
		case 5:
			sendPlayerDialogue(
					NORMAL,
					"That's not good enough.",
					"I want a tool that I can store in my tool belt, never degrades, and is better than a dragon hatchet or pickaxe.");
			stage = 6;
			break;
		case 6:
			sendNPCDialogue(
					20282,
					NORMAL,
					"Well, aren't we the demanding one?",
					"It's possible in theory, I suppose. I cold coat a dragon metal item in harmonic dust, crystallise it..");
			stage = 7;
			break;
		case 7:
			sendNPCDialogue(
					20282,
					NORMAL,
					"That would maintain the tool's structural integrity. And I",
					"could use an enchantment similar to the crystal chime, to inhibit degradation");
			stage = 8;
			break;
		case 8:
			sendNPCDialogue(
					20282,
					NORMAL,
					"Use a dragon hatchet or pickaxe, and atleast 4.000 harmonic dust on me, and I'll see what I can do.");
			stage = 9;
			break;
		case 9:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}