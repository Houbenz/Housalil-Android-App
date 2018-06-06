package databases;

import java.util.ArrayList;

import beans.Localite;
import beans.Logement;
import beans.Utilisateur;
import beans.Visite;

/**
 * Created by Houbenz on 27/05/2018.
 */

public interface CallBack {


        void onSuccess(ArrayList<Visite> visites);

        void onSuccessL(ArrayList<Logement> logements);

        void onSuccesPrendreVisite(Visite visite);

        void onSuccesConsulterCompte(Utilisateur utilisateur, Localite localite);

}
