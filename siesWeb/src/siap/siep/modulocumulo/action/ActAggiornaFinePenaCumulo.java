package siap.siep.modulocumulo.action;

import java.util.Date;

import org.apache.log4j.Logger;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Action che effettua l'aggiornamento del fine pena con quanto digitato in form 
 * dall'utente
 * 
 * @author d.fiorletta
 *
 */
public class ActAggiornaFinePenaCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo, ICostantiPenaRideterminataCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {    
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    super.getDatiIstruttoria();
    DatiFinaliCumuloAggregatoModel lDatiAggregati = super.getDatiFinaliCumuloAggregato();

   
    //==========================================================================
    // Aggiorna il fine pena e prosegue
    //==========================================================================
    PenaRideterminataCumuloModel lPenaResiduaCumulo = lDatiAggregati.getPenaResiduaCumulo();
    
    Date lDataFinePenaManuale = getRequestDateParameter (CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE) ;
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lDataFinePenaManuale = "+DateUtils.getDateToString(lDataFinePenaManuale, "dd/MM/yyyy"));
    
    lPenaResiduaCumulo.setDataFine (lDataFinePenaManuale);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lPenaResiduaCumulo = "+lPenaResiduaCumulo);
    IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
    lCtrlDatiFinali.ExModificaPenaRideterminataCumulo (lPenaResiduaCumulo);
    
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioProvvedimentoCumulo"
        + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter (ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

    
    return lPage;
  }
}