package net.nocturne.game.player;

import java.io.Serializable;
import java.util.HashMap;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.utils.Utils;

public class AuraManager implements Serializable {

	private static final long serialVersionUID = -8860530757819448608L;

	private transient Player player;
	private transient boolean warned;
	private long activation;
	private HashMap<Integer, Long> cooldowns;

	public AuraManager() {
		cooldowns = new HashMap<>();
	}

	protected void setPlayer(Player player) {
		this.player = player;
	}

	void process() {
		if (!isActivated())
			return;
		if (activation - Utils.currentTimeMillis() <= 60000 && !warned) {
			player.getPackets().sendGameMessage(
					"Your aura will deplete in 1 minute.");
			warned = true;
			return;
		}
		if (Utils.currentTimeMillis() < activation)
			return;
		deplete();
		player.getAppearence().generateAppearenceData();
	}

	public void removeAura() {
		if (isActivated())
			deplete();
	}

	private void deplete() {
		activation = 0;
		warned = false;
		player.getPackets().sendGameMessage("Your aura has depleted.");
	}

	private long getCoolDown(int aura) {
		Long coolDown = cooldowns.get(aura);
		if (coolDown == null)
			return 0;
		return coolDown;
	}

	void activate() {
		Item item = player.getEquipment().getItem(Equipment.SLOT_AURA);
		if (item == null)
			return;
		player.stopAll(false);
		int toId = getTransformIntoAura(item.getId());
		if (toId != -1) {
			player.getEquipment().getItem(Equipment.SLOT_AURA).setId(toId);
			player.getEquipment().refresh(Equipment.SLOT_AURA);
			player.getAppearence().generateAppearenceData();
		} else {
			if (activation != 0) {
				player.getPackets().sendGameMessage(
						"Your aura is already activated.");
				return;
			}
			if (Utils.currentTimeMillis() <= getCoolDown(item.getId())) {
				player.getPackets().sendGameMessage(
						"Your aura did not recharge yet.");
				return;
			}

			int tier = getTier(item.getId());
			activation = Utils.currentTimeMillis()
					+ getActivationTime(player, item.getId()) * 1000;
			cooldowns.put(item.getId(),
					activation + getCooldown(player, item.getId()) * 1000);
			player.setNextAnimation(new Animation(2231));
			player.setNextGraphics(new Graphics(getActiveGraphic(tier)));
			player.getAppearence().generateAppearenceData();
		}
	}

	private int getTransformIntoAura(int aura) {
		switch (aura) {
		case ItemIdentifiers.INFERNAL_GAZE_AURA:
			return ItemIdentifiers.INFERNAL_GAZE_AURA_TRANSFORM;
		case ItemIdentifiers.INFERNAL_GAZE_AURA_TRANSFORM:
			return ItemIdentifiers.INFERNAL_GAZE_AURA;
		case ItemIdentifiers.SERENE_GAZE_AURA:
			return ItemIdentifiers.SERENE_GAZE_AURA_TRANSFORM;
		case ItemIdentifiers.SERENE_GAZE_AURA_TRANSFORM:
			return ItemIdentifiers.SERENE_GAZE_AURA;
		case ItemIdentifiers.VERNAL_GAZE_AURA:
			return ItemIdentifiers.VERNAL_GAZE_AURA_TRANSFORM;
		case ItemIdentifiers.VERNAL_GAZE_AURA_TRANSFORM:
			return ItemIdentifiers.VERNAL_GAZE_AURA;
		case ItemIdentifiers.NOCTURNAL_GAZE_AURA:
			return ItemIdentifiers.NOCTURNAL_GAZE_AURA_TRANSFORM;
		case ItemIdentifiers.NOCTURNAL_GAZE_AURA_TRANSFORM:
			return ItemIdentifiers.NOCTURNAL_GAZE_AURA;
		case ItemIdentifiers.MYSTICAL_GAZE_AURA:
			return ItemIdentifiers.MYSTICAL_GAZE_AURA_TRANSFORM;
		case ItemIdentifiers.MYSTICAL_GAZE_AURA_TRANSFORM:
			return ItemIdentifiers.MYSTICAL_GAZE_AURA;
		case ItemIdentifiers.BLAZING_GAZE_AURA:
			return ItemIdentifiers.BLAZING_GAZE_AURA_TRANSFORM;
		case ItemIdentifiers.BLAZING_GAZE_AURA_TRANSFORM:
			return ItemIdentifiers.BLAZING_GAZE_AURA;
		case ItemIdentifiers.ABYSSAL_GAZE_AURA:
			return ItemIdentifiers.ABYSSAL_GAZE_AURA_TRANSFORM;
		case ItemIdentifiers.ABYSSAL_GAZE_AURA_TRANSFORM:
			return ItemIdentifiers.ABYSSAL_GAZE_AURA;
		case ItemIdentifiers.DIVINE_GAZE_AURA:
			return ItemIdentifiers.DIVINE_GAZE_AURA_TRANSFORM;
		case ItemIdentifiers.DIVINE_GAZE_AURA_TRANSFORM:
			return ItemIdentifiers.DIVINE_GAZE_AURA;
		default:
			return -1;
		}
	}

	void sendAuraRemainingTime() {
		if (!isActivated()) {
			long cooldown = getCoolDown(player.getEquipment().getAuraId());
			if (Utils.currentTimeMillis() <= cooldown) {
				player.getPackets().sendGameMessage(
						"Currently recharging. <col=ff0000>"
								+ getFormattedTime((cooldown - Utils
										.currentTimeMillis()) / 1000)
								+ " remaining.");
				return;
			}
			player.getPackets().sendGameMessage(
					"Currently not active. It is ready to use.");
			return;
		}
		player.getPackets().sendGameMessage(
				"Currently active. <col=00ff00>"
						+ getFormattedTime((activation - Utils
								.currentTimeMillis()) / 1000) + " remaining");
	}

	private String getFormattedTime(long seconds) {
		long minutes = seconds / 60;
		long hours = minutes / 60;
		minutes -= hours * 60;
		seconds -= (hours * 60 * 60) + (minutes * 60);
		String minutesString = (minutes < 10 ? "0" : "") + minutes;
		String secondsString = (seconds < 10 ? "0" : "") + seconds;
		return hours + ":" + minutesString + ":" + secondsString;
	}

	public void sendTimeRemaining(int aura) {
		long cooldown = getCoolDown(aura);
		if (cooldown < Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"The aura has finished recharging. It is ready to use.");
			return;
		}
		player.getPackets().sendGameMessage(
				"Currently recharging. <col=ff0000>"
						+ getFormattedTime((cooldown - Utils
								.currentTimeMillis()) / 1000) + " remaining.");
	}

	public boolean isActivated() {
		return activation > 0;
	}

	public int getAuraModelId2() {
		int aura = player.getEquipment().getAuraId();
		switch (aura) {
		case 22905: // Corruption.
			return 16449;
		case 22899: // Salvation.
			return 16465;
		case 23848: // Harmony.
			return 68605;
		case 22907: // Greater corruption.
			return 16464;
		case 22901: // Greater salvation.
			return 16524;
		case 23850: // Greater harmony.
			return 68610;
		case 22909: // SlayerMasterD corruption.
			return 16429;
		case 22903: // SlayerMasterD salvation.
			return 16450;
		case 23852: // SlayerMasterD harmony.
			return 68607;
		case 23874: // Supreme corruption.
			return 68615;
		case 23876: // Supreme salvation.
			return 68611;
		case 23854: // Supreme harmony.
			return 68613;
		default:
			return -1;
		}
	}

	public int getAuraModelId() {
		Item weapon = player.getEquipment().getItem(Equipment.SLOT_WEAPON);
		if (weapon == null)
			return 8719;
		String name = weapon.getDefinitions().getName().toLowerCase();
		if (name.contains("dagger"))
			return 8724;
		if (name.contains("whip"))
			return 8725;
		if (name.contains("2h sword") || name.contains("godsword"))
			return 8773;
		if (name.contains("sword") || name.contains("scimitar")
				|| name.contains("korasi"))
			return 8722;
		return 8719;
	}

	private int getActiveGraphic(int tier) {
		if (tier == 2)
			return 1764;
		if (tier == 3)
			return 1763;
		return 370; // default gold
	}

	public boolean hasPoisonPurge() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 20958 || aura == 22268;
	}

	public double getMagicAccuracyMultiplier() {
		if (!isActivated() || player.isCanPvp())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20962)
			return 1.03;
		if (aura == 22270)
			return 1.05;
		return 1;
	}

	public double getRangeAccuracyMultiplier() {
		if (!isActivated() || player.isCanPvp())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20967)
			return 1.03;
		if (aura == 22272)
			return 1.05;
		return 1;
	}

	public double getWoodcuttingAccuracyMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22280)
			return 1.03;
		if (aura == 22282)
			return 1.05;
		return 1;
	}

	public double getMininingAccuracyMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22284)
			return 1.03;
		if (aura == 22286)
			return 1.05;
		return 1;
	}

	public double getFishingAccuracyMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20966)
			return 1.03;
		if (aura == 22274)
			return 1.05;
		return 1;
	}

	public double getPrayerPotsRestoreMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20965)
			return 1.03;
		if (aura == 22276)
			return 1.05;
		return 1;
	}

	public double getThievingAccuracyMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22288)
			return 1.03;
		if (aura == 22290)
			return 1.05;
		return 1;
	}

	public double getChanceNotDepleteMN_WC() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22292)
			return 1.1;
		return 1;
	}

	@SuppressWarnings("ucd")
	public boolean usingEquilibrium() {
		if (!isActivated() || player.isCanPvp())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22294;
	}

	boolean usingPenance() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22300;
	}

	/**
	 * Gets the prayer experience multiplier.
	 * 
	 * @return The prayer experience multiplier.
	 */
	public double getPrayerMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		switch (aura) {
		case 22905: // Corruption.
		case 22899: // Salvation.
		case 23848: // Harmony.
			return 1.01;
		case 22907: // Greater corruption.
		case 22901: // Greater salvation.
		case 23850: // Greater harmony.
			return 1.015;
		case 22909: // SlayerMasterD corruption.
		case 22903: // SlayerMasterD salvation.
		case 23852: // SlayerMasterD harmony.
			return 1.02;
		case 23874: // Supreme corruption.
		case 23876: // Supreme salvation.
		case 23854: // Supreme harmony.
			return 1.025;
		}
		return 1.0;
	}

	/**
	 * Gets the amount of prayer points to restore (when getting 500 prayer
	 * experience).
	 * 
	 * @return The prayer restoration multiplier.
	 */
	public double getPrayerRestoration() {
		if (!isActivated())
			return 0;
		int aura = player.getEquipment().getAuraId();
		switch (aura) {
		case 22905: // Corruption.
		case 22899: // Salvation.
		case 23848: // Harmony.
			return 0.03;
		case 22907: // Greater corruption.
		case 22901: // Greater salvation.
		case 23850: // Greater harmony.
			return 0.05;
		case 22909: // SlayerMasterD corruption.
		case 22903: // SlayerMasterD salvation.
		case 23852: // SlayerMasterD harmony.
			return 0.07;
		case 23874: // Supreme corruption.
		case 23876: // Supreme salvation.
		case 23854: // Supreme harmony.
			return 0.1;
		}
		return 0;
	}

	public void checkSuccessfulHits(int damage) {
		if (!isActivated() || player.isCanPvp())
			return;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22296)
			useInspiration();
		else if (aura == 22298)
			useVampyrism(damage);
	}

	private void useVampyrism(int damage) {
		int heal = (int) (damage * 0.05);
		if (heal > 0)
			player.heal(heal);
	}

	private void useInspiration() {
		Integer atts = (Integer) player.getTemporaryAttributtes().get(
				"InspirationAura");
		if (atts == null)
			atts = 0;
		atts++;
		if (atts == 5) {
			atts = 0;
			player.getCombatDefinitions().restoreSpecialAttack(1);
		}
		player.getTemporaryAttributtes().put("InspirationAura", atts);
	}

	boolean usingWisdom() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22302;
	}

	/*
	 * return seconds
	 */
	private static int getActivationTime(Player player, int aura) {
		switch (aura) {
		case 20958:
			return 600; // 10minutes
		case 22268:
			return 1200; // 20minutes
		case 22302:
			return 1800; // 30minutes
		case 22294:
			return 7200; // 2hours
		case 20959:
			return 10800; // 3hours
		default:
			return 3600; // default 1hour
		}
	}

	private static int getCooldown(Player player, int aura) {
		switch (aura) {
		case 20962:
		case 22270:
		case 20967:
		case 22272:
		case 22280:
		case 22282:
		case 22284:
		case 22286:
		case 20966:
		case 22274:
		case 20965:
		case 22276:
		case 22288:
		case 22290:
		case 22292:
		case 22296:
		case 22298:
		case 22300:
			return 10800; // 3hours
		case 22294:
			return 14400; // 4hours
		case 20959:
		case 22302:
			return 86400; // 24hours
		default:
			return 10800; // default 3 hours - stated on
			// www.runescape.wikia.com/wiki/Aura
		}
	}

	private static int getTier(int aura) {
		switch (aura) {
		case 23874:
		case 23876:
		case 23854:
			return 4;
		case 22302:
		case 22909:
		case 22903:
		case 23852:
			return 3;
		case 22907:
		case 22901:
		case 23850:
		case 20959:
		case 22270:
		case 22272:
		case 22282:
		case 22286:
		case 22274:
		case 22276:
		case 22290:
		case 22292:
		case 22294:
		case 22296:
		case 22298:
		case 22300:
			return 2;
		default:
			return 1; // default 1
		}
	}
}