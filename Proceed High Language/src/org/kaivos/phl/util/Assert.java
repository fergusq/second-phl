package org.kaivos.phl.util;

/**
 * @author expositionrabbit, fergusq
 */
public final class Assert {
	
	public static <T> T nonNull(T obj) {
		if(obj == null) throw new NullPointerException();
		return obj;
	}
	
	public static int positive(int i) {
		if(i < 0) throw new IllegalArgumentException();
		return i;
	}
	
	public static long positive(long l) {
		if(l < 0) throw new IllegalArgumentException();
		return l;
	}
	
	public static <T> T[] nonEmpty(T[] obj) {
		nonNull(obj);
		if(obj.length == 0) throw new IllegalArgumentException();
		return obj;
	}
}