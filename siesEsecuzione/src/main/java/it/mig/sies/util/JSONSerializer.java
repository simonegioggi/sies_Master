package it.mig.sies.util;

import it.mig.sies.exception.CommunicationException;
import it.mig.sies.exception.LoadException;
import it.mig.sies.exception.ResultException;
import it.mig.sies.model.ResponseData;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * SIES FASE 2 - Classe di utility per la serializzazione 
 * della risposta in formato JSON
 * 
 * @author Federico Paparoni
 * */

public class JSONSerializer {

    public static JSONObject decode(ResponseData responseData)
            throws JSONException {
        JSONObject object = new JSONObject();
        object.put("esito", responseData.getEsito());
        object.put("id", responseData.getId());
        object.put("result", responseData.isCompleted());
        
        if ((responseData.getEstratto()==null)||(responseData.getEstratto().length==0))
        	object.put("estratto","0");
        else
        	object.put("estratto","1");
        return object;
    }

    public static JSONObject createErrorResponse(Exception e) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("esito", e.getMessage());
        object.put("id", 0);
        object.put("result", false);
        return object;
    }
    
    public static JSONObject createErrorResponse(LoadException e) throws JSONException {
        JSONObject object = new JSONObject();
        ApplicationProperties properties=ApplicationProperties.getIstance();
        String esito=properties.getProperty("messaggio.errore.caricamento");
        
        object.put("esito", esito+": "+e.toString());
        object.put("id", 0);
        object.put("result", false);
        return object;
    }
    
    public static JSONObject createErrorResponse(CommunicationException e) throws JSONException {
        JSONObject object = new JSONObject();
        ApplicationProperties properties=ApplicationProperties.getIstance();
        String esito=properties.getProperty("messaggio.errore.comunicazione");
        object.put("esito", esito+": "+e.toString());
        object.put("id", 0);
        object.put("result", false);
        return object;
    }
    
    public static JSONObject createErrorResponse(ResultException e) throws JSONException {
        JSONObject object = new JSONObject();
        ApplicationProperties properties=ApplicationProperties.getIstance();
        String esito=properties.getProperty("messaggio.errore.elaborazione");
        object.put("esito",  esito+": "+e.toString());
        object.put("id", 0);
        object.put("result", false);
        return object;
    }
    
}