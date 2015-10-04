package org.kaivos.phl.program;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kaivos.phl.program.reference.TypeReference;
import org.kaivos.phl.program.reference.TypeparameterSubstitutions;
import org.kaivos.phl.program.util.NamedChild;

public class Function implements NamedChild<Function.Signature, VariableScope> {

	public static abstract class Signature {
		public abstract String getName();
		
		@Override
		public boolean equals(Object obj) {
			return obj instanceof Signature
					&& ((Signature) obj).getName().equals(getName());
		}
		
		@Override
		public int hashCode() {
			return getName().hashCode();
		}
	}
	
	public static class SignatureImpl extends Signature {
		private String name;
		
		public SignatureImpl(String name) {
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
	}
	
	private class PrivateSignatureImpl extends Signature {
		@Override
		public String getName() {
			return name;
		}
	}
	
	private String name;
	private TypeReference returnType;
	private Variable[] parameters;
	private FunctionBody body;
	
	VariableScope parent;
	
	private final Signature signature = new PrivateSignatureImpl();
	
	private Set<FunctionInstance> knownInstances = new HashSet<>();
	
	public Function(String name, TypeReference returnType, Variable[] parameters, FunctionBody body) {
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
		this.body = body;
	}
	
	@Override
	public boolean equals(Object obj) {
		// no duplicates allowed
		return this == obj;
	}
	
	public String getName() {
		return name;
	}
	
	public Variable[] getParameters() {
		return parameters;
	}
	
	public TypeReference getReturnType() {
		return returnType;
	}
	
	public FunctionBody getBody() {
		return body;
	}

	@Override
	public Signature getSignature() {
		return signature;
	}

	@Override
	public void setParent(VariableScope p) {
		parent = p;
	}

	@Override
	public VariableScope getParent() {
		return parent;
	}
	
	public void validate() {
		returnType.validate();
		for (Variable var : parameters) var.validate();
	}
	
	public FunctionInstance getInstance(Map<String, TypeReference> typearguments) {
		FunctionInstance fi = new FunctionInstanceImpl(typearguments);
		knownInstances.add(fi);
		return fi;
	}
	
	private class FunctionInstanceImpl implements FunctionInstance {
		private TypeparameterSubstitutions substitutions;
		private InterfaceInstance returnType;
		private VariableInstance[] parameters;
		private FunctionBodyInstance body;
		
		public FunctionInstanceImpl(Map<String, TypeReference> typearguments) {
			substitutions = new TypeparameterSubstitutions(typearguments);
			returnType = getFunction().getReturnType().getReferencedInterfaceInstance(substitutions);
			parameters = new VariableInstance[getFunction().getParameters().length];
			for (int i = 0; i < parameters.length; i++) parameters[i] = getFunction().getParameters()[i].getInstance(substitutions);
			body = getFunction().getBody().getInstance(substitutions);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof FunctionInstanceImpl)) return false;
			FunctionInstanceImpl fii = (FunctionInstanceImpl) obj;
			
			if (!fii.substitutions.equals(substitutions)) return false;
			
			return fii.getFunction().equals(getFunction());
		}
		
		@Override
		public Function getFunction() {
			return Function.this;
		}

		@Override
		public String getName() {
			return getFunction().getName();
		}

		@Override
		public VariableInstance[] getParameters() {
			return parameters;
		}

		@Override
		public InterfaceInstance getReturnType() {
			return returnType;
		}
		
		@Override
		public FunctionBodyInstance getBody() {
			return body;
		}
	}
	
}
