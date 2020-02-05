package net.nocturne.game.player;

import java.io.Serializable;
import java.util.HashMap;

import net.nocturne.game.World;
import net.nocturne.utils.Color;

@SuppressWarnings("serial")
public class CompletionistCapeManager implements Serializable {

	private Player player;
	private HashMap<Requirement, Integer> progress;
	private int received;

	CompletionistCapeManager(Player player) {
		this.player = player;
		this.progress = new HashMap<Requirement, Integer>();
		this.received = 0;
		for (Requirement req : Requirement.values())
			if (!progress.containsKey(req))
				progress.put(req, 0);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean receivedCape(int type) {
		return received >= type;
	}

	public void setReceived(int type) {
		received = type;
	}

	public boolean hasCompleted(Requirement req) {
		return progress.get(req) >= req.amount;
	}

	public boolean hasUntrimmedRequirements() {
		for (Requirement req : Requirement.values()) {
			if (req.trimmed)
				continue;
			if (!hasCompleted(req))
				return false;
		}
		return true;
	}

	public boolean hasTrimmedRequirements() {
		for (Requirement req : Requirement.values())
			if (!hasCompleted(req))
				return false;
		return true;
	}

	public void sendComplete() {
		for (Requirement req : Requirement.values())
			progress.put(req, req.amount);
		World.sendNews(
				player,
				player.getDisplayName()
						+ " has just successfully achieved the trimmed completionist cape!",
				0, true);
	}

	public void increaseRequirement(Requirement req, int amount) {
		if (!progress.containsKey(req))
			progress.put(req, 0);
		if (hasCompleted(req))
			return;
		progress.put(req, progress.get(req) + amount);
		if (progress.get(req) >= req.amount) {
			player.getPackets().sendGameMessage(
					Color.ORANGE,
					"Congratulations, you have successfully finished the "
							+ req.toString().toLowerCase().replace("_", " ")
							+ " completionist cape requirement!");
			World.sendNews(player, player.getDisplayName()
					+ " has just successfully finished the "
					+ req.toString().toLowerCase().replace("_", " ")
					+ " completionist cape requirement!", 0, true);
		}
	}

	void setRequirement(Requirement req, int amount) {
		progress.put(req, amount);
		increaseRequirement(req, 0);
	}

	public void sendInterface() {
		player.getInterfaceManager().sendCentralInterface(868);
		player.getPackets().sendIComponentText(868, 1, "Completionist Cape");
		player.getPackets().sendIComponentText(868, 10,
				"Untrimmed Requirements: ");
		int index = 0;
		for (Requirement req : Requirement.values()) {
			if (!progress.containsKey(req))
				progress.put(req, 0);
			if (!req.trimmed)
				player.getPackets().sendIComponentText(
						868,
						10 + ++index,
						(hasCompleted(req) ? "<str>" : "") + req.description
								+ "(" + progress.get(req) + "/" + req.amount
								+ ")");
		}
		player.getPackets().sendIComponentText(868, 10 + ++index, "");
		player.getPackets().sendIComponentText(868, 10 + ++index,
				"Trimmed Requirements: ");
		for (Requirement req : Requirement.values())
			if (req.trimmed)
				player.getPackets().sendIComponentText(
						868,
						10 + ++index,
						(hasCompleted(req) ? "<str>" : "") + req.description
								+ " (" + progress.get(req) + "/" + req.amount
								+ ")");
		for (int i = index; i < 299; i++)
			player.getPackets().sendIComponentText(868, 10 + i, "");
	}

	public enum Requirement {

		// Untrimmed Requirements
		FIGHT_CAVES("I must have completed the fight caves minigame.", 1, false), VOTES(
				"I must have voted for the server in order to support it.", 30,
				false), SKILL_LEVELS(
				"I must have the highest possible level in every skill.", 2596,
				false), HIDE_AND_SEEK(
				"I must have spotted many penguins from hide and seek.", 40,
				false), EVIL_TREE(
				"I must have dealt damage towards the evil tree.", 10000, false), BARROWS(
				"I must have looted the chest in multiple games of barrows.",
				100, false), IMPLINGS(
				"I must have looted any type of impling.", 250, false), PEST_CONTROL(
				"I must have successfully won games of pest control.", 0, false), TREASURE_HUNTER(
				"I must have tested my luck with the treasure hunter.", 200,
				false), QUEEN_BLACK_DRAGON(
				"I must have slain the queen black dragon.", 1, false), HEFIN_AGILITY(
				"I must have completed the hefin agility course.", 200, false), PICK_POCKETING(
				"I must have stolen from an elf without getting caught.", 150,
				false), GEM_CUTTING(
				"I must have cut a large amount of diamonds.", 500, false), BOX_TRAPPING(
				"I must have captured many grenwalls in isfador.", 400, false), CANNON_BALLS(
				"I must have smelted cannon balls in a furnace.", 1500, false), RUNITE_MINING(
				"I must have mined a lot of runite ore.", 500, false), ROCK_TAILS(
				"I must have fished a load of rock tails.", 500, false), ELDER_TREES(
				"I must have chopped elder logs from a tree.", 500, false), SUMMONING_POUCHES(
				"I must have infused and created summoning pouches.", 800,
				false), CRYSTAL_CHEST("I must have looted the crystal chest.",
				50, false), STEALING_CREATIONS(
				"I must have played a few games of stealing creations.", 5,
				false), TROLL_INVASION(
				"I must have defeated the army in troll invasion.", 10, false), SLAYER_TASKS(
				"I must have completed my slayer tasks.", 80, false), COOK_FISH(
				"I must have successfully cooked any type of fish.", 2000,
				false), BURNING_LOGS(
				"I must have burnt a large amount of logs.", 5000, false), FARMING(
				"I must have harvested a yield from farming trees.", 100, false), HOUSE_BUILDING(
				"I must have spent a lot of money on rooms in my house.",
				5000000, false), STRANGE_ROCKS(
				"I must have combined strange rocks to create a teleportation orb.",
				1, false), OVERLOADS("I must create a handful of overloads.",
				50, false), CRYSTAL_FLASK(
				"I must have crafted a large number of crystal flasks.", 250,
				false),
		// Trimmed Requirements
		GOLDEN_STATUE(
				"I must have completed the golden plith statue in the max guild.",
				1, true), FIGHT_KILN(
				"I must have completed the fight kiln minigame.", 1, true), DOMINION_TOWER(
				"I must have killed any boss in the dominion tower.", 125, true), CASTLE_WARS(
				"I must have participated in castle wars games.", 10, true), SORC_GARDEN(
				"I must have picked sq'irks from the summer garden.", 100, true), DUNGEONEERING(
				"I must have completed dungeons in daemonheim.", 50, true), CLUE_SCROLLS(
				"I must have solved and looted clue scrolls.", 75, true), RISE_OF_SIX(
				"I must have won matches in rise of the six.", 15, true), REAPER_TASK(
				"I must have completed reaper tasks.", 25, true), VORAGO(
				"I must have slain the ultimate vorago.", 100, true), NEX(
				"I must have slain the ultimate nex.", 100, true), KALPHITE_KING(
				"I must have slain the ultimate kalphite king.", 100, true), ARAXXI(
				"I must have slain the ultimate araxxi.", 100, true), WILDY_WYRM(
				"I must have slain the ultimate wildy wyrm.", 30, true), ;

		private String description;
		private int amount;
		private boolean trimmed;

		Requirement(String description, int amount, boolean trimmed) {
			this.description = description;
			this.amount = amount;
			this.trimmed = trimmed;
		}
	}

}