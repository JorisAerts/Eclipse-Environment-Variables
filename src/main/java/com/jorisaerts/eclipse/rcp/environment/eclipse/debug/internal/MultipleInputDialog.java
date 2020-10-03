package com.jorisaerts.eclipse.rcp.environment.eclipse.debug.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.util.SWTUtils;

/**
 * original: org.eclipse.debug.internal.ui.MultipleInputDialog
 */
public class MultipleInputDialog extends Dialog {
	protected static final String FIELD_NAME = "FIELD_NAME"; //$NON-NLS-1$
	protected static final int TEXT = 100;
	protected static final int BROWSE = 101;
	protected static final int VARIABLE = 102;
	protected static final int MULTILINE_VARIABLE = 103;

	protected Composite panel;

	protected List<FieldSummary> fieldList = new ArrayList<>();
	protected List<Text> controlList = new ArrayList<>();
	protected List<Validator> validators = new ArrayList<>();
	protected Map<Object, String> valueMap = new HashMap<>();

	private final String title;

	public MultipleInputDialog(final Shell shell, final String title) {
		super(shell);
		this.title = title;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}

	}

	@Override
	protected Control createButtonBar(final Composite parent) {
		final Control bar = super.createButtonBar(parent);
		validateFields();
		return bar;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		panel = new Composite(container, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		panel.setLayout(layout);
		panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		for (final FieldSummary field : fieldList) {
			switch (field.type) {
			case TEXT:
				createTextField(field.name, field.initialValue, field.allowsEmpty);
				break;
			case BROWSE:
				createBrowseField(field.name, field.initialValue, field.allowsEmpty);
				break;
			case VARIABLE:
				createVariablesField(field.name, field.initialValue, field.allowsEmpty);
				break;
			case MULTILINE_VARIABLE:
				createMultilineVariablesField(field.name, field.initialValue, field.allowsEmpty);
				break;
			default:
				break;
			}
		}

		fieldList = null; // allow it to be gc'd
		Dialog.applyDialogFont(container);
		return container;
	}

	public void addBrowseField(final String labelText, final String initialValue, final boolean allowsEmpty) {
		fieldList.add(new FieldSummary(BROWSE, labelText, initialValue, allowsEmpty));
	}

	public void addTextField(final String labelText, final String initialValue, final boolean allowsEmpty) {
		fieldList.add(new FieldSummary(TEXT, labelText, initialValue, allowsEmpty));
	}

	public void addVariablesField(final String labelText, final String initialValue, final boolean allowsEmpty) {
		fieldList.add(new FieldSummary(VARIABLE, labelText, initialValue, allowsEmpty));
	}

	public void addMultilinedVariablesField(final String labelText, final String initialValue, final boolean allowsEmpty) {
		fieldList.add(new FieldSummary(MULTILINE_VARIABLE, labelText, initialValue, allowsEmpty));
	}

	protected void createTextField(final String labelText, final String initialValue, final boolean allowEmpty) {
		final Label label = new Label(panel, SWT.NONE);
		label.setText(labelText);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		final Text text = new Text(panel, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setData(FIELD_NAME, labelText);

		// make sure rows are the same height on both panels.
		label.setSize(label.getSize().x, text.getSize().y);

		if (initialValue != null) {
			text.setText(initialValue);
		}

		if (!allowEmpty) {
			validators.add(new Validator() {
				@Override
				public boolean validate() {
					return !text.getText().equals("");
				}
			});
			text.addModifyListener(e -> validateFields());
		}

		controlList.add(text);
	}

	protected void createBrowseField(final String labelText, final String initialValue, final boolean allowEmpty) {
		final Label label = new Label(panel, SWT.NONE);
		label.setText(labelText);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		final Composite comp = new Composite(panel, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		comp.setLayout(layout);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Text text = new Text(comp, SWT.SINGLE | SWT.BORDER);
		final GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;
		text.setLayoutData(data);
		text.setData(FIELD_NAME, labelText);

		// make sure rows are the same height on both panels.
		label.setSize(label.getSize().x, text.getSize().y);

		if (initialValue != null) {
			text.setText(initialValue);
		}

		if (!allowEmpty) {
			validators.add(new Validator() {
				@Override
				public boolean validate() {
					return !text.getText().equals("");
				}
			});

			text.addModifyListener(e -> validateFields());
		}

		final Button button = createButton(comp, IDialogConstants.IGNORE_ID, Messages.MultipleInputDialog_6, false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.SHEET);
				dialog.setMessage(Messages.MultipleInputDialog_7);
				final String currentWorkingDir = text.getText();
				if (!currentWorkingDir.trim().equals("")) {
					final File path = new File(currentWorkingDir);
					if (path.exists()) {
						dialog.setFilterPath(currentWorkingDir);
					}
				}

				final String selectedDirectory = dialog.open();
				if (selectedDirectory != null) {
					text.setText(selectedDirectory);
				}
			}
		});

		controlList.add(text);

	}

	public void createVariablesField(final String labelText, final String initialValue, final boolean allowEmpty) {
		final Label label = new Label(panel, SWT.NONE);
		label.setText(labelText);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		final Composite comp = new Composite(panel, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		comp.setLayout(layout);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Text text = new Text(comp, SWT.SINGLE | SWT.BORDER);
		final GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;
		text.setLayoutData(data);
		text.setData(FIELD_NAME, labelText);

		// make sure rows are the same height on both panels.
		label.setSize(label.getSize().x, text.getSize().y);

		if (initialValue != null) {
			text.setText(initialValue);
		}

		if (!allowEmpty) {
			validators.add(new Validator() {
				@Override
				public boolean validate() {
					return !text.getText().equals("");
				}
			});

			text.addModifyListener(e -> validateFields());
		}

		final Button button = createButton(comp, IDialogConstants.IGNORE_ID, Messages.MultipleInputDialog_8, false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
				final int code = dialog.open();
				if (code == IDialogConstants.OK_ID) {
					final String variable = dialog.getVariableExpression();
					if (variable != null) {
						text.insert(variable);
					}
				}
			}
		});

		controlList.add(text);

	}

	public void createMultilineVariablesField(final String labelText, final String initialValue, final boolean allowEmpty) {
		final Label label = new Label(panel, SWT.NONE);
		label.setText(labelText);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		gd.heightHint = 4 * text.getLineHeight();
		gd.horizontalSpan = 2;
		text.setLayoutData(gd);
		text.setData(FIELD_NAME, labelText);

		text.addTraverseListener(e -> {
			if (e.detail == SWT.TRAVERSE_RETURN && e.stateMask == SWT.SHIFT) {
				e.doit = true;
			}
		});

		// make sure rows are the same height on both panels.
		label.setSize(label.getSize().x, text.getSize().y);

		if (initialValue != null) {
			text.setText(initialValue);
		}

		if (!allowEmpty) {
			validators.add(new Validator() {
				@Override
				public boolean validate() {
					return !text.getText().equals("");
				}
			});

			text.addModifyListener(e -> validateFields());
		}
		final Composite comp = SWTUtils.createComposite(panel, panel.getFont(), 1, 2, GridData.HORIZONTAL_ALIGN_END);
		final GridLayout ld = (GridLayout) comp.getLayout();
		ld.marginHeight = 1;
		ld.marginWidth = 0;
		ld.horizontalSpacing = 0;
		final Button button = createButton(comp, IDialogConstants.IGNORE_ID, Messages.MultipleInputDialog_8, false);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
				final int code = dialog.open();
				if (code == IDialogConstants.OK_ID) {
					final String variable = dialog.getVariableExpression();
					if (variable != null) {
						text.insert(variable);
					}
				}
			}
		});

		controlList.add(text);
	}

	@Override
	protected void okPressed() {
		for (final Text control : controlList) {
			valueMap.put(control.getData(FIELD_NAME), control.getText());
		}
		controlList = null;
		super.okPressed();
	}

	@Override
	public int open() {
		applyDialogFont(panel);
		return super.open();
	}

	public Object getValue(final String key) {
		return valueMap.get(key);
	}

	public String getStringValue(final String key) {
		return (String) getValue(key);
	}

	public void validateFields() {
		for (final Validator validator : validators) {
			if (!validator.validate()) {
				getButton(IDialogConstants.OK_ID).setEnabled(false);
				return;
			}
		}
		getButton(IDialogConstants.OK_ID).setEnabled(true);
	}

	protected class FieldSummary {
		int type;
		String name;
		String initialValue;
		boolean allowsEmpty;

		public FieldSummary(final int type, final String name, final String initialValue, final boolean allowsEmpty) {
			this.type = type;
			this.name = name;
			this.initialValue = initialValue;
			this.allowsEmpty = allowsEmpty;
		}
	}

	protected class Validator {
		boolean validate() {
			return true;
		}
	}
}