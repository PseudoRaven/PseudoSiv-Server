package net.nocturne.game.player.actions.skills.runecrafting;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.GamePointManager.GPR;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

public final class Runecrafting {

	public enum RunecraftingTypes {
		REGULAR()
	}

	private final static int[] LEVEL_REQ = { 1, 25, 50, 75 };
	private final static int[] RUNES = { 556, 558, 555, 557, 554, 559, 564,
			562, 9075, 561, 563, 560, 565, 566 };
	private final static int[] CRAFT_REQ = { 1, 1, 5, 9, 14, 20, 27, 35, 40,
			44, 54, 65, 77, 90 };
	private final static double[] RUNES_XP = { 5.0, 5.5, 6.0, 6.5, 7.0, 7.5,
			8.0, 8.5, 8.7, 9.0, 9.5, 10.0, 10.5, 12.0 };
	private final static int RUNE_ESSENCE = 1436;
	private final static int PURE_ESSENCE = 7936;
	public final static int AIR_TIARA = 5527;
	public final static int MIND_TIARA = 5529;
	public final static int WATER_TIARA = 5531;
	public final static int BODY_TIARA = 5533;
	public final static int EARTH_TIARA = 5535;
	public final static int FIRE_TIARA = 5537;
	private final static int COSMIC_TIARA = 5539;
	private final static int NATURE_TIARA = 5541;
	private final static int CHAOS_TIARA = 5543;
	private final static int LAW_TIARA = 5545;
	private final static int DEATH_TIARA = 5547;
	private final static int BLOOD_TIARA = 5549;
	private final static int SOUL_TIARA = 5551;
	public final static int OMNI_TIARA = 13655;

	private static void enterAltar(Player player, WorldTile dest) {
		player.getPackets().sendGameMessage(
				"A mysterious force grabs hold of you.");
		player.useStairs(-1, dest, 0, 1);
	}

	public static void enterAirAltar(Player player) {
		enterAltar(player, new WorldTile(2841, 4829, 0));
	}

	public static void enterMindAltar(Player player) {
		enterAltar(player, new WorldTile(2792, 4827, 0));
	}

	public static void enterWaterAltar(Player player) {
		enterAltar(player, new WorldTile(3482, 4838, 0));
	}

	public static void enterEarthAltar(Player player) {
		enterAltar(player, new WorldTile(2655, 4830, 0));
	}

	public static void enterFireAltar(Player player) {
		enterAltar(player, new WorldTile(2574, 4848, 0));
	}

	public static void enterBodyAltar(Player player) {
		enterAltar(player, new WorldTile(2521, 4834, 0));
	}

	public static void enterLawAltar(Player player) {
		enterAltar(player, new WorldTile(2464, 4818, 0));
	}

	public static void enterCosmicAltar(Player player) {
		enterAltar(player, new WorldTile(2140, 4834, 0));
	}

	public static void enterBloodAltar(Player player) {
		enterAltar(player, new WorldTile(2466, 4891, 1));
	}

	public static void enterDeathAltar(Player player) {
		enterAltar(player, new WorldTile(2207, 4832, 0));
	}

	public static void enterChaosAltar(Player player) {
		enterAltar(player, new WorldTile(2277, 4838, 0));
	}

	public static void enterNatureAltar(Player player) {
		enterAltar(player, new WorldTile(2403, 4836, 0));
	}

	//
	public static final int[] OBJECTS = { 2478, 2481, 2482, 2480, 2483, 2479,
			30624, 2487, 2484, 2488, 2485, 2478, 2486 };
	private static final int[] TIARA = { AIR_TIARA, EARTH_TIARA, FIRE_TIARA,
			WATER_TIARA, BODY_TIARA, MIND_TIARA, BLOOD_TIARA, CHAOS_TIARA,
			COSMIC_TIARA, DEATH_TIARA, LAW_TIARA, SOUL_TIARA, NATURE_TIARA };

	public static void infuseTiara(Player player, int index) {// up to blood
		// tiara
		int tiaraId = TIARA[index];
		int talismanId = (index * 2) + 1438;
		if (player.getInventory().containsItem(5525, 1)
				&& player.getInventory().containsItem(talismanId, 1)) {
			player.getInventory().deleteItem(new Item(talismanId, 1));
			player.getInventory().deleteItem(new Item(5525, 1));
			player.getInventory().addItem(new Item(tiaraId));
			player.getPackets().sendGameMessage(
					"You infuse the tiara with the power of your talisman.");
		}
	}

	public static void craftEssence(Player player, int rune, int level,
			double experience, boolean pureEssOnly, int... multipliers) {
		int actualLevel = player.getSkills().getLevel(Skills.RUNECRAFTING);
		if (!player.getSkills().hasLevel(Skills.RUNECRAFTING, level))
			return;
		int runes = player.getInventory().getItems().getNumberOf(PURE_ESSENCE);

		if (rune == 556 && actualLevel >= 70) {
			int shards = player.getInventory().getAmountOf(21774);
			if (shards != 0) {
				if (shards > runes)
					shards = runes;
				else if (shards < runes)
					runes = shards;
				rune = 21773;
				player.getInventory().deleteItem(21774, runes);
			}
		}

		if (runes > 0)
			player.getInventory().deleteItem(PURE_ESSENCE, runes);
		if (!pureEssOnly) {
			int normalEss = player.getInventory().getItems()
					.getNumberOf(RUNE_ESSENCE);
			if (normalEss > 0) {
				player.getInventory().deleteItem(RUNE_ESSENCE, normalEss);
				runes += normalEss;
			}
		}
		if (runes == 0) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You don't have " + (pureEssOnly ? "pure" : "rune")
							+ " essence.");
			return;
		}
		double totalXp = experience * runes;
		if (hasRcingSuit(player))
			totalXp *= 1.025;
		player.getGamePointManager().increaseGamePoints((int) (totalXp / 4));
		player.getSkills().addXp(Skills.RUNECRAFTING, totalXp);
		player.getSkillTasks().handleTask(RunecraftingTypes.REGULAR, runes);
		for (int i = multipliers.length - 2; i >= 0; i -= 2) {
			if (actualLevel >= multipliers[i]) {
				runes *= multipliers[i + 1];
				break;
			}
		}
		player.setNextGraphics(new Graphics(186));
		player.setNextAnimation(new Animation(791));
		handlePet(player);
		player.lock(5);
		if (player.getGamePointManager().hasGamePointsReward(GPR.MORE_SKILLS))
			runes *= 1.3;
		player.getInventory().addItem(rune, runes);
		if (player.getClanManager() != null
				&& player.getClanManager().getClan() != null)
			player.getClanManager().getClan().increaseGatheredResources(runes);
		player.getPackets().sendGameMessage(
				"You bind the temple's power into "
						+ ItemDefinitions.getItemDefinitions(rune).getName()
								.toLowerCase() + "s.");
	}

	private static void handlePet(Player player) {
		if (Utils.random(1000) == 1) {
			player.getInventory().addItem(ItemIdentifiers.KIRIN, 1);
			player.getPackets()
					.sendGameMessage(Color.WHITE,
							"You have received Kirin the pet! It has been transferred to your bank!");
		}
	}

	static boolean hasRcingSuit(Player player) {
		return player.getEquipment().getHatId() == 21485
				&& player.getEquipment().getChestId() == 21484
				&& player.getEquipment().getLegsId() == 21486
				&& player.getEquipment().getBootsId() == 21487;
	}

	public static void locate(Player p, int xPos, int yPos) {
		String x = "";
		String y = "";
		int absX = p.getX();
		int absY = p.getY();
		if (absX >= xPos)
			x = "west";
		if (absY > yPos)
			y = "South";
		if (absX < xPos)
			x = "east";
		if (absY <= yPos)
			y = "North";
		p.getPackets().sendGameMessage(
				"The talisman pulls towards " + y + "-" + x + ".");
	}

	private static final int[] POUCH_SIZE = { 3, 6, 9, 12 };

	public static void fillPouch(Player p, int i) {
		if (i < 0)
			return;
		if (LEVEL_REQ[i] > p.getSkills().getLevel(Skills.RUNECRAFTING)) {
			p.getPackets().sendGameMessage(
					"You need a runecrafting level of " + LEVEL_REQ[i]
							+ " to fill this pouch.");
			return;
		}
		int essenceToAdd = POUCH_SIZE[i] - p.getPouches()[i];
		if (essenceToAdd > p.getInventory().getItems()
				.getNumberOf(PURE_ESSENCE))
			essenceToAdd = p.getInventory().getItems()
					.getNumberOf(PURE_ESSENCE);
		if (essenceToAdd > POUCH_SIZE[i] - p.getPouches()[i])
			essenceToAdd = POUCH_SIZE[i] - p.getPouches()[i];
		if (essenceToAdd > 0) {
			p.getInventory().deleteItem(PURE_ESSENCE, essenceToAdd);
			p.getPouches()[i] += essenceToAdd;
		}
		if (!p.getInventory().containsOneItem(PURE_ESSENCE)) {
			p.getPackets().sendGameMessage(
					"You don't have any pure essence with you.", false);
			return;
		}
		if (essenceToAdd == 0) {
			p.getPackets().sendGameMessage("Your pouch is full.", false);
		}
	}

	public static void emptyPouch(Player p, int i) {
		if (i < 0)
			return;
		int toAdd = p.getPouches()[i];
		if (toAdd > p.getInventory().getFreeSlots())
			toAdd = p.getInventory().getFreeSlots();
		if (toAdd > 0) {
			p.getInventory().addItem(PURE_ESSENCE, toAdd);
			p.getPouches()[i] -= toAdd;
		}
		if (toAdd == 0) {
			p.getPackets().sendGameMessage(
					"Your pouch has no essence left in it.", false);
		}
	}

	public static void craftZMIAltar(Player player) {
		int level = player.getSkills().getLevel(Skills.RUNECRAFTING);
		int runes = player.getInventory().getItems()
				.getNumberOf(ItemIdentifiers.PURE_ESSENCE);
		int length = 0;
		for (int aCRAFT_REQ : CRAFT_REQ) {
			if (aCRAFT_REQ > level) {
				break;
			}
			length++;
		}
		int[] possibleRunes = new int[length];
		double xp = 0;
		int craftedSoFar = 0;
		if (runes == 0) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You don't have pure essence.");
			return;
		}
		for (int i = 0; i < CRAFT_REQ.length; i++) {
			if (CRAFT_REQ[i] > level) {
				break;
			}
			possibleRunes[i] = RUNES[i];
		}
		player.getInventory().deleteItem(ItemIdentifiers.PURE_ESSENCE, runes);
		for (int i = 0; i < runes; i++) {
			if (i >= possibleRunes.length)
				i = 0;
			craftedSoFar++;
			if (craftedSoFar >= runes)
				break;
			if (Utils.random(1000) == 1) {
				player.getInventory().addItem(ItemIdentifiers.KIRIN, 1);
				player.getPackets()
						.sendGameMessage(
								"You have received Kirin the pet! It has been transferred to your bank! ");
			}
			player.getGamePointManager().addGamePointItem(
					new Item(possibleRunes[Utils.random(possibleRunes.length)],
							Utils.random(1, 3)));
			if (player.getClanManager() != null
					&& player.getClanManager().getClan() != null)
				player.getClanManager().getClan().increaseGatheredResources();
			xp += RUNES_XP[i];
		}
		if (hasRcingSuit(player))
			xp *= 1.025;
		player.getGamePointManager().increaseGamePoints((int) (xp / 4));
		player.getSkills().addXp(Skills.RUNECRAFTING, xp * 2);
		player.setNextGraphics(new Graphics(186));
		player.setNextAnimation(new Animation(791));
		player.lock(5);
		player.getPackets().sendGameMessage(
				"You bind the temple's power into assorted runes.");
	}

}