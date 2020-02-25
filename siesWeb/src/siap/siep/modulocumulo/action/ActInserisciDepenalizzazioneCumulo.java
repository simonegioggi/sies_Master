package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.sico.evento.action.ICostantiEvento;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un 
 * provedimento di Depenalizzazione disposto su uno dei titoli cumulati
 * @author Intersistemi SpA
 *
 */
public class ActInserisciDepenalizzazioneCumulo extends ActionModuloCumulo 
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
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaDepenalizzazioneCumulo" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    
    
    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella 

    IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    if ("I".equals(lModalita)) {
      siesLogger.debug("Sono in INSERIMENTO");

      StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("I");

      // Si carica in StatoEsecuzione l'ufficio emittente
      lStatoEsecMod.setCodUfficioEmittente       ( getCodUfficioByCodTipoUfficioDescrComune (getRequestStringParameter("CodTipoUffEmi"), getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) ) ) ;
      lStatoEsecMod.setCodLuogoEmittente         ( getCodComuneByDescr(getRequestStringParameter (ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) ).getCodComune()  );

      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      ComputiCumuloModel lComputo = this.getDatiComputo("I");
      siesLogger.debug("lComputo = "+lComputo);
      
      lStatoEsecMod=lCtrlStatoEsec.ExInserisciStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioDepenalizzazioneCumulo" 
			  + "&"+ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="+ lStatoEsecMod.getIdStatoEsecTitoloCumulato()
			  + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
			  + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    }
    else if ("M".equals(lModalita)) {
      siesLogger.debug("Sono in MODIFICA");

      StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("M");
      siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      // Si carica in StatoEsecuzione l'ufficio emittente reimpostato.
      
      lStatoEsecMod.setCodUfficioEmittente         ( getCodUfficioByCodTipoUfficioDescrComune (getRequestStringParameter("CodTipoUffEmi"), getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) ) ) ;
      lStatoEsecMod.setCodLuogoEmittente           ( getCodComuneByDescr(getRequestStringParameter (ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) ).getCodComune()  );

      ComputiCumuloModel lComputo = this.getDatiComputo("M");
      siesLogger.debug("lComputo = "+lComputo);
      
      lComputo.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());
      
      // NB Per la modifica di StatoEsec. e ComputoCumulo può essere nomenclato  ExModificaPresofferto --> ExModificaStatoEsecComputoCumulo.
      lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputo );

      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioDepenalizzazioneCumulo" 
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
    lStaMod.setCodTipoProvvedimento      ("03");
    lStaMod.setCodMotivo                 ("0285");  // Provvedimento Depenalizzazione
    if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
      lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
      lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
    }
    lStaMod.setDataEmissione             (DateUtils.getDate(getRequestStringParameter("DaAnArr"), getRequestStringParameter("DaMeArr"), getRequestStringParameter("DaGiArr"))); 
    lStaMod.setCodEsito                  ("-");
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
    /*
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
    }*/
    
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
	// Recupero i dati della Depenalizzazione.
	//==============================================================================
	ComputiCumuloModel lComputo = new ComputiCumuloModel();

	lComputo.setCodTipoAnnotazione(getRequestStringParameter(ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE));
	lComputo.setCodCausaleComputo("-");
	lComputo.setCodDpr("-");
	
	lComputo.setCodFonte(getRequestStringParameter(ICostantiReato.CAMPO_COD_FONTE));
	lComputo.setAnnoFonte(getRequestBigDecimalParameter(ICostantiReato.CAMPO_ANNO_FONTE));
	lComputo.setNumeroFonte(getRequestStringParameter(ICostantiReato.CAMPO_NUMERO_FONTE));
	lComputo.setCodSottonumerazione(getRequestStringParameter(ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE));
	lComputo.setComma(getRequestStringParameter(ICostantiReato.CAMPO_COMMA));
	lComputo.setLettera(getRequestStringParameter(ICostantiReato.CAMPO_LETTERA));
	lComputo.setNumero(getRequestStringParameter(ICostantiReato.CAMPO_NUMERO));
	lComputo.setArticolo(getRequestStringParameter(ICostantiReato.CAMPO_ARTICOLO));

	lComputo.setNote(getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NOTE));

	if (!isRequestParameterNullObj("IdReato"))
	{
		lComputo.setReaIdReatoCum(getRequestBigDecimalParameter("IdReato"));
	}

	//=========================================================
	// Recupero la Reclusione e la Multa
	//=========================================================
	String GRec = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE);
	String MRec = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE);
	String ARec = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE);
	String Multa = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA+"INT" );
	String Multa_dec = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA+"DEC");

	if (!ARec.equals(""))
		lComputo.setNumAnniReclusione(new BigDecimal(ARec));
	if (!MRec.equals(""))
		lComputo.setNumMesiReclusione(new BigDecimal(MRec));
	if (!GRec.equals(""))
		lComputo.setNumGiorniReclusione(new BigDecimal(GRec));

	if (!Multa.equals(""))
	{
		if (!Multa_dec.equals(""))
			lComputo.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
		else
			lComputo.setImportoMulta(new BigDecimal(Multa));
	} else if (!Multa_dec.equals(""))
		lComputo.setImportoMulta(new BigDecimal("0." + Multa_dec));

	//=========================================================
	// Recupero dati di Arresto e Ammenda
	//=========================================================
	String GArr = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_GIORNI_ARRESTO);
	String MArr = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_MESI_ARRESTO);
	String AArr = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_ANNI_ARRESTO);
	String Ammenda = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA+"INT");
	String Ammenda_dec = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA+"DEC");

    if (!AArr.equals(""))
    	lComputo.setNumAnniArresto(new BigDecimal(AArr));
    if (!MArr.equals(""))
    	lComputo.setNumMesiArresto(new BigDecimal(MArr));
    if (!GArr.equals(""))
    	lComputo.setNumGiorniArresto(new BigDecimal(GArr));

    if (!Ammenda.equals(""))
    {
      if (!Ammenda_dec.equals(""))
    	  lComputo.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
      else
    	  lComputo.setImportoAmmenda(new BigDecimal(Ammenda));
    }
    else if (!Ammenda_dec.equals(""))
    	lComputo.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

    lComputo.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
    lComputo.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );

    if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE))
    	lComputo.setNote  ( getRequestStringParameter  ( ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE) );
    
	lComputo.setCodUfficioEmittenteProvv    	( getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter ("CodTipoUffEmi"), getRequestStringParameter ("CodLuogoEmittente") ) ) ;
	lComputo.setCodLuogoUfficioProvv        	( getCodComuneByDescr(getRequestStringParameter ("CodLuogoEmittente") ).getCodComune() );

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

    lComputo.setAnnoProvv(getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE));
    lComputo.setProgrProvv(getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE));
    
    if (!getRequestStringParameter("DaAnArr").equals(""))
    	lComputo.setDataEmissioneProvv(DateUtils.getDate(getRequestStringParameter("DaAnArr"), getRequestStringParameter("DaMeArr"), getRequestStringParameter("DaGiArr")));

    lComputo.setFlagPiuMeno(getRequestStringParameter("PM"));

	return lComputo;
  }
  
}
