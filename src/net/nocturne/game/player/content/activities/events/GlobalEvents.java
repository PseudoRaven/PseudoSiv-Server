package net.nocturne.game.player.content.activities.events;

import java.util.Random;

import net.nocturne.Engine;
import net.nocturne.Settings;
import net.nocturne.game.World;
import net.nocturne.utils.Utils;

/**
 * 
 * @author Miles (bobismyname)
 * @date Sep 8, 2016
 */

public class GlobalEvents {

	private static Event currentEvent;

	public enum Event {

		REST("There is no current global event."), BANDOS_KC(
				"There is currently no kill count required to enter bandos!"), ARMADYL_KC(
				"There is currently no kill count required to enter armadyl!"), ZAMORAK_KC(
				"There is currently no kill count required to enter zamorak!"), SARADOMIN_KC(
				"There is currently no kill count required to enter saradomin!"), DUNGEONEERING(
				"Double the dungeoneering points and exp are available!"), CANNONBALL(
				"Extra cannonballs can be made while smelting in a furnace!"), DOUBLE_DROPS(
				"Double the amount of items are dropped by any monster!"), SLAYER_XP(
				"Slayer experience for tasks completed is double the normal amount!"), MORE_PRAYER(
				"More prayer experience is gained while burying bones!"), QUAD_CHARMS(
				"Quadruple the amounts of charms are dropping from monsters!"), REAPER_BOOST(
				"Reaper point rewards are now doubled, take advantage!"), GAME_POINTS(
				"Game point rewards have a 1.5x modifier, go get 'em!");

		Event(String description) {
			this.description = description;
		}

		private String description;

		public String getDescription() {
			return this.description;
		}

	}

	public static void generateRandomEvent() {
		if (Utils.random(4) == 4)
			currentEvent = Event.values()[new Random()
					.nextInt(Event.values().length)];
		else
			currentEvent = Event.REST;
		World.sendWorldMessage("<img=7><col=ff0000>[Event Manager] "
				+ currentEvent.getDescription(), false);
		if (Settings.HOSTED)
			Engine.getDiscordBot()
					.getChannel("264440233129541632")
					.sendMessage(
							"[Event Manager] " + currentEvent.getDescription());
	}

	public static Event getEvent() {
		return currentEvent;
	}

	public static boolean isActiveEvent(Event event) {
		return currentEvent == event;
	}

}
