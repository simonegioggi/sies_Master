package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.util.SigeMaggiorenniUtil;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadRicercaFascicoloSige
 * </p>
 * <p>
 * Description: Classe Action per la visualizzazione della maschera di Ricerca Fascicolo SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 5.0
 */
public class ActRicercaFascicoloSige extends ActionSige implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lTipoRicerca;
		String lCodUfficio;

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
		FascicoloSigeEstesoModel lFasEst = null;
		String lRetPage = "";

		// Viene istanziato il controller per la ricerca
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();

		// Determinazione Codice Ufficio

		lCodUfficio = getCodUfficioAccorpato();

		if (lCodUfficio.equals("0"))
			lCodUfficio = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_TIPO_UFFICIO), getRequestStringParameter(CAMPO_SEDE));

		lFascicolo.setChiaveUfficio(lCodUfficio);

		// Determinazione del tipo di ricerca selezionato
		lTipoRicerca = getRequestStringParameter(RADIO_TIPO_RICERCA);

		if ("B".equalsIgnoreCase(lTipoRicerca)) {
			// Valorizzazione dei criteri di ricerca
			lFascicolo.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
			lFascicolo.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_ORIGIN));

			lFasEst = lCtrl.ExRicercaFascicoloSigeByAnnoNumCodUfficio(lFascicolo);
			lFascicolo = lFasEst.getFascicoloSige();
			if (lFascicolo == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Fascicolo trovato !");

			// MEV_57: aggiunto controllo sull'età del soggetto (per uffici maggiorenni)
			// 28/01/2019 (errori rilevati in fase di test operativi da NUNZIA) per soggetto IGNOTO deve
			// bypassare il controllo
			boolean isSoggettoIgnoto = ("IGNOTO".equals(lFasEst.getSoggetto().getCognome())
					&& "IGNOTO".equals(lFasEst.getSoggetto().getNome()));
			String codTipoUfficio = getCodTipoUfficioConnesso();
			if (!isSoggettoIgnoto && !("CAPSM".equals(codTipoUfficio) || "DIBM".equals(codTipoUfficio)
					|| "GIPM".equals(codTipoUfficio) || "GUPM".equals(codTipoUfficio))) {
				if (!SigeMaggiorenniUtil.checkMinorenne(lFasEst))
					throw new F3BException(F3BException.USER_MESSAGE,
							"Il soggetto associato al fascicolo ricercato risulta minorenne!");
			}

			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.sige.fascicolo.action.ActLoadDettaglioFascicolo");
			lRedirigi.setParameter(CAMPO_ID_FASCICOLO_SIGE,
					(lFascicolo.getIdFascicoloSige() != null ? lFascicolo.getIdFascicoloSige().toString()
							: ""));
			lRetPage = lRedirigi.toString();
		} else if ("A".equalsIgnoreCase(lTipoRicerca)) {
			RicercaFascicoloSigeModel lRicercaFascicolo = new RicercaFascicoloSigeModel();
			// Valorizzazione dei criteri di ricerca
			lRicercaFascicolo.setChiaveUfficio(lCodUfficio);

			if (!isRequestParameterNullObj(CAMPO_CHIAVE_ACCORPATO)) {
				String ufficioAccorpato = getRequestStringParameter(CAMPO_CHIAVE_ACCORPATO);
				String[] parts = ufficioAccorpato.split("-");
				if (parts.length > 1 && parts[1] != null && !parts[1].equals("")) {
					lRicercaFascicolo.setChiaveUfficioInserimento(parts[1]);
				} else {
					lRicercaFascicolo.setChiaveUfficioInserimento(lCodUfficio);
				}
			}

			if (!isRequestParameterNullObj(CAMPO_ANNO_INI))
				lRicercaFascicolo.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_ANNO_INI));
			if (!isRequestParameterNullObj(CAMPO_NUM_INI))
				lRicercaFascicolo.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_NUM_INI));
			if (!isRequestParameterNullObj(CAMPO_ANNO_FINE))
				lRicercaFascicolo.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_ANNO_FINE));
			if (!isRequestParameterNullObj(CAMPO_NUM_FINE))
				lRicercaFascicolo.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_NUM_FINE));
			if (!isRequestParameterNullObj(CAMPO_GIORNO_INIZIALE)
					&& !isRequestParameterNullObj(CAMPO_MESE_INIZIALE)
					&& !isRequestParameterNullObj(CAMPO_ANNO_INIZIALE)) {
				lRicercaFascicolo.setDataIscrizioneIniziale(
						getRequestDateParameter(ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE,
								ICostantiFascicoloSige.CAMPO_MESE_INIZIALE,
								ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE));
			}
			if (!isRequestParameterNullObj(CAMPO_GIORNO_FINALE)
					&& !isRequestParameterNullObj(CAMPO_MESE_FINALE)
					&& !isRequestParameterNullObj(CAMPO_ANNO_FINALE)) {
				lRicercaFascicolo.setDataIscrizioneFinale(getRequestDateParameter(
						ICostantiFascicoloSige.CAMPO_ANNO_FINALE, ICostantiFascicoloSige.CAMPO_MESE_FINALE,
						ICostantiFascicoloSige.CAMPO_GIORNO_FINALE));
			}

			if (!super.isRequestParameterNullEmptyObj(CAMPO_SEZ_ID_SEZIONE)) {
				String sezione = super.getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE);
				if (sezione.equalsIgnoreCase("Tutte"))
					sezione = "0";
				if (sezione.equalsIgnoreCase("Nessuna"))
					sezione = "1";
				lRicercaFascicolo.setIdSezione(new BigDecimal(sezione));
			}

			// MEV_57: aggiunto parametro di passaggio nella Ricerca
			Vector lVect = lCtrl.ExRicercaFascicoloSigeEstesaPagina(lRicercaFascicolo,
					Integer.parseInt(lPagina), checkMinori());

			String lReturnPage = "";

			if (lVect.size() == 1) {
				FascicoloSigeEstesoModel lFasEst_e = (FascicoloSigeEstesoModel) lVect.get(0);

				lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIGE
						+ "=" + lFasEst_e.getFascicoloSige().getIdFascicoloSige().toString();
			} else {
				// Punto di ritorno
				setLinkRitorno();

				// Paginazione
				BigDecimal CountRisultati;
				if (isRequestParameterNullObj("CountRisultati")) {
					// MEV_57: aggiunto parametro di passaggio nella Ricerca
					CountRisultati = lCtrl.ExGetNumRicercaFascicoloSigeEstesa(lRicercaFascicolo,
							checkMinori());
				} else
					CountRisultati = getRequestBigDecimalParameter("CountRisultati");

				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("fascicoli", lVect);
				lReturnPage = ICostantiFascicoloSige.PG_RICERCAFASCICOLOSIGE_PERNUMERO;
			}
			// restituisce la jsp di VIEW
			return lReturnPage;
		}

		if (!isRequestParameterNullObj(CAMPO_AZIONE_CHIAMANTE)
				&& !getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE).equals("")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE));
			lRedirigi.setParameter(CAMPO_ID_FASCICOLO_SIGE,
					(lFascicolo.getIdFascicoloSige() != null ? lFascicolo.getIdFascicoloSige().toString()
							: ""));
			lRetPage = lRedirigi.toString();
		}

		// restituisce la jsp di VIEW
		return lRetPage;
	}

	private String getCodUfficioAccorpato() throws F3BException {

		String lCodUfficio = super.getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO);
		if (lCodUfficio.indexOf("-") != -1)
			lCodUfficio = lCodUfficio.split("-")[1];

		return lCodUfficio;
	}

}