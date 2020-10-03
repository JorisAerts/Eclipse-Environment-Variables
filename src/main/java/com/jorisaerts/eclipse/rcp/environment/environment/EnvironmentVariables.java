package com.jorisaerts.eclipse.rcp.environment.environment;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;

import com.jorisaerts.eclipse.rcp.environment.environment.internal.Environment;

public interface EnvironmentVariables {

	static String get(final String name) {
		return null;
	}

	static void set(final String name, final String value, final boolean overwrite) {
		String substituted = value;
		try {
			substituted = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(value);
		} catch (final CoreException e) {
			// TODO: log this
		}
		Environment.set(name, substituted, overwrite);
	}

	static void set(final String name, final String value) {
		set(name, value, true);
	}

	static void remove(final String name) {
		Environment.remove(name);
	}

}
