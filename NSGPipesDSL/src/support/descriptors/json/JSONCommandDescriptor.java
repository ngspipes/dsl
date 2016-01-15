package support.descriptors.json;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import descriptor.CommandDescriptor;
import descriptor.IArgumentDescriptor;
import descriptor.IOutputDescriptor;

public class JSONCommandDescriptor extends CommandDescriptor{

	public static final String NAME_JSON_KEY = "name";
	public static final String COMMAND_JSON_KEY = "command";
	public static final String ARGUMENTS_JSON_KEY = "arguments";
	public static final String OUTPUTS_JSON_KEY = "outputs";
	private static final String DESCRIPTION_JSON_KEY = "description";
	private static final String ARGUMENTS_COMPOSER_JSON_KEY = "argumentsComposer";
	private static final String PRIORITY_JSON_KEY = "priority";

	private static List<IArgumentDescriptor> getArguments(JSONObject json) throws JSONException{
		LinkedList<IArgumentDescriptor> arguments = new LinkedList<IArgumentDescriptor>();
		JSONArray args = json.getJSONArray(ARGUMENTS_JSON_KEY);

		for(int i=0; i<args.length(); ++i)
			arguments.addLast(new JSONArgumentDescriptor(args.getJSONObject(i), i));

		return arguments;
	}

	private static List<IOutputDescriptor> getOutputs(JSONObject json) throws JSONException{
		LinkedList<IOutputDescriptor> outputs = new LinkedList<IOutputDescriptor>();
		JSONArray otps = json.getJSONArray(OUTPUTS_JSON_KEY);

		for(int i=0; i<otps.length(); ++i)
			outputs.addLast(new JSONOutputDescriptor(otps.getJSONObject(i)));

		return outputs;
	}

	protected final JSONObject json;

	public JSONCommandDescriptor(String jsonContent) throws JSONException{
		this(new JSONObject(jsonContent));
	}

	public JSONCommandDescriptor(JSONObject json) throws JSONException{
		this(json, getArguments(json), getOutputs(json));
	}

	protected JSONCommandDescriptor(JSONObject json, List<IArgumentDescriptor> args, List<IOutputDescriptor> outputs) throws JSONException{
		super(json.getString(NAME_JSON_KEY), json.getString(COMMAND_JSON_KEY),json.getString(DESCRIPTION_JSON_KEY), 
				json.getString(ARGUMENTS_COMPOSER_JSON_KEY), args, outputs, json.getInt(PRIORITY_JSON_KEY));
		this.json = json;
	}

	public JSONObject getJSONObject(){
		return json;
	}

}
