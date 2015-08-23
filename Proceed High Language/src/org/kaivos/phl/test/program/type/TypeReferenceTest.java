package org.kaivos.phl.test.program.type;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kaivos.phl.program.Interface;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.reference.TypeReference;
import org.kaivos.phl.program.reference.TypeparameterSubstitutions;

public class TypeReferenceTest {

	@Test
	public void testSimpleNameResolvation() {
		Module m = new Module("m");
		Interface i = new Interface("i1");
		m.registerInterface(i);
		TypeReference tr = new TypeReference(false, "i1", m);
		assertEquals(i, tr.getReferencedInterface().get());
	}
	
	@Test
	public void testCompoundNameResolvation() {
		Module m = new Module("m");
		
		Interface i1 = new Interface("i1");
		m.registerInterface(i1);
		Interface i2 = new Interface("i2");
		i1.registerInterface(i2);
		
		TypeReference tr = new TypeReference(false, new String[] {"i1", "i2"}, m);
		assertEquals(i2, tr.getReferencedInterface().get());
	}
	
	@Test
	public void testTypeparameterSubstitution() {
		Module m = new Module("m");
		
		Interface i = new Interface("i");
		m.registerInterface(i);
		
		TypeparameterSubstitutions s = new TypeparameterSubstitutions(new String[] { "p" }, new TypeReference[] { new TypeReference(false, "i", m) });
		
		TypeReference tr = new TypeReference(true, "p", m);
		assertEquals(i, tr.getReferencedInterfaceInstance(s).getInterface());
	}

}
