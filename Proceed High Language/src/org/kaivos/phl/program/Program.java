package org.kaivos.phl.program;

import java.util.List;
import java.util.Optional;

import org.kaivos.phl.program.util.Registry;
import org.kaivos.phl.util.Version;

public class Program implements ModuleScope {

	private Registry<Module.Signature, Module, ModuleScope> modules = new Registry<>(this, "module");
	
	public void registerModule(Module module) {
		modules.register(module);
	}
	
	@Override
	public Optional<Module> resolveModule(String name) {
		List<Module> alternatives = modules.filter((module) -> module.getSignature().getName().equals(name));
		alternatives.sort((module1, module2) -> module1.getVersion().compareTo(module2.getVersion()));
		return alternatives.isEmpty() ? Optional.empty() : Optional.of(alternatives.get(alternatives.size()-1));
	}
	
	@Override
	public Optional<Module> resolveModule(String name, Version version) {
		return modules.resolve(new Module.SignatureImpl(name, version));
	}
	
	public void validate() {
		modules.toCollection().forEach(Module::validate);
	}

}
