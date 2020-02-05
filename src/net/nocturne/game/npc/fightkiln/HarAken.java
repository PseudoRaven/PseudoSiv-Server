package net.nocturne.game.npc.fightkiln;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.game.player.content.activities.minigames.FightKiln;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class HarAken extends NPC {

	private long time;
	private long spawnTentacleTime;
	private boolean underLava;
	private List<HarAkenTentacle> tentacles;

	private FightKiln Controller;

	public void resetTimer() {
		underLava = !underLava;
		if (time == 0)
			spawnTentacleTime = Utils.currentTimeMillis() + 9000;
		time = Utils.currentTimeMillis() + (underLava ? 45000 : 30000);
	}

	public HarAken(int id, WorldTile tile, FightKiln Controller) {
		super(id, tile, -1, true, true);
		this.Controller = Controller;
		tentacles = new ArrayList<HarAkenTentacle>();
	}

	@Override
	public void sendDeath(Entity source) {
		if (source instanceof Player)
			((Player) source).getTimersManager().removeTimer(
					RecordKey.FIGHT_KILN);
		increaseKills(RecordKey.FIGHT_KILN, false);
		setNextGraphics(new Graphics(2924 + getSize()));
		if (time != 0) {
			removeTentacles();
			Controller.removeNPC();
			time = 0;
		}
		super.sendDeath(source);
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		cancelFaceEntityNoCheck();
	}

	public void process() {
		if (isDead())
			return;
		if (time != 0) {
			if (time < Utils.currentTimeMillis()) {
				if (underLava) {
					Controller.showHarAken();
					resetTimer();
				} else
					Controller.hideHarAken();
			}
			if (spawnTentacleTime < Utils.currentTimeMillis())
				spawnTentacle();

		}
	}

	private void spawnTentacle() {
		tentacles.add(new HarAkenTentacle(Utils.random(2) == 0 ? 15209 : 15210,
				Controller.getTentacleTile(), this));
		spawnTentacleTime = Utils.currentTimeMillis()
				+ Utils.random(15000, 25000);
	}

	private void removeTentacles() {
		for (HarAkenTentacle t : tentacles)
			t.finish();
		tentacles.clear();
	}

	void removeTentacle(HarAkenTentacle tentacle) {
		tentacles.remove(tentacle);

	}

}
