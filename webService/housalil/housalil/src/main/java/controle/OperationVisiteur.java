package controle;

import bean.*;

import java.sql.*;
import java.util.ArrayList;

public class OperationVisiteur
{
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    /************************************* Constructor ********************************/
    public OperationVisiteur()
    {
        this.dbConnection();
    }


    /************************************* Authentification ********************************/
    public String authentification(String username , String password)
    {
        try
        {
            preparedStatement = connection.prepareStatement("SELECT * FROM utilisateur WHERE username LIKE ? AND password LIKE ?");
            preparedStatement.setString(1 , username);
            preparedStatement.setString(2 , password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                if (resultSet.getString("etat").equals("inactif") || resultSet.getString("etat").equals("bloque"))
                {
                	connection.close();
                    return "Votre compte est inaccessible";
                }
                else
                {
                	String type =resultSet.getString("type");
                	connection.close();
                    return type;
                }
            }
            else
            {
            	connection.close();
                return "Username/Password Invalide";
            }
        }
        catch (SQLException e)
        {
            return "Operation echouee";
        }
    }


    /************************************* Inscription ********************************/
    public String inscription(Utilisateur utilisateur , int localite)
    {
        int nombreTuple = 0;
        String type = utilisateur.getType();
        try
        {
            preparedStatement = connection.prepareStatement("SELECT * FROM utilisateur WHERE username LIKE ? OR email LIKE ?");
            preparedStatement.setString(1, utilisateur.getUsername());
            preparedStatement.setString(2, utilisateur.getEmail());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
            	connection.close();
                return "Username/Email taken";
            }

            preparedStatement = connection.prepareStatement("INSERT INTO  utilisateur VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, utilisateur.getUsername());
            preparedStatement.setString(2, utilisateur.getPassword());
            preparedStatement.setString(3, utilisateur.getNom());
            preparedStatement.setString(4, utilisateur.getPrenom());
            preparedStatement.setString(5, utilisateur.getEmail());
            preparedStatement.setString(6, utilisateur.getNumTel());
            preparedStatement.setString(7, utilisateur.getProfile_picture());
            preparedStatement.setString(8, utilisateur.getEtat());
            preparedStatement.setString(9, utilisateur.getType());
            preparedStatement.executeUpdate();

            if(type.equals("Agent"))
            {
                preparedStatement = connection.prepareStatement("INSERT INTO " + type + " VALUES (?,?)");
                preparedStatement.setString(1, utilisateur.getUsername());
                preparedStatement.setInt(2, localite);
                nombreTuple = preparedStatement.executeUpdate();
            }
            else
            {
                preparedStatement = connection.prepareStatement("INSERT INTO " + type + " VALUES (?)");
                preparedStatement.setString(1, utilisateur.getUsername());
                nombreTuple = preparedStatement.executeUpdate();
            }

            if (nombreTuple == 0)
            {
            	connection.close();
                return "Inscription echouee";
            }
            else
            {
            	connection.close();
                return "Inscription faite";
            }
        }
        catch (SQLException e)
        {
            return "Operation echouee";
        }
    }


    /************************************* Contact Us ********************************/
    public String contactUs(Message message)
    {
        int nombreTuples = 0;
        try
        {
            preparedStatement = connection.prepareStatement("INSERT INTO message(nom , prenom , numTel ,email , titre , contenu , message_date , message_heure) VALUES (?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, message.getNom());
            preparedStatement.setString(2, message.getPrenom());
            preparedStatement.setString(3, message.getNumTel());
            preparedStatement.setString(4, message.getEmail());
            preparedStatement.setString(5, message.getTitre());
            preparedStatement.setString(6, message.getContenu());
            preparedStatement.setDate(7, message.getMessage_date());
            preparedStatement.setTime(8, message.getMessage_heure());
            nombreTuples = preparedStatement.executeUpdate();

            if(nombreTuples == 0)
            {
            	connection.close();
                return "Opération échouée";
            }
            else
            {
            	connection.close();
                return "Message envoyé";
            }
        }
        catch (SQLException e)
        {
            return "Operation echouee";
        }
    }

    /************************************* Recherche ********************************/
    public ArrayList<Logement> recherche(String type, String localite , int prix)
    {
        ArrayList<Logement> logements = new ArrayList<Logement>();
        Logement logement = null;
        try
        {
            preparedStatement = connection.prepareStatement(
            		 "SELECT * FROM logement,localite WHERE logement.localite = localite.id "
            		+ "AND logement.type LIKE ? "
            		+ "AND logement.localite LIKE ? "
            		+ "AND prix <= ? "
            		+ "AND logement.id NOT IN "
            		+ "(SELECT contrat.logement FROM contrat)");
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, localite);
            preparedStatement.setInt(3, prix);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                logement = new Logement(resultSet.getString("id"),
                        resultSet.getString("address"),
                        new Localite(resultSet.getInt("localite.id"),resultSet.getString("localite.address")),
                        resultSet.getString("type"),
                        resultSet.getInt("prix"),
                        resultSet.getString("logement_picture"),
                        resultSet.getString("agent_service"));
                logements.add(logement);
            }
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return logements;
        }
        return logements;
    }
    /************************************* Search Agent ********************************/
    public ArrayList<Agent> getAgents(String localite)
    {
        ArrayList<Agent> agents = new ArrayList<Agent>();
        Agent agent = null;
        try
        {
            preparedStatement = connection.prepareStatement("SELECT * FROM utilisateur,agent,localite WHERE utilisateur.username = agent.username AND agent.localite = localite.id AND localite LIKE ?");
            preparedStatement.setString(1, localite);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                agent = new Agent(resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("numero_Tel"),
                        resultSet.getString("etat"),
                        resultSet.getString("type"),
                        resultSet.getString("profile_picture"),
                        new Localite(resultSet.getInt("localite.id"), resultSet.getString("localite.address")));
                agents.add(agent);
            }
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return agents;
        }
        return agents;
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
