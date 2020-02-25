package siap.siep.verbale.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciVerbaleArresto</p>
* <p>Description: Classe Action per l'inserimento di Verbale di Arresto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciVerbaleArresto extends ActionSiap implements ICostantiVerbale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Azione di Inserimento del Verbale di Arresto
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    //============================================
    // Verbale di Arresto MODEL
    //============================================
 		VerbaleModel lVerMod = new VerbaleModel();

    lVerMod.setCodTipoVerbale("01");
    lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
    lVerMod.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));
    lVerMod.setCodTipoUfficioFirmatario(this.getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));

    ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)));
    lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());

    lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));
    lVerMod.setDataInserimento(DateUtils.getSysDate());

    //REWORK dettaglio - Aggiunto per ricavare l'istituto di detenzione dal verbale piuttosto che dal fascicolo siep
    lVerMod.setIstDetIdIstitutoDetenzione(this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

    //============================================
    // Model Posizione Giuridica
    //============================================
    PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
    BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

    lPosMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lPosMod.setCodPosizioneGiuridica("03"); // Espiazione Pena in Regime Carcerario
    lPosMod.setDataInizio(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));
    lPosMod.setCodPosizioneProcessuale("-");

    lPosMod.setCodOperatoreInserimento (getCodUtenteConnesso());
    lPosMod.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lPosMod.setDataInserimento         (DateUtils.getSysDate());

    //============================================
    // Model Luogo Detenzione
    //============================================
    LuogoDetenzioneModel lLuoDetMod = new LuogoDetenzioneModel();

    lLuoDetMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lLuoDetMod.setDataInserimento(DateUtils.getSysDate());
    lLuoDetMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lLuoDetMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    // modifica relativa al tipo istituto
    lLuoDetMod.setIstDetIdIstitutoDetenzione(this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
    // lLuoDetMod.setCodTipoIstituto(
    // this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_COD_TIPO_ISTITUTO));

    // ComuneModel lCom =
    // getCodComuneByDescr(this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_COD_LUOGO));
    // lLuoDetMod.setCodLuogo(lCom.getCodComune());
    lLuoDetMod.setDataInizioDetenzione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));

    //============================================
    // Model Evento
    //============================================
    NotificaModel lNotMod = new NotificaModel();
    lNotMod.setCodEsito("01");  // Eseguita
    lNotMod.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
    lNotMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lNotMod.setDataAggiornamento(DateUtils.getSysDate());

    // Scadenzario Vane Ricerche
    ScadenzarioModel lScaMod = new ScadenzarioModel();
    lScaMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lScaMod.setCodTipoScadenzario("03"); // Imposto fisso 03=Vane Ricerche

/****************************************Pena Residua***************** */
    //==========================================================================
    // Ricalcolo la Pena Residua in base ai dati a sistema (quantum) e alla
    // data di inizio pena = data di arresto
    //==========================================================================
    // PenaResiduaModel lPenModFin = new PenaResiduaModel();
    String lGiorno = DateUtils.getDayToString   (lVerMod.getDataEmissione());
    String lMese   = DateUtils.getMonthToString (lVerMod.getDataEmissione());
    String lAnno   = DateUtils.getYearToString  (lVerMod.getDataEmissione());
    String vedoDataIntermedia = "N";


    Date lDataInizio = DateUtils.getDate(lAnno, lMese, lGiorno);

    //==========================================================================
    // Recupero l'ultima pena a sistema (validata o meno). Se non validata
    // verrà aggiornata
    //==========================================================================
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
/* commentate a seguito del rework calcolo pena. I caloli vengon effettuati del 
 * calcolo pena model      
      Date lDataFinePena       = null;
      Date lDataInizioArresto  = null;
      Date lDataFineReclusione = null;
      
      ICalcoloPena ICalPen = SIEPLookupRemote.getCalcoloPenaRemote();
      Vector lVectFine = ICalPen.exCalcolaDataFinePena(lDataInizio, lPenMod, true); //il flag true indica CON DIES_A_QUO

      if (lVectFine.size() == 0)
      {
        throw new F3BException(F3BException.USER_MESSAGE, "Impossibile calcolare la data di Fine Pena");
      }

      if (lVectFine.size() == 1)
      {
        lDataFinePena = (Date) lVectFine.get(0);
        lDataInizioArresto = lDataInizio;
      }

      if (lVectFine.size() == 2)
      {
        lDataFinePena       = (Date) lVectFine.get(1);
        lDataFineReclusione = (Date) lVectFine.get(0);
        lDataInizioArresto  = DateUtils.moveDateTo(lDataFineReclusione, Calendar.DAY_OF_MONTH, 1);
        lPenMod.setDataFineReclusione(lDataFineReclusione);
        vedoDataIntermedia = "S";
      }
*/
      
      lPenaModel = lCalcoloPenaModel.getPenaDaEspiare(lDataInizio,null,"all");
      lPenaModel.setDataFinePresunta(lPenaModel.getDataFine());
      if(lPenaModel != null && (lPenaModel.getDataFineReclusione() != null ||
                                lPenaModel.getDataInizioArresto() != null))
      {
        vedoDataIntermedia = "S";  //
      }

    }
    else // inizio ergastolo
    {
      lPenaModel = new PenaResiduaModel(lPenMod);
      Date lDataFinePenaErga = DateUtils.getDate(9999, 12, 31);
      lPenaModel.setDataFine(lDataFinePenaErga);
    } // fine ergastolo

    
    lPenaModel.setDataInizio(lDataInizio);
    lPenaModel.setFlagValidato(lPenMod.getFlagValidato()); //??
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
      lPenaModel.setIdPenaResidua(lPenMod.getIdPenaResidua()); // copio anche l'id perchè devo andare in update
      lPenaModel.setCodUfficioAggiornamento   (this.getCodUfficioUtenteConnesso());
      lPenaModel.setCodOperatoreAggiornamento (this.getCodUtenteConnesso());
      lPenaModel.setDataAggiornamento         (DateUtils.getSysDate());
    }

    // lPenModFin = IPenRes.ExInserisciAggiornaPenaResidua(lPenMod);
    // lPenModFin =
    // IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    // NEL CONTROLLER GESTISCE LO STATO PROCEDIMENTO
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
		VerbaleModel llVerModRet = lCtrl.ExInserisciVerbale( lFascMod.getIdFascicoloSiep(),
                                                         lVerMod,
                                                         lPosMod,
                                                         lLuoDetMod,
                                                         lNotMod,
                                                         lScaMod,
                                                         lPenaModel);

     PenaResiduaModel lPenModFin = new PenaResiduaModel(lPenaModel);

     setRequestAttribute("penaresidua", lPenModFin);
     setRequestAttribute("verbale", llVerModRet);
     setRequestAttribute("vedoDataIntermedia", vedoDataIntermedia);
		  //Prepara la pagina di destinazione
		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.verbale.action.ActLoadDettaglioVerbaleArresto&"+CAMPO_ID_VERBALE+"="+llVerModRet.getIdVerbale().toString();

     return lPage;
	 }
}