package net.nocturne.network.decoders.handlers;

import java.io.IOException;
import java.util.HashMap;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ClientScriptMap;
import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager;
import net.nocturne.game.TemporaryAtributtes.Key;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.item.actions.Bonds;
import net.nocturne.game.item.actions.ItemSets;
import net.nocturne.game.item.actions.MonkeyGreeGrees;
import net.nocturne.game.item.actions.RottenPotato;
import net.nocturne.game.map.bossInstance.BossInstanceHandler;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.npc.familiar.Familiar.SpecialAttack;
import net.nocturne.game.npc.familiar.impl.BeastOfBurden;
import net.nocturne.game.npc.others.GraveStone;
import net.nocturne.game.player.ActionBar.DefenceAbilityShortcut;
import net.nocturne.game.player.ActionBar.HealAbilityShortcut;
import net.nocturne.game.player.ActionBar.MagicAbilityShortcut;
import net.nocturne.game.player.ActionBar.MeleeAbilityShortcut;
import net.nocturne.game.player.ActionBar.RangeAbilityShortcut;
import net.nocturne.game.player.ActionBar.StrengthAbilityShortcut;
import net.nocturne.game.player.DoomsayerManager;
import net.nocturne.game.player.Equipment;
import net.nocturne.game.player.InterfaceManager;
import net.nocturne.game.player.Inventory;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.actions.HomeTeleport;
import net.nocturne.game.player.actions.Rest;
import net.nocturne.game.player.actions.ViewingOrb;
import net.nocturne.game.player.actions.skills.construction.House;
import net.nocturne.game.player.actions.skills.crafting.AccessorySmithing;
import net.nocturne.game.player.actions.skills.divination.DivinationConvert;
import net.nocturne.game.player.actions.skills.divination.DivinationConvert.ConvertMode;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonRewardShop;
import net.nocturne.game.player.actions.skills.farming.FarmingStore;
import net.nocturne.game.player.actions.skills.magic.BoltEnchanting.EnchantAction;
import net.nocturne.game.player.actions.skills.magic.Enchanting;
import net.nocturne.game.player.actions.skills.magic.EnchantingBolts;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.actions.skills.slayer.SlayerShop;
import net.nocturne.game.player.actions.skills.woodcutting.Canoes;
import net.nocturne.game.player.content.CarrierTravel;
import net.nocturne.game.player.content.CharacterSettings;
import net.nocturne.game.player.content.Combat;
import net.nocturne.game.player.content.GnomeGlider;
import net.nocturne.game.player.content.GraveStoneSelection;
import net.nocturne.game.player.content.LoyaltyProgram;
import net.nocturne.game.player.content.PlayerLook;
import net.nocturne.game.player.content.RecipeShop;
import net.nocturne.game.player.content.Shop;
import net.nocturne.game.player.content.SkillCapeCustomizer;
import net.nocturne.game.player.content.SkillingTeleports;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SpiritTree;
import net.nocturne.game.player.content.activities.clans.ClansManager;
import net.nocturne.game.player.content.activities.dailychallenges.DailyTasksInterface;
import net.nocturne.game.player.content.activities.events.WorldEvents;
import net.nocturne.game.player.content.activities.minigames.ArtisanWorkshop;
import net.nocturne.game.player.content.activities.minigames.CastleWars;
import net.nocturne.game.player.content.activities.minigames.PuroPuro;
import net.nocturne.game.player.content.activities.minigames.Sawmill;
import net.nocturne.game.player.content.activities.minigames.pest.CommendationExchange;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationShop;
import net.nocturne.game.player.content.activities.partyroom.PartyRoom;
import net.nocturne.game.player.content.activities.reaper.ReaperRewardsShop;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.content.reports.PlayerReporting;
import net.nocturne.game.player.controllers.GodWars;
import net.nocturne.game.player.controllers.events.DeathEvent;
import net.nocturne.game.player.dialogues.ConfirmDialogue;
import net.nocturne.game.player.dialogues.impl.BossInstanceD;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.network.decoders.WorldPacketsDecoder;
import net.nocturne.stream.InputStream;
import net.nocturne.utils.ItemExamines;
import net.nocturne.utils.Logger;
import net.nocturne.utils.ShopsHandler;
import net.nocturne.utils.Utils;

public class ButtonHandler {

	public static void handleButtons(final Player player, InputStream stream,
			final int packetId) throws ClassNotFoundException, IOException {
		final int slotId = stream.readUnsignedShortLE();
		final int interfaceHash = stream.readInt();
		final int itemId = stream.readUnsignedShortLE128();
		final int interfaceId = interfaceHash >> 16;
		if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		final int componentId = interfaceHash - (interfaceId << 16);
		System.out.println("Interface: " + interfaceId + ", Component: "
				+ componentId + ", Slot: " + slotId + ", Item: " + itemId
				+ ", Packet: " + packetId);
		if (!player.getControllerManager().processButtonClick(interfaceId,
				componentId, slotId, itemId, packetId))
			return;
		if (interfaceId == 793 && player.isLocked()) {// inter changed
			if (componentId == 15)
				player.disconnect(true, false);
			else if (componentId == 14)
				player.getDialogueManager().startDialogue("OpenURLPrompt",
						"forums");
		}
		// cant use inter while locked, temporarly gotta change 21
		if (player.isDead() || player.isLocked()) {
			if (player.getCutscenesManager().hasCutscene()
					&& player.getCutscenesManager().getCurrent()
							.allowSkipCutscene() && interfaceId == 1477
					&& componentId == 21
					&& packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				player.getCutscenesManager().stop();
				return;
			}
			if (Settings.DEBUG)
				System.out.println("BLOCK 2 " + packetId + "," + interfaceId
						+ "," + (interfaceHash & 0xFFFF));
			return;
		} else if (interfaceId == 1776) {
			if (componentId == 58)
				player.getInterfaceManager().removeWindowInterface(
						InterfaceManager.SCREEN_BACKGROUND_INTER_COMPONENT_ID);
		} else if (interfaceId == 1262) {
			if (player.getControllerManager().getController() instanceof GodWars) {
				if (componentId == 18) {
					player.setNextWorldTile(new WorldTile(2863, 5219, 0));
					player.getInterfaceManager().removeCentralInterface();
				} else if (componentId == 19)
					player.getInterfaceManager().removeCentralInterface();
			}
		}
		if (interfaceId == 131) {
			if (componentId == 1) {
				player.getActionManager()
						.setAction(
								new DivinationConvert(
										player,
										new Object[] { ConvertMode.CONVERT_TO_ENERGY }));
			} else if (componentId == 6) {
				player.getActionManager().setAction(
						new DivinationConvert(player,
								new Object[] { ConvertMode.CONVERT_TO_XP }));
			} else if (componentId == 7) {
				player.getActionManager()
						.setAction(
								new DivinationConvert(
										player,
										new Object[] { ConvertMode.CONVERT_TO_MORE_XP }));
			}
			player.getInterfaceManager().removeCentralInterface();
		}
		if (interfaceId == 301
				&& player.getAssistanceManager().handleButtons(componentId))
			return;
		if (interfaceId == 1484) {
			if (componentId == 14) {
				player.getPackets().sendOpenURL(Settings.STORE_LINK);
			}
			return;
		} 
		if (interfaceId == 1251){
			if (componentId == 53){
				player.getInterfaceManager().removeInterface(1251);
			}
		}
		if (interfaceId == 1591) {
			BossInstanceD.handleButtons(player, componentId);
		}
		if (interfaceId == 1072)// changed
			ArtisanWorkshop.handleButtons(player, componentId);
		if (interfaceId == 432)// done through skillinters
			EnchantingBolts.process(player, componentId, packetId == 14 ? 10
					: packetId == 67 ? 50 : 100);
		if (interfaceId == 761)
			FarmingStore.handleButtons(player, componentId);
		if (interfaceId == 1776) {
			CarrierTravel.handleArcButtons(player, componentId);
			return;
		}
		if (interfaceId == 1308)
			SlayerShop.handleButtons(player, componentId);
		if (interfaceId == 754)
			ReaperRewardsShop.handleButtons(player, componentId);
		if (interfaceId == 1555)
			RecipeShop.handleButtons(player, componentId, slotId);
		if (interfaceId == 1746) {
			if (componentId == 78)
				player.getPackets().sendCSVarInteger(5209, 1);
			else if (componentId == 37)
				player.getPackets().sendCSVarInteger(5209, 0);
		}
		if (interfaceId == 631)
			DailyTasksInterface.handleButton(player, interfaceId, componentId);
		if (interfaceId == 138)
			GnomeGlider.handleButtons(player, componentId);
		if (interfaceId == 734)
			player.getFairyRings().handleButtons(interfaceId, componentId);
		if (interfaceId == 506)
			CharacterSettings.handleButtons(player, componentId);
		if (interfaceId == PartyRoom.CHEST_INTERFACE
				|| interfaceId == PartyRoom.INVENTORY_INTERFACE) {
			PartyRoom.handleButtons(player, interfaceId, componentId, slotId,
					itemId, packetId);
			return;
		}
		if (interfaceId == 1157) {
			WorldEvents.handleButtons(player, componentId);
			return;
		}
		if (componentId != 65535
				&& Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			if (Settings.DEBUG)
				System.out.println("BLOCK 3 " + packetId + "," + interfaceId
						+ "," + componentId);
			return;
		}

		if (interfaceId == 1371) { // skill dialogue rs3
			if (componentId == 62)
				SkillsDialogue.setCategoryByIndex(player, slotId);
			else if (componentId == 44)
				SkillsDialogue.setProductByIndex(player, (slotId - 1) / 4);
			else if (componentId == 29)
				SkillsDialogue.setCurrentQuantity(player, true);
			else if (componentId == 31)
				SkillsDialogue.setCurrentQuantity(player, false);
			else if (componentId == 72)
				SkillsDialogue.backToSelect(player);
		} else if (interfaceId == 1345) {
			if (componentId == 202)
				player.getDialogueManager().startDialogue("OpenURLPrompt",
						"Error404");
			else if (componentId == 50)
				player.getInterfaceManager().switchMenu(6);
			else if (componentId == 47)
				player.getInterfaceManager().switchMenu(2);
			else if (componentId == 48 || componentId == 52)
				player.getInterfaceManager().switchMenu(3);

		} else if (interfaceId == 1465) {
			if (componentId == 9 || componentId == 76) {
				player.getDialogueManager().sendLogoutDialogue();
			} else if (componentId == 22) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getPriceCheckManager().openPriceCheck();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getMoneyPouch().examinePouch();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getMoneyPouch().withdrawPouch();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getInterfaceManager().getWealth();
			} else if (componentId == 14) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getSkills().switchXPDisplay();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getSkills().switchXPPopup();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getInterfaceManager().openMenu(8, 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.switchMakeXProgressWindow();
			} else if (componentId == 27) {
				player.getInterfaceManager().openGameTab(
						InterfaceManager.NOTES_TAB);
			} else if (componentId == 30) {// group system
				player.getInterfaceManager().openMenu(4, 4);
			} else if (componentId == 46) { // run energy
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.toogleRun(player.isResting() ? false : true);
					if (player.isResting())
						player.stopAll();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (player.isResting()) {
						player.stopAll();
						return;
					}
					if (player.getEmotesManager().isDoingEmote()) {
						player.getPackets().sendGameMessage(
								"You can't rest while perfoming an emote.");
						return;
					} else if (player.isLocked()) {
						player.getPackets().sendGameMessage(
								"You can't rest while perfoming an action.");
						return;
					}
					Action action = player.getActionManager().getAction();
					if (action != null && !(action instanceof Rest)) {
						player.getPackets()
								.sendGameMessage(
										"Please finish what you are doing before resting.");
						return;
					}
					player.stopAll();
					player.getActionManager().setAction(new Rest(false));
				}
			} else if (componentId == 42) {
				player.getDialogueManager().sendLogoutDialogue();
			} else if (componentId == 44) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					if (player.getInterfaceManager().containsScreenInterface()
							|| player.getInterfaceManager()
									.containsBankInterface()) {
						player.getPackets()
								.sendGameMessage(
										"Please finish what you're doing before opening the world map.");
						return;
					}
					if (player.isUnderCombat()) {
						player.getPackets()
								.sendGameMessage(
										"You cannot be in combat while opening the world map.");
						return;
					}
					player.stopAll();
					// world map open

					// player.setNextAnimation(new Animation(840));

					player.getInterfaceManager().sendGameMapInterface(1421);
					player.getInterfaceManager().sendWorldMapInterface(1422);
					player.getInterfaceManager().setInterface(true, 1422, 86,
							698);

					/*
					 * player.getPackets().sendIComponentSettings(1422, 66, 2,
					 * 2, 2); player.getPackets().sendIComponentSettings(1422,
					 * 67, 2, 2, 2);
					 * player.getPackets().sendIComponentSettings(1422, 68, 2,
					 * 2, 2); player.getPackets().sendIComponentSettings(1422,
					 * 69, 2, 2, 2);
					 * player.getPackets().sendIComponentSettings(1422, 70, 2,
					 * 2, 2); player.getPackets().sendIComponentSettings(1422,
					 * 71, 2, 2, 2);
					 * player.getPackets().sendIComponentSettings(1422, 72, 2,
					 * 2, 2); player.getPackets().sendIComponentSettings(1422,
					 * 73, 2, 2, 2);
					 * player.getPackets().sendIComponentSettings(1422, 74, 2,
					 * 2, 2); player.getPackets().sendIComponentSettings(1422,
					 * 75, 2, 2, 2);
					 */

					// player.getPackets().sendIComponentSettings(1422, 15, 0,
					// 19, 2);

					player.getPackets().sendUnlockIComponentOptionSlots(1422,
							86, 1, 19, 0, 2);

					/*
					 * player.setCloseInterfacesEvent(new Runnable() {
					 * 
					 * @Override public void run() {
					 * player.getInterfaceManager().sendGameMapInterface();
					 * player.getInterfaceManager().closeWorldMapInterface(); }
					 * });
					 */
					player.getPackets().sendCSVarInteger(622,
							player.getTileHash()); // center
					player.getPackets().sendCSVarInteger(674,
							player.getTileHash());// player
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					resetWorldMapMark(player);
			} else if (componentId == 57) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					Magic.useHomeTele(player);
				else {
					player.stopAll();
					HomeTeleport.useLodestone(player,
							player.getPreviousLodestone());
				}
			}
		} else if (interfaceId == 1461) {
			if (slotId == 158) {
				player.getPackets().sendGameMessage(
						"Use the home teleport button.");
			}
			if (componentId == 1) {
				if (slotId == 156) {
					if (player.getSkills().hasLevel(Skills.MAGIC, 4))
						player.getDialogueManager().startDialogue(
								"BoltEnchantingD", EnchantAction.OPAL_BOLTS_E);
					else
						return;
				} else
					player.getActionbar().useAbility(
							new MagicAbilityShortcut(slotId), packetId);
			} else if (componentId == 7) {
				if (slotId >= 7 && slotId <= 10)
					player.getCombatDefinitions().setMagicAbilityMenu(
							slotId - 7);
			}
		} else if (interfaceId == 1460) {
			if (componentId == 1)
				player.getActionbar()
						.useAbility(
								player.getCombatDefinitions().onStrengthMenu() ? new StrengthAbilityShortcut(
										slotId) : new MeleeAbilityShortcut(
										slotId), packetId);
			else if (componentId == 5 && (slotId == 7 || slotId == 8))
				player.getCombatDefinitions().setStrengthMenu(
						slotId == 8 ? 1 : 0);
		} else if (interfaceId == 1617) {
			if (componentId == 0) {
				if (slotId == 0 || slotId == 2)
					player.getCombatDefinitions().setStrengthMenu(
							slotId == 2 ? 1 : 0);
				else if (slotId == 24 || slotId == 26)
					player.getCombatDefinitions().setDefenceMenu(
							slotId == 26 ? 1 : 0);
			}
		}

		else if (interfaceId == 216) {
			if (componentId == 29) {
				player.closeInterfaces();
				return;
			}
			if ((boolean) player.getTemporaryAttributtes().get("NPCTeleports")) {
				if (componentId == 35)
					player.getDialogueManager().startDialogue(
							"MTLowLevelTraining");
				else if (componentId == 43)
					player.getDialogueManager().startDialogue(
							"MTMediumLevelTraining");
				else if (componentId == 51)
					player.getDialogueManager().startDialogue(
							"MTLowLevelDungeons");
				else if (componentId == 59)
					player.getDialogueManager().startDialogue(
							"MTMediumLevelDungeons");
				else if (componentId == 67)
					player.getDialogueManager().startDialogue(
							"MTHighLevelDungeons");
				else if (componentId == 75)
					player.getDialogueManager().startDialogue(
							"MTSlayerDungeons");
				else if (componentId == 83)
					player.getDialogueManager().startDialogue(
							"MTMediumLevelBosses");
				else if (componentId == 91)
					player.getDialogueManager().startDialogue(
							"MTHighLevelBosses");
				return;
			} else
				SkillingTeleports.handleButtons(player, componentId);
		} else if (interfaceId == 1449) {
			if (componentId == 1)
				player.getActionbar()
						.useAbility(
								player.getCombatDefinitions().onDefenceMenu() ? new DefenceAbilityShortcut(
										slotId) : new HealAbilityShortcut(
										slotId), packetId);
			else if (componentId == 7 && (slotId == 7 || slotId == 8))
				player.getCombatDefinitions().setDefenceMenu(
						slotId == 8 ? 1 : 0);
		} else if (interfaceId == 1452) {
			if (componentId == 1)
				player.getActionbar().useAbility(
						new RangeAbilityShortcut(slotId), packetId);
		} else if (interfaceId == 1422) {
			player.setNextAnimation(new Animation(-1));
			if (componentId == 11 || componentId == 86 && slotId == 19) {
				resetWorldMapMark(player);
			} else if (componentId == 86) {
				if (slotId == 1)
					player.getVarsManager().sendVarBit(
							14109,
							player.getVarsManager().getBitValue(14109) == 0 ? 1
									: 0);// TODO
				// uknow.
				else if (slotId == 4)
					player.getVarsManager().sendVarBit(
							14110,
							player.getVarsManager().getBitValue(14110) == 0 ? 1
									: 0);// TODO
				// uknow.
				else if (slotId == 12)
					player.getVarsManager().sendVarBit(
							14111,
							player.getVarsManager().getBitValue(14111) == 0 ? 1
									: 0);// TODO
				// uknow.
				else if (slotId == 16)
					player.getVarsManager().sendVarBit(
							14112,
							player.getVarsManager().getBitValue(14112) == 0 ? 1
									: 0);// TODO
				// uknow.
			} else if (componentId == 92) {
				// TODO not sure how this works.
				// player.getVarsManager().sendVarBit(18782,
				// player.getVarsManager().getBitValue(18782) == 1 ? 0 :
				// 1);//TODO uknow.
			}

		} else if (interfaceId == 1670) {
			if (componentId >= 11 && componentId <= 180) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
					player.getInterfaceManager().openMenu(9, 3);
				else
					player.getActionbar().pushShortcut1(
							(componentId - 11) / 13, packetId);
			} else if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getVarsManager().sendVarBit(29138, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getVarsManager().sendVarBit(29138, 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getVarsManager().sendVarBit(29138, 3);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getVarsManager().sendVarBit(29138, 4);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getVarsManager().sendVarBit(29138, 5);
			} else if (componentId == 189) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getInterfaceManager().openMenu(2, 6);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getDialogueManager().startDialogue("SetupActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getDialogueManager().startDialogue("ClearActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getCombatDefinitions().setCombatMode(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getCombatDefinitions().setCombatMode(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInterfaceManager().openMenu(9,
							player.getSubMenus()[9] + 1);
					player.getInterfaceManager().setInterface(true, 1443, 69,
							970);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
					player.getDialogueManager().sendConfirmDialogue(9,
							new ConfirmDialogue() {
								@Override
								public void process(int option) {
									if (option == 1)
										player.switchLegacyCombatMode();
								}

								@Override
								public void finish() {
								}
							});
				}
			}
		} else if (interfaceId == 1671) {
			if (componentId >= 8 && componentId <= 177) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
					player.getInterfaceManager().openMenu(9, 3);
				else
					player.getActionbar().pushShortcut2((componentId - 8) / 13,
							packetId);
			} else if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getVarsManager().sendVarBit(29139, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getVarsManager().sendVarBit(29139, 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getVarsManager().sendVarBit(29139, 3);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getVarsManager().sendVarBit(29139, 4);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getVarsManager().sendVarBit(29139, 5);
			} else if (componentId == 187) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getInterfaceManager().openMenu(2, 6);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getDialogueManager().startDialogue("SetupActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getDialogueManager().startDialogue("ClearActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getCombatDefinitions().setCombatMode(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getCombatDefinitions().setCombatMode(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInterfaceManager().openMenu(9,
							player.getSubMenus()[9] + 1);
					player.getInterfaceManager().setInterface(true, 1443, 69,
							970);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
					player.getDialogueManager().sendConfirmDialogue(9,
							new ConfirmDialogue() {

								@Override
								public void process(int option) {
									if (option == 1)
										player.switchLegacyCombatMode();
								}

								@Override
								public void finish() {
								}
							});
				}
			}
		} else if (interfaceId == 1672) {
			if (componentId >= 7 && componentId <= 176) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
					player.getInterfaceManager().openMenu(9, 3);
				else
					player.getActionbar().pushShortcut3((componentId - 7) / 13,
							packetId);
			} else if (componentId == 188) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getVarsManager().sendVarBit(29140, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getVarsManager().sendVarBit(29140, 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getVarsManager().sendVarBit(29140, 3);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getVarsManager().sendVarBit(29140, 4);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getVarsManager().sendVarBit(29140, 5);
			} else if (componentId == 186) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getInterfaceManager().openMenu(2, 6);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getDialogueManager().startDialogue("SetupActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getDialogueManager().startDialogue("ClearActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getCombatDefinitions().setCombatMode(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getCombatDefinitions().setCombatMode(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInterfaceManager().openMenu(9,
							player.getSubMenus()[9] + 1);
					player.getInterfaceManager().setInterface(true, 1443, 69,
							970);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
					player.getDialogueManager().sendConfirmDialogue(9,
							new ConfirmDialogue() {
								@Override
								public void process(int option) {
									if (option == 1)
										player.switchLegacyCombatMode();
								}

								@Override
								public void finish() {
								}
							});
				}
			}
		} else if (interfaceId == 1673) {
			if (componentId >= 7 && componentId <= 176) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
					player.getInterfaceManager().openMenu(9, 3);
				else
					player.getActionbar().pushShortcut4((componentId - 7) / 13,
							packetId);
			} else if (componentId == 188) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getVarsManager().sendVarBit(29141, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getVarsManager().sendVarBit(29141, 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getVarsManager().sendVarBit(29141, 3);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getVarsManager().sendVarBit(29141, 4);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getVarsManager().sendVarBit(29141, 5);
			} else if (componentId == 186) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getInterfaceManager().openMenu(2, 6);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getDialogueManager().startDialogue("SetupActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getDialogueManager().startDialogue("ClearActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getCombatDefinitions().setCombatMode(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getCombatDefinitions().setCombatMode(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInterfaceManager().openMenu(9,
							player.getSubMenus()[9] + 1);
					player.getInterfaceManager().setInterface(true, 1443, 69,
							970);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
					player.getDialogueManager().sendConfirmDialogue(9,
							new ConfirmDialogue() {

								@Override
								public void process(int option) {
									if (option == 1)
										player.switchLegacyCombatMode();
								}

								@Override
								public void finish() {
								}
							});
				}
			}
		} else if (interfaceId == 1430) {
			if (componentId == 8) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getActionbar().useAbility(
							new HealAbilityShortcut(1), packetId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					EffectsManager.healPoison(player);
			} else if (componentId == 20)
				Familiar.selectLeftOption(player);
			else if (componentId == 23 || componentId == 32) {
				if (player.getFamiliar() == null)
					player.getPackets().sendGameMessage(
							"You don't have a familiar.");
				else
					player.getFamiliar().getBob().takeBob();
			} else if (componentId == 24 || componentId == 33) {
				if (player.getFamiliar() == null)
					player.getPackets().sendGameMessage(
							"You don't have a familiar.");
				else
					player.getFamiliar().renewFamiliar();
			} else if (componentId == 35 || componentId == 36) {
				if (player.getFamiliar() == null)
					player.getPackets().sendGameMessage(
							"You don't have a familiar.");
				else
					player.getFamiliar().sendFollowerDetails();
			} else if (componentId == 39 || componentId == 41) {
				if (player.getFamiliar() == null)
					player.getPackets().sendGameMessage(
							"You don't have a familiar.");
				else
					player.getFamiliar().call();
			} else if (componentId == 40 || componentId == 42) {
				if (player.getFamiliar() == null)
					player.getPackets().sendGameMessage(
							"You don't have a familiar.");
				else
					player.getDialogueManager().startDialogue("DismissD");
			} else if (componentId == 50)
				player.getCombatDefinitions().switchAutoRelatie();
			else if (componentId == 48)
				submitSpecialRequest(player);
			else if (componentId == 13) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) // activate
					player.getPrayer().activateQuick();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) // switch
					player.getPrayer().selectQuick();
			} else if (componentId >= 57 && componentId <= 231) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
					player.getInterfaceManager().openMenu(9, 3);
				player.getActionbar().pushShortcut((componentId - 57) / 13,
						packetId);
			} else if (componentId == 250)
				player.getActionbar().decreaseCurrentBar();
			else if (componentId == 251)
				player.getActionbar().increaseCurrentBar();
			else if (componentId == 246) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getActionbar().setCurrentBar(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getActionbar().setCurrentBar(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getActionbar().setCurrentBar(2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getActionbar().setCurrentBar(3);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getActionbar().setCurrentBar(4);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
					player.getInterfaceManager().openMenu(8, 3);
			} else if (componentId == 254)
				player.getActionbar().switchLockBar();
			else if (componentId == 256) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getInterfaceManager().openMenu(2, 6);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getDialogueManager().startDialogue("SetupActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getDialogueManager().startDialogue("ClearActionBar");
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getCombatDefinitions().setCombatMode(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getCombatDefinitions().setCombatMode(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInterfaceManager().openMenu(9,
							player.getSubMenus()[9] + 1);
					player.getInterfaceManager().setInterface(true, 1443, 69,
							970);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {

					player.getDialogueManager().sendConfirmDialogue(9,
							new ConfirmDialogue() {
								@Override
								public void process(int option) {
									if (option == 1)
										player.switchLegacyCombatMode();
								}

								@Override
								public void finish() {
								}

							});
				}
			}
		} else if (interfaceId == 1477) {
			if (componentId == 3)
				player.getInterfaceManager().closeMenu();
			else if (componentId == 76)
				player.getDialogueManager().sendLogoutDialogue();
			else if (componentId == 44)
				player.switchLockInterfaceCustomization();
			else if (componentId == 50)
				player.getCombatDefinitions().switchSheathe();
			else if (componentId == 405) {
				if (slotId == 1)
					player.getInterfaceManager().closeMenu();
			} else if (componentId == 507) {
				if (slotId == 3)
					player.getInterfaceManager().switchMenu(1);
				else if (slotId == 7)
					player.getInterfaceManager().switchMenu(2);
				else if (slotId == 11)
					player.getInterfaceManager().switchMenu(3);
				else if (slotId == 15)
					player.getInterfaceManager().switchMenu(4);
				else if (slotId == 19)
					player.getInterfaceManager().switchMenu(5);
				else if (slotId == 23)
					player.getInterfaceManager().switchMenu(6);
			} else if (componentId == 898) {
				if (player.getInterfaceManager().containsInterface(1442)) {
					player.setUTCClock(slotId % 3);
				}
				if (player.isInLegacyCombatMode()
						|| player.isInLegacyInterfaceMode()) {
					player.getPackets().sendGameMessage(
							"That option is disabled in legacy mode.");
					return;
				}
				Object barSlot = player.getTemporaryAttributtes().remove(
						Key.ACTIONBAR_ID);
				if (barSlot != null) {
					player.getActionbar().updateOtherBar((int) barSlot, slotId);
				}
			}
		} else if (interfaceId == 1436) {
			if (componentId >= 25 && componentId <= 194)
				player.getActionbar().pushShortcut((componentId - 25) / 13,
						packetId);
			else if (componentId == 13)
				player.getActionbar().decreaseCurrentBar();
			else if (componentId == 14)
				player.getActionbar().increaseCurrentBar();
			else if (componentId == 11) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getActionbar().setCurrentBar(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getActionbar().setCurrentBar(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getActionbar().setCurrentBar(2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getActionbar().setCurrentBar(3);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					player.getActionbar().setCurrentBar(4);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
					player.getInterfaceManager().openMenu(8, 3);
			} else if (componentId == 15) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getActionbar().helpTrashCan();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getDialogueManager().startDialogue("ClearActionBar");
			} else if (componentId == 7)
				player.getActionbar().switchBlockIncomingShareOffers();

		} else if (interfaceId == 1431) { // options menu
			if (player.getInterfaceManager().containsTreasureHunterInterface()) {
				player.getPackets()
						.sendGameMessage(
								"Please finish what you are doing before opening this menu.");
				return;
			}
			player.getInterfaceManager().sendCustom(player);

			if (componentId == 5) {// hero f1
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.getInterfaceManager().openMenu(0,
							player.getSubMenus()[0] + 1);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					player.getInterfaceManager().openMenu(1,
							player.getSubMenus()[1] + 1);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					player.getInterfaceManager().openMenu(3,
							player.getSubMenus()[3] + 1);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					player.getInterfaceManager().openMenu(2,
							player.getSubMenus()[2] + 1);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON5_PACKET:
					player.getInterfaceManager().openMenu(4,
							player.getSubMenus()[4] + 1);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON9_PACKET:
					player.getInterfaceManager().openExtras();
					break;
				case WorldPacketsDecoder.ACTION_BUTTON6_PACKET:
					player.getInterfaceManager().openRS3Helper();
					break;
				}
			} else if (componentId == 7)
				player.getInterfaceManager().openMenu(0,
						player.getSubMenus()[0] + 1);
			else if (componentId == 8) {
				player.setCloseInterfacesEvent(new Runnable() {

					@Override
					public void run() {
						player.getCosmeticsManager().close();
					}
				});
				player.getInterfaceManager().openMenu(1,
						player.getSubMenus()[1] + 1);
			} else if (componentId == 9)
				player.getInterfaceManager().openMenu(3,
						player.getSubMenus()[3] + 1);
			else if (componentId == 10)
				player.getInterfaceManager().openMenu(2,
						player.getSubMenus()[2] + 1);
			else if (componentId == 11)
				player.getInterfaceManager().openMenu(4,
						player.getSubMenus()[4] + 1);
			else if (componentId == 12) { // open settings
				if (player.getInterfaceManager().isMenuOpen())
					player.getInterfaceManager().closeMenu();
			} else if (componentId == 13)
				player.getInterfaceManager().openExtras();
			else if (componentId == 14) {
				player.getInterfaceManager().openMenu(8,
						player.getSubMenus()[8] + 1);
			} else if (componentId == 36)
				player.getInterfaceManager().openRibbonSetup();
		} else if (interfaceId == 970) {
			if (componentId == 11 || componentId == 14 || componentId == 18
					|| componentId == 22) {
				if (player.isInLegacyCombatMode()
						|| player.isInLegacyInterfaceMode()) {
					return;
				}
				player.getVarsManager().sendVar(4735, interfaceHash);
				player.getVarsManager().sendVar(4734, 10322);
				player.getVarsManager().sendVar(4736, 0);
				player.getPackets().sendIComponentSettings(1477, 898, 0, 11, 2);
				player.getTemporaryAttributtes().put(Key.ACTIONBAR_ID,
						(componentId - 10) / 4);
			}
		} else if (interfaceId == 1443) { // gameplay settings
			// general gameplay
			if (componentId == 7) // one button game-play
				player.switchMouseButtons();
			else if (componentId == 9) // crashes the client for some reason
				player.getInterfaceManager().openMenu(9, 1);
			else if (componentId == 18)
				player.getInterfaceManager().setInterface(true, 1443, 69, 1623);
			else if (componentId == 27)
				player.getInterfaceManager().setInterface(true, 1443, 69, 1662);
			else if (componentId == 36)
				player.getInterfaceManager().setInterface(true, 1443, 69, 1664);
			else if (componentId == 45)
				player.getInterfaceManager().setInterface(true, 1443, 69, 970);
			else if (componentId == 57)
				player.getInterfaceManager().setInterface(true, 1443, 69, 583);
			else if (componentId == 65) {
				player.getInterfaceManager().setInterface(true, 1443, 69, 1674);
				player.getPackets().sendIComponentSettings(1674, 101, 0, 7, 2);
				player.getPackets().sendIComponentSettings(1674, 127, 0, 7, 2);
			} else if (componentId == 74)
				player.getInterfaceManager().setInterface(true, 1443, 69, 1690);
			else if (componentId == 82)
				player.getInterfaceManager().setInterface(true, 1443, 69, 1702);
		} else if (interfaceId == 1662) {
			if (componentId == 13)
				player.toggleGroupAssignments();
			else if (componentId == 21)
				player.toggleLargerTasks();
		} else if (interfaceId == 1622) {
			if (componentId == 8) {
				player.getInterfaceManager().removeInterface(1622);
			}
		} else if (interfaceId == 1663) {
			if (componentId == 15) // hide familiar options
				player.switchHideFamiliarOptions();
			else if (componentId == 21) // guidance system hints
				player.switchGuidanceSystemHints();
			else if (componentId == 27) // toogle quick chat
				player.switchToogleQuickChat();
			else if (componentId == 33)
				player.getDialogueManager().sendConfirmDialogue(9,
						new ConfirmDialogue() {
							@Override
							public void process(int option) {
								if (option == 1)
									player.switchLegacyCombatMode();
							}

							@Override
							public void finish() {
							}

						});
			else if (componentId == 47)
				player.getDialogueManager().sendConfirmDialogue(9,
						new ConfirmDialogue() {
							@Override
							public void process(int option) {
								if (option == 1)
									player.switchLegacyInterfaceMode();
							}

							@Override
							public void finish() {
							}

						});
		} else if (interfaceId == 1628)

		{
			if (componentId == 35 || componentId == 43) {
				player.getInterfaceManager().closeMenu();
				player.getCompCapeManager().sendInterface();
			}
			return;
		} else if (interfaceId == 1664) {
			if (componentId == 47) {
				if (!player.getBank().hasVerified(7))
					return;
				player.getHouse()
						.setBuildMode(!player.getHouse().isBuildMode());
			} else if (componentId == 22 || componentId == 18)
				player.getHouse().setArriveInPortal(componentId == 22);
			else if (componentId == 66 || componentId == 70)
				player.getHouse().setDoorsOpen(componentId == 66);
			else if (componentId == 27) {
				// TODO
			} else if (componentId == 31) {
				// TODO
			} else if (componentId == 76 || componentId == 80) {
				player.getHouse().setBuildMode(componentId == 76);
			} else if (componentId == 79)
				player.getHouse().expelGuests();
			else if (componentId == 89)
				House.leaveHouse(player);
			return;
		} else if (interfaceId == 1674) {
			int bitValue = player.getVarsManager().getBitValue(19010);
			int bitValue1 = player.getVarsManager().getBitValue(19011);
			if (componentId == 73)
				player.switchVirtualLeveling();
			else if (componentId == 79)
				player.switchGoldTrim();
			else if (componentId == 87) {
				if (bitValue >= 0 && bitValue < 8)
					player.decreaseFirstVirtualIcon();
			} else if (componentId == 92) {
				if (bitValue >= 0 && bitValue <= 8)
					player.increaseFirstVirtualIcon();
			} else if (componentId == 113) {
				if (bitValue1 >= 0 && bitValue < 8)
					player.decreaseSecondVirtualIcon();
			} else if (componentId == 118) {
				if (bitValue1 >= 0 && bitValue < 8)
					player.increaseSecondVirtualIcon();
			}
		} else if (interfaceId == 1485) {
			if (componentId == 16) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getCombatDefinitions().setCombatMode(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
					player.getCombatDefinitions().setCombatMode(1);
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
					player.getCombatDefinitions().setCombatMode(2);
			}
		} else if (interfaceId == 1607) {
			if (componentId == 34)
				player.getTreasureHunter().openSpinInterface(true, false, true); // perhaps
			else if (componentId == 9)
				player.getPackets().sendOpenURL(Settings.STORE_LINK);
			else if (componentId == 59)
				Bonds.openInterface(player, true);
		} else if (interfaceId == 1442) { // interface settings
											// interface customization
			if (componentId == 9) // slim headers
				player.switchSlimHeaders();
			else if (componentId == 15) // hide title bars when locked
				player.switchHideTitleBarsWhenLocked();
			else if (componentId == 21)
				player.switchLockInterfaceCustomization();
			else if (componentId == 155) // edit mode
				player.getInterfaceManager().openEditMode();
			else if (componentId == 156)
				player.refreshSlayerCounter();
			// interface options
			else if (componentId == 61) // always show target information
				player.switchAlwaysShowTargetInformation();
			else if (componentId == 67) // target reticles
				player.switchTargetReticules();
			else if (componentId == 73) // task complete popups
				player.switchTaskCompletePopups();
			else if (componentId == 90) // task information window
				player.switchTaskInformationWindow();
			else if (componentId == 84) // xp pop-ups
				player.getSkills().switchXPPopup();
			else if (componentId == 90) // make x progress window
				player.switchMakeXProgressWindow();
			else if (componentId == 114) // toogle player notification
				player.switchTooglePlayerNotification();
			else if (componentId == 97) // toogle ability cooldown timer
				player.switchToogleAbilityCooldownTimer();
			else if (componentId == 110) // toogle ability cooldown timer
				player.switchSkillTargetBasedXPPopup();
			else if (componentId == 114)
				player.setCurrentOptionsMenu(1442);
		}

		else if (interfaceId == 1702) {
			if (componentId == 32)
				player.switchClickThroughtChatBoxes();
			else if (componentId == 36)
				player.switchTimeStamps();
			else if (componentId == 40)
				player.switchSplitPrivateChat();
			else if (componentId == 44)
				player.switchTooglePlayerNotification();
			else if (componentId == 48)
				player.switchPublicEffects();
			else if (componentId == 137)
				player.getVarsManager().sendVar(5967, 512);
			else if (componentId == 141)
				player.getVarsManager().sendVar(5967, 514);
			else if (componentId == 145)
				player.getVarsManager().sendVar(5967, 516);
		} else if (interfaceId == 1623) {
			if (componentId == 7)
				player.switchLootInterface();
			if (componentId == 118) {
				player.getPackets()
						.sendInputLongTextScript(
								"Please enter minimum amount a lootbeam should appear in coins:");
				player.getTemporaryAttributtes().put("lootbeamMinValue",
						Boolean.TRUE);
				// end();
			}
		} else if (interfaceId == 1444) { // controls
			if (componentId == 350) // lock zoom
				player.switchLockZoom();
			else if (componentId == 356 || componentId == 360) // classic rs3
				// camera
				player.setCameraType(componentId == 360);
		} else if (interfaceId == 1433) { // options menu
			if (componentId == 24) {
				player.getInterfaceManager().openMenu(0,
						player.getSubMenus()[0] + 1);
			} else if (componentId == 106 || componentId == 135) {
				player.getInterfaceManager().openMenu(1,
						player.getSubMenus()[1] + 1);
			} else if (componentId == 107 || componentId == 136) {
				player.getInterfaceManager().openMenu(3,
						player.getSubMenus()[3] + 1);
			} else if (componentId == 108 || componentId == 137) {
				player.getInterfaceManager().openMenu(2,
						player.getSubMenus()[2] + 1);
			} else if (componentId == 109 || componentId == 138) {
				player.getInterfaceManager().openMenu(4,
						player.getSubMenus()[4] + 1);
			} else if (componentId == 110 || componentId == 139) {
				player.getInterfaceManager().openExtras();
			} else if (componentId == 34) {
				player.getInterfaceManager().openEditMode();
			} else if (componentId == 42) {
				player.getInterfaceManager().openMenu(9, 1);
			} else if (componentId == 82) {
				player.getInterfaceManager().openMenu(9, 2);
			} else if (componentId == 90) {
				player.getInterfaceManager().openMenu(9, 3);
			} else if (componentId == 50) {
				player.getInterfaceManager().openMenu(9, 4);
			} else if (componentId == 58) {
				player.getInterfaceManager().openMenu(9, 5);
			} else if (componentId == 66 || componentId == 74) {
				if (player.isUnderCombat()) {
					player.getPackets().sendGameMessage(
							"You cannot be in combat while logging out.");
					return;
				}
				player.stopAll();
				player.logout(componentId == 66);
			}
		} else if (interfaceId == 26) {
			player.getDialogueManager().handleConfirmDialogue(componentId);
		} else if (interfaceId == 1504) {
			if (componentId == 2) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getActionbar().useAbility(
							new HealAbilityShortcut(1), packetId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					EffectsManager.healPoison(player);
			}
		} else if (interfaceId == 759) {
			if (componentId >= 4 && componentId <= 40)
				player.getBank().sendNext(componentId, false);
		} else if (interfaceId == 939) {
			if (componentId >= 35 && componentId <= 48) {
				int playerIndex = (componentId - 35) / 3;
				if ((componentId & 0x3) != 0)
					player.getDungManager()
							.pressOption(
									playerIndex,
									packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
											: packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET ? 1
													: 2);
				else
					player.getDungManager().pressOption(playerIndex, 3);
			} else if (componentId == 106) {
				player.getDungManager().formParty();
			} else if (componentId == 88 || componentId == 36)
				player.getDungManager().checkLeaveParty();
			else if (componentId == 115)
				player.getDungManager().invite();
			else if (componentId == 142)
				player.getDungManager().changeComplexity();
			else if (componentId == 133)
				player.getDungManager().changeFloor();
			else if (componentId == 124)
				player.getDungManager().openResetProgress();
			else if (componentId == 67)
				player.getDungManager().switchGuideMode();
		} else if (interfaceId == 949) {
			if (componentId == 61)
				player.getDungManager().acceptInvite();
			else if (componentId == 75 || componentId == 115
					|| componentId == 68)
				player.closeInterfaces();
		} else if (interfaceId == 938) {
			if (componentId >= 13 && componentId <= 38)
				player.getDungManager().selectComplexity(
						(componentId - 13) / 5 + 1);
			else if (componentId == 89)
				player.getDungManager().confirmComplexity();
		} else if (interfaceId == 947) {
			if (componentId >= 16 && componentId <= 75)
				player.getDungManager().selectFloor((componentId - 16) + 1);
			else if (componentId == 734)
				player.getDungManager().confirmFloor();
		} else if (interfaceId == 375) {
			player.getActionManager().forceStop();
		} else if (interfaceId == 363) {
			if (componentId == 4)
				player.getTreasureTrailsManager().movePuzzlePeice(slotId);
		} else if (interfaceId == 1253 || interfaceId == 1252
				|| interfaceId == 1139) {
			player.getTreasureHunter().processClick(packetId, interfaceId,
					componentId, slotId, itemId);
		} else if (interfaceId == 1312 || interfaceId == 668
				|| interfaceId == 737 || interfaceId == 1262
				|| interfaceId == 1292 || interfaceId == 793) {
			player.getDialogueManager().continueDialogue(interfaceId,
					componentId, slotId);
		} else if (interfaceId == 34 || interfaceId == 1417) {// notes interface
			if ((interfaceId == 1417 && (componentId == 11 || componentId == 18))
					|| (interfaceId == 34 && (componentId == 12 || componentId == 18))) {
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.getNotes().delete();
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					player.getNotes().deleteAll();
					break;
				}
			} else if ((interfaceId == 1417 && componentId == 6)
					|| (interfaceId == 34 && componentId == 7)) {
				player.getPackets().sendInputLongTextScript("Add note:");
				player.getTemporaryAttributtes().put("entering_note",
						Boolean.TRUE);
			} else if ((interfaceId == 1417 && componentId == 16)
					|| (interfaceId == 34 && componentId == 16)) {
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					if (player.getNotes().getCurrentNote() == slotId)
						player.getNotes().removeCurrentNote();
					else
						player.getNotes().setCurrentNote(slotId);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					player.getPackets().sendInputLongTextScript("Edit note:");
					player.getNotes().setCurrentNote(slotId);
					player.getTemporaryAttributtes().put("editing_note",
							Boolean.TRUE);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					player.getNotes().setCurrentNote(slotId);
					player.getPackets().sendHideIComponent(interfaceId,
							interfaceId == 34 ? 14 : 13, false);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					player.getNotes().delete(slotId);
					break;
				}
			} else if ((interfaceId == 1417 && (componentId >= 34 && componentId <= 52))
					|| (interfaceId == 34 && (componentId >= 31 && componentId <= 52))) {
				player.getNotes().colour(
						interfaceId == 1417 ? (componentId == 34 ? 0
								: componentId == 40 ? 1 : componentId == 46 ? 3
										: 2) : (componentId == 52 ? 0
								: componentId == 45 ? 1 : componentId == 38 ? 3
										: 2));
				player.getPackets().sendHideIComponent(interfaceId,
						interfaceId == 34 ? 14 : 13, true);
			}
		} else if (interfaceId == 229 || interfaceId == 230) {
			Bonds.handleButtonClick(player, interfaceId, componentId);
			return;
		} else if (interfaceId == 675) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET
					&& slotId != 65535) {
				player.getPackets().sendInputIntegerScript("Enter amount:");
				player.getTemporaryAttributtes().put(Key.JEWLERY_SMITH_COMP,
						componentId);
				return;
			}
			AccessorySmithing
					.handleButtonClick(
							player,
							componentId,
							packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET ? 1
									: packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET ? 5
											: packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET ? 10
													: 1);
		} else if (interfaceId == 432) {
			final int index = Enchanting.getComponentIndex(componentId);
			if (index == -1)
				return;
			Enchanting.processBoltEnchantSpell(player, index,
					packetId == 14 ? 1 : packetId == 67 ? 5 : 10);
		} else if (interfaceId == 164 || interfaceId == 161
				|| interfaceId == 378) {
			player.getSlayerManager().handleRewardButtons(interfaceId,
					componentId);
		} else if (interfaceId == 1310) {
			if (componentId == 0) {
				player.getSlayerManager().createSocialGroup(true);
				player.setCloseInterfacesEvent(null);
			}
			player.closeInterfaces();
		} else if (interfaceId == 1011) {
			CommendationExchange.handleButtonOptions(player, componentId);
		} else if (interfaceId == 1309) {
			if (componentId == 20)
				player.getPackets()
						.sendGameMessage(
								"Use your enchanted stone ring onto the player that you would like to invite.",
								true);
			else if (componentId == 22) {
				Player p2 = player.getSlayerManager().getSocialPlayer();
				if (p2 == null)
					player.getPackets()
							.sendGameMessage(
									"You have no slayer group, invite a player to start one.");
				else
					player.getPackets().sendGameMessage(
							"Your current slayer group consists of you and "
									+ p2.getDisplayName() + ".");
			} else if (componentId == 24)
				player.getSlayerManager().resetSocialGroup(true);
			player.closeInterfaces();
		} else if (interfaceId == 1165) {

		} else if (interfaceId == 1128) {
			int index = -1;
			if (componentId == 98 || componentId == 4)
				index = 0;
			else if (componentId == 128 || componentId == 106)
				index = 1;
			else if (componentId == 144 || componentId == 166)
				index = 2;
			else if (componentId == 203 || componentId == 181)
				index = 3;
			else if (componentId == 240 || componentId == 218)
				index = 4;
			else if (componentId == 277 || componentId == 255)
				index = 5;
			else if (componentId == 292 || componentId == 314)
				index = 6;
			if (index != -1)
				StealingCreationShop.select(player, index);
			else if (componentId == 45)
				StealingCreationShop.purchase(player);
		} else if (interfaceId == 1263) {
			player.getDialogueManager().continueDialogue(interfaceId,
					componentId, slotId);
		} else if (interfaceId == 880) {
			if (componentId == 28) {
				if (slotId == 0) {
					player.getVarsManager().sendVarBit(6070, 0);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (slotId == 1) {
					player.getVarsManager().sendVarBit(6070, 1);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (slotId == 2) {
					player.getVarsManager().sendVarBit(6070, 2);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (slotId == 3) {
					player.getVarsManager().sendVarBit(6070, 3);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (slotId == 4) {
					player.getVarsManager().sendVarBit(6070, 4);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (slotId == 5) {
					player.getVarsManager().sendVarBit(6070, 5);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (slotId == 6) {
					player.getVarsManager().sendVarBit(6070, 6);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (slotId == 7) {
					player.getVarsManager().sendVarBit(6070, 7);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				}
			} else if (componentId == 20) {
				player.getInterfaceManager().removeCentralInterface();
				int bitValue = player.getVarsManager().getBitValue(6070);
				if (bitValue == 0) {
					player.getVarsManager().sendVarBit(6068, 0);
					player.getVarbits().put(6068, 0);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (bitValue == 1) {// TODO special move
					player.getVarsManager().sendVarBit(6068, 1);
					player.getVarbits().put(6068, 1);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (bitValue == 2) {
					player.getVarsManager().sendVarBit(6068, 2);
					player.getVarbits().put(6068, 2);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (bitValue == 3) {
					player.getVarsManager().sendVarBit(6068, 3);
					player.getVarbits().put(6068, 3);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (bitValue == 4) {
					player.getVarsManager().sendVarBit(6068, 4);
					player.getVarbits().put(6068, 4);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (bitValue == 5) {
					player.getVarsManager().sendVarBit(6068, 5);
					player.getVarbits().put(6068, 5);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (bitValue == 6) {
					player.getVarsManager().sendVarBit(6068, 6);
					player.getVarbits().put(6068, 6);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				} else if (bitValue == 7) {
					player.getVarsManager().sendVarBit(6068, 7);
					player.getVarbits().put(6068, 7);
					player.getPackets().sendHideIComponent(1430, 34, false);
					player.getPackets().sendHideIComponent(1430, 31, false);
				}
			}
		} else if (interfaceId == 1428) {
			player.closeInterfaces();
			if (player.getFamiliar() == null) {
				if (player.getPet() == null) {
					return;
				}
				if (componentId == 49)
					player.getPet().call();
				else if (componentId == 51)
					player.getDialogueManager().startDialogue("DismissD");
				return;
			}
			if (componentId == 25)
				player.getFamiliar().call();
			else if (componentId == 51)
				player.getDialogueManager().startDialogue("DismissD");
			else if (componentId == 67)
				player.getFamiliar().takeBob();
			else if (componentId == 69)
				player.getFamiliar().renewFamiliar();
			else if (componentId == 74) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
					player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().hasSpecialOn())
					player.getFamiliar().submitSpecial(player);
			}
		} else if (interfaceId == 60)
			CastleWars.handleInterfaces(player, interfaceId, componentId,
					packetId);
		else if (interfaceId == 652) {
			if (componentId == 31)
				GraveStoneSelection
						.handleSelectionInterface(player, slotId / 6);
			else if (componentId == 34)
				GraveStoneSelection.confirmSelection(player);
		} else if (interfaceId == 1145) {
			SpiritTree.handleSpiritTree(player, componentId);
		} else if (interfaceId == 540) {
			if (componentId == 69)
				PuroPuro.confirmPuroSelection(player);
			else if (componentId == 71)
				ShopsHandler.openShop(player, 54);
			else
				PuroPuro.handlePuroInterface(player, componentId);
		} else if (interfaceId == 728)
			PlayerLook.handleYrsaShoes(player, componentId, slotId);
		else if (interfaceId == 52) {
			if (componentId >= 30 && componentId <= 34) {
				player.getTemporaryAttributtes().put("selected_canoe",
						componentId - 30);
				Canoes.createShapedCanoe(player);
			}
		} else if (interfaceId == 53) {
			int selectedArea = -1;
			if (componentId == 47)
				selectedArea = 0;
			else if (componentId == 48)
				selectedArea = 1;
			else if (componentId == 3)
				selectedArea = 2;
			else if (componentId == 6)
				selectedArea = 3;
			else if (componentId == 49)
				selectedArea = 4;
			if (selectedArea != -1)
				Canoes.deportCanoeStation(player, selectedArea);
		} else if (interfaceId == 95) {
			if (componentId >= 23 && componentId <= 33)
				CarrierTravel.handleCharterOptions(player, componentId);
		} else if (interfaceId == 309)
			PlayerLook.handleHairdresserSalonButtons(player, componentId,
					slotId);
		else if (interfaceId == 729)
			PlayerLook.handleThessaliasMakeOverButtons(player, componentId,
					slotId);
		else if (interfaceId == 365)
			player.getTreasureTrailsManager().handleSextant(componentId);
		else if (interfaceId == 364) {
			if (componentId == 4)
				player.getPackets().sendGameMessage(
						ItemExamines.getExamine(new Item(itemId)));
		} else if (interfaceId == 187 || interfaceId == 1416) {
			if ((interfaceId == 187 && componentId == 1)
					|| (interfaceId == 1416 && componentId == 3)) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getMusicsManager().playAnotherMusic(slotId / 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getMusicsManager().sendHint(slotId / 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getMusicsManager().addToPlayList(slotId / 2);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getMusicsManager().removeFromPlayList(slotId / 2);
			} else if ((interfaceId == 187 && componentId == 4)
					|| (interfaceId == 1416 && componentId == 6))
				player.getMusicsManager().addPlayingMusicToPlayList();
			else if ((interfaceId == 187 && componentId == 9)
					|| (interfaceId == 1416 && componentId == 11)) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getMusicsManager()
							.playAnotherMusicFromPlayListByIndex(slotId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getMusicsManager().removeFromPlayListByIndex(slotId);

			} else if ((interfaceId == 187 && componentId == 10)
					|| (interfaceId == 1416 && componentId == 13))
				player.getMusicsManager().switchPlayListOn();
			else if ((interfaceId == 187 && componentId == 18)
					|| (interfaceId == 1416 && componentId == 20))
				player.getMusicsManager().skipMusic();
			else if ((interfaceId == 187 && componentId == 11)
					|| (interfaceId == 1416 && componentId == 14))
				player.getMusicsManager().clearPlayList();
			else if ((interfaceId == 187 && componentId == 16)
					|| (interfaceId == 1416 && componentId == 18))
				player.getMusicsManager().searchMusic();

			else if ((interfaceId == 187 && (componentId == 13 || componentId == 17))
					|| (interfaceId == 1416 && (componentId == 16 || componentId == 19)))
				player.getMusicsManager().switchShuffleOn();
		} else if (interfaceId == 590) {
			if (componentId == 8)
				player.getEmotesManager().useBookEmote(slotId, packetId);
		} else if (interfaceId == 1719) {
			if (componentId == 6) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ItemSets.sendComponents(player, itemId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					ItemSets.exchangeSet(player, itemId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					ItemSets.examineSet(player, itemId);
			}
		} else if (interfaceId == 1721
				&& player.getInterfaceManager().containsInterface(1719)) {
			if (componentId == 7) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					ItemSets.sendComponentsBySlot(player, slotId, itemId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					ItemSets.exchangeSet(player, slotId, itemId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == 334) {
			if (componentId == 52)
				player.closeInterfaces();
			else if (componentId == 47)
				player.getTrade().accept(false);
		} else if (interfaceId == 335) {
			if (componentId == 61)
				player.getTrade().accept(true);
			else if (componentId == 67)
				player.closeInterfaces();
			else if (componentId == 81) {
				player.getPackets().sendInputIntegerScript("Enter amount:");
				player.getTemporaryAttributtes().put(Key.TRADE_COIN_WITHDRAWL,
						true);
			} else if (componentId == 74) {
				for (int x = 0; x < 28; x++)
					player.getTrade().addItem(x, Integer.MAX_VALUE);
			} else if (componentId == 26) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().removeItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getTrade().removeItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getTrade().removeItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getTrade().removeItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("trade_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().put("trade_isRemove",
							Boolean.TRUE);
					player.getPackets().sendInputIntegerScript("Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getTrade().sendValue(slotId, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
					player.getTrade().sendExamine(slotId, false);
			} else if (componentId == 29) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().sendValue(slotId, true);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getTrade().sendExamine(slotId, true);
			}
		} else if (interfaceId == 336) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().addItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getTrade().addItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getTrade().addItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getTrade().addItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("trade_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().remove("trade_isRemove");
					player.getPackets().sendInputIntegerScript("Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getTrade().sendValue(slotId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == DungeonRewardShop.REWARD_SHOP) {
			if (componentId == 87) {
				if (slotId % 5 == 0)
					DungeonRewardShop.select(player, slotId);

			} else if (componentId == 114)
				DungeonRewardShop.sendConfirmationPurchase(player);

		} else if (interfaceId == LoyaltyProgram.LOYALTY_INTERFACE) {
			LoyaltyProgram.handleButtonClick(player, componentId);
		} else if (interfaceId == 206) {
			if (componentId == 5) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getPriceCheckManager().removeItem(slotId,
							Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getPackets().sendInputIntegerScript("Enter Amount:");
					player.getTemporaryAttributtes().put("pc_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().put("pc_isRemove",
							Boolean.TRUE);
				}
			} else if (componentId == 8)
				player.getPriceCheckManager().addAllInventory();
		} else if (interfaceId == 207) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getPriceCheckManager().addItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getPriceCheckManager().addItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getPriceCheckManager().addItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getPriceCheckManager().addItem(slotId,
							Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("pc_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().remove("pc_isRemove");
					player.getPackets().sendInputIntegerScript("Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == 665) {
			if ((player.getFamiliar() == null || player.getFamiliar().getBob() == null)
					&& (!player.hasLegendaryPet() || !player.getPet().activeBoB))
				return;
			if (player.hasLegendaryPet() && player.getPet().activeBoB
					&& componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getPet().bob.addItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getPet().bob.addItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getPet().bob.addItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getPet().bob.addItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			} else if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getFamiliar().getBob()
							.addItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().remove("bob_isRemove");
					player.getPackets().sendInputIntegerScript("Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == 671) {
			if ((player.getFamiliar() == null || player.getFamiliar().getBob() == null)
					&& (!player.hasLegendaryPet() || !player.getPet().activeBoB))
				return;
			if (player.hasLegendaryPet() && player.getPet().activeBoB) {
				if (componentId == 26) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getPet().bob.removeItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getPet().bob.removeItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getPet().bob.removeItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getPet().bob.removeItem(slotId,
								Integer.MAX_VALUE);
				} else if (componentId == 31) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getPet().bob.addItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getPet().bob.addItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getPet().bob.addItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getPet().bob.addItem(slotId, Integer.MAX_VALUE);
				} else if (componentId == 17) {
					for (int i = 0; i < 28; i++)
						player.getPet().bob.addItem(i, Integer.MAX_VALUE);
				}
			} else {
				if (componentId == 26) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getFamiliar().getBob().removeItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getFamiliar().getBob().removeItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getFamiliar().getBob().removeItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getFamiliar().getBob()
								.removeItem(slotId, Integer.MAX_VALUE);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes().put("bob_item_X_Slot",
								slotId);
						player.getTemporaryAttributtes().put("bob_isRemove",
								Boolean.TRUE);
						player.getPackets().sendInputIntegerScript(
								"Enter Amount:");
					}
				} else if (componentId == 31) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getFamiliar().getBob().addItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getFamiliar().getBob().addItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getFamiliar().getBob().addItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getFamiliar().getBob()
								.addItem(slotId, Integer.MAX_VALUE);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes().put("bob_item_X_Slot",
								slotId);
						player.getTemporaryAttributtes().put("bob_isRemove",
								Boolean.TRUE);
						player.getPackets().sendInputIntegerScript(
								"Enter Amount:");
					}
				} else if (componentId == 16) {
					player.getFamiliar().takeBob();
				} else if (componentId == 17) {
					for (int i = 0; i < 28; i++)
						player.getFamiliar().getBob()
								.addItem(i, Integer.MAX_VALUE);
				} else if (componentId == 29)
					player.getFamiliar().takeBob();
			}
		} else if (interfaceId == 261) {
			if (player.getInterfaceManager().containsInventoryInter())
				return;
			if (componentId == 22) {
				if (player.getInterfaceManager().containsScreenInterface()
						|| player.getInterfaceManager().containsBankInterface()) {
					player.getPackets()
							.sendGameMessage(
									"Please close the interface you have open before setting your graphic options.");
					return;
				}
				player.stopAll();
				player.getInterfaceManager().sendCentralInterface(742);
			} else if (componentId == 6)
				player.switchRightClickReporting();
			else if (componentId == 11)
				player.switchProfanityFilter();
			else if (componentId == 12)
				player.switchAllowChatEffects();
			else if (componentId == 13) // chat setup
				player.getInterfaceManager().sendSettings(982);
			else if (componentId == 16) // house options
				player.getInterfaceManager().sendSettings(398);
			else if (componentId == 14)
				player.switchMouseButtons();
			else if (componentId == 24) // audio options
				player.getInterfaceManager().sendSettings(429);
			else if (componentId == 26)
				;// Adventures Log
		} else if (interfaceId == 429) {
			if (componentId == 52)
				player.getMusicsManager().switchGlobalMute();
		} else if (interfaceId == 398) {
			if (componentId == 19)
				player.getInterfaceManager().sendSettings();
			else if (componentId == 15 || componentId == 1) {
				if (!player.getBank().hasVerified(7))
					return;
				player.getHouse().setBuildMode(componentId == 15);
			} else if (componentId == 25 || componentId == 26)
				player.getHouse().setArriveInPortal(componentId == 25);
			else if (componentId == 27)
				player.getHouse().expelGuests();
			else if (componentId == 29)
				House.leaveHouse(player);
		} else if (interfaceId == 402) {
			if (componentId >= 73 && componentId <= 91) {
				player.getHouse().createRoom((componentId - 73) / 9);
			} else if (componentId >= 99) {
				player.getHouse().createRoom(3 + (componentId - 99) / 8);
			}
		} else if (interfaceId == 1690) {
			// player.getAcceptAid().toggleAid(componentId);
			return;
		} else if (interfaceId == 982) {
			if (componentId == 7)
				player.setChatSetup(slotId);
			else if (componentId == 9 || componentId == 10)
				player.getTemporaryAttributtes().put(Key.CHAT_SETUP,
						componentId - 9);
			else if (componentId >= 24 && componentId <= 28)
				player.getTemporaryAttributtes().put(Key.CHAT_SETUP,
						componentId - 22);
			else if (componentId == 35)
				player.switchAllowChatEffects();
		} else if (interfaceId == 1458 || interfaceId == 1457) {
			if (interfaceId == 1458 && (componentId == 33 || componentId == 34))
				player.getPrayer().activate(slotId);
			else if ((interfaceId == 1458 && (componentId == 10 || componentId == 14))
					|| (interfaceId == 1457 && (componentId == 21 || componentId == 31)))
				player.getPrayer().selectQuick();
		} else if (interfaceId == 1466 || interfaceId == 320) {
			if ((interfaceId == 1466 && componentId == 7))
			player.getSkills().switchFlash(Skills.FIXED_SLOTS[slotId], false);
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				switch (slotId) {
				case 0:
					if (player.getTemporaryAttributtes().remove("leveledUp[0]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 1);
					} else {
						player.getVarsManager().sendVarBit(1230, 10);
					}
					break;
				case 6:
					if (player.getTemporaryAttributtes().remove("leveledUp[1]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 5);
					} else {
						player.getVarsManager().sendVarBit(1230, 40);
					}
					break;
				case 3:
					if (player.getTemporaryAttributtes().remove("leveledUp[2]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 2);
					} else {
						player.getVarsManager().sendVarBit(1230, 20);
					}
					break;
				case 1:
					if (player.getTemporaryAttributtes().remove("leveledUp[3]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 6);
					} else {
						player.getVarsManager().sendVarBit(1230, 50);
					}
					break;
				case 9:
					if (player.getTemporaryAttributtes().remove("leveledUp[4]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 3);
					} else {
						player.getVarsManager().sendVarBit(1230, 30);
					}
					break;
				case 12:
					if (player.getTemporaryAttributtes().remove("leveledUp[5]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 7);
					} else {
						player.getVarsManager().sendVarBit(1230, 60);
					}
					break;
				case 15:
					if (player.getTemporaryAttributtes().remove("leveledUp[6]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 4);
					} else {
						player.getVarsManager().sendVarBit(1230, 33);
					}
					break;
				case 11:
					if (player.getTemporaryAttributtes().remove("leveledUp[7]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 16);
					} else {
						player.getVarsManager().sendVarBit(1230, 641);
					}
					break;
				case 17:
					if (player.getTemporaryAttributtes().remove("leveledUp[8]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 18);
					} else {
						player.getVarsManager().sendVarBit(1230, 660);
					}
					break;
				case 16:
					if (player.getTemporaryAttributtes().remove("leveledUp[9]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 19);
					} else {
						player.getVarsManager().sendVarBit(1230, 665);
					}
					break;
				case 8:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[10]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 15);
					} else {
						player.getVarsManager().sendVarBit(1230, 120);
					}
					break;
				case 14:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[11]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 17);
					} else {
						player.getVarsManager().sendVarBit(1230, 649);
					}
					break;
				case 13:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[12]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 11);
					} else {
						player.getVarsManager().sendVarBit(1230, 90);
					}
					break;
				case 5:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[13]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 14);
					} else {
						player.getVarsManager().sendVarBit(1230, 115);
					}
					break;
				case 2:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[14]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 13);
					} else {
						player.getVarsManager().sendVarBit(1230, 110);
					}
					break;
				case 7:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[15]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 9);
					} else {
						player.getVarsManager().sendVarBit(1230, 75);
					}
					break;
				case 4:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[16]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 8);
					} else {
						player.getVarsManager().sendVarBit(1230, 65);
					}
					break;
				case 10:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[17]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 10);
					} else {
						player.getVarsManager().sendVarBit(1230, 80);
					}
					break;
				case 18:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[20]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 12);
					} else {
						player.getVarsManager().sendVarBit(1230, 100);
					}
					break;
				case 19:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[18]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 20);
					} else {
						player.getVarsManager().sendVarBit(1230, 673);
					}
					break;
				case 20:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[19]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 21);
					} else {
						player.getVarsManager().sendVarBit(1230, 681);
					}
					break;
				case 21:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[21]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 22);
					} else {
						player.getVarsManager().sendVarBit(1230, 698);
					}
					break;
				case 22:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[22]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 23);
					} else {
						player.getVarsManager().sendVarBit(1230, 689);
					}
					break;
				case 23:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[23]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 24);
					} else {
						player.getVarsManager().sendVarBit(1230, 705);
					}
					break;
				case 24:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[24]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 25);
					} else {
						player.getVarsManager().sendVarBit(1230, 705);
					}
					break;
				case 25:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[25]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 26);
					} else {
						player.getVarsManager().sendVarBit(1230, 705);
					}
					break;
				case 26:
					if (player.getTemporaryAttributtes()
							.remove("leveledUp[26]") != Boolean.TRUE) {
						player.getVarsManager().sendVarBit(965, 27);
					} else {
						player.getVarsManager().sendVarBit(1230, 705);
					}
					break;
				}
				SkillingTeleports.openInterface(player, player.getSkills()
						.getSkill(slotId));
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET
					|| packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				int skillId = player.getSkills().getSkill(slotId);
				boolean usingLevel = packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET;
				player.getTemporaryAttributtes().put(
						usingLevel ? "levelSkillTarget" : "xpSkillTarget",
						skillId);
				player.getPackets().sendInputIntegerScript(
						"Please enter target " + (usingLevel ? "level" : "xp")
								+ " you want to set: ");
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				try {
					int skillId = player.getSkills().getSkill(slotId);
					player.getSkills().setSkillTargetEnabled(skillId, false);
					player.getSkills().setSkillTargetValue(skillId, 0);
					player.getSkills().setSkillTargetUsingLevelMode(skillId,
							false);
				} catch (Exception e) {
					player.getPackets().sendGameMessage("error: " + e + ".");
				}
			}
		} else if (interfaceId == 1218) {
			if ((componentId >= 17 && componentId <= 40) || componentId == 99
					|| componentId == 130 || componentId == 165)
				player.getInterfaceManager().setInterface(false, 1218, 1, 1217); // seems
			// to
			// fix
		} else if (interfaceId == 1462 || interfaceId == 1464) {
			if (player.getInterfaceManager().containsInventoryInter())
				return;
			if ((interfaceId == 1462 && componentId == 14)
					|| (interfaceId == 1464 && componentId == 15))
				player.getEquipment().handleEquipment(slotId, itemId, packetId);
			else if ((interfaceId == 1462 && componentId == 20)
					|| (interfaceId == 1464 && componentId == 13)) {
				if (slotId == 12) {
					if (interfaceId == 1464)
						player.getInterfaceManager().openMenu(0,
								player.getSubMenus()[0] + 1);
				} else if (slotId == 7) {
					player.stopAll();
					openItemsKeptOnDeath(player);
				} else if (slotId == 2) {
					player.getInterfaceManager().sendCentralInterface(1178);
				}
			}
		} else if (interfaceId == 1626) {
			if (componentId == 46) {
				// player.getInterfaceManager().removeBackGroundInterface();
			}
		} else if (interfaceId == 17) {
			if (componentId == 28)
				sendItemsKeptOnDeath(player, player.getVarsManager()
						.getBitValue(1675) == 0);
			else if (componentId == 17 || componentId == 18
					|| componentId == 20 || componentId == 22) {
				player.getPackets().sendGameMessage(
						ItemExamines.getExamine(new Item(itemId)));
			}
		} else if (interfaceId == 1616) {
			if (componentId == 9)
				if (slotId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					submitSpecialRequest(player);
				else if (slotId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getCombatDefinitions().switchAutoRelatie();
			return;
		} else if (interfaceId == 1265) {
			Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
			if (shop == null)
				return;
			if (componentId == 49 || componentId == 50)
				player.setVerboseShopDisplayMode(componentId == 50);
			else if (componentId == 28 || componentId == 29)
				Shop.setBuying(player, componentId == 28);
			else if (componentId == 20) {
				boolean buying = Shop.isBuying(player);
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					shop.sendInfo(player, slotId, !buying);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (buying)
						shop.buy(player, slotId, 1);
					else
						shop.sell(player, slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					if (buying)
						shop.buy(player, slotId, 5);
					else
						shop.sell(player, slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					if (buying)
						shop.buy(player, slotId, 10);
					else
						shop.sell(player, slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					if (buying)
						shop.buy(player, slotId, 50);
					else
						shop.sell(player, slotId, 50);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
					if (buying)
						shop.buy(player, slotId, 500);
					else
						shop.sell(player, slotId, 500);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
					if (buying)
						shop.buyAll(player, slotId);
				}
			} else if (componentId == 224)
				shop.setTransaction(player, 1);
			else if (componentId == 221)
				shop.increaseTransaction(player, -5);
			else if (componentId == 218)
				shop.increaseTransaction(player, -1);
			else if (componentId == 15)
				shop.increaseTransaction(player, 1);
			else if (componentId == 212)
				shop.increaseTransaction(player, 5);
			else if (componentId == 215)
				shop.setTransaction(player, Integer.MAX_VALUE);
			else if (componentId == 205)
				shop.pay(player);
		} else if (interfaceId == 1266) {
			if (componentId == 0) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null)
					return;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					shop.sendInfo(player, slotId, true);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					shop.sell(player, slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					shop.sell(player, slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					shop.sell(player, slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					shop.sell(player, slotId, 50);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == Inventory.INVENTORY_INTERFACE
				|| interfaceId == Inventory.INVENTORY_INTERFACE_2) { // inventory
			if ((interfaceId == Inventory.INVENTORY_INTERFACE && componentId == 8)
					|| (interfaceId == Inventory.INVENTORY_INTERFACE_2 && componentId == 10)) {
				if (slotId > 27
						|| player.getInterfaceManager()
								.containsInventoryInter())
					return;
				final Item item = player.getInventory().getItem(slotId);
				if (item == null || item.getId() != itemId)
					return;
				if (item.getId() == 5733) {
					if (player.getRights() >= 2) {
						RottenPotato.handlePotato(player, packetId);
						return;
					} else {
						player.getInventory().deleteItem(5733,
								Integer.MAX_VALUE);
						return;
					}
				}
				if (item.getId() == 20428) {
					if (player.getRights() >= 2) {
						return;
					} else {
						player.getInventory().deleteItem(20428,
								Integer.MAX_VALUE);
						return;
					}
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					if (item.getId() == 15492 || item.getId() == 30656
							|| item.getId() == 30686 || item.getId() == 30716)
						InventoryOptionsHandler.handleWear(player, slotId,
								item, true);
					InventoryOptionsHandler.handleItemOption1(player, slotId,
							itemId, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (interfaceId == 1474)
						InventoryOptionsHandler.handleWear(player, slotId,
								item, true);
					InventoryOptionsHandler.handleItemOption2(player, slotId,
							itemId, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					InventoryOptionsHandler.handleItemOption3(player, slotId,
							itemId, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					InventoryOptionsHandler.handleItemOption4(player, slotId,
							itemId, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
					InventoryOptionsHandler.handleItemOption5(player, slotId,
							itemId, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
					InventoryOptionsHandler.handleItemOption6(player, slotId,
							itemId, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
					InventoryOptionsHandler.handleItemOption7(player, slotId,
							itemId, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					InventoryOptionsHandler.handleItemOption8(player, slotId,
							itemId, item);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
					InventoryOptionsHandler.handleExamine(player, slotId,
							itemId, item);
			}
			if (componentId == 36 || componentId == 34) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getPriceCheckManager().openPriceCheck();
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getMoneyPouch().examinePouch();
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getMoneyPouch().withdrawPouch();
			} else if (componentId == 21 || componentId == 22) {
				player.getInterfaceManager().getWealth();
			}
			if ((interfaceId == Inventory.INVENTORY_INTERFACE && componentId == 57)
					|| (interfaceId == Inventory.INVENTORY_INTERFACE_2 && componentId == 24)) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {

					if (player.getInterfaceManager().containsScreenInterface()
							|| player.getInterfaceManager()
									.containsBankInterface()) {
						player.getPackets()
								.sendGameMessage(
										"Please finish what you're doing before opening the price checker.");
						return;
					}
					player.stopAll();
					player.getPriceCheckManager().openPriceCheck();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getMoneyPouch().examinePouch();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getMoneyPouch().withdrawPouch();
			}
			if ((interfaceId == Inventory.INVENTORY_INTERFACE && componentId == 47)) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (player.getInterfaceManager().containsScreenInterface()
							|| player.getInterfaceManager()
									.containsBankInterface()) {
						player.getPackets()
								.sendGameMessage(
										"Please finish what you're doing before opening the wealth evaluator.");
						return;
					}
					player.stopAll();
					player.getInterfaceManager().getWealth();
				}
			}
		} else if (interfaceId == 403)
			Sawmill.handlePlanksConvertButtons(player, componentId, packetId);
		else if (interfaceId == 1505) {
			if (componentId == 2) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) // activate
					player.getPrayer().activateQuick();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) // switch
					player.getPrayer().selectQuick();
			}
		} else if (interfaceId == 11) {
			if (componentId == 1) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().depositItem(slotId, 1, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().depositItem(slotId, 5, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getBank().depositItem(slotId, 10, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getBank().depositItem(slotId, Integer.MAX_VALUE,
							false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.getPackets().sendInputIntegerScript("Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			} else if (componentId == 11)
				player.getBank().depositAllInventory(false);
			else if (componentId == 35)
				player.getBank().depositAllMoneyPouch(false);
			else if (componentId == 19)
				player.getBank().depositAllEquipment(false);
			else if (componentId == 27)
				player.getBank().depositAllBob(false);
		} else if (interfaceId == 762) {
			if (componentId == 24)
				player.getBank().searchBank();
			else if (componentId == 333)
				player.getPackets().sendGameMessage(
						"Your bank value is "
								+ Utils.format(player.getBankValue()) + " gp.");
			else if (componentId == 40 || componentId == 48
					|| componentId == 56) {
				player.getDialogueManager().startDialogue("PresetsD");
			} else if (componentId == 64)
				player.getBank().switchWithdrawNotes();
			else if (componentId == 91)
				player.getBank().depositAllInventory(true);
			else if (componentId == 115)
				player.getBank().depositAllMoneyPouch(true);
			else if (componentId == 99)
				player.getBank().depositAllEquipment(true);
			else if (componentId == 107)
				player.getBank().depositAllBob(true);
			else if (componentId == 128) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getBank().sendExamine(slotId);
				else
					sendRemove(
							player,
							slotId,
							packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET);
			} else if (componentId == 305) {
				player.getBank().openHelpInterface();
			} else if (componentId >= 148 && componentId <= 212) {
				int tabId = player.getBank().getTab(componentId);
				if (tabId == -1)
					return;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().setCurrentTab(tabId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().collapse(tabId);
			} else if (componentId == 243) {
				if (player.isOnBoBWindow) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getBank().withdrawBoBItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getBank().withdrawBoBItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getBank().withdrawBoBItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getBank().withdrawLastAmount(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes().put("slotBoB", slotId);
						player.getTemporaryAttributtes().put("withdrawBoB",
								Boolean.TRUE);
						player.getPackets().sendInputIntegerScript(
								"Enter Amount:");
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getBank().withdrawBoBItem(slotId,
								Integer.MAX_VALUE);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
						player.getBank().withdrawBoBItemButOne(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						player.getBank().sendWearBank(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
						player.getBank().sendExamine(slotId);
				} else {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getBank().withdrawItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getBank().withdrawItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getBank().withdrawItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getBank().withdrawLastAmount(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes()
								.put("slotBank", slotId);
						player.getTemporaryAttributtes().put("withdrawItem",
								Boolean.TRUE);
						player.getPackets().sendInputIntegerScript(
								"Enter Amount:");
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getBank()
								.withdrawItem(slotId, Integer.MAX_VALUE);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
						player.getBank().withdrawItemButOne(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						player.getBank().sendWearBank(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET)
						player.getBank().sendExamine(slotId);
				}
			} else if (componentId == 10) {
				if (player.isOnBoBWindow) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getBank().depositBoBItem(slotId, 1, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getBank().depositBoBItem(slotId, 5, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getBank().depositBoBItem(slotId, 10, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getBank().depositBoBLastAmount(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes().put("slotBoB", slotId);
						player.getTemporaryAttributtes().remove("withdrawBoB");
						player.getPackets().sendInputIntegerScript(
								"Enter Amount:");
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getBank().depositBoBItem(slotId,
								Integer.MAX_VALUE, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
						player.getBank().sendExamineInventory(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						player.getBank().sendWearInventory(slotId);

				} else {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getBank().depositItem(slotId, 1, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getBank().depositItem(slotId, 5, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getBank().depositItem(slotId, 10, true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getBank().depositLastAmount(slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes()
								.put("slotBank", slotId);
						player.getTemporaryAttributtes().remove("withdrawItem");
						player.getPackets().sendInputIntegerScript(
								"Enter Amount:");
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getBank().depositItem(slotId, Integer.MAX_VALUE,
								true);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						player.getBank().sendWearInventory(slotId);
				}
			} else if (componentId == 338)
				player.isOnBoBWindow = false;
			else if (componentId == 346)
				player.isOnBoBWindow = true;
			else if (componentId == 331) {
				if (player.onClanBank)
					player.onClanBank = false;
				player.getActionbar().increaseCurrentBar();
				player.getActionbar().decreaseCurrentBar();
				player.isOnBoBWindow = false;
				player.getPackets().sendCSVarInteger(4619, 0);
			}
		} else if (interfaceId == 1218) {
			if (componentId == 73)
				player.closeInterfaces();
		} else if (interfaceId == 767) {
			if (componentId == 10)
				player.getDialogueManager().startDialogue("ChooseBank");
		} else if (interfaceId == 1218) {
			if (componentId == 73)
				player.closeInterfaces();
		} else if (interfaceId == 767) {
			if (componentId == 10)
				player.getDialogueManager().startDialogue("ChooseBank");
		} else if (interfaceId == 1220) {
			if (componentId == 54)
				player.getInterfaceManager().openMenu(0, 4);
		} else if (interfaceId == 1503) {
			int melee = player.getVarsManager().getBitValue(1906);
			int ranged = player.getVarsManager().getBitValue(1907);
			int magic = player.getVarsManager().getBitValue(1908);
			int type = player.getCombatDefinitions().getType(
					Equipment.SLOT_WEAPON);
			if (componentId == 11) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getCombatDefinitions().switchSheathe();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.switchBuffTimer();
			} else if (componentId == 13)
				submitSpecialRequest(player);
			else if (componentId == 37) {
				if (melee == 0)
					player.getCombatDefinitions().setMeleeCombatExperience(1);
				else if (melee == 1)
					player.getCombatDefinitions().setMeleeCombatExperience(0);
				else if (melee == 2)
					player.getCombatDefinitions().setMeleeCombatExperience(3);
				else if (melee == 3)
					player.getCombatDefinitions().setMeleeCombatExperience(2);
				else if (melee == 4)
					player.getCombatDefinitions().setMeleeCombatExperience(5);
				else if (melee == 5)
					player.getCombatDefinitions().setMeleeCombatExperience(4);
				else if (melee == 6)
					player.getCombatDefinitions().setMeleeCombatExperience(7);
				else if (melee == 7)
					player.getCombatDefinitions().setMeleeCombatExperience(6);
				if (type == Combat.RANGE_TYPE) {
					if (ranged == 0)
						player.getCombatDefinitions()
								.setRangedCombatExperience(1);
					else if (ranged == 1)
						player.getCombatDefinitions()
								.setRangedCombatExperience(0);
					else if (ranged == 2)
						player.getCombatDefinitions()
								.setRangedCombatExperience(3);
					else if (ranged == 3)
						player.getCombatDefinitions()
								.setRangedCombatExperience(2);
				} else if (type == Combat.MAGIC_TYPE) {
					if (magic == 0)
						player.getCombatDefinitions().setMagicCombatExperience(
								1);
					else if (magic == 1)
						player.getCombatDefinitions().setMagicCombatExperience(
								0);
					else if (magic == 2)
						player.getCombatDefinitions().setMagicCombatExperience(
								3);
					else if (magic == 3)
						player.getCombatDefinitions().setMagicCombatExperience(
								2);
				}
			} else if (componentId == 41) {
				if (melee == 0)
					player.getCombatDefinitions().setMeleeCombatExperience(2);
				else if (melee == 1)
					player.getCombatDefinitions().setMeleeCombatExperience(3);
				else if (melee == 2)
					player.getCombatDefinitions().setMeleeCombatExperience(0);
				else if (melee == 3)
					player.getCombatDefinitions().setMeleeCombatExperience(1);
				else if (melee == 4)
					player.getCombatDefinitions().setMeleeCombatExperience(6);
				else if (melee == 5)
					player.getCombatDefinitions().setMeleeCombatExperience(7);
				else if (melee == 6)
					player.getCombatDefinitions().setMeleeCombatExperience(4);
				else if (melee == 7)
					player.getCombatDefinitions().setMeleeCombatExperience(5);

			} else if (componentId == 45) {
				if (melee == 0)
					player.getCombatDefinitions().setMeleeCombatExperience(4);
				else if (melee == 1)
					player.getCombatDefinitions().setMeleeCombatExperience(5);
				else if (melee == 2)
					player.getCombatDefinitions().setMeleeCombatExperience(6);
				else if (melee == 3)
					player.getCombatDefinitions().setMeleeCombatExperience(7);
				else if (melee == 4)
					player.getCombatDefinitions().setMeleeCombatExperience(0);
				else if (melee == 5)
					player.getCombatDefinitions().setMeleeCombatExperience(1);
				else if (melee == 6)
					player.getCombatDefinitions().setMeleeCombatExperience(2);
				else if (melee == 7)
					player.getCombatDefinitions().setMeleeCombatExperience(3);
				if (type == Combat.RANGE_TYPE) {
					if (ranged == 0)
						player.getCombatDefinitions()
								.setRangedCombatExperience(2);
					else if (ranged == 1)
						player.getCombatDefinitions()
								.setRangedCombatExperience(3);
					else if (ranged == 2)
						player.getCombatDefinitions()
								.setRangedCombatExperience(0);
					else if (ranged == 3)
						player.getCombatDefinitions()
								.setRangedCombatExperience(1);
				} else if (type == Combat.MAGIC_TYPE) {
					if (magic == 0)
						player.getCombatDefinitions().setMagicCombatExperience(
								2);
					else if (magic == 1)
						player.getCombatDefinitions().setMagicCombatExperience(
								3);
					else if (magic == 2)
						player.getCombatDefinitions().setMagicCombatExperience(
								0);
					else if (magic == 3)
						player.getCombatDefinitions().setMagicCombatExperience(
								1);
				}
			}
			// player.getCombatDefinitions().setCombatExperienceStyle((componentId
			// - 37) / 2);
			else if (componentId == 50)
				player.getCombatDefinitions().switchAutoRelatie();
		} else if (interfaceId == 662) {
			if (componentId == 160) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getFamiliar().getBob().removeItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getFamiliar().getBob().removeItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getFamiliar().getBob().removeItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getFamiliar().getBob()
							.removeItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().put("bob_isRemove",
							Boolean.TRUE);
					player.getPackets().sendInputIntegerScript("Enter Amount:");
				}
			} else if (componentId == 3)
				player.getFamiliar().call();
			else if (componentId == 4)
				player.getFamiliar().getBob().takeBob();
			else if (componentId == 5)
				player.getFamiliar().renewFamiliar();
			else if (componentId == 6) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getDialogueManager().startDialogue("DismissD");
				else
					player.getFamiliar().sendDeath(player);
			} else if (componentId == 13) {
				player.getPackets().sendHideIComponent(662, 8, true);
				player.getPackets().sendHideIComponent(662, 19, false);
				player.getPackets().sendIComponentSettings(662, 160, 0, 50, 62);
				player.getInterfaceManager().refreshInterface(false);
			} else if (componentId == 33) {
				player.getPackets().sendHideIComponent(662, 8, false);
				player.getPackets().sendHideIComponent(662, 19, true);
				player.getPackets().sendIComponentSettings(662, 160, 0, 50, 62);
			} else if (componentId == 29)
				player.getFamiliar().call();
			else if (componentId == 32)
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getDialogueManager().startDialogue("DismissD");
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
				player.getFamiliar().sendDeath(player);
			else if (componentId == 40) {
				BeastOfBurden bob = player.getFamiliar().getBob();
				if (bob == null)
					player.getPackets().sendGameMessage(
							"You can't store items to this familiar.");
				else
					player.getFamiliar().getBob().takeBob();
			} else if (componentId == 43)
				player.getFamiliar().renewFamiliar();
			else if (componentId == 55) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
					player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().hasSpecialOn())
					player.getFamiliar().submitSpecial(player);
			}
		} else if (interfaceId == 1506) {
			if (componentId == 6)
				player.getInterfaceManager().sendCentralInterface(880);
			else if (componentId == 9)
				player.getFamiliar().getBob().takeBob();
			else if (componentId == 10)
				player.getFamiliar().renewFamiliar();
			else if (componentId == 25)
				player.getFamiliar().call();
			else if (componentId == 26)
				player.getDialogueManager().startDialogue("DismissD");
			else if (componentId == 23)
				player.getFamiliar().sendFollowerDetails();
		} else if (interfaceId == 327) { // combat style
			int melee = player.getVarsManager().getBitValue(1906);
			int ranged = player.getVarsManager().getBitValue(1907);
			int magic = player.getVarsManager().getBitValue(1908);
			if (componentId == 42) { // switch legacy mode
				player.getDialogueManager().sendConfirmDialogue(9,
						new ConfirmDialogue() {

							@Override
							public void process(int option) {
								if (option == 1
										&& !player.isInLegacyCombatMode())
									player.switchLegacyCombatMode();
							}

							@Override
							public void finish() {
							}

						});
			} else if (componentId == 18) {
				if (player.isInLegacyCombatMode()) {
					player.getDialogueManager().sendConfirmDialogue(9,
							new ConfirmDialogue() {

								@Override
								public void process(int option) {
									if (option == 1) {
										player.getCombatDefinitions()
												.setCombatMode(2);
										player.switchLegacyCombatMode();
									}
								}

								@Override
								public void finish() {
								}

							});
				} else
					player.getCombatDefinitions().setCombatMode(2);
			} else if (componentId == 48)
				submitSpecialRequest(player);
			else if (componentId == 19) {
				if (player.isInLegacyCombatMode()) {
					player.getDialogueManager().sendConfirmDialogue(9,
							new ConfirmDialogue() {

								@Override
								public void process(int option) {
									if (option == 1) {
										player.getCombatDefinitions()
												.setCombatMode(1);
										player.switchLegacyCombatMode();
									}
								}

								@Override
								public void finish() {
								}

							});
				} else
					player.getCombatDefinitions().setCombatMode(1);
			} else if (componentId == 91) {
				if (melee == 0)
					player.getCombatDefinitions().setMeleeCombatExperience(1);
				else if (melee == 1)
					player.getCombatDefinitions().setMeleeCombatExperience(0);
				else if (melee == 2)
					player.getCombatDefinitions().setMeleeCombatExperience(3);
				else if (melee == 3)
					player.getCombatDefinitions().setMeleeCombatExperience(2);
				else if (melee == 4)
					player.getCombatDefinitions().setMeleeCombatExperience(5);
				else if (melee == 5)
					player.getCombatDefinitions().setMeleeCombatExperience(4);
				else if (melee == 6)
					player.getCombatDefinitions().setMeleeCombatExperience(7);
				else if (melee == 7)
					player.getCombatDefinitions().setMeleeCombatExperience(6);

			} else if (componentId == 96) {
				if (melee == 0)
					player.getCombatDefinitions().setMeleeCombatExperience(2);
				else if (melee == 1)
					player.getCombatDefinitions().setMeleeCombatExperience(3);
				else if (melee == 2)
					player.getCombatDefinitions().setMeleeCombatExperience(0);
				else if (melee == 3)
					player.getCombatDefinitions().setMeleeCombatExperience(1);
				else if (melee == 4)
					player.getCombatDefinitions().setMeleeCombatExperience(6);
				else if (melee == 5)
					player.getCombatDefinitions().setMeleeCombatExperience(7);
				else if (melee == 6)
					player.getCombatDefinitions().setMeleeCombatExperience(4);
				else if (melee == 7)
					player.getCombatDefinitions().setMeleeCombatExperience(5);

			} else if (componentId == 101) {
				if (melee == 0)
					player.getCombatDefinitions().setMeleeCombatExperience(4);
				else if (melee == 1)
					player.getCombatDefinitions().setMeleeCombatExperience(5);
				else if (melee == 2)
					player.getCombatDefinitions().setMeleeCombatExperience(6);
				else if (melee == 3)
					player.getCombatDefinitions().setMeleeCombatExperience(7);
				else if (melee == 4)
					player.getCombatDefinitions().setMeleeCombatExperience(0);
				else if (melee == 5)
					player.getCombatDefinitions().setMeleeCombatExperience(1);
				else if (melee == 6)
					player.getCombatDefinitions().setMeleeCombatExperience(2);
				else if (melee == 7)
					player.getCombatDefinitions().setMeleeCombatExperience(3);

			} else if (componentId == 81) {
				if (ranged == 0)
					player.getCombatDefinitions().setRangedCombatExperience(1);
				else if (ranged == 1)
					player.getCombatDefinitions().setRangedCombatExperience(0);
				else if (ranged == 2)
					player.getCombatDefinitions().setRangedCombatExperience(3);
				else if (ranged == 3)
					player.getCombatDefinitions().setRangedCombatExperience(2);
			} else if (componentId == 86) {
				if (ranged == 0)
					player.getCombatDefinitions().setRangedCombatExperience(2);
				else if (ranged == 1)
					player.getCombatDefinitions().setRangedCombatExperience(3);
				else if (ranged == 2)
					player.getCombatDefinitions().setRangedCombatExperience(0);
				else if (ranged == 3)
					player.getCombatDefinitions().setRangedCombatExperience(1);
			} else if (componentId == 71) {
				if (magic == 0)
					player.getCombatDefinitions().setMagicCombatExperience(1);
				else if (magic == 1)
					player.getCombatDefinitions().setMagicCombatExperience(0);
				else if (magic == 2)
					player.getCombatDefinitions().setMagicCombatExperience(3);
				else if (magic == 3)
					player.getCombatDefinitions().setMagicCombatExperience(2);
			} else if (componentId == 76) {
				if (magic == 0)
					player.getCombatDefinitions().setMagicCombatExperience(2);
				else if (magic == 1)
					player.getCombatDefinitions().setMagicCombatExperience(3);
				else if (magic == 2)
					player.getCombatDefinitions().setMagicCombatExperience(0);
				else if (magic == 3)
					player.getCombatDefinitions().setMagicCombatExperience(1);
			} else if (componentId >= 21 && componentId <= 23)
				player.getCombatDefinitions().setCombatMode(componentId - 21);
			else if (componentId >= 29 && componentId <= 31)
				player.getCombatDefinitions().setCombatMode(componentId - 29);
			else if (componentId == 79)
				player.getCombatDefinitions().switchShowCombatModeIcon();
			else if (componentId == 60)
				player.getCombatDefinitions().switchAllowAbilityQueueing();
			else if (componentId == 117)
				player.getCombatDefinitions().switchManualSpellCasting();
		} else if (interfaceId == 755) {
			if (componentId == 44)
				player.getPackets().sendRootInterface(
						player.getInterfaceManager().hasRezizableScreen() ? 746
								: 548, 2);
		} else if (interfaceId == 20)
			SkillCapeCustomizer.handleSkillCapeCustomizer(player, componentId);
		else if (interfaceId == 1056) {
			if (componentId == 173)
				player.getInterfaceManager().sendCentralInterface(917);
		} else if (interfaceId == 329) {
			if (componentId == 0)
				player.getDialogueManager().startDialogue("OpenURLPrompt",
						"store");
			else if (componentId == 8)
				player.getInterfaceManager().removeScreenInterfaceBG();
		} else if (interfaceId == 1163 || interfaceId == 1164
				|| interfaceId == 1168 || interfaceId == 1170
				|| interfaceId == 1171 || interfaceId == 1173)
			player.getDominionTower().handleButtons(interfaceId, componentId,
					slotId, packetId);
		else if (interfaceId == 900)
			PlayerLook.handleMageMakeOverButtons(player, componentId);
		else if (interfaceId == 1420)
			PlayerLook.handleCharacterCustomizingButtons(player, componentId,
					slotId);
		else if (interfaceId == 1108 || interfaceId == 1109
				|| interfaceId == 1427)
			player.getFriendsIgnores().handleFriendChatButtons(interfaceId,
					componentId, packetId);
		else if (interfaceId == 1441 || interfaceId == 550
				|| interfaceId == 235)
			player.getFriendsIgnores().handleFriendListButtons(interfaceId,
					componentId);
		else if (interfaceId == 1089) {
			if (componentId == 30)
				player.getTemporaryAttributtes().put("clanflagselection",
						slotId);
			else if (componentId == 26) {

				Integer flag = (Integer) player.getTemporaryAttributtes()
						.remove("clanflagselection");
				player.stopAll();
				if (flag != null)
					ClansManager.setClanFlagInterface(player, flag);
			}
		} else if (interfaceId == 1096) {
			if (componentId == 47)
				ClansManager.viewClammateDetails(player, slotId);
			else if (componentId == 99)
				ClansManager.switchGuestsInChatCanEnterInterface(player);
			else if (componentId == 100)
				ClansManager.switchGuestsInChatCanTalkInterface(player);
			else if (componentId == 101)
				ClansManager.switchRecruitingInterface(player);
			else if (componentId == 102)
				ClansManager.switchClanTimeInterface(player);
			else if (componentId == 130)
				ClansManager.openClanMottifInterface(player);
			else if (componentId == 137)
				ClansManager.openClanMottoInterface(player);
			else if (componentId == 246) // fixed
											// until
											// here
				ClansManager.setTimeZoneInterface(player, -720 + slotId * 10);
			else if (componentId == 268)
				player.getTemporaryAttributtes().put("editclanmatejob", slotId);
			else if (componentId == 282)
				player.getTemporaryAttributtes()
						.put("editclanmaterank", slotId);
			else if (componentId == 315)
				ClansManager.kickClanmate(player);
			else if (componentId == 324)
				ClansManager.saveClanmateDetails(player);
			else if (componentId == 296)
				ClansManager.setWorldIdInterface(player, slotId);
			else if (componentId == 303)
				ClansManager.openForumThreadInterface(player);
			else if (componentId == 352)
				ClansManager.openNationalFlagInterface(player);
			else if (componentId == 119)
				ClansManager.showClanSettingsClanMates(player);
			else if (componentId == 126)
				ClansManager.showClanSettingsSettings(player);
			else if (componentId == 390)
				ClansManager.showClanSettingsPermissions(player);

			else if (componentId >= 395 && componentId <= 475) {
				int selectedRank = (componentId - 395) / 8;
				if (selectedRank == 10)
					selectedRank = 125;
				else if (selectedRank > 5)
					selectedRank = 100 + selectedRank - 6;
				ClansManager.selectPermissionRank(player, selectedRank);
			} else if (componentId == 489)
				ClansManager.selectPermissionTab(player, 1);
			else if (componentId == 498)
				ClansManager.selectPermissionTab(player, 2);
			else if (componentId == 506)
				ClansManager.selectPermissionTab(player, 3);
			else if (componentId == 514)
				ClansManager.selectPermissionTab(player, 4);
			else if (componentId == 522)
				ClansManager.selectPermissionTab(player, 5);

		} else if (interfaceId == 522) {
			if (componentId == 6)
				player.getInterfaceManager().sendMagicAbilities();
		} else if (interfaceId == 523) {
			if (componentId == 106)
				player.getInterfaceManager().sendMagicAbilities();
		} else if (interfaceId == 1105) {
			if (componentId == 63 || componentId == 66)
				ClansManager.setClanMottifTextureInterface(player,
						componentId == 66, slotId);
			else if (componentId == 35)
				ClansManager.openSetMottifColor(player, 0);
			else if (componentId == 80)
				ClansManager.openSetMottifColor(player, 1);
			else if (componentId == 92)
				ClansManager.openSetMottifColor(player, 2);
			else if (componentId == 104)
				ClansManager.openSetMottifColor(player, 3);
			else if (componentId == 120)
				player.stopAll();
		} else if (interfaceId == 1558) {
			if (componentId == 16) {
				if (itemId >= Utils.getItemDefinitionsSize())
					return;
				Item item = new Item(itemId);
				player.getPackets()
						.sendInterfaceMessage(
								interfaceId,
								componentId,
								0,
								slotId,
								ItemExamines.getExamine(item)
										+ (ItemConstants.isTradeable(item) ? "<br>GE guide price: "
												+ Utils.format(GrandExchange
														.getPrice(item.getId()))
												+ " gp"
												: ""));
			}
		} else if (interfaceId == 1560) {
			if (componentId == 102)
				player.getPlayerExamineManager().reportPlayer();
			else if (componentId == 22)
				player.getPlayerExamineManager().closeExamineDetails();
		} else if (interfaceId == 1561) {
			if (componentId == 21)
				player.getPlayerExamineManager().setPersonalMessage();
			else if (componentId == 35)
				player.setCurrentOptionsMenu(1561);
			else if (componentId == 40)
				player.getPlayerExamineManager().clearPersonalMessage();
			else if (componentId == 45)
				player.getPlayerExamineManager().switchPrivacyOn();
		} else if (interfaceId == 1446) {
			if (componentId == 108)
				player.getPlayerExamineManager().openExamineSettings();
			else if (componentId == 86)
				player.getPackets().sendGameMessage("Feature to be added...");
		} else if (interfaceId == 1406) {
			if (componentId == 16)
				player.getPackets()
						.sendOpenURL(
								"https://nocturne3.org/community/index.php?/forum/19-report-a-player/");
			else if (componentId == 24)
				player.getPackets().sendOpenURL(Settings.BUG_LINK);
		} else if (interfaceId == 1468) {
			if (componentId == 1)
				player.switchAlwaysChatOnMode();
			else if (componentId == 2)
				player.getFriendsIgnores().handleFriendChatButtons(interfaceId,
						componentId, packetId);
			else if (componentId == 6)
				player.setGameStatus(InterfaceManager.getNextStatus(player
						.getGameStatus()));
			else if (componentId == 7)
				player.setPublicStatus(InterfaceManager.getNextStatus(player
						.getPublicStatus()));
			else if (componentId == 8)
				player.setPersonalStatus(InterfaceManager.getNextStatus(player
						.getPersonalStatus()));
			else if (componentId == 9)
				player.setFriendsChatStatus(InterfaceManager
						.getNextStatus(player.getFriendsChatStatus()));
			else if (componentId == 10)
				player.setClanStatus(InterfaceManager.getNextStatus(player
						.getClanStatus()));
			else if (componentId == 12)
				player.setTradeStatus(InterfaceManager.getNextStatus(player
						.getTradeStatus()));
			else if (componentId == 13)
				player.setAssistStatus(InterfaceManager.getNextStatus(player
						.getAssistStatus()));
			else if (componentId == 14)
				player.switchProfanityFilter();
			else if (componentId == 15) {
				// broadcast
			} else if (componentId == 19) {
				// chat bag
			} else if (componentId == 20)
				PlayerReporting.report(player);
			else if (componentId == 25) {
				player.getPlayerExamineManager().openExamineSettings();
			}
		} else if (interfaceId == 1036) {
			if (componentId == 14) {
				// player.getPackets().sendGameMessage("You have requested for
				// help.");
			}
		} else if (interfaceId == 137) {
			if (componentId == 62 || componentId == 96) {
				if (!player.getInterfaceManager().containsInterface(1468))
					player.getInterfaceManager().sendExpandOptionsInterface(
							1468);
			} else if (componentId == 103)
				player.switchAlwaysChatOnMode();
			else if (componentId == 65)
				player.setGameStatus(InterfaceManager.getNextStatus(player
						.getGameStatus()));
			else if (componentId == 68)
				player.setPublicStatus(InterfaceManager.getNextStatus(player
						.getPublicStatus()));
			else if (componentId == 71)
				player.setPersonalStatus(InterfaceManager.getNextStatus(player
						.getPersonalStatus()));
			else if (componentId == 74)
				player.setFriendsChatStatus(InterfaceManager
						.getNextStatus(player.getFriendsChatStatus()));
			else if (componentId == 77)
				player.setClanStatus(InterfaceManager.getNextStatus(player
						.getClanStatus()));
			else if (componentId == 83)
				player.setTradeStatus(InterfaceManager.getNextStatus(player
						.getTradeStatus()));
			else if (componentId == 86)
				player.setAssistStatus(InterfaceManager.getNextStatus(player
						.getAssistStatus()));
			else if (componentId == 89)
				player.switchProfanityFilter();
			else if (componentId == 117 || componentId == 297)
				PlayerReporting.report(player);
			else if (componentId == 295)
				player.getInterfaceManager().openMenu(4, 5);
			else if (componentId == 118) {
				player.getInterfaceManager().sendCentralInterface(1036);
				player.getPackets().sendIComponentText(1036, 13,
						"Send help request to Staff Member");
				player.getPackets().sendIComponentText(1036, 18, "Yes");
			} else if (componentId == 114 || componentId == 254
					&& packetId == 7)
				if (player.getDonationManager().isLegendaryDonator()) {
					player.getPackets()
							.sendGameMessage(
									player.getChatBadge() ? "Chat title is no longer visible."
											: "Chat title is now visible.");
					player.setChatBadge(player.getChatBadge() ? false : true);
					player.getAppearence().generateAppearenceData();
				} else {
					player.getPackets().sendGameMessage(
							"You must be a donator to use this feature.");
				}
			else if (componentId == 122)
				player.getPlayerExamineManager().openExamineSettings();
			else if (componentId == 260) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setGameStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setGameStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setGameStatus(2);
			} else if (componentId == 265) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setPublicStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setPublicStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setPublicStatus(2);
			} else if (componentId == 270) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setPersonalStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setPersonalStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setPersonalStatus(2);
			} else if (componentId == 275) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setFriendsChatStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setFriendsChatStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setFriendsChatStatus(2);
			} else if (componentId == 280) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setClanStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setClanStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setClanStatus(2);
			} else if (componentId == 290) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setTradeStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setTradeStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setTradeStatus(2);
			} else if (componentId == 295) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.setAssistStatus(0);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.setAssistStatus(1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.setAssistStatus(2);
			} else if (componentId == 256) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.switchAlwaysChatOnMode();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.switchProfanityFilter();
					player.getPackets().sendGameMessage(
							"Your profanity filter is now "
									+ (player.isFilteringProfanity() ? "on"
											: "off") + " .");
				}
			}
		} else if (interfaceId == 1472) { // friends
											// chat
			if (componentId == 79 || componentId == 220) {
				if (!player.getInterfaceManager().containsInterface(1468))
					player.getInterfaceManager().sendExpandOptionsInterface(
							1468);
			} else if (componentId == 226) // always
											// on
				player.switchAlwaysChatOnMode();
			else if (componentId == 106)
				player.switchProfanityFilter();
			else if (componentId == 231 || componentId == 228)
				player.getFriendsIgnores().handleFriendChatButtons(interfaceId,
						componentId, packetId);
		} else if (interfaceId == 1471) { // clan
											// chat
			if (componentId == 81 || componentId == 1) {
				if (!player.getInterfaceManager().containsInterface(1468))
					player.getInterfaceManager().sendExpandOptionsInterface(
							1468);
			} else if (componentId == 227) // always
											// on
				player.switchAlwaysChatOnMode();
			else if (componentId == 108)
				player.switchProfanityFilter();
		} else if (interfaceId == 1467) { // private
											// chat
			if (componentId == 78) {
				if (!player.getInterfaceManager().containsInterface(1468))
					player.getInterfaceManager().sendExpandOptionsInterface(
							1468);
			} else if (componentId == 224) // always
											// on
				player.switchAlwaysChatOnMode();
			else if (componentId == 105)
				player.switchProfanityFilter();
		} else if (interfaceId == 1470) { // guest
											// clan
											// chat
			if (componentId == 185 || componentId == 220) {
				if (!player.getInterfaceManager().containsInterface(1468))
					player.getInterfaceManager().sendExpandOptionsInterface(
							1468);
			} else if (componentId == 226) // always
											// on
				player.switchAlwaysChatOnMode();
			else if (componentId == 212)
				player.switchProfanityFilter();
		} else if (interfaceId == 464) { // trade
											// and
											// assist
			if (componentId == 3 || componentId == 220) {
				if (!player.getInterfaceManager().containsInterface(1468))
					player.getInterfaceManager().sendExpandOptionsInterface(
							1468);
			} else if (componentId == 226) // always
											// on
				player.switchAlwaysChatOnMode();
			else if (componentId == 30)
				player.switchProfanityFilter();
		} else if (interfaceId == 1529) { // group
											// chat
			if (componentId == 78 || componentId == 219) {
				if (!player.getInterfaceManager().containsInterface(1468))
					player.getInterfaceManager().sendExpandOptionsInterface(
							1468);
			} else if (componentId == 225) // always
											// on
				player.switchAlwaysChatOnMode();
			else if (componentId == 105)
				player.switchProfanityFilter();
		} else if (interfaceId == 1110 || interfaceId == 1440
				|| interfaceId == 233 || interfaceId == 234) {
			ClansManager.handleClanChatButtons(player, interfaceId,
					componentId, slotId);
		} else if (interfaceId == 1079)
			player.closeInterfaces();
		else if (interfaceId == 374) {
			if (componentId >= 5 && componentId <= 9) {
				if (player.getActionManager().getAction() instanceof ViewingOrb)
					player.setNextWorldTile(new WorldTile(
							((ViewingOrb) player.getActionManager().getAction())
									.getTps()[componentId - 5]));
			} else if (componentId == 15)
				player.stopAll();
		} else if (interfaceId == 105 || interfaceId == 107
				|| interfaceId == 109 || interfaceId == 389
				|| interfaceId == 1666 || interfaceId == 651)
			for (int i = 0; i < Utils
					.getInterfaceDefinitionsComponentsSize(105); i++) {
				if (componentId == i) {
					player.getGeManager().handleButtons(interfaceId,
							componentId, slotId, packetId);
				}
			}

		else if (interfaceId == 1092) {
			player.stopAll();
			HomeTeleport.useLodestone(player, componentId);
		} else if (interfaceId == 1560) {
			if (componentId == 22)
				player.getInterfaceManager().removeWindowInterface(395);
		} else if (interfaceId == 1214)
			player.getSkills().handleSetupXPCounter(componentId);
		else if (interfaceId == 1508) {
			if (componentId == 12)
				player.getTimersManager().showTimer();
		} else if (interfaceId == 753) {
			if (componentId == 65)
				player.getTimersManager().openBossInfo(slotId / 2);
			else if (componentId == 89)
				player.getTimersManager().setBeastMenu(slotId);
			else if (componentId == 96)
				player.getTimersManager().teleportToLodestone();
		} else if (interfaceId == 583) {
			if (componentId == 13)
				player.getDoomsayerManager().toogleWarning(1);
			else if (componentId == 18)
				player.getDoomsayerManager().toogleWarning(2);
			else if (componentId == 14)
				player.getDoomsayerManager().toogleWarning(3);
			else if (componentId == 15)
				player.getDoomsayerManager().toogleWarning(4);
			else if (componentId == 17)
				player.getDoomsayerManager().toogleWarning(5);
			else if (componentId == 29)
				player.getDoomsayerManager().toogleWarning(7);
			else if (componentId == 28)
				player.getDoomsayerManager().toogleWarning(8);
			else if (componentId == 24)
				player.getDoomsayerManager().toogleWarning(9);
			else if (componentId == 26)
				player.getDoomsayerManager().toogleWarning(10);
			else if (componentId == 19)
				player.getDoomsayerManager().toogleWarning(11);
			else if (componentId == 27)
				player.getDoomsayerManager().toogleWarning(12);
			else if (componentId == 20)
				player.getDoomsayerManager().toogleWarning(14);
			else if (componentId == 21)
				player.getDoomsayerManager().toogleWarning(15);
			else if (componentId == 22)
				player.getDoomsayerManager().toogleWarning(16);
			else if (componentId == 25)
				player.getDoomsayerManager().toogleWarning(17);
			else if (componentId == 31)
				player.getDoomsayerManager().toogleWarning(18);
			else if (componentId == 30)
				player.getDoomsayerManager().toogleWarning(19);
			else if (componentId == 23)
				player.getDoomsayerManager().toogleWarning(20);
			else if (componentId == 32)
				player.getDoomsayerManager().toogleWarning(23);
			else if (componentId == 33)
				player.getDoomsayerManager().toogleWarning(24);
			else if (componentId == 34)
				player.getDoomsayerManager().toogleWarning(25);
			else if (componentId == 35)
				player.getDoomsayerManager().toogleWarning(26);
			else if (componentId == 37)
				player.getDoomsayerManager().toogleWarning(27);
			else if (componentId == 38)
				player.getDoomsayerManager().toogleWarning(30);
			else if (componentId == 36)
				player.getDoomsayerManager().toogleWarning(33);
			else if (componentId == 39)
				player.getDoomsayerManager().toogleWarning(22);
			else if (componentId == 40)
				player.getDoomsayerManager().toogleWarning(31);
			else if (componentId == 41)
				player.getDoomsayerManager().toogleWarning(34);
			else if (componentId == 42)
				player.getDoomsayerManager().toogleWarning(35);
			else if (componentId == 43)
				player.getDoomsayerManager().toogleWarning(36);
			// messages
			else if (componentId == 16)
				player.getDoomsayerManager().toogleMessage(
						DoomsayerManager.SINKHOLE_MESSAGE);
			else if (componentId == 44)
				player.getDoomsayerManager().toogleMessage(
						DoomsayerManager.GOBLIN_RAID_MESSAGE);
			else if (componentId == 45)
				player.getDoomsayerManager().toogleMessage(
						DoomsayerManager.DEMON_RAID_MESSAGE);
			else if (componentId == 46)
				player.getDoomsayerManager().toogleMessage(
						DoomsayerManager.WILDERNESS_WARBAND_MESSAGE);
			else if (componentId == 47)
				player.getDoomsayerManager().toogleMessage(
						DoomsayerManager.WORLD_EVENT_MESSAGE);

		} else if (interfaceId == 1311)
			player.getCosmeticsManager().handleButtons(componentId, slotId,
					itemId, packetId);
	}

	public static boolean sendRemove(Player player, int slotId, boolean toBank) {
		if (slotId >= 15)
			return false;
		player.stopAll(false, false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null)
			return true;
		else if (!player.getControllerManager().canRemoveEquip(slotId,
				item.getId()))
			return false;
		if (toBank) {
			int[] slots = player.getBank().getItemSlot(item.getId());
			if (slots == null && !player.getBank().hasBankSpace())
				return false;
			player.getBank()
					.addItem(
							item.getId(),
							slots == null ? player.getBank().getCurrentTab()
									: slots[0], item.getAmount(), true);
		} else if (!player.getInventory().addItem(item.getId(),
				item.getAmount()))
			return false;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearence().generateAppearenceData();
		if (slotId == Equipment.SLOT_WEAPON)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		else if (slotId == Equipment.SLOT_AURA)
			player.getAuraManager().removeAura();
		if (player.getInterfaceManager().containsInterface(1463))
			player.getEquipment().refreshEquipmentInterfaceBonuses();
		player.getActionbar().refreshButtons();
		// player.getPackets().sendSoundEffect(item.getDefinitions().getCSOpcode(118),
		// 0, 1);
		player.getPackets().sendExecuteScript(6989); // forces refresh item in
		// actionbar for
		// wear/remove option
		return true;
	}

	public static boolean wear(Player player, int slotId, int itemId,
			boolean fromBank, boolean ignore) {
		if (player.hasFinished() || player.isDead())
			return false;
		player.stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId)
			return false;
		if (ignore == false) {
			if (item.getDefinitions().isNoted()
					|| !item.getDefinitions().isWearItem(
							player.getAppearence().isMale()) && itemId != 4084
					&& itemId != 33262) {
				player.getPackets().sendGameMessage("You can't wear that.");
				return false;
			}
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		if (itemId == 4084)
			targetSlot = 3;
		if (targetSlot == -1 && itemId != 33262) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		if (!ItemConstants.canWear(item, player))
			return false;
		boolean isTwoHandedWeapon = targetSlot == 3
				&& Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon
				&& player.getEquipment().getItem(Equipment.SLOT_SHIELD) != null
				&& !player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage(
					"Not enough free space in your inventory.");
			return false;
		}
		HashMap<Integer, Integer> requiriments = item.getDefinitions()
				.getWearingSkillRequiriments();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120)
					continue;
				if (player.getSkills().getLevel(skillId) < level) {
					if (hasRequiriments)
						player.getPackets()
								.sendGameMessage(
										"You are not high enough level to use this item.");
					hasRequiriments = false;
					String name = Skills.SKILL_NAME[skillId].toLowerCase();
					player.getPackets().sendGameMessage(
							"You need to have a"
									+ (name.startsWith("a") ? "n" : "") + " "
									+ name + " level of " + level + ".");
				}

			}
		}
		if (!hasRequiriments)
			return false;
		if (!player.getControllerManager().canEquip(targetSlot, itemId))
			return false;
		if (MonkeyGreeGrees.transform(player, item.getId()))
			targetSlot = Equipment.SLOT_WEAPON;
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems()
						.add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null
					&& Equipment.isTwoHandedWeapon(player.getEquipment()
							.getItem(3))) {
				if (!player.getInventory().getItems()
						.add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null
				&& (itemId != player.getEquipment().getItem(targetSlot).getId() || !item
						.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory()
						.getItems()
						.set(slotId,
								new Item(player.getEquipment()
										.getItem(targetSlot).getId(), player
										.getEquipment().getItem(targetSlot)
										.getAmount()));
			} else
				player.getInventory()
						.getItems()
						.add(new Item(player.getEquipment().getItem(targetSlot)
								.getId(), player.getEquipment()
								.getItem(targetSlot).getAmount()));
			player.getEquipment().getItems().set(targetSlot, null);
		}
		if (targetSlot == Equipment.SLOT_AURA)
			player.getAuraManager().removeAura();
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot,
				targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		if (targetSlot == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		player.getCharges().wear(targetSlot);
		if (player.getInterfaceManager().containsInterface(1463))
			player.getEquipment().refreshEquipmentInterfaceBonuses();
		// player.getPackets().sendSoundEffect(item.getDefinitions().getCSOpcode(118),
		// 0, 1);
		player.getPackets().sendExecuteScript(6989); // forces refresh item in
		// actionbar for
		// wear/remove option
		return true;
	}

	public static void submitSpecialRequest(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				try {
					if (player.isDead())
						return;
					Item weapon = player.getEquipment().getItem(
							Equipment.SLOT_WEAPON);

					if (weapon == null
							|| !weapon.getDefinitions().hasSpecialAttack()) {
						player.getPackets()
								.sendGameMessage(
										"You can only do that with a weapon that can perform a special attack.");
						return;
					}

					if (player.hasInstantSpecial(weapon)) {
						player.performInstantSpecial(weapon);
						return;
					}
					player.getCombatDefinitions().switchUsingSpecialAttack();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		});
	}

	public static void processWear(Player player, int[] slotIds, boolean ignore) {
		if (player.hasFinished() || player.isDead())
			return;
		boolean worn = false;
		Item[] copy = player.getInventory().getItems().getItemsCopy();
		for (int slotId : slotIds) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				continue;
			if (wear(player, slotId, item.getId(), false, ignore)) {
				worn = true;
				if (item.getId() == 14484) { // d claws wear exeption
					int slot = player.getInventory().getItems()
							.lookupSlot(25555);
					if (slot != -1)
						wear(player, slot, item.getId(), false, ignore);
				}
			}
		}
		player.getInventory().refreshItems(copy);
		if (worn)
			player.getAppearence().generateAppearenceData();
	}

	public static void openItemsKeptOnDeath(Player player) {
		player.getInterfaceManager().setBackgroundInterface(false, 1626);
		sendItemsKeptOnDeath(player, false);
	}

	public static void resetWorldMapMark(Player player) {
		player.getHintIconsManager().removeAll();
		player.getVarsManager().sendVar(2807, 0);
	}

	public static boolean canKeepSakeItem(Player player, Item item) {
		int itemId = item.getId();
		if (item.getDefinitions().isNoted()
				|| !item.getDefinitions().isWearItem(
						player.getAppearence().isMale()) && itemId != 4084)
			return false;
		int targetSlot = Equipment.getItemSlot(itemId);
		if (itemId == 4084)
			targetSlot = 3;
		if (targetSlot == -1)
			return false;
		if (!ItemConstants.canWear(item, player))
			return false;

		HashMap<Integer, Integer> requiriments = item.getDefinitions()
				.getWearingSkillRequiriments();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120)
					continue;
				if (player.getSkills().getLevel(skillId) < level)
					hasRequiriments = false;
			}
		}
		if (!hasRequiriments)
			return false;
		return true;
	}

	public static void sendItemsKeptOnDeath(Player player, boolean wilderness) {
		boolean skulled = player.hasSkull();
		Integer[][] slots = GraveStone.getItemSlotsKeptOnDeath(player,
				wilderness, skulled
				/* player.getPrayer().isProtectingItem() */, false);
		Item[][] items = GraveStone.getItemsKeptOnDeath(player, slots);
		long riskedWealth = 0;
		long carriedWealth = 0;
		for (Item item : items[1])
			// lost items
			carriedWealth = riskedWealth += GrandExchange
					.getPrice(item.getId()) * item.getAmount();
		for (Item item : items[0])
			// kept items
			carriedWealth += GrandExchange.getPrice(item.getId())
					* item.getAmount();

		for (int i = 0; i < slots[0].length; i++)
			player.getVarsManager().sendVarBit(1671 + i, slots[0][i]);
		player.getVarsManager().sendVarBit(1676, slots[0].length);

		player.getVarsManager().sendVarBit(1675, wilderness ? 1 : 0);
		player.getVarsManager().sendVarBit(1678, skulled ? 1 : 0);
		StringBuffer text = new StringBuffer();
		text.append("The number of items kept on").append("<br>")
				.append("death is normally 3.").append("<br>");
		text.append("<br>").append("The maximum this can be").append("<br>")
				.append("boosted to is 5.").append("<br>").append("<br>");
		if (wilderness) {
			text.append("Your gravestone will not").append("<br>")
					.append("appear.");
		} else {
			int time = GraveStone.getMaximumTicks(player.getGraveStone());
			int seconds = (int) (time * 0.6);
			int minutes = seconds / 60;
			seconds -= minutes * 60;

			text.append("Gravestone:")
					.append("<br>")
					.append(ClientScriptMap.getMap(1099).getStringValue(
							player.getGraveStone()))
					.append("<br>")
					.append("<br>")
					.append("Initial duration:")
					.append("<br>")
					.append(minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
		}
		text.append("<br>")
				.append("<br>")
				.append("Carried wealth:")
				.append("<br>")
				.append(carriedWealth > Integer.MAX_VALUE ? "Too high!" : Utils
						.getFormattedNumber((int) carriedWealth))
				.append("<br>")
				.append("<br>")
				.append("Risked wealth:")
				.append("<br>")
				.append(riskedWealth / 10 > Integer.MAX_VALUE ? "Too high!"
						: Utils.getFormattedNumber((int) riskedWealth / 10))
				.append("<br>").append("<br>");
		if (wilderness) {
			text.append("Your hub will be set to:").append("<br>")
					.append("Edgeville.");
		} else {
			text.append("Current hub: "
					+ ClientScriptMap.getMap(3792).getStringValue(
							DeathEvent.getCurrentHub(player)));
		}
		player.getPackets().sendCSVarString(2369, text.toString());

		player.getPackets().sendIComponentSettings(17, 18, 0, 14, 1024);
		player.getPackets().sendIComponentSettings(17, 17, 1, 47, 1024);
		player.getPackets().sendIComponentSettings(17, 20, 1, 47, 1024);
		player.getPackets().sendIComponentSettings(17, 22, 1, 47, 1024);
	}

}