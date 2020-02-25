package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un 
 * provedimento di computo presofferto disposta su uno dei titoli cumulati
 * @author
 *
 */
public class ActInserisciPresoffertoCumulo extends ActionModuloCumulo 
      implements ICostantiStatoEsecTitoloCumulato,  ICostantiComputiCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
    //==========================================================================
    // 
    //
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
    
    
 
    
    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella, NP=nuovo Periodo 

    IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    StatoEsecTitoloCumulatoModel lStatoEsecMod = null;
    
    if ("I".equals(lModalita)) {
      siesLogger.debug("Sono in INSERIMENTO");
      
      lStatoEsecMod = this.getDatiProvvedimento("I");
      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      ComputiCumuloModel lComputiModel = this.getDatiComputo("I");
      siesLogger.debug("lComputiModel = "+lComputiModel);
      
      // In caso di inserimento va caricato in StatoEsecuzione l'ufficio emittente
      if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null){
        lStatoEsecMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
        lStatoEsecMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
      }
      //lCtrlStatoEsec.ExInserisciPresofferto(lStatoEsecMod, lComputiModel);
      lCtrlStatoEsec.ExInserisciStatoEsecComputoCumulo(lStatoEsecMod, lComputiModel);
    }
    else if ("M".equals(lModalita)) {
      siesLogger.debug("Sono in MODIFICA");

      lStatoEsecMod = this.getDatiProvvedimento("M");
      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      ComputiCumuloModel lComputiModel = this.getDatiComputo("M");
      siesLogger.debug("lComputiModel = "+lComputiModel);
      
//      if (lStatoEsecMod.getFlagStato().equals("I"))
//        lStatoEsecMod.setFlagStato("I"); // Resta I
//      else 
//        lStatoEsecMod.setFlagStato("M"); // E' un dato estratto
      
      lComputiModel.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());
      
      //lCtrlStatoEsec.ExModificaPresofferto(lStatoEsecMod, lComputiModel);
      lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputiModel);
    }
    else if ("NP".equals(lModalita)) {
      siesLogger.debug("Sono in Aggiunta Nuovo Periodo");

      lStatoEsecMod = this.getDatiProvvedimento("M");
      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      ComputiCumuloModel lComputiModel = this.getDatiComputo("I");
      //siesLogger.debug("lComputiModel = "+lComputiModel);
      lComputiModel.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());
      
      //lCtrlStatoEsec.ExModificaPresofferto(lStatoEsecMod, lComputiModel);
      lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputiModel);
    }
    else if ("C".equals(lModalita)) {
      siesLogger.debug("Sono in CANCELLAZIONE");      
      
      BigDecimal lIdStatoEsecuzione = getRequestBigDecimalParameter ( CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      BigDecimal lIdComputo = getRequestBigDecimalParameter ( CAMPO_ID_COMPUTI_CUMULO);
      
      // Devo verificare se cancellare un solo computo o l'intero provvedimento
      IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();      
      StatoEsecTitoloCumulatoModel lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull (lIdStatoEsecuzione);  
      
      if (lStato.getListaComputi().size()==1){
        // Un solo computro, cancello tutto 
        siesLogger.debug("Elimino l'intero provvedimento di computo: "+lIdStatoEsecuzione);
        lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById (lIdStatoEsecuzione, null);
      }
      else {
        // Ho più computi cancello solo quello indicato sulla request
        siesLogger.debug("Elimino il solo conmputo:"+lIdComputo);
        IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
        lCtrlComputi.ExCancellaComputiCumuloBykey(lIdComputo);
      }
    } 

    String lPage = "";
    if ("I".equals(lModalita) || "M".equals(lModalita) || "NP".equals(lModalita) ){
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioPresoffertoCumulo" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
          + "&"+ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "=" +lStatoEsecMod.getIdStatoEsecTitoloCumulato();
    }
    else {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaPresoffertoCumulo" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    }
    
    
    
    return lPage;
   }  
  
  /**
   * 
   * @throws F3BException
   */
  private StatoEsecTitoloCumulatoModel getDatiProvvedimento(String aTipoOper) throws F3BException
  {
    StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
    
    lStaMod.setIdStatoEsecTitoloCumulato ( getRequestBigDecimalParameter ( CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO) );
    lStaMod.setCodTipoEvento             ("01");
    lStaMod.setCodTipoProvvedimento      ("04");
    lStaMod.setCodMotivo                 ( getRequestStringParameter     ( CAMPO_COD_MOTIVO) );
    
    if ("M".equals(aTipoOper)) {
      lStaMod.setCodUfficioEmittente       ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_EMITTENTE) );
      lStaMod.setCodLuogoEmittente         ( getRequestStringParameter     ( CAMPO_COD_LUOGO_EMITTENTE) );
    }
    
    lStaMod.setDataEmissione             ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE,CAMPO_MESE_DATA_EMISSIONE,CAMPO_GIORNO_DATA_EMISSIONE) );
    lStaMod.setCodEsito                  ("-");
    lStaMod.setCodEsitoTenore            ("-");
    lStaMod.setAnnoProcedimento          (null);
    lStaMod.setProgrProcedimento         (null);
    lStaMod.setAnnoProvvedimento         (null);
    lStaMod.setProgrProvvedimento        (null);
    
    if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE))
      lStaMod.setNote  ( getRequestStringParameter     ( ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE) );
    
    lStaMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
    lStaMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
    lStaMod.setFlagStato                 ( "I" );
    lStaMod.setMotivoModifica            ( null );
    
    if (   !isRequestParameterNullObj(getRequestStringParameter ( CAMPO_ID_EVENTO_ORIGINE))
        && !"".equals(getRequestStringParameter ( CAMPO_ID_EVENTO_ORIGINE))
       )
    {
      lStaMod.setIdEventoOrigine ( getRequestBigDecimalParameter ( CAMPO_ID_EVENTO_ORIGINE) );
    }

    if (   !isRequestParameterNullObj(getRequestStringParameter ( CAMPO_EVE_ID_EVENTO_ORIGINE))
        && !"".equals(getRequestStringParameter ( CAMPO_EVE_ID_EVENTO_ORIGINE))
       )
    {
      lStaMod.setEveIdEventoOrigine ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO_ORIGINE) );
    }
    
    if ("I".equals(aTipoOper)) {
      lStaMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
      lStaMod.setDataInserimento             ( DateUtils.getSysDate() );
      lStaMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
    } 
    else if ("M".equals(aTipoOper)) {
      // La Modifica cambia solo lo stato di "Estratto / Modificato".
      String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO); 
      if ("E".equals(flagStato)  ||
    	  "M".equals(flagStato) )
    	  lStaMod.setFlagStato               ( "M" );
      lStaMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      lStaMod.setDataAggiornamento           ( DateUtils.getSysDate() );
      lStaMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
    }    
    
    return lStaMod;
  }
  
  /**
   * Recupera i dati del computo cumulo
   * @return
   * @throws F3BException
   */
  private ComputiCumuloModel getDatiComputo(String aTipoOper) throws F3BException
  {
    ComputiCumuloModel lComputiModel = new ComputiCumuloModel();
    
    lComputiModel.setIdComputiCumulo           (getRequestBigDecimalParameter ( CAMPO_ID_COMPUTI_CUMULO) );
    lComputiModel.setCodTipoAnnotazione        ("005" ); //005-Pena Espiata per lo Stesso Titolo
    lComputiModel.setCodCausaleComputo         ("-"); // Fisso a trittino (res migrava 08)
    

    lComputiModel.setFlagPiuMeno               ("-"); // Sempre meno
    
    lComputiModel.setDataReclusioneDa          ( getRequestDateParameter ( CAMPO_ANNO_DATA_RECLUSIONE_DA,CAMPO_MESE_DATA_RECLUSIONE_DA,CAMPO_GIORNO_DATA_RECLUSIONE_DA) );
    lComputiModel.setDataReclusioneA           ( getRequestDateParameter ( CAMPO_ANNO_DATA_RECLUSIONE_A,CAMPO_MESE_DATA_RECLUSIONE_A,CAMPO_GIORNO_DATA_RECLUSIONE_A) );
    
    // Calcolo i Quantum
    CalendarModel lCalMod = new CalendarModel();
    lCalMod.setDataInizio (lComputiModel.getDataReclusioneDa());
    lCalMod.setDataFine   (lComputiModel.getDataReclusioneA());

    CalendarUtil lCalUtil = new CalendarUtil();
    lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni (lCalMod);
    lCalMod = lCalUtil.ricalcolaGAM (lCalMod);

    lComputiModel.setNumAnniReclusione   (new BigDecimal(lCalMod.getNumAnni()));
    lComputiModel.setNumMesiReclusione   (new BigDecimal(lCalMod.getNumMesi()));
    lComputiModel.setNumGiorniReclusione (new BigDecimal(lCalMod.getNumGiorni()));    
    
    siesLogger.debug("Quantum Calcolati = "+lCalMod.getStringPerStampa()); 
    
    if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_RECLUSIONE)){
      siesLogger.debug("quantum in form...");
      lComputiModel.setNumAnniReclusione         ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_RECLUSIONE) );
      lComputiModel.setNumMesiReclusione         ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_RECLUSIONE) );
      lComputiModel.setNumGiorniReclusione       ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_RECLUSIONE) );
    }

    if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_MAP)){ // MESSA ALLA PROVA
      lComputiModel.setNumGiorniMap ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_MAP) );
    }
    
    if (!isRequestParameterNullObj(CAMPO_COD_TIPO_MISURA)){
      lComputiModel.setCodTipoMisura( getRequestStringParameter ( CAMPO_COD_TIPO_MISURA) );
    }
    
    if (!isRequestParameterNullObj(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)){
      lComputiModel.setIstDetIdIstitutoDetenzione ( getRequestStringParameter ( CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) );
    }

    if (!isRequestParameterNullObj(CAMPO_ALTRO_LUOGO_DETENZIONE)){
      lComputiModel.setAltroLuogoDetenzione ( getRequestStringParameter ( CAMPO_ALTRO_LUOGO_DETENZIONE) );
    }    
    
    lComputiModel.setFlagStato("I");
    lComputiModel.setMotivoModifica(null);
    
    //lComputiModel.setFlagStato      ( getRequestStringParameter ( ICostantiComputiCumulo.CAMPO_FLAG_STATO) );
    //lComputiModel.setMotivoModifica ( getRequestStringParameter ( ICostantiComputiCumulo.CAMPO_MOTIVO_MODIFICA) );
    
    lComputiModel.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
    lComputiModel.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
    
    
    if ("I".equals(aTipoOper)) {
      lComputiModel.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
      lComputiModel.setDataInserimento             ( DateUtils.getSysDate() );
      lComputiModel.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
    } 
    else if ("M".equals(aTipoOper)) {
      lComputiModel.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      lComputiModel.setDataAggiornamento           ( DateUtils.getSysDate() );
      lComputiModel.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
    }      
    
    return lComputiModel;
  }
}
