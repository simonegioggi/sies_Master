package siap.siep.fascicolo.action;

/**
 * <p>Title: ActLoadModificaNoteProcedimento</p>
 * <p>Description: Azione di load della modifica delle Note del Procedimento</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;

public class ActLoadModificaNoteProcedimento extends ActionSiap implements ICostantiFascicoloSiep
{
  public String processRequest() throws Exception
  {
    // riempie il model.
    //Hashtable lockTable=(Hashtable)(getServletContext().getAttribute("lockTable"));
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"fascicolo",getRequestStringParameter(CAMPO_ID_FASCICOLO_SIEP),getCodUtenteConnesso(),getSession().getId());

    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }
    
    /*
     
    setRequestAttribute("modalita", "M");
    
    
    FascicoloSiepModel lFasMod = new FascicoloSiepModel();
    lFasMod.setIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEP));
    lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());

    //FascicoloSiepController lCtrl = new FascicoloSiepController();
    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    lFasMod = lCtrl.ExRicercaFascicoloByKey(lFasMod.getIdFascicoloSiep());

    setRequestAttribute("fascicolo", lFasMod);
    setSessionAttribute("fascicolo", lFasMod);
    
    */
    
    return PG_MODIFICA_NOTE_PROCEDIMENTO;
  }
}
