package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * MEV_65: aggiunta classe Action per la visualizzazione della maschera di Procedimenti Sige Con Ricorso od
 * Opposizione.
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActRicercaProcedimentiSigeConRicorsoOpposizione extends ActionSige
		implements ICostantiFascicoloSige {

	public String processRequest() throws Exception {

		String pagine = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			pagine = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Viene istanziato il controller per la ricerca
		IFascicoloSige ifs = SIGELookupRemote.getFascicoloSigeRemote();

		RicercaFascicoloSigeModel rfsm = letturaParametriRicerca();
		setRequestAttribute("model", rfsm);
		setSessionAttribute("modelRO", rfsm);

		// Ricerca Procedimenti Sige Con Ricorso/Opposizione
		Vector<FascicoloSigeEstesoModel> fascicoli = ifs.ExRicercaProcedimentiSigeConRicorsoOpposizione(rfsm,
				Integer.parseInt(pagine));

		// Punto di ritorno
		setLinkRitorno();

		// Paginazione
		BigDecimal countRisultati;
		if (isRequestParameterNullObj("CountRisultati"))
			countRisultati = ifs.ExGetNumRicercaProcedimentiSigeConRicorsoOpposizione(rfsm,
					Integer.parseInt(pagine));
		else
			countRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", countRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, pagine);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		setRequestAttribute("fascicoli", fascicoli);

		// restituisce la jsp di VIEW
		return ICostantiFascicoloSige.PG_RICERCAPROCEDIMENTISIGE_CONRICORSOOPPOSIZIONE;
	}

	protected RicercaFascicoloSigeModel letturaParametriRicerca() throws Exception {

		RicercaFascicoloSigeModel rfsm = new RicercaFascicoloSigeModel();

		// Tipo Ricorso/Opposizione
		rfsm.setTipoRicorso(getRequestStringParameter("tipoRicorso"));
		// Passaggio parametri di ricerca per visualizzarli nell'elenco.
		if (Utils.isPresent(rfsm.getTipoRicorso())) {
			String descTipoRicorso = "";
			switch (new Integer(rfsm.getTipoRicorso()).intValue()) {
			case 1:
				descTipoRicorso = "Ricorso";
				break;
			case 2:
				descTipoRicorso = "Ricorso senza esito";
				break;
			case 3:
				descTipoRicorso = "Ricorso con esito";
				break;
			case 4:
				descTipoRicorso = "Opposizione";
				break;
			case 5:
				descTipoRicorso = "Opposizione senza esito";
				break;
			case 6:
				descTipoRicorso = "Opposizione con esito";
				break;
			default:
				descTipoRicorso = "Tutti";
				break;
			}
			rfsm.setDescTipoRicorso(descTipoRicorso);
		}

		// Anno/Numero Ricorso/Opposizione
		if (!isRequestParameterNullObj("annoRicorso") && !isRequestParameterNullObj("numeroRicorso")) {
			rfsm.setChiaveAnnoRicorso(getRequestBigDecimalParameter("annoRicorso"));
			rfsm.setChiaveProgrRicorso(getRequestBigDecimalParameter("numeroRicorso"));
		}

		// Anno/Numero Iniziale/Finale Estremi Ricorso/Opposizione
		if (!isRequestParameterNullObj(CAMPO_ANNO_INI) && !isRequestParameterNullObj(CAMPO_NUM_INI)) {
			rfsm.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_ANNO_INI));
			rfsm.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_NUM_INI));
		}
		if (!isRequestParameterNullObj(CAMPO_ANNO_FINE) && !isRequestParameterNullObj(CAMPO_NUM_FINE)) {
			rfsm.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_ANNO_FINE));
			rfsm.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_NUM_FINE));
		}

		// Data Iniziale/Finale arrivo in cancelleria
		if (!isRequestParameterNullObj(CAMPO_GIORNO_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_INIZIALE))
			rfsm.setDataArrivoCancelleriaIniziale(
					getRequestDateParameter(CAMPO_ANNO_INIZIALE, CAMPO_MESE_INIZIALE, CAMPO_GIORNO_INIZIALE));
		if (!isRequestParameterNullObj(CAMPO_GIORNO_FINALE) && !isRequestParameterNullObj(CAMPO_MESE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_FINALE))
			rfsm.setDataArrivoCancelleriaFinale(
					getRequestDateParameter(CAMPO_ANNO_FINALE, CAMPO_MESE_FINALE, CAMPO_GIORNO_FINALE));

		// Determinazione dello stato validazione selezionato (Tutti / non Annullati / Annullati)
		rfsm.setStatoValidazione(getRequestStringParameter(RADIO_TIPO_RICERCA));

		// solo i soggetti con fascicoli dell'ufficio connesso
		rfsm.setChiaveUfficio(getCodUfficioUtenteConnesso());
		rfsm.setChiaveUfficioInserimento(getCodUfficioUtenteConnesso());

		// valore di ritorno
		return rfsm;
	}

}