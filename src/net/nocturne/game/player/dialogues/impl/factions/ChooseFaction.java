package net.nocturne.game.player.dialogues.impl.factions;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.dialogues.Dialogue;

/**
 * 
 * @author Frostbite<Abstract>
 * @contact<skype;frostbitersps><email;frostbitersps@gmail.com>
 */

public class ChooseFaction extends Dialogue {

	@Override
	public void start() {
		options("Choose a Faction", "Ally with Zamorak", "Brace upon Saradomin");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			switch (componentId) {
			/**
			 * Zamorak Selection
			 */
			case OPTION_1:
				player.isZamorak = true;
				player.setNextWorldTile(player.ZAMORAK_TILE);
				player.setChosenFaction(true);
				player.unlock();
				end();
				break;
			/**
			 * Saradomin Selection
			 */
			case OPTION_2:
				player.isZamorak = false;
				player.setNextWorldTile(Player.SARADOMIN_TILE);
				player.setChosenFaction(true);
				player.unlock();
				end();
				break;
			}
			break;

		}
	}

	@Override
	public void finish() {
		player.unlock();
	}

}
