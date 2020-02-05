package net.nocturne.game.npc.slayer;

import net.nocturne.game.Hit;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;

public class Airut extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4306635067955525202L;

	public Airut(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (hit.getSource() instanceof Player) {
			int damage = hit.getDamage();
			int newDamage = 0;
			int hp = getHitpoints();
			int maxHp = getMaxHitpoints();
			if (hp < (maxHp - (maxHp * 0.75))) {
				newDamage = (int) (damage - (damage * 0.70));
			} else if (hp < (maxHp - (maxHp * 0.50))) {
				newDamage = (int) (damage - (damage * 0.50));
			} else if (hp < (maxHp - (maxHp * 0.25))) {
				newDamage = (int) (damage - (damage * 0.20));
			} else {
				newDamage = damage;
			}
			hit.setDamage(newDamage);
		}
		super.handleIngoingHit(hit);
	}
}