package net.nocturne.game.player.content.activities.minigames;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Entity;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.others.BarrowsBrother;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.content.Combat;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public final class Barrows extends Controller {

	private static final short[][] TUNNEL_CONFIG = {
			{ 470, 479, 482, 476, 474 }, { 479, 477, 478, 480, 472 },
			{ 477, 471, 472, 476, 475, 478, 480, 477 } };
	private static final int[] CRYPT_NPCS = { 1243, 1244, 1245, 1246, 1247,
			1618, 2031, 2032, 2033, 2034, 2035, 2036, 2037, 4920, 4921, 5381,
			5422, 7637 };
	private static final Item[] COMMON_REWARDS = {
			new Item(ItemIdentifiers.MIND_RUNE, Utils.random(850)),
			new Item(ItemIdentifiers.CHAOS_RUNE, Utils.random(850)),
			new Item(ItemIdentifiers.DEATH_RUNE, Utils.random(600)),
			new Item(ItemIdentifiers.BLOOD_RUNE, Utils.random(850)),
			new Item(ItemIdentifiers.BOLT_RACK, Utils.random(850)) };
	private static final Item[] RARE_REWARDS = {
			new Item(ItemIdentifiers.DRAGON_HELM, 1),
			new Item(ItemIdentifiers.BARROWS_TOTEM, 1),
			new Item(ItemIdentifiers.LOOP_HALF_OF_A_KEY, 1),
			new Item(ItemIdentifiers.TOOTH_HALF_OF_A_KEY, 1) };
	private static final Item[] BARROW_REWARDS = {
			new Item(ItemIdentifiers.AHRIMS_HOOD, 1),
			new Item(ItemIdentifiers.AHRIMS_STAFF, 1),
			new Item(ItemIdentifiers.AHRIMS_ROBE_TOP, 1),
			new Item(ItemIdentifiers.AHRIMS_ROBE_SKIRT, 1),
			new Item(ItemIdentifiers.DHAROKS_HELM, 1),
			new Item(ItemIdentifiers.DHAROKS_GREATAXE, 1),
			new Item(ItemIdentifiers.DHAROKS_PLATEBODY, 1),
			new Item(ItemIdentifiers.DHAROKS_PLATELEGS, 1),
			new Item(ItemIdentifiers.GUTHANS_HELM, 1),
			new Item(ItemIdentifiers.GUTHANS_WARSPEAR, 1),
			new Item(ItemIdentifiers.GUTHANS_PLATEBODY, 1),
			new Item(ItemIdentifiers.GUTHANS_CHAINSKIRT, 1),
			new Item(ItemIdentifiers.KARILS_COIF, 1),
			new Item(ItemIdentifiers.KARILS_CROSSBOW, 1),
			new Item(ItemIdentifiers.KARILS_TOP, 1),
			new Item(ItemIdentifiers.KARILS_SKIRT, 1),
			new Item(ItemIdentifiers.TORAGS_HELM, 1),
			new Item(ItemIdentifiers.TORAGS_HAMMER, 1),
			new Item(ItemIdentifiers.TORAGS_PLATEBODY, 1),
			new Item(ItemIdentifiers.TORAGS_PLATELEGS, 1),
			new Item(ItemIdentifiers.VERACS_HELM, 1),
			new Item(ItemIdentifiers.VERACS_FLAIL, 1),
			new Item(ItemIdentifiers.VERACS_BRASSARD, 1),
			new Item(ItemIdentifiers.VERACS_PLATESKIRT, 1),
			new Item(ItemIdentifiers.AKRISAES_HOOD, 1),
			new Item(ItemIdentifiers.AKRISAES_WAR_MACE, 1),
			new Item(ItemIdentifiers.AKRISAES_ROBE_TOP, 1),
			new Item(ItemIdentifiers.AKRISAES_ROBE_SKIRT, 1) };
	private BarrowsBrother barrowsBrother;
	private int creatureCount;
	private int headComponentId;
	private int timer;

	public Barrows() {

	}

	public static boolean digIntoGrave(final Player player) {
		for (Hills hill : Hills.values()) {
			if (player.getPlane() == hill.outBound.getPlane()
					&& player.getX() >= hill.outBound.getX()
					&& player.getY() >= hill.outBound.getY()
					&& player.getX() <= hill.outBound.getX() + 3
					&& player.getY() <= hill.outBound.getY() + 3) {
				player.useStairs(-1, hill.inside, 1, 2,
						"You've broken into a crypt.");
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.getControllerManager()
								.startController("Barrows");
					}
				});
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canAttack(Entity target) {

		if (target instanceof BarrowsBrother && target != this.barrowsBrother) {
			player.getPackets().sendGameMessage(
					"This isn't your barrowsBrother.");
			return false;
		}
		return true;
	}

	private void exit(WorldTile outside) {
		player.setNextWorldTile(outside);
		leave(false);
	}

	private void leave(boolean logout) {

		if (barrowsBrother != null) {
			barrowsBrother.finish();
			player.getPackets().sendGameMessage(
					"We'll finish this later.......");
		}
		if (!logout) {
			for (int varBit : TUNNEL_CONFIG[getTunnelIndex()])
				player.getVarsManager().sendVarBit(varBit, 0);
			player.getVarsManager().sendVar(1270, 0);
			player.getPackets().sendBlackOut(0);
			if (player.getHiddenBrother() == -1)
				player.getPackets().sendStopCameraShake();
			else
				player.getInterfaceManager().removeMinigameInterface();
			removeController();
		}
	}

	private int getTunnelIndex() {
		if (getArguments() == null || getArguments().length == 0)
			return 0;
		return (int) this.getArguments()[0];
	}

	@Override
	public boolean sendDeath() {
		leave(false);
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		leave(false);
	}

	private int getRandomBrother() {
		List<Integer> bros = new ArrayList<>();
		for (int i = 0; i < Hills.values().length; i++) {
			if (player.getKilledBarrowBrothers()[i]
					|| player.getHiddenBrother() == i)
				continue;
			bros.add(i);
		}
		if (bros.isEmpty())
			return -1;
		return bros.get(Utils.random(bros.size()));

	}

	private void drop(Item item) {
		player.getInventory().addItemDrop(item.getId(), item.getAmount());

	}

	private void sendReward() {
		double percentage = 0;
		for (boolean died : player.getKilledBarrowBrothers()) {
			if (died)
				percentage += 2.5;
		}
		percentage += (player.getBarrowsKillCount() / 40d);
		if (percentage > 90)
			percentage = 90;
		if (percentage >= Math.random() * 95) {
			drop(BARROW_REWARDS[Utils.random(BARROW_REWARDS.length)]);

		}
		boolean ringOfWealth = Combat.hasRingOfWealth(player);
		if (ringOfWealth)
			percentage += 1;
		if (percentage / 2 >= Math.random() * 100) {
			drop(RARE_REWARDS[Utils.random(RARE_REWARDS.length)]);
			player.getPackets().sendGameMessage(
					"<col=ff7000>Your ring of wealth shines more brightly!",
					true);
		}
		for (int i = 0; i < 10; i++)
			if (percentage >= Math.random() * 100)
				drop(COMMON_REWARDS[Utils.random(COMMON_REWARDS.length)]);
		drop(new Item(ItemIdentifiers.COINS, Utils.random(50307)));
		player.getCompCapeManager().increaseRequirement(Requirement.BARROWS, 1);
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() >= 6702 && object.getId() <= 6707) {
			WorldTile out = Hills.values()[object.getId() - 6702].outBound;
			exit(new WorldTile(out.getX() + 1, out.getY() + 1, out.getPlane()));
			return false;
		} else if (object.getId() == 10284) {
			if (player.getHiddenBrother() == -1) {// reached chest
				player.getPackets().sendGameMessage("You found nothing.");
				return false;
			}
			if (!player.getKilledBarrowBrothers()[player.getHiddenBrother()])
				sendTarget(player.getHiddenBrother() == 6 ? 14297
						: 2025 + player.getHiddenBrother(), player);
			if (barrowsBrother != null) {
				player.getPackets().sendGameMessage("You found nothing.");
				return false;
			}
			sendReward();
			player.getPackets().sendCameraShake(3, 12, 25, 12, 25);
			player.getInterfaceManager().removeMinigameInterface();
			player.getPackets().sendAddObject(
					new WorldObject(6775, 10, 0, 3551, 9695, 0));
			player.resetBarrows();
			return false;
		} else if (object.getId() == 6711) {
			player.useStairs(828, new WorldTile(3565, 3312, 0), 1, 2);
			leave(false);
			return false;
		} else if (object.getId() >= 6716 && object.getId() <= 6749) {
			boolean inBetween = player.getTemporaryAttributtes().get(
					"between_barrow_door") != null;
			if (inBetween)
				player.getTemporaryAttributtes().remove("between_barrow_door");
			else
				player.getTemporaryAttributtes().put("between_barrow_door",
						true);
			WorldTile walkTo;
			if (object.getRotation() == 0)
				walkTo = new WorldTile(object.getX() + (inBetween ? -1 : 1),
						object.getY(), 0);
			else if (object.getRotation() == 1)
				walkTo = new WorldTile(object.getX(), object.getY()
						- (inBetween ? -1 : 1), 0);
			else if (object.getRotation() == 2)
				walkTo = new WorldTile(object.getX() - (inBetween ? -1 : 1),
						object.getY(), 0);
			else
				walkTo = new WorldTile(object.getX(), object.getY()
						+ (inBetween ? -1 : 1), 0);
			if (!World.isFloorFree(walkTo.getPlane(), walkTo.getX(),
					walkTo.getY()))
				return false;

			WorldObject opened = new WorldObject(object.getId(),
					object.getType(), object.getRotation() - 1, object.getX(),
					object.getY(), object.getPlane());
			World.spawnObjectTemporary(opened, 600);
			player.addWalkSteps(walkTo.getX(), walkTo.getY(), -1, false);
			player.lock(3);
			player.getVarsManager().sendVar(1270, inBetween ? 0 : 1);
			if (player.getHiddenBrother() != -1) {
				int brother = getRandomBrother();
				if (brother != -1)
					sendTarget(2025 + brother, walkTo);
			}
			return false;
		} else {
			int sarcoId = getSarcophagusId(object.getId());
			if (sarcoId != -1) {
				if (sarcoId == player.getHiddenBrother())
					player.getDialogueManager().startDialogue("BarrowsD");
				else if (barrowsBrother != null
						|| player.getKilledBarrowBrothers()[sarcoId])
					player.getPackets().sendGameMessage("You found nothing.");
				else
					sendTarget(sarcoId == 6 ? 14297 : 2025 + sarcoId, player);
				return false;
			}
		}
		return true;
	}

	private int getSarcophagusId(int objectId) {
		switch (objectId) {
		case 66017:
			return 0;
		case 63177:
			return 1;
		case 66020:
			return 2;
		case 66018:
			return 3;
		case 66019:
			return 4;
		case 66016:
			return 5;
		case 61189:
			return 6;
		default:
			return -1;
		}
	}

	public void targetDied() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		setBrotherSlain(barrowsBrother.getId() >= 14297 ? 6 : barrowsBrother
				.getId() - 2025);
		barrowsBrother = null;

	}

	public void targetFinishedWithoutDie() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		barrowsBrother = null;
	}

	private void setBrotherSlain(int index) {
		player.getKilledBarrowBrothers()[index] = true;
		sendBrotherSlain(index, true);
	}

	private void sendTarget(int id, WorldTile tile) {

		if (barrowsBrother != null)
			barrowsBrother.disappear();
		barrowsBrother = new BarrowsBrother(id, tile, this);
		barrowsBrother.setTarget(player);
		barrowsBrother.setNextForceTalk(new ForceTalk(
				"You dare disturb my rest!"));
		player.getHintIconsManager().addHintIcon(barrowsBrother, 1, -1, false);
	}

	private int getAndIncreaseHeadIndex() {
		Integer head = (Integer) player.getTemporaryAttributtes().remove(
				"BarrowsHead");
		if (head == null || head == player.getKilledBarrowBrothers().length - 1)
			head = 0;
		player.getTemporaryAttributtes().put("BarrowsHead", head + 1);
		return player.getKilledBarrowBrothers()[head] ? head : -1;
	}

	@Override
	public void process() {
		if (timer > 0) {
			timer--;
			return;
		}
		if (headComponentId == 0) {
			if (player.getHiddenBrother() == -1) {
				player.applyHit(new Hit(player, Utils.random(50) + 1,
						HitLook.REGULAR_DAMAGE));
				resetHeadTimer();
				return;
			}
			int headIndex = getAndIncreaseHeadIndex();
			if (headIndex == -1) {
				resetHeadTimer();
				return;
			}
			headComponentId = 9 + Utils.random(2);
			player.getPackets().sendItemOnIComponent(24, headComponentId,
					4761 + headIndex, 0);
			player.getPackets().sendIComponentAnimation(9810, 24,
					headComponentId);
			int activeLevel = player.getPrayer().getPoints();
			if (activeLevel > 0) {
				int level = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
				player.getPrayer().drain(level / 6);
			}
			timer = 3;
		} else {
			player.getPackets()
					.sendItemOnIComponent(24, headComponentId, -1, 0);
			headComponentId = 0;
			resetHeadTimer();
		}
	}

	private void resetHeadTimer() {
		timer = 20 + Utils.random(6);
	}

	@Override
	public void sendInterfaces() {
		if (player.getHiddenBrother() != -1)
			player.getInterfaceManager().sendMinigameInterface(24);
	}

	private void loadData() {
		resetHeadTimer();
		if (getArguments().length == 2)
			creatureCount = (int) getArguments()[1];
		for (int i = 0; i < player.getKilledBarrowBrothers().length; i++)
			sendBrotherSlain(i, player.getKilledBarrowBrothers()[i]);
		sendCreaturesSlainCount(player.getBarrowsKillCount());
		player.getPackets().sendBlackOut(2);
		for (int varBit : TUNNEL_CONFIG[getTunnelIndex()])
			player.getVarsManager().sendVarBit(varBit, 1);
		player.getVarsManager().sendVarBit(467, 1);
	}

	private void sendBrotherSlain(int index, boolean slain) {
		if (index == 6)
			return;
		player.getVarsManager().sendVarBit(4554 + index, slain ? 1 : 0);
	}

	private void sendCreaturesSlainCount(int count) {
		player.getPackets().sendIComponentText(24, 6, Utils.format(count));
		player.getVarsManager().sendVarBit(464, count);
	}

	@Override
	public void start() {
		if (player.getHiddenBrother() == -1 || player.getHiddenBrother() == 6)
			player.setHiddenBrother(Utils.random(6));
		setArguments(new Object[] { Utils.random(TUNNEL_CONFIG.length), 0 });
		loadData();
		sendInterfaces();
	}

	@Override
	public void processNPCDeath(NPC npc) {
		for (int crypt_npc : CRYPT_NPCS) {
			if (npc.getId() == crypt_npc) {
				creatureCount++;
				sendCreaturesSlainCount(creatureCount + 1);
			}
		}
	}

	@Override
	public boolean login() {
		if (player.getHiddenBrother() == -1)
			player.getPackets().sendCameraShake(3, 25, 50, 25, 50);
		loadData();
		sendInterfaces();
		return false;
	}

	@Override
	public boolean logout() {
		leave(true);
		this.setArguments(new Object[] { getTunnelIndex(), creatureCount });
		return false;
	}

	@Override
	public void forceClose() {
		leave(true);
	}

	private enum Hills {
		// old verac 3578, 9706, 3
		AHRIM_HILL(new WorldTile(3564, 3287, 0), new WorldTile(3557, 9703, 3)), DHAROK_HILL(
				new WorldTile(3573, 3296, 0), new WorldTile(3556, 9718, 3)), GUTHAN_HILL(
				new WorldTile(3574, 3279, 0), new WorldTile(3534, 9704, 3)), KARIL_HILL(
				new WorldTile(3563, 3276, 0), new WorldTile(3546, 9684, 3)), TORAG_HILL(
				new WorldTile(3553, 3281, 0), new WorldTile(3568, 9683, 3)), VERAC_HILL(
				new WorldTile(3556, 3296, 0), new WorldTile(3578, 9706, 3));

		private final WorldTile outBound;
		private final WorldTile inside;

		// out bound since it not a full circle

		Hills(WorldTile outBound, WorldTile in) {
			this.outBound = outBound;
			inside = in;
		}
	}
}