package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * Azione di Inserimento/Modifica e Cancellazione delle Richieste al GE in DatiFinaliCumulo
 * @author d.fiorletta
 *
 */
public class ActInsRichiesteComputiDatiFinali extends ActionModuloCumulo 
            implements ICostantiDatiFinaliCumulo, ICostantiComputiCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    
    super.getDatiIstruttoria();
    
    IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
    
    
    String lModalita = "";    
    lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella

    if ("I".equals(lModalita)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sono in INSERIMENTO");
      
      ComputiCumuloModel lComputoMod = this.getDatiForm();

      lComputoMod.setCodOperatoreInserimento     ( getCodUtenteConnesso() );
      lComputoMod.setDataInserimento             ( DateUtils.getSysDate() );
      lComputoMod.setCodUfficioInserimento       ( getCodUfficioUtenteConnesso() );  
      
      lCtrlComputi.ExInserisciComputiCumulo (lComputoMod);
    }
    else if ("M".equals(lModalita)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sono in MODIFICA");

      ComputiCumuloModel lComputoMod = this.getDatiForm();
      
      lComputoMod.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      lComputoMod.setDataAggiornamento           ( DateUtils.getSysDate() );
      lComputoMod.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso());      

      lCtrlComputi.ExModificaComputiCumulo (lComputoMod);
      
    }
    else if ("C".equals(lModalita)) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sono in CANCELLAZIONE");
      
      BigDecimal lIdComputo = getRequestBigDecimalParameter ( CAMPO_ID_COMPUTI_CUMULO) ;

      lCtrlComputi.ExCancellaComputiCumuloBykey (lIdComputo);
    }

    // Vado in aggiornamento del Flag sulla pena se presente in modo da segnalare 
    // la necessità di ricalcolare la pena
    DatiFinaliCumuloAggregatoModel lDatiAggregati = super.getDatiFinaliCumuloAggregato();
    PenaRideterminataCumuloModel lPenaResidua = lDatiAggregati.getPenaResiduaCumulo();
    if (lPenaResidua!=null && lPenaResidua.getIdPenaRideterminataCumulo()!=null) {
      lPenaResidua.setIsPenaDaRicalcolare ("S");
      IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
      lCtrlDatiFinali.ExModificaPenaRideterminataCumulo (lPenaResidua);
    }
    
    
    String lPage = null;
//    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioAltreSanzioni";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioPeneRideterminate";
    lPage += "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO ).toString();
    return lPage;
  }
  
  /**
   * 
   * @return
   * @throws F3BException
   */
  private ComputiCumuloModel getDatiForm () throws F3BException
  {
    ComputiCumuloModel lComputoMod = new ComputiCumuloModel();

    //========================================================================== 
    // Recupero i dati presenti in maschera 
    // n.b. eliminare o commentare i campi non presenti in maschera 
    //      es: chiave della tabella, date_ins, foreignkey... 
    //========================================================================== 
    lComputoMod.setIdComputiCumulo       ( getRequestBigDecimalParameter ( CAMPO_ID_COMPUTI_CUMULO) );
    
    lComputoMod.setCodTipoAnnotazione    ( getRequestStringParameter     ( CAMPO_COD_TIPO_ANNOTAZIONE) );
    lComputoMod.setCodCausaleComputo     ( null );
    
    lComputoMod.setCodDpr                ( getRequestStringParameter     ( CAMPO_COD_DPR));
  
    if (    !isRequestParameterNullObj(CAMPO_ANNO_DATA_RICHIESTA) 
         && getRequestStringParameters(CAMPO_ANNO_DATA_RICHIESTA).length>0
       )
    {
      lComputoMod.setDataRichiesta  ( getRequestDateParameter ( CAMPO_ANNO_DATA_RICHIESTA
                                                              , CAMPO_MESE_DATA_RICHIESTA
                                                              , CAMPO_GIORNO_DATA_RICHIESTA) );
    }
    
    lComputoMod.setFlagPiuMeno               ( getRequestStringParameter     ( CAMPO_FLAG_PIU_MENO) );

    CalendarUtil lCalUtil = new CalendarUtil();
    CalendarModel lCalendar = new CalendarModel();
    
    lCalendar.setNumAnni   (getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_RECLUSIONE));
    lCalendar.setNumMesi   (getRequestBigDecimalParameter ( CAMPO_NUM_MESI_RECLUSIONE));
    lCalendar.setNumGiorni (getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_RECLUSIONE));
    
    lCalendar = lCalUtil.ricalcolaGAM (lCalendar); // Normalizzo i quantum
    
    lComputoMod.setNumAnniReclusione         ( lCalendar.getNumAnni()==0?null:new BigDecimal (lCalendar.getNumAnni()) );
    lComputoMod.setNumMesiReclusione         ( lCalendar.getNumMesi()==0?null:new BigDecimal (lCalendar.getNumMesi()) );
    lComputoMod.setNumGiorniReclusione       ( lCalendar.getNumGiorni()==0?null:new BigDecimal (lCalendar.getNumGiorni()) );


    if (  (getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") != null && !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")).equals("")) 
        ||(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC") != null && !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")).equals("")) ) 
    { 
      lComputoMod.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") 
                                           + "."+getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC"))); 
    } 
    
    lCalendar = new CalendarModel(); 
    lCalendar.setNumAnni   (getRequestBigDecimalParameter ( CAMPO_NUM_ANNI_ARRESTO));
    lCalendar.setNumMesi   (getRequestBigDecimalParameter ( CAMPO_NUM_MESI_ARRESTO));
    lCalendar.setNumGiorni (getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI_ARRESTO));

    lCalendar = lCalUtil.ricalcolaGAM (lCalendar); // Normalizzo i quantum
    
    // Arresto
    lComputoMod.setNumAnniArresto            ( lCalendar.getNumAnni()==0?null:new BigDecimal (lCalendar.getNumAnni()) );
    lComputoMod.setNumMesiArresto            ( lCalendar.getNumMesi()==0?null:new BigDecimal (lCalendar.getNumMesi()) );
    lComputoMod.setNumGiorniArresto          ( lCalendar.getNumGiorni()==0?null:new BigDecimal (lCalendar.getNumGiorni()) );
    if (  (getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") != null && !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT")).equals("")) 
        ||(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC") != null && !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC")).equals("")) ) 
    { 
      lComputoMod.setImportoAmmenda(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") 
                                                   + "."+getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC"))); 
    } 

    lComputoMod.setNote  ( getRequestStringParameter     ( CAMPO_NOTE));

    lComputoMod.setFlagStato             ("I");
    lComputoMod.setMotivoModifica        (null);
    lComputoMod.setTitIdTitoloCumulato   (null);
    lComputoMod.setDatIdDatiFinaliCumulo     ( getRequestBigDecimalParameter ( ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO) );
    lComputoMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter (ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) );
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lComputoMod = "+lComputoMod);
    
    return lComputoMod;
  }
  
}