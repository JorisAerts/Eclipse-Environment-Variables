package com.jorisaerts.eclipse.rcp.environment.eclipse.debug.internal;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Borrowed code...
 */
public class TextGetSetEditingSupport<T> extends EditingSupport {
	private final Function<T, String> getter;
	private final BiConsumer<T, String> setter;
	private final ColumnViewer viewer;

	public TextGetSetEditingSupport(final ColumnViewer viewer, final Function<T, String> getter, final BiConsumer<T, String> setter) {
		super(viewer);
		this.viewer = viewer;
		this.getter = getter;
		this.setter = setter;

	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
		return new TextCellEditor((Composite) getViewer().getControl());

	}

	@Override
	protected boolean canEdit(final Object element) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object getValue(final Object element) {
		return getter.apply((T) element);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(final Object element, final Object value) {
		setter.accept((T) element, (String) value);
		viewer.update(element, null);

	}

}
