package com.jorisaerts.eclipse.rcp.environment;

import static com.jorisaerts.eclipse.rcp.environment.preferences.PreferenceConstants.P_ENV_CHOICE;
import static com.jorisaerts.eclipse.rcp.environment.preferences.PreferenceConstants.P_ENV_CHOICE_REPLACE;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;

import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariablesUtil;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		try {
			final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			final boolean replace = P_ENV_CHOICE_REPLACE.equals(store.getString(P_ENV_CHOICE));
			EnvironmentVariablesUtil.applyVariables(store, replace);
		} catch (final Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
