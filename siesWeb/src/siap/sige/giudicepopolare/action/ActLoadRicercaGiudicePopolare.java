package siap.sige.giudicepopolare.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaGiudicePopolare</p>
* <p>Description: Classe Action per la load ricerca del GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadRicercaGiudicePopolare extends ActionSiap 
implements ICostantiGiudicePopolare
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
  * Carica la form per la ricerca di un GiudicePopolare.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    //Controllo di accesso alla funzionalità.
    if( !getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAS") && 
        !getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP") )
      throw new F3BException( F3BException.USER_MESSAGE, "Funzione inibita per il tipo ufficio di competenza.");

    
    // Elenco delle sezioni.
    Option lOption = 
      new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
    lOption.setValueBlankItem("Tutte");
    setRequestAttribute("elencoSezioni", "" + lOption );

	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di Ricerca Giudice Popolare da Funzioni Amministrative.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
    String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_RICERCAGIUDICE_POPOLARE;  //restituisce la jsp di VIEW
  }
}