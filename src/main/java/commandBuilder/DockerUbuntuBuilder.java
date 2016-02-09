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
package commandBuilder;

import configurators.IConfigurator;
import dsl.entities.Command;
import exceptions.CommandBuilderException;

public class DockerUbuntuBuilder implements ICommandBuilder{

	private static final String VM_OUTPUT_DIRECTORY_PATH = "/home/ngspipes/Outputs/";
	private static final String SHARE_OUTPUT_DIRECTORY_PATH = "/shareOutputs/";
	private static final String VM_INPUT_DIRECTORY_PATH = "/home/ngspipes/Inputs/";
	private static final String SHARE_INPUT_DIRECTORY_PATH = "/shareInputs/";
	private static final String UBUNTU_ROOT_COMMAND = "sudo";
	private static final String OUTPUT_VOLUMECOMMAND = "-v " + VM_OUTPUT_DIRECTORY_PATH + ":" + SHARE_OUTPUT_DIRECTORY_PATH + ":rw";
	private static final String INPUT_VOLUMECOMMAND = "-v " + VM_INPUT_DIRECTORY_PATH + ":" + SHARE_INPUT_DIRECTORY_PATH + ":rw";
	private static final String SEPARATOR = " ";
	private static final String DOCKER_RUN_COMMAND = "docker run";


	private final String IMAGE_NAME;

	public DockerUbuntuBuilder(IConfigurator config) {
		this.IMAGE_NAME = config.getUri();
	}

	@Override
	public void build(Command command) throws CommandBuilderException {
		try {

			CommandBuilderUtils.updateArgumentsValue(command, SHARE_INPUT_DIRECTORY_PATH, SHARE_OUTPUT_DIRECTORY_PATH);

			StringBuilder runnableCommand = new StringBuilder(UBUNTU_ROOT_COMMAND);
			runnableCommand .append(SEPARATOR).append(DOCKER_RUN_COMMAND).append(SEPARATOR)
							.append(INPUT_VOLUMECOMMAND).append(SEPARATOR)
							.append(OUTPUT_VOLUMECOMMAND).append(SEPARATOR).append(IMAGE_NAME)
							.append(SEPARATOR).append(command.getCommand());

			command.setCommand(runnableCommand.toString());

		} catch(Exception e) {
			throw new CommandBuilderException("Error running command " + command.getName(), e);
		}
	}

}
