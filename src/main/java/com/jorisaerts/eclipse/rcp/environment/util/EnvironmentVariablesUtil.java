package com.jorisaerts.eclipse.rcp.environment.util;

import java.util.Map.Entry;

import org.eclipse.jface.preference.IPreferenceStore;

import com.jorisaerts.eclipse.rcp.environment.environment.Environment;
import com.jorisaerts.eclipse.rcp.environment.preferences.PreferenceConstants;

public class EnvironmentVariablesUtil {

	public static void applyVariables(final IPreferenceStore store, final boolean replace) {
		final EnvVars vars = getEnvironmentVariables(store);
		for (final Entry<String, String> entry : vars.entrySet()) {
			Environment.setenv(entry.getKey(), entry.getValue(), replace);
		}
	}

	public static EnvVars getEnvironmentVariables(final IPreferenceStore store) {
		final EnvVars vars = (EnvVars) MapHelper.fromStringSafe(store.getString(PreferenceConstants.P_ENV_VARS));
		return null == vars ? new EnvVars() : vars;
	}

	public static void setEnvironmentVariables(final IPreferenceStore store, final EnvVars vars) {
		final EnvVars oldVars = getEnvironmentVariables(store);
		for (final String key : oldVars.keySet()) {
			Environment.unsetenv(key);
		}
		store.setValue(PreferenceConstants.P_ENV_VARS, MapHelper.toStringSafe(vars));
		applyVariables(store, true);
	}

}
