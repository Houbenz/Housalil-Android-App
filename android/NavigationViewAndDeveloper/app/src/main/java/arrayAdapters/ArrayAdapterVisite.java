package arrayAdapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.navigationviewanddeveloper.R;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import beans.Visite;


public class ArrayAdapterVisite extends ArrayAdapter<Visite> {


    public ArrayAdapterVisite(@NonNull Context context, ArrayList<Visite> visites) {
        super(context, R.layout.visite_client,visites);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View visite_client =(View)inflater.inflate(R.layout.visite_client,parent,false);

        Visite visite = getItem(position);

        TextView agent=(TextView)visite_client.findViewById(R.id.agent);
        TextView dateheure=(TextView)visite_client.findViewById(R.id.dateheure);
        TextView logement=(TextView)visite_client.findViewById(R.id.logement);
        ImageView imagelog=(ImageView) visite_client.findViewById(R.id.imagelog);
        TextView etat =(TextView)visite_client.findViewById(R.id.etat);

        agent.setText(visite.getUsername_agent());

        Date  date = new Date(visite.getVisite_date());
        Time heure = new Time(visite.getVisite_heure());


        dateheure.setText(date+" à "+heure);
        logement.setText(visite.getLogement());
        etat.setText(visite.getEtat());
       // imagelog.setImageResource(R.mipmap.ic_launcher);


        return visite_client;
    }
}
