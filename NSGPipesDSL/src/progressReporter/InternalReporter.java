package progressReporter;

import exceptions.ProgressReporterException;

public class InternalReporter implements IProgressReporter{

	private final IProgressReporter reporter;
	
	public InternalReporter(IProgressReporter reporter) {
		this.reporter = reporter;
	}
	
	@Override
	public void reportTrace(String msg) throws ProgressReporterException{
		try {
			reporter.reportTrace(msg);
		} catch (ProgressReporterException ex) {
			throw new ProgressReporterException("Error reporting " + msg + "\n" + ex.getMessage(), ex);
		}
	}
	
	@Override
	public void reportError(String msg) throws ProgressReporterException{
		try {
			reporter.reportError(msg);
		} catch (ProgressReporterException ex) {
			throw new ProgressReporterException("Error reporting " + msg + "\n" + ex.getMessage(), ex);
		}
	}
	
	@Override
	public void reportInfo(String msg) throws ProgressReporterException{
		try {
			reporter.reportInfo(msg);
		} catch (ProgressReporterException ex) {
			throw new ProgressReporterException("Error reporting " + msg + "\n" + ex.getMessage(), ex);
		}
	}

	@Override
	public void open() throws ProgressReporterException{
		try {
			reporter.open();
		} catch (ProgressReporterException ex) {
			throw new ProgressReporterException("Error opening Reporter " + ex.getMessage(), ex);
		}
	}

	@Override
	public void close() throws ProgressReporterException{
		try {
			reporter.close();
		} catch (ProgressReporterException ex) {
			throw new ProgressReporterException("Error closing Reporter " + ex.getMessage(), ex);
		}
	}
	
}
