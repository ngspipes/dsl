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
package dsl.managers;

import dsl.entities.Argument;
import support.TrimmomaticComposer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.function.Function;

public class ArgumentsComposerManager {
		
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ComposerNameAnnotation {
		String name();
	}
	
	private static String compose(List<Argument> args, Function<Argument, String> func){
		StringBuilder sb = new StringBuilder();
		
		for(Argument arg : args)
			sb.append(func.apply(arg));
		
		return sb.toString();
	}
	
	private static String composeEndControlled(List<Argument> args, Function<Argument, String> func, Function<Argument, String> endFunc){
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
		return composeEndControlled(args, (arg)->arg.getValue()+":", Argument::getValue);
	}

	// Format: [value|value|value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_VERTICAL_BAR_NAME)
	public static String valuesSeparatedByVerticalBar(List<Argument> args){ 
		return composeEndControlled(args, (arg)->arg.getValue()+"|", Argument::getValue);
	}

	// Format: [value-value-value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_HYPHEN_NAME)
	public static String valuesSeparatedByHyphen(List<Argument> args){ 
		return composeEndControlled(args, (arg)->arg.getValue()+"-", Argument::getValue);
	}

	// Format: [value/value/value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_SLASH_NAME)
	public static String valuesSeparatedBySlash(List<Argument> args){ 
		return composeEndControlled(args, (arg)->arg.getValue()+"/", Argument::getValue);
	}

	// Format: [value,value,value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_COMMA_NAME)
	public static String valuesSeparatedByComma(List<Argument> args){ 
		return composeEndControlled(args, (arg)->arg.getValue()+",", Argument::getValue);
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
