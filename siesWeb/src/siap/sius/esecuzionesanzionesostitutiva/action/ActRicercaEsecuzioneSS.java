package siap.sius.esecuzionesanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.ESSFascGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaEsecuzioneSS
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
public class ActRicercaEsecuzioneSS extends ActionSiap implements ICostantiEsecuzioneSS {

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
		IEsecuzioneSS lEseSSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lEseSSCtrl.ExGetNumRicercaEsecuzioneSanzioniSostitutive(lAnno, lProgr,
					lAnnoIniziale, lProgrIniziale, lAnnoFinale, lProgrFinale, lCodUfficio);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		Vector lVect = lEseSSCtrl.ExRicercaEsecuzioneSanzioniSostitutive(lAnno, lProgr, lAnnoIniziale,
				lProgrIniziale, lAnnoFinale, lProgrFinale, lCodUfficio, Integer.parseInt(lPagina));

		if (lVect.size() == 1 && lAnno.length() > 1) {
			// Gestione del punto di ritorno
			this.gestioneRitorno();

			// Lettura dati
			ESSFascGPModel lEssFasModel = ((ESSFascGPModel) lVect.get(0));

			// Esecuzione S.S.
			if (lEssFasModel.getEsecuzioneSSModel() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Esecuzione Sanzione Sostitutiva assente !");
			BigDecimal lIdEsecuzioneSS = lEssFasModel.getEsecuzioneSSModel().getIdEsecuzioneSanzioneSost();

			// Fascicolo SIUS
			if (lEssFasModel.getFascicoloSiusModel() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo SIUS assente !");
			BigDecimal lIdFascicoloSius = lEssFasModel.getFascicoloSiusModel().getIdFascicoloSius();

			// ID Soggetto
			BigDecimal lIdSoggetto = lEssFasModel.getFascicoloSiusModel().getSogIdSoggetto();

			// Anno e Progr
			String appAnno = "";
			String appProgr = "";
			if (lEssFasModel.getFascicoloSiusModel().getChiaveAnno() != null)
				appAnno = lEssFasModel.getFascicoloSiusModel().getChiaveAnno().toString();
			if (lEssFasModel.getFascicoloSiusModel().getChiaveProgr() != null)
				appProgr = lEssFasModel.getFascicoloSiusModel().getChiaveProgr().toString();

			// ID Fascicolo SIEP
			BigDecimal idFascicoloSIEP = null;
			if (lEssFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
				idFascicoloSIEP = lEssFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep();

			// Attivazione Dettaglio
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.sius.esecuzionesanzionesostitutiva.action.ActDettaglioEsecuzioneSS");
			lPage.setParameter(CAMPO_CHIAVE_ANNO, appAnno);
			lPage.setParameter(CAMPO_CHIAVE_PROGR, appProgr);
			lPage.setParameter(CAMPO_CHIAVE_UFFICIO, lCodUfficio); // 30/06/2009
			if (idFascicoloSIEP != null)
				lPage.setParameter(CAMPO_ID_FASCICOLO_SIEP, idFascicoloSIEP.toString());
			if (lIdEsecuzioneSS != null)
				lPage.setParameter(CAMPO_ID_ESECUZIONE_SS, lIdEsecuzioneSS.toString());
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
			lReturnPage = PG_RICERCA_ESECUZIONE_SS;
		}
		return lReturnPage; // restituisce la jsp di VIEW
	}

}