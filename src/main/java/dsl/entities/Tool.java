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

import descriptors.ICommandDescriptor;
import descriptors.IToolDescriptor;
import exceptions.DSLException;

public class Tool {

	private final IToolDescriptor toolDescriptor;
	public IToolDescriptor getToolDescriptor() { return toolDescriptor; }
	
	public Tool(IToolDescriptor toolDescriptor){
		this.toolDescriptor = toolDescriptor;
	}
	
	public Command createCommand(String commandName) throws DSLException{
		ICommandDescriptor commandDescriptor = toolDescriptor.getCommand(commandName);
		
		if(commandDescriptor == null)
			throw new DSLException("Tool " + toolDescriptor.getName() + " doesn't contain command " +  commandName);
		
		return new Command(commandDescriptor, this);
	}

}
