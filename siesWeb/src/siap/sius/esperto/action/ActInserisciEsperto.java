package siap.sius.esperto.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActInserisciEsperto</p>
* <p>Description: Classe Action per l'inserimento di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciEsperto extends ActionSiap implements ICostantiEsperto
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Effettua Inserimento del Esperto.
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
    
    EspertoModel lEspMod = new EspertoModel();

    lEspMod.setCognome(getRequestStringParameter( CAMPO_COGNOME).toUpperCase() );
    lEspMod.setNome(getRequestStringParameter( CAMPO_NOME).toUpperCase() );
    lEspMod.setCodiceFiscale(getRequestStringParameter( CAMPO_CODICE_FISCALE).toUpperCase());
    lEspMod.setIndirizzo(getRequestStringParameter( CAMPO_INDIRIZZO) );
    lEspMod.setTelefono(getRequestStringParameter( CAMPO_TELEFONO) );
    lEspMod.setCodUfficioAppartenenza( getCodUfficioUtenteConnesso());
    lEspMod.setEmail(getRequestStringParameter( CAMPO_EMAIL) );
    lEspMod.setFax(getRequestStringParameter( CAMPO_FAX) );
    lEspMod.setCellulare(getRequestStringParameter( CAMPO_CELLULARE) );
    lEspMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lEspMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lEspMod.setDataInserimento(DateUtils.getSysDate());
    lEspMod.setFlagStato( getRequestStringParameter( CAMPO_FLAG_STATO) );
    lEspMod.setDataInizioValidita( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_VALIDITA,
                                                            CAMPO_MESE_DATA_INIZIO_VALIDITA,
                                                            CAMPO_GIORNO_DATA_INIZIO_VALIDITA ));
    lEspMod.setDataFineValidita( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,
                                                          CAMPO_MESE_DATA_FINE_VALIDITA,
                                                          CAMPO_GIORNO_DATA_FINE_VALIDITA ));

    // Chiama il controller.
    IEsperto lCtrl = SIUSLookupRemote.getEspertoRemote();
    EspertoModel lEspModRet = lCtrl.ExInserisciEsperto(lEspMod);		 // setta la risposta nella request

    setRequestAttribute("esperto", lEspModRet);
    
    RedirectTo lRedir = new RedirectTo();
    lRedir.setPage( IWebConstants.PG_MAIN );
    lRedir.setAction( "siap.sius.esperto.action.ActLoadDettaglioEsperto" );
    lRedir.setParameter(CAMPO_ID_ESPERTO, lEspModRet.getIdEsperto().toString() );
    
    String lPage = lRedir.toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return lPage;
  }
}