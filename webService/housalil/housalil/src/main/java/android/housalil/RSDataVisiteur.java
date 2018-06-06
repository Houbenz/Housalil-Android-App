package android.housalil;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bean.Logement;
import bean.Utilisateur;
import controle.OperationVisiteur;


@Path("visiteur")
public class RSDataVisiteur {

	
	
	@POST
	@Path("authentification")
	@Produces(MediaType.TEXT_HTML)
	public String authentification(@FormParam("username") String username,@FormParam("password") String password) {
		OperationVisiteur operationVisiteur=new OperationVisiteur();
		System.out.println(operationVisiteur.authentification(username, password));
		return operationVisiteur.authentification(username, password);
		
	}
	
	@POST
	@Path("rechercher")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Logement> rechercher(JsonArray jsonArray){
		OperationVisiteur operationVisiteur =new OperationVisiteur();

		
		JsonObject jsonObject=jsonArray.getJsonObject(1);
		String type=jsonObject.getString("type") ;
		String localite =jsonObject.getString("localite");
		int prix =Integer.parseInt(jsonObject.getString("budget"));
		System.out.println("im here "+type+" "+localite+" "+prix+" ");
		System.out.println();
		
		ArrayList<Logement> logements=operationVisiteur.recherche(type, localite, prix);
		for(Logement l : logements) {
			System.out.println(l.getId());
		}
		return logements;
		
	}
	@POST
	@Path("inscription")
	@Produces(MediaType.TEXT_HTML)
	public String inscription(
			@FormParam("username")String username,@FormParam("nom")String nom,
			@FormParam("prenom")String prenom,@FormParam("email")String email,
			@FormParam("pass")String pass,@FormParam("numTel")String numTel) {
		
		OperationVisiteur operationVisiteur = new OperationVisiteur();
		
		String etat="actif";
		String type="Client";
		String image ="/Assets/Images/accounts.png";
		Utilisateur utilisateur=new Utilisateur(username,pass,nom,prenom,email,numTel,etat,type,image);
		String response =operationVisiteur.inscription(utilisateur, 0);
		System.out.println(response);
		return response;
	}
	
}
