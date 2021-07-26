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
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActInserisciAvvocato extends ActProvvedimentoDifensore implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Avvocato
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// paramentro passato solo nel caso di iscrizione guidata
		// 20210608 e successivamente anche per la MEV Scheda-21
		if (!isRequestParameterNullObj("lTipoFunzione"))
			setRequestAttribute("lTipoFunzione", getRequestStringParameter("lTipoFunzione"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName());
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();

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

		// 20210726 Avvocato Recuperato da RegInde o da SIES
		if (getRequestStringParameter(CAMPO_ID_AVVOCATO) != null) {
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

		// 20210622 Ricerca Avvocati già assegnati al Fascicolo.
		AvvocatoModel lAvvMod = new AvvocatoModel();
		Vector lVectRic = new Vector();
		Vector lVectPrec = null;
		AvvocatoModel lAvvModRic = new AvvocatoModel();

		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoFascicoloSiepModel lAvvFascModPrec = new AvvocatoFascicoloSiepModel();
		lAvvFascModPrec.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) (getSessionAttribute("fascicolo"))).getIdFascicoloSiep());

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" nessun elemento trovato");
		}

		if (lVectPrec.size() > 0) {
			lAvvModPrec = (AvvocatoModel) lVectPrec.get(0);
			String lDescrTipo = lAvvModPrec.getDescrTipo();
			if ((lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02"))
					|| (lDescrTipo.equalsIgnoreCase("Della Fase di Giudizio")
							&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("03"))) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi di Fiducia o entrambi della Fase di Giudizio!");
			}

			if (idAvvocato != null && lAvvModPrec.getIdAvvocato()
					.compareTo(/* getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO) */idAvvocato) == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta già inserito!");
		}

		// 20210620 Costruzione AvvocatoFascicoloSiep
		// puntando all'Avvocato appena inserito/individuato con ID = idAvvocato.
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();

		// 20210705 In caso di inserimento Avvocato non certificato (idAvvocato=null) non si effettua la
		// ricerca.
		if (idAvvocato != null) {
			lAvvMod.setIdAvvocato(idAvvocato);

			lVectRic = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvMod);
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
					getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_DESIGNAZIONE,
							ICostantiAvvocato.CAMPO_MESE_DATA_DESIGNAZIONE,
							ICostantiAvvocato.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("02")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocato.CAMPO_ANNO_DATA_NOMINA, ICostantiAvvocato.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocato.CAMPO_GIORNO_DATA_NOMINA));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("03")) {
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());
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

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF) && (!this
				.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF)
				&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(
					ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvFascMod.setSedeAutoritaDif("-");
		}

		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAvvFascMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		lAvvFascMod.setAvvIdAvvocato(/* getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO) */idAvvocato);
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_NOTE))
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocato.CAMPO_NOTE));

		// 20210629 Inserimento di un nuovo avvocato per diverso Ufficio appartenenza
		// solo se l'avvocato non è cert. Reginde.
		if (!flagReginde && !("SI".equals(lAvvModRic.getFlagRegInde().trim()))) {
			if (!lAvvModRic.getCodUffAppartenenza().equals(getCodUfficioUtenteConnesso())) {
				// Selezionato Avvocato Standard: inserisco un nuovo Avvocato in copia
				// associandolo all'uffcio
				// MEV29 07/2015: Aggiunto aggiornamento foro
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
				lAvvModRic.setDescLuogoNascitaReginde(
						getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE));

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

				lAvvMod = lCtrl.ExInserisciAvvocato(lAvvModRic);
			} else {
				// L'avvocato selezionato è quello dell'ufficio: storicizzo e aggiorno
				StoricoAvvocatoModel lStoricoModel = new StoricoAvvocatoModel();

				lStoricoModel.setCognome(lAvvModRic.getCognome());
				lStoricoModel.setNome(lAvvModRic.getNome());
				lStoricoModel.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());
				lStoricoModel.setCodLuogoNascita(lAvvModRic.getCodLuogoNascita());
				lStoricoModel.setDataNascita(lAvvModRic.getDataNascita());
				lStoricoModel.setForo(lAvvModRic.getForo());
				lStoricoModel.setIndirizzo(lAvvModRic.getIndirizzo());
				lStoricoModel.setCodComuneResidenza(lAvvModRic.getCodComuneResidenza());
				lStoricoModel.setTelefono(lAvvModRic.getTelefono());
				lStoricoModel.setFax(lAvvModRic.getFax());
				lStoricoModel.setEMail(lAvvModRic.getEMail());
				lStoricoModel.setCodiceFiscale(lAvvModRic.getCodiceFiscale());
				lStoricoModel.setProvincia(lAvvModRic.getProvincia());
				lStoricoModel.setCap(lAvvModRic.getCap());
				lStoricoModel.setDataSospesoFinoAl(lAvvModRic.getDataSospensione());
				lStoricoModel.setDataRadiatoDal(lAvvModRic.getDataRadiazione());
				lStoricoModel.setCodNonAttivita(lAvvModRic.getCodNonAttivita());
				lStoricoModel.setCodUfficioAppartenenza(lAvvModRic.getCodUffAppartenenza());
				lStoricoModel.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());
				lStoricoModel.setCodUfficioInserimento(lAvvModRic.getCodUfficioAggiornamento());
				lStoricoModel.setCodOperatoreInserimento(lAvvModRic.getCodOperatoreAggiornamento());
				lStoricoModel.setDataInserimento(lAvvModRic.getDataAggiornamento());
				// MEV 29 - 07/2015 - FIX non aggiornava il foro
				lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
				lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
				lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
				lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
				lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL));
				// MEV_21: valorizzo nuovi campi da REGINDE ed annullo "setCodComuneResidenza"
				// per il luogo residenza sarà aggiunta una nuova colonna che conterrà la descrizione del
				// Comune
				// sede dello studio come presente in ReGIndE, abbandonando la valorizzazione della colonna
				// "COD_COMUNE_RESIDENZA", che resterà per i dati pregressi
				// ComuneModel lComModRes = new ComuneModel(getCodComuneByDescr(
				// getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
				// lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());
				if (flagReginde)
					AvvocatoUtil.valorizzaDatiReginde(lAvvModRic, getRequestStringParameter(CAMPO_PEC),
							getRequestStringParameter(CAMPO_COD_STATO_NASCITA),
							getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE),
							getRequestStringParameter(CAMPO_DESC_COMUNE_STUDIO));

				lAvvMod = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
			}
		}

		BigDecimal idAvvFascicoloSiep = null;
		String lFlagValidato = "";
		if (((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato() != null)
			lFlagValidato = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato();

		if ("01".equals(lAvvFascMod.getCodTipoAvvocato()) && (lFlagValidato.equals("S"))) {
			EventoNotificaModel lEve = CreoProvvedimento(lAvvFascMod);
			idAvvFascicoloSiep = lCtrl.ExInserisciAvvocato(lEve, lAvvMod, lAvvFascMod);
			setRequestAttribute("lEve", lEve);
		} else
			idAvvFascicoloSiep = lCtrl.ExInserisciAvvocato(lAvvMod, lAvvFascMod);

		// valore di ritorno
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.avvocato.action.ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep&idAvvFascicoloSiep="
				+ idAvvFascicoloSiep;
	}

}