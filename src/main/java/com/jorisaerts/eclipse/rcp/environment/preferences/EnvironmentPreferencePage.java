package com.jorisaerts.eclipse.rcp.environment.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jorisaerts.eclipse.rcp.environment.Activator;
import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.table.EnvironmentVariablesTable;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariableCollection;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariablesUtil;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class EnvironmentPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private final EnvironmentVariableCollection vars;
	private EnvironmentVariablesTable table;

	public EnvironmentPreferencePage() {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.EnvironmentTab_Environment_variables_to_set__3);
		vars = EnvironmentVariablesUtil.getEnvironmentVariables(getPreferenceStore());
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI
	 * blocks needed to manipulate various types of preferences. Each field editor
	 * knows how to save and restore itself.
	 */
	@Override
	public Control createContents(final Composite parent) {
		final Font font = parent.getFont();

		final SashForm composite = new SashForm(parent, SWT.VERTICAL);
		composite.setFont(font);

		final GridData sashData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(sashData);

		table = new EnvironmentVariablesTable(composite);
		table.setVariables(vars);

		return composite;
	}

	@Override
	public void init(final IWorkbench workbench) {
	}

	@Override
	public boolean performCancel() {
		return super.performCancel();
	}

	@Override
	protected void performDefaults() {
		table.clear();
		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		final EnvironmentVariableCollection vars = table.getEnvironmentVariables();
		EnvironmentVariablesUtil.setEnvironmentVariables(getPreferenceStore(), vars);
		return super.performOk();
	}

}