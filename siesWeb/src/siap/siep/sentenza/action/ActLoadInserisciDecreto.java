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

public class ActLoadInserisciDecreto extends ActionSiap implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {

    // Imposta la decisione cassazione
    Option lOption = new Option( DecodificheManager.getInstance().getTipoDecisioneCassazione(), "-");

    setRequestAttribute("tipoDecisioneCassazione", "" + lOption );

    // 22/06/2010 Sostituzione Elenco Autorità Emittenti
 		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
    lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
    // Imposta l'autorita Rif.
    setRequestAttribute("autoritaEmi", "" + lOption );

    // MEV 15
    // In fase di iscrizione del titolo esecutivo, selezionando il tipo 
    // "Decreto Penale", tra le Autorità Emittenti devono essere presenti 
    // solo i valori contenenti:GIP,GUP e Pretura.
    lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
    String[] lFilter = {"-","GIP","GIPM","GIPMI","GIPP","GIPPSD","GUP","GUPM","GUPMI","PT","PTC"};
    lOption.setFilter(lFilter);
    setRequestAttribute("autoritaEmiDecretoPenale", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "I");

	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di Inserimento Estremi Decreto Penale da Iscrizione Manuale.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
    String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);
    
	return PG_LOAD_INSERISCIDECRETO; //restituisce la jsp di VIEW
  }
}
