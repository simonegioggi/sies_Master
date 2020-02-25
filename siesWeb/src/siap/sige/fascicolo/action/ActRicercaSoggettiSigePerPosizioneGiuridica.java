package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import f3b.model.DecodeModel;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * MEV_65: aggiunta classe Action per la visualizzazione della maschera di Ricerca Soggetti SIGE per posizione
 * giuridica.
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActRicercaSoggettiSigePerPosizioneGiuridica extends ActionSige
		implements ICostantiFascicoloSige {

	public String processRequest() throws Exception {

		String pagine = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			pagine = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Viene istanziato il controller per la ricerca
		IFascicoloSige ifs = SIGELookupRemote.getFascicoloSigeRemote();

		RicercaFascicoloSigeModel rfsm = letturaParametriRicerca();
		setRequestAttribute("model", rfsm);
		setSessionAttribute("model", rfsm);

		// Ricerca soggetti per posizione giuridica
		Vector<FascicoloSigeEstesoModel> fascicoli = ifs.ExRicercaSoggettiSigePerPosizioneGiuridica(rfsm,
				Integer.parseInt(pagine));

		// Punto di ritorno
		// setLinkRitorno();

		// Paginazione
		BigDecimal countRisultati;
		if (isRequestParameterNullObj("CountRisultati"))
			countRisultati = ifs.ExGetNumRicercaSoggettiSigePerPosizioneGiuridica(rfsm,
					Integer.parseInt(pagine));
		else
			countRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", countRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, pagine);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		setRequestAttribute("fascicoli", fascicoli);

		// restituisce la jsp di VIEW
		return ICostantiFascicoloSige.PG_RICERCASOGGETTISIGE_PERPOSIZIONEGIURIDICA;
	}

	@SuppressWarnings("rawtypes")
	protected RicercaFascicoloSigeModel letturaParametriRicerca() throws Exception {

		RicercaFascicoloSigeModel rfsm = new RicercaFascicoloSigeModel();
		// Data Iniziale/Finale
		if (!isRequestParameterNullObj(CAMPO_GIORNO_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_INIZIALE))
			rfsm.setDataIscrizioneIniziale(
					getRequestDateParameter(CAMPO_ANNO_INIZIALE, CAMPO_MESE_INIZIALE, CAMPO_GIORNO_INIZIALE));
		if (!isRequestParameterNullObj(CAMPO_GIORNO_FINALE) && !isRequestParameterNullObj(CAMPO_MESE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_FINALE))
			rfsm.setDataIscrizioneFinale(
					getRequestDateParameter(CAMPO_ANNO_FINALE, CAMPO_MESE_FINALE, CAMPO_GIORNO_FINALE));
		// Anno/Numero Iniziale/Finale
		if (!isRequestParameterNullObj(CAMPO_ANNO_INI) && !isRequestParameterNullObj(CAMPO_NUM_INI)) {
			rfsm.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_ANNO_INI));
			rfsm.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_NUM_INI));
		}
		if (!isRequestParameterNullObj(CAMPO_ANNO_FINE) && !isRequestParameterNullObj(CAMPO_NUM_FINE)) {
			rfsm.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_ANNO_FINE));
			rfsm.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_NUM_FINE));
		}

		if (!isRequestParameterNullObj(CAMPO_COD_POSIZIONE_GIURIDICA))
			rfsm.setCodPosizioneGiuridica("" + getRequestBigDecimalParameter(CAMPO_COD_POSIZIONE_GIURIDICA));

		// Determinazione del tipo di ricerca selezionato (Tutti / solo Pendenti)
		String statoProcedimento = getRequestStringParameter(RADIO_TIPO_RICERCA);
		if ("P".equalsIgnoreCase(statoProcedimento)) {
			if (!isRequestParameterNullObj(CAMPO_GIORNO_FINE_PENDENZA)
					&& !isRequestParameterNullObj(CAMPO_MESE_FINE_PENDENZA)
					&& !isRequestParameterNullObj(CAMPO_ANNO_FINE_PENDENZA))
				rfsm.setDataFinePendenza(getRequestDateParameter(CAMPO_ANNO_FINE_PENDENZA,
						CAMPO_MESE_FINE_PENDENZA, CAMPO_GIORNO_FINE_PENDENZA));
		}

		// Magistrato
		if (!isRequestParameterNullObj(CAMPO_COD_MAG_ASS))
			if (getRequestStringParameter(CAMPO_COD_MAG_ASS).equals("-"))
				rfsm.setCodMagistrato("-");
			else if (getRequestStringParameter(CAMPO_COD_MAG_ASS).equals("Tutti"))
				rfsm.setCodMagistrato("9");
			else if (getRequestStringParameter(CAMPO_COD_MAG_ASS).equals("Nessuno"))
				rfsm.setCodMagistrato("0");
			else
				rfsm.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAG_ASS));

		// Sezione
		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE))
			if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("-"))
				rfsm.setIdSezione(null);
			else if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("Tutte"))
				rfsm.setIdSezione(new BigDecimal("9"));
			else if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("Nessuna"))
				rfsm.setIdSezione(new BigDecimal("0"));
			else
				rfsm.setIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));

		// Nazione
		if (!isRequestParameterNullObj(
				"nazionalita") /* && !"-".equals(getRequestStringParameter("nazionalita")) */) {
			if ("S".equals(getRequestStringParameter("nazionalita"))
					&& !"-".equals(getRequestStringParameter("IdNazione")))
				rfsm.setCodNazione(getRequestStringParameter("IdNazione"));
			else
				rfsm.setCodNazione(getRequestStringParameter("nazionalita"));
		}

		// solo i soggetti con fascicoli dell'ufficio connesso
		rfsm.setChiaveUfficio(getCodUfficioUtenteConnesso());
		rfsm.setChiaveUfficioInserimento(getCodUfficioUtenteConnesso());

		// Passaggio parametri di ricerca per visualizzarli nell'elenco.
		if (Utils.isPresent(rfsm.getCodPosizioneGiuridica())) {
			String descPosizioneGiuridica = "";
			switch (new Integer(rfsm.getCodPosizioneGiuridica()).intValue()) {
			case 1:
				descPosizioneGiuridica = "In espiazione pena in carcere";
				break;
			case 2:
				descPosizioneGiuridica = "In misura tutte";
				break;
			case 3:
				descPosizioneGiuridica = "Libero e assimilati";
				break;
			default:
				descPosizioneGiuridica = "Nessuna";
				break;
			}
			rfsm.setDescPosizioneGiuridica(descPosizioneGiuridica);
		}

		String descMagistrato = "-";
		if (getRequestStringParameter(CAMPO_COD_MAG_ASS).compareTo("-") != 0) {
			if (getRequestStringParameter(CAMPO_COD_MAG_ASS).toLowerCase().compareTo("tutti") == 0)
				descMagistrato = "Tutti i Magistrati";
			else if (getRequestStringParameter(CAMPO_COD_MAG_ASS).toLowerCase().compareTo("nessuno") == 0)
				descMagistrato = "Magistrato non presente";
			else {
				IMagistrato im = SICOLookupRemote.getMagistratoRemote();
				MagistratoModel mm = im
						.ExRicercaMagistratoByCod(getRequestStringParameter(CAMPO_COD_MAG_ASS));
				descMagistrato = mm.getCognome() + " " + mm.getNome() + " - " + mm.getCodMagistrato();
			}
		}
		rfsm.setDescMagistrato(descMagistrato);

		String descSezione = "-";
		if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).compareTo("-") != 0) {
			if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).toLowerCase().compareTo("tutte") == 0)
				descSezione = "Tutte le sezioni";
			else if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).toLowerCase().compareTo("nessuna") == 0)
				descSezione = "Sezione non presente";
			else {
				ISezione is = SIGELookupRemote.getSezioneRemote();
				SezioneModel sm = is.ExRicercaSezioneByKey(
						new BigDecimal(getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE)));
				descSezione = sm.getCodice() + " - " + sm.getDescrizione();
			}
		}
		rfsm.setDescSezione(descSezione);

		String descNazione = "-";
		if (Utils.isPresent(rfsm.getCodNazione()) && rfsm.getCodNazione().compareTo("-") != 0) {
			if ("039".equals(rfsm.getCodNazione()))
				descNazione = "Italiano";
			else if ("S".equals(rfsm.getCodNazione()))
				descNazione = ("Straniero");
			else {
				Collection nazioni = DecodificheManager.getInstance().getNazioni();
				Iterator i = nazioni.iterator();
				while (i.hasNext()) {
					DecodeModel dm = (DecodeModel) i.next();
					if (dm.getCode().equals(rfsm.getCodNazione())) {
						descNazione = dm.getDescription();
						break;
					}
				}
			}
		}
		rfsm.setDescNazione(descNazione);

		// valore di ritorno
		return rfsm;
	}

}