package descriptor;

public class ArgumentDescriptor implements IArgumentDescriptor{

	private ICommandDescriptor originCommand;
	public ICommandDescriptor getOriginCommand(){ return originCommand; }
	public void setOriginCommand(ICommandDescriptor originCommand){ this.originCommand = originCommand; }
	
	private final String name;
	private final String description;
	private final String type;
	private final boolean required;
	private final int order;
	
	
	public ArgumentDescriptor(String name, String description,  String type, boolean required, int order) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.required = required;
		this.order = order;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public boolean getRequired() {
		return required;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	public int getOrder(){
		return order;
	}
	
}
