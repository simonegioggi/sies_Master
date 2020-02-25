package siap.sius.avvocato.action;


/**
* <p>Title: ActLoadRicercaAvvocato</p>
* <p>Description: Classe Action per la load ricerca di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadRicercaAvvocato extends ActionSiap implements ICostantiAvvocato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
public String processRequest() throws Exception
  {
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.debug( "PAOLO LoadRicerca Lancio Ricerca" );
    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
  	//IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
  	//Vector lVect = lCtrl.ExRicercaForo();
  	//this.setRequestAttribute("foro", lVect);
  	
  	UfficioModel lUffUte = this.getUfficioUtenteConnesso();
  	String lDescrComune = lUffUte.getDescrComune();
  	this.setRequestAttribute("comune", lDescrComune);
    String lPage =  PG_LOAD_RICERCAAVVOCATO;
  
    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
    Option lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
    setRequestAttribute("foro", ""+ lOption);
  
    if (! isRequestParameterNullObj("modalita"))
    {
    	if (getRequestStringParameter("modalita").compareTo("BREVE")==0)
    		lPage = PG_LOAD_RICERCAAVVOCATOBREVE;
  	}
    return lPage;  //restituisce la jsp di VIEW
  }
}