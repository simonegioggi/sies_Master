package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.NotificaCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

public class ActInserisciDecretiSospPM extends ActionModuloCumulo
    implements ICostantiStatoEsecTitoloCumulato, ICostantiStatoEsecuzioneCumulo, ICostantiNotificaCumulo
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
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella 

    IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvedimenti = null;
    
    if ("I".equals(lModalita)) {
      siesLogger.debug("Sono in INSERIMENTO");
      
      lListaProvvedimenti = this.getDatiForm("I");
      siesLogger.debug("lStatoEsecMod = "+lListaProvvedimenti);      
    
      lCtrlStatoEsec.ExInserisciSospensioniDelPM (lListaProvvedimenti);
    }
    else if ("M".equals(lModalita)) {
      siesLogger.debug("Sono in MODIFICA");

      lListaProvvedimenti = this.getDatiForm("M");
      siesLogger.debug("lStatoEsecMod = "+lListaProvvedimenti.elementAt(0));    
      
      //lCtrlStatoEsec.ExModificaStatoEsecTitoloCumulato (lListaProvvedimenti.elementAt(0));
      lCtrlStatoEsec.ExModificaStatoEsecTitoloCumulatoFull (lListaProvvedimenti.elementAt(0));
    }
    else if ("C".equals(lModalita)) {
      siesLogger.debug("Sono in CANCELLAZIONE");      
      
      BigDecimal lIdStatoEsecuzione = getRequestBigDecimalParameter ( CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      
      siesLogger.debug("lIdStatoEsecuzione = "+lIdStatoEsecuzione);

      IStatoEsecTitoloCumulato lCtrlSET = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
      lCtrlSET.ExCancellaStatoEsecTitoloCumulatoById (lIdStatoEsecuzione, null);
      
    } 
    
    String lPage = "";
//    if ("I".equals(lModalita) || "M".equals(lModalita) ){
//      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioDecretiSospPM" 
//          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
//          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
//          + "&"+ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "=" +lStatoEsecMod.getIdStatoEsecTitoloCumulato();
//    }
//    else {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaDecretiSospPM" 
          + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
          + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
//    }    
    
    return lPage;
   }  
  
  
  /**
   * 
   * @return
   */
  private Vector <StatoEsecTitoloCumulatoModel> getDatiForm (String aTipoOper) throws F3BException
  {
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvedimenti = new Vector <StatoEsecTitoloCumulatoModel> ();
    
    String lTipoSosp = getRequestStringParameter (ATTIVITA_PM_TIPO_SOSP);
    
    if (lTipoSosp.equals(ATTIVITA_PM_SOSP_C5)){
      lListaProvvedimenti = getSospensioneSimeone (aTipoOper);
    }
    else if (lTipoSosp.equals(ATTIVITA_PM_SOSP_78)){
      lListaProvvedimenti = getSospensione78 (aTipoOper);
    }
    else if (lTipoSosp.equals(ATTIVITA_PM_SOSP_199)){      
      lListaProvvedimenti = getSospensione199 (aTipoOper); 
    }   
    
    return lListaProvvedimenti;    
  }
  
  /**
   * Recupera i dati della sospensione Simeone
   * @param aTipoOper
   * @return
   * @throws F3BException
   */
  private Vector <StatoEsecTitoloCumulatoModel> getSospensioneSimeone (String aTipoOper) throws F3BException {
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvedimenti = new Vector <StatoEsecTitoloCumulatoModel> ();
    
    // I provvedimenti da recuperare sono 5:
    //  - Il provvedimento di sospensione (01-06-0061|0063|01040117)
    //  - L'istanza:  03-08-0993
    //  - Il verbale Vane ricerche: 07-17-0313
    //  - Decreto Irreperibilità: 01-02-0282
    //  - Il provvedimento di Revoca: 01-04-[0078,0079,0080]
    //    - L'ordinanza di rigetto della Sorveglianza: 01-03-[9000,9001,9002]
    //========================================================================
    // Provvedimento di sospensione
    //========================================================================
    if (   !isRequestParameterNullObj(CAMPO_COD_MOTIVO)    
        && !"-".equals(getRequestStringParameter (CAMPO_COD_MOTIVO))        
       ) 
    {
      siesLogger.debug("Recupero i dati Del Provvedimento di Concessione Sosp Simeone");

      StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
      
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaMod.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));
      
      lStaMod.setCodTipoEvento        ("01");
      lStaMod.setCodTipoProvvedimento ("06");
      lStaMod.setCodMotivo            ( getRequestStringParameter (CAMPO_COD_MOTIVO) );

      
      lStaMod.setDataEmissione             ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE
                                                                      ,CAMPO_MESE_DATA_EMISSIONE
                                                                      ,CAMPO_GIORNO_DATA_EMISSIONE) );
      lStaMod.setCodEsito                  ("-");
      lStaMod.setCodEsitoTenore            ("-");
      lStaMod.setAnnoProcedimento          (null);
      lStaMod.setProgrProcedimento         (null);
      lStaMod.setAnnoProvvedimento         (null);
      lStaMod.setProgrProvvedimento        (null);
      
      lStaMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
      lStaMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lStaMod.setFlagStato                 ( "I" );
      lStaMod.setMotivoModifica            ( null );


      
      if ("I".equals(aTipoOper)) {
        lStaMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
        lStaMod.setDataInserimento             ( DateUtils.getSysDate() );
        lStaMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
        
        // 15/03/2019 Impostazione Autorità Emittente
        if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
            lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
            lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
        } else {
            siesLogger.debug("Impostazione Autorità Emittente FALLITA ");         
        }        
      } 
      else if ("M".equals(aTipoOper)) {
      	// La Modifica cambia solo lo stato di "Estratto / Modificato".
    		String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO); 
    		if ("E".equals(flagStato)  ||  "M".equals(flagStato) )
    			lStaMod.setFlagStato ( "M" );
    		
    		if ("I".equals(flagStato) ) {
          // 15/03/2019 Impostazione Autorità Emittente
          // n.b. forzo il dato solo se in origine è stato inserito a mano
          // se estratto non tocco il dato
          if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
              lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
              lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
          } else {
              siesLogger.debug("Impostazione Autorità Emittente FALLITA ");         
          }     		
    		}
    		
        lStaMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lStaMod.setDataAggiornamento           ( DateUtils.getSysDate() );
        lStaMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      }
      siesLogger.debug("Decreto di Sospensione = "+lStaMod);
      
      
      Vector <NotificaCumuloModel> lListaNotifiche = new Vector <NotificaCumuloModel>();
      
      // Aggiungo i dati della Notifiche All'Avvocato se indicati
      if (   !isRequestParameterNullObj(CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA+"_C5_AVV")
          && !getRequestStringParameter(CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA+"_C5_AVV").equals("")
         )
      {
        NotificaCumuloModel lNotificaAvvModel = new NotificaCumuloModel();
        
        String lIdNot = getRequestStringParameter(CAMPO_ID_NOTIFICA_CUMULO+"_C5_AVV");
        if (!lIdNot.equals(""))
          lNotificaAvvModel.setIdNotificaCumulo (new BigDecimal(lIdNot));  
        
        lNotificaAvvModel.setCodTipoNotifica("N");
        lNotificaAvvModel.setCodEsito("01"); // 01 = Eseguita
    
        lNotificaAvvModel.setDataAvvenutaNotifica  ( getRequestDateParameter ( CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA+"_C5_AVV"
                                                                          , CAMPO_MESE_DATA_AVVENUTA_NOTIFICA+"_C5_AVV"
                                                                          , CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA+"_C5_AVV" ));        
        
        lNotificaAvvModel.setStatIdStatoEsecTitCum (lStaMod.getIdStatoEsecTitoloCumulato());
        
        if ("I".equals(aTipoOper)) {
          lNotificaAvvModel.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
          lNotificaAvvModel.setDataInserimento             ( DateUtils.getSysDate() );
          lNotificaAvvModel.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
        } 
        else if ("M".equals(aTipoOper)) {
          lNotificaAvvModel.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
          lNotificaAvvModel.setDataAggiornamento           ( DateUtils.getSysDate() );
          lNotificaAvvModel.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
        }        
        
        
        siesLogger.debug("Notifica Avvocato = "+lNotificaAvvModel);
        lListaNotifiche.add(lNotificaAvvModel);
      }
      
      // Aggiungo i dati della Notifiche all'Interessato se indicati
      if (   !isRequestParameterNullObj(CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA+"_C5_COND")
          && !getRequestStringParameter(CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA+"_C5_COND").equals("")
         )
      {
        NotificaCumuloModel lNotificaIntModel = new NotificaCumuloModel();
        
        String lIdNot = getRequestStringParameter(CAMPO_ID_NOTIFICA_CUMULO+"_C5_COND");
        if (!lIdNot.equals(""))
          lNotificaIntModel.setIdNotificaCumulo (new BigDecimal(lIdNot));  
 
        lNotificaIntModel.setCodTipoNotifica("E");
        lNotificaIntModel.setCodEsito("03"); // 03 = Notificato
    
        lNotificaIntModel.setDataAvvenutaNotifica  ( getRequestDateParameter ( CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA+"_C5_COND"
                                                                             , CAMPO_MESE_DATA_AVVENUTA_NOTIFICA+"_C5_COND"
                                                                             , CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA+"_C5_COND" ));        

        lNotificaIntModel.setStatIdStatoEsecTitCum (lStaMod.getIdStatoEsecTitoloCumulato());

        if ("I".equals(aTipoOper)) {
          lNotificaIntModel.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
          lNotificaIntModel.setDataInserimento             ( DateUtils.getSysDate() );
          lNotificaIntModel.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
        } 
        else if ("M".equals(aTipoOper)) {
          lNotificaIntModel.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
          lNotificaIntModel.setDataAggiornamento           ( DateUtils.getSysDate() );
          lNotificaIntModel.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
        }        
        
        siesLogger.debug("Notifica Condannato = "+lNotificaIntModel);
        lListaNotifiche.add(lNotificaIntModel);
      }      
      lStaMod.setListaNotifiche(lListaNotifiche);
      
      lListaProvvedimenti.add(lStaMod);
    }    
    
    
    //==========================================================================
    // Verbale Vane ricerche (07-17-0313)
    //==========================================================================
    if (    !isRequestParameterNullObj(CAMPO_TIPO_AUTORITA_EMITTENTE+"_VVR")   
        && !getRequestStringParameter(CAMPO_TIPO_AUTORITA_EMITTENTE+"_VVR").equals("-")
        )
    {
      siesLogger.debug("Recupero i dati Del Verbale Vane Ricerche");

      StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
      
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaMod.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));
      
      lStaMod.setCodTipoEvento        ("07");
      lStaMod.setCodTipoProvvedimento ("17");
      lStaMod.setCodMotivo            ("0313");
      
      lStaMod.setDataEmissione             ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE+"_VVR"
                                                                      ,CAMPO_MESE_DATA_EMISSIONE+"_VVR"
                                                                      ,CAMPO_GIORNO_DATA_EMISSIONE+"_VVR") );

      String lCodTipoAutEmitt = getRequestStringParameter(CAMPO_TIPO_AUTORITA_EMITTENTE+"_VVR");      
      String lDescSedeAutEmitt = getRequestStringParameter(CAMPO_DESC_LUOGO_EMITTENTE+"_VVR");
      ComuneModel lComuneSede = getCodComuneByDescr(lDescSedeAutEmitt);
      
      lStaMod.setCodAutoritaEmittente      (lCodTipoAutEmitt);
      lStaMod.setCodLuogoEmittente         (lComuneSede.getCodComune() );     
      
      lStaMod.setCodEsito                  ("-");
      lStaMod.setCodEsitoTenore            ("-");
      lStaMod.setAnnoProcedimento          (null);
      lStaMod.setProgrProcedimento         (null);
      lStaMod.setAnnoProvvedimento         (null);
      lStaMod.setProgrProvvedimento        (null);
      
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
      siesLogger.debug("Verbale Vane Ricerche = "+lStaMod); 
      
      lListaProvvedimenti.add(lStaMod);
    }
    
    //==========================================================================
    // Decreto Irreperibilità (01-02-0282)
    //==========================================================================
    if (    !isRequestParameterNullObj(CAMPO_ANNO_DATA_EMISSIONE+"_IRR")   
        && !getRequestStringParameter(CAMPO_ANNO_DATA_EMISSIONE+"_IRR").equals("")
       )
    {
      siesLogger.debug("Recupero i dati del Decreto Irreperibilità");

      StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
      
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaMod.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));
      
      lStaMod.setCodTipoEvento        ("01");
      lStaMod.setCodTipoProvvedimento ("02");
      lStaMod.setCodMotivo            ("0282");
      
      lStaMod.setDataEmissione  ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE+"_IRR"
                                                           ,CAMPO_MESE_DATA_EMISSIONE+"_IRR"
                                                           ,CAMPO_GIORNO_DATA_EMISSIONE+"_IRR") );

      lStaMod.setCodEsito                  ("-");
      lStaMod.setCodEsitoTenore            ("-");
      lStaMod.setAnnoProcedimento          (null);
      lStaMod.setProgrProcedimento         (null);
      lStaMod.setAnnoProvvedimento         (null);
      lStaMod.setProgrProvvedimento        (null);
      
      lStaMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
      lStaMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lStaMod.setFlagStato                 ( "I" );
      lStaMod.setMotivoModifica            ( null );

      

      
      if ("I".equals(aTipoOper)) {
        lStaMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
        lStaMod.setDataInserimento             ( DateUtils.getSysDate() );
        lStaMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
        
        // 15/03/2019 Impostazione Autorità Emittente
        if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
            lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
            lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
        } else {
            siesLogger.debug("Impostazione Autorità Emittente FALLITA ");         
        }       
      } 
      else if ("M".equals(aTipoOper)) {
        // La Modifica cambia solo lo stato di "Estratto / Modificato".
      	String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO); 
      	if ("E".equals(flagStato)  || 	"M".equals(flagStato) )
      		lStaMod.setFlagStato ( "M" );
      	
        if ("I".equals(flagStato)) {
          // 15/03/2019 Impostazione Autorità Emittente
          // n.b. forzo il dato solo se in origine è stato inserito a mano
          // se estratto non tocco il dato
          if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
            lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
            lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
          } else {
              siesLogger.debug("Impostazione Autorità Emittente FALLITA ");         
          } 
        }
      	
        lStaMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lStaMod.setDataAggiornamento           ( DateUtils.getSysDate() );
        lStaMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      }
    
      siesLogger.debug("Decreto Irreperibilità = "+lStaMod); 
      
      lListaProvvedimenti.add(lStaMod);
    }    
    
    //==========================================================================
    // Istanza
    //==========================================================================
    if (    !isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_ISTANZA_PRESDEP)   
        && !getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_ISTANZA_PRESDEP).equals("-")
        )
    {
      siesLogger.debug("Recupero i dati dell'Istanza");

      StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
      
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaMod.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));
      
      lStaMod.setCodTipoEvento        ("03");   // 
      lStaMod.setCodTipoProvvedimento ("08");   // Istanza
      lStaMod.setCodMotivo            ("0993"); //Istanza
      
      lStaMod.setDataEmissione  ( getRequestDateParameter ( CAMPO_ANNO_DATA_ISTANZA
                                                           ,CAMPO_MESE_DATA_ISTANZA
                                                           ,CAMPO_GIORNO_DATA_ISTANZA) );


      lStaMod.setCodContenutoIstanza(getRequestStringParameter(CAMPO_COD_CONTENUTO_ISTANZA));        
      lStaMod.setDataIstanza  ( getRequestDateParameter ( CAMPO_ANNO_DATA_ISTANZA
                                                         ,CAMPO_MESE_DATA_ISTANZA
                                                         ,CAMPO_GIORNO_DATA_ISTANZA) );
                       
      lStaMod.setFlagIstanzaPresdep(getRequestStringParameter(CAMPO_FLAG_ISTANZA_PRESDEP));         

      //Se trasmessa al TDS
      if (   !isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO)   
          && !getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO).equals("-")
          )
      {
        lStaMod.setCodStatoIstanza("03");  
        String lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
        lStaMod.setCodTipoUfficioDestinatario (lCodTipoUfficio);
        
        String lDescComune = getRequestStringParameter(CAMPO_COD_LUOGO_DESTINATARIO);
        ComuneModel lComune = getCodComuneByDescr(lDescComune);
        lStaMod.setCodLuogoDestinatario(lComune.getCodComune());
        
        String lCodUffDest = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescComune);
        lStaMod.setCodUfficioDestinatario(lCodUffDest);    
  
        lStaMod.setDataTrasmissione  ( getRequestDateParameter ( CAMPO_ANNO_DATA_TRASMISSIONE
                                                               , CAMPO_MESE_DATA_TRASMISSIONE
                                                               , CAMPO_GIORNO_DATA_TRASMISSIONE) );
      }
      
      lStaMod.setCodEsito                  ("-");
      lStaMod.setCodEsitoTenore            ("-");
      lStaMod.setAnnoProcedimento          (null);
      lStaMod.setProgrProcedimento         (null);
      lStaMod.setAnnoProvvedimento         (null);
      lStaMod.setProgrProvvedimento        (null);
      
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
      siesLogger.debug("Istanza = "+lStaMod); 
      
      lListaProvvedimenti.add(lStaMod);
    }    
    
    //==========================================================================
    // Provvedimento di Revoca
    //==========================================================================
    if (    !isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO+"_C5_REV")   
        && !getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO+"_C5_REV").equals("-")
        )
    {
      siesLogger.debug("Recupero i dati del Provvedimento di Revoca decreto di sospensione");

      StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
      
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaMod.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));
      
      lStaMod.setCodTipoEvento        ("01");
      lStaMod.setCodTipoProvvedimento ("04");
      lStaMod.setCodMotivo            (getRequestStringParameter(CAMPO_COD_MOTIVO+"_C5_REV")); 
      
      lStaMod.setDataEmissione  ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE+"_C5_REV"
                                                           ,CAMPO_MESE_DATA_EMISSIONE+"_C5_REV"
                                                           ,CAMPO_GIORNO_DATA_EMISSIONE+"_C5_REV") );

      lStaMod.setCodMotivoRevoca   (getRequestStringParameter(CAMPO_COD_MOTIVO_REVOCA));
      
      if ("0001".equals(lStaMod.getCodMotivoRevoca())){
        if (!isRequestParameterNullObj(CAMPO_COD_MOTIVO_REVOCA_PM))
          lStaMod.setCodMotivoRevocaPm (getRequestStringParameter(CAMPO_COD_MOTIVO_REVOCA_PM));
        else 
          lStaMod.setCodMotivoRevocaPm (null);
        
        if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE+"_C5_REV"))
          lStaMod.setNote (getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE+"_C5_REV"));
        else 
          lStaMod.setNote (null);
      }
      
      lStaMod.setCodEsito                  ("-");
      lStaMod.setCodEsitoTenore            ("-");
      lStaMod.setAnnoProcedimento          (null);
      lStaMod.setProgrProcedimento         (null);
      lStaMod.setAnnoProvvedimento         (null);
      lStaMod.setProgrProvvedimento        (null);
      
      lStaMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
      lStaMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lStaMod.setFlagStato                 ( "I" );
      lStaMod.setMotivoModifica            ( null );

      if ("I".equals(aTipoOper)) {
        lStaMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
        lStaMod.setDataInserimento             ( DateUtils.getSysDate() );
        lStaMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );

        // 15/03/2019 Impostazione Autorità Emittente
        if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
        	lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
        	lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
        } else {
            siesLogger.debug("Impostazione Autorità Emittente FALLITA ");        	
        }        
      } 
      else if ("M".equals(aTipoOper)) {
        // La Modifica cambia solo lo stato di "Estratto / Modificato".
      	String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO); 
      	if ("E".equals(flagStato) || "M".equals(flagStato) )
      		lStaMod.setFlagStato ( "M" );
      	
        if ("I".equals(flagStato)) {
          // 15/03/2019 Impostazione Autorità Emittente
          // n.b. forzo il dato solo se in origine è stato inserito a mano
          // se estratto non tocco il dato
          if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
            lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
            lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
          } else {
              siesLogger.debug("Impostazione Autorità Emittente FALLITA ");         
          } 
        }
      	
        lStaMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lStaMod.setDataAggiornamento           ( DateUtils.getSysDate() );
        lStaMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      }
      
           

      siesLogger.debug("Provvedimento di Revoca  = "+lStaMod); 
      
      lListaProvvedimenti.add(lStaMod);
    }  
    
    //==========================================================================
    // Ordinanza di Revoca
    //==========================================================================
    if (    !isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO+"_C5_REV_ORD")   
        && !getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO+"_C5_REV_ORD").equals("-")
        )
    {
      siesLogger.debug("Recupero i dati dell'Ordinanza di Revoca decreto di sospensione");

      StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
      
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaMod.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));
      
      lStaMod.setCodTipoEvento        ("01");
      lStaMod.setCodTipoProvvedimento ("03");
      lStaMod.setCodMotivo            (getRequestStringParameter(CAMPO_COD_MOTIVO+"_C5_REV_ORD")); 
      
      lStaMod.setFlagTipoSosp("C5");
      
      lStaMod.setDataEmissione  ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE+"_C5_REV_ORD"
                                                           ,CAMPO_MESE_DATA_EMISSIONE+"_C5_REV_ORD"
                                                           ,CAMPO_GIORNO_DATA_EMISSIONE+"_C5_REV_ORD") );
      
      lStaMod.setAnnoProcedimento   (getRequestBigDecimalParameter(CAMPO_ANNO_PROCEDIMENTO+"_C5_REV_ORD"));
      lStaMod.setProgrProcedimento  (getRequestBigDecimalParameter(CAMPO_PROGR_PROCEDIMENTO+"_C5_REV_ORD"));
      lStaMod.setAnnoProvvedimento  (getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO+"_C5_REV_ORD"));
      lStaMod.setProgrProvvedimento (getRequestBigDecimalParameter(CAMPO_PROGR_PROVVEDIMENTO+"_C5_REV_ORD"));
      
      String lTipoUfficioEmittente = getRequestStringParameter (CAMPO_TIPO_UFFICIO_EMITTENTE+"_C5_REV_ORD");
      String lDescSedeUfficioEmitt = getRequestStringParameter (CAMPO_DESC_LUOGO_EMITTENTE+"_C5_REV_ORD");
      
      UfficioModel lUffSorv = getUfficioByCodTipoUfficioDescrComune(lTipoUfficioEmittente, lDescSedeUfficioEmitt);
      
      lStaMod.setCodUfficioEmittente       (lUffSorv.getCodUfficio());
      lStaMod.setCodLuogoEmittente         (lUffSorv.getCodComune());    
      
      lStaMod.setNote               (getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE+"_C5_REV_ORD"));
      
      lStaMod.setCodEsito                  ("-");
      lStaMod.setCodEsitoTenore            ("0002"); // Rigetta
      
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
      siesLogger.debug("Ordinanza di Revoca  = "+lStaMod); 
      
      lListaProvvedimenti.add(lStaMod);
    }      
    
    return lListaProvvedimenti;
  }
  
  /**
   * Recupera i dati della sospensione DL78
   * @param aTipoOper
   * @return
   * @throws F3BException
   */
  private Vector <StatoEsecTitoloCumulatoModel> getSospensione78 (String aTipoOper) throws F3BException {
    siesLogger.debug("Sospensione 78");
    // Devo recuperare i dati di: 
    // - provvedimento di sospensione
    // - eventuale notifica alla sorveglianza
    
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvedimenti = new Vector <StatoEsecTitoloCumulatoModel> ();
    
    StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
    String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
    if (!lIdStato.equals(""))
      lStaMod.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));
    
    lStaMod.setCodTipoEvento             ("01");
    lStaMod.setCodTipoProvvedimento      ("12");
    lStaMod.setCodMotivo                 ( getRequestStringParameter (CAMPO_COD_MOTIVO) );
    if (lStaMod.getCodMotivo().equals("1024")) 
        lStaMod.setCodTipoProvvedimento("06");    
    lStaMod.setDataEmissione             ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE
                                                                    ,CAMPO_MESE_DATA_EMISSIONE
                                                                    ,CAMPO_GIORNO_DATA_EMISSIONE) );
    lStaMod.setCodEsito                  ("-");
    lStaMod.setCodEsitoTenore            ("-");
    lStaMod.setAnnoProcedimento          (null);
    lStaMod.setProgrProcedimento         (null);
    lStaMod.setAnnoProvvedimento         (null);
    lStaMod.setProgrProvvedimento        (null);
    
    lStaMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
    lStaMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
    lStaMod.setFlagStato                 ( "I" );
    lStaMod.setMotivoModifica            ( null );

    if ("I".equals(aTipoOper)) {
      lStaMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
      lStaMod.setDataInserimento             ( DateUtils.getSysDate() );
      lStaMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );

      // 15/03/2019 Impostazione Autorità Emittente
      if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
          lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
          lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
      } else {
          siesLogger.debug("Impostazione Autorità Emittente FALLITA 3 ");        	
      }

    } 
    else if ("M".equals(aTipoOper)) {
      // La Modifica cambia solo lo stato di "Estratto / Modificato".
      String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO); 
      if ("E".equals(flagStato)  ||  "M".equals(flagStato) )
  		  lStaMod.setFlagStato               ( "M" );

      if ("I".equals(flagStato)) {
        // 15/03/2019 Impostazione Autorità Emittente
        // n.b. forzo il dato solo se in origine è stato inserito a mano
        // se estratto non tocco il dato
        if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
          lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
          lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
        } else {
            siesLogger.debug("Impostazione Autorità Emittente FALLITA ");         
        } 
      }
      
      lStaMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      lStaMod.setDataAggiornamento           ( DateUtils.getSysDate() );
      lStaMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
    }
    
    siesLogger.debug("lStaMod78 = "+lStaMod);

    //Recupero la notifica alla Sorveglianza se presente
    if (   !isRequestParameterNullObj(CAMPO_COD_TIPO_UFF_NOT+"_78") 
        && !getRequestStringParameter(CAMPO_COD_TIPO_UFF_NOT+"_78").equals("-")
       )
    {
      siesLogger.debug("Recupero la NOTIFICHE 78");
      NotificaCumuloModel lNotificaModel = new NotificaCumuloModel();
      
      String lIdNot = getRequestStringParameter(CAMPO_ID_NOTIFICA_CUMULO+"_78");
      if (!lIdNot.equals(""))
        lNotificaModel.setIdNotificaCumulo (new BigDecimal(lIdNot));      
      
      lNotificaModel.setCodTipoNotifica("N");
      lNotificaModel.setDataInvio ( getRequestDateParameter ( CAMPO_ANNO_DATA_INVIO+"_78"
                                                            , CAMPO_MESE_DATA_INVIO+"_78"
                                                            , CAMPO_GIORNO_DATA_INVIO+"_78") );
      lNotificaModel.setCodEsito("-");      
      
      String lTipoUfficioNot = getRequestStringParameter(CAMPO_COD_TIPO_UFF_NOT+"_78");
      String lSedeUfficioNot = getRequestStringParameter(CAMPO_SEDE_UFF_NOT+"_78");
      UfficioModel lUffSorv = getUfficioByCodTipoUfficioDescrComune(lTipoUfficioNot, lSedeUfficioNot);

      lNotificaModel.setUffCodUfficio(lUffSorv.getCodUfficio());
      lNotificaModel.setNote (getRequestStringParameter(ICostantiNotificaCumulo.CAMPO_NOTE+"_78"));
      
      lNotificaModel.setStatIdStatoEsecTitCum (lStaMod.getIdStatoEsecTitoloCumulato());
      
      if ("I".equals(aTipoOper)) {
        lNotificaModel.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
        lNotificaModel.setDataInserimento             ( DateUtils.getSysDate() );
        lNotificaModel.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
      } 
      else if ("M".equals(aTipoOper)) {
        lNotificaModel.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lNotificaModel.setDataAggiornamento           ( DateUtils.getSysDate() );
        lNotificaModel.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      }
      
      siesLogger.debug ("Notifiche 78 = "+lNotificaModel);
      Vector <NotificaCumuloModel> lListaNotifiche = new Vector <NotificaCumuloModel>();
      lListaNotifiche.add(lNotificaModel);
      lStaMod.setListaNotifiche(lListaNotifiche);
    }
    
    lListaProvvedimenti.add(lStaMod);
    
    return lListaProvvedimenti;
  }  
  
  /**
   * Recupera i dati della sospensione L199/2010
   * @param aTipoOper
   * @return
   * @throws F3BException
   */
  private Vector <StatoEsecTitoloCumulatoModel> getSospensione199 (String aTipoOper) throws F3BException {
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvedimenti = new Vector <StatoEsecTitoloCumulatoModel> ();
    
    siesLogger.debug("Sospensione 199");
    // Devo recuperare i dati di tre provvedimenti: 
    // - provvedimento di sospensione
    // - provvedimento di revoca
    // - ordinanza di revoca
    
    //========================================================================
    // Provvedimento di sospensione
    //========================================================================
    if (   !isRequestParameterNullObj(CAMPO_COD_MOTIVO)    
        && !"-".equals(getRequestStringParameter (CAMPO_COD_MOTIVO))        
       ) 
    {
      siesLogger.debug("Recupero i dati Del Provvedimento di Concessione Sosp 199");

      StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();
      
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaMod.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));
      
      lStaMod.setCodTipoEvento             ("01");
      lStaMod.setCodMotivo                 ( getRequestStringParameter (CAMPO_COD_MOTIVO) );

      siesLogger.debug("--XX-- lStaMod.getCodMotivo() = "+lStaMod.getCodMotivo());        	
      
      if (lStaMod.getCodMotivo().equals("0499")) 
        lStaMod.setCodTipoProvvedimento("04");
      else if (lStaMod.getCodMotivo().equals("0364") || lStaMod.getCodMotivo().equals("5527") || 
    		   lStaMod.getCodMotivo().equals("5528") || lStaMod.getCodMotivo().equals("5504") || 
		       lStaMod.getCodMotivo().equals("0365") || lStaMod.getCodMotivo().equals("0498") ) {
        lStaMod.setCodTipoProvvedimento("06");
        siesLogger.debug("--XX-- lStaMod.setCodTipoProvvedimento() = "+lStaMod.getCodTipoProvvedimento());        	
      }
      
      lStaMod.setDataEmissione             ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE
                                                                      ,CAMPO_MESE_DATA_EMISSIONE
                                                                      ,CAMPO_GIORNO_DATA_EMISSIONE) );
      lStaMod.setCodEsito                  ("-");
      lStaMod.setCodEsitoTenore            ("0002"); // Rigetta
      lStaMod.setAnnoProcedimento          (null);
      lStaMod.setProgrProcedimento         (null);
      lStaMod.setAnnoProvvedimento         (null);
      lStaMod.setProgrProvvedimento        (null);
      
      lStaMod.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
      lStaMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lStaMod.setFlagStato                 ( "I" );
      lStaMod.setMotivoModifica            ( null );

      if ("I".equals(aTipoOper)) {
        lStaMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
        lStaMod.setDataInserimento             ( DateUtils.getSysDate() );
        lStaMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
        
        // 15/03/2019 Impostazione Autorità Emittente
        if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
        	lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
        	lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
        } else {
            siesLogger.debug("Impostazione Autorità Emittente FALLITA");        	
        }
      } 
      else if ("M".equals(aTipoOper)) {
        // La Modifica cambia solo lo stato di "Estratto / Modificato".
        String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO); 
        if ("E".equals(flagStato)  || "M".equals(flagStato) )
  		    lStaMod.setFlagStato ( "M" );
        
        if ("I".equals(flagStato)) {
          // 15/03/2019 Impostazione Autorità Emittente
          // n.b. forzo il dato solo se in origine è stato inserito a mano
          // se estratto non tocco il dato
          if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
            lStaMod.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
            lStaMod.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
          } else {
              siesLogger.debug("Impostazione Autorità Emittente FALLITA ");         
          } 
        }        
        
        lStaMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lStaMod.setDataAggiornamento           ( DateUtils.getSysDate() );
        lStaMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      }
      siesLogger.debug("lStaMod = "+lStaMod);
      
      lListaProvvedimenti.add(lStaMod);
    }
    
    //========================================================================
    // Provvedimento di revoca (suffisso _REV)
    //========================================================================
    if (   !isRequestParameterNullObj(CAMPO_COD_MOTIVO+"_REV")
        && !"-".equals(getRequestStringParameter (CAMPO_COD_MOTIVO+"_REV"))) 
    {
      siesLogger.debug("Recupero i dati Del Provvedimento di revoca Sosp 199");

      StatoEsecTitoloCumulatoModel lStaModRev = new StatoEsecTitoloCumulatoModel();
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaModRev.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));      
      lStaModRev.setCodTipoEvento             ("01");
      lStaModRev.setCodMotivo                 ( getRequestStringParameter (CAMPO_COD_MOTIVO+"_REV") );

      if (lStaModRev.getCodMotivo().equals("0495") || lStaModRev.getCodMotivo().equals("5530") ||
    	  lStaModRev.getCodMotivo().equals("5531") || lStaModRev.getCodMotivo().equals("5532") )
        lStaModRev.setCodTipoProvvedimento("04");
      else if (lStaModRev.getCodMotivo().equals("0496") || lStaModRev.getCodMotivo().equals("0497"))
        lStaModRev.setCodTipoProvvedimento("12");      
      
      lStaModRev.setCodMotivoRevoca           ( getRequestStringParameter (CAMPO_COD_MOTIVO_REVOCA) );

      lStaModRev.setDataEmissione             ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE+"_REV"
                                                                         ,CAMPO_MESE_DATA_EMISSIONE+"_REV"
                                                                         ,CAMPO_GIORNO_DATA_EMISSIONE+"_REV") );
      lStaModRev.setCodEsito                  ("-");
      lStaModRev.setCodEsitoTenore            ("-");
      lStaModRev.setAnnoProcedimento          (null);
      lStaModRev.setProgrProcedimento         (null);
      lStaModRev.setAnnoProvvedimento         (null);
      lStaModRev.setProgrProvvedimento        (null);
      
      lStaModRev.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
      lStaModRev.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lStaModRev.setFlagStato                 ( "I" );
      lStaModRev.setMotivoModifica            ( null );

      if ("I".equals(aTipoOper)) {
        lStaModRev.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
        lStaModRev.setDataInserimento             ( DateUtils.getSysDate() );
        lStaModRev.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
        
        // 15/03/2019 Impostazione Autorità Emittente
        if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
        	lStaModRev.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
        	lStaModRev.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
        } else {
            siesLogger.debug("Impostazione Autorità Emittente FALLITA 1 ");        	
        }
      } 
      else if ("M".equals(aTipoOper)) {
        // La Modifica cambia solo lo stato di "Estratto / Modificato".
        String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO); 
        if ("E".equals(flagStato)  || "M".equals(flagStato) )
          lStaModRev.setFlagStato ( "M" );
        
        if ("I".equals(flagStato)) {
          // 15/03/2019 Impostazione Autorità Emittente
          // n.b. forzo il dato solo se in origine è stato inserito a mano
          // se estratto non tocco il dato
          if (super.getDatiTitoloCumulato().getProcedimentoCumulato()!=null) {
            lStaModRev.setCodUfficioEmittente       (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato() );
            lStaModRev.setCodLuogoEmittente         (super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato() );
          } else {
              siesLogger.debug("Impostazione Autorità Emittente FALLITA ");         
          } 
        }        
        
        lStaModRev.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lStaModRev.setDataAggiornamento           ( DateUtils.getSysDate() );
        lStaModRev.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      }
      siesLogger.debug("lStaModRev = "+lStaModRev);
      
      lListaProvvedimenti.add (lStaModRev);
    }
    
    //========================================================================
    // Ordinanza di revoca (suffisso _SORV)
    //========================================================================
    if (   !isRequestParameterNullObj(CAMPO_COD_MOTIVO+"_SORV")
        && !"-".equals(getRequestStringParameter (CAMPO_COD_MOTIVO+"_SORV"))) 
    {
      siesLogger.debug("Recupero i dati del Decreto di revoca Sosp 199");
      
      StatoEsecTitoloCumulatoModel lStaModSorv = new StatoEsecTitoloCumulatoModel();
      
      String lIdStato = getRequestStringParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      if (!lIdStato.equals(""))
        lStaModSorv.setIdStatoEsecTitoloCumulato(new BigDecimal(lIdStato));       
      lStaModSorv.setCodTipoEvento             ("01");
      lStaModSorv.setCodTipoProvvedimento      ("03");
      lStaModSorv.setCodMotivo                 ( getRequestStringParameter (CAMPO_COD_MOTIVO+"_SORV") );

      lStaModSorv.setFlagTipoSosp("199");
      
      lStaModSorv.setDataEmissione             ( getRequestDateParameter ( CAMPO_ANNO_DATA_EMISSIONE+"_SORV"
                                                                         ,CAMPO_MESE_DATA_EMISSIONE+"_SORV"
                                                                         ,CAMPO_GIORNO_DATA_EMISSIONE+"_SORV") );
      
      String lTipoUfficioEmittente = getRequestStringParameter (CAMPO_TIPO_UFFICIO_EMITTENTE+"_199");
      String lDescSedeUfficioEmitt = getRequestStringParameter (CAMPO_DESC_LUOGO_EMITTENTE+"_199");
      
      
      UfficioModel lUffSorv = getUfficioByCodTipoUfficioDescrComune(lTipoUfficioEmittente, lDescSedeUfficioEmitt);
      
      lStaModSorv.setCodUfficioEmittente       (lUffSorv.getCodUfficio());
      lStaModSorv.setCodLuogoEmittente         (lUffSorv.getCodComune());        
      
      lStaModSorv.setCodEsito                  ("-");   
      lStaModSorv.setCodEsitoTenore            ("-");
      lStaModSorv.setAnnoProcedimento          (getRequestBigDecimalParameter (CAMPO_ANNO_PROCEDIMENTO));
      lStaModSorv.setProgrProcedimento         (getRequestBigDecimalParameter (CAMPO_PROGR_PROCEDIMENTO));
      lStaModSorv.setAnnoProvvedimento         (getRequestBigDecimalParameter (CAMPO_ANNO_PROVVEDIMENTO));
      lStaModSorv.setProgrProvvedimento        (getRequestBigDecimalParameter (CAMPO_PROGR_PROVVEDIMENTO));
      lStaModSorv.setNote                      (getRequestStringParameter (ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE+"_SORV"));
      
      
      lStaModSorv.setTitIdTitoloCumulato       ( getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) );
      lStaModSorv.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lStaModSorv.setFlagStato                 ( "I" );
      lStaModSorv.setMotivoModifica            ( null );

      if ("I".equals(aTipoOper)) {
        lStaModSorv.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
        lStaModSorv.setDataInserimento             ( DateUtils.getSysDate() );
        lStaModSorv.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );
      } 
      else if ("M".equals(aTipoOper)) {
        // La Modifica cambia solo lo stato di "Estratto / Modificato".
        String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO); 
        if ("E".equals(flagStato)  ||
        	"M".equals(flagStato) )
      		lStaModSorv.setFlagStato               ( "M" );
        lStaModSorv.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
        lStaModSorv.setDataAggiornamento           ( DateUtils.getSysDate() );
        lStaModSorv.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());
      }
      siesLogger.debug("lStaModSorv = "+lStaModSorv);
      
      lListaProvvedimenti.add (lStaModSorv);
    }           
    
    return lListaProvvedimenti;
  }   
}
