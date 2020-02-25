package siap.sige.udienzaparti.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciDifensore</p>
* <p>Description: Classe Action per la load inserisci Difensore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActLoadInserisciDifensore extends ActionSiap implements ICostantiPartiUdienza
{
  public String processRequest() throws Exception
  {
  		
	String idSoggetto = "";
    if (!this.isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO))
    {
	  	idSoggetto =  this.getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
	}
    setRequestAttribute("idSoggetto", "" + idSoggetto);
    
	UfficioModel lUffUte = this.getUfficioUtenteConnesso();
  	String lDescrComune = lUffUte.getDescrComune();
  	this.setRequestAttribute("comune", lDescrComune);
  
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