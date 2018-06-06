package beans;

import java.sql.Date;
import java.sql.Time;

public class Message
{
    private int id;
    private String nom;
    private String prenom;
    private String numTel;
    private String email;
    private String titre;
    private String contenu;
    private Date message_date;
    private Time message_heure;

    public Message() {
    }

    public Message(String nom, String prenom, String numTel, String email, String titre, String contenu, Date message_date, Time message_heure) {
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
        this.email = email;
        this.titre = titre;
        this.contenu = contenu;
        this.message_date = message_date;
        this.message_heure = message_heure;
    }

    public Message(int id, String nom, String prenom, String numTel, String email, String titre, String contenu, Date message_date, Time message_heure) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
        this.email = email;
        this.titre = titre;
        this.contenu = contenu;
        this.message_date = message_date;
        this.message_heure = message_heure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getMessage_date() {
        return message_date;
    }

    public void setMessage_date(Date message_date) {
        this.message_date = message_date;
    }

    public Time getMessage_heure() {
        return message_heure;
    }

    public void setMessage_heure(Time message_heure) {
        this.message_heure = message_heure;
    }
}
