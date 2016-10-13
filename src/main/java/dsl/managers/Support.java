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

import argmentsComposer.IArgumentsComposer;
import commandBuilder.ICommandBuilder;
import configurators.IConfigurator;
import dsl.entities.Argument;
import dsl.managers.ArgumentsComposerManager.ComposerNameAnnotation;
import exceptions.CommandBuilderException;
import exceptions.DSLException;
import exceptions.RepositoryException;
import repository.IRepository;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Support {

	public static final Map<String, RepositoryFactory> REPOSITORY_FACTORIES = new HashMap<>();
	public static final Map<String, IArgumentsComposer> COMPOSERS = new HashMap<>();
	public static final Map<String, CommandBuilderFactory> COMMAND_BUILDER_FACTORIES = new HashMap<>();
	
	
	static{
		loadRepositoryFactories();
		loadArgumentsComposers();
		loadCommandBuilderFactories();
	}


	//REPOSITORY
	@FunctionalInterface
	public interface RepositoryFactory{
		IRepository create(String location) throws RepositoryException;
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



	
	//ARGUMENTS COMPOSER
	public static final String COMPOSER_DUMMY_NAME = "dummy";
	public static final String COMPOSER_VALUES_SEPARATED_BY_SPACE_NAME = "values_separated_by_space";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_EQUAL_NAME = "name_values_separated_by_equal";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_COLON_NAME = "name_values_separated_by_colon";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_HYPHEN_NAME = "name_values_separated_by_hyphen";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_SPACE_NAME = "name_values_separated_by_space";
	public static final String COMPOSER_NAME_VALUES_SEPARATED_BY_EQUAL_AND_BACKSLASH_NAME = "name_values_separated_by_equal_and_backslash";

	@SuppressWarnings("unchecked")
	private static boolean isComposer(Method method){
		// Is static
		// Returns String
		// Receives only 1 argument of type List<Argument>
		// Is annotated with ComposerNameAnnotation
		return Modifier.isStatic(method.getModifiers()) && 
				method.getReturnType().isAssignableFrom(String.class) &&
				method.getParameters().length==1 &&
				Argument.class.isAssignableFrom(method.getParameters()[0].getType()) &&
				method.getAnnotation(ComposerNameAnnotation.class) != null;
	}

	private static IArgumentsComposer getComposer(Method method){
		return (args)->{
			try{
				return (String) method.invoke(null, args);
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
	public interface CommandBuilderFactory {
		ICommandBuilder create(IConfigurator config) throws CommandBuilderException;
	}

	public static final String COMMAND_BUILDER_DOCKER = "Docker";
	public static final String COMMAND_BUILDER_LOCAL = "Local";

	private static void loadCommandBuilderFactories(){
		COMMAND_BUILDER_FACTORIES.put(COMMAND_BUILDER_DOCKER, CommandBuilderManager::dockerBuilder);
		COMMAND_BUILDER_FACTORIES.put(COMMAND_BUILDER_LOCAL, CommandBuilderManager::localBuilder);
	}

	public static ICommandBuilder getBuilder(IConfigurator config) throws CommandBuilderException {
		return COMMAND_BUILDER_FACTORIES.get(config.getBuilder()).create(config);
	}

}
