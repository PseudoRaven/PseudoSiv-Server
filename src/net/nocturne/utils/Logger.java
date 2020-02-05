package net.nocturne.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import net.nocturne.Settings;

public final class Logger {

	/**
	 * @edit: Pax M
	 */

	private static BufferedWriter globallogs;

	public static String mapContents;

	static {
		try {
			if (Settings.HOSTED) {
				Calendar c = Calendar.getInstance();
				globallogs = new BufferedWriter(new FileWriter(
						 System
							.getProperty("user.home") + "/Dropbox/logs/global/log." + ((c.get(Calendar.MONTH)) + 1)
								+ "." + c.get(Calendar.DATE) + "."
								+ c.get(Calendar.YEAR) + "."
								+ Settings.WORLD_ID + ".txt", true));
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static void globalLog(String ip, String name, Object o) {
		try {
			if (!Settings.DEBUG) {
				globallogs.write(Thread.currentThread().getName() + ", " + "["
						+ Utils.currentTime("hh:mm:ss z") + "]" + "" + name
						+ " - " + ip + " [ " + o + " ]");
				globallogs.newLine();
				globallogs.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void handle(Throwable t) {
		System.err.println("ERROR! THREAD NAME: "
				+ Thread.currentThread().getName());
		t.printStackTrace();
		mapContents += "<font color=red>ERROR! THREAD NAME: "
				+ Thread.currentThread().getName() + "</font><br>";
	}

	public static void handle(Throwable throwable, String section) {
		System.out.println("ERROR! THREAD NAME: "
				+ Thread.currentThread().getName() + " IN: " + section);
		try {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			String error = sw.toString();
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					Settings.getDropboxLocation() + "logs/nulls.txt", true));
			writer.newLine();
			writer.write("ERROR! THREAD NAME: "
					+ Thread.currentThread().getName() + " IN: " + section);
			writer.newLine();
			writer.write(error);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throwable.printStackTrace();
		mapContents += "<font color=red>ERROR! THREAD NAME: "
				+ Thread.currentThread().getName() + "</font><br>";
	}

	public static void log(Class<?> classInstance, Object message) {
		log(classInstance.getSimpleName(), message);
		mapContents += "[" + classInstance.getSimpleName() + "] " + message
				+ "<br>";
	}

	public static void log(Object classInstance, Object message) {
		log(classInstance.getClass().getSimpleName(), message);
		mapContents += "[" + classInstance.getClass().getSimpleName() + "] "
				+ message + "<br>";
	}

	public static void log(String className, Object message) {
		String text = "[" + className + "]" + " " + message.toString();
		System.out.println(text);
		mapContents += "[" + className + "] " + message + "<br>";
	}

	public static void logErr(Class<?> classInstance, Object message) {
		logErr(classInstance.getSimpleName(), message);
		mapContents += "<font color=red>[" + classInstance.getSimpleName()
				+ "] " + message + "</font><br>";
	}

	public static void logErr(Object classInstance, Object message) {
		logErr(classInstance.getClass().getSimpleName(), message);
		mapContents += "<font color=red>["
				+ classInstance.getClass().getSimpleName() + "] " + message
				+ "</font><br>";
	}

	public static void logErr(String className, Object message) {
		String text = "[" + className + "]" + " " + message.toString();
		System.err.println(text);
		mapContents += "<font color=red>[" + className + "] " + message
				+ "</font><br>";
	}

	public static String getMapContents() {
		return mapContents;
	}
}