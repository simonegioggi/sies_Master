package siap.sius.cancelleriaassegnataria.action;


/**
* <p>Title: ActInserisciCancelleriaAssegnataria</p>
* <p>Description: Classe Action per l'inserimento di CancelleriaAssegnataria</p>
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

public class ActInserisciCancelleriaAssegnataria extends ActionSiap implements ICostantiCancelleriaAssegnataria
{
/**
* Azione di Inserimento del CancelleriaAssegnataria
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
   public String processRequest() throws F3BException
   {
      CancelleriaAssegnatariaModel lCanMod = new CancelleriaAssegnatariaModel();

      // Valorizzazione del record da inserire
      lCanMod.setCodCancelleriaAssegnataria( getRequestStringParameter( CAMPO_COD_CANCELLERIA_ASSEGNATARIA).toUpperCase() );
      lCanMod.setCodUfficio( getRequestStringParameter( CAMPO_COD_UFFICIO) );
      lCanMod.setDescCancelleriaAssegnataria( getRequestStringParameter( CAMPO_DESC_CANCELLERIA_ASSEGNATARIA) );
      lCanMod.setCodOperatoreInserimento( getCodUtenteConnesso());
      lCanMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
      lCanMod.setDataInserimento(DateUtils.getSysDate());

      // Inserimento
      ICancelleriaAssegnataria lCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
      CancelleriaAssegnatariaModel llCanModRet = lCtrl.ExInserisciCancelleriaAssegnataria(lCanMod);		 // setta la risposta nella request

      // Si passa al dettaglio
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction("siap.sius.cancelleriaassegnataria.action.ActLoadDettaglioCancelleriaAssegnataria");
      lPage.setParameter(CAMPO_COD_CANCELLERIA_ASSEGNATARIA, llCanModRet.getCodCancelleriaAssegnataria());
      lPage.setParameter(CAMPO_COD_UFFICIO, llCanModRet.getCodUfficio());

      return lPage.toString();
   }

}