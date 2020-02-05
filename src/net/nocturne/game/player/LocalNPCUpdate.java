package net.nocturne.game.player;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.nocturne.Settings;
import net.nocturne.game.Graphics;
import net.nocturne.game.HeadIcon;
import net.nocturne.game.Hit;
import net.nocturne.game.HitBar;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.stream.OutputStream;
import net.nocturne.utils.Utils;

public final class LocalNPCUpdate {

	private Player player;
	private LinkedList<NPC> localNPCs;

	public void reset() {
		localNPCs.clear();
	}

	public LocalNPCUpdate(Player player) {
		this.player = player;
		localNPCs = new LinkedList<NPC>();
	}

	public OutputStream createPacketAndProcess() {
		OutputStream stream = new OutputStream();
		OutputStream updateBlockData = new OutputStream();
		stream.writePacketVarShort(player, 39);
		processLocalNPCsInform(stream, updateBlockData);
		stream.writeBytes(updateBlockData.getBuffer(), 0,
				updateBlockData.getOffset());
		stream.endPacketVarShort();
		return stream;
	}

	private void processLocalNPCsInform(OutputStream stream,
			OutputStream updateBlockData) {
		stream.initBitAccess();
		processInScreenNPCs(stream, updateBlockData);
		addInScreenNPCs(stream, updateBlockData);
		if (updateBlockData.getOffset() > 0)
			stream.writeBits(15, 32767);
		stream.finishBitAccess();
	}

	private void processInScreenNPCs(OutputStream stream,
			OutputStream updateBlockData) {
		stream.writeBits(8, localNPCs.size());
		int renderDistance = player.getRenderDistance();
		for (Iterator<NPC> it = localNPCs.iterator(); it.hasNext();) {
			NPC n = it.next();
			if (n.hasFinished()
					|| !(n.withinDistance(player, renderDistance) && (!player
							.hasLargeSceneView() || player.getMapRegionsIds()
							.contains(n.getRegionId()))) || n.hasTeleported()) {
				stream.writeBits(1, 1);
				stream.writeBits(2, 3);
				it.remove();
				continue;
			}
			boolean needUpdate = n.needMasksUpdate()
					|| player.getCombatDefinitions()
							.isNeedTargetReticuleUpdate(n);
			boolean walkUpdate = n.getNextWalkDirection() != -1;
			stream.writeBits(1, (needUpdate || walkUpdate) ? 1 : 0);
			if (walkUpdate) {
				stream.writeBits(2, n.getNextRunDirection() == -1 ? 1 : 2);
				if (n.getNextRunDirection() != -1)
					stream.writeBits(1, 1);
				stream.writeBits(3,
						Utils.getNpcMoveDirection(n.getNextWalkDirection()));
				if (n.getNextRunDirection() != -1)
					stream.writeBits(3,
							Utils.getNpcMoveDirection(n.getNextRunDirection()));
				stream.writeBits(1, needUpdate ? 1 : 0);
			} else if (needUpdate)
				stream.writeBits(2, 0);
			if (needUpdate)
				appendUpdateBlock(n, updateBlockData, false);
		}
	}

	private void addInScreenNPCs(OutputStream stream,
			OutputStream updateBlockData) {
		for (int regionId : player.getMapRegionsIds()) {
			List<Integer> indexes = World.getRegion(regionId).getNPCsIndexes();
			if (indexes == null)
				continue;
			int bitNum = 5;
			for (int npcIndex : indexes) {
				if (localNPCs.size() == Settings.LOCAL_NPCS_LIMIT)
					break;
				NPC n = World.getNPCs().get(npcIndex);
				if (n == null
						|| n.hasFinished()
						|| localNPCs.contains(n)
						|| !(n.withinDistance(player,
								player.getRenderDistance()) && (!player
								.hasLargeSceneView() || player
								.getMapRegionsIds().contains(n.getRegionId())))
						|| n.isDead())
					continue;
				stream.writeBits(15, n.getIndex());
				stream.writeBits(3, (n.getDirection() >> 11) - 4);

				boolean needUpdate = true;
				stream.writeBits(1, needUpdate ? 1 : 0);

				int x = n.getX() - player.getX();
				x &= -1 >>> (32 - bitNum);
				stream.writeBits(bitNum, x);

				stream.writeBits(15, n.getId());

				int y = n.getY() - player.getY();
				y &= -1 >>> (32 - bitNum);
				stream.writeBits(bitNum, y);

				stream.writeBits(2, n.getPlane());
				stream.writeBits(1, n.hasTeleported() ? 1 : 0);

				localNPCs.add(n);
				if (needUpdate)
					appendUpdateBlock(n, updateBlockData, true);
			}
		}
	}

	private void appendUpdateBlock(NPC n, OutputStream data, boolean added) {
		int maskData = 0;

		if (n.getNextGraphics1() != null)
			maskData |= 0x4;
		if (n.getNextGraphics3() != null)
			maskData |= 0x2000000;
		if (!n.getNextHits().isEmpty() || !n.getNextHitBars().isEmpty())
			maskData |= 0x80;
		if (player.getCombatDefinitions().isNeedTargetReticuleUpdate(n))
			maskData |= 0x10000000;
		if (n.getNextTransformation() != null)
			maskData |= 0x1;
		if (n.getNextGraphics2() != null)
			maskData |= 0x400;
		if (n.getNextFaceWorldTile() != null && n.getNextRunDirection() == -1
				&& n.getNextWalkDirection() == -1)
			maskData |= 0x40;
		if (n.getNextForceTalk() != null)
			maskData |= 0x8;
		if (n.getCustomCombatLevel() >= 0)
			maskData |= 0x20000;
		if (added || n.isNeedTargetInformationUpdate())
			maskData |= 0x200000;
		if (n.getNextFaceEntity() != -2
				|| (added && n.getLastFaceEntity() != -1))
			maskData |= 0x10;
		if (n.getNextGraphics4() != null)
			maskData |= 0x4000000;
		if (n.getCustomName() != null)
			maskData |= 0x800000;
		if (added || n.isRefreshHeadIcon())
			maskData |= 0x40000;
		if (n.getNextAnimation() != null)
			maskData |= 0x20;
		if (n.getNextForceMovement() != null)
			maskData |= 0x2000;

		if (maskData >= 0xff)
			maskData |= 0x2;
		if (maskData >= 0xffff)
			maskData |= 0x200;
		if (maskData >= 0xffffff)
			maskData |= 0x400000;

		data.writeShort(0); // rs doesnt use this lol
		data.writeByte(maskData);

		if (maskData >= 0xff)
			data.writeByte(maskData >> 8);
		if (maskData >= 0xffff)
			data.writeByte(maskData >> 16);
		if (maskData >= 0xffffff)
			data.writeByte(maskData >> 24);

		if (n.getNextGraphics1() != null)
			applyGraphicsMask1(n, data);
		if (n.getNextGraphics3() != null)
			applyGraphicsMask3(n, data);
		if (!n.getNextHits().isEmpty() || !n.getNextHitBars().isEmpty())
			applyHitsMask(n, data);
		if (player.getCombatDefinitions().isNeedTargetReticuleUpdate(n))
			applyTargetReticuleMask(n, data);
		if (n.getNextTransformation() != null)
			applyTransformationMask(n, data);
		if (n.getNextGraphics2() != null)
			applyGraphicsMask2(n, data);
		if (n.getNextFaceWorldTile() != null && n.getNextRunDirection() == -1
				&& n.getNextWalkDirection() == -1)
			applyFaceWorldTileMask(n, data);
		if (n.getNextForceTalk() != null)
			applyForceTalkMask(n, data);
		if (n.getCustomCombatLevel() >= 0)
			applyChangeLevelMask(n, data);
		if (added || n.isNeedTargetInformationUpdate())
			applyTargetInformationMask(n, data);
		if (n.getNextFaceEntity() != -2
				|| (added && n.getLastFaceEntity() != -1))
			applyFaceEntityMask(n, data);
		if (n.getNextGraphics4() != null)
			applyGraphicsMask4(n, data);
		if (n.getCustomName() != null)
			applyNameChangeMask(n, data);
		if (added || n.isRefreshHeadIcon())
			applyIconMask(n, data);
		if (n.getNextAnimation() != null)
			applyAnimationMask(n, data);
		if (n.getNextForceMovement() != null)
			applyForceMovementMask(n, data);
	}

	private void applyTargetInformationMask(NPC n, OutputStream data) {
		data.writeByte(1);
		data.writeByte(3);
		data.writeIntV2(n.getHitpoints());
		data.write24BitIntegerV2(n.getMaxHitpoints());
	}

	private void applyTargetReticuleMask(NPC n, OutputStream data) {
		Graphics reticle = player.getCombatDefinitions().getTargetReticule(n);
		data.writeShortLE128(reticle.getId());
		data.writeInt(reticle.getSettingsHash());
		data.writeByteC(reticle.getSettings2Hash());
	}

	private void applyChangeLevelMask(NPC n, OutputStream data) {
		data.writeShortLE128(n.getCombatLevel());
	}

	private void applyNameChangeMask(NPC npc, OutputStream data) {
		data.writeString(npc.getName());
	}

	private void applyTransformationMask(NPC n, OutputStream data) {
		data.writeBigSmart(n.getNextTransformation().getToNPCId());
	}

	private void applyForceTalkMask(NPC n, OutputStream data) {
		data.writeString(n.getNextForceTalk().getText());
	}

	private void applyForceMovementMask(NPC n, OutputStream data) {
		data.write128Byte(n.getNextForceMovement().getToFirstTile().getX()
				- n.getX());
		data.write128Byte(n.getNextForceMovement().getToFirstTile().getY()
				- n.getY());
		data.write128Byte(n.getNextForceMovement().getToSecondTile() == null ? 0
				: n.getNextForceMovement().getToSecondTile().getX() - n.getX());
		data.writeByte(n.getNextForceMovement().getToSecondTile() == null ? 0
				: n.getNextForceMovement().getToSecondTile().getY() - n.getY());
		data.writeByteC(n.getNextForceMovement().getToFirstTile().getPlane()
				- n.getPlane());// Start
		// Height
		data.write128Byte(n.getNextForceMovement().getToSecondTile() == null ? 0
				: n.getNextForceMovement().getToSecondTile().getPlane()
						- n.getPlane());
		data.writeShort128(n.getNextForceMovement().getFirstTileTicketDelay() * 30);
		data.writeShort128(n.getNextForceMovement().getToSecondTile() == null ? 0
				: (n.getNextForceMovement().getSecondTileTicketDelay() * 30));
		data.writeShortLE(n.getNextForceMovement().getDirection());
	}

	private void applyFaceWorldTileMask(NPC n, OutputStream data) {
		data.writeShort((n.getNextFaceWorldTile().getX() * 2)
				+ n.getNextFaceWorldTile().getSizeX());
		data.writeShortLE128((n.getNextFaceWorldTile().getY() * 2)
				+ n.getNextFaceWorldTile().getSizeY());
	}

	private void applyHitsMask(NPC n, OutputStream data) {
		data.writeByte128(n.getNextHits().size());
		for (Hit hit : n.getNextHits()) {
			boolean interactingWith = hit.interactingWith(player, n);
			if (hit.missed() && !interactingWith) {
				data.writeSmart(32766);
				data.writeByte128(hit.getDamageDisplay(player)); // dont ask me
				// why, 32766
				// sets dmg but
				// no hitmark.
			} else {
				if (hit.getSoaking() != null) {
					data.writeSmart(32767);
					data.writeSmart(hit.getMark(player, n));
					data.writeSmart(hit.getDamageDisplay(player));
					data.writeSmart(hit.getSoaking().getMark(player, n));
					data.writeSmart(hit.getSoaking().getDamageDisplay(player));
				} else {
					data.writeSmart(hit.getMark(player, n));
					data.writeSmart(hit.getDamageDisplay(player));
				}
			}
			data.writeSmart(hit.getDelay());
		}
		data.writeByte(n.getNextHitBars().size());
		for (HitBar bar : n.getNextHitBars()) {
			data.writeSmart(bar.getType());
			int perc = bar.getPercentage();
			int toPerc = bar.getToPercentage();
			boolean display = bar.display(player);
			data.writeSmart(display ? perc != toPerc ? 1 : 0 : 32767);
			if (display) {
				data.writeSmart(bar.getDelay());
				data.writeByteC(perc);
				if (toPerc != perc)
					data.writeByte128(toPerc);
			}
		}
	}

	private void applyFaceEntityMask(NPC n, OutputStream data) {
		data.writeShort(n.getNextFaceEntity() == -2 ? n.getLastFaceEntity() : n
				.getNextFaceEntity());
	}

	private void applyAnimationMask(NPC n, OutputStream data) {
		for (int id : n.getNextAnimation().getIds())
			data.writeBigSmart(id);
		data.write128Byte(n.getNextAnimation().getDelay());
	}

	private void applyGraphicsMask4(NPC n, OutputStream data) {
		data.writeShort(n.getNextGraphics4().getId());
		data.writeInt(n.getNextGraphics4().getSettingsHash());
		data.writeByteC(n.getNextGraphics4().getSettings2Hash());
	}

	private void applyGraphicsMask3(NPC n, OutputStream data) {
		data.writeShortLE128(n.getNextGraphics3().getId());
		data.writeInt(n.getNextGraphics3().getSettingsHash());
		data.writeByte128(n.getNextGraphics3().getSettings2Hash());
	}

	private void applyGraphicsMask2(NPC n, OutputStream data) {
		data.writeShortLE(n.getNextGraphics2().getId());
		data.writeIntLE(n.getNextGraphics2().getSettingsHash());
		data.write128Byte(n.getNextGraphics2().getSettings2Hash());
	}

	private void applyGraphicsMask1(NPC n, OutputStream data) {
		data.writeShort128(n.getNextGraphics1().getId());
		data.writeInt(n.getNextGraphics1().getSettingsHash());
		data.write128Byte(n.getNextGraphics1().getSettings2Hash());
	}

	private void applyIconMask(NPC n, OutputStream data) {
		HeadIcon[] icons = n.getIcons();
		int mask = 0;
		for (int i = 0; i < icons.length; i++)
			mask |= 1 << i;
		data.writeByte128(mask);
		for (HeadIcon icon : icons) {
			data.writeBigSmart(icon.getSpriteId());
			data.writeSmart(icon.getFileId() + 1);
		}
	}
}
