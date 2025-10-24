package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciDifferimentoSORV</p>
 * <p>Description: Questa classe pilota l'inserimento dei dati dei Provvedimento
 * SIUS riguardanti il Differimento:<br>
 * - Concessione differimento provvisorio<br>
 * - Concessione differimento definitivo<br>
 * - Revoca Differimento<br>
 * - Rigetto Differimento<br>
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.siep.calcolopena.controller.ICalcoloPena;
//import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciDifferimentoSORV extends ActMisuraAlternativa
		implements ICostantiSospensione, ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Questa classe pilota l'inserimento dei dati dei Provvedimento SIUS riguardanti il Differimento:<br>
	 * - Concessione differimento provvisorio<br>
	 * - Concessione differimento definitivo<br>
	 * - Revoca Differimento<br>
	 * - Rigetto Differimento<br>
	 * <br>
	 * Vengono effettuati i calcoli della data fine differimento se necessario
	 *
	 * n.b. Il caricamento dei dati del provvedimento della Sorveglianza viene effettuato solamente se
	 * l'utente ha inserito i dati manualmente o se ha selezionato in provvedimento già a sistema (Seleziona
	 * dalla lista) modificandone il contenuto. In questo secondo caso viene inserito in NUOVO provvedimento
	 * ignorando il precedente che resterà a sistema.
	 * 
	 * @return Chiama la ActLoadInsDifferimentoOE tramite la Main.jsp
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lTipoProvvedimento = getRequestStringParameter(TIPO_DIFFERIMENTO);

		MisuraAlternativaModel lMisuraAltModel = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

		BigDecimal lIdEventoOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

		// ==========================================================================
		// Se esiste CAMPO_ID_DOCUMENTO_SIUS vuol dire che l'utente ha selezionato
		// il decreto/ordinanza dalla lista senza modificarne i dati. In questo
		// caso recupero i dati dal DB
		// ==========================================================================
		if (lIdEventoOrdinanza != null && !lIdEventoOrdinanza.toString().equals(""))
			lMisuraAltModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdEventoOrdinanza);

		// ==========================================================================
		// Nuovo Provvedimento della Sorveglianza o Provvedimento modificato, devo
		// inserirlo
		// ==========================================================================
		if (lMisuraAltModel == null) {
			// =====================
			// EVENTO (TDS - UDS)
			// =====================
			// Tipo provvedimento: 02 - Decreto, 03 - Ordinanza
			String lTipoDecisione = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);

			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));

			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));

			// Data Emissione Provvedimento
			Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);

			EventoNotificaModel lEveMod = new EventoNotificaModel();

			// Combo 'Oggetto Decisione'
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

			lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoDecisione,
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento SIUS = " + lEveMod.getEvento());

			// ========================================================================
			// Nel caso di rigetto con posizione giuridica diversa da libero in
			// differimento provvisorio (17) si effettua la mera annotazione, quindi
			// l'evento va inserito validato
			// ========================================================================
			String codPosizioneGiuridica = getRequestStringParameter(
					ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
			if (lTipoProvvedimento.equals(DIFFERIMENTO_RIGETTO) && !codPosizioneGiuridica.equals("17")) {
				lEveMod.getEvento().setFlagDocumentoRegistrato("S");
			}

			// ===============================
			// DEPOSITO ORDINANZA o DECRETO
			// ===============================
			DepositoDecretoModel lDepDecMod = null;
			DepositoOrdinanzaPcModel lDepOrdMod = null;

			if (lTipoDecisione.equals("03")) // Ordinanza
				lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			else if (lTipoDecisione.equals("02")) // Decreto
				lDepDecMod = setDepositoDecreto(lCodiceUffEmi);

			// Atti trasmessi al TDS di
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE)
							.length() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("al tds di >"
						+ getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE)
						+ "<");
				// Atti trasmessi al TDS di
				String lCodiceUffTds = getCodUfficioByCodTipoUfficioDescrComune("TDS",
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE));
				lDepDecMod.setCodTdsComp(lCodiceUffTds);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Deposito Ordinanza = " + lDepOrdMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Deposito Decreto   = " + lDepDecMod);

			// =====================
			// MISURA ALTERNATIVA
			// =====================
			String lNaturaDecisione = "";
			if (lTipoProvvedimento.equals(DIFFERIMENTO_PROV) || lTipoProvvedimento.equals(DIFFERIMENTO_DEF)) {
				lNaturaDecisione = "CO"; // Concede
			} else if (lTipoProvvedimento.equals(DIFFERIMENTO_RIGETTO)) {
				lNaturaDecisione = "RG"; // Rigetta
			} else if (lTipoProvvedimento.equals(DIFFERIMENTO_REVOCA)) {
				lNaturaDecisione = "RE"; // Revoca
			}

			// Da scarcerare/già scarcerato
			String lUfficioScarc = "-";
			if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE)) {
				lUfficioScarc = getRequestStringParameter(
						ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE);
			}

			MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
			lMisMod = setMisuraAlternativa(lTipoDecisione, lNaturaDecisione, lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScarc);

			Date lDataRinvioFinoAl = null;
			// ========================================================================
			// Sezione presente solo nel caso di concessione differimento (prov/def)
			// - Rinvio fino al
			// - Rinvio nella misura di
			// - Fino alla decisione del TDS
			// - Con atti trasmessi al TDS
			// ========================================================================
			if (lTipoProvvedimento.equals(DIFFERIMENTO_PROV) || lTipoProvvedimento.equals(DIFFERIMENTO_DEF)) {

				// Fino alla decisione del TDS
				String lFlagDecisioneTribunale = "N"; // Flag fino alla decicione del tds
				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE)) {
					lFlagDecisioneTribunale = getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE);
					lMisMod.setFlagDecisioneTribunale(lFlagDecisioneTribunale);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("FlagDecisioneTribunale = " + lFlagDecisioneTribunale);
				}

				// Con atti trasmessi al TDS di
				if (lDepDecMod != null && lDepDecMod.getCodTdsComp() != null)
					lMisMod.setCodTdsCompetente(lDepDecMod.getCodTdsComp());

				// Data Differimento Esecuzione
				if (!this.isRequestParameterNullObj(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA)) {
					lMisMod.setDataInizioMisura(
							getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA,
									ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA,
									ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA));
				}

				// Rinvio nella misura di
				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)) {
					lMisMod.setNumAnniMisura(
							getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA));
					lMisMod.setNumMesiMisura(
							getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA));
					lMisMod.setNumGiorniMisura(getRequestBigDecimalParameter(
							ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA));
				}

				// ========================================================================
				// Rinvio fino al.
				// Nel caso sia stato inserito il quantum di rinvio e non sia stata inserita
				// la data Rinvio fino al, tale data viene calcolata in automatico a partire
				// dalla DATA_DIFFERIMENTO (in ogni caso non viene calcolata se inserita dall'utente)
				// ========================================================================
				if (lFlagDecisioneTribunale.equals("S")) {
					// Se selezionato fino alla decisione del TDS non faccio calcoli sulla
					// data fine misura. Sarà il tribunale a decidere.
				} else if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA)
						&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA)
								.length() > 0) {

					lDataRinvioFinoAl = getRequestDateParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA,
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA,
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data fine misura inserita = " + lDataRinvioFinoAl);

					lMisMod.setDataFineMisura(lDataRinvioFinoAl);
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data fine misura assente");

					CalendarModel lCalMod = new CalendarModel();
					lCalMod.setNumAnni(lMisMod.getNumAnniMisura());
					lCalMod.setNumMesi(lMisMod.getNumMesiMisura());
					lCalMod.setNumGiorni(lMisMod.getNumGiorniMisura());

					CalendarUtil lCalUtil = new CalendarUtil();
					lCalUtil.ricalcolaGAM(lCalMod);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lCalMod = " + lCalMod);

					if (!lCalUtil.isZero(lCalMod)) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Calcolo data fine differimento a partire dai Quantum ");

						ICalcoloPena lCalPenCtrl = SIEPLookupRemote.getCalcoloPenaRemote();
						lDataRinvioFinoAl = lCalPenCtrl.exCalcolaNuovaDataFine(lMisMod.getDataInizioMisura(),
								lCalMod, false); //

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("data fine calcolata: " + lDataRinvioFinoAl);

						// n.b. false esclude il giorno del differimento dal calcolo, in questo
						// modo se il diff è il 10/10/2000 e sono stati concessi 5 gg
						// la data rinvio calcolata è il 15/10/2000.
						// La data fino al viene considerata ancora come giorno di differimento
						// mentre la data del differimento NO!
						// Giorni di differimento goduti: 11-12-13-14-15
						lMisMod.setDataFineMisura(lDataRinvioFinoAl);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Rinvio fino al calcolata = " + lDataRinvioFinoAl);
					}
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Misura Alternativa prima inserimento = " + lMisMod);

			// =======================================================================
			// Carico i dati del TENORE
			// =======================================================================
			String esito_tenore = "";
			if (lTipoProvvedimento.equals(DIFFERIMENTO_PROV) || lTipoProvvedimento.equals(DIFFERIMENTO_DEF)) {
				esito_tenore = "0001"; // Concede
			} else if (lTipoProvvedimento.equals(DIFFERIMENTO_RIGETTO)) {
				// esito_tenore = "0002"; // Rigetta
				esito_tenore = getRequestStringParameter(ICostantiEvento.CAMPO_COD_ESITO);
			} else if (lTipoProvvedimento.equals(DIFFERIMENTO_REVOCA)) {
				esito_tenore = "0007"; // Accoglie Proposta e Revoca
			}
			TenoreModel lTenMod = setTenore(new BigDecimal(1), esito_tenore);

			// if( !this.isRequestParameterNullObj(ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO)){
			// lTenMod.setCodDettaglioOggetto(this.getRequestStringParameter(ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO));
			// }

			if (lDataRinvioFinoAl != null) {
				lTenMod.setDataFine(lDataRinvioFinoAl);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Tenore = " + lTenMod);

			// ========================================================================
			// Inserimento dei dati del Provvedimeto della Sorveglianza:
			// EVENTO, MISURA_ALTERNATIVA, DEPOSITO_ORDINANZA_PC o DEPOSITO_DECRETO, TENORE
			// ========================================================================
			MisuraAlternativaModel lMisuraModel = null;
			if (lTipoDecisione.equals("03")) { // Ordinanza
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lMisAltCtrl.ExInserisciMisuraAlternativaEventoNotifica");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lEveMod = " + lEveMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lDepOrdMod = " + lDepOrdMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lTenMod = " + lTenMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lMisMod = " + lMisMod);
				lMisuraModel = lMisAltCtrl.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod,
						lTenMod, lMisMod);
			} else if (lTipoDecisione.equals("02")) { // Decreto
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lMisAltCtrl.ExInserisciDecretoSospEventoNotifica");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lEveMod = " + lEveMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lDepDecMod = " + lDepDecMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lTenMod = " + lTenMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lMisMod = " + lMisMod);
				lMisuraModel = lMisAltCtrl.ExInserisciDecretoSospEventoNotifica(lEveMod, lDepDecMod, lTenMod,
						lMisMod);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Misura Alternativa dopo inserimento" + lMisuraModel);

			lIdEventoOrdinanza = lMisuraModel.getEveIdEvento();
		} else {
			// ========================================================================
			// Il Provvedimento della Sorveglianza è già a sistema, non devo fare nulla
			// ========================================================================
		}

		// ==========================================================================
		// Richiamo la Action di visualizzazione del dettaglio e inserimento
		// provvedimento SIES
		// ==========================================================================
		String lPage = null;

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadInsDifferimentoOE&"
				+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "=" + lIdEventoOrdinanza;

		return lPage;
	}

}