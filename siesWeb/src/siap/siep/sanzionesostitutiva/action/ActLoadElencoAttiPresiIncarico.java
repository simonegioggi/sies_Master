package siap.siep.sanzionesostitutiva.action;

import siap.sico.web.ActionSiap;
import siap.sius.presaincarico.action.ICostantiPresaincarico;
import siap.web.ISIAPCostantiWeb;


/**
* <p>Title: ActLoadElencoAttiPresiIncarico</p>
* <p>Description: Classe Action per la load ricerca Ordinanze Ricevute</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActLoadElencoAttiPresiIncarico extends ActionSiap implements ICostantiPresaincarico
{
  public String processRequest() throws Exception
  {
    
	  return ISIAPCostantiWeb.PG_UNDER_CONSTRUCTION;
	  
	  /*
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSIUS());
    setRequestAttribute("tipoUfficioSIUS", "" + lOption );

    
    setRequestAttribute("function_name", "Ricerca Atti Presi in Carico");
    setRequestAttribute("action_name", "siap.sius.presaincarico.action.ActRicercaProvvedimentiRicevuti");
    setRequestAttribute("tipo_provvedimento", ICostantiJMS.TRASFERIMENTO_SANZIONE_SOSTITUTIVA);
    
    return PG_LOAD_RICERCA_PROVV_SIUS;
    */
  }
}