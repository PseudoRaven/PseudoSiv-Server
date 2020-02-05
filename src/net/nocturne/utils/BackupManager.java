package net.nocturne.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.nocturne.Settings;

public class BackupManager {

	/**
	 * @author: Pax M
	 */

	/**
	 * The paths we wish to back up
	 */
	private static String[] FOLDERS = { "/src/", "/data/accounts/" };

	/**
	 * Inits the backup manager
	 */
	public static void init() {
		if (!Settings.DAILY_BACKUP) {
			Logger.logErr("BackupManager",
					"BackupManager denied access, service is closed.");
			return;
		}
		for (int folders = 0; folders < FOLDERS.length; folders++) {
			File input = new File("." + FOLDERS[folders]);
			File output = new File("./data/backups/"
					+ FOLDERS[folders].replaceAll("/", "").replaceAll("data",
							"") + "/" + Utils.getDate(false) + ".zip");
			if (!output.exists()) {
				try {
					if (input.list().length == 0)
						return;
					zipDirectory(input, output);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Zip directory.
	 *
	 * @param file
	 *            the file
	 * @param zippedFile
	 *            the zipped file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static final void zipDirectory(File file, File zippedFile)
			throws IOException {
		ZipOutputStream z = new ZipOutputStream(
				new FileOutputStream(zippedFile));
		zip(file, file, z);
		z.close();
	}

	/**
	 * Zip.
	 *
	 * @param directory
	 *            the directory
	 * @param base
	 *            the base
	 * @param zos
	 *            the zos
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static final void zip(File directory, File base, ZipOutputStream zos)
			throws IOException {
		File[] files = directory.listFiles();
		byte[] buffer = new byte[20000];
		int read = 0;
		for (int i = 0, n = files.length; i < n; i++) {
			if (files[i].isDirectory()) {
				zip(files[i], base, zos);
			} else {
				FileInputStream in = new FileInputStream(files[i]);
				ZipEntry entry = new ZipEntry(files[i].getPath().substring(
						base.getPath().length() + 1));
				zos.putNextEntry(entry);
				while (-1 != (read = in.read(buffer))) {
					zos.write(buffer, 0, read);
				}
				in.close();
			}
		}
	}
}