package databases;



public class Links {


    private String SERVER_IP_ADDRESS="http://192.168.1.9:8080";

    public Links()  {
    }

    public String getLoginActivityURL()
    {
        return SERVER_IP_ADDRESS+"/housalil/visiteur/authentification";
    }

    public String getRegisterURL()
    {
        return SERVER_IP_ADDRESS+"/housalil/visiteur/inscription";
    }

    public String getConsulterVisiteURL()
    {
        return SERVER_IP_ADDRESS+"/housalil/client/consulterVisiteClient";
    }

    public String getRechercherURL(){
        return SERVER_IP_ADDRESS+"/housalil/visiteur/rechercher";
    }

    public String getPrendreVisiteURL()
    {
        return SERVER_IP_ADDRESS+"/housalil/client/prendreVisite";
    }

    public String getAjouterRDVURL(){
        return  SERVER_IP_ADDRESS+"/housalil/client/ajouterRDV ";
    }


    public String getConsulterVisiteAgent()
    {
        return SERVER_IP_ADDRESS+"/housalil/agent/consulterVisiteAgent";
    }

    public String getConsulterCompteURL()
    {
        return  SERVER_IP_ADDRESS+"/housalil/utilisateur/consulterCompte";
    }
    public String getModifierCompteURL()
    {
        return SERVER_IP_ADDRESS+"/housalil/utilisateur/modifierCompte";
    }

    public String getValiderVisiteURL()
    {
        return  SERVER_IP_ADDRESS+"/housalil/agent/validerVisite";
    }

    public String getAnnulerVisite(){
        return  SERVER_IP_ADDRESS+"/housalil/client/annulerVisite";
    }
}
