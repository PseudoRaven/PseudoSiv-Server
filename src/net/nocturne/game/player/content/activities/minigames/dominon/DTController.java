package net.nocturne.game.player.content.activities.minigames.dominon;

import net.nocturne.game.WorldObject;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.controllers.Controller;

public class DTController extends Controller {

	@Override
	public void start() {

	}

	public int getMode() {
		return (Integer) getArguments()[0];
	}

	private NPC[] bosses;
	private int onArena;

	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch(object.getId())
		{
			case 62682:
			case 26283:
			case 62690:
				player.getDominionTower().destroyArena(false, getMode());
				return false;
			case 62684:
			case 62685:
				if (bosses == null && onArena == 0) {
					bosses = player.getDominionTower().createBosses();
					onArena = 1;
					player.getDominionTower().startFight(bosses);
				}
				return false;
			case 62686:
			case 62687:
				if (bosses == null && onArena == 1) {
					onArena = 2;
					player.lock(2);
					player.stopAll();
					player.addWalkSteps(player.getX() + 1, player.getY(), 1, false);
				}
				return false;
			case 62691:
				if (getMode() == DominionTower.ENDURANCE) {
					player.getPackets().sendGameMessage(
							"You can't bank on endurance mode.");
					return false;
				}
				player.getDialogueManager().startDialogue("ChooseBank");
				return false;
			case 62689:
				if (player.getDominionTower().getProgress() == 0) {
					player.getDominionTower().openModes();
					return false;
				}
				if (getMode() == DominionTower.ENDURANCE)
					player.getDominionTower().openEnduranceMode();
				else
					player.getDominionTower().openClimberMode();
				return false;
		}
		return true;
	}

	private int getPlayerHPPercentage( ) {
		return player.getHitpoints() * 100 / player.getMaxHitpoints();
	}

	private int getBossesHPPercentage( ) {
		int totalHp = 0;
		int totalMaxHp = 0;
		for (NPC n : bosses) {
			if (!n.hasFinished())
				totalHp += n.getHitpoints();
			totalMaxHp += n.getMaxHitpoints();
		}
		return totalHp * 100 / totalMaxHp;
	}

	private int playerHp, bossHp;

	@Override
	public boolean sendDeath() {
		if (bosses != null) {
			for (NPC n : bosses)
				n.finish();
		}
		bosses = null;
		player.getDominionTower().loss(getMode());
		return false;
	}

	@Override
	public void process() {
		if (bosses != null) {
			if (bossesDead()) {
				bosses = null;
				player.getDominionTower().win(getMode());
				return;
			}
			int playerHp = getPlayerHPPercentage();
			int bossHp = getBossesHPPercentage();

			if (bossHp != this.bossHp) {
				this.bossHp = bossHp;
				player.getPackets().sendCSVarInteger(1672, bossHp);
			}
			if (playerHp != this.playerHp) {
				this.playerHp = playerHp;
				player.getPackets().sendCSVarInteger(1673, playerHp);
			}
		}
	}

	private boolean bossesDead( ) {
		for (NPC n : bosses)
			if (!n.hasFinished())
				return false;
		return true;
	}

	@Override
	public void sendInterfaces() {
		if (bosses != null) {
			player.getInterfaceManager().sendMinigameInterface(1159);
			player.getPackets().sendHideIComponent(1159, 14, true);
			player.getPackets()
					.sendIComponentText(
							1159,
							32,
							getMode() == DominionTower.CLIMBER ? "Climber"
									: "Endurance"
											+ ". Floor "
											+ (player.getDominionTower()
													.getProgress() + 1));
			player.getPackets().sendIComponentText(1159, 40,
					player.getDisplayName());
			player.getPackets()
					.sendIComponentText(
							1159,
							41,
							/* player.getDominionTower().getNextBoss().getName() */player
									.getDominionTower().getMap()
									.getStringValue(2095));
		}
	}

	@Override
	public void magicTeleported(int type) {
		if (type != Magic.OBJECT_TELEPORT)
			player.getDominionTower().destroyArena(false, getMode());
	}

	@Override
	public boolean login() {
		if (player.isDead() || getArguments().length == 0)
			return true;
		player.getDominionTower().selectBoss();
		player.getDominionTower().createArena(getMode());
		return false;
	}

	@Override
	public boolean logout() {
		player.getDominionTower().destroyArena(true, getMode());
		return false;
	}

}
