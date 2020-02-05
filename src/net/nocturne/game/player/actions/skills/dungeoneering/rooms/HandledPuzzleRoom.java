package net.nocturne.game.player.actions.skills.dungeoneering.rooms;

import net.nocturne.game.player.actions.skills.dungeoneering.DungeonConstants;
import net.nocturne.game.player.actions.skills.dungeoneering.VisibleRoom;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonConstants.Puzzle;

public class HandledPuzzleRoom extends HandledRoom {

	private Puzzle puzzle;

	public HandledPuzzleRoom(int i, Puzzle puzzle) {
		this(i, puzzle, null);
	}

	public HandledPuzzleRoom(int i, Puzzle puzzle, RoomEvent event) {
		super(puzzle.getChunkX(), 528 + (i * 2), event, puzzle.getKeySpot(i),
				DungeonConstants.PUZZLE_DOOR_ORDER[i]);
		this.puzzle = puzzle;
	}

	// @Override
	// public final boolean allowSpecialDoors() {
	// return false;
	// }

	@Override
	public boolean isComplexity(int complexity) {
		return complexity == 6;
	}

	public VisibleRoom createVisibleRoom() {
		return puzzle.newInstance();
	}

	@Override
	public boolean isAvailableOnFloorType(int floorType) {
		return puzzle.isAvailableOnFloorType(floorType);
	}

	@Override
	public boolean allowResources() {
		return puzzle.allowResources();
	}

}
