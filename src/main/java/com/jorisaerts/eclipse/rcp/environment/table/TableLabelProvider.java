package com.jorisaerts.eclipse.rcp.environment.table;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

class TableLabelProvider implements ITableLabelProvider {
	@Override public void addListener(final ILabelProviderListener listener) {
	}

	@Override public void removeListener(final ILabelProviderListener listener) {
	}

	@Override public void dispose() {
	}

	@Override public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	@Override public Image getColumnImage(final Object element, final int columnIndex) {
		return null;
	}

	@Override public String getColumnText(final Object element, final int columnIndex) {
		final TableLine entry = (TableLine) element;
		switch (columnIndex) {
		case 0:
			return entry.getKey().toString();
		case 1:
			return entry.getValue().toString();
		}
		return element.toString();
	}
}