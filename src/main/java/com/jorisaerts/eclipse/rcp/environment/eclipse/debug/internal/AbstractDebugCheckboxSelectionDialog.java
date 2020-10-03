package com.jorisaerts.eclipse.rcp.environment.eclipse.debug.internal;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.util.SWTUtils;

/**
 * This class provides selection dialog using a check box table viewer.
 *
 * @since 3.4
 */
public abstract class AbstractDebugCheckboxSelectionDialog extends AbstractDebugSelectionDialog {

	/**
	 * Whether to add Select All / De-select All buttons to the custom footer controls.
	 */
	private boolean fShowSelectButtons = false;

	/**
	 * Constructor
	 * @param parentShell the parent shell
	 */
	public AbstractDebugCheckboxSelectionDialog(final Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	/**
	 * Returns the viewer cast to the correct instance.  Possibly <code>null</code> if
	 * the viewer has not been created yet.
	 * @return the viewer cast to CheckboxTableViewer
	 */
	protected CheckboxTableViewer getCheckBoxTableViewer() {
		return (CheckboxTableViewer) fViewer;
	}

	@Override
	protected void initializeControls() {
		final List<?> selectedElements = getInitialElementSelections();
		if (selectedElements != null && !selectedElements.isEmpty()) {
			getCheckBoxTableViewer().setCheckedElements(selectedElements.toArray());
			getCheckBoxTableViewer().setSelection(StructuredSelection.EMPTY);
		}
		super.initializeControls();
	}

	@Override
	protected StructuredViewer createViewer(final Composite parent) {
		//by default return a checkbox table viewer
		final Table table = new Table(parent, SWT.BORDER | SWT.SINGLE | SWT.CHECK);
		final GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 150;
		gd.widthHint = 250;
		table.setLayoutData(gd);
		return new CheckboxTableViewer(table);
	}

	@Override
	protected void addViewerListeners(final StructuredViewer viewer) {
		getCheckBoxTableViewer().addCheckStateListener(new DefaultCheckboxListener());
	}

	/**
	 * A checkbox state listener that ensures that exactly one element is checked
	 * and enables the OK button when this is the case.
	 *
	 */
	private class DefaultCheckboxListener implements ICheckStateListener {
		@Override
		public void checkStateChanged(final CheckStateChangedEvent event) {
			getButton(IDialogConstants.OK_ID).setEnabled(isValid());
		}
	}

	@Override
	protected boolean isValid() {
		return getCheckBoxTableViewer().getCheckedElements().length > 0;
	}

	@Override
	protected void okPressed() {
		final Object[] elements = getCheckBoxTableViewer().getCheckedElements();
		setResult(Arrays.asList(elements));
		super.okPressed();
	}

	@Override
	protected void addCustomFooterControls(final Composite parent) {
		if (fShowSelectButtons) {
			final Composite comp = SWTUtils.createComposite(parent, 2, 1, GridData.FILL_HORIZONTAL);
			final GridData gd = (GridData) comp.getLayoutData();
			gd.horizontalAlignment = SWT.END;
			Button button = SWTUtils.createPushButton(comp, Messages.AbstractDebugCheckboxSelectionDialog_0, null);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					getCheckBoxTableViewer().setAllChecked(true);
					getButton(IDialogConstants.OK_ID).setEnabled(isValid());
				}
			});
			button = SWTUtils.createPushButton(comp, Messages.AbstractDebugCheckboxSelectionDialog_1, null);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					getCheckBoxTableViewer().setAllChecked(false);
					getButton(IDialogConstants.OK_ID).setEnabled(isValid());
				}
			});
		}
	}

	/**
	 * If this setting is set to true before the dialog is opened, a Select All and
	 * a De-select All button will be added to the custom footer controls.  The default
	 * setting is false.
	 *
	 * @param setting whether to show the select all and de-select all buttons
	 */
	protected void setShowSelectAllButtons(final boolean setting) {
		fShowSelectButtons = setting;
	}

}