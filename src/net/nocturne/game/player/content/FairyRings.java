package net.nocturne.game.player.content;

import java.io.Serializable;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Logger;

public class FairyRings implements Serializable {

	private enum Rings {

		HUB(2412, 4434, 0, "Fairy Home"),

		AIQ(2996, 3114, 0, "Mudskipper Point"),

		AJR(2780, 3613, 0, "Golden Apple Tree"),

		AKQ(2319, 3619, 0, "Pictoris Hunter Area"),

		AKS(2571, 2956, 0, "Feldip Hunter Area"),

		ALP(2472, 3027, 0, "JigJig"),

		ALS(2644, 3495, 0, "McGrubor's Woods"),

		BIP(3410, 3324, 0, "Polypore Entrance"),

		BIQ(3251, 3095, 0, "Kalphite Queen"),

		BJQ(1737, 5342, 0, "Ancient Cavern"),

		BJR(2741, 3235, 0, "Fisher Realm"),

		BKP(2385, 3035, 0, "Castle Wars"),

		BKR(3469, 3431, 0, "Mort Myre"),

		BLP(4622, 5147, 0, "Tzhaar"),

		BLR(2740, 3351, 0, "Legend's Guild"),

		CIP(2513, 3884, 0, "Miscellania"),

		CIQ(2528, 3127, 0, "Yanille"),

		CJR(2705, 3576, 0, "Sinclair Mansion"),

		DJR(2676, 3587, 0, "Sinclair Mansion 2"),

		CKS(3447, 3470, 0, "Canifis"),

		CLR(2735, 2742, 0, "Ape Atoll"),

		DIP(3763, 2930, 0, "MosLe Harmless"),

		DIS(3108, 3149, 0, "Wizard's Tower"),

		DJP(2658, 3230, 0, "Tower of Life"),

		DKP(2900, 3111, 0, "Musa Point"),

		DKQ(4183, 5726, 0, "Glacor Cave"),

		DKR(3129, 3496, 0, "Grand Exchange"),

		DKS(2744, 3719, 0, "Snowy Hunter Area"),

		DLQ(3423, 3016, 0, "Kharidian Desert"),

		AIR(2700, 3247, 0, "Witch Island"),

		AJS(2500, 3896, 0, "Penguin Island"),

		ALR(3059, 4875, 0, "Abyss"),

		BIR(2455, 4396, 0, "Sparse Plane"),

		BIS(2635, 3266, 0, "Ardougne Zoo"),

		BKQ(3041, 4532, 0, "Enchanted Valley"),

		DIR(3038, 5348, 0, "Gorak Plane"),

		DLR(2213, 3099, 0, "Poison Waste"),

		BLS(2412, 4434, 0, "Zanaris");

		private final int x;

		private final int y;

		private final int plane;

		Rings(int x, int y, int plane, String description) {
			this.x = x;
			this.y = y;
			this.plane = plane;
		}

		public WorldTile getTile() {
			return new WorldTile(x, y, plane);
		}

	}

	private static final long serialVersionUID = 1714313085013227278L;
	public final int FAIRY_INTERFACE = 734;
	private int firstRing;
	private int secondRing;
	private int thirdRing;
	private boolean fairyClickDelay;

	private Player player;

	public FairyRings(Player player) {
		this.setPlayer(player);
	}

	private void closeInterface() {
		player.getInterfaceManager().removeInterface(FAIRY_INTERFACE);
	}

	public boolean getFairyClickDelay() {
		return fairyClickDelay;
	}

	public int getFirstRing() {
		return firstRing;
	}

	public Player getPlayer() {
		return player;
	}

	public Rings getRing() {
		final char[] charsAtOne = { 'a', 'd', 'c', 'b' };
		final char[] charsAtTwo = { 'i', 'l', 'k', 'j' };
		final char[] charsAtThree = { 'p', 's', 'r', 'q' };
		final String code = Character.toString(charsAtOne[getFirstRing()])
				+ Character.toString(charsAtTwo[getSecondRing()])
				+ Character.toString(charsAtThree[getThirdRing()]);
		for (final Rings ring : Rings.values()) {
			if (ring.name().equalsIgnoreCase(code))
				return ring;
		}
		return null;

	}

	public int getSecondRing() {
		return secondRing;
	}

	public int getThirdRing() {
		return thirdRing;
	}

	public boolean handleButtons(int interfaceId, int componentId) {
		if (interfaceId == FAIRY_INTERFACE) {
			Logger.log("FairyRing", "ComponentId: " + componentId);
			if (getFairyClickDelay()) {
				switch (componentId) {
				case 119:
					handleTeleport();
					return true;
				case 84:
					if (getFirstRing() == 2)
						return true;
					if (getFirstRing() == 1) {
						setFirstRing(3);
					} else {
						if (getFirstRing() + 1 == 4)
							setFirstRing(0);
						else
							setFirstRing(getFirstRing() + 1);

					}
					return true;
				case 85:
					// ringone end
					if (getFirstRing() - 1 < 0)
						setFirstRing(3);
					else
						setFirstRing(getFirstRing() - 1);
					return true;
				case 86:
					if (getSecondRing() == 2)
						return true;
					if (getSecondRing() == 1) {
						setSecondRing(3);
					} else {
						if (getSecondRing() + 1 == 4)
							setSecondRing(0);
						else
							setSecondRing(getSecondRing() + 1);
					}
					return true;
				case 87:
					// ringtwo end
					if (getSecondRing() - 1 < 0)
						setSecondRing(3);
					else
						setSecondRing(getSecondRing() - 1);
					return true;
				case 88:
					if (getThirdRing() == 2)
						return true;
					if (getThirdRing() == 1) {
						setThirdRing(3);
					} else {
						if (getThirdRing() + 1 == 4)
							setThirdRing(0);
						else
							setThirdRing(getThirdRing() + 1);
					}
					return true;
				case 89:
					// ringthree end
					if (getThirdRing() - 1 < 0)
						setThirdRing(3);
					else
						setThirdRing(getThirdRing() - 1);
					return true;
				default:
					return false;
				}
			}
		}
		return false;
	}

	public void handleObjects(final WorldObject object) {
		player.stopAll();
		player.addWalkSteps(object.getX(), object.getY());
		if (object.getX() == Rings.HUB.x && object.getY() == Rings.HUB.y
				&& object.getPlane() == Rings.HUB.plane)
			openInterface();
		else
			handleTeleportHub();
	}

	public void openInterface() {
		reset(player);
		player.getInterfaceManager().setBackgroundInterface(true,
				FAIRY_INTERFACE);
	}

	private void reset(Player player) {
		setFirstRing(0);
		setSecondRing(0);
		setThirdRing(0);
		fairyClickDelay = true;
	}

	public void setFairyClickDelay(boolean fairyClickDelay) {
		this.fairyClickDelay = fairyClickDelay;
	}

	public void setFirstRing(int firstRing) {
		this.firstRing = firstRing;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setSecondRing(int secondRing) {
		this.secondRing = secondRing;
	}

	public void setThirdRing(int thirdRing) {
		this.thirdRing = thirdRing;
	}

	private void handleTeleport() {
		if (getRing() == null) {
			player.getPackets().sendGameMessage("This combination is invalid");
			return;
		}
		if (Settings.DEBUG)
			Logger.log("FairyRings", "Ring code: " + getRing());
		WorldTasksManager.schedule(new WorldTask() {
			int steps;

			@Override
			public void run() {
				if (steps == 0) {
					closeInterface();
					player.setNextAnimation(new Animation(3254));
					player.setNextGraphics(new Graphics(2670));
				} else if (steps == 2) {
					if (getRing() == null || getRing().getTile() == null)
						player.getPackets().sendGameMessage(
								"Something went wrong.");
					else
						player.setNextWorldTile(new WorldTile(getRing()
								.getTile()));
				} else if (steps == 3) {
					player.setNextFaceWorldTile(new WorldTile(player.getX(),
							player.getY() - 1, player.getPlane()));
				} else if (steps == 4) {
					player.setNextAnimation(new Animation(3255));
					player.setNextGraphics(new Graphics(2671));
				} else if (steps >= 5) {
					stop();
				}
				steps++;
			}
		}, 0, 1 / 2);
	}

	private void handleTeleportHub() {
		WorldTasksManager.schedule(new WorldTask() {
			int steps;

			@Override
			public void run() {
				if (steps == 0) {
					closeInterface();
					player.setNextAnimation(new Animation(3254));
					player.setNextGraphics(new Graphics(2670));
				} else if (steps == 2) {
					player.setNextWorldTile(Rings.HUB.getTile());
				} else if (steps == 3) {
					player.setNextFaceWorldTile(new WorldTile(player.getX(),
							player.getY() - 1, player.getPlane()));
				} else if (steps == 4) {
					player.setNextAnimation(new Animation(3255));
					player.setNextGraphics(new Graphics(2671));
				} else if (steps >= 5) {
					stop();
				}
				steps++;
			}
		}, 0, 1 / 2);
	}
}