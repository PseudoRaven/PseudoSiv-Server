package net.nocturne.login.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.nocturne.login.Login;
import net.nocturne.network.LoginProtocol;
import net.nocturne.network.LoginServerChannelManager;
import net.nocturne.network.encoders.LoginChannelsPacketEncoder;
import net.nocturne.stream.OutputStream;

public class FriendsIgnores implements Serializable {
	/**
	 * Our serial UID.
	 */
	private static final long serialVersionUID = -1046904222242480545L;

	public static final int PM_STATUS_ONLINE = 0;
	public static final int PM_STATUS_FRIENDSONLY = 1;
	public static final int PM_STATUS_OFFLINE = 2;

	/**
	 * Our account instance.
	 */
	private transient Account account;

	/**
	 * Contains all friends list.
	 */
	private List<String> friends;
	/**
	 * Contains all ignores list.
	 */
	private List<String> ignores;
	/**
	 * Contains list of temporary ignores.
	 */
	private transient List<String> tempIgnores;
	/**
	 * Contains list of friend ranks.
	 */
	private Map<String, Integer> ranks;
	/**
	 * Our pm status.
	 */
	private int pmStatus;

	/**
	 * Friends chat name.
	 */
	private String fcName;
	/**
	 * Friends chat join requirement.
	 */
	private int fcJoinReq;
	/**
	 * Friends chat talk requirement.
	 */
	private int fcTalkReq;
	/**
	 * Friends chat kick requirement.
	 */
	private int fcKickReq;
	/**
	 * Friends chat lootshare requirement.
	 */
	private int fcLootshareReq;
	/**
	 * Friends chat coinshare settings.
	 */
	private boolean fcCoinshare;

	public FriendsIgnores() {
		this.friends = new ArrayList<String>(200);
		this.ignores = new ArrayList<String>(100);
		this.ranks = new HashMap<String, Integer>();
		this.pmStatus = PM_STATUS_ONLINE;

		this.fcName = null;
		this.fcJoinReq = -1;
		this.fcTalkReq = -1;
		this.fcKickReq = 7;
		this.fcLootshareReq = 0;
		this.fcCoinshare = false;
	}

	public void init(Account account) {
		this.account = account;
		this.tempIgnores = new ArrayList<String>(100);
	}

	/**
	 * Set's our pm status.
	 */
	public void setPmStatus(int newStatus, boolean updatePlayer) {
		if (pmStatus == newStatus)
			return;
		int old = pmStatus;
		pmStatus = newStatus;
		if (updatePlayer)
			LoginServerChannelManager.sendReliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder.encodePlayerVarUpdate(
							account.getUsername(),
							LoginProtocol.VAR_TYPE_PMSTATUS, pmStatus)
							.getBuffer());
		Login.onAccountPmStatusChange(account, old, pmStatus);
	}

	/**
	 * Set's our friends chat name.
	 */
	public void setFcName(String newName, boolean updatePlayer) {
		fcName = newName;
		if (updatePlayer)
			LoginServerChannelManager.sendReliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder.encodePlayerVarUpdate(
							account.getUsername(),
							LoginProtocol.VAR_TYPE_FCNAME, fcName).getBuffer());
		Login.onFriendsChatSettingsUpdate(account);
	}

	/**
	 * Set's our friends chat join requirement.
	 */
	public void setFcJoinReq(int newReq, boolean updatePlayer) {
		if (fcJoinReq == newReq)
			return;
		fcJoinReq = newReq;
		if (updatePlayer)
			LoginServerChannelManager.sendReliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder.encodePlayerVarUpdate(
							account.getUsername(),
							LoginProtocol.VAR_TYPE_FCJOINREQ, fcJoinReq)
							.getBuffer());
		Login.onFriendsChatSettingsUpdate(account);
	}

	/**
	 * Set's our friends chat talk requirement.
	 */
	public void setFcTalkReq(int newReq, boolean updatePlayer) {
		if (fcTalkReq == newReq)
			return;
		fcTalkReq = newReq;
		if (updatePlayer)
			LoginServerChannelManager.sendReliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder.encodePlayerVarUpdate(
							account.getUsername(),
							LoginProtocol.VAR_TYPE_FCTALKREQ, fcTalkReq)
							.getBuffer());
		Login.onFriendsChatSettingsUpdate(account);
	}

	/**
	 * Set's our friends chat kick requirement.
	 */
	public void setFcKickReq(int newReq, boolean updatePlayer) {
		if (fcKickReq == newReq)
			return;
		fcKickReq = newReq;
		if (updatePlayer)
			LoginServerChannelManager.sendReliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder.encodePlayerVarUpdate(
							account.getUsername(),
							LoginProtocol.VAR_TYPE_FCKICKREQ, fcKickReq)
							.getBuffer());
		Login.onFriendsChatSettingsUpdate(account);
	}

	/**
	 * Set's our friends chat loot share requirement.
	 */
	public void setFcLootShareReq(int newReq, boolean updatePlayer) {
		if (fcLootshareReq == newReq)
			return;
		fcLootshareReq = newReq;
		if (updatePlayer)
			LoginServerChannelManager.sendReliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder.encodePlayerVarUpdate(
							account.getUsername(),
							LoginProtocol.VAR_TYPE_FCLSREQ, fcLootshareReq)
							.getBuffer());
		Login.onFriendsChatSettingsUpdate(account);
	}

	/**
	 * Set's our friends chat loot share requirement.
	 */
	public void setFcCoinshare(boolean coinshare, boolean updatePlayer) {
		if (fcCoinshare == coinshare)
			return;
		fcCoinshare = coinshare;
		if (updatePlayer)
			LoginServerChannelManager.sendReliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder.encodePlayerVarUpdate(
							account.getUsername(),
							LoginProtocol.VAR_TYPE_FCCSSTATE,
							fcCoinshare ? 1 : 0).getBuffer());
		Login.onFriendsChatSettingsUpdate(account);
	}

	/**
	 * Happens on account login.
	 */
	public void onLogin() {
		if (pmStatus != PM_STATUS_OFFLINE)
			Login.onAccountPmStatusChange(account, PM_STATUS_OFFLINE, pmStatus); // signin
	}

	/**
	 * Happens on account login.
	 */
	public void onLogout() {
		if (pmStatus != PM_STATUS_OFFLINE)
			Login.onAccountPmStatusChange(account, pmStatus, PM_STATUS_OFFLINE); // signout
	}

	/**
	 * Initialize's client data
	 */
	public void sendAll() {
		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerVarUpdate(
						account.getUsername(), LoginProtocol.VAR_TYPE_PMSTATUS,
						pmStatus).getBuffer());
		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerVarUpdate(
						account.getUsername(), LoginProtocol.VAR_TYPE_FCNAME,
						fcName).getBuffer());
		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerVarUpdate(
						account.getUsername(),
						LoginProtocol.VAR_TYPE_FCJOINREQ, fcJoinReq)
						.getBuffer());
		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerVarUpdate(
						account.getUsername(),
						LoginProtocol.VAR_TYPE_FCTALKREQ, fcTalkReq)
						.getBuffer());
		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerVarUpdate(
						account.getUsername(),
						LoginProtocol.VAR_TYPE_FCKICKREQ, fcKickReq)
						.getBuffer());
		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerVarUpdate(
						account.getUsername(), LoginProtocol.VAR_TYPE_FCLSREQ,
						fcLootshareReq).getBuffer());
		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerVarUpdate(
						account.getUsername(),
						LoginProtocol.VAR_TYPE_FCCSSTATE, fcCoinshare ? 1 : 0)
						.getBuffer());

		OutputStream packet = LoginChannelsPacketEncoder
				.createUpdateFriendsPacket(account.getUsername());
		Iterator<String> it$ = friends.iterator();
		while (it$.hasNext()) {
			String user = it$.next();
			String displayName = Login.getDisplayName(user);
			if (displayName == null) {
				it$.remove(); // bad friend
				continue;
			}

			if (!ranks.containsKey(user))
				ranks.put(user, 0);

			Account account = Login.findAccount(user);
			if (account != null
					&& !isAccountOnline(account, account.getFriendsIgnores()
							.getPmStatus()))
				account = null;
			LoginChannelsPacketEncoder
					.appendUpdateFriend(packet, false, displayName, Login
							.getPreviousDisplayName(user),
							account != null ? account.getWorld().getId() : 0,
							getRank(user),
							account != null ? ((account.isLobby() ? "Lobby "
									: "World ") + account.getWorld().getId())
									: null);
		}
		LoginChannelsPacketEncoder.finishUpdateFriendsPacket(packet);
		LoginServerChannelManager.sendReliablePacket(account.getWorld(),
				packet.getBuffer());

		packet = LoginChannelsPacketEncoder.createUpdateIgnoresPacket(
				account.getUsername(), true);
		it$ = ignores.iterator();
		while (it$.hasNext()) {
			String user = it$.next();
			String displayName = Login.getDisplayName(user);
			if (displayName == null) {
				it$.remove(); // bad ignore
				continue;
			}
			LoginChannelsPacketEncoder.appendUpdateIgnore(packet, false,
					displayName, Login.getPreviousDisplayName(user));
		}
		it$ = tempIgnores.iterator();
		while (it$.hasNext()) {
			String user = it$.next();
			String displayName = Login.getDisplayName(user);
			if (displayName == null) {
				it$.remove(); // bad ignore
				continue;
			}
			LoginChannelsPacketEncoder.appendUpdateIgnore(packet, false,
					displayName, Login.getPreviousDisplayName(user));
		}
		LoginChannelsPacketEncoder.finishUpdateIgnoresPacket(packet);
		LoginServerChannelManager.sendReliablePacket(account.getWorld(),
				packet.getBuffer());
	}

	/**
	 * Happens when account has changed his online status.
	 */
	public void onAccountPmStatusChange(Account account, int previousStatus,
			int currentStatus) {
		if (!isFriend(account.getUsername()))
			return; // That account is not our friend afterall
		boolean wasOnline = isAccountOnline(account, previousStatus);
		boolean isOnline = isAccountOnline(account, currentStatus);
		if (wasOnline == isOnline)
			return; // Our visibility of that account did not change

		OutputStream packet = LoginChannelsPacketEncoder
				.createUpdateFriendsPacket(this.account.getUsername());
		LoginChannelsPacketEncoder.appendUpdateFriend(packet, true, account
				.getDisplayName(), account.getPreviousDisplayName(),
				isOnline ? account.getWorld().getId() : 0, getRank(account
						.getUsername()),
				isOnline ? ((account.isLobby() ? "Lobby " : "World ") + account
						.getWorld().getId()) : null);
		LoginChannelsPacketEncoder.finishUpdateFriendsPacket(packet);
		LoginServerChannelManager.sendReliablePacket(this.account.getWorld(),
				packet.getBuffer());
	}

	/**
	 * Happens when account has changed his display name.
	 */
	public void onAccountDisplayNameChange(Account account) {
		if (isFriend(account.getUsername())) {
			boolean isOnline = isAccountOnline(account, account
					.getFriendsIgnores().getPmStatus());
			OutputStream packet = LoginChannelsPacketEncoder
					.createUpdateFriendsPacket(this.account.getUsername());
			LoginChannelsPacketEncoder
					.appendUpdateFriend(packet, false,
							account.getDisplayName(), account
									.getPreviousDisplayName(),
							isOnline ? account.getWorld().getId() : 0,
							getRank(account.getUsername()),
							isOnline ? ((account.isLobby() ? "Lobby "
									: "World ") + account.getWorld().getId())
									: null);
			LoginChannelsPacketEncoder.finishUpdateFriendsPacket(packet);
			LoginServerChannelManager.sendReliablePacket(
					this.account.getWorld(), packet.getBuffer());
		} else if (isIgnore(account.getUsername())) {
			OutputStream packet = LoginChannelsPacketEncoder
					.createUpdateIgnoresPacket(this.account.getUsername(),
							false);
			LoginChannelsPacketEncoder.appendUpdateIgnore(packet, true,
					account.getDisplayName(), account.getPreviousDisplayName());
			LoginChannelsPacketEncoder.finishUpdateIgnoresPacket(packet);
			LoginServerChannelManager.sendReliablePacket(
					this.account.getWorld(), packet.getBuffer());
		}
	}

	/**
	 * Send's private message to given target.
	 */
	public void sendPrivateMessage(String target, String message) {
		String username = Login.getUsername(target);
		if (username == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Couldn't find player " + target + ".")
							.getBuffer());
			return;
		}

		if (!isFriend(username)) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									target + " is not on your friends list.")
							.getBuffer());
			return;
		}

		if (account.isMuted()) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(), "You are muted.")
							.getBuffer());
			return;
		}

		Account acc = Login.findAccount(username);
		if (acc != null
				&& !isAccountOnline(acc, acc.getFriendsIgnores().getPmStatus()))
			acc = null;

		if (acc == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									target + " is offline.").getBuffer());
			return;
		}

		if (pmStatus == PM_STATUS_OFFLINE)
			setPmStatus(PM_STATUS_ONLINE, true);

		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerSentPrivateMessage(
						account.getUsername(), target, message).getBuffer());
		LoginServerChannelManager.sendReliablePacket(
				acc.getWorld(),
				LoginChannelsPacketEncoder
						.encodePlayerReceivedPrivateMessage(acc.getUsername(),
								account.getDisplayName(), Login.createNewUid(),
								account.getMessageIcon(), message).getBuffer());
	}

	/**
	 * Send's private message to given target.
	 */
	public void sendPrivateMessage(String target, int qcFileId, byte[] qcData) {
		String username = Login.getUsername(target);
		if (username == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Couldn't find player " + target + ".")
							.getBuffer());
			return;
		}

		if (!isFriend(username)) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									target + " is not on your friends list.")
							.getBuffer());
			return;
		}

		Account acc = Login.findAccount(username);
		if (acc != null
				&& !isAccountOnline(acc, acc.getFriendsIgnores().getPmStatus()))
			acc = null;

		if (acc == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									target + " is offline.").getBuffer());
			return;
		}

		if (pmStatus == PM_STATUS_OFFLINE)
			setPmStatus(PM_STATUS_ONLINE, true);

		LoginServerChannelManager.sendReliablePacket(
				account.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerSentPrivateMessage(
						account.getUsername(), target, qcFileId, qcData)
						.getBuffer());
		LoginServerChannelManager.sendReliablePacket(
				acc.getWorld(),
				LoginChannelsPacketEncoder.encodePlayerReceivedPrivateMessage(
						acc.getUsername(), account.getDisplayName(),
						Login.createNewUid(), account.getMessageIcon(),
						qcFileId, qcData).getBuffer());
	}

	/**
	 * Add's friend to friends list.
	 */
	public void addFriend(String target) {
		if (friends.size() >= 200) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Your friends list is full.").getBuffer());
			return;
		}

		String username = Login.getUsername(target);
		if (username == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Couldn't find player " + target + ".")
							.getBuffer());
			return;
		}

		if (username.equals(account.getUsername())) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"You can't add yourself.").getBuffer());
			return;
		}

		if (isIgnore(username)) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Please remove " + target
											+ " from your ignores list first.")
							.getBuffer());
			return;
		}

		if (isFriend(username)) {
			LoginServerChannelManager
					.sendUnreliablePacket(
							account.getWorld(),
							LoginChannelsPacketEncoder
									.encodePlayerFriendIgnoreSystemMessage(
											account.getUsername(),
											target
													+ " is already on your friends list.")
									.getBuffer());
			return;
		}

		friends.add(username);
		ranks.put(username, 0);
		Login.onFriendRankUpdate(account, username);

		Account acc = Login.findAccount(username);
		if (acc != null
				&& !isAccountOnline(acc, acc.getFriendsIgnores().getPmStatus()))
			acc = null;

		OutputStream packet = LoginChannelsPacketEncoder
				.createUpdateFriendsPacket(account.getUsername());
		LoginChannelsPacketEncoder.appendUpdateFriend(packet, false, target,
				Login.getPreviousDisplayName(username), acc != null ? acc
						.getWorld().getId() : 0, getRank(username),
				acc != null ? ((acc.isLobby() ? "Lobby " : "World ") + acc
						.getWorld().getId()) : null);
		LoginChannelsPacketEncoder.finishUpdateFriendsPacket(packet);
		LoginServerChannelManager.sendReliablePacket(account.getWorld(),
				packet.getBuffer());

		if (pmStatus == PM_STATUS_FRIENDSONLY && acc != null)
			acc.getFriendsIgnores().onAccountPmStatusChange(account,
					PM_STATUS_OFFLINE, PM_STATUS_ONLINE);
	}

	/**
	 * Remove's friend from friends list.
	 */
	public void removeFriend(String target) {
		String username = Login.getUsername(target);
		if (username == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Couldn't find player " + target + ".")
							.getBuffer());
			return;
		}

		if (!isFriend(username)) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									target + " is not on your friends list.")
							.getBuffer());
			return;
		}

		friends.remove(username);
		ranks.remove(username);
		Login.onFriendRankUpdate(account, username);

		Account acc = Login.findAccount(username);
		if (pmStatus == PM_STATUS_FRIENDSONLY && acc != null)
			acc.getFriendsIgnores().onAccountPmStatusChange(account,
					PM_STATUS_ONLINE, PM_STATUS_OFFLINE);

	}

	/**
	 * Add's ignore to ignore's list.
	 */
	public void addIgnore(String target, boolean temporary) {
		if ((ignores.size() + tempIgnores.size()) >= 100) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Your ignores list is full.").getBuffer());
			return;
		}

		String username = Login.getUsername(target);
		if (username == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Couldn't find player " + target + ".")
							.getBuffer());
			return;
		}

		if (username.equals(account.getUsername())) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"You can't add yourself.").getBuffer());
			return;
		}

		if (isFriend(username)) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Please remove " + target
											+ " from your friends list first.")
							.getBuffer());
			return;
		}

		if (isIgnore(username)) {
			LoginServerChannelManager
					.sendUnreliablePacket(
							account.getWorld(),
							LoginChannelsPacketEncoder
									.encodePlayerFriendIgnoreSystemMessage(
											account.getUsername(),
											target
													+ " is already on your ignores list.")
									.getBuffer());
			return;
		}

		if (temporary)
			tempIgnores.add(username);
		else
			ignores.add(username);

		OutputStream packet = LoginChannelsPacketEncoder
				.createUpdateIgnoresPacket(account.getUsername(), false);
		LoginChannelsPacketEncoder.appendUpdateIgnore(packet, false, target,
				Login.getPreviousDisplayName(username));
		LoginChannelsPacketEncoder.finishUpdateIgnoresPacket(packet);
		LoginServerChannelManager.sendReliablePacket(account.getWorld(),
				packet.getBuffer());
	}

	/**
	 * Remove's ignore from ignore's list.
	 */
	public void removeIgnore(String target) {
		String username = Login.getUsername(target);
		if (username == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Couldn't find player " + target + ".")
							.getBuffer());
			return;
		}

		if (!isIgnore(username)) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									target + " is not on your ignores list.")
							.getBuffer());
			return;
		}

		ignores.remove(username);
		tempIgnores.remove(username);

	}

	/**
	 * Change's target's rank.
	 */
	public void changeRank(String target, int newRank) {
		String username = Login.getUsername(target);
		if (username == null) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									"Couldn't find player " + target + ".")
							.getBuffer());
			return;
		}

		if (!isFriend(username)) {
			LoginServerChannelManager.sendUnreliablePacket(
					account.getWorld(),
					LoginChannelsPacketEncoder
							.encodePlayerFriendIgnoreSystemMessage(
									account.getUsername(),
									target + " is not on your friends list.")
							.getBuffer());
			return;
		}

		if (newRank < 0)
			newRank = 0;
		else if (newRank > 6)
			newRank = 6;

		if (getRank(username) != newRank) {
			ranks.put(username, newRank);

			Account acc = Login.findAccount(username);
			if (acc != null
					&& !isAccountOnline(acc, acc.getFriendsIgnores()
							.getPmStatus()))
				acc = null;

			OutputStream packet = LoginChannelsPacketEncoder
					.createUpdateFriendsPacket(account.getUsername());
			LoginChannelsPacketEncoder.appendUpdateFriend(packet, true, target,
					Login.getPreviousDisplayName(username), acc != null ? acc
							.getWorld().getId() : 0, newRank,
					acc != null ? ((acc.isLobby() ? "Lobby " : "World ") + acc
							.getWorld().getId()) : null);
			LoginChannelsPacketEncoder.finishUpdateFriendsPacket(packet);
			LoginServerChannelManager.sendReliablePacket(account.getWorld(),
					packet.getBuffer());

			Login.onFriendRankUpdate(account, username);
		}
	}

	/**
	 * Whether given account appears online for our account.
	 */
	public boolean isAccountOnline(Account account, int pmStatus) {
		if (pmStatus == PM_STATUS_OFFLINE)
			return false;

		if (pmStatus == PM_STATUS_FRIENDSONLY
				&& !account.getFriendsIgnores().isFriend(
						this.account.getUsername()))
			return false;
		return true;
	}

	/**
	 * Whether friends list contains given username.
	 */
	public boolean isFriend(String username) {
		return friends.contains(username);
	}

	/**
	 * Whether ignores list contains given username.
	 */
	public boolean isIgnore(String username) {
		return ignores.contains(username) || tempIgnores.contains(username);
	}

	/**
	 * Return's ranks map. Modifying is strictly prohibited!
	 */
	public Map<String, Integer> getAllRanks() {
		return ranks;
	}

	/**
	 * Return's rank of specific username.
	 */
	public int getRank(String username) {
		Integer rank = ranks.get(username);
		if (rank != null)
			return rank.intValue();
		return 0;
	}

	public List<String> getFriends() {
		return friends;
	}

	public List<String> getIgnores() {
		return ignores;
	}

	public List<String> getTempIgnores() {
		return tempIgnores;
	}

	public int getPmStatus() {
		return pmStatus;
	}

	public String getFcName() {
		return fcName;
	}

	public int getFcJoinReq() {
		return fcJoinReq;
	}

	public int getFcTalkReq() {
		return fcTalkReq;
	}

	public int getFcKickReq() {
		return fcKickReq;
	}

	public int getFcLootshareReq() {
		return fcLootshareReq;
	}

	public boolean isFcCoinshare() {
		return fcCoinshare;
	}

}
