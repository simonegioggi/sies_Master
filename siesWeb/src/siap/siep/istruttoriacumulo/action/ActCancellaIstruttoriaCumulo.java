package siap.siep.istruttoriacumulo.action;


/**
* <p>Title: ActCancellaIstruttoriaCumulo</p>
* <p>Description: Classe Action per la cancellazione di IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActCancellaIstruttoriaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo
{
 /*****************************************************************************
  * Azione per l'Annullamento/Cancellazione dell'istruttoria. L'istruttoria
  * viene annullata se non è più necessario procedere ad emettere il cumulo
  * perchè per esempio è subentrato un nuovo titolo.
  * 
  * @return PG_MESSAGE di avvenuta cancellazione
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    //=============================================
    // Recupero l'id dell'istruttoria da chiudere  
    //=============================================
    BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO) ;

    //==========================================================================
    // Recupero i dati dell'istruttoria e aggiorno lo STATO e le NOTE   
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaModel = null;
    
    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    lIstruttoriaModel = lIstrCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoriaCumulo);

    lIstruttoriaModel.setFlagStato     (FLAG_STATO_ANNULLATA); // Annullato
    lIstruttoriaModel.setDataChiusura  (DateUtils.getSysDate()); // n.b. mi servono anche hh:mm:ss perchè nell'arco della gionata potrebbero venir aperte e chiuse più istruttorie
    lIstruttoriaModel.setNote          (getRequestStringParameter(CAMPO_NOTE));
    
    lIstruttoriaModel.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    lIstruttoriaModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
    lIstruttoriaModel.setDataAggiornamento         (DateUtils.getSysDate());
    
    //====================================================== 
    // Effettuo la cancellazione (logica per il momento) 
    //====================================================== 
    lIstrCtrl.ExAnnullaIstruttoriaCumulo(lIstruttoriaModel);
    
    //===========================================================
    // Restituisce la pagina di Conferma avvenuta Cancellazione. 
    //===========================================================
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Chiusura Istruttoria effettuata!");
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    lRedirigi.setAction("siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&"+CAMPO_ID_ISTRUTTORIA_CUMULO+"="+lIdIstruttoriaCumulo);
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

    
    return IWebConstants.PG_MESSAGE;
  }
}