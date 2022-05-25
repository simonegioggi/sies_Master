package siap.sige.avvocato.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.util.AvvocatoUtil;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;

public class ActInserisciAvvocato extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Avvocato
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// paramentro passato solo nel caso di iscrizione guidata
		// 20210608 e successivamente anche per la MEV Scheda-21
		if (!isRequestParameterNullObj("lTipoFunzione"))
			setRequestAttribute("lTipoFunzione", getRequestStringParameter("lTipoFunzione"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName());
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();

		// MEV_21: controllo se la ricerca proviene da Reginde, da SIES o si fa un nuovo inserimento.
		BigDecimal idAvvocato = null;
		boolean flagReginde = false;

		// 20210722 MEV_21 Si recuperano tutte le informazioni dalla Form.
		// Recupero Codice e descrizione comune di nascita.
		AvvocatoModel amReginde = null;
		Date dataNascita = null;
		String codLuogoNascita = "-";
		String codProvincia = "-";
		String codCap = null;
		String codNonAttivita = "-";
		String descCodLuogoNascita = "";
		String descLuogoNascitaReginde = "";
		ComuneModel comuneNascita = null;
		// 20210722 MEV_21 Controllo e valorizzazione comuneNascita.
		// Se presente, dal codice comune (e dalla descrizione).

		// D.F. 2022.05.06
		// - comuneNascita (ComuneModel)
		// 20210726 Recupero tipoInserimento (reginde, sies, manuale)
		String tipoInserimento = getRequestStringParameter("lTipoInserimento");
		siesLogger.debug("tipoInserimento = " + tipoInserimento);
		if (!"manuale".equals(tipoInserimento)) {
			String codiFisc = getRequestStringParameter(CAMPO_CODICE_FISCALE);

			siesLogger.debug("Recupero il comune di nascicta a partire dalcodice fiscale = " + codiFisc);
			siesLogger.debug("CAMPO_ID_AVVOCATO = " + getRequestStringParameter(CAMPO_ID_AVVOCATO));
			if (getRequestStringParameter(CAMPO_ID_AVVOCATO).contains("COA"))
				flagReginde = true;

			comuneNascita = AvvocatoUtil.calcolaComuneNascita(codiFisc);
			siesLogger.debug("comuneNascita = " + comuneNascita);
			codLuogoNascita = comuneNascita.getCodComune();
			descCodLuogoNascita = comuneNascita.getDescrizione();
			codCap = comuneNascita.getCap();
			codProvincia = comuneNascita.getCodProvincia();

			// Salvo comunque la descrizione del comune di nascicta reginde per tenerne traccia se diversa da
			// quella
			// calcolata dal CF
			if (!descCodLuogoNascita.equals(getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)))
				descLuogoNascitaReginde = getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA);
		} else {

			if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
					&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
				comuneNascita = new ComuneModel(getDatiComuneByCodDescr(
						getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
						getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)));
				codLuogoNascita = comuneNascita.getCodComune();
				codProvincia = comuneNascita.getCodProvincia();
				codCap = comuneNascita.getCap();
				descCodLuogoNascita = comuneNascita.getDescrizione();

				// altrimenti dalla sola descrizione (rischio omonimi).
			} else if (getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA).length() > 2) {
				comuneNascita = new ComuneModel(
						getDatiComuneByDescrOmonimia(getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)));
				descCodLuogoNascita = comuneNascita.getDescrizione();
				codLuogoNascita = comuneNascita.getCodComune();
				// altrimenti , in caso di Paese di Nascita Estero, dalla routine che ricava i dati dal C.F.
			} else if (getRequestStringParameter(CAMPO_COD_STATO_NASCITA).length() == 3
					&& !("039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA)))
					&& getRequestStringParameter(CAMPO_CODICE_FISCALE).length() == 16) {
				comuneNascita = AvvocatoUtil
						.calcolaComuneNascita(getRequestStringParameter(CAMPO_CODICE_FISCALE));
				// comuneNascita, in caso di stato estero, conterrà informazioni dello stato.
			}
		}

		if (getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE).length() > 0)
			descLuogoNascitaReginde = getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE);

		// Recupero Codice e descrizione comune di residenza.
		String codLuogoResidenza = "-";
		ComuneModel comuneResidenza = null;
		String descLuogoResidenza = !isRequestParameterNullObj(CAMPO_DESC_COMUNE_STUDIO)
				? getRequestStringParameter(CAMPO_DESC_COMUNE_STUDIO)
				: null;
		if (Utils.isPresent(descLuogoResidenza)) {
			try {
				comuneResidenza = new ComuneModel(getCodComuneByDescr(descLuogoResidenza));
			} catch (Exception e) {
				siesLogger.info(e.getMessage());

				// MEV_21: nel caso di avvocato REGINDE il dato potrebbe essere errato ma non modificabile.
				if (!"manuale".equals(tipoInserimento)) {
					// Non rilancio eccezione, resta valorizzata la descrizione dello studio in form
				} else
					throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
			}

			if (!Utils.isNullObj(comuneResidenza)) {
				codLuogoResidenza = comuneResidenza.getCodComune();
				descLuogoResidenza = comuneResidenza.getDescrizione();
			}
		}

		// Recupero dataNascita, stato Attività Avvocato.
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
				&& (!isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA)
						&& (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_NASCITA))))
			dataNascita = getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
					ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA, ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA);
		if (!isRequestParameterNullObj(CAMPO_COD_NON_ATTIVITA))
			codNonAttivita = getRequestStringParameter(CAMPO_COD_NON_ATTIVITA);

		if (!"manuale".equals(tipoInserimento)) {
			// Quindi selezionato da REGINDE o da SIEP (ma certificato reginde)
			if (getRequestStringParameter(CAMPO_ID_AVVOCATO).contains("COA"))
				flagReginde = true;
			// Provengo da Reginde o da SIES: quindi si cerca l'avvocato certificato su tabella AVVOCATO;
			// se esiste lo aggiorno, altrimenti inserisco nuovo avvocato da Reginde su SIES!

			// 20210614 MEV_21 Si esegue la ricerca puntuale dell'Avvocato certificato RegInde in SIES.
			AvvocatoModel lAvvCertRegSies = new AvvocatoModel();
			lAvvCertRegSies.setNome(getRequestStringParameter(CAMPO_NOME));
			lAvvCertRegSies.setCognome(getRequestStringParameter(CAMPO_COGNOME));
			lAvvCertRegSies.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE));
			lAvvCertRegSies.setFlagRegInde("SI");
			lAvvCertRegSies = lCtrl.ExRicercaAvvocatoCertRegInde(lAvvCertRegSies);

			amReginde = new AvvocatoModel(null, getRequestStringParameter(CAMPO_COGNOME),
					getRequestStringParameter(CAMPO_NOME),
					getRequestStringParameter(CAMPO_FORO).toUpperCase(), null,
					getRequestStringParameter(CAMPO_INDIRIZZO), getRequestStringParameter(CAMPO_TELEFONO),
					getRequestStringParameter(CAMPO_FAX), getRequestStringParameter(CAMPO_E_MAIL),
					codLuogoNascita, codLuogoResidenza, descCodLuogoNascita, descLuogoResidenza, dataNascita,
					getCodUtenteConnesso(), DateUtils.getSysDate(), null, null, null,
					getCodUfficioUtenteConnesso(), null, null, null, codNonAttivita, "00000", null, "N", null,
					getRequestStringParameter(CAMPO_CODICE_FISCALE), codProvincia, codCap, new BigDecimal(1),
					null, getRequestStringParameter(CAMPO_PEC), "SI", descLuogoResidenza,
					getRequestStringParameter(CAMPO_COD_STATO_NASCITA), null, descLuogoNascitaReginde, null);

			/*
			 * 20210623 MEV_21 Se l'avvocato certificato RegInde non è presente in SIES si inserisce Se è già
			 * presente in SIES si effettua l'aggiornamento con i dati da Reginde.
			 */
			if (flagReginde && lAvvCertRegSies == null) {
				amReginde = lCtrl.ExInserisciAvvocato(amReginde);
			} else {
				// ?? d.f. 20220506 in questo else si da per scontato che lAvvCertRegSies!=null ovvero è stato
				// trovato a sistema un avvocato certificato reginde
				// con stesso nome cognome e cf di quello della form
				// 20210720 MEV_21 Se l'avvocato certificato ha cambiato Foro, si storicizza
				// l'avvocato legato al vecchio Foro (con FLAG_REGINDE="NO") e si inserisce un nuovo Avvocato.
				// Se non cambia il foro si aggiornano solo i dati provenienti da REGINDE o non si
				// interviene(Avv. presente solo in SIES).
				siesLogger.debug("flagReginde = " + flagReginde);
				siesLogger.debug("lAvvCertRegSies = " + lAvvCertRegSies);

				if (lAvvCertRegSies.getForo().equals(amReginde.getForo())) {
					siesLogger.debug("Il foro non è cambiato...");
					amReginde.setIdAvvocato(lAvvCertRegSies.getIdAvvocato());
					amReginde.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					amReginde.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					amReginde.setDataAggiornamento(DateUtils.getSysDate());
					if (flagReginde) {
						siesLogger.debug(
								"Il foro non è cambiato: flagReginde = true aggiorno i dati dell'avvocato reginde a sistema ");
						amReginde = lCtrl.ExAggiornaAvvocatoDaReginde(amReginde);
					} else {
						siesLogger.debug("Il foro non è cambiato... flagReginde = false ");
						amReginde = lAvvCertRegSies;
					}
				} else {
					lAvvCertRegSies.setFlagRegInde("NO");
					lAvvCertRegSies.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					lAvvCertRegSies.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lAvvCertRegSies.setDataAggiornamento(DateUtils.getSysDate());
					// Stroricizzo
					lAvvCertRegSies = lCtrl.ExAggiornaAvvocatoDaReginde(lAvvCertRegSies);
					// Inserisco il "nuovo" avvocato (nuovo FORO)
					amReginde = lCtrl.ExInserisciAvvocato(amReginde);
				}
			}
			idAvvocato = amReginde.getIdAvvocato();
		} else {
			// 20210722 MEV_21 Avvocato non presente sia in RegInde che in SIES.
			idAvvocato = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);
		}

		// 20210622 Ricerca Avvocati già assegnati al Fascicolo.
		AvvocatoModel lAvvMod = new AvvocatoModel();
		Vector lVectRic = new Vector();
		Vector lVectPrec = null;
		AvvocatoModel lAvvModRic = new AvvocatoModel();

		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascModPrec = new AvvocatoFascicoloSigeModel();
		lAvvFascModPrec.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());
		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" nessun elemento trovato");
		}

		if (lVectPrec.size() > 0) {
			lAvvModPrec = ((AvvocatoSigeModel) lVectPrec.get(0)).getAvvocato();
			String lDescrTipo = lAvvModPrec.getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}

			if (idAvvocato != null && lAvvModPrec.getIdAvvocato()
					.compareTo(/* getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO) */idAvvocato) == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta già inserito!");
		}

		// 20210620 Costruzione AvvocatoFascicoloSiep
		// puntando all'Avvocato appena inserito/individuato con ID = idAvvocato.
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();

		// 20210705 In caso di inserimento Avvocato non certificato (idAvvocato=null) non si effettua la
		// ricerca.
		if (idAvvocato != null) {
			lAvvMod.setIdAvvocato(idAvvocato);
			// lAvvMod.setIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));
			lVectRic = lCtrl.ExRicercaAvvocato(lAvvMod);
			lAvvModRic = (AvvocatoModel) lVectRic.get(0);

			if (lAvvModRic.getDataSospensione() != null
					&& !"-".equals(lAvvModRic.getDataSospensione().toString()))
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta sospeso!");

			if (lAvvModRic.getDataRadiazione() != null
					&& !"-".equals(lAvvModRic.getDataRadiazione().toString()))
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta radiato!");

			if (lAvvModRic.getCodNonAttivita() != null && !lAvvModRic.getCodNonAttivita().equals("-")
					&& !lAvvModRic.getCodNonAttivita().equals("A")) // 20210627 MEV_21
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta non in attività per  "
								+ lAvvModRic.getDescrNonAttivita() + "");
		}

		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) {
			lAvvFascMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE,
							ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE,
							ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("02")) {
			lAvvFascMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA,
							ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA,
							ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("03")) {
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());
		}
		lAvvFascMod.setDataFineValidita(null);
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod.setCodMotivoDesignazione(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)
				&& (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA)
						&& !getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA)
								.equals("-"))) {
			// String lComneAutorita =
			// getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else
			lAvvFascMod.setSedeAutorita("-");

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod.setIndirizzoTipoAutorita(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod.setCodTipoAutoritaDif(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF)
						&& !getRequestStringParameter(
								ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvFascMod.setSedeAutoritaDif("-");
		}
		// CAMPO_COD_TIPO_AUTORITA_DIF
		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAvvFascMod.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());
		lAvvFascMod.setAvvIdAvvocato(/* getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO) */idAvvocato);
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE)) {
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));
		}

		// 20210726 Inserimento di un nuovo avvocato solo se l'avvocato non è cert. Reginde
		// e se non è presente in SIES.
		if (!flagReginde && idAvvocato == null) {
			lAvvModRic.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
			lAvvModRic.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL));
			lAvvModRic.setPec(getRequestStringParameter(CAMPO_PEC));
			lAvvModRic.setFlagCancellato("N");
			lAvvModRic.setFlagRegInde("NO");
			lAvvModRic.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE));
			lAvvModRic.setDescLuogoNasRegInde(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE));
			lAvvModRic.setCodLuogoNascita(codLuogoNascita);
			lAvvModRic.setDescLuogoNascita(descCodLuogoNascita);
			lAvvModRic.setProvincia(codProvincia);
			lAvvModRic.setCap(codCap);

			lAvvModRic.setCodComuneResidenza(codLuogoResidenza);
			lAvvModRic.setDescrComuneStudio(descLuogoResidenza);

			lAvvModRic.setCodUffAppartenenza(getCodUfficioUtenteConnesso());
			lAvvModRic.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAvvModRic.setDataInserimento(DateUtils.getSysDate());
			lAvvModRic.setFlagVisualizza(new BigDecimal(1));
			lAvvModRic.setDataNascita(dataNascita);
			lAvvModRic.setCodNonAttivita(codNonAttivita);
			lAvvModRic.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));

			lAvvMod = lCtrl.ExInserisciAvvocato(lAvvModRic);
			idAvvocato = lAvvMod.getIdAvvocato(); // Valorizzazione idAvvocato inserito
		}

		lAvvMod.setIdAvvocato(idAvvocato);
		lCtrl.ExInserisciAvvocato(lAvvMod, lAvvFascMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ idAvvocato;

		return lPage;
	}

}