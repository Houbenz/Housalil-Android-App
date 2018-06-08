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

import bean.Visite;

public class OperationAgent
{
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    /************************************* Constructor ********************************/
    public OperationAgent()
    {
        this.dbConnection();
    }

    /************************************* Valider Visite ********************************/
    public String validerVisite(int idVisite, int preavisVisite, String etatVisite) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE visite SET preavis = ? , etat = ? WHERE id = ?");
            preparedStatement.setInt(1, preavisVisite);
            preparedStatement.setString(2, etatVisite);
            preparedStatement.setInt(3, idVisite);
            preparedStatement.executeUpdate();

            /* Envoyer notification au client */
            if (etatVisite.equals("agentabsent")) {
                preparedStatement = connection.prepareStatement("SELECT * FROM visite WHERE visite.id = ?");
                preparedStatement.setInt(1, idVisite);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();

                Date dateVisite = resultSet.getDate("visite_date");
                Time heureVisite = resultSet.getTime("visite_heure");

                if (dateVisite.after(Date.valueOf(LocalDate.now())) || heureVisite.after(Time.valueOf(LocalTime.now()))) {
                    String titre = "Visite Annulé";
                    String contenu = "bonjour, votre visite pour le " + resultSet.getDate("visite_date") + " à " + resultSet.getTime("visite_heure") + " a était annulé car l'agent va étre absent.";

                    preparedStatement = connection.prepareStatement("INSERT INTO notification(username_client, titre , contenu , notification_date , notification_heure , etat) VALUES (?,?,?,?,?,?)");
                    preparedStatement.setString(1, resultSet.getString("username_client"));
                    preparedStatement.setString(2, titre);
                    preparedStatement.setString(3, contenu);
                    preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
                    preparedStatement.setTime(5, Time.valueOf(LocalTime.now()));
                    preparedStatement.setString(6, "envoye");
                    preparedStatement.executeUpdate();
                }
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
          
            return "Operation echouee";
        }
        return "success";
    }


    /************************************* Consulter Visite ********************************/
    public ArrayList<Visite> consulterVisite(String agent)
    {
        ArrayList<Visite> visites = new ArrayList<Visite>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM visite WHERE username_agent LIKE ? order by etat ASC ,visite_date DESC, visite_heure DESC");
            preparedStatement.setString(1, agent);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                visites.add(new Visite(
                        resultSet.getInt("id"),
                        resultSet.getString("username_client"),
                        resultSet.getString("username_agent"),
                        resultSet.getString("logement"),
                        resultSet.getDate("visite_date").getTime(),
                        resultSet.getTime("visite_heure").getTime(),
                        resultSet.getInt("preavis"),
                        resultSet.getString("etat")));
            }
            connection.close();
        }
        catch(SQLException e)
        {
            return visites;
        }
        return visites;
    }

    /************************************* Database Connection ********************************/
    private void dbConnection()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agence_housalil" , "root" , "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
