package dsl.managers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.function.Function;

import support.TrimmomaticComposer;
import dsl.entities.Argument;

public class ArgumentsComposerManager {
		
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ComposerNameAnnotation {
		String name();
	}
	
	private static String compose(List<Argument> args, Function<Argument, String> func){
		StringBuilder sb = new StringBuilder();
		
		for(Argument arg : args)
			sb.append(func.apply(arg));
		
		return sb.toString();
	}
	
	private static String composeEndControled(List<Argument> args, Function<Argument, String> func, Function<Argument, String> endFunc){
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<args.size()-1; ++i)
			sb.append(func.apply(args.get(i)));
		
		sb.append(endFunc.apply(args.get(args.size()-1)));
		
		return sb.toString();
	}
	
	// Format: []
	@ComposerNameAnnotation(name = Support.COMPOSER_DUMMY_NAME)
	public static String dummy(List<Argument> args){
		return "";
	}

	// Format: [value value value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_SPACE_NAME)
	public static String valuesSeparatedBySpace(List<Argument> args){
		return compose(args, (arg)-> arg.getValue() + " ");
	}
	
	// Format: [name=value name=value name=value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_EQUAL_NAME)
	public static String nameValuesSeparatedByEqual(List<Argument> args){
		return compose(args, (arg)-> arg.getName()+"="+arg.getValue()+" ");
	}	

	// Format: [name:value name:value name:value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_COLON_NAME)
	public static String nameValuesSeparatedByColon(List<Argument> args){
		return compose(args, (arg)-> arg.getName()+":"+arg.getValue()+" "); 
	}
	
	// Format: [name-value name-value name-value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_HYPHEN_NAME)
	public static String nameValuesSeparatedByHyphen(List<Argument> args){
		return compose(args, (arg)-> arg.getName()+"-"+arg.getValue()+" "); 
	}

	// Format: [name value name value name value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_SPACE_NAME)
	public static String nameValuesSeparatedBySpace(List<Argument> args){
		return compose(args, (arg)-> arg.getName()+" "+arg.getValue()+" "); 
	}

	// Format: [value:value:value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_COLON_NAME)
	public static String valuesSeparatedByColon(List<Argument> args){ 
		return composeEndControled(args, (arg)->arg.getValue()+":", (arg)->arg.getValue());
	}

	// Format: [value|value|value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_VERTICAL_BAR_NAME)
	public static String valuesSeparatedByVerticalBar(List<Argument> args){ 
		return composeEndControled(args, (arg)->arg.getValue()+"|", (arg)->arg.getValue());
	}

	// Format: [value-value-value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_HYPHEN_NAME)
	public static String valuesSeparatedByHyphen(List<Argument> args){ 
		return composeEndControled(args, (arg)->arg.getValue()+"-", (arg)->arg.getValue());
	}

	// Format: [value/value/value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_SLASH_NAME)
	public static String valuesSeparatedBySlash(List<Argument> args){ 
		return composeEndControled(args, (arg)->arg.getValue()+"/", (arg)->arg.getValue());
	}

	// Format: [value,value,value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_COMMA_NAME)
	public static String valuesSeparatedByComma(List<Argument> args){ 
		return composeEndControled(args, (arg)->arg.getValue()+",", (arg)->arg.getValue());
	}
	
	// Format: [TRIMMOMATIC STYLE ArgCategory:arg:arg:arg]
	@ComposerNameAnnotation(name = Support.COMPOSER_TRIMMOMATIC_NAME)
	public static String trimmomatic(List<Argument> args){ 
		return new TrimmomaticComposer().compose(args);
	}
	
	// Format: [VELVETG STYLE all arguments has format [name value] except output_directory that has format [value]]
	@ComposerNameAnnotation(name = Support.COMPOSER_VELVETG_NAME)
	public static String velvetG(List<Argument> args){ 
		return compose(args, (arg)-> { 
									if(!arg.getName().equals(Support.VELVET_OUTPUT_DIRECTORY_ARGUMENT_NAME))
										return arg.getName()+" "+arg.getValue()+" ";
									else
										return arg.getValue()+" ";
									}); 		
	}

}
