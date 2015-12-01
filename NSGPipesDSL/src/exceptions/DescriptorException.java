package exceptions;

public class DescriptorException extends DSLException {
	
	private static final long serialVersionUID = 1L;

	public DescriptorException(){}
	
	public DescriptorException(String msg){ super(msg); }
	
	public DescriptorException(String msg, Throwable cause){ super(msg, cause); }


}
