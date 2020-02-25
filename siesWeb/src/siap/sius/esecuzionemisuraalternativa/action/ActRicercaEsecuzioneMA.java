package siap.sius.esecuzionemisuraalternativa.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EMAFascGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaEsecuzioneMA
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaEsecuzioneMA extends ActionSiap implements ICostantiEsecuzioneMA {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Recupero dei parametri di ricerca.
		String lAnno = new String("");
		String lProgr = new String("");
		String lAnnoIniziale = new String("");
		String lProgrIniziale = new String("");
		String lAnnoFinale = new String("");
		String lProgrFinale = new String("");
		// 29/06/2009 Occorre impostare il Codice Ufficio del Fascicolo.
		String lCodUfficio = new String("");

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lAnno = getRequestStringParameter(CAMPO_CHIAVE_ANNO);

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			lProgr = getRequestStringParameter(CAMPO_CHIAVE_PROGR);

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lAnnoIniziale = getRequestStringParameter(CAMPO_CHIAVE_ANNO_INIZIALE);

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lProgrIniziale = getRequestStringParameter(CAMPO_CHIAVE_PROGR_INIZIALE);

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lAnnoFinale = getRequestStringParameter(CAMPO_CHIAVE_ANNO_FINALE);

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lProgrFinale = getRequestStringParameter(CAMPO_CHIAVE_PROGR_FINALE);

		lCodUfficio = getCodUfficioUtenteConnesso();

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO))
			lCodUfficio = getRequestStringParameter(CAMPO_CHIAVE_UFFICIO);

		String lReturnPage = "";
		IEsecuzioneMA lEseMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lEseMACtrl.ExGetNumRicercaEsecuzioneMisureAlternative(lAnno, lProgr,
					lAnnoIniziale, lProgrIniziale, lAnnoFinale, lProgrFinale, lCodUfficio);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		Vector lVect = lEseMACtrl.ExRicercaEsecuzioneMisureAlternative(lAnno, lProgr, lAnnoIniziale,
				lProgrIniziale, lAnnoFinale, lProgrFinale, lCodUfficio, Integer.parseInt(lPagina));

		if (lVect.size() == 1 && lAnno.length() > 1) {
			// Gestione del punto di ritorno
			this.gestioneRitorno();

			// Lettura dati
			EMAFascGPModel lEmaFasModel = ((EMAFascGPModel) lVect.get(0));

			// Esecuzione M A
			if (lEmaFasModel.getEsecuzioneMAModel() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Esecuzione Misura Alternativa assente !");
			BigDecimal lIdEsecuzioneMA = lEmaFasModel.getEsecuzioneMAModel().getIdEsecuzioneMisuraAlternati();

			// Fascicolo SIUS
			if (lEmaFasModel.getFascicoloSiusModel() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo SIUS assente !");
			BigDecimal lIdFascicoloSius = lEmaFasModel.getFascicoloSiusModel().getIdFascicoloSius();

			// ID Soggetto
			BigDecimal lIdSoggetto = lEmaFasModel.getFascicoloSiusModel().getSogIdSoggetto();

			// Anno e Progr
			String appAnno = "";
			String appProgr = "";
			if (lEmaFasModel.getFascicoloSiusModel().getChiaveAnno() != null)
				appAnno = lEmaFasModel.getFascicoloSiusModel().getChiaveAnno().toString();
			if (lEmaFasModel.getFascicoloSiusModel().getChiaveProgr() != null)
				appProgr = lEmaFasModel.getFascicoloSiusModel().getChiaveProgr().toString();

			// ID Fascicolo SIEP
			BigDecimal idFascicoloSIEP = null;
			if (lEmaFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
				idFascicoloSIEP = lEmaFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep();

			// Attivazione Dettaglio
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.sius.esecuzionemisuraalternativa.action.ActDettaglioEsecuzioneMA");
			lPage.setParameter(CAMPO_CHIAVE_ANNO, appAnno);
			lPage.setParameter(CAMPO_CHIAVE_PROGR, appProgr);
			lPage.setParameter(CAMPO_CHIAVE_UFFICIO, lCodUfficio); // 29/06/2009
			if (idFascicoloSIEP != null)
				lPage.setParameter(CAMPO_ID_FASCICOLO_SIEP, idFascicoloSIEP.toString());
			if (lIdEsecuzioneMA != null)
				lPage.setParameter(CAMPO_ID_ESECUZIONE_MA, lIdEsecuzioneMA.toString());
			if (lIdFascicoloSius != null)
				lPage.setParameter(CAMPO_ID_FASCICOLO_SIUS, lIdFascicoloSius.toString());
			if (lIdSoggetto != null)
				lPage.setParameter(CAMPO_ID_SOGGETTO, lIdSoggetto.toString());
			lPage.setParameter("TornaQui", "10");

			lReturnPage = lPage.toString();
			// setRequestAttribute("esecuzione", lVect.firstElement());
		} else {
			// Gestione del punto di ritorno
			this.setLinkRitorno();

			setRequestAttribute("esecuzioni", lVect);
			lReturnPage = PG_RICERCA_ESECUZIONE_MA;
		}
		return lReturnPage; // restituisce la jsp di VIEW
	}

}