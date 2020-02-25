package siap.sige.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.residenza.action.ICostantiResidenza;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;


/**
 * <p>Title: ActLoadModificaDomicilioFascicoloSige</p>
 * <p>Description: Classe Action per la modifica del domicilio fascicolo sige</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class ActLoadModificaDomicilioFascicoloSige extends ActionSiap implements ICostantiResidenza
{
  public String processRequest() throws Exception
  {

    if (isRequestParameterNullObj(CAMPO_ID_RESIDENZA))
      throw new F3BException(F3BException.USER_MESSAGE,"ID del domicilio assente !" );

    // Lock
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"Domicilio",getRequestStringParameter(CAMPO_ID_RESIDENZA),getCodUtenteConnesso(),getSession().getId());

    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "La  "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }

    // Ricerca della residenza da ID
    ResidenzaModel lRes = new ResidenzaModel();
    lRes.setIdResidenza(getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA));
    IResidenza lCtrl = SICOLookupRemote.getResidenzaRemote();
    lRes = (ResidenzaModel) lCtrl.ExRicercaResidenza(lRes).get(0);

    ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
    lResAss.setResidenza(lRes);
    setRequestAttribute("domicilioassociato", lResAss);
    setRequestAttribute("residenzaassociata", lResAss);

    setRequestAttribute("modalita", "M");

    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), lRes.getCodStato());
    setRequestAttribute("nazioni", "" + lOption );

    return ICostantiFascicoloSige.PG_LOAD_INSERISCIDOMICILIOFASCICOLOSIGE;  //restituisce la jsp di Inserimento

  }
}