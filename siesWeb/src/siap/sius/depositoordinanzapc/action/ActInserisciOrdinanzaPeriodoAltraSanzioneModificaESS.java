package siap.sius.depositoordinanzapc.action;

import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.sanzionesostitutiva.util.InserisciPeriodoAltraSanzioneModificaESS;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

 /**
 * <p>Title: ActInserisciOrdinanzaPeriodoAltraSanzioneModificaESS</p>
 * <p>Description: Classe Action per l'inserimento dell'Emissione di un Ordinanaza, di un Periodo Altra Sanzione e modifica Esecuzione Sanzione Sostitutiva</p>
 * @version 1.0
 */

public class ActInserisciOrdinanzaPeriodoAltraSanzioneModificaESS extends ActInserisciOrdinanzaUDS
implements ICostantiDepositoDecreto
{
	public String processRequest() throws Exception
	{
		return super.processRequest();
	}

	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
	throws F3BException
	{
		
		EsecuzioneSanzioneSostitutivaModel lEssM = null; // Model Esecuzione Sanzione Sostitutiva
		OrdinanzaEventoTenoriGProcModel lModRet = null; 
		
		// Richiama la funzione di "util" per Inserire Periodo Altra Sanzione e Modifica Esecuzione Sanzione Sostitutiva
		InserisciPeriodoAltraSanzioneModificaESS lPASmESS = new InserisciPeriodoAltraSanzioneModificaESS();
		
		// passa alla funzione di "util" i dati della Request e della Session
		lPASmESS.setReqSes(this.getRequest(), this.getSession());
		
		// esegue la funzione per popolare i model del Periodo Altra Sanzione e Esecuzione Sanzione Sostitutiva
		PeriodoAltraSanzioneModel lPASMod = lPASmESS.caricaPeriodoAltraSanzioneModESS(this.mFasGPMod);
		// Carica model dell'Esecuzione Sanzione Sostitutiva
		lEssM = lPASmESS.getESS();
		
		// inserimento
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();		
		lModRet = IDepOrdCtrl.ExInserisciOrdinanzaPeriodoAltraSanzioneModificaESS(aOrdEveTenGP, lPASMod, lEssM);

		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,"NESSUN INSERIMENTO EFFETTUATO.");
		return lModRet;
	}
}