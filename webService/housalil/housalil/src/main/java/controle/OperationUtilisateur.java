package controle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Util;

import bean.Agent;
import bean.Localite;
import bean.Utilisateur;

public class OperationUtilisateur
{
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    /************************************* Constructor ********************************/
    public OperationUtilisateur()
    {
        this.dbConnection();
    }

    /************************************* Consulter Compte ********************************/
    public Utilisateur consulterCompte(String username , String type)
    {
        Utilisateur utilisateur = null;
        Agent agent=null ;
        try
        {
            if(type.equals("Agent"))
            {
                preparedStatement = connection.prepareStatement("SELECT * FROM utilisateur , agent , localite WHERE utilisateur.username = agent.username AND agent.localite = localite.id AND utilisateur.username = ?");
                preparedStatement.setString(1, username);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next())
                {
                    utilisateur = new Agent(resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("email"),
                            resultSet.getString("numero_tel"),
                            resultSet.getString("etat"),
                            resultSet.getString("type"),
                            resultSet.getString("profile_picture"),
                            new Localite(resultSet.getInt("id") , resultSet.getString("address")));
                    return utilisateur ;
                }
            }
            else
            {
                preparedStatement = connection.prepareStatement("SELECT * FROM utilisateur WHERE username = ?");
                preparedStatement.setString(1, username);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next())
                {
                    utilisateur = new Utilisateur(resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("email"),
                            resultSet.getString("numero_tel"),
                            resultSet.getString("etat"),
                            resultSet.getString("type"),
                            resultSet.getString("profile_picture"));
                }
            }
            return utilisateur;
        }
        catch (SQLException e)
        {
            return utilisateur;
        }
    }

    /************************************* Modifier Compte ********************************/
    public String modifierCompte(String username , Utilisateur utilisateur )
    {
        try
        {
            preparedStatement = connection.prepareStatement("UPDATE utilisateur SET username = ? , nom = ? , prenom = ? , email = ? , numero_tel = ? WHERE username = ?");
            preparedStatement.setString(1, utilisateur.getUsername());
            preparedStatement.setString(2, utilisateur.getNom());
            preparedStatement.setString(3, utilisateur.getPrenom());
            preparedStatement.setString(4, utilisateur.getEmail());
            preparedStatement.setString(5, utilisateur.getNumTel());
            preparedStatement.setString(6, username);
            preparedStatement.executeUpdate();
/*
            if(utilisateur.getType().equals("Agent"))
            {
                preparedStatement = connection.prepareStatement("UPDATE agent SET localite = ? WHERE username = ?");
                preparedStatement.setInt(1, localite);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
*/
            return "Compte Modifiée";
        }
        catch (SQLException e)
        {
            if(e.getMessage().split(" ")[0].equals("Duplicate"))
            {
                return "Username/Password exist déja";
            }
            else
            {
                return "Modification échouée";
            }
        }
    }

    /************************************* Changer Password ********************************/
    public String changerPassword(String username , String oldPassword , String newPassword)
    {
        int nombreTuple = 0;
        try
        {
            preparedStatement = connection.prepareStatement("UPDATE utilisateur SET password = ? WHERE username = ? AND password = ?");
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, oldPassword);
            nombreTuple = preparedStatement.executeUpdate();

            if(nombreTuple == 0)
            {
                return "Mot de pass incorrect";
            }
            else
            {
                return "Mot de pass changé";
            }
        }
        catch (SQLException e)
        {
            return "Opération échouée";
        }
    }

    /************************************* Demande bloqué Compte ********************************/
    public String demandeBloque(String username)
    {
        try
        {
            preparedStatement = connection.prepareStatement("UPDATE utilisateur SET etat = ? WHERE username = ?");
            preparedStatement.setString(1, "demandebloque");
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();

            return "Demande envoyé";
        }
        catch (SQLException e)
        {
            return "Opération échouée";
        }
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
