package progressReporter;


public class ConsoleReporter implements IProgressReporter{

	public static final String TRACE_TAG = "-";
	
	@Override
	public void reportTrace(String msg) {
		System.out.println(TRACE_TAG + msg);
	}
	
	@Override
	public void reportError(String msg) {
		System.err.println(msg);
	}
	
	@Override
	public void reportInfo(String msg) {
		System.out.println(msg);
	}

	@Override
	public void open() {}

	@Override
	public void close() {}

}
