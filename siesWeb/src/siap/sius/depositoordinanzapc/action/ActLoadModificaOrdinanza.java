package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.controller.MisuraSicurezzaController;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.depositoordinanzapc.util.RicercaProvvedimentiCollegati;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadModificaOrdinanza
 * </p>
 * <p>
 * Description: Classe Action devoluta alla preparazione della Form di modifica di un'Ordinanza.
 * </p>
 * <p>
 * Poichè i dati da visualizzare sono gli stessi utilizzati per la visualizzazione del Dettaglio, la classe è
 * ottenuta come specializzazione della ActDettaglioEmissioneOrdinanza in modo da poter utilizzare le stesse
 * funzioni per ricavare i dati.
 * </p>
 * *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eunics
 * </p>
 *
 * @version 2.2
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadModificaOrdinanza extends ActDettaglioEmissioneOrdinanza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// Si utilizza la jsp di Dettaglio
		String lRetPage = super.processRequest();

		// Si richiama il lock
		lockApplicativo("Ordinanza");

		TenoreModel[] lTenori = super.mOrdEveTenPreMod.getTenori();
		if (lTenori != null) {
			// Preparazione delle combo Esiti, una per ogni oggetto
			int lNumOggetti = lTenori.length;
			String[] lEsiti = new String[lNumOggetti];
			for (int i = 0; i < lNumOggetti; i++)
				// poichè il cod esito è l'HIGH VALUE nel dominio ESITO_TENORI occorre ritradurlo (!)
				lEsiti[i] = getEsiti(lTenori[i].getCodOggettoTenore(), lTenori[i].getCodEsitoTenore());
			// trasferimento lista esiti
			setRequestAttribute("esiti", lEsiti);
			super.setSessionAttribute("esiti", lEsiti);
		}

		if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(LIBERAZIONE_ANTICIPATA) == 0
				|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(RECLAMO_LIBERAZIONE_ANTICIPATA) == 0
				|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(REVOCA_LIBERAZIONE_ANTICIPATA) == 0
				|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(VIOLAZIONE_CEDU) == 0
				|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMI_CEDU) == 0) {
			// Ordinanza di Liberazione Anticipata , di Reclamo Liberazione Anticipata, di Revoca Liberazione
			// Anticipata, Violazione CEDU
			// devo andare a cercare i periodi di L.A. per passarli alle Form successive
			ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			Vector lLicenzePeriodi = lCtrlDep.ExRicercaLicenzeLibanticipataByEve(mIdEvento);
			this.setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

			Vector lLicenze = new Vector();
			try {
				lLicenze = lCtrlDep.ExRicercaLicenzeByEve(mIdEvento);
			} catch (F3BException F3BEx) {
			}

			// Per un'ordinanza di LA su cui sia già stato emesso un provvedimento della procura o ci siano
			// comunque licenze
			// con flag_elaborato ad "S" vanno bloccate le modifiche
			if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(LIBERAZIONE_ANTICIPATA) == 0
					&& checkStatoElaborazioneLA(mOrdEveTenPreMod.getOrdinanza(), lLicenze))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Non è consentito modificare il provvedimento! La liberazione anticipata è già stata elaborata dalla procura!");
		}

		// Per un'ordinanza di applicazione misura sicurezza l'esito è modificabile solo se
		// non altera la trasformazione di misura
		if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(MISURA_SICUREZZA) == 0
				|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(TRASFORMA_MISURA_SICUREZZA) == 0
				|| (mFasGPMod.getGeneraleProcedimentoModel() != null
						&& mFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro() != null
						&& mFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro().equals("S09"))) {
			// 20191025 [SG]: caso particolare di modifica ordinanza per U122, non eseguo operazione
			if (!"U122".equals(mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())) {
				if (lTenori != null) {
					// Preparazione delle combo Esiti, una per ogni oggetto
					int lNumOggettiMSSosp = lTenori.length;
					String[] lEsiti = new String[lNumOggettiMSSosp];
					for (int i = 0; i < lNumOggettiMSSosp; i++) {
						// poichè il cod esito è l'HIGH VALUE nel dominio ESITO_TENORE occorre ritradurlo (!)
						// lEsiti[i] = getEsitiMSTMPerModifica (lTenori[i].getCodOggettoTenore(),
						// lTenori[i].getCodEsitoTenore());
						// @emma: 28/08/2018 : intervento post-collaudo
						if ("U023".equals(
								mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())) {
							lEsiti[i] = getEsitiMSCompatibiliPerModificaU023(lTenori[i].getCodOggettoTenore(),
									lTenori[i].getCodEsitoTenore());
						} else {
							lEsiti[i] = getEsitiMSCompatibiliPerModifica(lTenori[i].getCodOggettoTenore(),
									lTenori[i].getCodEsitoTenore());
						}
					}
					// trasferimento lista esiti
					setRequestAttribute("esiti", lEsiti);
				}
			}
		}

		//
		// 01/2014 - Decreto legge 146/2013 - Misuraa lternativa Ammissione in prova
		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(AMM_PROVVISORIA_AFFIDAMENTO_IN_PROVA_SERVIZI_SOC_ART47_OP) == 0) {
			lRetPage = PG_DETT_ORDINANZA_AMM_PROVV_AFFIDAMENTO_IN_PROVA;

			if (mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione() != null
					&& mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = lUffCtrl
						.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione());

				if (lUfficio != null)
					setRequestAttribute("ufficioTDS", lUfficio);
			}

			if (mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp() != null
					&& mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = lUffCtrl
						.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp());

				if (lUfficio != null)
					setRequestAttribute("ufficioUDS", lUfficio);
			}

			if (mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante() != null
					&& mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = lUffCtrl
						.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante());

				if (lUfficio != null)
					setRequestAttribute("ufficioProcura", lUfficio);
			}
		}

		/*
		 * ISSUE MEV : aggiunto codice per gestione oggetto C029 - AP Numero MEV : 39 Autore : Gioggi Data :
		 * 20/giu/2017 Branch : MEV_39
		 */
		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(APPELLO_MS) == 0) {
			// Lettura elenco Misure di Sicurezza Collegate al Fascicolo SIUS
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			// fascicolo originale
			BigDecimal idFascicoloSiusOrigine = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
			// info per il log
			siesLogger.debug("ID del Fascicolo Origine ->" + idFascicoloSiusOrigine);
			// ricerco provvedimento impugnato
			RicercaProvvedimentiCollegati rpc = new RicercaProvvedimentiCollegati(idFascicoloSiusOrigine);
			// Ricerca dell'Ordinanza
			OrdinanzaEventoTenoriPrescrizioniModel oetpm = rpc.RicercaOrdinanza("ALL");
			if (oetpm != null && oetpm.getOrdinanza() != null)
				setRequestAttribute("ordinanza", oetpm.getOrdinanza());
			else {
				// Ricerca del Decreto
				DepositoDecretoModel ddm = rpc.RicercaDecreto("ALL");
				if (ddm != null)
					setRequestAttribute("decreto", ddm);
			}
			MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();			
			// ricerco non per id fasc sius ma per id fasc sius origine (UDS) se esiste
						// altrimenti ricerco per id fasc sius 20200125 [SG]
			aMisuraSicurezza.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null
								? lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine()
								: lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			MisuraSicurezzaController lCtrl = new MisuraSicurezzaController();
			Vector lVect = lCtrl.ExRicercaMisuraSicurezza(aMisuraSicurezza);
			MisuraSicurezzaModel msm = null;
			Vector v = new Vector();
			if (!lVect.isEmpty()) {
				msm = (MisuraSicurezzaModel) lVect.firstElement();
				v.add(msm);
				setRequestAttribute("misuresicurezza", v);
			} else {
				// Lettura Misura di Sicurezza in Esecuzione Collegata al Fascicolo SIUS EMS padre
				IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
				FascicoloGPModel fgpm = ifs.ExRicercaFascicoloByKey(idFascicoloSiusOrigine);
				BigDecimal annoEMS = fgpm.getGeneraleProcedimentoModel().getAnnoS1();
				BigDecimal progEMS = fgpm.getGeneraleProcedimentoModel().getProgrS1();
				String uffiEMS = fgpm.getFascicoloSiusModel().getChiaveUfficio();
				fgpm = ifs.ExRicercaFascicoloByAnnoProgrCodUfficio(annoEMS, progEMS, uffiEMS);
				IEsecuzioneMS lEseMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
				EsecuzioneMisuraSicurezzaModel aEMS = lEseMSCtrl
						.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(
								fgpm.getGeneraleProcedimentoModel().getFasSiuIdFascicoloSius());
				setRequestAttribute("esecuzionemisurasicurezza", aEMS);
			}

			// Occorre passare alla jsp di inserimento anche le option per l'eventuale scelta di una nuova
			// misura
			// Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
			// setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);
			Option lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza());
			// 20191018 [SG]: aggiunto codice
			if (Utils.isPresent(mOrdEveTenPreMod.getTenori())) {
				for (TenoreModel tenore : mOrdEveTenPreMod.getTenori()) {
					String codEsitoTenore = tenore.getCodEsitoTenore();
					if (ICostantiMisuraSicurezza.COD_ACCOGLIE_APPELLO_E_MODIFICA_MDS
							.equalsIgnoreCase(codEsitoTenore)) {
						// ricerco ms per id evento
						List ms = lCtrl.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(
								lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
						if (!ms.isEmpty()) {
							MisuraSicurezzaModel msModel = (MisuraSicurezzaModel) ms.get(0);
							setRequestAttribute("datiNuovaMS", msModel);
							lOptionT.setSelected(msModel.getCodTipo());
						}
						break;
					}
				}
			}
			setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);
			// valore di ritorno
			lRetPage = PG_LOAD_MOD_ORDINANZA_APPELLO_CONTRO_PROVV_MS;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Appello contro Provvedimento Misura Sicurezza: "
					+ mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza());
		}
		// ***** FINE INTERVENTO MEV_39 *****//

		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		List<MisuraSicurezzaModel> lMisureSicurezza = lCtrl.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(
				((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
						.getIdFascicoloSius());
		if (lMisureSicurezza.size() > 0) {
			Iterator<MisuraSicurezzaModel> itxMis = lMisureSicurezza.iterator();
			while (itxMis.hasNext()) {
				MisuraSicurezzaModel lMisSicuSius = itxMis.next();
				// MERGE v10: modifica all'impostazione dell'attributo
				// scritto come era inseriva sempre l'ultimo
				if (Utils.isPresent(lMisSicuSius.getEveIdEvento())) {
					setRequestAttribute("misuraSicurezza", lMisSicuSius);
					break;
				}
			}
		}

		setRequestAttribute("modalita", "M");
		siesLogger.debug("lRetPage = "+lRetPage);
		return lRetPage;
	}

	// Preleva l'Elenco di Esiti corrispondente ad un dato oggetto.
	private String getEsiti(String codiceOggetto, String acodAltEsitoSelezionato) throws Exception {

		if (acodAltEsitoSelezionato != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito Alternativo selezionato -> " + acodAltEsitoSelezionato);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito Alternativo selezionato -> null ");

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		Collection<?> lColl = lDecodifiche.ExRicercaEsitiByOggetto(codiceOggetto);
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
		Collection<?> lColl = lDecodifiche.ExRicercaEsitiCompatibiliByEsitoOggetto(codiceOggetto,
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
	// DEVE ESSERE RIVISTO e rifatto con utilizzo del campo RV_ALT2_VALUE
	// o direttamente sul LOW_VALUE (non si riescono a coprire tutti i casi)
	// private String getEsitiMSTMPerModifica(String codiceOggetto, String acodAltEsitoSelezionato)
	// throws Exception {
	//
	// if (acodAltEsitoSelezionato != null)
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.debug("codice esito Alternativo selezionato -> " + acodAltEsitoSelezionato);
	// else
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.debug("codice esito Alternativo selezionato -> null ");
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.debug("codice oggetto -> " + codiceOggetto);
	// IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
	// Collection lColl = lDecodifiche.ExRicercaEsitiByOggetto(codiceOggetto);
	//
	// String lCodEsitoSelezionato = DecodificheUtils.getCodebyCodAlt(lColl, acodAltEsitoSelezionato);
	// Option lOption = new Option(lColl, lCodEsitoSelezionato);
	//
	// // Filtraggio AMS Trasforma
	// if (codiceOggetto.equals("2110") || codiceOggetto.equals("2111") || codiceOggetto.equals("2112")
	// || codiceOggetto.equals("2113") || codiceOggetto.equals("2114")) { // Filtraggio AMS
	// // Filtraggio per AMS Misura Trasformata
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0057")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0189")) {
	// String[] lFiltroEsitiAMSTrasf = { "1198", "1206" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	//
	// // Filtraggio per AMS Misura Non Trasformata
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0051")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0052")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0004")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0005")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0003")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0002")) {
	// String[] lFiltroEsitiAMSTrasf = { "1190", "1191", "1192", "1193", "1203", "1207" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// } // Fine Filtraggio AMS
	//
	// // Filtraggio EMS Riesame
	// if (codiceOggetto.equals("2440") || codiceOggetto.equals("2441") || codiceOggetto.equals("2442")) { //
	// Filtraggio
	// // Riesame
	// // Filtraggio per EMS Riesame con Esito Misura Trasformata (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0133")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0144")) {
	// String[] lFiltroEsitiAMSTrasf = { "1741", "1747" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// // Filtraggio per EMS Riesame con Esito Misura Non Trasformata
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0002")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0003")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0004")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0005")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0053")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0054")) {
	// String[] lFiltroEsitiAMSTrasf = { "1740", "1742", "1743", "1744", "1745", "1746" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// } // Fine Filtraggio Riesame
	//
	// // Filtraggio EMS Trasformazione
	// if (codiceOggetto.equals("2660")) { // Filtraggio Trasformazione
	// // Filtraggio per EMS Trasormazione con Esito Misura Trasformata (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0134")) {
	// String[] lFiltroEsitiAMSTrasf = { "1960" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// // Filtraggio per EMS Trasformazione con Esito Misura non Trasformata
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0002")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0003")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0004")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0005")) {
	// String[] lFiltroEsitiAMSTrasf = { "1961", "1962", "1963", "1964" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// } // Fine Filtraggio Trasformazione
	//
	// // Filtraggio EMS Inosservanza Obblighi
	// if (codiceOggetto.equals("2430")) { // Inosservanza Obblighi
	// // Filtraggio per EMS Inosservanza Obblighi con Esito Misura Trasformata (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0055")) {
	// String[] lFiltroEsitiAMSTrasf = { "1730" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// // Filtraggio per EMS Inosservanza Obblighi con Esito Misura Trasformata (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0056")) {
	// String[] lFiltroEsitiAMSTrasf = { "1731" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// // Filtraggio per EMS Inosservanza Obblighi con Esito Misura Trasformata (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0058")) {
	// String[] lFiltroEsitiAMSTrasf = { "1732" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// // Filtraggio per EMS Inosservanza Obblighi con Esito Misura Trasformata (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0059")) {
	// String[] lFiltroEsitiAMSTrasf = { "1733" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// // Filtraggio per EMS Inosservanza Obblighi con Esito Misura non Trasformata
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0003")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0004")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0005")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0060")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0147")) {
	// String[] lFiltroEsitiAMSTrasf = { "1737", "1738", "1739", "1734", "1736" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// } // Fine Filtraggio Inosservanza Obblighi
	//
	// // Filtraggio EMS Cessazione
	// if (codiceOggetto.equals("2670")) { // Cessazione MS
	// // Filtraggio per EMS Cessazione con Esito Cessazione Misura (Esito non modificabile)
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0118")) {
	// String[] lFiltroEsitiAMSTrasf = { "1970" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// // Filtraggio per EMS Cessazione con Esito Nod Cessazione Misura
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0003")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0004")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0005")) {
	// String[] lFiltroEsitiAMSTrasf = { "1972", "1973", "1974" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// } // Fine Filtraggio Cessazione MS
	//
	// // Filtraggio EMS Rinvio
	// if (codiceOggetto.equals("2610") || codiceOggetto.equals("2611")) { // Rinvio MS
	// // Filtraggio per EMS Cessazione con Esito Rinvio Misura
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0001")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0119")) {
	// String[] lFiltroEsitiAMSTrasf = { "1900", "1905" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// // Filtraggio per EMS Cessazione con Esito Non Rinvio Misura
	// if (acodAltEsitoSelezionato.equalsIgnoreCase("0002")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0003")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0004")
	// || acodAltEsitoSelezionato.equalsIgnoreCase("0005")) {
	// String[] lFiltroEsitiAMSTrasf = { "1901", "1902", "1903", "1904" };
	// lOption.setFilter(lFiltroEsitiAMSTrasf);
	// }
	// } // Fine Filtraggio Rinvio MS
	//
	// if (lCodEsitoSelezionato != null)
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.debug("codice esito selezionato -> " + lCodEsitoSelezionato);
	// else
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.debug("codice esito selezionato -> null ");
	//
	// return lOption.toString();
	//
	// }

	// Verifica lo stato del flag_elaborato per l'ordinanza di LA e per le relative licenze
	private boolean checkStatoElaborazioneLA(DepositoOrdinanzaPcModel ordinanzaLA, Vector licenzeLA)
			throws Exception {

		if (ordinanzaLA.getFlagElaborato() != null && ordinanzaLA.getFlagElaborato().compareTo("S") == 0)
			return true;

		if (licenzeLA.size() > 0) {
			Iterator itx = licenzeLA.iterator();
			while (itx.hasNext()) {
				LicenzaLibAnticipataModel licLibAnt = (LicenzaLibAnticipataModel) itx.next();
				if (licLibAnt.getFlagElaborato() != null && licLibAnt.getFlagElaborato().compareTo("S") == 0)
					return true;
			}
		}

		return false;
	}

	/**
	 * @param codiceOggetto
	 * @param acodAltEsitoSelezionato
	 * @return
	 * @throws Exception
	 *
	 *             emma: 28/08/2018 : intervento post-collaudo
	 */
	private String getEsitiMSCompatibiliPerModificaU023(String codiceOggetto, String acodAltEsitoSelezionato)
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
		Collection<?> lColl = lDecodifiche.ExRicercaEsitiCompatibiliByEsitoOggettoU023(codiceOggetto,
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

}