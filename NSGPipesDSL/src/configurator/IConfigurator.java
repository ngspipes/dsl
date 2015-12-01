package configurator;

import java.util.List;

import repository.IRepository;

public interface IConfigurator {
	
	public IRepository getOriginRepository();
	
	public void setOriginRepository(IRepository originRepository);
	
	public String getName();
	
	public String getUri();
	
	public List<String> getSetup();

}
