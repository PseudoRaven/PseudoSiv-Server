package net.nocturne.game.player.content;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

public class MaxGuild {

	public static boolean handleObject(final Player player,
			final WorldObject object) {
		switch (object.getId()) {
		case 92279:
			if (player.getVarbits().get(25107) == 32) {
				player.getPackets().sendGameMessage(
						"The golden statue glimmers.");
				player.getEffectsManager().removeEffect(EffectType.POISON);
				player.setHitpoints(player.getMaxHitpoints());
			} else
				player.getPackets().sendGameMessage(
						"Looks like I must find more pieces to this statue.");
			return true;
		case 92337:
			if (player.getSkills().hasLevel(Skills.PRAYER, 99)) {
				player.getVarbits().put(25078, 1);
				player.loadVarbits();
			}
			return true;
		case 92310:
			if (player.getSkills().hasLevel(Skills.DIVINATION, 99)) {
				player.getVarbits().put(25068, 1);
				player.loadVarbits();
			}
			return true;
		case 92313:
			if (player.getSkills().hasLevel(Skills.DUNGEONEERING, 99)) {
				player.getVarbits().put(25069, 1);
				player.loadVarbits();
			}
			return true;
		case 92352:
			if (player.getSkills().hasLevel(Skills.THIEVING, 99)) {
				player.getVarbits().put(25085, 1);
				player.loadVarbits();
			}
			return true;
		case 92355:
			if (player.getSkills().hasLevel(Skills.WOODCUTTING, 99)) {
				player.getVarbits().put(25086, 1);
				player.loadVarbits();
			}
			return true;
		case 92346:
			if (player.getSkills().hasLevel(Skills.SMITHING, 99)) {
				player.getVarbits().put(25082, 1);
				player.loadVarbits();
			}
			return true;
		case 92334:
			if (player.getSkills().hasLevel(Skills.MINING, 99)) {
				player.getVarbits().put(25077, 1);
				player.loadVarbits();
			}
			return true;
		case 92328:
			if (player.getSkills().hasLevel(Skills.HERBLORE, 99)) {
				player.getVarbits().put(25074, 1);
				player.loadVarbits();
			}
			return true;
		case 92325:
			if (player.getSkills().hasLevel(Skills.FLETCHING, 99)) {
				player.getVarbits().put(25073, 1);
				player.loadVarbits();
			}
			return true;
		case 92340:
			if (player.getSkills().hasLevel(Skills.RUNECRAFTING, 99)) {
				player.getVarbits().put(25080, 1);
				player.loadVarbits();
			}
			return true;
		case 92298:
			if (player.getSkills().hasLevel(Skills.AGILITY, 99)) {
				player.getVarbits().put(25061, 1);
				player.loadVarbits();
			}
			return true;
		case 92349:
			if (player.getSkills().hasLevel(Skills.SUMMONING, 99)) {
				player.getVarbits().put(25084, 1);
				player.loadVarbits();
			}
			return true;
		case 92319:
			if (player.getSkills().hasLevel(Skills.FIREMAKING, 99)) {
				player.getVarbits().put(25071, 1);
				player.loadVarbits();
			}
			return true;
		case 92304:
			if (player.getSkills().hasLevel(Skills.COOKING, 99)) {
				player.getVarbits().put(25065, 1);
				player.loadVarbits();
			}
			return true;
		case 92307:
			if (player.getSkills().hasLevel(Skills.CRAFTING, 99)) {
				player.getVarbits().put(25066, 1);
				player.loadVarbits();
			}
			return true;
		case 92331:
			if (player.getSkills().hasLevel(Skills.HUNTER, 99)) {
				player.getVarbits().put(25075, 1);
				player.loadVarbits();
			}
			return true;
		case 92301:
			if (player.getSkills().hasLevel(Skills.CONSTRUCTION, 99)) {
				player.getVarbits().put(25064, 1);
				player.loadVarbits();
			}
			return true;
		case 92316:
			if (player.getSkills().hasLevel(Skills.FARMING, 99)) {
				player.getVarbits().put(25070, 1);
				player.loadVarbits();
			}
			return true;
		case 92343:
			if (player.getSkills().hasLevel(Skills.SLAYER, 99)) {
				player.getVarbits().put(25081, 1);
				player.loadVarbits();
			}
			return true;
		case 92322:
			if (player.getSkills().hasLevel(Skills.FISHING, 99)) {
				player.getVarbits().put(25072, 1);
				player.loadVarbits();
			}
			return true;
		case 92377:
			if (player.getSkills().hasLevel(Skills.STRENGTH, 99)) {
				player.getVarbits().put(25083, 1);
				player.loadVarbits();
			}
			return true;
		case 92374:
			if (player.getSkills().hasLevel(Skills.RANGED, 99)) {
				player.getVarbits().put(25079, 1);
				player.loadVarbits();
			}
			return true;
		case 92371:
			if (player.getSkills().hasLevel(Skills.MAGIC, 99)) {
				player.getVarbits().put(25076, 1);
				player.loadVarbits();
			}
			return true;
		case 92362:
			if (player.getSkills().hasLevel(Skills.ATTACK, 99)) {
				player.getVarbits().put(25062, 1);
				player.loadVarbits();
			}
			return true;
		case 92368:
			if (player.getSkills().hasLevel(Skills.DEFENCE, 99)) {
				player.getVarbits().put(25067, 1);
				player.loadVarbits();
			}
			return true;
		case 92365:
			if (player.getSkills().hasLevel(Skills.HITPOINTS, 99)) {
				player.getVarbits().put(25063, 1);
				player.loadVarbits();
			}
			return true;
		case 92255:
			if (!player.getSkills().isMaxed()) {
				player.getPackets()
						.sendGameMessage(
								"You need to be maxed in order to enter the Max Guild tower.");
				return true;
			}
			for (int x = 25061; x < 25087; x++) {
				int bitValue = player.getVarsManager().getBitValue(x);
				if (bitValue != 1) {
					player.getPackets()
							.sendGameMessage(
									"You must unfurl all the skill banners first in order to enter the Max Guild tower.");
					return true;
				}
			}
			World.sendObjectAnimation(object, new Animation(24576));
			GameExecutorManager.slowExecutor.schedule(() -> {
				World.sendObjectAnimation(object, new Animation(24577));
				if (player.getX() == 2276 && player.getY() == 3318)
					player.addWalkSteps(2276, 3316, -1, false);
				else
					player.addWalkSteps(2276, 3318, -1, false);
			}, 900, TimeUnit.MILLISECONDS);
			GameExecutorManager.slowExecutor.schedule(() -> {
				World.sendObjectAnimation(object, new Animation(24578));
			}, 1400, TimeUnit.MILLISECONDS);
			return true;
		case 92238:
			if (player.getSkills().getHighestSkillLevel() < 99) {
				player.getPackets()
						.sendGameMessage(
								"You require atleast one skill at 99 in order to enter the Max Guild garden.");
				return true;
			}
			World.sendObjectAnimation(object, new Animation(24579));
			if (player.getX() == 2276 && player.getY() == 3338)
				player.addWalkSteps(2276, 3336, -1, false);
			else
				player.addWalkSteps(2276, 3338, -1, false);
			GameExecutorManager.slowExecutor.schedule(() -> World
					.sendObjectAnimation(object, new Animation(24580)), 1000,
					TimeUnit.MILLISECONDS);
			GameExecutorManager.slowExecutor.schedule(() -> {
				World.sendObjectAnimation(object, new Animation(24581));
			}, 1400, TimeUnit.MILLISECONDS);
			return true;
		}
		return false;
	}

	public static void buildStatue(Player player, Item item) {
		if (!player.getVarbits().containsKey(25107))
			player.getVarbits().put(25107, 0);
		int status = player.getVarbits().get(25107);
		switch (status) {
		case 0:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_AGILITY
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_AGILITY, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_AGILITY, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 2);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 2:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_CONSTRUCTION
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_CONSTRUCTION, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_CONSTRUCTION, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 4);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 4:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_CRAFTING
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_CRAFTING, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_CRAFTING, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 6);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 6:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_DIVINATION
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_DIVINATION, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_DIVINATION, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 8);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 8:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_DUNGEONEERING
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_DUNGEONEERING, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_DUNGEONEERING, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 10);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 10:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_FARMING
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_FARMING, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_FARMING, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 12);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 12:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_HERBLORE
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_HERBLORE, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_HERBLORE, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 14);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 14:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_MAGIC
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_MAGIC, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_MAGIC, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 16);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 16:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_MELEE
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_MELEE, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_MELEE, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 18);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 18:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_MINING
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_MINING, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_MINING, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 20);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 20:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_PRAYER
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_PRAYER, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_PRAYER, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 22);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 22:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_RANGED
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_RANGED, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_RANGED, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 24);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 24:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_SLAYER
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_SLAYER, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_SLAYER, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 26);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 26:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_SMITHING
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_SMITHING, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_SMITHING, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 28);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 28:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_SUMMONING
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_SUMMONING, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_SUMMONING, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 30);
				player.getPackets().sendGameMessage(
						"You succcessfully add a piece to the statue.");
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 30:
			if (item.getId() == ItemIdentifiers.GOLDEN_ROCK_WOODCUTTING
					&& player.getInventory().containsItem(
							ItemIdentifiers.GOLDEN_ROCK_WOODCUTTING, 1)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.GOLDEN_ROCK_WOODCUTTING, 1);
				player.setNextAnimation(new Animation(535));
				player.getVarbits().put(25107, 32);
				player.getPackets().sendGameMessage(
						"You succcessfully complete the plith statue.");
				player.getCompCapeManager().increaseRequirement(
						Requirement.GOLDEN_STATUE, 1);
			} else
				player.getPackets().sendGameMessage(
						"This doesn't seem to fit on the statue.");
			break;
		case 32:
			player.getPackets().sendGameMessage(
					"The statue is already complete!");
			break;
		}
		player.loadVarbits();
	}

}
