package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.util.CalcoloPenaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadCalcoloPenaCumulo extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo
{
  /**
   * 
   */
	
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
    BigDecimal lIdIstruttoriaCorrente = null;
    
    if (  isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
        || getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)==null
       )
    {
      siesLogger.debug("Istruttoria non selezionata");

      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Nessuna istruttoria selezionata" );
      lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      throw new F3BException(F3BException.USER_MESSAGE,"Nessuna istruttoria selezionata");      
    }
    else {
      lIdIstruttoriaCorrente = this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
      siesLogger.debug("lIdIstruttoriaCorrente = "+lIdIstruttoriaCorrente);
      
      IIstruttoriaCumulo lCtrlIstruttoria = SIEPLookupRemote.getIstruttoriaCumuloRemote();
      
      CalcoloPenaCumuloModel lCalcoloPenaModel = lCtrlIstruttoria.ExCalcolaPenaCumuloByIstruttoria(lIdIstruttoriaCorrente,null, true);
      
      //Elaborare i dati
      
      setRequestAttribute("aCalcoloPenaModel", lCalcoloPenaModel);
      
    }

    
    return PG_POPUP_CALCOLO_PENA;
  }

    
}
