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
package dsl.language;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import repository.IRepository;
import configurators.IConfigurator;
import descriptors.IToolDescriptor;
import dsl.entities.Command;
import dsl.entities.Pipeline;
import dsl.entities.Step;
import dsl.entities.Tool;
import exceptions.DSLException;

public class Context {
	
	private final LinkedList<Tool> tools = new LinkedList<>();
	private final Map<String, LinkedList<Tool>> toolsNames = new HashMap<>();
	private final Map<Tool, LinkedList<Command>> toolCommands = new HashMap<>();
	private final Map<Tool, IConfigurator> toolConfigurator = new HashMap<>();
	private final Map<Tool, Map<String, LinkedList<Command>>> commandsNames = new HashMap<>();

	public final IRepository repository;
	public final Pipeline pipeline;
	
	private Tool previousTool;
	private Tool currentTool;
	private Command previousCommand;
	private Command currentCommand;

	
	public Context(IRepository repository, Pipeline pipeline){
		this.repository = repository;
		this.pipeline = pipeline;
	}
	
	
	
	public void addTool(Tool tool, IConfigurator configurator){
		toolConfigurator.put(tool, configurator);
		
		//tools
		tools.addLast(tool);
		
		//toolsNames
		if(!toolsNames.containsKey(tool.getToolDescriptor().getName()))
			toolsNames.put(tool.getToolDescriptor().getName(), new LinkedList<>());
		toolsNames.get(tool.getToolDescriptor().getName()).addLast(tool);
		
		//toolCommands
		toolCommands.put(tool, new LinkedList<>());
		
		//commandNames
		commandsNames.put(tool, new HashMap<>());
		
		previousTool = currentTool;
		currentTool = tool;
	}
	
	public void addCommand(Command command) throws DSLException {
		Tool tool = command.getOriginTool();
		//toolCommands
		toolCommands.get(tool).addLast(command);
		
		//commandsNames
		if(!commandsNames.get(tool).containsKey(command.getName()))
			commandsNames.get(tool).put(command.getName(), new LinkedList<>());
		commandsNames.get(tool).get(command.getName()).addLast(command);
		
		previousCommand = currentCommand;
		currentCommand = command;
		
		pipeline.addStep(new Step(currentCommand, toolConfigurator.get(currentTool)));
	}

	
	
	public IConfigurator getToolConfigurator(IToolDescriptor tool){
		return toolConfigurator.get(tool);
	}
	
	
	
	public Tool getCurrentTool(){
		return currentTool;
	}
	
	public Tool getPreviousTool(){
		return previousTool;
	}
	
	public Tool getTool(int index){
		if(tools.size()<index)
			return null;
		
		return tools.get(index-1);
	}
	
	public Tool getTool(String toolName, int index){
		if(!toolsNames.containsKey(toolName))
			return null;
		
		if(toolsNames.get(toolName).size()<index)
			return null;
		
		return toolsNames.get(toolName).get(index-1);
	}
	
	public Tool getLastTool(String toolName){
		return toolsNames.get(toolName).getLast();
	}
	
	
	public Command getCurrentCommand(){
		return currentCommand;
	}
	
	public Command getPreviousCommand(){
		return previousCommand;
	}
	
	public Command getCommand(Tool tool, int index){
		if(!toolCommands.containsKey(tool))
			return null;
		
		if(toolCommands.get(tool).size()<index)
			return null;
		
		return toolCommands.get(tool).get(index-1);
	}
	
	public Command getCommand(Tool tool, String commandName, int index){
		if(!commandsNames.containsKey(tool))
			return null;
		
		if(!commandsNames.get(tool).containsKey(commandName))
			return null;
		
		if(commandsNames.get(tool).get(commandName).size()<index)
			return null;
		
		return commandsNames.get(tool).get(commandName).get(index-1);
	}

	public Command getLastCommandOf(Tool tool){
		if(!toolCommands.containsKey(tool))
			return null;
		
		return toolCommands.get(tool).getLast();
	}
	
	public Command getLastCommandOf(Tool tool, String commandName){
		if(!commandsNames.containsKey(tool))
			return null;
		
		if(!commandsNames.get(tool).containsKey(commandName))
			return null;
		
		return commandsNames.get(tool).get(commandName).getLast();
	}
	
}