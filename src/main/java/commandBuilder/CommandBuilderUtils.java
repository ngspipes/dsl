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

import dsl.ArgumentValidator;
import dsl.entities.Argument;
import dsl.entities.Chain;
import dsl.entities.Command;

public class CommandBuilderUtils {
	
	public static void updateArgumentsValue(Command command, String inputsDirectory, String outputsDirectory) {
		command.getArguments().forEach((arg)->{
			if(arg.getValue() != null && isFileOrDirectory(arg) && !isChainArgument(arg)) {
				if(!command.getDependentsOf(arg).isEmpty())
					arg.setValue(outputsDirectory + arg.getValue());
				else
					arg.setValue(inputsDirectory + arg.getValue());
			}
		});
	}

	private static boolean isChainArgument(Argument arg) {
		for(Chain chain  : arg.getOriginCommand().getOriginStep().getOriginPipe().getChains())
			if(chain.getArgument() == arg)
				return true;
		
		return false;
	}

	private static boolean isFileOrDirectory(Argument arg){
		return arg.getType().equals(ArgumentValidator.FILE_TYPE_NAME)
		|| arg.getType().equals(ArgumentValidator.DIRECTORY_TYPE_NAME);
	}
}
