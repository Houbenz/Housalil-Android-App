package beans;

import java.sql.Date;
import java.sql.Time;

public class Notification
{
    private enum  Etat
    {
        Lu , Envoye
    }
    private int id;
    private String username_client;
    private String titre;
    private String contenu;
    private Date notification_date;
    private Time notification_heure;
    Etat etat;

    public Notification() {
    }

    public Notification(int id, String titre , String username_client, String contenu, Date notification_date, Time notification_heure, String etat) {
        this.id = id;
        this.titre = titre;
        this.username_client = username_client;
        this.contenu = contenu;
        this.notification_date = notification_date;
        this.notification_heure = notification_heure;
        this.setEtat(etat);
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(Date notification_date) {
        this.notification_date = notification_date;
    }

    public Time getNotification_heure() {
        return notification_heure;
    }

    public void setNotification_heure(Time notification_heure) {
        this.notification_heure = notification_heure;
    }

    public String getEtat()
    {
        return String.valueOf(this.etat);
    }

    public void setEtat(String etat)
    {
        if(etat.equals("lu"))
        {
            this.etat = Etat.Lu;
        }
        else
        {
            this.etat = Etat.Envoye;
        }
    }
}
