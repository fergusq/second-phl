package org.kaivos.phl.program.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kaivos.phl.program.exception.RegistrationException;

public class Registry<T extends NamedChild<U>, U> {
	private Map<String, T> registry = new HashMap<>();
	private U parent;
	private String typeOfObjects;
	
	public Registry(U parent, String typeOfObjects) {
		this.parent = parent;
		this.typeOfObjects = typeOfObjects;
	}
	
	public void register(T f) {
		if (registry.containsKey(f.getName()))
			throw new RegistrationException(typeOfObjects + " " + f.getName());
		
		f.setParent(parent);
		registry.put(f.getName(), f);
	}
	
	public Optional<T> resolve(String name) {
		return Optional.ofNullable(registry.get(name));
	}
}
