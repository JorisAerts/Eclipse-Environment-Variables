package com.jorisaerts.eclipse.rcp.environment.table;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map.Entry;

class TableLine {

	private Object key;
	private Object value;
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public TableLine(final Entry<?, ?> entry) {
		setKey(entry.getKey());
		setValue(entry.getValue());
	}

	public void addKeyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener("key", listener);
	}

	public void addValueChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener("value", listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public final Object getKey() {
		return key;
	}

	public final void setKey(final Object key) {
		propertyChangeSupport.firePropertyChange("key", this.key, this.key = key);
	}

	public final Object getValue() {
		return value;
	}

	public final void setValue(final Object value) {
		propertyChangeSupport.firePropertyChange("value", this.value, this.value = value);
	}

	@Override public String toString() {
		return "TableLine[" + getKey() + "=" + getValue() + "]";
	}

}
