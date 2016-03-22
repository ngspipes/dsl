/*-
 * Copyright (c) 2016, NGSPipes Team <ngspipes@gmail.com>
 * All rights reserved.
 *
 * This file is part of NGSPipes <http://ngspipes.github.io/>.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 package dsl.entities;

import configurators.IConfigurator;
import dsl.Log;
import exceptions.DSLException;
import progressReporter.ConsoleReporter;
import progressReporter.IProgressReporter;
import utils.Event;
import utils.Utils;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

		steps.forEach((step)->configs.add(step.getConfigurator()));

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

	public void run(IProgressReporter reporter, int from, int to) throws DSLException {
		try{
			start(reporter);
			internalRun(reporter, from, to);
		}catch(DSLException ex){
			String message = "Error : " + ex.getMessage();

			try {
				reporter.reportError(message);
			} catch(DSLException e){
				Log.log("Error reporting msg : " + message);
			}

			Log.log(message);
			Log.log(Utils.getStackTrace(ex));

			throw ex;
		}finally{
			finish(reporter);
		}
	}

	private void start(IProgressReporter reporter) throws DSLException {
		DSLException logException = null;
		DSLException reporterException = null;

		try {
			Log.start();
		} catch (FileNotFoundException ex) {
			logException = new DSLException("Error starting Log.", ex);
		}

		try {
			reporter.open();
		} catch(DSLException ex) {
			reporterException = ex;
		}

		if(logException != null)
			throw logException;

		if(reporterException != null)
			throw reporterException;
	}

	private void finish(IProgressReporter reporter) throws DSLException {
		try {
			reporter.reportTrace(" :: FINISHED :: ");
			reporter.close();
		} finally {
			Log.finish();
		}
	}

	private void internalRun(IProgressReporter reporter, int from, int to) throws DSLException{
		List<Step> orderedSteps = new LinkedList<>(steps);
		Collections.sort(orderedSteps, (a,b)->a.getOrder()-b.getOrder());

		for(Step step : orderedSteps){
			if(step.getOrder() < from)
				reporter.reportTrace("Skipped -> " + step);
			else if(step.getOrder() <= to){
				reporter.reportTrace("Running -> " + step);
				step.run(reporter);
			}else
				break;
		}
	}

}
