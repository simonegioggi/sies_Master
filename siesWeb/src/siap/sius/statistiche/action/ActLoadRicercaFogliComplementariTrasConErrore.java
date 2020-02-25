package siap.sius.statistiche.action;


/**
 * <p>Title: ActLoadRicercaPerFoglioComplementare</p>
 * <p>Description: Visualizza la form di "Ricerca Fogli Complementari Trasmessi con Errore".
  * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;

public class ActLoadRicercaFogliComplementariTrasConErrore extends ActionSiap implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	setRequestAttribute("modalitaRicerca", RICERCA_FC_TRASMESSI_CON_ERRORE); 
	
    return PG_LOAD_RICERCA_FC_TRASMESSI;
  }
}
