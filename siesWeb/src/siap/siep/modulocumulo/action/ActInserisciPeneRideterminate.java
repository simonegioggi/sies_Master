package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Action per l'inserimento e Modifica dei dati della pena 
 * @author d.fiorletta
 *
 */
public class ActInserisciPeneRideterminate extends ActionModuloCumulo implements ICostantiPenaRideterminataCumulo, ICostantiDatiFinaliUlterioriSanzioni
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws F3BException
  {
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    super.getDatiIstruttoria();
    
    
   // String lModalita = getRequestStringParameter("modalita");
    
    //==========================================================================
    // Recupero i dati dalla FORM
    //==========================================================================
    PenaRideterminataCumuloModel lPenaRideterminata = this.getDatiPenaRideterminata();
    
    Vector <DatiFinaliUlterioriSanzioniModel> lListaUlterioriSanz = this.getDatiUlterioriSanzioni();
    
    
    DatiFinaliCumuloAggregatoModel lAggregatoOLD = super.getDatiFinaliCumuloAggregato();
    DatiFinaliCumuloAggregatoModel lAggregatoNEW = new DatiFinaliCumuloAggregatoModel();
    lAggregatoNEW.setPenaRideterminataCumulo(lPenaRideterminata);
    lAggregatoNEW.setListaDatiFinaliUlterioriSanzioni(lListaUlterioriSanz);
    
    // Verifico il tipo di operazioni da effettuare sui dati
    this.verificaOperazioniCRUD (lAggregatoOLD, lAggregatoNEW);
    
    // Solo debug
    for (int i = 0; i<lListaUlterioriSanz.size(); i++) {
      DatiFinaliUlterioriSanzioniModel lUlterioreSanzione = lListaUlterioriSanz.elementAt(i);
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("TipoOper: "+lUlterioreSanzione.getCodTipoUlterioreSanzione() +" - "+lUlterioreSanzione.getTipoOperazioneCRUD());
    }
    
    
    //==========================================================================
    // Effettuo l'inserimento/modifica
    //==========================================================================
    IDatiFinaliCumulo lCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
    lCtrl.ExCRUDPeneRideterminateUlterioriSanzioniCumulo(lAggregatoNEW);

    //==========================================================================
    // Invoco la Action di Dettaglio
    //==========================================================================
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioPeneRideterminate&" 
            + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

    return lPage;   
    
  }
  
  
  /**
   * 
   * @return
   * @throws F3BException
   */
  private PenaRideterminataCumuloModel getDatiPenaRideterminata() throws F3BException
  {
    PenaRideterminataCumuloModel lPenMod = new PenaRideterminataCumuloModel();   

    CalendarUtil lCalendarUtil = new CalendarUtil();
    
    lPenMod.setIdPenaRideterminataCumulo ( getRequestBigDecimalParameter ( CAMPO_ID_PENA_RIDETERMINATA_CUMULO) );
    
    lPenMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
    lPenMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    lPenMod.setFlagPenaResiduaCumulo     ("N" );
    
    
    // Reclusione
    if (!isRequestParameterNullObj(CAMPO_CHECK_RECLUSIONE_PD) && getRequestStringParameter(CAMPO_CHECK_RECLUSIONE_PD).equals(CAMPO_CHECK_RECLUSIONE_PD_VAL))
    {
      CalendarModel lCalModel = new CalendarModel();
      lCalModel.setNumAnni   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_RECLUSIONE));
      lCalModel.setNumMesi   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_RECLUSIONE) );
      lCalModel.setNumGiorni ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_RECLUSIONE) );
      
      // Normalizzo i quantum
      lCalModel = lCalendarUtil.ricalcolaGAM (lCalModel);
      
      lPenMod.setNumAnniReclusione    ( lCalModel.getNumAnni()  ==0?null:new BigDecimal(lCalModel.getNumAnni()) );
      lPenMod.setNumMesiReclusione    ( lCalModel.getNumMesi()  ==0?null:new BigDecimal(lCalModel.getNumMesi()) );
      lPenMod.setNumGiorniReclusione  ( lCalModel.getNumGiorni()==0?null:new BigDecimal(lCalModel.getNumGiorni()) );
    }
    
    // Multa
    if (!isRequestParameterNullObj(CAMPO_CHECK_MULTA_PD) && getRequestStringParameter(CAMPO_CHECK_MULTA_PD).equals(CAMPO_CHECK_MULTA_PD_VAL))
    {
      if (  (getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") != null && !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")).equals("")) 
          ||(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC") != null && !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")).equals("")) ) 
      { 
        lPenMod.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") + "."+getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC"))); 
      } 
    }
    
    // Arresto
    if (!isRequestParameterNullObj(CAMPO_CHECK_ARRESTO_PD) && getRequestStringParameter(CAMPO_CHECK_ARRESTO_PD).equals(CAMPO_CHECK_ARRESTO_PD_VAL))
    {
      CalendarModel lCalModel = new CalendarModel();
      lCalModel.setNumAnni   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_ARRESTO));
      lCalModel.setNumMesi   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_ARRESTO) );
      lCalModel.setNumGiorni ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_ARRESTO) );      
      
      lCalModel = lCalendarUtil.ricalcolaGAM (lCalModel);
      
      lPenMod.setNumAnniArresto    ( lCalModel.getNumAnni()  ==0?null:new BigDecimal(lCalModel.getNumAnni()) );
      lPenMod.setNumMesiArresto    ( lCalModel.getNumMesi()  ==0?null:new BigDecimal(lCalModel.getNumMesi()) );
      lPenMod.setNumGiorniArresto  ( lCalModel.getNumGiorni()==0?null:new BigDecimal(lCalModel.getNumGiorni()) );
    }
    
    // Ammenda
    if (!isRequestParameterNullObj(CAMPO_CHECK_AMMENDA_PD) && getRequestStringParameter(CAMPO_CHECK_AMMENDA_PD).equals(CAMPO_CHECK_AMMENDA_PD_VAL))
    {
      if (  (getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") != null && !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT")).equals("")) 
          ||(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC") != null && !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC")).equals("")) ) 
      { 
        lPenMod.setImportoAmmenda(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") + "."+getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC"))); 
      } 
    } 
    
    // Ergastolo
    if (!isRequestParameterNullObj(CAMPO_CHECK_ERGASTOLO_PD) && getRequestStringParameter(CAMPO_CHECK_ERGASTOLO_PD).equals(CAMPO_CHECK_ERGASTOLO_PD_VAL))
    {    
      CalendarModel lCalModel = new CalendarModel();
      lCalModel.setNumAnni   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO));
      lCalModel.setNumMesi   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_ISOLAMENTO_DIURNO) );
      lCalModel.setNumGiorni ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO) );      
      
      lCalModel = lCalendarUtil.ricalcolaGAM (lCalModel);
      
      lPenMod.setNumAnniIsolamentoDiurno    ( lCalModel.getNumAnni()  ==0?null:new BigDecimal(lCalModel.getNumAnni()) );
      lPenMod.setNumMesiIsolamentoDiurno    ( lCalModel.getNumMesi()  ==0?null:new BigDecimal(lCalModel.getNumMesi()) );
      lPenMod.setNumGiorniIsolamentoDiurno  ( lCalModel.getNumGiorni()==0?null:new BigDecimal(lCalModel.getNumGiorni()) );
       
      if (lPenMod.getNumAnniIsolamentoDiurno()!=null || lPenMod.getNumMesiIsolamentoDiurno()!=null || lPenMod.getNumGiorniIsolamentoDiurno()!=null){
        lPenMod.setCodTipoPenaDetentiva ("04"); // Ergastolo con Isolamento Diurno
      } else {
        lPenMod.setCodTipoPenaDetentiva ("03"); // Ergastolo
      }
    }    
    
    // Recupero i dati della Liberazione anticipata se presenti
    if (!isRequestParameterNullObj(CAMPO_CHECK_LIBANT) && getRequestStringParameter(CAMPO_CHECK_LIBANT).equals(CAMPO_CHECK_LIBANT_VAL)){
      lPenMod.setNumeroGiorniLA            ( getRequestBigDecimalParameter ( CAMPO_NUMERO_GIORNI_LA) );
      lPenMod.setNumeroGiorniLS            ( getRequestBigDecimalParameter ( CAMPO_NUMERO_GIORNI_LS) );
      lPenMod.setNumeroGiorniLI            ( getRequestBigDecimalParameter ( CAMPO_NUMERO_GIORNI_LI) );
      lPenMod.setNumeroGiorniRiduzione     ( getRequestBigDecimalParameter ( CAMPO_NUMERO_GIORNI_RIDUZIONE) );
      lPenMod.setNumeroGiorniScomputo      ( getRequestBigDecimalParameter ( CAMPO_NUMERO_GIORNI_SCOMPUTO) );
    }
    
    //if (lPenMod.isValorizzato())    
      return lPenMod;
    //else 
    //  return null;
  }
  
  
  /**
   * Metodo che recupera dalla form i dati delle ulteriori sanzioni
   * @return
   * @throws F3BException
   */
  private Vector <DatiFinaliUlterioriSanzioniModel> getDatiUlterioriSanzioni() throws F3BException
  {
    Vector <DatiFinaliUlterioriSanzioniModel> lListaUlterioriSanzioni = new Vector <DatiFinaliUlterioriSanzioniModel>();
    
    //==========================================================================
    // Sanzione Sostitutiva - Semidetenzione
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_SEMIDETENZIONE_SS)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzione Sostitutiva - Semidetenzione");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_SEMIDETENZIONE_SS) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_SEMIDETENZIONE_SS) );
      lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_SEMIDETENZIONE_SS) );
      lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_SEMIDETENZIONE_SS) );
      lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_SEMIDETENZIONE_SS) );

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }
    
    //==========================================================================
    // Sanzione Sostitutiva - Liberta Controllata
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_LIBERTACTRL_SS)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzione Sostitutiva - Liberta Controllata");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_LIBERTCONTR_SS) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_LIBERTACTRL_SS) );
      lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_LIBERTACTRL_SS) );
      lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_LIBERTACTRL_SS) );
      lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_LIBERTACTRL_SS) );

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }  
    
    //==========================================================================
    // Sanzione Sostitutiva - Pena Pecuniaria - Multa
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_MULTA_SS)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzione Sostitutiva - Pena Pecuniaria - Multa");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_MULTA_SS) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_MULTA_SS) );
      if (  (getRequestStringParameter(CAMPO_INTERO_MULTA_APPLICATA_SS) != null && !(getRequestStringParameter(CAMPO_INTERO_MULTA_APPLICATA_SS)).equals("")) 
          ||(getRequestStringParameter(CAMPO_DECIMALE_MULTA_APPLICATA_SS) != null && !(getRequestStringParameter(CAMPO_DECIMALE_MULTA_APPLICATA_SS)).equals("")) ) 
      { 
        lDatMod.setMulta(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_MULTA_APPLICATA_SS) + "."+getRequestStringParameter(CAMPO_DECIMALE_MULTA_APPLICATA_SS))); 
      } 

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }
    
    //==========================================================================
    // Sanzione Sostitutiva - Pena Pecuniaria - Ammenda
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_AMMENDA_SS)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzione Sostitutiva - Pena Pecuniaria - Ammenda");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_AMMENDA_SS) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_AMMENDA_SS) );
      if (  (getRequestStringParameter(CAMPO_INTERO_AMMENDA_APPLICATA_SS) != null && !(getRequestStringParameter(CAMPO_INTERO_AMMENDA_APPLICATA_SS)).equals("")) 
          ||(getRequestStringParameter(CAMPO_DECIMALE_AMMENDA_APPLICATA_SS) != null && !(getRequestStringParameter(CAMPO_DECIMALE_AMMENDA_APPLICATA_SS)).equals("")) ) 
      { 
        lDatMod.setAmmenda(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_AMMENDA_APPLICATA_SS) + "."+getRequestStringParameter(CAMPO_DECIMALE_AMMENDA_APPLICATA_SS))); 
      } 

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }
    
    
    //==========================================================================
    // Sanzione Sostitutiva - Espulsione
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_ESPULSIONE_STATO_SS)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzione Sostitutiva - Espulsione");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();
      
      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_ESPULSIONE_SS) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_ESPULSIONE_STATO_SS) );
      
      if (!isRequestParameterNullObj(CAMPO_CHECK_ESPULSIONE_PERPETUA_SS))
        lDatMod.setFlagEspulPerp ( getRequestStringParameter ( CAMPO_CHECK_ESPULSIONE_PERPETUA_SS) );
      else 
        lDatMod.setFlagEspulPerp("T");
      
      if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_ESPULSIONE_SS)) {
        lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_ESPULSIONE_SS) );
        lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_ESPULSIONE_SS) );
        lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_ESPULSIONE_SS) ); 
      }
      
      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }    
    
    //==========================================================================
    // Sanzione Sostitutiva - Lavoro Pubblica Utilita
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_LPU_SS)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzione Sostitutiva - Lavoro Pubblica Utilità");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();
      
      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_LPU_SS) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_LPU_SS) );
      
      lDatMod.setCodTipoLpu  ( getRequestStringParameter     ( CAMPO_COD_TIPO_LPU_SS) );
     
      lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_LPU_SS) );
      lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_LPU_SS) );
      lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_LPU_SS) ); 
      
      lDatMod.setNumOreTot                 ( getRequestBigDecimalParameter ( CAMPO_NUM_ORE_TOT) ); 
      lDatMod.setNumOreSett                ( getRequestBigDecimalParameter ( CAMPO_NUM_ORE_SETT) ); 
      lDatMod.setCodFreqSett               ( getRequestBigDecimalParameter ( CAMPO_COD_FREQ_SETT));      

      if (lDatMod.getCodFreqSett().compareTo(new BigDecimal(VAL_COD_FREQ_DETER))==0)
        lDatMod.setListaOrariLPU (caricaTipologiaOrario());
      
      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }
    
    //==========================================================================
    // Pena da conversione Pena Pecuniaria - Lavoro Sostitutivo 
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_LAVSOST_PP)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Pena da conversione Pena Pecuniaria - Lavoro Sostitutivo");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_LAVSOST_PP) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_LAVSOST_PP) );
      lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_LAVSOST_PP) );
      lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_LAVSOST_PP) );
      lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_LAVSOST_PP) );

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }
    
    //==========================================================================
    // Pena da conversione Pena Pecuniaria - Libertà controllata 
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_LIBCTRL_PP)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Pena da conversione Pena Pecuniaria - Libertà controllata");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_LIBCTRL_PP) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_LIBCTRL_PP) );
      lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_LIBCTRL_PP) );
      lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_LIBCTRL_PP) );
      lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_LIBCTRL_PP) );

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }
    
    //==========================================================================
    // Sanzioni del giudice di pace - Permanenza Domiciliare  
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_PERMANENZA_GP)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzioni del giudice di pace - Permanenza Domiciliare");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_PERMDOM_GP) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_PERMANENZA_GP) );
      lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_PERMDOM_GP) );
      lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_PERMDOM_GP) );
      lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_PERMDOM_GP) );

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    } 
    
    //==========================================================================
    // Sanzioni del giudice di pace - Lavoro sostitutivo  
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_LAVSOST_GP)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzioni del giudice di pace - Lavoro sostitutivo");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_LAVSOST_GP) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_LAVSOST_GP) );
      lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_LAVSOST_GP) );
      lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_LAVSOST_GP) );
      lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_LAVSOST_GP) );

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }
    
    //==========================================================================
    // Sanzioni del giudice di pace - Lavoro pubblica utilità   
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_LPU_GP)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzioni del giudice di pace - Lavoro pubblica utilità");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();

      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_LPU_GP) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_LPU_GP) );
      lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_LPU_GP) );
      lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_LPU_GP) );
      lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_LPU_GP) );

      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }     
    
    //==========================================================================
    // Sanzioni del giudice di pace - Espulsione dallo Stato    
    //==========================================================================
    if (!isRequestParameterNullObj(CAMPO_CHECK_ESP_GP)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sanzioni del giudice di pace - Espulsione");
      
      DatiFinaliUlterioriSanzioniModel lDatMod = new DatiFinaliUlterioriSanzioniModel();
      
      lDatMod.setIdDatiFinaliUlterioriSanz ( getRequestBigDecimalParameter ( CAMPO_ID_ESPULSIONE_GP) );
      lDatMod.setCodTipoUlterioreSanzione  ( getRequestStringParameter     ( CAMPO_CHECK_ESP_GP) );
      
      if (!isRequestParameterNullObj(CAMPO_CHECK_ESPULSIONE_PERPETUA_GP))
        lDatMod.setFlagEspulPerp ( getRequestStringParameter ( CAMPO_CHECK_ESPULSIONE_PERPETUA_GP) );
      else 
        lDatMod.setFlagEspulPerp("T");
      
      if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_ESP_GP)) {
        lDatMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_ESP_GP) );
        lDatMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI_ESP_GP) );
        lDatMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_ESP_GP) ); 
      }
      
      lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
      lDatMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    
      lListaUlterioriSanzioni.add(lDatMod);    
    }    
    
    
    return lListaUlterioriSanzioni;
  }
  
  
  /**
   * Metodo che verifica la situazione ante inserimento modifica per capire
   * se il dato va: inserito, modificato, cancellato
   * E valorizza il campo TipoOperazioneCRUD sui Model.
   * n.b. la form gestisce più dati per cui in fase di Modifica si potrebbe andare contemporaneamente
   *      in insert di alcuni, in modifica o cancellazione di altri ancora
   * @param aPenaRidet
   * @param aListaUlterioriSanzioni
   */
  private void verificaOperazioniCRUD (DatiFinaliCumuloAggregatoModel aAggregatoOLD
                                     , DatiFinaliCumuloAggregatoModel aAggregatoNEW) throws F3BException
  {
    
    // Pena rideterminata
    if (aAggregatoOLD.getPenaRideterminataCumulo()==null && aAggregatoNEW.getPenaRideterminataCumulo()!=null ) {
      aAggregatoNEW.getPenaRideterminataCumulo().setTipoOperazioneCRUD("I");
      
      aAggregatoNEW.getPenaRideterminataCumulo().setCodOperatoreInserimento   ( getCodUtenteConnesso() );
      aAggregatoNEW.getPenaRideterminataCumulo().setDataInserimento           ( DateUtils.getSysDate() );
      aAggregatoNEW.getPenaRideterminataCumulo().setCodUfficioInserimento     ( getCodUfficioUtenteConnesso() );      
    }
    else if (aAggregatoOLD.getPenaRideterminataCumulo()!=null && aAggregatoNEW.getPenaRideterminataCumulo()!=null) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Vado in update PenaRideterminataCumulo");
      aAggregatoNEW.getPenaRideterminataCumulo().setTipoOperazioneCRUD("U");
      
      aAggregatoNEW.getPenaRideterminataCumulo().setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      aAggregatoNEW.getPenaRideterminataCumulo().setDataAggiornamento           ( DateUtils.getSysDate() );
      aAggregatoNEW.getPenaRideterminataCumulo().setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso() ); 
      
      
      // Se sono stato modificati
      if (aAggregatoNEW.getPenaRideterminataCumulo().checkPenaModificata(aAggregatoOLD.getPenaRideterminataCumulo())){
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("Dati della pena moodificati");
        aAggregatoNEW.getPenaRideterminataCumulo().setAggiornaFlagRicalcoloPR ("S");
      }
      
    }
    else if (aAggregatoOLD.getPenaRideterminataCumulo()!=null && aAggregatoNEW.getPenaRideterminataCumulo()==null ) {
      aAggregatoOLD.getPenaRideterminataCumulo().setTipoOperazioneCRUD("D");
      aAggregatoNEW.setPenaRideterminataCumulo(aAggregatoOLD.getPenaRideterminataCumulo());
    }
    
    
    //==========================================================================
    // Gestione Ulteriori sanzioni
    //==========================================================================
    // Sanzione Sostitutiva - Semidetenzione
    if (aAggregatoOLD.getUltSanSanSosSemidetenzione()==null && aAggregatoNEW.getUltSanSanSosSemidetenzione()!=null) {
      setCRUDUlterioreSanzione (aAggregatoNEW.getUltSanSanSosSemidetenzione(),"I");
    }
    else if (aAggregatoOLD.getUltSanSanSosSemidetenzione()!=null && aAggregatoNEW.getUltSanSanSosSemidetenzione()!=null) {
      setCRUDUlterioreSanzione (aAggregatoNEW.getUltSanSanSosSemidetenzione(),"U");
    }
    else if (aAggregatoOLD.getUltSanSanSosSemidetenzione()!=null && aAggregatoNEW.getUltSanSanSosSemidetenzione()==null){
      aAggregatoOLD.getUltSanSanSosSemidetenzione().setTipoOperazioneCRUD("D");      
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanSanSosSemidetenzione());      
    }
    
    // Sanzione Sostitutiva - Liberta Controllata 
    if (aAggregatoOLD.getUltSanSanSosLibertContrl()==null && aAggregatoNEW.getUltSanSanSosLibertContrl()!=null) {
      setCRUDUlterioreSanzione (aAggregatoNEW.getUltSanSanSosLibertContrl(),"I");
    }
    else if (aAggregatoOLD.getUltSanSanSosLibertContrl()!=null && aAggregatoNEW.getUltSanSanSosLibertContrl()!=null) {
      setCRUDUlterioreSanzione (aAggregatoNEW.getUltSanSanSosLibertContrl(),"U");
    }
    else if (aAggregatoOLD.getUltSanSanSosLibertContrl()!=null && aAggregatoNEW.getUltSanSanSosLibertContrl()==null){
      aAggregatoOLD.getUltSanSanSosLibertContrl().setTipoOperazioneCRUD("D");      
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanSanSosLibertContrl());      
    }    
    
    // Sanzione Sostitutiva - Pena Pecuniaria - Multa 
    if (aAggregatoOLD.getUltSanSanSosPPMulta()==null && aAggregatoNEW.getUltSanSanSosPPMulta()!=null) {
      setCRUDUlterioreSanzione (aAggregatoNEW.getUltSanSanSosPPMulta(),"I");
    }
    else if (aAggregatoOLD.getUltSanSanSosPPMulta()!=null && aAggregatoNEW.getUltSanSanSosPPMulta()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanSanSosPPMulta(),"U");
    }
    else if (aAggregatoOLD.getUltSanSanSosPPMulta()!=null && aAggregatoNEW.getUltSanSanSosPPMulta()==null){
      aAggregatoOLD.getUltSanSanSosPPMulta().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanSanSosPPMulta());      
    }
    
    // Sanzione Sostitutiva - Pena Pecuniaria - Ammenda 
    if (aAggregatoOLD.getUltSanSanSosPPAmmenda()==null && aAggregatoNEW.getUltSanSanSosPPAmmenda()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanSanSosPPAmmenda(),"I");
    }
    else if (aAggregatoOLD.getUltSanSanSosPPAmmenda()!=null && aAggregatoNEW.getUltSanSanSosPPAmmenda()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanSanSosPPAmmenda(),"U");
    }
    else if (aAggregatoOLD.getUltSanSanSosPPAmmenda()!=null && aAggregatoNEW.getUltSanSanSosPPAmmenda()==null){
      aAggregatoOLD.getUltSanSanSosPPAmmenda().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanSanSosPPAmmenda());      
    }
    
    // Sanzione Sostitutiva - Espulsione
    if (aAggregatoOLD.getUltSanSanSosEspulsione()==null && aAggregatoNEW.getUltSanSanSosEspulsione()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanSanSosEspulsione(),"I");
    }
    else if (aAggregatoOLD.getUltSanSanSosEspulsione()!=null && aAggregatoNEW.getUltSanSanSosEspulsione()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanSanSosEspulsione(),"U");
    }
    else if (aAggregatoOLD.getUltSanSanSosEspulsione()!=null && aAggregatoNEW.getUltSanSanSosEspulsione()==null){
      aAggregatoOLD.getUltSanSanSosEspulsione().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanSanSosEspulsione());      
    }

    // Sanzione Sostitutiva - LPU
    if (aAggregatoOLD.getUltSanSanSosLPU()==null && aAggregatoNEW.getUltSanSanSosLPU()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanSanSosLPU(),"I");
    }
    else if (aAggregatoOLD.getUltSanSanSosLPU()!=null && aAggregatoNEW.getUltSanSanSosLPU()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanSanSosLPU(),"U");
    }
    else if (aAggregatoOLD.getUltSanSanSosLPU()!=null && aAggregatoNEW.getUltSanSanSosLPU()==null){
      aAggregatoOLD.getUltSanSanSosLPU().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanSanSosLPU());      
    }
    
    // Pena da conversione Pena Pecuniaria - Lavoro Sostitutivo 
    if (aAggregatoOLD.getUltSanConvPPLavSost()==null && aAggregatoNEW.getUltSanConvPPLavSost()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanConvPPLavSost(),"I");
    }
    else if (aAggregatoOLD.getUltSanConvPPLavSost()!=null && aAggregatoNEW.getUltSanConvPPLavSost()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanConvPPLavSost(),"U");
    }
    else if (aAggregatoOLD.getUltSanConvPPLavSost()!=null && aAggregatoNEW.getUltSanConvPPLavSost()==null){
      aAggregatoOLD.getUltSanConvPPLavSost().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanConvPPLavSost());      
    }    
    
    // Pena da conversione Pena Pecuniaria - Liberta Controllata 
    if (aAggregatoOLD.getUltSanConvPPLibCtrl()==null && aAggregatoNEW.getUltSanConvPPLibCtrl()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanConvPPLibCtrl(),"I");
    }
    else if (aAggregatoOLD.getUltSanConvPPLibCtrl()!=null && aAggregatoNEW.getUltSanConvPPLibCtrl()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanConvPPLibCtrl(),"U");
    }
    else if (aAggregatoOLD.getUltSanConvPPLibCtrl()!=null && aAggregatoNEW.getUltSanConvPPLibCtrl()==null){
      aAggregatoOLD.getUltSanConvPPLibCtrl().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanConvPPLibCtrl());      
    } 
    
    //===========================================================
    // Sanzioni del giudice di pace  
    //===========================================================
    // Permanenza Domiciliare 
    if (aAggregatoOLD.getUltSanGiuPacePermDom()==null && aAggregatoNEW.getUltSanGiuPacePermDom()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanGiuPacePermDom(),"I");
    }
    else if (aAggregatoOLD.getUltSanGiuPacePermDom()!=null && aAggregatoNEW.getUltSanGiuPacePermDom()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanGiuPacePermDom(),"U");
    }
    else if (aAggregatoOLD.getUltSanGiuPacePermDom()!=null && aAggregatoNEW.getUltSanGiuPacePermDom()==null){
      aAggregatoOLD.getUltSanGiuPacePermDom().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanGiuPacePermDom());      
    }
    
    // Lavoro sostitutivo
    if (aAggregatoOLD.getUltSanGiuPaceLavSost()==null && aAggregatoNEW.getUltSanGiuPaceLavSost()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanGiuPaceLavSost(),"I");
    }
    else if (aAggregatoOLD.getUltSanGiuPaceLavSost()!=null && aAggregatoNEW.getUltSanGiuPaceLavSost()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanGiuPaceLavSost(),"U");
    }
    else if (aAggregatoOLD.getUltSanGiuPaceLavSost()!=null && aAggregatoNEW.getUltSanGiuPaceLavSost()==null){
      aAggregatoOLD.getUltSanGiuPaceLavSost().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanGiuPaceLavSost());      
    }  
    
    // Lavoro pubblica utilità  
    if (aAggregatoOLD.getUltSanGiuPaceLPU()==null && aAggregatoNEW.getUltSanGiuPaceLPU()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanGiuPaceLPU(),"I");
    }
    else if (aAggregatoOLD.getUltSanGiuPaceLPU()!=null && aAggregatoNEW.getUltSanGiuPaceLPU()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanGiuPaceLPU(),"U");
    }
    else if (aAggregatoOLD.getUltSanGiuPaceLPU()!=null && aAggregatoNEW.getUltSanGiuPaceLPU()==null){
      aAggregatoOLD.getUltSanGiuPaceLPU().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanGiuPaceLPU());      
    }
    
    // Espulzione dallo stato
    if (aAggregatoOLD.getUltSanGiuPaceESP()==null && aAggregatoNEW.getUltSanGiuPaceESP()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanGiuPaceESP(),"I");
    }
    else if (aAggregatoOLD.getUltSanGiuPaceESP()!=null && aAggregatoNEW.getUltSanGiuPaceESP()!=null) {
      setCRUDUlterioreSanzione(aAggregatoNEW.getUltSanGiuPaceESP(),"U");
    }
    else if (aAggregatoOLD.getUltSanGiuPaceESP()!=null && aAggregatoNEW.getUltSanGiuPaceESP()==null){
      aAggregatoOLD.getUltSanGiuPaceESP().setTipoOperazioneCRUD("D");
      // Inserisco nella lista il vecchio record da cancellare
      aAggregatoNEW.getListaDatiFinaliUlterioriSanzioni().add (aAggregatoOLD.getUltSanGiuPaceESP());      
    }

    
  }
  
  /**
   * 
   * @param aUlterioreSanz
   * @param aTipoOperazione
   * @throws F3BException
   */
  private void setCRUDUlterioreSanzione (DatiFinaliUlterioriSanzioniModel aUlterioreSanz, String aTipoOperazione) throws F3BException 
  {
    if (aTipoOperazione.equals("I")){
      aUlterioreSanz.setTipoOperazioneCRUD("I");
      
      aUlterioreSanz.setCodOperatoreInserimento   ( getCodUtenteConnesso() );
      aUlterioreSanz.setDataInserimento           ( DateUtils.getSysDate() );
      aUlterioreSanz.setCodUfficioInserimento     ( getCodUfficioUtenteConnesso() );    
      
      
      if (   aUlterioreSanz.getCodTipoUlterioreSanzione().equals(ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_SS)
          && aUlterioreSanz.getListaOrariLPU()!=null
         )
      {
        Iterator <TipologiaOrarioModel> lOrariIter = aUlterioreSanz.getListaOrariLPU().iterator();
        while (lOrariIter.hasNext()){
          TipologiaOrarioModel lOrarioModel = lOrariIter.next();
          
          lOrarioModel.setCodOperatoreInserimento   ( getCodUtenteConnesso() );
          lOrarioModel.setDataInserimento           ( DateUtils.getSysDate() );
          lOrarioModel.setCodUfficioInserimento     ( getCodUfficioUtenteConnesso() );  
        }        
      }
    }
    else if (aTipoOperazione.equals("U")){
      aUlterioreSanz.setTipoOperazioneCRUD("U");
      
      aUlterioreSanz.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      aUlterioreSanz.setDataAggiornamento           ( DateUtils.getSysDate() );
      aUlterioreSanz.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso() );
      
      if (   aUlterioreSanz.getCodTipoUlterioreSanzione().equals(ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_SS)
          && aUlterioreSanz.getListaOrariLPU()!=null
         )
      {
        Iterator <TipologiaOrarioModel> lOrariIter = aUlterioreSanz.getListaOrariLPU().iterator();
        while (lOrariIter.hasNext()){
          TipologiaOrarioModel lOrarioModel = lOrariIter.next();
          
          lOrarioModel.setCodOperatoreInserimento   ( aUlterioreSanz.getCodOperatoreInserimento() );
          lOrarioModel.setDataInserimento           ( aUlterioreSanz.getDataInserimento() );
          lOrarioModel.setCodUfficioInserimento     ( aUlterioreSanz.getCodUfficioInserimento() );  
          
          lOrarioModel.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
          lOrarioModel.setDataAggiornamento           ( DateUtils.getSysDate() );
          lOrarioModel.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso() );          
        }        
      }      
    }
  }

  /**
   * 
   * @param model
   * @return
   * @throws Exception
   */
  private Vector <TipologiaOrarioModel> caricaTipologiaOrario() throws F3BException {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("caricaTipologiaOrario");

    Vector <TipologiaOrarioModel> lListaGGOrari = new Vector <TipologiaOrarioModel> ();
    
    int day = 1;
    while (day <= 7) {
      String myDay = "_0" + day;
      if (!this.isRequestParameterNullObj(ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_CHECK_ORARIO_DAY + myDay)) 
      {
        TipologiaOrarioModel element = new TipologiaOrarioModel();
        
        element.setCodNumGiorno      (getRequestStringParameter(ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_CHECK_ORARIO_DAY + myDay));
        element.setDalleOre          (getRequestStringParameter(ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_DALLE_ORE_DAY + myDay));
        element.setAlleOre           (getRequestStringParameter(ICostantiDatiFinaliUlterioriSanzioni.CAMPO_LPU_ALLE_ORE_DAY + myDay));
        
        lListaGGOrari.add(element);
      }
      day++;
    }    
    
    return lListaGGOrari;
  }
}