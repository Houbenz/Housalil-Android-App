package android.housalil;

import java.util.ArrayList;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bean.Visite;
import controle.OperationAgent;

@Path("agent")
public class RSDataAgent {

	
	
	
	
	@POST
	@Path("consulterVisiteAgent")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Visite> consulterVisiteAgent(JsonArray jsonArray){
		
		JsonObject jsonObject = jsonArray.getJsonObject(1);
		
		String username_agent = jsonObject.getString("username_agent");
		
		OperationAgent operationAgent =new OperationAgent();
		
		ArrayList<Visite> visites = new ArrayList<Visite>();
		visites=operationAgent.consulterVisite(username_agent);
		for(Visite vi: visites) {
			System.out.println(vi.getUsername_client());
		}
		return visites;
	}
	
	@POST
	@Path("validerVisite")
	@Produces(MediaType.TEXT_HTML)
	public String validerVisite(
			@FormParam("idVisite")String id,
			@FormParam("preavis")String pr,
			@FormParam("etatVisite")String etatVisite) {
		
		OperationAgent operationAgent =new OperationAgent();
		System.out.println(id +" "+pr + " "+etatVisite);
		int idVisite = Integer.parseInt(id);
		int preavisVisite;
		
		if(pr.equals("null"))
			preavisVisite=0;
			else
		preavisVisite=Integer.parseInt(pr);
		
		System.out.println("i have been executed validervisite");
		
		String response = operationAgent.validerVisite(idVisite, preavisVisite, etatVisite);
		
		return response;
	}
	
}
