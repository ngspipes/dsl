package argmentsProcessor;

import java.util.List;

import dsl.entities.Argument;
import exceptions.DSLException;

@FunctionalInterface
public interface IArgumentsProcessor {
	public String process(List<Argument> args) throws DSLException;
}