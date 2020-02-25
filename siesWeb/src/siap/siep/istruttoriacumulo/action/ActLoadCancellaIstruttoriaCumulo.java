package siap.siep.istruttoriacumulo.action;


/**
* <p>Title: ActLoadCancellaIstruttoriaCumulo</p>
* <p>Description: Classe Action per la load cancella di IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadCancellaIstruttoriaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
 /*****************************************************************************
  * Azione di caricamento della pagina di Cancellazione/Annullamento dell'istruttoria 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ActLoadCancellaIstruttoriaCumulo");
    
    //==========================================================================
    // Verifico se è stata passata l'istruttoria dal annullare e se risulta 
    // aperta
    //==========================================================================
    if (   isRequestParameterNullObj(CAMPO_ID_ISTRUTTORIA_CUMULO)
        || getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO)==null
       )
    {
      // Non
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Selezionare prima l'istruttoria da chiudere" );
      lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      return IWebConstants.PG_MESSAGE;
    }
    else 
    {
      // Recupero l'istruttoria e controllo se è aperta
      BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO) ;
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lIdIstruttoriaCumulo = "+lIdIstruttoriaCumulo);
      
      IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
      
      IstruttoriaCumuloModel lIstruttoriaModel = null;

      lIstruttoriaModel = lIstrCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoriaCumulo);
      
      if (lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)){
        this.setRequestAttribute("IstruttoriaCumulo", lIstruttoriaModel);
        return PG_LOAD_CANCELLA_ISTRUTTORIA_CUMULO;
      }
      else
      {// Sto visualizzando una Istruttoria non aperta, non posso chiuderla
        RedirectTo lRedirigi = new RedirectTo();
        lRedirigi.setPage(IWebConstants.PG_MAIN);
        if (lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_ANNULLATA))
        {
          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'istruttoria corrente "+lIstruttoriaModel.getAnnoProtocollo()+"/"+lIstruttoriaModel.getNumProtocollo()+" del "+DateUtils.getDateToString(lIstruttoriaModel.getDataApertura(),"dd-MM-yyyy")+" risulta già annullata" );
        }
        else if (lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_CHIUSA))
        {
          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'istruttoria corrente "+lIstruttoriaModel.getAnnoProtocollo()+"/"+lIstruttoriaModel.getNumProtocollo()+" del "+DateUtils.getDateToString(lIstruttoriaModel.getDataApertura(),"dd-MM-yyyy")+" risulta chiusa a seguito emissione Provvedimento di Cumulo" );
        }
        else
        {
          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'istruttoria corrente "+lIstruttoriaModel.getAnnoProtocollo()+"/"+lIstruttoriaModel.getNumProtocollo()+" del "+DateUtils.getDateToString(lIstruttoriaModel.getDataApertura(),"dd-MM-yyyy")+" non risulta aperta" );
        }
        
        lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&"+CAMPO_ID_ISTRUTTORIA_CUMULO+"="+lIstruttoriaModel.getIdIstruttoriaCumulo());
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

        return IWebConstants.PG_MESSAGE;
      }  
    }
  }
}