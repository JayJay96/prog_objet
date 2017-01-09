package tools.appControl;

public class Logger {

	public enum LogLevel {
		DEBUG, INFO, CRITICAL;
	}

	public static LogLevel	logLevel	= LogLevel.DEBUG;
	public static boolean	MTADebug	= true;

	public static void critical(final String msg) {
		System.out.println("Critical - " + msg);
	}

	public static void debug(final String msg) {
		if (Logger.logLevel == LogLevel.DEBUG) {
			System.out.println("Debug - " + msg);
		}
	}

	public static void info(final String msg) {
		if (Logger.logLevel != LogLevel.CRITICAL) {
			System.out.println("Info - " + msg);
		}
	}
}
