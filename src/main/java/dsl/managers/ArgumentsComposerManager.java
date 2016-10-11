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
import exceptions.DSLException;
import support.TrimmomaticComposer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class ArgumentsComposerManager {
		
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ComposerNameAnnotation {
		String name();
	}

	public static String compose(List<Argument> args) throws DSLException {

		if(!args.isEmpty() && args.get(0).getOriginCommand().getName().equals("trimmomatic"))
			return trimmomatic(args);

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

	// Format: [TRIMMOMATIC STYLE ArgCategory:arg:arg:arg]
	public static String trimmomatic(List<Argument> args){ 
		return new TrimmomaticComposer().compose(args);
	}

}
