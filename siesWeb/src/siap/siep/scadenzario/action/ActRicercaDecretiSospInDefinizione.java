package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaScadenzario
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Scadenzario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaDecretiSospInDefinizione extends ActionSiap
		implements ICostantiScadenzario, ICostantiFascicoloSiep {

	/**
	 * Compone la Stringa completa di una request NON multipart e NON con array di valori...
	 * 
	 * @return
	 * @throws F3BException
	 */
	public String getCompleteRequestURL() throws F3BException {

		String lRequest = this.getRequest().getRequestURL() + "?";
		Set lKeys = getRequest().getParameterMap().keySet();

		Iterator itx = lKeys.iterator();

		while (itx.hasNext()) {
			String key = (String) itx.next();
			if (!(key.equals(IWebConstants.NUM_PAGE) || key.equals(IWebConstants.LINK_RITORNO)
					|| key.equals(IWebConstants.FLAG_RITORNO))) {
				if (!"tipoNotifica".equals(key)) {
					lRequest += key + "=" + getRequestStringParameter(key) + "&";
				} else {
					String[] lTipoNotifica = getRequestStringParameters(key);

					for (int i = 0; i < lTipoNotifica.length; i++) {
						lRequest += key + "=" + lTipoNotifica[i] + "&";
					}
				}
			}
		}

		return lRequest.substring(0, lRequest.length() - 1);
	}

	public String processRequest() throws Exception {
		ScadenzarioModel lScaMod = new ScadenzarioModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		}

		// Imposto fisso 01 = Simeone,
		lScaMod.setCodTipoScadenzario("01");
		lScaMod.setFlagVisto("N");
		lScaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ACCORPATO)) {
			String ufficioAccorpato = getRequestStringParameter(CAMPO_CHIAVE_ACCORPATO);
			String[] parts = ufficioAccorpato.split("-");
			if (parts.length > 1 && parts[1] != null && !parts[1].equals("")) {
				lScaMod.setCodUfficioInserimento(parts[1]);
			}
		}

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lScaMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lScaMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lScaMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lScaMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

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

		String[] lCodiciStatoNotifica = this.getRequestStringParameters("tipoNotifica");
		String[] lFiltro = new String[lCodiciStatoNotifica.length];

		for (int i = 0; i < lCodiciStatoNotifica.length; i++) {
			String lSelezione = lCodiciStatoNotifica[i];

			if ("Tutti".equals(lSelezione)) {
				lScaMod.setCodiciStatoNotifica(new String[] { "N", "A", "M", "I", "R", "O" });

				break;
			} else {
				if ("Condannato".equals(lSelezione)) {
					lFiltro[i] = "N";
				} else if ("Avvocato".equals(lSelezione)) {
					lFiltro[i] = "A";
				} else if ("Mancata".equals(lSelezione)) {
					lFiltro[i] = "M";
				} else if ("Sollecito".equals(lSelezione)) {
					lFiltro[i] = "O";
				} else if ("Richiesta".equals(lSelezione)) {
					lFiltro[i] = "I";
				} else if ("Rinnovazione".equals(lSelezione)) {
					lFiltro[i] = "R";
				}

				if (i == (lCodiciStatoNotifica.length - 1)) {
					lScaMod.setCodiciStatoNotifica(lFiltro);
				}
			}
		}

		IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountScadenzarioSimeone(lScaMod);
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		List lList = lCtrl.ExRicercaScadenzarioSimeonePaged(lScaMod, Integer.parseInt(lPagina));

		setRequestAttribute("scadenzario", lList);

		return PG_RICERCA_DECRETISOSP_IN_DEFINIZIONE;
	}

}