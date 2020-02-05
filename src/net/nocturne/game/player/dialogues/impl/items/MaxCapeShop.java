package net.nocturne.game.player.dialogues.impl.items;

import net.nocturne.game.player.*;
import net.nocturne.game.item.Item;
import net.nocturne.game.World;
import net.nocturne.game.player.dialogues.Dialogue;

public class MaxCapeShop extends Dialogue {

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(3705, HAPPY,
				"Would you like to buy the maxcapes for 2,670,000?");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes that seems fair!",
					"Thats way too expensive!");
			stage = 0;
			break;
		case 0:
			switch (componentId) {
			case OPTION_1:
				if (player.getInventory().getCoinsAmount() >= 2670000) {
					player.getInventory().removeItemMoneyPouch(
							new Item(995, 2670000));
					player.getInventory().addItem(20768, 1);
					player.getInventory().addItem(20767, 1);
					player.getInventory().addItem(32151, 1);
				} else {
					player.getPackets().sendGameMessage(
							"You do not have enough coins to buy my gear.");
					end();
					break;
				}
				for (Player players : World.getPlayers()) {
					if (player.isAnIronMan())
						players.getPackets().sendGameMessage(
								"<img=6></img><shad=FF0000> News: "
										+ player.getIronmanTitle(false)
										+ " <shad=FF0000>"
										+ player.getDisplayName()
										+ " has been awarded the Max Cape!");
					else
						players.getPackets().sendGameMessage(
								"<img=6></img><shad=FF0000> News: "
										+ player.getDisplayName()
										+ " has been awarded the Max Cape!");
				}

				end();
				break;
			case OPTION_2:
				sendPlayerDialogue(NORMAL,
						"Okay, I'll be here if you change your mind.");
				stage = 1;
				end();
				break;
			}
		}

	}

	@Override
	public void finish() {

	}

}