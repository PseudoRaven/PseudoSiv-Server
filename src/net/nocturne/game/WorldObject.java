package net.nocturne.game;

import net.nocturne.cache.loaders.ObjectDefinitions;
import net.nocturne.game.player.Player;

@SuppressWarnings("serial")
public class WorldObject extends WorldTile {

	private int id;
	private int type;
	private int rotation;
	private int life;
	public Player owner;

	public WorldObject(int id, int type, int rotation, int x, int y, int plane,
			Player owner) {
		super(x, y, plane);
		this.owner = owner;
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	public WorldObject(int id, int type, int rotation, WorldTile tile) {
		super(tile.getX(), tile.getY(), tile.getPlane());
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	public WorldObject(int id, int type, int rotation, int x, int y, int plane) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	public WorldObject(int id, int type, int rotation, int x, int y, int plane,
			int life) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = life;
	}

	public WorldObject(WorldObject object) {
		super(object.getX(), object.getY(), object.getPlane());
		this.id = object.id;
		this.type = object.type;
		this.rotation = object.rotation;
		this.life = object.life;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void decrementObjectLife() {
		this.life--;
	}

	public ObjectDefinitions getDefinitions() {
		return ObjectDefinitions.getObjectDefinitions(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Player getOwner() {
		return owner;
	}

	public boolean isAt(int x, int y) {
		return this.x == x && this.y == y;
	}

	public boolean isAt(int x, int y, int z) {
		return this.x == x && this.y == y && this.plane == z;
	}
}