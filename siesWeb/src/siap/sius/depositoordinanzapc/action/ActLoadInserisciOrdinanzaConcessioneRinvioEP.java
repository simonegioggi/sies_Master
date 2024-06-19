package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActLoadInserisciOrdinanzaConcessioneRinvioEP - Classe Action per la load inserisci di Emissione Ordinanza
 * Concessione Rinvio Esecuzione Pena La Action effettua la ricerca preliminare del decreto emesso
 * dall'Ufficio di Sorveglianza.
 *
 * @version 1.0
 */
public class ActLoadInserisciOrdinanzaConcessioneRinvioEP extends ActionSiap
		implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	IGeneraleProcedimento mGenProcCtrl = null;

	public String processRequest() throws Exception {

		FascicoloGPModel lFasGPMod = null;
		BigDecimal lIdFasOrigine = null;
		// Generale Procedimento del Fascicolo Origine
		GeneraleProcedimentoModel lGenProc1 = null;
		DepositoDecretoModel lDepDecMod = null;

		// Si preleva dalla sessione il fascicolo GPModel.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		lFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				"ActLoadInserisciOrdinanzaConcessioneRinvioEP: Inizio ricerca dei procedimenti di riferimento");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				"ID del Fascicolo corrente ->" + lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		// Se esiste il Fascicolo SIUS Origine si effettua la ricerca
		// dei provvedimenti di riferimento.
		// Il Fascicolo Origine rappresenta il procedimento emesso dal
		// Magistrato di Sorveglianza e trasferito al TdS.
		lIdFasOrigine = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
		if (lIdFasOrigine != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID del Fascicolo Origine ->" + lIdFasOrigine);
			// Ricerca del Generale Procedimento legato al Fascicolo Origine
			mGenProcCtrl = SIUSLookupRemote.getGeneraleProcedimentoRemote();
			lGenProc1 = mGenProcCtrl.ExRicercaGeneraleProcedimentoByFascicolo(lIdFasOrigine);
			if (lGenProc1 != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ID del Generale Procedimento ->" + lGenProc1.getIdGeneraleProcedimento());
				lDepDecMod = RicercaDecreto(lGenProc1.getIdGeneraleProcedimento());
				// Ricerca del Decreto
				if (lDepDecMod != null)
					setRequestAttribute("decreto", lDepDecMod);
				else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Deposito Decreto non trovato");
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Il fascicolo (id ->" + lIdFasOrigine
						+ ") non ha un Generale Procedimento collegato");
		} else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Il fascicolo (id ->" + lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius()
					+ ") non ha un Fascicolo Origine di riferimento");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				"ActLoadInserisciOrdinanzaConcessioneRinvioEP: Fine ricerca dei procedimenti di riferimento");

		return PG_LOAD_INSERISCI_ORDINANZA_CONC_RINVIO_EP;
	}

	// Ricerca del decreto emesso dall' Ufficio di Sorveglianza
	private DepositoDecretoModel RicercaDecreto(BigDecimal aIdGenProc) throws Exception {

		IDepositoDecreto lDepDecCtrl = null;
		DepositoDecretoModel lDepDecMod = null;
		IUfficio lUffCtrl = null;
		UfficioModel lUffMod = null;
		String lCodUff = null;

		lDepDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		lDepDecMod = lDepDecCtrl.ExRicercaDepositoDecretoByGenProc(aIdGenProc,
				ICostantiDepositoDecreto.RINVIO_ESECUZIONE_PENA);
		if (lDepDecMod != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID del Deposito Decreto ->" + lDepDecMod.getIdDepositoDecreto());
			// Viene ricavato il comune dall'ufficio di emissione
			lCodUff = lDepDecMod.getCodUfficioInserimento();
			if (lCodUff != null) {
				lUffCtrl = SICOLookupRemote.getUfficioRemote();
				lUffMod = lUffCtrl.getUfficioByKey(lCodUff);
				lDepDecMod.setDescrUfficioInserimento(lUffMod.getDescrComune());
			}
		}
		return lDepDecMod;
	}

}