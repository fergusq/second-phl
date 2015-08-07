package org.kaivos.phl.program;

import java.util.Optional;

import org.kaivos.phl.util.Version;

public interface ModuleScope {
	public Optional<Module> resolveModule(String name);
	public Optional<Module> resolveModule(String name, Version version);
}
