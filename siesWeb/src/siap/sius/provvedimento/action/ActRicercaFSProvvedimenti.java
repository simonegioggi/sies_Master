package siap.sius.provvedimento.action;

import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * <p>Title: ActRicercaFSProvvedimenti </p>
 * <p>Description: Azione per la ricerca dei Provvedimenti
 * legati al fascicolo SIUS a partire da ANNO e PROGR del Fascicolo.
 * <p>Copyright: Bull Italia S.p.A.Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 * @version 1.0
 */

public class ActRicercaFSProvvedimenti extends ActRicercaFSPuntuale
  implements ICostantiProvvedimento, ICostantiFascicoloSius
{

  public String processRequest() throws Exception
  {
    String lRetPage = PG_ELENCOPROVVEDIMENTI;

    // 07/06/2004 Aggiunta valorizzazione mControl = false per evitare i controlli sullo stato procedimento.
    this.mControl = false;
    super.processRequest();

    // Recupero del FascicoloSiusGP. Se non in sessione solleva un errore di eccezione.
    if( isSessionAttributeNullObj("fascicoloSiusGP") )
      throw new SIUSException( SIUSException.USER_MESSAGE, "Procedimento non selezionato" );

    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

    // Si passa alla Action successiva per la ricerca Luigi 30-4-2004
    RedirectTo lPage = new RedirectTo();
    lPage.setPage(IWebConstants.PG_MAIN);
    lPage.setAction("siap.sius.provvedimento.action.ActRicercaProvvedimenti");
    lPage.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS,lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString());
    lRetPage = lPage.toString();

/* Sostituita Luigi 30-4-04
    IEvento mCtrl = SICOLookupRemote.getEventoRemote();

    Vector lVect = mCtrl.ExRicercaEventoByFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),COD_EVENTO_PROVVEDIMENTO);
    setRequestAttribute("provvedimenti", lVect);
    this.setFunctionsAvailableToRequest("siap.sius.provvedimento.action.ActRicercaProvvedimenti");
    */
    return lRetPage;
  }
}