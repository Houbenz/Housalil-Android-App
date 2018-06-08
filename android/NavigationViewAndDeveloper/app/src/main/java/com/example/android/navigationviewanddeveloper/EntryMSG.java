package com.example.android.navigationviewanddeveloper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryMSG extends Fragment {


    public EntryMSG() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.entry_msg, container, false);


        TextView title =(TextView)frameLayout.findViewById(R.id.title);

        String username = getArguments().getString("username");
        title.setText("Bonjour "+username);
        return frameLayout;
    }

}
