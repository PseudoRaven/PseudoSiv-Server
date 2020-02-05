package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.dialogues.Dialogue;

public final class LevelUp extends Dialogue {

	@Override
	public void start() {
		int skill = (Integer) parameters[0];
		final int level = player.getSkills().getLevelForXp(skill);
		player.getSkills();
		final int xp = Skills.getXPForLevel(skill);
		player.getTemporaryAttributtes().put("leveledUp", skill);
		player.getTemporaryAttributtes().put("leveledUp[" + skill + "]",
				Boolean.TRUE);
		player.setNextGraphics(new Graphics(199));
		if (level == 99 || level == 120)
			player.setNextGraphics(new Graphics(1765));
		final String name = Skills.SKILL_NAME[skill];
		player.getPackets().sendGameMessage(
				"You've just advanced a" + (name.startsWith("A") ? "n" : "")
						+ " " + name + " level! You are now level " + level
						+ ".");
		player.getPackets().sendConfigByFile(4757, getIconValue(skill));
		switchFlash(player, skill, true);
	}

	public static int getIconValue(int skill) {
		if (skill == Skills.ATTACK)
			return 1;
		if (skill == Skills.STRENGTH)
			return 2;
		if (skill == Skills.RANGED)
			return 3;
		if (skill == Skills.MAGIC)
			return 4;
		if (skill == Skills.DEFENCE)
			return 5;
		if (skill == Skills.HITPOINTS)
			return 6;
		if (skill == Skills.PRAYER)
			return 7;
		if (skill == Skills.AGILITY)
			return 8;
		if (skill == Skills.HERBLORE)
			return 9;
		if (skill == Skills.THIEVING)
			return 10;
		if (skill == Skills.CRAFTING)
			return 11;
		if (skill == Skills.RUNECRAFTING)
			return 12;
		if (skill == Skills.MINING)
			return 13;
		if (skill == Skills.SMITHING)
			return 14;
		if (skill == Skills.FISHING)
			return 15;
		if (skill == Skills.COOKING)
			return 16;
		if (skill == Skills.FIREMAKING)
			return 17;
		if (skill == Skills.WOODCUTTING)
			return 18;
		if (skill == Skills.FLETCHING)
			return 19;
		if (skill == Skills.SLAYER)
			return 20;
		if (skill == Skills.FARMING)
			return 21;
		if (skill == Skills.CONSTRUCTION)
			return 22;
		if (skill == Skills.SLAYER)
			return 23;
		if (skill == Skills.SUMMONING)
			return 24;
		if (skill == Skills.DUNGEONEERING)
			return 25;
		if (skill == Skills.DIVINATION)
			return 26;
		if (skill == Skills.INVENTION)
			return 27;
		return -1;
	}

	public static void switchFlash(Player player, int skill, boolean on) {
		int id;
		if (skill == Skills.ATTACK)
			id = 4732;
		else if (skill == Skills.STRENGTH)
			id = 4733;
		else if (skill == Skills.DEFENCE)
			id = 4734;
		else if (skill == Skills.RANGED)
			id = 4735;
		else if (skill == Skills.PRAYER)
			id = 4736;
		else if (skill == Skills.MAGIC)
			id = 4737;
		else if (skill == Skills.HITPOINTS)
			id = 4738;
		else if (skill == Skills.AGILITY)
			id = 4739;
		else if (skill == Skills.HERBLORE)
			id = 4740;
		else if (skill == Skills.THIEVING)
			id = 4741;
		else if (skill == Skills.CRAFTING)
			id = 4742;
		else if (skill == Skills.FLETCHING)
			id = 4743;
		else if (skill == Skills.MINING)
			id = 4744;
		else if (skill == Skills.SMITHING)
			id = 4745;
		else if (skill == Skills.FISHING)
			id = 4746;
		else if (skill == Skills.COOKING)
			id = 4747;
		else if (skill == Skills.FIREMAKING)
			id = 4748;
		else if (skill == Skills.WOODCUTTING)
			id = 4749;
		else if (skill == Skills.RUNECRAFTING)
			id = 4750;
		else if (skill == Skills.SLAYER)
			id = 4751;
		else if (skill == Skills.FARMING)
			id = 4752;
		else if (skill == Skills.CONSTRUCTION)
			id = 4753;
		else if (skill == Skills.HUNTER)
			id = 4754;
		else if (skill == Skills.SUMMONING)
			id = 4755;
		else if (skill == Skills.DUNGEONEERING)
			id = 4756;
		else if (skill == Skills.DIVINATION)
			id = 4757;
		else if (skill == Skills.INVENTION)
			id = 4757; //same as div for now idk the invention one
		else
			id = 7756;
		player.getVarsManager().sendVarBit(id, on ? 1 : 0);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();
	}

	@Override
	public void finish() {

	}
}