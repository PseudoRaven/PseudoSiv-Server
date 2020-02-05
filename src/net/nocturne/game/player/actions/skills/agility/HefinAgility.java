package net.nocturne.game.player.actions.skills.agility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.ItemsContainer;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.agility.Agility.AgilityCourses;
import net.nocturne.game.player.cutscenes.Cutscene;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class HefinAgility {

	private final ItemsContainer<Item> rewards = new ItemsContainer<>(2, true);

	public static boolean handleObject(Player player, final WorldObject object) {
		int id = object.getId();
		switch (id) {
		case 94056:
			player.lock(8);
			vault(player, object);
			return true;
		case 94057:
			player.lock(8);
			slideDown(player, object);
			return true;
		case 94055:
			player.lock(8);
			scale(player, object);
			return true;
		case 94051:
			player.lock(8);
			WorldTasksManager.schedule(new WorldTask() {
				boolean secondloop;

				@Override
				public void run() {
					if (!secondloop) {
						secondloop = true;
						player.addWalkSteps(2180, 3420);
					} else {
						player.lock(8);
						traverse(player, object);
						stop();
					}
				}
			}, 0, 2);
			return true;
		case 94050:
			player.getTemporaryAttributtes().putIfAbsent("HefinStage", 0);

			WorldTasksManager.schedule(new WorldTask() {
				boolean secondloop;

				@Override
				public void run() {
					if (!secondloop) {
						secondloop = true;
						player.addWalkSteps(2177, 3402);
					} else {
						player.lock(8);
						leapAcross(player, object);
						stop();
					}
				}
			}, 0, 2);
			return true;
		default:
			return false;
		}
	}

	public static WorldTile getWorldTile(Player player, int mapX, int mapY) {
		return new WorldTile(player.getX(), player.getY(), 1);
	}

	public static void merge(final Player player) {

		final WorldTile TurnTile1 = new WorldTile(2187, 3444, 2);
		final WorldTile NextTile = new WorldTile(2176, 3400, 1);

		if (player.getSkills().getLevel(Skills.AGILITY) < 77) {
			player.getPackets().sendGameMessage(
					"You need atleast level 77 agility to jump on this.");
			return;
		}

		player.getTemporaryAttributtes().putIfAbsent("HefinStage", 0);

		if ((int) player.getTemporaryAttributtes().get("HefinStage") == 5) {
			player.getPackets()
					.sendGameMessage(
							"You merge with the light creature and complete the Hefin Agility Course.",
							true);
			player.setNextFaceWorldTile(TurnTile1);
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					player.getTemporaryAttributtes().put("HefinStage", 0);
					time++;
					if (time == 1) {
						player.setNextAnimation(new Animation(17893));
					}
					if (time == 4) {
						player.setNextWorldTile(NextTile);
					}
					if (time == 5) {
						player.setNextAnimation(new Animation(17894));
						handleXP(player, true);
						stop();
					}
				}
			}, 0, 0);
		}
	}

	public static void slideDown(final Player player, final WorldObject object) {
		final WorldTile TurnTile1 = new WorldTile(2187, 3444, 2);
		final WorldTile NextTile = new WorldTile(2187, 3415, 2);
		if (player.getSkills().getLevel(Skills.AGILITY) < 77) {
			player.getPackets().sendGameMessage(
					"You need atleast level 77 agility to jump on this.");
			return;
		}

		player.getTemporaryAttributtes().putIfAbsent("HefinStage", 0);

		if ((Integer) player.getTemporaryAttributtes().get("HefinStage") == 4) {
			player.getPackets().sendGameMessage("You slide down the zip line.",
					true);
			player.setNextFaceWorldTile(TurnTile1);
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(TurnTile1);
						player.setNextAnimation(new Animation(25016));
					}
					if (time == 3) {
						player.setNextFaceWorldTile(TurnTile1);
						player.getPackets().sendCameraLook(
								Cutscene.getX(player, 2186),
								Cutscene.getY(player, 3424), 16800);
						player.getPackets().sendCameraPos(
								Cutscene.getX(player, 2180),
								Cutscene.getY(player, 3422), 17000);
					}
					if (time == 10) {
						player.setNextFaceWorldTile(TurnTile1);
						player.setNextWorldTile(NextTile);
						player.setNextAnimation(new Animation(-1));
						player.getPackets().sendResetCamera();
						handleXP(player, false);
						player.getTemporaryAttributtes().put("HefinStage", 5);
						stop();
					}
				}
			}, 0, 0);
		}
	}

	public static void vault(final Player player, final WorldObject object) {
		final boolean running = player.getRun();
		final WorldTile NextTile = new WorldTile(2187, 3443, 2);
		if (player.getSkills().getLevel(Skills.AGILITY) < 77) {
			player.getPackets().sendGameMessage(
					"You need atleast level 77 agility to jump on this.");
			return;
		}

		player.getTemporaryAttributtes().putIfAbsent("HefinStage", 0);

		if ((Integer) player.getTemporaryAttributtes().get("HefinStage") == 3) {
			player.getPackets().sendGameMessage("You vault around the roof.",
					true);
			player.setRunHidden(false);
			player.lock(8);
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.faceObject(object);
						player.setNextAnimation(new Animation(25015));
					}
					if (time == 6) {
						player.getPackets().sendCameraLook(
								Cutscene.getX(player, 2186),
								Cutscene.getY(player, 3448), 18500);
						player.getPackets().sendCameraPos(
								Cutscene.getX(player, 2183),
								Cutscene.getY(player, 3435), 19000);
					}
					if (time == 16) {
						player.setNextWorldTile(NextTile);
						player.setNextAnimation(new Animation(-1));
						player.getPackets().sendResetCamera();
						player.setRunHidden(running);
						handleXP(player, false);
						stop();
						player.getTemporaryAttributtes().put("HefinStage", 4);
					}
				}
			}, 0, 0);
		}
	}

	public static void scale(final Player player, final WorldObject object) {
		final boolean running = player.getRun();
		final WorldTile NextTile = new WorldTile(2177, 3448, 2);
		if (player.getSkills().getLevel(Skills.AGILITY) < 77) {
			player.getPackets().sendGameMessage(
					"You need atleast level 77 agility to jump on this.");
			return;
		}

		player.getTemporaryAttributtes().putIfAbsent("HefinStage", 0);

		if ((Integer) player.getTemporaryAttributtes().get("HefinStage") == 2) {
			player.getPackets().sendGameMessage("You scale the cathedral.",
					true);
			player.setRunHidden(false);
			player.lock(8);
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.faceObject(object);
						player.setNextAnimation(new Animation(25014));
					}
					if (time == 5) {
						player.getPackets().sendCameraLook(
								Cutscene.getX(player, 2165),
								Cutscene.getY(player, 3448), 13000);
						player.getPackets().sendCameraPos(
								Cutscene.getX(player, 2155),
								Cutscene.getY(player, 3451), 21000);
					}
					if (time == 16) {
						player.setNextWorldTile(NextTile);
						player.setNextAnimation(new Animation(-1));
						player.getPackets().sendResetCamera();
						player.setRunHidden(running);
						handleXP(player, false);
						stop();
						player.getTemporaryAttributtes().put("HefinStage", 3);
					}
				}
			}, 0, 0);
		}
	}

	public static void traverse(final Player player, final WorldObject object) {
		final boolean running = player.getRun();
		final WorldTile NextTile = new WorldTile(2171, 3437, 1);
		if (player.getSkills().getLevel(Skills.AGILITY) < 77) {
			player.getPackets().sendGameMessage(
					"You need atleast level 77 agility to jump on this.");
			return;
		}

		player.getTemporaryAttributtes().putIfAbsent("HefinStage", 0);

		if ((Integer) player.getTemporaryAttributtes().get("HefinStage") == 1) {
			player.getPackets()
					.sendGameMessage("You traverse the cliff.", true);
			player.setRunHidden(false);
			player.lock(8);
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.faceObject(object);
						player.setNextAnimation(new Animation(25011));
					}
					if (time == 5) {
						player.getPackets().sendCameraLook(
								Cutscene.getX(player, 2175),
								Cutscene.getY(player, 3418), 3222);
						player.getPackets().sendCameraPos(
								Cutscene.getX(player, 2175),
								Cutscene.getY(player, 3418), 3222);
					}
					if (time == 10) {
						player.setNextWorldTile(NextTile);
						player.setNextAnimation(new Animation(24533));
						player.getPackets().sendResetCamera();
						handleXP(player, false);
						player.setRunHidden(running);
						stop();
						player.getTemporaryAttributtes().put("HefinStage", 2);
					}
				}
			}, 0, 0);
		}
	}

	public static void leapAcross(final Player player, final WorldObject object) {
		final boolean running = player.getRun();
		final WorldTile NextTile = new WorldTile(2180, 3419, 1);
		final WorldTile TurnTile = new WorldTile(2177, 3405, 1);
		if (player.getSkills().getLevel(Skills.AGILITY) < 77) {
			player.getPackets().sendGameMessage(
					"You need atleast level 77 agility to jump on this.");
			return;
		}
		player.getPackets().sendGameMessage("You leap across the walkway.",
				true);
		player.setNextFaceWorldTile(TurnTile);
		player.setRunHidden(false);
		player.lock(8);
		WorldTasksManager.schedule(new WorldTask() {
			int time;
			boolean ready = false;

			@Override
			public void run() {
				if (player.getX() == 2177 && player.getY() == 3402 && time == 0) {
					ready = true;
				} else if (player.getX() != 2177 && player.getY() != 3402) {
					player.addWalkSteps(2177, 3402);
					player.setNextFaceWorldTile(TurnTile);
					ready = false;
					time = 0;
				}
				time++;
				if (time == 1 && ready) {
					player.setNextFaceWorldTile(TurnTile);
					player.setNextAnimation(new Animation(24587));
				}
				if (time == 3) {
					ready = false;
					player.getPackets().sendCameraLook(
							Cutscene.getX(player, 2177),
							Cutscene.getY(player, 3408), 3417);
					player.getPackets().sendCameraPos(
							Cutscene.getX(player, 2177),
							Cutscene.getY(player, 3408), 3417);
				}
				if (time == 5) {
					player.setNextWorldTile(NextTile);
					player.setNextAnimation(new Animation(-1));
					player.setRunHidden(running);
					player.getPackets().sendResetCamera();
					handleXP(player, false);
					player.getTemporaryAttributtes().put("HefinStage", 1);
					stop();
				}
			}

		}, 0, 1);
	}

	private static void handleXP(final Player player, boolean completed) {
		int modifier = completed ? 10 : 1;
		if (completed) {
			if (player.getInventory().containsOneItem(
					ItemIdentifiers.HEFIN_COURSE_MULTIPLIER)) {
				player.getInventory().removeItemMoneyPouch(
						ItemIdentifiers.HEFIN_COURSE_MULTIPLIER, 1);
				modifier *= 1.2;
			}
			player.getSkillTasks().handleTask(AgilityCourses.HEFIN_AGILITY, 1);
			player.getCompCapeManager().increaseRequirement(
					Requirement.HEFIN_AGILITY, 1);
		}
		if (player.getSkills().getLevel(Skills.AGILITY) >= 77
				&& player.getSkills().getLevel(Skills.AGILITY) <= 82) {
			player.getSkills().addXp(Skills.AGILITY, (44 * modifier));
		} else if (player.getSkills().getLevel(Skills.AGILITY) >= 82
				&& player.getSkills().getLevel(Skills.AGILITY) <= 87) {
			player.getSkills().addXp(Skills.AGILITY, (55 * modifier));
		} else if (player.getSkills().getLevel(Skills.AGILITY) >= 87
				&& player.getSkills().getLevel(Skills.AGILITY) <= 92) {
			player.getSkills().addXp(Skills.AGILITY, (66 * modifier));
		} else if (player.getSkills().getLevel(Skills.AGILITY) >= 92
				&& player.getSkills().getLevel(Skills.AGILITY) <= 97) {
			player.getSkills().addXp(Skills.AGILITY, (74 * modifier));
		} else if (player.getSkills().getLevel(Skills.AGILITY) >= 97) {
			player.getSkills().addXp(Skills.AGILITY, (83 * modifier));
		}
	}

	// item id ; item quan min, item quan max, item rare
	private static final int[][] REWARDS = {
			{ ItemIdentifiers.MINT_CAKE, 1, 1, 85 },
			{ ItemIdentifiers.SPICY_STEW, 1, 1, 85 },
			{ ItemIdentifiers.SUMMER_PIE, 1, 1, 85 },
			{ ItemIdentifiers.PRIFDDINIAN_WORKERS_TROUSERS, 1, 1, 70 },
			{ ItemIdentifiers.PRIFDDINIAN_WORKERS_ROBES, 1, 1, 70 },
			{ ItemIdentifiers.PRIFDDINIAN_WORKERS_BOOTS, 1, 1, 70 },
			{ ItemIdentifiers.PRIFDDINIAN_WORKERS_GLOVES, 1, 1, 70 },
			{ ItemIdentifiers.AMLODD_SYMBOL_PIECE, 1, 1, 1 },
			{ ItemIdentifiers.CADARN_SYMBOL_PIECE, 1, 1, 1 },
			{ ItemIdentifiers.CRWYS_SYMBOL_PIECE, 1, 1, 1 },
			{ ItemIdentifiers.HEFIN_SYMBOL_PIECE, 1, 1, 1 },
			{ ItemIdentifiers.IORWERTH_SYMBOL_PIECE, 1, 1, 1 },
			{ ItemIdentifiers.MEILYR_SYMBOL_PIECE, 1, 1, 1 },
			{ ItemIdentifiers.TRAHAEARN_SYMBOL_PIECE, 1, 1, 1 },
			{ ItemIdentifiers.THE_VOICE_OF_THE_ELDERS, 1, 1, 50 },
			{ ItemIdentifiers.BLISSFUL_SHADOW_CORE, 600, 600, 20 },
			{ ItemIdentifiers.HEFIN_COURSE_MULTIPLIER, 1, 3, 20 },
			{ ItemIdentifiers.SMALL_AGILITY_LAMP, 1, 1, 20 },
			{ ItemIdentifiers.SMALL_PRAYER_LAMP, 1, 1, 20 },
			{ ItemIdentifiers.TRUTHFUL_SHADOW_CORE, 1, 1, 20 },
			{ ItemIdentifiers.CRYSTAL_ACORN, 1, 1, 1 },
			{ ItemIdentifiers.CORRUPTED_ORE, 1, 1, 20 },
			{ ItemIdentifiers.AGILITY_POTION_3, 1, 1, 20 } };

	private void prepareRewards() {
		// todo: prepare the reward table above can possibly use araxxi's way
		// and use temp attrib to see if they can open the chest(talk to the
		// npc)
	}

	public static void lightCreature(Player player, int option) {
		if (!Agility.hasLevel(player, 77))
			return;
		switch (option) {
		case 1:
			player.getPackets()
					.sendGameMessage(
							"You do not currently have any rewards to claim from the light creature.");
			break;
		case 2:
			player.getDialogueManager()
					.startDialogue(
							"SimpleItemMessage",
							32616,
							"You must discover at least one page of the 'The Voice of the Elders' before you can claim the book.<br>Run the Hefin Agility Course for a chance to find pages.");
			break;
		}
	}
}
