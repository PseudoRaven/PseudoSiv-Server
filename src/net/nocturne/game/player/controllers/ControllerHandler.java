package net.nocturne.game.player.controllers;

import java.util.HashMap;

import net.nocturne.game.player.content.activities.minigames.Barrows;
import net.nocturne.game.player.content.activities.minigames.BrimhavenAgility;
import net.nocturne.game.player.content.activities.minigames.Falconry;
import net.nocturne.game.player.content.activities.minigames.FightCaves;
import net.nocturne.game.player.content.activities.minigames.FightKiln;
import net.nocturne.game.player.content.activities.minigames.PuroPuro;
import net.nocturne.game.player.content.activities.minigames.PyramidPlunder;
import net.nocturne.game.player.content.activities.minigames.RunespanController;
import net.nocturne.game.player.content.activities.minigames.SorceressGarden;
import net.nocturne.game.player.content.activities.minigames.WarriorsGuild;
import net.nocturne.game.player.content.activities.minigames.clanwars.ClanWarRequestController;
import net.nocturne.game.player.content.activities.minigames.clanwars.FfaZone;
import net.nocturne.game.player.content.activities.minigames.clanwars.WarController;
import net.nocturne.game.player.content.activities.minigames.dominon.DTController;
import net.nocturne.game.player.content.activities.minigames.duel.DuelArena;
import net.nocturne.game.player.content.activities.minigames.duel.DuelController;
import net.nocturne.game.player.content.activities.minigames.riseofthesix.RiseOfTheSix;
import net.nocturne.game.player.content.activities.minigames.soulwars.SoulWars;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationController;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationLobbyController;
import net.nocturne.game.player.content.activities.surpriseevents.LastManStandingController;
import net.nocturne.game.player.content.activities.surpriseevents.TeamVsTeamController;
import net.nocturne.game.player.controllers.bossInstance.BossInstanceController;
import net.nocturne.game.player.controllers.bossInstance.CorporealBeastInstanceController;
import net.nocturne.game.player.controllers.bossInstance.DagannothKingsInstanceController;
import net.nocturne.game.player.controllers.bossInstance.EvilChickenInstanceController;
import net.nocturne.game.player.controllers.bossInstance.KalphiteKingInstanceController;
import net.nocturne.game.player.controllers.bossInstance.KalphiteQueenInstanceController;
import net.nocturne.game.player.controllers.bossInstance.RiseOfTheSixController;
import net.nocturne.game.player.controllers.bossInstance.VoragoInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars.GeneralGraadorInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GorvekAndVindictaInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GregorovicInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.HelwyrInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.TwinFuriesInstanceController;
import net.nocturne.game.player.controllers.castlewars.CastleWarsPlaying;
import net.nocturne.game.player.controllers.castlewars.CastleWarsWaiting;
import net.nocturne.game.player.controllers.events.DeathEvent;
import net.nocturne.game.player.controllers.fightpits.FightPitsArena;
import net.nocturne.game.player.controllers.fightpits.FightPitsLobby;
import net.nocturne.game.player.controllers.godwars2.GodWars2;
import net.nocturne.game.player.controllers.pestcontrol.PestControlGame;
import net.nocturne.game.player.controllers.pestcontrol.PestControlLobby;
import net.nocturne.game.player.controllers.trollinvasion.TrollInvasion;
import net.nocturne.utils.Logger;

public class ControllerHandler {

	private static final HashMap<Object, Class<? extends Controller>> handledControllers = new HashMap<Object, Class<? extends Controller>>();

	@SuppressWarnings("unchecked")
	public static void init() {
		try {
			Class<Controller> value1 = (Class<Controller>) Class
					.forName(Wilderness.class.getCanonicalName());
			handledControllers.put("Wilderness", value1);
			Class<Controller> value2 = (Class<Controller>) Class
					.forName(Kalaboss.class.getCanonicalName());
			handledControllers.put("Kalaboss", value2);
			Class<Controller> value4 = (Class<Controller>) Class
					.forName(GodWars.class.getCanonicalName());
			handledControllers.put("GodWars", value4);
			Class<Controller> value19 = (Class<Controller>) Class
					.forName(GodWars2.class.getCanonicalName());
			handledControllers.put("GodWars2", value19);
			Class<Controller> value20 = (Class<Controller>) Class
					.forName(GorvekAndVindictaInstanceController.class
							.getCanonicalName());
			handledControllers.put("GorvekAndVindictaInstanceController",
					value20);
			Class<Controller> value21 = (Class<Controller>) Class
					.forName(TwinFuriesInstanceController.class
							.getCanonicalName());
			handledControllers.put("TwinFuriesInstanceController", value21);
			Class<Controller> value22 = (Class<Controller>) Class
					.forName(GregorovicInstanceController.class
							.getCanonicalName());
			handledControllers.put("GregorovicInstanceController", value22);
			Class<Controller> value23 = (Class<Controller>) Class
					.forName(HelwyrInstanceController.class.getCanonicalName());
			handledControllers.put("HelwyrInstanceController", value23);
			Class<Controller> value24 = (Class<Controller>) Class
					.forName(RiseOfTheSixController.class.getCanonicalName());
			handledControllers.put("RiseOfTheSixController", value24);
			Class<Controller> value5 = (Class<Controller>) Class
					.forName(ZEDController.class.getCanonicalName());
			handledControllers.put("ZEDController", value5);
			Class<Controller> value9 = (Class<Controller>) Class
					.forName(DuelArena.class.getCanonicalName());
			handledControllers.put("DuelArena", value9);
			Class<Controller> value10 = (Class<Controller>) Class
					.forName(DuelController.class.getCanonicalName());
			handledControllers.put("DuelController", value10);
			Class<Controller> value14 = (Class<Controller>) Class
					.forName(DTController.class.getCanonicalName());
			handledControllers.put("DTController", value14);
			Class<Controller> value17 = (Class<Controller>) Class
					.forName(CastleWarsPlaying.class.getCanonicalName());
			handledControllers.put("CastleWarsPlaying", value17);
			Class<Controller> value18 = (Class<Controller>) Class
					.forName(CastleWarsWaiting.class.getCanonicalName());
			handledControllers.put("CastleWarsWaiting", value18);
			handledControllers.put("DungeonController",
					(Class<Controller>) Class.forName(DungeonController.class
							.getCanonicalName()));
			handledControllers.put("LucienController",
					(Class<Controller>) Class.forName(LucienController.class
							.getCanonicalName()));
			handledControllers.put("clan_wars_request",
					(Class<Controller>) Class
							.forName(ClanWarRequestController.class
									.getCanonicalName()));
			handledControllers.put("clan_war", (Class<Controller>) Class
					.forName(WarController.class.getCanonicalName()));
			handledControllers.put("clan_wars_ffa", (Class<Controller>) Class
					.forName(FfaZone.class.getCanonicalName()));
			handledControllers.put("NomadsRequiem", (Class<Controller>) Class
					.forName(NomadsRequiem.class.getCanonicalName()));
			handledControllers.put("BorkController", BorkController.class);
			handledControllers.put("BrimhavenAgility",
					(Class<Controller>) Class.forName(BrimhavenAgility.class
							.getCanonicalName()));
			handledControllers.put("FightCavesController",
					(Class<Controller>) Class.forName(FightCaves.class
							.getCanonicalName()));
			handledControllers.put("FightKilnController",
					(Class<Controller>) Class.forName(FightKiln.class
							.getCanonicalName()));
			handledControllers.put("FightPitsLobby", (Class<Controller>) Class
					.forName(FightPitsLobby.class.getCanonicalName()));
			handledControllers.put("FightPitsArena", (Class<Controller>) Class
					.forName(FightPitsArena.class.getCanonicalName()));
			handledControllers.put("PestControlGame", (Class<Controller>) Class
					.forName(PestControlGame.class.getCanonicalName()));
			handledControllers.put("PestControlLobby",
					(Class<Controller>) Class.forName(PestControlLobby.class
							.getCanonicalName()));
			handledControllers.put("Barrows", (Class<Controller>) Class
					.forName(Barrows.class.getCanonicalName()));
			handledControllers.put("Falconry", (Class<Controller>) Class
					.forName(Falconry.class.getCanonicalName()));
			handledControllers.put("QueenBlackDragonController",
					(Class<Controller>) Class
							.forName(QueenBlackDragonController.class
									.getCanonicalName()));
			handledControllers.put("HouseController", (Class<Controller>) Class
					.forName(HouseController.class.getCanonicalName()));
			handledControllers.put("RuneSpanController",
					(Class<Controller>) Class.forName(RunespanController.class
							.getCanonicalName()));
			handledControllers.put("DeathEvent", (Class<Controller>) Class
					.forName(DeathEvent.class.getCanonicalName()));
			handledControllers.put("PyramidPlunder", (Class<Controller>) Class
					.forName(PyramidPlunder.class.getCanonicalName()));
			handledControllers.put("SorceressGarden", (Class<Controller>) Class
					.forName(SorceressGarden.class.getCanonicalName()));
			handledControllers.put("JailController", (Class<Controller>) Class
					.forName(JailController.class.getCanonicalName()));
			handledControllers.put("CrucibleController",
					(Class<Controller>) Class.forName(CrucibleController.class
							.getCanonicalName()));
			handledControllers.put("StealingCreationLobby",
					(Class<Controller>) Class
							.forName(StealingCreationLobbyController.class
									.getCanonicalName()));
			handledControllers.put("StealingCreationGame",
					(Class<Controller>) Class
							.forName(StealingCreationController.class
									.getCanonicalName()));
			handledControllers.put("NewPlayerController",
					(Class<Controller>) Class.forName(NewPlayerController.class
							.getCanonicalName()));
			handledControllers.put("PuroPuro", (Class<Controller>) Class
					.forName(PuroPuro.class.getCanonicalName()));
			handledControllers.put("RuneEssenceController",
					(Class<Controller>) Class
							.forName(RuneEssenceController.class
									.getCanonicalName()));
			handledControllers.put("UnderGroundDungeon",
					(Class<Controller>) Class.forName(UnderGroundDungeon.class
							.getCanonicalName()));
			handledControllers.put("TerrorDogsTarnsLairController",
					(Class<Controller>) Class
							.forName(TerrorDogsTarnsLairController.class
									.getCanonicalName()));
			handledControllers.put("WarriorsGuild", WarriorsGuild.class);
			handledControllers.put("JadinkoLair", (Class<Controller>) Class
					.forName(JadinkoLair.class.getCanonicalName()));
			handledControllers.put("KuradalDungeon", (Class<Controller>) Class
					.forName(KuradalDungeon.class.getCanonicalName()));
			handledControllers.put("SawmillController",
					(Class<Controller>) Class.forName(SawmillController.class
							.getCanonicalName()));
			handledControllers.put("NewSpawnPlayerController",
					(Class<Controller>) Class
							.forName(NewSpawnPlayerController.class
									.getCanonicalName()));
			handledControllers.put("LastManStandingController",
					LastManStandingController.class);
			handledControllers.put("TeamVsTeamController",
					TeamVsTeamController.class);
			handledControllers.put("SoulWarsController",
					(Class<Controller>) Class.forName(SoulWars.class
							.getCanonicalName()));
			// handledControllers.put("SoulWarsController", SoulWars.class);
			// boss instance
			handledControllers.put("BossInstanceController",
					BossInstanceController.class);
			handledControllers.put("KalphiteQueenInstanceController",
					KalphiteQueenInstanceController.class);
			handledControllers.put("DagannothKingsInstanceController",
					DagannothKingsInstanceController.class);
			handledControllers.put("CorporealBeastInstanceController",
					CorporealBeastInstanceController.class);
			handledControllers.put("GeneralGraadorInstanceController",
					GeneralGraadorInstanceController.class);
			handledControllers.put("EvilChickenInstanceController",
					EvilChickenInstanceController.class);
			handledControllers.put("KalphiteKingInstanceController",
					KalphiteKingInstanceController.class);
			handledControllers.put("VoragoInstanceController",
					VoragoInstanceController.class);
			handledControllers.put("FistOfGuthixController",
					FistOfGuthixController.class);
			handledControllers.put("TrollInvasion", TrollInvasion.class);
			handledControllers.put("RiseOfTheSix", (Class<Controller>) Class
					.forName(RiseOfTheSix.class.getCanonicalName()));
			handledControllers.put("AraxxiController",
					(Class<Controller>) Class.forName(AraxxiController.class
							.getCanonicalName()));
			handledControllers.put("SpectateControler",
					(Class<Controller>) Class.forName(SpectateControler.class
							.getCanonicalName()));

			// Start Quests
			handledControllers.put("SongFromTheDepths",
					(Class<Controller>) Class.forName(SongFromTheDepths.class
							.getCanonicalName()));
			handledControllers.put("SongFromTheDepthsSurface",
					(Class<Controller>) Class
							.forName(SongFromTheDepthsSurface.class
									.getCanonicalName()));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static void reload() {
		handledControllers.clear();
		init();
	}

	public static Controller getController(Object key) {
		if (key instanceof Controller)
			return (Controller) key;
		Class<? extends Controller> classC = handledControllers.get(key);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}
}