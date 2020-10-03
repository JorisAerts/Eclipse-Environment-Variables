package com.jorisaerts.eclipse.rcp.environment.table;

import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariableCollection;
import com.jorisaerts.eclipse.rcp.environment.util.SWTUtils;

public class TableButtons extends Composite {

	protected Button envAddButton;
	protected Button envSelectButton;
	protected Button envEditButton;
	protected Button envRemoveButton;
	protected Button envCopyButton;
	protected Button envPasteButton;

	public TableButtons(final Composite parent, final EnvironmentVariableCollection vars, final EnvironmentVariablesTable table) {
		super(parent, SWT.NONE);

		final Color background = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT);
		setBackground(background);

		final Font font = parent.getFont();
		setFont(font);

		final GridData data = new GridData(GridData.FILL_VERTICAL);
		setLayoutData(data);

		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);

		// Create buttons
		envAddButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Add_4, null);
		envAddButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				table.handleEnvAddButtonSelected();
			}
		});
		envSelectButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_18, null);
		envSelectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				table.handleEnvSelectButtonSelected();
			}
		});
		envEditButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Edit_5, null);
		envEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				table.handleEnvEditButtonSelected();
			}
		});
		envEditButton.setEnabled(false);
		envRemoveButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Remove_6, null);
		envRemoveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				table.handleEnvRemoveButtonSelected();
			}
		});
		envRemoveButton.setEnabled(false);
		envCopyButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Copy, null);
		envCopyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				table.handleEnvCopyButtonSelected();
			}
		});
		envCopyButton.setEnabled(false);
		envPasteButton = SWTUtils.createPushButton(this, Messages.EnvironmentTab_Paste, null);
		envPasteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				table.handleEnvPasteButtonSelected();
			}
		});
		envPasteButton.setEnabled(true);

		table.getTableViewer().addSelectionChangedListener((final SelectionChangedEvent event) -> {
			final int size = event.getStructuredSelection().size();
			envEditButton.setEnabled(size == 1);
			envRemoveButton.setEnabled(size > 0);
			envCopyButton.setEnabled(size > 0);
		});

	}

}
