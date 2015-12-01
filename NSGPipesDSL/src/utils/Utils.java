package utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {
	
	public static String getStackTrace(Throwable t){
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
}
