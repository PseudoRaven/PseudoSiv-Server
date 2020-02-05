package net.nocturne.game.npc.araxxi;

import net.nocturne.Engine;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.ItemsContainer;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class Araxxi extends NPC {

	/**
	 * {ITEMID , MIN , MAX , lower the number the rarer the item}
	 */
	private static final int[][] REWARDS = { { 31718, 1, 1, 3 }, // spider leg
																	// middle
			{ 31719, 1, 1, 3 }, // spider leg top
			{ 31720, 1, 1, 3 }, // spider leg bottom
			{ 28547, 1, 1, 15 }, // crystal triskellion 1
			{ 28548, 1, 1, 15 }, // crystal triskellion 2
			{ 28549, 1, 1, 15 }, // crystal triskellion 3
			{ 1127, 10, 10, 57 }, // rune platebody
			{ 31867, 40, 40, 31 }, // hydrix bolt tips
			{ 1444, 40, 70, 57 }, // water talismans
			{ 1514, 150, 323, 57 }, // magic logs
			{ 1516, 600, 600, 57 }, // yew logs
			{ 454, 600, 600, 57 }, // coal
			{ 29863, 2, 3, 15 }, // sirenic scale
			{ 6572, 2, 2, 15 }, // uncut onyx
			{ 450, 100, 100, 57 }, // adamant ores
			{ 1748, 70, 90, 57 }, // black d hide
			{ 26750, 2, 6, 57 }, // overload flasks (6)
			{ 23400, 2, 5, 57 }, // full restore flasks(6)
			{ 23352, 9, 15, 57 }, // saradomin brew flasks
			{ 15273, 8, 11, 57 }, // rocktails
			{ 31737, 70, 90, 85 }, // aracite arrow
			{ 452, 50, 50, 31 }, // rune ore
			{ 9245, 101, 251, 57 }, // onyx bolt(e)
			{ 1392, 50, 70, 31 }, // battlestaff
			{ 5316, 5, 8, 57 }, // magic seed
			{ 5303, 10, 10, 57 }, // dwarf weed seed
			{ 212, 45, 45, 57 }, // grimy avatoe
			{ 218, 45, 45, 57 }, // grimy dwarfweed
			{ 2486, 40, 55, 57 }, // Grimy lantadyme
			{ 995, 400024, 499225, 37 }, // Coins
			{ 31722, 1, 1, 1 }, // fang
			{ 31724, 1, 1, 1 }, // web
			{ 31723, 1, 1, 1 }, // eye
			{ 33870, 1, 1, 10 } // pheromone
	};
	/**
	 * The rewards container.
	 */
	private final ItemsContainer<Item> rewards = new ItemsContainer<>(10, true);
	public boolean canFollow = false;
	public int attackNumber = 0;
	public boolean canSpecial = false;
	public double damageMulti = 0.0;
	public double healingMulti = 0.0;
	public int minionNumber = 0;
	public int startingHp = 0;
	public int playerEnrageLevel = 0;
	public int AraxDeathZ;
	public int AraxDeathY;
	public int AraxDeathX;
	public boolean isFinished = false;
	private List<NPC> minions = new ArrayList<>();
	@SuppressWarnings("unused")
	private Iterator<NPC> iter = minions.iterator();
	private Player attacker;

	/**
	 * Starts the Araxxi NPC with specific instructions for spawning
	 *
	 * @param id
	 *            NPC ID (static number)
	 * @param tile
	 *            WorldTile that the NPC will spawn at
	 * @param mapAreaNameHash
	 *            NameHash of the region
	 * @param canBeAttackFromOutOfArea
	 *            Can the npc be attacked out of the area? true:false
	 * @param spawned
	 *            should the NPC spawn now? true:false
	 * @param attacker
	 *            the player that is in the instance
	 */
	public Araxxi(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned, Player attacker) {

		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCapDamage(10000);
		setForceAgressive(true);
		setForceMultiArea(true);
		isFinished = false;
		attackNumber = 0;
		canSpecial = false;
		canFollow = false;
		this.attacker = attacker;
		setIntelligentRouteFinder(true);
		setForceFollowClose(false);
	}

	/**
	 * #unused
	 * <p>
	 * Handles the Enrage numbers for the fight
	 */

	@SuppressWarnings("unused")
	public void EnrageNumbers() {
		// default values - if there is 0 enrage
		// multi values are 1 because x *1 = x;
		damageMulti = 1.0;
		healingMulti = 1.0;
		minionNumber = 3;
		startingHp = 100000;
		if (playerEnrageLevel != 0) {
			for (int i = 0; i < playerEnrageLevel; i++) {
				damageMulti += 0.15;
				healingMulti += 0.15;
				minionNumber += 1;
				startingHp += 2500;
			}
		}
	}

	/**
	 * Handles the spider spawning
	 */
	public void spawnSpiders() {

		WorldTile tile = new WorldTile(attacker, 1);
		int value = Utils.random(3);
		if (minions.size() < minionNumber) {
			switch (value) {
			case 0:
				// Blade spider
				NPC spider = new NPC(19458, tile, -1, true, true);
				spider.setForceMultiArea(true);
				spider.getCombat().setTarget(this.getCombat().getTarget());
				spider.setNoClipWalking(true);
				spider.setForceFollowClose(true);
				spider.removeClipping();
				minions.add(spider);
				break;
			case 1:
				// Imbude spider
				spider = new NPC(19459, tile, -1, true, true);
				spider.setForceMultiArea(true);
				spider.getCombat().setTarget(this.getCombat().getTarget());
				spider.setNoClipWalking(true);
				spider.removeClipping();
				minions.add(spider);
				break;
			case 2:
				// Spit spider
				spider = new NPC(19460, tile, -1, true, true);
				spider.setForceMultiArea(true);
				spider.getCombat().setTarget(this.getCombat().getTarget());
				spider.setNoClipWalking(true);
				spider.removeClipping();
				minions.add(spider);
				break;
			}
		}
	}

	/**
	 * Removes the spider npcs from the controller and the fight
	 */
	private void removeSpider() {

		for (Iterator<NPC> iter = minions.iterator(); iter.hasNext();) {
			NPC npc = iter.next();
			npc.finish();
			iter.remove();
		}
	}

	/**
	 * Handles the hit interaction via the npc and the player If the araxxorHeal
	 * in the player boolean variable is enabled, damage would be dealt to the
	 * player with reflected damage
	 *
	 * @param hit
	 *            the actual damage that will be applied from NPC to player
	 */
	@Override
	public void handleIngoingHit(Hit hit) {

		Entity player = hit.getSource();
		if (player instanceof Player) {
			Player p = (Player) player;
			if (p.araxxorHeal) {
				player.applyHit(new Hit(player, hit.getDamage(),
						HitLook.REFLECTED_DAMAGE));
			}
		}
		super.handleIngoingHit(hit);
	}

	public ItemsContainer<Item> getRewards() {

		return rewards;
	}

	@Override
	public void processNPC() {

		super.processNPC();
		if (isDead())
			return;
		for (Iterator<NPC> iter = minions.iterator(); iter.hasNext();) {
			NPC npc = iter.next();
			if (npc.hasFinished())
				iter.remove();
		}
		int maxhp = getMaxHitpoints();
		if (maxhp > getHitpoints() && getPossibleTargets().isEmpty())
			setHitpoints(maxhp);
		if (getPossibleTargets().isEmpty()) {
			removeSpider();

		}
	}

	/**
	 * Removes all spiders and grabs the X Y Z of the boss Does not reset the
	 * combat state of Araxxi Locks the NPC in place Starts the reward system
	 * calculations
	 * <p>
	 * Sends the death of the NPC and generates the Enrage for Araxxi
	 *
	 * @param source
	 *            Entity that the script will run for
	 */
	@Override
	public void sendDeath(Entity source) {

		removeSpider();
		WorldTile tile = Utils
				.getFreeTile(new WorldTile(this.getX() + 1, this.getY() + 1,
						this.getPlane()), 3);
		AraxDeathX = tile.getX();
		AraxDeathY = tile.getY();
		AraxDeathZ = tile.getPlane();
		setCantInteract(false);
		setLocked(true);
		if (getMostDamageReceivedSourcePlayer().araxxiEnrage == 0) {
			getMostDamageReceivedSourcePlayer().araxxiEnrageTimer = Utils
					.currentTimeMillis();
		}
		getMostDamageReceivedSourcePlayer().araxxiEnrage++;
		startDeath(getMostDamageReceivedSourcePlayer());
		getMostDamageReceivedSourcePlayer().getTimersManager().increaseKills(
				RecordKey.ARAXXI, false);
		getMostDamageReceivedSourcePlayer().getCompCapeManager()
				.increaseRequirement(Requirement.ARAXXI, 1);
		prepareRewards(getMostDamageReceivedSourcePlayer());
	}

	/**
	 * Handles the reward system that Araxxi uses
	 * <p>
	 * Chance for Fang, eye and web : 35% for it to actually drop Chance for Top
	 * Middle Bottom spider leg : 50% for it to actually drop
	 */
	private void prepareRewards(Player player) {

		List<Item> rewardTable = new ArrayList<>();
		for (int[] reward : REWARDS) {
			Item item = new Item(reward[0], Utils.random(reward[1], reward[2]));
			for (int i = 0; i < reward[3]; i++) {
				if (item.getId() == ItemIdentifiers.ARAXXIS_FANG
						|| item.getId() == ItemIdentifiers.ARAXXIS_WEB
						|| item.getId() == ItemIdentifiers.ARAXXIS_EYE) {
					if (Utils.random(100) > 65) {
						rewardTable.add(item);
					}
				} else if (item.getId() == ItemIdentifiers.SPIDER_LEG_TOP
						|| item.getId() == ItemIdentifiers.SPIDER_LEG_MIDDLE
						|| item.getId() == ItemIdentifiers.SPIDER_LEG_BOTTOM) {
					if (Utils.random(100) > 50) {
						rewardTable.add(item);
					}
				} else {
					rewardTable.add(item);
				}
			}
		}
		Collections.shuffle(rewardTable);
		for (int i = 0; i < 1 + 3; i++) {
			Item reward = rewardTable.get(Utils.random(rewardTable.size()));
			rewards.add(reward);
			if (reward.getName().contains("noxious")
					|| reward.getName().contains("spider leg")
					|| reward.getName().contains("araxxi")
					|| reward.getName().contains("araxyte egg")) {
				Engine.getDiscordBot()
						.getChannel("287324187079475202")
						.sendMessage(
								player.getDisplayName() + " has received "
										+ reward.getName().toLowerCase()
										+ " drop!");
				World.sendNews(player, player.getDisplayName()
						+ " has received " + reward.getName().toLowerCase()
						+ " drop!", 0, false);
			}
		}
	}

	/**
	 * Starts the death event for the player towards Araxxi
	 *
	 * @param player
	 *            player that is inside of the instance
	 */
	private void startDeath(Player player) {

		WorldTasksManager.schedule(new WorldTask() {
			int time;

			@Override
			public void run() {

				time++;
				if (time == 1) {
					isFinished = true;
					finish();

				}
				if (time == 9) {
					spawnCorpse(player);
					stop();
				}
			}
		}, 0, 0);
	}

	/**
	 * sets state of monster to finished Spawns the Araxxi corpse in place of
	 * the XYZ coordinates provided in the {@link #sendDeath(Entity)} Makes it
	 * so that any player in that instance kills Araxxi gets the object spawn
	 *
	 * @param player
	 *            the player object from the instance
	 */
	private void spawnCorpse(Player player) {

		isFinished = true;
		finish();
		final WorldObject AraxxiBody = new WorldObject(91673, 10, 0,
				AraxDeathX, AraxDeathY, AraxDeathZ, player);
		World.spawnTemporaryDivineObject(AraxxiBody, 120000, player);
		player.getPackets().sendGameMessage(
				"Araxxi's Corpse will only last 2 minutes.");
	}

	/**
	 * Uses recordKey to increase the boss kill count for the player Sends the
	 * interface with correct component text with proper implementation of
	 * ItemOptionsScript
	 *
	 * @param player
	 *            the player object from the instance
	 */
	public void openRewardChest(Player player) {
		player.getInterfaceManager().sendCentralInterface(1284);
		player.getPackets().sendIComponentText(1284, 28, "Araxxi's Corpse");
		player.getPackets().sendInterSetItemsOptionsScript(1284, 7, 100, 8, 3,
				"Take", "Bank", "Discard", "Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(1284, 7, 0, 10, 0,
				1, 2, 3);
		player.getPackets().sendItems(100, rewards);
	}
}