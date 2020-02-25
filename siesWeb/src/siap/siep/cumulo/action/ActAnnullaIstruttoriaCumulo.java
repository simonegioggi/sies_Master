package siap.siep.cumulo.action;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Action per l'annullamento con chiusura di una istruttoria cumulo
 * 
 * @author 
 */
public class ActAnnullaIstruttoriaCumulo extends ActionSiap implements ICostantiCumulo
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

//    FascicoloSiepModel lFascMod = ((FascicoloSiepModel)(getSessionAttribute("fascicolo")));
    
    //==========================================================================
    // Verifico se è stata passata l'istrittoria dal annullare e se risulta 
    // aperta
    //==========================================================================
    if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)){
      // Non
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Selezionare prima l'istruttoria da chiudere" );
      lRedirigi.setAction( "siap.siep.cumulo.action.ActLoadGrigliaCumulo");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;

    }
    else 
    {
      //========================================================================
      // Inserire qui il codice per effettuare l'annullamento
      //========================================================================
      
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Annullamento Correttamente Effettuato" );
      lRedirigi.setAction( "siap.siep.cumulo.action.ActLoadGrigliaCumulo");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }
  }
}
