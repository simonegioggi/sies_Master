package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

import siap.sico.utente.model.DatiOperazioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
* <p>Title: ActInserisciFascicoloProprioUfficioInIstruttoria</p>
* <p>Description: Classe Action per l'inserimento in istruttoria cumulo di fascicolo in carico al proprio Ufficio.
* @version 1.0
*/

public class ActInserisciFascicoloProprioUfficioInIstruttoria extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
	  siesLogger.debug("--XX-- START - ActInserisciFascicoloProprioUfficioInIstruttoria");
   
    //==========================================================================
    // Recupero l'istruttoria da passare alla form
    //==========================================================================
    IstruttoriaCumuloModel lIstuMod = super.getDatiIstruttoria();
    BigDecimal lIdIstruttoriaCorrente = this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    
    BigDecimal lIdFascCumulante = lIstuMod.getFasSieIdFascicoloSiep();
    
    //=============================================
    // Recupero il/i fascicolo/i da Cumulare
    //=============================================
    String[] lIdFascicoliMioUfficio = null;

   	if(isRequestChecked(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP) )
   		lIdFascicoliMioUfficio = getRequestStringParameters(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
    
    siesLogger.debug("--XX-- ho scelto N. >"+lIdFascicoliMioUfficio.length+"< Fascicoli da Iscrivere in Istruttoria");
   
    DatiOperazioneModel lDatiOpModel = new DatiOperazioneModel();
    lDatiOpModel.setCodOperatore (getCodUtenteConnesso());
    lDatiOpModel.setCodUfficio   (getCodUfficioUtenteConnesso());
    lDatiOpModel.setData         (DateUtils.getSysDate());
    
    IModuloCumulo lCtrlModCumulo = SIEPLookupRemote.getModuloCumuloRemote();
    
    // Connection lConn = null;		-   04 = Iscrizione da proprio Ufficio
    lCtrlModCumulo.ExInserisciTitoliProprioUfficioInIstruttoria(lIdIstruttoriaCorrente, lIdFascicoliMioUfficio, lDatiOpModel, null, lIdFascCumulante, "04" ); 

    //=============================================
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Fascicolo Cumulato Avvenuto Correttamente!");
    lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti");
    lRedirigi.setParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO, ""+lIdIstruttoriaCorrente);
    //lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

    return IWebConstants.PG_MESSAGE;
  }

  
}  // CHIUDE CLASSE
