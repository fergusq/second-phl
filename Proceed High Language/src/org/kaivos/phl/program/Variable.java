package org.kaivos.phl.program;

import org.kaivos.phl.program.reference.TypeReference;
import org.kaivos.phl.program.reference.TypeparameterSubstitutions;

public class Variable {
	private String name;
	private TypeReference type;
	
	public Variable(String name, TypeReference type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public TypeReference getType() {
		return type;
	}
	
	public void validate() {
		type.validate();
	}
	
	public VariableInstance getInstance(TypeparameterSubstitutions substitutions) {
		return new VariableInstanceImpl(substitutions);
	}
	
	private class VariableInstanceImpl implements VariableInstance {

		private InterfaceInstance type;
		
		public VariableInstanceImpl(TypeparameterSubstitutions substitutions) {
			type = getVariable().getType().getReferencedInterfaceInstance(substitutions);
		}

		@Override
		public String getName() {
			return getVariable().getName();
		}

		@Override
		public InterfaceInstance getType() {
			return type;
		}
		
		@Override
		public boolean equals(Object obj) {
			Variable v;
			return obj instanceof VariableInstanceImpl && (v=(Variable)obj).getName().equals(getName()) && v.getType().equals(getType());
		}
		
		private Variable getVariable() {
			return Variable.this;
		}
		
	}
}
