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

import dsl.ArgumentValidator;
import dsl.Log;
import dsl.entities.Argument;
import exceptions.DSLException;
import support.TrimmomaticComposer;
import utils.IO;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ArgumentsComposerManager {

	private static Map<String, Function<List<Argument>, String>> SPECIAL_CASES = new HashMap<>();

	static {
		SPECIAL_CASES.put("trimmomatic", ArgumentsComposerManager::trimmomatic);
		SPECIAL_CASES.put("velvetg", ArgumentsComposerManager::velvetG);
		SPECIAL_CASES.put("bowtie2-build", ArgumentsComposerManager::bowtie2Build);
		SPECIAL_CASES.put("bowtie2", ArgumentsComposerManager::bowtie2);
		SPECIAL_CASES.put("create_sample_dirs", ArgumentsComposerManager::createSamplesDir);
		SPECIAL_CASES.put("create_snp_matrix", ArgumentsComposerManager::snpPipeline);
		SPECIAL_CASES.put("call_consensus", ArgumentsComposerManager::snpPipeline);
		SPECIAL_CASES.put("create_snp_list", ArgumentsComposerManager::snpPipeline);
		SPECIAL_CASES.put("mpileup2snp", ArgumentsComposerManager::varscan);
		SPECIAL_CASES.put("view", ArgumentsComposerManager::samtools);
		SPECIAL_CASES.put("mpileup", ArgumentsComposerManager::samtools);
		SPECIAL_CASES.put("sort", ArgumentsComposerManager::samtools);
		SPECIAL_CASES.put("join", ArgumentsComposerManager::join);
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface ComposerNameAnnotation {
		String name();
	}

	public static String compose(List<Argument> args) throws DSLException {
		Log.log(args.get(0).getOriginCommand().getName());

		if(!args.isEmpty() && SPECIAL_CASES.containsKey(args.get(0).getOriginCommand().getName()))
			return SPECIAL_CASES.get(args.get(0).getOriginCommand().getName()).apply(args);

		StringBuilder sb = new StringBuilder();
		Argument arg;

		for(int i=0; i<args.size()-1; ++i) {
			arg = args.get(i);
			sb.append(arg.getComposer().compose(arg));
			sb.append(" ");
		}

		if(!args.isEmpty()) {
			arg = args.get(args.size() - 1);
			sb.append(arg.getComposer().compose(arg));
		}

		return sb.toString();
	}
	
	// Format: []
	@ComposerNameAnnotation(name = Support.COMPOSER_DUMMY_NAME)
	public static String dummy(Argument arg){
		return "";
	}

	// Format: [value]
	@ComposerNameAnnotation(name = Support.COMPOSER_VALUES_SEPARATED_BY_SPACE_NAME)
	public static String valuesSeparatedBySpace(Argument arg){
		return arg.getValue();
	}
	
	// Format: [name=value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_EQUAL_NAME)
	public static String nameValuesSeparatedByEqual(Argument arg) {
		return arg.getName()+"="+arg.getValue();
	}	

	// Format: [name:value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_COLON_NAME)
	public static String nameValuesSeparatedByColon(Argument arg) {
		return arg.getName()+":"+arg.getValue();
	}
	
	// Format: [name-value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_HYPHEN_NAME)
	public static String nameValuesSeparatedByHyphen(Argument arg) {
		return arg.getName()+"-"+arg.getValue();
	}

	// Format: [name value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_SPACE_NAME)
	public static String nameValuesSeparatedBySpace(Argument arg) {
		return arg.getName()+" "+arg.getValue();
	}

	// Format: [\ name=value]
	@ComposerNameAnnotation(name = Support.COMPOSER_NAME_VALUES_SEPARATED_BY_EQUAL_AND_BACKSLASH_NAME)
	public static String nameValuesSeparatedByEqualAndBackslash(Argument arg){
		return "\\ " + arg.getName()+"="+arg.getValue();
	}

	// Format: [TRIMMOMATIC STYLE ArgCategory:arg:arg:arg]
	public static String trimmomatic(List<Argument> args){ 
		return new TrimmomaticComposer().compose(args);
	}

	// Format: [Bowtie2-build]
	public static String bowtie2Build(List<Argument> args ){
		StringBuilder sb = new StringBuilder();
		for (Argument arg :	args) {
			sb.append(arg.getValue()).append(" ");
		}
		return sb.toString();
	}

	// Format: [Bowtie2]
	public static String bowtie2(List<Argument> args){
		StringBuilder sb = new StringBuilder();
		for (Argument arg :	args) {
			if (arg.getType().equals("flag"))
				sb.append(arg.getValue()).append(" ");
			else
				sb.append(nameValuesSeparatedBySpace(arg)).append(" ");
		}
		return sb.toString();
	}

	// Format [create_sample_dirs]
	public static String createSamplesDir(List<Argument> args){
		StringBuilder sb = new StringBuilder();
		for (Argument arg :	args) {
			if(arg.getName().equals("-d"))
				sb.append(nameValuesSeparatedBySpace(arg)).append(" ");
			else
				sb.append(" > ").append(arg.getValue());
		}
		return sb.toString();
	}

	// Format [Join]
	public static String join(List<Argument> args){
		StringBuilder sb = new StringBuilder();
		for (Argument arg :	args) {
			if(arg.getName().equals("outputName"))
				sb.append(" > ").append(arg.getValue());
			else
				sb.append(arg.getValue()).append(" ");
		}
		return sb.toString();
	}

	// Format [Split]
	public static String split(List<Argument> args){
		StringBuilder sb = new StringBuilder();
		for (Argument arg :	args) {
			if(!arg.getName().equals("inputName"))
				sb.append(nameValuesSeparatedByEqual(arg)).append(" ");
			else
				sb.append(arg.getValue()).append(" ");
		}
		return sb.toString();
	}

	// Format [SnpPipeline]
	public static String snpPipeline(List<Argument> args){
		StringBuilder sb = new StringBuilder();
		for (Argument arg :	args) {
			if(arg.getName().equals("sampleDirsFile") || arg.getName().equals("allPileupFile"))
				sb.append(arg.getValue()).append(" ");
			else
				sb.append(nameValuesSeparatedBySpace(arg)).append(" ");
		}
		return sb.toString();
	}

	// Format [Samtools]w
	public static String samtools(List<Argument> args){
		StringBuilder sb = new StringBuilder();
		for (Argument arg :	args) {
			if(arg.getType().equals("self-sufficient"))
				sb.append(arg.getName()).append(" ");
			else {
				if(arg.getName().equals("input"))
					sb.append(arg.getValue()).append(" ");
				else
					sb.append(nameValuesSeparatedBySpace(arg)).append(" ");
			}
		}
		return sb.toString();
	}

	// Format [VarScan]
	public static String varscan(List<Argument> args){
		StringBuilder sb = new StringBuilder();
		for (Argument arg :	args) {
			if(arg.getName().equals("mpileupFile"))
				sb.append(arg.getValue()).append(" ");
			else {
				if(arg.getName().equals("output"))
					sb.append(">> ").append(arg.getValue()).append(" ");
				else
					sb.append(nameValuesSeparatedBySpace(arg)).append(" ");
			}
		}
		return sb.toString();
	}

	// Format [VELVETG]
	public static String velvetG(List<Argument> args){
		StringBuilder sb = new StringBuilder();
		Log.log("velvetg");
		for (Argument arg: args) {
			if(!arg.getName().equals("output_directory")) {
				Log.log("not output dir");
				sb.append(arg.getName()).append(" ").append(arg.getValue()).append(" ");
			} else
				sb.append(arg.getValue()).append(" ");
		}

		return sb.toString();
	}
}
