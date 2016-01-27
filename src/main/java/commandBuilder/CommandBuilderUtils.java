package commandBuilder;

import dsl.ArgumentValidator;
import dsl.entities.Argument;
import dsl.entities.Chain;
import dsl.entities.Command;

public class CommandBuilderUtils {
	
	public static void updateArgumentsValue(Command command, String inputsDirectory, String outputsDirectory) {
		for (Argument arg : command.getArguments()) {
			if(arg.getValue() != null && isFileOrDirectory(arg) && !isChainArgument(arg)) {
				if(!command.getDependentsOf(arg).isEmpty())
					arg.setValue(outputsDirectory + arg.getValue());
				else
					arg.setValue(inputsDirectory + arg.getValue());
			}
		}
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
