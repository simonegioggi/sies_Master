package siap.siep.fascicolo.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Classe che effettua il caricamento del dettaglio delle Note del Procedimento 
 * @author
 *
 */

public class ActLoadDettaglioNoteProcedimento extends ActionSiap implements ICostantiFascicoloSiep {
	
  public String processRequest() throws Exception {

    setLinkRitorno();

    FascicoloSiepModel lFascMod = new FascicoloSiepModel();

    lFascMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");
    
    String noteFasc = lFascMod.getNote();

    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    
    if (noteFasc == null || noteFasc.trim().equals("")) {
    	
     setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Note Procedimento non presenti. <br> Per inserirle premere il tasto OK");
     lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadInserisciNoteProcedimento&" +
                        ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lFascMod.getIdFascicoloSiep());
     setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
     return IWebConstants.PG_MESSAGE;
    }
    
    return PG_DETTAGLIO_NOTE_PROCEDIMENTO;
  }
}