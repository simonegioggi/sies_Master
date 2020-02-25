package siap.sige.curatore.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActModificaCuratore</p>
* <p>Description: Classe Action per la modifica del Curatore</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActModificaCuratore extends ActionSiap implements ICostantiCuratore
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
* Azione di Modifica del Curatore.
* <p>
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* <p>
* @throws Exception
*/
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    // istanzia e riempie il model
    CuratoreModel lCurMod = new CuratoreModel();

    lCurMod.setIdCuratore(getRequestBigDecimalParameter(CAMPO_ID_CURATORE));
    lCurMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
    lCurMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
    lCurMod.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE).toUpperCase());
    lCurMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));
    lCurMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
    lCurMod.setEmail(getRequestStringParameter(CAMPO_EMAIL));
    lCurMod.setFax(getRequestStringParameter(CAMPO_FAX));
    lCurMod.setCellulare(getRequestStringParameter(CAMPO_CELLULARE));
    lCurMod.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));
    
    lCurMod.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA,
                                                          CAMPO_MESE_DATA_INIZIO_VALIDITA,
                                                          CAMPO_GIORNO_DATA_INIZIO_VALIDITA));
    lCurMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
                                                        CAMPO_MESE_DATA_FINE_VALIDITA,
                                                        CAMPO_GIORNO_DATA_FINE_VALIDITA));

    lCurMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lCurMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lCurMod.setDataAggiornamento(DateUtils.getSysDate());

    // chiama il controller
    ICuratore lCtrl = SIGELookupRemote.getCuratoreRemote();
    CuratoreModel lCurModRet = lCtrl.ExModificaCuratore(lCurMod);

    RedirectTo lRedir = new RedirectTo();
    lRedir.setPage( IWebConstants.PG_MAIN );
    lRedir.setAction( "siap.sige.curatore.action.ActLoadDettaglioCuratore" );
    lRedir.setParameter(CAMPO_ID_CURATORE, lCurModRet.getIdCuratore().toString());
    
    String lPage = lRedir.toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");

    return lPage;
  }
}