package commandBuilder;

import dsl.entities.Command;
import dsl.entities.Output;
import exceptions.CommandBuilderException;

public class CommandBuilder implements ICommandBuilder {

	@Override
	public void build(Command command) throws CommandBuilderException {}

	@Override
	public String getOutputValue(Output output) {
		return null;
	}
		
}
