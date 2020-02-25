package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un 
 * provedimento di Interruzione disposto su uno dei titoli cumulati
 * @author Intersistemi SpA
 *
 */
public class ActInserisciInterruzioneCumulo extends ActionModuloCumulo 
      implements ICostantiStatoEsecTitoloCumulato,  ICostantiComputiCumulo, ICostantiDecretoOrdinanzaSiep
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
    
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaInterruzioneCumulo" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    
    
    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella 

    IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    if ("I".equals(lModalita)) {
      siesLogger.debug("Sono in INSERIMENTO");

      StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("I");

      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      ComputiCumuloModel lComputo = this.getDatiComputo("I");
      siesLogger.debug("lComputo = "+lComputo);
      
      lStatoEsecMod=lCtrlStatoEsec.ExInserisciStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioInterruzioneCumulo" 
			  + "&"+ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="+ lStatoEsecMod.getIdStatoEsecTitoloCumulato()
              + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
              + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    }
    else if ("M".equals(lModalita)) {
      siesLogger.debug("Sono in MODIFICA");

      StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("M");
      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      ComputiCumuloModel lComputo = this.getDatiComputo("M");
      siesLogger.debug("lComputo = "+lComputo);
      
      lComputo.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());
      
      // NB Per la modifica di StatoEsec. e ComputoCumulo è stato rinominato: ExModificaPresofferto --> ExModificaStatoEsecComputoCumulo.
      lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputo );
      
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioInterruzioneCumulo" 
			  + "&"+ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="+ lStatoEsecMod.getIdStatoEsecTitoloCumulato()
              + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
              + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    }
    else if ("C".equals(lModalita)) {
      siesLogger.debug("Sono in CANCELLAZIONE");      
      
      BigDecimal lIdStatoEsecuzione = getRequestBigDecimalParameter ( CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      BigDecimal lIdComputo = getRequestBigDecimalParameter ( CAMPO_ID_COMPUTI_CUMULO);
      
      // Devo verificare se cancellare un solo computo o l'intero provvedimento
      IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();      
      StatoEsecTitoloCumulatoModel lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull (lIdStatoEsecuzione);  
      
      if (lStato.getListaComputi().size()==1){
        // Un solo computo, cancello tutto 
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
    lStaMod.setCodMotivo				 (getRequestStringParameter(CAMPO_COD_OGGETTO_DECISIONE));
    lStaMod.setCodTipoProvvedimento		 (getCodTipoProvvedimento(lStaMod.getCodMotivo()));

    if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null){
      lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
      lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
    }
    lStaMod.setDataEmissione             (DateUtils.getDate(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO), 
    														getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO), 
    														getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO))); 

    lStaMod.setCodEsitoTenore            ("-");
    lStaMod.setAnnoProcedimento          (null);
    lStaMod.setProgrProcedimento         (null);
    lStaMod.setAnnoProvvedimento         (null);
    lStaMod.setProgrProvvedimento        (null);
    
    if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE))
      lStaMod.setNote  ( getRequestStringParameter  ( ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE) );
    
    lStaMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
    lStaMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
    lStaMod.setFlagStato                 ( "I" );
    lStaMod.setMotivoModifica            ( null );
    
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
   * Recupera i dati dei computi cumulo
   * @return
   * @throws F3BException
   */
  private ComputiCumuloModel getDatiComputo(String aTipoOper) throws F3BException
  {
	//==============================================================================
	// Recupero i dati della Interruzione.
	//==============================================================================
	ComputiCumuloModel lComputo = new ComputiCumuloModel();

	lComputo.setCodTipoAnnotazione("-");
	lComputo.setCodCausaleComputo("-");
	lComputo.setCodDpr("-");
	
	lComputo.setDataSospensioneInterruzione (DateUtils.getDate(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA), 
													   		   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA), 
													   		   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA))); 
	lComputo.setDataEmissioneProvv  		(DateUtils.getDate(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO), 
			   												   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO), 
			   												   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO))); 
	lComputo.setDataRicezioneProvv  		(DateUtils.getDate(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO), 
													   		   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO), 
													   		   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO)));
	
	// n.b.CAMPO_COD_OGGETTO_DECISIONE è memorizzato sul COD_MOTIVO
	//lComputo.setCodOggettoDecisione			(getRequestStringParameter(CAMPO_COD_OGGETTO_DECISIONE) ); 

	lComputo.setCodTipoRegistroOrdinanza  	("-");
	lComputo.setCodUfficioEmittenteProvv    ("-");
	lComputo.setCodTipoAutoritaEmittente	("-");
	lComputo.setCodLuogoEmittente			("-");
	
	lComputo.setProtocollo      ( getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_PROTOCOLLO) );
	lComputo.setAltraAutorita   ( getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ALTRA_AUTORITA) ); 
	lComputo.setAltroLuogo      ( getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ALTRO_LUOGO) ); 
	lComputo.setNote						( getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MOTIVAZIONI) ); 
	
	lComputo.setTitIdTitoloCumulato       	( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
    lComputo.setIstrIdIstruttoriaCumulo   	( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );

    if ("I".equals(aTipoOper)) {
    	lComputo.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
    	lComputo.setDataInserimento             ( DateUtils.getSysDate() );
    	lComputo.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
	} 
	else if ("M".equals(aTipoOper)) {
		lComputo.setIdComputiCumulo				(getRequestBigDecimalParameter(ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO) );
		
		lComputo.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
		lComputo.setDataAggiornamento           ( DateUtils.getSysDate() );
		lComputo.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
	}    

	return lComputo;
  }
  
  /**
   * Il tipo di provvedimento emesso dipende dal codice Motivo
   *
   * @param aCodMotivo
   */
  private String getCodTipoProvvedimento(String aCodMotivo) throws F3BException
  {
    String lCodTipoProv = "";

    if (aCodMotivo == null)
      throw new F3BException(F3BException.USER_MESSAGE, "Codice Motivo assente");
    else if (aCodMotivo.compareTo("0266") == 0)
      lCodTipoProv = "12";
    else if (aCodMotivo.compareTo("0267") == 0)
      lCodTipoProv = "12";
    else if (aCodMotivo.compareTo("0268") == 0)
      lCodTipoProv = "25";
    else if (aCodMotivo.compareTo("0269") == 0)
      lCodTipoProv = "25";
    else if (aCodMotivo.compareTo("0270") == 0)
      lCodTipoProv = "25";
    else if (aCodMotivo.compareTo("0366") == 0) // new! since 40upd01
      lCodTipoProv = "25";
    else
      throw new F3BException(F3BException.USER_MESSAGE,  "Codice Motivo errato -> " + aCodMotivo);
    siesLogger.debug("CodTipoProvvedimento -> " + lCodTipoProv);
    return lCodTipoProv;
  }
}
