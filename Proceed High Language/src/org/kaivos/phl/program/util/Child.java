package org.kaivos.phl.program.util;

public interface Child<T> {

	public abstract void setParent(T t);
	public abstract T getParent();

}