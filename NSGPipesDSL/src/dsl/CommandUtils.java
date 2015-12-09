package dsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import jdk.internal.util.xml.impl.ReaderUTF8;
import progressReporter.IProgressReporter;
import exceptions.DSLException;
import exceptions.ProgressReporterException;

public class CommandUtils {
	
	private static class ExceptionBox{
		public Exception ex;
	}
	
	@FunctionalInterface
	private static interface LogLambda {
		public void log() throws Exception;
	}
	
	@FunctionalInterface
	private static interface InternalReporter {
		public void report(String msg) throws ProgressReporterException;
	}
	
	protected static void logStream(InputStream in, InternalReporter reporter) throws IOException, ProgressReporterException {
		BufferedReader bf = new BufferedReader(new ReaderUTF8(in));

		String line;
		StringBuilder sb = new StringBuilder();
		
		try{
			while((line=bf.readLine()) != null){
				sb.append(line).append("\n");
				reporter.report(line);
			}	
		}finally{
			if(sb.length() != 0)
				Log.log(sb.toString());
			
			bf.close();	
		}
	}
	
	public static void run(String command, IProgressReporter reporter) throws DSLException {
		ExceptionBox inputBox = new ExceptionBox();
		ExceptionBox errorBox = new ExceptionBox();
		Process p;
		
		try{
			p = Runtime.getRuntime().exec(command);

			Thread inputThread = createThread(()->logStream(p.getInputStream(), reporter::reportInfo), inputBox);
			Thread errorThread = createThread(()->logStream(p.getErrorStream(), reporter::reportError), errorBox);

			inputThread.join();
			errorThread.join();
		}catch(Exception ex){
			throw new DSLException("Error executing command " + command, ex);
		}
		
		//Ignoring IOExceptions from logStream(InputStream in)
		
		try {
			int exitCode = p.waitFor();
			if(exitCode != 0){
				String message = "Command " + command + " finished with Exit Code = " + exitCode;
				Log.log(message);
				reporter.reportError(message);
			}
		} catch (Exception ex) {
			throw new DSLException("Error executing command " + command, ex);
		}		
	}

	private static Thread createThread(LogLambda action,  ExceptionBox box) {
		Thread t = new Thread( () -> {
			try {
				action.log();
			} catch(Exception e) {
				box.ex = e;
			}
		});

		t.start();

		return t;
	}

}
