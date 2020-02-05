package net.nocturne.game.player.actions.skills.prayer;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.content.activities.events.GlobalEvents;
import net.nocturne.game.player.content.activities.events.GlobalEvents.Event;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class Burying {

	public enum Bone {
		NORMAL(526, 4.5),

		BURNT(528, 4.5),

		WOLF(2859, 4.5),

		MONKEY(3183, 5),

		BAT(530, 5),

		BIG(532, 15),

		JOGRE(3125, 15),

		ZOGRE(4812, 22.5),

		SHAIKAHAN(3123, 25),

		BABY(534, 30),

		WYVERN(6812, 50),

		DRAGON(536, 72),

		FAYRG(4830, 84),

		RAURG(4832, 96),

		DAGANNOTH(6729, 125),

		OURG(4834, 140),

		FROST_DRAGON(18830, 180),

		FROST_DRAGON4(18833, 180),

		FROST_DRAGON_3(18832, 180),

		ACCURSED(20266, 12.5),

		IMPIOUS(20264, 4),

		INFERNAL(20268, 62.5),

		SEARING(34159, 200),

		TORTURED(32945, 90),

		AIRUT(ItemIdentifiers.AIRUT_BONES, 132.5),

		ADAMANT_DRAGON(ItemIdentifiers.ADAMANT_DRAGON_BONES, 144),

		RUNE_DRAGON(ItemIdentifiers.RUNE_DRAGON_BONES, 190);

		private int id;
		private double experience;

		private static final Map<Integer, Bone> bones = new HashMap<>();

		static {
			for (Bone bone : Bone.values()) {
				bones.put(bone.getId(), bone);
			}
		}

		public static Bone forId(int id) {
			return bones.get(id);
		}

		Bone(int id, double experience) {
			this.id = id;
			this.experience = experience;
		}

		public int getId() {
			return id;
		}

		public double getExperience() {
			return experience;
		}

		public static final Animation BURY_ANIMATION = new Animation(827);

		public static void bury(final Player player, int inventorySlot) {
			final Item item = player.getInventory().getItem(inventorySlot);
			if (item == null)
				return;
			final Bone bone = Bone.forId(item.getId());
			if (bone == null)
				return;
			final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
			player.lock(2);
			switch (item.getId()) {
			case 20264:
				player.setNextAnimation(new Animation(445));
				player.setNextGraphics(new Graphics(56));
				break;
			case 20266:
				player.setNextAnimation(new Animation(445));
				player.setNextGraphics(new Graphics(47));
				break;
			case 20268:
				player.setNextAnimation(new Animation(445));
				player.setNextGraphics(new Graphics(40));
				break;
			default:
				player.setNextAnimation(BURY_ANIMATION);
				break;
			}

			player.getPackets().sendGameMessage(
					"You dig a hole in the ground...", true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getPackets().sendGameMessage(
							"You bury the " + itemDef.getName().toLowerCase(),
							true);
					player.getInventory().deleteItem(inventorySlot,
							new Item(item));
					double xp = bone.getExperience()
							* player.getAuraManager().getPrayerMultiplier()
							* (GlobalEvents.isActiveEvent(Event.MORE_PRAYER) ? 2
									: 1);
					final int maxPrayer = player.getSkills().getLevelForXp(
							Skills.PRAYER) * 10;
					handlePrayerBonus(player, bone, maxPrayer);
					player.getSkills().addXp(Skills.PRAYER, xp);
					Double lastPrayer = (Double) player
							.getTemporaryAttributtes().get("current_prayer_xp");
					if (lastPrayer == null) {
						lastPrayer = 0.0;
					}
					double total = xp + lastPrayer;
					int amount = (int) (total / 500);
					if (amount != 0) {
						double restore = player.getAuraManager()
								.getPrayerRestoration()
								* (player.getSkills().getLevelForXp(
										Skills.PRAYER) * 10);
						player.getPrayer().boost((int) (amount * restore));
						total -= amount * 500;
					}
					player.getTemporaryAttributtes().put("current_prayer_xp",
							total);
					stop();
				}

			});
		}
	}

	public static void handlePrayerBonus(Player player, Bone bone, int maxPrayer) {
		if (player.getEquipment().containsOneItem(19888)) {
			switch (bone.getId()) {
			case 526:
			case 528:
			case 530:
			case 20264:
				if (player.getPrayer().getPoints() < maxPrayer) {
					player.getPrayer().setPoints(
							player.getPrayer().getPoints() + 5, true);
				}
				break;
			case 532:
			case 534:
			case 3125:
			case 6812:
				if (player.getPrayer().getPoints() < maxPrayer) {
					player.getPrayer().setPoints(
							player.getPrayer().getPoints() + 10, true);
				}
				break;
			case 536:
			case 6729:
			case 4834:
			case 4835:
			case 14793:
			case 14794:
			case 18832:
			case 18830:
			case 18831:
			case 30209:
			case 20268:
			case 32945:
				if (player.getPrayer().getPoints() < maxPrayer) {
					player.getPrayer().setPoints(
							player.getPrayer().getPoints() + 15, true);
				}
				break;
			}
			player.getPackets()
					.sendGameMessage(
							"Your demon horn necklace boosts your prayer points.",
							true);
		}
	}
}