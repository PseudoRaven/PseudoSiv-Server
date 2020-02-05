package net.nocturne.game.player.actions.skills.mining;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

public final class Mining extends MiningBase {

	public enum RockDefinitions {

		SOFT_CLAY(1, 5, 1761, 15, 1, 11552, 5, 0),

		CLAY_ORE(1, 5, 434, 10, 1, 11552, 5, 0),

		COPPER_ORE(1, 17.5, 436, 10, 1, 11552, 5, 0),

		TIN_ORE(1, 17.5, 438, 15, 1, 11552, 5, 0),

		BLURITE_ORE(10, 17.5, 668, 15, 1, 11552, 7, 0),

		IRON_ORE(15, 35, 440, 15, 1, 11552, 10, 0),

		SANDSTONE_ORE(35, 30, 6971, 30, 1, 11552, 10, 0),

		SILVER_ORE(20, 40, 442, 25, 1, 11552, 20, 0),

		COAL_ORE(30, 50, 453, 50, 10, 11552, 30, 0),

		GRANITE_ORE(45, 50, 6979, 50, 10, 11552, 20, 0),

		GOLD_ORE(40, 60, 444, 80, 20, 11554, 40, 0),

		MITHRIL_ORE(55, 80, 447, 100, 20, 11552, 60, 0),

		ADAMANT_ORE(70, 95, 449, 130, 25, 11552, 180, 0),

		RUNITE_ORE(85, 125, 451, 150, 30, 11552, 360, 0),

		SEREN_STONE(89, 296.7, 32262, 160, 30, -1, -1, -1),

		LRC_COAL_ORE(77, 50, 453, 50, 10, -1, -1, -1),

		LRC_GOLD_ORE(80, 60, 444, 40, 10, -1, -1, -1),

		RED_SANDSTONE(81, 70, 23194, 50, 10, -1, -1, -1),
		
		CRYSTAL_SANDSTONE(81, 60, 32847, 5, 10, -1, 20, 5),

		GEM_ROCK(40, 65, -1, 20, 20, 11152, 60, 0);

		private int level;
		private double xp;
		private int oreId;
		private int oreBaseTime;
		private int oreRandomTime;
		private int emptySpot;
		private int respawnDelay;
		private int randomLifeProbability;

		RockDefinitions(int level, double xp, int oreId,
				int oreBaseTime, int oreRandomTime, int emptySpot,
				int respawnDelay, int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.oreId = oreId;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
			this.emptySpot = emptySpot;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getOreId() {
			return oreId;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

		public int getEmptyId() {
			return emptySpot;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int randomLifeProbability() {
			return randomLifeProbability;
		}
	}

	private WorldObject rock;
	private RockDefinitions definitions;
	private PickAxeDefinitions axeDefinitions;

	public Mining(WorldObject rock, RockDefinitions definitions) {
		this.rock = rock;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		axeDefinitions = getPickAxeDefinitions(player, false);
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage("You swing your pickaxe at the rock.", true);
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(Player player) {
		int summoningBonus = getSummoningBonus( player, 0 );
		int mineTimer = definitions.getOreBaseTime()
				- (player.getSkills().getLevel(Skills.MINING) + summoningBonus)
				- Utils.random(axeDefinitions.getPickAxeTime());
		if (mineTimer < 1 + definitions.getOreRandomTime())
			mineTimer = 1 + Utils.random(definitions.getOreRandomTime());
		mineTimer /= player.getAuraManager().getMininingAccuracyMultiplier();
		return mineTimer;
	}

	private boolean checkAll(Player player) {
		if (axeDefinitions == null) {
			player.getPackets()
					.sendGameMessage(
							"You do not have a pickaxe or do not have the required level to use the pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of " + definitions.getLevel()
							+ " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(axeDefinitions.getAnimationId()));
		return checkRock(player);
	}

	private boolean usedDeplateAurora;

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		if (definitions == RockDefinitions.RUNITE_ORE)
			player.getCompCapeManager().increaseRequirement(Requirement.RUNITE_MINING, 1);
		if (definitions.getEmptyId() != -1) {
			if (!usedDeplateAurora
					&& (1 + Math.random()) < player.getAuraManager()
							.getChanceNotDepleteMN_WC()) {
				usedDeplateAurora = true;
			} else if (Utils.random(definitions.randomLifeProbability()) == 0) {
				World.spawnObjectTemporary(
						new WorldObject(definitions.getEmptyId(), rock
								.getType(), rock.getRotation(), rock.getX(),
								rock.getY(), rock.getPlane()),
						definitions.respawnDelay * 600, false, true);
				player.setNextAnimation(new Animation(-1));
				return -1;
			}
		}
		if (definitions.getOreId() == 32262) {
			if (Utils.random(25) == 0)
				player.stopAll();
		}
		if (!player.getInventory().hasFreeSlots()
				&& definitions.getOreId() != -1) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getMiningDelay(player);
	}

	private void addOre(Player player) {
		double xpBoost = 0;
		int idSome = 0;
		if (definitions == RockDefinitions.GRANITE_ORE) {
			idSome = Utils.random(2) * 2;
			if (idSome == 2)
				xpBoost += 10;
			else if (idSome == 4)
				xpBoost += 25;
		} else if (definitions == RockDefinitions.SANDSTONE_ORE) {
			idSome = Utils.random(3) * 2;
			xpBoost += idSome / 2 * 10;
		} else if (definitions == RockDefinitions.RED_SANDSTONE) {
			if (player.getRedStoneDelay() >= Utils.currentTimeMillis()) {
				player.getPackets()
						.sendGameMessage(
								"It seems that there is no remaining ore, check again in twelve hours.");
				stop(player);
				return;
			}
			player.increaseRedStoneCount();
			if (player.getRedStoneCount() >= (player.getDonationManager().isSupremeDonator()
					|| player.getDonationManager().isDivineDonator() || player.getDonationManager().isAngelicDonator() ? 225
						: player.getDonationManager().isLegendaryDonator() ? 150 : 75)) {
				player.resetRedStoneCount();
				player.setRedStoneDelay(3600000 * 24); // 12 hours
				player.getVarsManager().sendVarBit(10133, 26);
			} else if (player.getRedStoneCount() == 125)
				player.getVarsManager().sendVarBit(10133, 25);
		} else if (definitions == RockDefinitions.CRYSTAL_SANDSTONE) {
			if (player.getRedStoneDelay() >= Utils.currentTimeMillis()) {
				player.getPackets()
						.sendGameMessage(
								"It seems that there is no remaining ore, check again in twelve hours.");
				stop(player);
				return;
			}
			player.increaseCrystalStoneCount();
			if (player.getRedStoneCount() >= (player.getDonationManager().isSupremeDonator()
					|| player.getDonationManager().isDivineDonator() || player.getDonationManager().isAngelicDonator() ? 225
						: player.getDonationManager().isLegendaryDonator() ? 100 : 50)) {
				player.resetCrystalStoneCount();
				player.setCrystalStoneDelay(3600000 * 24); // 12 hours
			}
		} else if (definitions == RockDefinitions.GEM_ROCK) {
			final int gem = Utils.random(5000);
			if (isAtPriff(player)) {
				if (gem >= 0 && gem <= 2)
					RockDefinitions.GEM_ROCK.oreId = 6571;
				else if (gem >= 3 && gem <= 379)
					RockDefinitions.GEM_ROCK.oreId = 1617;
				else if (gem >= 380 && gem <= 897)
					RockDefinitions.GEM_ROCK.oreId = 1619;
				else if (gem >= 898 && gem <= 2827)
					RockDefinitions.GEM_ROCK.oreId = 1621;
				else if (gem >= 2828 && gem <= 5000)
					RockDefinitions.GEM_ROCK.oreId = 1623;	
			} else {
				if (gem >= 0 && gem <= 355)
					RockDefinitions.GEM_ROCK.oreId = 1617;
				else if (gem >= 356 && gem <= 370)
					RockDefinitions.GEM_ROCK.oreId = 1619;
				else if (gem >= 371 && gem <= 420)
					RockDefinitions.GEM_ROCK.oreId = 1621;
				else if (gem >= 421 && gem <= 700)
					RockDefinitions.GEM_ROCK.oreId = 1623;
				else if (gem >= 701 && gem <= 1270)
					RockDefinitions.GEM_ROCK.oreId = 1629;
				else if (gem >= 1271 && gem <= 2190)
					RockDefinitions.GEM_ROCK.oreId = 1627;	
				else if (gem >= 2191 && gem <= 5000)
					RockDefinitions.GEM_ROCK.oreId = 1625;	
			}
		}
		double totalXp = definitions.getXp() * 1.45 + xpBoost;
		if (hasMiningSuit(player))
			totalXp *= 1.025;
		player.getSkills().addXp(Skills.MINING, totalXp / 2);
		handleHarmonizedRock(player);
		if (definitions.getOreId() != -1) {
			player.getGamePointManager().increaseGamePoints((int) totalXp / 10);
			player.getGamePointManager().addGamePointItem(new Item(definitions.getOreId() + idSome, 1));
			if (player.getClanManager() != null && player.getClanManager().getClan() != null)
				player.getClanManager().getClan().increaseGatheredResources();
			if ((Utils.currentTimeMillis() - player.getLastStarSprite()) <= 15 * 60 * 1000)
				player.getInventory().addItem(definitions.getOreId() + idSome,
						1);
			String oreName = ItemDefinitions
					.getItemDefinitions(definitions.getOreId() + idSome)
					.getName().toLowerCase();
			handlePet(player);
			player.getPackets().sendGameMessage(
					"You mine some " + oreName + ".", true);
			player.getSkillTasks().handleTask(definitions, 1);
			if (player.getDailyTask() != null)
				player.getDailyTask().incrementTask(player, 3,
						definitions.getOreId(), Skills.MINING);
		}
	}
	
	private void handleHarmonizedRock(Player player) {
		int amount = 0;
		if (isAtPriff(player)) {
			if (World.harmonizedRock[0] && definitions == RockDefinitions.COAL_ORE || World.harmonizedRock[1] && definitions == RockDefinitions.MITHRIL_ORE)
				amount = Utils.random(1, 3);
			else if (World.harmonizedRock[2] && definitions == RockDefinitions.ADAMANT_ORE | World.harmonizedRock[3] && definitions == RockDefinitions.RUNITE_ORE)
				amount = Utils.random(1, 2);
			//player.getGamePointManager().addGamePointItem(new Item(33262, amount));TODO idk what the correct id needs to be
		}
	}
	
	private void handlePet(Player player) {
		if (Utils.random(5000) == 1){
			player.getBank().addItem(31659, 1,true);
			player.getPackets().sendGameMessage(Color.WHITE, "You have received White Royal Rhino the pet! It has been transferred to your bank! ");
		}
		if (Utils.random(5000) == 1){
			player.getBank().addItem(31662, 1,true);
			player.getPackets().sendGameMessage(Color.WHITE, "You have received Black Royal Rhino the pet! It has been transferred to your bank! ");
		}
	}
	
	private static boolean isAtPriff(Player player) {
		WorldTile tile = player.getMiddleWorldTile();
		return ((tile.getX() > 2210 && tile.getX() < 2250) && tile.getY() > 3310 && tile.getY() < 3340);
	}

	private boolean checkRock(Player player) {
		return World.containsObjectWithId(rock, rock.getId());
	}
}