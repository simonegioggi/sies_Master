package siap.sige.decretounificazione.action;

//import java.util.Vector;
//import siap.sico.utente.model.UtenteModel;
//import siap.sico.security.action.ICostantiSecurity;
import siap.sige.web.ActionSige;

//import siap.sige.provvedimento.util.RicercaProvvedimentiUtil;
//import siap.sige.SIGEException;
//import siap.sige.tenore.controller.ITenoreSige;
//import siap.sige.tenore.model.TenoreSigeModel;
//import siap.sige.util.SIGELookupRemote;
//import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
//import siap.sige.decretounificazione.controller.IDecretoUnificazioneSige;

/**
* <p>Title: ActLoadInserisciDecretoUnificazioneSige</p>
* <p>Description: Classe Action per la load inserisci di Decreto Unificazione Sige</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciDecretoUnificazioneSige extends ActionSige implements ICostantiDecretoUnificazioneSige {
  public String processRequest() throws Exception {
    super.setLinkRitorno();
	//Recupero l'utente dalla sessione
    //UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    
    //TenoreSigeModel    lTenModUnificato = new TenoreSigeModel();
    //TenoreSigeModel    lTenModUnificante = new TenoreSigeModel();

    // Verifica della Unificabilità dei procedimenti indicati.

    // Se Unificabili, i 2 procedimenti vengono letti e passati alla request per presentarli a confronto.
    //IDecretoUnificazioneSige lCtrl = SIGELookupRemote.getDecretoUnificazioneSigeRemote();
    //FascicoloSigeEstesoModel lFasDaUnificare = lCtrl.ExVerificaFascicoloSigePerUnificazione(getRequestStringParameter( ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF), getRequestStringParameter( ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF), 
    //																						lUtenteMod.getUfficioUtente().getCodUfficio(), RUOLO_FASCICOLO_DA_UNIFICARE );
    //FascicoloSigeEstesoModel lFasUnificante  = lCtrl.ExVerificaFascicoloSigePerUnificazione(getRequestStringParameter( ICostantiDecretoUnificazioneSige.CAMPO_ANNO_UNIFICANTE), getRequestStringParameter( ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE), 
    //																						lUtenteMod.getUfficioUtente().getCodUfficio(), RUOLO_FASCICOLO_UNIFICANTE );

    // Verifica la presenza di Oggetti nel fascicolo da Unificare
    
    //lTenModUnificato.setFasIdFascicoloSige(lFasDaUnificare.getFascicoloSige().getIdFascicoloSige());
    //ITenoreSige lCtrlTS = SIGELookupRemote.getTenoreSigeRemote();
    //Vector lTenoriUnificato = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificato);
    
    //if (lTenoriUnificato == null || lTenoriUnificato.size() == 0)
  	//  throw new SIGEException (SIGEException.USER_MESSAGE, "Procedimento da Unificare privo di Oggetti. Azione non consentita.");
    
    //lTenModUnificante.setFasIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
    //Vector lTenoriUnificante = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificante);

    // Presentazione della FORM di inserimento Data di Unificazione.
    //setRequestAttribute("fasDaUnificare", lFasDaUnificare);
    //setRequestAttribute("fasUnificante", lFasUnificante);
    //setRequestAttribute("tenoriUnificato", lTenoriUnificato);
    //setRequestAttribute("tenoriUnificante", lTenoriUnificante);

    //return PG_LOAD_INSERISCIDECRETOUNIFICAZIONESIGE; //restituisce la jsp di VIEW
    
    return PG_LOAD_INSERISCIDECRETOUNIFICAZIONESIGE_FASCIOLO_UNIFICANTE;
  }
}