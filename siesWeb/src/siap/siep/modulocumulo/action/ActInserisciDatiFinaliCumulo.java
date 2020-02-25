package siap.siep.modulocumulo.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * Action di iserimento/modifica dei primi dati della fase Dati Finali
 * @author d.fiorletta
 *
 */
public class ActInserisciDatiFinaliCumulo extends ActionModuloCumulo implements ICostantiDatiFinaliCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    super.getDatiIstruttoria();
    
    String lModalita = getRequestStringParameter("modalita");
    
    DatiFinaliCumuloModel lDatiFinali = null;
    lDatiFinali = this.getDatiForm();
    IDatiFinaliCumulo lCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
    
    if (lModalita.equals("I")) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Inserimento ");
      lDatiFinali.setIstrIdIstruttoriaCumulo   (super.getIdIstruttoria());
      
      lDatiFinali.setCodOperatoreInserimento   ( getCodUtenteConnesso() );
      lDatiFinali.setDataInserimento           ( DateUtils.getSysDate() );
      lDatiFinali.setCodUfficioInserimento     ( getCodUfficioUtenteConnesso() );
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lDatiFinali = "+lDatiFinali);
      lDatiFinali = lCtrl.ExInserisciDatiFinaliCumulo (lDatiFinali);
    }
    else if (lModalita.equals("M")) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Modifica ");
      
      lDatiFinali.setIstrIdIstruttoriaCumulo   (super.getIdIstruttoria());

      lDatiFinali.setCodOperatoreAggiornamento   ( getCodUtenteConnesso() );
      lDatiFinali.setDataAggiornamento           ( DateUtils.getSysDate() );
      lDatiFinali.setCodUfficioAggiornamento     ( getCodUfficioUtenteConnesso() );
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lDatiFinali = "+lDatiFinali);
      
      lCtrl.ExModificaDatiFinaliCumulo (lDatiFinali);
    }
    
    
    //==========================================================================
    // Vado in insert o update
    //==========================================================================
    
    //=============================
    // Invoco la Action di dettaglio
    //=============================
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo&" 
            + ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO +"="+lDatiFinali.getIdDatiFinaliCumulo()
            + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

    return lPage;
  }
  
  /**
   * 
   * @return
   * @throws F3BException
   */
  private DatiFinaliCumuloModel getDatiForm() throws F3BException
  {
    DatiFinaliCumuloModel lDatMod = new DatiFinaliCumuloModel();

    //========================================================================== 
    // Recupero i dati presenti in maschera 
    //========================================================================== 
    lDatMod.setIdDatiFinaliCumulo        ( getRequestBigDecimalParameter ( CAMPO_ID_DATI_FINALI_CUMULO) );
    
    lDatMod.setTipoUfficioEmissione      ( getRequestStringParameter     ( CAMPO_TIPO_UFFICIO_EMISSIONE) );
    
    lDatMod.setDataProvvedimento         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PROVVEDIMENTO,CAMPO_MESE_DATA_PROVVEDIMENTO,CAMPO_GIORNO_DATA_PROVVEDIMENTO) );
    lDatMod.setCodTipoProvvedimento      ("03"); // Fisso Ordinanza
    
    if (lDatMod.getTipoUfficioEmissione().equals("03")){
      // Con ordinanza
      lDatMod.setAnnoProvvedimento         ( getRequestBigDecimalParameter ( CAMPO_ANNO_PROVVEDIMENTO) );
      lDatMod.setNumeroProvvedimento       ( getRequestBigDecimalParameter ( CAMPO_NUMERO_PROVVEDIMENTO) );
      
      
      lDatMod.setCodTipoUfficioEmittente   ( getRequestStringParameter     ( CAMPO_COD_TIPO_UFFICIO_EMITTENTE) );
      
      String lDescLuogoUfficio = getRequestStringParameter ( CAMPO_DESCR_LUOGO_UFFICIO_EMITTENTE);
      ComuneModel lComuneSedeUfficio = getCodComuneByDescr(lDescLuogoUfficio);
      
      // Verificare
      getCodUfficioByCodTipoUfficioDescrComune(lDatMod.getCodTipoUfficioEmittente(), lDescLuogoUfficio);
      
      lDatMod.setCodLuogoUfficioEmittente  ( lComuneSedeUfficio.getCodComune() );
      
      lDatMod.setSezioneUfficioEmittente   ( getRequestStringParameter     ( CAMPO_SEZIONE_UFFICIO_EMITTENTE) );
    }
    else {
      // d'ufficio
      lDatMod.setAnnoProvvedimento         ( null );
      lDatMod.setNumeroProvvedimento       ( null );     
      lDatMod.setCodTipoUfficioEmittente   ( null );
      lDatMod.setCodLuogoUfficioEmittente  ( null );      
      lDatMod.setSezioneUfficioEmittente   ( null );      
    }
    
    
    //lDatMod.setCodPosizioneGiuridica     ( getRequestStringParameter     ( CAMPO_COD_POSIZIONE_GIURIDICA) );
    //lDatMod.setEveIdEvento               ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO) );
    //lDatMod.setIstrIdIstruttoriaCumulo   ( getRequestBigDecimalParameter ( CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO) );
   
    return lDatMod;
  }
  
  
}