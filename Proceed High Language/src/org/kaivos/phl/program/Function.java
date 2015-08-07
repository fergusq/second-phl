package org.kaivos.phl.program;

import org.kaivos.phl.program.util.NamedChild;

public class Function implements NamedChild<VariableScope> {

	private String name;
	VariableScope parent;
	
	public Function(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setParent(VariableScope p) {
		parent = p;
	}

	@Override
	public VariableScope getParent() {
		return parent;
	}
	
	public void validate() {
		
	}
	
}
