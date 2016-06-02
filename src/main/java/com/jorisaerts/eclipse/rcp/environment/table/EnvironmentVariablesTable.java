package com.jorisaerts.eclipse.rcp.environment.table;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariables;

//The table to show the local secondary index info
public class EnvironmentVariablesTable extends Composite {

	private EnvironmentVariables vars;
	private final Table table;
	private final TableViewer viewer;
	private final TableLabelProvider labelProvider;

	public EnvironmentVariablesTable(final Composite parent) {
		super(parent, SWT.NONE);

		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		setLayout(tableColumnLayout);

		labelProvider = new TableLabelProvider();

		viewer = new TableViewer(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setColumnProperties(new String[] { getKeyName(), getValueName() });

		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		viewer.setLabelProvider(labelProvider);
		try {
			viewer.setContentProvider(new TableContentProvider(vars));
		} catch (final Exception e) {
			// noop
		}
		createColumns(tableColumnLayout, viewer.getTable());

		final CellEditor[] editors = new CellEditor[] { new TextCellEditor(table), new TextCellEditor(table) };

		// Assign the cell editors to the viewer
		viewer.setCellEditors(editors);
		viewer.setCellModifier(new TableCellModifier(viewer));
	}

	public void setVariables(final EnvironmentVariables vars) {
		this.vars = vars;
		viewer.setContentProvider(new TableContentProvider(vars));
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

	// Enforce call getElement method in contentProvider
	public void refresh() {
		viewer.setInput(new Object());
	}

	protected String getKeyName() {
		return "Variable";
	}

	protected String getValueName() {
		return "Value";
	}

	private void createColumns(final TableColumnLayout columnLayout, final Table table) {
		createColumn(table, columnLayout, getKeyName());
		createColumn(table, columnLayout, getValueName());
	}

	private TableColumn createColumn(final Table table, final TableColumnLayout columnLayout, final String text) {
		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText(text);
		column.setMoveable(true);
		columnLayout.setColumnData(column, new ColumnWeightData(50, true));
		return column;
	}
}