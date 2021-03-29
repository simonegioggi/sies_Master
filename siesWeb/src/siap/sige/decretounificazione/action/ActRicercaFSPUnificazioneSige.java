package siap.sige.decretounificazione.action;

/**
* <p>Title: ActRicercaFSPImpugnazioneSige</p>
* <p>Description: Classe Action per la ricerca puntuale del Fascicolo SIGE finalizzata alla trasmissione atti</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sige.SIGEException;
import siap.sige.decretounificazione.controller.IDecretoUnificazioneSige;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActRicercaFSPUnificazioneSige extends ActRicercaFSigePuntuale implements ICostantiDecretoUnificazioneSige {
  public String processRequest() throws Exception {
      super.processRequest();
      FascicoloSigeEstesoModel fascicoloSelezionato=super.getFascicoloSigeEstesoInSessione();
    
      IDecretoUnificazioneSige lCtrl = SIGELookupRemote.getDecretoUnificazioneSigeRemote();
      // Ticket#2021032201 - Modificato il RUOLO nella chiamata è lunificante e non da unificare
//      FascicoloSigeEstesoModel lFasDaUnificare = lCtrl.ExVerificaFascicoloSigePerUnificazione(String.valueOf(fascicoloSelezionato.getFascicoloSige().getChiaveAnno()), String.valueOf(fascicoloSelezionato.getFascicoloSige().getChiaveProgr()), 
//			                                   super.getCodUfficioUtenteConnesso(), RUOLO_FASCICOLO_DA_UNIFICARE );
      FascicoloSigeEstesoModel lFasDaUnificare = lCtrl.ExVerificaFascicoloSigePerUnificazione(String.valueOf(fascicoloSelezionato.getFascicoloSige().getChiaveAnno()), String.valueOf(fascicoloSelezionato.getFascicoloSige().getChiaveProgr()), 
              super.getCodUfficioUtenteConnesso(), RUOLO_FASCICOLO_UNIFICANTE );
      // Ticket#2021032201 - FINE
      TenoreSigeModel lTenModUnificato=new TenoreSigeModel();
      lTenModUnificato.setFasIdFascicoloSige(lFasDaUnificare.getFascicoloSige().getIdFascicoloSige());
      ITenoreSige lCtrlTS = SIGELookupRemote.getTenoreSigeRemote();
      Vector <TenoreSigeModel>lTenoriUnificato = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificato);
    
      if (lTenoriUnificato == null || lTenoriUnificato.size() == 0)
  	      throw new SIGEException (SIGEException.USER_MESSAGE, "Procedimento da Unificare privo di Oggetti. Azione non consentita.");
    
      RedirectTo redirect = new RedirectTo();
      redirect.setPage(IWebConstants.PG_MAIN);
      //redirect.setAction("siap.sige.decretounificazione.action.ActLoadInserisciDecretoUnificazioneSige");
     /* 
	 * ISSUE MEV : redirect action
	 * Numero MEV : 15_S4
	 * Autore    : sessa
	 * Data      : 26/gen/2016
	 * Branch    : MEV_15_S4
	 */
      String azioneChiamante = "";
      if (!isRequestParameterNullObj("azioneChiamante")) {
    	  azioneChiamante = getRequestStringParameter("azioneChiamante");
      }
      if(azioneChiamante != "" && azioneChiamante.equals("verbale")){
    	  redirect.setAction("siap.sige.unificazione.action.ActLoadInserisciVerbaleUnificazioneSige");
      } else {
    	  redirect.setAction("siap.sige.decretounificazione.action.ActLoadInserisciDecretoUnificazioneSige");
      }
  	//***** FINE INTERVENTO MEV_15_S4 *****//
      redirect.setParameter(IWebConstants.LINK_RITORNO, "20");
      return redirect.toString();
  }
}