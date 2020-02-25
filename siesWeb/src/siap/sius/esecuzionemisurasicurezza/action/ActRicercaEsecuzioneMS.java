package siap.sius.esecuzionemisurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EMSFascGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaEsecuzioneMS
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaEsecuzioneMS extends ActionSiap implements ICostantiEsecuzioneMS {

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
		// 30/06/2009 Occorre impostare il Codice Ufficio del Fascicolo.
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
		IEsecuzioneMS lEseMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lEseMSCtrl.ExGetNumRicercaEsecuzioneMisureSicurezza(lAnno, lProgr,
					lAnnoIniziale, lProgrIniziale, lAnnoFinale, lProgrFinale, lCodUfficio);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		Vector lVect = lEseMSCtrl.ExRicercaEsecuzioneMisureSicurezza(lAnno, lProgr, lAnnoIniziale,
				lProgrIniziale, lAnnoFinale, lProgrFinale, lCodUfficio, Integer.parseInt(lPagina));

		if (lVect.size() == 1 && lAnno.length() > 1) {
			// Gestione del punto di ritorno
			this.gestioneRitorno();

			// Lettura dati
			EMSFascGPModel lEmsFasModel = ((EMSFascGPModel) lVect.get(0));

			// Esecuzione M.S.
			if (lEmsFasModel.getEsecuzioneMSModel() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Esecuzione Misura Sicurezza assente !");
			BigDecimal lIdEsecuzioneMS = lEmsFasModel.getEsecuzioneMSModel().getIdEsecuzioneMisuraSicurezza();

			// Fascicolo SIUS
			if (lEmsFasModel.getFascicoloSiusModel() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo SIUS assente !");
			BigDecimal lIdFascicoloSius = lEmsFasModel.getFascicoloSiusModel().getIdFascicoloSius();

			// ID Soggetto
			BigDecimal lIdSoggetto = lEmsFasModel.getFascicoloSiusModel().getSogIdSoggetto();

			// Anno e Progr
			String appAnno = "";
			String appProgr = "";
			if (lEmsFasModel.getFascicoloSiusModel().getChiaveAnno() != null)
				appAnno = lEmsFasModel.getFascicoloSiusModel().getChiaveAnno().toString();
			if (lEmsFasModel.getFascicoloSiusModel().getChiaveProgr() != null)
				appProgr = lEmsFasModel.getFascicoloSiusModel().getChiaveProgr().toString();

			// ID Fascicolo SIEP
			BigDecimal idFascicoloSIEP = null;
			if (lEmsFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
				idFascicoloSIEP = lEmsFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep();

			// Attivazione Dettaglio
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.sius.esecuzionemisurasicurezza.action.ActDettaglioEsecuzioneMS");
			lPage.setParameter(CAMPO_CHIAVE_ANNO, appAnno);
			lPage.setParameter(CAMPO_CHIAVE_PROGR, appProgr);
			lPage.setParameter(CAMPO_CHIAVE_UFFICIO, lCodUfficio); // 30/06/2009
			if (idFascicoloSIEP != null)
				lPage.setParameter(CAMPO_ID_FASCICOLO_SIEP, idFascicoloSIEP.toString());
			if (lIdEsecuzioneMS != null)
				lPage.setParameter(CAMPO_ID_ESECUZIONE_MS, lIdEsecuzioneMS.toString());
			if (lIdFascicoloSius != null)
				lPage.setParameter(CAMPO_ID_FASCICOLO_SIUS, lIdFascicoloSius.toString());
			if (lIdSoggetto != null)
				lPage.setParameter(CAMPO_ID_SOGGETTO, lIdSoggetto.toString());
			if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
				lPage.setParameter(IWebConstants.LINK_RITORNO, "10");

			lReturnPage = lPage.toString();
		} else {
			// Gestione del punto di ritorno
			this.setLinkRitorno();

			setRequestAttribute("esecuzioni", lVect);
			lReturnPage = PG_RICERCA_ESECUZIONE_MS;
		}
		return lReturnPage; // restituisce la jsp di VIEW
	}

}