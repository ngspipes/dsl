package repository;

import java.util.Collection;

import configurator.IConfigurator;
import descriptor.IToolDescriptor;
import exceptions.RepositoryException;

public interface IRepository {
	
	public String getLocation();
	
	public String getType();
	
	public String getToolLogo(String toolName);
	
	public Collection<String> getToolsName() throws RepositoryException;
	
	public Collection<IToolDescriptor> getAllTools() throws RepositoryException;
	
	public IToolDescriptor getTool(String toolName) throws RepositoryException;
	
	public Collection<String> getConfiguratorsNameFor(String toolName) throws RepositoryException;
	
	public Collection<IConfigurator> getConfigurationsFor(String toolName) throws RepositoryException;
	
	public IConfigurator getConfigurationFor(String toolName, String configuratorName) throws RepositoryException;

}
