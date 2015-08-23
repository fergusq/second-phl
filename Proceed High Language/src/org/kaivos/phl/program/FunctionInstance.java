package org.kaivos.phl.program;

public interface FunctionInstance {
	public Function getFunction();
	public String getName();
	public VariableInstance[] getParameters();
	public InterfaceInstance getReturnType();
}
