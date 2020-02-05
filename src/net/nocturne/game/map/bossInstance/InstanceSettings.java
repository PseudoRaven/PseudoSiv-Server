package net.nocturne.game.map.bossInstance;

import java.io.Serializable;

import net.nocturne.game.map.bossInstance.BossInstanceHandler.Boss;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class InstanceSettings implements Serializable {

	private static final long serialVersionUID = 3082211665373930652L;

	private Boss boss;
	private int minCombat, maxPlayers, spawnSpeed, protection;
	private static boolean practiceMode;

	private boolean hardMode;

	private long creationTime;

	public InstanceSettings(Boss boss, int maxPlayers, int minCombat,
			int spawnSpeed, int protection, boolean practiceMode,
			boolean hardMode) {
		this.boss = boss;
		this.minCombat = minCombat;
		this.spawnSpeed = spawnSpeed;
		this.protection = protection;
		this.practiceMode = practiceMode;
		this.hardMode = hardMode;
	}

	public InstanceSettings(Boss boss) {
		this.boss = boss;
	}

	public void setPracticeMode(boolean practiceMode) {
		this.practiceMode = practiceMode;
	}

	public static void switchPractiseMode(Player player) {
		player.getVarsManager().sendVarBit(27142, practiceMode ? 0 : 1);
		practiceMode = !practiceMode;
	}

	public void setHardMode(boolean hardMode) {
		this.hardMode = hardMode;
	}

	public void setMinCombat(int minCombat) {
		this.minCombat = minCombat;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public void setSpawnSpeed(int spawnSpeed) {
		this.spawnSpeed = spawnSpeed;
	}

	public void setProtection(int protection) {
		this.protection = protection;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public boolean isHardMode() {
		return hardMode;
	}

	public boolean isPracticeMode() {
		return practiceMode;
	}

	public int getMinCombat() {
		return minCombat;
	}

	public int getProtection() {
		return protection;
	}

	public int getSpawnSpeed() {
		return spawnSpeed;
	}

	public Boss getBoss() {
		return boss;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	/*
	 * in milliseconds
	 */
	public long getTimeRemaining() {
		long diff = (Utils.currentTimeMillis() - creationTime);
		long timeRemaining = 60 * 60 * 1000 - diff;
		return timeRemaining < 0 ? 0 : timeRemaining;
	}

	public boolean hasTimeRemaining() {
		return getTimeRemaining() > 0;
	}
}
