package com.jorisaerts.eclipse.rcp.environment.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jorisaerts.eclipse.rcp.environment.Activator;
import com.jorisaerts.eclipse.rcp.environment.table.MapTable;
import com.jorisaerts.eclipse.rcp.environment.util.EnvVars;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariablesUtil;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class EnvironmentPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private final EnvVars vars;
	private MapTable table;

	public EnvironmentPreferencePage() {
		super(GRID);

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Environment variables to set:");
		vars = EnvironmentVariablesUtil.getEnvironmentVariables(getPreferenceStore());
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override public void createFieldEditors() {

		getShell().setLayout(new GridLayout(2, false));

		table = new MapTable(getFieldEditorParent(), vars);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);
		table.refresh();

		final Button addButton = new Button(getFieldEditorParent(), SWT.NONE);
		addButton.setText("Add");
		addButton.setFont(getFieldEditorParent().getFont());
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent evt) {
				String key = "Key";
				if (vars.containsKey(key)) {
					int i = 0;
					while (vars.containsKey(key + " " + ++i)) {
					}
					key = key + " " + i;
				}
				vars.put(key, "New Value");
				table.refresh();
			}
		});

		final Button removeButton = new Button(getFieldEditorParent(), SWT.NONE);
		removeButton.setText("Remove");
		removeButton.setFont(getFieldEditorParent().getFont());
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent evt) {
				table.removeSelected();
			}
		});

		final RadioGroupFieldEditor radioGroupFieldEditor = new RadioGroupFieldEditor(PreferenceConstants.P_ENV_CHOICE, "", 1,
				new String[][] { { "&Append environment to native environment", PreferenceConstants.P_ENV_CHOICE_APPEND }, { "&Replace environment with specified environment", PreferenceConstants.P_ENV_CHOICE_REPLACE } }, getFieldEditorParent());
		addField(radioGroupFieldEditor);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override public void init(final IWorkbench workbench) {
	}

	@Override public boolean performCancel() {
		return super.performCancel();
	}

	@Override protected void performDefaults() {
		super.performDefaults();
		vars.clear();
		vars.putAll(EnvironmentVariablesUtil.getEnvironmentVariables(getPreferenceStore()));
		EnvironmentVariablesUtil.applyVariables(getPreferenceStore(), true);
		if (null != table) {
			table.refresh();
		}
	}

	@Override public boolean performOk() {
		EnvironmentVariablesUtil.setEnvironmentVariables(getPreferenceStore(), vars);
		return super.performOk();
	}

}