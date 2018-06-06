package bean;

import java.sql.Date;
import java.sql.Time;

public class Operation
{
    private int id;
    private String username_client;
    private String username_operateur;
    private String service;
    private Date service_date;
    private Time service_heure;

    public Operation() {
    }

    public Operation(int id, String username_client, String username_operateur, String service, Date service_date, Time service_heure) {
        this.id = id;
        this.username_client = username_client;
        this.username_operateur = username_operateur;
        this.service = service;
        this.service_date = service_date;
        this.service_heure = service_heure;
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

    public String getUsername_operateur() {
        return username_operateur;
    }

    public void setUsername_operateur(String username_operateur) {
        this.username_operateur = username_operateur;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getService_date() {
        return service_date;
    }

    public void setService_date(Date service_date) {
        this.service_date = service_date;
    }

    public Time getService_heure() {
        return service_heure;
    }

    public void setService_heure(Time service_heure) {
        this.service_heure = service_heure;
    }
}
