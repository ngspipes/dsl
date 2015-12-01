package dsl.managers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.function.Function;

import support.algorithms.trimmomatic.Trimmomatic;
import dsl.entities.Argument;

public class ArgumentsProcessorManager {
		
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ProcessorNameAnnotation {
		String name();
	}
	
	private static String process(List<Argument> args, Function<Argument, String> func){
		StringBuilder sb = new StringBuilder();
		
		for(Argument arg : args)
			sb.append(func.apply(arg));
		
		return sb.toString();
	}
	
	private static String processEndControled(List<Argument> args, Function<Argument, String> func, Function<Argument, String> endFunc){
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<args.size()-1; ++i)
			sb.append(func.apply(args.get(i)));
		
		sb.append(endFunc.apply(args.get(args.size()-1)));
		
		return sb.toString();
	}
	
	// Format: []
	@ProcessorNameAnnotation(name = Support.PROCESSOR0_NAME)
	public static String processor0(List<Argument> args){
		return "";
	}

	// Format: [value value value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR1_NAME)
	public static String processor1(List<Argument> args){
		return process(args, (arg)-> arg.getValue() + " ");
	}
	
	// Format: [name=value name=value name=value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR2_NAME)
	public static String processor2(List<Argument> args){
		return process(args, (arg)-> arg.getName()+"="+arg.getValue()+" ");
	}	

	// Format: [name:value name:value name:value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR3_NAME)
	public static String processor3(List<Argument> args){
		return process(args, (arg)-> arg.getName()+":"+arg.getValue()+" "); 
	}
	
	// Format: [name-value name-value name-value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR4_NAME)
	public static String processor4(List<Argument> args){
		return process(args, (arg)-> arg.getName()+"-"+arg.getValue()+" "); 
	}

	// Format: [name value name value name value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR5_NAME)
	public static String processor5(List<Argument> args){
		return process(args, (arg)-> arg.getName()+" "+arg.getValue()+" "); 
	}

	// Format: [value:value:value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR6_NAME)
	public static String processor6(List<Argument> args){ 
		return processEndControled(args, (arg)->arg.getValue()+":", (arg)->arg.getValue());
	}

	// Format: [value|value|value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR7_NAME)
	public static String processor7(List<Argument> args){ 
		return processEndControled(args, (arg)->arg.getValue()+"|", (arg)->arg.getValue());
	}

	// Format: [value-value-value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR8_NAME)
	public static String processor8(List<Argument> args){ 
		return processEndControled(args, (arg)->arg.getValue()+"-", (arg)->arg.getValue());
	}

	// Format: [value/value/value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR9_NAME)
	public static String processor9(List<Argument> args){ 
		return processEndControled(args, (arg)->arg.getValue()+"/", (arg)->arg.getValue());
	}

	// Format: [value,value,value]
	@ProcessorNameAnnotation(name = Support.PROCESSOR10_NAME)
	public static String processor10(List<Argument> args){ 
		return processEndControled(args, (arg)->arg.getValue()+",", (arg)->arg.getValue());
	}
	
	// Format: ["TRIMMOMATIC STYLE"]
	@ProcessorNameAnnotation(name = Support.PROCESSOR_TRIMMOMATIC_NAME)
	public static String processorTrimmomatic(List<Argument> args){ 
		return Trimmomatic.process(args);
	}
	
	// Format: ["VELVETG STYLE"]
	@ProcessorNameAnnotation(name = Support.PROCESSOR_VELVETG_NAME)
	public static String processorVelvetG(List<Argument> args){ 
		return process(args, (arg)-> { 
									if(!arg.getName().equals(Support.VELVET_OUTPUT_DIRECTORY_ARGUMENT_NAME))
										return arg.getName()+" "+arg.getValue()+" ";
									else
										return arg.getValue()+" ";
									}); 		
	}

}
