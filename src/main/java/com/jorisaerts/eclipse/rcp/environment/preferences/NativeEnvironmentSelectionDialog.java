package com.jorisaerts.eclipse.rcp.environment.preferences;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.jorisaerts.eclipse.rcp.environment.eclipse.debug.internal.AbstractDebugCheckboxSelectionDialog;
import com.jorisaerts.eclipse.rcp.environment.preferences.internal.Messages;
import com.jorisaerts.eclipse.rcp.environment.util.EnvironmentVariable;

/**
 * This dialog allows users to select one or more known native environment variables from a list.
 */
public class NativeEnvironmentSelectionDialog extends AbstractDebugCheckboxSelectionDialog {

	private final Object fInput;

	public NativeEnvironmentSelectionDialog(final Shell parentShell, final Object input) {
		super(parentShell);
		fInput = input;
		setShellStyle(getShellStyle() | SWT.RESIZE);
		setShowSelectAllButtons(true);
	}

	@Override
	protected String getDialogSettingsId() {
		return IDebugUIConstants.PLUGIN_ID + ".ENVIRONMENT_TAB.NATIVE_ENVIROMENT_DIALOG"; //$NON-NLS-1$
	}

	@Override
	protected String getHelpContextId() {
		return null;
	}

	@Override
	protected Object getViewerInput() {
		return fInput;
	}

	@Override
	protected String getViewerLabel() {
		return Messages.EnvironmentTab_19;
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new ILabelProvider() {
			@Override
			public Image getImage(final Object element) {
				//return DebugPluginImages.getImage(IDebugUIConstants.IMG_OBJS_ENVIRONMENT);
				return null;
			}

			@Override
			public String getText(final Object element) {
				final EnvironmentVariable var = (EnvironmentVariable) element;
				return MessageFormat.format(Messages.EnvironmentTab_7, new Object[] { var.getName(), var.getValue() });
			}

			@Override
			public void addListener(final ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(final Object element, final String property) {
				return false;
			}

			@Override
			public void removeListener(final ILabelProviderListener listener) {
			}
		};
	}

	@Override
	protected IContentProvider getContentProvider() {
		return new IStructuredContentProvider() {
			@Override
			public Object[] getElements(final Object inputElement) {
				EnvironmentVariable[] elements = null;
				if (inputElement instanceof HashMap) {
					final Comparator<Object> comparator = (o1, o2) -> {
						final String s1 = (String) o1;
						final String s2 = (String) o2;
						return s1.compareTo(s2);
					};
					final TreeMap<Object, Object> envVars = new TreeMap<>(comparator);
					envVars.putAll((Map<?, ?>) inputElement);
					elements = new EnvironmentVariable[envVars.size()];
					int index = 0;
					for (final Iterator<Object> iterator = envVars.keySet().iterator(); iterator.hasNext(); index++) {
						final Object key = iterator.next();
						elements[index] = (EnvironmentVariable) envVars.get(key);
					}
				}
				return elements;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			}
		};
	}
}