package siap.sius.depositoordinanzapc.action;

import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.misurasicurezza.util.InserisciPeriodoAltraMisuraModificaEMS;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

 /**
 * <p>Title: ActInserisciOrdinanzaPeriodoAltraMisuraModificaEMS</p>
 * <p>Description: Classe Action per l'inserimento dell'Emissione di un Ordinanaza, di un Periodo Altra Misura e modifica Esecuzione Misura Sicurezza</p>
 * @version 1.0
 */

public class ActInserisciOrdinanzaPeriodoAltraMisuraModificaEMS extends ActInserisciOrdinanzaUDS
implements ICostantiDepositoDecreto
{
	public String processRequest() throws Exception
	{
		return super.processRequest();
	}

	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
	throws F3BException
	{
		EsecuzioneMisuraSicurezzaModel lEmsM = null; // Model Esecuzione Misura Sicurezza
		OrdinanzaEventoTenoriGProcModel lModRet = null; 
		
		// Richiama la funzione di "util" per Inserire Periodo Altra Misura e Modifica Esecuzione Misura Sicurezza
		InserisciPeriodoAltraMisuraModificaEMS lPAMmEMS = new InserisciPeriodoAltraMisuraModificaEMS();
		
		// passa alla funzione di "util" i dati della Request e della Session
		lPAMmEMS.setReqSes(this.getRequest(), this.getSession());
		// esegue la funzione per popolare i model del Periodo Altra Misura e Esecuzione Misura Sicurezza
		PeriodoAltraMisuraModel lPAMMod = lPAMmEMS.caricaPeriodoAltraMisuraModEMS(this.mFasGPMod);
		// Carica model dell'Esecuzione Misura Sicurezza
		lEmsM = lPAMmEMS.getEMS();
		
		// inserimento
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();	
		lModRet = IDepOrdCtrl.ExInserisciOrdinanzaPeriodoAltraMisuraModificaEMS(aOrdEveTenGP, lPAMMod, lEmsM);
		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,"NESSUN INSERIMENTO EFFETTUATO.");
		return lModRet;
	}
}