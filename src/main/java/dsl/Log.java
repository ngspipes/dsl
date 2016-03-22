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
package dsl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Log {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public static final String LOG_DIR = GetLogDir();
	private static PrintWriter WRITER;


	public static void start() throws FileNotFoundException {
		WRITER = new PrintWriter(LOG_DIR);
		log("START");
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
		log("FINISH");
		WRITER.close();
	}

	private static String getCurrentTime(){
		return DATE_FORMATTER.format(new Date());
	}

	public static void log(String msg){
		WRITER.println("");
		WRITER.println(getCurrentTime());
		WRITER.println(msg);
		WRITER.flush();
	}

}
