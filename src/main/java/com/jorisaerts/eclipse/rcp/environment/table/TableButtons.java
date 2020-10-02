package com.jorisaerts.eclipse.rcp.environment.table;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import com.jorisaerts.eclipse.rcp.environment.eclipse.debug.internal.MultipleInputDialog;
import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariable;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariableCollection;
import com.jorisaerts.eclipse.rcp.environment.util.SWTUtils;

public class TableButtons extends Composite {

	private final EnvironmentVariablesTable table;

	protected Button envAddButton;
	protected Button envSelectButton;
	protected Button envEditButton;
	protected Button envRemoveButton;
	protected Button envCopyButton;
	protected Button envPasteButton;

	public TableButtons(final Composite parent, final EnvironmentVariableCollection vars, final EnvironmentVariablesTable table) {
		super(parent, SWT.NONE);
		this.table = table;
		final Font font = parent.getFont();

		setLayout(SWTUtils.createGrid(1, 0, 0));
		setFont(font);
		setLayoutData(SWTUtils.createGridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_END, 1));

		// Create buttons
		envAddButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Add_4, null);
		envAddButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				handleEnvAddButtonSelected();

				//				vars.add(new EnvironmentVariable("Variable", "value"));
				//				table.refresh();
			}
		});
		envSelectButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_18, null);
		envSelectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				//				handleEnvSelectButtonSelected();
			}
		});
		envEditButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Edit_5, null);
		envEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				//				handleEnvEditButtonSelected();
			}
		});
		envEditButton.setEnabled(false);
		envRemoveButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Remove_6, null);
		envRemoveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				//				handleEnvRemoveButtonSelected();
				table.removeSelected();
			}
		});
		envRemoveButton.setEnabled(false);
		envCopyButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Copy, null);
		envCopyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				//				handleEnvCopyButtonSelected();
			}
		});
		envCopyButton.setEnabled(false);
		envPasteButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Paste, null);
		envPasteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				//				handleEnvPasteButtonSelected();
			}
		});
		envPasteButton.setEnabled(true);

	}

	/**
	 * Adds a new environment variable to the table.
	 */
	protected void handleEnvAddButtonSelected() {
		final MultipleInputDialog dialog = new MultipleInputDialog(getShell(), Messages.EnvironmentTab_22);
		dialog.addTextField(Messages.EnvironmentTab_8, null, false);
		dialog.addVariablesField(Messages.EnvironmentTab_9, null, true);

		if (dialog.open() != Window.OK) {
			return;
		}

		final String name = dialog.getStringValue(Messages.EnvironmentTab_8);
		final String value = dialog.getStringValue(Messages.EnvironmentTab_9);

		if (name != null && value != null && name.length() > 0 && value.length() > 0) {
			// Trim the environment variable name but *NOT* the value
			addVariable(new EnvironmentVariable(name.trim(), value));
			//updateAppendReplace();
		}
	}

	protected boolean addVariable(final EnvironmentVariable variable) {
		final String name = variable.getName();
		final TableItem[] items = table.getTable().getItems();
		for (final TableItem item : items) {
			final EnvironmentVariable existingVariable = (EnvironmentVariable) item.getData();
			if (existingVariable.getName().equals(name)) {
				final boolean overWrite = MessageDialog.openQuestion(getShell(), Messages.EnvironmentTab_12, MessageFormat.format(Messages.EnvironmentTab_13, new Object[] { name })); //
				if (!overWrite) {
					return false;
				}
				table.getTableViewer().remove(existingVariable);
				break;
			}
		}
		table.getTableViewer().add(variable);
		return true;
	}

}
