package siap.siep.penaaccessoria.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaPenaAccessoria extends ActionSiap implements ICostantiPenaAccessoria {
  /**
   * Azione di cancellazione del Soggetto
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    // riempie il model
    PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();

    lPenMod.setIdPenaAccessoria(getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA));

    // chiama il controller
    IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
    boolean esistePAsostitutiva = lCtrl.ExistPASostitutiva(getRequestBigDecimalParameter( CAMPO_ID_PENA_ACCESSORIA)) ;
    if (esistePAsostitutiva)
      throw new F3BException(F3BException.USER_MESSAGE,"Operazione non consentita causa presenza P.A. sostitutiva: procedere prima alla cancellazione di quest'ultima !");
    else
      lCtrl.ExCancellaPenaAccessoria(lPenMod);

   //Prepara la "pagina" di destinAction
    String lPage = "";
     // setta la risposta nella request
     //Si passa solo il Model
     lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.penaaccessoria.action.ActRicercaPenaAccessoria&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep();

     // Ticket 20210924018 - Se dopo la cancellazione non restavano PA a sistema la ricerca restituiva la pagina con 
     // il mesaggio "nessun elemento trovato" ma non impostava la pagina di ritorno che per default è hostory-1 per 
     // cui l'utente tornava sul dettaglio della PA appena cancellata o sull'elenco con la PA ancora presente.
     setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
     // Ticket 20210924018 - FINE
     
    return lPage;

  }
}
