package net.nocturne.game.player.dialogues.impl.vorago;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.nocturne.game.Animation;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.vorago.VoragoHandler;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class VoragoInstantChallenge extends Dialogue{

	@Override
	public void start() {
		sendOptionsDialogue("Challenge vorago? (brace for damage!)", "We fight", "Not right now");
		stage = 1;
		
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch(stage){
		case 1:
			switch(componentId){
			case OPTION_1:
				if (VoragoHandler.getPlayersCount() == 0) {
					World.ChallengerName = player.getDisplayName();
					end();
					final List<Player> players = Collections.synchronizedList(new ArrayList<Player>());
					for (Player p : World.getPlayers()) {
						if (p.getX() >= 3029 && p.getX() <= 3055 && p.getY() >= 6117 && p.getY() <= 6136) {
							players.add(p);
							if (p != player) {
								p.accChallenge = false;
								p.getDialogueManager().startDialogue("VoragoAccChallenge", true);
							} else {
								p.accChallenge = true;
							}
						}
					}
					if (players.size() >= 1) {
						player.getPackets().sendEntityMessage(1, 15263739, player,
								"<col=ee7600>Vorago accepts your challenge and begins to charge a massive attack.</col>");
						WorldTasksManager.schedule(new WorldTask() {
							int count = 0;

							@Override
							public void run() {
								for (Player p : players) {
									if (p.accChallenge) {
										if (count == 1) {
											p.setNextAnimation(new Animation(20338));
											p.setNextGraphics(new Graphics(4034));
											p.applyHit(new Hit(p, (5000 / players.size()), HitLook.REGULAR_DAMAGE));
										    WorldTile toTile = p.transform(0, -8, 0);
											p.setNextForceMovement(
													new ForceMovement(new WorldTile(p), 1, toTile, 2, ForceMovement.SOUTH));
										}
										if (count == 3) {
											p.setNextAnimation(new Animation(-1));
										}
									}
								}
								count++;
							}

						}, 35, 1);
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								for (Player p : players) {
									if (p.accChallenge) {
										VoragoHandler.addPlayer(p);
									}
								}
								VoragoHandler.beginFight();
							}

						}, 40);
					}
				} else {
					sendNPCDialogue(17161, NORMAL, "Sorry there is already a fight in progress.");
				}


				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		}
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
