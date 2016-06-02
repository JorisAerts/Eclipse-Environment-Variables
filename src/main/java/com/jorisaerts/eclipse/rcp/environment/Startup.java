package com.jorisaerts.eclipse.rcp.environment;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;

import com.jorisaerts.eclipse.rcp.environment.preferences.PreferenceConstants;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariablesUtil;

public class Startup implements IStartup {

	@Override public void earlyStartup() {
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		final boolean replace = PreferenceConstants.P_ENV_CHOICE_REPLACE.equals(store.getString(PreferenceConstants.P_ENV_CHOICE));
		EnvironmentVariablesUtil.applyVariables(store, replace);

	}

}
