package com.jorisaerts.eclipse.rcp.environment.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SWTUtils {

	public static GridLayout createGrid(final int columns, final int marginwidth, final int marginheight) {
		final GridLayout layout = new GridLayout(columns, false);
		layout.marginWidth = marginwidth;
		layout.marginHeight = marginheight;
		return layout;
	}

	public static GridData createGridData(final int fill, final int hspan) {
		final GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
		return gd;
	}

	public static Composite createComposite(final Composite parent, final Font font, final int columns, final int hspan, final int fill, final int marginwidth, final int marginheight) {
		final Composite g = new Composite(parent, SWT.NONE);
		g.setLayout(createGrid(columns, marginwidth, marginheight));
		g.setFont(font);
		g.setLayoutData(createGridData(fill, hspan));
		return g;
	}

	public static Composite createComposite(final Composite parent, final int columns, final int hspan, final int fill) {
		final Composite g = new Composite(parent, SWT.NONE);
		g.setLayout(new GridLayout(columns, false));
		g.setFont(parent.getFont());
		g.setLayoutData(createGridData(fill, hspan));
		return g;
	}

	public static Label createLabel(final Composite parent, final String text, final int hspan) {
		final Label l = new Label(parent, SWT.NONE);
		l.setFont(parent.getFont());
		l.setText(text);

		final GridData gd = createGridData(GridData.FILL_HORIZONTAL, hspan);
		gd.grabExcessHorizontalSpace = false;
		l.setLayoutData(gd);
		return l;
	}

}
