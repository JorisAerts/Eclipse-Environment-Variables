package com.jorisaerts.eclipse.rcp.environment.table;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariable;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariables;

class TableContentProvider extends ArrayContentProvider {

	private final EnvironmentVariables map;
	//	private final KeyChangeListener keyChangeListener;
	//	private final ValueChangeListener valueChangeListener;

	public TableContentProvider(final EnvironmentVariables map) {
		this.map = map;
	}

	@Override public void dispose() {
	}

	@Override public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
	}

	@Override public Object[] getElements(final Object inputElement) {
		final Object[] elements = new Object[map.size()];
		int i = 0;
		for (final EnvironmentVariable entry : map) {
			final TableLine line = new TableLine(entry);
			elements[i++] = line;
		}
		return elements;
	}

	//	private final class KeyChangeListener implements PropertyChangeListener {
	//		@Override public void propertyChange(final PropertyChangeEvent evt) {
	//			final TableLine line = (TableLine) evt.getSource();
	//final String oldKey = (String) evt.getOldValue();
	//			final String newKey = (String) evt.getNewValue();
	//			final Object oldValue = map.get(oldKey);
	//			map.remove(oldKey);
	//			map.add(newKey, oldValue);
	//		}
	//	}
	//
	//	private final class ValueChangeListener implements PropertyChangeListener {
	//		@Override public void propertyChange(final PropertyChangeEvent evt) {
	//			final String newValue = (String) evt.getNewValue();
	//			final TableLine line = (TableLine) evt.getSource();
	//			map.put(line.getVariable(), newValue);
	//		}
	//	}

}