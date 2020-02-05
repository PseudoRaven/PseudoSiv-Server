package net.nocturne.game.npc.others;

import java.util.ArrayList;

import net.nocturne.game.*;
import net.nocturne.game.item.Item;
import net.nocturne.game.map.MapInstance.Stages;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.controllers.BorkController;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class Bork extends NPC {

	private static final String[] MINION_MESSAGES = { "Hup! 2.... 3.... 4!",
			"Resistance is futile!", "We are the collective!",
			"Form a triangle!" };
	private final BorkController controller;
	private boolean spawnedMinions;
	private NPC[] borkMinion;

	public Bork(WorldTile tile, BorkController controller) {
		super(7134, tile, -1, true, true);
		setCantInteract(true);
		setDirection(Utils.getAngle(1, 0));
		setNoDistanceCheck(true);
		setForceAgressive(true);
		this.controller = controller;
	}

	public boolean isSpawnedMinions() {
		return spawnedMinions;
	}

	@Override
	public void drop() {
		int size = getSize();
		ArrayList<Item> drops = new ArrayList<>();
		drops.add(new Item(532, 1)); // big bones
		drops.add(new Item(995, 4000 + Utils.random(20000))); // coins
		drops.add(new Item(12163, 10)); // blue charm
		drops.add(new Item(12160, 17)); // crimson charm
		drops.add(new Item(12159, 5)); // green charm
		drops.add(new Item(1618, 1)); // uncut diamond
		drops.add(new Item(1620, 3)); // uncut ruby
		drops.add(new Item(1622, 4)); // uncut emerald
		drops.add(new Item(1624, 1)); // uncut sapphire
		for (Item item : drops) {
			World.addGroundItem(item, new WorldTile(getCoordFaceX(size),
					getCoordFaceY(size), getPlane()));
		}
	}

	private void setMinions() {
		borkMinion = new NPC[3];
		for (int i = 0; i < borkMinion.length; i++) {
			borkMinion[i] = World.spawnNPC(7135, new WorldTile(this, 1), -1,
					true, true);
			borkMinion[i].setNextForceTalk(new ForceTalk("For bork!"));
			borkMinion[i].setNextGraphics(new Graphics(1314));
			borkMinion[i].setTarget(controller.getPlayer());
		}
		setNextForceTalk(new ForceTalk("Destroy the intruder, my Legions!"));
		spawnedMinions = true;
		setCantInteract(false);
		setTarget(controller.getPlayer());
	}

	@Override
	public void processNPC() {
		if (borkMinion != null && Utils.random(20) == 0) {
			for (NPC n : borkMinion) {
				if (n == null || n.isDead())
					continue;
				n.setNextForceTalk(new ForceTalk(MINION_MESSAGES[Utils
						.random(MINION_MESSAGES.length)]));
			}
		}
		super.processNPC();
	}

	@Override
	public void sendDeath(Entity source) {
		if (!spawnedMinions) {
			setHitpoints(1);
			return;
		}
		controller.killBork();
		for (NPC n : borkMinion) {
			if (n == null || n.isDead())
				continue;
			n.sendDeath(source);
		}
		super.sendDeath(source);
	}

	public void spawnMinions() {
		setCantInteract(true);
		setNextForceTalk(new ForceTalk("Come to my aid, brothers!"));
		setNextAnimation(new Animation(8757));
		setNextGraphics(new Graphics(1315));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				if (controller.getStage() != Stages.RUNNING)
					return;
				setMinions();
				controller.spawnMinions();
			}

		}, 2);
	}

}
