package com.example.android.navigationviewanddeveloper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RechercherLogement extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RechercherLogement() {
    }

    public static RechercherLogement newInstance(String param1, String param2) {
        RechercherLogement fragment = new RechercherLogement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FrameLayout frameLayout =(FrameLayout) inflater.inflate(R.layout.fragment_rechercher_logement, container, false);


        // liste deroulante pour le choix de la loclaite
       final Spinner localiteSpinner =(Spinner) frameLayout.findViewById(R.id.localiteSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter =ArrayAdapter.createFromResource(getContext(),R.array.localite,android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localiteSpinner.setAdapter(spinnerAdapter);
        localiteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i);}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}});

        // liste deroulante pour le choix du type du logement
        final Spinner typeSpinner =(Spinner)frameLayout.findViewById(R.id.typeSpinner);
        spinnerAdapter=ArrayAdapter.createFromResource(getContext(),R.array.typelogement,android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}});


        final SeekBar seekBar =(SeekBar)frameLayout.findViewById(R.id.seekBar);
        final TextView max =(TextView)frameLayout.findViewById(R.id.max);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                max.setText(""+progress+" DZD");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}});


        Button submit =(Button)frameLayout.findViewById(R.id.submit);
        Button.OnClickListener onClickListener =new Button.OnClickListener(){
            @Override
            public void onClick(View view){

                int budget =seekBar.getProgress();

                String localite;
                if(localiteSpinner.getSelectedItemPosition()==0)
                    localite="%";
                else
                    localite =""+(localiteSpinner.getSelectedItemPosition());


                String typeLogement;
                if(typeSpinner.getSelectedItem().toString().equals("Toutes types"))
                    typeLogement ="%";
                else
                    typeLogement =typeSpinner.getSelectedItem().toString();

                Intent intent =new Intent(getContext(),ChoisirLogementA.class);
                intent.putExtra("localite",localite);
                intent.putExtra("typeLogement",typeLogement);
                intent.putExtra("budget",budget);
                String username = getArguments().getString("username");
                intent.putExtra("username",username);

                startActivity(intent);
                ;}
        };

        submit.setOnClickListener(onClickListener);

        return frameLayout ;
    }

    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int position);
    }


}
