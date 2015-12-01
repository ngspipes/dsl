package descriptor;

public interface IOutputDescriptor {
	
	public ICommandDescriptor getOriginCommand();
	
	public void setOriginCommand(ICommandDescriptor originCommand); 
	
	public String getName();
	
	public String getType();
	
	public String getInputName();

	public String getValue();

	public String getDescription();
	
}
