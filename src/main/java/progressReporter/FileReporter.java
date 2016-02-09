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
package progressReporter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import exceptions.ProgressReporterException;

public class FileReporter implements IProgressReporter{

	public static final String TRACE_TAG = "TRACE\t";
	public static final String ERROR_TAG = "ERROR\t";
	public static final String INFO_TAG = "INFO\t";
	
	private final String path;
	private PrintWriter writer;
	
	public FileReporter(String path){
		this.path = path;
	}
	
	@Override
	public void reportTrace(String msg) {
		report(TRACE_TAG + msg);
	}
	
	@Override
	public void reportError(String msg) {
		report(ERROR_TAG + msg);
	}
	
	@Override
	public void reportInfo(String msg) {
		report(INFO_TAG + msg);
	}
	
	private void report(String msg) {
		if(writer != null){
			writer.println(msg);
			writer.flush();
		}
	}

	@Override
	public void open() throws ProgressReporterException {
		try {
			writer = new PrintWriter(path);
		} catch (FileNotFoundException ex) {
			throw new ProgressReporterException("File " + path + " not found!", ex);
		}
	}

	@Override
	public void close() {
		writer.close();
	}

}
