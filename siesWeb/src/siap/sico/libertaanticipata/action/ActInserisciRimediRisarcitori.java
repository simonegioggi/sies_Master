package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * Action che effettua l'inserimento dei dati del provvedimento della Sorveglianza
 * di concessione di giorni di riduzione pena e/o risarcimento secondo quanto
 * previsto dal DL92/2014
 * 
 * 
 * @author d.fiorletta
 * @since
 */
public class ActInserisciRimediRisarcitori extends ActionSiap implements ICostantiLibertaAnticipata
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    
    DatiOperazioneModel lDatiOperazione = new DatiOperazioneModel();
    lDatiOperazione.setCodOperatore (getCodUtenteConnesso());
    lDatiOperazione.setCodUfficio   (getCodUfficioUtenteConnesso());
    lDatiOperazione.setData         (DateUtils.getSysDate());
    
    //==============================================
    // Recupero i dati delle Licenze e Periodi
    //==============================================
    Vector <LicenzaPeriodiLibAnticipataModel> lLicenzeEPeriodi = null;
    
    lLicenzeEPeriodi = this.caricaLicenzePeriodi(lDatiOperazione);
    
    //SOLO PER DEBUG
//    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//    siesLogger.debug("DIV con dati = "+lLicenzeEPeriodi.size());
//    for (int i = 0; i<lLicenzeEPeriodi.size(); i++ ){
//      LicenzaPeriodiLibAnticipataModel lLicPeriodo = lLicenzeEPeriodi.elementAt(i);
//      
//      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//      siesLogger.debug("Licenza = "+ lLicPeriodo.getLicenza().toString());
//      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//      siesLogger.debug("Numero Periodi = "+lLicPeriodo.getPeriodi().length);
//      for (int j=0; j<lLicPeriodo.getPeriodi().length;j++){
//        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//        siesLogger.debug("Periodo: "+lLicPeriodo.getPeriodi()[j].toString2());
//      }      
//    }

    // Stabilisco EsitoTenore in funzione dei dati compilati
    String lCodEsistoTenore = null;    
    lCodEsistoTenore = this.getCodEsitoTenore(lLicenzeEPeriodi);
    
    //==================================
    // Recupero i dati di:
    // - EVENTO
    // - DEPOSITO_ORDINANZA_PC
    // - DEPOSITO_DECRETO
    // - TENORE
    //==================================
    EventoModel              lEventoSorveglianza = null;
    DepositoDecretoModel     lDepDecMod = null;
    DepositoOrdinanzaPcModel lDepOrdMod = null;
    TenoreModel              lTenoreModel = null;
    
    //============================================
    // Evento
    //============================================
    lEventoSorveglianza = generaEvento(lCodEsistoTenore , lDatiOperazione);
    
    //============================================
    // Deposito Decreto / Deposito Ordinanza PC
    //============================================
    if ("02".equals(lEventoSorveglianza.getCodTipoProvvedimento())){
      lDepDecMod = generaDepositoDecreto (lEventoSorveglianza);
    }
    else {
      lDepOrdMod = generaDepositoOrdinanzaPc (lEventoSorveglianza);
    }
    
    //============================================
    // Tenore
    //============================================
    lTenoreModel = generaTenore (lEventoSorveglianza);
    
    //===================================
    // Effettuo l'inserimento dei dati
    //===================================
    ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
    EventoModel lRetEve = lCtrlLib.ExInserisciRimediRisarcitoriSIEP (lEventoSorveglianza, lLicenzeEPeriodi, lDepDecMod, lDepOrdMod, lTenoreModel);

    
    String lPage = "";

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD 
         + "=siap.sico.libertaanticipata.action.ActDettaglioRimediRisarcitori&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lRetEve.getIdEvento();

    return lPage;

//    if (1==1)
//      throw new F3BException(F3BException.USER_MESSAGE, " FATTO ");


  }

  /**
   * Carica dalla form i dati relativi a LICENZA_LIBANTICIPATA e PERIODO_LIBANTICIPATA
   * 
   * @return Vector <LicenzaPeriodiLibAnticipataModel> con LicenzaLibAnticipataModel e array con i periodi
   */
  private Vector <LicenzaPeriodiLibAnticipataModel> caricaLicenzePeriodi(DatiOperazioneModel aDatiOperazione) throws Exception
  {
    Vector <LicenzaPeriodiLibAnticipataModel> lLicenzeEPeriodi = new Vector <LicenzaPeriodiLibAnticipataModel>();
    
    //String [] lNomiDivPeriodi = {"DIV_RD_C","DIV_SL_C","DIV_RD_R","DIV_RD_I","DIV_RD_N"};
    
    //=======================================================
    // Carico i dati dei giorni concessi se presenti: RD_C
    //=======================================================
    if (   !this.isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
        && getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU).length()>0
       )
    { 
      LicenzaPeriodiLibAnticipataModel lLicPeriodiModel = new LicenzaPeriodiLibAnticipataModel();
      
      lLicPeriodiModel.setLicenza ( recuperaLicenza("DIV_RD_C", aDatiOperazione) );
      lLicPeriodiModel.setPeriodi ( recuperaPeriodi("DIV_RD_C", aDatiOperazione) );
      
      lLicenzeEPeriodi.add (lLicPeriodiModel);
    }
    
    //=======================================================
    // Carico i dati delle Somme Liquidate se presenti: SL_C
    //=======================================================
    if (   (   !this.isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU)
            && getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU).length()>0
           )
        || (   !this.isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU)
            && getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU).length()>0
           )
       )
    { 
      LicenzaPeriodiLibAnticipataModel lLicPeriodiModel = new LicenzaPeriodiLibAnticipataModel();
      
      lLicPeriodiModel.setLicenza ( recuperaLicenza("DIV_SL_C", aDatiOperazione) );
      lLicPeriodiModel.setPeriodi ( recuperaPeriodi("DIV_SL_C", aDatiOperazione) );
      
      lLicenzeEPeriodi.add (lLicPeriodiModel);
    }
    
    //=======================================================
    // Carico i dati dei Periodi Rigettati se presenti: RD_R
    //=======================================================
    { 
      LicenzaPeriodiLibAnticipataModel lLicPeriodiModel = new LicenzaPeriodiLibAnticipataModel();
      
      lLicPeriodiModel.setLicenza ( recuperaLicenza("DIV_RD_R", aDatiOperazione) );
      lLicPeriodiModel.setPeriodi ( recuperaPeriodi("DIV_RD_R", aDatiOperazione) );
      
      if (lLicPeriodiModel.getPeriodi()!=null && lLicPeriodiModel.getPeriodi().length>0)
        lLicenzeEPeriodi.add (lLicPeriodiModel);
    }
    
    //=======================================================
    // Carico i dati dei Periodi Inammissibili se presenti: RD_I
    //=======================================================
    { 
      LicenzaPeriodiLibAnticipataModel lLicPeriodiModel = new LicenzaPeriodiLibAnticipataModel();
      
      lLicPeriodiModel.setLicenza ( recuperaLicenza("DIV_RD_I", aDatiOperazione) );
      lLicPeriodiModel.setPeriodi ( recuperaPeriodi("DIV_RD_I", aDatiOperazione) );
      
      if (lLicPeriodiModel.getPeriodi()!=null && lLicPeriodiModel.getPeriodi().length>0)
        lLicenzeEPeriodi.add (lLicPeriodiModel);
    }
    
    //=======================================================
    // Carico i dati dei Periodi NLP/NDP se presenti: RD_N
    //=======================================================
    { 
      LicenzaPeriodiLibAnticipataModel lLicPeriodiModel = new LicenzaPeriodiLibAnticipataModel();
      
      lLicPeriodiModel.setLicenza ( recuperaLicenza("DIV_RD_N", aDatiOperazione) );
      lLicPeriodiModel.setPeriodi ( recuperaPeriodi("DIV_RD_N", aDatiOperazione) );
      
      if (lLicPeriodiModel.getPeriodi()!=null && lLicPeriodiModel.getPeriodi().length>0)
        lLicenzeEPeriodi.add (lLicPeriodiModel);
    }
    
    
    return lLicenzeEPeriodi;
  }
  
  /**
   * Genera il record LicenzaLibAnticipataModel relativo ai dati della DIV
   * passata in input
   * 
   * @param aNomeDiv
   * @return
   * @throws F3BException
   */
  private LicenzaLibAnticipataModel recuperaLicenza (String aNomeDiv, DatiOperazioneModel aDatiOperazione)  throws F3BException
  {
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.debug(" L.A. Normale - generaLicenza  Inizio");
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    LicenzaLibAnticipataModel lLicenza = new LicenzaLibAnticipataModel();
 
    if (aNomeDiv.equals("DIV_SL_C"))
      lLicenza.setCodTipoLicenza("SL"); // Somma Liquidata
    else 
      lLicenza.setCodTipoLicenza("RD"); // Riduzione
      
    // flag concesso
    lLicenza.setFlagConcesso(getFlagConcesso(aNomeDiv));   


    if (aNomeDiv.equals("DIV_RD_C")){
      // Recupero i giorni di riduzione concessi
      lLicenza.setNumeroGiorni (this.getGiorniRiduzione());
    }
    else if (aNomeDiv.equals("DIV_SL_C")){
      // recupero le somme liquidate
      lLicenza.setSommaRisarcDanni (this.getSommaLiquidata());
    }
    else {
      // non ci sono quantità da recuperare per i periodi rigettati
    }

    // Si aggiungono gli altri dati dell'ordinanza/decreto anche sul record LA
    lLicenza.setAnnoSius        (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS));
    lLicenza.setNumeroSius      (getRequestStringParameter    (ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS));
    lLicenza.setAnnoOrdinanza   (getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
    lLicenza.setNumeroOrdinanza (getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
   
    // COD_UFFICIO_EMITTENTE
    String lCodTipoUff  = getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE);    
    String lDescrComune = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE);
    String lCodUfficio  = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescrComune);
    lLicenza.setCodUfficioEmittente(lCodUfficio);
   
    // COD_LUOGO_EMITTENTE
    ComuneModel lComune = getCodComuneByDescr(lDescrComune);
    lLicenza.setCodLuogoEmittente(lComune.getCodComune());
   
    // DATA_EMISSIONE_ORDINANZA
    if (   !isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE)
        && getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE).length()>0
       )
    {
      lLicenza.setDataEmissioneOrdinanza(getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
                                                                  ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
                                                                  ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE)
                                                                 );
    }
    //========================================================================
    //
    //========================================================================
    lLicenza.setFasSieIdFascicoloSiep   (lFascicoloModel.getIdFascicoloSiep());
    
    lLicenza.setCodOperatoreInserimento (aDatiOperazione.getCodOperatore());
    lLicenza.setCodUfficioInserimento   (aDatiOperazione.getCodUfficio());
    lLicenza.setDataInserimento         (aDatiOperazione.getData());

    return lLicenza;
  }
 
  /**
   * Recupera dalla form in dati dei periodi per il singolo computo
   * @param aNomeDiv
   * @return
   * @throws Exception
   */
  private PeriodoLibAnticipataModel[] recuperaPeriodi (String aNomeDiv, DatiOperazioneModel aDatiOperazione)  throws Exception  
  {
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.debug("recuperaPeriodi per Nome Div = "+aNomeDiv);
    
    int lNumPeriodi = getRequestIntParameter("NumPeriodi");
  

    Vector <PeriodoLibAnticipataModel> lVectPeriodi = new Vector <PeriodoLibAnticipataModel>();
    
    // Recupero i periodi inseriti in form
    for (int i=0;i<lNumPeriodi;i++) {
      String lNomeCampoGiornoDa = ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO+"_"+aNomeDiv+"_"+i;
      
      if (   !isRequestParameterNullObj(lNomeCampoGiornoDa)
          && getRequestStringParameter(lNomeCampoGiornoDa).length()>0
         )
      {
        PeriodoLibAnticipataModel lPeriodoLib = new PeriodoLibAnticipataModel();
        Date lDataInizio = getRequestDateParameter( ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO+"_"+aNomeDiv+"_"+i,
                                                    ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO+"_"+aNomeDiv+"_"+i,
                                                    ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO+"_"+aNomeDiv+"_"+i);
           
        Date lDataFine = getRequestDateParameter( ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE+"_"+aNomeDiv+"_"+i,
                                                  ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE+"_"+aNomeDiv+"_"+i,
                                                  ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE+"_"+aNomeDiv+"_"+i);
        //
        lPeriodoLib.setDataInizio    (lDataInizio);
        lPeriodoLib.setDataFine      (lDataFine);
        lPeriodoLib.setFlagConcesso  (getFlagConcesso(aNomeDiv));
        
        lPeriodoLib.setCodOperatoreInserimento (aDatiOperazione.getCodOperatore());
        lPeriodoLib.setCodUfficioInserimento   (aDatiOperazione.getCodUfficio());
        lPeriodoLib.setDataInserimento         (aDatiOperazione.getData());
        
        lVectPeriodi.add(lPeriodoLib);
      }
    }
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Periodi presenti = "+lVectPeriodi.size());
    
    // Riverso i periodi nell'array
    PeriodoLibAnticipataModel[] lPeriodi = new PeriodoLibAnticipataModel[lVectPeriodi.size()];
    lPeriodi = lVectPeriodi.toArray(lPeriodi);
    
   
    return lPeriodi;
  }
  
  /**
   * Costruisce l'evento SIUS (decreto/ordinanza) prelevando i dati dalla form
   * @param aEsitoTenore
   * @return
   * @throws F3BException
   */
  private EventoModel generaEvento (String aEsitoTenore, DatiOperazioneModel aDatiOperazione) throws F3BException
  {
    EventoModel lEvento = new EventoModel();

    lEvento.setCodTipoEvento("01"); // 01 = Provvedimento.
    lEvento.setCodTipoProvvedimento (getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO)); // 02-03
    lEvento.setCodMotivo("2790");  // 2790 = Riduzione pena da espiare/risarcimento del danno (art. 35 ter O.P.) 

    // Cod. Ufficio Emittente
    String lCodTipoUff = getRequestStringParameter (ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE);
    String lDescComune = getRequestStringParameter (ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE);

    String lCodUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescComune);

    // Cod. Luogo Emittente
    lEvento.setCodUfficioEmittente(lCodUffEmittente);

    ComuneModel lComune = getCodComuneByDescr(lDescComune);
    lEvento.setCodLuogoEmittente(lComune.getCodComune());

    // Cod. Esito
    lEvento.setCodEsito(aEsitoTenore); 

    // Data Emissione
    Date lDataEmissione = getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
                                                   ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
                                                   ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
    lEvento.setDataEmissione (lDataEmissione);

    // Data Ricezione Atti
    Date lDataRicezione = getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
                                                   ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
                                                   ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI);
    lEvento.setDataRicezioneAtti (lDataRicezione);

    // Data Trasmissione Atti (n.b. valorizzata per simulare il deposito, altrimenti non esce dal seleziona dalla lista)
    lEvento.setDataTrasmissioneAtti (lDataRicezione);
    

    // Collego l'evento al fascicolo
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    lEvento.setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

    
    // Se sono assenti i giorni di Riduzione ed è quindi presente la sola
    // Somma Liquidata procedo a validare subito il provvedimento SIUS 
    // perchè non sarà possibile emettere ulteriori provvedimenti SIEP
    // Se validato il provvedimento esce sulla stampa Stato Esecuzione
    // 0392 - Liquida somma di denaro per risarcimento del danno  
    if ("0392".equals(aEsitoTenore)){
      lEvento.setFlagDocumentoRegistrato("S");
    }
    
    lEvento.setCodUfficioInserimento   ( aDatiOperazione.getCodUfficio() );
    lEvento.setCodOperatoreInserimento ( aDatiOperazione.getCodOperatore());
    lEvento.setDataInserimento         ( aDatiOperazione.getData());

    // campi a '-' per le join con la CG_REF_CODES
    lEvento.setCodLuogoDestinatario       ("-");
    lEvento.setCodTipoUfficioDestinatario ("-");
    lEvento.setCodUfficioDestinatario     ("-");
    
    lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEvento.setFlagStampaSiep("S");
    lEvento.setFlagVideoSiep("S");


    return lEvento;
  }
  
  /**
   * Recupera dalla FORM i dati del Deposito Decreto e li restituisce nel Model
   * completandolo con i dati passati in input.
   *
   * @param aUfficioEmittente -
   * @return
   * @throws F3BException
   */
  private DepositoDecretoModel generaDepositoDecreto (EventoModel aEventoModel) throws F3BException
  {
    DepositoDecretoModel lDepDecMod = new DepositoDecretoModel();
    
   
    // Anno e Numero Provvedimento della FORM
    lDepDecMod.setAnnoS72 (getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
    lDepDecMod.setNumS72  (getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
    
    lDepDecMod.setDataEmissione (aEventoModel.getDataEmissione());
    
    lDepDecMod.setCodUfficioInserimento   (aEventoModel.getCodUfficioInserimento());
    lDepDecMod.setCodOperatoreInserimento (aEventoModel.getCodOperatoreInserimento());
    lDepDecMod.setDataInserimento         (aEventoModel.getDataInserimento());
    
    lDepDecMod.setCodUfficioCompetente    (aEventoModel.getCodUfficioEmittente());  
    
    lDepDecMod.setCodTipoDecreto("VC");
    lDepDecMod.setNumeroGiorniRiduzionePena (this.getGiorniRiduzione());
    lDepDecMod.setSommaRisarcimentoDanni    (this.getSommaLiquidata());

    return lDepDecMod;
  }

  /**
   * Recupera dalla FORM i dati del Deposito Ordinanza PC e li restituisce nel Model
   * completandolo con i dati passati in input.
   *  
   * @param aUfficioEmittente
   * @return
   * @throws F3BException
   */
  private DepositoOrdinanzaPcModel generaDepositoOrdinanzaPc (EventoModel aEventoModel) throws F3BException
  {
    DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
    
    // Anno e Numero Provvedimento della FORM
    lDepOrdMod.setAnnoS3 (getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
    lDepOrdMod.setNumS3  (getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));

    // n.b. su DEPOSITO ORDINANZA la data emissione è memorizzata su DATA_CAMERA_CONSIGLIO
    lDepOrdMod.setDataCameraConsiglio (aEventoModel.getDataEmissione());
    lDepOrdMod.setCodTipoOrdinanza ("VC");
    
    lDepOrdMod.setCodUfficioInserimento   (aEventoModel.getCodUfficioInserimento());
    lDepOrdMod.setCodOperatoreInserimento (aEventoModel.getCodOperatoreInserimento());
    lDepOrdMod.setDataInserimento         (aEventoModel.getDataInserimento());
    
    lDepOrdMod.setCodUfficioMagistratoComp (aEventoModel.getCodUfficioEmittente());
    
    lDepOrdMod.setNumGiorniRiduzionePena (this.getGiorniRiduzione());
    lDepOrdMod.setSommaRisarcimento      (this.getSommaLiquidata());

    return lDepOrdMod;
  }
  
  /**
   * Costruisce i il model TenoreModel
   * @return
   */
  private TenoreModel generaTenore (EventoModel aEventoModel) {
    TenoreModel lTenoreModel = new TenoreModel();
    
    lTenoreModel.setCodOggettoTenore (aEventoModel.getCodMotivo()); 
    lTenoreModel.setCodEsitoTenore   (aEventoModel.getCodEsito());
    lTenoreModel.setProgrTenore      (new BigDecimal(1));
    lTenoreModel.setData             (aEventoModel.getDataEmissione());
    
    lTenoreModel.setCodUfficioInserimento   ( aEventoModel.getCodUfficioInserimento() ); 
    lTenoreModel.setCodOperatoreInserimento ( aEventoModel.getCodOperatoreInserimento() );    
    lTenoreModel.setDataInserimento         ( aEventoModel.getDataInserimento() );
    
    // Per le join
    lTenoreModel.setCodMagistrato       ( "-" );
    lTenoreModel.setCodDettaglioOggetto ( "-" );
    
    return lTenoreModel;
  }

  /**
   * Retituisce il cod esito tenore a partire dai campi compilati nella form
   * - 0390 = Dispone riduzione pena detentiva
   * - 0391 = Dispone riduzione pena detentiva e liquida somma di denaro per risarcimento del danno
   * - 0392 = Liquida somma di denaro per risarcimento del danno
   * 
   * n.b. non è possibile inserire solo "Periodi NON concessi"  
   * 
   * @param aLicenzeEPeriodi
   * @return codEsitoTenore
   */
  private String getCodEsitoTenore (Vector <LicenzaPeriodiLibAnticipataModel> aLicenzeEPeriodi) throws Exception
  {
    String lCodEsistoTenore = null;
    
    boolean isRiduzionePena = false;
    boolean isSommaLiquidata = false;
    
    for (int i = 0; i<aLicenzeEPeriodi.size(); i++ ){
      LicenzaPeriodiLibAnticipataModel lLicPeriodo = aLicenzeEPeriodi.elementAt(i);
      LicenzaLibAnticipataModel lLicenza  = lLicPeriodo.getLicenza();
      if ("RD".equals(lLicenza.getCodTipoLicenza()) && "C".equals(lLicenza.getFlagConcesso()))
        isRiduzionePena = true;
      else if ("SL".equals(lLicenza.getCodTipoLicenza()) && "C".equals(lLicenza.getFlagConcesso()))
        isSommaLiquidata = true;      
    }
    
    if (isRiduzionePena && isSommaLiquidata)
      lCodEsistoTenore = "0391"; // Dispone riduzione pena detentiva e liquida somma di denaro per risarcimento del danno 
    else if (isRiduzionePena)
      lCodEsistoTenore = "0390"; // Dispone riduzione pena detentiva
    else if (isSommaLiquidata)
      lCodEsistoTenore = "0392"; // Liquida somma di denaro per risarcimento del danno 
    else  
      throw new F3BException(F3BException.USER_MESSAGE, "Non sono stati specificati ne i Giorni di Riduzione Pena ne la Somma Liquidata");
    
    return lCodEsistoTenore;
  }

  /**
   * FLAG_CONCESSO = C per RD_C e SL_C ovvero Giorni Riduzione e Somme Liquidate
   *                 R per i periodi Rigettati, I = Inammissibili, N = NLP/NDP
   *
   * @param aNomeDiv
   * @return
   */
  private String getFlagConcesso (String aNomeDiv) {
    if (aNomeDiv.equals("DIV_RD_C") || aNomeDiv.equals("DIV_SL_C")) {
      return "C";
    }
    else if (aNomeDiv.equals("DIV_RD_R")){
      return "R";
    }
    else if (aNomeDiv.equals("DIV_RD_I")){
      return "I";
    }
    else if (aNomeDiv.equals("DIV_RD_N")){
      return "N";
    }
    else
      return null;
  }


  /**
   * Recupera dalla form la soma liquidata, parte intera + paret decimale
   * 
   * @return la somma liquidata se indicata in form, null altrimenti
   * @throws Exception
   */
  private BigDecimal getSommaLiquidata() throws F3BException {
    String lParteIntera   = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU);
    String lParteDecimale = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU);
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

  /**
   * Restituisce i gg di riduzione pena se indicati in maschera e > 0
   * @return gg di riduzione se indicati e > 0, null altrimenti
   * @throws F3BException
   */
  private BigDecimal getGiorniRiduzione() throws F3BException 
  {
    BigDecimal lGiorniRiduzione = null;
    
    if (   !this.isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
        && getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU).length()>0
       )
    { 
      lGiorniRiduzione = getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU);
    }

    return lGiorniRiduzione;   
    
  }
  
}