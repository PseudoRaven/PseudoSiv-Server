package net.nocturne.game.player.content.activities.clans;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import net.nocturne.game.player.Player;
import net.nocturne.utils.Logger;
import net.nocturne.utils.SerializableFilesManager;

public final class ClanRank implements Serializable {

	public static void checkRank(Clan clan, int option) {
		for (int i = 0; i < ranks[option].length; i++) {
			final ClanRank rank = ranks[option][i];
			if (rank == null)
				break;
			if (rank.clanName.equalsIgnoreCase(clan.getClanName())) {
				ranks[option][i] = new ClanRank(clan);
				sort();
				return;
			}
		}
		for (int i = 0; i < ranks[option].length; i++) {
			final ClanRank rank = ranks[option][i];
			if (rank == null) {
				ranks[option][i] = new ClanRank(clan);
				sort();
				return;
			}
		}
		for (int i = 0; i < ranks[option].length; i++) {
			if (option == 0) {
				if (ranks[0][i].gatheredResources < clan.getGatheredResources()) {
					ranks[0][i] = new ClanRank(clan);
					sort();
					return;
				}
			} else if (option == 1) {
				if (ranks[1][i].monsterKills < clan.getMonsterKills()) {
					ranks[1][i] = new ClanRank(clan);
					sort();
					return;
				}
			} else if (option == 2) {
				if (ranks[2][i].playerKills < clan.getPlayerKills()) {
					ranks[2][i] = new ClanRank(clan);
					sort();
					return;
				}
			}
		}
	}

	public static void init() {
		final File file = new File(PATH);
		if (file.exists())
			try {
				ranks = (ClanRank[][]) SerializableFilesManager
						.loadSerializedFile(file);
				return;
			} catch (final Throwable e) {
				Logger.handle(e);
			}
		ranks = new ClanRank[3][300];
	}

	public static void save() {
		try {
			SerializableFilesManager.storeSerializableClass(ranks, new File(
					PATH));
		} catch (final Throwable e) {
			Logger.handle(e);
		}
	}

	public static void showRanks(Player player, int option) {
		String title = "Clan Highscores";
		if (option == 0)
			title = "Clan Gathered Resources Highscores";
		else if (option == 1)
			title = "Clan Monster Kills (Level 100+) Highscores";
		else if (option == 2)
			title = "Clan Player Kills Highscores";
		for (int i = 0; i < 50; i++) {
			if (ranks[option][i] == null) {
				player.getPackets().sendIComponentText(868, i + 10, "");
				continue;
			}
			String text;
			String rankText = "";
			if (i >= 0 && i <= 2)
				text = "<col=ff9900>";
			else if (i <= 9)
				text = "<col=ff0000>";
			else if (i <= 49)
				text = "<col=38610B>";
			else
				text = "<col=000000>";
			if (option == 0) {
				rankText += text + "Top " + (i + 1) + " - "
						+ ranks[option][i].clanName + " - Resources: "
						+ ranks[option][i].gatheredResources + "<br>";
			} else if (option == 1) {
				rankText += text + "Top " + (i + 1) + " - "
						+ ranks[option][i].clanName + " - Monster Kills: "
						+ ranks[option][i].monsterKills + "<br>";
			} else if (option == 2) {
				rankText += text + "Top " + (i + 1) + " - "
						+ ranks[option][i].clanName + " - Player Kills: "
						+ ranks[option][i].playerKills + "<br>";
			}
			player.getPackets().sendIComponentText(868, i + 10, rankText);
		}
		player.getPackets().sendIComponentText(868, 1, title);
		player.getInterfaceManager().sendCentralInterface(868);
	}

	public static void sort() {
		Arrays.sort(ranks[0], new Comparator<ClanRank>() {
			@Override
			public int compare(ClanRank arg0, ClanRank arg1) {
				if (arg0 == null)
					return 1;
				if (arg1 == null)
					return -1;
				if (arg0.gatheredResources < arg1.gatheredResources)
					return 1;
				else if (arg0.gatheredResources > arg1.gatheredResources)
					return -1;
				else
					return 0;
			}

		});
		Arrays.sort(ranks[1], new Comparator<ClanRank>() {
			@Override
			public int compare(ClanRank arg0, ClanRank arg1) {
				if (arg0 == null)
					return 1;
				if (arg1 == null)
					return -1;
				if (arg0.monsterKills < arg1.monsterKills)
					return 1;
				else if (arg0.monsterKills > arg1.monsterKills)
					return -1;
				else
					return 0;
			}

		});
		Arrays.sort(ranks[2], new Comparator<ClanRank>() {
			@Override
			public int compare(ClanRank arg0, ClanRank arg1) {
				if (arg0 == null)
					return 1;
				if (arg1 == null)
					return -1;
				if (arg0.playerKills < arg1.playerKills)
					return 1;
				else if (arg0.playerKills > arg1.playerKills)
					return -1;
				else
					return 0;
			}

		});
	}

	private static final long serialVersionUID = -2036605876129556975L;

	private final String clanName;

	private final int gatheredResources, monsterKills, playerKills;

	private static ClanRank[][] ranks;

	private static final String PATH = "./data/clanRanks.ser";

	public ClanRank(Clan clan) {
		this.clanName = clan.getClanName();
		this.gatheredResources = clan.getGatheredResources();
		this.monsterKills = clan.getMonsterKills();
		this.playerKills = clan.getPlayerKills();
	}

}
