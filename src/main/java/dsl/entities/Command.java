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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import argmentsComposer.IArgumentsComposer;
import descriptors.IArgumentDescriptor;
import descriptors.ICommandDescriptor;
import descriptors.IOutputDescriptor;
import descriptors.OutputDescriptor;
import dsl.managers.Support;

public class Command {
	
	private Step originStep;
	public Step getOriginStep(){ return originStep; }
	public void setOriginStep(Step originStep){ this.originStep = originStep; }
	
	private final Tool originTool;
	public Tool getOriginTool(){ return originTool; }
	
	private final ICommandDescriptor descriptor;
	public ICommandDescriptor getDescriptor(){ return descriptor; }
	
	private final IArgumentsComposer composer;
	public IArgumentsComposer getComposer(){ return composer; }

	private final List<Argument> args;
	public List<Argument> getArguments(){ return args; }
	
	private final List<Output> otps;
	public List<Output> getOutputs(){ return otps; }
	
	private final Map<String, Argument> arguments;
	public Argument getArgument(String argumentName){ return arguments.get(argumentName); }
	
	private final Map<String, Output> outputs;
	public Output getOutput(String outputName){ return  outputs.get(outputName); }
	
	private final Map<Argument, Collection<Output>> inputDependencies;
	public Collection<Output> getDependentsOf(Argument argument){ return inputDependencies.get(argument); }
	
	private final Map<Output, Argument> outputDependency;
	public Argument getDespendencyOf(Output output){ return outputDependency.get(output); }

	private final String name;
	public String getName(){ return name; }
	
	private String command;
	public String getCommand() { return command; }
	public void setCommand(String command) { this.command = command; }

	public Command(ICommandDescriptor descriptor,Tool originTool){
		this.originTool = originTool;
		this.descriptor = descriptor;
		this.composer = Support.getComposer(descriptor.getArgumentsComposer());
		this.name = descriptor.getName();
		this.command = descriptor.getCommand();
		this.args = new LinkedList<>();
		this.arguments = new HashMap<>();
		this.otps = new LinkedList<>();
		this.outputs = new HashMap<>();
		this.inputDependencies = new HashMap<>();
		this.outputDependency = new HashMap<>();
		
		loadArguments();
		loadOutputs();
	}
	
	private void loadArguments(){
		Argument argument;
		for(IArgumentDescriptor arg : descriptor.getArguments()){
			argument = new Argument(arg, null, this);
			args.add(argument);
			arguments.put(argument.getName(), argument);
			inputDependencies.put(argument, new LinkedList<>());
		}
	}
	
	private void loadOutputs(){
		Output output;
		String type;
		Argument argument;
		for(IOutputDescriptor out : descriptor.getOutputs()){
			output = new Output(out, out.getValue(), this);
			otps.add(output);
			outputs.put(output.getName(), output);
			
			type = out.getType();
			if(type.equals(OutputDescriptor.FILE_DEPENDENT_TYPE) || type.equals(OutputDescriptor.DIRECTORY_DEPENDENT_TYPE)){
				argument = arguments.get(out.getArgumentName());
				inputDependencies.get(argument).add(output);
				outputDependency.put(output, argument);
				argument.valueChangedEvent.addListner(output::onArgumentValueChange);
			}
		}
	}

}
