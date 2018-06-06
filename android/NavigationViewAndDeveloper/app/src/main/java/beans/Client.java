package beans;

public class Client extends Utilisateur
{
    public Client() {
    }

    public Client(String username, String password, String nom, String prenom, String email, String numTel, String etat, String type) {
        super(username, password, nom, prenom, email, numTel, etat, type);
    }

    public Client(String username, String password, String nom, String prenom, String email, String numTel, String etat, String type, String profile_picture) {
        super(username, password, nom, prenom, email, numTel, etat, type, profile_picture);
    }
}
