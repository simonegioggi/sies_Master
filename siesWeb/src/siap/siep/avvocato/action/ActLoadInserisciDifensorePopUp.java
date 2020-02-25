package siap.siep.avvocato.action;

/**
* <p>Title: ActLoadInserisciAvvocato</p>
* <p>Description: Classe Action per la load inserisci di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadInserisciDifensorePopUp extends ActionSiap implements ICostantiAvvocato
{
  public String processRequest() throws Exception
  {
     		
  	UfficioModel lUffUte = this.getUfficioUtenteConnesso();
  	String lDescrComune = lUffUte.getDescrComune();
  	this.setRequestAttribute("comune", lDescrComune);
  
    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
    Option lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
    setRequestAttribute("foro", ""+ lOption);
    
    setRequestAttribute("formname",  getRequestStringParameter("formname"));
    setRequestAttribute("filtro",  getRequestStringParameter("filtro"));
    
  	lOption = new Option( DecodificheManager.getInstance().getListaAttivitaAvvocato());
    setRequestAttribute("autoritaAvvocato", "" + lOption );
    return PG_INSERISCI_DIFENSORE_POPUP;  //restituisce la jsp di VIEW
  }

}