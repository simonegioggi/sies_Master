package siap.sius.depositodecreto.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.magistrato.controller.IMagistrato;
//import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
//import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
//import siap.sius.esperto.controller.IEsperto;
//import siap.sius.esperto.model.EspertoModel;
//import siap.sius.fascicolo.model.FascicoloGPModel;
//import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
//import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
//import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
//import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
//import siap.sius.util.SIUSLookupRemote;
//import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

public class ActLoadModificaMagistratoDecreto extends ActionSiap implements ICostantiDepositoDecreto {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("processRequest() - inizio");
		
		gestioneRitorno();
	    MagistratoModel lMagistrato = null;
	    String lactionDest = null;
	    UtenteModel lUtenteMod = null;
	    BigDecimal lIdEvento = null;
	    //Passa la action di destinazione


	    if (!isRequestParameterNullObj("acdest"))
	        lactionDest = getRequestStringParameter("acdest");

	    lIdEvento = getRequestBigDecimalParameter("IdEvento");
	    	    
	    //Utente
	    lUtenteMod = 
	    		new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
	    
	    // 20131130 - si preleva dal magistrato
	    // Magistrato
	    IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
	    lMagistrato = lMagCtrl.ExRicercaMagistratoByEvento(lIdEvento);
	    
	    setRequestAttribute("magistrato", lMagistrato);
	    setRequestAttribute("modalita", "M");
	    setRequestAttribute("acdest", lactionDest);
	    setRequestAttribute("utente", lUtenteMod);
	    setRequestAttribute("TornaQui", "10");
	    
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug("processRequest() - fine");

	    return PG_LOAD_MODIFICA_MAGISTRATO_DECRETO;
	}
}