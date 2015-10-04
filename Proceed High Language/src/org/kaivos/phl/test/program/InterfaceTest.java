package org.kaivos.phl.test.program;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.kaivos.phl.program.Function;
import org.kaivos.phl.program.FunctionInstance;
import org.kaivos.phl.program.Interface;
import org.kaivos.phl.program.InterfaceInstance;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.Typeparameter;
import org.kaivos.phl.program.Variable;
import org.kaivos.phl.program.reference.TypeReference;

public class InterfaceTest {

	private TypeReference voidTypeWithin(Module m) {
		return new TypeReference(false, "Void", m);
	}
	
	private Module prepareModule(String name) {
		Module m = new Module(name);
		m.registerInterface(new Interface("Void"));
		return m;
	}
	
	private<K, V> Map<K, V> makeMap(K[] ks, V[] vs) {
		if (ks.length != vs.length)
			throw new IllegalArgumentException();
		HashMap<K, V> map = new HashMap<>();
		for (int i = 0; i < ks.length; i++) {
			map.put(ks[i], vs[i]);
		}
		return map;
	}
	
	@Test
	public void testAddingMultipleFunctions() {
		Module m = prepareModule("m");
		
		Interface j = new Interface("j");
		m.registerInterface(j);
		
		Interface k = new Interface("k");
		m.registerInterface(k);
		
		Interface i = new Interface("i", new Typeparameter("T"));
		m.registerInterface(i);
		Function f1 = new Function("f1", voidTypeWithin(m), new Variable[] {new Variable("a", new TypeReference(true, "T", m))}, new DummyBody());
		i.registerFunction(f1);
		
		InterfaceInstance ii = i.getInstance(makeMap(new String[] {"T"}, new TypeReference[] {new TypeReference(false, "j", m)}));
		Optional<FunctionInstance> fi = ii.resolveFunction("f1", new TypeReference(false, "void", m), new TypeReference(false, "k", m));
		assertEquals(k, fi.get().getParameters()[0].getType().getInterface());
	}

}
