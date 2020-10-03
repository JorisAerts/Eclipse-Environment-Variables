package com.jorisaerts.eclipse.rcp.environment.table;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jorisaerts.eclipse.rcp.environment.eclipse.debug.internal.MultipleInputDialog;
import com.jorisaerts.eclipse.rcp.environment.eclipse.debug.internal.TextGetSetEditingSupport;
import com.jorisaerts.eclipse.rcp.environment.preferences.NativeEnvironmentSelectionDialog;
import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariable;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariableCollection;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariablesUtil;

//The table to show the local secondary index info
public class EnvironmentVariablesTable extends Composite {

	private EnvironmentVariableCollection vars;
	private final Table table;
	private final TableViewer viewer;
	private final TableLabelProvider labelProvider;

	protected static final String P_VARIABLE = "variable";
	protected static final String P_VALUE = "value";

	public EnvironmentVariablesTable(final Composite parent) {
		super(parent, SWT.NONE);
		final Font font = parent.getFont();
		setFont(font);

		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		setLayout(layout);

		final Composite tableCompo = new Composite(this, SWT.NONE);
		final GridData data = new GridData(GridData.FILL_BOTH);
		tableCompo.setLayoutData(data);

		final GridLayout compoLayout = new GridLayout();
		compoLayout.marginHeight = 0;
		compoLayout.marginWidth = 0;
		tableCompo.setLayout(compoLayout);

		labelProvider = new TableLabelProvider();

		viewer = new TableViewer(tableCompo, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);

		table = viewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(font);

		viewer.setColumnProperties(new String[] { P_VARIABLE, P_VALUE });
		viewer.setLabelProvider(labelProvider);

		try {
			viewer.setContentProvider(new TableContentProvider(vars));
		} catch (final Exception e) {
			// noop
		}

		// Setup and create Columns
		final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(viewer) {
			@Override
			protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}

		};

		final int feature = ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION
				| ColumnViewerEditor.TABBING_CYCLE_IN_VIEWER;

		TableViewerEditor.create(viewer, actSupport, feature);

		// Setup environment variable name column
		final TableViewerColumn tcv1 = new TableViewerColumn(viewer, SWT.NONE, 0);
		tcv1.setLabelProvider(ColumnLabelProvider.createTextProvider(element -> ((EnvironmentVariable) element).getName()));

		final TableColumn tc1 = tcv1.getColumn();
		tc1.setText(Messages.EnvironmentTab_Variable_1);
		tcv1.setEditingSupport(new TextGetSetEditingSupport<>(tcv1.getViewer(), EnvironmentVariable::getName, (final EnvironmentVariable envVar, final String value) -> {
			// Trim environment variable names
			final String newName = value.trim();
			if (newName != null && !newName.isEmpty()) {
				if (!newName.equals(envVar.getName())) {
					if (canRenameVariable(newName)) {
						envVar.setName(newName);
						// updateAppendReplace();
						// updateLaunchConfigurationDialog();
					}
				}
			}
		}));

		// Setup environment variable value column
		final TableViewerColumn tcv2 = new TableViewerColumn(viewer, SWT.NONE, 1);
		tcv2.setLabelProvider(ColumnLabelProvider.createTextProvider(element -> ((EnvironmentVariable) element).getValue()));

		final TableColumn tc2 = tcv2.getColumn();
		tc2.setText(Messages.EnvironmentTab_Value_2);
		tcv2.setEditingSupport(new TextGetSetEditingSupport<>(tcv2.getViewer(), EnvironmentVariable::getValue, (envVar, value) -> {
			// Don't trim environment variable values
			envVar.setValue(value);
			// updateAppendReplace();
			// updateLaunchConfigurationDialog();
		}));

		// Create table column layout
		final TableColumnLayout tableColumnLayout = new TableColumnLayout(true);
		final PixelConverter pixelConverter = new PixelConverter(font);
		tableColumnLayout.setColumnData(tc1, new ColumnWeightData(1, pixelConverter.convertWidthInCharsToPixels(20)));
		tableColumnLayout.setColumnData(tc2, new ColumnWeightData(2, pixelConverter.convertWidthInCharsToPixels(20)));
		tableCompo.setLayout(tableColumnLayout);

		final CellEditor[] editors = new CellEditor[] { new TextCellEditor(table), new TextCellEditor(table) };

		// Assign the cell editors to the viewer
		viewer.setCellEditors(editors);
		viewer.setCellModifier(new TableCellModifier(viewer));
	}

	public void setVariables(final EnvironmentVariableCollection vars) {
		this.vars = vars;
		viewer.setContentProvider(new TableContentProvider(vars));
		refresh();
	}

	/**
	 * Returns whether the environment variable can be renamed to the given variable name. If the name is already used for another variable, the user decides with a dialog whether to overwrite the existing variable
	 *
	 * @param newVariableName the chosen name to give to the variable
	 * @return whether the new name should be used or not
	 */
	private boolean canRenameVariable(final String newVariableName) {
		for (final TableItem item : viewer.getTable().getItems()) {
			final EnvironmentVariable existingVariable = (EnvironmentVariable) item.getData();
			if (existingVariable.getName().equals(newVariableName)) {

				final boolean overWrite = MessageDialog.openQuestion(getShell(), Messages.EnvironmentTab_12, MessageFormat.format(Messages.EnvironmentTab_13, new Object[] { newVariableName }));
				if (!overWrite) {
					return false;
				}
				viewer.remove(existingVariable);
				return true;
			}
		}
		return true;
	}

	/**
	 * Adds a new environment variable to the table.
	 */
	public void handleEnvAddButtonSelected() {
		final MultipleInputDialog dialog = new MultipleInputDialog(getShell(), Messages.EnvironmentTab_22);
		dialog.addTextField(Messages.EnvironmentTab_8, null, false);
		dialog.addVariablesField(Messages.EnvironmentTab_9, null, true);

		if (dialog.open() != Window.OK) {
			return;
		}

		final String name = dialog.getStringValue(Messages.EnvironmentTab_8);
		final String value = dialog.getStringValue(Messages.EnvironmentTab_9);

		if (name != null && value != null && name.length() > 0) {
			// Trim the environment variable name but *NOT* the value
			addVariable(new EnvironmentVariable(name.trim(), value));
			//updateAppendReplace();
		}
	}

	/**
	 * Displays a dialog that allows user to select native environment variables to add to the table.
	 */
	public void handleEnvSelectButtonSelected() {
		// get Environment Variables from the OS
		final Map<String, EnvironmentVariable> envVariables = EnvironmentVariablesUtil.getNativeEnvironment();

		// get Environment Variables from the table
		final TableItem[] items = viewer.getTable().getItems();
		for (final TableItem item : items) {
			final EnvironmentVariable var = (EnvironmentVariable) item.getData();
			envVariables.remove(var.getName());
		}

		final NativeEnvironmentSelectionDialog dialog = new NativeEnvironmentSelectionDialog(getShell(), envVariables);
		dialog.setTitle(Messages.EnvironmentTab_20);

		final int button = dialog.open();
		if (button == Window.OK) {
			final Object[] selected = dialog.getResult();
			for (final Object o : selected) {
				viewer.add(o);
			}
		}

		// updateAppendReplace();
		// updateLaunchConfigurationDialog();
	}

	/**
	 * Creates an editor for the value of the selected environment variable.
	 */
	public void handleEnvEditButtonSelected() {
		final IStructuredSelection sel = viewer.getStructuredSelection();
		final EnvironmentVariable var = (EnvironmentVariable) sel.getFirstElement();
		if (var == null) {
			return;
		}
		final String originalName = var.getName();
		String value = var.getValue();
		final MultipleInputDialog dialog = new MultipleInputDialog(getShell(), Messages.EnvironmentTab_11);
		dialog.addTextField(Messages.EnvironmentTab_8, originalName, false);
		if (value != null && value.contains(System.lineSeparator())) {
			dialog.addMultilinedVariablesField(Messages.EnvironmentTab_9, value, true);
		} else {
			dialog.addVariablesField(Messages.EnvironmentTab_9, value, true);
		}

		if (dialog.open() != Window.OK) {
			return;
		}

		final String name = dialog.getStringValue(Messages.EnvironmentTab_8);
		value = dialog.getStringValue(Messages.EnvironmentTab_9);
		if (!originalName.equals(name)) {
			// Trim the environment variable name but *NOT* the value
			if (addVariable(new EnvironmentVariable(name.trim(), value))) {
				viewer.remove(var);
			}
		} else {
			var.setValue(value);
			viewer.update(var, null);
			// updateLaunchConfigurationDialog();
		}
	}

	/**
	 * Removes the selected environment variable from the table.
	 */
	public void handleEnvRemoveButtonSelected() {
		final IStructuredSelection sel = viewer.getStructuredSelection();
		try {
			viewer.getControl().setRedraw(false);
			for (final Iterator<?> i = sel.iterator(); i.hasNext();) {
				final EnvironmentVariable var = (EnvironmentVariable) i.next();
				viewer.remove(var);
			}
		} finally {
			viewer.getControl().setRedraw(true);
			viewer.getControl().redraw();
		}
		// updateAppendReplace();
		// updateLaunchConfigurationDialog();
	}

	/**
	 * Copy the currently selected table entries to the clipboard.
	 */
	public void handleEnvCopyButtonSelected() {
		@SuppressWarnings("unchecked")
		final Iterable<?> iterable = () -> viewer.getStructuredSelection().iterator();
		final String data = StreamSupport
				.stream(iterable.spliterator(), false)
				.filter(o -> o instanceof EnvironmentVariable)
				.map(EnvironmentVariable.class::cast)
				.map(var -> String.format("%s=%s", var.getName(), var.getValue())) //$NON-NLS-1$
				.collect(Collectors.joining(System.lineSeparator()));

		final Clipboard clipboard = new Clipboard(getShell().getDisplay());
		try {
			clipboard.setContents(new Object[] { data }, new Transfer[] { TextTransfer.getInstance() });
		} finally {
			clipboard.dispose();
		}
	}

	/**
	 * Extract the content from the clipboard and add the new content.
	 */
	public void handleEnvPasteButtonSelected() {
		final Clipboard clipboard = new Clipboard(getShell().getDisplay());
		try {
			final List<EnvironmentVariable> variables = convertEnvironmentVariablesFromData(clipboard.getContents(TextTransfer.getInstance()));
			addVariables(variables);
			//updateAppendReplace();
		} finally {
			clipboard.dispose();
		}
	}

	/**
	 * Convert the clipboard data to a list of {@link EnvironmentVariable}s. <br>
	 * Only entries containing an equals sign ({@code =} will be considered.
	 *
	 * @param data The clipboard data. May be {@code null}, which will result in an empty list.
	 * @return The resulting and valid {@link EnvironmentVariable}s in an unmodifiable list.
	 */
	private static List<EnvironmentVariable> convertEnvironmentVariablesFromData(final Object data) {
		if (!(data instanceof String)) {
			return Collections.emptyList();
		}

		final String entries[] = ((String) data).split("\\R"); //$NON-NLS-1$
		final List<EnvironmentVariable> result = new ArrayList<>(entries.length);
		for (final String entry : entries) {
			final int idx = entry.indexOf('=');
			if (idx < 1) {
				continue;
			}
			// the name is trimmed ...
			final String name = entry.substring(0, idx).trim();
			// .. but the value is *not* trimmed
			final String value = entry.substring(idx + 1);
			result.add(new EnvironmentVariable(name, value));
		}
		return Collections.unmodifiableList(result);
	}

	protected boolean addVariable(final EnvironmentVariable variable) {
		final String name = variable.getName();
		final TableItem[] items = table.getItems();
		for (final TableItem item : items) {
			final EnvironmentVariable existingVariable = (EnvironmentVariable) item.getData();
			if (existingVariable.getName().equals(name)) {
				final boolean overWrite = MessageDialog.openQuestion(getShell(), Messages.EnvironmentTab_12, MessageFormat.format(Messages.EnvironmentTab_13, new Object[] { name })); //
				if (!overWrite) {
					return false;
				}
				viewer.remove(existingVariable);
				break;
			}
		}
		viewer.add(variable);
		return true;
	}

	protected int addVariables(final List<EnvironmentVariable> variables) {
		if (variables.isEmpty()) {
			return 0;
		}

		final List<EnvironmentVariable> remove = new LinkedList<>();
		final List<EnvironmentVariable> conflicting = new LinkedList<>();
		final Map<String, String> requested = variables.stream().collect(Collectors.toMap(EnvironmentVariable::getName, EnvironmentVariable::getValue));

		for (final TableItem item : viewer.getTable().getItems()) {
			final EnvironmentVariable existingVariable = (EnvironmentVariable) item.getData();
			final String name = existingVariable.getName();
			final String currentValue = requested.get(name);
			if (currentValue != null) {
				remove.add(existingVariable);
				if (!currentValue.equals(existingVariable.getValue())) {
					conflicting.add(existingVariable);
				}
			}
		}
		if (!conflicting.isEmpty()) {
			final String names = conflicting
					.stream()
					.map(EnvironmentVariable::getName)
					.collect(Collectors.joining(", ")); //$NON-NLS-1$
			final boolean overWrite = MessageDialog.openQuestion(getShell(), Messages.EnvironmentTab_Paste_Overwrite_Title, MessageFormat.format(Messages.EnvironmentTab_Paste_Overwrite_Message, new Object[] { names })); //
			if (!overWrite) {
				return 0;
			}
		}

		remove.forEach(viewer::remove);
		variables.forEach(viewer::add);
		// updateLaunchConfigurationDialog();

		return variables.size();
	}

	public Table getTable() {
		return table;
	}

	public TableViewer getTableViewer() {
		return viewer;
	}

	// Enforce call getElement method in contentProvider
	public void refresh() {
		viewer.setInput(new Object());
	}
}