package siap.sige.decretounificazione.action;

import java.util.Vector;

import siap.sige.SIGEException;
import siap.sige.decretounificazione.controller.IDecretoUnificazioneSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
* <p>Title: ActLoadInserisciDecretoUnificazioneSige</p>
* <p>Description: Classe Action per la load inserisci di Decreto Unificazione Sige</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadConfermaInserisciDecretoUnificazioneSige extends ActionSige implements ICostantiDecretoUnificazioneSige {
  public String processRequest() throws Exception {
	  // 16/01/2018 SC *** inizio ****
	  // Risolta anomalia su tasto 'Indietro' riscontrata durante fase di test di pre-collaudo
	  //super.setLinkRitorno();
	  super.removeSessionAttribute("StackDiRitorno");
	  // 16/01/2018 SC *** fine ****
	  IDecretoUnificazioneSige lCtrl = SIGELookupRemote.getDecretoUnificazioneSigeRemote();
      FascicoloSigeEstesoModel lFasDaUnificare = super.getFascicoloSigeEstesoInSessione();
      FascicoloSigeEstesoModel lFasUnificante  = lCtrl.ExVerificaFascicoloSigePerUnificazione(getRequestStringParameter( ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF), getRequestStringParameter( ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF), 
			                                                                                super.getCodUfficioUtenteConnesso(), RUOLO_FASCICOLO_DA_UNIFICARE );
      // Verifica la presenza di Oggetti nel fascicolo da Unificare
      TenoreSigeModel lTenModUnificato = new TenoreSigeModel();
      lTenModUnificato.setFasIdFascicoloSige(lFasDaUnificare.getFascicoloSige().getIdFascicoloSige());
      ITenoreSige lCtrlTS = SIGELookupRemote.getTenoreSigeRemote();
      Vector <TenoreSigeModel>lTenoriUnificato = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificato);
    
      if (lTenoriUnificato == null || lTenoriUnificato.size() == 0)
  	      throw new SIGEException (SIGEException.USER_MESSAGE, "Procedimento da Unificare privo di Oggetti. Azione non consentita.");
     
      TenoreSigeModel lTenModUnificante = new TenoreSigeModel();
      lTenModUnificante.setFasIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
      Vector <TenoreSigeModel>lTenoriUnificante = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificante);

      // Presentazione della FORM di inserimento Data di Unificazione.
      setRequestAttribute("fasDaUnificare", lFasDaUnificare);
      setRequestAttribute("fasUnificante", lFasUnificante);
      setRequestAttribute("tenoriUnificato", lTenoriUnificato);
      setRequestAttribute("tenoriUnificante", lTenoriUnificante);
      return PG_LOAD_INSERISCIDECRETOUNIFICAZIONESIGE; //restituisce la jsp di VIEW
  }
}