package siap.siep.modulocumulo.action;


/**
* <p>Title: ActInserisciTitoloCumulato</p>
* <p>Description: Classe Action per l'inserimento di TitoloCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Action per l'inserimento e la modifica del Titolo cumulato
 * @author d.fiorletta
 *
 */
public class ActInserisciTitoloCumulato extends ActionModuloCumulo implements ICostantiTitoloCumulato 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException {
    super.getDatiIstruttoria();
    
    TitoloCumulatoModel lTitoloCumulatoMod = null;

    String lModalita = "";
    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella (non previsto)
    
    ITitoloCumulato lCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();

    lTitoloCumulatoMod = getDatiForm();
    
    if ("I".equals(lModalita)) {
      lTitoloCumulatoMod.setFlagStato("I");
      lTitoloCumulatoMod.setFlagEscluso("N");
      
      lTitoloCumulatoMod.setTipoIscrizione("02");	// 02 = Titolo Iscritto Manualmente
      
      lTitoloCumulatoMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
      lTitoloCumulatoMod.setDataInserimento             ( DateUtils.getSysDate() );
      lTitoloCumulatoMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );      
      
      lTitoloCumulatoMod = lCtrl.ExInserisciTitoloCumulato(lTitoloCumulatoMod);      
    }
    else if ("M".equals(lModalita)) {
      lTitoloCumulatoMod.setMotivoModifica("Modificato");
      
      lTitoloCumulatoMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      lTitoloCumulatoMod.setDataAggiornamento           ( DateUtils.getSysDate() );
      lTitoloCumulatoMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      
      lCtrl.ExModificaTitoloCumulato(lTitoloCumulatoMod);      
    }
        
    
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioTitoloCumulato";
    lPage += "&" + CAMPO_ID_TITOLO_CUMULATO + "=" + lTitoloCumulatoMod.getIdTitoloCumulato();
  
    return lPage;
  }
  
  
  /**
   * Recupera i dati dalla form e restituisce il model caricato.
   * n.b. la form ha sezioni a sconparsa che NON vengono ripulite dei dati quando
   * vengono nascoste. Tali dati tuttavia non vanno caricati
   * @return
   */
  private TitoloCumulatoModel getDatiForm() throws F3BException
  {
    TitoloCumulatoModel lTitoloCumulatoMod = new TitoloCumulatoModel();
    
    //========================================================================== 
    // Recupero i dati presenti in maschera 
    // n.b. eliminare o commentare i campi non presenti in maschera 
    //      es: chiave della tabella, date_ins, foreignkey... 
    //========================================================================== 
    lTitoloCumulatoMod.setIdTitoloCumulato            ( getRequestBigDecimalParameter ( CAMPO_ID_TITOLO_CUMULATO) );
    
    lTitoloCumulatoMod.setCodTipoProvvedimento        ( getRequestStringParameter     ( CAMPO_COD_TIPO_PROVVEDIMENTO) );
    lTitoloCumulatoMod.setDataIrrevocabilita          ( getRequestDateParameter       ( CAMPO_ANNO_DATA_IRREVOCABILITA,CAMPO_MESE_DATA_IRREVOCABILITA,CAMPO_GIORNO_DATA_IRREVOCABILITA) );
    
    if ("01".equals(lTitoloCumulatoMod.getCodTipoProvvedimento())){
      completaDatiSentenza (lTitoloCumulatoMod);
    }
    else if ("02bis".equals(lTitoloCumulatoMod.getCodTipoProvvedimento())){
      lTitoloCumulatoMod.setCodTipoProvvedimento("02");
      completaDatiDecretoPenale (lTitoloCumulatoMod);
    }
    else if ("05".equals(lTitoloCumulatoMod.getCodTipoProvvedimento())){
      completaDatiSentenzaStraniera (lTitoloCumulatoMod);
    }
    else if ("02".equals(lTitoloCumulatoMod.getCodTipoProvvedimento()) || "03".equals(lTitoloCumulatoMod.getCodTipoProvvedimento())){
      completaDatiProvvSorv (lTitoloCumulatoMod);
    }
    
    
    //
    lTitoloCumulatoMod.setIstrIdIstruttoriaCumulo     ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
    lTitoloCumulatoMod.setFlagStato                   ( getRequestStringParameter     ( CAMPO_FLAG_STATO) );
    lTitoloCumulatoMod.setMotivoModifica              ( getRequestStringParameter     ( ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA) );
    
   
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lTitoloCumulatoMod = "+lTitoloCumulatoMod);
    
    return lTitoloCumulatoMod;
  }
  
  /**
   * Metodo che recupera i dati della Sentenza
   * @param aTitoloModel
   * @throws F3BException
   */
  private void completaDatiSentenza (TitoloCumulatoModel aTitoloModel) throws F3BException
  {
    aTitoloModel.setAnnoRegePm                  ( getRequestBigDecimalParameter ( CAMPO_ANNO_REGE_PM) );
    aTitoloModel.setNumeroRegePm                ( getRequestStringParameter     ( CAMPO_NUMERO_REGE_PM) );
    
    String lSedeNDR = getRequestStringParameter     ( CAMPO_COD_SEDE_NOTIZIA_REATO);
    ComuneModel lComuneSedeNDR = getCodComuneByDescr(lSedeNDR);    
    aTitoloModel.setCodSedeNotiziaReato         ( lComuneSedeNDR.getCodComune() );
    
    aTitoloModel.setAnnoRegGen                  ( getRequestBigDecimalParameter ( CAMPO_ANNO_REG_GEN) );
    aTitoloModel.setNumeroRegGen                ( getRequestStringParameter     ( CAMPO_NUMERO_REG_GEN) );
    aTitoloModel.setTipoRegGen                  ( getRequestStringParameter     ( CAMPO_TIPO_REG_GEN) );
    
    aTitoloModel.setDataProvvedimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PROVVEDIMENTO,CAMPO_MESE_DATA_PROVVEDIMENTO,CAMPO_GIORNO_DATA_PROVVEDIMENTO) );
    aTitoloModel.setAnnoSentenza                ( getRequestBigDecimalParameter ( CAMPO_ANNO_SENTENZA) );
    aTitoloModel.setNumeroSentenza              ( getRequestStringParameter     ( CAMPO_NUMERO_SENTENZA) );
    aTitoloModel.setCodTipoAutoritaEmittente    ( getRequestStringParameter     ( CAMPO_COD_TIPO_AUTORITA_EMITTENTE) );
    
    
    String lSedeLuogoEmittente = getRequestStringParameter ( CAMPO_COD_LUOGO_EMITTENTE);
    ComuneModel lComuneLuogoEmittente= getCodComuneByDescr(lSedeLuogoEmittente);    
    aTitoloModel.setCodLuogoEmittente           ( lComuneLuogoEmittente.getCodComune() );
    aTitoloModel.setNumSezioneAutoritaEmittente ( getRequestStringParameter     ( CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE) );
    
    if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO))
      aTitoloModel.setCodTipoRito  ( getRequestStringParameter     ( CAMPO_COD_TIPO_RITO) );
   
    // Altro grado di giudizio
    aTitoloModel.setCodTipoProvvedimentoRif     ( getRequestStringParameter     ( CAMPO_COD_TIPO_PROVVEDIMENTO_RIF) );
    if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVV_RIF))
      aTitoloModel.setCodTipoProvvRif             ( getRequestStringParameter     ( CAMPO_COD_TIPO_PROVV_RIF) );
    
    aTitoloModel.setDataProvvRif                ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PROVV_RIF,CAMPO_MESE_DATA_PROVV_RIF,CAMPO_GIORNO_DATA_PROVV_RIF) );
    aTitoloModel.setAnnoProvvRif                ( getRequestBigDecimalParameter ( CAMPO_ANNO_PROVV_RIF) );
    aTitoloModel.setNumeroProvvRif              ( getRequestStringParameter     ( CAMPO_NUMERO_PROVV_RIF) );
    aTitoloModel.setCodTipoAutoritaProvvRif     ( getRequestStringParameter     ( CAMPO_COD_TIPO_AUTORITA_PROVV_RIF) );
    
    String lSedeLuogoEmittenteRif = getRequestStringParameter ( CAMPO_COD_LUOGO_PROVV_RIF);
    ComuneModel lComuneLuogoEmittenteRif = getCodComuneByDescr(lSedeLuogoEmittenteRif);    
    aTitoloModel.setCodLuogoProvvRif            ( lComuneLuogoEmittenteRif.getCodComune() );
    aTitoloModel.setNumSezioneAutoritaProvvRif  ( getRequestStringParameter     ( CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF) );
    
    if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO_RIF))
      aTitoloModel.setCodTipoRitoRif              ( getRequestStringParameter     ( CAMPO_COD_TIPO_RITO_RIF) );
    
    // Decisione Cassazione solo se Rif<>53 Ordinanza animmissibilità
    if (!"53".equals(aTitoloModel.getCodTipoProvvedimentoRif())) 
    {
      aTitoloModel.setCodTipoProvvedimentoAltro   ( getRequestStringParameter     ( CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO) );
      aTitoloModel.setAnnoSentenzaCassazione      ( getRequestBigDecimalParameter ( CAMPO_ANNO_SENTENZA_CASSAZIONE) );
      aTitoloModel.setNumeroSentenzaCassazione    ( getRequestStringParameter     ( CAMPO_NUMERO_SENTENZA_CASSAZIONE) );
  
      // Anno numero Reg. Gen
      if (!"53".equals(aTitoloModel.getCodTipoProvvedimentoAltro())) 
      {
        aTitoloModel.setNote1DecisioneCassazione    ( getRequestStringParameter     ( CAMPO_NOTE1_DECISIONE_CASSAZIONE) );
        aTitoloModel.setNote2DecisioneCassazione    ( getRequestStringParameter     ( CAMPO_NOTE2_DECISIONE_CASSAZIONE) );
        
        aTitoloModel.setAnnoRaccoltaGenerale        ( getRequestBigDecimalParameter ( CAMPO_ANNO_RACCOLTA_GENERALE) );
        aTitoloModel.setNumeroRaccoltaGenerale      ( getRequestStringParameter     ( CAMPO_NUMERO_RACCOLTA_GENERALE) );
        
        aTitoloModel.setCodTipoDecisioneCassazione  ( getRequestStringParameter     ( CAMPO_COD_TIPO_DECISIONE_CASSAZIONE) );
      }
    }
    
    aTitoloModel.setNote                        ( getRequestStringParameter     ( CAMPO_NOTE) );
  }
  
  /**
   * Metodo che recupera i dati del Decreto Penale
   * @param aTitoloModel
   * @throws F3BException
   */  
  private void completaDatiDecretoPenale (TitoloCumulatoModel aTitoloModel) throws F3BException
  {
    aTitoloModel.setAnnoRegePm                  ( getRequestBigDecimalParameter ( CAMPO_ANNO_REGE_PM+"_DECPEN") );
    aTitoloModel.setNumeroRegePm                ( getRequestStringParameter     ( CAMPO_NUMERO_REGE_PM+"_DECPEN") );
    
    String lSedeNDR = getRequestStringParameter     ( CAMPO_COD_SEDE_NOTIZIA_REATO+"_DECPEN");
    ComuneModel lComuneSedeNDR = getCodComuneByDescr(lSedeNDR);    
    aTitoloModel.setCodSedeNotiziaReato         ( lComuneSedeNDR.getCodComune() );
    
    aTitoloModel.setAnnoRegGen                  ( getRequestBigDecimalParameter ( CAMPO_ANNO_REG_GEN+"_DECPEN") );
    aTitoloModel.setNumeroRegGen                ( getRequestStringParameter     ( CAMPO_NUMERO_REG_GEN+"_DECPEN") );
    aTitoloModel.setTipoRegGen                  ( getRequestStringParameter     ( CAMPO_TIPO_REG_GEN+"_DECPEN") );
    
    aTitoloModel.setDataProvvedimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PROVVEDIMENTO+"_DECPEN"
                                                                                 ,CAMPO_MESE_DATA_PROVVEDIMENTO+"_DECPEN"
                                                                                 ,CAMPO_GIORNO_DATA_PROVVEDIMENTO+"_DECPEN") );
    aTitoloModel.setAnnoSentenza                ( getRequestBigDecimalParameter ( CAMPO_ANNO_SENTENZA+"_DECPEN") );
    aTitoloModel.setNumeroSentenza              ( getRequestStringParameter     ( CAMPO_NUMERO_SENTENZA+"_DECPEN") );
    
    aTitoloModel.setCodTipoAutoritaEmittente    ( getRequestStringParameter     ( CAMPO_COD_TIPO_AUTORITA_EMITTENTE+"_DECPEN") );
    
    String lSedeLuogoEmittente = getRequestStringParameter ( CAMPO_COD_LUOGO_EMITTENTE+"_DECPEN");
    ComuneModel lComuneLuogoEmittente= getCodComuneByDescr(lSedeLuogoEmittente);    
    aTitoloModel.setCodLuogoEmittente           ( lComuneLuogoEmittente.getCodComune() );
    aTitoloModel.setNumSezioneAutoritaEmittente ( getRequestStringParameter     ( CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE+"_DECPEN") );
    

    aTitoloModel.setAnnoSentenzaCassazione      ( getRequestBigDecimalParameter ( CAMPO_ANNO_SENTENZA_CASSAZIONE+"_DECPEN") );
    aTitoloModel.setNumeroSentenzaCassazione    ( getRequestStringParameter     ( CAMPO_NUMERO_SENTENZA_CASSAZIONE+"_DECPEN") );

    aTitoloModel.setAnnoRaccoltaGenerale        ( getRequestBigDecimalParameter ( CAMPO_ANNO_RACCOLTA_GENERALE+"_DECPEN" ));
    aTitoloModel.setNumeroRaccoltaGenerale      ( getRequestStringParameter     ( CAMPO_NUMERO_RACCOLTA_GENERALE+"_DECPEN") );

    aTitoloModel.setCodTipoDecisioneCassazione  ( getRequestStringParameter     ( CAMPO_COD_TIPO_DECISIONE_CASSAZIONE+"_DECPEN") );
    
    aTitoloModel.setNote                        ( getRequestStringParameter     ( CAMPO_NOTE+"_DECPEN") );
  }
  
  
  /**
   * Metodo che recupera i dati della Sentenza Straniera
   * @param aTitoloModel
   * @throws F3BException
   */
  private void completaDatiSentenzaStraniera (TitoloCumulatoModel aTitoloModel) throws F3BException
  {
    
    aTitoloModel.setDataProvvedimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PROVVEDIMENTO+"_SENT_STRA"
                                                                                 ,CAMPO_MESE_DATA_PROVVEDIMENTO+"_SENT_STRA"
                                                                                 ,CAMPO_GIORNO_DATA_PROVVEDIMENTO+"_SENT_STRA") );
    aTitoloModel.setAnnoSentenza                ( getRequestBigDecimalParameter ( CAMPO_ANNO_SENTENZA+"_SENT_STRA") );
    aTitoloModel.setNumeroSentenza              ( getRequestStringParameter     ( CAMPO_NUMERO_SENTENZA+"_SENT_STRA") );
    aTitoloModel.setCodTipoAutoritaEmittente    ( getRequestStringParameter     ( CAMPO_COD_TIPO_AUTORITA_EMITTENTE+"_SENT_STRA") );
    
    
    String lSedeLuogoEmittente = getRequestStringParameter ( CAMPO_COD_LUOGO_EMITTENTE+"_SENT_STRA");
    ComuneModel lComuneLuogoEmittente= getCodComuneByDescr(lSedeLuogoEmittente);    
    aTitoloModel.setCodLuogoEmittente ( lComuneLuogoEmittente.getCodComune() );
    
    aTitoloModel.setNumSezioneAutoritaEmittente ( getRequestStringParameter     ( CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE+"_SENT_STRA") );

    aTitoloModel.setNote ( getRequestStringParameter     ( CAMPO_NOTE+"_SENT_STRA") );
  }
  
  /**
   * Metodo che recupera i dati dei Provvedimenti della Sorveglianza Decreto/Ordinanza
   * @param aTitoloModel
   * @throws F3BException
   */
  private void completaDatiProvvSorv (TitoloCumulatoModel aTitoloModel) throws F3BException
  {
    
    aTitoloModel.setDataProvvedimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PROVVEDIMENTO+"_SORV"
                                                                                 ,CAMPO_MESE_DATA_PROVVEDIMENTO+"_SORV"
                                                                                 ,CAMPO_GIORNO_DATA_PROVVEDIMENTO+"_SORV") );
    aTitoloModel.setAnnoSentenza                ( getRequestBigDecimalParameter ( CAMPO_ANNO_SENTENZA+"_SORV") );
    aTitoloModel.setNumeroSentenza              ( getRequestStringParameter     ( CAMPO_NUMERO_SENTENZA+"_SORV") );
    aTitoloModel.setCodTipoAutoritaEmittente    ( getRequestStringParameter     ( CAMPO_COD_TIPO_AUTORITA_EMITTENTE+"_SORV") );
    
    
    String lSedeLuogoEmittente = getRequestStringParameter ( CAMPO_COD_LUOGO_EMITTENTE+"_SORV");
    ComuneModel lComuneLuogoEmittente= getCodComuneByDescr(lSedeLuogoEmittente);    
    aTitoloModel.setCodLuogoEmittente ( lComuneLuogoEmittente.getCodComune() );
  }
  
}