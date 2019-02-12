package com.jorisaerts.eclipse.rcp.environment.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jorisaerts.eclipse.rcp.environment.Activator;
import com.jorisaerts.eclipse.rcp.environment.table.EnvironmentVariablesTable;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariable;
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

public class EnvironmentPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private final EnvironmentVariableCollection vars;
	private EnvironmentVariablesTable table;

	public EnvironmentPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Environment variables to set:");
		vars = EnvironmentVariablesUtil.getEnvironmentVariables(getPreferenceStore());
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI
	 * blocks needed to manipulate various types of preferences. Each field editor
	 * knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {

		table = new EnvironmentVariablesTable(getFieldEditorParent());
		table.setVariables(vars);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);
		table.refresh();

		final Canvas canvas = new Canvas(getFieldEditorParent(), SWT.NONE);
		final GridLayout gl_canvas = new GridLayout(3, true);
		gl_canvas.verticalSpacing = 0;
		gl_canvas.marginHeight = 0;
		gl_canvas.marginWidth = 0;
		canvas.setLayout(gl_canvas);

		final Button addButton = createButton(canvas, "Add");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent evt) {
				vars.add(new EnvironmentVariable("Variable", "value"));
				table.refresh();
			}
		});

		final Button removeButton = createButton(canvas, "Remove");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent evt) {
				table.removeSelected();
			}
		});

		final Button importButton = createButton(canvas, "Import...");
		importButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent evt) {
				final FileDialog dialog = new FileDialog(getShell());
				final File file = new File(dialog.open());
				System.out.println("Selected: " + file.getAbsolutePath());
				final Properties props = new Properties();
				try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);) {
					props.load(reader);
				} catch (final IOException e) {
					e.printStackTrace();
				}
				for (final Entry<Object, Object> entry : props.entrySet()) {
					vars.add(new EnvironmentVariable(entry.getKey().toString(), entry.getValue().toString()));
				}
				table.refresh();
			}
		});

		// dummy field...
		addField(new RadioGroupFieldEditor("id", "", 1, new String[][] {}, getFieldEditorParent(), false));
	}

	private Button createButton(final Composite parent, final String text) {
		final Button button = new Button(parent, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		button.setText(text);
		button.setFont(getFieldEditorParent().getFont());
		return button;
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