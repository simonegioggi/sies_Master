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

public class ActSostituzioneDifensore extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Sostituzione dell' Avvocato assegnato al Fascicolo SIGE
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		BigDecimal id_fasc_sige = ((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
				.getFascicoloSige().getIdFascicoloSige();

		// Recupero i dati del Nuovo avvocato se selezionato, altrimenti l'id coincide
		// con il vecchio avvocato
		// BigDecimal id = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);
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
		// 20210727 Recupero tipoInserimento (reginde, sies, manuale)
		String tipoInserimento = getRequestStringParameter("lTipoInserimento");

		// 20210722 MEV_21 Controllo e valorizzazione comuneNascita.
		// Se presente, dal codice comune (e dalla descrizione).
		// 20210727 Si Propaga l'eccezione solo in caso di inserimento manuale.
		try {
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
		} catch (Exception e) {
			siesLogger.info(e.getMessage());
			if ("manuale".equals(tipoInserimento)) {
				throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
			} else {
			// non faccio nulla recupero dopo il comune dal CF
			}			
		}

		// In caso di iscrizione reginde o CERTIFICATO SIES il comune di nascita va sempre decodificato 
		// dal codice fiscale indipendentemente dal Comune riportato in form
		if (!"manuale".equals(tipoInserimento)) {
			String codiFiscAvv = getRequestStringParameter(CAMPO_CODICE_FISCALE);
			comuneNascita = AvvocatoUtil.calcolaComuneNascita(codiFiscAvv);
			codLuogoNascita = comuneNascita.getCodComune();
			descCodLuogoNascita = comuneNascita.getDescrizione();
			codCap = comuneNascita.getCap();
			codProvincia = comuneNascita.getCodProvincia();
			// Salvo comunque la descrizione del comune di nascita reginde per tenerne traccia
			descLuogoNascitaReginde = getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA);
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
		BigDecimal idVecchio = getRequestBigDecimalParameter("idAvvVecchio");

		// ==========================================================================
		// Recupero l'elenco degli avvocati attualmente associati al fascicolo
		// per controllare che nella sostituzione non vengano inseriti più avvocati
		// se non di fiducia
		// ==========================================================================
		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoSigeModel lAvvSigeMod = new AvvocatoSigeModel();
		AvvocatoFascicoloSigeModel lAvvFascModPrec = new AvvocatoFascicoloSigeModel();
		lAvvFascModPrec.setFasSigeIdFascicoloSige(id_fasc_sige);

		Vector lVectPrec = null;
		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore nella verifica degli avvocati già a sistema: " + e.getMessage());
		}

		if (lVectPrec.size() > 1) {
			lAvvSigeMod = (AvvocatoSigeModel) lVectPrec.get(0);
			String lDescrTipo = lAvvSigeMod.getAvvocato().getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}
		}

		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();

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
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else {
			lAvvFascMod.setSedeAutorita("-");
		}

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

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE))
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));

		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// ==========================================================================
		// Effettuo la sostituzione che consiste nel valorizzare la data fine
		// validità del vecchio avvocato e nell'inserimento dei dati del nuovo avvocato
		// Queste operazioni vengono effettuate contestualmente per motivi di
		// transazione
		// ==========================================================================

		AvvocatoFascicoloSigeModel lAvvFascUp = new AvvocatoFascicoloSigeModel();
		lAvvFascUp.setAvvIdAvvocato(idVecchio);
		lAvvFascUp.setFasSigeIdFascicoloSige(id_fasc_sige);
		lAvvFascUp.setDataFineValidita(DateUtils.getSysDate());

		lAvvFascUp.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvFascUp.setDataAggiornamento(DateUtils.getSysDate());
		lAvvFascUp.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		// if (!lAvvModRic.getCodUffAppartenenza().equals(getCodUfficioUtenteConnesso())) {
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
			lAvvModRic.setDescLuogoNasRegInde(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE));

			lAvvModRic.setCodLuogoNascita(codLuogoNascita);
			lAvvModRic.setDescLuogoNascita(descCodLuogoNascita);
			lAvvModRic.setProvincia(codProvincia);
			lAvvModRic.setCap(codCap);

			lAvvModRic.setCodComuneResidenza(codLuogoResidenza);
			lAvvModRic.setDescrComuneStudio(descLuogoResidenza);

			// ComuneModel lComModRes = new ComuneModel(getCodComuneByDescr(
			// getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			// lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic.setCodUffAppartenenza(getCodUfficioUtenteConnesso());
			lAvvModRic.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAvvModRic.setDataInserimento(DateUtils.getSysDate());
			lAvvModRic.setFlagRegInde("NO");
			lAvvModRic.setFlagVisualizza(new BigDecimal(1));

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
		// } else {
		// // Ho selezionato un avvocato dell'ufficio e ne sto modificando eventualmente
		// // i dati.
		// // Storicizzando i dati, quindi vado in update.
		// StoricoAvvocatoModel lStoricoModel = new StoricoAvvocatoModel();
		//
		// lStoricoModel.setCognome(lAvvModRic.getCognome());
		// lStoricoModel.setNome(lAvvModRic.getNome());
		// lStoricoModel.setForo(lAvvModRic.getForo());
		// lStoricoModel.setIndirizzo(lAvvModRic.getIndirizzo());
		// lStoricoModel.setTelefono(lAvvModRic.getTelefono());
		// lStoricoModel.setFax(lAvvModRic.getFax());
		// lStoricoModel.setEMail(lAvvModRic.getEMail());
		// lStoricoModel.setCodComuneResidenza(lAvvModRic.getCodComuneResidenza());
		// lStoricoModel.setCodLuogoNascita(lAvvModRic.getCodLuogoNascita());
		// lStoricoModel.setDataNascita(lAvvModRic.getDataNascita());
		// lStoricoModel.setDataSospesoFinoAl(lAvvModRic.getDataSospensione());
		// lStoricoModel.setDataRadiatoDal(lAvvModRic.getDataRadiazione());
		// lStoricoModel.setCodNonAttivita(lAvvModRic.getCodNonAttivita());
		// // mancano le note
		// lStoricoModel.setCodUfficioAppartenenza(lAvvModRic.getCodUffAppartenenza());
		//
		// lStoricoModel.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());
		//
		// // manca flag cancellato
		// lStoricoModel.setCodiceFiscale(lAvvModRic.getCodiceFiscale());
		// lStoricoModel.setProvincia(lAvvModRic.getProvincia());
		// lStoricoModel.setCap(lAvvModRic.getCap());
		//
		// // manca id_avvocato_Standard
		// lStoricoModel.setIdAvvocatoStandard(lAvvModRic.getIdAvvocatoStandard());
		//
		// lStoricoModel.setCodUfficioInserimento(lAvvModRic.getCodUfficioAggiornamento());
		// lStoricoModel.setCodOperatoreInserimento(lAvvModRic.getCodOperatoreAggiornamento());
		// lStoricoModel.setDataInserimento(lAvvModRic.getDataAggiornamento());
		//
		// // Aggiorno i dati con quanto prelevabile/modificabile della form
		// lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
		// lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
		// lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		// lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
		// lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
		// ComuneModel lComModRes = new ComuneModel(getCodComuneByDescr(
		// getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
		// lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());
		//
		// lAvvModRic.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		// lAvvModRic.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		// lAvvModRic.setDataAggiornamento(DateUtils.getSysDate());
		//
		// lAvvModRic = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		// }

		// Devo utilizzare IdAvvocato per referenziare l'avvocato prelevato da RegInde, da SIES, o appena
		// inserito.
		lAvvFascMod.setAvvIdAvvocato(idAvvocato);
		lAvvFascMod.setFasSigeIdFascicoloSige(id_fasc_sige);
		// lAvvFascMod.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());

		AvvocatoFascicoloSigeModel lAvvMod = new AvvocatoFascicoloSigeModel();
		lAvvMod = lCtrl.ExSostituzioneAvvocato(lAvvFascUp, lAvvFascMod);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ lAvvMod.getAvvIdAvvocato().toString();
	}

}