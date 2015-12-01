package exceptions;

public class ConfiguratorException extends DSLException {
	
	private static final long serialVersionUID = 1L;

	public ConfiguratorException(){}
	
	public ConfiguratorException(String msg){ super(msg); }
	
	public ConfiguratorException(String msg, Throwable cause){ super(msg, cause); }

}
