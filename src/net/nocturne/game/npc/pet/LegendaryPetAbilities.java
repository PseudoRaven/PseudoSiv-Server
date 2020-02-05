package net.nocturne.game.npc.pet;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.item.actions.RepairItems;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.others.Pet;
import net.nocturne.game.npc.vorago.Vorago;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.actions.PlayerCombat;
import net.nocturne.game.player.controllers.Wilderness;
import net.nocturne.utils.Utils;

/**
 * 
 * @author Homeboy927
 *
 */

public class LegendaryPetAbilities {

	/**
	 * 
	 * @param coolDownTime
	 *            Abilities cool down time in seconds
	 * @param lastTimeUsed
	 *            Last time ability was used - Utils.currentTimeMillis()
	 * @return True: ability is on cool down and cannot be used False: the
	 *         ability is ready to be used
	 */
	public static boolean onCoolDown(long coolDownTime, long lastTimeUsed) {
		// convert to milliseconds
		coolDownTime *= 1000;
		if (Utils.currentTimeMillis() - lastTimeUsed > coolDownTime)
			return false;
		return true;
	}

	/**
	 * 
	 * @param lastTimeUsed
	 *            Last time the ability was used - Utils.currentTimeMillis()
	 * @param coolDownTime
	 *            Abilities cooldown time in seconds
	 * @return The amount of time left until the ability is ready to be used (in
	 *         seconds)
	 */
	public static long getRemainingTime(long coolDownTime, long lastTimeUsed) {
		return coolDownTime
				- ((Utils.currentTimeMillis() - lastTimeUsed) / 1000);
	}

	// id - min - max
	private static int rewards[][] = { { 995, 5000, 50000 },// coins
			{ 200, 10, 20 },// grimy guam
			{ 202, 10, 20 },// grimy marrentill
			{ 204, 10, 20 },// grimy tarromin
			{ 206, 10, 20 },// grimy harralander
			{ 208, 10, 20 },// grimy ranarr
			{ 210, 10, 20 },// grimy irit
			{ 212, 10, 15 },// grimy avantoe
			{ 527, 10, 25 },// bones
			{ 533, 5, 25 },// big bones
			{ 535, 5, 25 },// babydragon bones
			{ 537, 5, 15 },// dragon bones
			{ 18831, 1, 15 },// frost dragon bones
	};

	public static void petVampyrism(Player player, Pet pet, Hit hit) {
		if (checkWilderness(player))
			return;
		double healAmount = (int) (hit.getDamage() * (getExecuteAmount(player)));
		player.heal((int) healAmount);
		// Hit heal = new Hit(null,(int)healAmount,HitLook.HEALED_DAMAGE);
		// heal.setHealHit();
		// player.applyHit(heal);
	}

	/**
	 * 
	 * @param player
	 * @param pet
	 * @return True: the pet found an item, does not matter if it gets added to
	 *         the players inventory False: the ability is on cooldown - should
	 *         never happen though because of how this method is called
	 */
	public static boolean petForge(Player player, Pet pet) {
		if (!onCoolDown(7200, pet.getPetItemForge())) {
			int index = Utils.random(rewards.length);
			Item reward = new Item(rewards[index][0], Utils.random(
					rewards[index][1], rewards[index][2]));
			if (player.getInventory().getFreeSlots() == 0) {
				player.getPackets()
						.sendGameMessage(
								"Your pet found you some items but had to drop them because your inventory was full.");
				World.addGroundItem(reward, pet, player, false, 100);
			} else {
				player.getInventory().addItem(reward);
				player.getPackets().sendGameMessage(
						"Your pet found " + reward.getName() + " X "
								+ reward.getAmount());
			}
			pet.forageCount++;
			player.petForageItems++;
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private static int getDonatorCoolDownTime(Player player, int time) {
		if (player.getDonationManager().isHeroicDonator())
			return (int) (time - (time * 0.35));// 35%
		else if (player.getDonationManager().isDemonicDonator())
			return (int) (time - (time * 0.25));// 25%
		else if (player.getDonationManager().isAngelicDonator())
			return (int) (time - (time * 0.20));// 20%
		else if (player.getDonationManager().isDivineDonator())
			return (int) (time - (time * 0.10));// take 10% off
		else if (player.getDonationManager().isSupremeDonator())
			return time;
		// person is none of those donator ranks
		// should not happen??
		return time;
	}

	private static double getExecuteAmount(Player player) {
		if (player.getDonationManager().isHeroicDonator())
			return 0.16;// 50%
		else if (player.getDonationManager().isDemonicDonator())
			return 0.13;// 40%
		else if (player.getDonationManager().isAngelicDonator())
			return 0.1;// 35%
		else if (player.getDonationManager().isDivineDonator())
			return 0.07;// 30%
		else if (player.getDonationManager().isSupremeDonator())
			return 0.04;// 20%
		// person is none of those donator ranks
		// should not happen??
		return 1;
	}

	private static boolean checkWilderness(Player player) {
		if (Wilderness.isAtWild(player))
			return true;
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param pet
	 * @return True: Npc was executed False: could not use execute ability
	 */
	public static boolean petNPCExecute(Player player, Pet pet) {
		if (!onCoolDown(3600, pet.getPetNPCExecute())) {
			Action action = player.getActionManager().getAction();
			if (action instanceof PlayerCombat) {
				Entity target = ((PlayerCombat) action).getTarget();
				if (target instanceof Player) {
					player.getPackets().sendGameMessage(
							"Your pet cannot execute players!");
					return false;
				} else if (target instanceof NPC) {
					NPC npc = (NPC) target;
					if (npc instanceof Vorago) {
						player.getPackets().sendGameMessage(
								"Your pet cannot execute this.");
						return false;
					}
					double hpToExecute = npc.getMaxHitpoints()
							* getExecuteAmount(player);
					if (npc.getHitpoints() <= hpToExecute) {
						player.getPackets().sendGameMessage(
								"Your pet executed the " + npc.getName() + ".");
						int temp = npc.getCapDamage();
						npc.setCapDamage(npc.getMaxHitpoints());
						npc.applyHit(new Hit(player, npc.getHitpoints(),
								HitLook.REGULAR_DAMAGE));
						npc.setCapDamage(temp);
						return true;
					} else {
						player.getPackets().sendGameMessage(
								"The npc is not hurt enough to be executed!");
						player.getPackets().sendGameMessage(
								"The npc must be less than "
										+ getExecuteAmount(player) * 100
										+ "% hp to execute.");
						return false;
					}
				} else {
					player.getPackets().sendGameMessage(
							"Your pet cannot execute this.");
					return false;
				}
			}
		} else {
			player.getPackets().sendGameMessage(
					"You can use this ability again in "
							+ getRemainingTime(3600, pet.getPetNPCExecute())
							/ 60 + " minutes.");
			return false;
		}
		player.getPackets().sendGameMessage("You have no target to execute.");
		return false;
	}

	/**
	 * 
	 * @param player
	 *            Pet owner
	 * @param item
	 *            Item being banked
	 * @param pet
	 *            The legendary pet
	 * @return True: Item was banked False: Item was not banked - will only
	 *         happen if the ability is on cooldown
	 */
	public static boolean petBank(Player player, Item item, Pet pet) {
		if (!onCoolDown(300, pet.getPetItemBank())) {
			player.getBank().addItem(item, true);
			player.getInventory().deleteItem(item);
			player.getPackets().sendGameMessage(
					"Your Pet banked: " + item.getName() + " X "
							+ item.getAmount() + ".");
			return true;
		} else {
			player.getPackets().sendGameMessage(
					"You can use this ability again in "
							+ getRemainingTime(300, pet.getPetItemBank())
							+ " seconds.");
			return false;
		}
	}

	/**
	 * 
	 * @param player
	 *            Pet owner
	 * @param item
	 *            item that needs to be repaired
	 * @param pet
	 *            The legendary pet
	 * @return True: the item was repaired False: item was not repaired
	 */
	public static boolean petRepair(Player player, Item item, Pet pet) {
		if (!onCoolDown(3600, pet.getPetItemRepair())) {
			int repairPrice = RepairItems
					.getDegradedPrice(player, item.getId()) / 2;
			if (player.getInventory().containsItem(995, repairPrice)) {
				int itemId = item.getId();
				player.getInventory().deleteItem(itemId, 1);
				player.getInventory().removeItemMoneyPouch(995, repairPrice);
				player.getInventory().addItemMoneyPouch(
						ItemConstants.getItemFixed(itemId), 1);
				ItemDefinitions iDef = ItemDefinitions
						.getItemDefinitions(ItemConstants.getItemFixed(itemId));
				player.getPackets().sendGameMessage(
						"Your pet has repaired your " + iDef.getName()
								+ " for price of " + repairPrice + ".");
				return true;
			} else {
				player.getPackets().sendGameMessage(
						"You do not have enough money to repair this. You need "
								+ repairPrice + "gp.");
			}
		} else {
			player.getPackets().sendGameMessage(
					"You can use this ability again in "
							+ getRemainingTime(3600, pet.getPetItemRepair())
							/ 60 + " minutes.");
		}
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param pet
	 * @return True: Player was saved by pet False: Player was not saved
	 */
	public static boolean petLifeSaver(Player player, Pet pet) {
		if (!onCoolDown(3600, pet.getPetLifeSaver())) {
			player.setHitpoints(1);
			player.setNextWorldTile(new WorldTile(2208, 3369, 1));
			player.getPackets().sendGameMessage(
					"Your pet was able to save you from death.");
			return true;
		}
		player.getPackets().sendGameMessage(
				"Your pet was not able to save you.");
		player.getPackets().sendGameMessage(
				"Your pet can save you again in "
						+ getRemainingTime(3600, pet.getPetItemRepair()) / 60
						+ " minutes.");
		return false;
	}

	public static void petAddBoBPouch(Player player, Item item, Pet pet,
			int slots) {
		player.getPackets().sendGameMessage(
				"Your pet consumes the " + item.getName() + ".");
		player.getInventory().deleteItem(item);
		if (pet.BoBTime == 0) {
			pet.BoBTime = Utils.currentTimeMillis();
			pet.bob = new LegendaryPetBoB(slots);
			pet.bob.setEntity(player, pet);
		} else {
			player.getPackets().sendGameMessage(
					"The time of your BoB ability has been extended.");
			pet.BoBTime = Utils.currentTimeMillis();
		}
		pet.activeBoB = true;
	}

	/**
	 * 
	 * @param player
	 *            Pet owner
	 * @param item
	 *            Item being alched
	 * @param pet
	 *            Legendary Pet
	 * @return True: Alched the item False: Could not alch item so no need to
	 *         put the ability on cool down
	 */
	public static boolean petAlch(Player player, Item item, Pet pet) {
		// The user needs to have the correct magic level even though they are
		// using the pet to alch
		if (player.getSkills().getLevel(Skills.MAGIC) < 55) {
			player.getPackets().sendGameMessage(
					"You need a magic level of 55 or higher to use this.");
			return false;
		}
		if (!onCoolDown(300, pet.getPetHighAlchemy())) {
			if (ItemConstants.isDungItem(item.getId()))
				return false;
			if ((!ItemConstants.isTradeable(item)) || item.getId() == 995) {
				player.getPackets().sendGameMessage("That isn't alchable!");
				return false;
			}
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(item
					.getId());
			if (def == null)
				return false;
			if (def.getName().toLowerCase().contains("master")) {
				player.getPackets()
						.sendGameMessage("You can't alch this item.");
				return false;
			}
			int amount = item.getDefinitions().getPrice();
			player.getInventory().deleteItem(item.getId(), 1);
			player.getInventory().addItemMoneyPouch(995, amount / 3);
			player.getPackets().sendGameMessage(
					"Your pet Alchs the " + item.getName() + ".");
			return true;
		} else {
			player.getPackets().sendGameMessage(
					"You can use this ability again in "
							+ getRemainingTime(300, pet.getPetHighAlchemy())
							+ " seconds.");
			return false;
		}
	}

}