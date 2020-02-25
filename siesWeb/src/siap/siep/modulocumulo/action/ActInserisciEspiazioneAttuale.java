package siap.siep.modulocumulo.action;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.controller.IPosizioneGiuridicaCumulo;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action che effettua l'inserimento o la modifica della posizione giuridica
 * puù ulterori dati 
 * 
 * @author d.fiorletta
 *
 */
public class ActInserisciEspiazioneAttuale extends ActionModuloCumulo implements ICostantiModuloCumulo, ICostantiPosizioneGiuridicaCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException
  {    
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    super.getDatiIstruttoria();

    String lAction ="=siap.siep.modulocumulo.action.ActDettaglioEspiazioneAttuale&";
    //==========================================================================
    // Vado in insert o update
    //==========================================================================
    String lModalita = getRequestStringParameter("modalita");
    PosizioneGiuridicaCumuloModel lPosizioneGiuridica = null;
    
    if (lModalita.equals(ICostantiModuloCumulo.MODALITA_INSERIMENTO)){      
      lPosizioneGiuridica = getDatiForm();
      lPosizioneGiuridica.setCodOperatoreInserimento (getCodUtenteConnesso());
      lPosizioneGiuridica.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
      lPosizioneGiuridica.setDataInserimento         (DateUtils.getSysDate());
      
      IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
      lPosizioneGiuridica=lCtrlDatiFinali.ExInserisciPosizioneGiuridicaCumulo (lPosizioneGiuridica);
      lAction+= ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM + "=" +lPosizioneGiuridica.getIdPosizioneGiuridicaCum()+"&";

    }
    else if (lModalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) {
      lPosizioneGiuridica = getDatiForm();
      lPosizioneGiuridica.setCodOperatoreAggiornamento (getCodUtenteConnesso());
      lPosizioneGiuridica.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
      lPosizioneGiuridica.setDataAggiornamento         (DateUtils.getSysDate());      

      IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
      lCtrlDatiFinali.ExModificaPosizioneGiuridicaCumulo (lPosizioneGiuridica);
      lAction+= ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM + "=" +lPosizioneGiuridica.getIdPosizioneGiuridicaCum()+"&";
      
    } else if (lModalita.equals(ICostantiModuloCumulo.MODALITA_CANCELLA)) {
        IPosizioneGiuridicaCumulo lCtrlPosGiuridicaCumulo = SIEPLookupRemote.getPosizioneGiuridicaCumuloRemote();
        lCtrlPosGiuridicaCumulo.ExCancellaPosizioneGiuridicaCumuloBykey(getRequestBigDecimalParameter ( CAMPO_ID_POSIZIONE_GIURIDICA_CUM) );
        lAction ="=siap.siep.modulocumulo.action.ActRicercaEspiato&";
    }
   
    
    //==============================
    // Invoco la Action di dettaglio
    //==============================
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + lAction 
            + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

    return lPage;
    
  }
  
  
  /**
   * Metodo che recupera i dati dalla form
   * @return
   */
  private PosizioneGiuridicaCumuloModel getDatiForm() throws F3BException 
  {
    PosizioneGiuridicaCumuloModel lPosMod = new PosizioneGiuridicaCumuloModel();
    
    lPosMod.setIdPosizioneGiuridicaCum    ( getRequestBigDecimalParameter ( CAMPO_ID_POSIZIONE_GIURIDICA_CUM) );
    lPosMod.setCodPosizioneGiuridica      ( getRequestStringParameter     ( CAMPO_COD_POSIZIONE_GIURIDICA) );
    
    String lTipoPosizione =  ( getRequestStringParameter ( CAMPO_RADIO_TIPO_POS) );
    
    if (lTipoPosizione.equals(CAMPO_CHECK_TIPO_POS_ESPIST)) {
      lPosMod.setDataInizio                 ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
      lPosMod.setIstDetIdIstitutoDetenzione ( getRequestStringParameter     ( CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) );
    
      if (   "14".equals(lPosMod.getCodPosizioneGiuridica())
          || "31".equals(lPosMod.getCodPosizioneGiuridica())
          || "32".equals(lPosMod.getCodPosizioneGiuridica())
          || "33".equals(lPosMod.getCodPosizioneGiuridica())
         )
      {
        // x il semilibero recupero anche i dati dell'ordinanza di concessione
        lPosMod.setChiaveAnnoFasSius          ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_ANNO_FAS_SIUS) );
        lPosMod.setChiaveProgrFasSius         ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_PROGR_FAS_SIUS) );
        
        String lCodTipoUff =  getRequestStringParameter ( CAMPO_TIPO_UFF_FAS_SIUS) ;
        String lDescSedeUff =  getRequestStringParameter ( CAMPO_SEDE_UFF_FAS_SIUS) ;
        
        String lCodUfficioTDS = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescSedeUff);
        
        lPosMod.setChiaveUffFasSius           (lCodUfficioTDS );
        
        lPosMod.setAnnoRegistro               ( getRequestBigDecimalParameter ( CAMPO_ANNO_REGISTRO) );
        lPosMod.setNumeroRegistro             ( getRequestBigDecimalParameter ( CAMPO_NUMERO_REGISTRO) );
        lPosMod.setCodTipoProvvedimento       ( getRequestStringParameter     ( CAMPO_COD_TIPO_PROVVEDIMENTO) );
        lPosMod.setDataEmissioneProvv         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_EMISSIONE_PROVV,CAMPO_MESE_DATA_EMISSIONE_PROVV,CAMPO_GIORNO_DATA_EMISSIONE_PROVV) );

        if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO_MISURA))
          lPosMod.setDataInizioMisura (getRequestDateParameter (CAMPO_ANNO_DATA_INIZIO_MISURA, CAMPO_MESE_DATA_INIZIO_MISURA, CAMPO_GIORNO_DATA_INIZIO_MISURA));
      }   
    }
    else if (lTipoPosizione.equals(CAMPO_CHECK_TIPO_POS_ESPALTRO)) {
      lPosMod.setDataInizio                 ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
      lPosMod.setAltroLuogo                 ( getRequestStringParameter     ( CAMPO_ALTRO_LUOGO) );

      if (lPosMod.isMisura()){
        lPosMod.setChiaveAnnoFasSius          ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_ANNO_FAS_SIUS) );
        lPosMod.setChiaveProgrFasSius         ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_PROGR_FAS_SIUS) );
        
        String lCodTipoUff =  getRequestStringParameter ( CAMPO_TIPO_UFF_FAS_SIUS) ;
        String lDescSedeUff =  getRequestStringParameter ( CAMPO_SEDE_UFF_FAS_SIUS) ;
        
        String lCodUfficioTDS = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescSedeUff);
        
        lPosMod.setChiaveUffFasSius           (lCodUfficioTDS );
        
        lPosMod.setAnnoRegistro               ( getRequestBigDecimalParameter ( CAMPO_ANNO_REGISTRO) );
        lPosMod.setNumeroRegistro             ( getRequestBigDecimalParameter ( CAMPO_NUMERO_REGISTRO) );
        lPosMod.setCodTipoProvvedimento       ( getRequestStringParameter     ( CAMPO_COD_TIPO_PROVVEDIMENTO) );
        lPosMod.setDataEmissioneProvv         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_EMISSIONE_PROVV,CAMPO_MESE_DATA_EMISSIONE_PROVV,CAMPO_GIORNO_DATA_EMISSIONE_PROVV) );
      }
      
      if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO_MISURA))
        lPosMod.setDataInizioMisura (getRequestDateParameter (CAMPO_ANNO_DATA_INIZIO_MISURA, CAMPO_MESE_DATA_INIZIO_MISURA, CAMPO_GIORNO_DATA_INIZIO_MISURA));
      
    }
    else if (lTipoPosizione.equals(CAMPO_CHECK_TIPO_POS_LIBERO)) 
    {
      if (   "16".equals(lPosMod.getCodPosizioneGiuridica())
          || "17".equals(lPosMod.getCodPosizioneGiuridica())
         )
      { // Differimento Recupero i dati del provvedimento di concessione
        lPosMod.setChiaveAnnoFasSius  ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_ANNO_FAS_SIUS) );
        lPosMod.setChiaveProgrFasSius ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_PROGR_FAS_SIUS) );
       
        String lCodTipoUff =  getRequestStringParameter ( CAMPO_TIPO_UFF_FAS_SIUS) ;
        String lDescSedeUff =  getRequestStringParameter ( CAMPO_SEDE_UFF_FAS_SIUS) ;
        
        String lCodUfficioTDS = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescSedeUff);
        
        lPosMod.setChiaveUffFasSius           (lCodUfficioTDS );
        
        lPosMod.setAnnoRegistro               ( getRequestBigDecimalParameter ( CAMPO_ANNO_REGISTRO) );
        lPosMod.setNumeroRegistro             ( getRequestBigDecimalParameter ( CAMPO_NUMERO_REGISTRO) );
        lPosMod.setCodTipoProvvedimento       ( getRequestStringParameter     ( CAMPO_COD_TIPO_PROVVEDIMENTO) );
        //lPosMod.setDataInizio                 ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
        lPosMod.setDataEmissioneProvv         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_EMISSIONE_PROVV,CAMPO_MESE_DATA_EMISSIONE_PROVV,CAMPO_GIORNO_DATA_EMISSIONE_PROVV) );
      
        lPosMod.setNumAnniMisura              ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_MISURA) );
        lPosMod.setNumMesiMisura              ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_MISURA) );
        lPosMod.setNumGiorniMisura            ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_MISURA) );
        
        lPosMod.setDataFineMisura             ( getRequestDateParameter       ( CAMPO_ANNO_DATA_FINE_MISURA, CAMPO_MESE_DATA_FINE_MISURA, CAMPO_GIORNO_DATA_FINE_MISURA) );
      
      }
      
      if ("17".equals(lPosMod.getCodPosizioneGiuridica() ) )
      {
        if (isRequestChecked(CAMPO_CHECK_DECISIONE_TDS) )
        {  
        	siesLogger.debug("--XX-- ActInserisciPosizioneGiuridicaCumulo - Chek Decisione TDS - true");
          lPosMod.setFlagDecisioneTDS("S");
        } 
      }
      
    }
    
    lPosMod.setDatIdDatiFinaliCumulo      ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    lPosMod.setIstrIdIstruttoriaCumulo    ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
    lPosMod.setTitIdTitoloCumulato		  ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
    //siesLogger.debug("lPosMod = "+lPosMod);
    
    return lPosMod;
  }
}
