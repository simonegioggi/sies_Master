package siap.siep.modulocumulo.action;


/**
* <p>Title: ActInserisciMisuraCautelareCumulo</p>
* <p>Description: Classe Action per l'inserimento di MisuraCautelareCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IMisuraCautelareCumulo;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


public class ActInserisciMisuraCautelareCumulo extends ActionModuloCumulo implements ICostantiMisuraCautelareCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

 /*****************************************************************************
  * Azione di Inserimento/Modifoca e Cancellazione del MisuraCautelareCumulo
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  ****************************************************************************/
  public String processRequest() throws F3BException 
  {
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
    
    
    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella 

 
    IMisuraCautelareCumulo lCtrl = SIEPLookupRemote.getMisuraCautelareCumuloRemote();
    
   
    MisuraCautelareCumuloModel lMisRetMod = null;
   

    if ("I".equals(lModalita)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sono in INSERIMENTO");
      
      MisuraCautelareCumuloModel lMisMod = this.getDatiForm();

      lMisMod.setFlagStato("I");
      
      lMisMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
      lMisMod.setDataInserimento             ( DateUtils.getSysDate() );
      lMisMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );      
      
      lMisRetMod = lCtrl.ExInserisciMisuraCautelareCumulo (lMisMod);
    }
    else if ("M".equals(lModalita)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sono in MODIFICA");

      MisuraCautelareCumuloModel lMisMod = this.getDatiForm();
      
      if (lMisMod.getFlagStato().equals("I"))
        lMisMod.setFlagStato("I"); // Resta I
      else 
        lMisMod.setFlagStato("M"); // E' un dato estratto


      lMisMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      lMisMod.setDataAggiornamento           ( DateUtils.getSysDate() );
      lMisMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      
      lCtrl.ExModificaMisuraCautelareCumulo (lMisMod);
    }
    else if ("C".equals(lModalita)) {
      // Gestire cancellazione
      BigDecimal lIdMisuraCautelare = getRequestBigDecimalParameter ( CAMPO_ID_MISURA_CAUTELARE_CUMULO);
     
      String lFlagStato = getRequestStringParameter( CAMPO_FLAG_STATO);

      if ("I".equals(lFlagStato)) {
        // Cancellazione Fisica del record
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("Cancellazione Fisica del record con id "+lIdMisuraCautelare);
        lCtrl.ExCancellaMisuraCautelareCumulo (lIdMisuraCautelare);
      }
      else if ("E".equals(lFlagStato) || "M".equals(lFlagStato)){
        // Cancellazione logica
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("Cancellazione Logica del record con id "+lIdMisuraCautelare);
        
        MisuraCautelareCumuloModel lMisMod = new MisuraCautelareCumuloModel();
        
        lMisMod.setIdMisuraCautelareCumulo     ( lIdMisuraCautelare );
        lMisMod.setFlagStato                   ( "C" );
        lMisMod.setMotivoModifica              ( getRequestStringParameter ( ICostantiMisuraCautelareCumulo.CAMPO_MOTIVO_MODIFICA) );
        
        lMisMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lMisMod.setDataAggiornamento           ( DateUtils.getSysDate() );
        lMisMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());

  
        lCtrl.ExCancellaMisuraCautelareCumuloLogica (lMisMod);        
        
      }
      else {
        throw new F3BException(F3BException.USER_MESSAGE,"Impossibile effettuare la cancellazione. Lo stato del Dato non è noto.");
      }
    }



    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    if ("C".equals(lModalita)) {
      // Ricarico la Lista delle Misure Cautelari
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaMisureCautelariCumulo";
      lPage += "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO ).toString();
      lPage += "&" + ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" + getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO).toString();
    }
    else {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioMisuraCautelareCumulo";
      if ("I".equals(lModalita))
        lPage += "&" + CAMPO_ID_MISURA_CAUTELARE_CUMULO + "=" + lMisRetMod.getIdMisuraCautelareCumulo();
      else
        lPage += "&" + CAMPO_ID_MISURA_CAUTELARE_CUMULO + "=" + getRequestBigDecimalParameter ( CAMPO_ID_MISURA_CAUTELARE_CUMULO);
    }

    return lPage;
  }
  
  /**
   * Recupera i dati dalla FORM
   * @return
   * @throws F3BException
   */
  private MisuraCautelareCumuloModel getDatiForm () throws F3BException
  {
    MisuraCautelareCumuloModel lMisMod = new MisuraCautelareCumuloModel();

    //========================================================================== 
    // Recupero i dati presenti in maschera 
    //========================================================================== 
    lMisMod.setIdMisuraCautelareCumulo     ( getRequestBigDecimalParameter ( CAMPO_ID_MISURA_CAUTELARE_CUMULO) );

    lMisMod.setCodTipoMisura               ( getRequestStringParameter     ( CAMPO_COD_TIPO_MISURA) );
    lMisMod.setDataInizio                  ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
    lMisMod.setDataFine                    ( getRequestDateParameter       ( CAMPO_ANNO_DATA_FINE,CAMPO_MESE_DATA_FINE,CAMPO_GIORNO_DATA_FINE) );
    
    // Calcolo i Quantum
    CalendarModel lCalMod = new CalendarModel();
    lCalMod.setDataInizio (lMisMod.getDataInizio());
    lCalMod.setDataFine   (lMisMod.getDataFine());

    CalendarUtil lCalUtil = new CalendarUtil();
    lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni (lCalMod);
    lCalMod = lCalUtil.ricalcolaGAM (lCalMod);

    lMisMod.setNumAnni   (new BigDecimal(lCalMod.getNumAnni()));
    lMisMod.setNumMesi   (new BigDecimal(lCalMod.getNumMesi()));
    lMisMod.setNumGiorni (new BigDecimal(lCalMod.getNumGiorni()));    

    if ("CL".equals(lMisMod.getCodTipoMisura())) {
      // Nel caso della Messa Alla Prova i Quantum calcolati vanno divisi per 3 e arrotondati all'intero più vicino
      // I giorni effettivamente espiati vanno memorizzati nel campo GIORNI- n.b. trattasi dei quantum convertiti in GG e non 
      // dei giorni calendariali.
    	
		 /*     CalendarModel lCalendarConvertito = MisuraCautelareCumuloModel.getQuantumMessaAllaProva (lCalMod);
		      
		      lMisMod.setNumAnni   (new BigDecimal (lCalendarConvertito.getNumAnni()));
		      lMisMod.setNumMesi   (new BigDecimal (lCalendarConvertito.getNumMesi()));
		      lMisMod.setNumGiorni (new BigDecimal (lCalendarConvertito.getNumGiorni())); 
		      
		      int lGiorni = 360*lCalMod.getNumAnni()+30*lCalMod.getNumMesi()+lCalMod.getNumGiorni();
		      lMisMod.setGiorni (new BigDecimal(lGiorni));
		      // Diff + 1 per il dies a quo
		      //lMisMod.setGiorni (new BigDecimal (DateUtils.getDaysBetween (lMisMod.getDataInizio(), lMisMod.getDataFine())+1 ) );
		 */
    	
    	// Il calcolo è stato effettuato direttamente nella form con una chiamata al metodo esterno;  
    	// L'Utente però, può digitare i quantum a mano, quindi nel DB vanno i campi Input della FORM. 
    	
    	if(getRequestBigDecimalParameter(CAMPO_NUM_ANNI)!=null)
    		lMisMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI) );
    	else
    		lMisMod.setNumAnni(new BigDecimal(0));
    	
    	if(getRequestBigDecimalParameter(CAMPO_NUM_MESI)!=null)
    		lMisMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI) );
    	else
    		lMisMod.setNumMesi(new BigDecimal(0));
    	
    	if(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI)!=null)
    		lMisMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI) );
    	else
    		lMisMod.setNumGiorni(new BigDecimal(0));
    	
    	if(getRequestBigDecimalParameter(CAMPO_GIORNI)!=null)
    		lMisMod.setGiorni (getRequestBigDecimalParameter(CAMPO_GIORNI) );
    	else
    		lMisMod.setGiorni(null);
    	
    }
    
    if ( VAL_TIPO_ESPIAZIONE_ISTITUTO.equals (getRequestStringParameter(CAMPO_TIPO_ESPIAZIONE))){
      lMisMod.setIstDetIdIstitutoDetenzione  ( getRequestStringParameter     ( CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) );
    }
    else {
      lMisMod.setAltroLuogoDetenzione        ( getRequestStringParameter     ( CAMPO_ALTRO_LUOGO_DETENZIONE) );
      
      String codTipoAutCompTerr = getRequestStringParameter     ( CAMPO_AUTORITA_COMPETENTE);
      if (!"-".equals(codTipoAutCompTerr)) {
        lMisMod.setAutoritaCompetente ( getRequestStringParameter ( CAMPO_AUTORITA_COMPETENTE) ); // cod Tipo Autorita
        String lDescrSede = getRequestStringParameter ( CAMPO_DESCR_AUTORITA_COMPETENTE_SEDE);
        if (!lDescrSede.equals(""))
          lMisMod.setAutoritaCompetenteSede      ( getCodComuneByDescr(lDescrSede).getCodComune() ); // é il COD_COMUNE
        lMisMod.setAutoritaCompetenteIndirizzo ( getRequestStringParameter     ( CAMPO_AUTORITA_COMPETENTE_INDIRIZZO) );
      }
    }    
    
    
    lMisMod.setTitIdTitoloCumulato         ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
    lMisMod.setFlagStato                   ( getRequestStringParameter     ( CAMPO_FLAG_STATO) );
    lMisMod.setMotivoModifica              ( getRequestStringParameter     ( ICostantiMisuraCautelareCumulo.CAMPO_MOTIVO_MODIFICA) );
    //lMisMod.setIdMisuraCautelareOrigine    ( getRequestBigDecimalParameter ( CAMPO_ID_MISURA_CAUTELARE_ORIGINE) );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lMisMod = "+lMisMod);
    
    return lMisMod;
  
  }
}