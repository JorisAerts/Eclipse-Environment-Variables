package com.jorisaerts.eclipse.rcp.environment.table;

import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jorisaerts.eclipse.rcp.environment.eclipse.debug.internal.TextGetSetEditingSupport;
import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariable;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariableCollection;
import com.jorisaerts.eclipse.rcp.environment.util.SWTUtils;

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
		setLayout(SWTUtils.createGrid(1, 0, 0));
		setLayoutData(SWTUtils.createGridData(GridData.FILL_BOTH, 1));

		// ClassCastException?
		//SWTUtils.createLabel(this, Messages.EnvironmentTab_Environment_variables_to_set__3, 2);

		labelProvider = new TableLabelProvider();

		viewer = new TableViewer(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);

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
		tcv1.setLabelProvider(ColumnLabelProvider.createTextProvider(element -> ((TableLine) element).getVariable()));

		final TableColumn tc1 = tcv1.getColumn();
		tc1.setText(Messages.EnvironmentTab_Variable_1);
		tcv1.setEditingSupport(new TextGetSetEditingSupport<>(tcv1.getViewer(), EnvironmentVariable::getName, (final EnvironmentVariable envVar, final String value) -> {
			// Trim environment variable names
			final String newName = value.trim();
			if (newName != null && !newName.isEmpty()) {
				if (!newName.equals(envVar.getName())) {
					//					if (canRenameVariable(newName)) {
					//						envVar.setName(newName);
					//						updateAppendReplace();
					//						updateLaunchConfigurationDialog();
					//					}
				}
			}
		}));

		// Setup environment variable value column
		final TableViewerColumn tcv2 = new TableViewerColumn(viewer, SWT.NONE, 1);
		tcv2.setLabelProvider(ColumnLabelProvider.createTextProvider(element -> ((TableLine) element).getValue()));

		final TableColumn tc2 = tcv2.getColumn();
		tc2.setText(Messages.EnvironmentTab_Value_2);
		tcv2.setEditingSupport(new TextGetSetEditingSupport<>(tcv2.getViewer(), EnvironmentVariable::getValue, (envVar, value) -> {
			// Don't trim environment variable values
			envVar.setValue(value);
			//			updateAppendReplace();
			//			updateLaunchConfigurationDialog();
		}));

		// Create table column layout
		final TableColumnLayout tableColumnLayout = new TableColumnLayout(true);
		final PixelConverter pixelConverter = new PixelConverter(font);
		tableColumnLayout.setColumnData(tc1, new ColumnWeightData(1, pixelConverter.convertWidthInCharsToPixels(20)));
		tableColumnLayout.setColumnData(tc2, new ColumnWeightData(2, pixelConverter.convertWidthInCharsToPixels(20)));
		this.setLayout(tableColumnLayout);

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

	public boolean removeSelected() {
		final int sel = table.getSelectionIndex();
		if (sel < 0) {
			return false;
		}

		final TableItem item = table.getItem(sel);
		final TableLine line = (TableLine) item.getData();
		vars.remove(line.getEntry());

		refresh();
		return true;
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