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
package support;

import argmentsComposer.IArgumentsComposer;
import descriptors.IArgumentDescriptor;
import dsl.entities.Argument;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TrimmomaticComposer implements IArgumentsComposer{
		 
	public static final String WITHOUT_GROUP_NAME = "";
	public static final String ILLUMINACLIP_GROUP_NAME = "ILLUMINACLIP";
	public static final String SLIDINGWINDOW_GROUP_NAME = "SLIDINGWINDOW";
	public static final String LEADING_GROUP_NAME = "LEADING";
	public static final String TRAILING_GROUP_NAME = "TRAILING";
	public static final String CROP_GROUP_NAME = "CROP";
	public static final String HEADCROP_GROUP_NAME = "HEADCROP";
	public static final String MINLEN_GROUP_NAME = "MINLEN";
	
	public static final String THREAD_ARGUMENT_NAME = "threads";
	public static final String MODE_ARGUMENT_NAME = "mode";
	public static final String QUALITY_ARGUMENT_NAME = "quality";
	public static final String TRIM_LOG_ARGUMENT_NAME = "trimlog";
	
	public static final String INPUT_FILE_SE_ARGUMENT_NAME = "inputFile";
	public static final String OUTPUT_FILE_SE_ARGUMENT_NAME = "outputFile";
	
	public static final String INPUT_FILE_1_PE_ARGUMENT_NAME = "paired input 1";
	public static final String INPUT_FILE_2_PE_ARGUMENT_NAME = "paired input 2";
	public static final String OUTPUT_FILE_PAIRED_1_PE_ARGUMENT_NAME = "paired output 1";
	public static final String OUTPUT_FILE_UNPAIRED_1_PE_ARGUMENT_NAME = "unpaired output 1";
	public static final String OUTPUT_FILE_PAIRED_2_PE_ARGUMENT_NAME = "paired output 2";
	public static final String OUTPUT_FILE_UNPAIRED_2_PE_ARGUMENT_NAME = "unpaired output 2";
	
	public static final String PAIRED_MODE_NAME = "PE";
	public static final String SINGLE_MODE_NAME = "SE";
	
	private static final Map<String, String> ARGS_GROUP = new HashMap<>();
	

	static {
		ARGS_GROUP.put("fastaWithAdaptersEtc", ILLUMINACLIP_GROUP_NAME);
		ARGS_GROUP.put("seed mismatches", ILLUMINACLIP_GROUP_NAME);
		ARGS_GROUP.put("palindrome clip threshold", ILLUMINACLIP_GROUP_NAME);
		ARGS_GROUP.put("simple clip threshold", ILLUMINACLIP_GROUP_NAME);
		ARGS_GROUP.put("windowSize", SLIDINGWINDOW_GROUP_NAME);
		ARGS_GROUP.put("requiredQuality", SLIDINGWINDOW_GROUP_NAME);
		ARGS_GROUP.put("leading quality", LEADING_GROUP_NAME);
		ARGS_GROUP.put("trailing quality", TRAILING_GROUP_NAME);
		ARGS_GROUP.put("crop length", CROP_GROUP_NAME);
		ARGS_GROUP.put("headcrop length", HEADCROP_GROUP_NAME);
		ARGS_GROUP.put("minlen length", MINLEN_GROUP_NAME);	
	}
	

	private static Map<String, Map<String, Argument>> convertToGroupMap(List<Argument> args){
		Map<String, Map<String, Argument>> arguments = new HashMap<>();
		
		String group;
		for(Argument argument : args){
			group = getGroup(argument.getDescriptor());
			if(!arguments.containsKey(group))
					arguments.put(group, new LinkedHashMap<>());
			
			arguments.get(group).put(argument.getName(), argument);
		}
		
		return arguments;
	}
	
	private static String getGroup(IArgumentDescriptor arg) {
		String argName = arg.getName();
		if(ARGS_GROUP.containsKey(argName))
			return ARGS_GROUP.get(arg.getName());
		return WITHOUT_GROUP_NAME;
	}

	private static String getWithoutName(Map<String, Map<String, Argument>> arguments){
		Map<String, Argument> withoutName = arguments.get(WITHOUT_GROUP_NAME);
		
		StringBuilder sb = new StringBuilder(withoutName.get(MODE_ARGUMENT_NAME).getValue()).append(" ");
		
		
		if(withoutName.containsKey(THREAD_ARGUMENT_NAME))
			sb.append("-").append(THREAD_ARGUMENT_NAME).append(" ").append(withoutName.get(THREAD_ARGUMENT_NAME).getValue()).append(" ");
		
		sb.append(withoutName.get(QUALITY_ARGUMENT_NAME).getValue()).append(" ");
		
		if(withoutName.containsKey(TRIM_LOG_ARGUMENT_NAME))
			sb.append("-").append(TRIM_LOG_ARGUMENT_NAME).append(" ").append(withoutName.get(TRIM_LOG_ARGUMENT_NAME).getValue()).append(" ");
			
			
		if(withoutName.get(MODE_ARGUMENT_NAME).getValue().equals(PAIRED_MODE_NAME)){
			sb.append(withoutName.get(INPUT_FILE_1_PE_ARGUMENT_NAME).getValue()).append(" ");
			sb.append(withoutName.get(INPUT_FILE_2_PE_ARGUMENT_NAME).getValue()).append(" ");
			sb.append(withoutName.get(OUTPUT_FILE_PAIRED_1_PE_ARGUMENT_NAME).getValue()).append(" ");
			sb.append(withoutName.get(OUTPUT_FILE_UNPAIRED_1_PE_ARGUMENT_NAME).getValue()).append(" ");
			sb.append(withoutName.get(OUTPUT_FILE_PAIRED_2_PE_ARGUMENT_NAME).getValue()).append(" ");
			sb.append(withoutName.get(OUTPUT_FILE_UNPAIRED_2_PE_ARGUMENT_NAME).getValue()).append(" ");
		} else {
			sb.append(withoutName.get(INPUT_FILE_SE_ARGUMENT_NAME).getValue()).append(" ");
			sb.append(withoutName.get(OUTPUT_FILE_SE_ARGUMENT_NAME).getValue()).append(" ");
		}
		
		return sb.toString();
	} 
	
	private static String get(Map<String, Map<String, Argument>> arguments, String group){
		if(!arguments.containsKey(group))
			return "";
		
		Map<String, Argument> args = arguments.get(group);
		
		StringBuilder sb = new StringBuilder(group).append(":");
		for(Entry<String, Argument> entry : args.entrySet())
			sb.append(entry.getValue().getValue()).append(":");
		
		return sb.toString().substring(0, sb.length()-":".length());
	}

	
	@Override
	public String compose(List<Argument> args){
		Map<String, Map<String, Argument>> arguments = convertToGroupMap(args);
		
		StringBuilder compose = new StringBuilder(getWithoutName(arguments)).append(" ");
				
		compose.append(get(arguments, ILLUMINACLIP_GROUP_NAME)).append(" ");
		compose.append(get(arguments, SLIDINGWINDOW_GROUP_NAME)).append(" ");
		compose.append(get(arguments, LEADING_GROUP_NAME)).append(" ");
		compose.append(get(arguments, TRAILING_GROUP_NAME)).append(" ");
		compose.append(get(arguments, CROP_GROUP_NAME)).append(" ");
		compose.append(get(arguments, HEADCROP_GROUP_NAME)).append(" ");
		compose.append(get(arguments, MINLEN_GROUP_NAME)).append(" ");
		
		return compose.toString();
	}
		
}
