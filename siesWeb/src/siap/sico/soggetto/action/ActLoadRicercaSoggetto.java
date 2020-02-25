package siap.sico.soggetto.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaSoggetto</p>
 * <p>Description: Carica la pagina della Ricerca del Soggetto</p>
 * <p>Copyright: Bull Italia Copyright (c) 2002</p>
 * <p>Company: Bull Italia</p>
 */

public class ActLoadRicercaSoggetto extends ActionSiap implements ICostantiSoggetto
{
  /**
   * Azione di caricamento della form di ricerca del Soggetto.
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * <p>
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {

     Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "-");
     setRequestAttribute("nazioni", "" + lOption );

	 // MEV 15 - Revisione SIGE
	 // Aggiunto parametro per identificare la funzione che richiama la maschera
	 // di Ricerca Soggetto da Iscrizione Manuale.
	 // Quando viene richiamata da SIGE sulla maschera viene inserito
     // il Calendario in corrispondenza di ogni campo data
	 String codFunzione = getCodFunMenuVerticale();
	 setRequestAttribute("codFunzione", codFunzione);
	 
     return PG_LOAD_RICERCASOGGETTO; //restituisce la jsp di VIEW
  }
}
