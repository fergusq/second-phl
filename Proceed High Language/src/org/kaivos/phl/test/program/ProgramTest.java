package org.kaivos.phl.test.program;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kaivos.phl.program.Module;
import org.kaivos.phl.program.Program;
import org.kaivos.phl.util.Version;

public class ProgramTest {

	@Test
	public void testAddingModule() {
		Program p = new Program();
		Module m = new Module("m");
		p.registerModule(m);
		assertEquals(m, p.resolveModule("m").orElse(null));
	}
	
	@Test
	public void testAddingModuleWithVersion() {
		Program p = new Program();
		Module m1 = new Module("m", Version.of(2, 3, 4)),
				m2 = new Module("m", Version.of(2, 3, 5));
		p.registerModule(m1);
		p.registerModule(m2);
		assertEquals(m2, p.resolveModule("m").orElse(null));
	}
	
	@Test
	public void testAddingAndResolvingModuleWithVersion() {
		Program p = new Program();
		Module m1 = new Module("m", Version.of(2, 3, 4)),
				m2 = new Module("m", Version.of(2, 3, 5));
		p.registerModule(m1);
		p.registerModule(m2);
		assertEquals(m1, p.resolveModule("m", Version.of(2, 3, 4)).orElse(null));
	}

}
