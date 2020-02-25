package siap.sico.magistrato.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import per le combo
import f3b.web.html.Option;


/**
* <p>Title: ActLoadModificaMagistrato</p>
* <p>Description: Classe Action per la load dettaglio di Magistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadModificaMagistrato extends ActionSiap implements ICostantiMagistrato
{
  public String processRequest() throws F3BException
  {
    Option lOption;
    String lId = getRequestStringParameter(CAMPO_COD_MAGISTRATO);

    // Lock
   LockModel lck = LockController.lockIfNotLocked(getServletContext(),"Magistrato",lId,getCodUtenteConnesso(),getSession().getId());
   if (lck!=null)
   {
     setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il  "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
     return IWebConstants.PG_MESSAGE;
   }

    if (lId == null)
      throw new F3BException(F3BException.USER_MESSAGE,"CAMPO_COD_MAGISTRATO ???");

    if (lId.equals("-"))
      throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la modifica di questo record: magistrato di default.");

    // chiama il controller
    IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
    MagistratoModel lMagMod = lCtrl.ExRicercaMagistratoByCod(lId);

    // Inserire Eventuali ComboBOX
    if(lMagMod.getFlagStato() != null)
      lOption = new Option( DecodificheManager.getInstance().getFlagStato(),lMagMod.getFlagStato());
    else
      lOption = new Option( DecodificheManager.getInstance().getFlagStato());
    setRequestAttribute( "elencoFlagStato", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
    setRequestAttribute("magistrato", lMagMod);

    return PG_LOAD_INSERISCIMAGISTRATO;
  }
}