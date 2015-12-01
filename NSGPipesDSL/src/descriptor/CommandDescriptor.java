package descriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDescriptor implements ICommandDescriptor{

	private IToolDescriptor originTool;
	public IToolDescriptor getOriginTool(){ return originTool; }
	public void setOriginTool(IToolDescriptor originTool){ this.originTool = originTool; }
	
	private final String name;
	private final String command;
	private final String description;
	private final String argumentsProcessor;
	private final List<IArgumentDescriptor> arguments;
	private final List<IOutputDescriptor> outputs;
	private final Map<String, IArgumentDescriptor> args;
	private final Map<String, IOutputDescriptor> otps;
	
	public CommandDescriptor(String name, String command, String description, String argumentsProcessor, 
							List<IArgumentDescriptor> arguments, List<IOutputDescriptor> outputs) {
		this.name = name;
		this.command = command;
		this.description = description;
		this.argumentsProcessor = argumentsProcessor;
		this.arguments = arguments;
		this.outputs = outputs;
		this.args = new HashMap<>();
		this.otps = new HashMap<>();
		
		for(IArgumentDescriptor arg : arguments){
			arg.setOriginCommand(this);
			args.put(arg.getName(), arg);
		}
			
		for(IOutputDescriptor out : outputs){
			out.setOriginCommand(this);
			otps.put(out.getName(), out);
		}
			
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public List<IArgumentDescriptor> getArguments() {
		return arguments;
	}

	@Override
	public List<IOutputDescriptor> getOutputs() {
		return outputs;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getArgumentsProcessor(){
		return argumentsProcessor;
	}
	
	@Override
	public IArgumentDescriptor getArgument(String argumentName) {
		return args.get(argumentName);
	}


	@Override
	public IOutputDescriptor getOutput(String outputName) {
		return otps.get(outputName);
	}
	
}
