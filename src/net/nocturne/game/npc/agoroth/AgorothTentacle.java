package net.nocturne.game.npc.agoroth;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;

@SuppressWarnings("serial")
class AgorothTentacle extends NPC {

	Agoroth main;

	AgorothTentacle(int id, WorldTile tile, int spawnEmote, Agoroth main) {
		super(id, tile, -1, true, true);
		setForceAgressive(true);
		setForceMultiArea(true);
		setCantFollowUnderCombat(true);
		setNextAnimation(new Animation(spawnEmote));
		setNextGraphics(new Graphics(68));
		this.main = main;
	}

	@Override
	public void setNextFaceWorldTile(WorldTile nextFaceWorldTile) {
		return;
	}

	@Override
	public void setForceWalk(WorldTile nextFaceWorldTile) {
		return;
	}

	@Override
	public void setDirection(int direction) {
		return;
	}

	@Override
	public void forceWalkRespawnTile() {
		return;
	}

	@Override
	public boolean addWalkSteps(final int destX, final int destY,
			int maxStepsCount, boolean check) {
		return true;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
	}

	@Override
	public void processNPC() {
		if (main.isDead()) {
			sendDeath(this);
			return;
		}
		cancelFaceEntityNoCheck();
		super.processNPC();
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(1);
		List<Integer> playerIndexes = World.getRegion(getRegionId())
				.getPlayerIndexes();
		if (playerIndexes != null) {
			for (int npcIndex : playerIndexes) {
				Player player = World.getPlayers().get(npcIndex);
				if (player == null || player.isDead() || player.hasFinished()
						|| !player.isRunning()
						|| player.getAppearence().isHidden())
					continue;
				possibleTarget.add(player);
			}
		}
		return possibleTarget;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.1;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.1;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.1;
	}

}