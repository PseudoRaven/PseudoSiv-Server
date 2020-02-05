package net.nocturne.game.player.content.activities.surpriseevents;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import net.nocturne.Settings;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.World;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Logger;
import net.nocturne.utils.Utils;

public class TeamVsTeam implements SurpriseEvent {

	/**
	 * 3 minutes before the event starts.
	 */
	private static final int PREP_MINS = 5;
	/**
	 * Maximum game time.
	 */
	static final int GAME_MINS = 15;

	/**
	 * Blue cape override id.
	 */
	private static final int CAPE_BLUE = 14642;
	/**
	 * Red cape override id.
	 */
	private static final int CAPE_RED = 14641;

	/**
	 * Combat level requirement to join.
	 */
	private static final int COMBAT_LEVEL_REQ = 70;

	/**
	 * Cash for event winner.
	 */
	private static final int REWARD_CASH_FINAL = 3000000;

	/**
	 * Drop rate modifier.
	 */
	static final double REWARD_DROP_MOD_PER_KILL = 0.1;
	/**
	 * Cash reward for single kill.
	 */
	static final int REWARD_CASH_PER_KILL = 50000;

	/**
	 * Phases... 0 - Pre-start 1 - Initializing area 2 - Accepting
	 * players/waiting till game 3 - Game 4 - Shutdown
	 */
	private int phase = 0;

	/**
	 * Our arena.
	 */
	private EventArena arena;
	/**
	 * Our task.
	 */
	private TimerTask task;

	/**
	 * When we will start game and end game.
	 */
	private long startTime, endTime;

	/**
	 * Our lock
	 */
	private final Object lock = new Object();

	/**
	 * List of all players.
	 */
	private final List<Player> blue = new ArrayList<>();

	/**
	 * List of all players.
	 */
	private final List<Player> red = new ArrayList<>();

	/**
	 * Our scores.
	 */
	private int scoreRed, scoreBlue;

	@Override
	public void start() {
		if (phase != 0)
			return;
		phase = 1;
		GameExecutorManager.fastExecutor.schedule(task = new TimerTask() {
			@Override
			public void run() {
				try {
					if (phase == 2 || phase == 3)
						TeamVsTeam.this.run();
					else if (phase == 4) {
						task.cancel();
						task = null;

						GameExecutorManager.slowExecutor.execute(() -> {
							arena.destroy();
							arena = null;
						});
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0L, 1000);

		GameExecutorManager.slowExecutor.execute(() -> {
			arena = ArenaFactory.randomEventArena(true);
			arena.create();
			phase = 2;
			timerinit();
		});
	}

	private void timerinit() {
		startTime = Utils.currentTimeMillis() + (1000 * 60 * PREP_MINS);
		endTime = startTime + (1000 * 60 * GAME_MINS);
		World.sendNews(null, "Team vs Team event will start in " + PREP_MINS
				+ " minute! Talk to oracle to get there.", World.WORLD_NEWS,
				false);
	}

	private void run() {
		if (phase == 2 && canBegin())
			begin();
		else if (phase == 3 && canEnd())
			end();
	}

	private boolean canBegin() {
		synchronized (lock) {
			if (blue.size() < 1 || red.size() < 1)
				return false;
		}
		return Utils.currentTimeMillis() >= startTime;
	}

	private void begin() {
		phase = 3;
		synchronized (lock) {
			for (Player player : blue)
				player.setCanPvp(true);
			for (Player player : red)
				player.setCanPvp(true);
		}
		World.sendNews(null, "Team vs Team event has started!",
				World.WORLD_NEWS, false);
	}

	private boolean canEnd() {
		synchronized (lock) {
			return Utils.currentTimeMillis() >= endTime || red.size() < 1
					|| blue.size() < 1;
		}
	}

	private void end() {
		phase = 4;

		synchronized (lock) {
			if (scoreBlue == scoreRed) {
				World.sendNews(null,
						"Team vs Team event has ended in tie. Score: "
								+ scoreBlue, World.WORLD_NEWS, false);
			} else if (scoreBlue > scoreRed) {
				World.sendNews(null,
						"Team vs Team event has ended, blue team has won! Score: "
								+ scoreBlue + " vs " + scoreRed,
						World.WORLD_NEWS, false);

				for (Player winner : blue) {
					winner.getInventory().addItemDrop(995, REWARD_CASH_FINAL);
				}
			} else if (scoreRed > scoreBlue) {
				World.sendNews(null,
						"Team vs Team event has ended, red team has won! Score: "
								+ scoreBlue + " vs " + scoreRed,
						World.WORLD_NEWS, false);

				for (Player winner : red) {
					winner.getInventory().addItemDrop(995, REWARD_CASH_FINAL);
				}
			}

			List<Player> ps = new ArrayList<>();
			ps.addAll(red);
			ps.addAll(blue);
			for (Player player : ps)
				player.getControllerManager().forceStop();
		}

	}

	void forceleave(final Player player) {
		synchronized (lock) {
			player.setCanPvp(false);
			player.stopAll();
			player.reset();
			player.getAppearence().setHidden(false);
			player.getAppearence().setIdentityHide(false);
			player.getAppearence().setForcedCape(-1);
			player.useStairs(-1, Settings.HOME_LOCATION, 0, 1);

			blue.remove(player);
			red.remove(player);
		}
	}

	@Override
	public void tryJoin(final Player player) {
		if (phase != 2)
			return;

		if (player.getSkills().getCombatLevel() < COMBAT_LEVEL_REQ) {
			player.getPackets().sendGameMessage(
					"You must be at least level " + COMBAT_LEVEL_REQ
							+ " to join this event.");
			return;
		}

		final boolean isRed;
		synchronized (lock) {
			int redcmbs = 0, bluecmbs = 0;
			for (Player p : red)
				redcmbs += p.getSkills().getCombatLevel();
			for (Player p : blue)
				bluecmbs += p.getSkills().getCombatLevel();

			isRed = redcmbs < bluecmbs;

			if (isRed)
				red.add(player);
			else
				blue.add(player);
		}

		player.stopAll();
		player.reset();
		player.getAppearence().setHidden(false);
		player.getAppearence().setIdentityHide(true);
		player.getAppearence().setForcedCape(isRed ? CAPE_RED : CAPE_BLUE);
		player.useStairs(-1, arena.randomSpawn(), 0, 1);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getControllerManager().startController(
						"TeamVsTeamController", TeamVsTeam.this, isRed);
			}
		}, 1);
	}

	void inc(boolean red) {
		if (red)
			scoreRed++;
		else
			scoreBlue++;
	}

	public int getPhase() {
		return phase;
	}

	public long startTime() {
		return startTime;
	}

	public long endTime() {
		return endTime;
	}

	public Object getLock() {
		return lock;
	}

	public List<Player> getRed() {
		return red;
	}

	public List<Player> getBlue() {
		return blue;
	}

	int blueScore() {
		return scoreBlue;
	}

	int redScore() {
		return scoreRed;
	}

	public EventArena getArena() {
		return arena;
	}

}
