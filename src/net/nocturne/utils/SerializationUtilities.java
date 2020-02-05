package net.nocturne.utils;

import java.io.*;

public class SerializationUtilities {

	public static Object tryLoadObject(byte[] data) {
		try {
			return loadObject(data);
		} catch (Throwable t) {
			Logger.handle(t);
			return null;
		}
	}

	public static byte[] tryStoreObject(Object o) {
		try {
			return storeObject(o);
		} catch (Throwable t) {
			Logger.handle(t);
			return null;
		}
	}

	public static Object loadObject(byte[] data) throws IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(
				data));
		Object object = in.readObject();
		in.close();
		return object;
	}

	public static byte[] storeObject(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(o);
		out.flush();
		byte[] buffer = baos.toByteArray();
		out.close();
		baos.close();
		return buffer;
	}
}