package org.kaivos.phl.program.util;

public interface NamedChild<T, U> extends Child<U> {

	public T getSignature();
	
}
