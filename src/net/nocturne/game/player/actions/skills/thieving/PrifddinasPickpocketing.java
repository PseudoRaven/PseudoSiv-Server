package net.nocturne.game.player.actions.skills.thieving;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class PrifddinasPickpocketing extends Action {

	private NPC npc;

	public PrifddinasPickpocketing(NPC npc) {
		this.npc = npc;
	}

	@Override
	public boolean start(Player player) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			player.stopAll();
			return false;
		}
		switch (npc.getId()) {
		case 20312: // Ithell
		case 20313:
		case 20314:
		case 20315:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 92) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 92 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20316: // Amlodd
		case 20317:
		case 20318:
		case 20319:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 94) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 94 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20320: // Hefin
		case 20321:
		case 20322:
		case 20323:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 96) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 96 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20324: // Meilyr
		case 20325:
		case 20326:
		case 20327:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 98) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 98 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20113: // Iorwerth
		case 20114:
		case 20115:
		case 20116:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 91) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 91 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20125: // Trahaearn
		case 20126:
		case 20127:
		case 20128:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 95) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 95 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20121: // Cadarn
		case 20122:
		case 20123:
		case 20124:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 93) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 93 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20117: // Crwys
		case 20118:
		case 20119:
		case 20120:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 97) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 97 to pickpocket from this npc.");
				return false;
			}
			player.setNextAnimation(new Animation(24887));
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(24887));
		if (player.getX() == npc.getX() && player.getY() == npc.getY())
			player.addWalkSteps(npc.getX() - 1, npc.getY(), -1, true);
		player.addWalkSteps(npc.getX(), npc.getY() - 1, -1, true);
		player.faceEntity(npc);
		if (checkAll(player)) {
			if (Utils.random(400) == 0) {
				player.stopAll();
			}
			if (Utils.random(player.getSkills().getLevel(Skills.THIEVING) * 10) == 0) {
				player.stopAll();
				npc.faceEntity(player);
				player.setNextAnimation(new Animation(424));
				player.setNextGraphics(new Graphics(80, 5, 60));
				player.getPackets().sendGameMessage("You have raised their suspicions.", true);
				player.applyHit(new Hit(player, 300, HitLook.REGULAR_DAMAGE));
				player.lock(3);
			}
			return true;
		}
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		player.getSkills().addXp(Skills.THIEVING, xpAmount());
		addItems(player);
		player.getCompCapeManager().increaseRequirement(Requirement.PICK_POCKETING, 1);
		player.stopAll();
		return -1;
	}

	private void addItems(Player player) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			player.stopAll();
			return;
		}
		switch (Utils.random(16)) {
		case 0:
			giveItem( player, ItemIdentifiers.SNAPE_GRASS_NOTED );
			break;
		case 1:
			giveItem( player, ItemIdentifiers.SHARK_NOTED );
			break;
		case 2:
			giveItem( player, ItemIdentifiers.COAL_NOTED );
			break;
		case 3:
			giveItem( player, ItemIdentifiers.GOLD_ORE_NOTED );
			break;
		case 4:
			giveItem( player, ItemIdentifiers.OAK_PLANK_NOTED );
			break;
		case 5:
			giveItem( player, ItemIdentifiers.BOWSTRING_NOTED );
			break;
		case 7:
			giveItem(player, ItemIdentifiers.DRAGON_SCALE_DUST_NOTED);
			break;
		case 9:
			giveItem(player, ItemIdentifiers.SUPER_ATTACK_1_NOTED);
			break;
		case 10:
			giveItem(player, ItemIdentifiers.SUPER_STRENGTH_1_NOTED);
			break;
		case 11:
			giveItem(player, ItemIdentifiers.SUPER_DEFENCE_1_NOTED);
			break;
		case 6:
		case 8:
		case 12:
		case 13:
		case 14:
		case 15:
			break;
		}
	}

	private int xpAmount() {
		switch (npc.getId()) {
		case 20312: // Ithell
		case 20313:
		case 20314:
		case 20315:
			return 130;

		case 20316: // Amlodd
		case 20317:
		case 20318:
		case 20319:
			return 140;

		case 20320: // Hefin
		case 20321:
		case 20322:
		case 20323:
			return 150;

		case 20324: // Meilyr
		case 20325:
		case 20326:
		case 20327:
			return 170;

		case 20113: // Iorwerth
		case 20114:
		case 20115:
		case 20116:
			return 125;

		case 20125: // Trahaearn
		case 20126:
		case 20127:
		case 20128:
			return 145;

		case 20121: // Cadarn
		case 20122:
		case 20123:
		case 20124:
			return 135;

		case 20117: // Crwys
		case 20118:
		case 20119:
		case 20120:
			return 155;
		}
		return 10;
	}

	private boolean checkAll(Player player) {
		switch (npc.getId()) {
		case 20312: // Ithell
		case 20313:
		case 20314:
		case 20315:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 92) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 92 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20316: // Amlodd
		case 20317:
		case 20318:
		case 20319:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 94) {
				player.getPackets().sendGameMessage("You need to have a thieving level of at least 94 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20320: // Hefin
		case 20321:
		case 20322:
		case 20323:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 96) {
				player.getPackets()
						.sendGameMessage(
								"You need to have a thieving level of at least 96 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20324: // Meilyr
		case 20325:
		case 20326:
		case 20327:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 98) {
				player.getPackets()
						.sendGameMessage(
								"You need to have a thieving level of at least 98 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20113: // Iorwerth
		case 20114:
		case 20115:
		case 20116:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 91) {
				player.getPackets()
						.sendGameMessage(
								"You need to have a thieving level of at least 91 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20125: // Trahaearn
		case 20126:
		case 20127:
		case 20128:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 95) {
				player.getPackets()
						.sendGameMessage(
								"You need to have a thieving level of at least 95 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20121: // Cadarn
		case 20122:
		case 20123:
		case 20124:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 93) {
				player.getPackets()
						.sendGameMessage(
								"You need to have a thieving level of at least 93 to pickpocket from this npc.");
				return false;
			}
			return true;
		case 20117: // Crwys
		case 20118:
		case 20119:
		case 20120:
			if (player.getSkills().getLevelForXp(Skills.THIEVING) < 97) {
				player.getPackets()
						.sendGameMessage(
								"You need to have a thieving level of at least 97 to pickpocket from this npc.");
				return false;
			}
			return true;
		}
		return false;
	}


	private void giveItem(Player player , int item)
	{
		player.getInventory().addItem(item, 1);
	}
	@Override
	public void stop(Player player) {
		npc.setNextFaceEntity(null);
		player.getEmotesManager().setNextEmoteEnd(2400);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextAnimation(new Animation(-1));
				player.getAppearence().setRenderEmote(-1);
			}
		}, 3);
	}
}