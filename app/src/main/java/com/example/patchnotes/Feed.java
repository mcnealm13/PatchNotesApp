package com.example.patchnotes;

import android.content.Context;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Feed extends Fragment {
    private static final String TAG = "FEEDMatthew";
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String [] streamdata = readFromFile(view.getContext());
        int numViewsToAdd = streamdata.length / 4;
        for (int i = 0; i < numViewsToAdd; i++) {
            final TextView rowTextView = new TextView(view.getContext());
            //Every update has 4 entries
            rowTextView.setText(streamdata[i * 4] + "\n" + streamdata[i * 4 + 1] +
                    "\n" + streamdata[i * 4 + 2] + "\n " + streamdata[i * 4 + 3]);
            //Add UI details about text view being added
            Linkify.addLinks(rowTextView, Linkify.ALL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(30,10,10,10);
            rowTextView.setLayoutParams(params);
            LinearLayout linearlayout = (LinearLayout) view.findViewById(R.id.feed);
            linearlayout.addView(rowTextView);
        }

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Feed.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

            }

        });
    }

    private String[] readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("streamdata.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        //^_@ is my delimeter, splitting into individual strings
        String[] result = ret.split("\\^_@");
        return result;
    }
}