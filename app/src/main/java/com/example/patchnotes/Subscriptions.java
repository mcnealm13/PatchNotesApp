package com.example.patchnotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class Subscriptions extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
               NavHostFragment.findNavController(Subscriptions.this).navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //Restore subscription values to Switches and save changes from Switches
        //Hearthstone
        Switch hearthstoneSwitch = (Switch) view.findViewById(R.id.Hearthstone);
        hearthstoneSwitch.setChecked(sharedPref.getBoolean(getString(R.string.hearthstone), false));
        hearthstoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.hearthstone), isChecked);
                editor.apply();
                if (isChecked) {
                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.hearthstone))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = "";
                                    if (!task.isSuccessful()) {
                                        msg = "Error: Subscribe Failed";
                                        hearthstoneSwitch.setChecked(false);
                                    } else {
                                        msg = "Successfully Subscribed";
                                        hearthstoneSwitch.setChecked(true);
                                    }
                                    Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.hearthstone))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = "";
                                    if (!task.isSuccessful()) {
                                        msg = "Unsubscribe Failed";
                                        hearthstoneSwitch.setChecked(true);
                                    } else {
                                        msg = "Successfully Unsubscribed";
                                        hearthstoneSwitch.setChecked(false);
                                    }
                                    Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        //Game 2

    }
}