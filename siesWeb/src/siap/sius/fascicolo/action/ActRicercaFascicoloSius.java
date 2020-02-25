package siap.sius.fascicolo.action;

/**
 * <p>Title: ActRicercaFascicolo</p>
 * <p>Description: Classe Action per la ricerca di Fascicolo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiusMinor;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActRicercaFascicoloSius extends ActionSiusMinor implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Istanzio il Model
		FascicoloGPModel lFasGP = new FascicoloGPModel();

		// Diversificato il controllo dell'ufficio a seconda del tipo di ricerca.
		if ((!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO))
				&& (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals(""))) {
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();

			if (lUffCtrl.verifyUfficioByDescrComune((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)
					.toUpperCase())))
				lFasGP.getFascicoloSiusModel().setDescrComuneUfficio(
						(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).toUpperCase()));
		} else if ((!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO2))
				&& (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2).equals(""))) {
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();

			if (lUffCtrl.verifyUfficioByDescrComune((getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2)
					.toUpperCase())))
				lFasGP.getFascicoloSiusModel().setDescrComuneUfficio(
						(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2).toUpperCase()));
		}

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lFasGP.getFascicoloSiusModel().setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			lFasGP.getFascicoloSiusModel().setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));

		// Casi in cui viene valorizzato solo il Tipo Ufficio.
		if (!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO)
				&& (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO))
				&& (getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals(""))
				&& (!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO).equals("-")))
			lFasGP.getFascicoloSiusModel().setChiaveUfficio(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO));

		if (!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO2)
				&& (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO2))
				&& (getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2).equals(""))
				&& (!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO2).equals("-")))
			lFasGP.getFascicoloSiusModel().setChiaveUfficio(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO2));

		// Casi in cui viene valorizzato sia il tipo che la sede dell'Ufficio.
		// Si Utilizza il campo CodUfficioInserimento come veicolo per trasmettere il codice ufficio
		// recuperato dal tipo ufficio e dalla descr ufficio
		if (!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO)
				&& (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO))
				&& (!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO).equals("-"))
				&& (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals("")))
			lFasGP.getFascicoloSiusModel().setCodUfficioInserimento(
					getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO),
							getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)));

		if (!isRequestParameterNullObj(CAMPO_DESCR_COMUNE_UFFICIO2)
				&& (!isRequestParameterNullObj(CAMPO_CHIAVE_UFFICIO2))
				&& (getRequestStringParameter(CAMPO_CHIAVE_UFFICIO2).compareTo("-") != 0)
				&& (getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2).compareTo("") != 0))
			lFasGP.getFascicoloSiusModel().setCodUfficioInserimento(
					getCodUfficioByCodTipoUfficioDescrComune(
							getRequestStringParameter(CAMPO_CHIAVE_UFFICIO2),
							getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO2)));

		// STUB 09/06/2004 Nuovi parametri per la ricerca dei procedimenti SIUS.
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasGP.getFascicoloSiusModel().setChiaveAnnoIniziale(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasGP.getFascicoloSiusModel().setChiaveProgrIniziale(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lFasGP.getFascicoloSiusModel().setChiaveAnnoFinale(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lFasGP.getFascicoloSiusModel().setChiaveProgrFinale(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE)) {
			lFasGP.getFascicoloSiusModel().setDataIscrizione(
					getRequestDateParameter(ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE,
							ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE,
							ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE));
		}
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_INIZIALE)) {
			lFasGP.getFascicoloSiusModel().setDataIscrizioneIniziale(
					getRequestDateParameter(ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE,
							ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE,
							ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE));

		}
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_FINALE)) {
			lFasGP.getFascicoloSiusModel().setDataIscrizioneFinale(
					getRequestDateParameter(ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE,
							ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE,
							ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE));
		}

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

		Vector lVect = null;
		// lVect = lCtrl.ExRicercaFascicoloSiusByProgrSius(lFasGP);
		// lVect = lCtrl.ExRicercaFascicoloSiusPagina( lFasGP, Integer.parseInt(lPagina) );
		lVect = lCtrl.ExRicercaFascicoloSiusPaginaMinori(lFasGP, Integer.parseInt(lPagina), checkMinori());

		String lReturnPage = "";

		if (lVect.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN
					+ "?"
					+ IWebConstants.ACTION_FIELD
					+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ CAMPO_ID_FASCICOLO_SIUS
					+ "="
					+ ((FascicoloGPModel) lVect.get(0)).getFascicoloSiusModel().getIdFascicoloSius()
							.toString();
		} else {
			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetNumRicercaFascicoloSius(lFasGP);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("fascicoli", lVect);

			lReturnPage = ICostantiFascicoloSius.PG_RICERCAFASCICOLOSIUS_PERNUMERO;
		}
		return lReturnPage; // restituisce la jsp di VIEW
	}

}