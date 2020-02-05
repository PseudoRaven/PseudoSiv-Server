package net.nocturne.game.npc.dungeonnering;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Entity;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonConstants;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonUtils;
import net.nocturne.game.player.actions.skills.dungeoneering.RoomReference;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class DungeonNPC extends NPC {

	private DungeonManager manager;
	private int[] bonuses;
	private boolean marked;
	private double multiplier;

	public DungeonNPC(int id, WorldTile tile, DungeonManager manager,
			double multiplier) {
		super(id, tile, -1, true, true);
		setManager(manager);
		setMultiplier(multiplier);
		if (getDefinitions().hasAttackOption()) {
			int level = manager.getTargetLevel(id, this instanceof DungeonBoss
					|| this instanceof DungeonSkeletonBoss, multiplier);
			setCombatLevel(level);
			setHitpoints(getMaxHitpoints());
			resetBonuses();
		}
		setForceAgressive(true);
		setForceTargetDistance(50); // includes whole room
	}

	void resetBonuses() {
		bonuses = manager.getBonuses(this instanceof DungeonBoss,
				getCombatLevel());
	}

	@Override
	public int getRespawnDirection() {
		return getDirection();
	}

	NPC getNPC(int id) {
		List<Integer> npcsIndexes = World.getRegion(getRegionId())
				.getNPCsIndexes();
		if (npcsIndexes != null) {
			for (int npcIndex : npcsIndexes) {
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc.getId() == id) {
					return npc;
				}
			}
		}
		return null;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (marked) {
			getManager().removeMark();
			marked = false;
		}
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isUnderCombat()) {
			Entity target = getCombat().getTarget();
			RoomReference thisR = getManager().getCurrentRoomReference(this);
			RoomReference targetR = getManager()
					.getCurrentRoomReference(target);
			if (!targetR.equals(thisR))
				getCombat().removeTarget();
		}
	}

	@Override
	public int getMaxHitpoints() {
		return getCombatLevel() * (this instanceof DungeonBoss ? 200 : 100) + 1;
	}

	public int getMaxHit() {
		return getCombatLevel();
	}

	@Override
	public int[] getBonuses() {
		return bonuses == null ? super.getBonuses() : bonuses;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	private int getBones() {
		return getName().toLowerCase().contains("dragon") ? 536
				: getSize() > 1 ? 532 : 526;
	}

	@Override
	public void drop() {
		int size = getSize();
		ArrayList<Item> drops = new ArrayList<>();

		if (getId() != 10831 && getId() != 10821)
			drops.add(new Item(getBones()));

		for (int i = 0; i < 1 + Utils.random(10); i++)
			drops.add(new Item(DungeonUtils.getFood(1 + Utils.random(8))));

		if (Utils.random(10) == 0)
			drops.add(new Item(DungeonUtils.getDagger(1 + Utils.random(5))));

		if (Utils.random(5) == 0)
			drops.add(new Item(DungeonConstants.RUNES[Utils
					.random(DungeonConstants.RUNES.length)], 90 + Utils
					.random(30)));

		if (getManager().getParty().getComplexity() >= 5
				&& Utils.random(5) == 0)
			drops.add(new Item(DungeonUtils.getTornBag(1 + Utils.random(10))));

		if (getManager().getParty().getComplexity() >= 3
				&& Utils.random(5) == 0)
			drops.add(new Item(DungeonUtils.getOre(1 + Utils.random(5)),
					1 + Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 2
				&& Utils.random(5) == 0)
			drops.add(new Item(DungeonUtils.getBranche(1 + Utils.random(5)),
					1 + Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 4
				&& Utils.random(5) == 0)
			drops.add(new Item(DungeonUtils.getTextile(1 + Utils.random(10)),
					1 + Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 5
				&& Utils.random(5) == 0)
			drops.add(new Item(DungeonUtils.getHerb(1 + Utils.random(9)),
					1 + Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 5
				&& Utils.random(5) == 0)
			drops.add(new Item(DungeonUtils.getSeed(1 + Utils.random(12)),
					1 + Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 5
				&& Utils.random(3) == 0)
			drops.add(new Item(DungeonConstants.CHARMS[Utils
					.random(DungeonConstants.CHARMS.length)], size));

		if (getManager().getParty().getComplexity() >= 2)
			drops.add(new Item(DungeonConstants.RUSTY_COINS, 1000 + Utils
					.random(10001)));

		if (getManager().getParty().getComplexity() >= 3
				&& Utils.random(5) == 0)
			drops.add(new Item(DungeonConstants.RUNE_ESSENCE, 10 + Utils
					.random(300)));

		if (getManager().getParty().getComplexity() >= 2
				&& Utils.random(5) == 0)
			drops.add(new Item(DungeonConstants.FEATHER, 10 + Utils.random(300)));

		if ((getManager().getParty().getComplexity() >= 5 && Utils.random(10) == 0))
			drops.add(new Item(17490));

		if ((Utils.random(10) == 0))
			drops.add(new Item(16933));

		if ((getManager().getParty().getComplexity() >= 4 && Utils.random(10) == 0))
			drops.add(new Item(17752));

		if ((getManager().getParty().getComplexity() >= 2 && Utils.random(10) == 0))
			drops.add(new Item(17794));

		if ((getManager().getParty().getComplexity() >= 4 && Utils.random(5) == 0))
			drops.add(new Item(17447, 10 + Utils.random(300)));

		for (Item item : drops)
			World.addGroundItem(item, new WorldTile(getCoordFaceX(size),
					getCoordFaceY(size), getPlane()));
	}

	public DungeonManager getManager() {
		return manager;
	}

	public void setManager(DungeonManager manager) {
		this.manager = manager;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
}