package net.nocturne.game.player.controllers;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.map.bossInstance.BossInstanceHandler;
import net.nocturne.game.map.bossInstance.BossInstanceHandler.Boss;
import net.nocturne.game.player.MusicsManager;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.agility.Agility;
import net.nocturne.game.player.content.activities.events.GlobalEvents;
import net.nocturne.game.player.content.activities.events.GlobalEvents.Event;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.network.decoders.handlers.ObjectHandler;
import net.nocturne.utils.Utils;

public class GodWars extends Controller {

	private static final int EMPTY_SECTOR = -1;
	private static final int BANDOS = 0;
	private static final int ARMADYL = 1;
	private static final int SARADOMIN = 2;
	private static final int ZAMORAK = 3;
	private static final int ZAROS = 4;
	private static final int BANDOS_SECTOR = 4, ARMADYL_SECTOR = 5,
			SARADOMIN_SECTOR = 6, ZAMORAK_SECTOR = 7, ZAROS_PRE_CHAMBER = 8,
			ZAROS_SECTOR = 9;
	private static final WorldTile[] CHAMBER_TELEPORTS = {
			new WorldTile(2863, 5357, 0), new WorldTile(2857, 5357, 0), // bandos
			new WorldTile(2835, 5295, 0), new WorldTile(2835, 5294, 0), // armadyl
			new WorldTile(2923, 5256, 0), new WorldTile(2923, 5257, 0), // saradomin
			new WorldTile(2925, 5332, 0), new WorldTile(2925, 5333, 0), // zamorak
	};

	private int[] killCount = new int[5];
	private long lastPrayerRecharge;
	private int sector;

	@Override
	public void start() {
		sector = EMPTY_SECTOR;// We always start with this :)
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendMinigameInterface(601);
		if (sector == ZAROS_PRE_CHAMBER)
			player.getPackets().sendExecuteScriptReverse(1171);
		refresh();
	}

	public void unlockZarosComponents() {
		player.getPackets().sendHideIComponent(601, 17, false);
		player.getPackets().sendHideIComponent(601, 22, false);
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean login() {
		player.getControllerManager().startController("GodWars");
		if (inZaros(player))
			unlockZarosComponents();
		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 57225) {
			player.getDialogueManager().startDialogue("NexEntrance");
			return false;
		} else if (object.getId() == 26287 || object.getId() == 26286
				|| object.getId() == 26288 || object.getId() == 26289) {
			if (lastPrayerRecharge >= Utils.currentTimeMillis()) {
				player.getPackets()
						.sendGameMessage(
								"You must wait a total of 10 minutes before being able to recharge your prayer points.");
				return false;
			} else if (player.getAttackedByDelay() >= Utils.currentTimeMillis()) {
				player.getPackets()
						.sendGameMessage(
								"You cannot recharge your prayer while engaged in combat.");
				return false;
			}
			player.getPrayer().boost(
					player.getSkills().getLevelForXp(Skills.PRAYER) * 10);
			player.setNextAnimation(new Animation(645));
			player.getPackets().sendGameMessage(
					"Your prayer points feel rejuvinated.");
			lastPrayerRecharge = 600000 + Utils.currentTimeMillis();
			return false;
		} else if (object.getId() == 57258) {
			if (sector == ZAROS) {
				player.getPackets().sendGameMessage(
						"The door will not open in this direction.");
				return false;
			}

			boolean hasCerimonial = hasFullCerimonial(player);
			int requiredKc = getRequiredKillcount();
			if (killCount[4] >= requiredKc || hasCerimonial) {
				if (hasCerimonial)
					player.getPackets()
							.sendGameMessage(
									"The door recognises your familiarity with the area and allows you to pass through.");
				if (killCount[4] >= requiredKc)
					killCount[4] -= requiredKc;
				sector = ZAROS;
				player.addWalkSteps(2900, 5203, -1, false);
				refresh();
			} else
				player.getPackets()
						.sendGameMessage(
								"You don't have enough kills to enter the lair of Zaros.");
			return false;
		}
		if (object.getId() == 57234 && IsInZarosStairsRoom(player)) {
			player.getInterfaceManager().sendCentralInterface(1262);
		} else if (!IsInZarosStairsRoom(player)) {
			player.getPackets().sendGameMessage(
					"You aren't able to go back from this direction.");
		}
		if (object.getId() == 75089) {
			if (sector == ZAROS_PRE_CHAMBER) {
				player.getDialogueManager()
						.startDialogue("SimpleMessage",
								"You pull out your key once more but the door doesn't respond.");
				return false;
			}
			if (player.getInventory().containsItem(20120, 1)) {
				player.getPackets().sendGameMessage(
						"You flash the key in front of the door");
				player.useStairs(1133, new WorldTile(2887, 5278, 0), 1, 2,
						"...and a strange force flings you in.");
				sector = ZAROS_PRE_CHAMBER;
			} else
				player.getDialogueManager()
						.startDialogue(
								"SimpleMessage",
								"You try to push the door open, but it wont budge.... It looks like there is some kind of key hole.");
			return false;
		} else if (object.getId() == 57256) {
			unlockZarosComponents();
			player.useStairs(-1, new WorldTile(2855, 5222, 0), 1, 2,
					"You climb down the stairs.");
			return false;
		} else if (object.getId() == 57260) {
			player.useStairs(-1, new WorldTile(2887, 5276, 0), 1, 2,
					"You climb up the stairs.");
			return false;
		} else if (object.getId() == 26293) {
			player.useStairs(828, new WorldTile(2913, 3741, 0), 1, 2);
			player.getControllerManager().forceStop();
			return false;
		} else if (object.getId() == 26384) { // bandos
			if (player.getSkills().getLevel(Skills.STRENGTH) < 70) {
				player.getPackets()
						.sendGameMessage(
								"You attempt to hit the door, but realize that you are not yet experienced enough.");
				return false;
			}
			final boolean withinBandos = sector == BANDOS_SECTOR;
			if (!withinBandos)
				player.setNextAnimation(new Animation(7002));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					ObjectHandler.handleDoor(player, object, 1000);
					player.addWalkSteps(withinBandos ? 2851 : 2850, 5334, -1,
							false);
					sector = withinBandos ? EMPTY_SECTOR : BANDOS_SECTOR;
				}
			}, withinBandos ? 0 : 1);
			return false;
		} else if (object.getId() == 75463) {
			if (object.withinDistance(player, 7)) {
				final boolean withinArmadyl = sector == ARMADYL_SECTOR;
				final WorldTile tile = new WorldTile(2872, withinArmadyl ? 5280
						: 5272, 0);
				WorldTasksManager.schedule(new WorldTask() {

					int ticks = 0, projectileTicks = 0;

					@Override
					public void run() {
						ticks++;
						if (ticks == 1) {
							player.setNextAnimation(new Animation(827));
							player.setNextFaceWorldTile(tile);
							player.lock();
						} else if (ticks == 3)
							player.setNextAnimation(new Animation(385));
						else if (ticks == 5) {
							player.setNextAnimation(new Animation(16635));
						} else if (ticks == 6) {
							player.getAppearence().setHidden(true);
							projectileTicks = ticks
									+ Utils.projectileTimeToCycles(World
											.sendProjectileNew(player, tile,
													2699, 18, 18, 20, 3, 175, 0)
											.getEndTime());
							player.setNextForceMovement(new ForceMovement(
									player, 1, tile, 6,
									withinArmadyl ? ForceMovement.NORTH
											: ForceMovement.SOUTH));
						} else if (ticks == projectileTicks) {
							player.getAppearence().setHidden(false);
							player.setNextAnimation(new Animation(16672));
							player.setNextWorldTile(tile);
							player.unlock();
							player.resetReceivedHits();
							sector = withinArmadyl ? EMPTY_SECTOR
									: ARMADYL_SECTOR;
							stop();
						}
					}
				}, 0, 1);
			}
			return false;
		} else if (object.getId() == 26439) {
			if (!player.getSkills().hasLevel(Skills.HITPOINTS, 70))
				return false;
			final boolean withinZamorak = sector == ZAMORAK_SECTOR;
			final WorldTile tile = new WorldTile(2887, withinZamorak ? 5336
					: 5346, 0);
			player.lock();
			player.setNextWorldTile(object);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.setNextAnimation(new Animation(17454));
					player.setNextFaceWorldTile(tile);
					if (!withinZamorak) {
						sector = ZAMORAK_SECTOR;
					} else
						sector = EMPTY_SECTOR;
					sendInterfaces();
				}
			}, 1);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.unlock();
					player.resetReceivedHits();
					player.setNextAnimation(new Animation(-1));
					player.setNextWorldTile(tile);
				}
			}, 5);
			return false;
		} else if (object.getId() == 75462) {
			if (object.getX() == 2912
					&& (object.getY() == 5298 || object.getY() == 5299)) {
				sector = SARADOMIN_SECTOR;
				useAgilityStones(player, object,
						new WorldTile(2915, object.getY(), 0), 15239);
			} else if (object.getX() == 2914
					&& (object.getY() == 5298 || object.getY() == 5299)) {
				sector = EMPTY_SECTOR;
				useAgilityStones(player, object,
						new WorldTile(2911, object.getY(), 0), 3378);
			} else if ((object.getX() == 2919 || object.getX() == 2920)
					&& object.getY() == 5278)
				useAgilityStones(player, object, new WorldTile(object.getX(),
						5275, 0), 15239);
			else if ((object.getX() == 2920 || object.getX() == 2919)
					&& object.getY() == 5276)
				useAgilityStones(player, object, new WorldTile(object.getX(),
						5279, 0), 3378);
			return false;
			/* Bandos */
		} else if (object.getId() == 84022) {
			int index = BANDOS;
			int requiredKc = getRequiredKillcount();
			if (player.getRights() == 2 || killCount[index] >= requiredKc
					|| GlobalEvents.isActiveEvent(Event.BANDOS_KC)) {
				if (player.getX() >= 2857 && player.getX() <= 2861) {
					WorldTile tile = new WorldTile(2856, 5357, 0);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
				} else {
					WorldTile tile = new WorldTile(2857, 5357, 0);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
					killCount[index] -= requiredKc;
				}
				sector = index;
				refresh();
			} else
				player.getPackets().sendGameMessage(
						"You need to have a killcount of " + requiredKc
								+ " kills to enter.");
			return false;
		} else if (object.getId() == 26425) {
			if (player.getX() >= 2857 && player.getX() <= 2862)
				BossInstanceHandler.enterInstance(player, Boss.General_Graador);
			sector = BANDOS;
			refresh();
			return false;
			/* Armadyl */
		} else if (object.getId() == 84024) {
			int index = ARMADYL;
			int requiredKc = getRequiredKillcount();
			if (player.getRights() == 2 || killCount[index] >= requiredKc
					|| GlobalEvents.isActiveEvent(Event.ARMADYL_KC)) {
				if (player.getX() >= 2831 && player.getX() <= 2835) {
					WorldTile tile = new WorldTile(2830, 5284, 0);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
					killCount[index] -= requiredKc;
				} else {
					WorldTile tile = new WorldTile(2831, 5284, 0);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
				}
				sector = index;
				refresh();
			} else
				player.getPackets().sendGameMessage(
						"You need to have a killcount of " + requiredKc
								+ " kills to enter.");
			return false;
		} else if (object.getId() == 26426) {
			if (player.getY() >= 5291 && player.getY() <= 5294)
				BossInstanceHandler.enterInstance(player, Boss.Kree_Arra);
			sector = ARMADYL;
			refresh();
			return false;
			/* ZAMORAK */
		} else if (object.getId() == 84028) {
			int index = ZAMORAK;
			int requiredKc = getRequiredKillcount();
			if (player.getRights() == 2 || killCount[index] >= requiredKc
					|| GlobalEvents.isActiveEvent(Event.ZAMORAK_KC)) {
				if (player.getY() >= 5333 && player.getY() <= 5339) {
					WorldTile tile = new WorldTile(2925, 5340, 0);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
				} else {
					WorldTile tile = new WorldTile(2925, 5339, 0);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
					killCount[index] -= requiredKc;
				}
				sector = index;
				refresh();
			} else
				player.getPackets().sendGameMessage(
						"You need to have a killcount of " + requiredKc
								+ " kills to enter.");
			return false;
		} else if (object.getId() == 26428) {
			if (player.getY() >= 5333 && player.getY() <= 5339)
				BossInstanceHandler.enterInstance(player, Boss.Kril_Tsutsaroth);
			sector = ZAMORAK;
			refresh();
			return false;
			/* SARADOMIN */
		} else if (object.getId() == 84026) {
			int index = SARADOMIN;
			int requiredKc = getRequiredKillcount();
			if (player.getRights() == 2 || killCount[index] >= requiredKc
					|| GlobalEvents.isActiveEvent(Event.SARADOMIN_KC)) {
				if (player.getY() >= 5265 && player.getY() <= 5268) {
					WorldTile tile = new WorldTile(2919, 5264, 0);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
					killCount[index] -= requiredKc;
				} else {
					WorldTile tile = new WorldTile(2919, 5265, 0);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
				}
				sector = index;
				refresh();
			} else
				player.getPackets().sendGameMessage(
						"You need to have a killcount of " + requiredKc
								+ " kills to enter.");
			return false;
		} else if (object.getId() == 26427) {
			if (player.getY() >= 5257 && player.getY() <= 5263)
				BossInstanceHandler.enterInstance(player,
						Boss.Commander_Zilyana);
			sector = SARADOMIN;
			refresh();
			return false;
		}
		return true;
	}

	private int getRequiredKillcount() {
		if (player.getDonationManager().isHeroicDonator())
			return 0;
		else if (player.getDonationManager().isDemonicDonator())
			return 5;
		else if (player.getDonationManager().isAngelicDonator())
			return 10;
		else if (player.getDonationManager().isDivineDonator())
			return 20;
		else if (player.getDonationManager().isSupremeDonator())
			return 30;
		return 40;
	}

	@Override
	public boolean processObjectClick2(WorldObject object) {
		if (object.getId() == 26287 || object.getId() == 26286
				|| object.getId() == 26288 || object.getId() == 26289) {
			player.getPackets()
					.sendGameMessage(
							"The god's pitty you and allow you to leave the encampment.");
			player.setNextWorldTile(new WorldTile(Settings.HOME_LOCATION));
			player.getControllerManager().forceStop();
			sector += 4;
			player.resetReceivedHits();
			return false;
		}
		return true;
	}

	@Override
	public void moved() {
		if (inZaros(player)) {
			player.getPackets().sendHideIComponent(601, 17, false);
			player.getPackets().sendHideIComponent(601, 22, false);
			player.getPackets().sendIComponentText(601, 22,
					"" + killCount[ZAROS]);
			player.getVarsManager().sendVarBit(15172, killCount[ZAROS]);
		} else if (!inZaros(player)) {
			player.getPackets().sendHideIComponent(601, 17, true);
			player.getPackets().sendHideIComponent(601, 22, true);
		}
	}

	public void incrementKillCount(int index) {
		killCount[index]++;
		refresh();
	}

	public void resetKillCount(int index) {
		killCount[index] = 0;
		refresh();
		player.getPackets()
				.sendGameMessage(
						"The power of all those you slew in the dungeon drains from your body.");
	}

	private void refresh() {
		player.getPackets()
				.sendIComponentText(601, 18, "" + killCount[ARMADYL]);// arma
		player.getPackets().sendIComponentText(601, 19, "" + killCount[BANDOS]);// bando
		player.getPackets().sendIComponentText(601, 20,
				"" + killCount[SARADOMIN]);// sara
		player.getPackets()
				.sendIComponentText(601, 21, "" + killCount[ZAMORAK]);// zamy
		if (inZaros(player))
			player.getPackets().sendIComponentText(601, 22,
					"" + killCount[ZAROS]);// zamy
		player.getVarsManager().sendVarBit(15163, killCount[ARMADYL]);// arma
		player.getVarsManager().sendVarBit(15165, killCount[BANDOS]);// bando
		player.getVarsManager().sendVarBit(15162, killCount[SARADOMIN]);// sara
		player.getVarsManager().sendVarBit(15166, killCount[ZAMORAK]);// zamy
	}

	private static void useAgilityStones(final Player player,
			final WorldObject object, final WorldTile tile, final int emote) {
		if (!Agility.hasLevel(player, 70))
			return;
		player.faceObject(object);
		player.addWalkSteps(object.getX(), object.getY());
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.resetReceivedHits();// Kinda unfair if not :)
				player.useStairs(emote, tile, 7, 7 + 1);
			}
		}, 1);
	}

	@Override
	public boolean sendDeath() {
		if (sector > EMPTY_SECTOR && sector < ZAMORAK) {
			player.lock(8);
			player.stopAll();

			WorldTasksManager.schedule(new WorldTask() {
				int loop;

				@Override
				public void run() {
					if (loop == 0) {
						player.setNextAnimation(player.getDeathAnimation());
					} else if (loop == 1) {
						player.getPackets().sendGameMessage(
								"Oh dear, you have died.");
					} else if (loop == 3) {
						player.getControllerManager().forceStop();
						player.getControllerManager().startController(
								"DeathEvent",
								CHAMBER_TELEPORTS[(sector * 2) + 1],
								player.hasSkull());
					} else if (loop == 4) {
						player.getMusicsManager().playMusicEffect(
								MusicsManager.DEATH_MUSIC_EFFECT);
						stop();
					}
					loop++;
				}
			}, 0, 1);
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		player.getControllerManager().forceStop();
	}

	@Override
	public void forceClose() {
		player.getInterfaceManager().removeMinigameInterface();
	}

	public static void passGiantBoulder(Player player, WorldObject object,
			boolean liftBoulder) {
		if (player.getSkills().getLevel(
				liftBoulder ? Skills.STRENGTH : Skills.AGILITY) < 60) {
			player.getPackets().sendGameMessage(
					"You need a " + (liftBoulder ? "Agility" : "Strength")
							+ " of 60 in order to "
							+ (liftBoulder ? "lift" : "squeeze past")
							+ "this boulder.");
			return;
		}
		if (liftBoulder)
			World.sendObjectAnimation(object, new Animation(318));
		boolean isReturning = player.getY() >= 3709;
		int baseAnimation = liftBoulder ? 3725 : 3466;
		player.setRunEnergy(0);
		player.useStairs(isReturning ? baseAnimation-- : baseAnimation,
				new WorldTile(player.getX(), player.getY()
						+ (isReturning ? -4 : 4), 0), liftBoulder ? 10 : 5,
				liftBoulder ? 11 : 6, null, true);
	}

	private static boolean hasFullCerimonial(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int bootsId = player.getEquipment().getBootsId();
		int glovesId = player.getEquipment().getGlovesId();
		return !(helmId == -1 || chestId == -1 || legsId == -1 || bootsId == -1 || glovesId == -1)
				&& ItemDefinitions.getItemDefinitions(helmId).getName()
						.contains("Ancient ceremonial")
				&& ItemDefinitions.getItemDefinitions(chestId).getName()
						.contains("Ancient ceremonial")
				&& ItemDefinitions.getItemDefinitions(legsId).getName()
						.contains("Ancient ceremonial")
				&& ItemDefinitions.getItemDefinitions(bootsId).getName()
						.contains("Ancient ceremonial")
				&& ItemDefinitions.getItemDefinitions(glovesId).getName()
						.contains("Ancient ceremonial");
	}

	private static boolean inZaros(Player player) {
		if (player.getX() <= 2940 && player.getX() >= 2848
				&& player.getY() <= 5229 && player.getY() >= 5188)
			return true;
		return false;
	}

	public static boolean IsInZarosStairsRoom(Player player) {
		if (player.getX() >= 2849 && player.getX() <= 2861
				&& player.getY() <= 5228 && player.getY() >= 5217)
			return true;
		return false;
	}
}