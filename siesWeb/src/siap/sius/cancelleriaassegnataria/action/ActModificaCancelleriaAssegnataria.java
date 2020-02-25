package siap.sius.cancelleriaassegnataria.action;


/**
* <p>Title: ActModificaCancelleriaAssegnataria</p>
* <p>Description: Classe Action per la modifica di CancelleriaAssegnataria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActModificaCancelleriaAssegnataria extends ActionSiap implements ICostantiCancelleriaAssegnataria
{
/**
* Azione di Modifica del CancelleriaAssegnataria
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
   public String processRequest() throws F3BException
   {
      // Si istanzia il model
      CancelleriaAssegnatariaModel lCanMod = new CancelleriaAssegnatariaModel ();

      // Valorizzazione del record da modificare
      lCanMod.setCodCancelleriaAssegnataria( getRequestStringParameter( CAMPO_COD_CANCELLERIA_ASSEGNATARIA) );
      lCanMod.setCodUfficio( getRequestStringParameter( CAMPO_COD_UFFICIO) );
      lCanMod.setDescCancelleriaAssegnataria( getRequestStringParameter( CAMPO_DESC_CANCELLERIA_ASSEGNATARIA) );
      lCanMod.setCodOperatoreAggiornamento( getCodUtenteConnesso());
      lCanMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
      lCanMod.setDataAggiornamento(DateUtils.getSysDate());

      // Modifica
      ICancelleriaAssegnataria lCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
      CancelleriaAssegnatariaModel lCanModRet = lCtrl.ExModificaCancelleriaAssegnataria(lCanMod);

      // Si passa al dettaglio
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction("siap.sius.cancelleriaassegnataria.action.ActLoadDettaglioCancelleriaAssegnataria");
      lPage.setParameter(CAMPO_COD_CANCELLERIA_ASSEGNATARIA, lCanModRet.getCodCancelleriaAssegnataria());
      lPage.setParameter(CAMPO_COD_UFFICIO, lCanModRet.getCodUfficio());

      return lPage.toString();
   }
}