package controle;

import bean.Visite;

import java.sql.*;
import java.util.ArrayList;

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
    public String validerVisite(int idVisite , int preavisVisite , String etatVisite)
    {
        try {
            preparedStatement = connection.prepareStatement("UPDATE visite SET preavis = ? , etat = ? WHERE id = ?");
            preparedStatement.setInt(1, preavisVisite);
            preparedStatement.setString(2, etatVisite);
            preparedStatement.setInt(3, idVisite);
            preparedStatement.executeUpdate();
        }catch (SQLException e)
        {
            return "Operation echoue";
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
