package net.nocturne.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;

import net.nocturne.Settings;
import net.nocturne.login.DisplayNames;
import net.nocturne.login.Offences;
import net.nocturne.login.account.Account;

public class LoginFilesManager {

	/**
	 * Path of the folder where account files are stored.
	 */
	private static final String ACCOUNTS_PATH = Settings.HOSTED ? Settings
			.getDropboxLocation() + "accounts/" : "./data/accounts/";
	/**
	 * Path where offences are stored.
	 */
	private static final String OFFENCES = "offences.ser";
	/**
	 * Path where display names are stored.
	 */
	private static final String DISPLAY_NAMES = "data/displayNames.ser";

	public synchronized static File[] getAllAccounts() {
		return new File(ACCOUNTS_PATH).listFiles();
	}

	public synchronized static boolean containsAccount(String username) {
		return new File(ACCOUNTS_PATH + username + ".acc").exists();
	}

	public synchronized static Account loadAccount(String username) {
		try {
			return (Account) loadObject(new File(ACCOUNTS_PATH + username
					+ ".acc"));
		} catch (Throwable e) {
			System.err.println("Cant load player " + username
					+ ", attempting to recover account.");
			e.printStackTrace();
		}
		return null;
	}

	public synchronized static void saveAccount(Account account) {
		try {
			byte[] data = SerializationUtilities.tryStoreObject(account);
			if (data == null || data.length <= 0)
				return;
			storeObject(account, new File(ACCOUNTS_PATH
					+ account.getUsername().toLowerCase() + ".acc"));
		} catch (ConcurrentModificationException e) {
			// happens because saving and logging out same time
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static synchronized Offences loadOffences() throws IOException {
		if (new File(OFFENCES).exists()) {
			try {
				return (Offences) loadObject(new File(OFFENCES));
			} catch (Throwable e) {
				System.err.println("Cant load offences.");
				e.printStackTrace();
				return null;
			}
		} else {
			Offences object = new Offences();
			new File(OFFENCES).createNewFile();
			storeObject(object, new File(OFFENCES));
			return object;
		}
	}

	public static synchronized void saveOffences(Offences offences) {
		try {
			byte[] data = SerializationUtilities.tryStoreObject(offences);
			if (data == null || data.length <= 0)
				return;
			storeObject(offences, new File(OFFENCES));
		} catch (ConcurrentModificationException e) {
			// happens because saving and logging out same time
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static synchronized DisplayNames loadDisplayNames()
			throws IOException {
		if (new File(DISPLAY_NAMES).exists()) {
			try {
				return (DisplayNames) loadObject(new File(DISPLAY_NAMES));
			} catch (Throwable e) {
				System.err.println("Cant load display names.");
				e.printStackTrace();
				return null;
			}
		} else {
			DisplayNames object = new DisplayNames();
			new File(DISPLAY_NAMES).createNewFile();
			storeObject(object, new File(DISPLAY_NAMES));
			return object;
		}
	}

	public static synchronized void saveDisplayNames(DisplayNames displayNames) {
		try {
			byte[] data = SerializationUtilities.tryStoreObject(displayNames);
			if (data == null || data.length <= 0)
				return;
			storeObject(displayNames, new File(DISPLAY_NAMES));
		} catch (ConcurrentModificationException e) {
			// happens because saving and logging out same time
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private static synchronized Object loadObject(File f) throws IOException,
			ClassNotFoundException {
		if (!f.exists())
			return null;
		FileInputStream fileInputStream = new FileInputStream(f);
		ObjectInputStream in = new ObjectInputStream(fileInputStream);
		Object object = in.readObject();
		fileInputStream.close();
		in.close();
		return object;
	}

	private static synchronized void storeObject(Serializable o, File f)
			throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(o);
		out.close();
	}

}