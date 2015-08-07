package org.kaivos.phl.program;

import java.util.Optional;

import org.kaivos.phl.program.util.NamedChild;
import org.kaivos.phl.program.util.Registry;

public class Module implements VariableScope, InterfaceScope, NamedChild<ModuleScope> {

	private Registry<Function, VariableScope> functions = new Registry<>(this, "function");
	private Registry<Interface, InterfaceScope> interfaces = new Registry<>(this, "interface");
	private ModuleScope parent;
	private String name;
	
	public Module(String name) {
		this.name = name;
	}
	
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

	@Override
	public void setParent(ModuleScope parentScope) {
		parent = parentScope;
	}

	@Override
	public ModuleScope getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void validate() {
		functions.toCollection().forEach(Function::validate);
		interfaces.toCollection().forEach(Interface::validate);
	}
	
}
