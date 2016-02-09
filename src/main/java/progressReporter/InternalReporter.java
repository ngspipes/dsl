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
