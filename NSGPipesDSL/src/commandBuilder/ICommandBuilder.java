package commandBuilder;

import dsl.entities.Command;
import dsl.entities.Output;
import exceptions.CommandBuilderException;

public interface ICommandBuilder {
	
	public void build(Command command) throws CommandBuilderException;
	
	public String getOutputValue(Output output);

}
