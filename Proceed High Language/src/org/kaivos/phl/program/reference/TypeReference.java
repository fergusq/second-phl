package org.kaivos.phl.program.reference;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kaivos.phl.program.Interface;
import org.kaivos.phl.program.InterfaceInstance;
import org.kaivos.phl.program.InterfaceScope;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.exception.ResolvationException;

import static org.kaivos.phl.util.Assert.nonEmpty;

public class TypeReference {
	
	private boolean typeparameter;
	
	private String[] referenceIdChain;
	private Map<String, TypeReference> typearguments;
	private Interface referencedInterface;
	private Module environment;
	
	public TypeReference(boolean isTypeparameter, String id, Module environment) {
		this(isTypeparameter, new String[] { id }, Collections.emptyMap(), environment);
	}
	
	public TypeReference(boolean isTypeparameter, String id, Map<String, TypeReference> typearguments, Module environment) {
		this(isTypeparameter, new String[] { id }, typearguments, environment);
	}
	
	public TypeReference(boolean isTypeparameter, String[] idChain, Module environment) {
		this(isTypeparameter, idChain, Collections.emptyMap(), environment);
	}
	
	public TypeReference(boolean isTypeparameter, String[] idChain, Map<String, TypeReference> typearguments, Module environment) {
		nonEmpty(idChain);
		
		if (isTypeparameter && (idChain.length != 1 || typearguments.size() > 0))
			throw new IllegalArgumentException();
		
		this.typeparameter = isTypeparameter;
		this.referenceIdChain = idChain;
		this.typearguments = typearguments;
		this.environment = environment;
	}
	
	public boolean isTypeparameter() {
		return typeparameter;
	}
	
	public void validate() {
		if (!isTypeparameter() && referencedInterface == null)
			resolveReference();
	}

	private void resolveReference() {
		InterfaceScope env = environment;
		for (int i = 0; i < referenceIdChain.length; i++) {
			int j = i;
			env = env.resolveInterface(referenceIdChain[i]).orElseThrow(() -> new ResolvationException("interface @" + referenceIdChain[j]));
		}
		referencedInterface = (Interface) env;
	}
	
	public Optional<Interface> getReferencedInterface() {
		if (isTypeparameter()) return Optional.empty();
		
		if (referencedInterface == null)
			resolveReference();
		
		return Optional.of(referencedInterface);
	}
	
	public InterfaceInstance getReferencedInterfaceInstance(TypeparameterSubstitutions substitutions) {
		if (isTypeparameter())
			return substitutions.substitute(referenceIdChain[0]).orElseThrow(() -> new ResolvationException("interface ?" + referenceIdChain[0])).getReferencedInterfaceInstanceWithoutSubstitutions();
		
		Map<String, TypeReference> ta = substituteTypearguments(substitutions);
		
		return getReferencedInterface().get().getInstance(ta);
	}
	
	private Map<String, TypeReference> substituteTypearguments(
			TypeparameterSubstitutions substitutions) {
		Map<String, TypeReference> tr = new HashMap<>();
		
		for (String key : tr.keySet()) {
			final TypeReference typeargument = typearguments.get(key);
			if (typeargument.isTypeparameter())
				tr.put(key, substitutions.substitute(typeargument.referenceIdChain[0]).orElseThrow(() -> new ResolvationException("interface ?" + typeargument.referenceIdChain[0])));
			else
				tr.put(key, new TypeReference(false, typeargument.referenceIdChain, typeargument.substituteTypearguments(substitutions), typeargument.environment));
		}
		
		return tr;
	}

	private InterfaceInstance getReferencedInterfaceInstanceWithoutSubstitutions() {
		return getReferencedInterface().get().getInstance(typearguments);
	}
	
}
