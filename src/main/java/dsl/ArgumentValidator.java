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
package dsl;

import dsl.entities.Argument;
import exceptions.DSLException;

import java.util.HashMap;
import java.util.Map;

public class ArgumentValidator {
	
	public static final class ArgumentValidationException extends DSLException{
	
		private static final long serialVersionUID = 1L;

		public ArgumentValidationException() {}
		
		public ArgumentValidationException(String msg) { super(msg);}
		
		public ArgumentValidationException(String msg, Throwable cause){ super(msg, cause); }
		
	}
	
	@FunctionalInterface
	public interface Validator{
		void validate(String value) throws ArgumentValidationException;
	}
	
	public static final String INT_TYPE_NAME = "int";
	public static final String STRING_TYPE_NAME = "string";
	public static final String FILE_TYPE_NAME = "file";
	public static final String DOUBLE_TYPE_NAME = "double";
	public static final String DIRECTORY_TYPE_NAME = "directory";
	
	public static final Map<String, Validator> VALIDATORS;
	
	static{
		VALIDATORS = new HashMap<>();
		
		VALIDATORS.put(INT_TYPE_NAME, ArgumentValidator::validateInt);
		VALIDATORS.put(STRING_TYPE_NAME, ArgumentValidator::validateString);
		VALIDATORS.put(FILE_TYPE_NAME, ArgumentValidator::validateFile);
		VALIDATORS.put(DOUBLE_TYPE_NAME, ArgumentValidator::validateDouble);
		VALIDATORS.put(DIRECTORY_TYPE_NAME, ArgumentValidator::validateDirectory);
	}
	
	private static void validateString(String value){}
	
	private static void validateDirectory(String value) {}
	
	private static void validateFile(String value) {}
	
	private static void validateInt(String value) throws ArgumentValidationException{
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			throw new ArgumentValidationException(value + " is not an Int value!");
		}
	}
	
	private static void validateDouble(String value) throws ArgumentValidationException {
		try {
			Double.parseDouble(value);
		} catch (Exception e) {
			throw new ArgumentValidationException(value + " is not an Float value!");
		}
	}
	
	
	public static void validate(Argument argument) throws ArgumentValidationException {
		Validator validator = ArgumentValidator.VALIDATORS.get(argument.getType());
		if (validator == null) {
            throw new ArgumentValidationException(
                    "No validator found for argument "
                            + argument.getName()
                            + " with type " + argument.getType());
        }
		try{
			validator.validate(argument.getValue());	
		}catch(ArgumentValidationException ex){
			throw new ArgumentValidationException("Invalid Argument "
					+ argument.getName() + " of type "
					+ argument.getType() + " from Command "
					+ argument.getOriginCommand().getName()
					+ " : " + ex.getMessage());
		}	
	}
	
}
