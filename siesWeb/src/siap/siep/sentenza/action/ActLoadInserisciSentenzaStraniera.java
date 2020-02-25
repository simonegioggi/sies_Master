package siap.siep.sentenza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciSentenza</p>
 * <p>Description: Classe Action che effettua la load dell'inserisci Sentenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciSentenzaStraniera extends ActionSiap implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {
	/* inizio modifica marzo 2010 */
//    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(), "-");
	Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
	/* fine modifica marzo 2010*/
    
    // Imposta i autorita Rif.
    setRequestAttribute("autoritaEmi", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "I");

    return PG_LOAD_INSERISCISENTENZASTRANIERA; //restituisce la jsp di VIEW
  }
}
