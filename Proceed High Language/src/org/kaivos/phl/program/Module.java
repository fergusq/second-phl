package org.kaivos.phl.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kaivos.phl.program.reference.ModuleReference;
import org.kaivos.phl.program.util.NamedChild;
import org.kaivos.phl.program.util.Registry;
import org.kaivos.phl.util.Version;

public class Module implements VariableScope, InterfaceScope, NamedChild<ModuleScope> {

	private Registry<Function, VariableScope> functions = new Registry<>(this, "function");
	private Registry<Interface, InterfaceScope> interfaces = new Registry<>(this, "interface");
	private ModuleScope parent;
	private String name;
	
	private List<ModuleReference> imports = new ArrayList<>(); 
	
	private Version version;
	
	public Module(String name) {
		this(name, Version.of(1, 0, 0));
	}
	
	public Module(String name, Version version) {
		this.name = name;
		this.version = version;
	}
	
	public void registerFunction(Function f) {
		functions.register(f);
	}
	
	public void registerInterface(Interface i) {
		interfaces.register(i);
	}
	
	public Optional<Function> resolveFunction(String name) {
		Optional<Function> f = functions.resolve(name);
		if (f.isPresent()) return f;
		for (ModuleReference imp : imports) {
			f = imp.getReferencedModule().resolveFunction(name);
			if (f.isPresent()) return f;
		}
		return Optional.<Function>empty();
	}
	
	public Optional<Interface> resolveInterface(String name) {
		Optional<Interface> i = interfaces.resolve(name);
		if (i.isPresent()) return i;
		for (ModuleReference imp : imports) {
			i = imp.getReferencedModule().resolveInterface(name);
			if (i.isPresent()) return i;
		}
		return Optional.<Interface>empty();
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
	
	public Version getVersion() {
		return version;
	}
	
	public void validate() {
		functions.toCollection().forEach(Function::validate);
		interfaces.toCollection().forEach(Interface::validate);
	}
	
}
