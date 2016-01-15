package argmentsComposer;

import java.util.List;

import dsl.entities.Argument;
import exceptions.DSLException;

@FunctionalInterface
public interface IArgumentsComposer {
	public String compose(List<Argument> args) throws DSLException;
}