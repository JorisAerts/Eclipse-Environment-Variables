package com.jorisaerts.eclipse.rcp.environment.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.eclipse.core.internal.preferences.Base64;

@SuppressWarnings("restriction") public class MapHelper {

	/** Read the object from Base64 string. */
	public static Object fromString(final String s) throws IOException, ClassNotFoundException {
		if (null == s) {
			return null;
		}
		final byte[] data = Base64.decode(s.getBytes());
		//final byte[] data = s.getBytes();
		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		final Object o = ois.readObject();
		ois.close();
		return o;
	}

	public static Object fromStringSafe(final String s) {
		try {
			return fromString(s);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** Write the object to a Base64 string. */
	public static String toString(final Serializable o) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		//return new String(baos.toByteArray());
		return new String(Base64.encode(baos.toByteArray()));
	}

	public static String toStringSafe(final Serializable o) {
		try {
			return toString(o);
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
