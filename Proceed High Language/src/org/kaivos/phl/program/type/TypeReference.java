package org.kaivos.phl.program.type;

import org.kaivos.phl.program.Interface;
import org.kaivos.phl.program.InterfaceScope;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.exception.ResolvationException;

public class TypeReference {

	private String[] referenceIdChain;
	private Interface referencedInterface;
	private InterfaceScope environment;
	
	public TypeReference(String id, Module environment) {
		this(new String[] { id }, environment);
	}
	
	public TypeReference(String[] idChain, Module environment) {
		if (idChain.length == 0) throw new IllegalArgumentException("idChain should be non null!");
		
		this.referenceIdChain = idChain;
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
	
}
