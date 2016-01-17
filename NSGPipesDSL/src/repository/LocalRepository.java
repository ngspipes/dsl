package repository;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utils.IO;
import configurator.IConfigurator;
import descriptor.IToolDescriptor;
import dsl.managers.Support;
import exceptions.RepositoryException;

public class LocalRepository extends Repository {
	
	private static File getDescriptor(File tool) throws RepositoryException{
    	File[] descriptor = tool.listFiles((file)-> !file.isDirectory() && getFileName(file).equals(DESCRIPTOR_NAME));
    	
    	if(descriptor.length == 0)
    		throw new RepositoryException("Invalid Tool folder!\nTool folder must contain a descriptor file with name Descriptor.");
    	
    	return descriptor[0];
    }
	
	private static File getConfigurator(File tool, String configuratorName) throws RepositoryException{
    	File[] configs = tool.listFiles((file)-> !file.isDirectory() && getFileName(file).equals(configuratorName));
    	
    	if(configs.length == 0)
    		throw new RepositoryException("There is no " + configuratorName + " Configurator!");
    	
    	return configs[0];
    }
    
    private static String getFileName(File file){
    	return file.getName().split("\\.")[0];
    }
  
    private static String getConfiguratorType(File configurator){
    	return getType(configurator);
    }
    
    private static String getDescriptorType(File descriptor){
    	return getType(descriptor);
    }
    
    private static String getType(File file){
		String name = file.getName();
		
		int startIndex = name.lastIndexOf(".") + ".".length(); 
		
		return name.substring(startIndex);
    }
    	
    private static final String DESCRIPTOR_NAME  = "Descriptor";
	private static final String CONFIGURATOR_NAME= "Config";
	private static final String LOGO_FILE_NAME = "Logo.png";
    
	public LocalRepository(String repositoryDir){
		super(repositoryDir, Support.REPOSITORY_LOCAL);
	}

	
	
	private File getToolDirectory(String toolName){
		return new File(this.location + "/" + toolName);
	}
	
	@Override
	protected String loadToolLogo(String toolName)throws RepositoryException {
		try{
			File logoFile = new File(getToolDirectory(toolName) + "/" + LOGO_FILE_NAME);
			
			if(!logoFile.exists())
				return null;
			
			return logoFile.toURI().toURL().toString();
			
		}catch(MalformedURLException e){
			throw new RepositoryException("MalFormed toolLogo uri!", e);
		}
	}

	
	@Override
	protected Collection<String> loadToolsName() throws RepositoryException {
		Stream<File> stream = Arrays.stream(new File(this.location).listFiles());
		
		return stream.map((folder)->folder.getName()).collect(Collectors.toList());
	}

	
	@Override
	protected IToolDescriptor loadTool(String toolName) throws RepositoryException {
		try{
    		File toolDir = getToolDirectory(toolName);
        	
        	File descriptor = getDescriptor(toolDir);
        	String descriptorType = getDescriptorType(descriptor);
        	String descriptorContent = IO.read(descriptor.getPath());
           
        	return Support.getToolDescriptor(descriptorType, descriptorContent);	
    	}catch(Exception e){
    		throw new RepositoryException("Erro loading tool " + toolName + "!", e);
    	}
	}

	
	@Override
	protected Collection<String> loadConfiguratorsNameFor(String toolName) throws RepositoryException {
		Stream<File> stream = Arrays.stream(getToolDirectory(toolName).listFiles());
		
		return stream.	filter((file)->getFileName(file).endsWith(CONFIGURATOR_NAME)).
						map((file)->{
							String fileName = getFileName(file);
							return fileName.substring(0, (fileName.length()-CONFIGURATOR_NAME.length()));
						}).
						collect(Collectors.toList());
	}

	
	@Override
	protected IConfigurator loadConfigurationFor(String toolName, String configuratorName) throws RepositoryException {
		try{
			configuratorName = configuratorName+CONFIGURATOR_NAME;
    		File config = getConfigurator(getToolDirectory(toolName), configuratorName);
        	String configuratorType = getConfiguratorType(config);
        	String configuratorContent = IO.read(config.getPath());
           
        	return Support.getConfigurator(configuratorType, configuratorContent);	
    	}catch(Exception e){
    		throw new RepositoryException("Erro loading tool " + toolName + "!", e);
    	}
	}
	
}
