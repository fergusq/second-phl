package org.kaivos.phl.test.program.type;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kaivos.phl.program.Interface;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.type.TypeReference;

public class TypeReferenceTest {

	@Test
	public void testSimpleNameResolvation() {
		Module m = new Module();
		Interface i = new Interface("i1");
		m.registerInterface(i);
		TypeReference tr = new TypeReference("i1", m);
		assertEquals(i, tr.getReferencedInterface());
	}
	
	@Test
	public void testCompoundNameResolvation() {
		Module m = new Module();
		
		Interface i1 = new Interface("i1");
		m.registerInterface(i1);
		Interface i2 = new Interface("i2");
		i1.registerInterface(i2);
		
		TypeReference tr = new TypeReference(new String[] {"i1", "i2"}, m);
		assertEquals(i2, tr.getReferencedInterface());
	}

}
