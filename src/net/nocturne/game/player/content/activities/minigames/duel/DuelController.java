package net.nocturne.game.player.content.activities.minigames.duel;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.Controller;

public class DuelController extends Controller {

	@Override
	public void start() {
		sendInterfaces();
		player.getPackets().sendPlayerOption("Challenge", 1, true);
		player.getAppearence().generateAppearenceData();
		moved();
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void forceClose() {
		remove();
	}

	@Override
	public void magicTeleported(int type) {
		removeController();
		remove();
	}

	@Override
	public void moved() {
		if (!isAtDuelArena(player)) {
			removeController();
			remove();
		}
	}

	@Override
	public boolean canPlayerOption1(final Player target) {
		player.stopAll();
		if (target.getInterfaceManager().containsScreenInterface()
				|| target.isLocked()) {
			player.getPackets().sendGameMessage("The other player is busy.");
			return false;
		}
		if (player.isBeginningAccount() || target.isBeginningAccount()) {
			player.getPackets()
					.sendGameMessage(
							"Starter accounts cannot duel or be dueled until after at least one hour of playing time.");
			return false;
		}
		if (!player.getBank().hasVerified(10))
			return false;
		if (target.getTemporaryAttributtes().get("DuelChallenged") == player) {
			player.getControllerManager().removeControllerWithoutCheck();
			target.getControllerManager().removeControllerWithoutCheck();
			target.getTemporaryAttributtes().remove("DuelChallenged");
			player.setLastDuelRules(new DuelRules(player, target));
			target.setLastDuelRules(new DuelRules(target, player));
			player.getControllerManager().startController("DuelArena", target,
					target.getTemporaryAttributtes().get("DuelFriendly"));
			target.getControllerManager().startController("DuelArena", player,
					target.getTemporaryAttributtes().remove("DuelFriendly"));
			sendInterfaces();
			player.getPackets().sendPlayerOption("Challenge", 1, true);
			return false;
		}
		player.getTemporaryAttributtes().put("DuelTarget", target);
		player.getPackets().sendPlayerHeadOnIComponent(1369, 8);
		player.getPackets().sendIComponentText(1369, 9, player.getUsername());
		player.getPackets().sendIComponentText(1369, 10,
				"Combat level: " + player.getSkills().getCombatLevel());
		player.getPackets().sendOtherPlayerOnIComponent(1369, 15, target);
		player.getPackets().sendIComponentText(1369, 16, target.getUsername());
		player.getPackets().sendIComponentText(1369, 17,
				"Combat level: " + target.getSkills().getCombatLevel());
		player.getInterfaceManager().sendCentralInterface(1369);
		player.getTemporaryAttributtes().put("WillDuelFriendly", true);
		player.getVarsManager().sendVar(283, 67108864);
		return false;
	}

	public static void challenge(Player player) {
		player.closeInterfaces();
		Boolean friendly = (Boolean) player.getTemporaryAttributtes().remove(
				"WillDuelFriendly");
		if (friendly == null)
			return;
		Player target = (Player) player.getTemporaryAttributtes().remove(
				"DuelTarget");
		if (target == null
				|| target.hasFinished()
				|| !target.withinDistance(player, 14)
				|| !(target.getControllerManager().getController() instanceof DuelController)) {
			player.getPackets().sendGameMessage(
					"Unable to find "
							+ (target == null ? "your target" : target
									.getDisplayName()));
			return;
		}
		if (player.isAnIronMan() || target.isAnIronMan()) {
			player.getPackets().sendGameMessage(
					"Iron man accounts are not allowed to stake.");
			return;
		}
		player.getTemporaryAttributtes().put("DuelChallenged", target);
		player.getTemporaryAttributtes().put("DuelFriendly", friendly);
		player.getPackets().sendGameMessage(
				"Sending " + target.getDisplayName() + " a request...");
		target.getPackets().sendDuelChallengeRequestMessage(player, friendly);
	}

	public void remove() {
		player.getInterfaceManager().removeMinigameInterface();
		player.getPackets().sendPlayerOption("null", 1, false);
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendMinigameInterface(1362);
	}

	public static boolean isAtDuelArena(Player player) {
		return player.withinArea(3327, 3201, 3393, 3262);

	}
}