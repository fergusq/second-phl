package org.kaivos.phl.program.reference;

import java.util.Optional;
import java.util.function.Supplier;

import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.ModuleScope;
import org.kaivos.phl.program.exception.ResolvationException;
import org.kaivos.phl.util.Version;

public class ModuleReference {
	
	private String name;
	private Optional<Version> version;
	private ModuleScope scope;
	
	public ModuleReference(String name, ModuleScope scope) {
		this.name = name;
		this.scope = scope;
		this.version = Optional.empty();
	}
	
	public ModuleReference(String name, Version version, ModuleScope scope) {
		this.name = name;
		this.version = Optional.of(version);
		this.scope = scope;
	}
	
	public Module getReferencedModule() {
		Supplier<ResolvationException> exception = () -> new ResolvationException("module " + name + " @ " + version.toString());
		
		if (version.isPresent())
			return scope.resolveModule(name, version.get())
					.orElseThrow(exception);
		
		return scope.resolveModule(name).orElseThrow(exception);
	}
}
