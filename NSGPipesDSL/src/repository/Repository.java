package repository;

import java.util.Collection;
import java.util.LinkedList;

import configurator.IConfigurator;
import descriptor.IToolDescriptor;
import exceptions.RepositoryException;
import utils.Cache;

public abstract class Repository implements IRepository {
	
	protected final Cache<String, String> imagesCache = new Cache<>();
	protected final Cache<String, IToolDescriptor> toolsCache = new Cache<>();
	protected final Cache<String, IConfigurator> configuratorsCache = new Cache<>();
	protected final Cache<String, Collection<String>> configuratorsNamesCache = new Cache<>();
	protected Collection<String> toolsName;
	protected final String location;
	protected final String type;
	
	public Repository(String location, String type){
		this.type = type;
		this.location = location;
	}
	
	
	@Override
	public String getType(){
		return type;
	}
	
	@Override
	public String getLocation(){
		return location;
	}
	
	@Override
	public String getToolLogo(String toolName) {
		String toolLogo = null;
		
		if((toolLogo=imagesCache.get(toolName))==null){
			
			try{
				toolLogo = loadToolLogo(toolName);	
			}catch(RepositoryException e){ /*In case of fail tool logo will be null*/}
			
			imagesCache.add(toolName, toolLogo);
		}
		
		return toolLogo;
	}
	
	@Override
	public Collection<String> getToolsName() throws RepositoryException {
		if(toolsName==null)
			toolsName = loadToolsName();
			
		return toolsName;
	}
	
	@Override
	public Collection<IToolDescriptor> getAllTools() throws RepositoryException {
		Collection<IToolDescriptor> tools = new LinkedList<>();
		
		for(String name : getToolsName())
			tools.add(getTool(name));
		
		return tools;
	}
	
	@Override
	public IToolDescriptor getTool(String toolName) throws RepositoryException {
		IToolDescriptor tool;
		
		if((tool=toolsCache.get(toolName))==null){
			tool = loadTool(toolName);
			tool.setOriginRepository(this);
			toolsCache.add(toolName, tool);
		}
		
		return tool;
	}
	
	@Override
	public Collection<String> getConfiguratorsNameFor(String toolName) throws RepositoryException {
		Collection<String> names;
		
		if((names=configuratorsNamesCache.get(toolName))==null){
			names = loadConfiguratorsNameFor(toolName);
			configuratorsNamesCache.add(toolName, names);
		}
		
		return names;
	}
	
	@Override
	public Collection<IConfigurator> getConfigurationsFor(String toolName) throws RepositoryException {
		Collection<String> configuratorsName = getConfiguratorsNameFor(toolName);
		LinkedList<IConfigurator> configurators = new LinkedList<>();

		for (String configuratorName : configuratorsName)
			configurators.add(getConfigurationFor(toolName, configuratorName));

		return configurators;
	}
	
	@Override
	public IConfigurator getConfigurationFor(String toolName, String configuratorName) throws RepositoryException {
		IConfigurator configurator;
		
		String key = toolName + "-" + configuratorName;
		if((configurator=configuratorsCache.get(key))==null){
			configurator = loadConfigurationFor(toolName, configuratorName);
			configurator.setOriginRepository(this);
			configuratorsCache.add(key, configurator);
		}
		
		return configurator;
	}

	@Override
	public boolean equals(Object other){
		if(other == null)
			return false;
		
		if(!(other instanceof IRepository))
			return false;
		
		IRepository otherRep = (IRepository)other;
		
		return this.location.equals(otherRep.getLocation()) && this.type.equals(otherRep.getType());
	}
	
	protected abstract String loadToolLogo(String toolName) throws RepositoryException;
	protected abstract Collection<String> loadToolsName() throws RepositoryException;
	protected abstract IToolDescriptor loadTool(String toolName) throws RepositoryException;
	protected abstract Collection<String> loadConfiguratorsNameFor(String toolName) throws RepositoryException;
	protected abstract IConfigurator loadConfigurationFor(String toolName, String configuratorName) throws RepositoryException;
	
}
