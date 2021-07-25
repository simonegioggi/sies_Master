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
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.util.AvvocatoUtil;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
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

		// Passa la action di destinazione
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();

		// MEV_21: controllo se la ricerca proviene da Reginde, da SIES o si fa un nuovo inserimento.
		BigDecimal idAvvocato = null;
		boolean flagReginde = false;
		if (getRequestStringParameter(CAMPO_ID_AVVOCATO).contains("COA")) {
			flagReginde = true;
			// Provengo da Reginde: quindi si cerca l'avvocato certificato su tabella AVVOCATO;
			// se esiste lo aggiorno, altrimenti inserisco nuovo avvocato da Reginde su SIES!

			// 20210614 MEV_21 Si esegue la ricerca puntuale dell'Avvocato certificato RegInde in SIES.
			AvvocatoModel lAvvCertRegSies = new AvvocatoModel();
			lAvvCertRegSies.setNome(getRequestStringParameter(CAMPO_NOME));
			lAvvCertRegSies.setCognome(getRequestStringParameter(CAMPO_COGNOME));
			lAvvCertRegSies.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE));
			lAvvCertRegSies.setFlagRegInde("SI");
			lAvvCertRegSies = lCtrl.ExRicercaAvvocatoCertRegInde(lAvvCertRegSies);

			// 20210623 MEV_21 Si recuperano tutte le informazioni dalla Form.
			// Recupero Codice e descrizione comune di nascita.
			AvvocatoModel amReginde = null;
			Date dataNascita = null;
			String codLuogoNascita = "-";
			String codProvincia = "-";
			String codCap = null;
			String codNonAttivita = "-";
			ComuneModel comuneNascita = null;
			String descCodLuogoNascita = "039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA))
					? getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)
					: getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE);
			if (Utils.isPresent(descCodLuogoNascita)) {
				try {
					comuneNascita = new ComuneModel(getCodComuneByDescr(descCodLuogoNascita));
				} catch (Exception e) {
					siesLogger.info(e.getMessage());
					// algortimo di omocodia
					comuneNascita = AvvocatoUtil
							.calcolaComuneNascita(getRequestStringParameter(CAMPO_CODICE_FISCALE));
				}
				if (!Utils.isNullObj(comuneNascita)) {
					if ("039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA))) {
						codLuogoNascita = comuneNascita.getCodComune();
						codProvincia = comuneNascita.getCodProvincia();
						codCap = comuneNascita.getCap();
						descCodLuogoNascita = comuneNascita.getDescrizione();
					}
				}
			}

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
				}
				if (!Utils.isNullObj(comuneResidenza)) {
					codLuogoResidenza = comuneResidenza.getCodComune();
					descLuogoResidenza = comuneResidenza.getDescrizione();
				}
			}

			// 20210622 Recupero dataNascita, stato Attività Avvocato.
			if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
					&& (!isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA)
							&& (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_NASCITA))))
				dataNascita = getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA);
			if (!isRequestParameterNullObj(CAMPO_COD_NON_ATTIVITA))
				codNonAttivita = getRequestStringParameter(CAMPO_COD_NON_ATTIVITA);

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
					getRequestStringParameter(CAMPO_COD_STATO_NASCITA), null, descCodLuogoNascita, null);

			/*
			 * 20210623 MEV_21 Se l'avvocato certificato RegInde non è presente in SIES si inserisce Se è già
			 * presente in SIES si effettua l'aggiornamento con i dati da Reginde.
			 */
			if (lAvvCertRegSies == null) {
				amReginde = lCtrl.ExInserisciAvvocato(amReginde);
			} else {
				// 20210720 MEV_21 Se l'avvocato certificato ha cambiato Foro, si storicizza
				// l'avvocato legato al vecchio Foro (con FLAG_REGINDE="NO") e si inserisce un nuovo Avvocato.
				// Se rimane nel foro si aggiornano solo i dati da REGINDE.
				if (lAvvCertRegSies.getForo().trim() == amReginde.getForo().trim()) {
					amReginde.setIdAvvocato(lAvvCertRegSies.getIdAvvocato());
					amReginde.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					amReginde.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					amReginde.setDataAggiornamento(DateUtils.getSysDate());
					amReginde = lCtrl.ExAggiornaAvvocatoDaReginde(amReginde);
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
			// 20210720 MEV_21 Avvocato non presente sia in RegInde che in SIES.
			// Controlli Comuni di Nascita e Residenza
			try {
				if (!"".equals(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA).trim())) {
					getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA));
				}
				if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_STUDIO)) {
					getCodComuneByDescr(
							getRequestStringParameter(ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO));
				}
			} catch (Exception ex) {
				throw new F3BException(ex);
			}

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
			siesLogger.debug(getClass().getName() + " # " + e.getMessage());
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
			/*
			 * if(lAvvModRic.getDataSospensione()!= null && !lAvvModRic.getDataSospensione().equals("-"))
			 * throw new F3BException(F3BException.USER_MESSAGE,"Attenzione: il difensore risulta sospeso!");
			 *
			 * if(lAvvModRic.getDataRadiazione()!= null && !lAvvModRic.getDataRadiazione().equals("-")) throw
			 * new F3BException(F3BException.USER_MESSAGE,"Attenzione: il difensore risulta radiato!");
			 *
			 * if(lAvvModRic.getCodNonAttivita()!= null && !lAvvModRic.getCodNonAttivita().equals("-") &&
			 * !lAvvModRic.getCodNonAttivita().equals("A")) throw new F3BException
			 * (F3BException.USER_MESSAGE,"Attenzione: il difensore risulta non in attività per  "+lAvvModRic
			 * .getDescrNonAttivita()+"");
			 */
		}

		// da qui sul secondo model
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

		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF) && (!this
				.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF)
				&& !getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF)
						.equals("-"))) {
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
		lAvvFascMod.setAvvIdAvvocato(/*getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO)*/idAvvocato);
		if (!isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE)) {
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));
		}

		// 20210629 Inserimento di un nuovo avvocato per diverso Ufficio appartenenza
		// solo se l'avvocato non è cert. Reginde.
		if (!flagReginde && !("SI".equals(lAvvModRic.getFlagRegInde().trim()))) {
			if (!lAvvModRic.getCodUffAppartenenza().equals(getCodUfficioUtenteConnesso())) {
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

				// Recupero Codice e descrizione comune di nascita.
				Date dataNascita = null;
				String codLuogoNascita = "-";
				String codProvincia = "-";
				String codCap = null;
				String codNonAttivita = "-";
				ComuneModel comuneNascita = null;
				String descCodLuogoNascita = "039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA))
						? getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)
						: getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE);
				if (Utils.isPresent(descCodLuogoNascita)) {
					try {
						comuneNascita = new ComuneModel(getCodComuneByDescr(descCodLuogoNascita));
					} catch (Exception e) {
						siesLogger.info(e.getMessage());
						// algortimo di omocodia
						comuneNascita = AvvocatoUtil
								.calcolaComuneNascita(getRequestStringParameter(CAMPO_CODICE_FISCALE));
					}
					if (!Utils.isNullObj(comuneNascita)) {
						if ("039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA))) {
							codLuogoNascita = comuneNascita.getCodComune();
							codProvincia = comuneNascita.getCodProvincia();
							codCap = comuneNascita.getCap();
							descCodLuogoNascita = comuneNascita.getDescrizione();
						}
					}
				}
				lAvvModRic.setCodLuogoNascita(codLuogoNascita);
				lAvvModRic.setDescLuogoNascita(descCodLuogoNascita);
				lAvvModRic.setProvincia(codProvincia);
				lAvvModRic.setCap(codCap);

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
					}
					if (!Utils.isNullObj(comuneResidenza)) {
						codLuogoResidenza = comuneResidenza.getCodComune();
						descLuogoResidenza = comuneResidenza.getDescrizione();
					}
				}
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
				if (flagReginde) {
					lAvvModRic.setCodComuneResidenza(null);
					lAvvModRic.setPec(getRequestStringParameter(CAMPO_PEC));
					lAvvModRic.setFlagRegInde("SI");
					lAvvModRic.setDescrComuneStudio(getRequestStringParameter(CAMPO_DESC_COMUNE_STUDIO));
					lAvvModRic.setDescLuogoNasRegInde(
							getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE));
					lAvvModRic.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
				}

				lAvvModRic.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lAvvModRic.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lAvvModRic.setDataAggiornamento(DateUtils.getSysDate());

				lAvvMod = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
			}
		}

		BigDecimal idAvv = null;
		lCtrl.ExInserisciAvvocato(lAvvMod, lAvvFascMod);
		idAvv = lAvvFascMod.getAvvIdAvvocato();

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ idAvv;

		return lPage;
	}

}