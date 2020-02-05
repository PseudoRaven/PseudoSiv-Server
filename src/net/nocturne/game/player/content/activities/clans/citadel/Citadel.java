package net.nocturne.game.player.content.activities.clans.citadel;

import java.util.ArrayList;

import net.nocturne.game.player.Player;

/**
 * 
 * @author Frostbite<Abstract>
 * @contact<skype;frostbitersps><email;frostbitersps@gmail.com>
 */

public class Citadel {

	public ArrayList<Player> members = new ArrayList<Player>();
	public ArrayList<Player> guests = new ArrayList<Player>();

	public ArrayList<Player> getMembers() {
		return members;
	}

	public ArrayList<Player> getGuests() {
		return guests;
	}
}