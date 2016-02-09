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

import java.util.function.Consumer;

import utils.Event;
import descriptors.IOutputDescriptor;
import descriptors.OutputDescriptor;

public class Output {
	
	private final Consumer<String> onArgumentValueChanged;

	private final Command originCommand;
	public Command getOriginCommand(){ return originCommand; }
	
	private final IOutputDescriptor descriptor;
	public IOutputDescriptor getDescriptor(){ return descriptor; }
	
	private final String initValue;
	private String value;
	public String getValue(){ return value; }
	public void setValue(String value){ 
		this.value = value;
		valueChangedEvent.trigger(value);
	}

	public final Event<String> valueChangedEvent; 
	
	public Output(IOutputDescriptor descriptor, String value, Command originCommand){
		this.descriptor = descriptor;
		this.initValue = value;
		this.value = value;
		this.valueChangedEvent = new Event<>();
		this.originCommand = originCommand;
		this.onArgumentValueChanged = getOnArgumentValueChanged();
	}

	private Consumer<String> getOnArgumentValueChanged(){ 
		switch (descriptor.getType()) {
			case OutputDescriptor.INDEPENDENT_TYPE: return (val) -> {};
			case OutputDescriptor.FILE_DEPENDENT_TYPE : return (val) -> this.setValue(val);
			case OutputDescriptor.DIRECTORY_DEPENDENT_TYPE : return (val) -> this.setValue(val + "/" + this.initValue);
			default : return null;
		}
	}
	
	public String getName(){
		return descriptor.getName();
	}
	
	public String getArgumentName() {
		return descriptor.getArgumentName();
	}

	public String getType() {
		return descriptor.getType();
	}

	protected void onArgumentValueChange(String newValue){
		onArgumentValueChanged.accept(newValue);
	}
		
}
