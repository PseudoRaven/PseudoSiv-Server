package net.nocturne.game.player.content.activities.ports.missions;

/**
 * 
 * @author Frostbite<Abstract>
 * @contact<skype;frostbitersps><email;frostbitersps@gmail.com>
 */

public class MissionConstants {

	public enum MissionType {
		DAILY, REPEATABLE, RARE, COMMON
	}

	public enum RewardType {

		EXPERIENCE, ITEM

	}

	public enum MissionState {

		NOT_STARTED, IN_PROGRESS, COMPLETED
	}

}
