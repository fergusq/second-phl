package org.kaivos.phl.test.program;

import org.kaivos.phl.program.FunctionBody;
import org.kaivos.phl.program.FunctionBodyInstance;
import org.kaivos.phl.program.reference.TypeparameterSubstitutions;

class DummyBody implements FunctionBody {
	@Override
	public FunctionBodyInstance getInstance(
			TypeparameterSubstitutions substitutions) {
		return new FunctionBodyInstance() {};
	}
}