package org.kaivos.phl.program.reference;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class TypeparameterSubstitutions {
	private String[] parameters;
	private TypeReference[] arguments;
	
	public TypeparameterSubstitutions(Map<String, TypeReference> map) {
		if (map.isEmpty())
			throw new IllegalArgumentException();
		
		int size = map.keySet().size();
		String[] names = new String[size];
		TypeReference[] references = new TypeReference[size];
		
		int i = 0;
		for (String key : map.keySet()) {
			names[i] = key;
			references[i++] = map.get(key);
		}
		
		this.parameters = names;
		this.arguments = references;
	}
	
	public TypeparameterSubstitutions(String[] parameters, TypeReference[] arguments) {
		if (parameters.length != arguments.length)
			throw new IllegalArgumentException();
		
		this.parameters = parameters;
		this.arguments = arguments;
	}
	
	public Optional<TypeReference> substitute(String name) {
		for (int i = 0; i < parameters.length; i++)
			if (parameters[i].equals(name)) return Optional.of(arguments[i]);
		
		return Optional.empty();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TypeparameterSubstitutions)) return false;
		TypeparameterSubstitutions ts = (TypeparameterSubstitutions) obj;
		return Arrays.equals(parameters, ts.parameters) && Arrays.equals(arguments, ts.arguments);
	}
}
