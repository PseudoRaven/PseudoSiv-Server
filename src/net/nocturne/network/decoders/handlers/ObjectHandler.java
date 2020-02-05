package net.nocturne.network.decoders.handlers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ObjectDefinitions;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.actions.Pickables;
import net.nocturne.game.item.actions.RottenPotato;
import net.nocturne.game.map.MapBuilder;
import net.nocturne.game.map.bossInstance.BossInstanceHandler;
import net.nocturne.game.map.bossInstance.BossInstanceHandler.Boss;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.others.PolyporeCreature;
import net.nocturne.game.npc.others.TreeSpirit;
import net.nocturne.game.npc.vorago.Vorago;
import net.nocturne.game.npc.vorago.VoragoHandler;
import net.nocturne.game.player.CoalTrucksManager;
import net.nocturne.game.player.DoomsayerManager;
import net.nocturne.game.player.Equipment;
import net.nocturne.game.player.FarmingManager.SpotInfo;
import net.nocturne.game.player.OwnedObjectManager;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.QuestManager.Quests;
import net.nocturne.game.player.RouteEvent;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.CowMilkingAction;
import net.nocturne.game.player.actions.PlayerCombat;
import net.nocturne.game.player.actions.SandBucketFillAction;
import net.nocturne.game.player.actions.WaterFilling;
import net.nocturne.game.player.actions.WaterFilling.Fill;
import net.nocturne.game.player.actions.skills.agility.Agility;
import net.nocturne.game.player.actions.skills.agility.AgilityPyramid;
import net.nocturne.game.player.actions.skills.agility.ApeAtollAgility;
import net.nocturne.game.player.actions.skills.agility.BarbarianOutpostAgility;
import net.nocturne.game.player.actions.skills.agility.GnomeAgility;
import net.nocturne.game.player.actions.skills.agility.HefinAgility;
import net.nocturne.game.player.actions.skills.agility.WildernessAgility;
import net.nocturne.game.player.actions.skills.construction.House;
import net.nocturne.game.player.actions.skills.construction.HouseConstants;
import net.nocturne.game.player.actions.skills.construction.SitChair;
import net.nocturne.game.player.actions.skills.cooking.Cooking;
import net.nocturne.game.player.actions.skills.cooking.Cooking.Cookables;
import net.nocturne.game.player.actions.skills.crafting.AccessorySmithing;
import net.nocturne.game.player.actions.skills.crafting.BowlSinging.CreateCrystal;
import net.nocturne.game.player.actions.skills.crafting.Harps;
import net.nocturne.game.player.actions.skills.crafting.Looming.CreateCape;
import net.nocturne.game.player.actions.skills.crafting.PotteryOven.FinishPot;
import net.nocturne.game.player.actions.skills.crafting.PotteryWheel.CreatePot;
import net.nocturne.game.player.actions.skills.crafting.RobustGlass.CreateGlass;
import net.nocturne.game.player.actions.skills.crafting.Spinning.CreateSpin;
import net.nocturne.game.player.actions.skills.divination.DivinationConvert;
import net.nocturne.game.player.actions.skills.divination.DivinationConvert.ConvertMode;
import net.nocturne.game.player.actions.skills.divination.actions.DivineFishing;
import net.nocturne.game.player.actions.skills.divination.actions.DivineHerblore;
import net.nocturne.game.player.actions.skills.divination.actions.DivineHunting;
import net.nocturne.game.player.actions.skills.divination.actions.DivineMining;
import net.nocturne.game.player.actions.skills.divination.actions.DivineWoodcutting;
import net.nocturne.game.player.actions.skills.divination.actions.DivineWoodcutting.DivineTreeDefinitions;
import net.nocturne.game.player.actions.skills.dungeoneering.rooms.puzzles.FishingFerretRoom;
import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringSpinning.DungSpin;
import net.nocturne.game.player.actions.skills.firemaking.Bonfire;
import net.nocturne.game.player.actions.skills.hunter.TrapAction;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.actions.skills.mining.EssenceMining;
import net.nocturne.game.player.actions.skills.mining.EssenceMining.EssenceDefinitions;
import net.nocturne.game.player.actions.skills.mining.LavaFlowMine;
import net.nocturne.game.player.actions.skills.mining.Mining;
import net.nocturne.game.player.actions.skills.mining.Mining.RockDefinitions;
import net.nocturne.game.player.actions.skills.mining.MiningBase;
import net.nocturne.game.player.actions.skills.prayer.Cleansing;
import net.nocturne.game.player.actions.skills.runecrafting.Runecrafting;
import net.nocturne.game.player.actions.skills.smithing.Smelting.SmeltingBar;
import net.nocturne.game.player.actions.skills.smithing.Smithing;
import net.nocturne.game.player.actions.skills.smithing.Smithing.ForgingBar;
import net.nocturne.game.player.actions.skills.smithing.SpiritshieldCreating;
import net.nocturne.game.player.actions.skills.summoning.SpecialScrolls.Scroll;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.player.actions.skills.thieving.Thieving;
import net.nocturne.game.player.actions.skills.thieving.WallSafe;
import net.nocturne.game.player.actions.skills.woodcutting.Canoes;
import net.nocturne.game.player.actions.skills.woodcutting.Woodcutting;
import net.nocturne.game.player.actions.skills.woodcutting.Woodcutting.HatchetDefinitions;
import net.nocturne.game.player.actions.skills.woodcutting.Woodcutting.TreeDefinitions;
import net.nocturne.game.player.content.AbbysObsticals;
import net.nocturne.game.player.content.DwarfMultiCannon;
import net.nocturne.game.player.content.FadingScreen;
import net.nocturne.game.player.content.Incubator;
import net.nocturne.game.player.content.MaxGuild;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SpiritTree;
import net.nocturne.game.player.content.WildernessObelisk;
import net.nocturne.game.player.content.activities.CrystalChest;
import net.nocturne.game.player.content.activities.MotherlodeMaw;
import net.nocturne.game.player.content.activities.XPWell;
import net.nocturne.game.player.content.activities.distractions.EvilTree;
import net.nocturne.game.player.content.activities.distractions.GodStatues;
import net.nocturne.game.player.content.activities.events.WorldEvents;
import net.nocturne.game.player.content.activities.minigames.ArtisanWorkshop;
import net.nocturne.game.player.content.activities.minigames.CastleWars;
import net.nocturne.game.player.content.activities.minigames.Crucible;
import net.nocturne.game.player.content.activities.minigames.Falconry;
import net.nocturne.game.player.content.activities.minigames.FightCaves;
import net.nocturne.game.player.content.activities.minigames.FightKiln;
import net.nocturne.game.player.content.activities.minigames.FightPits;
import net.nocturne.game.player.content.activities.minigames.PuroPuro;
import net.nocturne.game.player.content.activities.minigames.RunespanController;
import net.nocturne.game.player.content.activities.minigames.Sawmill;
import net.nocturne.game.player.content.activities.minigames.ShootingStars;
import net.nocturne.game.player.content.activities.minigames.WarriorsGuild;
import net.nocturne.game.player.content.activities.minigames.ectofuntus.Ectofuntus;
import net.nocturne.game.player.content.activities.minigames.ectofuntus.SlimeBucketFill;
import net.nocturne.game.player.content.activities.minigames.fistofguthix.MinigameManager;
import net.nocturne.game.player.content.activities.minigames.pest.Lander;
import net.nocturne.game.player.content.activities.minigames.soulwars.SoulLobby;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationController;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationLobbyController;
import net.nocturne.game.player.content.activities.minigames.warbands.LootingAction;
import net.nocturne.game.player.content.activities.minigames.warbands.Warbands;
import net.nocturne.game.player.content.activities.partyroom.PartyRoom;
import net.nocturne.game.player.content.activities.reaper.ReaperRewardsShop;
import net.nocturne.game.player.controllers.AscensionDungeon.AscDoors;
import net.nocturne.game.player.controllers.BorkController;
import net.nocturne.game.player.controllers.GodWars;
import net.nocturne.game.player.controllers.KuradalDungeon;
import net.nocturne.game.player.controllers.NomadsRequiem;
import net.nocturne.game.player.controllers.Wilderness;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GorvekAndVindictaInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GregorovicInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.HelwyrInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.TwinFuriesInstanceController;
import net.nocturne.game.player.controllers.godwars2.GodWars2;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.player.dialogues.impl.MiningGuildDwarf;
import net.nocturne.game.player.dialogues.impl.SqirkFruitSqueeze;
import net.nocturne.game.player.dialogues.impl.construction.EnterHouseD;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.stream.InputStream;
import net.nocturne.utils.Color;
import net.nocturne.utils.Logger;
import net.nocturne.utils.ObjectExamines;
import net.nocturne.utils.ShopsHandler;
import net.nocturne.utils.Utils;

public final class ObjectHandler {

	private ObjectHandler() {

	}

	public static void handleOption(final Player player, InputStream stream,
			int option) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		if (player.isLocked()
				|| player.getEmotesManager().getNextEmoteEnd() >= Utils
						.currentTimeMillis())
			return;
		int x = stream.readUnsignedShortLE();
		int y = stream.readUnsignedShort128();
		final int id = stream.readIntV1();
		boolean forceRun = stream.readUnsigned128Byte() == 1;
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getObjectWithId(tile, id);
		if (mapObject == null || mapObject.getId() != id) {
			return;
		}
		if (option != -1)
			player.stopAll();
		if (forceRun)
			player.setRun(forceRun);
		switch (option) {
		case 1:
			handleOption1(player, mapObject);
			break;
		case 2:
			handleOption2(player, mapObject);
			break;
		case 3:
			handleOption3(player, mapObject);
			break;
		case 4:
			handleOption4(player, mapObject);
			break;
		case 5:
			handleOption5(player, mapObject);
			break;
		case -1:
			handleOptionExamine(player, mapObject);
			break;
		}
	}

	private static void handleOption1(final Player player,
			final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		final int plane = object.getPlane();
		final int x = object.getX();
		final int y = object.getY();
		switch (object.getId()) {

		case 15468:
			if (player.getInventory().containsItem(Smithing.HAMMER, 1)) {
				player.getPackets().sendGameMessage(
						"You already have a hammer in your inventory.");
				return;
			}
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage(
						"You do not have enough inventory space.");
				return;
			}
			player.getDialogueManager().startDialogue("SimpleItemMessage",
					Smithing.HAMMER, "You take a hammer from the crate.");
			player.getInventory().addItem(Smithing.HAMMER, 1);
			break;
		}
		if (object.getId() == 79519) {
			RunespanController.enterRunespan(player, false);
		}
		if (object.getId() == 79520) {
			RunespanController.enterRunespan(player, true);
		}
		if (id == 79770) {
			player.setNextWorldTile(new WorldTile(player.getX(), player.getY(),
					1));
		}
		if (id == 79772) {
			player.setNextWorldTile(new WorldTile(player.getX(), player.getY(),
					2));
		}
		if (id == 82899) {
			Magic.sendSkyJumpTeleport(player, new WorldTile(1936, 5987, 0));
			player.getPackets()
					.sendGameMessage(
							"The ancient mechanism fills you with divine power allowing you to escape.");
			return;
		}
		if (id == 5008) {
			new WorldTile(2771, 10161, 0);

		}
		if (id == 5014) {
			new WorldTile(2728, 3707, 0);

		}
		if (id == 5973) {
			new WorldTile(2836, 10144, 0);

		}
		if (id == 57050) {
			new WorldTile(1519, 4704, 0);

		}
		if (id == 13967 || id == 13976 || id == 13974) {
			Magic.sendDemonicTeleport(player, new WorldTile(3297, 9824, 0));
			player.getPackets().sendGameMessage(
					"You are dragged beyond the depths of this mortal world");
		}
		if (id == 13999) {
			Magic.sendSkyJumpTeleport(player, new WorldTile(
					Settings.HOME_LOCATION));
		}
		if (id == 45008 || id == 45009) {
			new WorldTile(2821, 10212, 0);

		}
		/*
		 * if (id == 45802) { if (player.getX() == 414 && player.getY() == 674)
		 * { player.getDialogueManager().startDialogue("ReaperDialogue", 2);
		 * player.faceObject(object); return; } player.addWalkSteps(414, 674);
		 * player.lock(); do { WorldTasksManager.schedule(new WorldTask() {
		 * 
		 * @Override public void run() {
		 * player.getDialogueManager().startDialogue("ReaperDialogue", 2);
		 * player.faceObject(object); player.unlock(); this.stop(); return; } },
		 * 2, 1); } while (player.getX() == 414 && player.getY() == 674);
		 * return; }
		 */

		if (id == 6087 && player.getX() == 2893) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				player.faceObject(object);
				player.useStairs(-1, new WorldTile(2896, 10210, 1), 0, 1);
			}, true));
			return;
		}
		if (id == 6087 && player.getX() == 2893) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				player.faceObject(object);
				player.useStairs(-1, new WorldTile(2896, 10210, 1), 0, 1);
			}, true));
			return;
		}

		if (id == 67044) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				player.faceObject(object);
				player.useStairs(-1, new WorldTile(2924, 3408, 0), 0, 1);
			}, true));
			return;
		}
		if (object.getId() == 101910) {
			if (player.getControllerManager().getController() instanceof GodWars2) {
				player.setRouteEvent(new RouteEvent(object, () -> {
					player.faceObject(object);
					player.getControllerManager().startController(
							"GorvekAndVindictaInstanceController");
				}, true));
				return;
			} else if (player.getControllerManager().getController() instanceof GorvekAndVindictaInstanceController) {
				player.setRouteEvent(new RouteEvent(object, () -> {
					player.faceObject(object);
					player.getControllerManager()
							.removeControllerWithoutCheck();
					player.getControllerManager().startController("GodWars2");
					player.setNextWorldTile(new WorldTile(3112, 6896, 1));
					player.getInterfaceManager().removeInterface(1648);
				}, true));
				return;
			}
		} else if (object.getId() == 101907) { // TWINFURIES
			if (player.getControllerManager().getController() instanceof GodWars2) {
				player.setRouteEvent(new RouteEvent(object, () -> {
					player.faceObject(object);
					player.getControllerManager().startController(
							"TwinFuriesInstanceController");
				}, true));
				return;
			} else if (player.getControllerManager().getController() instanceof TwinFuriesInstanceController) {
				player.setRouteEvent(new RouteEvent(object, () -> {
					player.faceObject(object);
					player.getControllerManager()
							.removeControllerWithoutCheck();
					player.getControllerManager().startController("GodWars2");
					player.setNextWorldTile(new WorldTile(3115, 7060, 1));
					player.getInterfaceManager().removeInterface(1648);
				}, true));
				return;
			}
		} else if (object.getId() == 101902) { // GREGOROVIC
			if (player.getControllerManager().getController() instanceof GodWars2) {
				player.setRouteEvent(new RouteEvent(object, () -> {
					player.faceObject(object);
					player.getControllerManager().startController(
							"GregorovicInstanceController");
				}, true));
				return;
			} else if (player.getControllerManager().getController() instanceof GregorovicInstanceController) {
				player.setRouteEvent(new RouteEvent(object, () -> {
					player.faceObject(object);
					player.getControllerManager()
							.removeControllerWithoutCheck();
					player.getControllerManager().startController("GodWars2");
					player.setNextWorldTile(new WorldTile(3287, 7062, 1));
					player.getInterfaceManager().removeInterface(1648);
				}, true));
				return;
			}
		} else if (object.getId() == 101898) {
			if (player.getControllerManager().getController() instanceof GodWars2) {
				player.setRouteEvent(new RouteEvent(object, () -> {
					player.faceObject(object);
					player.getControllerManager().startController(
							"HelwyrInstanceController");
				}, true));
				return;
			} else if (player.getControllerManager().getController() instanceof HelwyrInstanceController) {
				player.setRouteEvent(new RouteEvent(object, () -> {
					player.faceObject(object);
					player.getControllerManager()
							.removeControllerWithoutCheck();
					player.getControllerManager().startController("GodWars2");
					player.setNextWorldTile(new WorldTile(3279, 6897, 1));
					player.getInterfaceManager().removeInterface(1648);
				}, true));
				return;
			}
		}
		if (id == 104065) { // stone of apollo
			objectDef.name = "Stone Of Apollo";
			player.getDialogueManager().startDialogue("ApolloDialogue");
			player.getPackets().sendGameMessage(
					"You have consulted with the Great Stone Of Apollo");
			return;
		}
		if (id == 94077) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				if (player.getY() <= 4635)
					player.setNextWorldTile(new WorldTile(1364, 4637, 0));
				if (player.getY() >= 4637)
					player.setNextWorldTile(new WorldTile(1364, 4635, 0));
				player.faceObject(object);
			}, true));
			return;

		}
		/**
		 * Godwars 2:
		 */
		if (id == 101865) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				player.getDialogueManager().startDialogue("WoodenLiftD");
			}, true));
			return;
		}
		if (id == 101766) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				WorldObject lift = new WorldObject(101865, 10, 3,
						new WorldTile(3198, 6935, 1));
				player.setNextWorldTile(new WorldTile(3198, 6940, 1));
				player.setNextAnimation(new Animation(26446));
				player.getControllerManager().startController("GodWars2");
				player.faceObject(lift);
			}, true));
			return;
		}
		/*
		 * Vorago Objects
		 */
		if (object.getId() == 84960) {
			player.getDialogueManager().startDialogue("ExitSphere");
			player.faceObject(object);
			player.setCantWalk(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setCantWalk(false);
					stop();
				}
			}, 3);
		}

		if (object.getId() == 84958) {
			Vorago n = VoragoHandler.vorago;
			if (n.getX() == 3097 && n.getY() == 6119) {
				if (n.getDirection() == 0) {
					n.setCantInteract(true);
					player.addWalkSteps(object.getX(), object.getY());
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {

							if (count == 1) {
								n.getCombat().removeTarget();
							}
							if (count == 2) {
								player.faceEntity(n);

								n.setNextFaceWorldTile(new WorldTile(object
										.getX(), object.getY() - 30, 0));
							}
							if (count == 3) {
								player.setNextAnimation(new Animation(20311));
								n.setNextAnimation(new Animation(20376));
							}
							if (count == 20) {
								n.canDie = true;
								player.getInventory().addItem(28600, 1);
								player.setNextWorldTile(new WorldTile(object
										.getX() + 6, object.getY() - 7, 0));
								n.setCantInteract(false);
								stop();
							}

							count++;
						}
					}, 0, 0);
				} else {
					player.getPackets()
							.sendEntityMessage(1, 15263739, player,
									"Vorago needs to be facing away for you to successfully land the jump!");
				}
			} else {
				player.getPackets().sendEntityMessage(1, 15263739, player,
						"Vorago isn't in the right place!");
			}
		}

		if (object.getId() == 84828 && player.getX() > object.getX()) {
			player.setNextWorldTile(new WorldTile(object.getX() - 1, object
					.getY(), 0));
		}

		if (object.getId() == 56990) {
			player.setNextWorldTile(new WorldTile(2177, 5664, 0));
		}

		if (object.getId() == 56989) {
			player.setNextWorldTile(new WorldTile(2939, 10197, 0));
		}
		// Magic.sendNormalTeleportSpell(player, 1, 0, Vorago.OUTSIDE);
		if (object.getId() == 84960) {
			player.getDialogueManager().startDialogue("ExitSphere");
			player.faceObject(object);
			player.setCantWalk(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setCantWalk(false);
					stop();
				}
			}, 3);
		}

		if (object.getId() == 84959
				&& VoragoHandler.getPlayers().contains(player)) {
			Vorago rago = VoragoHandler.vorago;
			player.setRouteEvent(new RouteEvent(
					object,
					() -> {
						player.getPackets()
								.sendEntityMessage(1, 15263739, player,
										"Vorago feels the pull of the gravity field, which destabilises him.");
						Projectile projectile = World.sendProjectileNew(object,
								rago, 4029, 5, 5, 0, 2, 0, 0);
						rago.setNextGraphics(new Graphics(4030, projectile
								.getEndTime(), NPCCombatDefinitions.MAGE));
						World.removeObject(object);
						if (rago.fieldCount == 4) {
							rago.special = true;
							rago.specialType = 2;

						}
					}, true));
			return;
		}

		if (object.getId() == 84827) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				player.setNextWorldTile(new WorldTile(object.getX() - 1, object
						.getY(), 0));
			}, true));
			return;
		}
		if (object.getId() == 84826) {// Gaps TODO Anims
			if (player.getY() == 6102 && player.getX() == 3043) {// Gap 1
				player.setNextWorldTile(new WorldTile(3043, 6106, 0));
			} else if (player.getY() == 6106 && player.getX() == 3043) {// Gap1
				player.setNextWorldTile(new WorldTile(3043, 6102, 0));
			} else if (player.getY() == 6107 && player.getX() == 3043) {// Gap2
				player.setNextWorldTile(new WorldTile(3047, 6107, 0));
			} else if (player.getY() == 6107 && player.getX() == 3047) {// Gap2
				player.setNextWorldTile(new WorldTile(3043, 6107, 0));
			} else if (player.getY() == 6111 && player.getX() == 3047) {// Gap3
				player.setNextWorldTile(new WorldTile(3047, 6115, 0));
			} else if (player.getY() == 6115 && player.getX() == 3047) {// Gap3
				player.setNextWorldTile(new WorldTile(3047, 6111, 0));
			} else if (object.getRotation() == 1) {
				player.setNextWorldTile(new WorldTile(object.getX() + 3, player
						.getY(), 0));

			}
			return;
		}
		if (id == 92120) {
			Magic.sendTeleportSpell(player, -1, -1, -1, -1, 1, -1,
					new WorldTile(414, 651, 0), 5, false, Magic.OBJECT_TELEPORT);
			FadingScreen.fade(player);
			FadingScreen.unfade(player,
					FadingScreen.fade(player, FadingScreen.TICK / 2),
					new Runnable() {
						@Override
						public void run() {
							WorldObject objectdoor = new WorldObject(45802, 10,
									3, 413, 680, 0);
							player.faceObject(objectdoor);
						}

					});
		}
		if (id == 92119) {
			player.setRouteEvent(new RouteEvent(object, new Runnable() {
				@Override
				public void run() {
					player.addWalkSteps(414, 652);
				}
			}, true));
			Magic.sendTeleportSpell(player, -1, -1, -1, -1, 1, -1,
					new WorldTile(3106, 3310, 0), 5, false,
					Magic.OBJECT_TELEPORT);
			FadingScreen.fade(player);
			FadingScreen.unfade(player,
					FadingScreen.fade(player, FadingScreen.TICK / 2),
					new Runnable() {
						@Override
						public void run() {
						}

					});
		}
		if (id == 17119) {
			if (player.getInventory().containsItem(1925, 1))
				player.getActionManager().setAction(new SlimeBucketFill());
			return;
		}
		if (id == 67044) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				// unreachable objects exception
					player.faceObject(object);
					player.useStairs(-1, new WorldTile(2924, 3408, 0), 0, 1);
				}, true));
			return;
		}
		if (Ectofuntus.handleObjects(player, object.getId())) {
			return;
		}
		if (object.getId() >= HouseConstants.HObject.WOOD_BENCH.getId()
				&& object.getId() <= HouseConstants.HObject.GILDED_BENCH
						.getId()) {
			player.setRouteEvent(new RouteEvent(object, () -> player
					.getControllerManager().processObjectClick1(object), true));
			return;
		}
		if (id == 87989) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				XPWell.give(player);
				player.faceObject(object);
			}, true));
			return;
		}
		if (id == 28094) {
			player.setNextWorldTile(new WorldTile(2910, 10176, 0));
		}
		if (id == 43529 || id == 69514 || (id >= 4550 && id <= 4559)) {
			player.setRouteEvent(new RouteEvent(object, () -> {
				// unreachable agility objects exception
					player.faceObject(object);
					if (id == 43529) {
						GnomeAgility.preSwing(player, object);
					} else if (id == 69514) {
						GnomeAgility.runGnomeBoard(player, object);
					} else if (id >= 4550 && id <= 4559) {
						if (!Agility.hasLevel(player, 35))
							return;
						if (object.withinDistance(player, 2)) {
							if (!Agility.hasLevel(player, 35))
								return;
							player.setNextForceMovement(new ForceMovement(
									player, 1, object, 2, Utils.getAngle(
											object.getX() - player.getX(),
											object.getY() - player.getY())));
							player.useStairs(-1, object, 1, 2);
							player.setNextAnimation(new Animation(769));
							player.getSkills().addXp(Skills.AGILITY, 2);
						}
					}
				}, true));
			return;
		}

		else if (id == 75463) {
			player.getControllerManager().processObjectClick1(object);
			return;
		} else if (object.getId() == 5949) {
			final boolean isSouth = player.getY() > 9553;
			player.getPackets().sendGameMessage(
					"You leap across with a mighty leap!");
			WorldTasksManager.schedule(new WorldTask() {
				int ticks = 0;

				@Override
				public void run() {
					ticks++;
					int direction = isSouth ? ForceMovement.SOUTH
							: ForceMovement.NORTH;
					WorldTile tile = new WorldTile(3221, isSouth ? 9552 : 9556,
							0);
					if (ticks == 1) {
						player.setNextForceMovement(new ForceMovement(player,
								1, object, 2, direction));
						player.setNextAnimation(new Animation(769));
					} else if (ticks == 2) {
						player.useStairs(769, object, 0, 1);
					} else if (ticks == 3) {
						player.setNextAnimation(new Animation(769));
						player.setNextForceMovement(new ForceMovement(player,
								1, tile, 2, direction));
					} else if (ticks == 4) {
						player.useStairs(-1, tile, 0, 1);
						stop();
					}
				}
			}, 0, 1);
			return;
		}

		player.setRouteEvent(new RouteEvent(
				object,
				() -> {
					player.stopAll();
					if (!player.getControllerManager().processObjectClick1(
							object))
						return;
					WorldObject mapObject = World.getObjectWithId(object, id);
					if (mapObject == null || mapObject.getId() != id)
						return;
					player.faceObject(object);
					if (Warbands.warband != null) {
						if (Warbands.warband.objectHasResources(object)) {
							LootingAction.loot(player, object);
							return;
						}
					}
					if (object.getId() == 87997) {
						BossInstanceHandler.enterInstance(player,
								Boss.Rise_Of_The_Six);
					} else if (MaxGuild.handleObject(player, object))
						return;
					else if (Pickables.handlePickable(player, object))
						return;
					else if (AgilityPyramid.handleObject(player, object))
						return;
					if (object.getId() == 3932) {
						handleLogBalance(player, object, 6, 45);
						return;
					} else if (object.getId() == 9322) {
						handleLogBalance(player, object, 4, 48);
						return;
					} else if (object.getId() == 82605) {
						handleLogBalance(player, object, 5, 48);
						return;
					}
					if (id == 66533) {
						player.setNextWorldTile(new WorldTile(2208, 4365, 0));
						player.getPackets()
								.sendGameMessage(
										"You crawl through the dank hole and discover a cave not of this world.");
					}
					if (id == 66534) {
						player.setNextWorldTile(new WorldTile(2878, 3570, 0));
						player.getPackets().sendGameMessage(
								"You return to the surface.");
					}
					if (id == 67002) {
						player.setNextWorldTile(new WorldTile(2878, 3502, 0));
					}
					if (id == 66876) {
						player.setNextWorldTile(new WorldTile(2291, 4516, 0));
					}
					if (object.getDefinitions().name.startsWith("Laboratory ")) {
						if (player.getSkills().getLevel(Skills.SLAYER) < 95) {
							player.getPackets()
									.sendGameMessage(
											"You need at least 95 Slayer to kill a Legio.");
							return;
						}
						String name = object.getDefinitions().name.replace(
								"Laboratory ", "");
						for (AscDoors door : AscDoors.values()) {
							if (door.name().equalsIgnoreCase(name)) {
								if (object.getDefinitions().getFirstOption()
										.equals("Enter")
										|| object.getDefinitions()
												.getFirstOption()
												.equals("Exit"))
									player.getDialogueManager().startDialogue(
											"AscensionDungeonD",
											door,
											name,
											object.getDefinitions()
													.getFirstOption()
													.equals("Exit"), false);
							}
						}
					}
					if (object.getId() == 101305 || object.getId() == 47233) {
						handleCrossOver(player, object, 2, 839);
						return;
					} else if (object.getId() == 2834) {
						handleCrossOver(player, object, 4, 6132);
						return;
					} else if (object.getId() == 47237) {
						handleCrossOver(player, object, 8, 6132);
						return;
					}
					if (object.getId() == 104119) {
						player.getDialogueManager().startDialogue("ClanScores");
						return;
					}
					if (object.getId() == 84715) {
						WorldObject other = null;
						for (int x1 = -6; x1 < 6; x1++) {
							for (int y1 = -6; y1 < 6; y1++) {
								if (x1 == 0 && y1 == 0)
									continue;
								other = World.getObjectWithId(
										object.transform(x1, y1, 0), 84715);
								if (other != null
										&& other.getRotation() != object
												.getRotation()) {
									player.setNextWorldTile(new WorldTile(other));
									return;
								}
							}
						}
					}
					switch (object.getId()) {
					case 104453:
						player.useStairs(832, new WorldTile(3045, 3927, 0), 1,
								2);
						break;
					case 82938:
						player.getDialogueManager()
								.startDialogue(
										"SimpleItemMessage",
										29293,
										"You have not unlocked any entries. You need to offer more elder chronicles first.");
						break;
					case 87998:
						player.useStairs(832, new WorldTile(3540, 3310, 0), 1,
								2);
						break;
					case 83074:
						player.getDialogueManager().startDialogue(
								"ShatteredWall");
						break;
					case 87325:
						if (player.getSkills().getLevel(Skills.DUNGEONEERING) >= 80) {
							if (player.getY() == 3609)
								player.addWalkSteps(3493, 3608, 1, false);
							else
								player.addWalkSteps(3493, 3609, 1, false);
						} else
							player.getPackets()
									.sendGameMessage(
											"You need a Dungeoneering level of at least 80 to pass through this barrier.");
						break;
					case 47236:
						handleBarrier(player);
						break;
					case 47237:
						if (player.getSkills().hasLevel(Skills.AGILITY, 90))
							return;
						if (player.getX() == 1641 && player.getY() == 5260
								|| player.getX() == 1641
								&& player.getY() == 5259
								|| player.getX() == 1640
								&& player.getY() == 5259) {
							player.setNextWorldTile(new WorldTile(1641, 5268, 0));
						} else {
							player.setNextWorldTile(new WorldTile(1641, 5260, 0));
						}
						break;
					case 35960:
						FadingScreen.unfade(player, FadingScreen.fade(player,
								400), () -> player
								.setNextWorldTile(new WorldTile(1923, 5987, 0)));
						break;
					case 104120:
						WorldEvents.sendInterface(player);
						break;
					case 30146:
						MinigameManager.INSTANCE().fistOfGuthix()
								.handlePortalEntering(object, player);
						break;
					case 30144:
						player.getDialogueManager().startDialogue(
								"LeaveFistOfGuthix");
						break;
					case 30141:
						MinigameManager.INSTANCE().fistOfGuthix()
								.handleBarriers(object, player);
						break;
					case 30143:
						MinigameManager.INSTANCE().fistOfGuthix()
								.pickUpStone(player);
						break;
					case 20604:
						player.setNextWorldTile(new WorldTile(3022, 3402, 0));
						break;
					case 20602:
						player.setNextWorldTile(new WorldTile(2954, 9675, 0));
						break;
					case 20608:
						MinigameManager.INSTANCE().fistOfGuthix()
								.handlePortalEnter(player);
						break;
					case 30203:
						MinigameManager.INSTANCE().fistOfGuthix()
								.handlePortalExit(player);
						break;
					case 30224:
						MinigameManager.INSTANCE().fistOfGuthix()
								.enterPassageway(player);
						break;
					case 2230:
						player.setNextWorldTile(new WorldTile(2833, 2955, 0));
						break;
					case 2265:
						player.setNextWorldTile(new WorldTile(2777, 3214, 0));
						break;
					case 42029:
					case 42030:
					case 42031:
						SoulLobby.handleObjects(player, object.getId());
						break;
					case 9038:
					case 9039:
						if (player.getX() >= 2815 && player.getX() <= 2816)
							player.addWalkSteps(2817, player.getY(), -1, false);
						else if (player.getX() >= 2817 && player.getX() <= 2818)
							player.addWalkSteps(2816, player.getY(), -1, false);
						break;
					case 5259:
						if (player.getY() >= 3505 && player.getY() <= 3507) {
							player.addWalkSteps(player.getX(), 3508, -1, false);
							player.getPackets().sendGameMessage(
									"You pass the barrier.", true);
						} else if (player.getY() >= 3508
								&& player.getY() <= 3511) {
							player.addWalkSteps(player.getX(), 3507, -1, false);
							player.getPackets().sendGameMessage(
									"You pass the barrier.", true);
						}
						break;
					case 82728:
						player.setNextWorldTile(player.getY() == 3538 ? new WorldTile(
								3423, 3540, 0)
								: player.getY() == 3540 ? new WorldTile(3423,
										3538, 0) : new WorldTile(3423, 3540, 0));
						break;
					case 1722:
						if (object.getPlane() == 0)
							player.setNextWorldTile(new WorldTile(2591, 3092, 1));
						else if (object.getPlane() == 1)
							player.setNextWorldTile(new WorldTile(2590, 3087, 2));
						return;
					case 1723:
						if (object.getPlane() == 2)
							player.setNextWorldTile(new WorldTile(2590, 3083, 1));
						else if (object.getPlane() == 1)
							player.setNextWorldTile(new WorldTile(2590, 3088, 0));
						return;
					case 82488:
						player.setNextWorldTile(new WorldTile(3411, 3543, 3));
						break;
					case 25340:
						player.setNextWorldTile(new WorldTile(1778, 5346, 0));
						return;
					case 82489:
						player.setNextWorldTile(new WorldTile(3435, 3543, 3));
						break;
					case 82490:
						player.setNextWorldTile(new WorldTile(3415, 3542, 2));
						break;
					case 82491:
						player.setNextWorldTile(new WorldTile(3431, 3543, 2));
						break;
					case 88260:
						ForgingBar fb = ForgingBar.getBar(player);
						player.getDialogueManager().startDialogue("SmithingD",
								object, fb);
						break;
					case 88261:
						SmeltingBar bar = SmeltingBar.getBar(player);
						if (bar != null) {
							player.getDialogueManager().startDialogue(
									"SmeltingD", object, bar);
						} else {
							player.getPackets().sendGameMessage(
									"You do not have a bar in your inventory.");
						}
						break;
					}
					if (player.getTreasureTrailsManager().useObject(object))
						return;
					else if (CastleWars.handleObjects(player, id))
						return;
					else if (player.getDungManager().enterResourceDungeon(
							object))
						return;
					else if (MotherlodeMaw.handleObject(player, object))
						return;
					else if (EvilTree.handleObject(player, object, 1))
						return;
					else if (player.getFarmingManager().isFarming(id, null, 1))
						return;
					else if (TrapAction.isTrap(player, object))
						return;
					if (id == 92413)
						player.getDialogueManager().startDialogue("ClanPortal",
								1);
					if (id == 90223) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_CRAYFISH,
												x, y, plane, object));
						return;
					}
					if (id == 7235) {
						player.getActionManager().setAction(
								new WallSafe(object));
						return;
					}
					if (id == 90224) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_HERRING,
												x, y, plane, object));
						return;
					}
					if (id == 90225) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_TROUT,
												x, y, plane, object));
						return;
					}
					if (id == 90226) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_SALMON,
												x, y, plane, object));
						return;
					}
					if (id == 90227) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_LOBSTER,
												x, y, plane, object));
						return;
					}
					if (id == 90228) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_SWORDFISH,
												x, y, plane, object));
						return;
					}
					if (id == 90229) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_SHARK,
												x, y, plane, object));
						return;
					}
					if (id == 90230) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_CAVEFISH,
												x, y, plane, object));
						return;
					}
					if (id == 90231) {
						player.getActionManager()
								.setAction(
										new DivineFishing(
												DivineFishing.DivineFishingSpots.DIVINE_ROCKTAIL,
												x, y, plane, object));
						return;
					}
					if (id == 87280) {
						player.getActionManager()
								.setAction(
										new DivineHerblore(
												DivineHerblore.DivineHerbsSpots.DIVINE_HERB_I,
												x, y, plane, object));
						return;
					}
					if (id == 87281) {
						player.getActionManager()
								.setAction(
										new DivineHerblore(
												DivineHerblore.DivineHerbsSpots.DIVINE_HERB_II,
												x, y, plane, object));
						return;
					}
					if (id == 87282) {
						player.getActionManager()
								.setAction(
										new DivineHerblore(
												DivineHerblore.DivineHerbsSpots.DIVINE_HERB_III,
												x, y, plane, object));
						return;
					}
					if (id == 87270) {
						player.getActionManager()
								.setAction(
										new DivineHunting(
												DivineHunting.DivineHuntingSpots.DIVINE_KEBBIT_BURROW,
												x, y, plane, object));
						return;
					}
					if (id == 87271) {
						player.getActionManager()
								.setAction(
										new DivineHunting(
												DivineHunting.DivineHuntingSpots.DIVINE_BIRD_SNARE,
												x, y, plane, object));
						return;
					}
					if (id == 87272) {
						player.getActionManager()
								.setAction(
										new DivineHunting(
												DivineHunting.DivineHuntingSpots.DIVINE_DEADFALL_TRAP,
												x, y, plane, object));
						return;
					}
					if (id == 87273) {
						player.getActionManager()
								.setAction(
										new DivineHunting(
												DivineHunting.DivineHuntingSpots.DIVINE_BOX_TRAP,
												x, y, plane, object));
						return;
					}
					if (id == 2261 || id == 2262) {
						handleGate(player, object);
						player.lock(2);
						player.addWalkSteps(player.getX() == x ? x + 1 : x,
								object.getY(), 1, false);
					}
					if (object.getId() == 87306)
						player.getInterfaceManager().sendCentralInterface(131);
					else if (((id >= 15477 && id <= 15482) || id == 93284))
						House.enterHousePortal(player);
					else if (id == 77461) {
						player.getPackets()
								.sendGameMessage(
										"Looks like you need special gloves to make this jump.");
						return;
					} else if (id == 1804 && object.getX() == 3115
							&& object.getY() == 3449) {
						if (!player.getInventory().containsItem(983, 1)) {
							player.getPackets().sendGameMessage(
									"This door is locked.");
							return;
						}
						player.addWalkSteps(x, y
								+ (player.getY() >= 3450 ? 0 : 1), -1, false);
					} else if (id == 2350
							&& (object.getX() == 3352 && object.getY() == 3417 && object
									.getPlane() == 0))
						player.useStairs(832, new WorldTile(3177, 5731, 0), 1,
								2);
					else if (id == 2114)
						player.getCoalTrucksManager().removeCoal();
					else if (id == 48496) {
						player.getDungManager().enterDungeon(true);
					} else if (id == 92415)
						Magic.sendTeleportSpell(player, 13652, 13654, 2602,
								2603, 1, 0, new WorldTile(3447, 3694, 0), 10,
								true, Magic.OBJECT_TELEPORT);
					else if (id == 25336 && x == 1770 && y == 5365)
						player.useStairs(-1, new WorldTile(1768, 5366, 1), 0, 1);
					else if (id == 25338 && x == 1769 && y == 5365)
						player.useStairs(-1, new WorldTile(1772, 5366, 0), 0, 1);
					else if (id == 25339 && x == 1778 && y == 5344)
						player.useStairs(-1, new WorldTile(1778, 5343, 1), 0, 1);
					else if (id == 25340 && x == 1778 && y == 5344)
						player.useStairs(-1, new WorldTile(1778, 5346, 0), 0, 1);
					else if (id == 25337 && x == 1744 && y == 5323)
						player.useStairs(-1, new WorldTile(1744, 5321, 1), 0, 1);
					else if (id == 39468 && x == 1744 && y == 5322)
						player.useStairs(-1, new WorldTile(1744, 5325, 0), 0, 1);
					else if (id == 25341)
						player.useStairs(-1, new WorldTile(1823, 5273, 0), 0, 1);
					else if (id == 40208)
						player.useStairs(-1, new WorldTile(1759, 5342, 1), 0, 1);
					else if (id == 69505 && x == 2445 && y == 3433)
						player.useStairs(-1, new WorldTile(2445, 3433, 1), 0, 1);
					else if (id == 36772)
						player.useStairs(827, new WorldTile(3207, 3224, 2), 0,
								1);
					else if (id == 36771)
						player.useStairs(828, new WorldTile(3207, 3222, 3), 0,
								1);
					else if (id == 55402)
						player.useStairs(-1, new WorldTile(2538, 3100, 1), 0, 1);
					else if (id == 1723 && x == 2537 && y == 3097)
						player.useStairs(-1, new WorldTile(2537, 3095, 0), 0, 1);
					else if (id == 2647) {
						if (player.getY() >= 3289 && player.getY() <= 3292) {
							if (player.getSkills().getLevel(12) < 40) {
								player.getDialogueManager()
										.startDialogue("SimpleNPCMessage", 805,
												"You need crafting level of 40 to enter my guild.");
								return;
							}
							if (player.getEquipment().containsOneItem(1005)) {
								player.getDialogueManager()
										.startDialogue("SimpleNPCMessage", 805,
												"You need to equip a brown apron to enter my guild, and not a white apron!");
								return;
							}
							if (!player.getEquipment().containsOneItem(1757)) {
								player.getDialogueManager()
										.startDialogue("SimpleNPCMessage", 805,
												"You need to equip a brown apron to enter my guild.");
								return;
							}
							player.addWalkSteps(player.getX(), 3288, -1, false);
						} else {
							player.addWalkSteps(player.getX(), 3289, -1, false);
						}
					}
					if (object.getId() == 82485) {
						if (player.getX() >= 3425)
							player.setNextWorldTile(new WorldTile(3431, 3558, 1));
						else
							player.setNextWorldTile(new WorldTile(3415, 3558, 1));

					}
					if (object.getId() == 82483) {
						player.getDialogueManager().startDialogue(
								new Dialogue() {
									@Override
									public void start() {
										sendOptionsDialogue(
												"choose an option:",
												"Upstairs.", "Downstairs.");
										stage = 1;
									}

									@Override
									public void run(int interfaceId,
											int componentId, int slotId) {
										switch (stage) {
										case 1:
											switch (componentId) {
											case OPTION_1:
												if (player.getX() >= 3425)
													player.setNextWorldTile(new WorldTile(
															3431, 3558, 2));
												else
													player.setNextWorldTile(new WorldTile(
															3415, 3558, 2));
												end();
												break;
											case OPTION_2:
												if (player.getX() >= 3425)
													player.setNextWorldTile(new WorldTile(
															3431, 3558, 2));
												else
													player.setNextWorldTile(new WorldTile(
															3415, 3558, 2));
												end();
												break;
											}
											break;
										}
									}

									@Override
									public void finish() {
									}
								});
					}
					if (object.getId() == 82481) {
						if (player.getX() >= 3425)
							player.setNextWorldTile(new WorldTile(3431, 3558, 1));
						else
							player.setNextWorldTile(new WorldTile(3415, 3558, 1));
					}
					/*
					 * Priff objects
					 */

					else if (id == 92240) {
						int maxedLevels = 0;
						int bit = player.getVarsManager().getBitValue(25055);
						for (int i = 0; i < 26; i++)
							if (player.getSkills().getLevel(i) == 99)
								maxedLevels++;
						boolean unlockable = (maxedLevels >= 15);
						if (unlockable && bit == 0)
							player.getDialogueManager().startDialogue(
									"SkillPortalSecondD");
						else if (bit == 1)
							player.getDialogueManager().startDialogue(
									"SimpleMessage",
									"This option hasn't been added yet.");
						else if (bit == 2)
							player.setNextWorldTile(new WorldTile(2947, 2953, 0));
						else if (bit == 3)
							player.setNextWorldTile(new WorldTile(2178, 5664, 0));
						else if (bit == 4)
							player.setNextWorldTile(new WorldTile(3651, 5122, 0));
						else if (bit == 5)
							player.setNextWorldTile(new WorldTile(2178, 5664, 0));
						else if (bit == 6)
							player.setNextWorldTile(new WorldTile(3400, 3299, 0));
						else if (bit == 7)
							player.setNextWorldTile(new WorldTile(3315, 2666, 0));
						else if (bit == 8)
							player.setNextWorldTile(new WorldTile(2285, 3054, 0));
						else if (bit == 9)
							player.setNextWorldTile(new WorldTile(4296, 6038, 1));
						else if (bit == 10)
							player.setNextWorldTile(new WorldTile(2461, 3436, 0));
						else if (bit == 11)
							player.setNextWorldTile(new WorldTile(2412, 4436, 0));
						else
							player.getDialogueManager()
									.startDialogue(
											"SimpleMessage",
											"You can only tune one of these skill portals until you've trained at least 15 skills to 99.");
					}

					else if (id == 92256) {
						int bit = player.getVarsManager().getBitValue(25113);
						if (bit == 0)
							player.getDialogueManager().startDialogue(
									"PvMPortalD");
						else
							player.getDialogueManager().startDialogue(
									"ProceedAnywayD");
					} else if (id == 92239) {
						int bit = player.getVarsManager().getBitValue(25054);
						if (bit == 0) {
							player.getDialogueManager().startDialogue(
									"SkillPortalD");
						} // else if (bit == 1) until we add active crystal
							// trees
							// player.setNextWorldTile(new WorldTile(2178, 5664,
							// 0));
						else if (bit == 2)
							player.setNextWorldTile(new WorldTile(2947, 2953, 0));
						else if (bit == 3)
							player.setNextWorldTile(new WorldTile(2178, 5664, 0));
						else if (bit == 4)
							player.setNextWorldTile(new WorldTile(3651, 5122, 0));
						else if (bit == 5)
							player.setNextWorldTile(new WorldTile(2178, 5664, 0));
						else if (bit == 6)
							player.setNextWorldTile(new WorldTile(3400, 3299, 0));
						else if (bit == 7)
							player.setNextWorldTile(new WorldTile(3315, 2666, 0));
						else if (bit == 8)
							player.setNextWorldTile(new WorldTile(2285, 3054, 0));
						else if (bit == 9)
							player.setNextWorldTile(new WorldTile(4296, 6038, 1));
						else if (bit == 10)
							player.setNextWorldTile(new WorldTile(2461, 3436, 0));
						else if (bit == 11)
							player.setNextWorldTile(new WorldTile(2412, 4436, 0));
					}

					else if (id == 92697) {
						if (player.getSkills().getLevel(Skills.DIVINATION) < 75) {
							player.getPackets()
									.sendGameMessage(
											"You need to have a divination level of at least 75 to use this.");
							return;
						}
						player.setNextGraphics(new Graphics(1549));
						switch (Utils.random(3)) {
						case 0:
							player.getPackets()
									.sendGameMessage(
											"You don't know what's waiting for you the in next corner.");
							break;
						case 1:
							player.getPackets().sendGameMessage(
									"Never say never.");
							break;
						}
					} else if (id == 94058) {
						player.getActionManager().setAction(new Harps(object));
						return;
					} else if (id == 94048) {
						if (player.getSkills().getLevel(Skills.PRAYER) < 75) {
							player.getPackets()
									.sendGameMessage(
											"You need to have a prayer level of at least 75 to use this.");
							return;
						}
						player.getActionManager().setAction(
								new Cleansing(object));
					} else if (id == 92885 || id == 92903)
						Bonfire.addLogs(player, object);
					else if (id == 92278) {
						if (player.getCompCapeManager()
								.hasUntrimmedRequirements())
							player.getActionManager().setAction(
									new SitChair(player, 92278, object));
						else
							player.getPackets()
									.sendGameMessage(
											"You must have earned a completionist cape in order to sit on this throne.");
						return;
					} else if (id == 2564)
						player.getDialogueManager().startDialogue("Altar");
					else if (id == 21306 && x == 2317 && y == 3824) {
						if (!Agility.hasLevel(player, 40))
							return;
						player.lock(5);
						player.addWalkSteps(2317, 3832, -1, false);
					} else if (id == 21307 && x == 2317 && y == 3831) {
						if (!Agility.hasLevel(player, 40))
							return;
						player.lock(5);
						player.addWalkSteps(2317, 3823, -1, false);
					} else if (id == 21308) {
						player.lock(5);
						player.addWalkSteps(2343, 3829, -1, false);
					} else if (id == 21309) {
						player.lock(5);
						player.addWalkSteps(2343, 3820, -1, false);
					} else if (id == 59463) {
						player.getDialogueManager()
								.startDialogue("EnterPortal");
					} else if (id == 59462) {
						player.getPackets().sendGameMessage(
								"Leaving citadel...");
						player.setNextWorldTile(new WorldTile(2970, 3288, 0));
					} else if (id == 67966) {
						player.lock();
						player.setNextAnimation(new Animation(6723));
						player.setNextForceMovement(new ForceMovement(
								new WorldTile(player), 4, new WorldTile(2512,
										3509, 0), 7, Utils.getAngle(0, -1)));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								FadingScreen
										.fade(player,
												0,
												() -> {
													player.getPackets()
															.sendGameMessage(
																	"You are swept, out of control, thought horrific underwater currents.");
													player.getPackets()
															.sendGameMessage(
																	"You are swirled beneath the water, dashed agaisnt sharp rocks.");
													player.getPackets()
															.sendGameMessage(
																	"Mystical forces guide you into a cavern below the whirpool.");
													player.setNextWorldTile(new WorldTile(
															1763, 5365, 1));
													player.lock(1);
												});
							}
						}, 4);
					} else if (id == 25216) {
						player.lock();
						FadingScreen
								.fade(player,
										0,
										() -> {
											player.getPackets()
													.sendGameMessage(
															"You are swept, out of control, thought horrific underwater currents.");
											player.getPackets()
													.sendGameMessage(
															"You are swirled beneath the water, dashed agaisnt sharp rocks.");
											player.getPackets()
													.sendGameMessage(
															"You find yourself on the banks of the river, far below the lake.");
											player.setNextWorldTile(new WorldTile(
													2531, 3446, 0));
											player.lock(1);
										});
						// Kuradal dungeon
					} else if (id == 47232)
						KuradalDungeon.enter(player);
					else if (id == 11844) {
						if (player.getX() == 2936)
							player.setNextWorldTile(new WorldTile(2935, 3355, 0));
						else
							player.setNextWorldTile(new WorldTile(2936, 3355, 0));
					}
					// kalphite king
					else if (id == 82014)
						BossInstanceHandler.enterInstance(player,
								Boss.Kalphite_King);
					// vorago
					else if (id == 17819)
						player.useStairs(828, new WorldTile(3043, 6100, 0), 1,
								2);
					else if (id == 84909) // vorago
											// graveyard
											// exit
						player.useStairs(828, new WorldTile(2972, 3431, 0), 1,
								2);
					// White Wolf Mountain cut

					else if (id == 56 && x == 2876 && y == 9880)
						player.useStairs(-1, new WorldTile(2879, 3465, 0), 0, 1);
					else if (id == 66990 && x == 2876 && y == 3462)
						player.useStairs(-1, new WorldTile(2875, 9880, 0), 0, 1);
					else if (id == 54 && x == 2820 && y == 9883)
						player.useStairs(-1, new WorldTile(2820, 3486, 0), 0, 1);
					else if (id == 55 && x == 2820 && y == 3484)
						player.useStairs(-1, new WorldTile(2821, 9882, 0), 0, 1);
					// sabbot lair
					else if (id == 34395 && x == 2857 && y == 3578)
						player.useStairs(-1, new WorldTile(2893, 10074, 0), 0,
								1);
					else if (id >= 2889 && id <= 2892) {
						boolean isNorth = player.getY() >= 2940;
						player.useStairs(-1,
								player.transform(0, isNorth ? -8 : 8, 0), 2, 3,
								"You slash your way through the marsh and get to the other side.");
					} else if (id == 32738 && x == 2892 && y == 10072)
						player.useStairs(-1, new WorldTile(2858, 3577, 0), 0, 1);
					else if (id == 34548 && x == 2610 && y == 3305)
						player.useStairs(-1, new WorldTile(2611, 3307, 1), 1, 2);
					else if (id == 34550 && x == 2610 && y == 3305)
						player.useStairs(-1, new WorldTile(2611, 3304, 0), 1, 2);
					else if (id == 1987)
						player.useStairs(-1, new WorldTile(2513, 3480, 0), 1,
								2,
								"The raft is pulled down by the strong currents.");
					else if (id == 10283)
						player.getPackets().sendGameMessage(
								"I don't think that's a very smart idea...");
					// varrock museum
					else if (id == 2562)
						player.getDialogueManager().startDialogue(
								"CompletionistCape");
					else if (id == 21514)
						player.useStairs(828, new WorldTile(2329, 3802, 1), 1,
								2);
					else if (id == 21515)
						player.useStairs(827, new WorldTile(2331, 3802, 0), 1,
								2);
					else if (id == 21512)
						player.useStairs(828, new WorldTile(2364, 3799, 2), 1,
								2);
					else if (id == 21513)
						player.useStairs(827, new WorldTile(2362, 3799, 0), 1,
								2);
					else if (id == 24359 && x == 3253 && y == 3444)
						player.useStairs(-1, new WorldTile(3253, 3442, 1), 0, 1);
					else if (id == 24357 && x == 3253 && y == 3443)
						player.useStairs(-1, new WorldTile(3253, 3446, 2), 0, 1);
					else if (id == 24359 && x == 3266 && y == 3453)
						player.useStairs(-1, new WorldTile(3267, 3451, 0), 0, 1);
					else if (id == 71903 && x == 2965 && y == 3219)
						player.useStairs(-1, new WorldTile(2964, 3219, 0), 0, 1);
					else if (id == 71902 && x == 2965 && y == 3219)
						player.useStairs(-1, new WorldTile(2968, 3219, 1), 0, 1);
					else if (id == 24358 && x == 3266 && y == 3452)
						player.useStairs(-1, new WorldTile(3267, 3455, 1), 0, 1);
					else if (id == 2788 || id == 2789) {
						boolean isEntering = player.getX() >= 2504;
						player.addWalkSteps(x + (isEntering ? -1 : 0), y, 1,
								false);
						handleGate(player, object, 600);
					} else if (id == 29099) {
						if (!Agility.hasLevel(player, 29))
							return;
						player.useStairs(
								1133,
								new WorldTile(2596,
										player.getY() <= 2869 ? 2871 : 2869, 0),
								1, 2);
					} else if (id == 3944 || id == 3945)
						player.addWalkSteps(x, y
								+ (player.getY() >= 3335 ? -1 : 1), -1, false);
					else if (id == 31149) {
						boolean isEntering = player.getX() <= 3295;
						player.useStairs(isEntering ? 9221 : 9220,
								new WorldTile(x + (isEntering ? 1 : 0), y, 0),
								1, 2);
					} else if (id == 2333 || id == 2334 || id == 2335) {
						if (!Agility.hasLevel(player, 30))
							return;
						player.setNextAnimation(new Animation(741));
						player.setNextForceMovement(new ForceMovement(object,
								1, object, 2, Utils.getAngle(object.getX()
										- player.getX(),
										object.getY() - player.getY())));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextWorldTile(object);
							}
						});
					} else if (id == 56805) {
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.useStairs(
										3303,
										new WorldTile(
												player.getX(),
												player.getY()
														+ ((y == 2941 ? player
																.getY() >= 2942
																: player.getY() >= 2917) ? -2
																: 2), 0), 4, 5,
										null, true);
							}
						}, 1);
					}
					// entrana dungeon
					else if (id == 2408 && x == 2820 && y == 3374)
						player.useStairs(828, new WorldTile(2829, 9773, 0), 1,
								2);
					else if (id == 2407)
						player.useStairs(-1, new WorldTile(2453, 4476, 0), 0, 1);
					else if (id >= 65616 && id <= 65622)
						WildernessObelisk.preTeleport(player, object);
					// polypore dungon
					else if (id == 64125) {
						int value = player.getVarsManager().getBitValue(10232);
						if (value == 7)
							return;
						player.lock(2);
						player.setNextAnimation(new Animation(15460));
						player.getInventory().addItem(22445, 1);
						if (value == 0) {
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									int value = player.getVarsManager()
											.getBitValue(10232);
									player.getVarsManager().sendVarBit(10232,
											value - 1);
									if (value == 1)
										stop();
								}
							}, 9, 9);
						}
						player.getVarsManager().sendVarBit(10232, value + 1);
					} else if (id == 64360 && x == 4629 && y == 5453)
						PolyporeCreature.useStairs(player, new WorldTile(4629,
								5451, 2), true);
					else if (id == 64361 && x == 4629 && y == 5452)
						PolyporeCreature.useStairs(player, new WorldTile(4629,
								5454, 3), false);
					else if (id == 64359 && x == 4632 && y == 5443)
						PolyporeCreature.useStairs(player, new WorldTile(4632,
								5443, 1), true);
					else if (id == 64361 && x == 4632 && y == 5442)
						PolyporeCreature.useStairs(player, new WorldTile(4632,
								5444, 2), false);
					else if (id == 64359 && x == 4632 && y == 5409)
						PolyporeCreature.useStairs(player, new WorldTile(4632,
								5409, 2), true);
					else if (id == 64361 && x == 4633 && y == 5409)
						PolyporeCreature.useStairs(player, new WorldTile(4631,
								5409, 3), false);
					else if (id == 64359 && x == 4642 && y == 5389)
						PolyporeCreature.useStairs(player, new WorldTile(4642,
								5389, 1), true);
					else if (id == 64361 && x == 4643 && y == 5389)
						PolyporeCreature.useStairs(player, new WorldTile(4641,
								5389, 2), false);
					else if (id == 64359 && x == 4652 && y == 5388)
						PolyporeCreature.useStairs(player, new WorldTile(4652,
								5388, 0), true);
					else if (id == 64362 && x == 4652 && y == 5387)
						PolyporeCreature.useStairs(player, new WorldTile(4652,
								5389, 1), false);
					else if (id == 64359 && x == 4691 && y == 5469)
						PolyporeCreature.useStairs(player, new WorldTile(4691,
								5469, 2), true);
					else if (id == 64361 && x == 4691 && y == 5468)
						PolyporeCreature.useStairs(player, new WorldTile(4691,
								5470, 3), false);
					else if (id == 64359 && x == 4689 && y == 5479)
						PolyporeCreature.useStairs(player, new WorldTile(4689,
								5479, 1), true);
					else if (id == 64361 && x == 4689 && y == 5480)
						PolyporeCreature.useStairs(player, new WorldTile(4689,
								5478, 2), false);
					else if (id == 64359 && x == 4698 && y == 5459)
						PolyporeCreature.useStairs(player, new WorldTile(4698,
								5459, 2), true);
					else if (id == 64361 && x == 4699 && y == 5459)
						PolyporeCreature.useStairs(player, new WorldTile(4697,
								5459, 3), false);
					else if (id == 64359 && x == 4705 && y == 5460)
						PolyporeCreature.useStairs(player, new WorldTile(4704,
								5461, 1), true);
					else if (id == 64361 && x == 4705 && y == 5461)
						PolyporeCreature.useStairs(player, new WorldTile(4705,
								5459, 2), false);
					else if (id == 64359 && x == 4718 && y == 5467)
						PolyporeCreature.useStairs(player, new WorldTile(4718,
								5467, 0), true);
					else if (id == 64361 && x == 4718 && y == 5466)
						PolyporeCreature.useStairs(player, new WorldTile(4718,
								5468, 1), false);
					else if (id == 1722)
						player.setNextWorldTile(new WorldTile(2590, 3092, 1));
					else if (id == 1723)
						player.setNextWorldTile(new WorldTile(2590, 3088, 0));
					else if (id == 89158)
						player.setNextWorldTile(new WorldTile(1750, 5243, 0));
					else if (id == 89159)
						player.setNextWorldTile(new WorldTile(2984, 3382, 0));
					else if (id == 91557)
						player.setNextWorldTile(new WorldTile(4502, 6284, 1));
					else if (id == 91553)
						player.setNextWorldTile(new WorldTile(3700, 3419, 0));
					else if (id == 91500)
						player.getDialogueManager().startDialogue(
								"AraxxorStart");
					else if (id == 84702) {
						player.setNextWorldTile(new WorldTile(1095, 580, 1));
					} else if (id == 84724) {
						player.setNextWorldTile(new WorldTile(2500, 2886, 0));
					} else if (id == 92435)
						player.useStairs(-1, new WorldTile(2214, 3363, 2), 1, 1);
					else if (id == 92436)
						player.useStairs(-1, new WorldTile(2215, 3364, 1), 1, 1);
					else if (id == 25340)
						player.useStairs(-1, new WorldTile(1778, 5346, 0), 1, 1);

					/**
					 * Pyramid Plunder objects start
					 */
					if (object.getId() == 6497)
						player.useStairs(828, new WorldTile(2913, 4954, 3), 2,
								2);
					if (object.getId() == 6504)
						player.useStairs(828, new WorldTile(3233, 2898, 0), 2,
								2);
					if (object.getId() == 6545 || object.getId() == 6547) {
						if (player.getY() >= 2900 && player.getY() <= 2903) {
							player.addWalkSteps(player.getX(), 2898, 0, false);
						} else {
							player.addWalkSteps(player.getX(),
									player.getY() + 2, 0, false);
						}
					}
					if (object.getId() == 16543) {
						if (player.getSkills().getLevel(Skills.THIEVING) < 21) {
							player.getPackets()
									.sendGameMessage(
											"You need a thieving level of 21 or more to enter the pyramid.");
							return;
						}
						player.setNextAnimation(new Animation(23314));
						player.getSkills().addXp(17, 5);
						player.getPackets()
								.sendGameMessage(
										"You use your thieving skills to search for stone panel.",
										true);
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								FadingScreen.unfade(player, FadingScreen.fade(
										player, 500), () -> player
										.getControllerManager()
										.startController("PyramidPlunder"));
								stop();
							}
						}, 0, 1500);
						/**
						 * Pyramid Plunder objects end
						 */
					} else if (id == 12290 || id == 12272) {
						if (id == 12290)
							player.setFavorPoints(1 - player.getFavorPoints());
						player.getActionManager().setAction(
								new Woodcutting(object,
										TreeDefinitions.STRAIT_VINE));// start
						// of
						// jadinkos
					} else if (id == 12328) {
						player.useStairs(3527, new WorldTile(3012, 9275, 0), 5,
								6);
						player.setNextForceMovement(new ForceMovement(player,
								3, object, 2, ForceMovement.WEST));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextFaceWorldTile(new WorldTile(3012,
										9274, 0));
								player.setNextAnimation(new Animation(11043));
								player.getControllerManager().startController(
										"JadinkoLair");
							}
						}, 4);
					} else if (id == 12277)
						player.getActionManager()
								.setAction(
										new Woodcutting(
												object,
												TreeDefinitions.STRAIT_VINE_COLLECTIBLE));// start
					// of
					// jadinkos
					else if (id == 12291)
						player.getActionManager().setAction(
								new Woodcutting(object,
										TreeDefinitions.MUTATED_VINE));
					else if (id == 12274)
						player.getActionManager().setAction(
								new Woodcutting(object,
										TreeDefinitions.CURLY_VINE));
					else if (id == 12279)
						player.getActionManager()
								.setAction(
										new Woodcutting(
												object,
												TreeDefinitions.CURLY_VINE_COLLECTIBLE));
					else if (id == 26684 || id == 26685 || id == 26686) // poison
						// waste
						// cave
						player.useStairs(-1, new WorldTile(1989, 4174, 0), 1,
								2, "You enter the murky cave...");
					else if (id == 26571 || id == 26572 || id == 26573
							|| id == 26574)
						player.useStairs(-1, new WorldTile(2321, 3100, 0), 1, 2);
					else if (id == 26560 && x == 2015 && y == 4255)
						player.getDialogueManager()
								.startDialogue(
										"SimpleMessage",
										"The room beyond the door is covred in gas, it is probably dangerous to go in there.");
					else if (id == 26519) {
						if (x == 1991 && y == 4175)
							player.useStairs(827, new WorldTile(1991, 4175, 0),
									1, 2);
						else if (x == 1998 && y == 4218)
							player.useStairs(827, new WorldTile(1998, 4218, 0),
									1, 2);
						else if (x == 2011 && y == 4218)
							player.useStairs(827, new WorldTile(2011, 4218, 0),
									1, 2);
						else
							player.useStairs(827, new WorldTile(x - 1, y, 0),
									1, 2);
					} else if (id == 26518) {
						if (x == 1991 && y == 4175)
							player.useStairs(828, new WorldTile(1991, 4176, 1),
									1, 2);
						else if (x == 1998 && y == 4218)
							player.useStairs(828, new WorldTile(1998, 4219, 1),
									1, 2);
						else if (x == 2011 && y == 4218)
							player.useStairs(828, new WorldTile(2011, 4219, 1),
									1, 2);
						else
							player.useStairs(828, new WorldTile(x + 1, y, 1),
									1, 2);
					} else if (id == 17270 || id == 17269)
						player.addWalkSteps(x, id == 17270 ? 9817 : 9814, -1,
								false);
					else if (id == 17239)
						player.getDialogueManager()
								.startDialogue("SimpleMessage",
										"You look into the bowl and a strange feeling goes amonst your body.");
					// wizards guild
					else if (id == 1722 && x == 2590 && y == 3089)
						player.useStairs(-1, new WorldTile(2590, 3092, 1), 0, 1);
					else if (id == 1723 && x == 2590 && y == 3090)
						player.useStairs(-1, new WorldTile(2590, 3088, 0), 0, 1);
					else if (id == 1722 && x == 2590 && y == 3084)
						player.useStairs(-1, new WorldTile(2590, 3087, 2), 0, 1);
					else if (id == 1723 && x == 2590 && y == 3085)
						player.useStairs(-1, new WorldTile(2591, 3083, 1), 0, 1);
					else if (id == 2158)
						player.useStairs(-1, new WorldTile(3104, 3163, 2), 0, 1);
					else if (id == 2157)
						player.useStairs(-1, new WorldTile(2908, 3332, 2), 0, 1);
					else if (id == 2156)
						player.useStairs(-1, new WorldTile(2702, 3405, 3), 0, 1);
					else if (id == 1754 && x == 2594 && y == 3085)
						player.useStairs(827, new WorldTile(2594, 9486, 0), 1,
								2);
					else if (id == 1757 && x == 2594 && y == 9485)
						player.useStairs(828, new WorldTile(2594, 3086, 0), 1,
								2);
					else if (id == 65203) {
						if (x == 3118 && y == 3570)
							player.useStairs(827, new WorldTile(3249, 5491, 0),
									1, 2);
						else if (x == 3058 && y == 3550)
							player.useStairs(827, new WorldTile(3184, 5471, 0),
									1, 2);
						else if (x == 3129 && y == 3587)
							player.useStairs(827, new WorldTile(3235, 5560, 0),
									1, 2);
						else if (x == 3176 && y == 3585)
							player.useStairs(827, new WorldTile(3290, 5539, 0),
									1, 2);
						else if (x == 3164 && y == 3561)
							player.useStairs(827, new WorldTile(3292, 5479, 0),
									1, 2);
					} else if (id == 28782) {
						if (x == 3183 && y == 5470)
							player.useStairs(828, new WorldTile(3057, 3551, 0),
									1, 2);
						else if (x == 3248 && y == 5490)
							player.useStairs(828, new WorldTile(3119, 3570, 0),
									1, 2);
						else if (x == 3234 && y == 5559)
							player.useStairs(828, new WorldTile(3130, 3586, 0),
									1, 2);
						else if (x == 3291 && y == 5538)
							player.useStairs(828, new WorldTile(3177, 3585, 0),
									1, 2);
						else if (x == 3292 && y == 5479)
							player.useStairs(828, new WorldTile(3165, 3562, 0),
									1, 2);
						player.getControllerManager().startController(
								"Wilderness");
					} else if (id == 77745 || id == 28779) {
						if (x == 3142 && y == 5545) {
							BorkController.enterBork(player);
							return;
						}
						player.addWalkSteps(object.getX(), object.getY(), -1,
								false);
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								if (getRepeatedTele(player, 3285, 5474, 0,
										3286, 5470, 0))
									return;
								else if (getRepeatedTele(player, 3302, 5469, 0,
										3290, 5463, 0))
									return;
								else if (getRepeatedTele(player, 3280, 5460, 0,
										3273, 5460, 0))
									return;
								else if (getRepeatedTele(player, 3299, 5450, 0,
										3296, 5455, 0))
									return;
								else if (getRepeatedTele(player, 3283, 5448, 0,
										3287, 5448, 0))
									return;
								else if (getRepeatedTele(player, 3260, 5491, 0,
										3266, 5446, 0))
									return;
								else if (getRepeatedTele(player, 3239, 5498, 0,
										3244, 5495, 0))
									return;
								else if (getRepeatedTele(player, 3238, 5507, 0,
										3232, 5501, 0))
									return;
								else if (getRepeatedTele(player, 3222, 5488, 0,
										3218, 5497, 0))
									return;
								else if (getRepeatedTele(player, 3222, 5474, 0,
										3224, 5479, 0))
									return;
								else if (getRepeatedTele(player, 3215, 5475, 0,
										3218, 5478, 0))
									return;
								else if (getRepeatedTele(player, 3210, 5477, 0,
										3208, 5471, 0))
									return;
								else if (getRepeatedTele(player, 3212, 5452, 0,
										3214, 5456, 0))
									return;
								else if (getRepeatedTele(player, 3235, 5457, 0,
										3229, 5454, 0))
									return;
								else if (getRepeatedTele(player, 3204, 5445, 0,
										3197, 5448, 0))
									return;
								else if (getRepeatedTele(player, 3191, 5495, 0,
										3194, 5490, 0))
									return;
								else if (getRepeatedTele(player, 3185, 5478, 0,
										3191, 5482, 0))
									return;
								else if (getRepeatedTele(player, 3186, 5472, 0,
										3192, 5472, 0))
									return;
								else if (getRepeatedTele(player, 3189, 5444, 0,
										3187, 5460, 0))
									return;
								else if (getRepeatedTele(player, 3178, 5460, 0,
										3168, 5456, 0))
									return;
								else if (getRepeatedTele(player, 3171, 5478, 0,
										3167, 5478, 0))
									return;
								else if (getRepeatedTele(player, 3171, 5473, 0,
										3167, 5471, 0))
									return;
								else if (getRepeatedTele(player, 3142, 5489, 0,
										3141, 5480, 0))
									return;
								else if (getRepeatedTele(player, 3142, 5462, 0,
										3154, 5462, 0))
									return;
								else if (getRepeatedTele(player, 3155, 5449, 0,
										3143, 5443, 0))
									return;
								else if (getRepeatedTele(player, 3303, 5477, 0,
										3299, 5484, 0))
									return;
								else if (getRepeatedTele(player, 3318, 5481, 0,
										3322, 5480, 0))
									return;
								else if (getRepeatedTele(player, 3307, 5496, 0,
										3317, 5496, 0))
									return;
								else if (getRepeatedTele(player, 3265, 5491, 0,
										3260, 5491, 0))
									return;
								else if (getRepeatedTele(player, 3297, 5510, 0,
										3300, 5514, 0))
									return;
								else if (getRepeatedTele(player, 3325, 5518, 0,
										3323, 5531, 0))
									return;
								else if (getRepeatedTele(player, 3321, 5554, 0,
										3315, 5552, 0))
									return;
								else if (getRepeatedTele(player, 3291, 5555, 0,
										3285, 5556, 0))
									return;
								else if (getRepeatedTele(player, 3285, 5508, 0,
										3280, 5501, 0))
									return;
								else if (getRepeatedTele(player, 3285, 5527, 0,
										3282, 5531, 0))
									return;
								else if (getRepeatedTele(player, 3289, 5532, 0,
										3288, 5536, 0))
									return;
								else if (getRepeatedTele(player, 3266, 5552, 0,
										3262, 5552, 0))
									return;
								else if (getRepeatedTele(player, 3268, 5534, 0,
										3261, 5536, 0))
									return;
								else if (getRepeatedTele(player, 3248, 5547, 0,
										3253, 5561, 0))
									return;
								else if (getRepeatedTele(player, 3256, 5561, 0,
										3252, 5543, 0))
									return;
								else if (getRepeatedTele(player, 3244, 5526, 0,
										3241, 5529, 0))
									return;
								else if (getRepeatedTele(player, 3230, 5547, 0,
										3226, 5553, 0))
									return;
								else if (getRepeatedTele(player, 3206, 5553, 0,
										3204, 5546, 0))
									return;
								else if (getRepeatedTele(player, 3211, 5533, 0,
										3214, 5533, 0))
									return;
								else if (getRepeatedTele(player, 3208, 5527, 0,
										3211, 5523, 0))
									return;
								else if (getRepeatedTele(player, 3201, 5531, 0,
										3197, 5529, 0))
									return;
								else if (getRepeatedTele(player, 3202, 5516, 0,
										3196, 5512, 0))
									return;
								if (getRepeatedTele(player, 3197, 5529, 0,
										3201, 5531, 0)) {
								} else if (getRepeatedTele(player, 3165, 5515,
										0, 3173, 5530, 0)) {
								} else if (getRepeatedTele(player, 3156, 5523,
										0, 3152, 5520, 0)) {
								} else if (getRepeatedTele(player, 3148, 5533,
										0, 3153, 5537, 0)) {
								} else if (getRepeatedTele(player, 3143, 5535,
										0, 3147, 5541, 0)) {
								} else if (getRepeatedTele(player, 3158, 5561,
										0, 3162, 5557, 0)) {
								} else if (getRepeatedTele(player, 3162, 5545,
										0, 3166, 5553, 0)) {
								} else if (getRepeatedTele(player, 3168, 5541,
										0, 3171, 5542, 0)) {
								} else if (getRepeatedTele(player, 3190, 5549,
										0, 3190, 5554, 0)) {
								} else if (getRepeatedTele(player, 3180, 5557,
										0, 3174, 5558, 0)) {
								} else if (getRepeatedTele(player, 3190, 5519,
										0, 3190, 5515, 0)) {
								} else if (getRepeatedTele(player, 3185, 5518,
										0, 3181, 5517, 0)) {
								} else if (getRepeatedTele(player, 3196, 5512,
										0, 3202, 5516, 0)) {
								} else if (getRepeatedTele(player, 3159, 5501,
										0, 3169, 5510, 0)) {
								} else if (getRepeatedTele(player, 3182, 5530,
										0, 3187, 5531, 0)) {
								}
							}
						});
					} else if (id == 1600 || id == 1601) {
						if (player.getSkills().getLevel(Skills.MAGIC) < 66) {
							player.getDialogueManager()
									.startDialogue("SimpleNPCMessage", 13,
											"Sorry, but you need level 66 Magic to enter here.");
							return;
						}
						player.lock(1);
						player.addWalkSteps(player.getX() == x ? x - 1 : x,
								object.getY(), 1, false);
					} else if (id == 26849) { // ZMI Altar down ladder
						player.setNextAnimation(new Animation(828));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								player.setNextWorldTile(new WorldTile(3271,
										4861, 0));
							}
						}, 1);
						return;
					} else if (id == 26850) { // ZMI Altar up ladder
						player.setNextAnimation(new Animation(828));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								player.setNextWorldTile(new WorldTile(2452,
										3232, 0));
							}
						}, 1);
						return;
					} else if (id == 26847) {
						Runecrafting.craftZMIAltar(player);
						return;
					}
					// heroes guild

					else if (id == 2624 || id == 2625) {
						if (!player.getQuestManager().completedQuest(
								Quests.HEROES_QUEST)) {
							player.getPackets()
									.sendGameMessage(
											"Please come back after you have Heroes' Quest quest requiriments.");
							return;
						}
						player.lock(1);
						player.addWalkSteps(player.getX() == x ? x - 1 : x,
								object.getY(), 1, false);
					} else if (id == 67346 && x == 2908 && y == 3512)
						player.useStairs(-1, new WorldTile(2912, 3514, 2), 0, 1);
					else if (id == 67694 && x == 2911 && y == 3513)
						player.useStairs(-1, new WorldTile(2907, 3514, 1), 0, 1);
					else if (id == 67690 && x == 2905 && y == 3516)
						player.useStairs(-1, new WorldTile(2893, 9907, 0), 0, 1);
					else if (id == 67691 && x == 2892 && y == 9907)
						player.useStairs(-1, new WorldTile(2906, 3516, 0), 0, 1);
					else if (id == 15653) {
						if (World.isSpawnedObject(object)
								|| !WarriorsGuild.canEnter(player))
							return;
						player.lock(2);
						new WorldObject(object.getId(), object.getType(),
								object.getRotation() - 1, object.getX(), object
										.getY(), object.getPlane());
						// TODO: properly fix
						// World.spawnObjectTemporary(opened, 600);
						player.addWalkSteps(object.getX() - 1, player.getY(),
								2, false);
					}
					// tarns lair
					else if (id == 20573 && x == 3149 && y == 4663)
						player.getControllerManager().startController(
								"TerrorDogsTarnsLairController");
					else if (id == 4918 && x == 3445 && y == 3236)
						player.useStairs(4853,
								new WorldTile(player.getX() == 3446 ? 3444
										: 3446, object.getY(), 0), 2, 3);
					else if (id == 12776)
						player.useStairs(4853,
								new WorldTile(player.getX() == 3474 ? 3473
										: 3474, object.getY(), 0), 2, 3);
					else if (id == 17757 || id == 17760)
						player.useStairs(4853,
								new WorldTile(object.getX(),
										object.getY() == player.getY() ? 3243
												: 3244, 0), 2, 3);
					else if (id == 20979)
						player.useStairs(-1, new WorldTile(3149, 4666, 0), 0, 1);
					else if (id == 4913 && x == 3440 && y == 3232)
						player.useStairs(-1, new WorldTile(3436, 9637, 0), 0, 1);
					else if (id == 4920 && x == 3437 && y == 9637)
						player.useStairs(-1, new WorldTile(3441, 3232, 0), 0, 1);
					else if (id == 3522 && y == 3329)
						player.useStairs(-1, new WorldTile(x, 3332, 0), 0, 1);
					else if (id == 3522 && y == 3331)
						player.useStairs(-1, new WorldTile(x, 3329, 0), 0, 1);
					else if (id == 4914 && x == 3430 && y == 3233)
						player.useStairs(-1, new WorldTile(3405, 9631, 0), 0, 1);
					else if (id == 4921 && x == 3404 && y == 9631)
						player.useStairs(-1, new WorldTile(3429, 3233, 0), 0, 1);
					else if (id == 20524 && x == 3408 && y == 9623)
						player.useStairs(-1, new WorldTile(3428, 3225, 0), 0, 1);
					else if (id == 4915 && x == 3429 && y == 3225)
						player.useStairs(-1, new WorldTile(3409, 9623, 0), 0, 1);
					else if (id == 28515)
						player.useStairs(4853, new WorldTile(3420, 2803, 1), 2,
								3);
					// scabaras florest
					else if (id == 28515)
						player.useStairs(4853, new WorldTile(3420, 2803, 1), 2,
								3);
					else if (id == 28516)
						player.useStairs(4853, new WorldTile(3420, 2801, 0), 2,
								3);
					// legends guild
					else if (id == 41435 && x == 2732 && y == 3377)
						player.useStairs(-1, new WorldTile(2732, 3380, 1), 0, 1);
					else if (id == 41436 && x == 2732 && y == 3378)
						player.useStairs(-1, new WorldTile(2732, 3376, 0), 0, 1);
					else if (id == 41425 && x == 2724 && y == 3374)
						player.useStairs(-1, new WorldTile(2720, 9775, 0), 0, 1);
					else if (id == 32048 && x == 2717 && y == 9773)
						player.useStairs(-1, new WorldTile(2723, 3375, 0), 0, 1);
					else if (id == 41449 || id == 66449 || id == 782)
						player.getDialogueManager().startDialogue("ChooseBank");
					else if (id == 2938) { // recharge combat bracelet + skill
											// necklackes
						for (Item item : player.getInventory().getItems()
								.getItems()) {
							if (item == null)
								continue;
							if (item.getId() >= 11120 && item.getId() <= 11126
									&& item.getId() % 2 == 0)
								item.setId(11118);
							else if (item.getId() >= 11107
									&& item.getId() <= 11113
									&& item.getId() % 2 != 0)
								item.setId(11105);
						}
						player.getInventory().refresh();
						player.getDialogueManager()
								.startDialogue(
										"ItemMessage",
										"Your combat bracelet and skill necklace have all been recharged.",
										11105);
					} else if (id == 2896 || id == 2897) {
						player.lock(1);
						player.addWalkSteps(object.getX(),
								player.getY() == y ? y + 1 : y, 1, false);
					} else if (id == 2391 || id == 2392) {
						if (!player.getQuestManager().completedQuest(
								Quests.LEGENDS_QUEST)) {
							player.getPackets()
									.sendGameMessage(
											"Please come back after you have Legends' Quest quest requiriments.");
							return;
						}
						player.lock(1);
						player.addWalkSteps(object.getX(),
								player.getY() == y ? y + 1 : y, 1, false);
						if (player.getY() == y) {
							List<Integer> indexes = World.getRegion(10804)
									.getNPCsIndexes();
							if (indexes != null) {
								for (Integer index : indexes) {
									NPC npc = World.getNPCs().get(index);
									if (npc == null
											|| (npc.getId() != 398 && npc
													.getId() != 399))
										continue;
									npc.setNextForceTalk(new ForceTalk(
											"Legends' guild member approaching!"));
								}
							}
						}
					}
					// start of death platue
					else if (id == 34877 || id == 34878 || id == 9303
							|| id == 9304 || id == 9305 || id == 9306) {
						if (!Agility.hasLevel(player, 61))
							return;
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								boolean isGoingDown = id == 34877 ? player
										.getY() >= 3620 : id == 34878 ? player
										.getY() >= 9587 : id == 9303 ? player
										.getX() >= 2856 : id == 9306 ? player
										.getX() <= 2909 : id == 9305 ? player
										.getX() <= 2894 : player.getY() >= 3662;
								if (isGoingDown)
									player.useStairs(
											3382,
											id == 34877 ? new WorldTile(2877,
													3618, 0)
													: id == 34878 ? new WorldTile(
															2875, 3594, 0)
															: id == 9303 ? new WorldTile(
																	2854, 3664,
																	0)
																	: id == 9306 ? new WorldTile(
																			2912,
																			3687,
																			0)
																			: id == 9305 ? new WorldTile(
																					2897,
																					3674,
																					0)
																					: new WorldTile(
																							2875,
																							3659,
																							0),
											6, 7, null, true);
								else
									player.useStairs(
											3381,
											id == 34877 ? new WorldTile(2876,
													3622, 0)
													: id == 34878 ? new WorldTile(
															2875, 3598, 0)
															: id == 9303 ? new WorldTile(
																	2858, 3664,
																	0)
																	: id == 9306 ? new WorldTile(
																			2908,
																			3686,
																			0)
																			: id == 9305 ? new WorldTile(
																					2893,
																					3673,
																					0)
																					: new WorldTile(
																							2874,
																							3663,
																							0),
											6, 7, null, true);
							}
						}, 1);
					} else if (id == 3803) {
						if (!Agility.hasLevel(player, 64))
							return;
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								boolean isGoingDown = player.getX() >= 2877;
								if (isGoingDown)
									player.useStairs(15239, new WorldTile(2875,
											3672, 0), 4, 5, null, true);
								else
									player.useStairs(3378, new WorldTile(2879,
											3673, 0), 4, 5, null, true);
							}
						}, 1);
					} else if (id == 3748 || id == 5847) {
						if (!Agility.hasLevel(player, 41))
							return;
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								boolean isFailed = Utils.random(4) == 0;
								if (isFailed) {
									player.applyHit(new Hit(player, Utils
											.random(20, 50),
											HitLook.REGULAR_DAMAGE));
									player.getPackets()
											.sendGameMessage(
													"You skid your knee across the rocks.");
								}
								boolean isTravelingEast = id == 5847 ? player
										.getX() >= 2760
										: (x == 2817 && y == 3630)
												&& player.getX() >= 2817;
								boolean isTravelingNorth = !isTravelingEast
										&& ((x == 2846 && y == 3620) ? player
												.getY() >= 3620
												: player.getY() >= 3675);

								if (x == 2846 && y == 3620) {
									if (player.getEquipment().getBootsId() != 3105
											&& player.getEquipment()
													.getBootsId() != 6145) {
										player.getDialogueManager()
												.startDialogue("SimpleMessage",
														"You need rock climbing boots in order to jump this ledge.");
										return;
									}
								}
								player.useStairs(3377, new WorldTile(
										isTravelingNorth ? player.getX()
												: (isTravelingEast ? -1 : 1)
														+ x, (isTravelingEast
												|| (x == 2817 && y == 3630)
												|| id == 5847 ? 0
												: (isTravelingNorth ? -2 : 2))
												+ player.getY(), 0), 4, 5,
										null, true);
							}
						});
					} else if (id == 35391 || id == 2832) {
						if (!Agility.hasLevel(player, id == 2832 ? 20 : 41))
							return;
						player.addWalkSteps(x, y);
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								boolean isTravelingWest = id == 2832 ? player
										.getX() >= 2508
										: (x == 2834 && y == 3626) ? player
												.getX() >= 2834
												: player.getX() >= 2900;
								player.useStairs(
										3303,
										new WorldTile(
												(isTravelingWest ? -2 : 2)
														+ player.getX(), player
														.getY(), 0), 4, 5,
										null, true);
							}
						});
					} else if (id == 19171) {
						if (!Agility.hasLevel(player, 20))
							return;
						player.useStairs(-1, new WorldTile(
								player.getX() >= 2523 ? 2522 : 2523, 3375, 0),
								1, 2, "You easily squeeze through the railing.");
					} else if (id == 2830 || id == 2831) {
						player.useStairs(-1, new WorldTile(player.getX(),
								id == 2831 ? 3026 : 3029, 0), 1, 2);
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.getDialogueManager().startDialogue(
										"SimplePlayerMessage",
										"Phew! I barely made it.");
							}
						}, 1);
					} else if (id == 34839 || id == 34836) {
						boolean firstDoor = x == 2912;
						boolean withinArea = firstDoor ? player.getX() > 2912
								: player.getY() < 3619;
						WorldObject opened = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX(), object.getY(), object.getPlane());
						World.spawnObjectTemporary(opened, 600);
						if (firstDoor)
							player.addWalkSteps(withinArea ? x : x + 1,
									player.getY(), 1, false);
						else
							player.addWalkSteps(player.getX(), withinArea ? y
									: y - 1, 1, false);
					} else if (id == 3758) {
						if (x == 2906 && y == 10017)
							player.useStairs(-1, new WorldTile(2911, 3636, 0),
									1, 2);
						else if (x == 2906 && y == 10036)
							player.useStairs(-1, new WorldTile(2922, 3658, 0),
									1, 2);
					} else if (id == 34395) {
						if (x == 2910 && y == 3637)
							player.useStairs(-1, new WorldTile(2907, 10019, 0),
									1, 2);
						else if (x == 2920 && y == 3654)
							player.useStairs(-1, new WorldTile(2907, 10035, 0),
									1, 2);
						else if (x == 2796 && y == 3614)
							player.useStairs(-1, new WorldTile(2808, 10002, 0),
									1, 2);
					} else if (id == 35390)
						GodWars.passGiantBoulder(player, object, true);
					// shanty pass
					else if (id == 76651 || id == 76652)
						player.getDialogueManager().startDialogue(
								"ShantyPassDangerSignD");
					else if (id == 12774) {
						player.lock(3);
						player.addWalkSteps(player.getX(),
								player.getY() <= object.getY() ? 3118 : 3115,
								3, false);
						// stronghold of security
					} else if (id == 16154) // entrance
						player.useStairs(-1, new WorldTile(1859, 5243, 0), 0, 1);
					else if (id == 16148 || id == 16146) { // stairs entrance up
						player.useStairs(828, new WorldTile(3081, 3421, 0), 1,
								2, "You climb up the ladder to the surface.");
						return;
					} else if (id == 16640 && object.getX() == 2330
							&& object.getY() == 10353)
						player.useStairs(828, new WorldTile(2141, 3944, 0), 1,
								2);
					else if (id == 36306 && object.getX() == 2142
							&& object.getY() == 3944)
						player.useStairs(833, new WorldTile(2329, 10353, 2), 1,
								2);
					else if (id == 16150) { // portal
						if (!player.getShosRewards()[0]) {
							player.getPackets()
									.sendGameMessage(
											"You can't use this portal without looting the rewards on this floor first.");
							return;
						}
						player.useStairs(-1, new WorldTile(1914, 5222, 0), 0, 1);
					} else if (id == 16149) { // stairs down

						player.getDialogueManager()
								.startDialogue(
										"WarningD",
										DoomsayerManager.NORMAL_WARNING,
										4,
										"Are you sure you want to climb down?",
										"Yes, I know it may be dangerous",
										(Runnable) () -> player
												.useStairs(828, new WorldTile(
														2042, 5245, 0), 1, 2,
														"You climb down the ladder to the next level."));
					} else if (id == 16082) { // portal
						if (!player.getShosRewards()[1]) {
							player.getPackets()
									.sendGameMessage(
											"You can't use this portal without looting the rewards on this floor first.");
							return;
						}
						player.useStairs(-1, new WorldTile(2021, 5223, 0), 0, 1);
					} else if (id == 16080 || id == 16078) // stairs up
						player.useStairs(828, new WorldTile(1859, 5243, 0), 1,
								2,
								"You climb up the ladder to the level above.");
					else if (id == 16081) { // stairs down
						player.getDialogueManager()
								.startDialogue(
										"WarningD",
										DoomsayerManager.NORMAL_WARNING,
										4,
										"Are you sure you want to climb down?",
										"Yes, I know it may be dangerous",
										(Runnable) () -> player
												.useStairs(828, new WorldTile(
														2123, 5252, 0), 1, 2,
														"You climb down the ladder to the next level."));
					} else if (id == 16116) { // portal
						if (!player.getShosRewards()[2]) {
							player.getPackets()
									.sendGameMessage(
											"You can't use this portal without looting the rewards on this floor first.");
							return;
						}
						player.useStairs(-1, new WorldTile(2146, 5287, 0), 0, 1);
					} else if (id == 7143 || id == 7153)
						AbbysObsticals.clearRocks(player, object);
					else if (id == 7152 || id == 7144)
						AbbysObsticals
								.clearTendrills(player, object, new WorldTile(
										id == 7144 ? 3028 : 3051, 4824, 0));
					else if (id == 7150 || id == 7146)
						AbbysObsticals.clearEyes(player, object, new WorldTile(
								object.getX() == 3021 ? 3028 : 3050, 4839, 0));
					else if (id == 7147)
						AbbysObsticals.clearGap(player, object, new WorldTile(
								3030, 4843, 0), false);
					else if (id == 7148)
						AbbysObsticals.clearGap(player, object, new WorldTile(
								3040, 4845, 0), true);
					else if (id == 7149)
						AbbysObsticals.clearGap(player, object, new WorldTile(
								3048, 4842, 0), false);
					else if (id == 7151)
						AbbysObsticals.burnGout(player, object, new WorldTile(
								3053, 4831, 0));
					else if (id == 7145)
						AbbysObsticals.burnGout(player, object, new WorldTile(
								3024, 4834, 0));
					else if (id == 7137)
						Runecrafting.enterWaterAltar(player);
					else if (id == 7139)
						Runecrafting.enterAirAltar(player);
					else if (id == 7140)
						Runecrafting.enterMindAltar(player);
					else if (id == 7131)
						Runecrafting.enterBodyAltar(player);
					else if (id == 7130)
						Runecrafting.enterEarthAltar(player);
					else if (id == 7129)
						Runecrafting.enterFireAltar(player);
					else if (id == 7133)
						Runecrafting.enterNatureAltar(player);
					else if (id == 7132)
						Runecrafting.enterCosmicAltar(player);
					else if (id == 7141)
						Runecrafting.enterBloodAltar(player);
					else if (id == 7134)
						Runecrafting.enterChaosAltar(player);
					else if (id == 7138)
						player.getPackets().sendGameMessage(
								"A strange power blocks your exit..");
					else if (id == 26341) {
						player.useStairs(827, new WorldTile(2882, 5311, 0), 2,
								1, "You climb down the rope...");
						player.getControllerManager()
								.startController("GodWars");
					} else if (id == 7135) {
						boolean hasEquip = false;
						for (Item item : player.getInventory().getItems()
								.getItems()) {
							if (item == null)
								continue;
							if (Equipment.getItemSlot(item.getId()) != -1) {
								hasEquip = true;
								break;
							}
						}
						if (player.getEquipment().wearingArmour() || hasEquip) {
							player.getPackets()
									.sendGameMessage(
											"The monk notices that you tried to fool him. Deposit your armor near the deposite box to travel to Entrana.");
							return;
						}
						Runecrafting.enterLawAltar(player);
					} else if (id == 7136)
						Runecrafting.enterDeathAltar(player);
					else if (id == 2469)
						player.useStairs(-1, new WorldTile(3315, 3253, 0), 1, 2);
					else if (id == 2468) // leaving
											// earth
						player.useStairs(-1, new WorldTile(3308, 3476, 0), 1, 2);
					else if (id == 2470) // leaving
											// body
						player.useStairs(-1, new WorldTile(3056, 3443, 0), 1, 2);
					else if (id == 2466) // leaving
											// mind
						player.useStairs(-1, new WorldTile(2985, 3512, 0), 1, 2);
					else if (id == 2465) // leaving
											// air
						player.useStairs(-1, new WorldTile(3128, 3407, 0), 1, 2);
					else if (id == 2467) // leaving
											// water
						player.useStairs(-1, new WorldTile(3188, 3162, 0), 1, 2);
					else if (id == 2473) // leaving
											// nature
						player.useStairs(-1, new WorldTile(2872, 3020, 0), 1, 2);
					else if (id == 2475) // leaving
											// death
						player.useStairs(-1, new WorldTile(1865, 4639, 0), 1, 2);
					else if (id == 2477) // leaving
											// blood
						player.useStairs(-1, new WorldTile(3559, 9778, 0), 1, 2);
					else if (id == 2472 || id == 2459) // leaving
														// law
						player.useStairs(-1, new WorldTile(2464, 4818, 0), 1, 2);
					else if (id == 2474) {// leaving choas
						player.useStairs(-1, new WorldTile(3058, 3588, 0), 1, 2);
						player.getControllerManager().startController(
								"Wilderness");
					} else if (id == 2471) // leaving cosmic
						player.useStairs(-1, new WorldTile(2409, 4380, 0), 1, 2);
					else if (id == 16114 || id == 16112) // stairs
															// up
						player.useStairs(828, new WorldTile(2042, 5245, 0), 1,
								2,
								"You climb up the ladder to the level above.");
					else if (id == 16115) {// stairs down
						player.getDialogueManager()
								.startDialogue(
										"WarningD",
										DoomsayerManager.NORMAL_WARNING,
										4,
										"Are you sure you want to climb down?",
										"Yes, I know it may be dangerous",
										(Runnable) () -> player
												.useStairs(828, new WorldTile(
														2358, 5215, 0), 1, 2,
														"You climb down the ladder to the next level."));
					} else if (id == 16050) { // portal
						if (!player.getShosRewards()[3]) {
							player.getPackets()
									.sendGameMessage(
											"You can't use this portal without looting the rewards on this floor first.");
							return;
						}
						player.useStairs(-1, new WorldTile(2341, 5219, 0), 0, 1);
					} else if (id == 16050 || id == 16048) { // stairs up
						player.useStairs(828, new WorldTile(3081, 3421, 0), 1,
								2,
								"You climb up the ladder, which seems to twist wind in all directions.");
						return;
					} else if (id == 16123 || id == 16124 || id == 16065
							|| id == 16066 || id == 16089 || id == 16090
							|| id == 16043 || id == 16044) { // stronghold
																// doors
						player.lock(3);
						player.setNextAnimation(new Animation(4282));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								WorldTile tile;
								switch (object.getRotation()) {
								case 0:
									tile = new WorldTile(
											object.getX() == player.getX() ? object
													.getX() - 1 : object.getX(),
											player.getY(), 0);
									break;
								case 1:
									tile = new WorldTile(player.getX(), object
											.getY() == player.getY() ? object
											.getY() + 1 : object.getY(), 0);
									break;
								case 2:
									tile = new WorldTile(
											object.getX() == player.getX() ? object
													.getX() + 1 : object.getX(),
											player.getY(), 0);
									break;
								case 3:
								default:
									tile = new WorldTile(player.getX(), object
											.getY() == player.getY() ? object
											.getY() - 1 : object.getY(), 0);
									break;
								}
								player.setNextWorldTile(tile);
								player.setNextAnimation(new Animation(4283));
								player.faceObject(object);
							}
						}, 0);
					} else if (id == 16135) {
						if (player.getShosRewards()[0]) {
							player.getPackets()
									.sendGameMessage(
											"You have already claimed your reward from this level.");
							return;
						}
						player.getDialogueManager().startDialogue(
								"StrongHoldOfSecurityRewards", 0);
					} else if (id == 16077) {
						if (player.getShosRewards()[1]) {
							player.getPackets()
									.sendGameMessage(
											"You have already claimed your reward from this level.");
							return;
						}
						player.getDialogueManager().startDialogue(
								"StrongHoldOfSecurityRewards", 1);
					} else if (id == 16118) {
						if (player.getShosRewards()[2]) {
							player.getPackets()
									.sendGameMessage(
											"You have already claimed your reward from this level.");
							return;
						}
						player.getDialogueManager().startDialogue(
								"StrongHoldOfSecurityRewards", 2);
					} else if (id == 16047)
						player.getDialogueManager().startDialogue(
								"StrongHoldOfSecurityRewards", 3);
					else if (id == 16152)
						player.getDialogueManager().startDialogue(
								"StrongholdSecurityDeadBody");
					else if (id == 2811 || id == 2812) {
						player.useStairs(id == 2812 ? 827 : -1,
								id == 2812 ? new WorldTile(2506, 3039, 0)
										: new WorldTile(2574, 3029, 0), 1, 2);
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.getDialogueManager().startDialogue(
										"SimplePlayerMessage",
										"Wow! That tunnel went a long way.");
							}
						});
					} else if (id == 2890 || id == 2893) {
						if (player.getEquipment().getWeaponId() != 975
								&& !player.getInventory().containsItemToolBelt(
										975, 1)) {
							player.getPackets()
									.sendGameMessage(
											"You need a machete in order to cutt through the terrain.");
							return;
						}
						player.setNextAnimation(new Animation(910));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								if (Utils.random(3) == 0) {
									player.getPackets()
											.sendGameMessage(
													"You fail to slash through the terrain.");
									return;
								}
								WorldObject o = new WorldObject(object);
								o.setId(id + 1);
								World.spawnObjectTemporary(o, 5000);
							}
						});
					} else if (id == 2231)
						player.useStairs(-1, new WorldTile(x == 2792 ? 2795
								: 2791, 2979, 0), 1, 2,
								x == 2792 ? "You climb down the slope."
										: "You climb up the slope.");
					else if (id == 23157)
						player.useStairs(-1, new WorldTile(2729, 3734, 0), 1, 2);
					else if (id == 492 && x == 2856 && y == 3168) // karamja
						// underground and
						// crandor
						player.useStairs(827, new WorldTile(2856, 9570, 0), 1,
								2);
					else if (id == 1764 && x == 2856 && y == 9569)
						player.useStairs(828, new WorldTile(2855, 3169, 0), 1,
								2);
					else if (id == 2606 && x == 2836 && y == 9600) {
						if (World.isSpawnedObject(object))
							return;
						player.lock(1);
						WorldObject opened = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX(), object.getY(), object.getPlane());
						World.spawnObjectTemporary(opened, 1200);
						player.addWalkSteps(2836, player.getY() == y ? y - 1
								: y, 1, false);
					} else if (id == 25154)
						player.useStairs(827, new WorldTile(2834, 9657, 0), 1,
								2);
					else if (id == 25213 && x == 2833 && y == 9657)
						player.useStairs(828, new WorldTile(2834, 3258, 0), 1,
								2);
					else if (id == 68134) // thzaar
											// entrance
						player.useStairs(-1, new WorldTile(4667, 5059, 0), 0, 1);
					else if (id == 68135)
						player.useStairs(-1, new WorldTile(2845, 3170, 0), 0, 1);
					// alkarid east area,(abbey + desert wyverms)
					else if (id == 75882) {
						player.lock(3);
						player.addWalkSteps(
								player.getX() > object.getX() ? 3332 : 3335,
								player.getY(), 3, false);
						// elemental workshop
					} else if (id == 26114 || id == 26115) {
						if (World.isSpawnedObject(object))
							return;
						if (!player.getQuestManager().completedQuest(
								Quests.ELEMENTAL_WORKSHOP_I)) {
							player.getPackets()
									.sendGameMessage(
											"Please come back after you have Elemental Workshop I quest requiriments.");
							return;
						}
						player.lock(1);
						World.spawnObjectTemporary(
								new WorldObject(object.getId(), object
										.getType(), object.getRotation() - 1,
										object.getX(), object.getY(), object
												.getPlane()), 1200);
						player.addWalkSteps(object.getX(),
								player.getY() == y ? y + 1 : y, 1, false);
					} else if (id == 3415 && x == 2710 && y == 3497)
						player.useStairs(-1, new WorldTile(2716, 9888, 0), 0, 1);
					else if (id == 3416 && x == 2714 && y == 9887)
						player.useStairs(-1, new WorldTile(2709, 3498, 0), 0, 1);
					else if (id == 34395 && object.getX() == 2796
							&& object.getY() == 3614)
						player.useStairs(-1, new WorldTile(2808, 10003, 0), 0,
								1);
					else if (id == 77053 && object.getX() == 2809
							&& object.getY() == 10001)
						player.useStairs(-1, new WorldTile(2795, 3616, 0), 0, 1);
					else if (id == 21585)
						player.getPackets()
								.sendGameMessage(
										"The doors seem to be frozen shut. Perhaps there is another way to enter.");
					else if (id == 21584) {
						player.getPackets()
								.sendGameMessage(
										"The doors seem to be barricaded by a pile of rocks. You decide to enter anyways.");
						player.useStairs(-1, new WorldTile(2394, 10299, 1), 1,
								2);
					} else if (id == 21598)
						player.useStairs(-1, new WorldTile(2402, 3887, 0), 1, 2);
					else if (id == 3413)
						ShopsHandler.openShop(player, 80);
					// canafis shortcutbehind bar
					else if (id == 5055 && x == 3495 && y == 3465)
						player.useStairs(827, new WorldTile(3477, 9845, 0), 1,
								2);
					else if (id == 5054 && x == 3477 && y == 9846)
						player.useStairs(828, new WorldTile(3494, 3465, 0), 1,
								2);
					if (id == 5052) {
						if (World.isSpawnedObject(object))
							return;
						if (!player.getQuestManager().completedQuest(
								Quests.IN_SEARCH_OF_THE_MYREQUE)) {
							player.getPackets()
									.sendGameMessage(
											"Please come back after you have Elemental Workshop I quest requiriments.");
							return;
						}
						WorldObject opened = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX(), object.getY(), object.getPlane());
						World.spawnObjectTemporary(opened, 1200);
						player.lock(1);
						player.addWalkSteps(object.getX(),
								player.getY() == y ? y - 1 : y, 1, false);
					} else if (id == 2259 || id == 2260)
						player.addWalkSteps(
								player.getX() >= 2875 ? 2874 : 2875,
								player.getY(), -1, false);
					else if (id == 2216)
						player.useStairs(-1, new WorldTile(
								player.getX() >= 2877 ? 2876 : 2880, 2953, 0),
								1, 2);
					else if (id == 30261 || id == 30262)
						player.useStairs(-1, new WorldTile(3491, 3411, 0), 0, 1);
					// mortayna obstacles
					else if (id == 5005 && x == 3502 && y == 3431)
						player.useStairs(828, new WorldTile(3502, 3425, 0), 1,
								2);
					else if (id == 5005 && x == 3502 && y == 3426)
						player.useStairs(828, new WorldTile(3502, 3432, 0), 1,
								2);
					// agarnia ice dungeon
					else if (id == 9472 && x == 3008 && y == 3150)
						player.useStairs(-1, new WorldTile(3009, 9550, 0), 0, 1);
					else if (id == 32015 && x == 3008 && y == 9550)
						player.useStairs(-1, new WorldTile(3009, 3150, 0), 0, 1);
					else if (id == 33173)
						player.useStairs(-1, new WorldTile(3056, 9555, 0), 0, 1);
					else if (id == 33174)
						player.useStairs(-1, new WorldTile(3056, 9562, 0), 0, 1);
					// Gnome Stronghold Brimstail cave
					else if (id == 5083)
						player.getDialogueManager().startDialogue("SaniBoch",
								true);
					else if (id == 77421)
						player.useStairs(-1, new WorldTile(2745, 3152, 0), 0, 1);
					else if (id == 17222 || id == 17223)
						player.useStairs(-1, new WorldTile(2402, 3419, 0), 0, 1);
					else if (id == 17209)
						player.useStairs(-1, new WorldTile(2408, 9812, 0), 0, 1);
					// The Tale of the Muspah cave
					else if (id == 42793 && x == 2737 && y == 3729)
						player.useStairs(
								-1,
								new WorldTile(
										player.getQuestManager()
												.completedQuest(
														Quests.THE_TALE_OF_THE_MUSPAH) ? 3485
												: 3421, 5511, 0), 0, 1);
					else if (id == 42891)
						player.useStairs(-1, new WorldTile(2736, 3731, 0), 0, 1);
					else if (id == 52626 || id == 52627)
						player.addWalkSteps(player.getX(), 2937, -1, false);
					else if (id == 52628 || id == 52629)
						player.addWalkSteps(player.getX(), 2940, -1, false);
					else if (id == 52624 || id == 52625)
						player.addWalkSteps(3754, player.getY(), -1, false);
					else if (id == 52622 || id == 52623)
						player.addWalkSteps(3751, player.getY(), -1, false);
					else if (id == 4627)
						player.useStairs(-1, new WorldTile(2893, 3567, 0), 1, 2);
					else if (id == 66973)
						player.useStairs(-1, new WorldTile(2206, 4934, 1), 1, 2);
					else if (id == 4620)
						player.useStairs(-1, new WorldTile(2207, 4938, 0), 1, 2);
					else if (id == 4622 && x == 2212)
						player.useStairs(-1, new WorldTile(2212, 4944, 1), 1, 2);
					else if (id == 42794)
						player.useStairs(-1, new WorldTile(object.getX(),
								object.getY() + 7, 0), 0, 1);
					else if (id == 42795)
						player.useStairs(-1, new WorldTile(object.getX(),
								object.getY() - 6, 0), 0, 1);
					else if (id == 48188)
						player.useStairs(-1, new WorldTile(3435, 5646, 0), 0, 1);
					else if (id == 48189)
						player.useStairs(-1, new WorldTile(3509, 5515, 0), 0, 1);
					else if (id == 15767)
						player.getDialogueManager().startDialogue("CaveyDavey");
					// slayer tower
					else if (id == 9319) {
						if (!Agility.hasLevel(player,
								x == 3422 && y == 3550 ? 61 : 71))
							return;
						player.useStairs(828, new WorldTile(player.getX(),
								player.getY(), player.getPlane() + 1), 1, 2);
					} else if (id == 9320) {
						if (!Agility.hasLevel(player, 61))
							return;
						player.useStairs(827, new WorldTile(player.getX(),
								player.getY(), player.getPlane() - 1), 1, 2);
					} else if (object.getId() == 15791) {
						if (object.getX() == 3829)
							player.useStairs(-1, new WorldTile(3830, 9461, 0),
									1, 2);
						if (object.getX() == 3814)
							player.useStairs(-1, new WorldTile(3815, 9461, 0),
									1, 2);
						player.getControllerManager().startController(
								"UnderGroundDungeon", false, true);
					} else if (object.getId() == 78630) {
						player.getControllerManager().forceStop();
						player.useStairs(828, new WorldTile(3176, 3161, 0), 1,
								2);
					} else if (object.getId() == 6898) {
						player.setNextAnimation(new Animation(10578));
						player.useStairs(-1, object, 1, 2);
						player.useStairs(10579, new WorldTile(3221, 9618, 0),
								1, 2);
						player.getControllerManager().startController(
								"UnderGroundDungeon", false, true);
						player.getPackets().sendGameMessage(
								"You squeeze through the hole.");
						return;
					} else if (id == 36002) {
						player.getControllerManager().startController(
								"UnderGroundDungeon", true, false);
						player.useStairs(833, new WorldTile(3206, 9379, 0), 1,
								2);
					} else if (id == 4493 && x == 3434 && y == 3537)
						player.useStairs(-1, new WorldTile(3433, 3538, 1), 0, 1);
					else if (id == 4494 && x == 3434 && y == 3537)
						player.useStairs(-1, new WorldTile(3438, 3538, 0), 0, 1);
					else if (id == 4495 && x == 3413 && y == 3540)
						player.useStairs(-1, new WorldTile(3417, 3541, 2), 0, 1);
					else if (id == 4496 && x == 3415 && y == 3540)
						player.useStairs(-1, new WorldTile(3412, 3541, 1), 0, 1);
					// Paterdomus underground
					else if (id == 30572 && x == 3405 && y == 3507)
						player.useStairs(827, new WorldTile(3405, 9906, 0), 1,
								2);
					else if (id == 30575 && x == 3405 && y == 9907)
						player.useStairs(828, new WorldTile(3405, 3506, 0), 1,
								2);
					else if (id == 3443)
						player.useStairs(-1, new WorldTile(3423, 3484, 0), 0, 1);
					else if (id == 30574 && x == 3422 && y == 3484)
						player.useStairs(827, new WorldTile(3440, 9887, 0), 1,
								2);
					// artisian workshop stairs
					else if (id == 29392)
						player.useStairs(-1, new WorldTile(3061, 3335, 0), 0, 1);
					else if (id == 29385 || id == 29385)
						player.useStairs(-1, new WorldTile(3067, 9710, 0), 0, 1);
					else if (id == 29387)
						player.useStairs(-1, new WorldTile(3035, 9713, 0), 0, 1);
					else if (id == 29391)
						player.useStairs(-1, new WorldTile(3037, 3342, 0), 0, 1);
					else if (id == 29375) {
						final boolean isNorth = player.getY() > 9964;
						final WorldTile tile = new WorldTile(player.getX(),
								player.getY() + (isNorth ? -7 : 7), 0);
						player.setNextAnimation(new Animation(745));
						player.setNextForceMovement(new ForceMovement(player,
								1, tile, 5, isNorth ? ForceMovement.SOUTH
										: ForceMovement.NORTH));
						WorldTasksManager.schedule(new WorldTask() {
							int ticks = 0;

							@Override
							public void run() {
								ticks++;
								if (ticks > 1)
									player.setNextAnimation(new Animation(744));
								if (ticks == 5) {
									player.setNextWorldTile(tile);
									stop();
								}
							}
						}, 0, 0);
					} else if (id == 44339) {
						if (!Agility.hasLevel(player, 81))
							return;
						boolean isEast = player.getX() > 2772;
						final WorldTile tile = new WorldTile(isEast ? 2768
								: 2775, 10002, 0);
						WorldTasksManager.schedule(new WorldTask() {

							int ticks = -1;

							@Override
							public void run() {
								ticks++;
								if (ticks == 0)
									player.setNextFaceWorldTile(object);
								else if (ticks == 1) {
									player.setNextAnimation(new Animation(10738));
									player.setNextForceMovement(new ForceMovement(
											player, 0, tile, 5, Utils.getAngle(
													object.getX()
															- player.getX(),
													object.getY()
															- player.getY())));
								} else if (ticks == 3)
									player.setNextWorldTile(tile);
								else if (ticks == 4) {
									player.getPackets()
											.sendGameMessage(
													"Your feet skid as you land floor.");
									stop();
								}
							}
						}, 0, 0);
					} else if (id == 77052) {
						if (!Agility.hasLevel(player, 70))
							return;
						boolean isEast = player.getX() > 2734;
						final WorldTile tile = new WorldTile(isEast ? 2730
								: 2735, 10008, 0);
						WorldTasksManager.schedule(new WorldTask() {

							int ticks = -1;

							@Override
							public void run() {
								ticks++;
								if (ticks == 0)
									player.setNextFaceWorldTile(object);
								else if (ticks == 1)
									player.setNextAnimation(new Animation(17811));
								else if (ticks == 9)
									player.setNextWorldTile(tile);
								else if (ticks == 10) {
									stop();
								}
							}
						}, 0, 0);
					} else if (id == 9311 || id == 9312) {
						if (!Agility.hasLevel(player, 21))
							return;
						WorldTasksManager.schedule(new WorldTask() {

							int ticks = 0;

							@Override
							public void run() {
								boolean withinGE = id == 9312;
								WorldTile tile = withinGE ? new WorldTile(3139,
										3516, 0) : new WorldTile(3143, 3514, 0);
								player.lock();
								ticks++;
								if (ticks == 1) {
									player.setNextAnimation(new Animation(2589));
									player.setNextForceMovement(new ForceMovement(
											object, 1,
											withinGE ? ForceMovement.WEST
													: ForceMovement.EAST));
								} else if (ticks == 3) {
									player.setNextWorldTile(new WorldTile(3141,
											3515, 0));
									player.setNextAnimation(new Animation(2590));
								} else if (ticks == 5) {
									player.setNextAnimation(new Animation(2591));
									player.setNextWorldTile(tile);
								} else if (ticks == 6) {
									player.setNextWorldTile(new WorldTile(tile
											.getX() + (withinGE ? -1 : 1), tile
											.getY(), tile.getPlane()));
									player.unlock();
									stop();
								}
							}
						}, 0, 0);
					} else if (id == 14931 || id == 14929) {
						if (player.getY() >= 3659 && player.getY() <= 3662) {
							if (!player.getDonationManager().isDonator()) {
								player.getDialogueManager().startDialogue(
										"SimpleMessage",
										"You need to be a member to enter.");
								return;
							}
							int bonus = 0;
							player.addWalkSteps(2344, 3663, -1, false);
							if (player.getDonationManager().isDonator())
								bonus = 5;
							else if (player.getDonationManager()
									.isExtremeDonator())
								bonus = 5;
							else if (player.getDonationManager()
									.isLegendaryDonator())
								bonus = 10;
							else if (player.getDonationManager()
									.isSupremeDonator())
								bonus = 12;
							else if (player.getDonationManager()
									.isDivineDonator())
								bonus = 15;
							player.getDialogueManager().startDialogue(
									"SimpleMessage",
									"While training in this area, you will receive an XP boost by a total of "
											+ bonus + "%.");
						} else
							player.addWalkSteps(2344, 3662, -1, false);
					} else if (id == 14922) {
						if (!Agility.hasLevel(player, 58))
							return;
						if (object.getY() == 3651)
							player.setNextWorldTile(new WorldTile(2344, 3655, 0));
						else
							player.setNextWorldTile(new WorldTile(2344, 3650, 0));
					} else if (id == 2878 || id == 2879) {
						player.getDialogueManager()
								.startDialogue(
										"SimpleMessage",
										"You step into the pool of sparkling water. You feel the energy rush through your veins.");
						final boolean isLeaving = id == 2879;
						final WorldTile tile = isLeaving ? new WorldTile(2509,
								4687, 0) : new WorldTile(2542, 4720, 0);
						player.setNextForceMovement(new ForceMovement(player,
								1, tile, 2, isLeaving ? ForceMovement.SOUTH
										: ForceMovement.NORTH));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextAnimation(new Animation(13842));
								WorldTasksManager.schedule(new WorldTask() {

									@Override
									public void run() {
										player.setNextAnimation(new Animation(
												-1));
										player.setNextWorldTile(isLeaving ? new WorldTile(
												2542, 4718, 0) : new WorldTile(
												2509, 4689, 0));
									}
								}, 2);
							}
						});
					} else if (id == 24991) {
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.getControllerManager().startController(
										"PuroPuro");
							}
						}, 10);
						Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0,
								0, new WorldTile(2591, 4320, 0), 9, false,
								Magic.OBJECT_TELEPORT);
					} else if (id == 2873 || id == 2874 || id == 2875) {
						player.getPackets().sendGameMessage(
								"You kneel and begin to chant to "
										+ objectDef.name.replace("Statue of ",
												"") + "...");
						player.setNextAnimation(new Animation(645));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.getDialogueManager()
										.startDialogue(
												"SimpleMessage",
												"You feel a rush of energy charge through your veins. Suddenly a cape appears before you.");
								World.addGroundItem(
										new Item(id == 2873 ? 2412
												: id == 2874 ? 2414 : 2413),
										new WorldTile(object.getX(), object
												.getY() - 1, 0));
							}
						}, 3);
					} else if (id == 18050) {
						return;
					} else if (id == 77574 || id == 77573) {
						boolean back = id == 77573;
						player.lock(4);
						final WorldTile tile = back ? new WorldTile(2687, 9506,
								0) : new WorldTile(2682, 9506, 0);
						final boolean isRun = player.isRunning();
						player.setRun(false);
						player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setRun(isRun);
							}
						}, 4);
					} else if (id == 77377 || id == 77379 || id == 77375
							|| id == 77373 || id == 77371) {
						final HatchetDefinitions hatchet = Woodcutting
								.getHatchet(player, false);
						if (hatchet == null) {
							player.getPackets()
									.sendGameMessage(
											"You dont have the required level to use that axe or you don't have a hatchet.");
							return;
						}
						player.lock();
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextAnimation(new Animation(hatchet
										.getEmoteId()));
								if (Utils.random(13 - hatchet.getAxeTime()) <= 3) {
									stop();
									WorldObject o = new WorldObject(object);
									o.setId(object.getId() - 1);
									World.spawnObjectTemporary(o, 10000);
									player.addWalkSteps(x, y, 1, false);
									player.unlock();
								}
							}
						}, 1, 1);
					} else if (id == 49016 || id == 49014) {
						if (player.getSkills().getLevel(Skills.FISHING) < 60) {
							player.getPackets()
									.sendGameMessage(
											"You need a Fishing level of 60 in order to pass through this gate.");
							return;
						}
						player.addWalkSteps(x,
								player.getY() < 3387 ? y + 1 : y, 1, false);
					} else if (id == 77506 || id == 77507) {
						player.useStairs(-1, new WorldTile(player.getX(),
								player.getY() + (id == 77506 ? -9 : 9),
								id == 77506 ? 2 : 0), 1, 2);
					} else if (id == 77508 || id == 77509) {
						player.useStairs(-1, id == 77508 ? new WorldTile(2643,
								9595, 2) : new WorldTile(2649, 9591, 0), 1, 2);
					} else if (id >= 77570 && id <= 77572) {
						player.lock(1);
						player.setNextAnimation(new Animation(741));
						player.setNextForceMovement(new ForceMovement(player,
								0, object, 1, Utils.getAngle(object.getX()
										- player.getX(),
										object.getY() - player.getY())));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextWorldTile(object);
							}
						});
					} else if (id == 73681) {
						WorldTile dest = new WorldTile(
								player.getX() == 2595 ? 2598 : 2595, 3608, 0);
						player.setNextForceMovement(new ForceMovement(player,
								1, dest, 2, Utils.getAngle(
										dest.getX() - player.getX(),
										dest.getY() - player.getY())));
						player.useStairs(-1, dest, 1, 2);
						player.setNextAnimation(new Animation(769));
					} else if (id == 9738 || id == 9330) {
						boolean rightDoor = object.getId() == 9330;
						WorldObject o = new WorldObject(object);
						o.setRotation(rightDoor ? -1 : 1);
						World.spawnObjectTemporary(o, 1000);
						WorldObject o2 = new WorldObject(rightDoor ? 9738
								: 9330, object.getType(), object.getRotation(),
								2558, rightDoor ? 3299 : 3300, object
										.getPlane());
						o2.setRotation(rightDoor ? 1 : 3);
						World.spawnObjectTemporary(o2, 1000);
						player.addWalkSteps(player.getX()
								+ (player.getX() >= 2559 ? -3 : 3), y, -1,
								false);
					} else if (id == 2406)

						handleDoor(player, object);
					else if (id == 50552) {
						player.setNextForceMovement(new ForceMovement(object,
								1, ForceMovement.NORTH));
						player.useStairs(13760, new WorldTile(3454, 3725, 0),
								2, 3);
					} else if (id == 74867 && object.getX() == 2842
							&& object.getY() == 3424) {
						player.useStairs(833, new WorldTile(2842, 9823, 0), 1,
								2);
					} else if (id == 74866 && object.getX() == 2842
							&& object.getY() == 9824) {
						player.useStairs(828, new WorldTile(2842, 3423, 0), 1,
								2);
					} else if (id == 9294) {
						if (!Agility.hasLevel(player, 80))
							return;
						final boolean isRunning = player.getRun();
						final boolean isSouth = player.getY() > 9813;
						final WorldTile tile = isSouth ? new WorldTile(2878,
								9812, 0) : new WorldTile(2881, 9814, 0);
						player.setRun(true);
						player.addWalkSteps(isSouth ? 2881 : 2877,
								isSouth ? 9814 : 9812);
						WorldTasksManager.schedule(new WorldTask() {
							int ticks = 0;

							@Override
							public void run() {
								ticks++;
								if (ticks == 2)
									player.setNextFaceWorldTile(object);
								else if (ticks == 3) {
									player.setNextAnimation(new Animation(1995));
									player.setNextForceMovement(new ForceMovement(
											player, 0, tile, 4, Utils.getAngle(
													object.getX()
															- player.getX(),
													object.getY()
															- player.getY())));
								} else if (ticks == 4)
									player.setNextAnimation(new Animation(1603));
								else if (ticks == 7) {
									player.setNextWorldTile(tile);
									player.setRun(isRunning);
									stop();
								}
							}
						}, 0, 0);
					} else if (id == 2333 || id == 2334 || id == 2335) {
						if (!Agility.hasLevel(player, 74))
							return;
						player.lock(2);
						player.setNextAnimation(new Animation(741));
						player.setNextForceMovement(new ForceMovement(object,
								1, Utils.getMoveDirection(player.getX()
										- object.getX(),
										player.getY() - object.getY()) == 6 ? 0
										: 2));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextWorldTile(object);
							}
						});
					} else if (id == 70794) {
						player.useStairs(-1, new WorldTile(1340, 6488, 0), 1, 2);
					} else if (id == 70795) {
						if (!Agility.hasLevel(player, 50))
							return;
						player.getDialogueManager().startDialogue(
								"GrotwormLairD", true);
					} else if (id == 70812) {
						player.getDialogueManager().startDialogue(
								"GrotwormLairD", false);
					} else if (id == 70799) {
						player.useStairs(-1, new WorldTile(1178, 6355, 0), 1, 2);
					} else if (id == 70796) {
						player.useStairs(-1, new WorldTile(1090, 6360, 0), 1, 2);
					} else if (id == 70798) {
						player.useStairs(-1, new WorldTile(1340, 6380, 0), 1, 2);
					} else if (id == 70797) {
						player.useStairs(-1, new WorldTile(1090, 6497, 0), 1, 2);
					} else if (id == 70792) {
						player.useStairs(-1, new WorldTile(1206, 6371, 0), 1, 2);
					} else if (id == 70793) {
						player.useStairs(-1, new WorldTile(2989, 3237, 0), 1, 2);
					} else if (id == 2353
							&& (object.getX() == 3177 && object.getY() == 5730 && object
									.getPlane() == 0))
						player.useStairs(828, new WorldTile(3353, 3416, 0), 1,
								2);
					else if (id == 38279)
						player.getDialogueManager().startDialogue(
								"RunespanPortalD");
					else if (id == 31359) {
						player.useStairs(-1, new WorldTile(3360, 9352, 0), 1, 2);
						player.getControllerManager().startController(
								"UnderGroundDungeon", true, true);
					} else if (id == 8929)
						player.useStairs(-1, new WorldTile(2442, 10147, 0), 1,
								2);
					else if (id == 8966)
						player.useStairs(-1, new WorldTile(2523, 3739, 0), 1, 2);
					else if (id == 76219) {
						player.getPackets().sendGameMessage(
								"You search the shelves...");
						player.lock();
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.unlock();
								player.getPackets()
										.sendGameMessage(
												"...and among the strange paraphernalia, you find an empty beer glass.");
								player.getInventory().addItem(
										SqirkFruitSqueeze.BEER_GLASS, 1);
							}
						}, 2);
					} else if (id == 12163 || id == 12164 || id == 12165
							|| id == 12166)

					{
						if (player.getTemporaryAttributtes()
								.get("canoe_shaped") != null
								&& (boolean) player.getTemporaryAttributtes()
										.get("canoe_shaped"))
							Canoes.openTravelInterface(player, id - 12163);
						else if (player.getTemporaryAttributtes().get(
								"canoe_chopped") != null
								&& (boolean) player.getTemporaryAttributtes()
										.get("canoe_chopped"))
							Canoes.openSelectionInterface(player);
						else
							Canoes.chopCanoeTree(player, id - 12163);
					} else if (id == 76499) {
						player.lock(1);
						player.addWalkSteps(player.getX(),
								player.getY() == 3164 ? 3162 : 3164, 2, false);
					} else if (object.getId() == 39508
							|| object.getId() == 39509) {
						StealingCreationLobbyController.climOverStile(player,
								object, true);
					} else if (id == 2491 || id == 16684)
						player.getActionManager()
								.setAction(
										new EssenceMining(
												object,
												player.getSkills().getLevel(
														Skills.MINING) < 30 ? EssenceDefinitions.Rune_Essence
														: EssenceDefinitions.Pure_Essence));
					else if (id == 63093)
						player.useStairs(-1, new WorldTile(4620, 5458, 3), 0, 1);
					else if (id == 63094)
						player.useStairs(-1, new WorldTile(3410, 3329, 0), 0, 1);
					else if (id == 64294 || id == 64295) {
						if (!Agility.hasLevel(player, 73)) {
							player.getDialogueManager().startDialogue(
									"SimplePlayerMessage",
									"A fall would be a long way down.");
							return;
						}
						player.setNextAnimation(new Animation(6132));
						player.lock(3);
						final WorldTile toTile;
						if (id == 64295 && x == 4661 && y == 5476)
							toTile = new WorldTile(4658, 5476, 3);
						else if (id == 64295 && x == 4682 && y == 5476)
							toTile = new WorldTile(4685, 5476, 3);
						else if (id == 64294 && x == 4684 && y == 5476)
							toTile = new WorldTile(4681, 5476, 3);
						else
							toTile = new WorldTile(4662, 5476, 3);
						player.setNextForceMovement(new ForceMovement(player,
								0, toTile, 2,
								object.getRotation() == 2 ? ForceMovement.EAST
										: ForceMovement.WEST));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextWorldTile(toTile);
							}
						}, 1);
					} else if (GodStatues.handleObject(player, object, 1))
						return;
					else if (id == 2481)
						Runecrafting.craftEssence(player, 557, 9, 6.5, false,
								26, 2, 52, 3, 78, 4);
					else if (id == 2478)
						Runecrafting.craftEssence(player, 556, 1, 5, false, 11,
								2, 22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77, 8,
								88, 9, 99, 10);
					else if (id == 2479)
						Runecrafting
								.craftEssence(player, 558, 2, 5.5, false, 14,
										2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7,
										98, 8);
					else if (id == 2480)
						Runecrafting.craftEssence(player, 555, 5, 6, false, 19,
								2, 38, 3, 57, 4, 76, 5, 95, 6);
					else if (id == 2482)
						Runecrafting.craftEssence(player, 554, 14, 7, false,
								35, 2, 70, 3);
					else if (id == 2483)
						Runecrafting.craftEssence(player, 559, 20, 7.5, false,
								46, 2, 92, 3);
					else if (id == 2484)
						Runecrafting.craftEssence(player, 564, 27, 8, true, 59,
								2);
					else if (id == 2487)
						Runecrafting.craftEssence(player, 562, 35, 8.5, true,
								74, 2);
					else if (id == 17010)
						Runecrafting.craftEssence(player, 9075, 40, 8.7, true,
								82, 2);
					else if (id == 2486)
						Runecrafting.craftEssence(player, 561, 45, 9, true, 91,
								2);
					else if (id == 2485)
						Runecrafting.craftEssence(player, 563, 50, 9.5, true);
					else if (id == 2488)
						Runecrafting.craftEssence(player, 560, 65, 10, true);
					else if (id == 30624)
						Runecrafting.craftEssence(player, 565, 77, 10.5, true);
					else if (id == 2452) {
						int hatId = player.getEquipment().getHatId();
						if (hatId == Runecrafting.AIR_TIARA
								|| hatId == Runecrafting.OMNI_TIARA
								|| player.getInventory().containsItem(1438, 1))
							Runecrafting.enterAirAltar(player);
					} else if (id == 2455) {
						int hatId = player.getEquipment().getHatId();
						if (hatId == Runecrafting.EARTH_TIARA
								|| hatId == Runecrafting.OMNI_TIARA
								|| player.getInventory().containsItem(1440, 1))
							Runecrafting.enterEarthAltar(player);
					} else if (id == 2456) {
						int hatId = player.getEquipment().getHatId();
						if (hatId == Runecrafting.FIRE_TIARA
								|| hatId == Runecrafting.OMNI_TIARA
								|| player.getInventory().containsItem(1442, 1))
							Runecrafting.enterFireAltar(player);
					} else if (id == 2454) {
						int hatId = player.getEquipment().getHatId();
						if (hatId == Runecrafting.WATER_TIARA
								|| hatId == Runecrafting.OMNI_TIARA
								|| player.getInventory().containsItem(1444, 1))
							Runecrafting.enterWaterAltar(player);
					} else if (id == 2457) {
						int hatId = player.getEquipment().getHatId();
						if (hatId == Runecrafting.BODY_TIARA
								|| hatId == Runecrafting.OMNI_TIARA
								|| player.getInventory().containsItem(1446, 1))
							Runecrafting.enterBodyAltar(player);
					} else if (id == 2453) {
						int hatId = player.getEquipment().getHatId();
						if (hatId == Runecrafting.MIND_TIARA
								|| hatId == Runecrafting.OMNI_TIARA
								|| player.getInventory().containsItem(1448, 1))
							Runecrafting.enterMindAltar(player);
					} else if (id == 47120) { // zaros altar
												// recharge if needed
						if (player.getPrayer().getPoints() < player.getSkills()
								.getLevel(Skills.PRAYER) * 10) {
							player.lock(12);
							player.setNextAnimation(new Animation(12563));
							player.getPrayer().setPoints(
									(int) ((player.getSkills().getLevel(
											Skills.PRAYER) * 10) * 1.15), true);
							player.getPrayer().refreshPrayerPoints();
						}
						player.getDialogueManager().startDialogue("ZarosAltar");
					} else if (id == 19222)
						Falconry.beginFalconry(player);
					else if (object.getDefinitions().name.toLowerCase()
							.contains("statue")
							&& object.getDefinitions().containsOption("Pray")) {
						final int maxPrayer = player.getSkills().getLevel(
								Skills.PRAYER) * 10;
						if (player.getPrayer().getPoints() < maxPrayer) {
							player.lock(5);
							player.getPackets().sendGameMessage(
									"You pray to the god...", true);
							player.setNextAnimation(new Animation(645));
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									player.getPrayer().boost(maxPrayer);
									player.getPackets().sendGameMessage(
											"...and recharged your prayer.",
											true);
								}
							}, 2);
						} else
							player.getPackets().sendGameMessage(
									"You already have full prayer.");
						return;
					} else if (id == 5947) {
						player.useStairs(540, new WorldTile(3170, 9571, 0), 8,
								9);
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.getControllerManager().startController(
										"UnderGroundDungeon", false, true);
								player.setNextAnimation(new Animation(-1));
							}
						}, 8);
						return;
					} else if (object.getId() == 6658) {
						player.useStairs(-1, new WorldTile(3226, 9542, 0), 1, 2);
						player.getControllerManager().startController(
								"UnderGroundDungeon", false, true);
					} else if (object.getId() == 6673) {
						player.useStairs(-1, new WorldTile(
								player.getX() == 3239 ? player.getX() + 3
										: player.getX() - 3, player.getY(),
								player.getPlane()), 1, 2);
					} else if (id == 10137) {
						if (player.getX() == 2153 && player.getY() == 5108)
							player.useStairs(-1, new WorldTile(2150, 5109, 0),
									1, 2);
						else if (player.getX() == 2130 && player.getY() == 5096)
							player.useStairs(-1, new WorldTile(2129, 5093, 0),
									1, 2);
						else
							player.useStairs(-1, new WorldTile(
									player.getX() + 3, player.getY() - 1, 0),
									1, 2);
					} else if (id == 10136) {
						if (player.getX() == 2150 && player.getY() == 5109)
							player.useStairs(-1, new WorldTile(2153, 5108, 1),
									1, 2);
						else if (player.getX() == 2129 && player.getY() == 5093)
							player.useStairs(-1, new WorldTile(2130, 5096, 1),
									1, 2);
						else
							player.useStairs(-1, new WorldTile(
									player.getX() - 3, player.getY() + 1, 1),
									1, 2);
					} else if (id == 36786)
						player.getDialogueManager().startDialogue("Banker",
								4907);
					else if (id == 42377 || id == 42378)
						player.getDialogueManager().startDialogue("Banker",
								2759);
					else if (id == 42217 || id == 782 || id == 34752)
						player.getDialogueManager()
								.startDialogue("Banker", 553);
					else if (id == 57437 || id == 12309 || id == 2693)
						player.getDialogueManager().startDialogue("ChooseBank");
					else if (id == 9356)
						FightCaves.enterFightCaves(player);
					else if (id == 68107)
						FightKiln.enterFightKiln(player, false);
					else if (id == 68223)
						FightPits.enterLobby(player, false);
					else if (id == 9293) {
						if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
							player.getPackets()
									.sendGameMessage(
											"You need an agility level of 70 to use this obstacle.",
											true);
							return;
						}
						int x1 = player.getX() == 2886 ? 2892 : 2886;
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextAnimation(new Animation(10580));
							}
						}, 0);
						player.setNextForceMovement(new ForceMovement(
								new WorldTile(x1, 9799, 0), 3,
								player.getX() == 2886 ? 1 : 3));
						player.useStairs(-1, new WorldTile(x1, 9799, 0), 3, 4);
					} else if (id == 29370
							&& (object.getX() == 3150 || object.getX() == 3153)
							&& object.getY() == 9906) { // edgeville
														// dungeon
														// cut
						if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
							player.getPackets()
									.sendGameMessage(
											"You need an agility level of 53 to use this obstacle.");
							return;
						}
						final boolean running = player.getRun();
						player.setRunHidden(false);
						player.lock(8);
						player.addWalkSteps(x == 3150 ? 3155 : 3149, 9906, -1,
								false);
						player.getPackets().sendGameMessage(
								"You pulled yourself through the pipes.", true);
						WorldTasksManager.schedule(new WorldTask() {
							boolean secondloop;

							@Override
							public void run() {
								if (!secondloop) {
									secondloop = true;
									player.getAppearence().setRenderEmote(295);
								} else {
									player.getAppearence().setRenderEmote(-1);
									player.setRunHidden(running);
									player.getSkills().addXp(Skills.AGILITY, 7);
									stop();
								}
							}
						}, 0, 5);
					}
					// start forinthry dungeon
					else if (id == 18341 && object.getX() == 3036
							&& object.getY() == 10172)
						player.useStairs(-1, new WorldTile(3039, 3765, 0), 0, 1);
					else if (id == 20599 && object.getX() == 3038
							&& object.getY() == 3761)
						player.useStairs(-1, new WorldTile(3037, 10171, 0), 0,
								1);
					else if (id == 18342 && object.getX() == 3074
							&& object.getY() == 10057)
						player.useStairs(-1, new WorldTile(3071, 3649, 0), 0, 1);
					else if (id == 20600 && object.getX() == 3072
							&& object.getY() == 3648)
						player.useStairs(-1, new WorldTile(3077, 10058, 0), 0,
								1);
					// nomads requiem
					else if (id == 18425
							&& !player.getQuestManager().completedQuest(
									Quests.NOMADS_REQUIEM))
						NomadsRequiem.enterNomadsRequiem(player);
					else if (id == 42219) {
						player.useStairs(-1, new WorldTile(1886, 3178, 0), 0, 1);
						if (player.getQuestManager().getQuestStage(
								Quests.NOMADS_REQUIEM) == -2) // for
							// now,
							// on
							// future
							// talk
							// with
							// npc
							// +
							// quest
							// reqs
							player.getQuestManager().setQuestStageAndRefresh(
									Quests.NOMADS_REQUIEM, 0);
					} else if (id == 8689)
						player.getActionManager().setAction(
								new CowMilkingAction());
					else if (id == 42220)
						player.useStairs(-1, new WorldTile(3082, 3475, 0), 0, 1);
					else if (id == 67043)
						player.useStairs(-1, new WorldTile(2219, 4532, 0), 0, 1);
					// start falador mininig
					else if (id == 30942 && object.getX() == 3019
							&& object.getY() == 3450)
						player.useStairs(828, new WorldTile(3020, 9850, 0), 1,
								2);
					else if (id == 6226 && object.getX() == 3019
							&& object.getY() == 9850)
						player.useStairs(833, new WorldTile(3018, 3450, 0), 1,
								2);
					else if (id == 31002
							&& player.getQuestManager().completedQuest(
									Quests.PERIL_OF_ICE_MONTAINS))
						player.useStairs(833, new WorldTile(2998, 3452, 0), 1,
								2);
					else if (id == 31012
							&& player.getQuestManager().completedQuest(
									Quests.PERIL_OF_ICE_MONTAINS))
						player.useStairs(828, new WorldTile(2996, 9845, 0), 1,
								2);
					else if (id == 30943 && object.getX() == 3059
							&& object.getY() == 9776)
						player.useStairs(-1, new WorldTile(3061, 3376, 0), 0, 1);
					else if (id == 30944 && object.getX() == 3059
							&& object.getY() == 3376)
						player.useStairs(-1, new WorldTile(3058, 9776, 0), 0, 1);
					else if (id == 2112 && object.getX() == 3046
							&& object.getY() == 9756) {
						if (player.getSkills().getLevel(Skills.MINING) < 60) {
							player.getDialogueManager()
									.startDialogue(
											"SimpleNPCMessage",
											MiningGuildDwarf
													.getClosestDwarfID(player),
											"Sorry, but you need level 60 Mining to go in there.");
							return;
						}
						WorldObject openedDoor = new WorldObject(
								object.getId(), object.getType(), object
										.getRotation() - 1, object.getX(),
								object.getY() + 1, object.getPlane());
						if (World.removeObjectTemporary(object, 1200)) {
							World.spawnObjectTemporary(openedDoor, 1200);
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(
									3046,
									player.getY() > object.getY() ? object
											.getY() : object.getY() + 1, -1,
									false);
						}
					} else if (id == 2113) {
						if (player.getSkills().getLevel(Skills.MINING) < 60) {
							player.getDialogueManager()
									.startDialogue(
											"SimpleNPCMessage",
											MiningGuildDwarf
													.getClosestDwarfID(player),
											"Sorry, but you need level 60 Mining to go in there.");
							return;
						}
						player.useStairs(-1, new WorldTile(3021, 9739, 0), 0, 1);
					} else if (id == 6226 && object.getX() == 3019
							&& object.getY() == 9740)
						player.useStairs(828, new WorldTile(3019, 3341, 0), 1,
								2);
					else if (id == 6226 && object.getX() == 3019
							&& object.getY() == 9738)
						player.useStairs(828, new WorldTile(3019, 3337, 0), 1,
								2);
					else if (id == 6226 && object.getX() == 3018
							&& object.getY() == 9739)
						player.useStairs(828, new WorldTile(3017, 3339, 0), 1,
								2);
					else if (id == 6226 && object.getX() == 3020
							&& object.getY() == 9739)
						player.useStairs(828, new WorldTile(3021, 3339, 0), 1,
								2);
					else if (id == 30963)
						player.getDialogueManager().startDialogue("ChooseBank");
					else if (id == 6045)
						player.getPackets().sendGameMessage(
								"You search the cart but find nothing.");
					else if (id == 97841) {
						player.getEffectsManager().removeEffect(
								EffectType.POISON);
						player.getEffectsManager().removeEffect(
								EffectType.WEAKEN_EFFECT);
						player.heal(player.getMaxHitpoints()
								- player.getHitpoints());
						player.getPackets()
								.sendGameMessage(Color.GREEN,
										"You smell the mysterious flowers and they seem to heal you!");
						return;
					} else if (id == 5906) {
						if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
							player.getPackets()
									.sendGameMessage(
											"You need an agility level of 42 to use this obstacle.");
							return;
						}
						player.lock();
						WorldTasksManager.schedule(new WorldTask() {
							int count = 0;

							@Override
							public void run() {
								if (count == 0) {
									player.setNextAnimation(new Animation(2594));
									WorldTile tile = new WorldTile(object
											.getX()
											+ (object.getRotation() == 2 ? -2
													: +2), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(
											tile,
											4,
											Utils.getMoveDirection(tile.getX()
													- player.getX(),
													tile.getY() - player.getY())));
								} else if (count == 2) {
									WorldTile tile = new WorldTile(object
											.getX()
											+ (object.getRotation() == 2 ? -2
													: +2), object.getY(), 0);
									player.setNextWorldTile(tile);
								} else if (count == 5) {
									player.setNextAnimation(new Animation(2590));
									WorldTile tile = new WorldTile(object
											.getX()
											+ (object.getRotation() == 2 ? -5
													: +5), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(
											tile,
											4,
											Utils.getMoveDirection(tile.getX()
													- player.getX(),
													tile.getY() - player.getY())));
								} else if (count == 7) {
									WorldTile tile = new WorldTile(object
											.getX()
											+ (object.getRotation() == 2 ? -5
													: +5), object.getY(), 0);
									player.setNextWorldTile(tile);
								} else if (count == 10) {
									player.setNextAnimation(new Animation(2595));
									WorldTile tile = new WorldTile(object
											.getX()
											+ (object.getRotation() == 2 ? -6
													: +6), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(
											tile,
											4,
											Utils.getMoveDirection(tile.getX()
													- player.getX(),
													tile.getY() - player.getY())));
								} else if (count == 12) {
									WorldTile tile = new WorldTile(object
											.getX()
											+ (object.getRotation() == 2 ? -6
													: +6), object.getY(), 0);
									player.setNextWorldTile(tile);
								} else if (count == 14) {
									stop();
									player.unlock();
								}
								count++;
							}

						}, 0, 0);
					}
					if (HefinAgility.handleObject(player, object))
						return;
					// BarbarianOutpostAgility start
					if (id == 20210)
						BarbarianOutpostAgility.enterObstaclePipe(player,
								object);
					else if (id == 43526) // worked
											// in
											// 831
											// but
											// not
											// 876
											// weird
						BarbarianOutpostAgility
								.swingOnRopeSwing(player, object);
					else if (id == 43595 && x == 2550 && y == 3546)
						BarbarianOutpostAgility.walkAcrossLogBalance(player,
								object);
					else if (id == 20211 && x == 2538 && y == 3545)
						BarbarianOutpostAgility
								.climbObstacleNet(player, object);
					else if (id == 2302 && x == 2535 && y == 3547)
						BarbarianOutpostAgility.walkAcrossBalancingLedge(
								player, object);
					else if (id == 1948) {
						BarbarianOutpostAgility.climbOverCrumblingWall(player,
								object);
						if (player.getDailyTask() != null)
							player.getDailyTask().incrementTask(player, 2, id,
									Skills.AGILITY);
					} else if (id == 43533)
						BarbarianOutpostAgility.runUpWall(player, object);
					else if (id == 43597)
						BarbarianOutpostAgility.climbUpWall(player, object);
					else if (id == 43587)
						BarbarianOutpostAgility
								.fireSpringDevice(player, object);
					else if (id == 43527)
						BarbarianOutpostAgility
								.crossBalanceBeam(player, object);
					else if (id == 43531)
						BarbarianOutpostAgility.jumpOverGap(player, object);
					else if (id == 43532) {
						BarbarianOutpostAgility.slideDownRoof(player, object);
						if (player.getDailyTask() != null)
							player.getDailyTask().incrementTask(player, 2, id,
									Skills.AGILITY);
					}
					// sawmill
					else if (id == 46307 && x == 3311 && y == 3491)
						Sawmill.enter(player, object);
					else if (id == 83134) {
						player.getPackets()
								.sendGameMessage(
										"You break the icicle and it shatters to pieces.");
						World.removeObject(object);
					}
					// Wilderness course start
					else if (id == 64698)
						WildernessAgility.walkAcrossLogBalance(player, object);
					else if (id == 64699)
						WildernessAgility.jumpSteppingStones(player, object);
					else if (id == 65362 && x == 3004 && y == 3938)
						WildernessAgility.enterWildernessPipe(player,
								object.getX(), object.getY());
					else if (id == 65734) {
						WildernessAgility.climbUpWall(player, object);
						if (player.getDailyTask() != null)
							player.getDailyTask().incrementTask(player, 2, id,
									Skills.AGILITY);
					} else if (id == 64696)
						WildernessAgility.swingOnRopeSwing(player, object);
					else if (id == 65365)
						WildernessAgility.enterWildernessCourse(player);
					else if (id == 65367)
						WildernessAgility.exitWildernessCourse(player);
					else if (ApeAtollAgility.handleObject(player, object))
						return;
					else if (id == 45077) {
						player.getDialogueManager().startDialogue(
								"WarningD",
								DoomsayerManager.NORMAL_WARNING,
								33,
								null,
								null,
								(Runnable) () -> {
									player.lock();
									if (player.getX() != object.getX()
											|| player.getY() != object.getY())
										player.addWalkSteps(object.getX(),
												object.getY(), -1, false);
									WorldTasksManager.schedule(new WorldTask() {

										private int count;

										@Override
										public void run() {
											if (count == 0) {
												player.setNextFaceWorldTile(new WorldTile(
														object.getX() - 1,
														object.getY(), 0));
												player.setNextAnimation(new Animation(
														12216));
												player.unlock();
											} else if (count == 2) {
												player.setNextWorldTile(new WorldTile(
														3651, 5122, 0));
												player.setNextFaceWorldTile(new WorldTile(
														3651, 5121, 0));
												player.setNextAnimation(new Animation(
														12217));
											} else if (count == 3) {
												// TODO find emote
												// player.getPackets().sendObjectAnimation(new
												// WorldObject(45078, 0, 3,
												// 3651, 5123,
												// 0), new
												// Animation(12220));
											} else if (count == 5) {
												player.unlock();
												stop();
											}
											count++;
										}

									}, 1, 0);
								});
					} else if (id == 45076)
						player.getActionManager()
								.setAction(
										new Mining(object,
												RockDefinitions.LRC_GOLD_ORE));
					else if (id == 5999)
						player.getActionManager()
								.setAction(
										new Mining(object,
												RockDefinitions.LRC_COAL_ORE));
					else if (id == 94069)
						player.getActionManager().setAction(
								new Mining(object,
										RockDefinitions.CRYSTAL_SANDSTONE));
					else if (id == 45078)
						player.useStairs(2413, new WorldTile(3012, 9832, 0), 2,
								2);
					else if (id == 45079)
						player.getBank().openDepositBox();
					// champion guild
					else if (id == 24357 && object.getX() == 3188
							&& object.getY() == 3355)
						player.useStairs(-1, new WorldTile(3189, 3354, 1), 0, 1);
					else if (id == 24359 && object.getX() == 3188
							&& object.getY() == 3355)
						player.useStairs(-1, new WorldTile(3189, 3358, 0), 0, 1);
					else if (id == 1805 && object.getX() == 3191
							&& object.getY() == 3363) {
						if (World.isSpawnedObject(object))
							return;
						WorldObject openedDoor = new WorldObject(
								object.getId(), object.getType(), object
										.getRotation() - 1, object.getX(),
								object.getY(), object.getPlane());
						World.spawnObjectTemporary(openedDoor, 1200);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(3191, player.getY() >= object
								.getY() ? object.getY() - 1 : object.getY(),
								-1, false);
						if (player.getY() >= object.getY())
							player.getDialogueManager()
									.startDialogue(
											"SimpleNPCMessage",
											198,
											"Greetings bolt adventurer. Welcome to the guild of",
											"Champions.");
					}
					// lighthouse dungeon
					else if (id == 4383)
						player.useStairs(833, new WorldTile(2519, 9993, 1), 1,
								2);
					else if (id == 4412)
						player.useStairs(828, new WorldTile(2510, 3644, 0), 1,
								2);
					else if (id == 4546) {
						if (player.getY() <= 10002) {
							player.getPackets()
									.sendGameMessage(
											"This door cannot be opened from this side.");
							return;
						}
						player.lock(2);
						player.addWalkSteps(2513, 10002, 1, false);
					} else if (id == 4545) {
						if (player.getY() >= 10003) {
							player.getPackets()
									.sendGameMessage(
											"This door cannot be opened from this side.");
							return;
						}
						player.lock(2);
						player.addWalkSteps(2516, 10003, 1, false);
					} else if (id == 4544)
						player.getInterfaceManager().sendCentralInterface(142);
					else if (id == 29355 && object.getX() == 3209
							&& object.getY() == 9616)
						player.useStairs(828, new WorldTile(3210, 3216, 0), 1,
								2);
					else if (id == 36687 && object.getX() == 3209
							&& object.getY() == 3216)
						player.useStairs(833, new WorldTile(3208, 9616, 0), 1,
								2);
					else if (id == 4485)
						player.useStairs(828, new WorldTile(2515, 10007, 0), 1,
								2);
					else if (id == 4413)
						player.useStairs(828, new WorldTile(2515, 10005, 1), 1,
								2);
					// start of varrock dungeon
					else if (id == 29355 && object.getX() == 3230
							&& object.getY() == 9904) // varrock
						// dungeon
						// climb
						// to
						// bear
						player.useStairs(828, new WorldTile(3229, 3503, 0), 1,
								2);
					else if (id == 24264)
						player.useStairs(833, new WorldTile(3229, 9904, 0), 1,
								2);
					else if (id == 24366)
						player.useStairs(828, new WorldTile(3237, 3459, 0), 1,
								2);
					else if (id == 882 && object.getX() == 3237
							&& object.getY() == 3458)
						player.useStairs(833, new WorldTile(3237, 9858, 0), 1,
								2);
					else if (id == 29355 && object.getX() == 3097
							&& object.getY() == 9868) // edge
						// dungeon
						// climb
						player.useStairs(828, new WorldTile(3096, 3468, 0), 1,
								2);
					else if (id == 26934 && x == 3097 && y == 3468)
						player.useStairs(833, new WorldTile(3096, 9868, 0), 1,
								2);
					else if (id == 74864)
						player.useStairs(-1, new WorldTile(2885, 3395, 0), 1, 2);
					else if (id == 66991)
						player.useStairs(-1, new WorldTile(2885, 9795, 0), 1, 2);
					else if (id == 29355 && object.getX() == 3088
							&& object.getY() == 9971)
						player.useStairs(828, new WorldTile(3087, 3571, 0), 1,
								2);
					else if (id == 65453)
						player.useStairs(833, new WorldTile(3089, 9971, 0), 1,
								2);
					else if (id == 12389 && object.getX() == 3116
							&& object.getY() == 3452)
						player.useStairs(833, new WorldTile(3117, 9852, 0), 1,
								2);
					else if (id == 29355 && object.getX() == 3116
							&& object.getY() == 9852)
						player.useStairs(833, new WorldTile(3115, 3452, 0), 1,
								2);
					else if (id == 2348) {
						if (x == 3092 && y == 3281)
							player.useStairs(-1, new WorldTile(3091, 3281, 0),
									1, 2);
						else if (x == 3098 && y == 3281)
							player.useStairs(-1, new WorldTile(3100, 3281, 0),
									1, 2);
						else if (x == 3100 && y == 3266)
							player.useStairs(-1, new WorldTile(3099, 3266, 0),
									1, 2);
						else if (x == 3084 && y == 3262)
							player.useStairs(-1, new WorldTile(3086, 3262, 0),
									1, 2);
						else if (x == 3091 && y == 3251)
							player.useStairs(-1, new WorldTile(3090, 3251, 0),
									1, 2);
						else if (x == 3100 && y == 3255)
							player.useStairs(-1, new WorldTile(3099, 3255, 0),
									1, 2);
					} else if (id == 2347) {
						if (x == 3092 && y == 3281)
							player.useStairs(-1, new WorldTile(3094, 3281, 1),
									1, 2);
						else if (x == 3098 && y == 3281)
							player.useStairs(-1, new WorldTile(3097, 3281, 1),
									1, 2);
						else if (x == 3100 && y == 3266)
							player.useStairs(-1, new WorldTile(3102, 3266, 1),
									1, 2);
						else if (x == 3084 && y == 3262)
							player.useStairs(-1, new WorldTile(3083, 3262, 1),
									1, 2);
						else if (x == 3091 && y == 3251)
							player.useStairs(-1, new WorldTile(3093, 3251, 1),
									1, 2);
						else if (x == 3100 && y == 3255)
							player.useStairs(-1, new WorldTile(3102, 3255, 1),
									1, 2);
					} else if (id == 2296 && y == 3477) {
						if (!Agility.hasLevel(player, 20))
							return;
						final boolean running = player.getRun();
						player.setRunHidden(false);
						player.lock(5);
						player.addWalkSteps(
								object.getX()
										+ (object.getRotation() == 3 ? 4 : -4),
								object.getY(), -1, false);
						player.getPackets()
								.sendGameMessage(
										"You walk carefully across the slippery log...",
										true);
						WorldTasksManager.schedule(new WorldTask() {
							boolean secondloop;

							@Override
							public void run() {
								if (!secondloop) {
									secondloop = true;
									player.getAppearence().setRenderEmote(155);
								} else {
									player.getAppearence().setRenderEmote(-1);
									player.setRunHidden(running);
									player.getSkills().addXp(Skills.AGILITY,
											7.5);
									player.getPackets()
											.sendGameMessage(
													"... and make it safely to the other side.",
													true);
									stop();
								}
							}
						}, 0, 4);
					} else if (id == 69526)
						GnomeAgility.walkGnomeLog(player);
					else if (id == 69383)
						GnomeAgility.climbGnomeObstacleNet(player);
					else if (id == 69508)
						GnomeAgility.climbUpGnomeTreeBranch(player);
					else if (id == 2312)
						GnomeAgility.walkGnomeRope(player);
					else if (id == 4059)
						GnomeAgility.walkBackGnomeRope(player);
					else if (id == 69507)
						GnomeAgility.climbDownGnomeTreeBranch(player);
					else if (id == 69384)
						GnomeAgility.climbGnomeObstacleNet2(player, object);
					else if (id == 69377 || id == 69378) {
						GnomeAgility.enterGnomePipe(player, object.getX(),
								object.getY());
						if (player.getDailyTask() != null)
							player.getDailyTask().incrementTask(player, 2, id,
									Skills.AGILITY);
					} else if (id == 69389)
						GnomeAgility.jumpDown(player, object);
					else if (id == 69506)
						GnomeAgility.climbUpTree(player);
					else if (Wilderness.isDitch(id)) {// wild ditch

						player.getDialogueManager()
								.startDialogue(
										"WarningD",
										DoomsayerManager.WILDERNESS_DITCH_WARNING,
										7,
										null,
										null,
										(Runnable) () -> {
											player.stopAll();
											player.lock(4);
											player.setNextAnimation(new Animation(
													6132));
											final WorldTile toTile = new WorldTile(
													object.getRotation() == 3
															|| object
																	.getRotation() == 1 ? object
															.getX() - 1
															: player.getX(),
													object.getRotation() == 0
															|| object
																	.getRotation() == 2 ? object
															.getY() + 2
															: player.getY(),
													object.getPlane());
											player.setNextForceMovement(new ForceMovement(
													new WorldTile(player),
													1,
													toTile,
													2,
													object.getRotation() == 0
															|| object
																	.getRotation() == 2 ? ForceMovement.NORTH
															: ForceMovement.WEST));
											WorldTasksManager.schedule(
													new WorldTask() {
														@Override
														public void run() {
															player.setNextWorldTile(toTile);
															player.faceObject(object);
															player.getControllerManager()
																	.startController(
																			"Wilderness");
															player.resetReceivedDamage();
														}
													}, 2);
										});
					} else if (id == 42611) {// Magic Portal
						player.getDialogueManager()
								.startDialogue("MagicPortal");
					} else if (id == 27254) {// Edgeville portal
						player.getPackets().sendGameMessage(
								"You enter the portal...");
						player.useStairs(10584, new WorldTile(3087, 3488, 0),
								2, 3, "..and are transported to Edgeville.");
						player.addWalkSteps(1598, 4506, -1, false);
					} else if (id == 12202) {// mole entrance
						if (!player.getInventory().containsItemToolBelt(952)) {
							player.getPackets().sendGameMessage(
									"You need a spade to dig this.");
							return;
						}
						if (player.getX() != object.getX()
								|| player.getY() != object.getY()) {
							player.lock();
							player.addWalkSteps(object.getX(), object.getY());
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									InventoryOptionsHandler.dig(player);
								}

							}, 1);
						} else
							InventoryOptionsHandler.dig(player);
					} else if (id == 12230 && object.getX() == 1752
							&& object.getY() == 5136) // mole
						// exit
						player.setNextWorldTile(new WorldTile(2986, 3316, 0));
					else if (id == 66115 || id == 66116)
						InventoryOptionsHandler.dig(player);
					else if (id == 15522) {// portal sign
						if (player.withinDistance(new WorldTile(1598, 4504, 0),
								1)) {// PORTAL
							// 1
							player.getInterfaceManager().sendCentralInterface(
									327);
							player.getPackets().sendIComponentText(327, 13,
									"Edgeville");
							player.getPackets()
									.sendIComponentText(
											327,
											14,
											"This portal will take you to edgeville. There "
													+ "you can multi pk once past the wilderness ditch.");
						}
						if (player.withinDistance(new WorldTile(1598, 4508, 0),
								1)) {// PORTAL
							// 2
							player.getInterfaceManager().sendCentralInterface(
									327);
							player.getPackets().sendIComponentText(327, 13,
									"Mage Bank");
							player.getPackets()
									.sendIComponentText(
											327,
											14,
											"This portal will take you to the mage bank. "
													+ "The mage bank is a 1v1 deep wilderness area.");
						}
						if (player.withinDistance(new WorldTile(1598, 4513, 0),
								1)) {// PORTAL
							// 3
							player.getInterfaceManager().sendCentralInterface(
									327);
							player.getPackets().sendIComponentText(327, 13,
									"Magic's Portal");
							player.getPackets()
									.sendIComponentText(
											327,
											14,
											"This portal will allow you to teleport to areas that "
													+ "will allow you to change your magic spell book.");
						}
					} else if (id == 38811) {// corp beast
						player.getDialogueManager()
								.startDialogue(
										"WarningD",
										DoomsayerManager.NORMAL_WARNING,
										30,
										"The Beast is incredibly powerful and will easily kill all but the most skilled warriors.<br><br>Due to the beast's soul devouring powers<br><col=ff0000>YOU WILL NOT GET A GRAVESTONE ON DEATH</col><br><br>Are you sure that you wish to enter?",
										"Enter the cavern",
										(Runnable) () -> BossInstanceHandler
												.enterInstance(player,
														Boss.Corporeal_Beast));
					} else if (id == 21065) {// evil chicken
						if (player.getY() == 10418) {
							player.setNextWorldTile(new WorldTile(2643, 10417,
									0));
							player.getMusicsManager().reset();
							player.getControllerManager()
									.removeControllerWithoutCheck();
						} else {
							player.getDialogueManager()
									.startDialogue(
											"WarningD",
											DoomsayerManager.NORMAL_WARNING,
											30,
											"This Beast is one of the most powerful of it's kind, and could easily slay any men.<br><br>Are you sure that you wish to enter?",
											"Enter the room",
											(Runnable) () -> BossInstanceHandler
													.enterInstance(player,
															Boss.Evil_Chicken));
						}
					} else if (id == 26425) {
						player.getMusicsManager().reset();
						player.getControllerManager()
								.removeControllerWithoutCheck();
						player.setNextWorldTile(new WorldTile(2862, 5357, 0));
						player.getControllerManager()
								.startController("GodWars");
					} else if (id == 26426) {
						player.getMusicsManager().reset();
						player.getControllerManager()
								.removeControllerWithoutCheck();
						player.setNextWorldTile(new WorldTile(2835, 5294, 0));
						player.getControllerManager()
								.startController("GodWars");
					} else if (id == 26427) {
						player.getMusicsManager().reset();
						player.getControllerManager()
								.removeControllerWithoutCheck();
						player.setNextWorldTile(new WorldTile(2923, 5257, 0));
						player.getControllerManager()
								.startController("GodWars");
					} else if (id == 26428) {
						player.getMusicsManager().reset();
						player.getControllerManager()
								.removeControllerWithoutCheck();
						player.setNextWorldTile(new WorldTile(2925, 5333, 0));
						player.getControllerManager()
								.startController("GodWars");
					} else if (id == 21171 || id == 21169 || id == 21160
							|| id == 21662 || id == 21068 || id == 43459
							|| id == 43461) {
						player.getDialogueManager()
								.startDialogue("SimpleMessage",
										"This door leads to a dead end...<br>Find another way to the Evil Chicken.");
					} else if (id == 37929) {
						player.stopAll();
						player.setNextWorldTile(new WorldTile(
								player.getX() == 2921 ? 2917 : 2921, player
										.getY(), player.getPlane()));
					} else if (id == 37928 && object.getX() == 2883
							&& object.getY() == 4370) {
						player.stopAll();
						player.setNextWorldTile(new WorldTile(3214, 3782, 0));
						player.getControllerManager().startController(
								"Wilderness");
					} else if (id == 38815 && object.getX() == 3209
							&& object.getY() == 3780 && object.getPlane() == 0) {
						if (player.getSkills().getLevel(Skills.WOODCUTTING) < 37
								|| player.getSkills().getLevel(Skills.MINING) < 45
								|| player.getSkills()
										.getLevel(Skills.SUMMONING) < 23
								|| player.getSkills().getLevel(
										Skills.FIREMAKING) < 47
								|| player.getSkills().getLevel(Skills.PRAYER) < 55) {
							player.getPackets()
									.sendGameMessage(
											"You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
							return;
						}
						player.stopAll();
						player.setNextWorldTile(new WorldTile(2885, 4372, 2));
						player.getControllerManager().forceStop();
						// TODO all reqs, skills not added
					} else if (id == 82049) {
						if (player.isKalphiteLairSetted())
							player.useStairs(19507,
									new WorldTile(3420, 9510, 0), 4, 5, null,
									true);
						else if (player.getInventory().containsItem(954, 1)) {
							player.getInventory().deleteItem(954, 1);
							player.setKalphiteLair();
						} else
							player.getPackets().sendGameMessage(
									"You need a rope to climb down.");
					} else if (id == 48803) {
						if (player.isKalphiteLairEntranceSetted())
							BossInstanceHandler.enterInstance(player,
									Boss.Kalphite_Queen);
						else
							player.getPackets().sendGameMessage(
									"You need a rope to climb down.");
					} else if (id == 3829) {
						if (object.getX() == 3419 && object.getY() == 9510) {
							player.useStairs(828, new WorldTile(3228, 3105, 0),
									1, 2);
						}
					} else if (id == 82017)
						BossInstanceHandler.enterInstance(player,
								Boss.Exiled_Kalphite_Queen);
					else if (id == 54019 || id == 54020 || id == 55301)
						player.getPackets().sendGameMessage(
								"This feature is disabled due to rework.");
					else if (id == 1817) // kbd
											// lever
						Magic.pushLeverTeleport(player, new WorldTile(3048,
								3519, 0), 827, "You activate the artefact...",
								"and teleport out of the dragon's lair.");
					else if (id == 77834) {
						player.getDialogueManager()
								.startDialogue(
										"WarningD",
										DoomsayerManager.NORMAL_WARNING,
										36,
										"This artefact will transport you directly to the King Black Dragon's Lair. The King Black Dragon's Lair is extremely <col=ff0000>dangerous</col>. Are you sure you want to continue?<br><br>(A Combat level of 50+ is recommended)",
										null,
										(Runnable) () -> {
											player.setNextAnimation(new Animation(
													827));
											player.getPackets()
													.sendGameMessage(
															"You activate the artefact...",
															true);
											BossInstanceHandler.enterInstance(
													player,
													Boss.King_Black_Dragon);
										});
					} else if (id == 1816 && object.getX() == 3067
							&& object.getY() == 10252) { // kbd
															// out
															// lever
						Magic.pushLeverTeleport(player, new WorldTile(2273,
								4681, 0));
					} else if (id == 32015 && object.getX() == 3069
							&& object.getY() == 10256) { // kbd
															// stairs
						player.useStairs(828, new WorldTile(3017, 3848, 0), 1,
								2);
						player.getControllerManager().startController(
								"Wilderness");
					} else if (id == 1765 && object.getX() == 3017
							&& object.getY() == 3849) { // kbd
														// out
														// stairs
						player.stopAll();
						player.setNextWorldTile(new WorldTile(3069, 10255, 0));
						player.getControllerManager().forceStop();
					} else if (id == 102758) {
						if (Lander.canEnter(player, 0)) {
						}
					} else if (id == 102760) {
						if (Lander.canEnter(player, 1)) {
						}
					} else if (id == 102761) {
						if (Lander.canEnter(player, 2)) {
						}
					} else if (id == 5959) {
						Magic.pushLeverTeleport(player, new WorldTile(2539,
								4712, 0));
					} else if (id == 5960) {
						Magic.pushLeverTeleport(player, new WorldTile(3089,
								3957, 0));
					} else if (id == 1814) {
						Magic.pushLeverTeleport(player, new WorldTile(3155,
								3923, 0));
					} else if (id == 1815) {
						Magic.pushLeverTeleport(player, new WorldTile(2561,
								3311, 0));
					} else if (id == 62675)
						player.getCutscenesManager().play("DTPreview");
					else if (id == 2322 || id == 2323) {
						if (!Agility.hasLevel(player, 10))
							return;
						player.lock(4);
						player.setNextAnimation(new Animation(751));
						World.sendObjectAnimation(player, object,
								new Animation(497));
						final WorldTile toTile = new WorldTile(
								id == 2323 ? 2709 : 2704, y, object.getPlane());
						player.setNextForceMovement(new ForceMovement(player,
								1, toTile, 3, id == 2323 ? ForceMovement.EAST
										: ForceMovement.WEST));
						player.getSkills().addXp(Skills.AGILITY, 22);
						player.getPackets().sendGameMessage(
								"You skilfully swing across.", true);
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.setNextWorldTile(toTile);
							}

						}, 1);
					} else if (id == 62681)
						player.getDominionTower().viewScoreBoard();
					else if (id == 62678 || id == 62679)
						player.getDominionTower().openModes();
					else if (id == 62688)
						player.getDialogueManager().startDialogue(
								"DTClaimRewards");
					else if (id == 62677)
						player.getDominionTower().talkToFace();
					else if (id == 62680)
						player.getDominionTower().openBankChest();
					else if (id == 48797)
						player.useStairs(-1, new WorldTile(3877, 5526, 1), 0, 1);
					else if (id == 48798)
						player.useStairs(-1, new WorldTile(3246, 3198, 0), 0, 1);
					else if (id == 48678 && x == 3858 && y == 5533)
						player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
					else if (id == 48678 && x == 3858 && y == 5543)
						player.useStairs(-1, new WorldTile(3861, 5543, 0), 0, 1);
					else if (id == 48678 && x == 3858 && y == 5533)
						player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
					else if (id == 48677 && x == 3858 && y == 5543)
						player.useStairs(-1, new WorldTile(3856, 5543, 1), 0, 1);
					else if (id == 48677 && x == 3858 && y == 5533)
						player.useStairs(-1, new WorldTile(3856, 5533, 1), 0, 1);
					else if (id == 48679)
						player.useStairs(-1, new WorldTile(3875, 5527, 1), 0, 1);
					else if (id == 48688)
						player.useStairs(-1, new WorldTile(3972, 5565, 0), 0, 1);
					else if (id == 48683)
						player.useStairs(-1, new WorldTile(3868, 5524, 0), 0, 1);
					else if (id == 48682)
						player.useStairs(-1, new WorldTile(3869, 5524, 0), 0, 1);
					else if (id == 62676) { // dominion exit
						player.useStairs(-1, new WorldTile(3374, 3093, 0), 0, 1);
					} else if (id == 62674) { // dominion entrance
						player.useStairs(-1, new WorldTile(3744, 6405, 0), 0, 1);
					} else if (id == 11993 && x == 3107 && y == 3162) {
						player.addWalkSteps(
								player.getY() >= 3163 ? 3106 : 3107,
								player.getY() >= 3163 ? 3161 : 3163, -1, false);
					} else if (id == 3192) {
						player.getPackets().sendGameMessage(
								"This feature is disabled due to rework.");
					} else if (id == 65349) {
						player.useStairs(-1, new WorldTile(3044, 10325, 0), 0,
								1);
					} else if (id == 32048 && object.getX() == 3043
							&& object.getY() == 10328) {
						player.useStairs(-1, new WorldTile(3045, 3927, 0), 0, 1);
					} else if (id == 26194) {
						player.getDialogueManager().startDialogue(
								"PartyRoomLever");
					} else if (id == 61190 || id == 61191 || id == 61192
							|| id == 61193) {
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(
									new Woodcutting(object,
											TreeDefinitions.NORMAL));
					} else if (id == 20573)
						player.getControllerManager().startController(
								"RefugeOfFear");
					else if (id == 11739)
						player.useStairs(828, new WorldTile(3050, 3354, 2), 1,
								2);
					// crucible
					else if (id == 67050)
						player.useStairs(-1, new WorldTile(3359, 6110, 0), 0, 1);
					else if (id == 9662) {
						player.setNextAnimation(new Animation(832));
						player.getInventory().addItem(952, 1);
						player.lock(2);
						World.removeObjectTemporary(object, 60000);
					} else if (id == 67053)
						player.useStairs(-1, new WorldTile(3120, 3519, 0), 0, 1);
					else if (id == 20602) // gamers
											// groto
						player.useStairs(-1, new WorldTile(2954, 9675, 0), 0, 1);
					else if (id == 20604) // gamers
											// groto
						player.useStairs(-1, new WorldTile(3018, 3404, 0), 0, 1);
					else if (id == 4577) {
						player.lock(2);
						player.addWalkSteps(2509, player.getY() == 3635 ? 3636
								: 3635, 1, false);
					} else if (id == 67051)
						player.getDialogueManager()
								.startDialogue("Marv", false);
					else if (id == 67052)
						Crucible.enterCrucibleEntrance(player);
					else if (id == 16944)
						player.getFairyRings().handleObjects(object);
					else if (id == 6 || id == 29406 || id == 29408) {
						if (OwnedObjectManager.isPlayerObject(player, object))
							DwarfMultiCannon.fire(player);
						else
							player.getPackets().sendGameMessage(
									"This is not your cannon!");

					} else if (id == 29402) {
						player.cannonType = DwarfMultiCannon.CannonType.GOLD;
						DwarfMultiCannon.pickupCannon(player, 3, object);
					} else if (id == 29401) {
						player.cannonType = DwarfMultiCannon.CannonType.GOLD;
						DwarfMultiCannon.pickupCannon(player, 2, object);
					} else if (id == 29398) {
						player.cannonType = DwarfMultiCannon.CannonType.GOLD;
						DwarfMultiCannon.pickupCannon(player, 1, object);
					} else if (id == 29405) {
						player.cannonType = DwarfMultiCannon.CannonType.ROYALE;
						DwarfMultiCannon.pickupCannon(player, 3, object);
					} else if (id == 29404) {
						player.cannonType = DwarfMultiCannon.CannonType.ROYALE;
						DwarfMultiCannon.pickupCannon(player, 2, object);
					} else if (id == 29403) {
						player.cannonType = DwarfMultiCannon.CannonType.ROYALE;
						DwarfMultiCannon.pickupCannon(player, 1, object);
					} else if (id == 9) {
						player.cannonType = DwarfMultiCannon.CannonType.NORMAL;
						DwarfMultiCannon.pickupCannon(player, 3, object);
					} else if (id == 8) {
						player.cannonType = DwarfMultiCannon.CannonType.NORMAL;
						DwarfMultiCannon.pickupCannon(player, 2, object);
					} else if (id == 7) {
						player.cannonType = DwarfMultiCannon.CannonType.NORMAL;
						DwarfMultiCannon.pickupCannon(player, 1, object);
					} else if (id == 172) {
						if (player.getInventory().containsItem(989, 1))
							CrystalChest.openChest(player, object);
					} else if (id == 2330)
						player.getActionManager().setAction(
								new Mining(object,
										RockDefinitions.RED_SANDSTONE));
					else if (id == 26723)
						player.getDialogueManager()
								.startDialogue(
										"SpiritTreeD",
										(object.getId() == 68973 && object
												.getId() == 68974) ? 3637
												: 3636);
					else if (id == 38669)
						ShootingStars.openNoticeboard(player);
					else if (id == 25591)
						ShootingStars.openTelescope(player);
					else if (id == 104007)
						player.getActionManager()
								.setAction(
										new Woodcutting(object,
												TreeDefinitions.BAMBOO));
					else if (id >= 87508 && id <= 87530)
						player.getActionManager().setAction(
								new Woodcutting(object, TreeDefinitions.ELDER));
					else if (id == 88257)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.COPPER_ORE));
					else if (id == 88258)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.TIN_ORE));
					else if (id == 92119 || id == 2503)
						Magic.sendNormalTeleportSpell(player, 0, 0,
								Settings.HOME_LOCATION);
					else if (id >= 8958 && id <= 8960) {
						List<Integer> pIndex = World.getRegion(
								object.getRegionId()).getPlayerIndexes();
						if (pIndex != null) {
							for (Integer i : pIndex) {
								Player p = World.getPlayers().get(i);
								if (p == null
										|| p == player
										|| !Utils.isOnRange(p.getX(), p.getY(),
												p.getSize(), object.getX(),
												object.getY(), 3, 0))
									continue;
								player.lock(1);
								World.removeObjectTemporary(object, 60000);
								return;
							}
						}
						player.getPackets().sendGameMessage(
								"You cannot see a way to open this door...");
					} else if (id == 10177 && x == 2546 && y == 10143)
						player.getDialogueManager()
								.startDialogue("ClimbEmoteStairs",
										new WorldTile(2544, 3741, 0),
										new WorldTile(1798, 4407, 3),
										"Go up the stairs.",
										"Go down the stairs.", 828);
					else if ((id == 10193 && x == 1798 && y == 4406)
							|| (id == 8930 && x == 2542 && y == 3740))
						player.useStairs(-1, new WorldTile(2545, 10143, 0), 0,
								1);
					else if (id == 10195/* && x == 1808 && y == 4405 */)
						player.setNextWorldTile(new WorldTile(1810, 4405, 2));
					else if (id == 10196 && x == 1809 && y == 4405)
						player.useStairs(-1, new WorldTile(1807, 4405, 3), 0, 1);
					else if (id == 10198 && x == 1823 && y == 4404)
						player.useStairs(-1, new WorldTile(1825, 4404, 3), 0, 1);
					else if (id == 10197 && x == 1824 && y == 4404)
						player.useStairs(-1, new WorldTile(1823, 4404, 2), 0, 1);
					else if (id == 10199 && x == 1834 && y == 4389)
						player.useStairs(-1, new WorldTile(1834, 4388, 2), 0, 1);
					else if (id == 10200 && x == 1834 && y == 4388)
						player.useStairs(-1, new WorldTile(1834, 4390, 3), 0, 1);
					else if (id == 10201 && x == 1811 && y == 4394)
						player.useStairs(-1, new WorldTile(1810, 4394, 1), 0, 1);
					else if (id == 10202 && x == 1810 && y == 4394)
						player.useStairs(-1, new WorldTile(1812, 4394, 2), 0, 1);
					else if (id == 10203 && x == 1799 && y == 4388)
						player.useStairs(-1, new WorldTile(1799, 4386, 2), 0, 1);
					else if (id == 10204 && x == 1799 && y == 4387)
						player.useStairs(-1, new WorldTile(1799, 4389, 1), 0, 1);
					else if (id == 10205 && x == 1797 && y == 4382)
						player.useStairs(-1, new WorldTile(1797, 4382, 1), 0, 1);
					else if (id == 10206 && x == 1798 && y == 4382)
						player.useStairs(-1, new WorldTile(1796, 4382, 2), 0, 1);
					else if (id == 10207 && x == 1802 && y == 4369)
						player.useStairs(-1, new WorldTile(1800, 4369, 2), 0, 1);
					else if (id == 10208 && x == 1801 && y == 4369)
						player.useStairs(-1, new WorldTile(1802, 4369, 1), 0, 1);
					else if (id == 10209 && x == 1826 && y == 4362)
						player.useStairs(-1, new WorldTile(1828, 4362, 1), 0, 1);
					else if (id == 10210 && x == 1827 && y == 4362)
						player.useStairs(-1, new WorldTile(1825, 4362, 2), 0, 1);
					else if (id == 10211 && x == 1863 && y == 4371)
						player.useStairs(-1, new WorldTile(1863, 4373, 2), 0, 1);
					else if (id == 10212 && x == 1863 && y == 4372)
						player.useStairs(-1, new WorldTile(1863, 4370, 1), 0, 1);
					else if (id == 10213 && x == 1864 && y == 4388)
						player.useStairs(-1, new WorldTile(1864, 4389, 1), 0, 1);
					else if (id == 10214 && x == 1864 && y == 4389)
						player.useStairs(-1, new WorldTile(1864, 4387, 2), 0, 1);
					else if (id == 10215 && x == 1890 && y == 4407)
						player.useStairs(-1, new WorldTile(1890, 4408, 0), 0, 1);
					else if (id == 10216 && x == 1890 && y == 4408)
						player.useStairs(-1, new WorldTile(1890, 4406, 1), 0, 1);
					else if (id == 10230 && x == 1911 && y == 4367) {
						BossInstanceHandler.enterInstance(player,
								Boss.Dagannoth_Kings);
						// player.useStairs(-1, new WorldTile(2900, 4449, 0), 0,
						// 1);
					} else if (id == 10229 && x == 2899 && y == 4449)
						player.useStairs(-1, new WorldTile(1912, 4367, 0), 0, 1);
					else if (id == 10217 && x == 1957 && y == 4371)
						player.useStairs(-1, new WorldTile(1957, 4373, 1), 0, 1);
					else if (id == 10218 && x == 1957 && y == 4372)
						player.useStairs(-1, new WorldTile(1957, 4370, 0), 0, 1);
					else if (id == 10226 && x == 1932 && y == 4378)
						player.useStairs(-1, new WorldTile(1932, 4380, 2), 0, 1);
					else if (id == 10225 && x == 1932 && y == 4379)
						player.useStairs(-1, new WorldTile(1932, 4377, 1), 0, 1);
					else if (id == 10228 && x == 1961 && y == 4391)
						player.useStairs(-1, new WorldTile(1961, 4393, 3), 0, 1);
					else if (id == 10227 && x == 1961 && y == 4392)
						player.useStairs(-1, new WorldTile(1961, 4392, 2), 0, 1);
					else if (id == 10194 && x == 1975 && y == 4408)
						player.useStairs(-1, new WorldTile(2501, 3636, 0), 0, 1);
					else if (id == 10219 && x == 1824 && y == 4381)
						player.useStairs(-1, new WorldTile(1824, 4379, 3), 0, 1);
					else if (id == 10220 && x == 1824 && y == 4380)
						player.useStairs(-1, new WorldTile(1824, 4382, 2), 0, 1);
					else if (id == 10221 && x == 1838 && y == 4376)
						player.useStairs(-1, new WorldTile(1838, 4374, 2), 0, 1);
					else if (id == 10222 && x == 1838 && y == 4375)
						player.useStairs(-1, new WorldTile(1838, 4377, 3), 0, 1);
					else if (id == 10223 && x == 1850 && y == 4386)
						player.useStairs(-1, new WorldTile(1850, 4385, 1), 0, 1);
					else if (id == 10224 && x == 1850 && y == 4385)
						player.useStairs(-1, new WorldTile(1850, 4387, 2), 0, 1);
					else if (id == 94068) {
						if (objectDef.containsOption(0, "Mine"))
							player.getActionManager().setAction(
									new Mining(object,
											RockDefinitions.CRYSTAL_SANDSTONE));
					} else {
						switch (objectDef.name.toLowerCase()) {
						case "robust glass machine":
							player.getDialogueManager().startDialogue(
									"RobustGlassD", CreateGlass.CRYSTAL_GLASS);
							break;
						case "singing bowl":
							CreateCrystal crystal = CreateCrystal
									.getBar(player);
							player.getDialogueManager().startDialogue(
									"BowlSingingD", object, crystal);
							break;
						case "fire":
						case "firepit":
							SkillsDialogue.selectTool(player, -2);
							return;
						case "death's hourglass":
							WorldObject objectdoor = new WorldObject(92278, 10,
									3, 2275, 3303, 1);
							player.faceObject(objectdoor);
							if (player.getX() >= 400 && player.getX() <= 450) {
								Magic.sendObjectTeleportSpell(player, false,
										new WorldTile(3107, 3310, 0));
								FadingScreen.fade(player);
								FadingScreen.unfade(player, FadingScreen.fade(
										player, FadingScreen.TICK / 2),
										new Runnable() {
											@Override
											public void run() {
												WorldObject objectdoor = new WorldObject(
														96780, 10, 3, 3106,
														3310, 0);
												player.faceObject(objectdoor);
											}
										});
							} else {
								Magic.sendObjectTeleportSpell(player, false,
										new WorldTile(414, 652, 0));
								FadingScreen.fade(player);
								FadingScreen.unfade(player, FadingScreen.fade(
										player, FadingScreen.TICK / 2),
										new Runnable() {
											@Override
											public void run() {
												WorldObject objectdoor = new WorldObject(
														96782, 10, 3, 414, 651,
														0);
												player.faceObject(objectdoor);
											}
										});
							}
							break;
						case "fountain":
						case "well":
						case "sink":
						case "water barrel":
						case "water barrels":
							if (object.getId() != 87997) {
								for (Fill fill : Fill.values()) {
									player.getDialogueManager().startDialogue(
											"WaterFillingD", fill);
									return;
								}
							}
							break;
						case "range":
						case "grill":
						case "cooking range":
						case "stove":
						case "portable range":
							/*
							 * if (object.getId() == 114) {
							 * player.getDialogueManager().startDialogue(
							 * "LumbridgeCook", 278, false); } else {
							 */
							Cookables cook = Cooking.getCook(player);
							if (cook != null) {
								if (cook.isFireOnly()
										&& !object.getDefinitions().name
												.equals("Fire")) {
									player.getDialogueManager()
											.startDialogue("SimpleMessage",
													"You may only cook this on a fire.");
									return;
								} else if (cook.isSpitRoast()
										&& object.getId() != 11363) {
									player.getDialogueManager()
											.startDialogue("SimpleMessage",
													"You may only cook this on an iron spit.");
									return;
								} else if (player.getSkills().getLevel(
										Skills.COOKING) < cook.getLvl()) {
									player.getDialogueManager().startDialogue(
											"SimpleMessage",
											"You need a cooking level of "
													+ cook.getLvl()
													+ " to cook this food.");
									return;
								}
								player.getDialogueManager().startDialogue(
										"CookingD", cook, object);
								return;
							}
							player.getDialogueManager()
									.startDialogue(
											"SimpleMessage",
											"You can't cook that on a "
													+ (objectDef.name
															.equals("Fire") ? "fire"
															: "range") + ".");
							break;
						case "trapdoor":
						case "manhole":
							if (objectDef.containsOption(0, "Open")) {
								WorldObject openedHole = new WorldObject(object
										.getId() + 1, object.getType(), object
										.getRotation(), object.getX(), object
										.getY(), object.getPlane());
								World.spawnObjectTemporary(openedHole, 60000);
							}
							break;
						case "closed chest":
						case "chest":
						case "drawers":
						case "drawer":
							if (objectDef.containsOption(0, "Open")
									&& (ObjectDefinitions
											.getObjectDefinitions(object
													.getId() + 1).name
											.toLowerCase().contains("open"))) {
								player.setNextAnimation(new Animation(536));
								player.lock(2);
								WorldObject openedChest = new WorldObject(
										object.getId() + 1, object.getType(),
										object.getRotation(), object.getX(),
										object.getY(), object.getPlane());
								World.spawnObjectTemporary(openedChest, 60000);
							}
							break;
						case "open chest":
							if (objectDef.containsOption(0, "Search"))
								player.getPackets()
										.sendGameMessage(
												"You search the chest but find nothing.");
							break;
						case "spirit tree":
							player.getDialogueManager()
									.startDialogue(
											"SpiritTreeD",
											(object.getId() == 68973 && object
													.getId() == 68974) ? 3637
													: 3636);
							break;
						case "fairy ring":
						case "enchanted land":
							player.getFairyRings().handleObjects(object);
							break;
						case "spiderweb":
							if (object.getRotation() == 2) {
								player.lock(2);
								if (Utils.random(2) == 0) {
									player.addWalkSteps(player.getX(), player
											.getY() < y ? object.getY() + 2
											: object.getY() - 1, -1, false);
									player.getPackets().sendGameMessage(
											"You squeeze though the web.");
								} else
									player.getPackets()
											.sendGameMessage(
													"You fail to squeeze though the web; perhaps you should try again.");
							}
							break;
						case "web":
							if (objectDef.containsOption(0, "Slash")) {
								player.setNextAnimation(new Animation(
										PlayerCombat.getAttackAnimation(player,
												true)));
								slashWeb(player, object);
							}
							break;
						case "stile":
						case "stiles":
							handleCrossOver(player, object, 3, 839);
							return;
						case "anvil":
							if (objectDef.containsOption(0, "Smith")) {
								ForgingBar bar = ForgingBar.getBar(player);
								if (bar != null) {
									player.getDialogueManager().startDialogue(
											"SmithingD", object, bar);
								} else {
									player.getPackets()
											.sendGameMessage(
													"You do not have a bar in your inventory.");
								}
							}
							break;
						case "portable forge":
							SmeltingBar bar = SmeltingBar
									.getBar(ItemIdentifiers.BRONZE_BAR);
							if (bar != null) {
								player.getDialogueManager().startDialogue(
										"SmeltingD", object, bar);
							} else {
								player.getPackets()
										.sendGameMessage(
												"You do not have a bar in your inventory.");
							}
							break;
						case "potter's wheel":
							player.getDialogueManager().startDialogue(
									"PotteryWheelD", CreatePot.POT_UNFIRED);
							break;
						case "pottery oven":
							player.getDialogueManager().startDialogue(
									"PotteryOvenD", FinishPot.EMPTY_POT);
							break;
						case "crashed star":
							if (objectDef.containsOption(0, "Mine"))
								ShootingStars.mine(player, object);
							break;
						case "rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getPackets().sendGameMessage(
										"That rock is currently unavailable.");
							break;
						case "tin rock":
						case "tin ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.TIN_ORE));
							break;
						case "gold rock":
						case "gold ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.GOLD_ORE));
							break;
						case "iron rock":
						case "iron ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.IRON_ORE));
							break;
						case "silver rock":
						case "silver ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.SILVER_ORE));
							break;
						case "coal rock":
						case "coal rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.COAL_ORE));
							break;
						case "clay rock":
						case "clay rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.CLAY_ORE));
							break;
						case "soft clay":
						case "soft clay rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.SOFT_CLAY));
							break;
						case "copper rock":
						case "copper ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.COPPER_ORE));
							break;
						case "blurite rock":
						case "blurite ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.BLURITE_ORE));
							break;
						case "adamantite rock":
						case "adamantite ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.ADAMANT_ORE));
							break;
						case "runite rock":
						case "runite ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.RUNITE_ORE));
							break;
						case "gem rock":
						case "gem rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.GEM_ROCK));
							break;
						case "granite rock":
						case "granite rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.GRANITE_ORE));
							break;
						case "sandstone rock":
						case "sandstone rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.SANDSTONE_ORE));
							break;
						case "mithril rock":
						case "mithril ore rocks":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.MITHRIL_ORE));
							break;
						case "divine runite rock":
							player.getActionManager()
									.setAction(
											new DivineMining(
													object,
													DivineMining.RockDefinitions.DIVINE_RUNE_ORE));
							break;
						case "divine adamantite rock":
							player.getActionManager()
									.setAction(
											new DivineMining(
													object,
													DivineMining.RockDefinitions.DIVINE_ADAMANTITE_ORE));
							break;
						case "divine mithril rock":
							player.getActionManager()
									.setAction(
											new DivineMining(
													object,
													DivineMining.RockDefinitions.DIVINE_MITHRIL_ORE));
							break;
						case "divine coal rock":
							player.getActionManager()
									.setAction(
											new DivineMining(
													object,
													DivineMining.RockDefinitions.DIVINE_COAL_ORE));
							break;
						case "divine iron rock":
							player.getActionManager()
									.setAction(
											new DivineMining(
													object,
													DivineMining.RockDefinitions.DIVINE_IRON_ORE));
							break;
						case "divine bronze rock":
							player.getActionManager()
									.setAction(
											new DivineMining(
													object,
													DivineMining.RockDefinitions.DIVINE_BRONZE_ORE));
							break;
						case "seren stone":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new Mining(object,
												RockDefinitions.SEREN_STONE));
							break;
						case "deposit box":
						case "bank deposit box":
						case "portable bank deposit box":
							if (objectDef.containsOption(0, "Deposit"))
								player.getBank().openDepositBox();
							break;
						case "bank":
						case "bank chest":
						case "bank booth":
						case "counter":
							if (objectDef.containsOption(0, "Bank")
									|| objectDef.containsOption(0, "Use"))
								player.getDialogueManager().startDialogue(
										"ChooseBank");
							break;
						// Woodcutting start
						case "dramen tree":
							if (objectDef.containsOption(0, "Chop down")) {
								if (!player.isKilledLostCityTree()) {
									if (player.getTemporaryAttributtes().get(
											"HAS_SPIRIT_TREE") != null)
										return;
									new TreeSpirit(player, new WorldTile(2859,
											9734, 0));
									return;
								}
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.DRAMEN));
							}
							break;
						case "crust":
							if (objectDef.containsOption(0, "Mine"))
								player.getActionManager().setAction(
										new LavaFlowMine(object));
						case "tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.NORMAL));
							break;
						case "evergreen":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.EVERGREEN));
							break;
						case "dead tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.DEAD));
							break;
						case "oak":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.OAK));
							break;
						case "teak":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.TEAK));
							break;
						case "mahogany":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.MAHOGANY));
							break;
						case "willow":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.WILLOW));
							break;
						case "maple tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.MAPLE));
							break;
						case "ivy":
							if (objectDef.containsOption(0, "Chop"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.IVY));
							break;
						case "yew":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.YEW));
							break;
						case "magic tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.MAGIC));
							break;
						case "cursed magic tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.CURSED_MAGIC));
							break;
						case "divine magic tree":
							if (objectDef.containsOption(0, "Chop"))
								player.getActionManager()
										.setAction(
												new DivineWoodcutting(
														object,
														DivineTreeDefinitions.DIVINE_MAGIC));
							break;
						case "divine tree":
							if (objectDef.containsOption(0, "Chop"))
								player.getActionManager()
										.setAction(
												new DivineWoodcutting(
														object,
														DivineTreeDefinitions.DIVINE_NORMAL));
							break;
						case "divine oak tree":
							if (objectDef.containsOption(0, "Chop"))
								player.getActionManager()
										.setAction(
												new DivineWoodcutting(
														object,
														DivineTreeDefinitions.DIVINE_OAK));
							break;
						case "divine willow tree":
							if (objectDef.containsOption(0, "Chop"))
								player.getActionManager()
										.setAction(
												new DivineWoodcutting(
														object,
														DivineTreeDefinitions.DIVINE_WILLOW));
							break;
						case "divine maple tree":
							if (objectDef.containsOption(0, "Chop"))
								player.getActionManager()
										.setAction(
												new DivineWoodcutting(
														object,
														DivineTreeDefinitions.DIVINE_MAPLE));
							break;
						case "divine yew tree":
							if (objectDef.containsOption(0, "Chop"))
								player.getActionManager()
										.setAction(
												new DivineWoodcutting(
														object,
														DivineTreeDefinitions.DIVINE_YEW));
							break;
						case "sand pit":
						case "sand box":
							player.getActionManager().setAction(
									new SandBucketFillAction());
							break;
						// Woodcutting end
						case "gate":
						case "city gate":
						case "large door":
						case "metal door":
						case "church door":
						case "long hall door":
							if (objectDef.containsOption(0, "Open"))
								if (!handleGate(player, object))
									handleDoor(player, object);
							break;
						case "door":
							if ((objectDef.containsOption(0, "Open") || objectDef
									.containsOption(0, "Unlock")))
								handleDoor(player, object);
							break;
						case "ladder":
							handleLadder(player, object, 1);
							break;
						case "stairs":
						case "staircase":
							handleStaircases(player, object, 1);
							break;
						case "small obelisk":
							if (objectDef.containsOption(0, "Renew-points"))
								renewSummoningPoints(player);
							break;
						case "obelisk":
							if (objectDef.containsOption(0, "Infuse-pouch")) {
								Pouch pouch = Pouch.getPouch(player);

								Scroll scroll = Scroll.getScroll(player);
								if (pouch != null) {
									if (pouch.hasPouch(player)
											&& scroll != null)
										player.getDialogueManager()
												.startDialogue(
														"SpecialScrollsD",
														object, scroll);
									else {
										if (pouch != null)
											player.getDialogueManager()
													.startDialogue(
															"SummoningD",
															object, pouch);
									}
								} else {
									player.getPackets()
											.sendGameMessage(
													"You do not have proper ingredients with you.");
								}
							}
							break;
						case "bandos altar":
						case "armadyl altar":
						case "saradomin altar":
						case "zamorak altar":
							if (player.isUnderCombat()) {
								player.getPackets().sendGameMessage(
										"You can't use this while in combat.");
								return;
							}
							if (player.godWarsWatch == null)
								player.godWarsWatch = Stopwatch.createStarted();
							else {
								if (player.godWarsWatch
										.elapsed(TimeUnit.MINUTES) < 5) {
									int minutes = 5 - (int) player.godWarsWatch
											.elapsed(TimeUnit.MINUTES);
									player.getPackets()
											.sendGameMessage(
													"You must wait "
															+ minutes
															+ " more minute"
															+ (minutes == 1 ? ""
																	: "s")
															+ " until you can use the altar.");
									return;
								} else if (player.godWarsWatch
										.elapsed(TimeUnit.MINUTES) >= 5)
									player.getTemporaryAttributtes().put(
											"GodWarsAltar",
											Stopwatch.createStarted());
							}
							int maxPrayer = player.getSkills().getLevel(
									Skills.PRAYER) * 10;
							if (player.getPrayer().getPoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage(
										"You pray to the god...", true);
								player.setNextAnimation(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().boost(maxPrayer);
										player.getPackets()
												.sendGameMessage(
														"...and recharged your prayer.",
														true);
									}
								}, 2);
							} else
								player.getPackets().sendGameMessage(
										"You already have full prayer.");
							break;
						case "altar":
						case "gorilla statue":
						case "chaos altar":
							if (objectDef.containsOption(0, "Recharge")
									|| objectDef.containsOption(0, "Pray")
									|| objectDef.containsOption(0, "Pray-at")
									|| objectDef.containsOption(0, "Pray at")) {
								if (player.getPrayer().getPoints() < player
										.getSkills().getLevel(Skills.PRAYER) * 10) {
									player.lock(5);
									player.getPackets().sendGameMessage(
											"You pray to the god...", true);
									player.setNextAnimation(new Animation(645));
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											player.getPrayer()
													.boost(player
															.getSkills()
															.getLevel(
																	Skills.PRAYER) * 10);
											player.getPackets()
													.sendGameMessage(
															"...and recharged your prayer.",
															true);
										}
									}, 2);
								} else
									player.getPackets().sendGameMessage(
											"You already have full prayer.");
								if (id == 6552)
									player.getDialogueManager().startDialogue(
											"AncientAltar");
							}
							break;
						default:
							// player.getPackets().sendGameMessage("Nothing
							// interesting happens.");
							break;
						}
					}

				}));
	}

	private static boolean getRepeatedTele(Player player, int x1, int y1,
			int p1, int x2, int y2, int p2) {
		if (player.getX() == x1 && player.getY() == y1) {
			Magic.sendTeleportSpell(player, 17803, -1, 3447, -1, 1, 0.0,
					new WorldTile(x2, y2, p2), 2, false, Magic.OBJECT_TELEPORT);
			return true;
		} else if (player.getX() == x2 && player.getY() == y2) {
			Magic.sendTeleportSpell(player, 17803, -1, 3447, -1, 1, 0.0,
					new WorldTile(x1, y1, p1), 2, false, Magic.OBJECT_TELEPORT);
			return true;
		}
		return false;
	}

	private static void renewSummoningPoints(Player player) {
		int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
		if (player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
			player.lock(3);
			player.setNextAnimation(new Animation(8502));
			player.getSkills().set(Skills.SUMMONING, summonLevel);
			player.getPackets().sendGameMessage(
					"You have recharged your Summoning points.", true);
		} else
			player.getPackets().sendGameMessage(
					"You already have full Summoning points.");
	}

	private static void handleOption2(final Player player,
			final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		if (id == 45802) {
			if (player.getX() == 414 && player.getY() == 674) {
				player.getDialogueManager().startDialogue("ReaperDialogue", 2);
				player.faceObject(object);
				return;
			}
			player.addWalkSteps(414, 674);
			player.lock();
			do {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.getDialogueManager().startDialogue(
								"ReaperDialogue", 2);
						player.faceObject(object);
						player.unlock();
						this.stop();
						return;
					}
				}, 2, 1);
			} while (player.getX() == 414 && player.getY() == 674);
			return;
		}
		player.setRouteEvent(new RouteEvent(
				object,
				() -> {
					player.stopAll();
					player.faceObject(object);
					if (!player.getControllerManager().processObjectClick2(
							object))
						return;
					if (player.getTreasureTrailsManager().useObject(object))
						return;
					else if (player.getFarmingManager().isFarming(id, null, 2))
						return;
					else if (Pickables.handlePickable(player, object))
						return;
					else if (EvilTree.handleObject(player, object, 2))
						return;
					else if (id == 92256) {
						int bit = player.getVarsManager().getBitValue(25113);
						if (bit >= 1 && bit <= 20)
							player.getDialogueManager().startDialogue(
									"PvMPortalD");

					} else if (id == 92239) {
						int bit = player.getVarsManager().getBitValue(25054);
						if (bit >= 1 && bit <= 11)
							player.getDialogueManager().startDialogue(
									"SkillPortalD");
					}

					else if (id == 92240) {
						int bit = player.getVarsManager().getBitValue(25055);
						if (bit >= 1 && bit <= 11)
							player.getDialogueManager().startDialogue(
									"SkillPortalSecondD");
					} else if (id == 82483) {
						if (player.getX() >= 3425)
							player.setNextWorldTile(new WorldTile(3431, 3558, 2));
						else
							player.setNextWorldTile(new WorldTile(3415, 3558, 2));
					} else if (id == 87306)
						player.getActionManager()
								.setAction(
										new DivinationConvert(
												player,
												new Object[] { ConvertMode.CONVERT_TO_ENERGY }));
					else if ((id >= 15477 && id <= 15482) || id == 93284)
						EnterHouseD.enterHouse(player, false);
					else if (GodStatues.handleObject(player, object, 2))
						return;
					else if (id == 2114)
						player.getCoalTrucksManager().investigate();
					else if (id == 2564) {
						if (player.getPrayer().isAncientCurses()) {
							player.getDialogueManager().startDialogue(
									"SimpleMessage",
									"Your mind clears and you switch",
									"back to the normal prayer book.");
							player.getPrayer().setPrayerBook(false);
						} else {
							player.getDialogueManager().startDialogue(
									"SimpleMessage",
									"Your mind clears and you switch",
									"back to the curses prayer book.");
							player.getPrayer().setPrayerBook(true);
						}

					} else if (id == 17010)
						player.getDialogueManager().startDialogue("LunarAltar");
					else if (id == 87997) {
						player.setNextWorldTile(new WorldTile(2399, 6055, 1));
						player.setNextAnimation(new Animation(2924));
					} else if (id == 24821)
						ArtisanWorkshop.TakeBronzeIngots(player);
					else if (id == 24823)
						ArtisanWorkshop.TakeSteelIngots(player);
					else if (id == 24822)
						ArtisanWorkshop.TakeIronIngots(player);
					else if (id == 29394)
						ArtisanWorkshop.DepositIngots(player);
					else if (id == 29395)
						ArtisanWorkshop.DepositIngots(player);
					else if (id == 24821)
						ArtisanWorkshop.GiveBronzeIngots(player);
					else if (id == 24822)
						ArtisanWorkshop.GiveIronIngots(player);
					else if (id == 24823)
						ArtisanWorkshop.GiveSteelIngots(player);
					else if (id == 12774)
						player.getDialogueManager().startDialogue(
								"ShantyPassDangerSignD");
					else if (id == 79771 && player.getPlane() == 1)
						player.setNextWorldTile(new WorldTile(player.getX(),
								player.getY(), 2));
					else if (id == 79771 && player.getPlane() == 2)
						player.setNextWorldTile(new WorldTile(player.getX(),
								player.getY(), 3));
					else if (id == 91557) {
						player.setNextWorldTile(new WorldTile(4704, 6238, 1));
					} else if (id == 59463) {
						if (player.isConnectedClanChannel()) {
							player.getPackets().sendGameMessage(
									"Entering citadel...");
							player.setNextWorldTile(new WorldTile(5504, 4494, 0));
						} else {
							player.getPackets().sendGameMessage(
									"You aren't in a Clan Chat channel.");
						}
					} else if (id == 92278) {
						player.getCompCapeManager().sendInterface();
						return;
					} else if (id == 59462) {
						player.getPackets().sendGameMessage(
								"Leaving citadel...");
						player.setNextWorldTile(new WorldTile(2970, 3288, 0));
					} else if (id == 26807) {
						if (player.getRights() >= 2)
							player.getDialogueManager().startDialogue(
									"JModTable");
						else
							player.getPackets().sendGameMessage(
									"Only Administrators may use this.");
					} else if (id == 70812) {
						/*
						 * if (player.getSkills().getLevel(Skills.SUMMONING) <
						 * 60) { player.getPackets() .sendGameMessage(
						 * "You need a summoning level of 60 to go through this portal."
						 * ); return; }
						 */
						player.lock();
						player.getControllerManager().startController(
								"QueenBlackDragonController");
						player.setNextAnimation(new Animation(16752));
					} else if (id == 87989) {
						player.getDialogueManager().startDialogue("ChooseBank");
						return;

					} else if (id == 62677)
						player.getDominionTower().openRewards();
					else if (id == 2491)
						MiningBase
								.prospect(player,
										"This rock contains unbound Rune Stone essence.");

					else if (id == 62688)
						player.getDialogueManager().startDialogue(
								"SimpleMessage",
								"You have a Dominion Factor of "
										+ player.getDominionTower()
												.getDominionFactor() + ".");
					else if (id == 68107)
						FightKiln.enterFightKiln(player, true);
					if (id == 6165)
						player.getPackets().sendGameMessage(
								"There is nothing here worth stealing.");
					else if (id == 34384 || id == 34383 || id == 14011
							|| id == 7053 || id == 34387 || id == 34386
							|| id == 34385 || id == 104420 || id == 66696
							|| id == 66694 || id == 66692 || id == 17031
							|| id == 6162 || id == 6163 || id == 6164
							|| id == 6166)
						Thieving.handleStalls(player, object);
					else if (id == 2693 || id == 782)
						player.getGeManager().openCollectionBox();
					else if (id == 35390)
						GodWars.passGiantBoulder(player, object, false);
					else if (id == 26193)
						PartyRoom.openChest(player);
					else if (id == 26194)
						player.getDialogueManager().startDialogue(
								"PartyRoomLever");
					else if (id == 10177 && object.getX() == 2546
							&& object.getY() == 10143)
						player.useStairs(828, new WorldTile(2544, 3741, 0), 1,
								2);
					else if (id == 67051)
						player.getDialogueManager().startDialogue("Marv", true);
					else if (id == 17819) // vorago graveyard
						player.useStairs(-1, new WorldTile(3039, 6182, 0), 0, 1);
					else if (id == 38811) // corp beast
						player.getCutscenesManager()
								.play("CorporealBeastScene");
					else if (id == 39660 || id == 39661) {
						StealingCreationController.passWall(player, object,
								true);
					} else if (id == 92413) {
						player.getDialogueManager().startDialogue("ClanPortal",
								2);
					} else if (id == 12309) {
						ShopsHandler.openShop(player, 187);
					} else if (id == 6 || id == 29406 || id == 29408) {
						if (id == 6)
							player.cannonType = DwarfMultiCannon.CannonType.NORMAL;
						else if (id == 29406)
							player.cannonType = DwarfMultiCannon.CannonType.GOLD;
						else if (id == 29408)
							player.cannonType = DwarfMultiCannon.CannonType.ROYALE;

						DwarfMultiCannon.pickupCannon(player, 4, object);
					}

					switch (object.getId()) {
					case 2230:
						player.setNextWorldTile(new WorldTile(2833, 2955, 0));
						break;
					case 2265:
						player.setNextWorldTile(new WorldTile(2777, 3214, 0));
						break;
					case 9038:
					case 9039:
						if (player.getX() >= 2815 && player.getX() <= 2816)
							player.addWalkSteps(2817, player.getY(), -1, false);
						else if (player.getX() >= 2817 && player.getX() <= 2818)
							player.addWalkSteps(2816, player.getY(), -1, false);
						return;
					case 82938:
						player.getDialogueManager()
								.startDialogue("SimpleItemMessage", 29293,
										"You don't have any chronicles to offer at the monument.");
						break;
					}

					if (object.getDefinitions().name.startsWith("Laboratory ")) {
						String name = object.getDefinitions().name.replace(
								"Laboratory ", "");
						for (AscDoors door : AscDoors.values()) {
							if (door.name().equalsIgnoreCase(name)) {
								player.getDialogueManager().startDialogue(
										"AscensionDungeonD", door, name, true,
										true);
							}
						}
					}

					else {
						switch (objectDef.name.toLowerCase()) {
						case "range":
						case "cooking range":
						case "stove":
						case "fire":
						case "firepit":
						case "grill":
						case "portable range":
							SkillsDialogue.selectTool(player, -2);
							break;
						case "bandos altar":
						case "armadyl altar":
						case "saradomin altar":
						case "zamorak altar":
							player.getControllerManager()
									.removeControllerWithoutCheck();
							Magic.sendNormalTeleportSpell(player, 1, 0,
									Settings.HOME_LOCATION);
							break;
						case "104420":
						case "66696":
						case "66694":
						case "66692":
							Thieving.handleStalls(player, object);
							player.getPackets().sendGameMessage(
									"This is a test");
							break;
						case "crashed star":
							if (objectDef.containsOption(1, "Check-progress"))
								ShootingStars.prospect(player);
							break;
						case "portable forge":
							if (objectDef.containsOption(1, "Smith")) {
								ForgingBar bar = ForgingBar.getBar(player);
								if (bar != null) {
									player.getDialogueManager().startDialogue(
											"SmithingD", object, bar);
								} else {
									player.getPackets().sendGameMessage(
											"This bar has not been added yet.");
								}
							}
							break;
						case "small furnace":
						case "furnace":
						case "lava furnace":
							SmeltingBar bar = SmeltingBar.getBar(player);
							if (objectDef.containsOption(1, "Smelt")) {
								player.getDialogueManager().startDialogue(
										"SmeltingD", object, bar);
							}
							break;
						case "loom":
							player.getDialogueManager().startDialogue(
									"LoomingD", CreateCape.MILESTONE_CAPE_10);
							break;
						case "spinning wheel":
							if (player.getDungManager().isInside())
								player.getDialogueManager().startDialogue(
										"DungeoneeringSpinningD",
										DungSpin.SALVE_CLOTH);
							else
								player.getDialogueManager().startDialogue(
										"SpinningD", CreateSpin.BALL_OF_WOOL);
							break;
						case "bank":
						case "bank chest":
						case "bank booth":
						case "counter":
							if (objectDef.containsOption(1, "Bank")
									|| objectDef.containsOption(1, "Use"))
								player.getDialogueManager().startDialogue(
										"ChooseBank");
							break;
						case "bank deposit box":
							if (objectDef.containsOption(1, "Deposit-all")) {
								player.getBank().depositAllInventory(false);
								player.getBank().depositAllMoneyPouch(false);
								player.getBank().depositAllEquipment(false);
								player.getBank().depositAllBob(false);
								player.getPackets()
										.sendGameMessage(
												"You deposit all of your items into the deposit box");
							}
							break;
						case "spirit tree":
							SpiritTree.openInterface(player);
							break;
						case "barrier":
							if (object.getX() == player.getX()) {
								if (player.getY() > object.getY())
									player.addWalkSteps(player.getX(),
											(player.getY() - 2), -1, false);
								else
									player.addWalkSteps(player.getX(),
											(player.getY() + 2), -1, false);
							} else if (object.getY() == player.getY()) {
								if (player.getX() > object.getX())
									player.addWalkSteps((player.getX() - 2),
											player.getY(), -1, false);
								else
									player.addWalkSteps((player.getX() + 2),
											player.getY(), -1, false);
							}
							return;
						case "city gate":
						case "tent door":
						case "large door":
						case "arboretum door":
						case "church door":
						case "palace entrance":
						case "bamboo gate":
						case "legends guild door":
						case "long hall door":
						case "fence":
						case "ogre stone door":
						case "tent flap":
						case "gates":
						case "gate":
						case "metal door":
							if (objectDef.containsOption(1, "Open"))
								handleGate(player, object);
							break;
						case "door":
							if (objectDef.containsOption(1, "Open"))
								handleDoor(player, object);
							break;
						case "ladder":
							handleLadder(player, object, 2);
							break;
						case "stairs":
						case "staircase":
							handleStaircases(player, object, 2);
							break;
						case "obelisk":
						case "summoning obelisk":
							if (objectDef.containsOption(1, "Renew-points"))
								renewSummoningPoints(player);
							break;
						default:
							player.getPackets().sendGameMessage(
									"Nothing interesting happens.");
							break;
						}
					}
					if (Settings.DEBUG) {
						Logger.log("ObjectHandler",
								"clicked 2 at object id : " + id + ", "
										+ object.getX() + ", " + object.getY()
										+ ", " + object.getPlane());
						player.getPackets().sendGameMessage(
								"Click 2: " + object.getDefinitions().name
										+ " (id: " + object.getId() + ") "
										+ object.getX() + ", " + object.getY()
										+ ", " + object.getPlane() + ".", true);
					}
				}));
	}

	private static void handleOption3(final Player player,
			final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		if (id == 45802) {
			if (player.getX() == 414 && player.getY() == 674) {
				ReaperRewardsShop.openShop(player);
				player.faceObject(object);
				return;
			}
			player.addWalkSteps(414, 674);
			player.lock();
			do {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						ReaperRewardsShop.openShop(player);
						player.faceObject(object);
						player.unlock();
						this.stop();
						return;
					}
				}, 2, 1);
			} while (player.getX() == 414 && player.getY() == 674);
			return;
		}
		player.setRouteEvent(new RouteEvent(object, () -> {
			player.stopAll();
			player.faceObject(object);
			if (!player.getControllerManager().processObjectClick3(object))
				return;
			switch (object.getId()) {
			case 82483:
				if (player.getX() >= 3425)
					player.setNextWorldTile(new WorldTile(3431, 3558, 0));
				else
					player.setNextWorldTile(new WorldTile(3415, 3558, 0));
				break;
			case 2230:
				player.setNextWorldTile(new WorldTile(2833, 2955, 0));
				break;
			case 2265:
				player.setNextWorldTile(new WorldTile(2777, 3214, 0));
				break;
			}
			if (id == 45802) {
				if (player.getX() == 414 && player.getY() == 674) {
					player.faceObject(object);
					return;
				}
				player.addWalkSteps(414, 674);
				player.lock();
				do {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							ReaperRewardsShop.openShop(player);
							player.faceObject(object);
							player.unlock();
							this.stop();
							return;
						}
					}, 2, 1);
				} while (player.getX() == 414 && player.getY() == 674);
				return;
			}
			if (id == 87306)
				player.getActionManager().setAction(
						new DivinationConvert(player,
								new Object[] { ConvertMode.CONVERT_TO_XP }));
			else if (player.getFarmingManager().isFarming(id, null, 3))
				return;
			else if (id == 12309)
				ShopsHandler.openShop(player, 71);
			else if ((id >= 15477 && id <= 15482) || id == 93284)
				EnterHouseD.enterHouse(player, true);
			else if (EvilTree.handleObject(player, object, 3))
				return;
			else if (id == 10177 && object.getX() == 2546
					&& object.getY() == 10143)
				player.useStairs(828, new WorldTile(1798, 4407, 3), 1, 2);
			else if (id == 79771 && player.getPlane() == 2)
				player.setNextWorldTile(new WorldTile(player.getX(), player
						.getY(), 1));
			else if (id == 79771 && player.getPlane() == 1)
				player.setNextWorldTile(new WorldTile(player.getX(), player
						.getY(), 0));
			else if (id == 2564) {
				if (player.getCombatDefinitions().getSpellBook() == 0) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"Your mind clears and you switch",
							"back to the ancient spellbook.");
					player.getCombatDefinitions().setSpellBook(1);
				} else if (player.getCombatDefinitions().getSpellBook() == 1) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"Your mind clears and you switch",
							"back to the lunar spellbook.");
					player.getCombatDefinitions().setSpellBook(2);
				} else if (player.getCombatDefinitions().getSpellBook() == 2) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"Your mind clears and you switch",
							"back to the normal spellbook.");
					player.getCombatDefinitions().setSpellBook(0);
				}
			} else {
				switch (objectDef.name.toLowerCase()) {
				case "bank":
				case "bank chest":
				case "bank booth":
				case "counter":
					if (objectDef.containsOption(2, "Collect"))
						player.getGeManager().openCollectionBox();
					break;
				case "gate":
				case "metal door":
					if (objectDef.containsOption(2, "Open"))
						handleGate(player, object);
					break;
				case "door":
					if (objectDef.containsOption(2, "Open"))
						handleDoor(player, object);
					break;
				case "ladder":
					handleLadder(player, object, 3);
					break;
				case "stairs":
				case "staircase":
					handleStaircases(player, object, 3);
					break;
				default:
					player.getPackets().sendGameMessage(
							"Nothing interesting happens.");
					break;
				}
			}
			// if (Settings.DEBUG) {
				Logger.log("ObjectHandler", "clicked 3 at object id : " + id
						+ ", " + object.getX() + ", " + object.getY() + ", "
						+ object.getPlane() + ", ");
				player.getPackets().sendGameMessage(
						"Click 3: " + object.getDefinitions().name + " (id: "
								+ object.getId() + ") " + object.getX() + ", "
								+ object.getY() + ", " + object.getPlane()
								+ ".", true);
				// }
			}));
	}

	private static void handleOption4(final Player player,
			final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setRouteEvent(new RouteEvent(object,
				() -> {
					player.stopAll();
					player.faceObject(object);
					if (!player.getControllerManager().processObjectClick4(
							object))
						return;
					// living rock Caverns
				if (id == 87306)
					player.getActionManager()
							.setAction(
									new DivinationConvert(
											player,
											new Object[] { ConvertMode.CONVERT_TO_MORE_XP }));
				else if (id == 45076)
					MiningBase
							.prospect(player,
									"This rock contains a large concentration of gold.");
				else if (id == 5999)
					MiningBase
							.prospect(player,
									"This rock contains a large concentration of coal.");
				else if (((id >= 15477 && id <= 15482) || id == 93284))
					EnterHouseD.enterFriendsHouse(player);
				else if (id == 5259) {
					if (player.getY() >= 3505 && player.getY() <= 3507) {
						player.addWalkSteps(player.getX(), 3508, -1, false);
						player.getPackets().sendGameMessage(
								"You pass the barrier.", true);
					} else if (player.getY() >= 3508 && player.getY() <= 3511) {
						player.addWalkSteps(player.getX(), 3507, -1, false);
						player.getPackets().sendGameMessage(
								"You pass the barrier.", true);
					}
				} else {
					switch (objectDef.name.toLowerCase()) {
					default:
						player.getPackets().sendGameMessage(
								"Nothing interesting happens.");
						break;
					}
				}
				// if (Settings.DEBUG) {
				Logger.log("ObjectHandler", "cliked 4 at object id : " + id
						+ ", " + object.getX() + ", " + object.getY() + ", "
						+ object.getPlane() + ", ");
				player.getPackets().sendGameMessage(
						"Click 4: " + object.getDefinitions().name + " (id: "
								+ object.getId() + ") " + object.getX() + ", "
								+ object.getY() + ", " + object.getPlane()
								+ ".", true);
				// }
			}));
	}

	private static void handleOption5(final Player player,
			final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		if (object.getId() >= HouseConstants.HObject.WOOD_BENCH.getId()
				&& object.getId() <= HouseConstants.HObject.GILDED_BENCH
						.getId()) {
			player.setRouteEvent(new RouteEvent(object, () -> player
					.getControllerManager().processObjectClick5(object), true));
			return;
		}
		player.setRouteEvent(new RouteEvent(object, () -> {
			player.stopAll();
			player.faceObject(object);
			if (!player.getControllerManager().processObjectClick5(object))
				return;
			if (id == -1) {
			} else {
				if (objectDef.name.toLowerCase().contains("lodestone")) {
					String lodestone = objectDef.name.toLowerCase();
					lodestone.replace(" lodestone", "");
					player.getPackets().sendGameMessage(
							"This is the lodestone for the city of "
									+ Utils.fixChatMessage(lodestone) + ".");
				}
				switch (objectDef.name.toLowerCase()) {
				case "range":
				case "cooking range":
				case "stove":
				case "fire":
				case "firepit":
				case "grill":
				case "portable range":
					SkillsDialogue.selectTool(player, -2);
					break;
				case "magical wheat":
					PuroPuro.pushThrough(player, object);
					break;
				default:
					player.getPackets().sendGameMessage(
							"Nothing interesting happens.");
					break;
				}
			}
			// if (Settings.DEBUG) {
				Logger.log("ObjectHandler", "cliked 5 at object id : " + id
						+ ", " + object.getX() + ", " + object.getY() + ", "
						+ object.getPlane() + ", ");
				player.getPackets().sendGameMessage(
						"Click 5: " + object.getDefinitions().name + " (id: "
								+ object.getId() + ") " + object.getX() + ", "
								+ object.getY() + ", " + object.getPlane()
								+ ".", true);
				// }
			}));
	}

	private static void handleOptionExamine(final Player player,
			final WorldObject object) {
		player.getPackets().sendObjectMessage(0, 15263739, object,
				ObjectExamines.getExamine(object), true);
		player.getPackets().sendResetMinimapFlag();
		if (player.getRights() == 2 || player.getRights() == 4)
			player.getPackets().sendGameMessage(
					"object id : " + object.getId() + ", " + object.getX()
							+ ", " + object.getY() + ", " + object.getPlane()
							+ ", " + object.getType() + ", "
							+ object.getRotation() + ", "
							+ object.getDefinitions().name + ", config= "
							+ object.getDefinitions().configId
							+ ", configByFile= "
							+ object.getDefinitions().configFileId + "animId= "
							+ object.getDefinitions().objectAnimation);
		if (Settings.DEBUG) {
			Logger.log(
					"ObjectHandler",
					"examined object id : " + object.getId() + ", "
							+ object.getX() + ", " + object.getY() + ", "
							+ object.getPlane() + ", " + object.getType()
							+ ", " + object.getRotation() + ", "
							+ object.getDefinitions().name);
			player.getPackets().sendGameMessage(
					"Examine: " + object.getDefinitions().name + " (id: "
							+ object.getId() + ") " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane() + ".",
					true);
		}
	}

	private static void slashWeb(Player player, WorldObject object) {
		if (Utils.random(2) == 0) {
			World.spawnObjectTemporary(new WorldObject(object.getId() + 1,
					object.getType(), object.getRotation(), object.getX(),
					object.getY(), object.getPlane()), 60000);
			player.getPackets().sendGameMessage("You slash through the web!");
		} else
			player.getPackets().sendGameMessage(
					"You fail to cut through the web.");
	}

	private static boolean handleGate(Player player, WorldObject object) {
		return handleGate(player, object, 60000);
	}

	public static boolean handleGate(Player player, WorldObject object,
			long delay) {
		if (World.isSpawnedObject(object))
			return false;
		if (object.getRotation() == 0) {
			boolean south = true;
			WorldObject otherDoor = World.getObjectWithType(new WorldTile(
					object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObjectWithType(new WorldTile(
						object.getX(), object.getY() - 1, object.getPlane()),
						object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
								.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor1.setRotation(3);
				openedDoor2.moveLocation(-1, 0, 0);
			} else {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor2.moveLocation(-1, 0, 0);
				openedDoor2.setRotation(3);
			}

			if (World.removeObjectTemporary(object, delay)
					&& World.removeObjectTemporary(otherDoor, delay)) {
				player.faceObject(openedDoor1);
				World.spawnObjectTemporary(openedDoor1, delay);
				World.spawnObjectTemporary(openedDoor2, delay);
				return true;
			}
		} else if (object.getRotation() == 2) {

			boolean south = true;
			WorldObject otherDoor = World.getObjectWithType(new WorldTile(
					object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObjectWithType(new WorldTile(
						object.getX(), object.getY() - 1, object.getPlane()),
						object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
								.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor2.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			} else {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor1.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			}
			if (World.removeObjectTemporary(object, delay)
					&& World.removeObjectTemporary(otherDoor, delay)) {
				player.faceObject(openedDoor1);
				World.spawnObjectTemporary(openedDoor1, delay);
				World.spawnObjectTemporary(openedDoor2, delay);
				return true;
			}
		} else if (object.getRotation() == 3) {

			boolean right = true;
			WorldObject otherDoor = World.getObjectWithType(new WorldTile(
					object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObjectWithType(new WorldTile(
						object.getX() + 1, object.getY(), object.getPlane()),
						object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
								.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor2.setRotation(0);
				openedDoor1.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			} else {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			}
			if (World.removeObjectTemporary(object, delay)
					&& World.removeObjectTemporary(otherDoor, delay)) {
				player.faceObject(openedDoor1);
				World.spawnObjectTemporary(openedDoor1, delay);
				World.spawnObjectTemporary(openedDoor2, delay);
				return true;
			}
		} else if (object.getRotation() == 1) {

			boolean right = true;
			WorldObject otherDoor = World.getObjectWithType(new WorldTile(
					object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObjectWithType(new WorldTile(
						object.getX() + 1, object.getY(), object.getPlane()),
						object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
								.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			} else {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor2.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			}
			if (World.removeObjectTemporary(object, delay)
					&& World.removeObjectTemporary(otherDoor, delay)) {
				player.faceObject(openedDoor1);
				World.spawnObjectTemporary(openedDoor1, delay);
				World.spawnObjectTemporary(openedDoor2, delay);
				return true;
			}
		}
		return false;
	}

	public static boolean handleDoor(Player player, WorldObject object,
			long timer) {
		if (World.isSpawnedObject(object)) {
			// World.removeObject(object);
			return false;
		}
		WorldObject openedDoor = new WorldObject(object.getId(),
				object.getType(), (object.getRotation() + 1) & 0x3,
				object.getX(), object.getY(), object.getPlane());
		World.spawnObjectTemporary(openedDoor, timer);
		return false;
	}

	private static boolean handleDoor(Player player, WorldObject object) {
		return handleDoor(player, object, 60000);
	}

	public static WorldTile getClimbTile(WorldObject object, boolean up) {
		int x;
		int y;
		if (up) {
			switch (object.getRotation()) {
			case 0:
				x = object.getX() + object.getDefinitions().getSizeX() - 1;
				y = object.getY() + object.getDefinitions().getSizeY();
				break;
			case 1:
				x = object.getX() + object.getDefinitions().getSizeY();
				y = object.getY() + object.getDefinitions().getSizeX() - 1;
				break;
			case 2:
				x = object.getX() + object.getDefinitions().getSizeX() - 1;
				y = object.getY() - 1;
				break;
			case 3:
			default:
				x = object.getX() - 1;
				y = object.getY() + object.getDefinitions().getSizeX() - 1;
				break;
			}
		} else {
			switch (object.getRotation()) {
			case 0:
				x = object.getX() + object.getDefinitions().getSizeX() - 1;
				y = object.getY() - 1;
				break;
			case 1:
				x = object.getX() - object.getDefinitions().getSizeY();
				y = object.getY() - 1;
				break;
			case 2:
				x = object.getX() + object.getDefinitions().getSizeX() - 1;
				y = object.getY() - object.getDefinitions().getSizeY();
				break;
			case 3:
			default:
				x = object.getX() + object.getDefinitions().getSizeY();
				y = object.getY() + object.getDefinitions().getSizeX() - 1;
				break;
			}
		}
		return new WorldTile(x, y, object.getPlane() + (up ? 1 : -1));
	}

	private static boolean handleStaircases(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);

		if (object.getId() != 82488 && object.getId() != 82489
				&& object.getId() != 82490 && object.getId() != 82491) {
			if (option.equalsIgnoreCase("Climb-up")
					|| option.equalsIgnoreCase("Climb up")) {
				if (player.getPlane() == 3)
					return false;
				player.useStairs(-1, player.transform(0, 0, 1), 0, 1);
			} else if (option.equalsIgnoreCase("Climb-down")
					|| option.equalsIgnoreCase("Climb down")) {
				if (player.getPlane() == 0)
					return false;
				player.useStairs(-1, player.transform(0, 0, -1), 0, 1);
			} else if (option.equalsIgnoreCase("Climb")) {
				if (player.getPlane() == 3 || player.getPlane() == 0)
					return false;
				player.getDialogueManager().startDialogue("ClimbNoEmoteStairs",
						player.transform(0, 0, 1), player.transform(0, 0, -1),
						"Go up the stairs.", "Go down the stairs.");
			} else
				return false;
		}
		return false;
	}

	private static boolean handleLadder(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")
				|| option.equalsIgnoreCase("Climb up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")
				|| option.equalsIgnoreCase("Climb down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() - 1), "Climb up the ladder.",
					"Climb down the ladder.", 828);
		} else
			return false;
		return true;
	}

	public static void handleItemOnObject(final Player player,
			final WorldObject object, final int interfaceId, final int slot,
			final int itemId) {
		final Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		final ObjectDefinitions objectDef = object.getDefinitions();
		if (item.getId() == 5733) {
			if (player.getRights() < 2) {
				player.getInventory().deleteItem(5733, 28);
				return;
			}
			RottenPotato.useOnObject(player, objectDef);
		}
		if (item.getId() == 5350 && SpotInfo.getInfo(object.getId()) != null
				&& SpotInfo.getInfo(object.getId()).getType() == 0) {
			player.getInventory().deleteItem(5350, 1);
			player.getInventory().addItem(5354, 1);
			return;
		}
		if (object.getId() == 1473 && item.getId() == 4252)
			Ectofuntus.handleWorship(player);
		if (object.getId() == 92279) {
			MaxGuild.buildStatue(player, item);
			return;
		}
		if (object.getId() == 1996) {
			if (item.getId() != 954)
				return;
			for (int i = 0; i < 7; i++) {
				WorldObject o = new WorldObject(object);
				o.setId(1998);
				o.setLocation(o.getX(), 3475 - i, o.getPlane());
				player.getPackets().sendAddObject(o);
			}
			WorldObject o = new WorldObject(object);
			o.setId(1997);
			player.getPackets().sendAddObject(o);
			player.getAppearence().setRenderEmote(188);
			player.setNextForceMovement(new ForceMovement(object, 8,
					ForceMovement.SOUTH));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 7; i++) {
						WorldObject o = new WorldObject(object);
						o.setId(1998);
						o.setLocation(o.getX(), 3475 - i, o.getPlane());
						player.getPackets().sendRemoveObject(o);
					}
					WorldObject o = new WorldObject(object);
					o.setId(1996);
					player.getPackets().sendAddObject(o);
					player.getAppearence().setRenderEmote(-1);
					player.setNextWorldTile(new WorldTile(2513, 3468, 0));
				}
			}, 7);
		} else {
			if (FishingFerretRoom.handleFerretThrow(player, object, item))
				return;
		}

		player.setRouteEvent(new RouteEvent(
				object,
				() -> {
					if (!player.getControllerManager().handleItemOnObject(
							object, item))
						return;
					player.faceObject(object);
					if (Ectofuntus.handleItemOnObject(player, itemId,
							object.getId()))
						return;
					else if (SpotInfo.getInfo(object.getId()) != null
							&& player.getFarmingManager().getSpot(
									SpotInfo.getInfo(object.getId())) != null
							&& !player.getFarmingManager()
									.getSpot(SpotInfo.getInfo(object.getId()))
									.isEmpty() && itemId == 952) {
						player.getFarmingManager().clearFarmingPatch(
								player.getFarmingManager().getSpot(
										SpotInfo.getInfo(object.getId())));
						return;
					}
					switch (object.getId()) {
					case 88260:
						ForgingBar fbar = ForgingBar.getBar(item.getId());
						if (fbar != null) {
							player.getDialogueManager().startDialogue(
									"SmithingD", object, fbar);
						} else {
							player.getPackets().sendGameMessage(
									"You do not have a bar in your inventory.");
						}
						break;
					case 88261:
						SmeltingBar bar = SmeltingBar.getBar(item.getId());
						if (bar != null) {
							player.getDialogueManager().startDialogue(
									"SmeltingD", object, bar);
						} else {
							player.getPackets().sendGameMessage(
									"You do not have a bar in your inventory.");
						}
						break;
					}
					if (itemId == 5733) {
						RottenPotato.useOnObject(player, object);
					}
					if (itemId >= 1438 && itemId <= 1450) {
						for (int index = 0; index < Runecrafting.OBJECTS.length; index++) {
							if (Runecrafting.OBJECTS[index] == object.getId()) {
								Runecrafting.infuseTiara(player, index);
								break;
							}
						}
					} else if (itemId == CoalTrucksManager.COAL
							&& object.getId() == 2114)
						player.getCoalTrucksManager().addCoal();

					else if (itemId == 1438 && object.getId() == 2452) {
						Runecrafting.enterAirAltar(player);
					} else if (itemId == 1440 && object.getId() == 2455) {
						Runecrafting.enterEarthAltar(player);
					} else if (object.getId() == 28352
							|| object.getId() == 28550) {
						Incubator.useEgg(player, itemId);
					} else if (itemId == 1442 && object.getId() == 2456) {
						Runecrafting.enterFireAltar(player);
					} else if (itemId == 1444 && object.getId() == 2454) {
						Runecrafting.enterWaterAltar(player);
					} else if (itemId == 1446 && object.getId() == 2457) {
						Runecrafting.enterBodyAltar(player);
					} else if (itemId == 1448 && object.getId() == 2453) {
						Runecrafting.enterMindAltar(player);
					} else if (itemId == 1456 && object.getId() == 2462) {
						Runecrafting.enterDeathAltar(player);
					} else if (itemId == 1452 && object.getId() == 2461) {
						Runecrafting.enterChaosAltar(player);
					} else if (itemId == 1450 && object.getId() == 2464) {
						Runecrafting.enterBloodAltar(player);
					} else if (itemId == 1454 && object.getId() == 2458) {
						Runecrafting.enterCosmicAltar(player);
					} else if (itemId == 1462 && object.getId() == 2460) {
						Runecrafting.enterNatureAltar(player);
					} else if (player.getFarmingManager().isFarming(
							object.getId(), item, 0)) {
					} else if (object.getId() == 733 || object.getId() == 64729) {
						player.setNextAnimation(new Animation(PlayerCombat
								.getAttackAnimation(player, true)));
						slashWeb(player, object);
					} else if (object.getId() == 82049 && itemId == 954) {
						if (player.isKalphiteLairSetted())
							return;
						player.getInventory().deleteItem(954, 1);
						player.setKalphiteLair();
					} else if (object.getId() == 48803 && itemId == 954) {
						if (player.isKalphiteLairEntranceSetted())
							return;
						player.getInventory().deleteItem(954, 1);
						player.setKalphiteLairEntrance();
					} else if (object.getId() == 13715) {
						if (ItemConstants.getItemFixed(itemId) == -1) {
							player.getDialogueManager().startDialogue(
									"SimpleMessage",
									"You cant repair this item.");
							return;
						}
						player.getDialogueManager().startDialogue("Repair",
								945, itemId);
					} else if (object.getId() == 172 && itemId == 989)
						CrystalChest.openChest(player, object);
					else if (object.getId() == 17239 && itemId == 9626)
						player.getDialogueManager().startDialogue(
								"SingingBowl", item);
					else if (object.getId() == 36695) {
						if ((item.getId() >= 1704 && item.getId() <= 1710 && item
								.getId() % 2 == 0)
								|| (item.getId() >= 10356
										&& item.getId() <= 10366 && item
										.getId() % 2 == 0)
								|| (item.getId() == 2572 || (item.getId() >= 20653
										&& item.getId() <= 20657 && item
										.getId() % 2 != 0))) {
							for (Item item1 : player.getInventory().getItems()
									.getItems()) {
								if (item1 == null)
									continue;
								if (item1.getId() >= 1704
										&& item1.getId() <= 1710
										&& item1.getId() % 2 == 0)
									item1.setId(1712);
								else if (item1.getId() >= 10356
										&& item1.getId() <= 10366
										&& item1.getId() % 2 == 0)
									item1.setId(10354);
								else if (item1.getId() == 2572
										|| (item1.getId() >= 20653
												&& item1.getId() <= 20657 && item1
												.getId() % 2 != 0))
									item1.setId(20659);
							}
							player.getInventory().refresh();
							player.getDialogueManager()
									.startDialogue(
											"ItemMessage",
											"Your ring of wealth and amulet of glory have all been recharged.",
											1712);
						} else if (AccessorySmithing.isEmptyRing(itemId)
								&& object.getX() == 3047
								&& object.getY() == 4493) {
							player.getDialogueManager().startDialogue(
									"ImbueingDialouge", itemId);
						}
					} else {
						switch (objectDef.name.toLowerCase()) {
						case "fountain":
						case "well":
						case "sink":
						case "water barrel":
						case "water barrels":
							WaterFilling.isFilling(player, itemId, false);
							break;
						case "sand pit":
						case "sand box":
							player.getActionManager().setAction(
									new SandBucketFillAction());
							break;
						case "anvil":
							if (itemId == 1540 || itemId == 11286) {
								player.getDialogueManager().startDialogue(
										"DragonFireShieldCreating");
								return;
							}
							if (itemId == 2366 || itemId == 2368) {
								player.getDialogueManager().startDialogue(
										"DragonSqShieldD");
								return;
							}
							if (itemId == 11710 || itemId == 11712
									|| itemId == 11714) {
								player.getDialogueManager().startDialogue(
										"GodswordCreatingD");
								return;
							}
							if (itemId == 13746 || itemId == 13748
									|| itemId == 13750 || itemId == 13752) {
								player.getDialogueManager().startDialogue(
										"SpiritShieldCreating", item);
								return;
							}
							if (itemId == 13736) {
								Item item1 = player.getInventory()
										.containsItem(13748, 1) ? new Item(
										13748, 1) : player.getInventory()
										.containsItem(13750, 1) ? new Item(
										13750, 1) : player.getInventory()
										.containsItem(13746, 1) ? new Item(
										13746, 1) : player.getInventory()
										.containsItem(13752, 1) ? new Item(
										13752, 1) : null;
								if (item1 == null) {
									player.getPackets().sendGameMessage(
											"You need a sigil to do this");
									return;
								} else {
									player.getDialogueManager().startDialogue(
											"SpiritShieldCreating", item1);
									return;
								}
							}
							if (objectDef.containsOption(0, "Smith")) {
								ForgingBar bar = ForgingBar.getBar(itemId);
								if (bar != null) {
									player.getDialogueManager().startDialogue(
											"SmithingD", object, bar);
								} else {
									player.getPackets().sendGameMessage(
											"This bar has not been added yet.");
								}
							}
							break;
						case "furnace":
						case "lava furnace":
							SmeltingBar bar = SmeltingBar.getBar(itemId);
							if (bar != null) { // else nothing interestin
												// happens
								player.getDialogueManager().startDialogue(
										"SmeltingD", object, bar);
							}
							break;
						case "altar":
							if (itemId == SpiritshieldCreating.SPIRIT_SHIELD
									|| itemId == SpiritshieldCreating.HOLY_ELIXIR) {
								SpiritshieldCreating.blessShield(player, true);
								return;
							}
							break;
						case "fire":
						case "firepit":
							if (item.getId() == 17374) {
								player.getInventory().deleteItem(item.getId(),
										1);
								player.getInventory().addItem(17375, 1);
								player.getPackets().sendGameMessage(
										"You successfully cook the vile fish.");
								return;
							}
							SkillsDialogue.selectTool(player, -2);
							return;
						case "range":
						case "cooking range":
						case "stove":
						case "grill":
						case "portable range":
							Cookables cook = Cooking.isCookingSkill(item);
							if (cook != null) {
								if (cook.isFireOnly()
										&& !object.getDefinitions().name
												.equals("Fire")) {
									player.getDialogueManager()
											.startDialogue("SimpleMessage",
													"You may only cook this on a fire.");
									return;
								} else if (cook.isSpitRoast()
										&& object.getId() != 11363) {
									player.getDialogueManager()
											.startDialogue("SimpleMessage",
													"You may only cook this on an iron spit.");
									return;
								} else if (player.getSkills().getLevel(
										Skills.COOKING) < cook.getLvl()) {
									player.getDialogueManager().startDialogue(
											"SimpleMessage",
											"You need a cooking level of "
													+ cook.getLvl()
													+ " to cook this food.");
									return;
								}
								player.getDialogueManager().startDialogue(
										"CookingD", cook, object);
								return;
							}
							player.getDialogueManager()
									.startDialogue(
											"SimpleMessage",
											"You can't cook that on a "
													+ (objectDef.name
															.equals("Fire") ? "fire"
															: "range") + ".");
							break;
						}
						if (Settings.DEBUG) {
							System.out.println("Item on object: "
									+ object.getId());
							player.getPackets().sendGameMessage(
									"Item on Object: "
											+ object.getDefinitions().name
											+ " (id: " + object.getId() + ") "
											+ object.getX() + ", "
											+ object.getY() + ", "
											+ object.getPlane() + ".", true);
						}
					}
				}));

	}

	public static void handleBarrier(Player player) {
		if (player.getX() == 1650 && player.getY() == 5281
				|| player.getX() == 1651 && player.getY() == 5281
				|| player.getX() == 1650 && player.getY() == 5281) {
			player.addWalkSteps(1651, 5280, 1, false);
		}
		if (player.getX() == 1652 && player.getY() == 5280
				|| player.getX() == 1651 && player.getY() == 5280
				|| player.getX() == 1653 && player.getY() == 5280) {
			player.addWalkSteps(1651, 5281, 1, false);
		}
		if (player.getX() == 1650 && player.getY() == 5301
				|| player.getX() == 1650 && player.getY() == 5302
				|| player.getX() == 1650 && player.getY() == 5303) {
			player.addWalkSteps(1649, 5302, 1, false);
		}
		if (player.getX() == 1649 && player.getY() == 5303
				|| player.getX() == 1649 && player.getY() == 5302
				|| player.getX() == 1649 && player.getY() == 5301) {
			player.addWalkSteps(1650, 5302, 1, false);
		}
		if (player.getX() == 1626 && player.getY() == 5301
				|| player.getX() == 1626 && player.getY() == 5302
				|| player.getX() == 1626 && player.getY() == 5303) {
			player.addWalkSteps(1625, 5302, 1, false);
		}
		if (player.getX() == 1625 && player.getY() == 5301
				|| player.getX() == 1625 && player.getY() == 5302
				|| player.getX() == 1625 && player.getY() == 5303) {
			player.addWalkSteps(1626, 5302, 1, false);
		}
		if (player.getX() == 1609 && player.getY() == 5289
				|| player.getX() == 1610 && player.getY() == 5289
				|| player.getX() == 1611 && player.getY() == 5289) {
			player.addWalkSteps(1610, 5288, 1, false);
		}
		if (player.getX() == 1609 && player.getY() == 5288
				|| player.getX() == 1610 && player.getY() == 5288
				|| player.getX() == 1611 && player.getY() == 5288) {
			player.addWalkSteps(1610, 5289, 1, false);
		}
		if (player.getX() == 1606 && player.getY() == 5265
				|| player.getX() == 1605 && player.getY() == 5265
				|| player.getX() == 1604 && player.getY() == 5265) {
			player.addWalkSteps(1605, 5264, 1, false);
		}
		if (player.getX() == 1606 && player.getY() == 5264
				|| player.getX() == 1605 && player.getY() == 5264
				|| player.getX() == 1604 && player.getY() == 5264) {
			player.addWalkSteps(1605, 5265, 1, false);
		}
		if (player.getX() == 1634 && player.getY() == 5254
				|| player.getX() == 1634 && player.getY() == 5253
				|| player.getX() == 1634 && player.getY() == 5252) {
			player.addWalkSteps(1635, 5253, 1, false);
		}
		if (player.getX() == 1635 && player.getY() == 5254
				|| player.getX() == 1635 && player.getY() == 5253
				|| player.getX() == 1635 && player.getY() == 5252) {
			player.addWalkSteps(1634, 5253, 1, false);
		}
		if (player.getX() == 1659 && player.getY() == 5304
				|| player.getX() == 1658 && player.getY() == 5304
				|| player.getX() == 1657 && player.getY() == 5304) {
			player.addWalkSteps(1658, 5303, 1, false);
		}
		if (player.getX() == 1657 && player.getY() == 5303
				|| player.getX() == 1658 && player.getY() == 5303
				|| player.getX() == 1659 && player.getY() == 5303) {
			player.addWalkSteps(1658, 5304, 1, false);
		}
	}

	public static void handleLogBalance(Player player, WorldObject object,
			int distance, int requiredLevel) {
		if (!player.getSkills().hasLevel(Skills.AGILITY, requiredLevel))
			return;
		boolean hasRun = player.getRun();
		player.setRun(false);
		if (object.getX() == player.getX()) {
			if (player.getY() > object.getY())
				player.addWalkSteps(player.getX(), (player.getY() - distance),
						-1, false);
			else
				player.addWalkSteps(player.getX(), (player.getY() + distance),
						-1, false);
		} else if (object.getY() == player.getY()) {
			if (player.getX() > object.getX())
				player.addWalkSteps((player.getX() - distance), player.getY(),
						-1, false);
			else
				player.addWalkSteps((player.getX() + distance), player.getY(),
						-1, false);
		}
		player.getPackets().sendGameMessage(
				"You walk carefully across the slippery log...", true);
		player.lock(distance);
		player.setNextAnimation(new Animation(9908));
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					player.getPackets().sendGameMessage(
							"... and make it safely to the other side.", true);
					if (hasRun)
						player.setRun(true);
					player.setNextAnimation(new Animation(-1));
				}, distance / 2 + 1, TimeUnit.SECONDS);
		return;
	}

	public static void handleCrossOver(Player player, WorldObject object,
			int distance, int animation) {
		WorldTile toTile = null;
		if (object.getX() == player.getX()) {
			if (player.getY() > object.getY())
				player.setNextForceMovement(new ForceMovement(
						toTile = new WorldTile(player.getX(), player.getY()
								- distance, player.getPlane()), distance,
						MapBuilder.SOUTH));
			else
				player.setNextForceMovement(new ForceMovement(
						toTile = new WorldTile(player.getX(), player.getY()
								+ distance, player.getPlane()), distance,
						MapBuilder.NORTH));
		} else if (object.getY() == player.getY()) {
			if (player.getX() > object.getX())
				player.setNextForceMovement(new ForceMovement(
						toTile = new WorldTile(player.getX() - distance, player
								.getY(), player.getPlane()), distance,
						MapBuilder.WEST));
			else
				player.setNextForceMovement(new ForceMovement(
						toTile = new WorldTile(player.getX() + distance, player
								.getY(), player.getPlane()), distance,
						MapBuilder.EAST));
		}
		player.setNextAnimation(new Animation(animation));
		final WorldTile destination = toTile;
		GameExecutorManager.slowExecutor.schedule(() -> {
			player.setNextWorldTile(destination);
		}, distance / 2, TimeUnit.SECONDS);
		if (object.getId() == 101305)
			GameExecutorManager.slowExecutor
					.schedule(
							() -> {
								player.getDialogueManager()
										.startDialogue(
												"WarningD",
												DoomsayerManager.NORMAL_WARNING,
												36,
												"This artefact will transport you directly to the King Black Dragon's Lair. The King Black Dragon's Lair is extremely <col=ff0000>dangerous</col>. Are you sure you want to continue?<br><br>(A Combat level of 50+ is recommended)",
												null,
												(Runnable) () -> {
													player.setNextAnimation(new Animation(
															827));
													player.getPackets()
															.sendGameMessage(
																	"You activate the artefact...",
																	true);
													BossInstanceHandler
															.enterInstance(
																	player,
																	Boss.King_Black_Dragon);
												});
							}, distance, TimeUnit.SECONDS);
		return;
	}
}
