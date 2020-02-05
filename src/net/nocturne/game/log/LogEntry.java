package net.nocturne.game.log;

import java.io.Serializable;

import net.nocturne.utils.Utils;

public class LogEntry implements Serializable {
	/**
	 * Serial number.
	 */
	private static final long serialVersionUID = -9141858878691509737L;

	/**
	 * Type of the entry.
	 */
	private int type;
	/**
	 * Date of the log.
	 */
	private long date;

	private LogEntry(int type) {
		this.type = type;
		this.date = Utils.currentTimeMillis();
	}

	public int getType() {
		return type;
	}

	public long getDate() {
		return date;
	}

}
