package siap.siep.archiviazione.action;

import java.util.Date;

import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * Action di Archiviazione semplificata RES
 * @author 
 *
 */
public class ActInserisciArchSemplificataRES extends ActionSiap implements ICostantiArchiviazione
{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascModCorrente = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    //
    Date lDataArchiviazione = getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
                                                      CAMPO_MESE_DATA_DEFINIZIONE,
                                                      CAMPO_GIORNO_DATA_DEFINIZIONE);
    
    String lNotaProcedimento = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_NOTE);
    
    // Creo una copia del fascicolo in quanto non posso utilizzare la classe in 
    // sessione altrimenti modifico
    FascicoloSiepModel lFascMod = new FascicoloSiepModel();
    lFascMod.setIdFascicoloSiep(lFascModCorrente.getIdFascicoloSiep());
    lFascMod.setDataArchiviazione(lDataArchiviazione);
    lFascMod.setNote(lNotaProcedimento);
    
    lFascMod.setDataIscrizione(lFascModCorrente.getDataIscrizione());
    
    lFascMod.setCodOperatoreAggiornamento  (this.getCodUtenteConnesso());
    lFascMod.setCodUfficioAggiornamento    (this.getCodUfficioUtenteConnesso());
    lFascMod.setDataAggiornamento          (DateUtils.getSysDate());
    
    
    IArchiviazione lArchCtrl = SIEPLookupRemote.getArchiviazioneRemote();
    lArchCtrl.ExArchiviazioneSemplificataRES(lFascMod);
    
    // Ritorno il controllo al Dettaglio
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                        "Archiviazione Effettuata Correttamente.");
    lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +
                        ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

    return IWebConstants.PG_MESSAGE;
  }
}
