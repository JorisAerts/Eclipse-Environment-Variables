package com.jorisaerts.eclipse.rcp.environment.environment;

import com.jorisaerts.eclipse.rcp.environment.environment.internal.Environment;

public interface EnvironmentVariables {

	static String get(final String name) {
		return null;
	}

	static void set(final String name, final String value, final boolean overwrite) {
		Environment.set(name, value, overwrite);
	}

	static void set(final String name, final String value) {
		set(name, value, true);
	}

	static void remove(final String name) {
		Environment.remove(name);
	}

}
