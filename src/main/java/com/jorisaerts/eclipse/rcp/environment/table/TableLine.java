package com.jorisaerts.eclipse.rcp.environment.table;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariable;

class TableLine {

	private final EnvironmentVariable entry;
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public TableLine(final EnvironmentVariable entry) {
		this.entry = entry;
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

	public final String getVariable() {
		return entry.getName();
	}

	public final void setVariable(final String key) {
		final String oldValue = entry.getName();
		entry.setName(key);
		propertyChangeSupport.firePropertyChange("key", oldValue, entry.getName());
	}

	public final String getValue() {
		return entry.getValue();
	}

	public final void setValue(final String value) {
		final String oldValue = entry.getValue();
		entry.setValue(value);
		propertyChangeSupport.firePropertyChange("value", oldValue, entry.getValue());
	}

	@Override
	public String toString() {
		return "TableLine[" + getVariable() + "=" + getValue() + "]";
	}

	public EnvironmentVariable getEntry() {
		return entry;
	}

}
