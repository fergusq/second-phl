package org.kaivos.phl.program;

import java.util.Optional;

public interface ModuleScope {
	public Optional<Module> resolveModule(String name);
}
