package net.nocturne.game.player.actions.skills.agility;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class LabsAgility {

	public static void jumpOne(final Player player, final WorldObject object) {
		// first obsticle lvl 80
		final WorldTile NextTile1 = new WorldTile(1135, 611, 1);
		final WorldTile NextTile2 = new WorldTile(1131, 611, 1);
		// second obsticle lvl 70
		final WorldTile NextTile5 = new WorldTile(1096, 628, 1);
		final WorldTile NextTile6 = new WorldTile(1096, 626, 1);
		// third obsticle lvl 60
		final WorldTile NextTile3 = new WorldTile(1067, 652, 1);
		final WorldTile NextTile4 = new WorldTile(1065, 652, 1);
		// fourth obsticle lvl 90
		final WorldTile NextTile7 = new WorldTile(1150, 689, 1);
		final WorldTile NextTile8 = new WorldTile(1152, 689, 1);
		// fith obsticle lvl 90
		final WorldTile NextTile9 = new WorldTile(1178, 654, 1);
		final WorldTile NextTile10 = new WorldTile(1178, 652, 1);
		// start of 1st jump
		if (object.getX() == 1131 && object.getY() == 611) {
			if (!Agility.hasLevel(player, 80)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 80 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile1);
						player.addWalkSteps(1131, 611);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(15461));
					}
					if (time == 10) {
						player.setNextWorldTile(NextTile1);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						player.addWalkSteps(1136, 611);
						stop();
					}
				}
			}, 0, 0);

		} else if (object.getX() == 1136 && object.getY() == 611) {
			if (!Agility.hasLevel(player, 80)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 80 agility to use this.");
				return;
			} else {
				WorldTasksManager.schedule(new WorldTask() {
					int time;

					@Override
					public void run() {
						time++;
						if (time == 1) {
							player.setNextFaceWorldTile(NextTile2);
							player.addWalkSteps(1135, 611);
						}
						if (time == 3) {
							player.setNextAnimation(new Animation(15461));
						}
						if (time == 10) {
							player.setNextWorldTile(NextTile2);
							player.setNextAnimation(new Animation(-1));

						}
						if (time == 11) {
							player.addWalkSteps(1131, 611);
							stop();

						}
					}
				}, 0, 0);
			}
			// end of 1st jump
			// start of 2nd jump
		} else if (object.getX() == 1096 && object.getY() == 626) {
			if (!Agility.hasLevel(player, 70)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 70 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile5);
						player.addWalkSteps(1096, 626);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(13495));
					}
					if (time == 6) {
						player.setNextWorldTile(NextTile5);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						stop();
					}
				}
			}, 0, 0);

		} else if (object.getX() == 1096 && object.getY() == 628) {
			if (!Agility.hasLevel(player, 70)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 70 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile6);
						player.addWalkSteps(1096, 628);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(13495));
					}
					if (time == 6) {
						player.setNextWorldTile(NextTile6);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						stop();
					}
				}
			}, 0, 0);
			// end of 2nd jump
			// start of 3rd jump
		} else if (object.getX() == 1065 && object.getY() == 652) {
			if (!Agility.hasLevel(player, 60)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 60 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile3);
						player.addWalkSteps(1067, 652);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(13495));
					}
					if (time == 6) {
						player.setNextWorldTile(NextTile3);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						stop();
					}
				}
			}, 0, 0);

		} else if (object.getX() == 1067 && object.getY() == 652) {
			if (!Agility.hasLevel(player, 60)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 60 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile4);
						player.addWalkSteps(1065, 652);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(13495));
					}
					if (time == 6) {
						player.setNextWorldTile(NextTile4);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						stop();
					}
				}
			}, 0, 0);
			// end of 3rd jump
			// start of 4th jump
		} else if (object.getX() == 1150 && object.getY() == 689) {
			if (!Agility.hasLevel(player, 90)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 90 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile8);
						player.addWalkSteps(1150, 689);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(13495));
					}
					if (time == 6) {
						player.setNextWorldTile(NextTile8);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						stop();
					}
				}
			}, 0, 0);

		} else if (object.getX() == 1152 && object.getY() == 689) {
			if (!Agility.hasLevel(player, 90)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 90 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile7);
						player.addWalkSteps(1152, 689);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(13495));
					}
					if (time == 6) {
						player.setNextWorldTile(NextTile7);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						stop();
					}
				}
			}, 0, 0);
			// end of 4th jump
			// start of 5th jump
		} else if (object.getX() == 1178 && object.getY() == 654) {
			if (!Agility.hasLevel(player, 90)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 90 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile10);
						player.addWalkSteps(1178, 654);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(13495));
					}
					if (time == 6) {
						player.setNextWorldTile(NextTile10);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						stop();
					}
				}
			}, 0, 0);

		} else if (object.getX() == 1178 && object.getY() == 652) {
			if (!Agility.hasLevel(player, 90)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need 90 agility to use this.");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {
				int time;

				@Override
				public void run() {
					time++;
					if (time == 1) {
						player.setNextFaceWorldTile(NextTile9);
						player.addWalkSteps(1178, 652);
					}
					if (time == 3) {
						player.setNextAnimation(new Animation(13495));
					}
					if (time == 6) {
						player.setNextWorldTile(NextTile9);
						player.setNextAnimation(new Animation(-1));

					}
					if (time == 11) {
						stop();
					}
				}
			}, 0, 0);
		}
		// end of 5th jump
	}
}
