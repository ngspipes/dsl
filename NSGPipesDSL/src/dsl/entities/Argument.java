package dsl.entities;

import utils.Event;
import descriptor.IArgumentDescriptor;

public class Argument {
	
	private final Command originCommand;
	public Command getOriginCommand(){ return originCommand; }

	private final IArgumentDescriptor descriptor;
	public IArgumentDescriptor getDescriptor(){ return descriptor; }
	
	private String value;
	public String getValue(){ return value; }
	public void setValue(String value){
		this.value = value;
		valueChangedEvent.trigger(value);
	}
	
	public final Event<String> valueChangedEvent; 
	
	public Argument(IArgumentDescriptor descriptor, String value, Command originCommand){
		this.descriptor = descriptor;
		this.value = value;
		this.valueChangedEvent = new Event<>();
		this.originCommand = originCommand;
	}

	public String getName(){
		return descriptor.getName();
	}

	public String getType(){
		return descriptor.getType();
	}

	public boolean getRequired(){
		return descriptor.getRequired();
	}

	public int getOrder(){
		return descriptor.getOrder();
	}
	
}
