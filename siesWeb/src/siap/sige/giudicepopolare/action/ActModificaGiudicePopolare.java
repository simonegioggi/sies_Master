package siap.sige.giudicepopolare.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActModificaGiudicePopolare</p>
* <p>Description: Classe Action per la modifica del GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
public class ActModificaGiudicePopolare extends ActionSiap 
implements ICostantiGiudicePopolare
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   *   Azione di Modifica del GiudicePopolare.
   * <p>
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * <p>
   * @throws Exception
   */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    // Istanzia e riempie il model.
    GiudicePopolareModel lGiuPopMod = new GiudicePopolareModel();

    lGiuPopMod.setIdGiudicePopolare(getRequestBigDecimalParameter(CAMPO_ID_GIUDICE_POPOLARE));
    lGiuPopMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
    lGiuPopMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
    lGiuPopMod.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE).toUpperCase());
    
    lGiuPopMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
    
    lGiuPopMod.setDataNascita(
        getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA,
                                CAMPO_MESE_DATA_NASCITA,
                                CAMPO_GIORNO_DATA_NASCITA));
    
    lGiuPopMod.setCodStatoNascita( getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
    
    ComuneModel lComuneMod;
    if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_NASCITA) &&
         getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA).length() > 0)
    {
      // se presente dal codice comune (e descrizione)
      lComuneMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA));
      lGiuPopMod.setCodComuneNascita(lComuneMod.getCodComune());
    }  
    
    if (!isRequestParameterNullObj(CAMPO_COMUNE_ESTERO_NASCITA))
      lGiuPopMod.setComuneEsteroNascita( getRequestStringParameter(CAMPO_COMUNE_ESTERO_NASCITA));
    
    lGiuPopMod.setCodSesso(getRequestStringParameter(CAMPO_COD_SESSO));
    
    lGiuPopMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));
    lGiuPopMod.setCodRuolo(getRequestStringParameter(CAMPO_COD_RUOLO));
    
    lGiuPopMod.setDataInizioValidita(
        getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA,
                                CAMPO_MESE_DATA_INIZIO_VALIDITA,
                                CAMPO_GIORNO_DATA_INIZIO_VALIDITA));
    lGiuPopMod.setDataFineValidita(
        getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
                                CAMPO_MESE_DATA_FINE_VALIDITA,
                                CAMPO_GIORNO_DATA_FINE_VALIDITA));

    // Campo id sezione. 
    if(!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE))  
      lGiuPopMod.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
 
    lGiuPopMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lGiuPopMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lGiuPopMod.setDataAggiornamento(DateUtils.getSysDate());

    // Chiama il controller.
    IGiudicePopolare lCtrl = SIGELookupRemote.getGiudicePopolareRemote();
    GiudicePopolareModel lGiuPopModRet = lCtrl.ExModificaGiudicePopolare(lGiuPopMod);

    RedirectTo lRedir = new RedirectTo();
    lRedir.setPage(IWebConstants.PG_MAIN);
    lRedir.setAction("siap.sige.giudicepopolare.action.ActLoadDettaglioGiudicePopolare");
    lRedir.setParameter(CAMPO_ID_GIUDICE_POPOLARE, lGiuPopModRet.getIdGiudicePopolare().toString());
    
    String lPage = lRedir.toString();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");

    return lPage;
  }
}