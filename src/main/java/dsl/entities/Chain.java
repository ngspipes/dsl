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
package dsl.entities;

import utils.Event;

import java.util.function.Consumer;

public class Chain {
	
	private final Argument argument;
	public Argument getArgument() { return argument; }
	
	private final Output output;
	public Output getOutput() { return output; }
	
	private final Consumer<String> onOutputChange;
	
	private boolean connected;
	public boolean getConnected(){ return connected; }
	private void setConnected(boolean connected){ 
		this.connected = connected;
		connectEvent.trigger(connected);
	}
	
	public final Event<Boolean> connectEvent;
	
	public Chain(Argument argument, Output output){
		this.argument = argument;
		this.output = output;
		this.connected = false;
		this.connectEvent = new Event<>();
		this.onOutputChange = argument::setValue;
	}
	
	public void connect(){
		if(connected)
			return;
		
		if(output.getValue()!=null)
			argument.setValue(output.getValue());
		
		output.valueChangedEvent.addListener(onOutputChange);
		setConnected(true);
	}
	
	public void disconnect(){
		if(!connected)
			return; 
		
		output.valueChangedEvent.removeListener(onOutputChange);

		argument.setValue(null);

		setConnected(false);
	}

}
