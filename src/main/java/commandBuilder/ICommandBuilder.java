package commandBuilder;

import dsl.entities.Command;
import exceptions.CommandBuilderException;

@FunctionalInterface
public interface ICommandBuilder {

	public void build(Command command) throws CommandBuilderException;

}
