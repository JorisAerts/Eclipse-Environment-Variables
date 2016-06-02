package com.jorisaerts.eclipse.rcp.environment.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;

class TableContentProvider extends ArrayContentProvider {

	private final Map<Object, Object> map;
	private final KeyChangeListener keyChangeListener;
	private final ValueChangeListener valueChangeListener;

	@SuppressWarnings("unchecked") public TableContentProvider(final Map<?, ?> map) {
		this.map = (Map<Object, Object>) map;
		keyChangeListener = new KeyChangeListener();
		valueChangeListener = new ValueChangeListener();
	}

	@Override public void dispose() {
	}

	@Override public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
	}

	@Override public Object[] getElements(final Object inputElement) {
		final Object[] elements = new Object[map.size()];
		int i = 0;
		for (final Entry<?, ?> entry : map.entrySet()) {
			final TableLine line = new TableLine(entry);
			line.addKeyChangeListener(keyChangeListener);
			line.addValueChangeListener(valueChangeListener);
			elements[i++] = line;
		}
		return elements;
	}

	private final class KeyChangeListener implements PropertyChangeListener {
		@Override public void propertyChange(final PropertyChangeEvent evt) {
			final String oldKey = (String) evt.getOldValue();
			final String newKey = (String) evt.getNewValue();
			final Object oldValue = map.get(oldKey);
			map.remove(oldKey);
			map.put(newKey, oldValue);
		}
	}

	private final class ValueChangeListener implements PropertyChangeListener {
		@Override public void propertyChange(final PropertyChangeEvent evt) {
			final String newValue = (String) evt.getNewValue();
			final TableLine line = (TableLine) evt.getSource();
			map.put(line.getKey(), newValue);
		}
	}

}