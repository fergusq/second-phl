package org.kaivos.phl.program;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kaivos.phl.program.exception.RegistrationException;

public class Module implements Scope {

	private Map<String, Function> functions = new HashMap<>();
	
	public void registerFunction(Function f) {
		if (functions.containsKey(f.getName()))
			throw new RegistrationException("function " + f.getName());
		
		f.parent = this;
		functions.put(f.getName(), f);
	}
	
	public Optional<Function> resolveFunction(String name) {
		return Optional.ofNullable(functions.get(name));
	}
	
}
