package com.jorisaerts.eclipse.rcp.environment.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Base64;

public class SerializerUtil {

	/** Read the object from Base64 string. */
	public static Object fromString(final String s) throws IOException, ClassNotFoundException {
		if (null == s) {
			return null;
		}
		final byte[] data = Base64.getDecoder().decode(s.getBytes(Charset.forName("UTF-8")));
		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		final Object o = ois.readObject();
		ois.close();
		return o;
	}

	public static Object fromStringSafe(final String s) {
		try {
			return fromString(s);
		} catch (final Exception e) {
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
		return new String(Base64.getEncoder().encode(baos.toByteArray()), Charset.forName("UTF-8"));
	}

	public static String toStringSafe(final Serializable o) {
		try {
			return toString(o);
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
