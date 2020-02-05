package net.nocturne.game.player.events;

import java.util.HashMap;

import net.nocturne.utils.Logger;

class EventHandler {

	private static final HashMap<Object, Class<? extends Event>> handledEvents = new HashMap<>();

	public static void reload() {
		handledEvents.clear();
	}

	public static Event getEvent(Object key) {
		if (key instanceof Event)
			return (Event) key;
		Class<? extends Event> classC = handledEvents.get(key);
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