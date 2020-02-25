package siap.sige.giudicepopolare.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaGiudicePopolareLista</p>
* <p>Description: Classe Action per la load ricerca del GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActLoadRicercaGiudicePopolareLista extends ActionSiap 
implements ICostantiGiudicePopolare
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");

    //Controllo di accesso alla funzionalità
    if( !getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAS") && 
        !getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP") )
      throw new F3BException( F3BException.USER_MESSAGE, "Funzione non disponibile per il tipo ufficio di competenza.");

    
    String lPage = PG_LOAD_RICERCA_GIUDICE_POPOLARE_LISTA;
    
    if( !isRequestParameterNullObj("isFormFilter") )
    {
      // Elenco delle sezioni.
      Option lOption = 
        new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
      lOption.setValueBlankItem("Tutte");
      setRequestAttribute("elencoSezioni", "" + lOption );
      
      lPage = PG_FILTRA_GIUPOP_ASSEGNAZIONE_LISTA; 
    }  
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    return lPage;  //restituisce la jsp di VIEW
  }
}