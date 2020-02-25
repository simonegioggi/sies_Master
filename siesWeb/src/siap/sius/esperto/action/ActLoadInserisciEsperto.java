package siap.sius.esperto.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciEsperto</p>
* <p>Description: Classe Action per la load inserisci di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciEsperto extends ActionSiap 
implements ICostantiEsperto
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
  * Carica la form per l'inserimento del Esperto.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @throws Exception propaga errore di eccezione.
  */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    // 20081222 - Inibizione funzione per tipo uffici in ambito SIGE
    if( getUfficioUtenteConnesso().getCodTipoUfficio().equals("GIP") ||
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAS") || 
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("DIB") )
      throw new F3BException( F3BException.USER_MESSAGE, 
                              "Funzione inibita per il tipo ufficio di competenza.");

    // Inserire Eventuali ComboBOX
    Option lOption = new Option( DecodificheManager.getInstance().getFlagStato());
    setRequestAttribute("elencoFlagStato", "" + lOption );

    // Imposta Modalità inserimento.
    setRequestAttribute("modalita", "I");
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");

	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di Inserimento di un Esperto da Funzioni Amministrative.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
    String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);
    
    return PG_LOAD_INSERISCIESPERTO;  //restituisce la jsp di VIEW
  }
}