package org.kaivos.phl.program;

import java.util.Optional;

import org.kaivos.phl.program.reference.TypeReference;

public interface InterfaceInstance {
	public Interface getInterface();
	
	public Optional<FunctionInstance> resolveFunction(String name, TypeReference expectedReturnType, TypeReference ... argumentTypes);
}
