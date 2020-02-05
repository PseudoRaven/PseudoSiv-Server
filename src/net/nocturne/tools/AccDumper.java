package net.nocturne.tools;

import minifs.MiniFS;
import net.nocturne.Settings;
import net.nocturne.cache.Cache;
import net.nocturne.game.npc.combat.CombatScriptsHandler;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.fishing.FishingSpotsHandler;
import net.nocturne.game.player.content.activities.clans.ClansManager;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.controllers.ControllerHandler;
import net.nocturne.game.player.cutscenes.CutscenesHandler;
import net.nocturne.game.player.dialogues.DialogueHandler;
import net.nocturne.utils.*;
import net.nocturne.utils.huffman.Huffman;

import java.io.File;
import java.io.FileOutputStream;

public class AccDumper {

	public static void main(String[] args) throws Throwable {
		Settings.HOSTED = false;
		Logger.log("Launcher", "Initing File System...");
		Logger.log("Launcher", "Initing Cache...");
		Cache.init();
		Huffman.init();
		Logger.log("Launcher", "Initing Data Files...");
		Censor.init();
		MapArchiveKeys.init();
		MapAreas.init();
		ObjectSpawns.init();
		ObjectExamines.init();
		NPCSpawns.init();
		NPCCombatDefinitionsL.init();
		NPCDrops.init();
		NPCExamines.init();
		ItemExamines.init();
		ItemDestroys.init();
		ItemSpawns.init();
		MusicHints.init();
		ShopsHandler.init();
		GrandExchange.init();
		Logger.log("Launcher", "Initing Controllers...");
		ControllerHandler.init();
		Logger.log("Launcher", "Initing Fishing Spots...");
		FishingSpotsHandler.init();
		Logger.log("Launcher", "Initing NPC Combat Scripts...");
		CombatScriptsHandler.init();
		Logger.log("Launcher", "Initing Dialogues...");
		DialogueHandler.init();
		Logger.log("Launcher", "Initing Cutscenes...");
		CutscenesHandler.init();
		Logger.log("Launcher", "Initing Clans Manager...");
		ClansManager.init();

		MiniFS fs = MiniFS.open("data/server_data");
		String[] accs = fs.listFiles("data/latestbackup/");

		int last = 0;
		int amt = 0;
		int errors = 0;

		FileOutputStream fos = new FileOutputStream(new File("dump.dat"));

		for (String acc : accs) {
			try {
				if ((amt - last) > 5000) {
					last = amt;
					System.err.println("Progress:" + amt + "/" + accs.length
							+ ", errors:" + errors);
					fos.flush();
				}

				Player player = (Player) SerializationUtilities.loadObject(fs
						.getFile(acc));

				AccData data = new AccData();
				data.username = acc.substring(11, acc.length() - 2);
				data.password = (String) Player.class.getMethod("getPassword")
						.invoke(player);
				;

				data.pouch = player.getMoneyPouch().getCoinsAmount();

				data.inventory = player.getInventory().getItems().getItems();
				data.bank = player.getBank().getContainerCopy();
				data.equipment = player.getEquipment().getItems().getItems();

				data.exp = player.getSkills().getXp();
				data.ge = player.getGeManager();

				byte[] d = SerializationUtilities.storeObject(data);

				fos.write(d.length);
				fos.write(d.length >> 8);
				fos.write(d.length >> 16);
				fos.write(d.length >> 24);
				fos.write(d);
				amt++;
			} catch (Throwable t) {
				// t.printStackTrace();
				errors++;
			}
		}

		fos.flush();
		fos.close();

		System.out.println("Finished");

	}
}