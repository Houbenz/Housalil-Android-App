package arrayAdapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.navigationviewanddeveloper.R;

import java.util.ArrayList;

import beans.Logement;

/**
 * Created by Houbenz on 17/05/2018.
 */

public class ArrayAdapterGrid extends ArrayAdapter<Logement> {

    public ArrayAdapterGrid(Context context ,ArrayList<Logement> logements){

        super(context, R.layout.logement,logements);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View logement_layout =(View)inflater.inflate(R.layout.logement,parent,false);

        Logement logement= getItem(position);

        TextView logementd=(TextView) logement_layout.findViewById(R.id.logement);
        ImageView imageL=(ImageView) logement_layout.findViewById(R.id.imageL);
        TextView type=(TextView)logement_layout.findViewById(R.id.type);
        TextView localite=(TextView) logement_layout.findViewById(R.id.localite);

        logementd.setText(logement.getId());
        type.setText(logement.getType());
        localite.setText(logement.getLocalite().getAddress());

        imageL.setImageResource(R.drawable.apartement);
        View v =logement_layout.findViewById(R.id.linear);
        v.setElevation(24);
        return logement_layout;
    }
}
