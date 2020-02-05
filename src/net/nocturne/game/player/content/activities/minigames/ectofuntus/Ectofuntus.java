package net.nocturne.game.player.content.activities.minigames.ectofuntus;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.prayer.Burying;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class Ectofuntus {

	/**
	 * @author: miles M
	 */

	private static final int HOPPER_OBJECT = 11162;
	private static final int GRINDER_OBJECT = 11163;
	private static final int BIN_OBJECT = 11164;

	private enum BoneMeal {
		BONES(ItemIdentifiers.BONES, ItemIdentifiers.BONEMEAL), BAT_BONES(
				ItemIdentifiers.BAT_BONES, ItemIdentifiers.BAT_BONEMEAL), BIG_BONES(
				ItemIdentifiers.BIG_BONES, ItemIdentifiers.BIG_BONEMEAL), BABY_DRAGON_BONES(
				ItemIdentifiers.BABY_DRAGON_BONES,
				ItemIdentifiers.BABY_DRAGON_BONEMEAL), DRAGON_BONES(
				ItemIdentifiers.DRAGON_BONES, ItemIdentifiers.DRAGON_BONEMEAL), DAGANNOTH_BONES(
				ItemIdentifiers.DAGANNOTH_BONES,
				ItemIdentifiers.DAGANNOTH_BONEMEAL), WYVERN_BONES(
				ItemIdentifiers.WYVERN_BONES, ItemIdentifiers.WYVERN_BONEMEAL), OURG_BONES(
				ItemIdentifiers.OURG_BONES, ItemIdentifiers.OURG_BONEMEAL);
		// FROST_BONES(ItemIdentifiers.FROST_DRAGON_BONES, 18834);

		private int boneId;
		private int boneMealId;

		private static Map<Integer, BoneMeal> bonemeals = new HashMap<>();
		private static Map<Integer, BoneMeal> bones = new HashMap<>();

		public static BoneMeal forBoneId(int itemId) {
			return bonemeals.get(itemId);
		}

		public static BoneMeal forMealId(int itemId) {
			return bones.get(itemId);
		}

		static {
			for (final BoneMeal bonemeal : BoneMeal.values()) {
				bonemeals.put(bonemeal.boneId, bonemeal);
			}
			for (final BoneMeal bonemeal : BoneMeal.values()) {
				bones.put(bonemeal.boneMealId, bonemeal);
			}
		}

		BoneMeal(int boneId, int boneMealId) {
			this.boneId = boneId;
			this.boneMealId = boneMealId;
		}

		public int getBoneId() {
			return boneId;
		}

		public int getBoneMealId() {
			return boneMealId;
		}
	}

	public static void handleWorship(Player player) {
		if (player.getInventory().containsItem(ItemIdentifiers.ECTOPHIAL_EMPTY,
				1)) {
			player.setNextAnimation(new Animation(1649));
			player.getInventory()
					.deleteItem(ItemIdentifiers.ECTOPHIAL_EMPTY, 1);
			player.getInventory().addItem(ItemIdentifiers.ECTOPHIAL, 1);
			player.getPackets().sendGameMessage(
					"You refill the ectophial from the Ectofuntus.");
			return;
		}
		if (!player.getInventory().containsItem(
				ItemIdentifiers.BUCKET_OF_SLIME, 1)) {
			player.getPackets()
					.sendGameMessage(
							"You need a bucket of slime before you can worship the ectofuntus.");
			return;
		}
		for (Item item : player.getInventory().getItems().getItems()) {
			if (item == null)
				continue;
			BoneMeal bone = BoneMeal.forMealId(item.getId());
			if (bone != null) {
				Burying.Bone boneData = Burying.Bone.forId(bone.getBoneId());
				if (boneData == null) {
					player.getPackets()
							.sendGameMessage(
									"Error bone not added.. Please post the bone you tried to add on the forums.");
					return;
				}
				player.setNextAnimation(new Animation(1651));
				player.getInventory().deleteItem(bone.getBoneMealId(), 1);
				player.getInventory().addItem(ItemIdentifiers.EMPTY_POT, 1);
				player.getInventory().deleteItem(
						ItemIdentifiers.BUCKET_OF_SLIME, 1);
				player.getInventory().addItem(ItemIdentifiers.EMPTY_BUCKET, 1);
				player.getSkills().addXp(Skills.PRAYER,
						boneData.getExperience() * 4);
				player.unclaimedEctoTokens += 5;
				return;
			}
		}
	}

	public static boolean handleObjects(final Player player, final int objectId) {
		switch (objectId) {
		case 5268: {
			player.setNextAnimation(new Animation(828));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(new WorldTile(3669, 9888, 3));
				}
			}, 0);
		}
			return true;

		case 5264: {
			player.setNextAnimation(new Animation(828));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(new WorldTile(3654, 3519, 0));
				}
			}, 0);
		}
			return true;

		case 9308: {
			if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
				player.getPackets().sendGameMessage(
						"You need 53 Agility to maneuver this obstacle.");
				return true;
			}
			player.setNextAnimation(new Animation(828));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(new WorldTile(3671, 9888, 2));
				}
			}, 1);
		}
			return true;

		case 9307: {
			if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
				player.getPackets().sendGameMessage(
						"You need 53 Agility to maneuver this obstacle.");
				return true;
			}
			player.setNextAnimation(new Animation(828));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(new WorldTile(3670, 9888, 3));
				}
			}, 1);
		}
			return true;

		case 5263:
			if (player.getPlane() == 3)
				player.setNextWorldTile(new WorldTile(3688, 9888, 2));
			if (player.getPlane() == 2)
				player.setNextWorldTile(new WorldTile(3675, 9887, 1));
			if (player.getPlane() == 1)
				player.setNextWorldTile(new WorldTile(3683, 9888, 0));
			return true;

		case 5262:
			if (player.getPlane() == 2)
				player.setNextWorldTile(new WorldTile(3692, 9888, 3));
			if (player.getPlane() == 1)
				player.setNextWorldTile(new WorldTile(3671, 9888, 2));
			if (player.getPlane() == 0)
				player.setNextWorldTile(new WorldTile(3687, 9888, 1));
			return true;

		case 5282:
			handleWorship(player);
			return true;

		case GRINDER_OBJECT:
			if (player.boneType != -1 && !player.bonesGrinded) {
				player.getPackets()
						.sendGameMessage(
								"You turn the grinder, some crushed bones fall into the bin.");
				player.setNextAnimation(new Animation(1648));
				player.bonesGrinded = true;
			} else {
				player.setNextAnimation(new Animation(1648));
			}
			return true;

		case BIN_OBJECT:
			if (player.boneType == -1) {
				player.getPackets()
						.sendGameMessage(
								"You need to put some bones in the hopper and grind them first.");
				return true;
			}
			if (!player.bonesGrinded) {
				player.getPackets()
						.sendGameMessage(
								"You need to grind the bones by turning the grinder first.");
				return true;
			}
			if (!player.getInventory().containsItem(ItemIdentifiers.EMPTY_POT,
					1)) {
				player.getPackets()
						.sendGameMessage(
								"You need an empty pot to fill with the crushed bones.");
				return true;
			}
			if (player.boneType != -1 && player.bonesGrinded) {
				BoneMeal meal = BoneMeal.forBoneId(player.boneType);
				if (meal != null) {
					player.getPackets().sendGameMessage(
							"You fill an empty pot with bones.");
					player.setNextAnimation(new Animation(1650));
					player.getInventory().deleteItem(ItemIdentifiers.EMPTY_POT,
							1);
					player.getInventory().addItem(meal.getBoneMealId(), 1);
					player.boneType = -1;
					player.bonesGrinded = false;
				} else {
					player.boneType = -1;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean handleItemOnObject(Player player, int itemId,
			int objectId) {
		ItemDefinitions itemDefs = ItemDefinitions.getItemDefinitions(itemId);

		if (itemId == ItemIdentifiers.EMPTY_BUCKET && objectId == 17119) {
			player.getActionManager().setAction(new SlimeBucketFill());
			return true;
		}

		if (itemDefs.getName().toLowerCase().contains("bone")
				&& objectId == HOPPER_OBJECT) {
			if (player.boneType != -1) {
				player.getPackets().sendGameMessage(
						"You already have some bones in the hopper.");
				return true;
			}
			BoneMeal meal = BoneMeal.forBoneId(itemId);
			if (meal != null) {
				player.boneType = meal.getBoneId();
				player.getPackets().sendGameMessage(
						"You put the bones in the hopper.");
				player.setNextAnimation(new Animation(1649));
				player.getInventory().deleteItem(meal.getBoneId(), 1);
			} else {
				player.boneType = -1;
			}
		}
		return false;
	}
}