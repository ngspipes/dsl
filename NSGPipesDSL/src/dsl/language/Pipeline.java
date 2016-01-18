package dsl.language;

import repository.IRepository;
import configurators.IConfigurator;
import descriptors.IToolDescriptor;
import dsl.entities.Argument;
import dsl.entities.Chain;
import dsl.entities.Command;
import dsl.entities.Output;
import dsl.entities.Tool;
import dsl.managers.Support;
import exceptions.DSLException;
import exceptions.RepositoryException;

public class Pipeline {
	
	public class PipeTool{
		public PipeCommandArgument command(String commandName) throws DSLException {
			Tool currentTool = ctx.getCurrentTool();
			ctx.addCommand(currentTool.createCommand(commandName));
			return new PipeCommandArgument();
		}
	}
	
	public class PipeCommandArgument{
		
		public PipeTool tool(String toolName, String configuratorName) throws DSLException {
			try{
				if(!ctx.repository.getToolsName().contains(toolName))
					throw new DSLException("Tool " + toolName + " doesn't exist!");	
			}catch(RepositoryException ex){
				throw new DSLException("Error getting tools names from repository", ex);
			}
			
			try{
				return tool(ctx.repository.getTool(toolName), ctx.repository.getConfigurationFor(toolName, configuratorName));	
			}catch(RepositoryException ex){
				throw new DSLException("Error getting tool " + toolName +  " from repository", ex);
			}
		}
		
		public PipeTool tool(IToolDescriptor toolDescriptor, IConfigurator configurator) throws DSLException {
			ctx.addTool(new Tool(toolDescriptor), configurator);
			return new PipeTool();
		}
		
		
		public PipeCommandArgument command(String commandName) throws DSLException {
			Tool currentTool = ctx.getCurrentTool();
			ctx.addCommand(currentTool.createCommand(commandName));
			return this;
		}

		
		public PipeCommandArgument argument(String argumentName, String value) throws DSLException {
			Argument argument = ctx.getCurrentCommand().getArgument(argumentName);
			
			if(argument == null)
				throw new DSLException("Argument " + argumentName + " doesn't exist!");
			
			argument.setValue(value);	
			return this;
		}
		
		private PipeCommandArgument chain(Output output, String argumentName) throws DSLException {
			Argument argument = ctx.getCurrentCommand().getArgument(argumentName);
			
			if(argument == null)
				throw new DSLException("Argument " + argumentName + " doesn't exist!");
			
			ctx.pipeline.addChain(new Chain(argument, output));
			return this;
		}
		
		public PipeCommandArgument chain(String argumentName, String outputName) throws DSLException {
			Output output = ctx.getPreviousCommand().getOutput(outputName);
			
			if(output == null)
				throw new DSLException("Output " + outputName + " doesn't exist!");
			
			return chain(output, argumentName);
		}
		
		public PipeCommandArgument chain(String argumentName, String commandName, String outputName) throws DSLException {
			Command command = ctx.getLastCommandOf(ctx.getCurrentTool(), commandName);
			
			if(command == null)
				throw new DSLException("There is no Command " + commandName + " at this point!");
			
			Output output = command.getOutput(outputName);
			
			if(output == null)
				throw new DSLException("Output " + outputName + " doesn't exist!");
			
			return chain(output, argumentName);			
		}
		
		public PipeCommandArgument chain(String argumentName, int commandPos, String commandName, String outputName) throws DSLException {
			Command command = ctx.getCommand(ctx.getCurrentTool(), commandName, commandPos);
			
			if(command == null)
				throw new DSLException("There is no Command " + commandName + " at this point!");
			
			Output output = command.getOutput(outputName);
			
			if(output == null)
				throw new DSLException("Output " + outputName + " doesn't exist!");
			
			return chain(output, argumentName);
		}
		
		public PipeCommandArgument chain(String argumentName, String toolName, String commandName, String outputName) throws DSLException {
			Tool tool = ctx.getLastTool(toolName);
			
			if(tool == null)
				throw new DSLException("There is no Tool " + toolName + " at this point!");
			
			Command command = ctx.getLastCommandOf(tool, commandName);
			
			if(command == null)
				throw new DSLException("There is no Command " + commandName + " at this point!");
			
			Output output = command.getOutput(outputName);
			
			if(output == null)
				throw new DSLException("Output " + outputName + " doesn't exist!");
			
			return chain(output, argumentName);
		}
		
		public PipeCommandArgument chain(String argumentName, int toolPos, String toolName, String commandName, String outputName) throws DSLException {
			Tool tool = ctx.getTool(toolName, toolPos);
			
			if(tool == null)
				throw new DSLException("There is no Tool " + toolName + " at this point!");
			
			Command command = ctx.getLastCommandOf(tool, commandName);
			
			if(command == null)
				throw new DSLException("There is no Command " + commandName + " at this point!");
			
			Output output = command.getOutput(outputName);
					
			if(output == null)
				throw new DSLException("Output " + outputName + " doesn't exist!");
			
			return chain(output, argumentName);
		}
		
		public PipeCommandArgument chain(String argumentName, int toolPos, String toolName, int commandPos, String commandName, String outputName) throws DSLException 	{
			Tool tool  = ctx.getTool(toolName, toolPos);
			
			if(tool == null)
				throw new DSLException("There is no Tool " + toolName + " at this point!");
			
			Command command = ctx.getCommand(tool, commandName, commandPos);
			
			if(command == null)
				throw new DSLException("There is no Command " + commandName + " at this point!");
			
			Output output = command.getOutput(outputName);
			
			if(output == null)
				throw new DSLException("Output " + outputName + " doesn't exist!");
			
			return chain(output, argumentName);
		}
		
		
		public dsl.entities.Pipeline build() throws DSLException {
			ctx.pipeline.build();
			return ctx.pipeline;
		}
		
	}
	
	
	private final Context ctx;
	
	
	public Pipeline() {
		this(null);
	}
	
	public Pipeline(String repositoryType, String repositoryLocation) throws RepositoryException {
		this(Support.getRepository(repositoryType, repositoryLocation));		
	}
	
	public Pipeline(IRepository repository) {
		this.ctx = new Context(repository, new dsl.entities.Pipeline());
	}
	
	
	public PipeTool tool(String toolName, String configuratorName) throws DSLException {
		try{
			if(!ctx.repository.getToolsName().contains(toolName))
				throw new DSLException("Tool " + toolName + " doesn't exist!");	
		}catch(RepositoryException ex){
			throw new DSLException("Error getting tools names from repository", ex);
		}
		
		try {
			return tool(ctx.repository.getTool(toolName), ctx.repository.getConfigurationFor(toolName, configuratorName));	
		} catch (RepositoryException ex) {
			throw new DSLException("Error getting tool " + toolName + " from repository", ex);
		}
	}

	public PipeTool tool(IToolDescriptor toolDescriptor, IConfigurator configurator) throws DSLException {
		ctx.addTool(new Tool(toolDescriptor), configurator);
		return new PipeTool();
	}
	
}
