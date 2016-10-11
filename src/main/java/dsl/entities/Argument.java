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

import argmentsComposer.IArgumentsComposer;
import dsl.managers.Support;
import utils.Event;
import descriptors.IArgumentDescriptor;

public class Argument {
	
	private final Command originCommand;
	public Command getOriginCommand(){ return originCommand; }

	private final IArgumentDescriptor descriptor;
	public IArgumentDescriptor getDescriptor(){ return descriptor; }

	private final IArgumentsComposer composer;
	public IArgumentsComposer getComposer(){ return composer; }

	private String value;
	public String getValue(){ return value; }
	public void setValue(String value){
		this.value = value;
		valueChangedEvent.trigger(value);
	}
	
	public final Event<String> valueChangedEvent; 
	
	public Argument(IArgumentDescriptor descriptor, String value, Command originCommand){
		this.descriptor = descriptor;
		this.value = value;
		this.valueChangedEvent = new Event<>();
		this.originCommand = originCommand;
		this.composer = Support.getComposer(descriptor.getArgumentComposer());
	}

	public String getName(){
		return descriptor.getName();
	}

	public String getType(){
		return descriptor.getType();
	}

	public boolean getRequired(){
		return descriptor.getRequired();
	}

	public int getOrder(){
		return descriptor.getOrder();
	}
	
}
