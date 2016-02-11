/*-
 * Copyright (c) 2016, NGSPipes Team <ngspipes@gmail.com>
 * All rights reserved.
 *
 * This file is part of NGSPipes <http://ngspipes.github.io/>.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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
		BufferedReader bf = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));

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

		t.setDaemon(true);
		t.start();

		return t;
	}

}
