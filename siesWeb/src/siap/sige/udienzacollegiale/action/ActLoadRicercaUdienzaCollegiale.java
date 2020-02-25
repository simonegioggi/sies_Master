package siap.sige.udienzacollegiale.action;


/**
* <p>Title: ActLoadRicercaUdienzaCollegiale</p>
* <p>Description: Classe Action per la load ricerca di UdienzaCollegialeSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadRicercaUdienzaCollegiale extends ActionSige 
implements ICostantiUdienzaCollegiale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
 /*****************************************************************************
  * Azione di caricamento della pagina di ricerca. Si occupa di precaricare
  * tutti i dati da visualizzare i tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    //=============================================================
    // Aggiungere qui eventuali caricamento di combo o altri dati 
    // da passare alla finestra di ricerca, e relative setRequest 
    //=============================================================
    // es: 
    //Option lOption = new Option(codice per caricare la option);
    //setRequestAttribute("nome_attributo", "" + lOption);

    //restituisce la jsp di visualizzazione della pagina di ricerca
    
    
    // Crea Lista Elenco Magistrati per ruolo di Presidente/Giudice/Consigliere.
    IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
    Option lOption = 
      new Option( lMagCtrl.ExElencoCbxMagistratiByCodUfficio( getCodUfficioUtenteConnesso() ));
    lOption.setAddBlankItem(Option.BLANK_ITEM);
    lOption.setValueBlankItem("-");
    setRequestAttribute( "elencoMagistrati", "" + lOption );
    
     // intervento per 11.2.1 (aggiiungo al lista delle sezioni)   
     lOption = 
    	      new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
    		lOption.setValueBlankItem("-");
    	    setRequestAttribute("elencoSezioni", "" + lOption );

    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_RICERCAUDIENZACOLLEGIALE;
  }
}