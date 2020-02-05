package net.nocturne.network.encoders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.netty.channel.ChannelFuture;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.DynamicRegion;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Region;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.FloorItem;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemsContainer;
import net.nocturne.game.item.ItemsContainerNew;
import net.nocturne.game.map.MapUtils;
import net.nocturne.game.map.MapUtils.Structure;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.ChatMessage;
import net.nocturne.game.player.HintIcon;
import net.nocturne.game.player.InterfaceManager;
import net.nocturne.game.player.Inventory;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.PublicChatMessage;
import net.nocturne.game.player.QuickChatMessage;
import net.nocturne.game.player.content.activities.clans.ClansManager;
import net.nocturne.game.player.content.grandExchange.Offer;
import net.nocturne.login.WorldInformation;
import net.nocturne.network.Session;
import net.nocturne.stream.OutputStream;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;
import net.nocturne.utils.huffman.Huffman;

public class WorldPacketsEncoder extends Encoder {

	private Player player;

	public WorldPacketsEncoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void sendMinimapFlag(int x, int y) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 111);
		stream.writeByteC(y);
		stream.write128Byte(x);
		session.write(stream);
	}

	public void sendResetMinimapFlag() {
		sendMinimapFlag(255, 255);
	}

	public void sendPing() {
		OutputStream packet = new OutputStream(1);
		packet.writePacket(player, 176);
		session.write(packet);
	}

	public void sendWorldList(int clientChecksum, int[] online) {
		int lowestWorldId = Integer.MAX_VALUE;
		int highestWorldId = Integer.MIN_VALUE;
		int ourChecksum = Settings.WORLDS_INFORMATION.length;
		for (int i = 0; i < Settings.WORLDS_INFORMATION.length; i++) {
			WorldInformation world = Settings.WORLDS_INFORMATION[i];
			ourChecksum += (ourChecksum * world.hashCode());
			if (world.getId() < lowestWorldId)
				lowestWorldId = world.getId();
			if (world.getId() > highestWorldId)
				highestWorldId = world.getId();
		}

		OutputStream packet = new OutputStream();
		packet.writePacketVarShort(player, 149);
		packet.writeByte(1); // 0 - buffer only, 1 - parse
		packet.writeByte(2); // list version
		packet.writeByte(ourChecksum != clientChecksum ? 1 : 0);
		if (ourChecksum != clientChecksum) {
			packet.writeSmart(Settings.WORLDS_INFORMATION.length); // number of
			// locations
			for (int i = 0; i < Settings.WORLDS_INFORMATION.length; i++) {
				WorldInformation world = Settings.WORLDS_INFORMATION[i];
				packet.writeSmart(world.getCountryFlagID());
				packet.writeVersionedString(world.getCountryName());

			}

			packet.writeSmart(lowestWorldId);
			packet.writeSmart(highestWorldId);
			packet.writeSmart(Settings.WORLDS_INFORMATION.length); // number of
			// worlds
			for (int i = 0; i < Settings.WORLDS_INFORMATION.length; i++) {
				WorldInformation world = Settings.WORLDS_INFORMATION[i];
				packet.writeSmart(world.getId() - lowestWorldId);
				packet.writeByte(i); // world location index
				packet.writeInt(world.getFlags());
				packet.writeSmart(0); // if not 0 also sends a string which i
				// dont know what is for yet
				packet.writeVersionedString(world.getActivity());
				packet.writeVersionedString(Settings.VPS1_IP);
			}
			packet.writeInt(ourChecksum);
		}

		for (int i = 0; i < Settings.WORLDS_INFORMATION.length; i++) {
			WorldInformation world = Settings.WORLDS_INFORMATION[i];
			packet.writeSmart(world.getId() - lowestWorldId);
			if (online[i] == -1)
				packet.writeShort(65535);
			else
				packet.writeShort(online[i]);
		}

		packet.endPacketVarShort();
		session.write(packet);
	}

	public void sendPlayerUnderNPCPriority(boolean priority) {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 61);
		stream.write128Byte(priority ? 1 : 0);
		session.write(stream);
	}

	public void sendHintIcon(HintIcon icon) {
		OutputStream stream = new OutputStream(16);
		stream.writePacket(player, 129);
		stream.writeByte((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5));
		if (icon.getTargetType() == 0)
			stream.skip(13);
		else {
			stream.writeByte(icon.getArrowType());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				stream.writeShort(icon.getTargetIndex());
				stream.writeShort(2500); // how often the arrow flashes, 2500
				// ideal, 0 never
				stream.skip(4);
			} else if ((icon.getTargetType() >= 2 && icon.getTargetType() <= 6)) { // directions
				stream.writeByte(icon.getPlane()); // unknown
				stream.writeShort(icon.getCoordX());
				stream.writeShort(icon.getCoordY());
				stream.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
				stream.writeShort(-1); // distance to start showing on minimap,
				// 0 doesnt show, -1 infinite
			}
			stream.writeInt(icon.getModelId());
		}
		session.write(stream);

	}

	public void sendCameraShake(int slotId, int b, int c, int d, int e) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 27); //
		stream.writeByteC(d);
		stream.writeByte128(slotId);
		stream.writeShort(e);
		stream.writeByte(b);
		stream.writeByteC(c);
		session.write(stream);
	}

	public void sendStopCameraShake() {
		sendResetCamera();
	}

	public void sendIComponentModel(int interfaceId, int componentId,
			int modelId) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 171);
		stream.writeInt(modelId);
		stream.writeIntV1(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendGrandExchangeOffer(Offer offer) {
		OutputStream stream = new OutputStream(21);
		stream.writePacket(player, 131);
		stream.writeByte(0);
		stream.writeByte(offer.getSlot());
		stream.writeByte(offer.getStage());
		if (offer.forceRemove())
			stream.skip(18);
		else {
			stream.writeShort(offer.getId());
			stream.writeInt(offer.getPrice());
			stream.writeInt(offer.getAmount());
			stream.writeInt(offer.getTotalAmountSoFar());
			stream.writeInt(offer.getTotalPriceSoFar());
		}
		session.write(stream);
	}

	public void sendIComponentSprite(int interfaceId, int componentId,
			int spriteArchive) {
		OutputStream stream = new OutputStream(11);
		stream.writePacket(player, 3);
		stream.writeInt(spriteArchive);
		stream.writeInt(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendHideIComponent(int interfaceId, int componentId,
			boolean hidden) {
		OutputStream stream = new OutputStream(6);
		stream.writePacket(player, 134);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeByte128(hidden ? 1 : 0);
		session.write(stream);
	}

	public void sendRemoveGroundItem(FloorItem item) {
		OutputStream stream = createChunkInSceneStream(item.getTile());
		stream.writePacket(player, 96);
		stream.writeShort128(item.getId());
		stream.writeByte128((item.getTile().getXInChunk() << 4)
				| item.getTile().getYInChunk());
		session.write(stream);
	}

	public void sendGroundItem(FloorItem item) {
		OutputStream stream = createChunkInSceneStream(item.getTile());
		stream.writePacket(player, 191);
		stream.writeShort128(0);// Ground items in region, useless failsafe.
		stream.writeShortLE(item.getAmount());
		stream.writeByteC((item.getTile().getXInChunk() << 4)
				| item.getTile().getYInChunk());
		stream.writeShortLE(item.getId());
		session.write(stream);
	}

	public void refreshLobbyInformation() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 81);
		session.write(stream);
	}

	public void refreshSomethingIdk() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 18);
		session.write(stream);
	}

	@Deprecated
	public void sendProjectile(Entity receiver, WorldTile startTile,
			WorldTile endTile, int gfxId, int startHeight, int endHeight,
			int speed, int delay, int curve, int startDistanceOffset,
			int creatorSize) {
		/*
		 * fuck the idiot who made this refactoring OutputStream stream =
		 * createWorldTileStream(startTile); stream.writePacket(player, 20); int
		 * localX = startTile.getLocalX(player.getLastLoadedMapRegionTile(),
		 * player.getMapSize()); int localY =
		 * startTile.getLocalY(player.getLastLoadedMapRegionTile(),
		 * player.getMapSize()); int offsetX = localX - ((localX >> 3) << 3);
		 * int offsetY = localY - ((localY >> 3) << 3);
		 * stream.writeByte((offsetX << 3) | offsetY);
		 * stream.writeByte(endTile.getX() - startTile.getX());
		 * stream.writeByte(endTile.getY() - startTile.getY());
		 * stream.writeShort(receiver == null ? 0 : (receiver instanceof Player
		 * ? -(receiver.getIndex() + 1) : receiver.getIndex() + 1));
		 * stream.writeShort(gfxId); stream.writeByte(startHeight);
		 * stream.writeByte(endHeight); stream.writeShort(delay); int duration =
		 * (Utils.getDistance(startTile.getX(), startTile.getY(),
		 * endTile.getX(), endTile.getY()) * 30 / ((speed / 10) < 1 ? 1 : (speed
		 * / 10))) + delay; stream.writeShort(duration);
		 * stream.writeByte(curve); stream.writeShort(creatorSize * 64 +
		 * startDistanceOffset * 64); //////// // session.write(stream);
		 */

		sendProjectileProper(startTile, creatorSize, creatorSize, endTile,
				receiver != null ? receiver.getSize() : 1,
				receiver != null ? receiver.getSize() : 1, receiver, gfxId,
				startHeight, endHeight, delay, (Utils.getDistance(
						startTile.getX(), startTile.getY(), endTile.getX(),
						endTile.getY()) * 30 / ((speed / 10) < 1 ? 1
						: (speed / 10))), startDistanceOffset, curve);

	}

	public void sendProjectileProper(WorldTile from, int fromSizeX,
			int fromSizeY, WorldTile to, int toSizeX, int toSizeY,
			Entity lockOn, int gfxId, int startHeight, int endHeight,
			int delay, int speed, int slope, int angle) {
		WorldTile src = new WorldTile(((from.getX() << 1) + fromSizeX) >> 1,
				((from.getY() << 1) + fromSizeY) >> 1, from.getPlane());
		WorldTile dst = new WorldTile(((to.getX() << 1) + toSizeX) >> 1,
				((to.getY() << 1) + toSizeY) >> 1, to.getPlane());
		OutputStream stream = createChunkInSceneStream(src);
		stream.writePacket(player, 110);
		// includes a boolean here now. idk what for. 0x80
		stream.writeByte(((src.getX() & 0x7) << 3) | (src.getY() & 0x7));

		stream.writeByte(dst.getX() - src.getX());
		stream.writeByte(dst.getY() - src.getY());
		stream.writeShort(lockOn == null ? 0
				: (lockOn instanceof Player ? -(lockOn.getIndex() + 1) : lockOn
						.getIndex() + 1));
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		stream.writeShort(delay + speed);
		stream.writeByte(angle);
		stream.writeShort(slope);

		// new. idk, according to client, doesnt do anything, if 0, havent
		// checked much but it seemed to compare with target index
		stream.writeShort(0);
		session.write(stream);
	}

	public void sendProjectileNew(WorldTile from, int fromSizeX, int fromSizeY,
			WorldTile to, int toSizeX, int toSizeY, Entity fromE, Entity toE,
			boolean adjustFlyingHeight, boolean adjustSenderHeight,
			int senderBodyPartId, int graphicId, int startHeight,
			int endHeight, int startTime, int endTime, int slope, int angle,
			int unknown) {

		int fromX = from.getXInScene(player);
		int fromY = from.getYInScene(player);

		int toX = to.getXInScene(player);
		int toY = to.getYInScene(player);

		int originalFromX = fromX;
		int originalFromY = fromY;

		fromX = fromX * 2 + fromSizeX;
		fromY = fromY * 2 + fromSizeY;
		toX = toX * 2 + toSizeX;
		toY = toY * 2 + toSizeY;

		int baseChunk[] = MapUtils.decode(Structure.CHUNK,
				player.getSceneBaseChunkId());

		OutputStream stream = createChunkInSceneStream(new WorldTile(
				baseChunk[0] * 8 + fromX / 2, baseChunk[1] * 8 + fromY / 2,
				from.getPlane()));

		stream.writePacket(player, 112);

		int adjX = ((originalFromX & 0x7) * 2) + (fromX % 2);
		int adjY = ((originalFromY & 0x7) * 2) + (fromY % 2);

		int pos = 0;
		pos |= (adjX & 0xF) << 4;
		pos |= (adjY & 0xF);

		int settings = 0;
		settings |= adjustFlyingHeight ? 0x1 : 0;
		settings |= adjustSenderHeight ? 0x2 : 0;
		settings |= adjustSenderHeight ? senderBodyPartId << 2 : 0;

		stream.writeByte(pos);
		stream.writeByte(settings);
		stream.writeByte(toX - fromX);
		stream.writeByte(toY - fromY);
		stream.writeShort(fromE == null ? 0
				: (fromE instanceof Player ? -(fromE.getIndex() + 1) : fromE
						.getIndex() + 1));
		stream.writeShort(toE == null ? 0 : (toE instanceof Player ? -(toE
				.getIndex() + 1) : toE.getIndex() + 1));
		stream.writeShort(graphicId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(startTime);
		stream.writeShort(endTime);
		stream.writeByte(angle);
		stream.writeShort(slope);
		stream.writeShort(unknown);

		session.write(stream);
	}

	public void sendUnlockIComponentOptionSlots(int interfaceId,
			int componentId, int fromSlot, int toSlot, int... optionsSlots) {
		int settingsHash = 0;
		for (int slot : optionsSlots)
			settingsHash |= 2 << slot;
		sendIComponentSettings(interfaceId, componentId, fromSlot, toSlot,
				settingsHash);
	}

	public void sendUnlockIComponentOptionSlots(int interfaceId,
			int componentId, int fromSlot, int toSlot, boolean unlockEvent,
			int... optionsSlots) {
		int settingsHash = unlockEvent ? 1 : 0;
		for (int slot : optionsSlots)
			settingsHash |= 2 << slot;
		sendIComponentSettings(interfaceId, componentId, fromSlot, toSlot,
				settingsHash);
	}

	public void sendIComponentSettings(int interfaceId, int componentId,
			int fromSlot, int toSlot, int settingsHash) {
		OutputStream stream = new OutputStream(13);
		stream.writePacket(player, 124);
		stream.writeShortLE128(toSlot);
		stream.writeIntV2(interfaceId << 16 | componentId);
		stream.writeShort(fromSlot);
		stream.writeIntV2(settingsHash);
		session.write(stream);
	}

	public void sendInterFlashScript(int interfaceId, int componentId,
			int width, int height, int slot) {
		Object[] parameters = new Object[4];
		int index = 0;
		parameters[index++] = slot;
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		sendExecuteScriptReverse(143, parameters);
	}

	public void sendInterSetItemsOptionsScript(int interfaceId,
			int componentId, int key, int width, int height, String... options) {
		sendInterSetItemsOptionsScript(interfaceId, componentId, key, false,
				width, height, options);
	}

	public void sendInterSetItemsOptionsScript(int interfaceId,
			int componentId, int key, boolean negativeKey, int width,
			int height, String... options) {
		Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		parameters[index++] = -1; // dunno but always this
		parameters[index++] = 0;// dunno but always this, maybe startslot?
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendExecuteScriptReverse(negativeKey ? 695 : 150, parameters); // scriptid
		// 150
		// does
		// that the method
		// name says*/
	}

	public void sendPouchInfusionOptionsScript(boolean dung, int interfaceId,
			int componentId, int slotLength, int width, int height,
			String... options) {
		Object[] parameters = new Object[5 + options.length];
		int index = 0;
		if (dung) {
			parameters[index++] = 1159;
			parameters[index++] = 1100;
		} else {
			parameters[index++] = slotLength;
			parameters[index++] = 1;
		}
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		sendExecuteScriptReverse(757, parameters);
	}

	public void sendScrollInfusionOptionsScript(boolean dung, int interfaceId,
			int componentId, int slotLength, int width, int height,
			String... options) {
		Object[] parameters = new Object[5 + options.length];
		int index = 0;
		if (dung) {
			parameters[index++] = 1159;
			parameters[index++] = 1100;
		} else {
			parameters[index++] = slotLength;
			parameters[index++] = 1;
		}
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		sendExecuteScriptReverse(763, parameters);
	}

	public void sendInputNameScript(String message) {
		player.getInterfaceManager().sendInputTextInterface();
		sendExecuteScript(109, new Object[] { message });
	}

	public void sendInputIntegerScript(String message) {
		player.getInterfaceManager().sendInputTextInterface();
		sendExecuteScript(108, new Object[] { message });
	}

	public void sendInputLongTextScript(String message) {
		player.getInterfaceManager().sendInputTextInterface();
		sendExecuteScript(110, new Object[] { message });
	}

	public void sendExecuteScript(int scriptId, Object... params) {
		List<Object> l = Arrays.asList(params);
		Collections.reverse(l);
		sendExecuteScriptReverse(scriptId, l.toArray());
	}

	public void sendExecuteScriptReverse(int scriptId, Object... params) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 45);
		String parameterTypes = "";
		if (params != null) {
			for (int count = params.length - 1; count >= 0; count--) {
				if (params[count] instanceof String)
					parameterTypes += "s"; // string
				else
					parameterTypes += "i"; // integer
			}
		}
		stream.writeString(parameterTypes);
		if (params != null) {
			int index = 0;
			for (int count = parameterTypes.length() - 1; count >= 0; count--) {
				if (parameterTypes.charAt(count) == 's')
					stream.writeString((String) params[index++]);
				else
					stream.writeInt((Integer) params[index++]);
			}
		}
		stream.writeInt(scriptId);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendCSVarInteger(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendCSVarInteger2(id, value);
		else
			sendCSVarInteger1(id, value);
	}

	public void sendCSVarInteger1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 41);
		stream.writeShortLE128(id);
		stream.writeByte128(value);
		session.write(stream);
	}

	public void sendCSVarInteger2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 87);
		stream.writeIntLE(value);
		stream.writeShortLE(id);

		session.write(stream);
	}

	/*
	 * resets queue of sent vars from client to server
	 */
	public void sendResetNISVars() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 147);
		session.write(stream);
	}

	public void sendNISVar(int id, Object value) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 67);
		stream.writeShort(id);
		if (value instanceof Integer)
			stream.writeInt((int) value);
		else if (value instanceof Long)
			stream.writeLong((long) value);
		else if (value instanceof String)
			stream.writeVersionedString((String) value);
		else {
			// TODO last type
		}
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendConfig(int id, int value) {
		sendVar(id, value);
	}

	public void sendVar(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendVar2(id, value);
		else
			sendVar1(id, value);
	}

	public void sendVar1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 142);
		stream.writeShortLE128(id);
		stream.writeByteC(value);
		session.write(stream);
	}

	public void sendVar2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 128);
		stream.writeInt(value);
		stream.writeShortLE128(id);
		session.write(stream);
	}

	public void sendRunEnergy() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 29);
		stream.writeByte(player.getRunEnergy());
		session.write(stream);
	}

	public void refreshWeight() {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 25);
		stream.writeShort((int) player.getWeight());
		session.write(stream);
	}

	public void sendIComponentText(int interfaceId, int componentId, String text) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 34);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeString(text);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendIComponentAnimation(int emoteId, int interfaceId,
			int componentId) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 92);
		stream.writeIntV2(emoteId);
		stream.writeIntV2(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendItemOnIComponent(int interfaceid, int componentId, int id,
			int amount) {
		OutputStream stream = new OutputStream(11);
		stream.writePacket(player, 109);
		stream.writeInt(interfaceid << 16 | componentId);
		stream.writeIntLE(amount);
		stream.writeShort128(id);
		session.write(stream);

	}

	public void sendEntityOnIComponent(boolean isPlayer, int entityId,
			int interfaceId, int componentId) {
		if (isPlayer)
			sendPlayerHeadOnIComponent(interfaceId, componentId);
		else
			sendNPCHeadOnIComponent(interfaceId, componentId, entityId);
	}

	private OutputStream createChunkInSceneStream(WorldTile tile) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 157);
		stream.writeByteC(tile.getPlane());
		stream.writeByte(tile.getChunkXInScene(player));
		stream.writeByte128(tile.getChunkYInScene(player));
		return stream;
	}

	public void sendObjectAnimation(WorldObject object, Animation animation) {
		OutputStream stream = new OutputStream(10);
		stream.writePacket(player, 158);
		stream.writeIntV1(object.getTileHash());
		stream.writeByteC(0);// Not important
		stream.writeIntLE(animation.getIds()[0]);
		stream.writeByte128((object.getType() << 2)
				+ (object.getRotation() & 0x3));
		session.write(stream);
	}

	public void sendAppearenceLook() {
		sendAppearanceLook(player.getEquipment().getCosmeticItems().getItems(),
				player.getAppearence().getLook());
	}

	public void sendAppearanceLook(Item[] items, int[] look) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 180);
		stream.writeByte(player.getAppearence().isMale() ? 0 : 1);
		stream.writeBytes(player.getAppearence().getAppearenceLook(items, look));
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendAddObject(WorldObject object) {
		OutputStream stream = createChunkInSceneStream(object);
		stream.writePacket(player, 6);
		stream.write128Byte((object.getType() << 2)
				+ (object.getRotation() & 0x3));
		stream.writeByte128((object.getXInChunk() << 4) | object.getYInChunk());
		stream.writeIntLE(object.getId());
		session.write(stream);
	}

	public void sendRemoveObject(WorldObject object) {
		OutputStream stream = createChunkInSceneStream(object);
		stream.writePacket(player, 103);
		stream.writeByteC((object.getType() << 2)
				+ (object.getRotation() & 0x3));
		stream.writeByte((object.getXInChunk() << 4) | object.getYInChunk());
		session.write(stream);
	}

	public void sendPlayerHeadOnIComponent(int interfaceId, int componentId) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 160);
		stream.writeIntV2(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendPlayerOnIComponent(int interfaceId, int componentId) {
		OutputStream stream = new OutputStream(6);
		stream.writePacket(player, 185);
		stream.write128Byte(0);
		stream.writeIntLE(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendOtherPlayerOnIComponent(int interfaceId, int componentId,
			Player p2) {
		OutputStream stream = new OutputStream(11); // um,
		stream.writePacket(player, 153);
		stream.writeShortLE128(p2.getIndex());
		stream.writeIntV2(p2.getDisplayName().hashCode());
		stream.writeIntLE(interfaceId << 16 | componentId);
		session.write(stream);

	}

	public void sendNPCHeadOnIComponent(int interfaceId, int componentId,
			int npcId) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 104);
		stream.writeIntV1(interfaceId << 16 | componentId);
		stream.writeIntV2(npcId);
		session.write(stream);
	}

	public void sendRandomOnIComponent(int interfaceId, int componentId, int id) {
		/*
		 * OutputStream stream = new OutputStream(); stream.writePacket(player,
		 * 235); stream.writeShort(id); stream.writeIntV1(interfaceId << 16 |
		 * componentId); stream.writeShort(interPacketsCount++); //////// //
		 * session.write(stream);
		 */
	}

	public void sendFaceOnIComponent(int interfaceId, int componentId,
			int look1, int look2, int look3) {
		/*
		 * OutputStream stream = new OutputStream(); stream.writePacket(player,
		 * 192); stream.writeIntV2(interfaceId << 16 | componentId);
		 * stream.writeShortLE128(interPacketsCount++);
		 * stream.writeShortLE128(look1); stream.writeShortLE128(look2);
		 * stream.writeShort128(look2); //////// // session.write(stream);
		 */
	}

	public OutputStream startFriendsChatPacket() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 23);
		return stream;
	}

	public void sendEmptyFriendsChatChannel() {
		endFriendsChatChannel(startFriendsChatPacket());
	}

	public OutputStream startFriendsChatChannel(String owner, String name,
			int kickReq, int membersCount) {
		OutputStream stream = startFriendsChatPacket();
		stream.writeString(owner);
		stream.writeByte(0); // not sending extra name string which is not used
		// anywhere in client
		stream.writeString(name);
		stream.writeByte(kickReq);
		stream.writeByte(membersCount);
		return stream;
	}

	public void appendFriendsChatMember(OutputStream stream,
			String displayName, int worldId, int rank, String worldName) {
		stream.writeString(displayName);
		stream.writeByte(0); // no secondary name (this sync's with second and
		// 4th string in ignore list)
		stream.writeShort(worldId);
		stream.writeByte(rank);
		stream.writeString(worldName);
	}

	public void endFriendsChatChannel(OutputStream stream) {
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendClanChannel(ClansManager manager, boolean myClan) {
		OutputStream stream = new OutputStream(manager == null ? 4
				: manager.getClanChannelDataBlock().length + 4);
		stream.writePacketVarShort(player, 99);
		stream.writeByte(myClan ? 1 : 0);
		if (manager != null)
			stream.writeBytes(manager.getClanChannelDataBlock());
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendClanSettings(ClansManager manager, boolean myClan) {
		OutputStream stream = new OutputStream(manager == null ? 4
				: manager.getClanSettingsDataBlock().length + 4);
		stream.writePacketVarShort(player, 190);
		stream.writeByte(myClan ? 1 : 0);
		if (manager != null)
			stream.writeBytes(manager.getClanSettingsDataBlock());
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendPlainIgnore(boolean isUpdate, String displayName,
			String previousDisplayName) {
		OutputStream stream = startIgnoresPacket();
		appendIgnore(stream, isUpdate, displayName, previousDisplayName);
		endIgnoresPacket(stream);
	}

	public OutputStream startIgnoresPacket() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 78);
		return stream;
	}

	public void appendIgnore(OutputStream stream, boolean isUpdate,
			String displayName, String previousDisplayName) {
		// TODO find out real meaning of values
		stream.writeByte(isUpdate ? 0x1 : 0x0);
		stream.writeString(displayName);
		stream.writeString(previousDisplayName == null ? ""
				: previousDisplayName);
		stream.writeString(""); // TODO note here
	}

	public void endIgnoresPacket(OutputStream stream) {
		stream.endPacketVarShort();
		session.write(stream);
	}

	public OutputStream startFriendsPacket() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 113);
		return stream;
	}

	public void appendFriend(OutputStream stream, boolean isStatusUpdate,
			String displayName, String previousDisplayName, int world,
			int fcRank, String worldName) {
		stream.writeByte(isStatusUpdate ? 0 : 1);
		stream.writeString(displayName);
		stream.writeString(previousDisplayName == null ? ""
				: previousDisplayName);
		stream.writeShort(world);
		stream.writeByte(fcRank);
		stream.writeByte(0); // also has a new flag here.
		if (world > 0) {
			stream.writeString(worldName);
			stream.writeByte(0);
			stream.writeInt(0); // idk. new
		}
		stream.writeString(""); // TODO note here
	}

	public void endFriendsPacket(OutputStream stream) {
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendPrivateMessage(String target, String message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 83);
		stream.writeString(target);
		Huffman.encodeString(stream, message);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendPrivateMessage(String target, int qcFileId, byte[] qcData) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 28);
		stream.writeString(target);
		stream.writeShort(qcFileId);
		if (qcData != null)
			stream.writeBytes(qcData);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void receivePrivateMessage(String target, long messageUid,
			int iconId, String message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 126);
		stream.writeByte(0); // no secondary name (this sync's with second and
		// 4th string in ignore list)
		stream.writeString(target);
		for (int bitpos = 0; bitpos < 40; bitpos += 8)
			stream.writeByte((int) (messageUid >> bitpos));
		stream.writeByte(iconId > 2 ? 0 : iconId);
		Huffman.encodeString(stream, message);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void receivePrivateMessage(String target, long messageUid,
			int iconId, int qcFileId, byte[] qcData) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 88);
		stream.writeByte(0); // no secondary name (this sync's with second and
		// 4th string in ignore list)
		stream.writeString(target);
		for (int bitpos = 0; bitpos < 40; bitpos += 8)
			stream.writeByte((int) (messageUid >> bitpos));
		stream.writeByte(iconId > 2 ? 0 : iconId);
		stream.writeShort(qcFileId);
		if (qcData != null)
			stream.writeBytes(qcData);
		stream.endPacketVarByte();
		// //session.write(stream);
	}

	public void sendGameBarStages() {// TODO Change this when we do all tabs.
		boolean isGameOn = player.getGameStatus() != 2;
		player.getVarsManager().sendVarBit(18805, player.getGameStatus());
		player.getVarsManager().sendVarBit(18797, isGameOn ? 1 : 0);

		boolean isTradeOn = player.getTradeStatus() != 2;
		player.getVarsManager().sendVarBit(18806, player.getTradeStatus());
		player.getVarsManager().sendVarBit(18798, isTradeOn ? 1 : 0);

		boolean isAssistOn = player.getAssistStatus() != 2;
		player.getVarsManager().sendVarBit(18807, player.getAssistStatus());
		player.getVarsManager().sendVarBit(18799, isAssistOn ? 1 : 0);

		boolean isPublicOn = player.getPublicStatus() != 2;
		player.getVarsManager().sendVarBit(18808, player.getPublicStatus());
		player.getVarsManager().sendVarBit(18800, isPublicOn ? 1 : 0);

		boolean isPersonalOn = player.getPersonalStatus() != 2;
		player.getVarsManager().sendVarBit(18809, player.getPersonalStatus());
		player.getVarsManager().sendVarBit(18801, isPersonalOn ? 1 : 0);

		boolean isFriendsOn = player.getFriendsChatStatus() != 2;
		player.getVarsManager()
				.sendVarBit(18810, player.getFriendsChatStatus());
		player.getVarsManager().sendVarBit(18802, isFriendsOn ? 1 : 0);

		boolean isClanOn = player.getClanStatus() != 2;
		player.getVarsManager().sendVarBit(18811, player.getClanStatus());
		player.getVarsManager().sendVarBit(18803, isClanOn ? 1 : 0);
		player.getVarsManager().sendVarBit(18804, isClanOn ? 1 : 0);

		// TODO announcement 20828
		// TODO CHATBADGE 21020 on ? 1 : 0
		sendOtherGameBarStages();
	}

	public void sendOtherGameBarStages() {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 21);
		stream.writeByteC(player.getTradeStatus());
		stream.writeByteC(player.getPublicStatus());
		session.write(stream);
	}

	public void sendPmStatus() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 74);
		stream.writeByte(player.getFriendsIgnores().getPmStatus());
		session.write(stream);
	}

	public void receiveClanChatMessage(boolean myClan, String display,
			int rights, ChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 90);
		stream.writeByte(myClan ? 1 : 0);
		stream.writeString(display);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.random(256));
		stream.writeByte(rights);
		Huffman.encodeString(stream,
				message.getMessage(player.isFilteringProfanity()));
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void receiveClanChatQuickMessage(boolean myClan, String display,
			int rights, QuickChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 44);
		stream.writeByte(myClan ? 1 : 0);
		stream.writeString(display);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.random(256));
		stream.writeByte(rights);
		stream.writeShort(message.getDefinition().id);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void receiveFriendChatMessage(String name, String chatName,
			long messageUid, int iconId, String message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 69);
		stream.writeByte(0); // no secondary name (this sync's with second and
		// 4th string in ignore list)
		stream.writeString(name);
		stream.writeString(chatName);
		for (int bitpos = 0; bitpos < 40; bitpos += 8)
			stream.writeByte((int) (messageUid >> bitpos));
		stream.writeByte(iconId > 2 ? 0 : iconId);
		Huffman.encodeString(stream, message);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void receiveFriendChatMessage(String name, String chatName,
			long messageUid, int iconId, int qcFileId, byte[] qcData) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 17);
		stream.writeByte(0); // no secondary name (this sync's with second and
		// 4th string in ignore list)
		stream.writeString(name);
		stream.writeString(chatName);
		for (int bitpos = 0; bitpos < 40; bitpos += 8)
			stream.writeByte((int) (messageUid >> bitpos));
		stream.writeByte(iconId > 2 ? 0 : iconId);
		stream.writeShort(qcFileId);
		if (qcData != null)
			stream.writeBytes(qcData);
		stream.endPacketVarByte();
		session.write(stream);
	}

	/*
	 * dynamic map region
	 */
	public void sendDynamicGameScene(boolean sendLswp) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 173);
		if (sendLswp) // exists on newer protocol, sends all player encoded
			// region ids, afterwards new pupdate protocol is
			// regionbased
			player.getLocalPlayerUpdate().init(stream);
		int middleChunkX = player.getChunkX();
		int middleChunkY = player.getChunkY();

		stream.writeByte(player.isForceNextMapLoadRefresh() ? 1 : 0);
		stream.writeShortLE128(middleChunkY);
		stream.writeByte128(player.getMapSize());
		stream.writeByte(5);
		stream.writeShort128(middleChunkX);
		stream.writeByteC(2); // exists on newer protocol, triggers gamescene

		stream.initBitAccess();
		/*
		 * cene length in chunks. scene tiles length / 16, 8 is a chunk size, 16
		 * because the code behind its signed and goes from middle-length to
		 * middle+length
		 */
		int sceneLength = Settings.MAP_SIZES[player.getMapSize()] >> 4;
		// the regionids(maps files) that will be used to load this scene
		int[] regionIds = new int[4 * sceneLength * sceneLength];
		int newRegionIdsCount = 0;
		for (int plane = 0; plane < 4; plane++) {
			for (int realChunkX = (middleChunkX - sceneLength); realChunkX <= ((middleChunkX + sceneLength)); realChunkX++) {
				int regionX = realChunkX / 8;
				y: for (int realChunkY = (middleChunkY - sceneLength); realChunkY <= ((middleChunkY + sceneLength)); realChunkY++) {
					int regionY = realChunkY / 8;
					// rcx / 8 = rx, rcy / 8 = ry, regionid is encoded region x
					// and y
					int regionId = (regionX << 8) + regionY;
					Region region = World.getRegions().get(regionId);
					int newChunkX;
					int newChunkY;
					int newPlane;
					int rotation;
					if (region instanceof DynamicRegion) { // generated map
						DynamicRegion dynamicRegion = (DynamicRegion) region;
						int[] pallete = dynamicRegion.getRegionCoords()[plane][realChunkX
								- (regionX * 8)][realChunkY - (regionY * 8)];
						newChunkX = pallete[0];
						newChunkY = pallete[1];
						newPlane = pallete[2];
						rotation = pallete[3];
					} else { // real map
						newChunkX = realChunkX;
						newChunkY = realChunkY;
						newPlane = plane;
						rotation = 0;// no rotation
					}
					// invalid chunk, not built chunk
					if (newChunkX == 0 || newChunkY == 0)
						stream.writeBits(1, 0);
					else {
						stream.writeBits(1, 1);
						// chunk encoding = (x << 14) | (y << 3) | (plane <<
						// 24), theres addition of two more bits for rotation
						stream.writeBits(26, (rotation << 1) | (newPlane << 24)
								| (newChunkX << 14) | (newChunkY << 3));
						int newRegionId = (((newChunkX / 8) << 8) + (newChunkY / 8));
						for (int index = 0; index < newRegionIdsCount; index++)
							if (regionIds[index] == newRegionId)
								continue y;
						regionIds[newRegionIdsCount++] = newRegionId;
					}

				}
			}

		}
		stream.finishBitAccess();
		stream.endPacketVarShort();
		session.write(stream);
	}

	/*
	 * normal map region
	 */
	public void sendGameScene(boolean sendLswp) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 66);

		if (sendLswp)
			player.getLocalPlayerUpdate().init(stream);

		stream.writeShort(player.getChunkX());
		stream.writeByte(player.getMapSize());
		stream.writeByte(player.getMapRegionsIds().size());
		stream.write128Byte(player.isForceNextMapLoadRefresh() ? 1 : 0);
		stream.writeByteC(5);
		stream.writeShort128(player.getChunkY());
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendCutscene(int id) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 63);
		stream.writeShort(id);
		stream.writeShort(20);
		byte[] appearence = player.getAppearence().getAppeareanceData();
		stream.writeByte(appearence.length);
		stream.writeBytes(appearence);
		stream.endPacketVarShort();
		session.write(stream);
	}

	/*
	 * sets the pane interface
	 */
	public void sendRootInterface(int id, int type) {
		int[] xteas = new int[4];
		player.getInterfaceManager().setWindowsPane(id);
		OutputStream stream = new OutputStream(20);
		stream.writePacket(player, 73);
		stream.writeByteC(type);// No longer used.
		stream.writeIntV2(xteas[3]);
		stream.writeIntV2(xteas[0]);
		stream.writeShort(id);
		stream.writeIntLE(xteas[2]);
		stream.writeIntV1(xteas[1]);
		session.write(stream);
	}

	public void sendPlayerOption(String option, int slot, boolean top) {
		sendPlayerOption(option, slot, top, -1);
	}

	public void sendPublicMessage(Player p, PublicChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 35);
		stream.writeShort(p.getIndex());
		stream.writeShort(message.getEffects());
		stream.writeByte(p.getMessageIcon() > 2 ? 0 : p.getMessageIcon());
		if (message instanceof QuickChatMessage) {
			QuickChatMessage qcMessage = (QuickChatMessage) message;
			stream.writeShort(qcMessage.getDefinition().id);
			stream.writeBytes(qcMessage.getEncoded());
		} else {
			Huffman.encodeString(stream,
					message.getMessage(player.isFilteringProfanity()));
		}
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendPlayerOption(String option, int slot, boolean top,
			int cursor) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 84);
		stream.writeString(option);
		stream.write128Byte(slot);
		stream.writeByte128(top ? 1 : 0);
		stream.writeShort(cursor);
		stream.endPacketVarByte();
		session.write(stream);
	}

	/*
	 * sends local players update
	 */
	public void sendLocalPlayersUpdate() {
		session.write(player.getLocalPlayerUpdate().createPacketAndProcess());
	}

	/*
	 * sends local npcs update
	 */
	public void sendLocalNPCsUpdate() {
		session.write(player.getLocalNPCUpdate().createPacketAndProcess());
	}

	public void sendGraphics(Graphics graphics, Object target) {
		OutputStream stream = new OutputStream(13);
		int hash = 0;
		if (target instanceof Player) {
			Player p = (Player) target;
			hash = p.getIndex() & 0xffff | 1 << 28;
		} else if (target instanceof NPC) {
			NPC n = (NPC) target;
			hash = n.getIndex() & 0xffff | 1 << 29;
		} else {
			WorldTile tile = (WorldTile) target;
			hash = tile.getPlane() << 28 | tile.getX() << 14 | tile.getY()
					& 0x3fff | 1 << 30;
		}
		stream.writePacket(player, 82);
		stream.writeByte128(graphics.getSettings2Hash());
		stream.writeByteC(0); // slot id used for entitys
		stream.writeShort(graphics.getId());
		stream.writeShort128(graphics.getHeight());
		stream.writeShortLE128(graphics.getSpeed());
		stream.writeIntV2(hash);
		session.write(stream);
	}

	public void sendEntityInterface(Entity entity, boolean clickThrought,
			int windowId, int windowComponentId, int interfaceId) {
		if (entity instanceof NPC)
			sendNPCInterface((NPC) entity, clickThrought, windowId,
					windowComponentId, interfaceId);
		else
			sendPlayerInterface((Player) entity, clickThrought, windowId,
					windowComponentId, interfaceId);
	}

	public void sendPlayerInterface(Player p, boolean clickThrough,
			int windowId, int windowComponentId, int interfaceId) {
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(26);
		stream.writePacket(player, 13);
		stream.writeByte128(clickThrough ? 1 : 0);
		stream.writeShortLE128(p.getIndex());
		stream.writeIntLE(windowId << 16 | windowComponentId);
		stream.writeInt(xteas[0]);
		stream.writeShort(interfaceId);
		stream.writeInt(xteas[2]);
		stream.writeIntV1(xteas[1]);
		stream.writeIntV2(xteas[3]);
		session.write(stream);
	}

	public void sendNPCInterface(NPC npc, boolean clickThrough, int windowId,
			int windowComponentId, int interfaceId) {
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(26);
		stream.writePacket(player, 116);
		stream.writeShortLE128(npc.getIndex());
		stream.writeShort128(interfaceId);
		stream.writeIntV1(xteas[3]);
		stream.writeIntV1(xteas[2]);
		stream.writeIntV1(xteas[1]);
		stream.writeIntV1(windowId << 16 | windowComponentId);
		stream.writeByteC(clickThrough ? 1 : 0);
		stream.writeIntV2(xteas[0]);
		session.write(stream);
	}

	public void sendObjectInterface(WorldObject object, boolean clickThrought,
			int windowId, int windowComponentId, int interfaceId) {
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(33);
		stream.writePacket(player, 36);
		stream.write128Byte(clickThrought ? 1 : 0);
		stream.writeIntV2(xteas[0]);
		stream.writeByte128((object.getType() << 2)
				| (object.getRotation() & 0x3));
		stream.writeIntV2((windowId << 16) | windowComponentId);
		stream.writeIntV2(xteas[2]);
		stream.writeIntLE(object.getTileHash());
		stream.writeShortLE(interfaceId);
		stream.writeInt(xteas[3]);
		stream.writeIntLE(xteas[1]);
		stream.writeIntLE(object.getId());
		session.write(stream);
	}

	public void sendInterface(boolean clickThrough, int parentUID,
			int interfaceId) {
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(24);
		stream.writePacket(player, 8);

		stream.writeByte128(clickThrough ? 1 : 0);
		stream.writeShort128(interfaceId);
		stream.writeIntV2(xteas[0]);
		stream.writeIntV2(xteas[3]);
		stream.writeIntLE(xteas[2]);
		stream.writeIntLE(parentUID);
		stream.writeIntLE(xteas[1]);

		session.write(stream);
	}

	public void closeInterface(int parentUID) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 48);
		stream.writeIntV2(parentUID);
		session.write(stream);
	}

	public void moveInterface(int fromParentUID, int toParentUID) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 125);
		stream.writeInt(fromParentUID);
		stream.writeIntV2(toParentUID);
		session.write(stream);
	}

	public void sendSystemUpdate(int delay, boolean isLobby) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 60);
		if (isLobby)
			stream.writeShort(delay * 20);
		else
			stream.writeShort((int) (delay * 1.6)); // x2 correct.
		session.write(stream);
	}

	public void sendCurrentTarget(Entity target) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 187);
		stream.writeShortLE128(target == null ? 0
				: (target instanceof Player ? -(target.getIndex() + 1) : target
						.getIndex() + 1));
		session.write(stream);
	}

	public void sendUpdateItems(int key, ItemsContainer<Item> items,
			int... slots) {
		sendUpdateItems(key, items.getItems(), slots);
	}

	public void sendUpdateItems(int key, Item[] items, int... slots) {
		sendUpdateItems(key, key < 0, items, slots);
	}

	public void sendUpdateItems(int key, boolean negativeKey, Item[] items,
			int... slots) {// 138
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 14);
		stream.writeShort(key);
		stream.writeByte(negativeKey ? 1 : 0);
		for (int slotId : slots) {
			if (slotId >= items.length)
				continue;
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			Item item = items[slotId];
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			if (id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if (amount >= 255)
					stream.writeInt(amount);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendCSVarString(int id, String string) {
		OutputStream stream = new OutputStream();
		if (string.length() < 253) {
			stream.writePacketVarByte(player, 56);
			stream.writeShort(id);
			stream.writeString(string);
			stream.endPacketVarByte();
		} else {
			stream.writePacketVarShort(player, 57);
			stream.writeString(string);
			stream.writeShortLE(id);
			stream.endPacketVarShort();
		}
		session.write(stream);
	}

	public void sendUpdateItemsContainer(int key, ItemsContainerNew container) {// 138
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 14);
		stream.writeShort(key);
		stream.writeByte(key < 0 ? 1 : 0);
		for (int i = 0; i < container.getSize(); i++) {
			if (!container.needsUpdating(i))
				continue;
			stream.writeSmart(i);
			int id = -1;
			int amount = 0;
			Item item = container.get(i);
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			if (id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if (amount >= 255)
					stream.writeInt(amount);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendItems(int key, ItemsContainer<Item> items) {
		sendItems(key, key < 0, items);
	}

	public void sendItems(int key, boolean negativeKey,
			ItemsContainer<Item> items) {
		sendItems(key, negativeKey, items.getItems());
	}

	public void sendItems(int key, Item[] items) {
		sendItems(key, key < 0, items);
	}

	public void sendItemsContainer(int key, ItemsContainerNew container) {// 77
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 140);
		stream.writeShort(key);

		int flag = 0;
		if (key < 0)
			flag |= 0x1;

		stream.writeByte(flag);

		stream.writeByte(flag);
		stream.writeShort(container.getSize());
		for (int i = 0; i < container.getSize(); i++) {
			int id = -1;
			int amount = 0;
			Item item = container.get(i);
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			stream.writeByte(amount >= 255 ? 255 : amount);
			if (amount >= 255)
				stream.writeInt(amount);

		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendItems(int key, boolean negativeKey, Item[] items) {// 77
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 140);
		stream.writeShort(key); // negativeKey ? -key : key

		int flag = 0;
		if (negativeKey)
			flag |= 0x1;

		stream.writeByte(flag);
		stream.writeShort(items.length);
		for (int index = 0; index < items.length; index++) {
			Item item = items[index];
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			stream.writeByte(amount >= 255 ? 255 : amount);
			if (amount >= 255)
				stream.writeInt(amount);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public ChannelFuture sendLogout(boolean lobby) {
		OutputStream stream = new OutputStream();
		stream.writePacket(player, lobby ? 181 : 150);
		if (lobby)
			player.getInterfaceManager().sendLobbyInterfaces();
		return session.write(stream);
	}

	public void sendInventoryMessage(int border, int slotId, String message) {
		sendInterfaceMessage(Inventory.INVENTORY_INTERFACE, 8, border, slotId,
				message);
	}

	public void sendInterfaceMessage(int interfaceId, int componentId,
			int border, int slotId, String message) {
		sendGameMessage(message);
		sendExecuteScript(7774, message, interfaceId << 16 | componentId,
				slotId, border);
	}

	public void sendEntityMessage(int border, int color, Entity entity,
			String message) {
		sendGameMessage(message);
		sendCSVarString(2251, message);
		sendCSVarInteger(1699, color);
		sendCSVarInteger(1700, border);
		sendCSVarInteger(1695, 1);
		sendEntityInterface(entity, true, InterfaceManager.RESIZABLE_WINDOW_ID,
				17, 1177);
	}

	public void sendObjectMessage(int border, int color, WorldObject object,
			String message, boolean aBool) {
		// cfg2243
		sendGameMessage(message);
		sendCSVarString(2251, message);
		sendCSVarInteger(1699, color);
		sendCSVarInteger(1700, border);
		sendCSVarInteger(1695, 1);
		sendObjectInterface(object, true, InterfaceManager.RESIZABLE_WINDOW_ID,
				17, 1177);
	}

	public void sendFloorItemMessage(int border, int color, FloorItem tile,
			String message) {
		sendGameMessage(message);
		sendCSVarString(2251, message);
		sendCSVarInteger(1699, color);
		sendCSVarInteger(1700, border);
		sendCSVarInteger(1695, 1);
		sendFloorItemInterface(tile, true,
				InterfaceManager.RESIZABLE_WINDOW_ID, 17, 1177);
	}

	private void sendFloorItemInterface(FloorItem tile, boolean clickThrough,
			int rootInterface, int rootComponent, int subInterface) {
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(30);
		stream.writePacket(player, 172);
		stream.writeShort(tile.getId());
		stream.writeInt(tile.getTile().getTileHash());
		stream.writeInt(xteas[2]);
		stream.writeIntLE(xteas[3]);
		stream.writeShort(subInterface);
		stream.writeIntV1(xteas[0]);
		stream.writeByte128(clickThrough ? 1 : 0);
		stream.writeIntLE((rootInterface << 16) | rootComponent);
		stream.writeIntV1(xteas[1]);
		session.write(stream);
	}

	public void sendForceGameMessage(String text) {
		sendMessage(11, text, null);
	}

	public void sendForceGameMessage(Color color, String text) {
		sendMessage(11, color.getHex() + text, null);
	}

	public void sendGameMessage(Color color, String text) {
		sendGameMessage(color.getHex() + text, false);
	}

	public void sendGameMessage(String text) {
		sendGameMessage(text, false);
	}

	public void sendGameMessage(String text, boolean filter) {
		sendMessage(filter ? 109 : 0, text, null);
	}

	public void sendGameMessage(Color color, String text, boolean filter) {
		sendGameMessage(color.getHex() + text, filter);
	}

	public void sendGoblinRaidMessage(String text) {
		sendMessage(121, text, null);
	}

	public void sendDemonRaidMessage(String text) {
		sendMessage(122, text, null);
	}

	public void sendSinkholeMessage(String text) {
		sendMessage(123, text, null);
	}

	public void sendWarbandsMessage(String text) {
		sendMessage(124, text, null);
	}

	public void sendWorldEventMessage(String text) {
		sendMessage(126, text, null);
	}

	public void sendPanelBoxMessage(String text) {
		sendMessage(99, text, null); // panel
		sendMessage(0, text, null); // normal
	}

	public void sendTradeRequestMessage(Player p) {
		sendMessage(100, "wishes to trade with you.", p);
	}

	public void sendClanWarsRequestMessage(Player p) {
		sendMessage(101, "wishes to challenge your clan to a clan war.", p);
	}

	public void sendClanInviteMessage(Player p) {
		sendMessage(117, p.getDisplayName()
				+ " is inviting you to join their clan.", p);
	}

	public void sendDuelChallengeRequestMessage(Player p, boolean friendly) {
		sendMessage(101, "wishes to duel with you("
				+ (friendly ? "friendly" : "stake") + ").", p);
	}

	public void sendDungeonneringRequestMessage(Player p) {
		sendMessage(111, "has invited you to a dungeon party.", p);
	}

	public void sendAssistRequestMessage(Player p) {
		sendMessage(100, "is requesting assistance from you.", p);
	}

	public void sendMessage(int type, String text, Player p) {
		int maskData = 0;
		if (p != null) {
			maskData |= 0x1;
			maskData |= 0x2;
		}
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 43);
		stream.writeSmart(type);
		stream.writeInt(player.getTileHash()); // junk, not used by client
		stream.writeByte(maskData);
		if ((maskData & 0x1) != 0) {
			stream.writeString(Utils.formatPlayerNameForDisplay(p.getUsername()));
			stream.writeString(p.getDisplayName());
		}
		stream.writeString(text);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendSoundEffect(int id) {
		sendSoundEffect(id, 0, 255);
	}

	public void sendSoundEffect(int id, int delay, int volume) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 76);
		stream.writeShort(id);
		stream.writeByte(1); // amt of times it repeats
		stream.writeShort(delay);
		stream.writeByte(volume); // volume*/
		session.write(stream);
	}

	public void sendVoice(int id) {
		resetSoundEffects();
		sendSoundEffect(id, 0, 255);
	}

	public void resetSoundEffects() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 100);
		session.write(stream);
	}

	// TODO update and remove
	public void sendMusicEffectOld(int id) {
	}

	public void sendMusicEffect(int archiveId, int volume) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 9);
		stream.writeShortLE(archiveId);
		session.write(stream);
	}

	public void sendMusicEffect(int archiveId) {
		sendMusicEffect(archiveId, 0);
	}

	public void sendMusic(int archiveId, int volume) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 70);
		stream.writeByte128(volume);
		stream.writeShort128(archiveId);
		session.write(stream);
	}

	public void sendSkillLevel(int skill) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 5);
		stream.writeByte(player.getSkills().getLevel(skill));
		stream.writeIntLE((int) player.getSkills().getXp(skill));
		stream.write128Byte(skill);
		session.write(stream);
	}

	/**
	 * This will blackout specified area.
	 * 
	 * @param byte area = area which will be blackout (0 = unblackout; 1 =
	 *        blackout orb; 2 = blackout map; 5 = blackout orb and map)
	 */
	public void sendBlackOut(int area) {
		OutputStream out = new OutputStream(1);
		out.writePacket(player, 165);
		out.writeByte(area);
		session.write(out);
	}

	// instant
	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ) {
		sendCameraLook(viewLocalX, viewLocalY, viewZ, -1, -1);
	}

	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ,
			int speed1, int speed2) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 177);
		stream.write128Byte(viewLocalY);
		stream.writeShort(viewZ >> 2);
		stream.write128Byte(viewLocalX);
		stream.writeByteC(speed2);
		stream.writeByteC(speed1);
		session.write(stream);
	}

	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ,
			int speed1, int speed2) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 50);
		stream.write128Byte(moveLocalX);
		stream.writeByte(speed2);
		stream.writeByteC(speed1);
		stream.writeShort128(moveZ >> 2);
		stream.writeByteC(moveLocalY);
		session.write(stream);
	}

	public void sendResetCamera() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 40);
		session.write(stream);
	}

	public void sendCameraRotation(int localX, int localY) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 102);
		stream.writeByte(localY);
		stream.writeShort128(localX);
		session.write(stream);
	}

	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ) {
		sendCameraPos(moveLocalX, moveLocalY, moveZ, -1, -1);
	}

	public void sendClientConsoleCommand(String command) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 89);
		stream.writeString(command);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendOpenURL(String url) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 130);
		stream.writeByte(0);
		stream.writeString(url);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendSetMouse(String walkHereReplace, int cursor) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 93);
		stream.writeString(walkHereReplace);
		stream.writeShort(cursor);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendItemsLook() {
		// currently disabled
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 159);
		stream.writeByte(player.isOldItemsLook() ? 1 : 0);
		// session.write(stream);
	}

	public void sendLogReq(String file) {
		// currently disabled
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 132);
		stream.writeString(file);
		stream.endPacketVarShort();
		// session.write(stream);
	}

	public void sendConfigByFile(int id, int value) {
		player.getVarsManager().sendVarBit(id, value);
	}

}
