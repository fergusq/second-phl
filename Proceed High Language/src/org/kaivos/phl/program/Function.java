package org.kaivos.phl.program;

public class Function {

	private String name;
	Scope parent;
	
	public Function(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
