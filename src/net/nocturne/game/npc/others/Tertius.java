package net.nocturne.game.npc.others;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;

/**
 * @author Nosz & Jason
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Tertius extends NPC {

	private List<NPC> followers = new ArrayList<NPC>();

	@SuppressWarnings("unused")
	private Iterator<NPC> iter = followers.iterator();

	private int Rorarius = 17144;
	private int Gladius = 17145;
	private int Capsarius = 17146;

	public Tertius(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCapDamage(5000);
		setLureDelay(3000);
		setForceTargetDistance(64);
		setForceMultiArea(true);
		setForceAgressive(true);
		setForceFollowClose(false);
	}

	public void spawnSpidersOne(final Player player) {
		WorldTile tile = new WorldTile(this, 3);
		WorldTile tile2 = new WorldTile(this, 3);
		WorldTile tile3 = new WorldTile(this, 3);
		NPC spider = new NPC(Rorarius, tile, -1, true, true);
		NPC spider2 = new NPC(Rorarius, tile2, -1, true, true);
		NPC spider3 = new NPC(Rorarius, tile3, -1, true, true);
		spider.setForceMultiArea(true);
		spider2.setForceMultiArea(true);
		spider3.setForceMultiArea(true);
		spider.isForceMultiArea();
		spider2.isForceMultiArea();
		spider3.isForceMultiArea();
		followers.add(spider);
		followers.add(spider2);
		followers.add(spider3);
		spider.getCombat().setTarget(player);
		spider2.getCombat().setTarget(player);
		spider3.getCombat().setTarget(player);
	}

	public void spawnSpidersTwo(final Player player) {
		WorldTile tile = new WorldTile(this, 3);

		NPC spider = new NPC(Gladius, tile, -1, true, true);
		spider.setForceMultiArea(true);
		spider.isForceMultiArea();
		followers.add(spider);
		spider.getCombat().setTarget(player);
		followers.add(spider);
	}

	public void spawnSpidersThree(final Player player) {
		WorldTile tile = new WorldTile(this, 3);
		NPC spider = new NPC(Capsarius, tile, -1, true, true);
		spider.setForceMultiArea(true);
		spider.isForceMultiArea();
		followers.add(spider);
		spider.getCombat().setTarget(player);
		followers.add(spider);

	}

	public void removeSpider() {
		for (Iterator<NPC> iter = followers.iterator(); iter.hasNext();) {
			NPC npc = iter.next();
			npc.finish();
			iter.remove();
		}
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		for (Iterator<NPC> iter = followers.iterator(); iter.hasNext();) {
			NPC npc = iter.next();
			if (npc.hasFinished() == true)
				iter.remove();
		}
		int maxhp = getMaxHitpoints();
		if (maxhp > getHitpoints() && getPossibleTargets().isEmpty())
			setHitpoints(maxhp);
		if (getPossibleTargets().isEmpty()) {
			removeSpider();
		}
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		removeSpider();

	}

}