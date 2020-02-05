package net.nocturne.utils;

import net.nocturne.Settings;

public enum News {

	/*
	 * NEWS1("876 Revision",
	 * "We have now upgraded to the latest revision, enjoy our new features! More to come soon!"
	 * , Settings.GAME_UPDATES, "1/7/17", true), NEWS2("Home Sweet Home",
	 * "Enjoy the new home, packed with everything you need to get started!",
	 * Settings.GAME_UPDATES, "1/9/17", false), NEWS3("Alpha Update v1",
	 * "The alpha has been updated with every update between the 7th and the 10th of January!"
	 * , Settings.GAME_UPDATES, "1/11/17", false), NEWS4("Alpha Update v2",
	 * "Prayers, banking, and more: every update between the 11th and 13th of January!"
	 * , Settings.GAME_UPDATES, "1/13/17", false), NEWS5("Alpha Update v3",
	 * "Many new features, enjoy our updates from January 14th to January 18th!"
	 * , Settings.GAME_UPDATES, "1/18/17", false), NEWS6("Exp Rates",
	 * "The current rates will be FLAT in order to adjust skilling rates.",
	 * Settings.EVENTS, "1/18/17", true), NEWS7("Alpha Update v4",
	 * "Did someone say action bars? Major work has been done on the ability system!"
	 * , Settings.GAME_UPDATES, "1/19/17", false), NEWS8("Perfect Reality",
	 * "We're currently aiming to get 99% or more of the world accessible and interactable!"
	 * , Settings.FUTURE_UPDATES, "1/22/2017", false), NEWS9("Website Online",
	 * "Chance and Danny have finished our new website, check it out!",
	 * Settings.WEBSITE, "1/23/2017", true), NEWS10("Alpha Update v5",
	 * "Nearing the final days of the alpha, many improvements have arrived, read the forums!"
	 * , Settings.GAME_UPDATES, "1/23/2017", false), NEWS11("Alpha Update v6",
	 * "Virtual leveling, combat improvements, and even more!",
	 * Settings.GAME_UPDATES, "1/25/2017", false), NEWS12("Beta is LIVE",
	 * "Welcome to Nocturne's official beta, the last phase before the final reset on Feb 3rd!"
	 * , Settings.EVENTS, "1/27/2017", true), NEWS13("Nocturne is LIVE!",
	 * "The long awaited official release has now come, enjoy the game!",
	 * Settings.EVENTS, "2/3/2017", true), NEWS14("Major Fixes",
	 * "Tonight we have fixed a lot of the major issues experience today!",
	 * Settings.GAME_UPDATES, "2/3/2017", true), NEWS15("Wilderness Revamp",
	 * "There are a lot of new wilderness activities to check out, read it up on the forums!"
	 * , Settings.GAME_UPDATES, "2/4/2017", true), NEWS16("Edimmu Dungeon",
	 * "There is a new dungeon in prif, check it out for new activities!",
	 * Settings.GAME_UPDATES, "2/5/2017", false), NEWS17("Agility Update",
	 * "Courses, shortcuts, minigames, and more have been updated!",
	 * Settings.GAME_UPDATES, "2/6/2017", false), NEWS18("Rune Dragons",
	 * "Rune dragons have now been added! check the slayer teleport tab! Happy hunting!"
	 * , Settings.GAME_UPDATES, "2/7/2017", false), NEWS19("Donator Revamp",
	 * "Many new donator benefits for the higher ranks, enjoy!",
	 * Settings.GAME_UPDATES, "2/11/2017", false), NEWS20("Retro Capes",
	 * "Claim your retro skillcape from retro ronnie at home!",
	 * Settings.GAME_UPDATES, "2/12/2017", false),
	 */

	NEWS21(
			"Maintenance",
			"Server will be down and relaunched after bugs are patched, read the forums.",
			Settings.TECHNICAL, "2/13/2017", true),

	NEWS22(
			"Relaunch",
			"After nearly two weeks of hard work, we are ready to make a come back! Welcome back everyone :)",
			Settings.EVENTS, "2/24/2017", true),

	NEWS23(
			"Bosses Rework",
			"For the next week our updates will be centered around PvM, bosses, and more. Stay tuned!",
			Settings.BEHIND_THE_SCENES, "2/27/2017", true),

	NEWS24(
			"Combat Rework v1",
			"Part one of our up coming combat updates including damage caps, potion buffs, and more!",
			Settings.GAME_UPDATES, "3/4/2017", false),

	NEWS25(
			"Combat Rework v2",
			"Combination potions, decanting, improved drop rates, and more! A few more days till v3.",
			Settings.GAME_UPDATES, "3/5/2017", false),

	NEWS26(
			"Combat Rework v3",
			"Better weapons, monastery of ascencion, game points, and more! Combat is turning out lovely.",
			Settings.GAME_UPDATES, "3/7/2017", false),

	NEWS27(
			"Completionist Cape",
			"Lot's of the requirements have been redone, check them out with ::compcape!",
			Settings.GAME_UPDATES, "3/9/2017", false),

	NEWS28(
			"New Developer",
			"I would like to welcome Pax to the development team! Great things to come soon to Nocturne.",
			Settings.EVENTS, "3/11/2017", true),

	NEWS29(
			"Major Improvements",
			"Lot's of important bug fixes have been done the past few days, read about it on the forums.",
			Settings.GAME_UPDATES, "3/13/2017", false),

	NEWS30(
			"New Supports",
			"I would like to congratulate Bob and Party Blues on the promotion! We are still hiring.",
			Settings.EVENTS, "3/13/2017", true),

	NEWS31(
			"Rise of the Six",
			"A new minigame and many amazing fixes today, more information on the website!",
			Settings.GAME_UPDATES, "3/14/2017", false),

	NEWS32(
			"Null Improvement",
			"A major issue with nulls have been patched, a lot more to come this week once Danny is better!",
			Settings.TECHNICAL, "3/19/2017", true),

	NEWS33(
			"Skiller Tasks",
			"Reworked the skiller task system and improved functionality when completing tasks!",
			Settings.GAME_UPDATES, "3/20/2017", false);

	private String title, message, date;
	private int category;
	private boolean pinned;

	News(String title, String message, int category, String date, boolean pinned) {
		this.title = title;
		this.message = message;
		this.category = category;
		this.date = date;
		this.pinned = pinned;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public int getCategory() {
		return category;
	}

	public String getDate() {
		return date;
	}

	public boolean isPinned() {
		return pinned;
	}
}