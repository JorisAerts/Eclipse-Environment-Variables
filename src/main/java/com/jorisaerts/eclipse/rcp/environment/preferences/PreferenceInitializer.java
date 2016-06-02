package com.jorisaerts.eclipse.rcp.environment.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.jorisaerts.eclipse.rcp.environment.Activator;
import com.jorisaerts.eclipse.rcp.environment.util.EnvVars;
import com.jorisaerts.eclipse.rcp.environment.util.MapHelper;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override public void initializeDefaultPreferences() {
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_ENV_CHOICE, PreferenceConstants.P_ENV_CHOICE_APPEND);
		store.setDefault(PreferenceConstants.P_ENV_VARS, MapHelper.toStringSafe(new EnvVars()));
	}

}
