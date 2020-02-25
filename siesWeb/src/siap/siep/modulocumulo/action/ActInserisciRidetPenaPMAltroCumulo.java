package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un 
 * provedimento di Rideterminazione Pena PM altro disposto su uno dei titoli cumulati
 * @author
 *
 */
public class ActInserisciRidetPenaPMAltroCumulo extends ActionModuloCumulo 
      implements ICostantiStatoEsecTitoloCumulato,  ICostantiComputiCumulo
{
	// Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
    
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaRidetPenaPMAltroCumulo" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    
    
    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella 

    IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    if ("I".equals(lModalita)) {
      siesLogger.debug("Sono in INSERIMENTO");

      // Si carica lo Stato Esecuzione per la Ridet. pena 
      StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("I");

      // Si caricano i Computi Cumulo
      Vector <ComputiCumuloModel> lComputi = this.getDatiComputi("I");
      //siesLogger.debug("lComputi = "+lComputi);
      
      lStatoEsecMod=lCtrlStatoEsec.ExInserisciStatoEsecComputiCumulo(lStatoEsecMod, lComputi);

      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioRidetPenaPMAltroCumulo" 
              + "&"+ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="+ lStatoEsecMod.getIdStatoEsecTitoloCumulato()
              + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
              + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    }
    else if ("M".equals(lModalita)) {
      siesLogger.debug("Sono in MODIFICA");

      StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("M");
      //siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);
      
      // Si caricano i Computi Cumulo
      Vector <ComputiCumuloModel> lComputi = this.getDatiComputi("M");
      siesLogger.debug("lComputi = "+lComputi);
      
      lCtrlStatoEsec.ExModificaStatoEsecComputiCumulo(lStatoEsecMod, lComputi );
      
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioRidetPenaPMAltroCumulo" 
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
        siesLogger.debug("Elimino il solo computo:"+lIdComputo);
        IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
        lCtrlComputi.ExCancellaComputiCumuloBykey(lIdComputo);
      }
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaRidetPenaPMAltroCumulo" 
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
    lStaMod.setCodTipoProvvedimento 	 ("25"); 

    // Valorizzazione Stato Esecuzione in base al tipo Autorità Emittente (d'ufficio / in esecuzione di provvedimento altro ufficio)
    if (getRequestStringParameter("TipoOrd").equals("dufficio")){
    	lStaMod.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
	} else {
		lStaMod.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO+"_AA"));
	}

    if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null){
      lStaMod.setCodUfficioEmittente	 (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
      lStaMod.setCodLuogoEmittente		 (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
    }
    lStaMod.setDataEmissione			 (getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
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
  private Vector <ComputiCumuloModel> getDatiComputi(String aTipoOper) throws F3BException
  {
	//==============================================================================
	// Recupero i dati delle annotazioni Rideterminazione Pena altro.
	//==============================================================================
    Vector<ComputiCumuloModel> lListaComputiCumulo = new Vector<ComputiCumuloModel>();
    
    if (!this.isRequestParameterNullObj("maxNumComputi"))
    {
      int maxNumComputi = getRequestIntParameter("maxNumComputi");
      for (int i=0;i<maxNumComputi;i++)
      {
        if (  !isRequestParameterNullObj("PM_"+i))
        {
          if (!getRequestStringParameter("PM_"+i).equals(""))
          {
            //siesLogger.debug("Parametro PM_"+i+" impostato");
            ComputiCumuloModel lComputo =  getComputo(i);

        	//-----------------------------------------
            // Valorizzazioni comuni ai Computi Cumulo
        	//-----------------------------------------
        	lComputo.setStatIdStatoEsecTitCum(getRequestBigDecimalParameter ( CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO) );
        	lComputo.setCodTipoAnnotazione("014"); // altro
        	lComputo.setCodCausaleComputo("-");
        	lComputo.setCodDpr("-");

            lComputo.setTitIdTitoloCumulato			( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
            lComputo.setIstrIdIstruttoriaCumulo		( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
        
            if ("I".equals(aTipoOper)) {
            	lComputo.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
            	lComputo.setDataInserimento             ( DateUtils.getSysDate() );
            	lComputo.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
            } 
            else if ("M".equals(aTipoOper)) {
            	lComputo.setIdComputiCumulo				(getRequestBigDecimalParameter(ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO+"_"+i) );
    		
            	lComputo.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
            	lComputo.setDataAggiornamento           ( DateUtils.getSysDate() );
            	lComputo.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
            }

    		if (getRequestStringParameter("TipoOrd").equals("altroUfficio"))
    		{
	    	  lComputo.setDataEmissioneProvv	(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE+"_AA", ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE+"_AA", ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE+"_AA"));
	    	  lComputo.setAnnoProvv				(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO+"_AA"));
	    	  lComputo.setProgrProvv			(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO+"_AA"));
	    	  lComputo.setCodTipoProvv			(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO+"_AA"));
	
	    	  // Autorità emittente
	    	  String lCodTipoUff  = getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE+"_AA");
	    	  String lDescrComune = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE+"_AA");
	    	  
	    	  ComuneModel lComune = this.getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE+"_AA"));
	    	  String lCodLuogoEmittente = lComune.getCodComune();
	    	 
	    	  String lCodUffEmittente   = this.getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescrComune);
	    	  
	    	  lComputo.setCodUfficioEmittenteProvv	(lCodUffEmittente);
	    	  lComputo.setCodLuogoUfficioProvv		(lCodLuogoEmittente);
	    	
	    	  lComputo.setDataRicezioneProvv		(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI+"_AA", ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI+"_AA", ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI+"_AA"));
    		}

            lListaComputiCumulo.add(lComputo);

          } else {
            siesLogger.debug("Parametro PM_"+i+" non selezionato nella form");
          }
        } else {
          siesLogger.debug("Parametro PM_"+i+" assente nella form");
        }
      }
    }

	return lListaComputiCumulo;
  }

  /**
   * Recupera dalla form i dati dei quantum di computo relativi all'id passato 
   * in input 
   * @param id_computo
   * @return
   * @throws F3BException
   */
  private ComputiCumuloModel getComputo(int id_computo) throws F3BException
  {
    //==========================================================================
    // Recupero dalla form i dati dell'annotazione manuale da inserire
    //==========================================================================
    ComputiCumuloModel lComputo = new ComputiCumuloModel();

    lComputo.setCodTipoAnnotazione("014"); // altro
    lComputo.setFlagPiuMeno(getRequestStringParameter("PM_"+id_computo));
    //lComputo.setFlagConforme("-");
    //lComputo.setFlagValidato("N");
    lComputo.setCodFonte("-");
    lComputo.setCodSottonumerazione("-");
    lComputo.setCodCausaleComputo("-");
    lComputo.setCodDpr("-");
    //lComputo.setFlagAppProvvisoria("-");
    lComputo.setNote(getRequestStringParameter("motivazioni_"+id_computo));

    //===========================================
    // reclusione e multa
    //===========================================
    String GRec = getRequestStringParameter("GRec_"+id_computo);
    String MRec = getRequestStringParameter("MRec_"+id_computo);
    String ARec = getRequestStringParameter("ARec_"+id_computo);
    String Multa = getRequestStringParameter("Multa_"+id_computo);
    String Multa_dec = getRequestStringParameter("Mul_dec_"+id_computo);

    if (!ARec.equals(""))
    	lComputo.setNumAnniReclusione(new BigDecimal(ARec));
    else
    	lComputo.setNumAnniReclusione(new BigDecimal(0));

    if (!MRec.equals(""))
    	lComputo.setNumMesiReclusione(new BigDecimal(MRec));
    else
    	lComputo.setNumMesiReclusione(new BigDecimal(0));

    if (!GRec.equals(""))
    	lComputo.setNumGiorniReclusione(new BigDecimal(GRec));
    else
    	lComputo.setNumGiorniReclusione(new BigDecimal(0));

    if (!Multa.equals("")) {
      if (!Multa_dec.equals("")) {
    	  lComputo.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
      } else
    	  lComputo.setImportoMulta(new BigDecimal(Multa));
    } else if (!Multa_dec.equals(""))
    	lComputo.setImportoMulta(new BigDecimal("0." + Multa_dec));

    //===========================================
    // arresto e ammenda
    //===========================================
    String GArr = getRequestStringParameter("GArr_"+id_computo);
    String MArr = getRequestStringParameter("MArr_"+id_computo);
    String AArr = getRequestStringParameter("AArr_"+id_computo);
    String Ammenda = getRequestStringParameter("Ammenda_"+id_computo);
    String Ammenda_dec = getRequestStringParameter("Amm_dec_"+id_computo);

    if (!AArr.equals(""))
    	lComputo.setNumAnniArresto(new BigDecimal(AArr));
    else
    	lComputo.setNumAnniArresto(new BigDecimal(0));

    if (!MArr.equals(""))
    	lComputo.setNumMesiArresto(new BigDecimal(MArr));
    else
    	lComputo.setNumMesiArresto(new BigDecimal(0));

    if (!GArr.equals(""))
    	lComputo.setNumGiorniArresto(new BigDecimal(GArr));
    else
    	lComputo.setNumGiorniArresto(new BigDecimal(0));

    if (!Ammenda.equals("")) {
      if (!Ammenda_dec.equals("")) {
    	  lComputo.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
      } else
    	  lComputo.setImportoAmmenda(new BigDecimal(Ammenda));
    } else if (!Ammenda_dec.equals(""))
    	lComputo.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

    return lComputo;
  }
  
}
