package dsl.managers;

import commandBuilder.CommandBuilder;
import commandBuilder.DockerUbuntuBuilder;
import commandBuilder.ICommandBuilder;

import configurators.IConfigurator;

public class CommandBuilderManager {

	public static ICommandBuilder localBuilder(IConfigurator config) {
		return new CommandBuilder();
	}
	
	public static ICommandBuilder dockerBuilder(IConfigurator config) {
		return new DockerUbuntuBuilder(config);
	}

}

