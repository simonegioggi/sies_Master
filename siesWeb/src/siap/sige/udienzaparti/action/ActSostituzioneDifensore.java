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
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.util.AvvocatoUtil;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.udienzaparti.model.AvvocatoParteModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;

/**
 * <p>
 * Title: ActSostituzioneDifensore
 * </p>
 * <p>
 * Description: Classe Action per la Sostituzione di un Difensore
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class ActSostituzioneDifensore extends ActionSiap implements ICostantiPartiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String idSoggetto = "";
		if (!isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO)) {
			idSoggetto = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		}

		// Identificativo evento udienza
		String lIdEventoUdienza = getRequestStringParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV);

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector lVectPrec = null;
		// Vector lVectRic = null;
		// BigDecimal id = getRequestBigDecimalParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
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
		// 20210722 MEV_21 Controllo e valorizzazione comuneNascita.
		// Se presente, dal codice comune (e dalla descrizione).
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			comuneNascita = new ComuneModel(
					getDatiComuneByCodDescr(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
							getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA)));
			codLuogoNascita = comuneNascita.getCodComune();
			codProvincia = comuneNascita.getCodProvincia();
			codCap = comuneNascita.getCap();
			descCodLuogoNascita = comuneNascita.getDescrizione();

			// altrimenti dalla sola descrizione (rischio omonimi).
		} else if (getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA).length() > 2) {
			comuneNascita = new ComuneModel(getDatiComuneByDescrOmonimia(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA)));
			descCodLuogoNascita = comuneNascita.getDescrizione();
			codLuogoNascita = comuneNascita.getCodComune();
			// altrimenti , in caso di Paese di Nascita Estero, dalla routine che ricava i dati dal C.F.
		} else if (getRequestStringParameter(CAMPO_COD_STATO_NASCITA).length() == 3
				&& !("039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA)))
				&& getRequestStringParameter(ICostantiAvvocato.CAMPO_CODICE_FISCALE).length() == 16) {
			comuneNascita = AvvocatoUtil
					.calcolaComuneNascita(getRequestStringParameter(ICostantiAvvocato.CAMPO_CODICE_FISCALE));
			// comuneNascita, in caso di stato estero, conterrà informazioni dello stato.
		}

		if (getRequestStringParameter(ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE).length() > 0)
			descLuogoNascitaReginde = getRequestStringParameter(
					ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE);

		// Recupero Codice e descrizione comune di residenza.
		String codLuogoResidenza = "-";
		ComuneModel comuneResidenza = null;
		String descLuogoResidenza = !isRequestParameterNullObj(ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO)
				? getRequestStringParameter(ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO)
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
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA))
			codNonAttivita = getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA);
		// 20210726 Recupero tipoInserimento (reginde, sies, manuale)
		String tipoInserimento = getRequestStringParameter("lTipoInserimento");

		if (!"manuale".equals(tipoInserimento)) {
			if (getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO).contains("COA"))
				flagReginde = true;
			// Provengo da Reginde o da SIES: quindi si cerca l'avvocato certificato su tabella AVVOCATO;
			// se esiste lo aggiorno, altrimenti inserisco nuovo avvocato da Reginde su SIES!

			// 20210614 MEV_21 Si esegue la ricerca puntuale dell'Avvocato certificato RegInde in SIES.
			AvvocatoModel lAvvCertRegSies = new AvvocatoModel();
			lAvvCertRegSies.setNome(getRequestStringParameter(CAMPO_NOME));
			lAvvCertRegSies.setCognome(getRequestStringParameter(CAMPO_COGNOME));
			lAvvCertRegSies
					.setCodiceFiscale(getRequestStringParameter(ICostantiAvvocato.CAMPO_CODICE_FISCALE));
			lAvvCertRegSies.setFlagRegInde("SI");
			lAvvCertRegSies = lCtrl.ExRicercaAvvocatoCertRegInde(lAvvCertRegSies);

			amReginde = new AvvocatoModel(null, getRequestStringParameter(CAMPO_COGNOME),
					getRequestStringParameter(CAMPO_NOME),
					getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO).toUpperCase(), null,
					getRequestStringParameter(CAMPO_INDIRIZZO),
					getRequestStringParameter(ICostantiAvvocato.CAMPO_TELEFONO),
					getRequestStringParameter(ICostantiAvvocato.CAMPO_FAX),
					getRequestStringParameter(ICostantiAvvocato.CAMPO_E_MAIL), codLuogoNascita,
					codLuogoResidenza, descCodLuogoNascita, descLuogoResidenza, dataNascita,
					getCodUtenteConnesso(), DateUtils.getSysDate(), null, null, null,
					getCodUfficioUtenteConnesso(), null, null, null, codNonAttivita, "00000", null, "N", null,
					getRequestStringParameter(ICostantiAvvocato.CAMPO_CODICE_FISCALE), codProvincia, codCap,
					new BigDecimal(1), null, getRequestStringParameter(ICostantiAvvocato.CAMPO_PEC), "SI",
					descLuogoResidenza, getRequestStringParameter(CAMPO_COD_STATO_NASCITA), null,
					descLuogoNascitaReginde, null);

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
			idAvvocato = getRequestBigDecimalParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		}

		AvvocatoModel lAvvModRic = new AvvocatoModel();
		// lAvvModRic.setIdAvvocato(id);
		BigDecimal id_avv_old = getRequestBigDecimalParameter("idAvvVecchio");
		// lVectRic = lCtrl.ExRierccaAvvocato(lAvvModRic);
		// lAvvModRic = (AvvocatoModel) lVectRic.get(0);
		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoParteModel lAvvParteMod = new AvvocatoParteModel();
		PartiUdienzaDifensoreModel lAvvParteModPrec = new PartiUdienzaDifensoreModel();
		lAvvParteModPrec.setSoggIdSoggetto(new BigDecimal(idSoggetto));

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaDifensoreAttualiParteUdienza(lAvvModPrec, lAvvParteModPrec);
		} catch (Exception e) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore nella verifica degli avvocati già a sistema: " + e.getMessage());
		}

		if (lVectPrec.size() > 1) {
			lAvvParteMod = (AvvocatoParteModel) lVectPrec.get(0);
			String lDescrTipo = lAvvParteMod.getAvvocato().getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}
		} else {
			lAvvParteMod = (AvvocatoParteModel) lVectPrec.get(0);
		}

		PartiUdienzaDifensoreModel lDifParteMod = new PartiUdienzaDifensoreModel();
		lDifParteMod.setAvvIdAvvocato(idAvvocato);

		lDifParteMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lDifParteMod.getCodTipoAvvocato().equals("01")) {
			lDifParteMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE,
							ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE,
							ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		}
		if (lDifParteMod.getCodTipoAvvocato().equals("02")) {
			lDifParteMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA,
							ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA,
							ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA));
		}
		if (lDifParteMod.getCodTipoAvvocato().equals("03")) {
			lDifParteMod.setDataInizioValidita(DateUtils.getSysDate());
		}
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lDifParteMod.setCodMotivoDesignazione(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lDifParteMod.setCodMotivoDesignazione("-");
		}
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA))
			lDifParteMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA));
		else
			lDifParteMod.setCodTipoAutorita("-");

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)
				&& (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA)
						&& !getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA)
								.equals("-"))) {
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)));
			lDifParteMod.setSedeTipoAutorita(lComMod.getCodComune());
		} else {
			lDifParteMod.setSedeTipoAutorita("-");
		}

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lDifParteMod.setIndirizzoTipoAutorita(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDifParteMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF))
			lDifParteMod.setCodTipoAutoritaDif(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lDifParteMod.setCodTipoAutoritaDif("-");

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF)
						&& !getRequestStringParameter(
								ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lDifParteMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lDifParteMod.setSedeAutoritaDif("-");
		}

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE))
			lDifParteMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));

		lDifParteMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lDifParteMod.setDataInserimento(DateUtils.getSysDate());
		lDifParteMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// ==========================================================================
		// Effettuo la sostituzione che consiste nel valorizzare la data fine
		// validità del vecchio avvocato e nell'inserimento dei dati del nuovo avvocato
		// Queste operazioni vengono effettuate contestualmente per motivi di
		// transazione
		// ==========================================================================
		AvvocatoSigeModel avv_sige_old = lCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSige(id_avv_old);
		AvvocatoFascicoloSigeModel lAvvFascUp = avv_sige_old.getAvvocatoFascicoloSigeModel();
		lAvvFascUp.setDataFineValidita(DateUtils.getSysDate());

		lAvvFascUp.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvFascUp.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAvvFascUp.setDataAggiornamento(DateUtils.getSysDate());

		// 20210726 Inserimento di un nuovo avvocato solo se l'avvocato non è cert. Reginde
		// e se non è presente in SIES.
		if (!flagReginde && idAvvocato == null) {
			lAvvModRic.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
			lAvvModRic.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
			lAvvModRic.setForo(getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO).toUpperCase());
			lAvvModRic
					.setIndirizzo(getRequestStringParameter(ICostantiAvvocato.CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(ICostantiAvvocato.CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(ICostantiAvvocato.CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(ICostantiAvvocato.CAMPO_E_MAIL));
			lAvvModRic.setPec(getRequestStringParameter(ICostantiAvvocato.CAMPO_PEC));
			lAvvModRic.setFlagCancellato("N");
			lAvvModRic.setCodiceFiscale(getRequestStringParameter(ICostantiAvvocato.CAMPO_CODICE_FISCALE));
			lAvvModRic.setDescLuogoNasRegInde(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE));

			lAvvModRic.setCodLuogoNascita(codLuogoNascita);
			lAvvModRic.setDescLuogoNascita(descCodLuogoNascita);
			lAvvModRic.setProvincia(codProvincia);
			lAvvModRic.setCap(codCap);

			lAvvModRic.setCodComuneResidenza(codLuogoResidenza);
			lAvvModRic.setDescrComuneStudio(descLuogoResidenza);

			lAvvModRic.setCodUffAppartenenza(getCodUfficioUtenteConnesso());
			// ComuneModel lComModRes = new ComuneModel(getCodComuneByDescr(
			// getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			// lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());
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
			if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA))
				codNonAttivita = getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA);

			lAvvModRic.setDataNascita(dataNascita);
			lAvvModRic.setCodNonAttivita(codNonAttivita);
			lAvvModRic.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));

			lAvvModRic = lCtrl.ExInserisciAvvocato(lAvvModRic);

			idAvvocato = lAvvModRic.getIdAvvocato(); // Valorizzazione idAvvocato (precedentemente = null)
		}

		// Devo utilizzare IdAvvocato per referenziare l'avvocato prelevato da RegInde, da SIES, o appena
		// inserito.
		lDifParteMod.setAvvIdAvvocato(idAvvocato);
		// lAvvFascMod.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());

		// update
		PartiUdienzaDifensoreModel lParteUdienzaUp = new PartiUdienzaDifensoreModel();
		lParteUdienzaUp.setAvvIdAvvocato(id_avv_old);
		lParteUdienzaUp.setSoggIdSoggetto(new BigDecimal(idSoggetto));
		lParteUdienzaUp.setDataFineValidita(DateUtils.getSysDate());
		lParteUdienzaUp.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lParteUdienzaUp.setDataAggiornamento(DateUtils.getSysDate());
		PartiUdienzaDifensoreModel lAvvMod = new PartiUdienzaDifensoreModel();
		lAvvMod = lCtrl.ExSostituzioneAvvocato(lParteUdienzaUp, lDifParteMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.udienzaparti.action.ActLoadDettaglioAvvocatoParte&"
				+ ICostantiAvvocato.CAMPO_ID_AVVOCATO + "=" + lAvvMod.getAvvIdAvvocato() + "&"
				+ ICostantiPartiUdienza.CAMPO_ID_AVVOCATO_PARTE_UDIENZA + "="
				+ lAvvParteMod.getAvvocatoParteUdienzaModel().getIdAvvocatoParteUdienza() + "&"
				+ ICostantiPartiUdienza.CAMPO_ID_SOGGETTO + "=" + idSoggetto + "&"
				// MERGE v10 COLLAUDO: aggiunto parametro di passaggio in query string
				+ ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA + "=" + lIdEventoUdienza;

		return lPage;
	}

}