package net.nocturne.game.player.actions.skills.fishing;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.GamePointManager.GPR;
import net.nocturne.game.player.actions.Action;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

public class Fishing extends Action {

	public enum Fish {

		CRAYFISH(ItemIdentifiers.RAW_CRAYFISH, 1, 10),

		SHRIMP(ItemIdentifiers.RAW_SHRIMP, 1, 10),

		SARDINES(ItemIdentifiers.RAW_SARDINE, 5, 20),

		HERRING(ItemIdentifiers.RAW_HERRING, 10, 30),

		ANCHOVIES(ItemIdentifiers.RAW_ANCHOVIES, 15, 40),

		MACKEREL(ItemIdentifiers.RAW_MACKEREL, 16, 45),

		TROUT(ItemIdentifiers.RAW_TROUT, 20, 50),

		COD(ItemIdentifiers.RAW_COD, 23, 45),

		PIKE(ItemIdentifiers.RAW_PIKE, 25, 60),

		SALMON(ItemIdentifiers.RAW_SALMON, 30, 70),

		SEAWEED(ItemIdentifiers.SEAWEED, 30, 5),

		OYSTER(ItemIdentifiers.OYSTER, 30, 5),

		TUNA(ItemIdentifiers.RAW_TUNA, 35, 80),

		LOBSTER(ItemIdentifiers.RAW_LOBSTER, 40, 90),

		BASS(ItemIdentifiers.RAW_BASS, 46, 100),

		LEAPING_TROUT(ItemIdentifiers.LEAPING_TROUT, 48, 105),

		SWORDFISH(ItemIdentifiers.RAW_SWORDFISH, 50, 115),

		LEAPING_SALMON(ItemIdentifiers.LEAPING_SALMON, 58, 135),

		MONKFISH(ItemIdentifiers.RAW_MONKFISH, 62, 140),

		KARAMBWAN(ItemIdentifiers.RAW_KARAMBWAN, 65, 155),

		LEAPING_STURGEON(ItemIdentifiers.LEAPING_STURGEON, 70, 165),

		SHARK(ItemIdentifiers.RAW_SHARK, 76, 175),

		SEA_TURTLE(ItemIdentifiers.RAW_SEA_TURTLE, 79, 180),

		MANTA(ItemIdentifiers.RAW_MANTA_RAY, 81, 200),

		CAVE_FISH(ItemIdentifiers.RAW_CAVEFISH, 85, 225),

		ROCKTAIL(ItemIdentifiers.RAW_ROCKTAIL, 90, 245),

		TARPON(ItemIdentifiers.RAW_TARPON, 90, 132.5),

		SEERFISH(ItemIdentifiers.RAW_SEERFISH, 92, 145.5),

		SILLAGO(ItemIdentifiers.RAW_SILLAGO, 92, 365);

		private final int id, level;
		private final double xp;

		Fish(int id, int level, double xp) {
			this.id = id;
			this.level = level;
			this.xp = xp;
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}
	}

	public enum FishingSpots {
		CAVEFISH_SHOAL(8841, 1, 307, 313, new Animation(622), Fish.CAVE_FISH), ROCKTAIL_SHOAL(
				8842, 1, 307, 15263, new Animation(622), Fish.ROCKTAIL), BAIT(
				327, 2, 307, 313, new Animation(622), Fish.SARDINES,
				Fish.HERRING, Fish.PIKE), NET(327, 1, 303, -1, new Animation(
				621), Fish.SHRIMP, Fish.ANCHOVIES), //
		LURE(328, 1, 309, 314, new Animation(622), Fish.TROUT, Fish.SALMON), //
		LURE2(329, 1, 309, 314, new Animation(622), Fish.TROUT, Fish.SALMON), LURE3(
				317, 1, 309, 314, new Animation(622), Fish.TROUT, Fish.SALMON), BAIT2(
				328, 2, 307, 313, new Animation(622), Fish.PIKE), //
		BAIT3(329, 2, 307, 313, new Animation(622), Fish.PIKE, Fish.CAVE_FISH), NET4(
				325, 1, 303, -1, new Animation(621), Fish.SHRIMP,
				Fish.ANCHOVIES), BAIT4(325, 2, 307, 313, new Animation(622),
				Fish.SARDINES, Fish.HERRING), CAGE(6267, 1, 301, -1,
				new Animation(619), Fish.CRAYFISH), LURE4(3019, 1, 11323, 314,
				new Animation(622), Fish.LEAPING_TROUT, Fish.LEAPING_SALMON,
				Fish.LEAPING_STURGEON), LURE5(317, 1, 11323, 314,
				new Animation(622), Fish.LEAPING_TROUT, Fish.LEAPING_SALMON,
				Fish.LEAPING_STURGEON), CAGE2(312, 1, 301, -1, new Animation(
				619), Fish.LOBSTER), HARPOON(312, 2, 311, -1,
				new Animation(618), Fish.TUNA, Fish.SWORDFISH), BIG_NET(313, 1,
				305, -1, new Animation(620), Fish.MACKEREL, Fish.COD,
				Fish.BASS, Fish.SEAWEED, Fish.OYSTER), HARPOON2(313, 2, 311,
				-1, new Animation(618), Fish.SHARK), HARPOON3(3848, 2, 311, -1,
				new Animation(618), Fish.TUNA, Fish.SWORDFISH), NET3(3848, 1,
				303, -1, new Animation(621), Fish.MONKFISH), VESSEL(1177, 1,
				3157, -1, new Animation(621), Fish.KARAMBWAN), NET2(952, 1,
				303, -1, new Animation(621), Fish.SHRIMP), ARC_NET(23134, 1,
				ItemIdentifiers.BIG_FISHING_NET,
				ItemIdentifiers.WUSHANKO_FISHING_BAIT, new Animation(621),
				Fish.SEERFISH, Fish.SILLAGO), ARC_BAIT(23132, 1,
				ItemIdentifiers.FISHING_ROD,
				ItemIdentifiers.WUSHANKO_FISHING_BAIT, new Animation(622),
				Fish.TARPON, Fish.SEERFISH);

		static final Map<Integer, FishingSpots> spot = new HashMap<>();

		static {
			for (final FishingSpots spots : FishingSpots.values())
				spot.put(spots.id | spots.option << 24, spots);
		}

		public static FishingSpots forId(int id) {
			return spot.get(id);
		}

		private final Fish[] fish;

		private final int id, option, tool, bait;

		private final Animation animation;

		FishingSpots(int id, int option, int tool, int bait,
				Animation animation, Fish... fish) {
			this.id = id;
			this.tool = tool;
			this.bait = bait;
			this.animation = animation;
			this.fish = fish;
			this.option = option;
		}

		public Animation getAnimation() {
			return animation;
		}

		public int getBait() {
			return bait;
		}

		public Fish[] getFish() {
			return fish;
		}

		public int getId() {
			return id;
		}

		public int getOption() {
			return option;
		}

		public int getTool() {
			return tool;
		}
	}

	private final FishingSpots spot;

	private final NPC npc;
	private final WorldTile tile;
	private int fishId;

	private final int[] BONUS_FISH = { 341, 349, 401, 407 };

	private boolean multipleCatch;

	public Fishing(FishingSpots spot, NPC npc) {
		this.spot = spot;
		this.npc = npc;
		tile = new WorldTile(npc);
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		fishId = getRandomFish(player);
		if (spot.getFish()[fishId] == Fish.TUNA
				|| spot.getFish()[fishId] == Fish.SHARK
				|| spot.getFish()[fishId] == Fish.SWORDFISH) {
			if (Utils.random(50) <= 5) {
				if (player.getSkills().getLevel(Skills.AGILITY) >= spot
						.getFish()[fishId].getLevel())
					multipleCatch = true;
			}
		} else
			multipleCatch = false;
		player.getPackets().sendGameMessage("You attempt to capture a fish...",
				true);
		setActionDelay(player, getFishingDelay(player));
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(spot.getAnimation());
		return checkAll(player);
	}

	private int getFishingDelay(Player player) {
		int level = player.getSkills().getLevel(Skills.FISHING);
		int levelRequired = spot.getFish()[fishId].getLevel();
		double multiplier = getMultiplier(player);
		int baseTime = (int) (9 + Math.pow(levelRequired, 1.07));
		int removeTime = (int) ((level) * multiplier);
		int difference = (baseTime - removeTime);
		if (difference < 1)
			difference = 1;
		int randomTime = Utils.random((levelRequired / 17));
		double cycleCount = Utils.getHalfRandom(difference) + randomTime + 3;
		if (cycleCount < 1)
			cycleCount = 1;
		int delay = (int) cycleCount + 1;
		delay /= player.getAuraManager().getFishingAccuracyMultiplier();
		return Utils.getHalfRandom(delay);
	}

	private double getMultiplier(Player player) {
		double multiplier = player.getAuraManager()
				.getFishingAccuracyMultiplier();
		if (player.getGamePointManager().hasGamePointsReward(GPR.MORE_SKILLS))
			multiplier += 0.1;
		return multiplier;
	}

	private int getSpecialFamiliarBonus(int id) {
		switch (id) {
		case 6796:
		case 6795:// rock crab
			return 1;
		}
		return -1;
	}

	private int getRandomFish(Player player) {
		int random = Utils.random(spot.getFish().length);
		int difference = player.getSkills().getLevel(Skills.FISHING)
				- spot.getFish()[random].getLevel();
		if (difference < -1)
			return 0;
		if (random < -1)
			return 0;
		return random;
	}

	@Override
	public int processWithDelay(Player player) {
		addFish(player);
		return getFishingDelay(player);
	}

	private void addFish(Player player) {
		Item fish = new Item(spot.getFish()[fishId].getId(), multipleCatch ? 2
				: 1);
		player.getPackets().sendGameMessage(getMessage(fish), true);
		player.getInventory().deleteItem(spot.getBait(), 1);
		double totalXp = spot.getFish()[fishId].getXp();
		if (hasFishingSuit(player))
			totalXp *= 1.125;
		player.getGamePointManager().increaseGamePoints((int) (totalXp / 10));
		player.getSkills().addXp(Skills.FISHING, totalXp);
		player.getInventory().addItem(fish);
		if (player.getClanManager() != null
				&& player.getClanManager().getClan() != null)
			player.getClanManager().getClan()
					.increaseGatheredResources(fish.getAmount());
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3, fish.getId(),
					Skills.FISHING);
		if (fish.getId() == ItemIdentifiers.RAW_ROCKTAIL)
			player.getCompCapeManager().increaseRequirement(
					Requirement.ROCK_TAILS, 1);
		// handleTrophyFish(player, fish);
		player.getSkillTasks().handleTask(spot.getFish()[fishId], 1);
		handlePetDrop(player);
		handleBonusFish(player);
		if (player.getFamiliar() != null) {
			if (Utils.random(50) == 0
					&& getSpecialFamiliarBonus(player.getFamiliar().getId()) > 0) {
				player.getInventory().addItem(
						new Item(BONUS_FISH[Utils.random(BONUS_FISH.length)]));
				player.getSkills().addXp(Skills.FISHING, 5.5);
			}
		}
		fishId = getRandomFish(player);
		if (Utils.random(50) == 0 && FishingSpotsHandler.moveSpot(npc))
			player.setNextAnimation(new Animation(-1));
	}

	private boolean hasFishingSuit(Player player) {
		return player.getEquipment().getHatId() == 24427
				&& player.getEquipment().getChestId() == 24428
				&& player.getEquipment().getLegsId() == 24429
				&& player.getEquipment().getBootsId() == 24430;
	}

	private String getMessage(Item fish) {
		if (spot.getFish()[fishId] == Fish.ANCHOVIES
				|| spot.getFish()[fishId] == Fish.SHRIMP)
			return "You manage to catch some "
					+ fish.getDefinitions().getName().toLowerCase() + ".";
		else if (multipleCatch)
			return "Your quick reactions allow you to catch two "
					+ fish.getDefinitions().getName().toLowerCase() + ".";
		else
			return "You manage to catch a "
					+ fish.getDefinitions().getName().toLowerCase() + ".";
	}

	private void handlePetDrop(Player player) {
		if (Utils.random(5000) == 1) {
			player.getBank().addItem(ItemIdentifiers.MEGA_DUCKLINGS, 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.WHITE,
							"You have received mega ducklings the pet! It has been transferred to your bank! ");
		}
	}

	private void handleTrophyFish(Player player, Item fish) {
		final int bigFish = Utils.random(2000);
		if (bigFish == 1) {
			if (fish.getId() == ItemIdentifiers.RAW_BASS)
				player.getGamePointManager().addGamePointItem(
						new Item(ItemIdentifiers.BIG_BASS, 1));
			else if (fish.getId() == ItemIdentifiers.RAW_SWORDFISH)
				player.getGamePointManager().addGamePointItem(
						new Item(ItemIdentifiers.BIG_SWORDFISH, 1));
			else if (fish.getId() == ItemIdentifiers.RAW_SHARK)
				player.getGamePointManager().addGamePointItem(
						new Item(ItemIdentifiers.BIG_SHARK, 1));
			player.getPackets().sendGameMessage(Color.YELLOW,
					"You catch an enormous fish!");
		} else
			player.getGamePointManager().addGamePointItem(fish);
	}

	private void handleBonusFish(Player player) {
		if (player.getFamiliar() != null) {
			if (Utils.random(50) == 0
					&& getSpecialFamiliarBonus(player.getFamiliar().getId()) > 0) {
				player.getGamePointManager().addGamePointItem(
						new Item(BONUS_FISH[Utils.random(BONUS_FISH.length)]));
				player.getSkills().addXp(Skills.FISHING, 5.5);
			}
		}
	}

	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.FISHING) < spot.getFish()[fishId]
				.getLevel()) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a fishing level of "
							+ spot.getFish()[fishId].getLevel()
							+ " to fish here.");
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(spot.getTool())
				&& !(spot.getTool() == 311 && player.getEquipment()
						.getWeaponId() == 10129)) {
			player.getPackets()
					.sendGameMessage(
							"You need a "
									+ new Item(spot.getTool()).getDefinitions()
											.getName().toLowerCase()
									+ " to fish here.");
			return false;
		}
		if (!player.getInventory().containsOneItem(spot.getBait())
				&& spot.getBait() != -1) {
			player.getPackets()
					.sendGameMessage(
							"You don't have "
									+ new Item(spot.getBait()).getDefinitions()
											.getName().toLowerCase()
									+ " to fish here.");
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You don't have enough inventory space.");
			return false;
		}
		return !(tile.getX() != npc.getX() || tile.getY() != npc.getY());
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}