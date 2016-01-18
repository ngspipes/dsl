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
