package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Action di inserimento delle Misure di sicurezza Selezionate per i dati finali Cumulo
 * 
 * 
 * @author d.fiorletta
 *
 */
public class ActSelezionaMisuraSicurezzaDatiFinali extends ActionModuloCumulo implements ICostantiModuloCumulo, ICostantiMisuraSicurezzaCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaMdel = super.getDatiIstruttoria();
    DatiFinaliCumuloAggregatoModel lDatiFinaliAgg = super.getDatiFinaliCumuloAggregato();  

    //==========================================================================
    //
    //==========================================================================
    Vector <MisuraSicurezzaCumuloModel> lElencoMisure =  getDatiForm (lIstruttoriaMdel.getIdIstruttoriaCumulo());
    BigDecimal lIdFascicoloMs = null;
    
    // Recupero se
    if (!isRequestParameterNullObj (ICostantiDatiFinaliCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_MS) )
    {      
      lIdFascicoloMs = getRequestBigDecimalParameter (ICostantiDatiFinaliCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_MS);
    }
    
    IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
    lCtrl.ExAggiornaMisureDatiFinaliCumulo (lElencoMisure, lIdFascicoloMs,  lDatiFinaliAgg.getDatiFinaliCumulo().getIdDatiFinaliCumulo());
    
    String lPage = null;
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioAltreSanzioni";
    lPage += "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO ).toString();
    return lPage;
  } 
  
  /**
   * 
   * @return
   * @throws F3BException
   */
  private Vector <MisuraSicurezzaCumuloModel> getDatiForm (BigDecimal aIdIstruttoria) throws F3BException
  {
    // Recupera le Misure di Sicurezza presenti nella form
    Vector <MisuraSicurezzaCumuloModel> lElencoMisure = new Vector <MisuraSicurezzaCumuloModel>();
    IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
    
    lElencoMisure = lCtrl.ExRicercaMisureSicurezzaCumuloByIdIstruttoria (aIdIstruttoria,false);

    
    // Recupero gli idMisura dei checkDatiFinali
    String[] listaMisureDaIncludere;
    if (!isRequestParameterNullObj(ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_DATI_FINALI))
      listaMisureDaIncludere = getRequestStringParameters ( ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_DATI_FINALI);
    else 
      listaMisureDaIncludere = new String[] {};
    
    Hashtable <BigDecimal, String> lHashIdDaIncludere = new Hashtable <BigDecimal, String>();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("listaMisureDaIncludere.length = "+listaMisureDaIncludere.length);
    for (int i=0; i<listaMisureDaIncludere.length;i++){
      lHashIdDaIncludere.put(new BigDecimal(listaMisureDaIncludere[i]), "S");      
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lHashIdDaIncludere.size() = "+lHashIdDaIncludere.size());
    

//    // Recupero gli idMisura dei checkCreaProcedimento
//    String[] listaMisureDaCreare;
//    if (!isRequestParameterNullObj(ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_CREA_PROCEDIMENTO))
//      listaMisureDaCreare = getRequestStringParameters (ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_CREA_PROCEDIMENTO);
//    else
//      listaMisureDaCreare = new String[] {};
//    
//    Hashtable <BigDecimal, String> lHashIdDaCreare = new Hashtable <BigDecimal, String>();
//    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//    siesLogger.debug("listaMisureDaCreare.length = "+listaMisureDaCreare.length);
//    for (int i=0; i<listaMisureDaCreare.length;i++){
//      lHashIdDaCreare.put(new BigDecimal(listaMisureDaCreare[i]), "S");      
//    }
//    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//    siesLogger.debug("lHashIdDaCreare.size() = "+lHashIdDaCreare.size());
    
    //========================================================================== 
    // Verifico cosa è stato selezionato/deselezionato in form e aggiorno i 
    // campi delle misure
    //========================================================================== 
    Iterator <MisuraSicurezzaCumuloModel> lIterator = lElencoMisure.iterator();
    while (lIterator.hasNext()){
      MisuraSicurezzaCumuloModel lMisura = lIterator.next();
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("IdMisuraSicurezzaCumulo = "+lMisura.getIdMisuraSicurezzaCumulo());
      
      if (lHashIdDaIncludere.containsKey(lMisura.getIdMisuraSicurezzaCumulo())){
        lMisura.setFlagDatiFinali("S");
      }
      else {
        lMisura.setFlagDatiFinali("N");
      }
      
//      if (lHashIdDaCreare.containsKey (lMisura.getIdMisuraSicurezzaCumulo())){
//        lMisura.setFlagCreaProcedimento ("S");
//      }
//      else {
//        lMisura.setFlagCreaProcedimento("N");
//      }
    }
    
    
    return lElencoMisure;  
  }
}