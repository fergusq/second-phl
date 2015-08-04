package org.kaivos.phl.program;

import java.util.Optional;

import org.kaivos.phl.program.util.NamedChild;
import org.kaivos.phl.program.util.Registry;

public class Interface implements NamedChild<InterfaceScope>, InterfaceScope {

	private String name;
	private InterfaceScope parent;
	private Registry<Interface, InterfaceScope> subinterfaces = new Registry<>(this, "interface");
	
	public Interface(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setParent(InterfaceScope p) {
		parent = p;
	}

	@Override
	public InterfaceScope getParent() {
		return parent;
	}

	public void registerInterface(Interface i) {
		subinterfaces.register(i);
	}
	
	@Override
	public Optional<Interface> resolveInterface(String name) {
		return subinterfaces.resolve(name);
	}
	
}
