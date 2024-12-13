package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * ActLoadModificaDecreto - Classe Action devoluta alla preparazione della Form di modifica di un Decreto.
 * Poichè i dati da visualizzare sono quelli el Dettaglio, la classe è ottenuta come specializzazione della
 * ActLoadDettaglioDecretoDeposito in modo da poter riutilizzare le stesse funzioni per ricavare i dati.
 * 
 * @version 2.2
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadModificaDecreto extends ActLoadDettaglioDecretoDeposito {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// Pagina jsp per la modifica del decreto
		String lRetPage = PG_MODIFICA_DECRETO;

		// Viene richiamata l'elaborazione della Action di dettaglio
		super.processRequest();

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Si richiama il lock sul decreto
		lockApplicativo("Decreto");

		// L'elenco Tenori in forma di Vector viene convertito in Array
		TenoreModel[] lTenori = (TenoreModel[]) mTenori.toArray(new TenoreModel[0]);
		if (lTenori != null) {
			// Preparazione delle combo Esiti, una per ogni oggetto
			int lNumOggetti = lTenori.length;
			String[] lEsiti = new String[lNumOggetti];
			for (int i = 0; i < lNumOggetti; i++)
				lEsiti[i] = getEsiti(lTenori[i].getCodOggettoTenore(), lTenori[i].getCodEsitoTenore());
			// trasferimento lista esiti
			setRequestAttribute("esiti", lEsiti);
		}

		// DL 146/2014 - luglio 2014 -
		if ((mDepDecrMotMod.getDepositoDecreto().getCodTipoDecreto()
				.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA) == 0)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ActLoadModificaDecreto -> Decreto di Revoca Liberazione Anticipata");
			// Per questo decreto devo cercare i Periodi di L.A.
			ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			Vector lLicenzePeriodi = lCtrlDep
					.ExRicercaLicenzeLibanticipataByEve(mDepDecrMotMod.getEvento().getIdEvento());
			this.setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

			// for (int cc = 0; cc < lLicenzePeriodi.size(); cc++) {
			// LicenzaPeriodiLibAnticipataModel llModel = new LicenzaPeriodiLibAnticipataModel();
			// llModel = (LicenzaPeriodiLibAnticipataModel) lLicenzePeriodi.get(cc);
			// }

			lRetPage = PG_MODIFICA_DECRETO_REVOCA_LA;

		}

		// DL 92 2014 Violazione CEDU -
		if ((mDepDecrMotMod.getDepositoDecreto().getCodTipoDecreto()
				.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) == 0)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ActLoadModificaDecreto --> Decreto Violazione Art 3 CEDU");
			// Per questo decreto devo cercare i Periodi di L.A.

			ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			Vector lLicenzePeriodi = lCtrlDep
					.ExRicercaLicenzeLibanticipataByEve(mDepDecrMotMod.getEvento().getIdEvento());
			this.setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);
			this.setRequestAttribute("CodTipoProvvedimento", "02");
			lRetPage = PG_MODIFICA_DECRETO_VIOLAZIONE_CEDU;
		}

		// Per un decreto di sospensione misura sicurezza l'esito è modificabile solo se
		// non altera la sospensione della misura
		// Per un decreto di diffida inosservanza obblighi misura sicurezza l'esito è unco

		if (mDepDecrMotMod.getDepositoDecreto().getCodTipoDecreto().compareTo(SOSPENSIONE_ESECUZIONE_MS) == 0
				|| mDepDecrMotMod.getDepositoDecreto().getCodTipoDecreto()
						.compareTo(DEC_INOSSERVANZA_OBBLIGHI_MS) == 0) {
			if (lTenori != null) {
				// Preparazione delle combo Esiti, una per ogni oggetto
				int lNumOggettiMSSosp = lTenori.length;
				String[] lEsiti = new String[lNumOggettiMSSosp];
				for (int i = 0; i < lNumOggettiMSSosp; i++)
					// poichè il cod esito è l'HIGH VALUE nel dominio ESITO_TENORE occorre ritradurlo (!)
					// lEsiti[i] = getEsitiMSSospPerModifica (lTenori[i].getCodOggettoTenore(),
					// lTenori[i].getCodEsitoTenore());
					lEsiti[i] = getEsitiMSCompatibiliPerModifica(lTenori[i].getCodOggettoTenore(),
							lTenori[i].getCodEsitoTenore());
				// trasferimento lista esiti
				setRequestAttribute("esiti", lEsiti);
			}
		}

		// MEV63: aggiunta gestione della sezione per minorenni
		String codOggettoProcedimento = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		if ((codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA))
				&& super.isUserUDSM()) {
			BigDecimal chiaveAnno = lFasGPMod.getFascicoloSiusModel().getChiaveAnno();
			BigDecimal chiaveProgr = lFasGPMod.getFascicoloSiusModel().getChiaveProgr();
			IMisuraAlternativa iMisuraAlternativa = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel misuraAlternativa = iMisuraAlternativa
					.ExRicercaMisuraAlternativaCorrenteByAnnoProgr(chiaveAnno, chiaveProgr);
			setRequestAttribute("misuraAlternativa", misuraAlternativa);
		}

		setRequestAttribute("modalita", "M");
		return lRetPage;
	}

	/**
	 * Preleva l'Elenco di Esiti corrispondente ad un dato oggetto in formato utile per la costruzione della
	 * combo.
	 * 
	 * @param codiceOggetto
	 * @param acodAltEsitoSelezionato
	 * @return
	 * @throws Exception
	 */
	private String getEsiti(String codiceOggetto, String acodAltEsitoSelezionato) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("codice esito Alternativo selezionato -> "
				+ ((acodAltEsitoSelezionato != null) ? acodAltEsitoSelezionato : "null"));

		// Ricerca degli Esiti previsti per l'oggetto specifico
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		Collection lColl = lDecodifiche.ExRicercaEsitiByOggetto(codiceOggetto);

		/*
		 * poichè il cod esito memorizzato nel tenore è l'HIGH VALUE nel dominio ESITO_TENORI occorre
		 * ritradurlo nel LOW VALUE(!)
		 */
		String lCodEsitoSelezionato = DecodificheUtils.getCodebyCodAlt(lColl, acodAltEsitoSelezionato);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("codice esito selezionato -> "
				+ ((lCodEsitoSelezionato != null) ? lCodEsitoSelezionato : "null"));

		// Costruzione dell'oggetto Option per la costruzione della combo
		Option lOption = new Option(lColl, lCodEsitoSelezionato);

		return lOption.toString();
	}

	// Preleva l'Elenco di Esiti corrispondente ad un dato oggetto compatibili con la modifica dell'esito
	// attuale
	private String getEsitiMSCompatibiliPerModifica(String codiceOggetto, String acodAltEsitoSelezionato)
			throws Exception {

		if (acodAltEsitoSelezionato != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito Alternativo selezionato -> " + acodAltEsitoSelezionato);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito Alternativo selezionato -> null ");

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		Collection lColl = lDecodifiche.ExRicercaEsitiCompatibiliByEsitoOggetto(codiceOggetto,
				acodAltEsitoSelezionato);
		String lCodEsitoSelezionato = DecodificheUtils.getCodebyCodAlt(lColl, acodAltEsitoSelezionato);
		Option lOption = new Option(lColl, lCodEsitoSelezionato);
		if (lCodEsitoSelezionato != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito selezionato -> " + lCodEsitoSelezionato);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito selezionato -> null ");

		return lOption.toString();
	}

	// Preleva l'Elenco di Esiti corrispondente ad un dato oggetto
	// Per la modifica di un' ordinanza AMS con trasformazione solo esiti di trasformazione e viceversa
	// Idem per le Ordinanze di EMS Riesame e Trasormazione
	// Andrebbe rivisto e rifatto con utilizzo del campo RV_ALT2_VALUE
	// private String getEsitiMSSospPerModifica(String codiceOggetto, String acodAltEsitoSelezionato)
	// throws Exception {
	//
	// if (acodAltEsitoSelezionato != null)
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("codice esito Alternativo selezionato -> " + acodAltEsitoSelezionato);
	// else
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("codice esito Alternativo selezionato -> null ");
	//
	// IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
	// Collection lColl = lDecodifiche.ExRicercaEsitiByOggetto(codiceOggetto);
	//
	// String lCodEsitoSelezionato = DecodificheUtils.getCodebyCodAlt(lColl, acodAltEsitoSelezionato);
	// Option lOption = new Option(lColl, lCodEsitoSelezionato);
	//
	// // Filtraggio per EMS Sospensione con Esito Misura Sospesa (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0136")) {
	// String[] lFiltroEsitiAMSTrasf = { "1710" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	//
	// // Filtraggio per EMS Sospensione con Esito Misura Non Sospesa
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0003")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0004")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0005")) {
	// String[] lFiltroEsitiAMSTrasf = { "1713", "1711", "1712" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	//
	// // Filtraggio per EMS Inosservanza Obblighi con Esito Diffida (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0132")) {
	// String[] lFiltroEsitiAMSTrasf = { "1735" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	//
	// if (lCodEsitoSelezionato != null)
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("codice esito selezionato -> " + lCodEsitoSelezionato);
	// else
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("codice esito selezionato -> null ");
	//
	// return lOption.toString();
	//
	// }

}