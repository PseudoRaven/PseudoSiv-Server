package net.nocturne.game.player.actions.skills.summoning;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.utils.Utils;

public class SpecialScrolls extends Action {

	public enum Scroll {

		FRUITFALL(69, 1.4, new Item(12033), 12423, 10),

		THIEVING_FINGERS(47, 0.9, new Item(12041), 12426, 10),

		STEEL_OF_LEGENDS(99, 4.9, new Item(12790), 12825, 10),

		WINTER_STORAGE(96, 4.8, new Item(12093), 12435, 10),

		MAMMOTH_FEAST(99, 5, new Item(36060), 36056, 10),

		HEALING_AURA(88, 1.8, new Item(12039), 12434, 10),

		MAGIC_FOCUS(92, 4.6, new Item(12090), 12437, 10),

		ABYSSAL_STEALTH(62, 1.2, new Item(12037), 12427, 10),

		TIRELESS_RUN(52, 0.5, new Item(12007), 12441, 10);

		private static final Map<Integer, Scroll> pouches = new HashMap<>();

		static {
			for (Scroll pouch : Scroll.values()) {
				pouches.put(pouch.realPouchId, pouch);
			}
		}

		public static Scroll forId(int id) {
			return pouches.get(id);
		}

		public static Scroll getPouchByProduce(int id) {
			for (Scroll pouch : Scroll.values()) {
				if (pouch.getRealPouchId() == id)
					return pouch;
			}
			return null;
		}

		public static Scroll getPouch(int id) {
			for (Scroll pouch : Scroll.values()) {
				int item = pouch.getItemsRequired().getId();
				if (item == id)
					return pouch;
			}
			return null;
		}

		public static Scroll getScroll(Player player) {
			for (Scroll scroll : Scroll.values()) {
				Item item = scroll.getItemsRequired();
				if (!player.getInventory()
						.containsItems(new Item(item.getId())))
					return scroll;
			}
			return null;
		}

		public boolean hasPouch(Player player) {
			for (Scroll pouch : Scroll.values())
				if (player.getInventory().containsOneItem(
						pouch.getRealPouchId()))
					return true;
			return false;
		}

		private int levelRequired;
		private double experience;
		private Item ingredientsRequired;
		private int realPouchId;
		private int amount;

		Scroll(int levelRequired, double experience, Item itemsRequired,
				int producedItem, int amount) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.ingredientsRequired = itemsRequired;
			this.realPouchId = producedItem;
			this.amount = amount;
		}

		public Item getItemsRequired() {
			return ingredientsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getRealPouchId() {
			return realPouchId;
		}

		public double getExperience() {
			return experience;
		}

		public int getAmount() {
			return amount;
		}
	}

	private final Scroll pouch;
	private final WorldObject object;
	private int ticks;

	public SpecialScrolls(Scroll pouch, WorldObject object, int ticks) {
		this.object = object;
		this.pouch = pouch;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player)) {
			return false;
		}
		return true;
	}

	public boolean checkAll(Player player) {
		if (pouch == null || player == null || object == null) {
			System.out.println("lol4");
			return false;
		}
		if (!player.getInventory().containsItems(pouch.getItemsRequired())) {
			System.out.println("lol5");
			return false;

		}
		if (pouch.getItemsRequired() != null) {
			if (!player.getInventory().containsItemToolBelt(
					pouch.getItemsRequired().getId(),
					pouch.getItemsRequired().getAmount())) {
				StringBuilder sb = new StringBuilder();
				sb.append("You need ");
				sb.append(pouch.getItemsRequired() + " "
						+ pouch.getItemsRequired().getDefinitions().getName()
						+ pouch.getItemsRequired().getId() + "");
				sb.append(" to create a "
						+ new Item(pouch.getRealPouchId()).getDefinitions()
								.getName() + ".");
				player.getPackets().sendGameMessage(sb.toString());
				System.out.println("lol2");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SUMMONING) < pouch
				.getLevelRequired()) {
			player.getPackets().sendGameMessage(
					"You need a Summoning level of at least "
							+ pouch.getLevelRequired()
							+ " to create "
							+ new Item(pouch.getRealPouchId()).getDefinitions()
									.getName());
			System.out.println("lol3");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(725));
		player.setNextGraphics(new Graphics(1207));
		double xp = pouch.getExperience();
		int amount = player.getInventory().getAmountOf(
				pouch.getItemsRequired().getId());
		if (new Item(pouch.getRealPouchId()).getDefinitions().isStackable())
			amount *= Settings.XP_RATE;
		for (int i = 0; i < ticks; i++) {
			if (!player.getInventory().containsItems(pouch.getItemsRequired())) {
				System.out.println("lol");
				return false;

			}
			Item required = pouch.getItemsRequired();
			player.getInventory().deleteItem(required.getId(), amount);
		}
		player.getInventory().addItem(new Item(pouch.getRealPouchId()).getId(),
				amount);
		player.getSkills().addXp(Skills.SUMMONING, xp);
		int random = Utils.random(900);

		if (random == 57 && !player.getInventory().containsItem(32072, 1)
				&& !player.getBank().containsItem(32072)
				&& player.getInventory().getFreeSlots() >= 1
				&& player.getSkills().getLevelForXp(Skills.SUMMONING) >= 99) {
			player.getInventory().addItem(32072, 1);
			player.getPackets()
					.sendGameMessage(
							"<col=33e5e1>As you infuse the creature's essence a part of it's essence gets trapped inside a crystal, you quick place the crystal in your backpack.");
		}

		else if (random == 57 && !player.getInventory().containsItem(32072, 1)
				&& !player.getBank().containsItem(32072)
				&& player.getInventory().getFreeSlots() == 0
				&& player.getSkills().getLevelForXp(Skills.SUMMONING) >= 99) {
			player.getBank().addItem(32072, 1, true);
			player.getPackets()
					.sendGameMessage(
							"<col=33e5e1>As you infuse the creature's essence a part of it's essence gets trapped inside a crystal. The crystal has been sent to your bank.");
		}

		ticks--;

		player.getPackets().sendGameMessage(
				"You have successfully infused all your pouches into "
						+ new Item(pouch.getRealPouchId()).getDefinitions()
								.getName() + "s.", true);
		player.faceObject(object);
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		return -1;
	}

	@Override
	public void stop(Player player) {

	}
}