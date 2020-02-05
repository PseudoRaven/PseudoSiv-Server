package net.nocturne.game.npc.dungeonnering;

import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.WorldTile;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.player.actions.skills.dungeoneering.RoomReference;
import net.nocturne.game.player.actions.skills.mining.MiningBase;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public final class BulwarkBeast extends DungeonBoss {

	private int shieldHP;
	private int maxShieldHP;

	public BulwarkBeast(int id, WorldTile tile, DungeonManager manager,
			RoomReference reference) {
		super(id, tile, manager, reference);
		maxShieldHP = shieldHP = 500;
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		handleHit(hit);
		super.handleIngoingHit(hit);
	}

	private void handleHit(Hit hit) {
		if (getShieldHP() <= 0 || hit.getLook() == HitLook.MAGIC_DAMAGE)
			return;
		hit.setDamage(0);
		Entity source = hit.getSource();
		if (source == null || !(source instanceof Player))
			return;
		if (hit.getLook() != HitLook.MELEE_DAMAGE)
			return;
		Player playerSource = (Player) source;
		int weaponId = playerSource.getEquipment().getWeaponId();
		if (weaponId != -1
				&& MiningBase.getPickAxeDefinitions(playerSource, true) != null) {
			hit.setDamage(Utils.random(50));
			hit.setLook(HitLook.ABSORB_DAMAGE);
			setShieldHP(getShieldHP() - hit.getDamage());
			playerSource
					.getPackets()
					.sendGameMessage(
							getShieldHP() > 0 ? "Your pickaxe chips away at the beast's armour plates."
									: "Your pickaxe finally breaks through the heavy armour plates.");
			refreshBar();
		}
	}

	private int getShieldHP() {
		return shieldHP;
	}

	private void setShieldHP(int shieldHP) {
		this.shieldHP = shieldHP;
	}

	public boolean hasShield() {
		return getShieldHP() > 0 && !isDead() && !hasFinished();
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		refreshBar();
	}

	public void refreshBar() {
		if (hasShield())
			getManager().showBar(getReference(), "Bulwark Beast's Armour",
					shieldHP * 100 / maxShieldHP);
		else
			getManager().hideBar(getReference());
	}

}
