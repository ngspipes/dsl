package dsl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Log {

	private static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public static final String LOG_DIR = GetLogDir();
	private static PrintWriter WRITER;


	public static void start() throws FileNotFoundException {
		WRITER = new PrintWriter(LOG_DIR);
	}

	private static String GetLogDir() {
		Calendar calendar = new GregorianCalendar();
		StringBuilder dir = new StringBuilder();
		
		dir.append(calendar.get(Calendar.YEAR)).append("_");
		dir.append(calendar.get(Calendar.MONTH)+1).append("_");
		dir.append(calendar.get(Calendar.DAY_OF_MONTH)).append("_");
		dir.append(calendar.get(Calendar.HOUR_OF_DAY)).append("_");
		dir.append(calendar.get(Calendar.MINUTE)).append("_");
		dir.append(calendar.get(Calendar.SECOND)).append("_");
		dir.append("Log.txt");
		
		return dir.toString();
	}

	public static void finish() {
		WRITER.close();
	}

	private static String getCurrentTime(){
		return DATE_FORMATER.format(new Date());
	}

	public static void log(String msg){
		WRITER.println("");
		WRITER.println(getCurrentTime());
		WRITER.println(msg);
		WRITER.flush();
	}

}
