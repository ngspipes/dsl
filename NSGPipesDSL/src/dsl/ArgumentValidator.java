package dsl;

import java.util.HashMap;
import java.util.Map;

import dsl.entities.Argument;
import exceptions.DSLException;

public class ArgumentValidator {
	
	public static final class ArgumentValidationException extends DSLException{
	
		private static final long serialVersionUID = 1L;

		public ArgumentValidationException() {}
		
		public ArgumentValidationException(String msg) { super(msg);}
		
		public ArgumentValidationException(String msg, Throwable cause){ super(msg, cause); }
		
	}
	
	@FunctionalInterface
	public static interface Validator{
		public void validate(String value) throws ArgumentValidationException;
	}
	
	public static final String INT_TYPE_NAME = "int";
	public static final String STRING_TYPE_NAME = "string";
	public static final String FILE_TYPE_NAME = "file";
	public static final String FLOAT_TYPE_NAME = "float";
	public static final String OUT_DIRECTORY_TYPE_NAME = "outputDir";
	public static final String IN_DIRECTORY_TYPE_NAME = "inputDir";
	
	public static final Map<String, Validator> VALIDATORS;
	
	static{
		VALIDATORS = new HashMap<String, Validator>();
		
		VALIDATORS.put(INT_TYPE_NAME, ArgumentValidator::validateInt);
		VALIDATORS.put(STRING_TYPE_NAME, ArgumentValidator::validateString);
		VALIDATORS.put(FILE_TYPE_NAME, ArgumentValidator::validateFile);
		VALIDATORS.put(FLOAT_TYPE_NAME, ArgumentValidator::validateFloat);
		VALIDATORS.put(OUT_DIRECTORY_TYPE_NAME, ArgumentValidator::validateDirectory);
		VALIDATORS.put(IN_DIRECTORY_TYPE_NAME, ArgumentValidator::validateDirectory);
	}
	
	private static void validateString(String value){}
	
	private static void validateDirectory(String value) throws ArgumentValidationException {}
	
	private static void validateFile(String value) throws ArgumentValidationException {}
	
	private static void validateInt(String value) throws ArgumentValidationException{
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			throw new ArgumentValidationException(value + " is not an Int value!");
		}
	}
	
	private static void validateFloat(String value) throws ArgumentValidationException {
		try {
			Float.parseFloat(value);
		} catch (Exception e) {
			throw new ArgumentValidationException(value + " is not an Float value!");
		}
	}
	
	
	public static void validate(Argument argument) throws ArgumentValidationException {
		Validator validator = ArgumentValidator.VALIDATORS.get(argument.getType());
		
		try{
			validator.validate(argument.getValue());	
		}catch(ArgumentValidationException ex){
			throw new ArgumentValidationException("Invalid Argument " + argument.getName() + " from Command " + argument.getOriginCommand().getName() + " : " + ex.getMessage());
		}	
	}
	
}
