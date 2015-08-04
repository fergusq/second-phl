package org.kaivos.phl.program;

import java.util.Optional;

public interface InterfaceScope {
	public Optional<Interface> resolveInterface(String name);
}
