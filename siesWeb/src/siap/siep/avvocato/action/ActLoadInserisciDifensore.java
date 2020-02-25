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

public class ActLoadInserisciDifensore extends ActionSiap implements ICostantiAvvocato
{
  public String processRequest() throws Exception
  {
    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
  	//IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
  	//Vector lVect = lCtrl.ExRicercaForiDisponibili();
  	//this.setRequestAttribute("foro", lVect);
  		
  	UfficioModel lUffUte = this.getUfficioUtenteConnesso();
  	String lDescrComune = lUffUte.getDescrComune();
  	this.setRequestAttribute("comune", lDescrComune);
  
    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
    Option lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
    setRequestAttribute("foro", ""+ lOption);
  	
  	lOption = new Option( DecodificheManager.getInstance().getListaAttivitaAvvocato());
    setRequestAttribute("autoritaAvvocato", "" + lOption );
    setRequestAttribute("modalita", "I");

	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di Inserimento Difensore.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
    String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);

    return PG_INSERISCI_DIFENSORE;  //restituisce la jsp di VIEW
  }

}