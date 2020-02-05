package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.Item;
import net.nocturne.game.item.actions.Drinkables;
import net.nocturne.game.item.actions.Drinkables.Drink;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.dialogues.Dialogue;

public class FlaskDecantingD extends Dialogue {

	private Drink usedPot;
	private int potionSlot, flaskSlot, ticks;

	// TODO flasks decanting is wrong. this skill dialogue never existed....

	@Override
	public void start() {
		usedPot = (Drink) this.parameters[0];
		potionSlot = (int) this.parameters[1];
		flaskSlot = (int) this.parameters[2];
		player.getActionManager().setAction(new Action() {

			@Override
			public boolean start(Player player) {
				ticks = 1;// SkillsDialogue.getQuantity(player);
				potionSlot = calculatePotionSlot(player);
				calculateFlaskSlot(player);
				if (!checkAll(player)) {
					return false;
				}
				return true;
			}

			private boolean checkAll(Player player) {
				int maxDoses = usedPot.getMaxDoses();
				if (maxDoses > 4) {
					System.out.println("Error in pot: "
							+ usedPot.getIdForDoses(maxDoses));
					return false;
				} else if (flaskSlot == -1) {
					return false;
				} else if (potionSlot == -1) {
					return false;
				} else if (player.getInventory().getItem(flaskSlot).getId() == usedPot.getIdForDoses(usedPot.getMaxDoses())) {
					calculateFlaskSlot(player);
					if (flaskSlot == -1)
						return false;
				} else if (player.getInventory().getItem(potionSlot).getId() == Drinkables.VIAL) {
					potionSlot = calculatePotionSlot(player);
					if (potionSlot == -1)
						return false;
				}
				return true;
			}

			@Override
			public boolean process(Player player) {
				return checkAll(player) && ticks > 0;
			}

			@Override
			public int processWithDelay(Player player) {
				ticks--;
				Item jar = player.getInventory().getItem(flaskSlot);
				Item potion = player.getInventory().getItem(potionSlot);
				if (jar == null || potion == null)
					return -1;
				int requiredDoses = usedPot.getMaxDoses()
						- Drinkables.getDoses(usedPot, jar);
				int potionDoses = Drinkables.getDoses(usedPot, potion);
				int reducedDoses = ((requiredDoses - potionDoses) * -1);
				player.getInventory()
						.getItems()
						.set(potionSlot,
								new Item(
										requiredDoses >= potionDoses ? Drinkables.VIAL
												: usedPot
														.getIdForDoses(reducedDoses),
										1));
				if (requiredDoses > potionDoses) {
					requiredDoses = potionDoses;
				}
				int totalDoses = requiredDoses + Drinkables.getDoses(usedPot, jar);
				Drink flaskFilled = Drink.getFlask(usedPot);
				player.getInventory()
						.getItems()
						.set(flaskSlot,
								new Item(flaskFilled.getIdForDoses(totalDoses > 6 ? 6
										: totalDoses)));
				player.getInventory().refresh(potionSlot, flaskSlot);
				return 1;
			}

			@Override
			public void stop(Player player) {
				setActionDelay(player, 3);
			}

			private int calculatePotionSlot(Player player) {
				for (int ids : usedPot.getId()) {
					int slot = player.getInventory().getItems()
							.getThisItemSlot(ids);
					if (slot != -1)
						return slot;
				}
				return -1;
			}

			private void calculateFlaskSlot(Player player) {
				flaskSlot = player.getInventory().getItems()
						.getThisItemSlot(23191);
			}
		});
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();
	}

	@Override
	public void finish() {

	}
}
