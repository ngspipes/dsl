package dsl.managers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import repository.IRepository;
import argmentsComposer.IArgumentsComposer;

import commandBuilder.ICommandBuilder;

import configurator.IConfigurator;
import descriptor.IToolDescriptor;
import dsl.entities.Argument;
import dsl.managers.ArgumentsComposerManager.ComposerNameAnnotation;
import exceptions.CommandBuilderException;
import exceptions.ConfiguratorException;
import exceptions.DSLException;
import exceptions.DescriptorException;
import exceptions.RepositoryException;

public class Support {

	public static final List<String> SUPPORTED_TYPES = new LinkedList<>();;
	public static final String JSON_TYPE= "json";
	public static final String XML_TYPE= "xml";

	private static final Map<String, ConfiguratorFactory> CONFIGURATOR_FACTORIES = new HashMap<>();
	public static final Map<String, RepositoryFactory> REPOSITORY_FACTORIES = new HashMap<>();
	private static final Map<String, ToolDescriptorFactory> TOOL_DESCRIPTOR_FACTORIES = new HashMap<>();
	public static final Map<String, IArgumentsComposer> COMPOSERS = new HashMap<>();
	public static final Map<String, CommanBuilderFactory> COMMAND_BUILDER_FACTORIES = new HashMap<>();
	
	
	static{
		loadConfiguratorFactories();
		loadRepositoryFactories();
		loadToolDescriptorFactories();
		loadArgumentsComposers();
		loadCommandBuilderFactories();
		
		SUPPORTED_TYPES.add(JSON_TYPE);
		SUPPORTED_TYPES.add(XML_TYPE);
	}

	//CONFIGURATORS
	@FunctionalInterface
	private static interface ConfiguratorFactory{
		public IConfigurator create(String content) throws ConfiguratorException;
	}
	
	private static void loadConfiguratorFactories(){
		CONFIGURATOR_FACTORIES.put(Support.JSON_TYPE, ConfiguratorManager::JSONFactory);
		CONFIGURATOR_FACTORIES.put(Support.XML_TYPE, ConfiguratorManager::XMLFactory);
	}
	
	public static IConfigurator getConfigurator(String type, String content) throws ConfiguratorException {
		return CONFIGURATOR_FACTORIES.get(type).create(content);
	}




	//REPOSITORY
	@FunctionalInterface
	public static interface RepositoryFactory{
		public IRepository create(String location) throws RepositoryException;
	}

	public static final String REPOSITORY_URI_BASED = "UriBased";
	public static final String REPOSITORY_LOCAL = "Local";
	public static final String REPOSITORY_GITHUB = "Github";

	private static void loadRepositoryFactories(){
		REPOSITORY_FACTORIES.put(REPOSITORY_URI_BASED, RepositoryManager::createUriBased);
		REPOSITORY_FACTORIES.put(REPOSITORY_LOCAL, RepositoryManager::createLocal);   
		REPOSITORY_FACTORIES.put(REPOSITORY_GITHUB, RepositoryManager::createGithub);    	
	}

	public static IRepository getRepository(String type, String location) throws RepositoryException {
		return REPOSITORY_FACTORIES.get(type).create(location);
	}


	
	
	//TOOL DESCRIPTORS
	@FunctionalInterface
	public static interface ToolDescriptorFactory{
		public IToolDescriptor create(String content) throws DescriptorException;
	}

	private static void loadToolDescriptorFactories(){
		TOOL_DESCRIPTOR_FACTORIES.put(Support.JSON_TYPE, ToolDescriptorManager::jsonFactory);
		TOOL_DESCRIPTOR_FACTORIES.put(Support.XML_TYPE, ToolDescriptorManager::xmlFactory);
	}	

	public static IToolDescriptor getToolDescriptor(String type, String content) throws DescriptorException{
		return TOOL_DESCRIPTOR_FACTORIES.get(type).create(content);
	}




	//ARGUMENTS COMPOSER
	public static final String COMPOSER_DUMMY_NAME = "dummy";
	public static final String COMPOSER_VALUES_SEPARATED_BY_SPACE_NAME = "values_separated_by_space";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_EQUAL_NAME = "name_values_separated_by_equal";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_COLON_NAME = "name_values_separated_by_colon";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_HYPHEN_NAME = "name_values_separated_by_hyphen";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_SPACE_NAME = "name_values_separated_by_space";
	public static final String COMPOSER_VALUES_SEPARATED_BY_COLON_NAME = "values_separated_by_colon";
	public static final String COMPOSER_VALUES_SEPARATED_BY_VERTICAL_BAR_NAME = "values_separated_by_vertical_bar";
	public static final String COMPOSER_VALUES_SEPARATED_BY_HYPHEN_NAME = "values_separated_by_hyphen";
	public static final String COMPOSER_VALUES_SEPARATED_BY_SLASH_NAME = "values_separated_by_slash";
	public static final String COMPOSER_VALUES_SEPARATED_BY_COMMA_NAME = "values_separated_by_comma";
	public static final String COMPOSER_TRIMMOMATIC_NAME = "trimmomatic";
	public static final String COMPOSER_VELVETG_NAME = "velvetG";
	public static final String VELVET_OUTPUT_DIRECTORY_ARGUMENT_NAME = "output_directory";

	@SuppressWarnings("unchecked")
	private static boolean isComposer(Method method){
		// Is static
		// Returns String
		// Recieves only 1 argument of type List<Argument>
		// Is anotated with ComposerNameAnnotation
		return Modifier.isStatic(method.getModifiers()) && 
				method.getReturnType().isAssignableFrom(String.class) &&
				method.getParameters().length==1 &&
				List.class.isAssignableFrom(method.getParameters()[0].getType()) &&
				Argument.class.isAssignableFrom((Class<Argument>)((ParameterizedType)(method.getParameters()[0].getParameterizedType())).getActualTypeArguments()[0]) && 
				method.getAnnotation(ComposerNameAnnotation.class) != null;
	}

	private static IArgumentsComposer getComposer(Method method){
		return (args)->{
			try{
				return (String) method.invoke(null, new Object[]{args});	
			}catch(Exception e){
				throw new DSLException("Error invoking composer!", e);
			}
		};
	}

	private static String getComposerName(Method method){
		return method.getAnnotation(ComposerNameAnnotation.class).name();
	}

	private static void loadArgumentsComposers(){
		for(Method method : ArgumentsComposerManager.class.getMethods())
			if(isComposer(method))
				COMPOSERS.put(getComposerName(method), getComposer(method));	
	}

	public static IArgumentsComposer getComposer(String name){
		return COMPOSERS.get(name);
	}




	//COMMAND BUILDER
	@FunctionalInterface
	public static interface CommanBuilderFactory{
		public ICommandBuilder create(IConfigurator config) throws CommandBuilderException;
	}

	public static final String COMMAND_BUILDER_DOCKER = "Docker";
	public static final String COMMAND_BUILDER_LOCAL = "Local";

	private static void loadCommandBuilderFactories(){
		COMMAND_BUILDER_FACTORIES.put(COMMAND_BUILDER_DOCKER, CommandBuilderManager::dockerBuilder);
		COMMAND_BUILDER_FACTORIES.put(COMMAND_BUILDER_LOCAL, CommandBuilderManager::localBuilder);
	}

	public static ICommandBuilder getBuilder(IConfigurator config) throws CommandBuilderException {
		return COMMAND_BUILDER_FACTORIES.get(config.getName()).create(config);
	}

}
