package dsl.entities;

import java.util.function.Consumer;

import utils.Event;
import descriptor.IOutputDescriptor;
import descriptor.OutputDescriptor;

public class Output {
	
	private final Consumer<String> onArgumentValueChanged;

	private final Command originCommand;
	public Command getOriginCommand(){ return originCommand; }
	
	private final IOutputDescriptor descriptor;
	public IOutputDescriptor getDescriptor(){ return descriptor; }
	
	private final String initValue;
	private String value;
	public String getValue(){ return value; }
	public void setValue(String value){ 
		this.value = value;
		valueChangedEvent.trigger(value);
	}

	public final Event<String> valueChangedEvent; 
	
	public Output(IOutputDescriptor descriptor, String value, Command originCommand){
		this.descriptor = descriptor;
		this.initValue = value;
		this.value = value;
		this.valueChangedEvent = new Event<>();
		this.originCommand = originCommand;
		this.onArgumentValueChanged = getOnArgumentValueChanged();
	}

	private Consumer<String> getOnArgumentValueChanged(){ 
		switch (descriptor.getType()) {
			case OutputDescriptor.INDEPENDENT_TYPE: return (val) -> {};
			case OutputDescriptor.FILE_DEPENDENT_TYPE : return (val) -> this.setValue(val);
			case OutputDescriptor.DIRECTORY_DEPENDENT_TYPE : return (val) -> this.setValue(val + "/" + this.initValue);
			default : return null;
		}
	}
	
	public String getName(){
		return descriptor.getName();
	}
	
	public String getInputName() {
		return descriptor.getInputName();
	}

	public String getType() {
		return descriptor.getType();
	}

	protected void onArgumentValueChange(String newValue){
		onArgumentValueChanged.accept(newValue);
	}
		
}
