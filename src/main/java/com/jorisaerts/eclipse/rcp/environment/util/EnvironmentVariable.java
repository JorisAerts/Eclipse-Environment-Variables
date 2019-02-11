package com.jorisaerts.eclipse.rcp.environment.util;

import java.io.Serializable;

public class EnvironmentVariable implements Serializable {

	private static final long serialVersionUID = 6623486306609548545L;

	private String name;
	private String value;

	public EnvironmentVariable(final String variable, final String value) {
		this.name = variable;
		this.value = value;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(final String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		boolean equal = false;
		if (obj instanceof EnvironmentVariable) {
			final EnvironmentVariable var = (EnvironmentVariable) obj;
			equal = var.getName().equals(name);
		}
		return equal;
	}

	@Override
	public String toString() {
		return getName();
	}

}
