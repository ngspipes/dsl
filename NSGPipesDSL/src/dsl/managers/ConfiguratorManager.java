package dsl.managers;

import org.json.JSONException;

import support.configurator.JSONConfigurator;
import support.configurator.XMLConfigurator;
import support.descriptors.xml.xmlObject.XMLException;
import configurator.IConfigurator;
import exceptions.ConfiguratorException;

public class ConfiguratorManager {
	
	public static IConfigurator XMLFactory(String content) throws ConfiguratorException {
		try{
			return new XMLConfigurator(content);
		} catch (XMLException e) {
			throw new ConfiguratorException("Error instanciating XMLConfigurator!", e);
		}
	}
	
	public static IConfigurator JSONFactory(String content) throws ConfiguratorException {
		try{
			return new JSONConfigurator(content);
		} catch (JSONException e) {
			throw new ConfiguratorException("Error instanciating JSONConfigurator!", e);
		}
	}

}
