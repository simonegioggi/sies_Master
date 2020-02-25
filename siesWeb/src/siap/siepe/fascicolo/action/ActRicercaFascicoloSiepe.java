package siap.siepe.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeRicercaModel;
import siap.siepe.fascicolo.model.FascicoloSoggAttModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaFascicoloSiepe
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Fascicolo Siepe
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaFascicoloSiepe extends ActionSiap implements ICostantiFascicoloSiepe {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Istanzio il Model per la ricerca.
		FascicoloSiepeRicercaModel lFasSiepeRic = new FascicoloSiepeRicercaModel();

		// Diversificato il controllo dell'ufficio a seconda del tipo di ricerca.
		if ((!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO))
				&& (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals(""))) {
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();

			if (lUffCtrl.verifyUfficioByDescrComune((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)
					.toUpperCase())))
				lFasSiepeRic.setDescrComuneUfficio((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)
						.toUpperCase()));
		} else if ((!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO2))
				&& (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2).equals(""))) {
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();

			if (lUffCtrl.verifyUfficioByDescrComune((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2)
					.toUpperCase())))
				lFasSiepeRic.setDescrComuneUfficio((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2)
						.toUpperCase()));
		}

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lFasSiepeRic.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			lFasSiepeRic.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));

		if (!isRequestParameterNullObj(CAMPO_ANNO_UEPE))
			lFasSiepeRic.setAnnoUepe(getRequestBigDecimalParameter(CAMPO_ANNO_UEPE));
		if (!isRequestParameterNullObj(CAMPO_NUM_UEPE))
			lFasSiepeRic.setNumUepe(getRequestBigDecimalParameter(CAMPO_NUM_UEPE));
		if (!isRequestParameterNullObj(CAMPO_PROGR_UEPE))
			lFasSiepeRic.setProgrUepe(getRequestBigDecimalParameter(CAMPO_PROGR_UEPE));

		// Casi in cui viene valorizzato solo il Tipo Ufficio.
		if (!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO)
				&& (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO))
				&& (getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals(""))
				&& (!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO).equals("-")))
			lFasSiepeRic.setChiaveUfficio(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO));

		if (!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO2)
				&& (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO2))
				&& (getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2).equals(""))
				&& (!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO2).equals("-")))
			lFasSiepeRic.setChiaveUfficio(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO2));

		// Casi in cui viene valorizzato sia il tipo che la sede dell'Ufficio.
		// Si Utilizza il campo CodUfficioInserimento come veicolo per trasmettere il codice ufficio
		// recuperato dal tipo ufficio e dalla descr ufficio
		if (!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO)
				&& (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO))
				&& (!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO).equals("-"))
				&& (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals("")))
			lFasSiepeRic.setCodUfficioInserimento(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_CHIAVE_UFFICIO),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)));

		if (!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO2)
				&& (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO2))
				&& (getRequestStringParameter(CAMPO_CHIAVE_UFFICIO2).compareTo("-") != 0)
				&& (getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2).compareTo("") != 0))
			lFasSiepeRic.setCodUfficioInserimento(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_CHIAVE_UFFICIO2),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2)));

		// Ulteriori Parametri per la ricerca dei procedimenti SIEPE.
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasSiepeRic.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasSiepeRic.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lFasSiepeRic.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lFasSiepeRic.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE)) {
			lFasSiepeRic.setDataIscrizione(getRequestDateParameter(
					ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE,
					ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE,
					ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE));
		}
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_INIZIALE)) {
			lFasSiepeRic.setDataIscrizioneIniziale(getRequestDateParameter(
					ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE,
					ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE,
					ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE));

		}
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_FINALE)) {
			lFasSiepeRic.setDataIscrizioneFinale(getRequestDateParameter(
					ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE,
					ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE,
					ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE));
		}

		IFascicoloSiepe lCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();

		Vector lVect = null;
		// lVect = lCtrl.ExRicercaFascicoloSiusPagina( lFasGP, Integer.parseInt(lPagina) );
		lVect = lCtrl.ExRicercaFascicoloSiepePaginata(lFasSiepeRic, Integer.parseInt(lPagina));

		String lReturnPage = "";
		if (lVect.size() == 1) {
			// lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
			// "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"+CAMPO_ID_FASCICOLO_SIUS+"="+((FascicoloGPModel)lVect.get(0)).getFascicoloSiusModel().getIdFascicoloSius().toString();
			lReturnPage = IWebConstants.PG_MAIN
					+ "?"
					+ IWebConstants.ACTION_FIELD
					+ "=siap.siepe.fascicolo.action.ActLoadDettaglioFascicoloSiepe&"
					+ ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE
					+ "="
					+ ((FascicoloSoggAttModel) lVect.get(0)).getFascicoloSiepeRicercaModel()
							.getIdFascicoloSiepe().toString();
		} else {
			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				// CountRisultati = lCtrl.ExGetNumRicercaFascicoloSius(lFasGP);
				CountRisultati = lCtrl.ExGetNumRicercaFascicoloSiepe(lFasSiepeRic);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("fascicoli", lVect);

			// lReturnPage = ICostantiFascicoloSius.PG_RICERCAFASCICOLOSIUS_PERNUMERO;
			lReturnPage = ICostantiFascicoloSiepe.PG_RICERCAFASCICOLOSIEPE;
		}
		return lReturnPage; // restituisce la jsp di VIEW
	}

}