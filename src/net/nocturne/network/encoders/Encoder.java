package net.nocturne.network.encoders;

import net.nocturne.network.Session;

public abstract class Encoder {

	protected Session session;

	public Encoder(Session session) {
		this.session = session;
	}

}
