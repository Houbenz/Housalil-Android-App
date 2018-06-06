package bean;

public class Agent extends Utilisateur
{
    private Localite localite;

    public Agent() {
    }

    public Agent(String username, String password, String nom, String prenom, String email, String numTel, String etat, String type, Localite localite) {
        super(username, password, nom, prenom, email, numTel, etat, type);
        this.localite = localite;
    }

    public Agent(String username, String password, String nom, String prenom, String email, String numTel, String etat, String type, String profile_picture, Localite localite) {
        super(username, password, nom, prenom, email, numTel, etat, type, profile_picture);
        this.localite = localite;
    }

    public Localite getLocalite() {
        return localite;
    }

    public void setLocalite(Localite localite) {
        this.localite = localite;
    }
}
