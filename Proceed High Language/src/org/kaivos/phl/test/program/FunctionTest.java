package org.kaivos.phl.test.program;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.kaivos.phl.program.Function;
import org.kaivos.phl.program.FunctionInstance;
import org.kaivos.phl.program.Interface;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.Variable;
import org.kaivos.phl.program.reference.TypeReference;

public class FunctionTest {

	private static<T, U> Map<T, U> createMap(T t, U u) {
		Map<T, U> map = new HashMap<>();
		map.put(t, u);
		return map;
	}
	
	@Test
	public void testReturnTypeSubstitution() {
		Module m = new Module("m");
		
		Interface i = new Interface("I");
		m.registerInterface(i);
		
		Function f = new Function("f", new TypeReference(true, "T", m));
		m.registerFunction(f);
		
		FunctionInstance fi = f.getInstance(createMap("T", new TypeReference(false, "I", m)));
		assertEquals(fi.getReturnType().getInterface(), i);
	}
	
	@Test
	public void testParameterubstitution() {
		Module m = new Module("m");
		
		Interface i = new Interface("I");
		m.registerInterface(i);
		
		Interface j = new Interface("J");
		m.registerInterface(j);
		
		Function f = new Function("f", new TypeReference(false, "J", m), new Variable("v", new TypeReference(true, "T", m)));
		m.registerFunction(f);
		
		FunctionInstance fi = f.getInstance(createMap("T", new TypeReference(false, "I", m)));
		assertEquals(fi.getParameters()[0].getType().getInterface(), i);
	}

}
