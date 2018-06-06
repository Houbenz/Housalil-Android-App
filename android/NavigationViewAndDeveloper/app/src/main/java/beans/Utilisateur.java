package beans;

public class Utilisateur
{
    protected enum Etat
    {
        actif , inactif , bloque , demandeBloque
    };

    protected enum Type
    {
        Client , Agent , Operateur , AgentServiceVente
    };

    protected String username;
    protected String password;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String numTel;
    protected String  etat;
    protected String type;
    protected String profile_picture;

    public Utilisateur()
    {

    }

    public Utilisateur(String username, String password, String nom, String prenom, String email, String numTel, String etat, String type)
    {
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
        this.setEtat(etat);
        this.setType(type);
    }

    public Utilisateur(String username, String password, String nom, String prenom, String email, String numTel, String etat, String type, String profile_picture)
    {
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
        this.setEtat(etat);
        this.setType(type);
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getEtat() {
        return String.valueOf(etat);
    }

    public void setEtat(String etat) {
        if(etat.equals("actif"))
        {
            this.etat = "actif";
        }
        else if(etat.equals("inactif"))
        {
            this.etat = "inactif";
        }
        else if(etat.equals("bloque"))
        {
            this.etat = "bloque";
        }
        else
        {
            this.etat = "demandeBloque";
        }
    }

    public String getType() {
        return String.valueOf(type);
    }

    public void setType(String type) {
        if(type.equals("Client"))
        {
            this.type = "Client";
        }
        else if(type.equals("Agent"))
        {
            this.type = "Agent";
        }
        else if(type.equals("Operateur"))
        {
            this.type = "Operateur";
        }
        else
        {
            this.type = "AgentServiceVente";
        }
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
