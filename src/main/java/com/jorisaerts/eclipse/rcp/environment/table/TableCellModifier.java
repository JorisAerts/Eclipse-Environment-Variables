package com.jorisaerts.eclipse.rcp.environment.table;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariable;

class TableCellModifier implements ICellModifier {

	private final TableViewer tableViewer;
	private final String keyProperty;
	private final String valueProperty;

	public TableCellModifier(final TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		keyProperty = (String) tableViewer.getColumnProperties()[0];
		valueProperty = (String) tableViewer.getColumnProperties()[1];
	}

	@Override
	public boolean canModify(final Object element, final String property) {
		return true;
	}

	@Override
	public Object getValue(final Object element, final String property) {
		final EnvironmentVariable entry = (EnvironmentVariable) element;
		if (property.equals(keyProperty)) {
			return entry.getName();
		} else if (property.equals(valueProperty)) {
			return entry.getValue();
		}
		return element.toString();
	}

	@Override
	public void modify(final Object element, final String property, final Object value) {
		final TableItem item = (TableItem) element;
		final EnvironmentVariable variable = (EnvironmentVariable) item.getData();
		if (property.equals(keyProperty)) {
			variable.setName((String) value);
		} else if (property.equals(valueProperty)) {
			variable.setValue((String) value);
		}
		tableViewer.update(new Object[] { variable }, new String[] { property });
		tableViewer.refresh(variable);
	}
}