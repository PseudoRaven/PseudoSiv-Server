package net.nocturne.game.player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.nocturne.game.World;
import net.nocturne.game.item.Item;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

/**
 * @author Miles (bobismyname)
 */

public class DropThresholdManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3582583687002296866L;
	private Player player;
	private int[] kills;

	public DropThresholdManager(Player player) {
		this.player = player;
		this.kills = new int[100];
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public enum ThreshDrop {

		ARAXXOR(0, "arraxor", 500, new Item[] { new Item(33809, 1) }), ARAXXI(
				1, "araxxi", 500, new Item[] { new Item(33810, 1) }), KREEARRA(
				2, "kree'arra", 2500, new Item[] { new Item(33778, 1) }), COMMANDER_ZILYANA(
				3, "commander zilyana", 2500, new Item[] { new Item(33807, 1) }), GENERAL_GRAARDOR(
				4, "general graardor", 2500, new Item[] { new Item(33806, 1) }), KRIL_TSUTSAROTH(
				5, "k'ril tsutsaroth", 2500, new Item[] { new Item(33805, 1) }), NEX(
				6, "nex", 1000, new Item[] { new Item(33808, 1) }), GIANT_MOLE(
				7, "giant mole", 1500, new Item[] { new Item(33813, 1) }), HAR_AKEN(
				8, "har-aken", 100, new Item[] { new Item(33788, 1) }), KING_BLACK_DRAGON(
				9, "king black dragon", 1250, new Item[] { new Item(33792, 1) }), QUEEN_BLACK_DRAGON(
				10, "queen black dragon", 1250,
				new Item[] { new Item(33799, 1) }), KALPHITE_QUEEN(11,
				"kalphite queen", 1250, new Item[] { new Item(33790, 1) }), KALPHITE_KING(
				12, "kalphite king", 1000, new Item[] { new Item(33789, 1) }), CORPOREAL_BEAST(
				13, "corporeal beast", 1250, new Item[] { new Item(33786, 1) }), CHAOS_ELEMENTAL(
				14, "chaos elemental", 1250, new Item[] { new Item(33785, 1) }), LEGIO_PRIMUS(
				15, "legio primus", 300, new Item[] { new Item(33819, 1) }), LEGIO_SECUNDUS(
				16, "legio secundus", 300, new Item[] { new Item(33820, 1) }), LEGIO_TERTIUS(
				17, "legio tertius", 300, new Item[] { new Item(33821, 1) }), LEGIO_QUARTUS(
				18, "legio quartus", 300, new Item[] { new Item(33822, 1) }), LEGIO_QUINTUS(
				19, "legio quintus", 300, new Item[] { new Item(33823, 1) }), LEGIO_SEXTUS(
				20, "legio sextus", 1000, new Item[] { new Item(33824, 1) }), DAGANNOTH_REX(
				21, "dagannoth rex", 3750, new Item[] { new Item(33801, 1) }), DAGANNOTH_PRIME(
				22, "dagannoth prime", 3750, new Item[] { new Item(33800, 1) }), DAGANNOTH_SUPREME(
				23, "dagannoth supreme", 3750,
				new Item[] { new Item(33802, 1) }), BLINK(24, "blink", 2000,
				new Item[] { new Item(31457, 1) }), LUCIEN(25, "lucien", 800,
				new Item[] { new Item(31025, 1) }), FROST_DRAGON(26,
				"frost dragon", 5000, new Item[] { new Item(31459, 1) }), VORAGO(
				27, "vorago", 1000, new Item[] { new Item(28626, 1),
						new Item(33716, 1) }), EDIMMU(28, "edimmu", 3500,
				new Item[] { new Item(32730, 1) });

		private static final Map<String, ThreshDrop> npcs = new HashMap<>();

		public static ThreshDrop forName(String npcName) {
			npcName = npcName.toLowerCase().replaceAll("_", " ");
			return npcs.get(npcName);
		}

		static {
			for (ThreshDrop npc : ThreshDrop.values()) {
				npcs.put(npc.getNPCName(), npc);
			}
		}

		private final int threshId;
		private final String npcName;
		private final int kills;
		private final Item[] rareDrops;

		ThreshDrop(int threshId, String npcName, int kills, Item[] rareDrops) {
			this.threshId = threshId;
			this.npcName = npcName;
			this.kills = kills;
			this.rareDrops = rareDrops;
		}

		public int getThreshId() {
			return threshId;
		}

		public String getNPCName() {
			return npcName;
		}

		public int getKills() {
			return kills;
		}

		public Item[] getRareDrops() {
			return rareDrops;
		}

		public boolean isRareDrop(int itemId) {
			for (Item item : rareDrops) {
				if (item.getId() == itemId)
					return true;
			}
			return false;
		}
	}

	public void dropChance(ThreshDrop npc) {
		kills[npc.getThreshId()]++;
		int number = npc.getKills() * 2;
		if (kills[npc.getThreshId()] > npc.getKills())
			number *= 0.6;
		else if (kills[npc.getThreshId()] == npc.getKills()) {
			player.getPackets().sendGameMessage(
					Color.YELLOW,
					"You have reached a boss threshold of " + npc.getKills()
							+ "!");
			player.getPackets()
					.sendGameMessage(Color.YELLOW,
							"You are now more likely to receive a boss pet from this monster!");
		}
		int chance = Utils.random(1, number);
		if (chance == 1) {
			Item drop = npc.getRareDrops()[Utils.random(0,
					npc.getRareDrops().length - 1)];
			player.getBank().addItem(drop, true);
			player.getPackets().sendGameMessage(
					Color.YELLOW,
					"Congratulations! You have unlocked a pet from: "
							+ npc.getNPCName()
							+ "! It has been added to your bank!");
			World.sendWorldMessage(
					"<col=00BD00><img=7>News: "
							+ player.getDisplayName()
							+ ""
							+ (player.isHardcoreIronman() ? " (HC Iron Man) "
									: player.isIronman() ? "(Iron Man)" : "")
							+ "has just received a boss pet from "
							+ npc.getNPCName() + "!" + "</col> ", false);
			kills[npc.getThreshId()] = 0;
		}
	}

}
