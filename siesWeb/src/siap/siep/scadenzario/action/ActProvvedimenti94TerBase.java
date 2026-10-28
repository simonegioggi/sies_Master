// 20261028-MEV_2026-105_INLAY - MEV Legge 140/2026: ricerca provvedimenti emessi ex art. 94 ter DPR 309/90
package siap.siep.scadenzario.action;

import java.util.Iterator;
import java.util.Set;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.scadenzario.model.ScadenzarioModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Base per le action di ricerca ed export dei provvedimenti emessi ex art. 94 ter DPR 309/90
 * (EVENTO con COD_MOTIVO 0760, 0761, 0762).
 */
public abstract class ActProvvedimenti94TerBase extends ActionSiap implements ICostantiScadenzario,
		ICostantiFascicoloSiep {

	protected static final String CAMPO_TIPO_PROVVEDIMENTO = "tipoNotifica";
	protected static final String CAMPO_DEFINITI = "estraiDefiniti";
	protected static final String[] MOTIVI_94TER = new String[] { "0760", "0761", "0762" };

	protected ScadenzarioModel buildFiltro() throws F3BException {
		ScadenzarioModel lScaMod = new ScadenzarioModel();
		lScaMod.setCodTipoScadenzario("01");
		lScaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE)) {
			lScaMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));
			lScaMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));
		}
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE)
				&& !isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE)) {
			lScaMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));
			lScaMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));
		}

		if (!isRequestParameterNullObj("GiornoEmissioneIniziale")
				&& !isRequestParameterNullObj("MeseEmissioneIniziale")
				&& !isRequestParameterNullObj("AnnoEmissioneIniziale")) {
			lScaMod.setDataEmissioneIniziale(getRequestDateParameter("AnnoEmissioneIniziale",
					"MeseEmissioneIniziale", "GiornoEmissioneIniziale"));
		}
		if (!isRequestParameterNullObj("GiornoEmissioneFinale")
				&& !isRequestParameterNullObj("MeseEmissioneFinale")
				&& !isRequestParameterNullObj("AnnoEmissioneFinale")) {
			lScaMod.setDataEmissioneFinale(getRequestDateParameter("AnnoEmissioneFinale",
					"MeseEmissioneFinale", "GiornoEmissioneFinale"));
		}

		// "Tutti" oppure uno o piu' motivi tra 0760, 0761, 0762
		String[] lSelezione = this.getRequestStringParameters(CAMPO_TIPO_PROVVEDIMENTO);
		String[] lMotivi = MOTIVI_94TER;
		if (lSelezione != null && lSelezione.length > 0) {
			boolean lTutti = false;
			StringBuilder lScelti = new StringBuilder();
			for (int i = 0; i < lSelezione.length; i++) {
				if ("Tutti".equals(lSelezione[i])) {
					lTutti = true;
				} else if ("0760".equals(lSelezione[i]) || "0761".equals(lSelezione[i])
						|| "0762".equals(lSelezione[i])) {
					lScelti.append(lScelti.length() > 0 ? "," : "").append(lSelezione[i]);
				}
			}
			if (!lTutti && lScelti.length() > 0)
				lMotivi = lScelti.toString().split(",");
		}
		lScaMod.setCodiciStatoNotifica(lMotivi);
		return lScaMod;
	}

	protected Boolean getDefiniti() throws F3BException {
		return Boolean.valueOf(!isRequestParameterNullObj(CAMPO_DEFINITI)
				&& "S".equals(getRequestStringParameter(CAMPO_DEFINITI)));
	}

	/** Ricostruisce la URL corrente (parametri multipli inclusi) per la paginazione. */
	@SuppressWarnings("rawtypes")
	public String getCompleteRequestURL() throws F3BException {
		String lRequest = this.getRequest().getRequestURL() + "?";
		Set lKeys = getRequest().getParameterMap().keySet();
		Iterator itx = lKeys.iterator();
		while (itx.hasNext()) {
			String key = (String) itx.next();
			if (!(key.equals(IWebConstants.NUM_PAGE) || key.equals(IWebConstants.LINK_RITORNO)
					|| key.equals(IWebConstants.FLAG_RITORNO) || key.equals("CountRisultati"))) {
				String[] lValori = getRequestStringParameters(key);
				for (int i = 0; lValori != null && i < lValori.length; i++) {
					lRequest += key + "=" + lValori[i] + "&";
				}
			}
		}
		return lRequest.substring(0, lRequest.length() - 1);
	}
}
