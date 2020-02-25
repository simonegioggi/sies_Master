package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.RichPMMisSicCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * Action per la load dell'inserimento/modifica della richiesta alla Sorveglianza di unificazione delle Misure
 * di sicurezza
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActLoadInsRichiestaSORVUnificaMS extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();
		String lPage = "";

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		if ("I".equals(lModalita)) {
			// Inserimento
			this.getTitoliMisureSicurezzaScelte(lIstrCumulo);
			lPage = PG_INS_RICH_SORV_UNIFICA_MS;

			// siesLogger.debug("--XX-- Inserimento - lpage = "+lPage);
		} else if ("M".equals(lModalita)) {
			// In Modifica cerco la Richiesta da Modificare
			BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

			siesLogger.info("--ZZZZZZZZZZZZZZZZZZZZ-- lRicMod.toString = " + lRicMod.toString());
			setRequestAttribute("RichiestaAllaSORV", lRicMod);

			this.getTitoliMisureSicurezzaPresenti(aIdRich);
			lPage = PG_MOD_RICH_SORV_UNIFICA_MS;

			// siesLogger.debug("--XX-- Modifica - lpage = "+lPage);
		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		setRequestAttribute("modalita", lModalita);
		// return PG_INS_RICH_SORV_UNIFICA_MS;
		return lPage;

	} // Chiude processRequest()

	private void getTitoliMisureSicurezzaScelte(IstruttoriaCumuloModel aIstruttoriaModel)
			throws F3BException {
		// siesLogger.debug("--XX-- Inserimento - Start getTitoliMisureSicurezzaScelte");

		Vector<TitoloCumulatoModel> listaTitoliMS = new Vector<>();

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		TitoloCumulatoModel lTitolo = null;

		// Lista dei check Selezionati dall'Utente.
		String[] lIdTitoliSelezionati = null;
		if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_SELEZIONATO))
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

		// siesLogger.info(">>>>>>>>>> size Misure Sicurezza selezionati = "+lIdMisureSicurezza.length );

		Vector<MisuraSicurezzaCumuloModel> lVecMisureSic = new Vector<>();
		MisuraSicurezzaCumuloModel lMisSicMod = null;
		IMisuraSicurezzaCumulo lCtrlMS = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();

		String lIdTitoloCorrente = "";
		for (int i = 0; i < lIdTitoliSelezionati.length; i++) {
			String lIdTitoloSel = lIdTitoliSelezionati[i].substring(0, lIdTitoliSelezionati[i].indexOf("-"))
					.trim();
			String lIdMisuraSel = lIdTitoliSelezionati[i].substring(lIdTitoliSelezionati[i].indexOf("-") + 1)
					.trim();

			// Gestione rottura Identificativo
			if (lIdTitoloSel.trim().compareTo(lIdTitoloCorrente.trim()) != 0) {

				// Si salva il Titolo Corrente
				if (lIdTitoloCorrente != "") {
					// Caricamento Misure di Sicurezza del Titolo
					lTitolo.setMisureSicurezzaCumulo(lVecMisureSic);
					listaTitoliMS.add(lTitolo);
				}
				// Lettura del nuovo Titolo_Cumulato_Model e nuovo vettore Misure Sic.
				lTitolo = lCtrlT.ExRicercaTitoloCumulatoById(new BigDecimal(lIdTitoloSel));
				lVecMisureSic = new Vector<>();
				lIdTitoloCorrente = lIdTitoloSel;
			}

			// Lettura della Misura Selezionata
			lMisSicMod = lCtrlMS.ExRicercaMisuraSicurezzaCumuloById(new BigDecimal(lIdMisuraSel));
			if (lMisSicMod != null && lMisSicMod.getIdMisuraSicurezzaCumulo() != null)
				lVecMisureSic.add(lMisSicMod);

		}
		// Si salva l'ultimo Titolo Caricato
		if (lVecMisureSic != null && lVecMisureSic.size() > 0) {

			// Caricamento Misure di Sicurezza del Titolo
			lTitolo.setMisureSicurezzaCumulo(lVecMisureSic);
			listaTitoliMS.add(lTitolo);
		}

		setRequestAttribute("listaTitoliMisure", listaTitoliMS);

	} // Chiude getTitoliMisureSicurezzaScelte()

	private void getTitoliMisureSicurezzaPresenti(BigDecimal aIdRich) throws F3BException {

		// Vector<TitoloCumulatoModel> listaTitoliMS = new Vector<>();
		// ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		// TitoloCumulatoModel lTitolo = null;

		// Lettura delle relazioni RICHPM_MISSICUR_CUM)
		Vector<RichPMMisSicCumModel> VecRicPM = new Vector<>();
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		VecRicPM = lCtrlRich.ExRicercaRichPMMisSicCum(aIdRich);
		siesLogger.info(">>>>>>>>>> aIdRich = " + aIdRich);

		// Lettura delle Misure di Sicurezza coinvolte.
		Vector<MisuraSicurezzaCumuloModel> VecMisSic = new Vector<>();
		IMisuraSicurezzaCumulo lCtrMSC = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
		for (int i = 0; i < VecRicPM.size(); i++) {
			RichPMMisSicCumModel lRicMSMod = VecRicPM.elementAt(i);
			MisuraSicurezzaCumuloModel lMisSicMod = lCtrMSC
					.ExRicercaMisuraSicurezzaCumuloById(lRicMSMod.getMisIdMisSicCumulo());
			VecMisSic.add(lMisSicMod);
		}

		// Lettura dei Titoli collegati alle Misure di Sicurezza.
		ITitoloCumulato lCtrlTitCum = SIEPLookupRemote.getTitoloCumulatoRemote();
		ArrayList<BigDecimal> lIdTitoli = new ArrayList<>();
		Vector<TitoloCumulatoModel> VecTitoli = new Vector<>();
		for (int j = 0; j < VecMisSic.size(); j++) {
			MisuraSicurezzaCumuloModel lMisSicMod = VecMisSic.elementAt(j);
			if (lIdTitoli.contains(lMisSicMod.getTitIdTitoloCumulato())) {
			} else {
				TitoloCumulatoModel lTitCumMod = lCtrlTitCum
						.ExRicercaTitoloCumulatoById(lMisSicMod.getTitIdTitoloCumulato());
				// Aggregazione Misure di Sicurezza ai Titoli
				Vector<MisuraSicurezzaCumuloModel> VecMSTitolo = new Vector<>();
				for (int k = 0; k < VecMisSic.size(); k++) {
					MisuraSicurezzaCumuloModel lMSMod = VecMisSic.elementAt(k);
					if (lMSMod.getTitIdTitoloCumulato().compareTo(lTitCumMod.getIdTitoloCumulato()) == 0) {
						VecMSTitolo.add(lMSMod);
					}
				}
				lTitCumMod.setMisureSicurezzaCumulo(VecMSTitolo);
				VecTitoli.add(lTitCumMod);
				lIdTitoli.add(lTitCumMod.getIdTitoloCumulato());
			}
		}
		siesLogger.info(">>>>>>>>>> SIZE Vector Titoli della richiesta = " + VecTitoli.size());

		setRequestAttribute("listaTitoliMisure", VecTitoli);

	} // Chiude getTitoliMisureSicurezzaPresenti()

} // End Action