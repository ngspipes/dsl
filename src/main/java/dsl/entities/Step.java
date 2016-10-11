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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import dsl.managers.ArgumentsComposerManager;
import progressReporter.IProgressReporter;
import utils.Event;

import commandBuilder.ICommandBuilder;

import configurators.IConfigurator;
import dsl.ArgumentValidator;
import dsl.ArgumentValidator.ArgumentValidationException;
import dsl.CommandUtils;
import dsl.managers.Support;
import exceptions.CommandBuilderException;
import exceptions.DSLException;



public class Step {

	private Pipeline originPipe;
	public Pipeline getOriginPipe(){ return originPipe; }
	public void setOriginPipe(Pipeline originPipe){ this.originPipe = originPipe; }

	private int order;
	public int getOrder() { return order; }
	public void setOrder(int order){
		this.order = order;
		orderEvent.trigger(order);
	}
	public final Event<Integer> orderEvent;

	private final Command command;
	public Command getCommand() { return command; }

	private IConfigurator configurator;
	public IConfigurator getConfigurator(){ return configurator; }
	public void setConfigurator(IConfigurator configurator){ this.configurator = configurator; }
	public final Event<IConfigurator> configuratorEvent;

	private final ICommandBuilder commandBuilder;
	public ICommandBuilder getCommandBuilder(){ return commandBuilder; }


	public Step(Command command,  IConfigurator configurator) throws CommandBuilderException{
		this.order = -1;
		this.orderEvent = new Event<>();
		this.command = command;
		command.setOriginStep(this);
		this.configurator = configurator;
		this.configuratorEvent = new Event<>();
		this.commandBuilder = Support.getBuilder(configurator);
	}


	public int getRequiredMemory(){
		return command.getDescriptor().getOriginTool().getRequiredMemory();
	}

	public void build() throws DSLException {
		commandBuilder.build(command);
	}

	public void run(IProgressReporter reporter) throws DSLException {
		validateArguments(command.getArguments());

		List<Argument> arguments = arrangeArguments();

		String cmd = command.getCommand() + " " + ArgumentsComposerManager.compose(arguments);

		reporter.reportInfo("Executing : " + cmd);

		CommandUtils.run(cmd, reporter);
	}

	private void validateArguments(Collection<Argument> arguments) throws ArgumentValidationException{
		for(Argument argument : arguments){
			if(argument.getRequired() && argument.getValue()==null)
				throw new ArgumentValidationException("Argument " + argument.getName() + " from Command " + command.getName()+ " is required!");
			if(argument.getValue() != null)
				ArgumentValidator.validate(argument);
		}
	}

	private List<Argument> arrangeArguments(){
		List<Argument> arguments = new LinkedList<>(command.getArguments());

		arguments = arguments.parallelStream().filter((a)->a.getValue()!=null).collect(Collectors.toList());

		Collections.sort(arguments, (a,b)->a.getOrder()-b.getOrder());

		return arguments;
	}

	@Override
	public String toString(){
		return "Step : " + order + " Tool : " + command.getDescriptor().getOriginTool().getName() + " Command : " + command.getName();
	}

}
