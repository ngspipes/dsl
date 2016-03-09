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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import exceptions.ProgressReporterException;


public class SocketReporter implements IProgressReporter{
	
	public static final String TRACE_TAG = "TRACE\t";
	public static final String ERROR_TAG = "ERROR\t";
	public static final String INFO_TAG = "INFO\t";
	
	private final int port;
	private final String ipAddress; 
	private PrintWriter writer;
	private Socket serverSocket;
	
	public SocketReporter(int port, String ipAddress) {
		this.port = port;
		this.ipAddress = ipAddress;
	}
	
	
	@Override
	public void reportTrace(String msg) throws ProgressReporterException {
		writer.println(TRACE_TAG + msg);
		writer.flush();
	}
	
	@Override
	public void reportError(String msg) throws ProgressReporterException {
		writer.println(ERROR_TAG + msg);
		writer.flush();
	}
	
	@Override
	public void reportInfo(String msg) throws ProgressReporterException {
		writer.println(INFO_TAG + msg);
		writer.flush();
	}

	@Override
	public void open() throws ProgressReporterException {

		InetAddress address;
		try {
			address = InetAddress.getByName(ipAddress);
			System.out.println(address.getHostAddress() + " : " + address.getHostName());
			serverSocket = new Socket(address, port);
			writer = new PrintWriter(serverSocket.getOutputStream(), true);
		} catch (IOException e) {
			throw new ProgressReporterException("Couldn't connect to host: " + ipAddress + " on port: "
												+ port, e);
		}
	}
	
	@Override
	public void close() throws ProgressReporterException {
		writer.close();
		try {
			if(!serverSocket.isClosed())
				serverSocket.close();
		} catch (IOException e) {
			throw new ProgressReporterException("Error closing socket in host: " + ipAddress + " and port: "
												+ port, e);
		}
	}

}
