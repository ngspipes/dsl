package exceptions;

public class DSLException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DSLException(){}
	
	public DSLException(String msg){ super(msg); }
	
	public DSLException(String msg, Throwable cause){ super(msg, cause); }

}
