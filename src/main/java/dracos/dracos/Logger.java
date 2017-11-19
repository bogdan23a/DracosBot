package dracos.dracos;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class Logger {
	// Vars
	private static final String PREMESSAGE = "DRACOS";
	private final static boolean LOG_PRINT_STACK_SOURCE = false;
	private final static LogType MINLOGLEVEL = LogType.DEBUG;
	private static final boolean LOG_SAVE_TO_FILE = true;
	private static final StorageHandler storageHandler = new StorageHandler("bot_logs.txt");
	private static final String[] LOGS = new String[1000];
	private static int head = 0;

	/**
	 * Dracos print function.
	 * 
	 * @param text
	 */
	public static void print(Object... text) {
		log(LogType.GENERAL, text);
	}

	public static void printf(String text, Object... args) {
		log(LogType.GENERAL, String.format(text, args));
	}

	public static void debug(Object... text) {
		log(LogType.DEBUG, text);
	}

	public static void debugf(String text, Object... args) {
		log(LogType.DEBUG, String.format(text, args));
	}

	public static void info(Object... text) {
		log(LogType.INFO, text);
	}

	public static void infof(String text, Object... args) {
		log(LogType.INFO, String.format(text, args));
	}

	public static void critical(Object... text) {
		log(LogType.CRITICAL, text);
	}

	public static void criticalf(String text, Object... args) {
		log(LogType.CRITICAL, String.format(text, args));
	}

	private static void log(LogType type, Object... text) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String str = null;

		if (System.getProperty("os.name").contains("Windows")) { // Check type of OS - Windows colors dont work
			str = String.format("[%s][%s][%s] %s", sdf.format(cal.getTime()), PREMESSAGE, type, Arrays.toString(text));
		} else {
			str = String.format("%s[%s][%s][%s]%s %s", type.getColor(), sdf.format(cal.getTime()), PREMESSAGE, type,
					LogType.ANSI_RESET, Arrays.toString(text));
		}

		// Send it to the print out stream
		System.out.println(str);

		if (LOG_PRINT_STACK_SOURCE) {
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			for (int i = 3; i < 6 && i < stack.length; i++) {
				String str2 = (String.format("%40s:\t\t%s", type, (stack[i].toString())));
				System.out.println(str2);
				str = str + "\n" + str2;
			}

		}

		if (LOG_SAVE_TO_FILE) {
			saveToFile(str);
		}
	}

	private static void saveToFile(String str) {
		if (head > 999) {
			Arrays.fill(LOGS, null);
			head = 0;
		}

		LOGS[head] = str;
		head++;
		storageHandler.appendToTextFile(str);
	}

	public static String[] getLogs() {
		return LOGS.clone();
	}

	public static int getHead() {
		return head;
	}
}
