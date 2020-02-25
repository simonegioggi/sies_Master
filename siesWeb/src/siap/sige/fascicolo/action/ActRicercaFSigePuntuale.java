package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActRicercaFSigePuntuale
 * </p>
 * <p>
 * Description: Classe Action per la ricerca puntuale del Fascicolo SIGE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaFSigePuntuale extends ActionSige implements ICostantiFascicoloSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected UdienzaSigeModel getUdienzaSige(String aIdUdienzaSige) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inizio");
		// ==========================================
		// Recupera i dati del record
		// ==========================================
		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel lUdiMod = lCtrl.ExRicercaUdienzaSigeById(new BigDecimal(aIdUdienzaSige));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Lettura di lUdiMod : " + lUdiMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("fine");
		return lUdiMod;
	}

	protected FascicoloSigeEstesoModel getFascicoloSigeEsteso(FascicoloSigeModel lFascicolo)
			throws Exception {
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		FascicoloSigeEstesoModel lFasEst = lCtrl.ExRicercaEstesaFascicoloSigeByAnnoNumCodUfficio(lFascicolo);
		return lFasEst;
	}

	protected void reloadFascicoloSigeEsteso() throws Exception {
		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");
		FascicoloSigeModel fascInSession = lFasEsteso.getFascicoloSige();
		FascicoloSigeModel fascInSearch = new FascicoloSigeModel();
		fascInSearch.setChiaveUfficio(fascInSession.getChiaveUfficio());
		fascInSearch.setChiaveAnno(fascInSession.getChiaveAnno());
		fascInSearch.setChiaveProgr(fascInSession.getChiaveProgr());
		// complete = false;
		lFasEsteso = getFascicoloSigeEsteso(fascInSearch);
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);
	}

	@SuppressWarnings("rawtypes")
	protected boolean checkImpugnazioniProvvedimento(FascicoloSigeEstesoModel fasEsteso) throws F3BException {

		// Inizio gestione Accoglimento Opposizione:
		// nel caso in cui viene accolta, deve essere possibile fissare per lo stesso procedimento una nuova
		// udienza.
		// UDIENZA_PROCEDIMENTO_SIGE se trovato si ricava l'ID Udienza e l'ID Evento
		boolean esisteImpugnazioniProvvedimento = false; // false non esiste
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		// ricerca provvedimento
		ProvvedimentoSigeEventoModel lProvvedimento = lCtrlProv
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(
						fasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lProvvedimento != null && lProvvedimento.getProvvedimento() != null
				&& lProvvedimento.getProvvedimento().getIdProvvedimentoSige() != null) {
			IImpugnazioneSige ctrIS = SIGELookupRemote.getImpugnazioneSigeRemote();
			// ricerca impugnazione
			Vector lVectImpugnazioniProvvedimento = ctrIS.ExRicercaImpugnazioneByIdProvvedimentoSige(
					lProvvedimento.getProvvedimento().getIdProvvedimentoSige());
			if (lVectImpugnazioniProvvedimento != null) {
				Iterator itx = lVectImpugnazioniProvvedimento.iterator();
				while (itx.hasNext()) {
					ImpugnazioneSigeModel impugnazioniProvvedimentoModel = (ImpugnazioneSigeModel) itx.next();
					if ("07".equalsIgnoreCase(fasEsteso.getFascicoloSige().getCodStatoFascicolo()) && "10"
							.equalsIgnoreCase(impugnazioniProvvedimentoModel.getCodTenoreDecisione())) {
						// Opposizione accolta, non bloccare l'utente con un messaggio a video
						esisteImpugnazioniProvvedimento = true;
						break;
					}
				}
			}
		}
		// Fine gestione Accoglimento Opposizione:

		return esisteImpugnazioniProvvedimento;
	}

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		FascicoloSigeModel lFascicolo = new FascicoloSigeModel();

		// Determinazione Codice Ufficio
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String lCodUfficio = lUtenteConnesso.getUfficioUtente().getCodUfficio();
		lFascicolo.setChiaveUfficio(lCodUfficio);
		setRequestAttribute("CodiceUfficioUtente", lCodUfficio);

		// Valorizzazione dei criteri di ricerca
		// if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO)) {
		lFascicolo.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
		// }
		// if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR)) {
		lFascicolo.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
		// }

		// Ricerca
		FascicoloSigeEstesoModel lFasEst = getFascicoloSigeEsteso(lFascicolo);
		lFascicolo = lFasEst.getFascicoloSige();
		if (lFascicolo == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Fascicolo trovato !");

		// Prepara la "pagina" di destinAction
		String lRetPage = PG_LOAD_DETTAGLIOFASCICOLOSIGE;

		// Si mette in sessione il fascicolo SIGE.
		setSessionAttribute("FascicoloSigeEsteso", lFasEst);

		// Mette in sessione il fascicolo SIEP collegato.
		setSessionAttribute("fascicolo", lFasEst.getFascicoloSiep());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return lRetPage;
	}

}