package com.jorisaerts.eclipse.rcp.environment.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariableCollection;

public class TableButtons extends Composite {

	protected Button envAddButton;
	protected Button envSelectButton;
	protected Button envEditButton;
	protected Button envRemoveButton;
	protected Button envCopyButton;
	protected Button envPasteButton;

	public TableButtons(final EnvironmentVariablesTable table, final EnvironmentVariableCollection vars) {
		super(table, SWT.NONE);

		final Color background = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT);
		setBackground(background);

		final Font font = table.getFont();
		setFont(font);

		final GridData data = new GridData(GridData.FILL_VERTICAL);
		setLayoutData(data);

		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);

	}

}
