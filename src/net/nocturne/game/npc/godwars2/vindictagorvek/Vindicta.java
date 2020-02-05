package net.nocturne.game.npc.godwars2.vindictagorvek;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GorvekAndVindictaInstanceController;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

@SuppressWarnings("serial")
public class Vindicta extends NPC {

	private int phase;
	public int vindictaX;
	public int vindictaY;
	public int vindictaX2;
	public int vindictaY2;
	public int vindictaX3;
	public int vindictaY3;
	public int vindictaX4;
	public int vindictaY4;
	public int vindictaX5;
	public int vindictaY5;
	public int vindictaX6;
	public int vindictaY6;
	public int vindictaP2X;
	public int vindictaP2Y;
	public int vindictaP2X2;
	public int vindictaP2Y2;
	public int vindictaP2X3;
	public int vindictaP2Y3;
	public int vindictaP2X4;
	public int vindictaP2Y4;
	public int vindictaP2X5;
	public int vindictaP2Y5;
	public int vindictaP2X6;
	public int vindictaP2Y6;
	public int vindictaFirePhaseTwo = 0;;
	public int vindictaFirePhase = 0;
	private boolean riding = false;

	public Vindicta(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
		setIntelligentRouteFinder(true);
		phase = 0;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		Player player = (Player) hit.getSource();
		player.getVarsManager().sendVarBit(28663, getHitpoints());
		player.getVarsManager().sendVar(5775, getHitpoints() - hit.getDamage());
	}

	@Override
	public void sendDeath(final Entity source) {
		Player player = (Player) source;
		player.getInterfaceManager().removeInterface(1648);
		player.getVarsManager().sendVar(6372, 0);
		player.getVarsManager().sendVar(5775, 0);
		resetVariables();
		resetPhases();
		super.sendDeath(source);
	}

	@Override
	public void spawn() {
		resetVariables();
		resetPhases();
		super.spawn();
		setNextNPCTransformation(22459);
		riding = false;
		Gorvek gorvek = new Gorvek(22463, new WorldTile(
				GorvekAndVindictaInstanceController.getWorldTile(27, 23, 1)),
				-1, true, false);
		gorvek.setNextAnimation(new Animation(28264));
		setNextAnimation(new Animation(28257));
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					gorvek.setNextWorldTile(new WorldTile(
							GorvekAndVindictaInstanceController.getWorldTile(
									63, 62, 1)));
					gorvek.setCantFollowUnderCombat(true);
					gorvek.isCantSetTargetAutoRelatio();
				}, 850, TimeUnit.MILLISECONDS);
		GorvekAndVindictaInstanceController.Player.forEach(player -> {
			GameExecutorManager.slowExecutor.schedule(() -> {
				player.getInterfaceManager().sendOverlayInterface(1648);
				player.getVarsManager().sendVar(6372, 200000);
				player.getVarsManager().sendVar(5775, 200000);
				phase = 0;
			}, 2400, TimeUnit.MILLISECONDS);
			GameExecutorManager.slowExecutor.schedule(() -> {
				player.getPackets().sendIComponentText(1648, 27, "Vindicta");
			}, 6410, TimeUnit.MILLISECONDS);
		});
	}

	public void resetPhases() {
		vindictaFirePhaseTwo = 0;
		;
		vindictaFirePhase = 0;
	}

	public void resetVariables() {
		vindictaP2X = -1;
		vindictaP2Y = -1;
		vindictaP2X2 = -1;
		vindictaP2Y2 = -1;
		vindictaP2X3 = -1;
		vindictaP2Y3 = -1;
		vindictaP2X4 = -1;
		vindictaP2Y4 = -1;
		vindictaP2X5 = -1;
		vindictaP2Y5 = -1;
		vindictaP2X6 = -1;
		vindictaP2Y6 = -1;
	}

	private void rideOnDragon() {
		GorvekAndVindictaInstanceController.Gorvek
				.forEach(gorvek -> {
					gorvek.setNextWorldTile(new WorldTile(getX(), getY(), 1));
					gorvek.setNextAnimation(new Animation(28276));
					setNextAnimation(new Animation(28263));
					setNextNPCTransformation(22460);
					setRun(true);
					gorvek.setNextWorldTile(new WorldTile(
							GorvekAndVindictaInstanceController.getWorldTile(
									63, 62, 1)));
					resetVariables();
				});

	}

	@Override
	public void processNPC() {
		if (!riding && getHitpoints() <= 100000) {
			rideOnDragon();
			riding = true;
			setCantInteract(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setCantInteract(false);
					stop();
				}
			}, 2, 1);
		}
		super.processNPC();
	}

	public int getPhase() {
		return phase;
	}

	public void nextPhase() {
		phase++;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public void setVindictaX(int x) {
		this.vindictaX = x;
	}

	public void setVindictaY(int y) {
		this.vindictaY = y;
	}

	public int getVindictaX() {
		return vindictaX;
	}

	public int getVindictaY() {
		return vindictaY;
	}

	public void setVindictaFirePhase(int phase) {
		this.vindictaFirePhase = phase;
	}

	public int getVindictaFirePhase() {
		return vindictaFirePhase;
	}

	public void setVindictaFirePhaseTwo(int phase) {
		this.vindictaFirePhaseTwo = phase;
	}

	public int getVindictaFirePhaseTwo() {
		return vindictaFirePhaseTwo;
	}
}
