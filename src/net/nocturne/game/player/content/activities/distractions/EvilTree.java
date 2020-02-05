package net.nocturne.game.player.content.activities.distractions;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.nocturne.Engine;
import net.nocturne.Settings;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.TemporaryAtributtes.Key;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.actions.skills.woodcutting.Woodcutting;
import net.nocturne.game.player.actions.skills.woodcutting.Woodcutting.HatchetDefinitions;
import net.nocturne.game.player.content.activities.events.WorldEvents;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Color;
import net.nocturne.utils.Logger;
import net.nocturne.utils.Utils;

/**
 * @author Miles Black (bobismyname)
 * 
 */

public class EvilTree {

	public static EvilTreeDefinitions tree;
	private static HatchetDefinitions hatchet;
	private final static WorldObject deadTree = new WorldObject(12715, 10, 0,
			2456, 2835, 0);

	public static enum EvilTreeDefinitions {

		EVIL_NORMAL_TREE("Evil Tree", 11435, 1, 1, 1, 15.1, 20.0, 200.0, 200),

		EVIL_OAK_TREE("Oak Evil Tree", 11437, 15, 7, 15, 32.4, 45.0, 300.0, 500),

		EVIL_WILLOW_TREE("Willow Evil Tree", 11441, 30, 15, 30, 45.7, 66.0,
				450.0, 800),

		EVIL_MAPLE_TREE("Maple Evil Tree", 11444, 45, 22, 45, 55.8, 121.5,
				675.0, 1000),

		EVIL_YEW_TREE("Yew Evil Tree", 11916, 60, 30, 60, 64.4, 172.5, 1012.5,
				1500),

		EVIL_MAGIC_TREE("Magic Evil Tree", 11919, 75, 37, 75, 70.9, 311.0,
				1517.5, 3000),

		EVIL_ELDER_TREE("Elder Evil Tree", 11922, 90, 42, 90, 77.8, 687.5,
				2000.5, 5000);

		private String treeName;
		private int id;
		private int health;
		private int woodcuttingLevel;
		private int farmingLevel;
		private int firemakingLevel;
		private double woodcuttingXp;
		private double farmingXp;
		private double firemakingXp;

		private EvilTreeDefinitions(String treeName, int id,
				int woodcuttingLevel, int farmingLevel, int firemakingLevel,
				double woodcuttingXp, double farmingXp, double firemakingXp,
				int health) {
			this.treeName = treeName;
			this.id = id;
			this.health = health;
			this.woodcuttingLevel = woodcuttingLevel;
			this.farmingLevel = farmingLevel;
			this.firemakingLevel = firemakingLevel;
			this.woodcuttingXp = woodcuttingXp;
			this.farmingXp = farmingXp;
			this.firemakingXp = firemakingXp;
		}

		public String getTreeName() {
			return treeName;
		}

		public int getId() {
			return id;
		}

		public int getHealth() {
			return health;
		}

		public void decreaseHealth(int amt) {
			health -= amt;
			if (health <= 0
					&& !WorldEvents.EVIL_TREE_STATUS
							.equalsIgnoreCase("The Evil Tree is currently dead!")) {
				World.spawnObjectTemporary(deadTree, 600000, true, false);
				WorldEvents.EVIL_TREE_STATUS = "The Evil Tree is currently dead!";
				World.sendWorldMessage(
						"<img=6><col=FFA500><shad=000000>The Evil Tree has been defeated!",
						false);
				if (Settings.HOSTED)
					Engine.getDiscordBot()
							.getChannel("264440233129541632")
							.sendMessage(
									"[Evil Tree] The Evil Tree has been defeated!");
			}
		}

		public int getWoodcuttingLevel() {
			return woodcuttingLevel;
		}

		public int getFarmingLevel() {
			return farmingLevel;
		}

		public int getFiremakingLevel() {
			return firemakingLevel;
		}

		public double getWoodcuttingXp() {
			return woodcuttingXp;
		}

		public double getFarmingXp() {
			return farmingXp;
		}

		public double getFiremakingXp() {
			return firemakingXp;
		}
	}

	public static void init() {
		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					if (tree == null || tree.getHealth() <= 0) {
						tree = EvilTreeDefinitions.values()[new Random()
								.nextInt(EvilTreeDefinitions.values().length)];
						WorldObject evilTree = new WorldObject(tree.getId(),
								10, 0, 2456, 2835, 0);
						World.removeObject(deadTree);
						World.spawnObject(evilTree, true);
						WorldEvents.EVIL_TREE_STATUS = "The Evil Tree is currently alive!";
						World.sendWorldMessage(
								"<img=6><col=FFA500><shad=000000>An Evil Tree has appeared nearby Mobilizing Armies, talk to a Spirit Tree to reach it!",
								false);
						if (Settings.HOSTED)
							Engine.getDiscordBot()
									.getChannel("264440233129541632")
									.sendMessage(
											"[Evil Tree] An Evil Tree has appeared nearby Mobilizing Armies, talk to a Spirit Tree to reach it!");
					}
				} catch (final Throwable e) {
					Logger.handle(e);
				}
			}
		}, Utils.random(60, 90), 180, TimeUnit.MINUTES);
	}

	private static int maxKindleAllowed(Player player) {
		int amount = 0;
		switch (player.getDonationManager().getDonatorTitle()) {
		case "Regular":
			amount = 300;
			break;
		case "Extreme":
			amount = 400;
			break;
		case "Legendary":
			amount = 500;
			break;
		case "Supreme":
			amount = 600;
			break;
		case "Divine":
			amount = 700;
			break;
		case "Angelic":
			amount = 800;
			break;
		case "Demonic":
			amount = 900;
			break;
		case "Heroic":
			amount = 1000;
			break;
		case "Non":
			amount = 200;
			break;

		}

		return amount;

	}

	private static boolean canGetKindle(Player player) {
		int maxKindle = maxKindleAllowed(player);
		int currentKindle = player.getInventory().getAmountOf(
				ItemIdentifiers.EVIL_TREE_KINDLING);

		return currentKindle < maxKindle;

	}

	private static void chopRoot(final Player player, final WorldObject object) {
		if (!canInteract(player, object))
			return;

		if (player.getSkills().getLevel(Skills.WOODCUTTING) < tree
				.getWoodcuttingLevel()) {
			player.getPackets().sendGameMessage(
					Color.OLIVE,
					"You must have a woodcutting level of "
							+ tree.getWoodcuttingLevel()
							+ " in order to do this.");
			return;
		}
		hatchet = Woodcutting.getHatchet(player, false);
		final WorldObject movingRoot = new WorldObject(11426, 10, 0,
				object.getX(), object.getY(), 0);

		player.getTemporaryAttributtes().putIfAbsent(Key.ROOTING_TREE, false);

		if (!(boolean) player.getTemporaryAttributtes().get(Key.ROOTING_TREE)) {
			if (hatchet != null) {
				player.getTemporaryAttributtes().put(Key.ROOTING_TREE, true);
				player.getActionManager().setAction(new Action() {

					int loop = 0;
					final int emote = hatchet.getEmoteId();

					@Override
					public boolean process(Player player) {
						return true;
					}

					@Override
					public int processWithDelay(Player player) {
						if (!canInteract(player, object) || !player.isLocked()) {
							player.getTemporaryAttributtes().put(
									Key.ROOTING_TREE, false);
							player.stopAll();
							player.unlock();
							return -1;
						}

						int amount = canGetKindle(player) ? Utils.random(1, 5)
								: 0;
						switch (loop) {
						case 0:
							player.getPackets().sendGameMessage(Color.OLIVE,
									"You begin to chop the root.");
							handleRootCut(player, amount, emote);
							break;
						case 1:
							player.getPackets().sendGameMessage(Color.OLIVE,
									"The root begins to weaken.");
							handleRootCut(player, amount, emote);
							break;
						case 2:
							player.getPackets().sendGameMessage(Color.OLIVE,
									"The root begins to give way.");
							handleRootCut(player, amount, emote);
							break;
						case 3:
							player.getPackets().sendGameMessage(Color.OLIVE,
									"The root shrivels up.");
							handleRootCut(player, amount, emote);
							break;
						case 4:
							player.getPackets().sendGameMessage(Color.OLIVE,
									"You successfully chop down the root.");
							World.removeObject(movingRoot);
							World.removeObject(object);
							player.getSkills().addXp(Skills.WOODCUTTING,
									tree.getWoodcuttingXp());
							player.getTemporaryAttributtes().put(
									Key.ROOTING_TREE, false);
							player.unlock();
							return -1;
						}
						loop++;
						return 2;
					}

					@Override
					public boolean start(Player player) {
						player.lock();
						return hatchet.getEmoteId() > -1;
					}

					@Override
					public void stop(Player player) {
						player.getTemporaryAttributtes().put(Key.ROOTING_TREE,
								false);
						player.unlock();
					}

				});
			} else {
				player.getPackets().sendGameMessage(Color.OLIVE,
						"You need an axe to chop this Evil Tree down.");
			}
		} else {
			player.getPackets().sendGameMessage(Color.OLIVE,
					"You are already chopping this root.");
		}
	}

	private static void handleRootCut(Player player, int amount, int emote) {
		player.setNextAnimation(new Animation(emote));
		if (amount == 0)
			return;
		player.getInventory().addItemMoneyPouch(
				ItemIdentifiers.EVIL_TREE_KINDLING, amount);
	}

	private static boolean canInteract(Player player, WorldObject object) {
		return object.withinDistance(player.getTile());
	}

	private static void chopTree(final Player player, final WorldObject object) {
		if (!canInteract(player, object))
			return;

		if (player.getSkills().getLevel(Skills.WOODCUTTING) < tree
				.getWoodcuttingLevel()) {
			player.getPackets().sendGameMessage(
					Color.OLIVE,
					"You must have a woodcutting level of "
							+ tree.getWoodcuttingLevel()
							+ " in order to do this.");
			return;
		}
		hatchet = Woodcutting.getHatchet(player, false);

		player.getTemporaryAttributtes().putIfAbsent(Key.CHOPPING_TREE, false);

		if (!player.getTemporaryAttributtes().contains(Key.CHOPPING_TREE))
			player.getTemporaryAttributtes().put(Key.CHOPPING_TREE, false);
		if (!(boolean) player.getTemporaryAttributtes().get(Key.CHOPPING_TREE)) {
			rootAttack(player);
			if (hatchet != null) {
				player.getTemporaryAttributtes().put(Key.CHOPPING_TREE, true);
				player.getActionManager().setAction(new Action() {

					@Override
					public boolean process(Player player) {
						return true;
					}

					@Override
					public int processWithDelay(Player player) {
						int emote = hatchet.getEmoteId();
						if (tree.getHealth() <= 0)
							return -1;
						player.getTemporaryAttributtes().put(Key.CHOPPING_TREE,
								false);
						if (!canInteract(player, object)) {
							player.getTemporaryAttributtes().put(
									Key.CHOPPING_TREE, false);
							return -1;
						}
						player.getPackets().sendGameMessage(Color.OLIVE,
								"You chop away some of the evil tree.", true);
						player.setNextAnimation(new Animation(emote));
						tree.decreaseHealth(1);
						player.getSkills().addXp(Skills.WOODCUTTING,
								tree.getWoodcuttingXp());
						if (!player.getTemporaryAttributtes().contains(
								Key.EVIL_TREE_DAMAGE))
							player.getTemporaryAttributtes().put(
									Key.EVIL_TREE_DAMAGE, 0);
						player.getTemporaryAttributtes().put(
								Key.EVIL_TREE_DAMAGE,
								(int) player.getTemporaryAttributtes().get(
										Key.EVIL_TREE_DAMAGE) + 1);
						player.getCompCapeManager().increaseRequirement(
								Requirement.EVIL_TREE, 1);
						return 2;
					}

					@Override
					public boolean start(Player player) {
						return hatchet.getEmoteId() > -1;
					}

					@Override
					public void stop(Player player) {
						player.getTemporaryAttributtes().put(Key.CHOPPING_TREE,
								false);
					}

				});
			} else {
				player.getPackets().sendGameMessage(Color.OLIVE,
						"You need an axe to chop this Evil Tree down.");
			}
		} else {
			player.getPackets().sendGameMessage(Color.OLIVE,
					"You are already chopping the evil tree.");
		}
	}

	public static boolean handleObject(Player player, final WorldObject object,
			int option) {
		switch (object.getId()) {
		case 11435:
		case 11437:
		case 11441:
		case 11444:
		case 11916:
		case 11919:
		case 11922:
			if (option == 1) {
				if (player.getTemporaryAttributtes().containsKey(
						Key.CHOPPING_TREE)
						&& (boolean) player.getTemporaryAttributtes().get(
								Key.CHOPPING_TREE)) {
					player.getPackets().sendGameMessage(
							"You are already doing this action right now.");
					return true;
				}
				chopTree(player, object);
			} else if (option == 2) {

				if (player.getTemporaryAttributtes().containsKey(
						Key.BURNING_TREE)
						&& (boolean) player.getTemporaryAttributtes().get(
								Key.BURNING_TREE)) {
					player.getPackets().sendGameMessage(
							"You are already doing this action right now.");
					return true;
				}
				lightTree(player, object);
			} else if (option == 3) {

				player.getTemporaryAttributtes().putIfAbsent(
						Key.EVIL_TREE_DAMAGE, 0);

				player.getPackets().sendGameMessage(
						Color.OLIVE,
						"The tree's health is currently at "
								+ tree.getHealth()
								+ ", you have dealt "
								+ (int) player.getTemporaryAttributtes().get(
										Key.EVIL_TREE_DAMAGE)
								+ " damage to the Evil Tree.");
			}
			return true;
		case 11426:
			if (player.getTemporaryAttributtes().containsKey(Key.ROOTING_TREE)
					&& (boolean) player.getTemporaryAttributtes().get(
							Key.ROOTING_TREE)) {
				player.getPackets().sendGameMessage(
						"You are already doing this action right now.");
				return true;
			}
			chopRoot(player, object);
			return true;
		case 12715:
			player.getPackets()
					.sendGameMessage(
							"The evil tree is dead, I should speak to the lumberjack for a reward!");
			return true;
		}
		return false;
	}

	private static void lightTree(final Player player, final WorldObject object) {
		if (!canInteract(player, object))
			return;

		if (player.getSkills().getLevel(Skills.FIREMAKING) < tree
				.getFiremakingLevel()) {
			player.getPackets().sendGameMessage(
					Color.OLIVE,
					"You must have a firemaking level of "
							+ tree.getFiremakingLevel()
							+ " in order to do this.");
			return;
		}

		player.getTemporaryAttributtes().putIfAbsent(Key.BURNING_TREE, false);

		if (!(boolean) player.getTemporaryAttributtes().get(Key.BURNING_TREE)) {
			if (player.getInventory().containsItemToolBelt(590, 1)) {
				if (player.getInventory().containsItem(
						ItemIdentifiers.EVIL_TREE_KINDLING, 1)) {
					player.lock(4);
					WorldTasksManager.schedule(new WorldTask() {
						int loop = 0;

						@Override
						public void run() {
							if (tree.getHealth() <= 0)
								stop();
							switch (loop) {

							case 0:
								player.getTemporaryAttributtes().put(
										Key.BURNING_TREE, true);
								player.getPackets()
										.sendGameMessage(Color.OLIVE,
												"You begin to set the evil tree on fire...");
								player.setNextAnimation(new Animation(16700));
								break;
							case 1:
								player.getPackets()
										.sendGameMessage(Color.OLIVE,
												"You successfully catch the tree on fire!");
								break;
							case 2:
								if (!canInteract(player, object)
										|| player.hasTeleported()) {
									player.getTemporaryAttributtes().put(
											Key.BURNING_TREE, false);
									stop();
									return;
								}
								player.getPackets()
										.sendGameMessage(
												Color.OLIVE,
												"The fire continues to burn on the evil tree.",
												true);
								player.getInventory().deleteItem(
										ItemIdentifiers.EVIL_TREE_KINDLING, 1);
								tree.decreaseHealth(fireDamage);
								player.getSkills().addXp(Skills.FIREMAKING,
										tree.getFiremakingXp());
								// TODO causing a null
								// player.getTemporaryAttributtes().put(Key.EVIL_TREE_DAMAGE,
								// (int)
								// player.getTemporaryAttributtes().get(Key.EVIL_TREE_DAMAGE)
								// + fireDamage);
								// player.getCompCapeManager().increaseRequirement(Requirement.EVIL_TREE,
								// fireDamage);
								break;
							case 3:
								player.getTemporaryAttributtes().put(
										Key.BURNING_TREE, false);
								player.getPackets()
										.sendGameMessage(Color.OLIVE,
												"You have run out of kindel to fuel the fire.");
								stop();
								break;

							}
							if (player.getInventory().containsItem(
									ItemIdentifiers.EVIL_TREE_KINDLING, 1))
								loop = 1;
							loop++;
						}
					}, 0, 1);
				} else
					player.getPackets()
							.sendGameMessage(Color.OLIVE,
									"You need some evil tree kindling in order to light this on fire.");
			} else
				player.getPackets().sendGameMessage(Color.OLIVE,
						"You need a tinderbox to burn the Evil Tree down.");
		} else
			player.getPackets().sendGameMessage(Color.RED,
					"You are already lighting this evil tree on fire.");
	}

	public static void processReward(Player player) {
		if ((int) player.getTemporaryAttributtes().get(Key.EVIL_TREE_DAMAGE) <= 0)
			player.getPackets()
					.sendGameMessage(Color.OLIVE,
							"You did not do enough damage to the Evil Tree to receive a reward.");
		else {
			if ((int) player.getTemporaryAttributtes()
					.get(Key.EVIL_TREE_DAMAGE) > 450)
				player.getTemporaryAttributtes().put(Key.EVIL_TREE_DAMAGE, 450);
			final int chance = Utils.random(500 - (int) player
					.getTemporaryAttributtes().get(Key.EVIL_TREE_DAMAGE));
			if (chance >= 0 && chance <= 10) {
				final int blister = Utils.random(6);
				if (blister == 1)
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.BLISTERWOOD_STAFF, 1);
				else if (blister == 2)
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.BLISTERWOOD_STAKE, 100);
				else if (blister == 3)
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.BLISTERWOOD_POLEARM, 1);
			} else if (chance >= 11 && chance <= 50) {
				final int lumber = Utils.random(1, 5);
				switch (lumber) {
				case 1:
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.LUMBERJACK_BOOTS, 1);
					break;
				case 2:
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.LUMBERJACK_TOP, 1);
					break;
				case 3:
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.LUMBERJACK_LEGS, 1);
					break;
				case 4:
					player.getInventory().addItemMoneyPouch(
							ItemIdentifiers.LUMBERJACK_HAT, 1);
					break;
				case 5:
					player.getInventory().addItemMoneyPouch(10945, 1);
				}
				player.getInventory().addItemMoneyPouch(
						ItemIdentifiers.COINS,
						(int) player.getTemporaryAttributtes().get(
								Key.EVIL_TREE_DAMAGE) * 1000); // Cash
				player.getInventory().addItemMoneyPouch(
						ItemIdentifiers.MAGIC_LOGS_NOTED, Utils.random(10, 20)); // Start
				player.getInventory().addItemMoneyPouch(
						ItemIdentifiers.YEW_LOGS_NOTED, Utils.random(10, 30));
				player.getInventory().addItemMoneyPouch(
						ItemIdentifiers.MAPLE_LOGS_NOTED, Utils.random(10, 40));
				player.getInventory()
						.addItemMoneyPouch(ItemIdentifiers.WILLOW_LOGS_NOTED,
								Utils.random(10, 50));
				player.getInventory().addItemMoneyPouch(
						ItemIdentifiers.OAK_LOGS_NOTED, Utils.random(10, 60)); // End
				player.getTemporaryAttributtes().put(Key.EVIL_TREE_DAMAGE, 0);
			}
		}
	}

	private static void rootAttack(final Player player) {
		final int attack = Utils.random(7);
		final int rootX = player.getX();
		final int rootY = player.getY();
		final WorldObject movingRoot = new WorldObject(11426, 10, 0, rootX,
				rootY, 0);
		if (attack == 1) {
			WorldTasksManager.schedule(new WorldTask() {
				int loop = 0;

				@Override
				public void run() {
					if (loop == 0) {
						World.spawnObject(movingRoot, true);
					} else if (loop == 1) {
						player.lock(1);
						if (player.getX() > 2456) {
							player.addWalkSteps(player.getX() + 1,
									player.getY(), 0, false);
						} else if (player.getX() < 2456) {
							player.addWalkSteps(player.getX() - 1,
									player.getY(), 0, false);
						} else if (player.getY() > 2835) {
							player.addWalkSteps(player.getX(),
									player.getY() + 1, 0, false);
						} else if (player.getY() < 2835) {
							player.addWalkSteps(player.getX(),
									player.getY() - 1, 0, false);
						}
						player.setNextAnimation(new Animation(846));
						stop();
					}
					loop++;
				}
			}, 0, 1);
		}
	}

	public static int time, fireDamage;

}
