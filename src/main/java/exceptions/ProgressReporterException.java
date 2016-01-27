package exceptions;

public class ProgressReporterException extends DSLException {
	private static final long serialVersionUID = 1L;

	public ProgressReporterException(){}
	
	public ProgressReporterException(String msg){ super(msg); }
	
	public ProgressReporterException(String msg, Throwable cause){ super(msg, cause); }
}

