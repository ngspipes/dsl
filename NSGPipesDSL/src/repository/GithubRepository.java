package repository;

import java.net.HttpURLConnection;
import java.util.Collection;

import repository.RemoteRepository.IFactory;
import configurator.IConfigurator;
import descriptor.IToolDescriptor;
import dsl.managers.Support;
import exceptions.RepositoryException;

public class GithubRepository extends Repository {
	
	private static final String CONNECTION_BASE_URI = "https://raw.githubusercontent.com"; 
	private static final String CONFIGURATOR_NAME= "Config";
	private static final String LOGO_FILE_NAME = "Logo.png";
	
	public static <T> T getObject(String uri, IFactory<T> factory) throws RepositoryException {
		try {
			HttpURLConnection connection = RemoteRepository.getConnection(uri);
			String content = RemoteRepository.readStream(connection);

			return factory.getObj("json", content);
		} catch (Exception e) {
			throw new RepositoryException("Error loading objects", e);
		}
	}

	

	private String connectionUri;
	private void setConnectionUri(String location) {
		String repoLocation = location.substring(location.indexOf(".com/") + ".com".length());
		connectionUri = CONNECTION_BASE_URI + repoLocation + "/master";		
	}
	
	public GithubRepository(String location, String type) {
		super(location, type);
		setConnectionUri(location);
	}

	public GithubRepository(String location) {
		this(location, Support.REPOSITORY_GITHUB);
	}

	@Override
	protected String loadToolLogo(String toolName) throws RepositoryException {
		return this.connectionUri + "/" + toolName + "/" + LOGO_FILE_NAME;
	}

	@Override
	protected Collection<String> loadToolsName() throws RepositoryException {
		return RemoteRepository.getStringCollection(this.connectionUri + "/tools.json", "toolsName");
	}

	@Override
	protected IToolDescriptor loadTool(String toolName) throws RepositoryException {
		String descriptorUri = this.connectionUri + "/" + toolName + "/Descriptor.json";
		try {
			return getObject(descriptorUri, Support::getToolDescriptor);
		} catch (Exception e) {
			throw new RepositoryException("Error getting tool", e);
		}
	}

	@Override
	protected Collection<String> loadConfiguratorsNameFor(String toolName) throws RepositoryException {
		return RemoteRepository.getStringCollection(this.connectionUri + "/" + toolName +  
													"/configurators.json", "configuratorsName");
	}

	@Override
	protected IConfigurator loadConfigurationFor(String toolName, String configuratorName) throws RepositoryException {
		String configuratorUri = this.connectionUri + "/"  + toolName +  "/" + configuratorName + CONFIGURATOR_NAME + ".json";

		return getObject(configuratorUri, Support::getConfigurator);
	}

}
