package exceptions;

public class CommandBuilderException extends DSLException {
	private static final long serialVersionUID = 1L;

	public CommandBuilderException(){}
	
	public CommandBuilderException(String msg){ super(msg); }
	
	public CommandBuilderException(String msg, Throwable cause){ super(msg, cause); }
}
