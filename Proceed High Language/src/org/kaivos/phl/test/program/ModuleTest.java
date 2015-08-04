package org.kaivos.phl.test.program;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kaivos.phl.program.Function;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.exception.RegistrationException;

public class ModuleTest {

	@Test
	public void testAddingMultipleFunctions() {
		Module m = new Module();
		Function f1 = new Function("f1");
		Function f2 = new Function("f2");
		Function f3 = new Function("f3");
		m.registerFunction(f1);
		m.registerFunction(f2);
		m.registerFunction(f3);
		assertEquals(f1, m.resolveFunction("f1").orElse(null));
		assertEquals(f2, m.resolveFunction("f2").orElse(null));
		assertEquals(f3, m.resolveFunction("f3").orElse(null));
	}
	
	@Test
	public void testDuplicateFunctions() {
		Module m = new Module();
		Function f;
		m.registerFunction(new Function("f1"));
		m.registerFunction(f = new Function("f2"));
		
		boolean throwsError = false;
		try {
			m.registerFunction(new Function("f2"));
		} catch (RegistrationException ex) {
			throwsError = true;
		}
		
		assertTrue("duplicate function throws an exception", throwsError);
		assertEquals(f, m.resolveFunction("f2").orElse(null));
	}

}
