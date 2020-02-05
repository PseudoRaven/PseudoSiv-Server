package net.nocturne.game;

public class DeathBar extends HitBar {

	int percent;
	int endPercent;

	public DeathBar(Entity e, int percent) {
		this.percent = percent;
	}

	@Override
	public int getPercentage() {
		return percent;
	}

	@Override
	public int getType() {
		return 5;
	}

	public boolean display(Entity e) {
		return true;
	}
}
