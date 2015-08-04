package org.kaivos.phl.program.exception;

public class ResolvationException extends RuntimeException {

	private static final long serialVersionUID = 21014207282574969L;
	
	private String name;
	
	public ResolvationException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "unable to resolve " + name;
	}

}
