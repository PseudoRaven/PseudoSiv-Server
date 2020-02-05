package net.nocturne.game.player;

import java.io.Serializable;

import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.FloorItem;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.actions.Drinkables.Drink;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.controllers.ControllerHandler;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.utils.Logger;

public final class ControllerManager implements Serializable {

	private static final long serialVersionUID = 2084691334731830796L;

	private transient Player player;
	private transient Controller controller;
	private transient boolean inited;
	private Object[] lastControllerArguments;

	private String lastController;

	public ControllerManager() {
		lastController = "";
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Controller getController() {
		return controller;
	}

	public void startController(Object key, Object... parameters) {
		if (controller != null)
			forceStop();
		controller = (Controller) (key instanceof Controller ? key
				: ControllerHandler.getController(key));
		if (controller == null)
			return;
		controller.setPlayer(player);
		lastControllerArguments = parameters;
		lastController = (String) key;
		controller.start();
		inited = true;
		Logger.globalLog(player.getUsername(), player.getSession().getIP(),
				" started controller: " + key.toString() + ".");
	}

	public void login() {
		if (lastController == null)
			return;
		controller = ControllerHandler.getController(lastController);
		if (controller == null) {
			forceStop();
			return;
		}
		controller.setPlayer(player);
		if (controller.login())
			forceStop();
		else
			inited = true;
	}

	public void logout() {
		if (controller == null)
			return;
		if (controller.logout())
			forceStop();
	}

	public boolean canMove(int dir) {
		return controller == null || !inited || controller.canMove(dir);
	}

	public boolean addWalkStep(int lastX, int lastY, int nextX, int nextY) {
		return controller == null || !inited
				|| controller.checkWalkStep(lastX, lastY, nextX, nextY);
	}

	public boolean canTakeItem(FloorItem item) {
		return controller == null || !inited || controller.canTakeItem(item);
	}

	public boolean keepCombating(Entity target) {
		return controller == null || !inited
				|| controller.keepCombating(target);
	}

	public boolean canEquip(int slotId, int itemId) {
		return controller == null || !inited
				|| controller.canEquip(slotId, itemId);
	}

	public boolean canRemoveEquip(int slotId, int itemId) {
		return controller == null || !inited
				|| controller.canRemoveEquip(slotId, itemId);
	}

	public boolean canAddInventoryItem(int itemId, int amount) {
		return controller == null || !inited
				|| controller.canAddInventoryItem(itemId, amount);
	}

	public void trackXP(int skillId, int addedXp) {
		if (controller == null || !inited)
			return;
		controller.trackXP(skillId, addedXp);
	}

	public void trackLevelUp(int skillId, int level) {
		if (controller == null || !inited)
			return;
		controller.trackLevelUp(skillId, level);
	}

	public boolean canDeleteInventoryItem(int itemId, int amount) {
		return controller == null || !inited
				|| controller.canDeleteInventoryItem(itemId, amount);
	}

	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		return controller == null || !inited
				|| controller.canUseItemOnItem(itemUsed, usedWith);
	}

	public boolean canAttack(Entity entity) {
		return controller == null || !inited || controller.canAttack(entity);
	}

	public boolean canPlayerOption1(Player target) {
		return controller == null || !inited
				|| controller.canPlayerOption1(target);
	}

	public boolean canPlayerOption2(Player target) {
		return controller == null || !inited
				|| controller.canPlayerOption2(target);
	}

	public boolean canPlayerOption3(Player target) {
		return controller == null || !inited
				|| controller.canPlayerOption3(target);
	}

	public boolean canPlayerOption4(Player target) {
		return controller == null || !inited
				|| controller.canPlayerOption4(target);
	}

	public boolean canHit(Entity entity) {
		return controller == null || !inited || controller.canHit(entity);
	}

	public void moved() {
		if (controller == null || !inited)
			return;
		controller.moved();
	}

	public void magicTeleported(int type) {
		if (controller == null || !inited)
			return;
		player.getAppearence().setRenderEmote(-1);
		controller.magicTeleported(type);
	}

	public void sendInterfaces() {
		if (controller == null || !inited)
			return;
		controller.sendInterfaces();
	}

	public void process() {
		if (controller == null || !inited)
			return;
		controller.process();
	}

	public boolean sendDeath() {
		return controller == null || !inited || controller.sendDeath();
	}

	public boolean canEat(int heal) {
		return controller == null || !inited || controller.canEat(heal);
	}

	public boolean canPot(Drink pot) {
		return controller == null || !inited || controller.canPot(pot);
	}

	public boolean useDialogueScript(Object key) {
		return controller == null || !inited
				|| controller.useDialogueScript(key);
	}

	public boolean processMagicTeleport(WorldTile toTile) {
		return controller == null || !inited
				|| controller.processMagicTeleport(toTile);
	}

	public boolean processItemTeleport(WorldTile toTile) {
		return controller == null || !inited
				|| controller.processItemTeleport(toTile);
	}

	public boolean processObjectTeleport(WorldTile toTile) {
		return controller == null || !inited
				|| controller.processObjectTeleport(toTile);
	}

	public boolean processObjectClick1(WorldObject object) {
		return controller == null || !inited
				|| controller.processObjectClick1(object);
	}

	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int slotId2, int packetId) {
		return controller == null
				|| !inited
				|| controller.processButtonClick(interfaceId, componentId,
						slotId, slotId2, packetId);
	}

	public boolean processNPCClick1(NPC npc) {
		return controller == null || !inited
				|| controller.processNPCClick1(npc);
	}

	public boolean canSummonFamiliar() {
		return controller == null || !inited || controller.canSummonFamiliar();
	}

	public boolean processNPCClick2(NPC npc) {
		return controller == null || !inited
				|| controller.processNPCClick2(npc);
	}

	public boolean processNPCClick3(NPC npc) {
		return controller == null || !inited
				|| controller.processNPCClick3(npc);
	}

	public boolean processNPCClick4(NPC npc) {
		return controller == null || !inited
				|| controller.processNPCClick4(npc);
	}

	public boolean processObjectClick2(WorldObject object) {
		return controller == null || !inited
				|| controller.processObjectClick2(object);
	}

	public boolean processObjectClick3(WorldObject object) {
		return controller == null || !inited
				|| controller.processObjectClick3(object);
	}

	public boolean processItemOnNPC(NPC npc, Item item) {
		return controller == null || !inited
				|| controller.processItemOnNPC(npc, item);
	}

	public boolean canDropItem(Item item) {
		return controller == null || !inited || controller.canDropItem(item);
	}

	public void forceStop() {
		if (controller != null) {
			controller.forceClose();
			controller = null;
		}
		lastControllerArguments = null;
		lastController = null;
		inited = false;
		Logger.globalLog(player.getUsername(), player.getSession().getIP(),
				" current controller has been stopped.");
	}

	public void removeControllerWithoutCheck() {
		controller = null;
		lastControllerArguments = null;
		lastController = null;
		inited = false;
		Logger.globalLog(player.getUsername(), player.getSession().getIP(),
				" current controller has been stopped.");
	}

	public void setLastController(String controller, Object... args) {
		lastController = controller;
		lastControllerArguments = args;
	}

	public Object[] getLastControllerArguments() {
		return lastControllerArguments;
	}

	public String getLastController() {
		return lastController;
	}

	public void setLastControllerArguments(Object[] lastControllerArguments) {
		this.lastControllerArguments = lastControllerArguments;
	}

	public boolean processObjectClick4(WorldObject object) {
		return controller == null || !inited
				|| controller.processObjectClick4(object);
	}

	public boolean processObjectClick5(WorldObject object) {
		return controller == null || !inited
				|| controller.processObjectClick5(object);
	}

	public boolean handleItemOnObject(WorldObject object, Item item) {
		return controller == null || !inited
				|| controller.handleItemOnObject(object, item);
	}

	public boolean processItemOnPlayer(Player p2, Item item, int slot) {
		return controller == null || !inited
				|| controller.processItemOnPlayer(p2, item, slot);
	}

	public void processNPCDeath(NPC id) {
		if (controller == null || !inited)
			return;
		controller.processNPCDeath(id);
	}

	public void processIncomingHit(Hit hit, Entity target) {
		if (controller == null || !inited)
			return;
		controller.processIncommingHit(hit, target);
	}

	public void processIngoingHit(Hit hit) {
		if (controller == null || !inited)
			return;
		controller.processIngoingHit(hit);
	}
}
