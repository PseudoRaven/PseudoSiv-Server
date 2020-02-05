package net.nocturne.network.decoders;

import net.nocturne.Settings;
import net.nocturne.game.player.Player;
import net.nocturne.network.Session;
import net.nocturne.stream.InputStream;
import net.nocturne.utils.Logger;

public final class WorldLoginPacketsDecoder extends Decoder {

	private Player player;

	public WorldLoginPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public final int decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		switch (packetId) {
		case 26:
			return decodeLogin(stream);
		default:
			if (Settings.DEBUG)
				Logger.log(this, "WorldLoginPacketId " + packetId);
			session.getChannel().close();
			return -1;
		}
	}

	private final int decodeLogin(InputStream stream) {
		if (stream.getRemaining() != 0) {
			session.getChannel().close();
			return -1;
		} // switches decoder
		session.setDecoder(3, player);
		return stream.getOffset();
	}
}