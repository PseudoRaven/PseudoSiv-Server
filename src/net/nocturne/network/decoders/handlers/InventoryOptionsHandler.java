package net.nocturne.network.decoders.handlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.actions.AlcoholicBeverages;
import net.nocturne.game.item.actions.AncientEffigies;
import net.nocturne.game.item.actions.Bonds;
import net.nocturne.game.item.actions.Coalbag;
import net.nocturne.game.item.actions.Consumables;
import net.nocturne.game.item.actions.Drinkables;
import net.nocturne.game.item.actions.Drinkables.Drink;
import net.nocturne.game.item.actions.ExplorerRing;
import net.nocturne.game.item.actions.ItemDyes;
import net.nocturne.game.item.actions.ItemSets;
import net.nocturne.game.item.actions.Lamps;
import net.nocturne.game.item.actions.LightSource;
import net.nocturne.game.item.actions.MysteryBox;
import net.nocturne.game.item.actions.OrnamentKits;
import net.nocturne.game.item.actions.PrizedPendant;
import net.nocturne.game.item.actions.SerensSymbol;
import net.nocturne.game.item.actions.SmithingCombinations;
import net.nocturne.game.item.actions.StrangeRock;
import net.nocturne.game.npc.clans.ClanVexillum;
import net.nocturne.game.npc.familiar.Familiar.SpecialAttack;
import net.nocturne.game.npc.others.PolyporeCreature;
import net.nocturne.game.npc.others.Revenant;
import net.nocturne.game.npc.vorago.Vorago;
import net.nocturne.game.npc.vorago.VoragoHandler;
import net.nocturne.game.player.ActionBar.MagicAbilityShortcut;
import net.nocturne.game.player.Equipment;
import net.nocturne.game.player.Inventory;
import net.nocturne.game.player.MoneyPouch;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.QuestManager.Quests;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Portables;
import net.nocturne.game.player.actions.Portables.Portable;
import net.nocturne.game.player.actions.skills.crafting.AccessorySmithing;
import net.nocturne.game.player.actions.skills.crafting.BattlestaffCrafting.CraftStaffAction;
import net.nocturne.game.player.actions.skills.crafting.GemCutting.CraftGemAction;
import net.nocturne.game.player.actions.skills.crafting.GlassBlowing.CraftGlassAction;
import net.nocturne.game.player.actions.skills.crafting.LeatherCrafting.CraftAction;
import net.nocturne.game.player.actions.skills.crafting.NoxiousWeaponCreation;
import net.nocturne.game.player.actions.skills.crafting.NoxiousWeaponCreation.CreateWeapon;
import net.nocturne.game.player.actions.skills.crafting.SirenicCrafting.CraftSirenicAction;
import net.nocturne.game.player.actions.skills.divination.DivinePlacing;
import net.nocturne.game.player.actions.skills.divination.WeavingEnergy.Energy;
import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringMagicCrafting.CraftMageDung;
import net.nocturne.game.player.actions.skills.farming.TreeSaplings;
import net.nocturne.game.player.actions.skills.firemaking.FireLighter;
import net.nocturne.game.player.actions.skills.firemaking.FireLighter.Lighters;
import net.nocturne.game.player.actions.skills.firemaking.Firemaking;
import net.nocturne.game.player.actions.skills.fletching.ArrowFletching;
import net.nocturne.game.player.actions.skills.fletching.ArrowFletching.FletchArrowAction;
import net.nocturne.game.player.actions.skills.fletching.BoltFletching;
import net.nocturne.game.player.actions.skills.fletching.BoltFletching.FletchBoltAction;
import net.nocturne.game.player.actions.skills.fletching.Fletching;
import net.nocturne.game.player.actions.skills.fletching.Fletching.FletchAction;
import net.nocturne.game.player.actions.skills.fletching.LogsFletching;
import net.nocturne.game.player.actions.skills.fletching.LogsFletching.FletchData;
import net.nocturne.game.player.actions.skills.herblore.Grinding.GrindAction;
import net.nocturne.game.player.actions.skills.herblore.Herblore;
import net.nocturne.game.player.actions.skills.herblore.Herblore.CleanAction;
import net.nocturne.game.player.actions.skills.herblore.WeaponPoison;
import net.nocturne.game.player.actions.skills.hunter.FlyingEntityHunter;
import net.nocturne.game.player.actions.skills.hunter.FlyingEntityHunter.FlyingEntities;
import net.nocturne.game.player.actions.skills.hunter.TrapAction;
import net.nocturne.game.player.actions.skills.invention.Invention.InventionAction;
import net.nocturne.game.player.actions.skills.magic.JewelleryEnchanting.JewelleryAction;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.actions.skills.prayer.Burying.Bone;
import net.nocturne.game.player.actions.skills.prayer.PrayerBooks;
import net.nocturne.game.player.actions.skills.prayer.Scattering;
import net.nocturne.game.player.actions.skills.runecrafting.Runecrafting;
import net.nocturne.game.player.actions.skills.runecrafting.TectonicCrafting.CraftTectonicAction;
import net.nocturne.game.player.actions.skills.slayer.Slayer;
import net.nocturne.game.player.actions.skills.smithing.DragonfireShield;
import net.nocturne.game.player.actions.skills.smithing.GodswordCreating;
import net.nocturne.game.player.actions.skills.smithing.Smithing;
import net.nocturne.game.player.actions.skills.smithing.SpiritshieldCreating;
import net.nocturne.game.player.actions.skills.summoning.Summoning;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.player.actions.skills.woodcutting.Nest;
import net.nocturne.game.player.content.DwarfMultiCannon;
import net.nocturne.game.player.content.FadingScreen;
import net.nocturne.game.player.content.ItemTransportation;
import net.nocturne.game.player.content.RecipeShop;
import net.nocturne.game.player.content.SkillCapeCustomizer;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.activities.minigames.Barrows;
import net.nocturne.game.player.content.activities.minigames.FightKiln;
import net.nocturne.game.player.content.activities.minigames.SorceressGarden;
import net.nocturne.game.player.content.activities.minigames.fistofguthix.MinigameManager;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.controllers.Wilderness;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.player.dialogues.impl.AmuletAttaching;
import net.nocturne.game.player.dialogues.impl.AttachingOrbsDialouge;
import net.nocturne.game.player.dialogues.impl.CombinationsD.Combinations;
import net.nocturne.game.player.dialogues.impl.SqirkFruitSqueeze;
import net.nocturne.game.player.dialogues.impl.SqirkFruitSqueeze.SqirkFruit;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.stream.InputStream;
import net.nocturne.utils.Color;
import net.nocturne.utils.Logger;
import net.nocturne.utils.Utils;

public class InventoryOptionsHandler {

	public static void handleItemOption2(final Player player, final int slotId, final int itemId, Item item) {
		if (player.isLocked() || player.isStunned() || player.getEmotesManager().isDoingEmote())
			return;
		else if (Firemaking.isFiremaking(player, itemId))
			return;
		else if (StrangeRock.teleport(player, itemId))
			return;
		if (itemId == 18337 || itemId == 27996)
			return;
		if (itemId == 30920) {
			player.getPackets().sendGameMessage("You have " + player.getSilverhawkFeathers() + "% charges left.");
			return;
		}
		if (itemId == ItemIdentifiers.BOND || itemId == ItemIdentifiers.BOND_UNTRADEABLE) {
			Bonds.openInterface(player, false);
			return;
		}
		if (ExplorerRing.handleOption(player, item, slotId, 2))
			return;
		if (itemId == 33262) {
			player.getDialogueManager().startDialogue("ShardBagD");
		}
		if (itemId == 15492 || itemId == 30656 || itemId == 30686 || itemId == 30716)
			player.getDialogueManager().startDialogue("EnchantedGemD",
					player.getSlayerManager().getCurrentMaster().getNPCId());
		GrindAction grind1 = GrindAction.getHerb(itemId);
		if (item.getDefinitions().containsInventoryOption(1, "Slice")) {
			player.getDialogueManager().startDialogue("GrindingD", item, grind1);
			return;
		} else if (itemId == 20709) {
			long currentTime = Utils.currentTimeMillis();
			if (player.getClanManager() == null) {
				player.getPackets().sendGameMessage("You must be in a clan to do that!");
				return;
			}
			if (player.getAttackedByDelay() + 2500 > currentTime) {
				player.getPackets().sendGameMessage("You can't do this while in combat.");
				return;
			}
			if (!player.getUsername().equalsIgnoreCase(ClanVexillum.getClanPlanterUsername(player))) {
				if (player.getTemporaryAttributtes().contains("placedClanVex")) {
					ClanVexillum vex = (ClanVexillum) player.getTemporaryAttributtes().remove("placedClanVex");
					vex.finish();
				}
				player.getTemporaryAttributtes().put("placedClanVex",
						new ClanVexillum(player, player.getClanManager().getClan(),
								new WorldTile(player.getX() + 1, player.getY(), player.getPlane())));
			} else
				player.getPackets().sendGameMessage("You have already placed a vexillum!");
		} else if (itemId == 4155)
			player.getSlayerManager().checkKillsLeft();
		else if (itemId == 18339)
			Coalbag.withdrawCoal(player, 1);
		else if (itemId == 15262)
			ItemSets.openSkillPack(player, itemId, 12183, 5000, player.getInventory().getAmountOf(itemId));
		else if (itemId == 15362)
			ItemSets.openSkillPack(player, itemId, 230, 50, player.getInventory().getAmountOf(itemId));
		else if (itemId == 15363)
			ItemSets.openSkillPack(player, itemId, 228, 50, player.getInventory().getAmountOf(itemId));
		else if (itemId == 15364)
			ItemSets.openSkillPack(player, itemId, 222, 50, player.getInventory().getAmountOf(itemId));
		else if (itemId == 15365)
			ItemSets.openSkillPack(player, itemId, 9979, 50, player.getInventory().getAmountOf(itemId));
		else if (itemId == 1225) {
			// player.getPackets().sendInputIntegerScript("What would you like
			// to do when you grow up?");
			// player.getTemporaryAttributtes().put("xformring", Boolean.TRUE);
		} else if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510 || itemId == 5511)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.emptyPouch(player, pouch);
			player.stopAll(false);
		} else if (itemId == 6583 || itemId == 7927) {
			AccessorySmithing.ringTransformation(player, itemId);
		} else if (item.getDefinitions().containsInventoryOption(1, "Extinguish")) {
			if (LightSource.extinguishSource(player, slotId, false)) {
			}
		} else {
			if (item.getId() == 33262 || item.getId() == 15492 || item.getId() == 30656 || item.getId() == 30686
					|| item.getId() == 30716) {

				return;
			} else
				handleWear(player, slotId, item, true);

		}
	}

	public static void handleWear(final Player player, final int slotId, Item item, boolean ignore) {
		if (player.isEquipDisabled())
			return;
		if (item.getId() == 22332) {
			player.getPackets().sendGameMessage("You can't wear this item.");
			return;
		}
		if (player.getSwitchItemCache().isEmpty()) {
			player.getSwitchItemCache().add(slotId);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					List<Integer> slots = player.getSwitchItemCache();
					int[] slot = new int[slots.size()];
					for (int i = 0; i < slot.length; i++)
						slot[i] = slots.get(i);
					player.getSwitchItemCache().clear();
					ButtonHandler.processWear(player, slot, ignore);
					player.stopAll(false, true, false);
				}
			});
		} else if (!player.getSwitchItemCache().contains(slotId)) {
			player.getSwitchItemCache().add(slotId);
		}
	}

	public static void dig(final Player player) {
		player.resetWalkSteps();
		player.setNextAnimation(new Animation(830));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
				if (player.getTreasureTrailsManager().useDig())
					return;
				if (Barrows.digIntoGrave(player))
					return;
				if (player.getX() == 3005 && player.getY() == 3376 || player.getX() == 2999 && player.getY() == 3375
						|| player.getX() == 2996 && player.getY() == 3377
						|| player.getX() == 2989 && player.getY() == 3378
						|| player.getX() == 2987 && player.getY() == 3387
						|| player.getX() == 2984 && player.getY() == 3387) {
					// mole
					player.setNextWorldTile(new WorldTile(1752, 5137, 0));
					player.getPackets()
							.sendGameMessage("You seem to have dropped down into a network of mole tunnels.");
					return;
				} else if (player.withinDistance(new WorldTile(2748, 3734, 0), 2)) {
					player.lock();
					player.setNextGraphics(new Graphics(80, 5, 60));
					FadingScreen.fade(player, 1000, () -> {
						player.unlock();
						player.setNextWorldTile(new WorldTile(2696, 10121, 0));
					});
					player.getPackets().sendGameMessage("You fall through the ground into a network of tunnels.");
					return;
				}
				player.getPackets().sendGameMessage("You find nothing.");
			}

		});
	}

	public static void handleItemOption1(final Player player, final int slotId, final int itemId, Item item) {
		if (player.isLocked() || player.isStunned() || player.getEmotesManager().isDoingEmote())
			return;
		player.stopAll(false);
		// if (!player.getControllerManager().canDropItem(item))
		// return;
		if (itemId == 24154) {
			if (player.getInventory().containsItem(24154, 1)) {
				player.getInventory().deleteItem(24154, 1);
				player.getTreasureHunter().handleEarnedKeys(1);
				player.getPackets().sendGameMessage("You have successfully redeemed a treasure hunter key!");
			}
			return;
		}
		
		if (itemId == ItemIdentifiers.SERENS_SYMBOL)
			SerensSymbol.Activate(player);
		if(itemId==ItemIdentifiers.SERENS_SYMBOL_UNF)
			SerensSymbol.joinPieces(player);
		if (itemId == 18337 || itemId == 27996)
			return;
		if (Drinkables.drink(player, item, slotId))
			return;
		else if (AlcoholicBeverages.drink(player, slotId))
			return;
		else if (Scattering.scatterAsh(player, itemId))
			return;
		else if (PrizedPendant.chooseSkill(player, itemId))
			return;
		else if (itemId == 31846)
			player.getDialogueManager().startDialogue("ReaperDialogue", 2);
		else if (itemId == ItemIdentifiers.MEILYR_POTION_RECIPE) {
			RecipeShop.handleRecipe(player);
			return;
		} /*
			 * else if (Pendants.forId(itemId) != null) {
			 * player.getPackets().sendGameMessage(Color.PINK,
			 * "Your prized pendant will now grant double exp for the next hour."
			 * ); player.getTemporaryAttributtes().put("PrizedPendant", 0);
			 * return; }
			 */
		Pouch sumPouch = Pouch.forId(itemId);
		if (sumPouch != null)
			Summoning.spawnFamiliar(player, sumPouch);
		if (itemId == 30920) {
			handleWear(player, slotId, item, false);
			return;
		}
		switch (item.getId()) {

		/*
		 * QUEST ITEMS
		 */

		case 24313:
			if (player.getControllerManager().getController() != null) {
				player.getPackets().sendGameMessage(
						"You must finish the story you are currently playing before entering the dream world.");
				return;
			} else if (player.getQuestManager().getQuestStage(Quests.SONG_FROM_THE_DEPTHS) == -2
					|| player.getQuestManager().getQuestStage(Quests.SONG_FROM_THE_DEPTHS) == -1) {
				player.getInventory().deleteItem(24313, 28);
				player.getPackets().sendGameMessage("Please submit a bug report telling us how you got that.");
				return;
			} else if (player.getX() < 2938 || player.getY() < 3190 || player.getX() > 2998 || player.getY() > 3260) {
				player.getPackets()
						.sendGameMessage("The potion won't have any affect as you are not close enough to Waylan.");
				return;
			} else {
				player.lock(6);
				player.setNextAnimationNoPriority(
						new Animation((player.getCombatDefinitions().isCombatStance() && !player.isInLegacyCombatMode())
								? 18003 : 18000));
				// player.getPackets().sendSound(4580, 0, 1);//potion drink
				// sound
				player.getInventory().deleteItem(24313, 1);
				player.getControllerManager().startController("SongFromTheDepthsSurface");
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you drink the potion, you fall asleep, but you still have some control over your body.");
			}
			break;

		/*
		 * Divination
		 */
		case 29313:
		case 29314:
		case 29315:
		case 29316:
		case 29317:
		case 29318:
		case 29319:
		case 29320:
		case 29321:
		case 29322:
		case 29323:
		case 29324:
			Energy energy = Energy.getEnergy(itemId);
			if (energy != null)
				player.getDialogueManager().startDialogue("WeavingD", item, energy);
			else
				player.getPackets().sendGameMessage("Weaving for this energy has not been added.");
			break;
		case ItemIdentifiers.SWAMP_TOAD:
			if (player.getInventory().containsItem(ItemIdentifiers.SWAMP_TOAD, 1)) {
				player.getInventory().deleteItem(ItemIdentifiers.SWAMP_TOAD, 1);
				player.getInventory().addItem(ItemIdentifiers.TOADS_LEGS, 1);
				player.getPackets().sendGameMessage("You detach the legs from the swamp toad.");
			}
			return;
		case 9044:
		case 9046:
		case 9048:
			player.getDialogueManager().startDialogue("PharaohSceptre", itemId);
			break;

		case 9050:
			player.getPackets().sendGameMessage("Your sceptre has has run out of charges.");
			break;

		case 22332:
			player.stopAll();
			player.getInterfaceManager().sendCentralInterface(1153);
			player.getPackets().sendIComponentText(1153, 139, "-1");
			player.getPackets().sendIComponentText(1153, 143, "-1");
			player.getPackets().sendIComponentText(1153, 134, "-1");
			break;

		case 32335:
			if (player.isHardcoreIronman())
				player.getDialogueManager().startDialogue("JarOfDivineLight");
			else
				player.getPackets().sendGameMessage("You need to be an Hardcore Ironman to use this item.");
			break;

		case 26384:
		case 6199:
			if (player.getInventory().containsItem(item.getId(), 1))
				MysteryBox.openBox(player, item);
			else
				player.getPackets()
						.sendGameMessage("You don't have the required items in your inventory to open this item.");
			break;
		case 985:
		case 987:
			if (player.getInventory().containsItems(new Item(985, 1), new Item(987, 1))) {
				player.getInventory().deleteItem(985, 1);
				player.getInventory().deleteItem(987, 1);
				player.getInventory().addItem(989, 1);
				player.getPackets().sendGameMessage("You join the two halves of the key together.");
			}
			break;
		case 20709:
			handleWear(player, slotId, item, true);
			break;

		case 23030:
			player.getPetManager().spawnPet(itemId, true);
			break;

		/*
		 * Quest Item Creating
		 */

		/*
		 * Dramen
		 */
		case 771:
			if (player.getInventory().containsItem(946, 1) || player.getToolbelt().containsItem(946)) {
				if (!player.getInventory().containsItem(771, 1)) {
					player.getPackets().sendGameMessage("You need a Dramen Branch to create a staff.");
					return;
				}
				player.setNextAnimation(new Animation(6702));
				player.getInventory().deleteItem(771, 1);
				player.getInventory().addItem(772, 1);
			} else
				player.getPackets().sendGameMessage("You do not have a knife in your inventory or toolbelt.");

			break;
		case 15484:
			player.getInterfaceManager().gazeOrbOfOculus();
			break;

		case 30915:
			if (player.getSilverhawkFeathers() >= 100) {
				player.getPackets().sendGameMessage("You may only fill max 500 Silverhawk feathers at a time.");
				return;
			}
			if (player.getInventory().containsItem(30915, 5)) {
				int amount = player.getInventory().getAmountOf(30915);
				int charge = amount / 5;
				player.getInventory().deleteItem(30915, amount);
				player.setSilverhawkFeathers(charge + player.getSilverhawkFeathers());
				player.getPackets().sendGameMessage("You have charged your Silverhawk boots with " + charge + "%.");
			} else
				player.getPackets().sendGameMessage("You need at least 5 Silverhawk feathers.");
			break;

		case 27234:
		case 27235:
		case 27236:
			player.getDDToken().claimToken(itemId);
			break;
		case 31770:
		case 31771:
		case 31772:
			if (System.currentTimeMillis() - player.delay > 800 && player.getInventory().containsItem(itemId, 1)) {
				player.delay = System.currentTimeMillis();
				int skill = itemId == 31770 ? 0 : itemId == 31771 ? 4 : 6;
				double xp = player.getSkills().getTotalLevel() * 5;
				player.getSkills().addXp(skill, xp);
				player.getInventory().deleteItem(slotId, item);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You received " + Utils.format(xp * Settings.getLampXpRate()) + " xp in "
								+ player.getSkills().getSkillName(skill) + ".");
			}
			break;
		case 3062:
			if (System.currentTimeMillis() - player.delay > 800 && player.getInventory().containsItem(itemId, 1)) {
				player.delay = System.currentTimeMillis();
				if (!player.getInventory().hasFreeSlots()) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
					return;
				}
				int ITEMS[] = { 23244, 23610, 26314, 1392, 537, 892, 11212, 560, 565, 386, 15273, 28465, 220 };
				int rewardId = ITEMS[(int) (Math.random() * ITEMS.length)];
				int amount = rewardId == 560 || rewardId == 565 ? Utils.random(50, 100)
						: rewardId == 892 || rewardId == 11212 ? Utils.random(20, 50) : Utils.random(5, 30);
				player.getInventory().deleteItem(itemId, 1);
				player.getInventory().addItem(rewardId, amount);
				player.getDialogueManager().startDialogue("SimpleMessage", "You received x" + Utils.format(amount) + " "
						+ ItemDefinitions.getItemDefinitions(rewardId).getName() + " from the strange box.");
			}
			return;
		}
		/*
		 * int leatherIndex = LeatherCraftingD.getIndex(item.getId()); if
		 * (leatherIndex != -1) {
		 * player.getDialogueManager().startDialogue("LeatherCraftingD",
		 * leatherIndex, false); return; }
		 */
		if (ExplorerRing.handleOption(player, item, slotId, 1))
			return;
		if (itemId == 11159) {
			if (!player.getInventory().containsItem(11159, 1) || player.getInventory().getFreeSlots() < 1)
				return;
			player.getInventory().deleteItem(11159, 1);
			Item items[] = { new Item(10150), new Item(10010), new Item(10006), new Item(10031), new Item(10029),
					new Item(596), new Item(10008) };
			for (Item xyz : items) {
				if (!player.getInventory().addItem(xyz.getId(), 1))
					World.addGroundItem(xyz, player, player, true, 180);
			}
		}
		if (itemId == 12853 || itemId == 12855) {
			if (MinigameManager.INSTANCE().fistOfGuthix().team(player) != null)
				MinigameManager.INSTANCE().fistOfGuthix().team(player).handleItems(player, item);
		}
		if (itemId == 33262) {
			player.getInterfaceManager().sendCentralInterface(1556);
		}
		if (itemId == 28626) { // vitalis
			if (player.getInventory().containsItem(28626, 1)) {
				player.getInventory().deleteItem(28626, 1);
				player.getInventory().addItem(28630, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33832) { // general awwdor
			if (player.getInventory().containsItem(33832, 1)) {
				player.getInventory().deleteItem(33832, 1);
				player.getInventory().addItem(33806, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33833) { // commander miniana
			if (player.getInventory().containsItem(33833, 1)) {
				player.getInventory().deleteItem(33833, 1);
				player.getInventory().addItem(33807, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33832) { // k'ril tinyroth
			if (player.getInventory().containsItem(33831, 1)) {
				player.getInventory().deleteItem(33831, 1);
				player.getInventory().addItem(33805, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33830) { // chick'ara
			if (player.getInventory().containsItem(33830, 1)) {
				player.getInventory().deleteItem(33830, 1);
				player.getInventory().addItem(33804, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33834) { // nexterminator
			if (player.getInventory().containsItem(33834, 1)) {
				player.getInventory().deleteItem(33834, 1);
				player.getInventory().addItem(33808, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33835) { // mallory
			if (player.getInventory().containsItem(33835, 1)) {
				player.getInventory().deleteItem(33835, 1);
				player.getInventory().addItem(33810, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33836) { // ellie
			if (player.getInventory().containsItem(33836, 1)) {
				player.getInventory().deleteItem(33836, 1);
				player.getInventory().addItem(33811, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33837) { // corporeal puppy
			if (player.getInventory().containsItem(33837, 1)) {
				player.getInventory().deleteItem(33837, 1);
				player.getInventory().addItem(33812, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33838) { // molly
			if (player.getInventory().containsItem(33838, 1)) {
				player.getInventory().deleteItem(33838, 1);
				player.getInventory().addItem(33813, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33839) { // shrimpy
			if (player.getInventory().containsItem(33839, 1)) {
				player.getInventory().deleteItem(33839, 1);
				player.getInventory().addItem(33814, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33840) { // kalphite grubling
			if (player.getInventory().containsItem(33840, 1)) {
				player.getInventory().deleteItem(33840, 1);
				player.getInventory().addItem(33815, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33841) { // kalphite grublet
			if (player.getInventory().containsItem(33841, 1)) {
				player.getInventory().deleteItem(33841, 1);
				if (player.getBank().containsItem(33816)) {
					player.getInventory().addItem(33817, 1);
				} else {
					player.getInventory().addItem(33816, 1);
				}
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33842) { // King black dragonling
			if (player.getInventory().containsItem(33842, 1)) {
				player.getInventory().deleteItem(33842, 1);
				player.getInventory().addItem(33818, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33843) { // legio primulus
			if (player.getInventory().containsItem(33843, 1)) {
				player.getInventory().deleteItem(33843, 1);
				player.getInventory().addItem(33819, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33844) { // legio secundulus
			if (player.getInventory().containsItem(33844, 1)) {
				player.getInventory().deleteItem(33844, 1);
				player.getInventory().addItem(33820, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33845) { // legio tertioulus
			if (player.getInventory().containsItem(33845, 1)) {
				player.getInventory().deleteItem(33845, 1);
				player.getInventory().addItem(33821, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33846) { // legio quartulus
			if (player.getInventory().containsItem(33846, 1)) {
				player.getInventory().deleteItem(33846, 1);
				player.getInventory().addItem(33822, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33847) { // legio quintulus
			if (player.getInventory().containsItem(33847, 1)) {
				player.getInventory().deleteItem(33847, 1);
				player.getInventory().addItem(33823, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33848) { // legio legio sextulus
			if (player.getInventory().containsItem(33848, 1)) {
				player.getInventory().deleteItem(33848, 1);
				player.getInventory().addItem(33824, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33849) { // queen black dragonling
			if (player.getInventory().containsItem(33849, 1)) {
				player.getInventory().deleteItem(33849, 1);
				player.getInventory().addItem(33825, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33850) { // prime hatchling
			if (player.getInventory().containsItem(33850, 1)) {
				player.getInventory().deleteItem(33850, 1);
				player.getInventory().addItem(33826, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33851) { // rex hatchling
			if (player.getInventory().containsItem(33851, 1)) {
				player.getInventory().deleteItem(33851, 1);
				player.getInventory().addItem(33827, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33852) { // supreme hatchling
			if (player.getInventory().containsItem(33852, 1)) {
				player.getInventory().deleteItem(33852, 1);
				player.getInventory().addItem(33828, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 33716) { // bombi
			if (player.getInventory().containsItem(33716, 1)) {
				player.getInventory().deleteItem(33716, 1);
				player.getInventory().addItem(33817, 1);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"As you inspect the " + item.getName() + ", it magically transforms into a creature!");
				player.setNextGraphics(new Graphics(1935));
			}
			return;
		}
		if (itemId == 29294) {
			DivinePlacing.placeDivine(player, itemId, 87285, 34107, 1, 14);
		} else if (itemId == ItemIdentifiers.BOND || itemId == ItemIdentifiers.BOND_UNTRADEABLE) {
			Bonds.redeem(player);
		} else if (itemId == 29295) {
			DivinePlacing.placeDivine(player, itemId, 87286, 57572, 15, 14);
		} else if (itemId == 29296) {
			DivinePlacing.placeDivine(player, itemId, 87287, 87266, 30, 14);
		} else if (itemId == 29297) {
			DivinePlacing.placeDivine(player, itemId, 87288, 87267, 55, 14);
		} else if (itemId == 29298) {
			DivinePlacing.placeDivine(player, itemId, 87289, 87268, 70, 14);
		} else if (itemId == 29299) {
			DivinePlacing.placeDivine(player, itemId, 87290, 87269, 85, 14);
		} else if (itemId == 28600 || itemId == 28602 || itemId == 28604)
			if (player.getInventory().containsItems(new Item(28600), new Item(28602), new Item(28604))) { // checks
				// inventory
				// for
				// 3
				// pieces
				player.getInventory().deleteItem(28600, 1);
				player.getInventory().deleteItem(28602, 1);
				player.getInventory().deleteItem(28604, 1);
				player.getInventory().addItem(28606, 1);
				player.getPackets().sendGameMessage("You create a Maul of Omens from your weapon pieces.");
				return;
			} else {
				player.getPackets().sendGameMessage("You need all 3 pieces of the " + "Maul to combine it."); // if
				// you
				// don't
				// have
				// 3
				// pieces
			}
		// divine trees
		else if (itemId == 29304) {
			DivinePlacing.placeDivine(player, itemId, 87295, 87274, 1, 8);
		} else if (itemId == 29305) {
			DivinePlacing.placeDivine(player, itemId, 87296, 87275, 15, 8);
		} else if (itemId == 29306) {
			DivinePlacing.placeDivine(player, itemId, 87297, 87276, 30, 8);
		} else if (itemId == 29307) {
			DivinePlacing.placeDivine(player, itemId, 87298, 87277, 45, 8);
		} else if (itemId == 29308) {
			DivinePlacing.placeDivine(player, itemId, 87299, 87278, 60, 8);
		} else if (itemId == 29309) {
			DivinePlacing.placeDivine(player, itemId, 87300, 87279, 75, 8);
		} else if (itemId >= 27153 && itemId <= 27155) {
			if (System.currentTimeMillis() - player.delay > 500 && player.getInventory().containsItem(itemId, 1)) {
				player.delay = System.currentTimeMillis();
				int a = Utils.random(750, 8000);
				int b = Utils.random(1, 84);
				int c = player.getSkills().getTotalLevel();
				int d = itemId == 27155 ? Utils.random(35, 133)
						: itemId == 27154 ? Utils.random(11, 55) : Utils.random(4, 20);
				int amount = a + (c - b) * d;
				player.getInventory().deleteItem(itemId, 1);
				player.getMoneyPouch().setAmount(amount, false);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You find " + Utils.format(amount) + " coins inside the " + item.getName().toLowerCase() + ".");
			}
		}
		// portables
		else if (itemId == 31041) {
			Portables.deployPortable(player, itemId, Portable.PORTABLEFORGE.getId(), Portable.PORTABLEFORGE.getId());
		} else if (itemId == 31042) {
			Portables.deployPortable(player, itemId, Portable.PORTABLERANGE.getId(), Portable.PORTABLERANGE.getId());
		} else if (itemId == 31043) {
			Portables.deployPortable(player, itemId, Portable.PORTABLESAWMILL.getId(),
					Portable.PORTABLESAWMILL.getId());
		} else if (itemId == 31044) {
			Portables.deployPortable(player, itemId, Portable.PORTABLEWELL.getId(), Portable.PORTABLEWELL.getId());
		} else if (itemId == 25205) {
			Portables.deployPortable(player, itemId, Portable.PORTABLEBANK.getId(), Portable.PORTABLEBANK.getId());
		}
		// herbolore
		else if (itemId == 29310) {
			DivinePlacing.placeDivine(player, itemId, 87301, 87280, 9, 15);
		} else if (itemId == 29311) {
			DivinePlacing.placeDivine(player, itemId, 87302, 87281, 44, 15);
		} else if (itemId == 29312) {
			DivinePlacing.placeDivine(player, itemId, 87303, 87282, 67, 15);
		}
		// hunting
		else if (itemId == 29300) {
			DivinePlacing.placeDivine(player, itemId, 87291, 87270, 1, 21);
		} else if (itemId == 29301) {
			DivinePlacing.placeDivine(player, itemId, 87292, 87271, 1, 21);
		} else if (itemId == 29302) {
			DivinePlacing.placeDivine(player, itemId, 87293, 87272, 23, 21);
		} else if (itemId == 29303) {
			DivinePlacing.placeDivine(player, itemId, 87294, 87273, 53, 21);
		}
		// fishing
		else if (itemId == 31080) {
			DivinePlacing.placeDivine(player, itemId, 90232, 90223, 10, 10);
		} else if (itemId == 31081) {
			DivinePlacing.placeDivine(player, itemId, 90233, 90224, 10, 10);
		} else if (itemId == 31082) {
			DivinePlacing.placeDivine(player, itemId, 90234, 90225, 20, 10);
		} else if (itemId == 31083) {
			DivinePlacing.placeDivine(player, itemId, 90235, 90226, 30, 10);
		} else if (itemId == 31084) {
			DivinePlacing.placeDivine(player, itemId, 90236, 90227, 40, 10);
		} else if (itemId == 31085) {
			DivinePlacing.placeDivine(player, itemId, 90237, 90228, 50, 10);
		} else if (itemId == 31086) {
			DivinePlacing.placeDivine(player, itemId, 90238, 90229, 76, 10);
		} else if (itemId == 31087) {
			DivinePlacing.placeDivine(player, itemId, 90239, 90230, 85, 10);
		} else if (itemId == 31088) {
			DivinePlacing.placeDivine(player, itemId, 90240, 90231, 90, 10);
		} else if (itemId == 18339)
			player.getPackets().sendGameMessage(Coalbag.getCoal(player));
		// div not being used
		else if (itemId == 31310) {
			DivinePlacing.placeDivine(player, itemId, 66526, 66528, 65, 1);
		} else if (itemId == 31311) {
			DivinePlacing.placeDivine(player, itemId, 66529, 66531, 65, 1);
		} else if (player.getTreasureTrailsManager().useItem(item, slotId))
			return;
		else if (Consumables.eat(player, slotId, item))
			return;
		else if (itemId == 2574)
			player.getTreasureTrailsManager().useSextant();
		else if (item.getId() == 20667)
			Magic.useVecnaSkull(player);
		else if (item.getId() == 25205) {
			if (!World.isTileFree(player.getPlane(), player.getX(), player.getY() - 1, 3)) {
				player.getPackets().sendGameMessage("You need clear space outside in order to place a deposit box.");
				return;
			}
			if (player.getControllerManager().getController() != null
					&& !(player.getControllerManager().getController() instanceof Wilderness)) {
				player.getPackets().sendGameMessage("You can't set a deposit box here.");
				return;
			}
			player.getInventory().deleteItem(slotId, item);
			player.setNextAnimation(new Animation(832));
			player.lock(1);
			World.spawnObjectTemporary(
					new WorldObject(73268, 10, 0, player.getX() + 1, player.getY(), player.getPlane()), 3600 * 1000);
		} else if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.fillPouch(player, pouch);
			return;
		} else if (itemId == 952) {// spade
			dig(player);
			return;
		} else if (itemId == 10952) {
			if (Slayer.isUsingBell(player))
				return;
		} else if (TrapAction.isTrap(player, new WorldTile(player), itemId))
			return;
		else if (Bone.forId(itemId) != null) {
			Bone.bury(player, slotId);
			return;
		} else if (Magic.useTabTeleport(player, itemId))
			return;
		// else if (item.getId() == 22370)
		// Summoning.openDreadNipSelection(player);
		else if (item.getId() == 7509 || item.getId() == 7510) {
			player.setNextForceTalk(new ForceTalk("Ow! It nearly broke my tooth!"));
			player.getPackets().sendGameMessage("The rock cake resists all attempts to eat it.");
			player.applyHit(new Hit(
					player, player.getHitpoints() - 10 < 35
							? player.getHitpoints() - 35 < 0 ? 0 : player.getHitpoints() - 35 : 10,
					HitLook.REGULAR_DAMAGE));

		} else if (ItemTransportation.transportationDialogue(player, item, true))
			return;
		else if (Lamps.isSelectable(itemId) || Lamps.isSkillLamp(itemId))
			Lamps.processLampClick(player, slotId, itemId, item.getId() == 20935 ? 60 : 1);
		/*
		 * else if (FallenStars.isSelectable(itemId) ||
		 * FallenStars.isStarLamp(itemId) || FallenStars.isOtherStar(itemId))
		 * FallenStars.processClick(player, slotId, itemId);
		 */
		else if (LightSource.lightSource(player, slotId))
			return;
		else if (LightSource.extinguishSource(player, slotId, false))
			return;
		else if (StrangeRock.inspect(player, itemId))
			return;
		else if (StrangeRock.lookAt(player, slotId, itemId))
			return;
		else if (itemId == 299) {
			if (player.withinDistance(Settings.HOME_LOCATION, 120)) {
				player.getPackets().sendGameMessage("You can't plant flowers here.");
				return;
			} else if (player.isCanPvp()) {
				player.getPackets().sendGameMessage("You cant plant a seed while doing this action.");
				return;
			} else if (World.getStandartObject(player) != null) {
				player.getPackets().sendGameMessage("You can't plant a flower here.");
				return;
			}
			player.setNextAnimation(new Animation(827));
			final WorldObject object = new WorldObject(2980 + Utils.random(8), 10, 0, player.getX(), player.getY(),
					player.getPlane());
			World.spawnObjectTemporary(object, 25000);
			player.getInventory().deleteItem(299, 1);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
						if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
							if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
								if (!player.addWalkSteps(player.getX(), player.getY() - 1, 1))
									return;
					player.getDialogueManager().startDialogue("FlowerPickD", object);
				}
			}, 2);
		} else if (itemId == 4251)
			Magic.useEctoPhial(player, item);
		else if (itemId == 15262)
			ItemSets.openSkillPack(player, itemId, 12183, 5000, 1);
		else if (itemId == 15362)
			ItemSets.openSkillPack(player, itemId, 230, 50, 1);
		else if (itemId == 15363)
			ItemSets.openSkillPack(player, itemId, 228, 50, 1);
		else if (itemId == 15364)
			ItemSets.openSkillPack(player, itemId, 222, 50, 1);
		else if (itemId == 15365)
			ItemSets.openSkillPack(player, itemId, 9979, 50, 1);
		else if (itemId == 15367)
			ItemSets.openSkillPack(player, itemId, 5419, 50, 1);
		else if (itemId == 15366)
			ItemSets.openSkillPack(player, itemId, 5377, 50, 1);
		else if (itemId == 2798 || itemId == 3565 || itemId == 3576 || itemId == 19042)
			player.getTreasureTrailsManager().openPuzzle(itemId);
		else if (itemId == 22445)
			player.getDialogueManager().startDialogue("NeemDrupeSqueeze");

		if (itemId == 32063 || itemId == 32064 || itemId == 32065 || itemId == 32066 || itemId == 32067
				|| itemId == 32068) {
			if (player.getInventory().containsItems(new Item[] { new Item(32063), new Item(32064), new Item(32065),
					new Item(32066), new Item(32067), new Item(32068) })) {
				player.getInventory().deleteItem(32063, 1);
				player.getInventory().deleteItem(32064, 1);
				player.getInventory().deleteItem(32065, 1);
				player.getInventory().deleteItem(32066, 1);
				player.getInventory().deleteItem(32067, 1);
				player.getInventory().deleteItem(32068, 1);
				player.getEquipment().getItems().set(Equipment.SLOT_CAPE, new Item(32052));
				player.getEquipment().refresh(Equipment.SLOT_CAPE);
				player.getAppearence().generateAppearenceData();
				player.getInventory().addItem(32056, 1);
				player.setNextAnimation(new Animation(24503));
				player.setNextGraphics(new Graphics(5120));
				GameExecutorManager.slowExecutor.schedule(new Runnable() {

					@Override
					public void run() {
						player.setNextAnimation(new Animation(24494));
						player.setNextGraphics(new Graphics(5113));
					}
				}, 2400, TimeUnit.MILLISECONDS);
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You don't have all the shards yet to assemble the Gatherer's cape.");
			}
		}
		if (itemId == 32069 || itemId == 32070 || itemId == 32071 || itemId == 32072 || itemId == 32073
				|| itemId == 32074 || itemId == 32075 || itemId == 32076) {
			if (player.getInventory().containsItems(new Item[] { new Item(32069), new Item(32070), new Item(32071),
					new Item(32072), new Item(32073), new Item(32074), new Item(32075), new Item(32076) })) {
				player.getInventory().deleteItem(32069, 1);
				player.getInventory().deleteItem(32070, 1);
				player.getInventory().deleteItem(32071, 1);
				player.getInventory().deleteItem(32072, 1);
				player.getInventory().deleteItem(32073, 1);
				player.getInventory().deleteItem(32074, 1);
				player.getInventory().deleteItem(32075, 1);
				player.getInventory().deleteItem(32076, 1);
				player.getEquipment().getItems().set(Equipment.SLOT_CAPE, new Item(32053));
				player.getEquipment().refresh(Equipment.SLOT_CAPE);
				player.getAppearence().generateAppearenceData();
				player.getInventory().addItem(32057, 1);
				player.setNextAnimation(new Animation(24503));
				player.setNextGraphics(new Graphics(5119));
				GameExecutorManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						player.setNextAnimation(new Animation(24496));
						player.setNextGraphics(new Graphics(5115));
						player.setNextGraphics(new Graphics(5114));
					}
				}, 2400, TimeUnit.MILLISECONDS);

			} else {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You don't have all the shards yet to assemble the Combatant's cape.");
			}
		}
		if (itemId == 32077 || itemId == 32078 || itemId == 32079 || itemId == 32080 || itemId == 32081
				|| itemId == 32082 || itemId == 32083 || itemId == 32084) {
			if (player.getInventory().containsItems(new Item[] { new Item(32077), new Item(32078), new Item(32079),
					new Item(32080), new Item(32081), new Item(32082), new Item(32083), new Item(32084) })) {
				player.getInventory().deleteItem(32077, 1);
				player.getInventory().deleteItem(32078, 1);
				player.getInventory().deleteItem(32079, 1);
				player.getInventory().deleteItem(32080, 1);
				player.getInventory().deleteItem(32081, 1);
				player.getInventory().deleteItem(32082, 1);
				player.getInventory().deleteItem(32083, 1);
				player.getInventory().deleteItem(32084, 1);
				player.getEquipment().getItems().set(Equipment.SLOT_CAPE, new Item(32054));
				player.getEquipment().refresh(Equipment.SLOT_CAPE);
				player.getAppearence().generateAppearenceData();
				player.getInventory().addItem(32058, 1);
				player.setNextAnimation(new Animation(24503));
				player.setNextGraphics(new Graphics(5118));
				GameExecutorManager.slowExecutor.schedule(new Runnable() {

					@Override
					public void run() {
						player.setNextAnimation(new Animation(24501));
						player.setNextGraphics(new Graphics(5117));
					}
				}, 2400, TimeUnit.MILLISECONDS);
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You don't have all the shards yet to assemble the Artisan's cape.");
			}
		}
		if (itemId == 32085 || itemId == 32086 || itemId == 32087 || itemId == 32088) {
			if (player.getInventory()
					.containsItems(new Item[] { new Item(32085), new Item(32086), new Item(32087), new Item(32088) })) {
				player.getInventory().deleteItem(32085, 1);
				player.getInventory().deleteItem(32086, 1);
				player.getInventory().deleteItem(32087, 1);
				player.getInventory().deleteItem(32088, 1);
				player.getEquipment().getItems().set(Equipment.SLOT_CAPE, new Item(32055));
				player.getEquipment().refresh(Equipment.SLOT_CAPE);
				player.getAppearence().generateAppearenceData();
				player.getInventory().addItem(32059, 1);
				player.setNextAnimation(new Animation(24503));
				player.setNextGraphics(new Graphics(5121));
				GameExecutorManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						player.setNextAnimation(new Animation(24499));
						player.setNextGraphics(new Graphics(5116));
					}
				}, 2400, TimeUnit.MILLISECONDS);
			} else

			{
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You don't have all the shards yet to assemble the Supporter's cape.");
			}
		} else if (item.getId() == ItemIdentifiers.SHELL_CHIPPINGS) {
			if (!player.getSkills().hasLevel(Skills.CRAFTING, 91))
				return;
			if (player.getInventory().containsItem(ItemIdentifiers.SHELL_CHIPPINGS, 5)) {
				player.getInventory().deleteItem(ItemIdentifiers.SHELL_CHIPPINGS, 5);
				player.getInventory().addItem(ItemIdentifiers.TORTLE_SHELL_BOWL, 1);
				player.getSkills().addXp(Skills.CRAFTING, 75);
				player.getPackets().sendGameMessage("You craft the shell chippings into a tortle shell bowl.");
			} else
				player.getPackets()
						.sendGameMessage("You need four shell chippings in order to create a tortle shell bowl.");
			return;
		} else if (item.getId() == ItemIdentifiers.TORTLE_SHELL_BOWL) {
			if (player.getInventory().containsItems(new Item(ItemIdentifiers.TORTLE_SHELL_BOWL, 1),
					new Item(ItemIdentifiers.BUNDLE_OF_BAMBOO, 1), new Item(ItemIdentifiers.RUMBERRY, 1),
					new Item(ItemIdentifiers.FISH_OIL, 1), new Item(ItemIdentifiers.SEA_SALT, 1))) {
				player.getInventory().removeItems(new Item(ItemIdentifiers.TORTLE_SHELL_BOWL, 1),
						new Item(ItemIdentifiers.BUNDLE_OF_BAMBOO, 1), new Item(ItemIdentifiers.RUMBERRY, 1),
						new Item(ItemIdentifiers.FISH_OIL, 1), new Item(ItemIdentifiers.SEA_SALT, 1));
				player.getInventory().addItem(ItemIdentifiers.UNCOOKED_ARC_GUMBO, 1);
				player.getPackets().sendGameMessage("You create an uncooked arc gumbo.");
			} else
				player.getPackets().sendGameMessage("You do not have the proper ingredients to make this.");
			return;
		}

		/**
		 * Skill Actions.
		 */
		else if ((item.getDefinitions().containsInventoryOption(0, "Craft")
				|| item.getDefinitions().containsInventoryOption(0, "Fletch"))
				&& SkillsDialogue.selectTool(player, item.getId())) {
			return;
		}
		CraftGemAction gem = CraftGemAction.getBar(itemId);
		if (itemId == 1625 || itemId == 1627 || itemId == 1629 || itemId == 1623 || itemId == 1621 || itemId == 1619
				|| itemId == 1617 || itemId == 1631 || itemId == 6571 || itemId == 31853 || itemId == 2859
				|| itemId == 1609 || itemId == 1611 || itemId == 411 || itemId == 1613 || itemId == 1607
				|| itemId == 1605 || itemId == 1603 || itemId == 1601 || itemId == 1615 || itemId == 6571
				|| itemId == 31855) {
			if (gem != null)
				player.getDialogueManager().startDialogue("GemCuttingD", item, gem);
			return;
		}
		CraftStaffAction staff = CraftStaffAction.getBar(itemId);
		if (itemId == 571 || itemId == 575 || itemId == 569 || itemId == 573 || itemId == 21775) {
			if (staff != null) {
				player.getDialogueManager().startDialogue("BattlestaffCraftingD", item, staff);
				return;
			}
		}
		CraftAction craft = CraftAction.getBar(itemId);
		if (itemId == 1741 || itemId == 1743 || itemId == 25545 || itemId == 25547 || itemId == 25551 || itemId == 25549
				|| itemId == 6289 || itemId == 1745 || itemId == 2505 || itemId == 2507 || itemId == 2509
				|| itemId == 24374 || itemId == 10820) {
			if (craft != null) {
				player.getDialogueManager().startDialogue("LeatherCraftingD", item, craft);
				return;
			}
		}
		CraftGlassAction glass = CraftGlassAction.getBar(itemId);
		if (itemId == 1775 || itemId == 23193 || itemId == 32845) {
			if (glass != null) {
				player.getDialogueManager().startDialogue("GlassBlowingD", item, glass);
				return;
			}
		}
		CraftMageDung mageDung = CraftMageDung.getBar(itemId);
		if (item.getDefinitions().containsInventoryOption(0, "Craft") && player.getDungManager().isInside()) {
			if (mageDung != null) {
				player.getDialogueManager().startDialogue("DungeoneeringMagicCraftingD", item, mageDung);
				return;
			}
		}
		InventionAction invention = InventionAction.getByProduct(itemId);
		if (item.getDefinitions().containsInventoryOption(0, "Add materials")) {
			if (invention != null) {
				player.getDialogueManager().startDialogue("InventionD", item, invention);
				return;
			}
		}
		FletchBoltAction fletch = FletchBoltAction.getBar(itemId);
		if (item.getDefinitions().containsInventoryOption(0, "Feather")) {
			if (fletch != null) {
				player.getDialogueManager().startDialogue("BoltFletchingD", item, fletch);
				return;
			}
		}
		FletchArrowAction fletch1 = FletchArrowAction.getBar(itemId);
		if (item.getDefinitions().containsInventoryOption(0, "Tip") || item.getId() == 28436) {
			if (fletch1 != null) {
				player.getDialogueManager().startDialogue("ArrowFletchingD", item, fletch1);
				return;
			}
		}
		FletchAction fletch2 = FletchAction.getBar(itemId);
		if (item.getDefinitions().containsInventoryOption(0, "String")) {
			if (fletch2 != null) {
				player.getDialogueManager().startDialogue("FletchingD", item, fletch2);
				return;
			}
		}
		CleanAction clean = CleanAction.getHerb(itemId);
		if (item.getDefinitions().containsInventoryOption(0, "Clean")
				|| item.getDefinitions().containsInventoryOption(0, "Mix potion")) {
			if (clean != null) {
				player.getDialogueManager().startDialogue("HerbloreD", item, clean);
				return;
			}
		}
		GrindAction grind = GrindAction.getHerb(itemId);
		if (item.getDefinitions().containsInventoryOption(0, "Grind")) {
			player.getDialogueManager().startDialogue("GrindingD", item, grind);
			return;
		}
		CreateWeapon weapon = CreateWeapon.getWeapon(itemId);
		if (itemId == ItemIdentifiers.SPIDER_LEG_MIDDLE || itemId == ItemIdentifiers.SPIDER_LEG_TOP
				|| itemId == ItemIdentifiers.SPIDER_LEG_BOTTOM || itemId == ItemIdentifiers.SPIDER_LEG
				|| itemId == ItemIdentifiers.ARAXXIS_FANG || itemId == ItemIdentifiers.ARAXXIS_EYE
				|| itemId == ItemIdentifiers.ARAXXIS_WEB) {
			if (weapon != null) {
				player.getDialogueManager().startDialogue("NoxiousWeaponCreationD", item, weapon);
				return;
			}
		}
		if (itemId == ItemIdentifiers.SIRENIC_SCALE) {
			player.getDialogueManager().startDialogue("SirenicCraftingD", CraftSirenicAction.SIRENIC_MASK);
		}
		if (itemId == ItemIdentifiers.TECTONIC_ENERGY) {
			player.getDialogueManager().startDialogue("TectonicCraftingD", CraftTectonicAction.TECTONIC_MASK);
		}

		/**
		 * End of the Skilling Actions.
		 */

		else if (itemId == 1775 || itemId == ItemIdentifiers.RED_SANDSTONE
				|| itemId == ItemIdentifiers.CRYSTALFLECKED_SANDSTONE)
			player.getDialogueManager().startDialogue("GlassBlowingD",
					(itemId == ItemIdentifiers.RED_SANDSTONE || itemId == ItemIdentifiers.CRYSTALFLECKED_SANDSTONE) ? 1
							: 0);
		else if (itemId == 22444)
			PolyporeCreature.sprinkleOil(player, null);
		else if (itemId == 550)
			player.getInterfaceManager().sendCentralInterface(270);
		else if (itemId == AncientEffigies.SATED_ANCIENT_EFFIGY || itemId == AncientEffigies.GORGED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.NOURISHED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.STARVED_ANCIENT_EFFIGY)
			player.getDialogueManager().startDialogue("AncientEffigiesD", itemId);
		else if (itemId == 4155)
			player.getDialogueManager().startDialogue("EnchantedGemD",
					player.getSlayerManager().getCurrentMaster().getNPCId());
		else if (itemId >= 23653 && itemId <= 23658)
			FightKiln.useCrystal(player, itemId);
		else if (itemId == 20124 || itemId == 20123 || itemId == 20122 || itemId == 20121)
			GodswordCreating.attachKeys(player);
		else if (itemId == 6) {
			player.cannonType = DwarfMultiCannon.CannonType.NORMAL;
			DwarfMultiCannon.setUp(player);
		} else if (itemId == 20494) {
			player.cannonType = DwarfMultiCannon.CannonType.GOLD;
			DwarfMultiCannon.setUp(player);
		} else if (itemId == 20498) {
			player.cannonType = DwarfMultiCannon.CannonType.ROYALE;
			DwarfMultiCannon.setUp(player);
		} else if (itemId == 15707)
			player.getDungManager().openPartyInterface();
		else if (Nest.isNest(itemId))
			Nest.searchNest(player, slotId);
		else if (itemId == 14057) // broomstick
			player.setNextAnimation(new Animation(10532));
		else if (itemId == 21776) {
			if (!player.getSkills().hasLevel(Skills.CRAFTING, 77))
				return;
			else if (player.getInventory().containsItem(itemId, 100)) {
				player.setNextAnimation(new Animation(713));
				player.setNextGraphics(new Graphics(1383));
				player.getInventory().deleteItem(new Item(itemId, 100));
				player.getInventory().addItem(new Item(21775, 1));
				player.getSkills().addXp(Skills.CRAFTING, 150);
				player.getPackets().sendGameMessage("You combine the shards into an orb.");
			} else {
				player.getPackets()
						.sendGameMessage("You need at least 100 shards in order to create an orb of armadyl.");
			}
		} else if (itemId == 5974) {
			if (!player.getInventory().containsItemToolBelt(Smithing.HAMMER)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need a hammer in order to break open a coconut.");
				return;
			}
			player.getInventory().addItem(new Item(5976, 1));
			player.getInventory().deleteItem(new Item(5974, 1));
			player.getPackets()
					.sendGameMessage("You smash the coconut with a hammer and it breaks into two symmetrical pieces.");
		} else if (itemId == 24352)
			player.getDialogueManager().startDialogue("DragonBoneUpgradeKiteInfoD");
		else if (itemId == SqirkFruitSqueeze.SqirkFruit.AUTUMM.getFruitId())
			player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.AUTUMM);
		else if (itemId == SqirkFruitSqueeze.SqirkFruit.SPRING.getFruitId())
			player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.SPRING);
		else if (itemId == SqirkFruitSqueeze.SqirkFruit.SUMMER.getFruitId())
			player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.SUMMER);
		else if (itemId == SqirkFruitSqueeze.SqirkFruit.WINTER.getFruitId())
			player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.WINTER);
		else if (item.getDefinitions().getName().startsWith("Burnt"))
			player.getDialogueManager().startDialogue("SimplePlayerMessage", "Ugh, this is inedible.");
		else if ((item.getDefinitions().containsInventoryOption(0, "Fletch"))
				&& SkillsDialogue.selectTool(player, item.getId())) {
			return;
		}
		if (Settings.DEBUG) {
			Logger.log("ItemHandler", "Item option 1: " + itemId + ", slotId: " + slotId);
			player.getPackets().sendGameMessage("Item option 1: " + itemId + ", slotId " + slotId + ".", true);
		}
	}

	/*
	 * returns the other
	 */
	public static Item contains(int id1, Item item1, Item item2) {
		if (item1.getId() == id1)
			return item2;
		if (item2.getId() == id1)
			return item1;
		return null;
	}

	public static boolean contains(int id1, int id2, Item... items) {
		boolean containsId1 = false;
		boolean containsId2 = false;
		for (Item item : items) {
			if (item.getId() == id1)
				containsId1 = true;
			else if (item.getId() == id2)
				containsId2 = true;
		}
		return containsId1 && containsId2;
	}

	public static void handleInterfaceOnInterface(final Player player, InputStream stream) {
		int toInterfaceHash = stream.readInt(); // correct
		int fromSlot = stream.readUnsignedShort(); // correct
		int toSlot = stream.readUnsignedShortLE(); // correct
		int itemUsedId = stream.readUnsignedShortLE(); // correct
		int usedWithId = stream.readUnsignedShort(); // correct
		int fromInterfaceHash = stream.readInt(); // correct

		int interfaceId = fromInterfaceHash >> 16;
		int interfaceComponent = fromInterfaceHash - (interfaceId << 16);
		int interfaceId2 = toInterfaceHash >> 16;
		@SuppressWarnings("unused")
		int interface2Component = toInterfaceHash - (interfaceId2 << 16);

		if (Settings.DEBUG)
			Logger.log("ItemHandler",
					"ItemOnItem; usedWithId: " + usedWithId + ", toSlot: " + toSlot + ", itemUsedId: " + itemUsedId
							+ ", fromSlot: " + fromSlot + ", inter1: " + interfaceId + ", inter2: " + interfaceId2
							+ ", componentId: " + interfaceComponent);

		if (player.isLocked() || player.isStunned() || player.getEmotesManager().isDoingEmote())
			return;
		player.stopAll();
		if (interfaceId == 1461 && (interfaceId2 == 1473 || interfaceId2 == 1474) && (fromSlot == 17 || fromSlot == 29
				|| fromSlot == 41 || fromSlot == 49 || fromSlot == 64 || fromSlot == 77)) {
			JewelleryAction enchant = JewelleryAction.getBar(usedWithId);
			if (enchant != null) {
				if (fromSlot == 17) {
					if (player.getSkills().hasLevel(Skills.MAGIC, 7))
						player.getDialogueManager().startDialogue("JewelleryEnchantingD",
								JewelleryAction.RING_OF_RECOIL);
					else
						return;
				} else if (fromSlot == 29) {
					if (player.getSkills().hasLevel(Skills.MAGIC, 27))
						player.getDialogueManager().startDialogue("JewelleryEnchantingD",
								JewelleryAction.RING_OF_DUELLING);
					else
						return;
				} else if (fromSlot == 41) {
					if (player.getSkills().hasLevel(Skills.MAGIC, 49))
						player.getDialogueManager().startDialogue("JewelleryEnchantingD",
								JewelleryAction.RING_OF_FORGING);
					else
						return;
				} else if (fromSlot == 49) {
					if (player.getSkills().hasLevel(Skills.MAGIC, 57))
						player.getDialogueManager().startDialogue("JewelleryEnchantingD", JewelleryAction.RING_OF_LIFE);
					else
						return;
				} else if (fromSlot == 64) {
					if (player.getSkills().hasLevel(Skills.MAGIC, 68))
						player.getDialogueManager().startDialogue("JewelleryEnchantingD",
								JewelleryAction.RING_OF_WEALTH);
					else
						return;
				} else if (fromSlot == 77) {
					if (player.getSkills().hasLevel(Skills.MAGIC, 87)) {
						if (usedWithId == 31857 || usedWithId == 31859 || usedWithId == 31865 || usedWithId == 31863) {
							player.getDialogueManager().startDialogue("JewelleryEnchantingD",
									JewelleryAction.RING_OF_DEATH);
						} else
							player.getDialogueManager().startDialogue("JewelleryEnchantingD",
									JewelleryAction.RING_OF_STONE);
					} else
						return;
				}
			} else {
				player.getPackets().sendGameMessage(
						"This spell can only be cast on amulets, necklaces, rings, braclets or shapes in the training arena.");
			}
		}
		if (interfaceId == 1430 && interfaceComponent >= 55 && interfaceComponent <= 229
				&& (interfaceId2 == Inventory.INVENTORY_INTERFACE || interfaceId2 == Inventory.INVENTORY_INTERFACE_2)
				&& !player.getInterfaceManager().containsInventoryInter()) {
			Item item = player.getInventory().getItem(toSlot);
			if (item == null || item.getId() != usedWithId)
				return;
			player.getActionbar().pushShortcutOnSomething((interfaceComponent - 57) / 13, item);
			return;
		}
		if ((interfaceId == 747 || interfaceId == 662)
				&& (interfaceId2 == Inventory.INVENTORY_INTERFACE || interfaceId2 == Inventory.INVENTORY_INTERFACE_2)) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(toSlot);
				}
			}
			return;
		}
		if ((interfaceId == Inventory.INVENTORY_INTERFACE || interfaceId == Inventory.INVENTORY_INTERFACE_2)
				&& interfaceId == interfaceId2 && !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28 || toSlot == fromSlot)
				return;
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null || itemUsed.getId() != itemUsedId
					|| usedWith.getId() != usedWithId)
				return;
			if (!player.getControllerManager().canUseItemOnItem(itemUsed, usedWith))
				return;
			else if (itemUsedId == 23191 || usedWithId == 23191) {
				Drink pot = Drinkables.getDrink(itemUsedId == 23191 ? usedWithId : itemUsedId);
				if (pot == null)
					return;
				player.getDialogueManager().startDialogue("FlaskDecantingD", pot, itemUsedId == 23191 ? toSlot : fromSlot, itemUsedId == 23191 ? fromSlot : toSlot);
				return;
			} else if (itemUsedId == 30372) {
				if (usedWith.getDefinitions().isNoted()) {
					player.getPackets().sendGameMessage("You can't note a note.");
					return;
				}
				if (usedWith.getDefinitions().isDungeoneeringItem()) {
					player.getPackets().sendGameMessage("You can't note this item.");
					return;
				}
				if (usedWith.getDefinitions().isLended()) {
					player.getPackets().sendGameMessage("You can't note this item.");
					return;
				}
				if (usedWith.getDefinitions().isStackable()) {
					player.getPackets().sendGameMessage("You can't note this item.");
					return;
				}
				if (!ItemDefinitions.getItemDefinitions(usedWithId + 1).isNoted()) {
					player.getPackets().sendGameMessage("You can't note this item.");
					return;
				}
				if (ItemDefinitions.getItemDefinitions(usedWithId + 1).getId() > Utils.getItemDefinitionsSize()) {
					player.getPackets().sendGameMessage("That item id is not supported in the cache.");
					return;
				}
				if (player.getInventory().containsItem(30372, 1) && player.getInventory().containsItem(usedWithId, 1)) {
					player.getInventory().deleteItem(30372, 1);
					player.getInventory().deleteItem(usedWithId, 1);
					player.getInventory().addItem(usedWithId + 1, 1);
				}
			} else if ((itemUsedId == ItemIdentifiers.ONYX || usedWithId == ItemIdentifiers.ONYX) && ((itemUsedId >= 35378 && itemUsedId <= 35381) || (usedWithId >= 35378 && usedWithId <= 35381)
					|| (itemUsedId >= 31869 && itemUsedId <= 31880) || (usedWithId >= 31869 && usedWithId <= 31880))) {
				int uncharged = itemUsedId == ItemIdentifiers.ONYX ? usedWithId : itemUsedId;
				player.getInventory().deleteItem(ItemIdentifiers.ONYX, 1);
				player.getInventory().deleteItem(uncharged, 1);
				if (uncharged >= 35378 && uncharged <= 35381)
					player.getInventory().addItem(ItemConstants.getItemFixed(uncharged), 1);
				else
					player.getCharges().addCharges(uncharged, 30000, -1);
				player.getPackets().sendGameMessage("You add charges to your hydrix jewelry.");
			} else if (itemUsedId == 22452 && usedWithId == 22451 || itemUsedId == 22451 && usedWithId == 22452) {
				player.getDialogueManager().startDialogue(new Dialogue() {

					@Override
					public void start() {
						if (!player.getInventory().containsItem(22451, 500)) {
							sendDialogue("You don't have enough ganodermic flakes.");
							stage = 3;
							return;
						}
						if (player.getSkills().getLevel(Skills.CRAFTING) < 86) {
							sendDialogue("You need a Crafting level of atleast 86 to do that.");
							stage = 3;
							return;
						}
						if (!player.getInventory().containsItem(1734, 1)) {
							sendDialogue("You must have some thread first in order to craft this.");
							stage = 3;
							return;
						}
						sendItemDialogue(22482,
								"Are you sure you want to attach the ganodermic flakes to the Mycelium visor web?");
						stage = 1;
					}

					public void run(int interfaceId, int componentId, int slotId) throws ClassNotFoundException {
						switch (stage) {
						case 1:
							sendOptionsDialogue("select an option", "Yes.", "No.");
							stage = 2;
							break;
						case 2:
							switch (componentId) {
							case OPTION_1:
								player.setNextAnimation(new Animation(25594));
								player.getInventory().deleteItem(22452, 1);
								player.getInventory().deleteItem(22451, 500);
								player.getInventory().addItem(22482, 1);
								player.getSkills().addXp(Skills.CRAFTING, 100);
								end();
								break;
							case OPTION_2:
								end();
								break;
							}
							break;
						case 3:
							end();
							break;
						}
					}

					@Override
					public void finish() {
					}
				});
			} else if (itemUsedId == 22454 && usedWithId == 22451 || itemUsedId == 22451 && usedWithId == 22454) {
				player.getDialogueManager().startDialogue(new Dialogue() {

					@Override
					public void start() {
						if (!player.getInventory().containsItem(22451, 1500)) {
							sendDialogue("You don't have enough ganodermic flakes.");
							stage = 3;
							return;
						}
						if (player.getSkills().getLevel(Skills.CRAFTING) < 92) {
							sendDialogue("You need a Crafting level of atleast 92 to do that.");
							stage = 3;
							return;
						}
						if (!player.getInventory().containsItem(1734, 1)) {
							sendDialogue("You must have some thread first in order to craft this.");
							stage = 3;
							return;
						}
						sendItemDialogue(22486,
								"Are you sure you want to attach the ganodermic flakes to the Mycelium leggings web.");
						stage = 1;
					}

					public void run(int interfaceId, int componentId, int slotId) throws ClassNotFoundException {
						switch (stage) {
						case 1:
							sendOptionsDialogue("select an option", "Yes.", "No.");
							stage = 2;
							break;
						case 2:
							switch (componentId) {
							case OPTION_1:
								player.setNextAnimation(new Animation(25594));
								player.getInventory().deleteItem(22454, 1);
								player.getInventory().deleteItem(22451, 1500);
								player.getInventory().addItem(22486, 1);
								player.getSkills().addXp(Skills.CRAFTING, 300);
								end();
								break;
							case OPTION_2:
								end();
								break;
							}
							break;
						case 3:
							end();
							break;
						}
					}

					@Override
					public void finish() {
					}
				});
			} else if (itemUsedId == 22456 && usedWithId == 22451 || itemUsedId == 22451 && usedWithId == 22456) {
				player.getDialogueManager().startDialogue(new Dialogue() {

					@Override
					public void start() {
						if (!player.getInventory().containsItem(22451, 5000)) {
							sendDialogue("You don't have enough ganodermic flakes.");
							stage = 3;
							return;
						}
						if (player.getSkills().getLevel(Skills.CRAFTING) < 98) {
							sendDialogue("You need a Crafting level of atleast 98 to do that.");
							stage = 3;
							return;
						}
						if (!player.getInventory().containsItem(1734, 1)) {
							sendDialogue("You must have some thread first in order to craft this.");
							stage = 3;
							return;
						}
						sendItemDialogue(22490,
								"Are you sure you want to attach the ganodermic flakes to the Mycelium poncho web?");
						stage = 1;
					}

					public void run(int interfaceId, int componentId, int slotId) throws ClassNotFoundException {
						switch (stage) {
						case 1:
							sendOptionsDialogue("select an option", "Yes.", "No.");
							stage = 2;
							break;
						case 2:
							switch (componentId) {
							case OPTION_1:
								player.setNextAnimation(new Animation(25594));
								player.getInventory().deleteItem(22456, 1);
								player.getInventory().deleteItem(22451, 5000);
								player.getInventory().addItem(22490, 1);
								player.getSkills().addXp(Skills.CRAFTING, 1000);
								end();
								break;
							case OPTION_2:
								end();
								break;
							}
							break;
						case 3:
							end();
							break;
						}
					}

					@Override
					public void finish() {
					}
				});
			} else if (usedWithId == 30372) {
				if (itemUsed.getDefinitions().isNoted()) {
					player.getPackets().sendGameMessage("You can't note a note.");
					return;
				}
				if (itemUsed.getDefinitions().isDungeoneeringItem()) {
					player.getPackets().sendGameMessage("You can't note this item.");
					return;
				}
				if (itemUsed.getDefinitions().isLended()) {
					player.getPackets().sendGameMessage("You can't note this item.");
					return;
				}
				if (itemUsed.getDefinitions().isStackable()) {
					player.getPackets().sendGameMessage("You can't note this item.");
					return;
				}
				if (!ItemDefinitions.getItemDefinitions(itemUsedId + 1).isNoted()) {
					player.getPackets().sendGameMessage("You can't note this item.");
					return;
				}
				if (ItemDefinitions.getItemDefinitions(itemUsedId + 1).getId() > Utils.getItemDefinitionsSize()) {
					player.getPackets().sendGameMessage("That item id is not supported in the cache.");
					return;
				}
				if (player.getInventory().containsItem(30372, 1) && player.getInventory().containsItem(itemUsedId, 1)) {
					player.getInventory().deleteItem(30372, 1);
					player.getInventory().deleteItem(itemUsedId, 1);
					player.getInventory().addItem(itemUsedId + 1, 1);
				}
			} else if ((itemUsedId == 11702 && usedWithId == 11690) || (itemUsedId == 11690 && usedWithId == 11702)) {
				player.getInventory().deleteItem(11702, 1);
				player.getInventory().deleteItem(11690, 1);
				player.getInventory().addItem(11694, 1);
			} else if (itemUsedId == 11704 && usedWithId == 11690 || itemUsedId == 11690 && usedWithId == 11704) {
				player.getInventory().deleteItem(11704, 1);
				player.getInventory().deleteItem(11690, 1);
				player.getInventory().addItem(11696, 1);
			} else if (itemUsedId == 11706 && usedWithId == 11690 || itemUsedId == 11690 && usedWithId == 11706) {
				player.getInventory().deleteItem(11706, 1);
				player.getInventory().deleteItem(11690, 1);
				player.getInventory().addItem(11698, 1);
			} else if (itemUsedId == 11708 && usedWithId == 11690 || itemUsedId == 11690 && usedWithId == 11708) {
				player.getInventory().deleteItem(11708, 1);
				player.getInventory().deleteItem(11690, 1);
				player.getInventory().addItem(11700, 1);
			}
			if (itemUsed.getId() >= 15522 && itemUsed.getId() <= 15550 && usedWith.getId() >= 15522
					&& usedWith.getId() <= 15550) {
				StrangeRock.createTeleport(player);
				return;
			}
			Lighters lighters = FireLighter.getColoredLog(itemUsedId, fromSlot);
			if (lighters != null) {
				FireLighter.handleLighter(player, lighters);
				return;
			}

			/**
			 * Expert skillshards
			 */
			else if (itemUsedId == 33262 && usedWithId == 32069 || itemUsedId == 32069 && usedWithId == 33262) {
				player.expertSkillShards[Skills.ATTACK] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32069, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32071 || itemUsedId == 32071 && usedWithId == 33262) {
				player.expertSkillShards[Skills.DEFENCE] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32071, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32070 || itemUsedId == 32070 && usedWithId == 33262) {
				player.expertSkillShards[Skills.STRENGTH] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32070, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32074 || itemUsedId == 32074 && usedWithId == 33262) {
				player.expertSkillShards[Skills.HITPOINTS] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32074, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32075 || itemUsedId == 32075 && usedWithId == 33262) {
				player.expertSkillShards[Skills.RANGED] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32075, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32073 || itemUsedId == 32073 && usedWithId == 33262) {
				player.expertSkillShards[Skills.PRAYER] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32073, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32076 || itemUsedId == 32076 && usedWithId == 33262) {
				player.expertSkillShards[Skills.MAGIC] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32076, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32077 || itemUsedId == 32077 && usedWithId == 33262) {
				player.expertSkillShards[Skills.COOKING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32077, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32064 || itemUsedId == 32064 && usedWithId == 33262) {
				player.expertSkillShards[Skills.WOODCUTTING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32064, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32080 || itemUsedId == 32080 && usedWithId == 33262) {
				player.expertSkillShards[Skills.FLETCHING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32080, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32063 || itemUsedId == 32063 && usedWithId == 33262) {
				player.expertSkillShards[Skills.FISHING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32063, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32079 || itemUsedId == 32079 && usedWithId == 33262) {
				player.expertSkillShards[Skills.FIREMAKING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32079, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32082 || itemUsedId == 32082 && usedWithId == 33262) {
				player.expertSkillShards[Skills.CRAFTING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32082, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32084 || itemUsedId == 32084 && usedWithId == 33262) {
				player.expertSkillShards[Skills.SMITHING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32084, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32065 || itemUsedId == 32065 && usedWithId == 33262) {
				player.expertSkillShards[Skills.MINING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32065, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32081 || itemUsedId == 32081 && usedWithId == 33262) {
				player.expertSkillShards[Skills.HERBLORE] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32081, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32087 || itemUsedId == 32087 && usedWithId == 33262) {
				player.expertSkillShards[Skills.AGILITY] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32087, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32086 || itemUsedId == 32086 && usedWithId == 33262) {
				player.expertSkillShards[Skills.THIEVING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32086, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32088 || itemUsedId == 32088 && usedWithId == 33262) {
				player.expertSkillShards[Skills.SLAYER] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32088, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32067 || itemUsedId == 32067 && usedWithId == 33262) {
				player.expertSkillShards[Skills.FARMING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32067, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32078 || itemUsedId == 32078 && usedWithId == 33262) {
				player.expertSkillShards[Skills.RUNECRAFTING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32078, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32083 || itemUsedId == 32083 && usedWithId == 33262) {
				player.expertSkillShards[Skills.HUNTER] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32083, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32068 || itemUsedId == 32068 && usedWithId == 33262) {
				player.expertSkillShards[Skills.CONSTRUCTION] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32068, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32072 || itemUsedId == 32072 && usedWithId == 33262) {
				player.expertSkillShards[Skills.SUMMONING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32072, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32085 || itemUsedId == 32085 && usedWithId == 33262) {
				player.expertSkillShards[Skills.DUNGEONEERING] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32085, 1);
			} else if (itemUsedId == 33262 && usedWithId == 32066 || itemUsedId == 32066 && usedWithId == 33262) {
				player.expertSkillShards[Skills.DIVINATION] = true;
				player.refreshShardConfigs();
				player.getInventory().deleteItem(32066, 1);

			}
			/**
			 * Skills Actions
			 */
			FletchBoltAction fletch1 = BoltFletching.isAttaching(player, usedWith, itemUsed);
			if (fletch1 != null) {
				player.getDialogueManager().startDialogue("BoltFletchingD", itemUsed, fletch1);
				return;
			}
			FletchData fletch = LogsFletching.isAttaching(player, itemUsed, usedWith);
			if (fletch != null) {
				player.getDialogueManager().startDialogue("LogsFletchingD", fletch);
				return;
			}
			FletchAction fletch2 = Fletching.isAttaching(player, usedWith, itemUsed);
			if (fletch2 != null) {
				player.getDialogueManager().startDialogue("FletchingD", itemUsed, fletch2);
				return;
			}
			FletchArrowAction fletch3 = ArrowFletching.isAttaching(player, usedWith, itemUsed);
			if (fletch3 != null) {
				player.getDialogueManager().startDialogue("ArrowFletchingD", itemUsed, fletch3);
			}
			CreateWeapon weapon = NoxiousWeaponCreation.isAttaching(player, usedWith, usedWith);
			if (weapon != null) {
				player.getDialogueManager().startDialogue("NoxiousWeaponCreationD", itemUsed, weapon);
				return;
			}
			Combinations combination = Combinations.isCombining(itemUsedId, usedWithId);
			if (combination != null) {
				player.getDialogueManager().startDialogue("CombinationsD", combination);
				return;
			} else if (Firemaking.isFiremaking(player, itemUsed, usedWith))
				return;
			else if (OrnamentKits.attachKit(player, itemUsed, usedWith, fromSlot, toSlot))
				return;
			else if (AmuletAttaching.isAttaching(itemUsedId, usedWithId))
				player.getDialogueManager().startDialogue("AmuletAttaching");
			// else if (GemCutting.isCutting(player, itemUsed, usedWith))
			// return;
			if (ItemDyes.dyeItem(player, usedWithId, itemUsedId))
				return;
			else if (AttachingOrbsDialouge.isAttachingOrb(player, itemUsed, usedWith))
				return;
			else if (SmithingCombinations.combineItem(player, usedWith.getId(), itemUsed.getId()))
				return;
			else if (TreeSaplings.hasSaplingRequest(player, itemUsedId, usedWithId)) {
				if (itemUsedId == 5354)
					TreeSaplings.plantSeed(player, usedWithId, fromSlot);
				else
					TreeSaplings.plantSeed(player, itemUsedId, toSlot);
			} else if (Drinkables.mixPot(player, itemUsed, usedWith, fromSlot, toSlot, true) != -1)
				return;
			else if (WeaponPoison.poison(player, itemUsed, usedWith, false))
				return;
			/**
			 * Skill Actions
			 */
			CleanAction herb = Herblore.isMixing(player, usedWith, itemUsed);
			if (herb != null) {
				player.getDialogueManager().startDialogue("HerbloreD", itemUsed, herb);
				return;
			}

			else if (PrayerBooks.isGodBook(itemUsedId, false) || PrayerBooks.isGodBook(usedWithId, false)) {
				PrayerBooks.bindPages(player, itemUsed.getName().contains(" page ") ? usedWithId : itemUsedId);
			} else if (contains(22498, 554, itemUsed, usedWith) || contains(22498, 22448, itemUsed, usedWith)) {
				if (!player.getSkills().hasLevel(Skills.FARMING, 80))
					return;
				else if (!player.getInventory().containsItem(22448, 3000)) {
					player.getPackets()
							.sendGameMessage("You need 3,000 polypore spores in order to make a polypore staff.");
					return;
				} else if (!player.getInventory().containsItem(554, 15000)) {
					player.getPackets()
							.sendGameMessage("You need 15,000 fire runes in order to make a polypore staff.");
					return;
				}
				player.setNextAnimation(new Animation(15434));
				player.lock(2);
				player.getInventory().deleteItem(554, 15000);
				player.getInventory().deleteItem(22448, 3000);
				player.getInventory().deleteItem(22498, 1);
				player.getInventory().addItem(22494, 1);
				player.getPackets().sendGameMessage(
						"You attach the polypore spores and infuse the fire runes to the stick in order to create a staff.");
			} else if (contains(22496, 22448, itemUsed, usedWith)) {
				if (!player.getSkills().hasLevel(Skills.FARMING, 80))
					return;
				int charges = 3000 - player.getCharges().getCharges(22496);
				if (!player.getInventory().containsItem(22448, charges)) {
					player.getPackets().sendGameMessage(
							"You need " + charges + " polypore spores in order to recharge polypore staff.");
					return;
				}
				player.setNextAnimation(new Animation(15434));
				player.lock(2);
				player.getInventory().deleteItem(22448, charges);
				player.getInventory().deleteItem(22496, 1);
				player.getCharges().resetCharges(22496);
				player.getInventory().addItem(22494, 1);
				player.getPackets().sendGameMessage("You attach the polypore spores to the staff.");
			} else if (Slayer.createSlayerHelmet(player, itemUsedId, usedWithId))
				return;
			else if (itemUsedId == 18339 || itemUsedId == 453) {
				Coalbag.addCoal(player);
			} else if (itemUsedId == 18339 || itemUsedId == 454) {
				player.getPackets().sendGameMessage("You can't fill in with noted coals!");
			} else if ((itemUsedId == 27068 && usedWithId == 11716) || (itemUsedId == 11716 && usedWithId == 27068))
				player.getDialogueManager().startDialogue("ChaoticSpear");
			else if ((itemUsedId == 14484 && usedWithId == 27068) || (itemUsedId == 27068 && usedWithId == 14484))
				player.getDialogueManager().startDialogue("ChaoticClaw");
			else if ((itemUsedId == 25555 && usedWithId == 27068) || (itemUsedId == 27068 && usedWithId == 25555))
				player.getDialogueManager().startDialogue("ChaoticOffHandClaw");
			else if ((itemUsedId == 25034 && usedWithId == 31449) || (itemUsedId == 31449 && usedWithId == 25034))
				player.getDialogueManager().startDialogue("ChaoticMurmur");
			else if ((itemUsedId == 25028 && usedWithId == 31449) || (itemUsedId == 31449 && usedWithId == 25028))
				player.getDialogueManager().startDialogue("ChaoticWhisper");
			else if ((itemUsedId == 25031 && usedWithId == 31449) || (itemUsedId == 31449 && usedWithId == 25031))
				player.getDialogueManager().startDialogue("ChaoticHiss");
			else if (itemUsedId == 5733) {
				if (player.getRights() < 2) {
					player.getInventory().deleteItem(5733, 28);
					return;
				}
				player.getInventory().deleteItem(usedWithId, 1);
				player.getPackets()
						.sendGameMessage("Oi - The Rotten Potato ate the " + usedWith.getDefinitions().getName() + "!");
			} else if (usedWithId == 5733) {
				if (player.getRights() < 2) {
					player.getInventory().deleteItem(5733, 28);
					return;
				}
				player.getInventory().deleteItem(itemUsedId, 1);
				player.getPackets()
						.sendGameMessage("Oi - The Rotten Potato ate the " + itemUsed.getDefinitions().getName() + "!");
			} else if (usedWithId == 20767 && itemUsedId == 20768) {
				player.getInventory().deleteItem(20767, 1);
				player.getInventory().deleteItem(20768, 1);
				player.getInventory().addItem(32151, 1);
				player.getPackets().sendGameMessage("You attach your hood to your cape.");
			} else if (itemUsedId == 20767 && usedWithId == 20768) {
				player.getInventory().deleteItem(20767, 1);
				player.getInventory().deleteItem(20768, 1);
				player.getInventory().addItem(32151, 1);
				player.getPackets().sendGameMessage("You attach your hood to your cape.");
			} else if (usedWithId == 20769 && itemUsedId == 20770) {
				player.getInventory().deleteItem(20769, 1);
				player.getInventory().deleteItem(20770, 1);
				player.getInventory().addItem(32152, 1);
				player.getPackets().sendGameMessage("You attach your hood to your cape.");
			} else if (itemUsedId == 20769 && usedWithId == 20770) {
				player.getInventory().deleteItem(20769, 1);
				player.getInventory().deleteItem(20770, 1);
				player.getInventory().addItem(32152, 1);
				player.getPackets().sendGameMessage("You attach your hood to your cape.");
			} else if (usedWithId == 20771 && itemUsedId == 20772) {
				player.getInventory().deleteItem(20771, 1);
				player.getInventory().deleteItem(20772, 1);
				player.getInventory().addItem(32153, 1);
				player.getPackets().sendGameMessage("You attach your hood to your cape.");
			} else if (itemUsedId == 20771 && usedWithId == 20772) {
				player.getInventory().deleteItem(20771, 1);
				player.getInventory().deleteItem(20772, 1);
				player.getInventory().addItem(32153, 1);
				player.getPackets().sendGameMessage("You attach your hood to your cape.");
			} else if (contains(SpiritshieldCreating.HOLY_ELIXIR, SpiritshieldCreating.SPIRIT_SHIELD, itemUsed,
					usedWith))
				player.getPackets().sendGameMessage("The shield must be blessed at an altar.");
			else if (contains(SpiritshieldCreating.SPIRIT_SHIELD, 13746, itemUsed, usedWith)
					|| contains(SpiritshieldCreating.SPIRIT_SHIELD, 13748, itemUsed, usedWith)
					|| contains(SpiritshieldCreating.SPIRIT_SHIELD, 13750, itemUsed, usedWith)
					|| contains(SpiritshieldCreating.SPIRIT_SHIELD, 13752, itemUsed, usedWith))
				player.getPackets().sendGameMessage("You need a blessed spirit shield to attach the sigil to.");
			else if (contains(SqirkFruitSqueeze.SqirkFruit.AUTUMM.getFruitId(), 233, itemUsed, usedWith))
				player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.AUTUMM);
			else if (contains(SqirkFruitSqueeze.SqirkFruit.SPRING.getFruitId(), 233, itemUsed, usedWith))
				player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.SPRING);
			else if (contains(SqirkFruitSqueeze.SqirkFruit.SUMMER.getFruitId(), 233, itemUsed, usedWith))
				player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.SUMMER);
			else if (contains(SqirkFruitSqueeze.SqirkFruit.WINTER.getFruitId(), 233, itemUsed, usedWith))
				player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.WINTER);
			else if (contains(5976, 229, itemUsed, usedWith)) {
				player.getInventory().deleteItem(new Item(5976, 1));
				player.getInventory().deleteItem(new Item(229, 1));
				player.getInventory().addItem(new Item(5935, 1));
				player.getInventory().addItem(new Item(5978, 1));
				player.getPackets().sendGameMessage("You pour the milk of the coconut into a vial.");
			} else if (contains(4151, 21369, itemUsed, usedWith)) {
				if (!player.getSkills().hasRequiriments(Skills.ATTACK, 75, Skills.SLAYER, 80)) {
					player.getPackets().sendGameMessage(
							"You need an attack level of 75 and slayer level of 80 in order to attach the whip vine to the whip.");
					return;
				}
				player.getInventory().replaceItem(21371, 1, toSlot);
				player.getInventory().deleteItem(fromSlot, itemUsed);
				player.getPackets().sendGameMessage("You attach the whip vine to the abbysal whip.");
			} else if (contains(985, 987, itemUsed, usedWith)) { // crystal key
				// make
				player.getInventory().deleteItem(toSlot, usedWith);
				itemUsed.setId(989);
				player.getInventory().refresh(fromSlot);
				player.getPackets().sendGameMessage("You join the two halves of the key together.");
			} else if (player.getCosmeticsManager().keepSakeItem(itemUsed, usedWith))
				return;
			else
				player.getPackets().sendGameMessage("Nothing interesting happens.");
			if (Settings.DEBUG)
				Logger.log("ItemHandler", "Used:" + itemUsed.getId() + ", With:" + usedWith.getId());
		} else if ((interfaceId == 1461 && (interfaceComponent == 1 || interfaceComponent == 8))
				&& (interfaceId2 == Inventory.INVENTORY_INTERFACE || interfaceId2 == Inventory.INVENTORY_INTERFACE_2)
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28)
				return;
			Item item = player.getInventory().getItem(toSlot);
			if (item == null || item.getId() != usedWithId)
				return;
			player.getActionbar().useAbility(new MagicAbilityShortcut(fromSlot), item);
			// Magic.handleSpellOnItem(player, fromSlot, (byte) toSlot);
		}
	}

	public static void handleItemOption3(Player player, int slotId, int itemId, Item item) {
		if (player.isLocked() || player.isStunned() || player.getEmotesManager().isDoingEmote())
			return;
		player.stopAll(false);
		FlyingEntities impJar = FlyingEntities.forItem((short) itemId);
		if (impJar != null)
			FlyingEntityHunter.openJar(player, impJar, slotId);
		if (LightSource.lightSource(player, slotId))
			return;
		if (OrnamentKits.splitKit(player, item))
			return;
		if (ExplorerRing.handleOption(player, item, slotId, 3))
			return;
		if (item.getDefinitions().isBindItem())
			player.getDungManager().bind(item, slotId);
		else if (itemId == 15492 || itemId == 30656 || itemId == 30686 || itemId == 30716) {
			player.getInterfaceManager().sendCentralInterface(1309);
			player.getPackets().sendIComponentText(1309, 37, "List Co-Op Partner");
		} else if (itemId >= 11095 && itemId <= 11103 && (itemId & 0x1) != 0)
			Revenant.useForinthryBrace(player, item, slotId);
		else if (itemId == 19748)
			renewSummoningPoints(player);
		else if (itemId >= 13281 && itemId <= 13288)
			player.getSlayerManager().checkKillsLeft();
		else if (SmithingCombinations.dismantleItem(player, itemId))
			return;
		else if (itemId == 31846)
			player.getReaperTasks().checkKillsLeft();
		else if (itemId == 15707)
			Magic.sendTeleportSpell(player, 13652, 13654, 2602, 2603, 1, 0, new WorldTile(3447, 3694, 0), 10, true,
					Magic.ITEM_TELEPORT);
		else if (itemId >= 24713 && itemId <= 24762) {
			player.getPackets()
					.sendGameMessage("You have " + (61 - (int) player.getTemporaryAttributtes().get("PrizedPendant"))
							+ " more minutes until your pendant depletes.");
			return;
		} else if (itemId == 18339) {
			player.getPackets().sendInputLongTextScript("Enter amount to withdraw (withdrawn as notes)");
			player.getTemporaryAttributtes().put("coalWithdraw", Boolean.TRUE);
		} else if (itemId == 24437 || itemId == 24439 || itemId == 24440 || itemId == 24441)
			player.getDialogueManager().startDialogue("FlamingSkull", item, slotId);
		else if (Equipment.getItemSlot(itemId) == Equipment.SLOT_AURA)
			player.getAuraManager().sendTimeRemaining(itemId);
		else if (itemId == 20709) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2968, 3285, 0));
		} else if (PrayerBooks.isGodBook(itemId, true))
			PrayerBooks.sermonize(player, itemId);
		else if (itemId == 32152 || itemId == 32153 || itemId == 20769 || itemId == 20771)
			SkillCapeCustomizer.startCustomizing(player, itemId);
		else if (itemId == 32151 || itemId == 20767) {
			Magic.sendTeleportSpell(player, 8939, 8941, 1576, 1577, 1, 0, new WorldTile(2276, 3313, 1), 4, false,
					Magic.ITEM_TELEPORT);
			FadingScreen.fade(player);
			FadingScreen.unfade(player, FadingScreen.fade(player, FadingScreen.TICK / 2), new Runnable() {
				@Override
				public void run() {
					WorldObject objectdoor = new WorldObject(92278, 10, 3, 2275, 3303, 1);
					player.faceObject(objectdoor);
				}
			});
		} else if (itemId == 28606) {// Finish him!
			if (VoragoHandler.vorago.pushBackDamage <= VoragoHandler.vorago.startPushBack / 10) {
				if (Utils.isOnRange(VoragoHandler.vorago, player, 1)) {

					Vorago n = VoragoHandler.vorago;
					n.setCantInteract(true);
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {

							if (count == 1) {
								n.getCombat().removeTarget();
								n.canDie = true;
							}
							if (count == 2) {
								player.faceEntity(n);
							}
							if (count == 3) {
								player.setNextAnimation(new Animation(20387));
								n.sendDeath(player);
								player.getInventory().removeItems(item);
							}
							count++;
						}

					}, 0, 0);
				} else {
					player.getPackets().sendGameMessage("You need to be closer to Vorago");// TODO
					// find
					// act
					// message
				}
			} else {
				player.getPackets().sendGameMessage("Vorago isn't in the right place");// TODO
				// find
				// act
				// message
			}
		} else if (itemId == 27477)
			player.getDialogueManager().startDialogue("SixthAgeCircuit");
		else if (itemId == 22332) {
			Magic.sendNormalTeleportSpell(player, 1, 0, new WorldTile(3098, 3162, 3));
		} else if (itemId == 21371) {
			player.getInventory().replaceItem(4151, 1, slotId);
			player.getInventory().addItem(21369, 1);
			player.getPackets().sendGameMessage("You split the whip vine from the abbysal whip.");
		} else if (itemId == 4155) {
			player.getSlayerManager().checkKillsLeft();
		} else if (itemId == 11694 || itemId == 11696 || itemId == 11698 || itemId == 11700)
			GodswordCreating.dismantleGS(player, item, slotId);
		else if (itemId == 23044 || itemId == 23045 || itemId == 23046 || itemId == 23047)
			player.getDialogueManager().startDialogue("MindSpikeD", itemId, slotId);
		else if (item.getDefinitions().containsOption("Teleport")
				&& ItemTransportation.transportationDialogue(player, item, true))
			return;
		else if (player.getCharges().checkCharges(item))
			return;
		else if (itemId == 33262) {
			if (player.getInventory().containsItem(32063, 1)) {
				player.getInventory().deleteItem(32063, 1);
				player.expertSkillShards[Skills.FISHING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32064, 1)) {
				player.getInventory().deleteItem(32064, 1);
				player.expertSkillShards[Skills.WOODCUTTING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32065, 1)) {
				player.getInventory().deleteItem(32065, 1);
				player.expertSkillShards[Skills.MINING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32066, 1)) {
				player.getInventory().deleteItem(32066, 1);
				player.expertSkillShards[Skills.DIVINATION] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32067, 1)) {
				player.getInventory().deleteItem(32067, 1);
				player.expertSkillShards[Skills.FARMING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32083, 1)) {
				player.getInventory().deleteItem(32083, 1);
				player.expertSkillShards[Skills.HUNTER] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32069, 1)) {
				player.getInventory().deleteItem(32069, 1);
				player.expertSkillShards[Skills.ATTACK] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32070, 1)) {
				player.getInventory().deleteItem(32070, 1);
				player.expertSkillShards[Skills.STRENGTH] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32071, 1)) {
				player.getInventory().deleteItem(32071, 1);
				player.expertSkillShards[Skills.DEFENCE] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32072, 1)) {
				player.getInventory().deleteItem(32072, 1);
				player.expertSkillShards[Skills.SUMMONING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32073, 1)) {
				player.getInventory().deleteItem(32073, 1);
				player.expertSkillShards[Skills.PRAYER] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32074, 1)) {
				player.getInventory().deleteItem(32074, 1);
				player.expertSkillShards[Skills.HITPOINTS] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32075, 1)) {
				player.getInventory().deleteItem(32075, 1);
				player.expertSkillShards[Skills.RANGED] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32076, 1)) {
				player.getInventory().deleteItem(32076, 1);
				player.expertSkillShards[Skills.MAGIC] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32077, 1)) {
				player.getInventory().deleteItem(32077, 1);
				player.expertSkillShards[Skills.COOKING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32078, 1)) {
				player.getInventory().deleteItem(32078, 1);
				player.expertSkillShards[Skills.RUNECRAFTING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32079, 1)) {
				player.getInventory().deleteItem(32079, 1);
				player.expertSkillShards[Skills.FIREMAKING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32080, 1)) {
				player.getInventory().deleteItem(32080, 1);
				player.expertSkillShards[Skills.FLETCHING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32081, 1)) {
				player.getInventory().deleteItem(32081, 1);
				player.expertSkillShards[Skills.HERBLORE] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32082, 1)) {
				player.getInventory().deleteItem(32082, 1);
				player.expertSkillShards[Skills.CRAFTING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32068, 1)) {
				player.getInventory().deleteItem(32068, 1);
				player.expertSkillShards[Skills.CONSTRUCTION] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32084, 1)) {
				player.getInventory().deleteItem(32084, 1);
				player.expertSkillShards[Skills.SMITHING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32085, 1)) {
				player.getInventory().deleteItem(32085, 1);
				player.expertSkillShards[Skills.DUNGEONEERING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32086, 1)) {
				player.getInventory().deleteItem(32086, 1);
				player.expertSkillShards[Skills.THIEVING] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32087, 1)) {
				player.getInventory().deleteItem(32087, 1);
				player.expertSkillShards[Skills.AGILITY] = true;
				player.refreshShardConfigs();
			}
			if (player.getInventory().containsItem(32088, 1)) {
				player.getInventory().deleteItem(32088, 1);
				player.expertSkillShards[Skills.SLAYER] = true;
				player.refreshShardConfigs();
			}

		}
		if (Settings.DEBUG) {
			Logger.log("ItemHandler", "Item option 3: " + itemId + ", slotId: " + slotId);
			player.getPackets().sendGameMessage("Item option 3: " + itemId + ", slotId " + slotId + ".", true);
		}
	}

	private static void renewSummoningPoints(Player player) {
		int summonLevel = player.getSkills().getLevel(Skills.SUMMONING);
		if (player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
			player.lock(3);
			player.setNextAnimation(new Animation(8502));
			player.setNextGraphics(new Graphics(1308));
			player.getSkills().set(Skills.SUMMONING, summonLevel);
			player.getPackets().sendGameMessage("You have recharged your Summoning points.", true);
		} else
			player.getPackets().sendGameMessage("You already have full Summoning points.");
	}

	public static void handleItemOption4(Player player, int slotId, int itemId, Item item) {
		if (ExplorerRing.handleOption(player, item, slotId, 4))
			return;
		else if (itemId == 32151 || itemId == 32152 || itemId == 32153 || itemId == 20767 || itemId == 20769
				|| itemId == 20771)
			SkillCapeCustomizer.startCustomizing(player, itemId);
		if (Settings.DEBUG) {
			Logger.log("ItemHandler", "Item option 4: " + itemId + ", slotId: " + slotId);
			player.getPackets().sendGameMessage("Item option 4: " + itemId + ", slotId " + slotId + ".", true);
		}
	}

	public static void handleItemOption5(Player player, int slotId, int itemId, Item item) {
		if (ExplorerRing.handleOption(player, item, slotId, 5))
			return;
		else if (Consumables.eat(player, slotId, item))
			return;
		if (Settings.DEBUG) {
			Logger.log("ItemHandler", "Item option 5: " + itemId + ", slotId: " + slotId);
			player.getPackets().sendGameMessage("Item option 5: " + itemId + ", slotId " + slotId + ".", true);
		}
	}

	public static void handleItemOption6(Player player, int slot, int itemId, Item item) {
		if (player.isLocked() || player.isStunned() || player.getEmotesManager().isDoingEmote())
			return;
		player.stopAll(false);
		switch (itemId) {
		case 15345:
			if (player.isLocked() || player.getControllerManager().getController() != null) {
				player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
				return;
			}
			player.getDialogueManager().startDialogue("ArdougneCloak", false);
			break;
		case 15347:
		case 15349:
		case 19748:
			if (player.isLocked() || player.getControllerManager().getController() != null) {
				player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
				return;
			}
			player.getDialogueManager().startDialogue("ArdougneCloak", true);
			break;
		}
		if (player.getToolbelt().addItem(slot, item))
			return;
		if ((item.getDefinitions().containsOption("Rub") || item.getDefinitions().containsOption("Cabbage-port"))
				&& ItemTransportation.transportationDialogue(player, item, true))
			return;
		else if (Drinkables.emptyPot(player, item, slot))
			return;
		if (ExplorerRing.handleOption(player, item, slot, 6)) {
		}
		GrindAction grind = GrindAction.getHerb(itemId);
		if (grind != null) {
			player.getDialogueManager().startDialogue("GrindingD", item, grind);
			return;
		} else if (item.getDefinitions().isBindItem())
			player.getDungManager().bind(item, slot);
		else if (itemId == 20767 || itemId == 32151)
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2276, 3335, 1));
		else if (itemId == 1438)
			Runecrafting.locate(player, 3127, 3405);
		else if (itemId == 1440)
			Runecrafting.locate(player, 3306, 3474);
		else if (itemId == 1442)
			Runecrafting.locate(player, 3313, 3255);
		else if (itemId == 1444)
			Runecrafting.locate(player, 3185, 3165);
		else if (itemId == 1446)
			Runecrafting.locate(player, 3053, 3445);
		else if (itemId == 1448)
			Runecrafting.locate(player, 2982, 3514);
		else if (itemId == 1458)
			Runecrafting.locate(player, 2858, 3381);
		else if (itemId == 1454)
			Runecrafting.locate(player, 2408, 4377);
		else if (itemId == 1452)
			Runecrafting.locate(player, 3060, 3591);
		else if (itemId == 1462)
			Runecrafting.locate(player, 2872, 3020);
		else if (itemId == 14057)
			SorceressGarden.teleportToSocreressGarden(player, true);
		else if (itemId == 18339)
			Coalbag.addCoal(player);
		else if (itemId == 11283)
			DragonfireShield.empty(player);
		else if (itemId == 15492 || itemId == 13263)
			Slayer.dissasembleSlayerHelmet(player, itemId == 15492);
		else if (Slayer.isBlackMask(itemId)) {
			player.getInventory().replaceItem(8921, 1, slot);
			player.getPackets().sendGameMessage("You remove all the charges from the black mask.");
		} else
			player.getPackets().sendGameMessage("Nothing interesting happens.");
	}

	public static void handleItemOption7(Player player, int slotId, int itemId, Item item) {
		if (itemId == 32151 || itemId == 20767)
			SkillCapeCustomizer.startCustomizing(player, itemId);
		else if (Consumables.eat(player, slotId, item))
			return;
		else if (itemId == 995) {
			if (player.isCanPvp()) {
				player.getPackets()
						.sendGameMessage("You cannot access your money pouch within a player-vs-player zone.");
				return;
			}
			player.getMoneyPouch().sendDynamicInteraction(item.getAmount(), false, MoneyPouch.TYPE_POUCH_INVENTORY);
		} else if (itemId == 4155) {
			player.getInterfaceManager().sendCentralInterface(1309);
			player.getPackets().sendIComponentText(1309, 37, "List Co-Op Partner");
		} else if (itemId == 31846)
			player.getReaperTasks().checkKillsLeft();
		else if (player.getToolbelt().addItem(slotId, item))
			return;
		else if (itemId == 15492 || itemId == 13263)
			Slayer.dissasembleSlayerHelmet(player, itemId == 15492);
		else if (itemId == 20659 || itemId == 20657 || itemId == 20655 || itemId == 20653) {
			player.getPackets().sendGameMessage(Color.ORANGE, "Your ring of wealth shines more brightly!");
			return;
		} else if (itemId == 30656) {
			player.getInventory().addItem(new Item(13263, 1));
			player.getInventory().addItem(new Item(15490, 1));
			player.getInventory().addItem(new Item(15488, 1));
			player.getInventory().deleteItem(new Item(30656));
		} else if (itemId == 30686) {
			player.getInventory().addItem(new Item(13263, 1));
			player.getInventory().addItem(new Item(15490, 1));
			player.getInventory().addItem(new Item(15488, 1));
			player.getInventory().deleteItem(new Item(30686));
		} else if (itemId == 30716) {
			player.getInventory().addItem(new Item(13263, 1));
			player.getInventory().addItem(new Item(15490, 1));
			player.getInventory().addItem(new Item(15488, 1));
			player.getInventory().deleteItem(new Item(30716));
		}

		if (Settings.DEBUG)

		{
			Logger.log("ItemHandler", "Item option 7: " + itemId + ", slotId: " + slotId);
			player.getPackets().sendGameMessage("Item option 7: " + itemId + ", slotId " + slotId + ".", true);
		}
	}

	public static void handleItemOption8(Player player, int slotId, int itemId, Item item) {
		if (player.isLocked() || player.isStunned() || player.getEmotesManager().isDoingEmote())
			return;
		if (!player.getControllerManager().canDropItem(item))
			return;
		player.stopAll(false);
		if (player.getPetManager().spawnPet(itemId, true))
			return;
		if (item.getDefinitions().isDestroyItem() && item.getId() != 30372) {
			player.getDialogueManager().startDialogue("DestroyItemOption", slotId, item);
			return;
		}
		if (GrandExchange.getPrice(item.getId()) >= 1000000) {
			player.getDialogueManager().startDialogue("HighValueItemOption", item);
			return;
		}
		if (item.getId() == 707 || item.getId() == 703) {
			player.setNextForceTalk(new ForceTalk("Ow! The " + item.getName().toLowerCase() + " exploded!"));
			int damage = item.getId() == 703 ? 350 : 650;
			player.applyHit(new Hit(player,
					player.getHitpoints() - damage < 35
							? player.getHitpoints() - 35 < 0 ? 0 : player.getHitpoints() - 35 : damage,
					HitLook.REGULAR_DAMAGE));
			player.setNextAnimation(new Animation(827));
			player.setNextGraphics(new Graphics(954));
			player.getInventory().deleteItem(slotId, item);
			return;
		}

		player.getInventory().deleteItem(slotId, item);
		if (player.getCharges().degradeCompletely(item, true) != -1)
			item.setId(player.getCharges().degradeCompletely(item, false));
		if (player.isBeginningAccount()) {
			World.addGroundItem(item, new WorldTile(player), player, true, 60, 2, 0);
		} else if (player.getControllerManager().getController() instanceof Wilderness
				&& ItemConstants.isTradeable(item))
			World.addGroundItem(item, new WorldTile(player), player, false, -1);
		else if (System.currentTimeMillis() - player.lastDrop > 500) {
			World.addGroundItem(item, new WorldTile(player), player, true, 60);
			Logger.globalLog(player.getUsername(), player.getSession().getIP(),
					" has dropped item [ id: " + item.getId() + ", amount: " + item.getAmount() + " ].");
		}
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			final String FILE_PATH = Settings.getDropboxLocation() + "logs/dropped/";
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + player.getUsername() + ".txt", true));
			writer.newLine();
			writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: " + player.getSession().getIP() + "] "
					+ "Player: " + player.getUsername() + " - Dropped: " + item.getName() + " - Amount: "
					+ item.getAmount() + " at: " + player.getX() + ", " + player.getY() + ", " + player.getPlane());
			writer.flush();
			writer.close();
		} catch (IOException er) {
			System.out.println("Error picking up.");
		}
	}

	public static void handleExamine(Player player, int slotId, int itemId, Item item) {
		player.getInventory().sendExamine(slotId);
	}

}