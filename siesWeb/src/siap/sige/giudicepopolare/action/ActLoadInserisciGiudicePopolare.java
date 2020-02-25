package siap.sige.giudicepopolare.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciGiudicePopolare</p>
 * <p>Description: Classe Action per la load inserisci di 
 *                 GiudicePopolare</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia S.p.A.</p>
 * @version 1.0
 */
public class ActLoadInserisciGiudicePopolare extends ActionSiap 
implements ICostantiGiudicePopolare
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Carica la form per l'inserimento del GiudicePopolare.
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
    
    //Controllo di accesso alla funzionalità
    
    if( !getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAS") && 
        !getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP") )
      throw new F3BException( F3BException.USER_MESSAGE, "Funzione inibita per il tipo ufficio di competenza.");
    
    // Elenco dei Ruoli.
    Option lOption = 
      new Option( DecodificheManager.getInstance().getRuoloGiudicePopolare());
    setRequestAttribute("elencoRuoli", "" + lOption );

    // Elenco delle Nazioni.
    lOption = 
      new Option(DecodificheManager.getInstance().getNazioni());
    lOption.setSelected("039"); // Default si posiziona su Italia.
    setRequestAttribute("elencoNazioni", "" + lOption );
    
    // Elenco delle Sezioni.
    lOption = 
      new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
    setRequestAttribute("elencoSezioni", "" + lOption );

    // Elenco dei Sessi.
    lOption = 
      new Option(DecodificheManager.getInstance().getSesso(), Option.BLANK_ITEM);    
    setRequestAttribute("elencoSessi", "" + lOption );

    // Imposta Modalità inserimento.
    setRequestAttribute("modalita", "I");
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_INSERISCIGIUDICE_POPOLARE;  //restituisce la jsp di VIEW
  }
}