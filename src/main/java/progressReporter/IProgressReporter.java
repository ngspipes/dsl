package progressReporter;

import exceptions.ProgressReporterException;


public interface IProgressReporter {

	public void open() throws ProgressReporterException;
	public void reportTrace(String msg) throws ProgressReporterException;
	public void reportError(String msg) throws ProgressReporterException;
	public void reportInfo(String msg) throws ProgressReporterException;
	public void close() throws ProgressReporterException;
	
}
