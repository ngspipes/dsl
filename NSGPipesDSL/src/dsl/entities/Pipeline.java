 package dsl.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import progressReporter.ConsoleReporter;
import progressReporter.IProgressReporter;
import progressReporter.InternalReporter;
import utils.Event;
import utils.Utils;
import configurators.IConfigurator;
import dsl.Log;
import exceptions.DSLException;

public class Pipeline {

	private final List<Step> steps = new LinkedList<>();

	private final List<Chain> chains = new LinkedList<>();
	public Collection<Chain> getChains(){ return chains; }

	public final Event<Step> stepAddedEvent = new Event<>();
	public final Event<Step> stepRemovedEvent = new Event<>();
	public final Event<Chain> chainAddedEvent = new Event<>();
	public final Event<Chain> chainRemovedEvent = new Event<>();

	public void addStep(Step step){
		step.setOrder(steps.size()+1);
		steps.add(step);
		step.setOriginPipe(this);
		stepAddedEvent.trigger(step);
	}

	public void removeStep(Step step){
		steps.remove(step);
		step.setOriginPipe(null);
		stepRemovedEvent.trigger(step);
	}

	public void addChain(Chain chain){
		chains.add(chain);
		chain.connect();
		chainAddedEvent.trigger(chain);
	}

	public void removeChain(Chain chain){
		chains.remove(chain);
		chain.disconnect();
		chainRemovedEvent.trigger(chain);
	}

	public void build() throws DSLException{
		for(Step step : steps)
			step.build();
	}


	public List<Step> getSteps(){
		return steps;
	}

	public Collection<Step> getSteps(int from){
		return steps.subList(from-1, steps.size()-1);
	}

	public Collection<Step> getSteps(int from, int to){
		return steps.subList(from-1, to);
	}


	public Collection<IConfigurator> getConfigurators(){
		return getConfiguratorsFor(steps);
	}

	public Collection<IConfigurator> getConfigurators(int from){
		return getConfiguratorsFor(getSteps(from));
	}

	public Collection<IConfigurator> getConfigurators(int from, int to){
		return getConfiguratorsFor(getSteps(from, to));
	}

	private Collection<IConfigurator> getConfiguratorsFor(Collection<Step> steps){
		Collection<IConfigurator> configs = new LinkedList<>();

		for(Step step : steps)
			configs.add(step.getConfigurator());

		return configs;
	}


	public int getRequiredMemory(){
		return getRequiresMemoryFor(steps);
	}

	public int getRequiredMemory(int from){
		return getRequiresMemoryFor(getSteps(from));
	}

	public int getRequiredMemory(int from, int to){
		return getRequiresMemoryFor(getSteps(from, to));
	}

	public int getRequiresMemoryFor(Collection<Step> steps){
		int requiredMemory = -1;

		for(Step step: steps)
			if(step.getRequiredMemory()>requiredMemory)
				requiredMemory = step.getRequiredMemory();

		return requiredMemory;
	}



	public void run() throws DSLException{
		run(new ConsoleReporter());
	}

	public void run(int from) throws DSLException{
		run(new ConsoleReporter(), from);
	}

	public void run(int from, int to) throws DSLException{
		run(new ConsoleReporter(), from, to);
	}

	public void run(IProgressReporter reporter) throws DSLException{
		run(reporter, 1);
	}

	public void run(IProgressReporter reporter, int from) throws DSLException{
		run(reporter, from, steps.size());
	}

	public void run(IProgressReporter reporter, int from, int to) throws DSLException{
		reporter = new InternalReporter(reporter);

		try{
			Log.start();
			reporter.open();
			reporter.reportTrace(" :: STARTED :: ");

			internalRun(reporter, from, to);
		}catch(Exception ex){
			reporter.reportError("Error : " + ex.getMessage());
			Log.log(Utils.getStackTrace(ex));
			throw new DSLException("Error running Pipeline from " + from + " to " + to, ex);
		}finally{
			reporter.reportTrace(" :: FINISHED :: ");
			reporter.close();
			Log.finish();
		}
	}

	private void internalRun(IProgressReporter reporter, int from, int to) throws DSLException{
		List<Step> orderedSteps = new LinkedList<Step>(steps);
		Collections.sort(orderedSteps, (a,b)->a.getOrder()-b.getOrder());

		for(Step step : orderedSteps){
			if(step.getOrder()<from)
				reporter.reportTrace("Skipped -> " + step);
			else if(step.getOrder()<=to){
				reporter.reportTrace("Running -> " + step);
				step.run(reporter);
			}else
				break;
		}
	}

}
