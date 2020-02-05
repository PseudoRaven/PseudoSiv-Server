package net.nocturne.game.player;

import java.io.Serializable;

public class AcceptAid implements Serializable {

	private static final long serialVersionUID = 4676081153229721969L;

	public static final int TELE_OTHER = 0, LUNAR_SPELLS = 1, SEREN_SPELLS = 2,
			ASSISTANCE_REQUESTS = 3, INVITATIONS = 4, BENEFICIAL_BOOSTS = 5,
			MISCELLANEOUS = 6;

	private Player player;
	private int aid[];

	public AcceptAid(Player player) {
		this.player = player;
		this.aid = new int[7];
	}

	public boolean isAcceptingAid(int type, Player offer) {
		if (aid[type] == 2)
			return true;
		else if (aid[type] == 1
				&& (player.getFriendsIgnores().isFriend(offer.getDisplayName()) || (player
						.getClanManager() != null
						&& player.getClanManager().getClan() != null && player
						.getClanManager().getClan().getMember(offer) != null)))
			return true;
		return false;
	}

	public void toggleAid(int componentId) {
		switch (componentId) {
		case 34:
			aid[TELE_OTHER] = 2;
		case 37:
			aid[TELE_OTHER] = 1;
		case 40:
			aid[TELE_OTHER] = 0;
		case 47:
			aid[LUNAR_SPELLS] = 2;
		case 50:
			aid[LUNAR_SPELLS] = 1;
		case 53:
			aid[LUNAR_SPELLS] = 0;
		case 59:
			aid[SEREN_SPELLS] = 2;
		case 62:
			aid[SEREN_SPELLS] = 1;
		case 65:
			aid[SEREN_SPELLS] = 0;
		case 71:
			aid[ASSISTANCE_REQUESTS] = 2;
		case 74:
			aid[ASSISTANCE_REQUESTS] = 1;
		case 77:
			aid[ASSISTANCE_REQUESTS] = 0;
		case 83:
			aid[INVITATIONS] = 2;
		case 86:
			aid[INVITATIONS] = 1;
		case 89:
			aid[INVITATIONS] = 0;
		case 95:
			aid[BENEFICIAL_BOOSTS] = 2;
		case 98:
			aid[BENEFICIAL_BOOSTS] = 1;
		case 101:
			aid[BENEFICIAL_BOOSTS] = 0;
		case 107:
			aid[MISCELLANEOUS] = 2;
		case 110:
			aid[MISCELLANEOUS] = 1;
		case 113:
			aid[MISCELLANEOUS] = 0;
		}
	}

}
