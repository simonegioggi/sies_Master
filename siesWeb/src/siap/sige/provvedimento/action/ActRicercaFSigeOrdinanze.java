package siap.sige.provvedimento.action;

import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActRicercaFSigeProvvedimenti </p>
 * <p>Description: Azione per la ricerca dei Provvedimenti
 * legati al fascicolo SIGE a partire da ANNO e PROGR del Fascicolo.
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 * @version 1.0
 */

public class ActRicercaFSigeOrdinanze extends ActRicercaFSigePuntuale
  implements ICostantiProvvedimentoSige, ICostantiFascicoloSige
{

  public String processRequest() throws Exception
  {
    String lRetPage = PG_ELENCOPROVVEDIMENTI;

    super.processRequest();

    // Fascicolo Sige Esteso in sessione.
    FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();

    // Si passa alla Action successiva per la ricerca.
    RedirectTo lPage = new RedirectTo();
    lPage.setPage(IWebConstants.PG_MAIN);
    lPage.setAction("siap.sige.provvedimento.action.ActRicercaOrdinanze");
    lPage.setParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE, lFasEsteso.getFascicoloSige().getIdFascicoloSige().toString());
    lRetPage = lPage.toString();

    return lRetPage;
  }
}