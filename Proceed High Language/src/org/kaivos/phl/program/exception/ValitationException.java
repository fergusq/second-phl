package org.kaivos.phl.program.exception;

public class ValitationException extends RuntimeException {

	private static final long serialVersionUID = 8454072740441894746L;
	
	private String name;
	
	public ValitationException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "unable to validate " + name;
	}
	
}
