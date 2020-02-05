package net.nocturne.game.player.actions.skills.dungeoneering.rooms;

import net.nocturne.game.player.actions.skills.dungeoneering.DungeonConstants;

public final class StartRoom extends HandledRoom {

	public StartRoom(int chunkX, int chunkY, int... doorsDirections) {

		super(chunkX, chunkY, (dungeon, reference) -> {
			dungeon.telePartyToRoom(reference);
			dungeon.spawnNPC(reference, DungeonConstants.SMUGGLER, 8, 8); // smoother
				dungeon.setTableItems(reference);
				dungeon.linkPartyToDungeon();

			}, new int[] { 7, 7 }, doorsDirections);

	}

	@Override
	public boolean allowResources() {
		return false;
	}

}
