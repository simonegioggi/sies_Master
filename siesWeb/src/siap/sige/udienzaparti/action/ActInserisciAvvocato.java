package siap.sige.udienzaparti.action;

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
import siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.udienzaparti.model.AvvocatoParteModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;

/**
 * ActInserisciAvvocato - Classe Action per l'inserimento di Avvocato
 *
 * @version 1.0
 */
public class ActInserisciAvvocato extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento dell' Avvocato
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// paramentro passato solo nel caso di iscrizione guidata
		// 20210608 e successivamente anche per la MEV Scheda-21
		if (!isRequestParameterNullObj("lTipoFunzione"))
			setRequestAttribute("lTipoFunzione", getRequestStringParameter("lTipoFunzione"));

		String idSoggetto = "";
		String idEventoUdienza = "";
		if (!isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO)) {
			idSoggetto = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		}
		setRequestAttribute("idSoggetto", "" + idSoggetto);

		if (!isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA)) {
			idEventoUdienza = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA);
		}
		setRequestAttribute("idEventoUdienza", "" + idEventoUdienza);

		// Passa la action di destinazione
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

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaAvvocatiByParteUdienza(new BigDecimal(idSoggetto));
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" nessun elemento trovato");
		}

		if (lVectPrec.size() > 0) {
			lAvvModPrec = ((PartiUdienzaDifensoreModel) lVectPrec.get(0)).getAvvocato();
			String lDescrTipo = lAvvModPrec.getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}

			// MEV_21 - Se selezionato da REGINDE l'idAvvocato è una stringa es COA058091 e non 
			//          può essere utilizzata per la varifica
			/*if (lAvvModPrec.getIdAvvocato()
					.compareTo(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO)) == 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta già inserito!");
			}*/
			if (idAvvocato!=null && lAvvModPrec.getIdAvvocato().compareTo(idAvvocato) == 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta già inserito!");
			}
		  // MEV_21 - FINE
			
		}

		PartiUdienzaDifensoreModel lAvvParteMod = new PartiUdienzaDifensoreModel();
		// 20210705 In caso di inserimento Avvocato non certificato (idAvvocato=null) non si effettua la
		// ricerca.
		if (idAvvocato != null) {
			// lAvvMod.setIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));
			lAvvMod.setIdAvvocato(idAvvocato);
			lVectRic = lCtrl.ExRicercaAvvocato(lAvvMod);
			lAvvModRic = (AvvocatoModel) lVectRic.get(0);
		}

		lAvvParteMod.setSoggIdSoggetto(new BigDecimal(idSoggetto));
		// lAvvParteMod.setAvvIdAvvocato(/*getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO)*/idAvvocato);
		// da qui sul secondo model
		lAvvParteMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvParteMod.getCodTipoAvvocato().equals("01")) {
			lAvvParteMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE,
							ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE,
							ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE));

		}
		if (lAvvParteMod.getCodTipoAvvocato().equals("02")) {
			lAvvParteMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA,
							ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA,
							ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA));

		}
		if (lAvvParteMod.getCodTipoAvvocato().equals("03")) {
			lAvvParteMod.setDataInizioValidita(DateUtils.getSysDate());

		}
		lAvvParteMod.setDataFineValidita(null);
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvParteMod.setCodMotivoDesignazione(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvParteMod.setCodMotivoDesignazione("-");
		}
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA))
			lAvvParteMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvParteMod.setCodTipoAutorita("-");

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)
				&& (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA)
						&& !getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA)
								.equals("-"))) {
			// String lComuneAutorita =
			// getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)));
			lAvvParteMod.setSedeTipoAutorita(lComMod.getCodComune());
		} else {

			lAvvParteMod.setSedeTipoAutorita("-");
		}

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvParteMod.setIndirizzoTipoAutorita(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvParteMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvParteMod.setCodTipoAutoritaDif(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvParteMod.setCodTipoAutoritaDif("-");

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF) && (!this
				.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF)
				&& !getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF)
						.equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));
			lAvvParteMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvParteMod.setSedeAutoritaDif("-");
		}

		lAvvParteMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvParteMod.setDataInserimento(DateUtils.getSysDate());
		lAvvParteMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAvvParteMod.setSoggIdSoggetto(new BigDecimal(idSoggetto));
		lAvvParteMod.setAvvIdAvvocato(/* getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO) */idAvvocato);
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE))
			lAvvParteMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));

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

		AvvocatoParteModel avvParteMod = new AvvocatoParteModel();
		BigDecimal idAvvParteUdienza = null;
		lAvvMod.setIdAvvocato(idAvvocato);
		avvParteMod = lCtrl.ExInserisciAvvocato(lAvvMod, lAvvParteMod);

		// chiave tabella PARTI_UDIENZA_DIFENSORE
		idAvvParteUdienza = avvParteMod.getAvvocatoParteUdienzaModel().getIdAvvocatoParteUdienza();

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.udienzaparti.action.ActLoadDettaglioAvvocatoParte&" + CAMPO_ID_AVVOCATO + "="
				+ idAvvocato + "&" + ICostantiPartiUdienza.CAMPO_ID_AVVOCATO_PARTE_UDIENZA + "="
				+ idAvvParteUdienza + "&" + ICostantiPartiUdienza.CAMPO_ID_SOGGETTO + "=" + idSoggetto + "&"
				+ ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA + "=" + idEventoUdienza;

		return lPage;
	}

}