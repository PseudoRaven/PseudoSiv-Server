package net.nocturne.game.player.actions.skills.hunter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.OwnedObjectManager;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;

@SuppressWarnings("unused")
public class NetTrapAction extends Action {

	private enum HunterNPC {

		PET_SQUIRREL(15602, new Item[] { new Item(29931, 5) }, 29, 152,
				HunterEquipment.YOUNG_TREE, 28567, 19192, new Animation(5191),
				new Animation(5192)),

		PENGUIN(15602, new Item[] { new Item(29931, 5) }, 50, 250,
				HunterEquipment.YOUNG_TREE, 28567, 19192, new Animation(5191),
				new Animation(5192)),

		SWAMP_LIZARD(5117, new Item[] { new Item(12539, 5) }, 29, 152,
				HunterEquipment.YOUNG_TREE, 28567, 19192, new Animation(5191),
				new Animation(5192)),

		ORANGE_SALAMANDER(5114, new Item[] { new Item(10146) }, 224, 265,
				HunterEquipment.YOUNG_TREE, 28558, 19192, new Animation(5184),
				new Animation(5185)),

		RED_SALAMANDER(5115, new Item[] { new Item(10147) }, 59, 272,
				HunterEquipment.YOUNG_TREE, 19189, 19192, new Animation(5191),
				new Animation(5192)),

		BLACK_SALAMANDER(5116, new Item[] { new Item(10148) }, 67, 304,
				HunterEquipment.YOUNG_TREE, 19190, 19192, new Animation(8362),
				new Animation(8361));

		private int npcId, level, successfulTransformObjectId,
				failedTransformObjectId;
		private Item[] item;
		private double xp;
		private HunterEquipment hunter;
		private Animation successCatchAnim, failCatchAnim;

		static final Map<Integer, HunterNPC> npc = new HashMap<>();
		static final Map<Integer, HunterNPC> object = new HashMap<>();

		public static HunterNPC forId(int id) {
			return npc.get(id);
		}

		static {
			for (HunterNPC npcs : HunterNPC.values())
				npc.put(npcs.npcId, npcs);
			for (HunterNPC objects : HunterNPC.values())
				object.put(objects.successfulTransformObjectId, objects);
		}

		public static HunterNPC forObjectId(int id) {
			return object.get(id);
		}

		HunterNPC(int npcId, Item[] item, int level, double xp,
				HunterEquipment hunter, int successfulTransformObjectId,
				int failedTransformObjectId, Animation successCatchAnim,
				Animation failCatchAnim) {
			this.npcId = npcId;
			this.item = item;
			this.level = level;
			this.xp = xp;
			this.hunter = hunter;
			this.successfulTransformObjectId = successfulTransformObjectId;
			this.failedTransformObjectId = failedTransformObjectId;
			this.successCatchAnim = successCatchAnim;
			this.failCatchAnim = failCatchAnim;
		}

		public int getLevel() {
			return level;
		}

		public int getNpcId() {
			return npcId;
		}

		public double getXp() {
			return xp;
		}

		public Item[] getItems() {
			return item;
		}

		public HunterEquipment getEquipment() {
			return hunter;
		}

		public int getSuccessfulTransformObjectId() {
			return successfulTransformObjectId;
		}

		public int getFailedTransformObjectId() {
			return failedTransformObjectId;
		}

		public HunterEquipment getHunter() {
			return hunter;
		}

		public Animation getSuccessCatchAnim() {
			return successCatchAnim;
		}

		public Animation getFailCatchAnim() {
			return failCatchAnim;
		}
	}

	private enum HunterEquipment {

		YOUNG_TREE(new int[] { 303, 954 }, 19678, 19681, new Animation(5208),
				29);

		private final int[] itemIds;
		private final int replaceObjectId;
		private final int placeObjectId;
		private final int baseLevel;
		private final Animation pickUpAnimation;

		HunterEquipment(int[] itemIds, int replaceObjectId, int placeObjectId,
				Animation pickUpAnimation, int baseLevel) {
			this.itemIds = itemIds;
			this.replaceObjectId = replaceObjectId;
			this.placeObjectId = placeObjectId;
			this.pickUpAnimation = pickUpAnimation;
			this.baseLevel = baseLevel;
		}

		public int getId() {
			return itemIds[0];
		}

		public int[] getIds() {
			return itemIds;
		}

		public int getReplaceObjectId() {
			return replaceObjectId;
		}

		public int getPlaceObjectId() {
			return placeObjectId;
		}

		public Animation getPickUpAnimation() {
			return pickUpAnimation;
		}

		public int getBaseLevel() {
			return baseLevel;
		}
	}

	private final HunterEquipment hunt;

	private int getTrapAmount(Player player) {
		int level = 20;
		int trapAmount = 2;
		for (int i = 0; i < 3; i++) {
			if (player.getSkills().getLevel(Skills.HUNTER) >= level) {
				trapAmount++;
				level += 20;
			}
		}
		return trapAmount;
	}

	public NetTrapAction(HunterEquipment hunt) {
		this.hunt = hunt;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage("You start setting up the trap..");
		player.setNextAnimation(new Animation(5208));
		player.lock(3);
		setActionDelay(player, 2);
		return true;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
		for (int itemId : hunt.getIds())
			player.getInventory().deleteItem(itemId, 1);
		OwnedObjectManager
				.addOwnedObjectManager(
						player,
						new WorldObject[] { new WorldObject(hunt
								.getPlaceObjectId(), 10, 0, player.getX(),
								player.getY(), player.getPlane()) },
						new long[] { 600000 });
		return -1;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.HUNTER) < hunt.getBaseLevel()) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a Hunter level of " + hunt.getBaseLevel()
							+ " to use this.");
			return false;
		}
		int trapAmt = getTrapAmount(player);
		if (OwnedObjectManager.getObjectsforValue(player,
				hunt.getPlaceObjectId()) == trapAmt) {
			player.getPackets().sendGameMessage(
					"You can't setup more than " + trapAmt + " traps.");
			return false;
		}
		if (!World.canMoveNPC(0, player.getX(), player.getY(),
				player.getPlane())) {
			player.getPackets().sendGameMessage(
					"You can't setup your trap here.");
			return false;
		}
		List<WorldObject> objects = World.getRegion(player.getRegionId())
				.getSpawnedObjects();
		if (objects != null) {
			for (WorldObject object : objects) {
				if (object.getX() == player.getX()
						&& object.getY() == player.getY()
						&& object.getPlane() == player.getPlane()) {
					player.getPackets().sendGameMessage(
							"You can't setup your trap here.");
					return false;
				}
			}
		}
		return true;
	}
}