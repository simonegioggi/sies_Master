package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActAggiornaTenoriSige extends ActionSige implements ICostantiTenoreSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// 20190514 [SG]: aggiunto parametro di passaggio
	public void caricaTenori(BigDecimal mIdRichiesta, StringTokenizer lCodOggetto,
			StringTokenizer lIdSentenza, StringTokenizer lIdReati, String modalita, String codContenuto,
			String codContenutoOld) throws Exception {

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Di seguito gli scenari che si possono presentare in fase di inserimento/modifica
		// CASO 1) 1 oggetto selezionato – 1 titolo esecutivo selezionato = associazione un oggetto un titolo
		// esecutivo
		// CASO 2) 1 oggetto selezionato – N titoli esecutivi selezionati = associazione oggetto con N titoli
		// esecutivi
		// CASO 3) N oggetti selezionati – N titoli esecutivi selezionati = associazione di ogni oggetto
		// selezionato con N titoli esecutivi
		// CASO 4) N oggetti selezionati – 1 titolo esecutivo selezionato = associazione di ogni oggetto
		// selezionato con un titolo esecutivo.

		// numero di Oggetti selezionati
		int lNumOggetti = 1;
		if (lCodOggetto.countTokens() > 0) {
			lNumOggetti = lCodOggetto.countTokens();
		}

		// numero di Titoli Esecutivi selezionati
		int lNumTitoliEsecutivi = 1;
		if (lIdSentenza.countTokens() > 0) {
			lNumTitoliEsecutivi = lIdSentenza.countTokens();
		}

		// Se definiti più reati ci sarà un Tenore per ogni Reato
		// numero di Reati selezionati
		int lNumReati = 1;
		if (lIdReati.countTokens() > 0) {
			lNumReati = lIdReati.countTokens();
		}

		Date lOggi = DateUtils.getSysDate();
		String codUtenteConnesso = getCodUtenteConnesso();
		String codUfficioUtenteConnesso = getCodUfficioUtenteConnesso();

		// CASO 1
		if (lNumOggetti == 1 && lNumTitoliEsecutivi == 1) {
			// Si istanzia un array di Tenori
			TenoreSigeModel lTenori[] = new TenoreSigeModel[lNumReati];

			// ID Oggetto corrente
			String lIdOggetto = null;
			// ID Titolo ESecutivo corrente
			BigDecimal lIdTitoloEsecutivo = null;

			if (lCodOggetto.hasMoreTokens()) {
				lIdOggetto = lCodOggetto.nextToken();
			}

			if (lIdSentenza.hasMoreTokens()) {
				lIdTitoloEsecutivo = new BigDecimal(lIdSentenza.nextToken());
			}

			// Valorizzazione dell'array di Tenori
			// Si cicla sul numero di Reati selezonati
			for (int i = 0; i < lNumReati; i++) {
				// ID Reato corrente
				BigDecimal lIdReato = null;

				if (lIdReati.hasMoreTokens()) {
					lIdReato = new BigDecimal(lIdReati.nextToken());
				}

				TenoreSigeModel lTenore = new TenoreSigeModel();

				lTenore.setCodOperatoreInserimento(codUtenteConnesso);
				lTenore.setCodUfficioInserimento(codUfficioUtenteConnesso);
				lTenore.setDataInserimento(lOggi);
				if (modalita.equals("M")) {
					lTenore.setCodOperatoreAggiornamento(codUtenteConnesso);
					lTenore.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
					lTenore.setDataAggiornamento(lOggi);
				}
				lTenore.setData(lOggi);
				lTenore.setCodOggettoSige(lIdOggetto);
				lTenore.setIdSentenza(lIdTitoloEsecutivo);
				lTenore.setIdReato(lIdReato);
				lTenore.setRicSigIdRichiestaSige(mIdRichiesta);
				lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
				lTenore.setFlagOggetto("S");
				lTenore.setCodContenutoSige(codContenuto);
				lTenori[i] = lTenore;

			} // endwhile

			// Inserimento/Modifica
			// 20190514 [SG]: aggiunto parametro di passaggio
			aggiornaTenori(mIdRichiesta, lTenori, modalita, codContenutoOld);

		}

		// CASO 2
		if (lNumOggetti == 1 && lNumTitoliEsecutivi > 1) {
			// Si istanzia un array di Tenori
			TenoreSigeModel lTenori[] = new TenoreSigeModel[lNumTitoliEsecutivi];

			// ID Oggetto corrente
			String lIdOggetto = null;

			if (lCodOggetto.hasMoreTokens()) {
				lIdOggetto = lCodOggetto.nextToken();
			}

			// Valorizzazione dell'array di Tenori
			// Si cicla sul numero di Titoli Esecutivi selezonati
			for (int i = 0; i < lNumTitoliEsecutivi; i++) {
				// ID Titolo ESecutivo corrente
				BigDecimal lIdTitoloEsecutivo = null;
				// ID Reato corrente
				BigDecimal lIdReato = null;

				if (lIdSentenza.hasMoreTokens()) {
					lIdTitoloEsecutivo = new BigDecimal(lIdSentenza.nextToken());
				}

				if (lIdReati.hasMoreTokens()) {
					lIdReato = new BigDecimal(lIdReati.nextToken());
				}

				TenoreSigeModel lTenore = new TenoreSigeModel();

				if (i == 0) {
					lTenore.setFlagOggetto("S");
				} else {
					lTenore.setFlagOggetto("N");
				}

				lTenore.setCodOperatoreInserimento(codUtenteConnesso);
				lTenore.setCodUfficioInserimento(codUfficioUtenteConnesso);
				lTenore.setDataInserimento(lOggi);
				if (modalita.equals("M")) {
					lTenore.setCodOperatoreAggiornamento(codUtenteConnesso);
					lTenore.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
					lTenore.setDataAggiornamento(lOggi);
				}
				lTenore.setData(lOggi);
				lTenore.setCodOggettoSige(lIdOggetto);
				lTenore.setIdSentenza(lIdTitoloEsecutivo);
				lTenore.setIdReato(lIdReato);
				lTenore.setRicSigIdRichiestaSige(mIdRichiesta);
				lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
				lTenore.setCodContenutoSige(codContenuto);
				lTenori[i] = lTenore;

			} // endwhile

			// Inserimento/Modifica
			// 20190514 [SG]: aggiunto parametro di passaggio
			aggiornaTenori(mIdRichiesta, lTenori, modalita, codContenutoOld);

		}

		// CASO 3
		if (lNumOggetti > 1 && lNumTitoliEsecutivi > 1) {
			// Si istanzia un array di Tenori
			int numRecordTot = lNumOggetti * lNumTitoliEsecutivi;
			int numRecord = 0;
			TenoreSigeModel lTenori[] = new TenoreSigeModel[numRecordTot];

			// si cicla sugli Oggetti selezionati
			for (int j = 0; j < lNumOggetti; j++) {
				if (j > 0) {
					numRecord = numRecord + 1;
				}

				// ID Oggetto corrente
				String lIdOggetto = null;

				if (lCodOggetto.hasMoreTokens()) {
					lIdOggetto = lCodOggetto.nextToken();
				}

				lIdSentenza = new StringTokenizer(getRequestStringParameter(CAMPO_SEN_ID_SENTENZA), "|");

				// Valorizzazione dell'array di Tenori
				// Si cicla sul numero di Titoli Esecutivi selezonati
				for (int i = 0; i < lNumTitoliEsecutivi; i++) {
					if (i > 0) {
						numRecord = numRecord + 1;
					}

					// ID Titolo Esecutivo corrente
					BigDecimal lIdTitoloEsecutivo = null;
					// ID Reato corrente
					BigDecimal lIdReato = null;

					if (lIdSentenza.hasMoreTokens()) {
						lIdTitoloEsecutivo = new BigDecimal(lIdSentenza.nextToken());
					}

					if (lIdReati.hasMoreTokens()) {
						lIdReato = new BigDecimal(lIdReati.nextToken());
					}

					TenoreSigeModel lTenore = new TenoreSigeModel();

					if (i == 0) {
						lTenore.setFlagOggetto("S");
					} else {
						lTenore.setFlagOggetto("N");
					}
					lTenore.setCodOperatoreInserimento(codUtenteConnesso);
					lTenore.setCodUfficioInserimento(codUfficioUtenteConnesso);
					lTenore.setDataInserimento(lOggi);
					if (modalita.equals("M")) {
						lTenore.setCodOperatoreAggiornamento(codUtenteConnesso);
						lTenore.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
						lTenore.setDataAggiornamento(lOggi);
					}
					lTenore.setData(lOggi);
					lTenore.setCodOggettoSige(lIdOggetto);
					lTenore.setIdSentenza(lIdTitoloEsecutivo);
					lTenore.setIdReato(lIdReato);
					lTenore.setRicSigIdRichiestaSige(mIdRichiesta);
					lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
					lTenore.setCodContenutoSige(codContenuto);
					lTenori[numRecord] = lTenore;

				} // end for (int i = 0; i < lNumTitoliEsecutivi; i++)

			} // end for (int j = 0; j < lNumOggetti; j++)

			// Inserimento/Modifica
			// 20190514 [SG]: aggiunto parametro di passaggio
			aggiornaTenori(mIdRichiesta, lTenori, modalita, codContenutoOld);

		}

		// CASO 4
		if (lNumOggetti > 1 && lNumTitoliEsecutivi == 1) {
			// Si istanzia un array di Tenori
			int numRecordTot = lNumOggetti * lNumReati;
			int numRecord = 0;
			TenoreSigeModel lTenori[] = new TenoreSigeModel[numRecordTot];

			// si cicla sugli Oggetti selezionati
			for (int j = 0; j < lNumOggetti; j++) {
				if (j > 0) {
					numRecord = numRecord + 1;
				}

				lIdSentenza = new StringTokenizer(getRequestStringParameter(CAMPO_SEN_ID_SENTENZA), "|");

				// ID Oggetto corrente
				String lIdOggetto = null;
				// ID Titolo Esecutivo corrente
				BigDecimal lIdTitoloEsecutivo = null;

				if (lCodOggetto.hasMoreTokens()) {
					lIdOggetto = lCodOggetto.nextToken();
				}

				if (lIdSentenza.hasMoreTokens()) {
					lIdTitoloEsecutivo = new BigDecimal(lIdSentenza.nextToken());
				}

				lIdReati = new StringTokenizer(getRequestStringParameter(CAMPO_REA_ID_REATO), "|");

				// Valorizzazione dell'array di Tenori
				// Si cicla sul numero di Reati selezionati
				for (int i = 0; i < lNumReati; i++) {
					if (i > 0) {
						numRecord = numRecord + 1;
					}

					// ID Reato corrente
					BigDecimal lIdReato = null;

					if (lIdReati.hasMoreTokens()) {
						lIdReato = new BigDecimal(lIdReati.nextToken());
					}

					TenoreSigeModel lTenore = new TenoreSigeModel();

					if (i == 0) {
						lTenore.setFlagOggetto("S");
					} else {
						lTenore.setFlagOggetto("N");
					}
					lTenore.setCodOperatoreInserimento(codUtenteConnesso);
					lTenore.setCodUfficioInserimento(codUfficioUtenteConnesso);
					lTenore.setDataInserimento(lOggi);
					if (modalita.equals("M")) {
						lTenore.setCodOperatoreAggiornamento(codUtenteConnesso);
						lTenore.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
						lTenore.setDataAggiornamento(lOggi);
					}
					lTenore.setData(lOggi);
					lTenore.setCodOggettoSige(lIdOggetto);
					lTenore.setIdSentenza(lIdTitoloEsecutivo);
					lTenore.setIdReato(lIdReato);
					lTenore.setRicSigIdRichiestaSige(mIdRichiesta);
					lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
					lTenore.setCodContenutoSige(codContenuto);
					lTenori[numRecord] = lTenore;

				} // endwhile

			}

			// Inserimento/Modifica
			// 20190514 [SG]: aggiunto parametro di passaggio
			aggiornaTenori(mIdRichiesta, lTenori, modalita, codContenutoOld);

		}
	}

	// 20190514 [SG]: aggiunto parametro di passaggio
	private void aggiornaTenori(BigDecimal mIdRichiesta, TenoreSigeModel lNuoviTenori[], String modalita,
			String codContenutoOld) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "aggiornaTenori: inizio");

		ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();

		// Si ricavano i Tenori già presenti per controllare eventuale
		// duplicazioni a seguito del nuovo inserimento
		Vector lElencoTenori = lCtrl.ExRicercaTenoreEstesoByRichiesta(mIdRichiesta);

		// Controllo duplicazione
		if (lElencoTenori.size() > 0) {
			// conversione di formato dell'array per effettuare il controllo
			TenoreSigeEstesoModel[] lTenoriEstesiNuovi = new TenoreSigeEstesoModel[lNuoviTenori.length];
			for (int i = 0; i < lNuoviTenori.length; i++) {
				lTenoriEstesiNuovi[i] = new TenoreSigeEstesoModel(lNuoviTenori[i]);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("controllo duplicazione");
			for (int i = 0; i < lTenoriEstesiNuovi.length; i++) {
				if (Collections.binarySearch(lElencoTenori, lTenoriEstesiNuovi[i]) >= 0) {
					throw new SIGEException(SIGEException.USER_MESSAGE,
							"Non è possibile inserire 2 volte la stessa tripletta: Oggetto-Sentenza-Reato!");
				}
				// 20190513 [SG]: aggiunto controllo
				String triplaNuova = "";
				if (lTenoriEstesiNuovi[i].getTenoreSige() != null)
					triplaNuova = lTenoriEstesiNuovi[i].getTenoreSige().getCodOggettoSige() + "|"
							+ lTenoriEstesiNuovi[i].getTenoreSige().getIdSentenza() + "|"
							+ lTenoriEstesiNuovi[i].getTenoreSige().getIdReato();
				for (int j = 0; j < lElencoTenori.size(); j++) {
					String triplaVecchia = "";
					TenoreSigeEstesoModel tsem = (TenoreSigeEstesoModel) lElencoTenori.get(j);
					if (tsem.getTenoreSige() != null)
						triplaVecchia = tsem.getTenoreSige().getCodOggettoSige() + "|"
								+ tsem.getTenoreSige().getIdSentenza() + "|"
								+ tsem.getTenoreSige().getIdReato();
					if (triplaNuova.equals(triplaVecchia)) {
						throw new SIGEException(SIGEException.USER_MESSAGE,
								"Non è possibile inserire 2 volte la stessa tripletta: Oggetto-Sentenza-Reato!");
					}
				}
			}
		}

		if (modalita.equals("I")) {
			// Inserimento
			lCtrl.ExInserisciOggettoXRichiesta(lNuoviTenori);
		} else {
			// Modifica
			// 20190514 [SG]: aggiunto parametro di passaggio
			lCtrl.ExModificaOggettoXRichiesta(mIdRichiesta, lNuoviTenori, codContenutoOld);
		}
	}

}