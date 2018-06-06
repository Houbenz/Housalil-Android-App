package beans;

public class Logement
{
    private enum Type
    {
        F2 , F3 , F4 , F5
    };

    private String id;
    private String address;
    private Localite localite ;
    private String type;
    public int prix;
    public String logement_picture;
    public String agent_service;

    public Logement() {
    }

    public Logement(String id, String address, Localite localite, String type, int prix, String logement_picture, String agent_service) {
        this.id = id;
        this.address = address;
        this.localite = localite;
        this.type=type;
        this.prix = prix;
        this.logement_picture = logement_picture;
        this.agent_service = agent_service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Localite getLocalite() {
        return localite;
    }

    public void setLocalite(Localite localite) {
        this.localite = localite;
    }

    public String getType() {
        return String.valueOf(this.type);
    }

    public void setType(String type)
    {
        if(type.equals("f2"))
        {
            this.type ="F2";
        }
        else if(type.equals("f3"))
        {
            this.type = "F3";
        }
        else if(type.equals("f4"))
        {
            this.type = "F4";
        }
        else
        {
            this.type = "F5";
        }
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getLogement_picture() {
        return logement_picture;
    }

    public void setLogement_picture(String logement_picture) {
        this.logement_picture = logement_picture;
    }

    public String getAgent_service() {
        return agent_service;
    }

    public void setAgent_service(String agent_service) {
        this.agent_service = agent_service;
    }
}
