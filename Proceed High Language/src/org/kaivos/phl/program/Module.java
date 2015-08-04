package org.kaivos.phl.program;

import java.util.Optional;

import org.kaivos.phl.program.util.Registry;

public class Module implements VariableScope, InterfaceScope {

	private Registry<Function, VariableScope> functions = new Registry<>(this, "function");
	private Registry<Interface, InterfaceScope> interfaces = new Registry<>(this, "interface");
	
	public void registerFunction(Function f) {
		functions.register(f);
	}
	
	public void registerInterface(Interface i) {
		interfaces.register(i);
	}
	
	public Optional<Function> resolveFunction(String name) {
		return functions.resolve(name);
	}
	
	public Optional<Interface> resolveInterface(String name) {
		return interfaces.resolve(name);
	}
	
}
