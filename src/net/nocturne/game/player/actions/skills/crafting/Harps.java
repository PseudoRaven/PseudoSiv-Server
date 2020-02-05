package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Harps extends Action {

	/**
	 * @author: miles M
	 */

	private WorldObject object;

	public Harps(WorldObject object) {
		this.object = object;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.setNextAnimation(new Animation(25022));
		setActionDelay(player, getDelay(player));
		return true;
	}

	private int getDelay(Player player) {
		int randomTime = 20;
		int timer = randomTime - (player.getSkills().getLevel(Skills.CRAFTING));
		if (timer < 1 + randomTime)
			timer = 1 + Utils.random(randomTime);
		timer /= player.getAuraManager().getMininingAccuracyMultiplier();
		return timer;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(25021));
		player.faceObject(object);
		if (checkAll(player)) {
			if (Utils.random(220) == 0) {
				player.stopAll();
			}
			return true;
		}
		return false;
	}

	private boolean checkAll(Player player) {
		return hasLevel(player);
	}

	private boolean hasLevel(Player player) {
		if (!player.getSkills().hasLevel(Skills.CRAFTING, 75))
			return false;
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (Utils.random(3) == 0) {
			addXP(player);
		}
		return getDelay(player);
	}

	private void addXP(Player player) {
		player.getSkills().addXp(Skills.CRAFTING, 80);
		if (Utils.random(2) < 1) {
			int amount = Utils.random(7, 8);
			player.getInventory().addItem(32622, amount);
		}
	}

	@Override
	public void stop(final Player player) {
		player.getEmotesManager().setNextEmoteEnd(2400);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextAnimation(new Animation(16702));
				player.getAppearence().setRenderEmote(-1);
			}
		}, 3);
	}
}