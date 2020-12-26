package com.example.patchnotes;
import android.content.Context;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            Log.d(TAG, "Message data payload: " + data);
            String soontobestored =  data.get("titledate");
            soontobestored += "^_@" + data.get("title");
            soontobestored += "^_@" + data.get("body");
            soontobestored += "^_@" + data.get("link") + "^_@";;
            //reset whole data file
            //deleteFile("streamdata.txt");
            writeToFileAppend(soontobestored);
        }

    }

    private void writeToFileAppend(String data) {
        try {
            FileOutputStream fos = openFileOutput("streamdata.txt", Context.MODE_APPEND);
            fos.write(data.getBytes());
            fos.close();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }
    private void writeToFileOverride(String data,Context context) {
        try {
            FileOutputStream fos = openFileOutput("streamdata.txt", Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

}