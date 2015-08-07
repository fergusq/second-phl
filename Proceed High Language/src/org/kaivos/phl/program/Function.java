package org.kaivos.phl.program;

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
	VariableScope parent;
	
	private final Signature signature = new PrivateSignatureImpl();
	
	public Function(String name) {
		this.name = name;
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
		
	}
	
}
