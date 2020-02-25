package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActLoadEmissioneOrdinanzaUDS
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Emissione Ordinanza Revoca Misura Alternativa
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciOrdinanzaRevocaMA extends ActionSiap implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	IGeneraleProcedimento mGenProcCtrl = null;

	public String processRequest() throws Exception {

		FascicoloGPModel lFasGPMod = null;
		BigDecimal lIdFasOrigine = null;
		// Generale Procedimento del Fascicolo Origine
		GeneraleProcedimentoModel lGenProc1 = null;
		// Generale Procedimento capostipite, ovvero di Iscrizione Misura Alternative
		GeneraleProcedimentoModel lGenProc2 = null;
//		BigDecimal lIGenProc = null;
		DepositoDecretoModel lDepDecMod = null;
		EsecuzioneMisuraAlternativaModel lEsecMAModel = null;

		// Si preleva dalla sessione il fascicolo GPModel.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		lFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciOrdinanzaRevocaMA: Inizio ricerca dei procedimenti di riferimento");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ID del Fascicolo corrente ->"
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
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

				lGenProc2 = RicercaGeneraleProcedimentoMA(lGenProc1);
				if (lGenProc2 != null) {
					lEsecMAModel = ricercaMisuraAlternativa(lGenProc2.getIdGeneraleProcedimento());
					if (lEsecMAModel != null)
						setRequestAttribute("esecuzioneMA", lEsecMAModel);
					else
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Esecuzione Misura Alternativa non trovato");
				} else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Generale Procedimento Misura Alternativa non trovato!");
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
		siesLogger.debug("ActLoadInserisciOrdinanzaRevocaMA: Fine ricerca dei procedimenti di riferimento");

		return PG_LOAD_INSERISCI_ORDINANZA_REVOCA_MA;
	}

	// Ricerca dell' ESECUZIONE MISURA ALTERNATIVA per ID Generale Procedimento
	private EsecuzioneMisuraAlternativaModel ricercaMisuraAlternativa(BigDecimal aIdGenProc) throws Exception {

		IEsecuzioneMA lEsecMACtrl = null;
		EsecuzioneMisuraAlternativaModel lEsecMAModel = new EsecuzioneMisuraAlternativaModel();

		lEsecMAModel.setGenPridGeneraleProcedimento(aIdGenProc);
		lEsecMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
		Vector lEsecuzioni = lEsecMACtrl.ExRicercaEsecuzioneMisuraAlternativa(lEsecMAModel);
		if (lEsecuzioni != null && !(lEsecuzioni.isEmpty())) {
			lEsecMAModel = (EsecuzioneMisuraAlternativaModel) lEsecuzioni.get(0);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID del Esecuzione Misura Alternativa ->"
					+ lEsecMAModel.getIdEsecuzioneMisuraAlternati());
		} else
			lEsecMAModel = null;
		return lEsecMAModel;
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
				ICostantiDepositoDecreto.INOSSERVANZA_OBBLIGHI);
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

	// Ricerca del GENERALE PROCEDIMENTO capostipite:
	// stesso Anno/Prog, stesso ufficio e COD_OGGETTO_PROCEDIMENTO = U004
	// Ricerca del decreto emesso dall' Ufficio di Sorveglianza
	private GeneraleProcedimentoModel RicercaGeneraleProcedimentoMA(GeneraleProcedimentoModel aGenProc)
			throws Exception {

		GeneraleProcedimentoModel lGenProc = new GeneraleProcedimentoModel();
		lGenProc.setAnnoS1(aGenProc.getAnnoS1());
		lGenProc.setProgrS1(aGenProc.getProgrS1());
		lGenProc.setCodUfficioInserimento(aGenProc.getCodUfficioInserimento());
		lGenProc.setCodOggettoProcedimento("U004");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Criteri di Ricerca dell' Generale Procedimento: ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("AnnoS1 -> " + lGenProc.getAnnoS1());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ProgrS1 -> " + lGenProc.getProgrS1());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("CodUfficioInserimento -> " + lGenProc.getCodUfficioInserimento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("CodOggettoProcedimento -> " + lGenProc.getCodOggettoProcedimento());

		if (mGenProcCtrl == null)
			mGenProcCtrl = SIUSLookupRemote.getGeneraleProcedimentoRemote();
		Vector lGenProcedimenti = mGenProcCtrl.ExRicercaGeneraleProcedimento(lGenProc);

		if (lGenProcedimenti != null && !(lGenProcedimenti.isEmpty())) {
			lGenProc = (GeneraleProcedimentoModel) lGenProcedimenti.get(0);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID del Generale Procedimento Iniziale ->"
					+ lGenProc.getIdGeneraleProcedimento());
		} else
			lGenProc = null;
		return lGenProc;
	}

}