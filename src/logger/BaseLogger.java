package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides a base logging system for logging info and error messages.
 * The log method writes the messages to the specified files depending on the log type.
 */
public class BaseLogger {
    private String logType;

    /**
     * The constructor for the BaseLogger class.
     * @param logType The type of log, can be either "INFO" or "ERROR".
     */
    private BaseLogger(String logType) {
        this.logType = logType;
    }

    /**
     * Returns a new BaseLogger instance with log type set to "INFO".
     * @return A new BaseLogger instance.
     */
    public static BaseLogger info() {
        return new BaseLogger("INFO");
    }

    /**
     * Returns a new BaseLogger instance with log type set to "ERROR".
     * @return A new BaseLogger instance.
     */
    public static BaseLogger error() {
        return new BaseLogger("ERROR");
    }

    /**
     * Logs a message to the file corresponding to the log type.
     * "INFO" messages are logged to application_info.txt and "ERROR" messages to application_error.txt.
     * The log entry contains a timestamp, the log type, and the message.
     * @param message The message to be logged.
     */
    public void log(String message) {
        String filename = logType.equals("INFO") ? "application_info.txt" : "application_error.txt";

        try (FileWriter fw = new FileWriter(filename, true); PrintWriter pw = new PrintWriter(fw)) {
            String timeStamp = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy").format(new Date());
            pw.println("[" + timeStamp + "][" + logType + "] " + message);
        } catch (IOException e) {
            e.printStackTrace();  
        }
    }
}