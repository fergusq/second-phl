package org.kaivos.phl.program;

import org.kaivos.phl.program.reference.TypeparameterSubstitutions;

public interface FunctionBody {

	public FunctionBodyInstance getInstance(TypeparameterSubstitutions substitutions);
	
}
