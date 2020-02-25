package siap.siep.verbale.action;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciNotificaCarcere</p>
* <p>Description: Classe Action per l'inserimento di Notifica Carcere</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciNotificaCarcere extends ActionSiap implements ICostantiVerbale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Azione di Inserimento del Verbale di Notica del Carcere
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

 	  VerbaleModel lVerMod = new VerbaleModel();

    lVerMod.setCodTipoVerbale("06"); // Notifica Carcere
    lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
    lVerMod.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_SCADENZA, ICostantiVerbale.CAMPO_MESE_DATA_SCADENZA, ICostantiVerbale.CAMPO_GIORNO_DATA_SCADENZA));
    
    lVerMod.setCodTipoUfficioFirmatario("-");
    lVerMod.setCodLuogoUfficioFirmatario("-");

    lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    
    //lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));
    
    lVerMod.setDataInserimento(DateUtils.getSysDate());

    //REWORK dettaglio - Aggiunto per ricavare l'istituto di detenzione dal verbale piuttosto che dal fascicolo siep
    lVerMod.setIstDetIdIstitutoDetenzione(this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
  
/****************************************Pena Residua***************** */
    // PenaResiduaModel lPenModFin = new PenaResiduaModel();
    String lGiorno = DateUtils.getDayToString   (lVerMod.getDataEmissione());
    String lMese   = DateUtils.getMonthToString (lVerMod.getDataEmissione());
    String lAnno   = DateUtils.getYearToString  (lVerMod.getDataEmissione());
    String vedoDataIntermedia = "N";

   // Date lDataFinePena       = null;
   // Date lDataInizioArresto  = null;
   // Date lDataFineReclusione = null;

    
    Date lDataFineAltraCausa = DateUtils.getDate(lAnno, lMese, lGiorno);
    
    // aggiungo un giorno alla data di fine altra pena  per settare
    // correttamente la data inizio della pena per questa causa
    Date lDataInizio = DateUtils.moveDateTo(lDataFineAltraCausa, GregorianCalendar.DATE, 1); 	
   
    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    lPenMod = IPenRes.ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(lFascMod.getIdFascicoloSiep());

    PenaResiduaModel lPenaModel = new PenaResiduaModel();

    //========================================================================
    // Recupero i quantum di pena Validati che concorrono alla calcolo della
    // pena 
    //========================================================================
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
    ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
    CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(), null);

    //==========================================================================
    // NO ERGASTOLO: calcolo le date in funzione del quantum a sistema e della
    // data di arresto (data inizio)
    //==========================================================================
    if( lPenMod.getFlagErgastolo() == null || lPenMod.getFlagErgastolo().equals("N") )
    {

      lPenaModel = lCalcoloPenaModel.getPenaDaEspiare(lDataInizio,null,"all");
      lPenaModel.setDataFinePresunta(lPenaModel.getDataFine());
      if(lPenaModel != null && (lPenaModel.getDataFineReclusione() != null ||
                                lPenaModel.getDataInizioArresto() != null))
      {
       vedoDataIntermedia = "S";
      }

    }
    else // inizio ergastolo
    {
      lPenaModel = new PenaResiduaModel(lPenMod);
      Date lDataFinePenaErga = DateUtils.getDate(9999, 12, 31);
      lPenaModel.setDataFine(lDataFinePenaErga);
    } // fine ergastolo

    lPenaModel.setDataInizio(lDataInizio);
    lPenaModel.setFlagValidato(lPenMod.getFlagValidato());
    lPenaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
    lPenaModel.setFlagErgastolo(lPenMod.getFlagErgastolo());
    lPenaModel.setDiesAQuo("S");

    if (lPenaModel.getFlagValidato().equals("S"))
    {
      lPenaModel.setCodUfficioInserimento   (this.getCodUfficioUtenteConnesso());
      lPenaModel.setCodOperatoreInserimento (this.getCodUtenteConnesso());
      lPenaModel.setDataInserimento         (DateUtils.getSysDate());
    }
    else if (lPenaModel.getFlagValidato().equals("N"))
    {
      lPenaModel.setCodUfficioAggiornamento   (this.getCodUfficioUtenteConnesso());
      lPenaModel.setCodOperatoreAggiornamento (this.getCodUtenteConnesso());
      lPenaModel.setDataAggiornamento         (DateUtils.getSysDate());
    }

  
    IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
    VerbaleModel llVerModRet = lCtrl.ExInserisciNotificaCarcere( lFascMod.getIdFascicoloSiep(),
                                                                 lVerMod,
                                                                 lPenaModel);

    PenaResiduaModel lPenModFin = new PenaResiduaModel(lPenaModel);

    setRequestAttribute("penaresidua", lPenModFin);
    setRequestAttribute("verbale", llVerModRet);
    setRequestAttribute("vedoDataIntermedia", vedoDataIntermedia);

    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.verbale.action.ActLoadDettaglioNotificaCarcere&"+CAMPO_ID_VERBALE+"="+llVerModRet.getIdVerbale().toString();

    return lPage;
	}
}