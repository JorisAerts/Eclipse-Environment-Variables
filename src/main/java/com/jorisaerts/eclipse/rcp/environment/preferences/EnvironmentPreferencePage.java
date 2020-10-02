package com.jorisaerts.eclipse.rcp.environment.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jorisaerts.eclipse.rcp.environment.Activator;
import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.table.EnvironmentVariablesTable;
import com.jorisaerts.eclipse.rcp.environment.table.TableButtons;
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

		final SashForm advancedComposite = new SashForm(parent, SWT.VERTICAL);
		final GridData sashData = new GridData(SWT.FILL, SWT.FILL, true, true);
		advancedComposite.setLayoutData(sashData);

		final Composite mainColumn = new Composite(advancedComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		mainColumn.setFont(parent.getFont());
		mainColumn.setLayout(layout);

		table = new EnvironmentVariablesTable(mainColumn);
		table.setVariables(vars);

		GridData data = new GridData(GridData.BEGINNING);
		data.horizontalSpan = 2;

		// --- buttons
		final Composite controlColumn = new Composite(mainColumn, SWT.NONE);
		data = new GridData(GridData.FILL_VERTICAL);
		controlColumn.setLayoutData(data);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		controlColumn.setLayout(layout);

		new TableButtons(controlColumn, vars, table);

		advancedComposite.setWeights(new int[] { 75, 25 });
		return advancedComposite;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(final IWorkbench workbench) {
	}

	@Override
	public boolean performCancel() {
		return super.performCancel();
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		vars.clear();
		EnvironmentVariablesUtil.reset(getPreferenceStore());
		if (null != table) {
			table.refresh();
		}
	}

	@Override
	public boolean performOk() {
		EnvironmentVariablesUtil.setEnvironmentVariables(getPreferenceStore(), vars);
		return super.performOk();
	}

}