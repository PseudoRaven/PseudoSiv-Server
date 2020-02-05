package net.nocturne.game.item.actions;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.game.Animation;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;

public class AlcoholicBeverages {

	private enum Drinks {

		BEER(1917, -1, "Mmm...beer!"),

		CIDER(5763, -1, "Mmm...cider!"),

		ASGARNIAN_ALE(1905, 52, "That hit the good spots!"),

		AXEMANS_FOLLY(5751, 52, "Damn that was awesome!"),

		BANDITS_BREW(4627, 52, "This place is just great!"),

		DWARVEN_STOUT(1913, 52, "I could use another one of those!"),

		MOONLIGHT_MEAD(2955, 52, "*hic* that was alright *hic*"),

		PIGSWILL(28659, 52, "*glug*"),

		RANGERS_AID(15119, 52, "That was the shit!"),

		SLAYERS_RESPITE(5759, 52, "WOOOO HOOOO!"),

		GREENMANS_ALE(1909, 52, "I need another one of those!"),

		CHEFS_DELIGHT(5755, 52, "That packed a punch!"),

		DRAGON_BITTER(1911, 290, "Holy shit, that was intense!"),

		WIZARD_MINDBOMB(1907, -1, "That was weak!"),

		KEG_OF_BEER(3801, -1, "I gotta have some more!"),

		ANOTHER_BEER(3803, -1, "I should stop already!"),

		RUM(431, -1, "That was delicious!");

		public static Drinks drinkForId(int id) {
			return drink.get(id);
		}

		private final int drinkId, renderAnimation;

		private final String shout;

		private static Map<Integer, Drinks> drink = new HashMap<>();

		static {
			for (final Drinks drinks : Drinks.values()) {
				drink.put(drinks.drinkId, drinks);
			}
		}

		Drinks(int drinkId, int renderAnimation, String shout) {
			this.drinkId = drinkId;
			this.renderAnimation = renderAnimation;
			this.shout = shout;
		}
	}

	public static boolean drink(final Player player, int slot) {
		final Item item = player.getInventory().getItem(slot);
		if (item == null || Drinks.drinkForId(item.getId()) == null)
			return false;
		final Drinks drink = Drinks.drinkForId(item.getId());
		player.lock();
		player.getInventory().deleteItem(item.getId(), 1);
		if (drink == Drinks.KEG_OF_BEER)
			player.setNextAnimation(new Animation(1330));
		else
			player.setNextAnimation(new Animation(1327));
		player.getPackets().sendGameMessage(
				"You drink the "
						+ item.getDefinitions().getName().toLowerCase() + ".");
		if (drink.renderAnimation != -1)
			player.getAppearence().setRenderEmote(drink.renderAnimation);
		player.setNextForceTalk(new ForceTalk(drink.shout));
		player.unlock();
		return true;
	}
}