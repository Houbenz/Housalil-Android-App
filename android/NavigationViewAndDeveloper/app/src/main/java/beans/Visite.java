package beans;


public class Visite
{
    private enum Etat
    {
        Encours , Termine , Annule , ClientAbsent , AgentAbsent
    }
    private int id;
    private String username_client;
    private String username_agent;
    private String logement;
    private long visite_date;
    private long visite_heure;
    private int preavis;
    private String etat;

    public Visite() {
    }

    public Visite(String username_client, String username_agent, String logement, long visite_date, long visite_heure, int preavis, String etat) {
        this.username_client = username_client;
        this.username_agent = username_agent;
        this.logement = logement;
        this.visite_date = visite_date;
        this.visite_heure = visite_heure;
        this.preavis = preavis;
        this.etat=etat;
    }

    public Visite(int id, String username_client, String username_agent, String logement, long visite_date, long visite_heure, int preavis, String etat) {
        this.id = id;
        this.username_client = username_client;
        this.username_agent = username_agent;
        this.logement = logement;
        this.visite_date = visite_date;
        this.visite_heure = visite_heure;
        this.preavis = preavis;
        this.etat=etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername_client() {
        return username_client;
    }

    public void setUsername_client(String username_client) {this.username_client = username_client;}

    public String getUsername_agent() {
        return username_agent;
    }

    public void setUsername_agent(String username_agent) {
        this.username_agent = username_agent;
    }

    public String getLogement() {
        return logement;
    }

    public void setLogement(String logement) {
        this.logement = logement;
    }

    public long getVisite_date() {return visite_date;}

    public void setVisite_date(long visite_date) {
        this.visite_date = visite_date;
    }

    public long getVisite_heure() {
        return visite_heure;
    }

    public void setVisite_heure(long visite_heure) {
        this.visite_heure = visite_heure;
    }

    public int getPreavis() {
        return preavis;
    }

    public void setPreavis(int preavis) {
        this.preavis = preavis;
    }

    public String getEtat() {
        return this.etat;
    }

    public void setEtat(String etat)
    {
        if(etat.equals("encours"))
        {
            this.etat = "Encours";
        }
        else if(etat.equals("termine"))
        {
            this.etat = "Termine";
        }
        else if(etat.equals("annule"))
        {
            this.etat = "Annule";
        }
        else if(etat.equals("clientabsent"))
        {
            this.etat = "ClientAbsent";
        }
        else
        {
            this.etat = "AgentAbsent";
        }
    }
}
