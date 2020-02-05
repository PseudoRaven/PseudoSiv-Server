package net.nocturne.game.player.content.activities.jobs.impl.bananaplantation;

import java.io.Serializable;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.jobs.Job;
import net.nocturne.game.player.content.activities.jobs.JobReward;
import net.nocturne.game.player.content.activities.jobs.RewardType;

/**
 * @Author arrow
 * @Contact<arrowrsps@gmail.com;skype:arrowrsps>
 */
public class BananaPlantation extends Job implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8653661953154110854L;
	protected Player player;

	public BananaPlantation(Player player) {
		super(player);
	}

	@Override
	public JobReward[] getRewards() {
		return new JobReward[] { new JobReward(RewardType.ITEM, new Item(995,
				30)) };
	}
}
