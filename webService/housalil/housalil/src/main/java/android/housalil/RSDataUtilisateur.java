package android.housalil;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bean.Agent;
import bean.Utilisateur;
import controle.OperationUtilisateur;

@Path("utilisateur")
public class RSDataUtilisateur {
	
	
	
	@POST
	@Path("consulterCompte")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Utilisateur consulterCompte(JsonObject json) {
		String username = json.getString("username");
		String type = json.getString("type");
		OperationUtilisateur operationUtilisateur =new OperationUtilisateur();

		System.out.println("im executed" +"  "+ type +" "+username);
		Utilisateur utilisateur= operationUtilisateur.consulterCompte(username,type);
	
		return utilisateur;

	}

	@POST
	@Path("modifierCompte")
	@Produces(MediaType.TEXT_HTML)
	public String modifierCompte(@FormParam("originalUser")String originalUser
			,@FormParam("nom")String nom,@FormParam("prenom")String prenom,
			@FormParam("username")String username,@FormParam("email")String email,@FormParam("numTel")String numTel) {
			
			OperationUtilisateur operationUtilisateur =new OperationUtilisateur();
			
			Utilisateur utilisateur =new Utilisateur();
			System.out.println(nom +" "+prenom+" "+username+" "+email+" "+numTel);
			utilisateur.setNom(nom);
			utilisateur.setPrenom(prenom);
			utilisateur.setEmail(email);
			utilisateur.setUsername(username);
			utilisateur.setNumTel(numTel);
			
			System.out.println(utilisateur.getNom()+" "+utilisateur.getNom()+
					" "+utilisateur.getUsername()+" "+utilisateur.getEmail()+" "+utilisateur.getNumTel()+"  original : "
							+ ""+originalUser);
			
			
			String response = operationUtilisateur.modifierCompte(originalUser, utilisateur);
		
		return response;
	}
}
