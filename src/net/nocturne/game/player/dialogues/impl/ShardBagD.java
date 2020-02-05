package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.Skills;
import net.nocturne.game.player.dialogues.Dialogue;

public class ShardBagD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("withdraw shards for which cape?", "Gatherer's",
				"Combatant", "Artisan's", "Support");
		stage = 1;
	}

	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			switch (componentId) {
			case OPTION_1:
				int bit1 = player.getVarsManager().getBitValue(26040);
				int bit2 = player.getVarsManager().getBitValue(26041);
				int bit3 = player.getVarsManager().getBitValue(26039);
				int bit4 = player.getVarsManager().getBitValue(26038);
				int bit5 = player.getVarsManager().getBitValue(26063);
				int bit6 = player.getVarsManager().getBitValue(26042);
				if (bit1 == 1) {
					player.getInventory().addItem(32063, 1);
					player.expertSkillShards[Skills.FISHING] = false;
					player.getVarsManager().sendVarBit(26040, 0);
					player.refreshShardConfigs();
				}
				if (bit2 == 1) {
					player.getInventory().addItem(32065, 1);
					player.expertSkillShards[Skills.MINING] = false;
					player.getVarsManager().sendVarBit(26041, 0);
					player.refreshShardConfigs();
				}
				if (bit3 == 1) {
					player.getInventory().addItem(32068, 1);
					player.expertSkillShards[Skills.CONSTRUCTION] = false;
					player.getVarsManager().sendVarBit(26039, 0);
					player.refreshShardConfigs();
				}
				if (bit4 == 1) {
					player.getInventory().addItem(32066, 1);
					player.expertSkillShards[Skills.DIVINATION] = false;
					player.getVarsManager().sendVarBit(26038, 0);
					player.refreshShardConfigs();
				}
				if (bit5 == 1) {
					player.getInventory().addItem(32064, 1);
					player.expertSkillShards[Skills.WOODCUTTING] = false;
					player.getVarsManager().sendVarBit(26063, 0);
					player.refreshShardConfigs();
				}
				if (bit6 == 1) {
					player.getInventory().addItem(32067, 1);
					player.expertSkillShards[Skills.FARMING] = false;
					player.getVarsManager().sendVarBit(26042, 0);
					player.refreshShardConfigs();
				} else
					player.getPackets().sendGameMessage(
							"You have no shards in this category.");
				end();
				break;
			case OPTION_2:
				int bit7 = player.getVarsManager().getBitValue(26043);
				int bit8 = player.getVarsManager().getBitValue(26050);
				int bit9 = player.getVarsManager().getBitValue(26045);
				int bit10 = player.getVarsManager().getBitValue(26046);
				int bit11 = player.getVarsManager().getBitValue(26049);
				int bit12 = player.getVarsManager().getBitValue(26047);
				int bit13 = player.getVarsManager().getBitValue(26044);
				int bit14 = player.getVarsManager().getBitValue(26048);
				if (bit7 == 1) {
					player.getInventory().addItem(32069, 1);
					player.expertSkillShards[Skills.ATTACK] = false;
					player.getVarsManager().sendVarBit(26043, 0);
					player.refreshShardConfigs();
				}
				if (bit8 == 1) {
					player.getInventory().addItem(32074, 1);
					player.expertSkillShards[Skills.HITPOINTS] = false;
					player.getVarsManager().sendVarBit(26050, 0);
					player.refreshShardConfigs();
				}
				if (bit9 == 1) {
					player.getInventory().addItem(32071, 1);
					player.expertSkillShards[Skills.DEFENCE] = false;
					player.getVarsManager().sendVarBit(26045, 0);
					player.refreshShardConfigs();
				}
				if (bit10 == 1) {
					player.getInventory().addItem(32076, 1);
					player.expertSkillShards[Skills.MAGIC] = false;
					player.getVarsManager().sendVarBit(26046, 0);
					player.refreshShardConfigs();
				}
				if (bit11 == 1) {
					player.getInventory().addItem(32073, 1);
					player.expertSkillShards[Skills.PRAYER] = false;
					player.getVarsManager().sendVarBit(26049, 0);
					player.refreshShardConfigs();
				}
				if (bit12 == 1) {
					player.getInventory().addItem(32075, 1);
					player.expertSkillShards[Skills.RANGED] = false;
					player.getVarsManager().sendVarBit(26047, 0);
					player.refreshShardConfigs();
				}
				if (bit13 == 1) {
					player.getInventory().addItem(32070, 1);
					player.expertSkillShards[Skills.STRENGTH] = false;
					player.getVarsManager().sendVarBit(26044, 0);
					player.refreshShardConfigs();
				}
				if (bit14 == 1) {
					player.getInventory().addItem(32072, 1);
					player.expertSkillShards[Skills.SUMMONING] = false;
					player.getVarsManager().sendVarBit(26048, 0);
					player.refreshShardConfigs();
				} else
					player.getPackets().sendGameMessage(
							"You have no shards in this category.");
				end();
				break;
			case OPTION_3:
				int bit15 = player.getVarsManager().getBitValue(26051);
				int bit16 = player.getVarsManager().getBitValue(26058);
				int bit17 = player.getVarsManager().getBitValue(26055);
				int bit18 = player.getVarsManager().getBitValue(26054);
				int bit19 = player.getVarsManager().getBitValue(26052);
				int bit20 = player.getVarsManager().getBitValue(26053);
				int bit21 = player.getVarsManager().getBitValue(26056);
				int bit22 = player.getVarsManager().getBitValue(26057);
				if (bit15 == 1) {
					player.getInventory().addItem(32077, 1);
					player.expertSkillShards[Skills.COOKING] = false;
					player.getVarsManager().sendVarBit(26051, 0);
					player.refreshShardConfigs();
				}
				if (bit16 == 1) {
					player.getInventory().addItem(32083, 1);
					player.expertSkillShards[Skills.HUNTER] = false;
					player.getVarsManager().sendVarBit(26058, 0);
					player.refreshShardConfigs();
				}
				if (bit17 == 1) {
					player.getInventory().addItem(32082, 1);
					player.expertSkillShards[Skills.CRAFTING] = false;
					player.getVarsManager().sendVarBit(26055, 0);
					player.refreshShardConfigs();
				}
				if (bit18 == 1) {
					player.getInventory().addItem(32079, 1);
					player.expertSkillShards[Skills.FIREMAKING] = false;
					player.getVarsManager().sendVarBit(26054, 0);
					player.refreshShardConfigs();
				}
				if (bit19 == 1) {
					player.getInventory().addItem(32080, 1);
					player.expertSkillShards[Skills.FLETCHING] = false;
					player.getVarsManager().sendVarBit(26052, 0);
					player.refreshShardConfigs();
				}
				if (bit20 == 1) {
					player.getInventory().addItem(32081, 1);
					player.expertSkillShards[Skills.HERBLORE] = false;
					player.getVarsManager().sendVarBit(26053, 0);
					player.refreshShardConfigs();
				}
				if (bit21 == 1) {
					player.getInventory().addItem(32078, 1);
					player.expertSkillShards[Skills.RUNECRAFTING] = false;
					player.getVarsManager().sendVarBit(26056, 0);
					player.refreshShardConfigs();
				}
				if (bit22 == 1) {
					player.getInventory().addItem(32084, 1);
					player.expertSkillShards[Skills.SMITHING] = false;
					player.getVarsManager().sendVarBit(26057, 0);
					player.refreshShardConfigs();
				} else
					player.getPackets().sendGameMessage(
							"You have no shards in this category.");
				end();
				break;
			case OPTION_4:
				int bit23 = player.getVarsManager().getBitValue(26059);
				int bit24 = player.getVarsManager().getBitValue(26060);
				int bit25 = player.getVarsManager().getBitValue(26061);
				int bit26 = player.getVarsManager().getBitValue(26062);
				if (bit23 == 1) {
					player.getInventory().addItem(32087, 1);
					player.expertSkillShards[Skills.AGILITY] = false;
					player.getVarsManager().sendVarBit(26059, 0);
					player.refreshShardConfigs();
				}
				if (bit24 == 1) {
					player.getInventory().addItem(32085, 1);
					player.expertSkillShards[Skills.DUNGEONEERING] = false;
					player.getVarsManager().sendVarBit(26060, 0);
					player.refreshShardConfigs();
				}
				if (bit25 == 1) {
					player.getInventory().addItem(32086, 1);
					player.expertSkillShards[Skills.THIEVING] = false;
					player.getVarsManager().sendVarBit(26061, 0);
					player.refreshShardConfigs();
				}
				if (bit26 == 1) {
					player.getInventory().addItem(32088, 1);
					player.expertSkillShards[Skills.SLAYER] = false;
					player.getVarsManager().sendVarBit(26062, 0);
					player.refreshShardConfigs();
				} else
					player.getPackets().sendGameMessage(
							"You have no shards in this category.");
				end();
				break;
			}
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
