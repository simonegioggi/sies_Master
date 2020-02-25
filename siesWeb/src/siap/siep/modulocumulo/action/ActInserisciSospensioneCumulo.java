package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.sico.evento.action.ICostantiEvento;
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
 * provedimento di Sospensione disposto su uno dei titoli cumulati
 * @author Intersistemi SpA
 *
 */
public class ActInserisciSospensioneCumulo extends ActionModuloCumulo 
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
    
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaSospensioneCumulo" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    
    
    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella 

    IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    if ("I".equals(lModalita)) {
      siesLogger.debug("Sono in INSERIMENTO");

      StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("I");

      // Si carica in StatoEsecuzione l'ufficio emittente
      lStatoEsecMod.setCodUfficioEmittente       ( getCodUfficioByCodTipoUfficioDescrComune (getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE), getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) ) ) ;
      lStatoEsecMod.setCodLuogoEmittente         ( getCodComuneByDescr(getRequestStringParameter (ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) ).getCodComune()  );

      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      ComputiCumuloModel lComputo = this.getDatiComputo("I");
      siesLogger.debug("lComputo = "+lComputo);
      
      lStatoEsecMod=lCtrlStatoEsec.ExInserisciStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioSospensioneCumulo" 
			  + "&"+ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="+ lStatoEsecMod.getIdStatoEsecTitoloCumulato()
              + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
              + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    }
    else if ("M".equals(lModalita)) {
      siesLogger.debug("Sono in MODIFICA");

      StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("M");
      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      // Si carica in StatoEsecuzione l'ufficio emittente reimpostato.
      
      lStatoEsecMod.setCodUfficioEmittente         ( getCodUfficioByCodTipoUfficioDescrComune (getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE), getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) ) ) ;
      lStatoEsecMod.setCodLuogoEmittente           ( getCodComuneByDescr(getRequestStringParameter (ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) ).getCodComune()  );

      ComputiCumuloModel lComputo = this.getDatiComputo("M");
      siesLogger.debug("lComputo = "+lComputo);
      
      lComputo.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());
      
      // NB Per la modifica di StatoEsec. e ComputoCumulo è stato rinominato: ExModificaPresofferto --> ExModificaStatoEsecComputoCumulo.
      lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputo );
      
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioSospensioneCumulo" 
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
    // lStaMod.setCodTipoProvvedimento    ("02");
    lStaMod.setCodTipoProvvedimento      ( getRequestStringParameter ( ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO) );   
    //lStaMod.setCodMotivo                 ("0800");  // Provvedimento Sospensione GE
    // Combo Oggetto Decisione = cod Motivo evento
    lStaMod.setCodMotivo                 (getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO));  
    if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null){
      lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
      lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
    }
    lStaMod.setDataEmissione             (DateUtils.getDate(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO), 
    														getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO), 
    														getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO))); 

    lStaMod.setCodEsito					 (getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO) ); 
    
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
    		lStaMod.setFlagStato             ( "M" );
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
	// Recupero i dati della Sospensione.
	//==============================================================================
	ComputiCumuloModel lComputo = new ComputiCumuloModel();

	lComputo.setCodTipoAnnotazione("-");
	lComputo.setCodCausaleComputo("-");
	lComputo.setCodDpr("-");
	
	lComputo.setDataRicezioneProvv  		(DateUtils.getDate(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO), 
													   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO), 
													   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO))); 
	//Registro
	lComputo.setCodTipoRegistroOrdinanza  	(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA) );
	
	lComputo.setAnnoProc					(getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO));
	lComputo.setProgrProc					(getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO));
	lComputo.setDataEmissioneProvv			(DateUtils.getDate(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO), 
			   												   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO), 
			   												   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO))); 
	lComputo.setAnnoProvv					(getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO));
	lComputo.setProgrProvv					(getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO));

	String CodTipoAutoEmi = getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
	String DescLuogoAutoEmi = getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE);
	lComputo.setCodTipoAutoritaEmittente(CodTipoAutoEmi);
	lComputo.setCodLuogoEmittente(getCodComuneByDescr(DescLuogoAutoEmi).getCodComune());

	lComputo.setDataSospensioneInterruzione (DateUtils.getDate(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE), 
			   												   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE), 
			   												   getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE))); 
	
	// Combo Contenuto Decisione
//  lComputo.setCodOggettoDecisione     (getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO) ); 
	lComputo.setCodOggettoDecisione			(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE) ); 
	
	
	lComputo.setNote				  (getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MOTIVAZIONI) ); 
	lComputo.setFlagStato			("I"); 
  
	lComputo.setCodUfficioEmittenteProvv    ( getCodUfficioByCodTipoUfficioDescrComune(CodTipoAutoEmi, DescLuogoAutoEmi ) ) ;
	lComputo.setCodLuogoUfficioProvv        ( getCodComuneByDescr(DescLuogoAutoEmi ).getCodComune() );

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
  
}
