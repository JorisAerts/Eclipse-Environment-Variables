package com.jorisaerts.eclipse.rcp.environment.util;

import org.eclipse.jface.preference.IPreferenceStore;

import com.jorisaerts.eclipse.rcp.environment.environment.EnvironmentVariables;
import com.jorisaerts.eclipse.rcp.environment.preferences.PreferenceConstants;

public class EnvironmentVariablesUtil {

	public static void applyVariables(final IPreferenceStore store, final boolean replace) {
		final EnvironmentVariableCollection vars = getEnvironmentVariables(store);
		for (final EnvironmentVariable entry : vars) {
			EnvironmentVariables.set(entry.getName(), entry.getValue(), replace);
		}
	}

	public static void clearOldVariables(final IPreferenceStore store) {
		final EnvironmentVariableCollection oldVars = getEnvironmentVariables(store);
		for (final EnvironmentVariable entry : oldVars) {
			EnvironmentVariables.remove(entry.getName());
		}
	}

	public static void reset(final IPreferenceStore store) {
		clearOldVariables(store);
		setEnvironmentVariables(store, new EnvironmentVariableCollection());
	}

	public static EnvironmentVariableCollection getEnvironmentVariables(final IPreferenceStore store) {
		final EnvironmentVariableCollection vars = (EnvironmentVariableCollection) SerializerUtil
				.fromStringSafe(store.getString(PreferenceConstants.P_ENV_VARS));
		return null == vars ? new EnvironmentVariableCollection() : vars;
	}

	public static void setEnvironmentVariables(final IPreferenceStore store, final EnvironmentVariableCollection vars) {
		clearOldVariables(store);
		store.setValue(PreferenceConstants.P_ENV_VARS, SerializerUtil.toStringSafe(vars));
		applyVariables(store, true);
	}

}
