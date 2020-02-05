package net.nocturne.network.decoders;

import net.nocturne.network.Session;
import net.nocturne.stream.InputStream;

public abstract class Decoder {

	protected Session session;

	public Decoder(Session session) {
		this.session = session;
	}

	public abstract int decode(InputStream stream);

}