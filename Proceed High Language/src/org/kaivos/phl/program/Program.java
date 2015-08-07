package org.kaivos.phl.program;

import java.util.Optional;

import org.kaivos.phl.program.util.Registry;

public class Program implements ModuleScope {

	private Registry<Module, ModuleScope> modules = new Registry<>(this, "module");
	
	public void registerModule(Module module) {
		modules.register(module);
	}
	
	@Override
	public Optional<Module> resolveModule(String name) {
		return modules.resolve(name);
	}
	
	public void validate() {
		modules.toCollection().forEach(Module::validate);
	}

}
