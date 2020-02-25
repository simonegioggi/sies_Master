package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActSelezionaPenaAccessoriaDatiFinali extends ActionModuloCumulo 
           implements ICostantiModuloCumulo, ICostantiPenaAccessoriaCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaMdel = super.getDatiIstruttoria();
    super.getDatiFinaliCumuloAggregato();  

    //==========================================================================
    //
    //==========================================================================
    Vector <PenaAccessoriaCumuloModel> lElencoMisure =  getDatiForm (lIstruttoriaMdel.getIdIstruttoriaCumulo());
    
    IPenaAccessoriaCumulo lCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
    lCtrl.ExAggiornaPeneAccessorieDatiFinaliCumulo (lElencoMisure);
    
    String lPage = null;
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioAltreSanzioni";
    lPage += "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO ).toString();
    return lPage;
  } 
  
  /**
   * 
   * @param aIdIstruttoria
   * @return
   * @throws F3BException
   */
  private Vector <PenaAccessoriaCumuloModel> getDatiForm (BigDecimal aIdIstruttoria) throws F3BException
  {
    // Recupera le Pene Accessorie presenti nella form
    Vector <PenaAccessoriaCumuloModel> lElencoPene = new Vector <PenaAccessoriaCumuloModel>();
    IPenaAccessoriaCumulo lCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
    
    lElencoPene = lCtrl.ExRicercaPenaAccessoriaCumuloByIdIstruttoria (aIdIstruttoria,false);

    
    // Recupero gli idPA dei checkDatiFinali
    String[] listaPeneDaIncludere;
    if (!isRequestParameterNullObj(ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_DATI_FINALI))
      listaPeneDaIncludere = getRequestStringParameters ( ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_DATI_FINALI);
    else 
      listaPeneDaIncludere = new String[] {};
    
    Hashtable <BigDecimal, String> lHashIdDaIncludere = new Hashtable <BigDecimal, String>();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("listaPeneDaIncludere.length = "+listaPeneDaIncludere.length);
    for (int i=0; i<listaPeneDaIncludere.length;i++){
      lHashIdDaIncludere.put(new BigDecimal(listaPeneDaIncludere[i]), "S");      
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lHashIdDaIncludere.size() = "+lHashIdDaIncludere.size());
    
    
    //========================================================================== 
    // Verifico cosa è stato selezionato/deselezionato in form e aggiorno i 
    // campi delle PA
    //========================================================================== 
    Iterator <PenaAccessoriaCumuloModel> lIterator = lElencoPene.iterator();
    while (lIterator.hasNext()){
      PenaAccessoriaCumuloModel lPenaAccessoria = lIterator.next();
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("IdPenaAccessoriaCumulo = "+lPenaAccessoria.getIdPenaAccessoriaCumulo());
      
      if (lHashIdDaIncludere.containsKey(lPenaAccessoria.getIdPenaAccessoriaCumulo())){
        lPenaAccessoria.setFlagDatiFinali("S");
      }
      else {
        lPenaAccessoria.setFlagDatiFinali("N");
      }      
    }
    
    return lElencoPene;  
  }  
}