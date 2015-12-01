package dsl.entities;

import java.util.function.Consumer;

import utils.Event;

public class Chain {
	
	private final Argument argument;
	public Argument getArgument() { return argument; }
	
	private final Output output;
	public Output getOutput() { return output; }
	
	private final Consumer<String> onOutputChange;
	
	private boolean connected;
	public boolean getConnected(){ return connected; }
	private void setConnected(boolean connected){ 
		this.connected = connected;
		connectEvent.trigger(connected);
	}
	
	public final Event<Boolean> connectEvent;
	
	public Chain(Argument argument, Output output){
		this.argument = argument;
		this.output = output;
		this.connected = false;
		this.connectEvent = new Event<>();
		this.onOutputChange = argument::setValue;
	}
	
	public void connect(){
		if(connected)
			return;
		
		if(output.getValue()!=null)
			argument.setValue(output.getValue());
		
		output.valueChangedEvent.addListner(onOutputChange);
		setConnected(true);
	}
	
	public void disconnect(){
		if(!connected)
			return; 
		
		output.valueChangedEvent.removeListner(onOutputChange);
		setConnected(false);
	}

}
