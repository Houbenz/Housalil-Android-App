package android.housalil;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bean.Visite;
import controle.OperationClient;

@Path("client")
public class RSDataClient {

	@POST
	@Path("consulterVisiteClient")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Visite> consulterVisite(JsonArray jsonArray) {
		JsonObject jsonObject = jsonArray.getJsonObject(1);

		String client = jsonObject.getString("client");

		OperationClient operationClient = new OperationClient();
		System.out.println("hello");
		ArrayList<Visite> visites = operationClient.consulterVisite(client);

		return visites;

	}

	@POST
	@Path("prendreVisite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Visite> prendreVisite(JsonArray jsonArray) {
		
		JsonObject jsonObject = jsonArray.getJsonObject(1);
		System.out.println(jsonObject.getString("idLogement"));
		String idLogement = jsonObject.getString("idLogement");
		String username_client = jsonObject.getString("username_client");
		
		Date date=null;
		if(jsonObject.getString("date").equals("vide"))
			date=null;
		else
			date = Date.valueOf(jsonObject.getString("date"));
		
		
		OperationClient operationClient = new OperationClient();

		
		System.out.println(date);
		ArrayList<Visite> visites = operationClient.prendreVisite(idLogement, username_client, date);
		
		for(Visite visite : visites) {
			System.out.println(new java.sql.Date
					(visite.getVisite_date()) + "  " + new Time(visite.getVisite_heure()));
		}


		return visites;
	}
	
	@POST
	@Path("ajouterRDV")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String,String> ajouterRDV(JsonObject jsonObject) {
		OperationClient operationClient = new OperationClient();

		//int id=Integer.parseInt(jsonObject.getString("id") );
		String username_client=jsonObject.getString("username_client") ;
		String username_agent=jsonObject.getString("username_agent") ;
		String logement=jsonObject.getString("logement");
		
		String date=jsonObject.getString("visite_date");
		String heure=jsonObject.getString("visite_heure");
		
		//long visite_date=Long.parseLong(jsonObject.getString("visite_date") );
		//long visite_heure=Long.parseLong(jsonObject.getString("visite_heure") );
		String etat=jsonObject.getString("etat") ;
		int preavis=Integer.parseInt(jsonObject.getString("preavis") );
		
		System.out.println("etat android = "+etat+"\n****************************");
		
		
		System.out.println(date +" "+heure);
		
		long visite_date=Date.valueOf(date).getTime(); 
		long visite_heure=Time.valueOf(heure).getTime();
				
		Visite visite = new Visite(username_client,username_agent,logement,visite_date,visite_heure,preavis,etat);
		
		String ajout =operationClient.ajouterRDV(visite);
		
		
		System.out.println(ajout+ " "+ visite.getEtat());
		
		HashMap<String ,String > params = new HashMap<String,String>();
		params.put("ajout", ajout);
	
		return params ;
	}
	
	@POST
	@Path("annulerVisite")
	@Produces(MediaType.TEXT_HTML)
	public String annulerVisite(@FormParam("id")String request) {
		
		OperationClient operationClient =new OperationClient();
		
		String response=operationClient.annulerVisite(Integer.parseInt(request));
		
		System.out.println(response);
		
		return response;
		
	}
	
}
