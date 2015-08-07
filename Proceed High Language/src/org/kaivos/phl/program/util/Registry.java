package org.kaivos.phl.program.util;

import java.util.Collection;
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
	
	public void register(T t) {
		if (registry.containsKey(t.getName()))
			throw new RegistrationException(typeOfObjects + " " + t.getName());
		
		t.setParent(parent);
		registry.put(t.getName(), t);
	}
	
	public Optional<T> resolve(String name) {
		return Optional.ofNullable(registry.get(name));
	}
	
	public Collection<T> toCollection() {
		return registry.values();
	}
}
