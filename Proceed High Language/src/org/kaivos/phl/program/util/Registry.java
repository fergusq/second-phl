package org.kaivos.phl.program.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.kaivos.phl.program.exception.RegistrationException;

public class Registry<K, T extends NamedChild<K, U>, U> {
	private Map<K, T> registry = new HashMap<>();
	private U parent;
	private String typeOfObjects;
	
	public Registry(U parent, String typeOfObjects) {
		this.parent = parent;
		this.typeOfObjects = typeOfObjects;
	}
	
	public void register(T t) {
		if (registry.containsKey(t.getSignature()))
			throw new RegistrationException(typeOfObjects + " " + t.getSignature());
		
		t.setParent(parent);
		registry.put(t.getSignature(), t);
	}
	
	public Optional<T> resolve(K name) {
		return Optional.ofNullable(registry.get(name));
	}
	
	public List<T> filter(Predicate<? super T> condition) {
		return registry.values().stream().filter(condition).collect(Collectors.toList());
	}
	
	public Collection<T> toCollection() {
		return registry.values();
	}
}
