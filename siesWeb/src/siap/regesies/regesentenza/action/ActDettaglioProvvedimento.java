package siap.regesies.regesentenza.action;

//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.siep.sentenza.controller.SentenzaController;
import siap.regesies.regesentenza.controller.IRegeSentenza;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>Title: ActDettaglioProvvedimento</p>
 * <p>Description: Dettaglio Provvedimento proveniente da ReGe</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class ActDettaglioProvvedimento extends ActionSiap
   implements ICostantiRegeSentenza
{
  public String processRequest() throws F3BException
  {
    // Parse della request
    String lId = getRequestStringParameter(CAMPO_ID_FILE);

    //Ricerca la sentenza in Rege
    IRegeSentenza lCtrl = RegeSiesLookupRemote.getRegeSentenzaRemote();
    ProvvedimentoModel lProv = lCtrl.ExDettaglioProvvedimento(lId);

    setRequestAttribute("provvedimento", lProv);
    setSessionAttribute(PROVVEDIMENTO_IN_SESSION, lProv);

    return PG_LOAD_DETTAGLIO_PROVVEDIMENTO;
  }
}