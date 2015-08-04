package org.kaivos.phl.program.exception;

public class RegistrationException extends RuntimeException {

	private static final long serialVersionUID = -6122957607315623723L;

	private String name;
	
	public RegistrationException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "unable to register " + name;
	}
}
