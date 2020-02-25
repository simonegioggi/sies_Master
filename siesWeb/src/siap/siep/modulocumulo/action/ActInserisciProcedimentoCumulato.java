package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciProcedimentoCumulato extends ActionModuloCumulo implements ICostantiProcedimentoCumulato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException 
  {
    
    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
    
    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella (non previsto)

    ITitoloCumulato lCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
    
    ProcedimentoCumulatoModel lProcedimentoCumulatoMod = null; 
    
    BigDecimal lIdTitoloCumulato = null;
    
    if ("I".equals(lModalita)) {
      lProcedimentoCumulatoMod = getDatiForm();

      lProcedimentoCumulatoMod.setFlagStato("I");
      lProcedimentoCumulatoMod.setTitIdTitoloCumulato(getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

      lProcedimentoCumulatoMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
      lProcedimentoCumulatoMod.setDataInserimento             ( DateUtils.getSysDate() );
      lProcedimentoCumulatoMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );      
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lProcedimentoCumulatoMod = "+lProcedimentoCumulatoMod);
      lProcedimentoCumulatoMod = lCtrl.ExInserisciProcedimentoCumulato(lProcedimentoCumulatoMod);      
    }
    else if ("M".equals(lModalita)) {
      lProcedimentoCumulatoMod = getDatiForm();

      if (   lProcedimentoCumulatoMod.getFlagStato().equals("E")
          || lProcedimentoCumulatoMod.getFlagStato().equals("M")
         ) 
      { // Dati origiari. 
        lProcedimentoCumulatoMod.setFlagStato("M");
        // nel caso di dati inseriti manualmente "I" non marco il record come modificato
      }
      
      lProcedimentoCumulatoMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      lProcedimentoCumulatoMod.setDataAggiornamento           ( DateUtils.getSysDate() );
      lProcedimentoCumulatoMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lProcedimentoCumulatoMod = "+lProcedimentoCumulatoMod);
      lCtrl.ExModificaProcedimentoCumulato(lProcedimentoCumulatoMod);      
    }        
    else if ("C".equals(lModalita)) {
      BigDecimal lIdProCum = getRequestBigDecimalParameter ( CAMPO_ID_PROCEDIMENTO_CUMULATO);

      lProcedimentoCumulatoMod = lCtrl.ExRicercaProcedimentoCumulatoById(lIdProCum);
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lProcedimentoCumulatoMod = "+lProcedimentoCumulatoMod);
      lIdTitoloCumulato = lProcedimentoCumulatoMod.getTitIdTitoloCumulato();
      
      if (   lProcedimentoCumulatoMod.getFlagStato().equals("E")
          || lProcedimentoCumulatoMod.getFlagStato().equals("M")
         ) 
      { // Dati origiari. Procedo alla cancellazione logica
        
        lProcedimentoCumulatoMod.setFlagStato("C");
        
        lProcedimentoCumulatoMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lProcedimentoCumulatoMod.setDataAggiornamento           ( DateUtils.getSysDate() );
        lProcedimentoCumulatoMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
        
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("lProcedimentoCumulatoMod = "+lProcedimentoCumulatoMod);
        lCtrl.ExModificaProcedimentoCumulato(lProcedimentoCumulatoMod);      
      }
      else {
        // Cancellazione fisica
        lCtrl.ExCancellaProcedimentoCumulato(lProcedimentoCumulatoMod);  
      }
    }
    
    String lPage="";

    if ("I".equals(lModalita) || "M".equals(lModalita)){
      // Vado sul dettaglio 
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioProcedimentoCumulato";
      lPage += "&" + CAMPO_ID_PROCEDIMENTO_CUMULATO + "=" + lProcedimentoCumulatoMod.getIdProcedimentoCumulato();
    }
    else { // Cancellazione vado sul dettaglio del Titolo (o sulla lista titoli?)
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioTitoloCumulato";
      lPage += "&" + ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" + lIdTitoloCumulato;      
    }
    return lPage;
  }
  
  /**
   * 
   * @return
   * @throws F3BException
   */
  private ProcedimentoCumulatoModel getDatiForm() throws F3BException
  {
    ProcedimentoCumulatoModel lProMod = new ProcedimentoCumulatoModel();

    lProMod.setIdProcedimentoCumulato     ( getRequestBigDecimalParameter ( CAMPO_ID_PROCEDIMENTO_CUMULATO) );
//    lProMod.setIdFascicoloSiepOrigine     ( getRequestBigDecimalParameter ( CAMPO_ID_FASCICOLO_SIEP_ORIGINE) );
    
    lProMod.setChiaveAnnoFasCumulato      ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_ANNO_FAS_CUMULATO) );
    lProMod.setChiaveProgrFasCumulato     ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_PROGR_FAS_CUMULATO) );    

    lProMod.setCodTipoUfficioFasCumulato  ( getRequestStringParameter     ( CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO) );
    
    String lDescLuogoUfficio = getRequestStringParameter ( CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO);
    ComuneModel lComuneSedeUfficio = getCodComuneByDescr(lDescLuogoUfficio);
    lProMod.setCodLuogoUfficioFasCumulato (lComuneSedeUfficio.getCodComune() );
    String lDocUfficio = getCodUfficioByCodTipoUfficioDescrComune(lProMod.getCodTipoUfficioFasCumulato(), lDescLuogoUfficio);
    lProMod.setCodUfficioFasCumulato      ( lDocUfficio );

    
    if (   !isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)
        && !"".equals(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)) 
        && !"0".equals(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)) 
       )
    { // Trattasi di Fascicolo Appartenente ad ufficio accorpato quindi con numerazione
      // alterata
      lProMod.setFlagAccorpato("S");
      lProMod.setChiaveProgrOrigine (lProMod.getChiaveProgrFasCumulato());

      BigDecimal lIncrementoAccorpato = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO);
      BigDecimal lChiaveProgrNew = null;
      lChiaveProgrNew = lProMod.getChiaveProgrFasCumulato().add (lIncrementoAccorpato);
      
      lProMod.setChiaveProgrFasCumulato (lChiaveProgrNew);

      UfficioModel lUfficioOrigine = getUfficioAccorpatoByCodAccorpanteIncrement (lProMod.getCodUfficioFasCumulato(), ""+lIncrementoAccorpato);
      
      lProMod.setChiaveUfficioOrigine (lUfficioOrigine.getCodUfficio());
    }
    else if (!isRequestParameterNullObj (CAMPO_CHIAVE_PROGR_ORIGINE)){
      // Sono in modifica di un procedimento Accorpato e Importato
      // I dati sono in Hidden
      lProMod.setFlagAccorpato("S");
      lProMod.setChiaveProgrOrigine   (getRequestBigDecimalParameter (CAMPO_CHIAVE_PROGR_ORIGINE));
      lProMod.setChiaveUfficioOrigine (getRequestStringParameter (CAMPO_CHIAVE_UFFICIO_ORIGINE));
    }
    
    

    lProMod.setDataRichiestaFascicolo     ( getRequestDateParameter       ( CAMPO_ANNO_DATA_RICHIESTA_FASCICOLO,CAMPO_MESE_DATA_RICHIESTA_FASCICOLO,CAMPO_GIORNO_DATA_RICHIESTA_FASCICOLO) );
    lProMod.setDataPervenimentoFascicolo  ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PERVENIMENTO_FASCICOLO,CAMPO_MESE_DATA_PERVENIMENTO_FASCICOLO,CAMPO_GIORNO_DATA_PERVENIMENTO_FASCICOLO) );
    
    lProMod.setNote                       ( getRequestStringParameter     ( CAMPO_NOTE) );
    //lProMod.setTitIdTitoloCumulato        ( getRequestBigDecimalParameter ( CAMPO_TIT_ID_TITOLO_CUMULATO) );
    lProMod.setFlagStato                  ( getRequestStringParameter     ( CAMPO_FLAG_STATO) );
    lProMod.setMotivoModifica             ( getRequestStringParameter     ( ICostantiProcedimentoCumulato.CAMPO_MOTIVO_MODIFICA) );
    
    return lProMod;
  }
  
}
