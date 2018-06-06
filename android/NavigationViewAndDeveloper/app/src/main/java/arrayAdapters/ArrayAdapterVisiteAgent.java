package arrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.navigationviewanddeveloper.R;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.zip.Inflater;

import beans.Visite;

/**
 * Created by Houbenz on 02/06/2018.
 */

public class ArrayAdapterVisiteAgent extends ArrayAdapter<Visite> {



    public ArrayAdapterVisiteAgent(@NonNull Context context, ArrayList<Visite> visites) {
        super(context, R.layout.visite_agent,visites);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(getContext());

        Visite visite =getItem(position);

        View view =(View)inflater.inflate(R.layout.visite_agent,parent,false);

        TextView client=(TextView)view.findViewById(R.id.client);
        TextView logement=(TextView)view.findViewById(R.id.logement);
        TextView type=(TextView)view.findViewById(R.id.type);
        TextView localite=(TextView)view.findViewById(R.id.localite);
        TextView dateheure=(TextView)view.findViewById(R.id.dateheure);
        RatingBar preavis=(RatingBar) view.findViewById(R.id.preavisO);

        client.setText(visite.getUsername_client());
        logement.setText(visite.getLogement());

        Date date = new Date(visite.getVisite_date());
        Time heure =new Time(visite.getVisite_heure());
        dateheure.setText(date+" à "+heure);
        preavis.setIsIndicator(true);
        preavis.setRating(visite.getPreavis());

         //   ViewGroup.LayoutParams lp =view.getLayoutParams();
         //   view.requestLayout();
        return view;
    }
}

