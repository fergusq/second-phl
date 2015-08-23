package org.kaivos.phl.test.program;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kaivos.phl.program.Function;
import org.kaivos.phl.program.Interface;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.Program;
import org.kaivos.phl.program.exception.RegistrationException;
import org.kaivos.phl.program.reference.ModuleReference;
import org.kaivos.phl.program.reference.TypeReference;

public class ModuleTest {

	private TypeReference voidTypeWithin(Module m) {
		return new TypeReference(false, "void", m);
	}
	
	private Module prepareModule(String name) {
		Module m = new Module(name);
		m.registerInterface(new Interface("Void"));
		return m;
	}
	
	@Test
	public void testAddingMultipleFunctions() {
		Module m = prepareModule("m");
		Function f1 = new Function("f1", voidTypeWithin(m));
		Function f2 = new Function("f2", voidTypeWithin(m));
		Function f3 = new Function("f3", voidTypeWithin(m));
		m.registerFunction(f1);
		m.registerFunction(f2);
		m.registerFunction(f3);
		assertEquals(f1, m.resolveFunction("f1").orElse(null));
		assertEquals(f2, m.resolveFunction("f2").orElse(null));
		assertEquals(f3, m.resolveFunction("f3").orElse(null));
	}
	
	@Test
	public void testDuplicateFunctions() {
		Module m = prepareModule("m");
		Function f;
		m.registerFunction(new Function("f1", voidTypeWithin(m)));
		m.registerFunction(f = new Function("f2", voidTypeWithin(m)));
		
		boolean throwsError = false;
		try {
			m.registerFunction(new Function("f2", voidTypeWithin(m)));
		} catch (RegistrationException ex) {
			throwsError = true;
		}
		
		assertTrue("duplicate function doesn't throw an exception", throwsError);
		assertEquals(f, m.resolveFunction("f2").orElse(null));
	}
	
	@Test
	public void testAddingMultipleInterfaces() {
		Module m = prepareModule("m");
		Interface i1 = new Interface("i1");
		Interface i2 = new Interface("i2");
		Interface i3 = new Interface("i3");
		m.registerInterface(i1);
		m.registerInterface(i2);
		m.registerInterface(i3);
		assertEquals(i1, m.resolveInterface("i1").orElse(null));
		assertEquals(i2, m.resolveInterface("i2").orElse(null));
		assertEquals(i3, m.resolveInterface("i3").orElse(null));
	}
	
	@Test
	public void testDuplicateInterfaces() {
		Module m = prepareModule("m");
		Interface i;
		m.registerInterface(new Interface("f1"));
		m.registerInterface(i = new Interface("f2"));
		
		boolean throwsError = false;
		try {
			m.registerInterface(new Interface("f2"));
		} catch (RegistrationException ex) {
			throwsError = true;
		}
		
		assertTrue("duplicate interface doesn't throw an exception", throwsError);
		assertEquals(i, m.resolveInterface("f2").orElse(null));
	}
	
	@Test
	public void testImports() {
		Program p = new Program();
		Module m = prepareModule("m");
		Module n = prepareModule("n");
		Interface i = new Interface("i");
		p.registerModule(m);
		p.registerModule(n);
		n.registerInterface(i);
		m.registerImport(new ModuleReference("n", p));
		assertEquals(i, m.resolveInterface("i").orElse(null));
	}

}
