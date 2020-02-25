package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.cancassfascsius.action.ICostantiCancAssFascSius;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaProcedimentoPerEstremi extends ActionSiap implements ICostantiFascicoloSius {

	// 20131209 - Creazione HashMap per il trasporto dei parametri di filtro ricerca
	HashMap<String, Object> mParams = null;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		mParams = new HashMap<String, Object>();

		setLinkRitorno();

		String lFiltroCollaboratore = getFiltroCollaboratore();

		// Se è stato selezionato il filtro per Cancelleria Assegnataria si prepara il model per la ricerca
		CancAssFascSiusModel lCancAssFasc = null;
		if (!isRequestParameterNullObj(ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA)) {
			String lCodCancelleriaAssegnataria = getRequestStringParameter(ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA);
			if (lCodCancelleriaAssegnataria.trim().length() > 0) {
				lCancAssFasc = new CancAssFascSiusModel();
				lCancAssFasc.setCodCancelleriaAssegnataria(lCodCancelleriaAssegnataria);
				// La ricerca è sempre limitata all'Ufficio dell'utente connesso
				lCancAssFasc.setCodUfficio(getCodUfficioUtenteConnesso());
				// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
				// per la generazone del foglio excel post estrazione.
				mParams.put("cancAssFasc", lCancAssFasc);

				// Ricerca della Cancelleria Assegnataria da passare nella request
				// per indicarla tra le condizioni di ricerca
				ICancelleriaAssegnataria lCancCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
				Vector lCancellerie = lCancCtrl.ExRicercaCancelleriaAssegnataria(lCancAssFasc);
				if (lCancellerie != null && lCancellerie.size() > 0) {
					setRequestAttribute("cancelleria_assegnataria",
							(CancelleriaAssegnatariaModel) (lCancellerie.get(0)));
					// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione
					// necessaria
					// per la generazone del foglio excel post estrazione.
					mParams.put("cancelleria_assegnataria", lCancellerie.get(0));
				} else if (lCodCancelleriaAssegnataria.compareTo("nocanc") == 0) {
					CancelleriaAssegnatariaModel lNessunaCancAssFasc = new CancelleriaAssegnatariaModel();
					lNessunaCancAssFasc.setCodCancelleriaAssegnataria(lCodCancelleriaAssegnataria);
					lNessunaCancAssFasc.setCodUfficio(getCodUfficioUtenteConnesso());
					setRequestAttribute("cancelleria_assegnataria", lNessunaCancAssFasc);
					// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione
					// necessaria
					// per la generazone del foglio excel post estrazione.
					mParams.put("cancelleria_assegnataria", lNessunaCancAssFasc);
				}
			}
		}

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		String cod_atto = getRequestStringParameter(CAMPO_COD_OGGETTO);
		// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
		// per la generazone del foglio excel post estrazione.
		mParams.put("codAtto", cod_atto);

		if (cod_atto.compareTo("-") != 0) {
			String lDescrContenuto = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getOggettoProcedimento(), cod_atto);
			setRequestAttribute("codAtto", lDescrContenuto);
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("descrContenuto", lDescrContenuto);
		}

		// Riempie il model
		FascicoloSiusModel fSmod = new FascicoloSiusModel();

		// STUB 17/06/2004 Nuovi parametri per la ricerca per estremi atto.
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_INIZIALE)
				&& (getRequestStringParameter("tipoRicerche").equals("iscrizione")))
			fSmod.setDataIscrizioneIniziale(getRequestDateParameter(
					ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE,
					ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE,
					ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE));

		if (fSmod.getDataIscrizioneIniziale() != null) {
			setRequestAttribute(
					"dataIscrizioneInizio",
					getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
							+ "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE)
							+ "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE));
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("dataIscrizioneInizio", getRequestAttribute("dataIscrizioneInizio"));
		}

		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_FINALE)
				&& (getRequestStringParameter("tipoRicerche").equals("iscrizione")))
			fSmod.setDataIscrizioneFinale(getRequestDateParameter(
					ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE,
					ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE,
					ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE));

		if (fSmod.getDataIscrizioneFinale() != null) {
			setRequestAttribute("dataIscrizioneFine",
					getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE) + "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE)
							+ "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE));
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("dataIscrizioneFine", getRequestAttribute("dataIscrizioneFine"));
		}

		if (!isRequestParameterNullObj(CAMPO_GIORNO_ARRIVO_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ARRIVO_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ARRIVO_INIZIALE)
				&& (getRequestStringParameter("tipoRicerche").equals("arrivo")))
			fSmod.setDataArrivoIniziale(getRequestDateParameter(
					ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_INIZIALE,
					ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_INIZIALE,
					ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_INIZIALE));

		if (fSmod.getDataArrivoIniziale() != null) {
			setRequestAttribute("dataArrivoInizio",
					getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_INIZIALE) + "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_INIZIALE)
							+ "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_INIZIALE));
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("dataArrivoInizio", getRequestAttribute("dataArrivoInizio"));
		}

		if (!isRequestParameterNullObj(CAMPO_GIORNO_ARRIVO_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ARRIVO_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ARRIVO_FINALE)
				&& (getRequestStringParameter("tipoRicerche").equals("arrivo")))
			fSmod.setDataArrivoFinale(getRequestDateParameter(
					ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_FINALE,
					ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_FINALE,
					ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_FINALE));

		if (fSmod.getDataArrivoFinale() != null) {
			setRequestAttribute("dataArrivoFine",
					getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_FINALE) + "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_FINALE)
							+ "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_FINALE));
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("dataArrivoFine", getRequestAttribute("dataArrivoFine"));
		}

		if (!isRequestParameterNullObj(CAMPO_GIORNO_ATTO_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ATTO_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ATTO_INIZIALE)
				&& (getRequestStringParameter("tipoRicerche").equals("atto")))
			fSmod.setDataAttoIniziale(getRequestDateParameter(
					ICostantiFascicoloSius.CAMPO_ANNO_ATTO_INIZIALE,
					ICostantiFascicoloSius.CAMPO_MESE_ATTO_INIZIALE,
					ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_INIZIALE));

		if (fSmod.getDataAttoIniziale() != null) {
			setRequestAttribute("dataAttoInizio",
					getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_INIZIALE) + "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_ATTO_INIZIALE)
							+ "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_ATTO_INIZIALE));
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("dataAttoInizio", getRequestAttribute("dataAttoInizio"));
		}

		if (!isRequestParameterNullObj(CAMPO_GIORNO_ATTO_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ATTO_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ATTO_FINALE)
				&& (getRequestStringParameter("tipoRicerche").equals("atto")))
			fSmod.setDataAttoFinale(getRequestDateParameter(ICostantiFascicoloSius.CAMPO_ANNO_ATTO_FINALE,
					ICostantiFascicoloSius.CAMPO_MESE_ATTO_FINALE,
					ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_FINALE));

		if (fSmod.getDataAttoFinale() != null) {
			setRequestAttribute("dataAttoFine",
					getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_FINALE) + "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_ATTO_FINALE) + "/"
							+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_ATTO_FINALE));
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("dataAttoFine", getRequestAttribute("dataAttoFine"));
		}

		// STUB 20/07/2004 Si Utilizza il campo CodUfficioInserimento come veicolo per trasmettere il codice
		// ufficio recuperato dal tipo ufficio e dalla descr ufficio
		if ((!getRequestStringParameter(CAMPO_CHIAVE_UFFICIO).equals("-"))
				&& (!getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equals(""))) {
			fSmod.setCodUfficioInserimento(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_CHIAVE_UFFICIO),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)));
			if (fSmod.getCodUfficioInserimento().compareTo(this.getCodUfficioUtenteConnesso()) != 0) {
				setRequestAttribute("descrUfficio", getRequestStringParameter(CAMPO_CHIAVE_UFFICIO) + " - "
						+ getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO));
				// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
				// per la generazone del foglio excel post estrazione.
				mParams.put("descrUfficio", getRequestAttribute("descrUfficio"));
			}
		}

		// STUB 18/02/2005 Nuovi parametri per la ricerca per estremi atto.
		if (getRequestStringParameter("statoProcedimento").equals("tutti"))
			fSmod.setDescrStatoFascicolo("tutti");
		else {
			if (getRequestStringParameter("statoProcedimento").equals("pendenti")) {
				fSmod.setDataFinePendenza(getRequestDateParameter(
						ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENDENZA,
						ICostantiFascicoloSius.CAMPO_MESE_FINE_PENDENZA,
						ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA));
				fSmod.setDescrStatoFascicolo("pendenti");
				setRequestAttribute("dataFinePendenza",
						getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA) + "/"
								+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_FINE_PENDENZA)
								+ "/"
								+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENDENZA));
				// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
				// per la generazone del foglio excel post estrazione.
				mParams.put("dataFinePendenza", getRequestAttribute("dataFinePendenza"));
			} else if (getRequestStringParameter("statoProcedimento").equals("definiti")) {
				fSmod.setDataDefinizioneIniziale(getRequestDateParameter(
						ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_INIZIALE,
						ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_INIZIALE,
						ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_INIZIALE));
				fSmod.setDataDefinizioneFinale(getRequestDateParameter(
						ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_FINALE,
						ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_FINALE,
						ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_FINALE));
				fSmod.setDescrStatoFascicolo("definiti");
				setRequestAttribute(
						"dataDefinizioneIniziale",
						getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_INIZIALE)
								+ "/"
								+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_INIZIALE)
								+ "/"
								+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_INIZIALE));
				setRequestAttribute(
						"dataDefinizioneFinale",
						getRequestStringParameter(ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_FINALE)
								+ "/"
								+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_FINALE)
								+ "/"
								+ getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_FINALE));
				// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
				// per la generazone del foglio excel post estrazione.
				mParams.put("dataDefinizioneIniziale", getRequestAttribute("dataDefinizioneIniziale"));
				mParams.put("dataDefinizioneFinale", getRequestAttribute("dataDefinizioneFinale"));

			}
			setRequestAttribute("statoProcedimento", getRequestStringParameter("statoProcedimento"));
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("statoProcedimento", getRequestAttribute("statoProcedimento"));
		}
		// STUB 18/02/2005 Si Utilizza il campo CodStatoFascicolo come veicolo per trasmettere il codice
		// Magistrato per il filtro di estrazione dati.
		fSmod.setCodStatoFascicolo(getRequestStringParameter(CAMPO_COD_MAGISTRATO));

		if (getRequestStringParameter(CAMPO_COD_MAGISTRATO).toLowerCase().compareTo("tutti") != 0) {
			IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel lMagistrato = lMagCtrl
					.ExRicercaMagistratoByCod(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
			String descMagistrato = lMagistrato.getCognome() + " " + lMagistrato.getNome() + " - "
					+ lMagistrato.getCodMagistrato();
			setRequestAttribute("codMagistrato", descMagistrato);
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("descMagistrato", descMagistrato);
		}

		// 20131206 - viene inserito in session il fascicoloSiusModel, operazione necessaria
		// per la generazone del foglio excel post estrazione.
		setSessionAttribute("fSmod", fSmod);

		// Chiama il controller.
		IFascicoloSius fSCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

		// Effettuo la ricerca dei Provvedimenti per estremi (data inizio e data fine) passati nel model
		// fSmod.
		Vector lFascicoliSoggetti = fSCtrl.ExRicercaFascicoloSiusByEstremiPagina(fSmod, cod_atto,
				lCancAssFasc, lFiltroCollaboratore, Integer.parseInt(lPagina));

		// Nel caso di Pendenti per ogni fascicolo estratto (20) provo a recuperare
		// la data emissione dal documento_allegato (se presente evento con idmax)
		// n.b. la query di estrezione non va più in LEFT OUTER JOIN con evento
		// e documento allegato per problemi di performance.
		if (getRequestStringParameter("statoProcedimento").equals("pendenti")) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Ricerco date definizione");
			for (int i = 0; i < lFascicoliSoggetti.size(); i++) {
				FascicoloGPModel lModel = (FascicoloGPModel) lFascicoliSoggetti.elementAt(i);
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("DataDefinizione = "+lModel.getFascicoloSiusModel().getDataDefinizione());
				if (lModel.getFascicoloSiusModel().getDataDefinizione() == null) {
					// Provo a recuperare la data definizione da evetuale doc allegato
					Date lDataDefinizioneFinale = null;
					lDataDefinizioneFinale = fSCtrl.ExGetDataDefinizineFinale(lModel.getFascicoloSiusModel()
							.getIdFascicoloSius(), null);
					if (lDataDefinizioneFinale != null) {
						lModel.getFascicoloSiusModel().setDataDefinizioneFinale(lDataDefinizioneFinale);
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("Data Trovata su Doc All: "+lModel.getFascicoloSiusModel().getChiaveAnno()+"/"+lModel.getFascicoloSiusModel().getChiaveProgr());
					}
				}
			}
		}

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetti != null) {
			SoggettoModel lSoggetto = ((FascicoloGPModel) lFascicoliSoggetti.get(0)).getFascicoloSiusModel()
					.getSoggetto();
			setRequestAttribute("soggetto", lSoggetto);
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("soggetto", lSoggetto);
		}

		if (lFascicoliSoggetti.size() == 1) {
			FascicoloGPModel lFascicoloSoggettoSius = new FascicoloGPModel();
			lFascicoloSoggettoSius = (FascicoloGPModel) lFascicoliSoggetti.firstElement();

			// Inserisce il model soggetto nella request
			setRequestAttribute("fascicolo", lFascicoloSoggettoSius);
			// 20131206 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
			// per la generazone del foglio excel post estrazione.
			mParams.put("fascicolo", lFascicoloSoggettoSius);

			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS + "="
					+ lFascicoloSoggettoSius.getFascicoloSiusModel().getIdFascicoloSius();
		} else {
			// Paginazione
			BigDecimal CountRisultati = null;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = fSCtrl.ExGetNumRicercaFascicoloSiusByEstremi(fSmod, cod_atto, lCancAssFasc,
						lFiltroCollaboratore);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// setta la risposta nella request
			setRequestAttribute("fascicoli", lFascicoliSoggetti);

			lReturnPage = ICostantiFascicoloSius.PG_RICERCAFASCICOLOSIUS_PERESTREMIATTO;
		}

		setSessionAttribute("parametri_ricerca", mParams);

		return lReturnPage; // restituisce la jsp di VIEW
	}

	// Funzione di lettura Filtro sul Collaboratore eventualmente presente nella form di input
	private String getFiltroCollaboratore() throws F3BException {
		String lRet = null;
		if (!isRequestParameterNullObj("filtroCollaboratore")) {
			lRet = getRequestStringParameter("filtroCollaboratore");
			if (!lRet.equalsIgnoreCase("tutti")) {
				setRequestAttribute("filtroCollaboratore", lRet);
				// 20131208 - viene inserito in hash i dati provenienti dalla request, operazione necessaria
				// per la generazone del foglio excel post estrazione.
				mParams.put("filtroCollaboratore", lRet);
			}
		}
		return lRet;
	}

}