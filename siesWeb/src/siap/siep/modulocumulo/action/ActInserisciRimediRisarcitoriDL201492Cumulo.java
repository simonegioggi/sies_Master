package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import org.apache.log4j.Logger;

/**
 * Action che effettua l'inserimento dei dati del provvedimento di Rimedi Risarcitori DL 2014/92 (Titolo Cumulato)
 * concessione di giorni di riduzione pena e/o risarcimento, 
 * secondo quanto previsto dal DL92/2014
 */
public class ActInserisciRimediRisarcitoriDL201492Cumulo extends ActionModuloCumulo implements ICostantiLibAnticipataCumulo, ICostantiStatoEsecTitoloCumulato, ICostantiPeriodoLibAntCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws Exception
  {
	  siesLogger.debug("--XX-- INIZIO ActInserisciRimediRisarcitoriDL201492Cumulo " );
	  
    String Concessione="C";  // per default vengono aggiunti
    StatoEsecTitoloCumulatoModel lStatoEsec = new StatoEsecTitoloCumulatoModel();

    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella, NP=nuovo Periodo 
    
    String lStato="I";
    if(lModalita.equals("M"))
    {	
    	// Cambia stato solo se il dato NON è Iscritto manualmente  
    	if(getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("E") ||
    		getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("M")	)
    	{	
    		lStato = "M";
    	}	
    }
    
    //===============================================================================================
    // Stato_Esecuzione_Titolo_Cumulato
    //================================================================================================
    if(!lModalita.equals("C"))
    {
    	String lCodTipoUfficio = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE);
        String lDescrComune = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE);
        String lCodUfficio = getUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComune).getCodUfficio();
        ComuneModel lComune = getCodComuneByDescr(lDescrComune);
        
	    if ("M".equals(lModalita)) 
	    {
	    	siesLogger.debug(" ----- >  StatoEsec - modifica ");
	    	lStatoEsec.setIdStatoEsecTitoloCumulato(getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO));
	    	
	        lStatoEsec.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
	        lStatoEsec.setDataAggiornamento           ( DateUtils.getSysDate() );
	        lStatoEsec.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso() );
	    }
	    else if ("I".equals(lModalita)) 
	    {
	    	lStatoEsec.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
	        lStatoEsec.setDataInserimento             ( DateUtils.getSysDate() );
	        lStatoEsec.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
	    }
	    
	    lStatoEsec.setCodUfficioEmittente(lCodUfficio);
	    lStatoEsec.setCodLuogoEmittente(lComune.getCodComune());
	    
	    lStatoEsec.setDataEmissione(getRequestDateParameter (CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE) );
	    
	    lStatoEsec.setAnnoProcedimento   (getRequestBigDecimalParameter(CAMPO_ANNO_PROCEDIMENTO) );
	    lStatoEsec.setProgrProcedimento	 (getRequestBigDecimalParameter(CAMPO_PROGR_PROCEDIMENTO) );
	    lStatoEsec.setAnnoProvvedimento  (getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO) );
	    lStatoEsec.setProgrProvvedimento (getRequestBigDecimalParameter(CAMPO_PROGR_PROVVEDIMENTO) );
	    
	    lStatoEsec.setCodTipoEvento("01"); // 01 = Provvedimento.
		lStatoEsec.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));  // 02 = Decreto ; 03 = Ordinanza.
		
		lStatoEsec.setCodEsitoTenore("-");
		lStatoEsec.setCodEsito(getRequestStringParameter(CAMPO_COD_ESITO));
		
	    if(getRequestStringParameter(CAMPO_COD_ESITO).equals("0028")  )				// Accoglie il Reclamo dell'interessato/difensore
	    {	
	          lStatoEsec.setCodMotivo("9027");  						// Riduzione pena da espiare/risarcimento del danno (Reclamo art. 35 ter O.P.)
	    }     
	    else if(getRequestStringParameter(CAMPO_COD_ESITO).equals("2758") )			// Accoglie il reclamo del PM/Amministrazione Penitenziaria
	    {
	    	  lStatoEsec.setCodMotivo("9027");  						// Riduzione pena da espiare/risarcimento del danno(Reclamo art. 35 ter O.P.)
	    	  Concessione = "S";
	    }
	    else if(getRequestStringParameter(CAMPO_COD_ESITO).equals("0390") ||		// Dispone riduzione pena detentiva 
	    		getRequestStringParameter(CAMPO_COD_ESITO).equals("0391") || 		// Dispone riduzione pena detentiva e liquida somma di denaro per risarcimento del danno 
	    		getRequestStringParameter(CAMPO_COD_ESITO).equals("0392") 	) 		// Liquida somma di denaro per risarcimento del danno
	    {
	          lStatoEsec.setCodMotivo("2790");  						// Riduzione pena da espiare/risarcimento del danno (art. 35 ter O.P.)
	
	    }
	    
	    if(getRequestStringParameter(CAMPO_NOTE) != null && !getRequestStringParameter(CAMPO_NOTE).equals(""))
	        lStatoEsec.setNote(getRequestStringParameter(CAMPO_NOTE));
	      else
	        lStatoEsec.setNote("");
	    
	    lStatoEsec.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
	    lStatoEsec.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
	    lStatoEsec.setFlagStato                 ( lStato );
	    lStatoEsec.setMotivoModifica            ( null );
    }
    //------------------------------------------------------------------------------------------------------------------------------------
    
    Vector <LibAnticipataCumuloModel> VecLiberazioni = new Vector <LibAnticipataCumuloModel>();
    //LibAnticipataCumuloModel LibAntMod  = null;
    
    //=========================================================================
    // Gestione dei dati dei GG di Riduzione (Liberazione Anticipata)
    //===========================================================================
    if ( ( !isRequestParameterNullObj(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI) 
            && !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI).equals("") 
            && !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI).equals("0")
         )
         	&& !lModalita.equals("C")	// Casi di INSERIMENTO e MODIFICA
       )  
    { 
    	//siesLogger.debug(" ----- >  L.A. Giorni - NumggLA = "+this.getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI) );
    	
      LibAnticipataCumuloModel LibAntMod	= new LibAnticipataCumuloModel();
    	
        LibAntMod.setNumeroGiorni(new BigDecimal(this.getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI) ) );
        LibAntMod.setCodTipoLicenza("RD");
       // LibAntMod.setTipoLa("LA");

        LibAntMod.setCodOperatoreInserimento(getCodUtenteConnesso());
       	LibAntMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
       	LibAntMod.setDataInserimento(DateUtils.getSysDate());

       	// In caso di MODIFICA inserisco volutamente Attributi Ins e Mod
       	if ("M".equals(lModalita)) 
        {
      	  	LibAntMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
      	  	LibAntMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
      	  	LibAntMod.setDataAggiornamento(DateUtils.getSysDate());
        } 
      
        LibAntMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
        LibAntMod.setFlagStato                 ( lStato );
        LibAntMod.setMotivoModifica            ( null );
        
        LibAntMod.setFlagConcesso(Concessione); 

        VecLiberazioni.add(LibAntMod);

        //=============================
        //    Gestione dei PERIODI 
        //=============================

        Vector <PeriodoLibAntCumuloModel> Vecperiodi = new Vector <PeriodoLibAntCumuloModel>();
        Vecperiodi = recuperaPeriodi("DIV_RD_C", lModalita, lStato);
        siesLogger.debug("--XX-- lModalita = "+lModalita+" - Periodi presenti per RD = "+Vecperiodi.size());
        LibAntMod.setListaPeriodiLibAnticipate(Vecperiodi);
    }
// End L.A. 
    
    
    //=========================================================================
    // Gestione dei dati della Somma di Risarcimento (Liberazione Anticipata)
    //===========================================================================
      
    if ( ( !isRequestParameterNullObj(ICostantiLibAnticipataCumulo.CAMPO_INTERO_SOMMA_RISARC_DANNI) 
            && !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_INTERO_SOMMA_RISARC_DANNI).equals("") 
            && !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_INTERO_SOMMA_RISARC_DANNI).equals("0")
         )
         ||
         ( !isRequestParameterNullObj(ICostantiLibAnticipataCumulo.CAMPO_DECIMALE_SOMMA_RISARC_DANNI) 
             && !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_DECIMALE_SOMMA_RISARC_DANNI).equals("") 
             && !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_DECIMALE_SOMMA_RISARC_DANNI).equals("0")
         )
         	&& !lModalita.equals("C")	// Casi di INSERIMENTO e MODIFICA
       )
    { 
    	//siesLogger.debug(" ----- >  L.A. Giorni - NumggLA = "+this.getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI) );
    	siesLogger.debug("Presente Somma Liquidata");
    	
    	BigDecimal lSomma = getSommaLiquidata();
    	
    	LibAnticipataCumuloModel LibAntMod	= new LibAnticipataCumuloModel();
    	
        LibAntMod.setSommaRisarcDanni(lSomma);
        LibAntMod.setCodTipoLicenza("SL");
       // LibAntMod.setTipoLa("LA");
      
       	LibAntMod.setCodOperatoreInserimento(getCodUtenteConnesso());
       	LibAntMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
       	LibAntMod.setDataInserimento(DateUtils.getSysDate());

       	if ("M".equals(lModalita)) 
        {
      	  	LibAntMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
      	  	LibAntMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
      	  	LibAntMod.setDataAggiornamento(DateUtils.getSysDate());
        } 
      
        LibAntMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
        LibAntMod.setFlagStato                 ( lStato );
        LibAntMod.setMotivoModifica            ( null );
        
        LibAntMod.setFlagConcesso(Concessione); 

        //
        VecLiberazioni.add(LibAntMod);

        //=============================
        //    Gestione dei PERIODI 
        //=============================

        Vector <PeriodoLibAntCumuloModel> Vecperiodi = new Vector <PeriodoLibAntCumuloModel>();
        Vecperiodi = recuperaPeriodi("DIV_SL_C", lModalita, lStato);
        siesLogger.debug("--XX-- lModalita = "+lModalita+" - Periodi presenti per SL = "+Vecperiodi.size());
        LibAntMod.setListaPeriodiLibAnticipate(Vecperiodi);
    }
// End L.A.     
   
    
    
    //===================================
    // Effettuo l'inserimento dei dati
    //===================================
    	
   	IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    BigDecimal IdStatoEsec = null;
    
    if ("I".equals(lModalita) )
    {
    	IdStatoEsec = lCtrlStatoEsec.ExInserisciRimediRisarcitoriCumulo(lStatoEsec, VecLiberazioni);
    }
    else if ("M".equals(lModalita) )
    {
    	IdStatoEsec = lCtrlStatoEsec.ExModificaRimediRisarcitoriCumulo(lStatoEsec, VecLiberazioni);
    } 
    else if ("C".equals(lModalita) )
    {
    	lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById( getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO), null);
    }
 
    String lPage = "";

    if ("I".equals(lModalita) || "M".equals(lModalita) ){
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioRimediRisarcitoriDL201492Cumulo" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
          + "&"+ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "=" +IdStatoEsec;
    }
    else {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaRimediRisarcitoriDL201492Cumulo" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    }

    return lPage;

//    if (1==1)
//      throw new F3BException(F3BException.USER_MESSAGE, " FATTO ");


  }
 
  /**
   * Recupera dalla form in dati dei periodi per il singolo computo
   * @param aNomeDiv
   * @return
   * @throws Exception
   */
  private Vector<PeriodoLibAntCumuloModel> recuperaPeriodi (String aNomeDiv, String aModalita, String aStato)  throws Exception  
  {
    //siesLogger.debug("recuperaPeriodi per Nome Div = "+aNomeDiv);
    
    int lNumPeriodi = getRequestIntParameter("NumPeriodi");
  

    Vector <PeriodoLibAntCumuloModel> lVectPeriodi = new Vector <PeriodoLibAntCumuloModel>();
    
    // Recupero i periodi inseriti in form
    for (int i=0;i<lNumPeriodi;i++) {
      String lNomeCampoGiornoDa = ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO+"_"+aNomeDiv+"_"+i;
      
      if (   !isRequestParameterNullObj(lNomeCampoGiornoDa)
          && getRequestStringParameter(lNomeCampoGiornoDa).length()>0
         )
      {
        PeriodoLibAntCumuloModel lPeriodoLib = new PeriodoLibAntCumuloModel();
        Date lDataInizio = getRequestDateParameter( ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO+"_"+aNomeDiv+"_"+i,
        											ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO+"_"+aNomeDiv+"_"+i,
        											ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO+"_"+aNomeDiv+"_"+i);
           
        Date lDataFine = getRequestDateParameter( ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE+"_"+aNomeDiv+"_"+i,
        										  ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE+"_"+aNomeDiv+"_"+i,
        										  ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE+"_"+aNomeDiv+"_"+i);
        //
        lPeriodoLib.setDataInizio    (lDataInizio);
        lPeriodoLib.setDataFine      (lDataFine);
        
        lPeriodoLib.setFlagStato(aStato);
        lPeriodoLib.setMotivoModifica(null);
        
       	lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
       	lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
        lPeriodoLib.setDataInserimento(DateUtils.getSysDate());
 
    	if ("M".equals(aModalita)) 
        {
    		lPeriodoLib.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    		lPeriodoLib.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    		lPeriodoLib.setDataAggiornamento(DateUtils.getSysDate());
        }
        
        lVectPeriodi.add(lPeriodoLib);
      }
    }
    
    return lVectPeriodi;
  }


  /**
   * Recupera dalla form la soma liquidata, parte intera + paret decimale
   * 
   * @return la somma liquidata se indicata in form, null altrimenti
   * @throws Exception
   */
  private BigDecimal getSommaLiquidata() throws F3BException {
    String lParteIntera   = getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_INTERO_SOMMA_RISARC_DANNI);
    String lParteDecimale = getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_DECIMALE_SOMMA_RISARC_DANNI);
    BigDecimal lSommaLiquidata = null;
    
    if (!lParteIntera.equals("")) {
      if (!lParteDecimale.equals("")) {
        lSommaLiquidata = new BigDecimal(lParteIntera + "." + lParteDecimale);
      } else
        lSommaLiquidata = new BigDecimal(lParteIntera);
    } else if (!lParteDecimale.equals("")) {
      lSommaLiquidata = new BigDecimal("0." + lParteDecimale); 
    }
  
    return lSommaLiquidata;
  }

}
