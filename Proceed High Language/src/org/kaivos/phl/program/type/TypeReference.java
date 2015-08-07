package org.kaivos.phl.program.type;

import org.kaivos.phl.program.Interface;
import org.kaivos.phl.program.InterfaceInstance;
import org.kaivos.phl.program.InterfaceScope;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.exception.ResolvationException;

import static org.kaivos.phl.util.Assert.nonEmpty;

public class TypeReference {

	private String[] referenceIdChain;
	private TypeReference[] typearguments;
	private Interface referencedInterface;
	private InterfaceScope environment;
	
	public TypeReference(String id, Module environment) {
		this(new String[] { id }, new TypeReference[0], environment);
	}
	
	public TypeReference(String id, TypeReference[] typearguments, Module environment) {
		this(new String[] { id }, typearguments, environment);
	}
	
	public TypeReference(String[] idChain, Module environment) {
		this(idChain, new TypeReference[0], environment);
	}
	
	public TypeReference(String[] idChain, TypeReference[] typearguments, Module environment) {
		nonEmpty(idChain);
		
		this.referenceIdChain = idChain;
		this.typearguments = typearguments;
		this.environment = environment;
	}
	
	public void validate() {
		if (referencedInterface == null)
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
	
	public Interface getReferencedInterface() {
		if (referencedInterface == null)
			resolveReference();
		
		return referencedInterface;
	}
	
	public InterfaceInstance getReferencedInterfaceInstance() {
		return getReferencedInterface().getInstance(typearguments);
	}
	
}
