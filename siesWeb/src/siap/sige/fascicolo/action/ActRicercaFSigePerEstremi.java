package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.richiesta.action.ICostantiRichiestaSige;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.tenore.action.ICostantiTenoreSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaFSigePerEstremi
 * </p>
 * <p>
 * Description: Classe Action per la visualizzazione della maschera di Ricerca Fascicolo SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class ActRicercaFSigePerEstremi extends ActionSige implements ICostantiFascicoloSige {

	public String processRequest() throws Exception {

//		String lCodUfficio;
		this.setLinkRitorno();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

//		String lRetPage = "";

		// Viene istanziato il controller per la ricerca
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();

		RicercaFascicoloSigeModel lRicercaFascicolo = letturaParametriRicerca();

		// Ricerca
		Vector<FascicoloSigeEstesoModel> lVect = null;
		lVect = getElencoFascicoliSige(lRicercaFascicolo, lPagina);

		String lReturnPage = "";

		if (lVect.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN
					+ "?"
					+ IWebConstants.ACTION_FIELD
					+ "=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ CAMPO_ID_FASCICOLO_SIGE
					+ "="
					+ ((FascicoloSigeEstesoModel) lVect.get(0)).getFascicoloSige().getIdFascicoloSige()
							.toString();
		} else {
			// Punto di ritorno
			setLinkRitorno();

			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetNumRicercaFascicoloSigeByEstremi(lRicercaFascicolo);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("fascicoli", lVect);
			lReturnPage = ICostantiFascicoloSige.PG_RICERCAFASCICOLOSIGE_PERESTREMI;
		}

		return lReturnPage; // restituisce la jsp di VIEW
	}

	protected Vector<FascicoloSigeEstesoModel> getElencoFascicoliSige(
			RicercaFascicoloSigeModel lFascicoloSigeMod, String lPagina) throws F3BException {

		// Viene istanziato il controller per la ricerca
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();

		Vector<FascicoloSigeEstesoModel> lFascicoli = null;

		lFascicoli = lCtrl
				.ExRicercaFascicoloSigeByEstremiPagina(lFascicoloSigeMod, Integer.parseInt(lPagina));

		return lFascicoli;
	}

	protected RicercaFascicoloSigeModel letturaParametriRicerca() throws Exception {

		RicercaFascicoloSigeModel lRicercaFascicolo = new RicercaFascicoloSigeModel();
		String lTipoRicerca;

		// Valorizzazione dei criteri di ricerca
		lRicercaFascicolo.setChiaveUfficio(this.getCodUfficioUtenteConnesso());

		if ((!isRequestParameterNullObj(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO))
				&& (!getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO).equals("-")))
			lRicercaFascicolo
					.setCodTipoAtto(getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO));
		if ((!isRequestParameterNullObj(ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE))
				&& (!getRequestStringParameter(ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE).equals("Tutti")))
			lRicercaFascicolo
					.setCodOggettoSige(getRequestStringParameter(ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE));

		if (!isRequestParameterNullObj(CAMPO_COD_MAG_ASS))
			if (getRequestStringParameter(CAMPO_COD_MAG_ASS).equals("-"))
				lRicercaFascicolo.setCodMagistrato(null);
			else if (getRequestStringParameter(CAMPO_COD_MAG_ASS).equals("Tutti"))
				lRicercaFascicolo.setCodMagistrato("9");
			else if (getRequestStringParameter(CAMPO_COD_MAG_ASS).equals("Nessuno"))
				lRicercaFascicolo.setCodMagistrato("0");
			else
				lRicercaFascicolo.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAG_ASS));

		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE))
			if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("-"))
				lRicercaFascicolo.setIdSezione(null);
			else if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("Tutte"))
				lRicercaFascicolo.setIdSezione(new BigDecimal("9"));
			else if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("Nessuna"))
				lRicercaFascicolo.setIdSezione(new BigDecimal("0"));
			else
				lRicercaFascicolo.setIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_INIZIALE))
			lRicercaFascicolo.setDataIscrizioneIniziale(getRequestDateParameter(
					CAMPO_ANNO_ISCRIZIONE_INIZIALE, CAMPO_MESE_ISCRIZIONE_INIZIALE,
					CAMPO_GIORNO_ISCRIZIONE_INIZIALE));
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_FINALE))
			lRicercaFascicolo.setDataIscrizioneFinale(getRequestDateParameter(CAMPO_ANNO_ISCRIZIONE_FINALE,
					CAMPO_MESE_ISCRIZIONE_FINALE, CAMPO_GIORNO_ISCRIZIONE_FINALE));

		// 30/11/2009 Aggiunto filtro per Tipo Rito.
		lRicercaFascicolo.setCodTipoRito("-");
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_GIUDIZIO)) {
			if (getRequestStringParameter(CAMPO_COD_TIPO_GIUDIZIO).equals("-"))
				lRicercaFascicolo.setCodTipoRito("-");
			else if (getRequestStringParameter(CAMPO_COD_TIPO_GIUDIZIO).equals("Mancante")) {
				lRicercaFascicolo.setCodTipoRito("N");
				setRequestAttribute("tipoRito", "Mancante");
			} else if (getRequestStringParameter(CAMPO_COD_TIPO_GIUDIZIO).equals("Tutti")) {
				lRicercaFascicolo.setCodTipoRito("T");
				setRequestAttribute("tipoRito", "Monocratico e Collegiale");
			} else if (getRequestStringParameter(CAMPO_COD_TIPO_GIUDIZIO).equals("C")) {
				lRicercaFascicolo.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_GIUDIZIO));
				setRequestAttribute("tipoRito", "Collegiale");
			} else if (getRequestStringParameter(CAMPO_COD_TIPO_GIUDIZIO).equals("M")) {
				lRicercaFascicolo.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_GIUDIZIO));
				setRequestAttribute("tipoRito", "Monocratico");
			}
		}

		// Determinazione del tipo di ricerca selezionato (Tutti / solo Pendenti / solo Definiti)
		lTipoRicerca = getRequestStringParameter(RADIO_TIPO_RICERCA);

		if (lTipoRicerca.equalsIgnoreCase("P")) {
			if (!isRequestParameterNullObj(CAMPO_GIORNO_FINE_PENDENZA)
					&& !isRequestParameterNullObj(CAMPO_MESE_FINE_PENDENZA)
					&& !isRequestParameterNullObj(CAMPO_ANNO_FINE_PENDENZA))
				lRicercaFascicolo.setDataFinePendenza(getRequestDateParameter(CAMPO_ANNO_FINE_PENDENZA,
						CAMPO_MESE_FINE_PENDENZA, CAMPO_GIORNO_FINE_PENDENZA));
		} else if (lTipoRicerca.equalsIgnoreCase("D")) {
			if (!isRequestParameterNullObj(CAMPO_GIORNO_DEFINIZIONE_INIZIALE)
					&& !isRequestParameterNullObj(CAMPO_MESE_DEFINIZIONE_INIZIALE)
					&& !isRequestParameterNullObj(CAMPO_ANNO_DEFINIZIONE_INIZIALE))
				lRicercaFascicolo.setDataDefinizioneIniziale(getRequestDateParameter(
						CAMPO_ANNO_DEFINIZIONE_INIZIALE, CAMPO_MESE_DEFINIZIONE_INIZIALE,
						CAMPO_GIORNO_DEFINIZIONE_INIZIALE));
			if (!isRequestParameterNullObj(CAMPO_GIORNO_DEFINIZIONE_FINALE)
					&& !isRequestParameterNullObj(CAMPO_MESE_DEFINIZIONE_FINALE)
					&& !isRequestParameterNullObj(CAMPO_ANNO_DEFINIZIONE_FINALE))
				lRicercaFascicolo.setDataDefinizioneFinale(getRequestDateParameter(
						CAMPO_ANNO_DEFINIZIONE_FINALE, CAMPO_MESE_DEFINIZIONE_FINALE,
						CAMPO_GIORNO_DEFINIZIONE_FINALE));
		}

		// Passaggio parametri di ricerca per visualizzarli nell'elenco.
		if (lRicercaFascicolo.getDataIscrizioneIniziale() != null)
			setRequestAttribute("dataIscrizioneInizio",
					getRequestStringParameter(CAMPO_GIORNO_ISCRIZIONE_INIZIALE) + "/"
							+ getRequestStringParameter(CAMPO_MESE_ISCRIZIONE_INIZIALE) + "/"
							+ getRequestStringParameter(CAMPO_ANNO_ISCRIZIONE_INIZIALE));

		if (lRicercaFascicolo.getDataIscrizioneFinale() != null)
			setRequestAttribute("dataIscrizioneFine",
					getRequestStringParameter(CAMPO_GIORNO_ISCRIZIONE_FINALE) + "/"
							+ getRequestStringParameter(CAMPO_MESE_ISCRIZIONE_FINALE) + "/"
							+ getRequestStringParameter(CAMPO_ANNO_ISCRIZIONE_FINALE));

		if (lRicercaFascicolo.getDataDefinizioneIniziale() != null)
			setRequestAttribute("dataDefinizioneInizio",
					getRequestStringParameter(CAMPO_GIORNO_DEFINIZIONE_INIZIALE) + "/"
							+ getRequestStringParameter(CAMPO_MESE_DEFINIZIONE_INIZIALE) + "/"
							+ getRequestStringParameter(CAMPO_ANNO_DEFINIZIONE_INIZIALE));

		if (lRicercaFascicolo.getDataDefinizioneFinale() != null)
			setRequestAttribute("dataDefinizioneFine",
					getRequestStringParameter(CAMPO_GIORNO_DEFINIZIONE_FINALE) + "/"
							+ getRequestStringParameter(CAMPO_MESE_DEFINIZIONE_FINALE) + "/"
							+ getRequestStringParameter(CAMPO_ANNO_DEFINIZIONE_FINALE));

		if (lRicercaFascicolo.getDataFinePendenza() != null)
			setRequestAttribute("dataFinePendenza", getRequestStringParameter(CAMPO_GIORNO_FINE_PENDENZA)
					+ "/" + getRequestStringParameter(CAMPO_MESE_FINE_PENDENZA) + "/"
					+ getRequestStringParameter(CAMPO_ANNO_FINE_PENDENZA));

		if (getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO).compareTo("-") != 0)
			setRequestAttribute("tipoAtto",
					DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoAttoSige(),
							getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO)));

		if (getRequestStringParameter(ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE).toLowerCase().compareTo(
				"tutti") != 0)
			setRequestAttribute("codOggetto", DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getOggettoSige(), getRequestStringParameter(ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE)));

		if (getRequestStringParameter(CAMPO_COD_MAG_ASS).toLowerCase().compareTo("-") != 0) {
			if (getRequestStringParameter(CAMPO_COD_MAG_ASS).toLowerCase().compareTo("tutti") == 0)
				setRequestAttribute("codMagistrato", "Tutti i magistrati");
			else if (getRequestStringParameter(CAMPO_COD_MAG_ASS).toLowerCase().compareTo("nessuno") == 0)
				setRequestAttribute("codMagistrato", "Magistrato non presente");
			else {
				IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
				MagistratoModel lMagistrato = lMagCtrl
						.ExRicercaMagistratoByCod(getRequestStringParameter(CAMPO_COD_MAG_ASS));
				String descMagistrato = lMagistrato.getCognome() + " " + lMagistrato.getNome() + " - "
						+ lMagistrato.getCodMagistrato();
				setRequestAttribute("codMagistrato", descMagistrato);
			}
		}

		if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).toLowerCase().compareTo("-") != 0) {
			if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).toLowerCase().compareTo("tutte") == 0)
				setRequestAttribute("descrSezione", "Tutte le sezioni");
			else if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).toLowerCase().compareTo("nessuna") == 0)
				setRequestAttribute("descrSezione", "Sezione non presente");
			else {
				ISezione lSezCtrl = SIGELookupRemote.getSezioneRemote();
				SezioneModel lSezione = lSezCtrl.ExRicercaSezioneByKey(new BigDecimal(
						getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE)));
				String descSezione = lSezione.getDescrizione();
				setRequestAttribute("descrSezione", descSezione);
			}
		}

		return lRicercaFascicolo;
	}

}