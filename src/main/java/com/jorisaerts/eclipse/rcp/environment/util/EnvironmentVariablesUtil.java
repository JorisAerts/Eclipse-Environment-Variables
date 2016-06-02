package com.jorisaerts.eclipse.rcp.environment.util;

import org.eclipse.jface.preference.IPreferenceStore;

import com.jorisaerts.eclipse.rcp.environment.environment.Environment;
import com.jorisaerts.eclipse.rcp.environment.preferences.PreferenceConstants;

public class EnvironmentVariablesUtil {

	public static void applyVariables(final IPreferenceStore store, final boolean replace) {
		final EnvironmentVariables vars = getEnvironmentVariables(store);
		for (final EnvironmentVariable entry : vars) {
			Environment.setenv(entry.getVariable(), entry.getValue(), replace);
		}
	}

	public static void clearOldVariables(final IPreferenceStore store) {
		final EnvironmentVariables oldVars = getEnvironmentVariables(store);
		for (final EnvironmentVariable entry : oldVars) {
			Environment.unsetenv(entry.getVariable());
		}
	}

	public static void reset(final IPreferenceStore store) {
		clearOldVariables(store);
		setEnvironmentVariables(store, new EnvironmentVariables());
	}

	public static EnvironmentVariables getEnvironmentVariables(final IPreferenceStore store) {
		final EnvironmentVariables vars = (EnvironmentVariables) SerializerUtil.fromStringSafe(store.getString(PreferenceConstants.P_ENV_VARS));
		return null == vars ? new EnvironmentVariables() : vars;
	}

	public static void setEnvironmentVariables(final IPreferenceStore store, final EnvironmentVariables vars) {
		clearOldVariables(store);
		store.setValue(PreferenceConstants.P_ENV_VARS, SerializerUtil.toStringSafe(vars));
		applyVariables(store, true);
	}

}
