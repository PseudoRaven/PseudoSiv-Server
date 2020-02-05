package net.nocturne.game.player.controllers;

import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;

public class Kalaboss extends Controller {

	private boolean showingOption;

	@Override
	public void start() {
		setInviteOption(true);
	}

	@Override
	public boolean canPlayerOption1(Player target) {
		player.setNextFaceWorldTile(target);
		player.getDungManager().invite(target.getDisplayName());
		return false;
	}

	@Override
	public boolean login() {
		moved();
		DungeonManager.checkRejoin(player);
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		setInviteOption(false);
		player.getDungManager().leaveParty();
		removeController();
	}

	@Override
	public boolean sendDeath() {
		setInviteOption(false);
		player.getDungManager().leaveParty();
		removeController();
		return true;
	}

	@Override
	public boolean logout() {
		return false; // so doesn't remove script
	}

	@Override
	public void forceClose() {
		setInviteOption(false);
	}

	/**
	 * return process normally
	 */
	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == 9707)
			player.getDungManager().leaveParty();
		return true;
	}

	/**
	 * return process normally
	 */
	@Override
	public boolean processNPCClick2(NPC npc) {
		if (npc.getId() == 9707)
			player.getDungManager().leaveParty();
		return true;
	}

	@Override
	public void moved() {
		if (player.getDungManager().isInside())
			return;
		if ((player.getX() == 3385 || player.getX() == 3384)
				&& player.getY() == 3615) {
			setInviteOption(false);
			player.getDungManager().leaveParty();
			removeController();
			player.getControllerManager().startController("Wilderness");
		} else {
			if (!isAtKalaboss(player)) {
				setInviteOption(false);
				player.getDungManager().leaveParty();
				removeController();
			} else
				setInviteOption(true);
		}
	}

	public static boolean isAtKalaboss(WorldTile tile) {
		return tile.getX() >= 3385 && tile.getX() <= 3513
				&& tile.getY() >= 3605 && tile.getY() <= 3794;
	}

	private void setInviteOption(boolean show) {
		if (show == showingOption)
			return;
		showingOption = show;
		player.getPackets()
				.sendPlayerOption(show ? "Invite" : "null", 1, false);
	}
}
