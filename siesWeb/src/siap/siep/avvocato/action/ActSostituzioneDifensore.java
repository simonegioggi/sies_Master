package siap.siep.avvocato.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.AvvocatoUtil;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActSostituzioneDifensore extends ActProvvedimentoDifensore implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Sostituzione di un avvocato su un fascicolo SIEP, recupera dalla form i dati del nuovo
	 * avvocato e l'id del vecchio. E quindi aggiorna la data fine validità del vecchio avvocato e inserisce i
	 * dati del nuovo.
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName());
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		BigDecimal id_fasc_siep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

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
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			comuneNascita = new ComuneModel(
					getDatiComuneByCodDescr(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
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

		// 20210726 Recupero tipoInserimento (reginde, sies, manuale)
		String tipoInserimento = getRequestStringParameter("lTipoInserimento");

		if (!"manuale".equals(tipoInserimento)) {
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
					getRequestStringParameter(CAMPO_PEC), "SI", descLuogoResidenza, descLuogoNascitaReginde,
					getRequestStringParameter(CAMPO_COD_STATO_NASCITA), null, null,
					getRequestStringParameter(CAMPO_INDIRIZZO), getRequestStringParameter(CAMPO_TELEFONO),
					getRequestStringParameter(CAMPO_FAX), getRequestStringParameter(CAMPO_E_MAIL),
					getRequestStringParameter(CAMPO_CODICE_FISCALE), codProvincia, codCap, new BigDecimal(1),
					getCodUtenteConnesso(), getCodUfficioUtenteConnesso(), DateUtils.getSysDate(), null, null,
					null, null, descCodLuogoNascita, descLuogoResidenza, null, codLuogoNascita,
					codLuogoResidenza, dataNascita, null, null, codNonAttivita, "00000", null, "N", null);

			/*
			 * 20210623 MEV_21 Se l'avvocato certificato RegInde non è presente in SIES si inserisce Se è già
			 * presente in SIES si effettua l'aggiornamento con i dati da Reginde.
			 */
			if (lAvvCertRegSies == null) {
				amReginde = lCtrl.ExInserisciAvvocato(amReginde);
			} else {
				// 20210720 MEV_21 Se l'avvocato certificato ha cambiato Foro, si storicizza
				// l'avvocato legato al vecchio Foro (con FLAG_REGINDE="NO") e si inserisce un nuovo Avvocato.
				// Se non cambia il foro si aggiornano solo i dati provenienti da REGINDE o non si
				// interviene(Avv. presente solo in SIES).
				if (lAvvCertRegSies.getForo().equals(amReginde.getForo())) {
					amReginde.setIdAvvocato(lAvvCertRegSies.getIdAvvocato());
					amReginde.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					amReginde.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					amReginde.setDataAggiornamento(DateUtils.getSysDate());
					if (flagReginde)
						amReginde = lCtrl.ExAggiornaAvvocatoDaReginde(amReginde);
					else
						amReginde = lAvvCertRegSies;
				} else {
					lAvvCertRegSies.setFlagRegInde("NO");
					lAvvCertRegSies.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					lAvvCertRegSies.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lAvvCertRegSies.setDataAggiornamento(DateUtils.getSysDate());
					lAvvCertRegSies = lCtrl.ExAggiornaAvvocatoDaReginde(lAvvCertRegSies);
					amReginde = lCtrl.ExInserisciAvvocato(amReginde);
				}
			}
			idAvvocato = amReginde.getIdAvvocato();

		} else {
			// 20210722 MEV_21 Avvocato non presente sia in RegInde che in SIES.
			idAvvocato = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);
		}

		AvvocatoModel lAvvModRic = new AvvocatoModel();
		BigDecimal id_avv_old = getRequestBigDecimalParameter("idAvvVecchio");

		// ==========================================================================
		// Recupero l'elenco degli avvocati attualmente associati al fascicolo
		// per controllare che nella sostituzione non vengano inseriti più avvocati
		// se non di fiducia
		// ==========================================================================
		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoFascicoloSiepModel lAvvFascModPrec = new AvvocatoFascicoloSiepModel();
		lAvvFascModPrec.setFasSieIdFascicoloSiep(id_fasc_siep);

		Vector lVectPrec = null;
		try {
			lVectPrec = new Vector();
			// recupera tutti gli avvocati attualmente associati al fascicolo
			// n.b. recupera solo i dati della tabella AVVOCATO
			lVectPrec = lCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore nella verifica degli avvocati già a sistema: " + e.getMessage());
		}

		// ==========================================================================
		// Prima di sostituire l'avvocato vecchio con il nuovo devo verificare
		// che non vengano inseriti più avvocati se non di fiducia
		// ==========================================================================
		if (lVectPrec.size() > 1) {

			lAvvModPrec = (AvvocatoModel) lVectPrec.get(0);
			String lDescrTipo = lAvvModPrec.getDescrTipo();

			if ((lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02"))
					|| (lDescrTipo.equalsIgnoreCase("Della Fase di Giudizio")
							&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("03"))) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi di Fiducia o entrambi della Fase di Giudizio!");
			}
		}

		// ==========================================================================
		// Carico il model di Inserimento con i dati recuperati dalla Form
		// ==========================================================================
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();

		lAvvFascMod.setAvvIdAvvocato(idAvvocato);
		lAvvFascMod.setFasSieIdFascicoloSiep(id_fasc_siep);

		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) { // d'ufficio
			lAvvFascMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_DESIGNAZIONE,
							ICostantiAvvocato.CAMPO_MESE_DATA_DESIGNAZIONE,
							ICostantiAvvocato.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		} else if (lAvvFascMod.getCodTipoAvvocato().equals("02")) { // di fiducia
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocato.CAMPO_ANNO_DATA_NOMINA, ICostantiAvvocato.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocato.CAMPO_GIORNO_DATA_NOMINA));
		} else if (lAvvFascMod.getCodTipoAvvocato().equals("03")) { // Della Fase di Giudizio
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate()); // ?????????????
		}

		lAvvFascMod.setDataFineValidita(null);
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod.setCodMotivoDesignazione(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)
				&& (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
						&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
								.equals("-"))) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else
			lAvvFascMod.setSedeAutorita("-");

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod.setIndirizzoTipoAutorita(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod.setCodTipoAutoritaDif(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF)
						&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF)
								.equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(
					ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));
			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvFascMod.setSedeAutoritaDif("-");
		}

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_NOTE)) {
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocato.CAMPO_NOTE));
		}

		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// ==========================================================================
		// Effettuo la sostituzione che consiste nel valorizzare la data fine
		// validità del vecchio avvocato e nell'inserimento dei dati del nuovo avvocato
		// Queste operazioni vengono effettuate contestualmente per motivi di
		// transazione
		// ==========================================================================

		AvvocatoSiepModel avv_siep_old = lCtrl
				.ExRicercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(id_avv_old, id_fasc_siep);
		AvvocatoFascicoloSiepModel lAvvFascUp = avv_siep_old.getAvvocatoFascicoloSiepModel();
		lAvvFascUp.setDataFineValidita(DateUtils.getSysDate());

		lAvvFascUp.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvFascUp.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAvvFascUp.setDataAggiornamento(DateUtils.getSysDate());

		lAvvFascUp.setMotivo("Sostituzione");

		lAvvFascMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_NOTE))
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocato.CAMPO_NOTE));

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
			lAvvModRic.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE));
			lAvvModRic
					.setDescLuogoNascitaReginde(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE));

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
			lAvvModRic.setFlagRegInde("NO");
			// Recupero dataNascita, Stato Attività Avvocato.
			if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
					&& (!isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA)
							&& (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_NASCITA))))
				dataNascita = getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA);
			if (!isRequestParameterNullObj(CAMPO_COD_NON_ATTIVITA))
				codNonAttivita = getRequestStringParameter(CAMPO_COD_NON_ATTIVITA);

			lAvvModRic.setDataNascita(dataNascita);
			lAvvModRic.setCodNonAttivita(codNonAttivita);
			lAvvModRic.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));

			lAvvModRic = lCtrl.ExInserisciAvvocato(lAvvModRic);

			idAvvocato = lAvvModRic.getIdAvvocato(); // Valorizzazione idAvvocato (precedentemente = null)
		}

		// Devo utilizzare IdAvvocato per referenziare l'avvocato prelevato da RegInde, da SIES, o appena
		// inserito.
		lAvvFascMod.setAvvIdAvvocato(idAvvocato);

		AvvocatoFascicoloSiepModel lAvvMod = new AvvocatoFascicoloSiepModel();
		lAvvFascMod.setAvvIdAvvocatoFascicoloSost(lAvvFascUp.getIdAvvocatoFascicoloSiep());

		String lFlagValidato = "";
		if (((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato() != null) {
			lFlagValidato = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato();
		}

		// Se avvocato d'ufficio
		if ("01".equals(lAvvFascMod.getCodTipoAvvocato()) && (lFlagValidato.equals("S"))) {
			EventoNotificaModel lEve = CreoProvvedimento(lAvvFascMod);
			lAvvMod = lCtrl.ExSostituzioneAvvocato(lEve, lAvvFascUp, lAvvFascMod);
			setRequestAttribute("lEve", lEve);
		} else {
			lAvvMod = lCtrl.ExSostituzioneAvvocato(lAvvFascUp, lAvvFascMod);
		}

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.avvocato.action.ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep&idAvvFascicoloSiep="
				+ lAvvMod.getIdAvvocatoFascicoloSiep();
	}

}