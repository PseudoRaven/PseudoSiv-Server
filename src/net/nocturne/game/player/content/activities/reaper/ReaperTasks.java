package net.nocturne.game.player.content.activities.reaper;

public enum ReaperTasks {


	//BARROWS(2025,60,7000,7,"Barrows Brothers",
	//		"Any of the 6 (7 after Ritual of the Mahjarrat) barrows brothers can be slain for this task. The assigned number requires 4-8 chests to be opened, not brothers killed."),
    
	//VORAGO(new String[]{"Vorago"},120,20000,25,"Vorago",
	//		"A complete task of hard-mode Vorago awards 25k xp and 25 points. A group of 5 or more players is recommended for this task."),
	 
	ARAXXOR(new String[]{"Araxxor"}, 120, 20000, 20, "Araxxor",
				"I have nothing to tell you about this creature.", false),
	
	CHAOS_ELEMENTAL(new String[]{"Chaos Elemental"}, 60, 10000, 10, "Chaos Elemental",
			"Be aware the Chaos Elemental is located inside the Wilderness.", false),

	NEX(new String[]{"Nex"}, 110, 17000, 17, "Nex", "A group is recommended for this task.", true),

	CORPOREAL_BEAST(new String[]{"Corporeal Beast"}, 70, 12000, 12, "Corporeal Beast",
			"I have nothing to tell you about this creature.", false),

	KALPHITE_KING(new String[]{"Kalphite King"}, 100, 15000, 15, "Kalphite King",
			"A group of two or more players is required for this task.", true),

	KALPHITE_QUEEN(new String[]{"Kalphite Queen", "Exiled Kalphite Queen"}, 60, 10000, 10, "Kalphite Queen",
			"Kalphite Queen can be completed to fulfil this task but offers no additional rewards.", false),

	KBD(new String[]{"King Black Dragon"}, 50, 7000, 7, "King Black Dragon",
			"I have nothing to tell you about this creature.", false),

	QBD(new String[]{"Queen Black Dragon"}, 80, 10000, 10, "Queen Black Dragon",
			"I have nothing to tell you about this creature.", false),

	SARADOMIN(new String[]{"Commander Zilyana"}, 80, 12000, 12, "Commander Zilyana",
			"A complete task of hard-mode Zilyana awards 15k xp and 15 points.", false),

	ZAMORAK(new String[]{"K'ril Tsutsaroth"}, 80, 12000, 12, "K'ril Tsutsaroth",
			"A complete task of hard-mode K'ril awards 15k xp and 15 points.", false),

	ARMADYL(new String[]{"Kree'arra"}, 80, 12000, 12, "Kree'arra",
			"A complete task of hard-mode Kree'arra awards 15k xp and 15 points.", false),

	BANDOS(new String[]{"General Graardor"}, 80, 12000, 12, "General Graardor",
			"A complete task of hard-mode Graardor awards 15k xp and 15 points.", false),

	DAGANNOTH_KINGS(new String[]{"Dagannoth Supreme", "Dagannoth Rex", "Dagannoth Prime"}, 70, 10000, 10, "Dagannoth Kings",
			"Any of the three dagannoth kings can be slain for this task.", false),

	GIANT_MOLE(new String[]{"Giant Mole"}, 50, 7000, 7, "Giant Mole",
			"A complete task of hard-mode Giant mole awards 10,000xp and 10 points.", false),

	LEGIONES(new String[]{"Legio Primus", "Legio Secundus", "Legio Tertius", "Legio Quartus", "Legio Quintus", "Legio Sextus"}, 100, 15000, 15, "Legiones",
	"Any of the six legiones can be slain for this task.", false),

	JAD(new String[]{"TzTok-Jad", "TokHaar-Jad"}, 10, 10000 ,10, "TzTok-Jad",
			"TokHaar-Jad encountered during the TokHaar Fight Kiln count towards this task and are recommended.", false);

	private String[] npcName;
	private int requirement;
	private int xp;
	private int points;
	private String name;
	private String hints;
	private boolean group;

	ReaperTasks(String[] npcName, int requirement, int xp, int points, String name,String hints, boolean group) {
		this.npcName = npcName;
		this.requirement = requirement;
		this.name = name;
		this.hints = hints;
		this.group = group;
	}

	public String[] getNPCName() {
		return npcName;
	}

	public int getRequirement() {
		return requirement;
	}

	public int getXP() {
		return xp;
	}

	public int getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

	public String getHints() {
		return hints;
	}
	
	public boolean isGroup() {
		return group;
	}
}