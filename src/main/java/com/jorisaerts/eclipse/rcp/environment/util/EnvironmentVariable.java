package com.jorisaerts.eclipse.rcp.environment.util;

import java.io.Serializable;

public class EnvironmentVariable implements Serializable {

	private static final long serialVersionUID = 6623486306609548545L;

	private String variable;
	private String value;

	public EnvironmentVariable(final String variable, final String value) {
		this.variable = variable;
		this.value = value;
	}

	public final String getVariable() {
		return variable;
	}

	public final void setVariable(final String variable) {
		this.variable = variable;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(final String value) {
		this.value = value;
	}

}
