package com.jorisaerts.eclipse.rcp.environment.util;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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

	public static Composite createComposite(final Composite parent, final Font font, final int columns, final int hspan, final int fill) {
		final Composite g = new Composite(parent, SWT.NONE);
		g.setLayout(new GridLayout(columns, false));
		g.setFont(font);
		final GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
		g.setLayoutData(gd);
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

	public static Label createWrapLabel(final Composite parent, final String text, final int hspan) {
		final Label l = new Label(parent, SWT.NONE | SWT.WRAP);
		l.setFont(parent.getFont());
		l.setText(text);
		final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = hspan;
		l.setLayoutData(gd);
		return l;
	}

	public static Button createPushButton(final Composite parent, final String label, final Image image) {
		final Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		if (image != null) {
			button.setImage(image);
		}
		if (label != null) {
			button.setText(label);
		}
		final GridData gd = new GridData();
		button.setLayoutData(gd);
		setButtonDimensionHint(button);
		return button;
	}

	public static int getButtonWidthHint(final Button button) {
		/*button.setFont(JFaceResources.getDialogFont());*/
		final PixelConverter converter = new PixelConverter(button);
		final int widthHint = converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}

	public static void setButtonDimensionHint(final Button button) {
		Assert.isNotNull(button);
		final Object gd = button.getLayoutData();
		if (gd instanceof GridData) {
			((GridData) gd).widthHint = getButtonWidthHint(button);
			((GridData) gd).horizontalAlignment = GridData.FILL;
		}
	}

}
