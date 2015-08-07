package org.kaivos.phl.program;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.kaivos.phl.program.reference.TypeReference;
import org.kaivos.phl.program.util.NamedChild;
import org.kaivos.phl.program.util.Registry;

public class Interface implements NamedChild<String, InterfaceScope>, InterfaceScope {

	private String name;
	private InterfaceScope parent;
	private Registry<String, Interface, InterfaceScope> subinterfaces = new Registry<>(this, "interface");
	
	private Set<InterfaceInstance> knownInstances = new HashSet<>();
	
	public Interface(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		// There should not be duplicate interfaces
		return this == obj;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String getSignature() {
		return name;
	}

	@Override
	public void setParent(InterfaceScope p) {
		parent = p;
	}

	@Override
	public InterfaceScope getParent() {
		return parent;
	}

	public void registerInterface(Interface i) {
		subinterfaces.register(i);
	}
	
	@Override
	public Optional<Interface> resolveInterface(String name) {
		Optional<Interface> i = subinterfaces.resolve(name);
		return i.isPresent() ? i : parent.resolveInterface(name);
	}
	
	public void validate() {
		
	}
	
	public InterfaceInstance getInstance(TypeReference... typearguments) {
		InterfaceInstance ii = new InterfaceInstanceImpl(typearguments);
		knownInstances.add(ii);
		return ii;
	}
	
	private class InterfaceInstanceImpl implements InterfaceInstance {
		private TypeReference[] typearguments;
		
		public InterfaceInstanceImpl(TypeReference[] typearguments) {
			this.typearguments = typearguments;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof InterfaceInstanceImpl)) return false;
			InterfaceInstanceImpl iii = (InterfaceInstanceImpl) obj;
			
			if (typearguments.length != iii.typearguments.length) return false;
			for (int i = 0; i < typearguments.length; i++) {
				if (!typearguments[i].equals(iii.typearguments[i])) return false;
			}
			
			return iii.getInterface().equals(getInterface());
		}
		
		private Interface getInterface() {
			return Interface.this;
		}
	}
	
}
