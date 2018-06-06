package controle;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

import bean.Agent;
import bean.Localite;
import bean.Notification;
import bean.Visite;

public class OperationClient
{
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    /************************************* Constructor ********************************/
    public OperationClient()
    {
        this.dbConnection();
    }

    /************************************* Prendre Visite ********************************/
    public Visite prendreVisite(String idLogement , String username_client , Date date)
    {
        int loopIteration = 30;
        if (date != null && date.after(Date.valueOf(LocalDate.now())))
        {
            loopIteration = 0;
        }
        Visite visite = null;
        Date dateVisite = null;
        ArrayList<Time> heuresTravail = null;
        ArrayList<Time> heuresLibre = null;
        for (int i = 0; i <= loopIteration; i++)    //Test 31 Date de visite si le client ne precise pas une date.
        {
            if(date != null && date.after(Date.valueOf(LocalDate.now())))
            {
                dateVisite = Date.valueOf(date.toLocalDate().plusDays(i));
            }
            else
            {
                dateVisite = Date.valueOf(LocalDate.now().plusDays(i));
            }
            heuresTravail = new ArrayList<Time>();
            heuresLibre = new ArrayList<Time>();
            try
            {
                // Trouver les heures libres
                preparedStatement = connection.prepareStatement("SELECT visite_heure FROM visite WHERE (logement LIKE ? OR username_client LIKE ?) AND visite_date LIKE ?");
                preparedStatement.setString(1, idLogement);
                preparedStatement.setString(2, username_client);
                preparedStatement.setDate(3, dateVisite);
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next())
                {
                    heuresTravail.add(resultSet.getTime("visite_heure"));
                }
                for (int j = 8; j < 17; j++)
                {
                    if(!heuresTravail.contains(Time.valueOf(j + ":00:00")))
                    {
                    	
       if(((!dateVisite.equals(Date.valueOf(LocalDate.now())) || Time.valueOf(j + ":00:00").after(Time.valueOf(LocalTime.now())))))
                          {
                            heuresLibre.add(Time.valueOf(j + ":00:00"));
                        }
                    }
                }
                // Trouver un agent libre de la meme localite que le logement
                for (Time heure : heuresLibre)
                {
                    preparedStatement = connection.prepareStatement("SELECT username FROM agent , logement WHERE logement.id LIKE ? AND agent.localite = logement.localite AND agent.username NOT IN "
                    		+ "(SELECT username_agent FROM visite WHERE visite_date LIKE ? AND visite_heure LIKE ? OR username_agent IN"
                    		+ " (SELECT username FROM utilisateur WHERE type LIKE ? AND etat IN"
                    		+ " ('inactif' , 'bloque'))) ORDER BY  rand()");
                    preparedStatement.setString(1, idLogement);
                    preparedStatement.setDate(2, dateVisite);
                    preparedStatement.setTime(3, heure);
                    preparedStatement.setString(4, "Agent");
                    resultSet = preparedStatement.executeQuery();
                    if(resultSet.next())
                    {
                        visite = new Visite(username_client , resultSet.getString("username") , idLogement , dateVisite.getTime() , heure.getTime() , 0 , "Encours");
                        
                        return visite;
                    }
                }
            }
            catch (SQLException e)
            {
                return visite;
            }
        }
        return visite;
    }

    /************************************* Modifier Visite ********************************/
    public String modifierVisite(int idVisite , String idLogement , String username_client , Date date)
    {
        Visite visite = null;
        if(date.after(Date.valueOf(LocalDate.now())))
        {
            visite = this.prendreVisite(idLogement , username_client , date);
        }
        else
        {
            return "Date invalide veuillez donner une autre date";
        }

        if (visite == null )
        {
            return "La date entré est chargé veuillez donner une autre date";
        }
        else
        {
            try
            {
                preparedStatement = connection.prepareStatement("UPDATE visite SET visite_date = ? , visite_heure = ? WHERE id = ?");
              Date datenew = new Date(visite.getVisite_date());
              Time heurenew = new Time(visite.getVisite_heure());
                
                preparedStatement.setDate(1,datenew);
                preparedStatement.setTime(2,heurenew);
                preparedStatement.setInt(3, idVisite);
                preparedStatement.executeUpdate();

                return "Visite date modifié";
            }
            catch (SQLException e)
            {
                return "Modfication échouée";
            }
        }
    }

    /************************************* Ajouter Visite ********************************/
    public String ajouterRDV(Visite visite){
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO visite(username_client , username_agent , logement , visite_date , visite_heure , preavis , etat) VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setString(1, visite.getUsername_client());
            preparedStatement.setString(2, visite.getUsername_agent());
            preparedStatement.setString(3, visite.getLogement());
            
            Date date =new Date(visite.getVisite_date());
            Time heure= new Time(visite.getVisite_heure());
            preparedStatement.setDate(4,date );
            preparedStatement.setTime(5, heure);
            preparedStatement.setInt(6, visite.getPreavis());
            preparedStatement.setString(7, visite.getEtat());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
            return "Opération échouée";

        }
        return "succes";
    }

    /************************************* Consulter Visite ********************************/
    public Agent consulterProfileAgent(String username)
    {
        Agent agent = null;
        try
        {
            preparedStatement = connection.prepareStatement("SELECT * FROM utilisateur,agent,localite WHERE utilisateur.username = agent.username AND agent.localite = localite.id AND utilisateur.username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                agent = new Agent(resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("numero_tel"),
                        resultSet.getString("etat"),
                        resultSet.getString("type"),
                        resultSet.getString("profile_picture"),
                        new Localite(resultSet.getInt("localite.id") , resultSet.getString("localite.address")));
            }

            return agent;
        }
        catch (SQLException e)
        {
            return agent;
        }
    }

    /************************************* Annuler Visite ********************************/
    public String annulerVisite(int idVisite)
    {
        try
        {
            preparedStatement = connection.prepareStatement("UPDATE visite SET etat = ? WHERE visite.id = ?");
            preparedStatement.setString(1, "Annule");
            preparedStatement.setInt(2, idVisite);
            preparedStatement.executeUpdate();
            return "Visite annulé";
        }
        catch (SQLException e)
        {
            return "Opération échouée";
        }
    }

    /************************************* Consulter Visite ********************************/
    public ArrayList<Visite> consulterVisite(String client)
    {
        ArrayList<Visite> visites = new ArrayList<Visite>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM visite WHERE username_client LIKE ?");
            preparedStatement.setString(1, client);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
            	
            	Date date =resultSet.getDate("visite_date");
            	Time time =resultSet.getTime("visite_heure");
            	
            	
                visites.add(new Visite(
                        resultSet.getInt("id"),
                        resultSet.getString("username_client"),
                        resultSet.getString("username_agent"),
                        resultSet.getString("logement"),
                        date.getTime(),
                        time.getTime(),
                        resultSet.getInt("preavis"),
                        resultSet.getString("etat")));
            }
        }
        catch(SQLException e)
        {
            return visites;
        }
        return visites;
    }

    /************************************* Consulter Notification ********************************/
    /************************************* Notification Client ********************************/
    public ArrayList<Notification> getNotifications(String client)
    {
        ArrayList<Notification> notifications = new ArrayList<>();
        try
        {
            preparedStatement = connection.prepareStatement("SELECT * FROM notification WHERE username_client like ? ORDER BY etat , notification.notification_date DESC LIMIT 7");
            preparedStatement.setString(1,client);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                notifications.add(new Notification(
                        resultSet.getInt("id"),
                        resultSet.getString("username_client"),
                        resultSet.getString("titre"),
                        resultSet.getString("contenu"),
                        resultSet.getDate("notification_date"),
                        resultSet.getTime("notification_heure"),
                        resultSet.getString("etat")
                ));
            }

            return notifications;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return notifications;
        }
    }

    /************************************* Notification Client Lu ********************************/
    public void notificationLu(String username_client , int idNotification)
    {
        try {
            preparedStatement = connection.prepareStatement("UPDATE notification SET etat = ? WHERE username_client like ? AND id = ? AND etat = ?");
            preparedStatement.setString(1, "lu");
            preparedStatement.setString(2, username_client);
            preparedStatement.setInt(3, idNotification);
            preparedStatement.setString(4, "envoye");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /************************************* Nombre Notification Client ********************************/
    public int nombreNotifications(String username_client){
        try {
            preparedStatement = connection.prepareStatement("SELECT count(id) AS nombreNotification FROM notification WHERE username_client like ? AND etat = ?");
            preparedStatement.setString(1, username_client);
            preparedStatement.setString(2, "envoye");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("nombreNotification");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /************************************* Database Connection ********************************/
    private void dbConnection()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agence_housalil" , "root" , "Benz231");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
		OperationClient operationClient =new OperationClient();
		
		Date date = Date.valueOf(LocalDate.now());
		System.out.println(date +" azdza");
		Visite visites = operationClient.prendreVisite("1b01a11", "salim101", date);
		
		
		System.out.println(visites.getVisite_heure()+" "+visites.getVisite_date());
		
		/*for(Visite v : visites) {
			Date date1 = new Date(v.getVisite_heure());
			Time heure = new Time(v.getVisite_date());
			System.out.println(date1+" "+heure);
		}
		*/

	}
}
