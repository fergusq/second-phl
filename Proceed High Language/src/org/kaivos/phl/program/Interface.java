package org.kaivos.phl.program;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.kaivos.phl.program.exception.ResolvationException;
import org.kaivos.phl.program.reference.TypeReference;
import org.kaivos.phl.program.util.NamedChild;
import org.kaivos.phl.program.util.Registry;

public class Interface implements NamedChild<String, InterfaceScope>, InterfaceScope, VariableScope {

	private String name;
	private InterfaceScope parent;
	private Registry<String, Interface, InterfaceScope> subinterfaces = new Registry<>(this, "interface");
	
	private Set<InterfaceInstance> knownInstances = new HashSet<>();
	
	private Typeparameter[] typeparameters;
	
	private Registry<Function.Signature, Function, VariableScope> functions = new Registry<>(this, "method");
	
	public Interface(String name, Typeparameter... typeparameters) {
		this.name = name;
		this.typeparameters = typeparameters;
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
	
	public Typeparameter[] getTypeparameters() {
		return typeparameters;
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

	public void registerFunction(Function f) {
		functions.register(f);
	}
	
	@Override
	public Optional<Interface> resolveInterface(String name) {
		Optional<Interface> i = subinterfaces.resolve(name);
		return i.isPresent() ? i : parent.resolveInterface(name);
	}
	
	public void validate() {
		
	}
	
	public InterfaceInstance getInstance(Map<String, TypeReference> typearguments) {
		InterfaceInstance ii = new InterfaceInstanceImpl(typearguments);
		knownInstances.add(ii);
		return ii;
	}
	
	private class InterfaceInstanceImpl implements InterfaceInstance {
		private Map<String, TypeReference> typearguments;
		
		public InterfaceInstanceImpl(Map<String, TypeReference> typearguments) {
			this.typearguments = typearguments;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof InterfaceInstanceImpl)) return false;
			InterfaceInstanceImpl iii = (InterfaceInstanceImpl) obj;
			
			if (typearguments.keySet().size() != iii.typearguments.keySet().size()) return false;
			for (String key : typearguments.keySet()) {
				if (!iii.typearguments.containsKey(key) || !typearguments.get(key).equals(iii.typearguments.get(key))) return false;
			}
			
			return iii.getInterface().equals(getInterface());
		}

		@Override
		public Optional<FunctionInstance> resolveFunction(String name,
				TypeReference expectedReturnType,
				TypeReference... argumentTypes) {
			
			Function f = getInterface().functions.resolve(new Function.SignatureImpl(name)).orElse(null);
			if (f == null) return Optional.empty();
			if (argumentTypes.length != f.getParameters().length)
				throw new ResolvationException("method " + name);
			
			HashMap<String, TypeReference> ta = new HashMap<>();
			
			if (f.getReturnType().isTypeparameter())
				ta.put(f.getReturnType().getShortName(), expectedReturnType);
			for (int i = 0; i < argumentTypes.length; i++) {
				TypeReference par = f.getParameters()[i].getType();
				if (par.isTypeparameter())
					ta.put(par.getShortName(), argumentTypes[i]);
			}
			
			return resolveFunction(name, ta);
		}
		
		public Optional<FunctionInstance> resolveFunction(String name, Map<String, TypeReference> functionTypearguments) {
			Function f = getInterface().functions.resolve(new Function.SignatureImpl(name)).orElse(null);
			if (f == null) return Optional.empty();
			
			HashMap<String, TypeReference> ta = new HashMap<>();
			ta.putAll(typearguments);
			ta.putAll(functionTypearguments);
			
			return Optional.of(f.getInstance(ta));
		}
		
		@Override
		public Interface getInterface() {
			return Interface.this;
		}
	}
	
}
