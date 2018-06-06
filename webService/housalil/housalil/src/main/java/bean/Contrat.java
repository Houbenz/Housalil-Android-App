package bean;

import java.sql.Date;
import java.sql.Time;

public class Contrat
{
    private int id;
    private String username_client;
    private String username_agentservice;
    private String logement;
    private Date contrat_date;
    private Time contrat_heure;

    public Contrat() {
    }

    public Contrat(int id, String username_client, String username_agentservice, String logement, Date contrat_date, Time contrat_heure) {
        this.id = id;
        this.username_client = username_client;
        this.username_agentservice = username_agentservice;
        this.logement = logement;
        this.contrat_date = contrat_date;
        this.contrat_heure = contrat_heure;
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

    public void setUsername_client(String username_client) {
        this.username_client = username_client;
    }

    public String getUsername_agentservice() {
        return username_agentservice;
    }

    public void setUsername_agentservice(String username_agentservice) {
        this.username_agentservice = username_agentservice;
    }

    public String getLogement() {
        return logement;
    }

    public void setLogement(String logement) {
        this.logement = logement;
    }

    public Date getContrat_date() {
        return contrat_date;
    }

    public void setContrat_date(Date contrat_date) {
        this.contrat_date = contrat_date;
    }

    public Time getContrat_heure() {
        return contrat_heure;
    }

    public void setContrat_heure(Time contrat_heure) {
        this.contrat_heure = contrat_heure;
    }
}
